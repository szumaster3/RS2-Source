package content.region.wilderness.plugin.rogue_castle

import core.api.*
import core.game.dialogue.DialogueFile
import core.game.dialogue.FaceAnim
import core.game.node.item.Item
import shared.consts.Items
import shared.consts.NPCs
import kotlin.math.min

class RogueJewelleryDialogue(
    private val itemId: Int,
    private val itemName: String,
    private val price: Int
) : DialogueFile() {

    override fun handle(componentID: Int, buttonID: Int) {
        val player = player ?: return
        when (stage) {
            0 -> {
                val amount = amountInInventory(player, itemId)
                val total = amount * price
                sendNPCDialogueLines(
                    player,
                    NPCs.ROGUE_8122,
                    FaceAnim.HAPPY,
                    false,
                    "That's a nice ${itemName.lowercase()} you've got there. I'll give you",
                    "$total for the bunch. Do we have a deal?"
                )
                stage = 1
            }

            1 -> {
                options("Yes, we do.", "No, we do not.")
                stage = 2
            }

            2 -> when (buttonID) {
                1 -> {
                    val invAmount = amountInInventory(player, itemId)
                    if (invAmount <= 0) {
                        sendMessage(player, "You don't have any ${itemName.lowercase()} to sell.")
                        return
                    }

                    val limitAmount = min(invAmount, LIMIT_OF_SELLING_ITEMS)
                    val profit = limitAmount * price
                    val removed = removeItem(player, Item(itemId, limitAmount))
                    if (!removed) {
                        sendNPCDialogue(
                            player,
                            NPCs.ROGUE_8122,
                            "Sorry, I can't seem to take those from you. Make sure it's unenchanted gold jewellery."
                        )
                        return
                    }
                    addItem(player, Items.COINS_995, profit)
                    sendNPCDialogueLines(
                        player,
                        NPCs.ROGUE_8122,
                        FaceAnim.FRIENDLY,
                        false,
                        "It was a pleasure doing business with you. Come back if",
                        "you have more jewellery to sell."
                    )
                    if (invAmount > limitAmount) {
                        sendNPCDialogue(
                            player,
                            NPCs.ROGUE_8122,
                            "Whoa, that's quite a bit of jewellery! Selling only $limitAmount at a time."
                        )
                    }
                    end()
                }

                2 -> end()
            }
        }
    }

    companion object {
        private const val LIMIT_OF_SELLING_ITEMS = 10_000
    }
}