package content.global.random

import core.api.addItem
import core.api.removeItem
import core.api.sendMessage
import core.api.utils.WeightBasedTable
import core.api.utils.WeightedItem
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import shared.consts.Items

/**
 * Handles interaction with random event box.
 * @author szu
 */
class RandomEventGift : InteractionListener {
    override fun defineListeners() {

        /*
         * Handles opening and roll the random event gift.
         */

        on(Items.RANDOM_EVENT_GIFT_14649, IntType.ITEM, "open") { player, node ->
            val loot = GIFT_BOX.roll().first()

            if (!removeItem(player, node.asItem())) return@on true

            sendMessage(
                player,
                if (loot.id == Items.FLIER_956) {
                    "Inside the box you find a flier! Better luck next time!"
                } else {
                    "You open the mystery box and find ${loot.amount}x ${loot.name.lowercase()}!"
                },
            )
            addItem(player, loot.id, loot.amount)
            return@on true
        }
    }

    companion object {
        /**
         * Gift box table for quiz random event.
         */
        private val GIFT_BOX =
            WeightBasedTable.create(
                WeightedItem(Items.MITHRIL_2H_SWORD_1315, 1, 1, 0.15),
                WeightedItem(Items.BLOOD_RUNE_565, 10, 10, 0.03),
                WeightedItem(Items.NATURE_RUNE_561, 20, 20, 0.12),
                WeightedItem(Items.DIAMOND_1601, 1, 1, 0.02),
                WeightedItem(Items.STEEL_PLATEBODY_1119, 1, 1, 0.1),
                WeightedItem(Items.UNCUT_EMERALD_1622, 1, 1, 0.03),
                WeightedItem(Items.TOOTH_HALF_OF_A_KEY_985, 1, 1, 0.09),
                WeightedItem(Items.LOOP_HALF_OF_A_KEY_987, 1, 1, 0.09),
                WeightedItem(Items.COINS_995, 500, 500, 1.0),
                WeightedItem(Items.MITHRIL_SCIMITAR_1329, 1, 1, 0.2),
                WeightedItem(Items.BLOOD_RUNE_565, 1, 1, 0.04),
                WeightedItem(Items.BUCKET_1925, 1, 1, 1.0),
                WeightedItem(Items.CABBAGE_1965, 1, 1, 1.1),
                WeightedItem(Items.FLIER_956, 1, 1, 0.005),
                WeightedItem(Items.LEATHER_BOOTS_1061, 1, 1, 1.0),
                WeightedItem(Items.ONION_1957, 1, 1, 1.1),
                WeightedItem(Items.OLD_BOOT_685, 1, 1, 1.4),
                WeightedItem(Items.ASHES_592, 1, 1, 1.1),
                WeightedItem(Items.RAW_TUNA_359, 1, 1, 0.3),
                WeightedItem(Items.CASKET_405, 1, 1, 0.28),
            )
    }
}