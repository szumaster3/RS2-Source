package content.global.skill.agility.shortcuts

import content.global.skill.agility.AgilityHandler
import core.api.*
import core.cache.def.impl.SceneryDefinition
import core.game.interaction.NodeUsageEvent
import core.game.interaction.OptionHandler
import core.game.interaction.UseWithHandler
import core.game.node.Node
import core.game.node.entity.player.Player
import core.game.node.entity.skill.Skills
import core.game.node.scenery.SceneryBuilder
import core.game.system.task.Pulse
import core.game.world.GameWorld
import core.game.world.map.Location
import core.game.world.update.flag.context.Animation
import core.plugin.Initializable
import core.plugin.Plugin
import shared.consts.Animations
import shared.consts.Items
import shared.consts.Scenery
import shared.consts.Sounds

/**
 * Handles tree swing shortcut to Grew island.
 */
@Initializable
class TreeSwingShortcut : OptionHandler() {

    override fun newInstance(arg: Any?): Plugin<Any> {

        /*
         * Handles interaction with branch.
         */

        UseWithHandler.addHandler(
            Scenery.BRANCH_2326,
            UseWithHandler.OBJECT_TYPE,
            object : UseWithHandler(Items.ROPE_954) {
                override fun newInstance(arg: Any?): Plugin<Any> = this

                override fun handle(event: NodeUsageEvent): Boolean {
                    val player = event.player
                    val `object` = event.usedWith.asScenery()
                    if (`object`.isActive) SceneryBuilder.replace(`object`, `object`.transform(2325))

                    if (!inInventory(player, event.usedItem.id, 1)) {
                        sendMessage(player, "You need a rope to do that.")
                        return true
                    }

                    if (getStatLevel(player, Skills.AGILITY) < 10) {
                        sendDialogue(player, "You need an Agility level of 10 to negotiate this obstacle.")
                        return true
                    }

                    if (`object`.isActive) {
                        SceneryBuilder.replace(`object`, `object`.transform(Scenery.ROPESWING_2325))
                    }

                    removeItem(player, event.usedItem)
                    playGlobalAudio(player.location, Sounds.TIGHTROPE_2495)
                    animate(player, Animations.SUMMON_ROPE_SWING_775, true)
                    sendMessage(player, "You tie the rope to the tree...")
                    return true
                }
            })

        /*
         * Handles ropeswing interaction.
         */

        SceneryDefinition.forId(Scenery.ROPESWING_2325).handlers["option:swing-on"] = this
        SceneryDefinition.forId(Scenery.ROPESWING_2324).handlers["option:swing-on"] = this
        return this
    }

    override fun handle(player: Player, node: Node, option: String): Boolean {
        if (!player.location.withinDistance(node.location, 2)) {
            sendMessage(player, "You cannot do that from here.")
            return true
        }
        if (AgilityHandler.delay > GameWorld.ticks) {
            sendMessage(player, "The rope is being used.")
            return true
        }

        val experience = if (node.id == Scenery.ROPESWING_2324) 0.0 else 12.5
        val end = if (node.id == 2325) Location(2505, 3087, 0) else Location(2511, 3096, 0)

        faceLocation(player, end)
        playGlobalAudio(player.location, Sounds.SWING_ACROSS_2494, 1)
        animateScenery(player, node.asScenery(), 497, true)
        AgilityHandler.delay = GameWorld.ticks + 2
        player.lock(3)

        GameWorld.Pulser.submit(object : Pulse(1, player) {
            override fun pulse(): Boolean {
                AgilityHandler.forceWalk(
                    player,
                    0,
                    player.location,
                    end,
                    Animation.create(Animations.SWING_ACROSS_OBSTACLE_3130),
                    50,
                    experience,
                    "You skillfully swing across."
                )
                return true
            }
        })

        return true
    }

    override fun getDestination(node: Node, n: Node): Location =
        if (n.id == Scenery.ROPESWING_2324) Location(2511, 3092, 0) else Location(2501, 3087, 0)
}
