package content.region.asgarnia.falador.diary.plugin

import content.data.consumables.effects.PrayerEffect
import core.api.*
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.interaction.QueueStrength
import core.game.node.entity.player.Player
import core.game.node.item.Item
import core.game.world.update.flag.context.Graphics
import shared.consts.Animations
import shared.consts.Items
import java.util.concurrent.TimeUnit

class FaladorShieldPlugin : InteractionListener {

    override fun defineListeners() {

        /*
         * Handles interaction with Falador shield.
         */

        on(FALADOR_SHIELD, IntType.ITEM, "prayer-restore", "operate") { player, node ->
            val item = node as Item
            val level = getLevel(item.id)
            val opt = getUsedOption(player)

            when (opt) {
                "prayer-restore" -> {
                    handlePrayerRestore(player, level)
                }
                "operate" -> {
                    queueScript(player, 3, QueueStrength.SOFT) {
                        visualize(player, ANIM_EMOTE, GFX_EMOTE[level])
                        resetAnimator(player.asPlayer())
                        return@queueScript  stopExecuting(player)
                    }
                }
            }

            return@on true
        }
    }

    private fun handlePrayerRestore(player: Player, level: Int) {
        val lastRestoreTime = player.getAttribute<Long>("diary:falador:shield-restore-time")

        setTitle(player, 2)
        sendOptions(player, "Are you sure you wish to recharge?", "Yes, recharge my Prayer points.", "No, I've changed my mind.")

        addDialogueAction(player) { _, button ->
            if (button != 2) return@addDialogueAction
            if (lastRestoreTime != null && lastRestoreTime > System.currentTimeMillis()) {
                sendMessage(player, "You have no charges left today.")
                return@addDialogueAction
            }
            val restoreMultiplier = when (level) {
                0 -> 0.25
                1 -> 0.5
                else -> 1.0
            }
            val effect = PrayerEffect(0.0, restoreMultiplier)
            effect.activate(player)
            player.graphics(Graphics(GFX_PRAYER_RESTORE[level]))
            setAttribute(player, "/save:diary:falador:shield-restore-time",
                System.currentTimeMillis() + TimeUnit.DAYS.toMillis(1)
            )
            val messageText = if (level < 2) "some" else "your"
            sendMessage(player, "You restore $messageText Prayer points.")
            return@addDialogueAction
        }
    }

    private fun getLevel(itemId: Int): Int = when (itemId) {
        Items.FALADOR_SHIELD_1_14577 -> 0
        Items.FALADOR_SHIELD_2_14578 -> 1
        Items.FALADOR_SHIELD_3_14579 -> 2
        else -> -1
    }

    companion object {
        const val ANIM_EMOTE: Int = Animations.HUMAN_FALADOR_SHIELD_RESTORE_11012
        val GFX_EMOTE: IntArray = intArrayOf(
            shared.consts.Graphics.FALADOR_SHIELD_1_EMOTE_1966,
            shared.consts.Graphics.FALADOR_SHIELD_3_EMOTE_1965,
            shared.consts.Graphics.FALADOR_SHIELD_3_EMOTE_1965
        )
        val GFX_PRAYER_RESTORE: IntArray = intArrayOf(
            shared.consts.Graphics.FALADOR_SHIELD_PRAY_RESTORE_1962,
            shared.consts.Graphics.FALADOR_SHIELD_PRAY_RESTORE_1963,
            shared.consts.Graphics.FALADOR_SHIELD_PRAY_RESTORE_1964
        )
        val FALADOR_SHIELD: IntArray = intArrayOf(
            Items.FALADOR_SHIELD_1_14577,
            Items.FALADOR_SHIELD_2_14578,
            Items.FALADOR_SHIELD_3_14579
        )
    }
}
