package content.region.kandarin.west_ardougne.diary.dialogue

import content.data.GameAttributes
import core.api.*
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.diary.Diary
import core.game.node.entity.player.link.diary.DiaryType
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import shared.consts.Items
import shared.consts.NPCs

/**
 * Represents the Ardougne diary dialogue with Doctor Orbon.
 */
@Initializable
class DoctorOrbonDiaryDialogue(player: Player? = null) : Dialogue(player) {

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        player("I have a question about my Achievement Diary.")
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when (stage) {
            0 -> {
                if (Diary.canClaimLevelRewards(player, DiaryType.ARDOUGNE, 1)) {
                    player("I've completed all of the Easy tasks in my Ardougne set.").also { stage = 8 }
                } else if (Diary.canReplaceReward(player, DiaryType.ARDOUGNE, 1)) {
                    npcl(FaceAnim.ASKING, "You again? How did you get on with the cloak?").also { stage = 15 }
                } else if (isDiaryComplete(player, DiaryType.ARDOUGNE, 2)) {
                    player("I'd like to change my Watchtower Teleport point.").also { stage = 18 }
                } else {
                    options("What is the Achievement Diary?", "What are the rewards?", "How do I claim the rewards?", "Sorry, I was just leaving.")
                    stage++
                }
            }

            1 -> when (buttonId) {
                1 -> player("What is the Achievement Diary?").also { stage++ }
                2 -> player("What are the rewards?").also { stage = 5 }
                3 -> player("How do I claim the rewards?").also { stage = 6 }
                4 -> end()
            }

            2 -> npcl(FaceAnim.NEUTRAL, "It's a diary that helps you keep track of particular achievements. Here on Ardougne it can help you, discover some quite useful things. Eventually, with, enough exploration, the people of Ardougne will reward you.").also { stage++ }
            3 -> options("What are the rewards?", "How do I claim the rewards?", "Sorry, I was just leaving.").also { stage++ }
            4 -> when (buttonId) {
                1 -> player("What are the rewards?").also { stage = 5 }
                2 -> player("How do I claim the rewards?").also { stage = 6 }
                3 -> end()
            }
            5 -> npcl(FaceAnim.NEUTRAL, "For completing the Ardougne set, you are presented with a cloak. This cloak will become increasingly useful with each difficulty level of the set that you complete. When you are presented with your rewards, you will be told of their uses.").also { stage = 3 }
            6 -> npc(FaceAnim.NEUTRAL, "You need to complete all of the Diary in a set of a particular difficulty, then you can claim your reward.", "Some of the Ardougne set's Diary are simple, some will require skill levels, and some might require quests to be started or completed.").also { stage++ }
            7 -> npcl(FaceAnim.FRIENDLY, "To claim the Ardougne task rewards, speak to Ardougne's town crier, Aleck in Yanille, or myself.").also { stage = 0 }
            8 -> npcl(FaceAnim.HAPPY, "Ah, that's a relief. You can wear an Ardougne cloak now - I have one for you.").also { stage++ }
            9 -> player("Yes please.").also { stage++ }
            10 -> {
                Diary.flagRewarded(player, DiaryType.ARDOUGNE, 1)
                sendDoubleItemDialogue(player, Items.ARDOUGNE_CLOAK_1_14701, Items.ANTIQUE_LAMP_14704, "Doctor Orbon hands you a cloak and an old lamp.")
                stage++
            }
            11 -> npcl(FaceAnim.NEUTRAL, "It's strange, that cloak. I'm sure it has magical powers. People like market stallholders don't seem to notice doing certain...things when you wear it.").also { stage++ }
            12 -> npcl(FaceAnim.NEUTRAL, "You'll see what I mean when you try it on. Oh, there's also a lamp here I found in the storeroom - you might find a use for it.").also { stage++ }
            13 -> player(FaceAnim.HALF_ASKING, "Thanks! What else does it do?").also { stage++ }
            14 -> npcl(FaceAnim.NEUTRAL, "Well, now, it seems to be able to teleport you to the monastery south of Ardougne, though why you'd want to visit those drunken monks, I don't know. I'm sure that there are other bonuses that I haven't heard of, but maybe you can find them.").also { stage = 0 }
            15 -> player(FaceAnim.SAD, "I lost it.").also { stage++ }
            16 -> npcl(FaceAnim.ASKING, "Lost it? Well, it just so happens I found another. Take better care of this one.").also { stage++ }
            17 -> {
                Diary.grantReplacement(player, DiaryType.ARDOUGNE, 1)
                sendItemDialogue(player, Items.ARDOUGNE_CLOAK_1_14701, "Doctor Orbon hands you a cloak.")
                stage = 0
            }
            18 -> {
                val altTele = getAttribute(player, GameAttributes.ATTRIBUTE_WATCHTOWER_ALT_TELE, false)
                sendOptions(player, "Toggle Watchtower Teleport to ${if (altTele) "Watchtower" else "centre of Yanille"}?", "Yes", "No")
                stage++
            }
            19 -> {
                if (buttonId == 1) {
                    setAttribute(player, GameAttributes.ATTRIBUTE_WATCHTOWER_ALT_TELE, !getAttribute(player, GameAttributes.ATTRIBUTE_WATCHTOWER_ALT_TELE, false))
                }
                val destinationText = if (getAttribute(player, GameAttributes.ATTRIBUTE_WATCHTOWER_ALT_TELE, false)) "centre of Yanille" else "Watchtower"
                sendMessage(player, "Watchtower Teleport will now teleport you to the $destinationText.")
                stage = END_DIALOGUE
            }
        }
        return true
    }

    override fun getIds(): IntArray = intArrayOf(
        NPCs.DOCTOR_ORBON_290
    )
}