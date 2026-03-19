package content.region.kandarin.seers_village.diary

import core.api.runTask
import core.api.sendMessage
import core.game.interaction.InteractionListener
import core.game.world.map.zone.impl.DarkZone.Companion.checkDarkArea
import shared.consts.Items

class SeersHeadbandPlugin : InteractionListener {

    companion object {
        val HEADBAND_ITEM_IDS = intArrayOf(
            Items.SEERS_HEADBAND_1_14631,
            Items.SEERS_HEADBAND_2_14645,
            Items.SEERS_HEADBAND_3_14646
        )
    }

    override fun defineListeners() {
        onUnequip(HEADBAND_ITEM_IDS) { player, _ ->
            if (player.zoneMonitor.isInZone("Dark zone")) {
                sendMessage(player, "Removing the headband may leave you without a light source.")
                return@onUnequip false
            }
            return@onUnequip true
        }

        onEquip(HEADBAND_ITEM_IDS) { player, _ ->
            if (player.zoneMonitor.isInZone("Dark zone")) {
                runTask(player, 1) {
                    checkDarkArea(player)
                }
            }
            return@onEquip true
        }
    }
}