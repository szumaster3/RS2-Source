package content.global.plugins.inter.with_item

import core.api.*
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.item.Item
import shared.consts.Animations
import shared.consts.Items

class BlamishSnailSlimePlugin : InteractionListener {

    override fun defineListeners() {

        /*
         * Handles creating the blamish snail slime.
         */

        onUseWith(IntType.ITEM, Items.THIN_SNAIL_3363, Items.PESTLE_AND_MORTAR_233) { player, used, _ ->
            val regions = intArrayOf(13621, 13877, 13876, 13875)
            if (!regions.any { inBorders(player, getRegionBorders(it)) }) {
                return@onUseWith false
            }

            if (!inInventory(player, Items.SAMPLE_BOTTLE_3377)) {
                sendItemDialogue(player, Items.SAMPLE_BOTTLE_3377, "You need an empty sample bottle to place the slime in.")
                return@onUseWith true
            }

            if (!removeItem(player, used.asItem())) {
                return@onUseWith false
            }

            removeItem(player, Item(Items.SAMPLE_BOTTLE_3377, 1))
            animate(player, Animations.HUMAN_USE_PESTLE_AND_MORTAR_364)
            sendMessage(player, "You grind the blamish snail up into slimy pulp.")
            addItem(player, Items.BLAMISH_SNAIL_SLIME_1581, 1)
            return@onUseWith true
        }
    }
}