package content.region.misthalin.varrock.quest.garden

import core.api.addItem
import core.api.removeItem
import core.api.sendMessage
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import shared.consts.Items

class PlantCurePlugin : InteractionListener {
    override fun defineListeners() {
        onUseWith(IntType.ITEM, Items.PLANT_CURE_6036, Items.RUNE_DUST_6467) { player, used, with ->
            val usedItem = used.asItem()
            val withItem = with.asItem()
            if(removeItem(player, usedItem) && removeItem(player, withItem)) {
                addItem(player, Items.PLANT_CURE_6468, 1)
                sendMessage(player, "You mix the rune dust into the plant cure.")
            }
            return@onUseWith true
        }
    }
}