package content.region.other.ape_atoll.dialogue

import core.api.*
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.dialogue.Topic
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import shared.consts.Items
import shared.consts.NPCs

/**
 * Represents the Solihib dialogue.
 */
@Initializable
class SolihibDialogue(player: Player? = null) : Dialogue(player) {

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        if(!inEquipment(player, Items.MSPEAK_AMULET_4022)) {
            npc(FaceAnim.OLD_DEFAULT, "Ah! Ah Uh Ah! Ook Ook! Ah Uh Ah!")
        } else {
            npc(FaceAnim.OLD_DEFAULT, "Would you like to buy or sell some food?")
            stage = 1
        }

        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when (stage) {
            0 -> sendDialogue(player!!, "You cannot understand what he is saying.").also { stage = END_DIALOGUE }
            1 -> showTopics(
                Topic("Yes, please.", 2, false),
                Topic("No, thanks.", END_DIALOGUE, false)
            )
            2 -> {
                end()
                openNpcShop(player!!, NPCs.SOLIHIB_1433)
            }
        }
        return true
    }

    override fun newInstance(player: Player?): Dialogue = DagaDialogue(player)

    override fun getIds(): IntArray = intArrayOf(NPCs.SOLIHIB_1433)
}
