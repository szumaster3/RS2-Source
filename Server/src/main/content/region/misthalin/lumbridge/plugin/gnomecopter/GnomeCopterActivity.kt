package content.region.misthalin.lumbridge.plugin.gnomecopter

import content.region.misthalin.lumbridge.plugin.gnomecopter.data.IntroductionPageManager

import core.api.*
import core.game.activity.ActivityPlugin
import core.game.container.impl.EquipmentContainer
import core.game.interaction.Option
import core.game.node.Node
import core.game.node.entity.Entity
import core.game.node.entity.impl.ForceMovement
import core.game.node.entity.player.Player
import core.game.node.item.Item
import core.game.node.scenery.Scenery
import core.game.node.scenery.SceneryBuilder
import core.game.system.task.Pulse
import core.game.world.GameWorld.Pulser
import core.game.world.map.Direction
import core.game.world.map.Location
import core.game.world.map.zone.ZoneBorders
import core.game.world.update.flag.context.Animation
import core.game.world.update.flag.context.Graphics
import core.plugin.Initializable
import shared.consts.Items
import shared.consts.Music

@Initializable
class GnomeCopterActivity : ActivityPlugin("Gnome copters", false, false, true) {
    private val usedLandingPads = BooleanArray(4)

    override fun newInstance(p: Player?): ActivityPlugin = this

    override fun interact(e: Entity, target: Node, option: Option): Boolean {
        if (target is Scenery) {
            val obj = target

            if (obj.id == 30032) {
                flyGnomeCopter(e as Player, obj)
                return true
            }
        } else if (target is Item && e.getAttribute("gc:flying", false)) {
            (e as Player).packetDispatch.sendMessage("You can't do this right now.")
            return true
        }
        return false
    }

    override fun leave(e: Entity, logout: Boolean): Boolean {
        if (logout && e.getAttribute("gc:flying", false)) {
            e.location = spawnLocation
            (e as Player).equipment.remove(COPTER_ITEM)
        }
        return super.leave(e, logout)
    }

    private fun flyGnomeCopter(player: Player, scenery: Scenery) {
        if (player.location != scenery.location.transform(0, 1, 0)) return

        if (scenery.charge == 88) {
            sendMessage(player, "That gnomecopter is occupied at the moment.")
            return
        }

        if (player.equipment[EquipmentContainer.SLOT_HAT] != null ||
            player.equipment[EquipmentContainer.SLOT_CAPE] != null
        ) {
            sendMessage(player, "You can't wear that on a Gnomecopter.")
            return
        }

        if (player.equipment[3] != null || player.equipment[5] != null) {
            sendMessage(player, "You need to have your hands free to use this.")
            return
        }

        if (!inInventory(player, Items.GNOMECOPTER_TICKET_12843)) {
            sendMessage(player, "You need to have gnomecopter ticket to use this.")
            return
        }

        val destination = GnomeCopterDestination.default()
        setAttribute(player, "gc:route", destination)
        setAttribute(player, "gc:flying", true)
        setAttribute(player, "gc:page", 0)


        player.lock()
        removeItem(player, Items.GNOMECOPTER_TICKET_12843)

        sendMessage(player, "The gnomecopter accepts the ticket and sets off for ${destination.name}.")

        IntroductionPageManager.sendPage(player, destination.tab, 0)

        player.faceLocation(player.location.transform(0, 3, 0))
        scenery.charge = 88

        if (player.settings.isRunToggled) {
            player.settings.toggleRun()
        }

        Pulser.submit(object : Pulse(1, player) {
            var stage = 0

            override fun pulse(): Boolean {
                if (++stage == 1) {
                    player.interfaceManager.removeTabs(0,1,2,3,4,5,6,7,11)

                    ForceMovement.run(
                        player,
                        player.location,
                        scenery.location,
                        ForceMovement.WALK_ANIMATION,
                        Animation(8955),
                        Direction.NORTH,
                        8
                    )

                } else if (stage == 3) {
                    player.equipment.replace(COPTER_ITEM, 3)
                    player.animate(Animation.create(8956))
                    sendGraphics(player, 1578, 100)
                    player.appearance.standAnimation = 8964
                    player.appearance.walkAnimation = 8961
                    player.appearance.runAnimation = 8963
                    player.appearance.turn180 = 8963
                    player.appearance.turn90ccw = 8963
                    player.appearance.turn90cw = 8963
                    player.appearance.standTurnAnimation = 8963

                //}// else if (stage == 4) {
                 //   scenery.charge = 88
                 //   player.packetDispatch.sendSceneryAnimation(scenery, Animation(5))
//
                } else if (stage == 16) {
                    player.walkingQueue.reset()
                    player.walkingQueue.addPath(scenery.location.x, scenery.location.y + 16, true)
                    Graphics.send(Graphics.create(1579), scenery.location)

                } else if (stage == 20) {
                    scenery.charge = 1000
                    player.packetDispatch.sendSceneryAnimation(scenery, Animation(8941))

                } else if (stage == 33) {
                    player.unlock()
                    landGnomeCopter(player)
                    return true
                }
                return false
            }
        })
    }

    private fun landGnomeCopter(player: Player) {
        val destination = player.getAttribute(
            "gc:route",
            GnomeCopterDestination.default()
        )

        var index = 0
        while (index < usedLandingPads.size) {
            if (!usedLandingPads[index]) break
            index++
        }

        usedLandingPads[index] = true
        player.lock()

        val pad = index
        player.direction = Direction.SOUTH

        player.properties.teleportLocation = destination.startLocation

        Pulser.submit(object : Pulse(1, player) {
            var stage = 0
            var tick = 0

            override fun pulse(): Boolean {
                if (++stage == 1) {
                    player.walkingQueue.reset()

                    if (destination.autoPilot.isNotEmpty()) {
                        for (step in destination.autoPilot) {
                            player.walkingQueue.addPath(step.x, step.y, true)
                        }
                    } else {
                        val base = destination.startLocation
                        player.walkingQueue.addPath(base.x, base.y - 4, true)
                        player.walkingQueue.addPath(base.x - (pad shl 1), base.y - 16, true)
                    }

                    tick = stage + player.walkingQueue.queue.size

                } else if (stage == tick) {
                    player.animate(Animation.create(8957))

                } else if (stage == tick + 14) {
                    SceneryBuilder.add(Scenery(30034, player.location), 6)

                    player.equipment.replace(null, 3)

                    ForceMovement.run(
                        player,
                        player.location,
                        player.location.transform(0, -1, 0),
                        Animation(8959),
                        8
                    )

                    player.lock(2)

                } else if (stage == tick + 15) {
                    player.unlock()
                    player.interfaceManager.restoreTabs()
                    player.interfaceManager.openDefaultTabs()

                    usedLandingPads[pad] = false
                    removeAttribute(player, "gc:flying")
                    removeAttribute(player, "gc:route")
                    sendMessage(player, "Returning to Lumbridge.")
                    return true
                }
                return false
            }
        })
    }

    override fun getSpawnLocation(): Location = Location.create(3161, 3337, 0)

    override fun configure() {
        register(ZoneBorders(3154, 3330, 3171, 3353))
    }

    companion object {
        private val COPTER_ITEM = Item(Items.GNOMECOPTER_12842)

        val SONG_TO_UNLOCK = arrayOf(
            Music.FAR_AWAY_372,     // Lletya
            Music.POLES_APART_548,  // Rellekka
            Music.CASTLE_WARS_314,  // Castle wars
            Music.HELLS_BELLS_4     // Trollweiss
        )

        val RETURN_INTERACTION_MESSAGE = "Please don't disturb the elves."
        val DEATH_INSIDE = "The gnomecopter abadons you to your fate."
    }
}