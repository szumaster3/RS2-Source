package content.region.morytania.plugin

import content.global.skill.agility.AgilityHandler
import content.region.morytania.mort_myre.dialogue.AbidorCrankDialogue
import core.api.*
import core.game.bots.AIPlayer
import core.game.global.action.ClimbActionHandler
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.interaction.QueueStrength
import core.game.node.entity.Entity
import core.game.node.entity.player.Player
import core.game.node.entity.player.info.Rights
import core.game.system.task.Pulse
import core.game.world.GameWorld
import core.game.world.map.Location
import core.game.world.map.zone.ZoneBorders
import core.game.world.update.flag.context.Animation
import core.game.world.update.flag.context.Graphics
import shared.consts.*
import kotlin.random.Random

class MorytaniaPlugin : InteractionListener, MapArea {

    companion object {
        private val SWAMP_BOAT = intArrayOf(Scenery.SWAMP_BOATY_6970, Scenery.SWAMP_BOATY_6969)
        private const val GROTTO_BRIDGE = Scenery.BRIDGE_3522
        private val SWIMMING_ANIMATION = Animation(Animations.SWIMMING_6988)
        private val JUMP_ANIMATION = Animation(Animations.JUMP_OBSTACLE_5355)
        private val FAIL_LOCATION = Location(3439, 3330)
        private val SPLASH_GFX = Graphics(shared.consts.Graphics.WATER_SPLASH_68)
    }

    override fun defineAreaBorders(): Array<ZoneBorders> = arrayOf(ZoneBorders(3426, 3191, 3715, 3588))

    override fun areaEnter(entity: Entity) {
        if (entity is Player &&
            entity !is AIPlayer &&
            !isQuestComplete(entity, Quests.PRIEST_IN_PERIL) &&
            entity.details.rights != Rights.ADMINISTRATOR
        ) {
            kickThemOut(entity)
        }
    }

    private fun kickThemOut(entity: Player) {
        val watchdog =
            core.game.node.entity.npc.NPC(NPCs.ABIDOR_CRANK_3635).apply {
                isNeverWalks = true
                isWalks = false
                location = entity.location
                init()
            }
        entity.lock()

        runTask(watchdog, 1) {
            watchdog.moveStep()
            watchdog.face(entity)
            openDialogue(entity, AbidorCrankDialogue(), watchdog)
            GameWorld.Pulser.submit(
                object : Pulse() {
                    override fun pulse(): Boolean {
                        if (getAttribute(entity, "teleporting-away", false)) return true
                        if (!entity.isActive) poofClear(watchdog)
                        if (entity.dialogueInterpreter.dialogue == null ||
                            entity.dialogueInterpreter.dialogue.file == null
                        ) {
                            openDialogue(entity, AbidorCrankDialogue(), watchdog)
                        }
                        return !watchdog.isActive || !entity.isActive
                    }
                },
            )
        }
    }

    override fun defineListeners() {
        on(SWAMP_BOAT, IntType.SCENERY, "board", "Board ( Pay 10 )") { player, node ->
            if (!isQuestComplete(player, Quests.NATURE_SPIRIT)) return@on false

            val isForward = node.id == 6970
            val overlay = if (isForward) Components.SWAMP_BOATJOURNEY_321 else Components.SWAMP_BOATJOURNEY_BACK_322
            val destination = if (isForward) Location(3522, 3285, 0) else Location(3498, 3380, 0)
            val destinationName = if (isForward) "Mort'ton." else "the swamp"
            val anim = if(isForward) 1372 else 1373

            lock(player, 13)
            openOverlay(player, Components.FADE_TO_BLACK_120)

            queueScript(player, 3, QueueStrength.SOFT) { stage ->
                when (stage) {
                    0 -> {
                        setMinimapState(player, 2)
                        openOverlay(player, overlay)
                        animateInterface(player, overlay, 2, anim)
                        return@queueScript delayScript(player, 7)
                    }

                    1 -> {
                        teleport(player, destination)
                        setMinimapState(player, 0)
                        openInterface(player, Components.FADE_FROM_BLACK_170)
                        return@queueScript keepRunning(player)
                    }

                    2 -> {
                        closeOverlay(player)
                        sendDialogue(player, "You arrive at $destinationName")
                        return@queueScript stopExecuting(player)
                    }

                    else -> return@queueScript stopExecuting(player)
                }
            }

            return@on true
        }

        on(GROTTO_BRIDGE, IntType.SCENERY, "jump") { player, node ->
            val start = node.location
            val fromGrotto = start.y == 3331

            val failAnim = if (fromGrotto) Animation(771) else Animation(770)
            val failLand = if (fromGrotto) Location(3438, 3328) else Location(3438, 3331)

            lock(player, 10)

            if (AgilityHandler.hasFailed(player, 1, 0.1)) {
                val end = if (fromGrotto) FAIL_LOCATION else start

                AgilityHandler.forceWalk(player, -1, start, end, failAnim, 15, 0.0, null, 0).endAnimation = SWIMMING_ANIMATION
                AgilityHandler.forceWalk(player, -1, FAIL_LOCATION, failLand, SWIMMING_ANIMATION, 15, 2.0, null, 3)
                submitIndividualPulse(player, object : Pulse(2) {
                    override fun pulse(): Boolean {
                        sendMessage(player, "You nearly drown in the disgusting swamp.")
                        visualize(player, failAnim, SPLASH_GFX)
                        teleport(player, FAIL_LOCATION)
                        AgilityHandler.fail(player, 0, failLand, SWIMMING_ANIMATION, Random.nextInt(1, 7), "You nearly drown in the disgusting swamp.")
                        return true
                    }
                })
            } else {
                val end = start.transform(0, if (fromGrotto) -2 else 2,0)
                AgilityHandler.forceWalk(player, -1, start, end, JUMP_ANIMATION, 15, 15.0, null, 0)
            }
            return@on true
        }

        on(Scenery.TREE_5005, IntType.SCENERY, "climb up", "climb down") { player, node ->
            val isAtFirstLocation = node.location == Location(3502, 3431)

            val (climbType, targetLocation) = when (getUsedOption(player)) {
                "climb up" -> ClimbActionHandler.CLIMB_UP to
                        if (isAtFirstLocation) Location(3502, 3430, 0) else Location(3502, 3427, 0)
                "climb down" -> ClimbActionHandler.CLIMB_DOWN to
                        if (isAtFirstLocation) Location(3503, 3431, 0) else Location(3502, 3425, 0)
                else -> return@on false
            }

            ClimbActionHandler.climb(player, climbType, targetLocation)
            return@on true
        }

        on(Scenery.ROPE_BRIDGE_5002, IntType.SCENERY, "walk-here") { player, node ->
            val target = if (node.location == Location(3502, 3428)) Location(3502, 3430, 0) else Location(3502, 3427, 0)
            teleport(player, target)
            return@on true
        }
    }
}
