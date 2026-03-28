package content.global.plugins.item

import content.global.travel.glider.GnomeGlider
import core.api.*
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.interaction.QueueStrength
import core.game.node.entity.player.Player
import core.game.node.entity.skill.Skills
import core.game.world.map.Location
import shared.consts.Animations
import shared.consts.Components
import shared.consts.Items

private const val SQUASH_GRAPHICS_BEGIN = 767
private const val SQUASH_GRAPHICS_END = 769
private const val SQUASH_ANIM_BEGIN = 4544
private const val SQUASH_ANIM_END = 4546
private const val LAUNCH_GRAPHICS = 768
private const val LAUNCH_ANIMATION = 4547

/**
 * Handles interactions with the Grand Seed Pod item.
 */
class GrandSeedPodOptionPlugin : InteractionListener {
    override fun defineListeners() {
        on(intArrayOf(Items.GRAND_SEED_POD_9469), IntType.ITEM, "squash", "launch") { player, _ ->
            val option = getUsedOption(player)
            if (!removeItem(player, Items.GRAND_SEED_POD_9469)) return@on false

            when (option) {
                "launch" -> launchSeedPod(player)
                "squash" -> squashSeedPod(player)
                else -> return@on false
            }
            true
        }
    }

    /**
     * Handles the "launch" option for the Grand Seed Pod.
     *
     * @param player The player.
     */
    private fun launchSeedPod(player: Player) {
        visualize(player, LAUNCH_ANIMATION, LAUNCH_GRAPHICS)
        delayEntity(player, 7)
        queueScript(player, 3, QueueStrength.SOFT) { stage ->
            when (stage) {
                0 -> {
                    rewardXP(player, Skills.FARMING, 100.0)
                    openOverlay(player, Components.FADE_TO_BLACK_115)
                    keepRunning(player)
                }
                1 -> {
                    setMinimapState(player, 2)
                    delayScript(player, 3)
                }
                2 -> {
                    teleport(player, GnomeGlider.TA_QUIR_PRIW.location)
                    delayScript(player, 2)
                }
                3 -> {
                    closeOverlay(player)
                    setMinimapState(player, 0)
                    stopExecuting(player)
                }
                else -> stopExecuting(player)
            }
        }
    }

    /**
     * Handles the "squash" option for the Grand Seed Pod.
     *
     * @param player The player.
     */
    private fun squashSeedPod(player: Player) {
        visualize(player, SQUASH_ANIM_BEGIN, SQUASH_GRAPHICS_BEGIN)
        delayEntity(player, 12)
        queueScript(player, 3, QueueStrength.SOFT) { stage ->
            when (stage) {
                0 -> {
                    animate(player, Animations.INVISIBLE_1241, true)
                    keepRunning(player)
                }
                1 -> {
                    teleport(player, Location.create(2464, 3494, 0))
                    keepRunning(player)
                }
                2 -> {
                    visualize(player, Animations.INVISIBLE_1241, SQUASH_GRAPHICS_END)
                    delayScript(player, 2)
                }
                3 -> {
                    animate(player, SQUASH_ANIM_END, true)
                    adjustLevel(player, Skills.FARMING, -5)
                    keepRunning(player)
                }
                else -> stopExecuting(player)
            }
        }
    }
}
