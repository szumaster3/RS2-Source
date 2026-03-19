package content.region.kandarin.yanille.dialogue

import content.region.kandarin.west_ardougne.diary.dialogue.AleckDiaryDialogue
import core.api.openDialogue
import core.api.openNpcShop
import core.game.dialogue.Dialogue
import core.game.dialogue.IfTopic
import core.game.dialogue.Topic
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.diary.Diary
import core.game.node.entity.player.link.diary.DiaryType
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import shared.consts.NPCs

@Initializable
class AleckDialogue(player: Player? = null) : Dialogue(player) {

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        player("Hello.")
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when (stage) {
            0 -> npc("Hello, hello, and a most warm welcome to my Hunter", "Emporium. We have everything the discerning Hunter", "could need.").also { stage++ }
            1 -> npc("Would you like me to show you our range of", "equipment? Or was there something specific you were", "after?").also { stage++ }
            2 -> showTopics(
                IfTopic("Talk about achievement diary", openDialogue(player, AleckDiaryDialogue()), Diary.canClaimLevelRewards(player!!, DiaryType.ARDOUGNE, 2)),
                Topic("Ok, let's see what you've got.",4),
                Topic("I'm not interested, thanks.",end()),
                Topic("Who's that guy over there?",30)
            )
            4 -> player("Ok, let's see what you've got!").also { stage = 10 }
            5 -> player("I'm not interested, thanks.").also { stage = END_DIALOGUE }
            6 -> player("Who's that guy over there?").also { stage = 30 }
            10 -> {
                end()
                openNpcShop(player, NPCs.ALECK_5110)
            }
            30 -> npc("Him? I think he might be crazy. Either that or he's", "seeking attention.").also { stage++ }
            31 -> npc("He keeps trying to sell me these barmy looking weapons", "he's invented. I can't see them working, personally.").also { stage = END_DIALOGUE }
        }
        return true
    }

    override fun newInstance(player: Player?): Dialogue = AleckDialogue(player)

    override fun getIds(): IntArray = intArrayOf(NPCs.ALECK_5110)
}
