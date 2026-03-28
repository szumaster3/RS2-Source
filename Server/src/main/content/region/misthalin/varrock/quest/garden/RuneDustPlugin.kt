package content.region.misthalin.varrock.quest.garden

import core.api.addItem
import core.api.removeItem
import core.api.sendMessage
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import shared.consts.Items

class RuneDustPlugin : InteractionListener {
    // https://youtu.be/Au_Sw35iBfc?si=IKMWkl34M-peri5v&t=147
    private val toolKitID = intArrayOf(Items.HAMMER_2347, Items.CHISEL_1755)
    private val essenceIDs = intArrayOf(Items.PURE_ESSENCE_7936, Items.RUNE_ESSENCE_1436)
    override fun defineListeners() {
        onUseWith(IntType.ITEM, toolKitID, *essenceIDs) { player, _, with ->
            val withItem = with.asItem()
            if(removeItem(player, withItem)) {
                addItem(player, Items.RUNE_SHARDS_6466, 1)
                sendMessage(player, "You break up the rune essence into shards.")
            }
            return@onUseWith true
        }
    }
}