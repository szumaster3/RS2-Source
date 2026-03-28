package content.region.kandarin.gnome_stronghold.quest.eyeglo

import core.api.addItem
import core.api.removeItem
import core.api.sendMessage
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import shared.consts.Items

class MagicGluePlugin : InteractionListener {

    override fun defineListeners() {
        onUseWith(IntType.ITEM, Items.GROUND_MUD_RUNES_9594, Items.BUCKET_OF_SAP_4687) { player, used, with ->
            val usedItem = used.asItem()
            val withItem = with.asItem()
            if(removeItem(player, usedItem) && removeItem(player, withItem)) {
                addItem(player, Items.MAGIC_GLUE_9592)
                sendMessage(player, "You mix the mud rune powder into the tree sap. It looks sticky.")
            }
            return@onUseWith true
        }
    }
}