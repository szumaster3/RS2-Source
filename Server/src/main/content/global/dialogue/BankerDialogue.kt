package content.global.dialogue

import content.global.plugins.npc.BankerNPC
import core.api.*
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.dialogue.Topic
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import core.tools.START_DIALOGUE
import shared.consts.Items
import shared.consts.NPCs

/**
 * Represents the dialogue plugin used for the Banker NPC.
 */
@Initializable
class BankerDialogue(player: Player? = null) : Dialogue(player) {

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        val oldModels = intArrayOf(NPCs.GNOME_BANKER_166, NPCs.BANKER_498, NPCs.BANKER_2163, NPCs.BANKER_2164, NPCs.BANKER_5776)
        val incompatibleModels = intArrayOf(NPCs.TZHAAR_KET_ZUH_2619, NPCs.OGRESS_BANKER_7049, NPCs.OGRESS_BANKER_7050)
        val checkRestriction = player?.let { amountInInventory(it, Items.STARDUST_13727) > 1 && amountInBank(it, Items.STARDUST_13727) >= 200 } ?: false

        val checkFaceAnim = when (npc.id) {
            in oldModels -> FaceAnim.OLD_NORMAL
            in incompatibleModels -> FaceAnim.CHILD_NORMAL
            else -> FaceAnim.FRIENDLY
        } ?: FaceAnim.FRIENDLY


        when (stage) {
            START_DIALOGUE -> npcl(checkFaceAnim, "Good day, how may I help you?").also {
                if (hasAwaitingGrandExchangeCollections(player)) {
                    stage++
                } else {
                    stage += 2
                }
            }



            1 -> npcl(checkFaceAnim, "Before we go any further, I should inform you that you " + "have items ready for collection from the Grand Exchange.").also { stage++ }
            2 -> showTopics(
                Topic(FaceAnim.FRIENDLY, "I'd like to access my bank account, please.", 10),
                Topic(FaceAnim.FRIENDLY, "I'd like to check my PIN settings.", 11),
                Topic(FaceAnim.FRIENDLY, "I'd like to collect items.", 12),
                Topic(FaceAnim.ASKING, "What is this place?", 3),
            )
            3 -> npcl(checkFaceAnim, "This is a branch of the Bank of Gielinor. We have branches in many towns.").also { stage++ }
            4 -> playerl(FaceAnim.ASKING, "And what do you do?").also { stage++ }
            5 -> npcl(checkFaceAnim, "We will look after your items and money for you. " + "Leave your valuables with us if you want to keep them safe.").also { stage = END_DIALOGUE }
            10 -> {
                end()
                if (checkRestriction) {
                    sendDialogue(player, "The banker detected that the items in your inventory are blacklisted, which prevented you from opening a bank account. You can do it again after removing them.")
                } else {
                    openBankAccount(player)
                }
            }

            11 -> {
                openBankPinSettings(player)
                end()
            }

            12 -> end()
        }
        return true
    }

    override fun newInstance(player: Player?): Dialogue = BankerDialogue(player)

    override fun getIds(): IntArray = intArrayOf(*BankerNPC.NPC_IDS)
}
