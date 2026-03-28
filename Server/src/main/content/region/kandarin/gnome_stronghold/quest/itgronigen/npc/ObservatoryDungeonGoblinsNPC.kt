package content.region.kandarin.gnome_stronghold.quest.itgronigen.npc

import core.api.sendChat
import core.game.node.entity.npc.NPC
import core.game.node.entity.npc.NPCBehavior
import shared.consts.NPCs

class ObservatoryDungeonGoblinsNPC : NPCBehavior(NPCs.WAGCHIN_6124, NPCs.NAGHEAD_6123) {

    companion object
    {
        private data class Line(val npcId: Int, val text: String)
        private val DIALOGUE = listOf(
            Line(NPCs.NAGHEAD_6123, "What was I talking about, now?"),
            Line(NPCs.WAGCHIN_6124, "Weren't you going to tell me about the rumour?"),
            Line(NPCs.NAGHEAD_6123, "Oh yeah, that was it!"),
            Line(NPCs.WAGCHIN_6124, "Carry on."),
            Line(NPCs.NAGHEAD_6123, "It's about the new family."),
            Line(NPCs.WAGCHIN_6124, "That joined our horde?"),
            Line(NPCs.NAGHEAD_6123, "The very same!"),
            Line(NPCs.WAGCHIN_6124, "Oh my! Do tell."),
            Line(NPCs.NAGHEAD_6123, "I were hangin' out me washin'."),
            Line(NPCs.WAGCHIN_6124, "Yes..."),
            Line(NPCs.NAGHEAD_6123, "And I looked over at-"),
            Line(NPCs.WAGCHIN_6124, "I'm listening."),
            Line(NPCs.NAGHEAD_6123, "Wait, we're being watched!"),
            Line(NPCs.WAGCHIN_6124, "We are?"),
            Line(NPCs.NAGHEAD_6123, "Yes, a person over there."),
            Line(NPCs.WAGCHIN_6124, "Where?"),
            Line(NPCs.NAGHEAD_6123, "No! Don't make eye contact."),
            Line(NPCs.WAGCHIN_6124, "Oh my!"),
            Line(NPCs.NAGHEAD_6123, "They're listening to us."),
            Line(NPCs.WAGCHIN_6124, "A goblin folk?"),
            Line(NPCs.NAGHEAD_6123, "Oh no! It's one of those tall people."),
            Line(NPCs.WAGCHIN_6124, "Bizarre creatures for sure."),
            Line(NPCs.NAGHEAD_6123, "Makes my skin crawl to see them."),
            Line(NPCs.WAGCHIN_6124, "Why?"),
            Line(NPCs.NAGHEAD_6123, "So tall and neat."),
            Line(NPCs.WAGCHIN_6124, "For sure, my dear."),
            Line(NPCs.NAGHEAD_6123, "I hope it doesn't come to speak with us."),
            Line(NPCs.WAGCHIN_6124, "What a horrid thought!"),
            Line(NPCs.NAGHEAD_6123, "Horrid indeed, my dear."),
            Line(NPCs.WAGCHIN_6124, "What should we do?"),
            Line(NPCs.NAGHEAD_6123, "Let us carry on our conversation."),
            Line(NPCs.WAGCHIN_6124, "Let's!"),
            Line(NPCs.NAGHEAD_6123, "So..."),
            Line(NPCs.WAGCHIN_6124, "Yes?")
        )

        private var index = 0
        private var tickDelay = 0
    }

    override fun onCreation(self: NPC)
    {
        self.isNeverWalks = false
        self.isWalks = false
    }

    override fun tick(self: NPC): Boolean
    {
        if (index >= DIALOGUE.size) return true
        if (tickDelay-- > 0) return true

        val line = DIALOGUE[index]
        if (self.id == line.npcId)
        {
            sendChat(self, line.text)
            index++
            tickDelay = 3
        }

        return true
    }
}