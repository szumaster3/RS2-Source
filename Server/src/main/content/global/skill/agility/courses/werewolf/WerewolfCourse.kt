package content.global.skill.agility.courses.werewolf

import content.global.skill.agility.AgilityCourse
import content.global.skill.agility.AgilityHandler
import core.api.*
import core.cache.def.impl.SceneryDefinition
import core.game.dialogue.FaceAnim
import core.game.node.Node
import core.game.node.entity.player.Player
import core.game.node.entity.skill.Skills
import core.game.node.scenery.Scenery
import core.game.system.task.Pulse
import core.game.world.GameWorld
import core.game.world.map.Direction
import core.game.world.map.Location
import core.game.world.update.flag.context.Animation
import core.plugin.Initializable
import shared.consts.Animations
import shared.consts.Items
import shared.consts.NPCs
import shared.consts.Scenery as Objects

@Initializable
class WerewolfCourse : AgilityCourse {

    constructor() : super(null, 5, 0.0)
    constructor(player: Player?) : super(player, 5, 0.0)

    override fun createInstance(player: Player): AgilityCourse = WerewolfCourse(player)

    override fun handle(p: Player, node: Node, option: String): Boolean {
        getCourse(p)
        val scenery = node as? Scenery ?: return false
        return when (scenery.id) {
            Objects.STEPPING_STONE_35996 -> steppingStoneObstacle(p, scenery)
            Objects.HURDLE_5133, Objects.HURDLE_5134, Objects.HURDLE_5135 -> jumpHurdleObstacle(p, scenery)
            Objects.PIPE_5152 -> squeezeThroughPipeObstacle(p, scenery)
            Objects.SKULL_SLOPE_5136 -> climbSkullSlopeObstacle(p, scenery)
            Objects.ZIP_LINE_5139, Objects.ZIP_LINE_5140, Objects.ZIP_LINE_5141 -> zipLineObstacle(p, scenery)
            else -> false
        }
    }

    private fun checkRequirements(p: Player, level: Int = 60): Boolean {
        if (getStatLevel(p, Skills.AGILITY) < level) {
            sendMessage(p, "You need an Agility level of $level to do this.")
            return false
        }
        if (!anyInEquipment(p, Items.RING_OF_CHAROS_4202, Items.RING_OF_CHAROSA_6465)) {
            sendNPCDialogue(p, NPCs.AGILITY_TRAINER_1663, "Grrr - you don't belong in here, human!", FaceAnim.CHILD_NORMAL)
            return false
        }
        return true
    }

    /**
     * Handles steeping stone obstacle.
     */
    private fun steppingStoneObstacle(p: Player, scenery: Scenery): Boolean {
        if (!checkRequirements(p)) return true

        val loc = p.location
        val index = steppingPath.indexOf(p.location)

        if (index == -1 || loc.y >= 9882) {
            sendMessage(p, "You can't do that from here.")
            return true
        }

        if (index == steppingPath.lastIndex) {
            return true
        }

        val next = steppingPath[index + 1]

        if (loc == Location(3538, 9873, 0)) {
            findLocalNPC(p, NPCs.AGILITY_BOSS_1661)?.let { face(it, p, 3) }
            findLocalNPC(p, NPCs.AGILITY_BOSS_1661)?.sendChat("FETCH!!!!!")
            findLocalNPC(p, NPCs.AGILITY_BOSS_1661)?.let { animate(it, Animations.WEREWOLF_FETCH_6536) }
        }
        lock(p, 3)
        GameWorld.Pulser.submit(object : Pulse() {
            override fun pulse(): Boolean {
                AgilityHandler.forceWalk(p, -1, p.location, next, Animation.create(1604), 20, 10.0, null)
                return true
            }
        })
        return true
    }

    /**
     * Handles jump hurdle obstacle
     */
    private fun jumpHurdleObstacle(p: Player, scenery: Scenery): Boolean {
        val loc = p.location
        AgilityHandler.forceWalk(p, -1, loc, loc.transform(Direction.NORTH, 2), Animation.create(1603), 10, 25.0, null)
        runTask(p, 2) { p.faceLocation(loc.transform(Direction.SOUTH)) }
        return true
    }

    /**
     * Handles squeeze through pipe obstacle.
     */
    private fun squeezeThroughPipeObstacle(p: Player, scenery: Scenery): Boolean {
        val loc = p.location
        if (loc.y > 9908) {
            sendMessage(p, "You can't do that from here.")
            return false
        }

        if (loc.x in arrayOf(3538, 3541, 3544)) {
            produceGroundItem(p, Items.STICK_4179, 1, stickRandomLocation.location)
        }
        lock(p, 6)
        GameWorld.Pulser.submit(object : Pulse(1, p) {
            override fun pulse(): Boolean {
                AgilityHandler.forceWalk(
                    p, -1, loc, loc.transform(Direction.NORTH, 5),
                    Animation.create(Animations.CLIMB_THROUGH_OBSTACLE_10580), 10, 20.0, null
                )
                p.animate(Animation(Animations.CRAWL_844), 3)
                p.animate(Animation(Animations.CLIMB_OUT_OF_OBSTACLE_10579), 4)
                return true
            }
        })
        return true
    }

    /**
     * Handles skull slope obstacle
     */
    private fun climbSkullSlopeObstacle(p: Player, scenery: Scenery): Boolean {
        val loc = p.location
        if (loc.x == 3530) {
            sendMessage(p, "You can't do that from here.")
            return false
        }
        lock(p, 3)
        GameWorld.Pulser.submit(object : Pulse() {
            override fun pulse(): Boolean {
                AgilityHandler.forceWalk(p, -1, loc, loc.transform(Direction.WEST, 3), Animation.create(Animations.CLIMB_DOWN_B_740), 15, 25.0, null)
                p.animate(Animation(-1), 2)
                return true
            }
        })
        return true
    }

    /**
     * Handles zip line obstacle
     */
    private fun zipLineObstacle(p: Player, scenery: Scenery): Boolean {
        val loc = p.location
        val helmet = getItemFromEquipment(p, EquipmentSlot.HEAD)

        val randomMessage = messages.random()
        if (helmet != null) {
            sendNPCDialogue(p, NPCs.AGILITY_TRAINER_1664, randomMessage)
            return true
        }

        if (!finishedMoving(p))
            return false

        lock(p, 16)
        face(p, loc.transform(Direction.SOUTH))
        animate(p, Animations.WEREWOLF_ZIPLINE_1601)
        replaceScenery(scenery, shared.consts.Scenery.ZIP_LINE_5142, 6)
        sendMessage(p, "You bravely cling on to the death slide by your teeth.")

        GameWorld.Pulser.submit(object : Pulse() {
            override fun pulse(): Boolean {
                sendChat(p, "WAAAAAARRRGGGHHHH!!!!!!", 4)
                AgilityHandler.forceWalk(
                    p, -1, loc, Location.create(3528, 9874, 0),
                    Animation(1602), 30, 0.0, null, 1
                )
                p.sendMessage("... and land safely on your feet.", 14)
                runTask(p, 14) {
                    rewardXP(p, Skills.AGILITY, 180.0)
                    resetAnimator(p)
                }
                return true
            }
        })
        return true
    }

    override fun configure() {
        SceneryDefinition.forId(shared.consts.Scenery.STEPPING_STONE_35996).handlers["option:jump-to"] = this
        arrayOf(5133, 5134, 5135).forEach { SceneryDefinition.forId(it).handlers["option:jump"] = this }
        SceneryDefinition.forId(shared.consts.Scenery.PIPE_5152).handlers["option:squeeze-through"] = this
        SceneryDefinition.forId(shared.consts.Scenery.SKULL_SLOPE_5136).handlers["option:climb-up"] = this
        arrayOf(5139, 5140, 5141).forEach { SceneryDefinition.forId(it).handlers["option:teeth-grip"] = this }
    }

    override fun getDestination(node: Node, n: Node): Location?
    {
        return when (n.id)
        {
            Objects.ZIP_LINE_5139 -> Location.create(3528, 9910, 0)
            Objects.STEPPING_STONE_35996 -> node.location ?: super.getDestination(node, n)
            else -> super.getDestination(node, n)
        }
    }

    companion object {
        private val stickLocation = Location.create(3542, 9912, 0)
        val stickRandomLocation: Location = Location.getRandomLocation(stickLocation, 2, true)
        val messages = listOf(
            "That headgear won't help you here, human! Take it off!",
            "You need to take your headgear off before you try the Deathslide, otherwise you won't be able to get a good enough grip with your teeth."
        )
        private val steppingPath = listOf(
            Location.create(3538, 9873, 0),
            Location.create(3538, 9875, 0),
            Location.create(3538, 9877, 0),
            Location.create(3540, 9877, 0),
            Location.create(3540, 9879, 0),
            Location.create(3540, 9881, 0)
        )
    }
}
