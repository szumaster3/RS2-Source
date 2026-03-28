package content.region.kandarin.gnome_stronghold.quest.itgronigen.npc

import core.api.sendChat
import core.game.node.entity.npc.NPC
import core.game.node.entity.npc.NPCBehavior
import core.tools.RandomFunction
import shared.consts.NPCs

class CampsiteGoblinsNPC : NPCBehavior(NPCs.GREASYCHEEKS_6127, NPCs.SMELLYTOES_6128, NPCs.CREAKYKNEES_6129) {

    private data class Dialogue(val lines: Array<String>)

    private val GREASYCHEEKS = Dialogue(
        arrayOf("This is gonna taste sooo good", "Cook, cook, cook!", "I'm so hungry!")
    )

    private val SMELLYTOES = Dialogue(
        arrayOf("La la la. Do di dum dii!", "Doh ray meeee laa doh faaa!")
    )

    private val CREAKYKNEES = Dialogue(
        arrayOf("Come on! Please light!", "Was that a spark?", "I'm so hungry!")
    )

    private fun getDialogue(id: Int): Dialogue?
    {
        return when (id) {
            NPCs.GREASYCHEEKS_6127 -> GREASYCHEEKS
            NPCs.SMELLYTOES_6128   -> SMELLYTOES
            NPCs.CREAKYKNEES_6129  -> CREAKYKNEES
            else -> null
        }
    }

    override fun onCreation(self: NPC)
    {
        self.isNeverWalks = false
        self.isWalks = false

        self.setAttribute("cooldown", 0)
    }

    override fun tick(self: NPC): Boolean
    {
        val cooldown = self.getAttribute("cooldown", 0)
        if (cooldown > 0)
        {
            self.setAttribute("cooldown", cooldown - 1)
            return true
        }

        val dialogue = getDialogue(self.id) ?: return true
        if (RandomFunction.roll(25))
        {
            sendChat(self, dialogue.lines.random())
            self.setAttribute("cooldown", RandomFunction.random(5, 15))
        }

        return true
    }

}