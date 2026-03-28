package content.region.kandarin.gnome_stronghold.quest.itgronigen.dialogue

import content.data.GameAttributes
import core.api.*
import core.game.dialogue.DialogueFile
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.tools.END_DIALOGUE
import shared.consts.Items
import shared.consts.NPCs
import shared.consts.Quests

/**
 * Represents the Observatory assistant dialogue file.
 *
 * # Relations:
 * - [Observatory quest][content.region.kandarin.gnome_stronghold.quest.itgronigen.Observatory]
 */
class ObservatoryAssistantDialogue : DialogueFile() {

    override fun handle(componentID: Int, buttonID: Int) {
        npc = NPC(NPCs.OBSERVATORY_ASSISTANT_6118)
        val questStage = getQuestStage(player!!, Quests.OBSERVATORY_QUEST)
        val amountOfPlanks = amountInInventory(player!!, Items.PLANK_960)
        when (questStage) {
            0 -> when (stage) {
                0 -> playerl(FaceAnim.HALF_ASKING, "Hi, are you busy?").also { stage++ }
                1 -> npcl(FaceAnim.HALF_ASKING, "Me? I'm always busy. See that man there? That's the professor. If he had his way, I think he'd never let me sleep! Anyway, how might I help you?").also { stage++ }
                2 -> options("I was wondering what you do here.", "Just looking around, thanks.", "Can I have a look through that telescope?").also { stage++ }
                3 -> when (buttonID) {
                    1 -> player("I was wondering what you do here.").also { stage++ }
                    2 -> player("Just looking around, thanks.").also { stage = 8 }
                    3 -> player(FaceAnim.HALF_ASKING, "Can I have a look through that telescope?").also { stage = 10 }
                }
                4 -> npcl(FaceAnim.HALF_GUILTY, "Glad you ask. This is the Observatory reception. Up on the cliff is the Observatory dome, from which you can view the heavens. Usually...").also { stage++ }
                5 -> player(FaceAnim.HALF_ASKING, "What do you mean, 'usually'?").also { stage++ }
                6 -> npc("*Ahem*. Back to work, please.").also { stage++ }
                7 -> npc("I'd speak with the professor. He'll explain.").also { stage = 2 }
                8 -> npcl(FaceAnim.HALF_GUILTY, "Okay, just don't break anything. If you need any help, let me know.").also { stage++ }
                9 -> {
                    end()
                    sendDialogue(player!!, "The assistant continues with his work.")
                }
                10 -> npcl(FaceAnim.HALF_GUILTY, "You can. You won't see much though.").also { stage++ }
                11 -> player(FaceAnim.HALF_ASKING, "And that's because?").also { stage++ }
                12 -> npc("Just talk to the professor. He'll fill you in.").also { stage++ }
                13 -> {
                    end()
                    setQuestStage(player!!, Quests.OBSERVATORY_QUEST, 1)
                }
            }
            2 -> when (amountOfPlanks) {
                0 -> when (stage) {
                    0 -> player("Hello there.").also { stage++ }
                    1 -> npc(FaceAnim.HALF_ASKING, "Yes?").also { stage++ }
                    2 -> playerl(FaceAnim.HALF_ASKING, "Where can I find planks of wood? I need some for the telescope's base.").also { stage++ }
                    3 -> npcl(FaceAnim.FRIENDLY, "I understand planks can be found at Port Khazard, to the east of here.").also { stage++ }
                    4 -> npcl(FaceAnim.FRIENDLY, "There are some at the Barbarian Outpost, too. Failing that, you could always ask the Sawmill Operator. He's to the north-east of Varrock, by the Lumber Yard.").also { stage = END_DIALOGUE }
                }

                1, 2 -> when (stage) {
                    0 -> player("Might I have a word?").also { stage++ }
                    1 -> npcl(FaceAnim.HALF_ASKING, "Sure, how can I help you?").also { stage++ }
                    2 -> player("I've got a plank.").also { stage++ }
                    3 -> npcl(FaceAnim.FRIENDLY, "That's nice.").also { stage++ }
                    4 -> playerl(FaceAnim.FRIENDLY, "You know, for the telescope's base.").also { stage++ }
                    5 -> npcl(FaceAnim.FRIENDLY, "Well done. Remember that you'll need three in total.").also {
                        stage = END_DIALOGUE
                    }
                }
                3 -> when (stage) {
                    0 -> player(FaceAnim.HALF_ASKING, "Can I speak with you?").also { stage++ }
                    1 -> npc(FaceAnim.HALF_ASKING, "Why, of course. What is it?").also { stage++ }
                    2 -> playerl(FaceAnim.FRIENDLY, "I've got some planks for the telescope's base.").also { stage++ }
                    3 -> npcl(FaceAnim.FRIENDLY, "Good work! The professor has been eagerly waiting them.").also { stage++ }
                    4 -> {
                        end()
                        setQuestStage(player!!, Quests.OBSERVATORY_QUEST, 3)
                    }
                }
            }
            4 -> when (stage) {
                0 -> player(FaceAnim.HALF_ASKING, "Can I speak with you?").also { stage++ }
                1 -> npc(FaceAnim.HALF_ASKING, "Why, of course. What is it?").also { stage++ }
                2 -> if (!inInventory(player!!, Items.BRONZE_BAR_2349)) {
                    playerl(FaceAnim.HALF_ASKING, "Can you help me? How do I go about getting a bronze bar?").also { stage++ }
                } else {
                    playerl(FaceAnim.HALF_ASKING, "The bronze bar is ready, and waiting for the professor.").also { stage = 5 }
                }
                3 -> npcl(FaceAnim.FRIENDLY, "You'll need to use tin and copper ore on a furnace to produce this metal.").also { stage++ }
                4 -> player(FaceAnim.FRIENDLY, "Right you are.").also { stage = END_DIALOGUE }
                5 -> npc(FaceAnim.FRIENDLY, "He'll surely be pleased. Go ahead and give", "it to him.").also { stage++ }
                6 -> {
                    end()
                    setQuestStage(player!!, Quests.OBSERVATORY_QUEST, 5)
                }
            }
            6 -> when (stage) {
                0 -> player(FaceAnim.HALF_ASKING, "Can I speak with you?").also { stage++ }
                1 -> npc(FaceAnim.HALF_ASKING, "Why, of course. What is it?").also { stage++ }
                2 -> if (!inInventory(player!!, Items.MOLTEN_GLASS_1775)) {
                    playerl(FaceAnim.HALF_ASKING, "What's the best way for me to get molten glass?").also { stage++ }
                } else {
                    playerl(FaceAnim.FRIENDLY, "I managed to get hold of some molten glass.").also { stage = 7 }
                }
                3 -> npcl(FaceAnim.FRIENDLY, "There are many ways, but I'd suggest making it yourself. Get yourself a bucket of sand and some soda ash, which you can get from using seaweed with a furnace.").also { stage++ }
                4 -> npcl(FaceAnim.FRIENDLY, "Use the soda ash and sand together in a furnace and bang - molten glass is all yours.").also { stage++ }
                5 -> npcl(FaceAnim.FRIENDLY, "There's a book about it on the table if you want to know more.").also { stage++ }
                6 -> playerl(FaceAnim.HAPPY, "Thank you!").also { stage = END_DIALOGUE }
                7 -> npcl(FaceAnim.FRIENDLY, "I suggest you have a word with the professor, in that case.").also { stage++ }
                8 -> {
                    end()
                    setQuestStage(player!!, Quests.OBSERVATORY_QUEST, 7)
                }
            }
            8 -> when (stage) {
                0 -> player(FaceAnim.HALF_ASKING, "Can I speak with you?").also { stage++ }
                1 -> npc(FaceAnim.HALF_ASKING, "Why, of course. What is it?").also { stage++ }
                2 -> if (!inInventory(player!!, Items.LENS_MOULD_602)) {
                    playerl(FaceAnim.HALF_ASKING, "Where can I find this lens mould you mentioned?").also { stage++ }
                } else {
                    playerl(FaceAnim.FRIENDLY, "I have the lens mould.").also { stage = 10 }
                }
                3 -> npc("I'm sure I heard one of those goblins talking about it. I", "bet they've hidden it somewhere. Probably using it", "for some strange purpose, I'm sure.").also { stage++ }
                4 -> player(FaceAnim.HALF_ASKING, "What makes you say that?").also { stage++ }
                5 -> npc("I had a nice new star chart, until recently. I went out", "to do an errand for the professor the other day, only", "to see a goblin using it...").also { stage++ }
                6 -> npcl(FaceAnim.FRIENDLY, "...as some kind of makeshift hankey to blow his nose!").also { stage++ }
                7 -> player("Oh dear.").also { stage++ }
                8 -> npc("You may want to look through the dungeon they have under", "their little village.").also { stage++ }
                9 -> player("Thanks for the advice.").also { stage = END_DIALOGUE }
                10 -> npcl(FaceAnim.FRIENDLY, "Well done on finding that! I am honestly quite impressed. Make sure you take it straight to the professor.").also { stage++ }
                11 -> npcl(FaceAnim.FRIENDLY, "Will do.").also { stage++ }
                12 -> {
                    end()
                    setQuestStage(player!!, Quests.OBSERVATORY_QUEST, 9)
                }
            }
            11 -> when (stage) {
                0 -> player(FaceAnim.HALF_ASKING, "Can I speak with you?").also { stage++ }
                1 -> npc(FaceAnim.HALF_ASKING, "Why, of course. What is it?").also { stage++ }
                2 -> if (!inInventory(player!!, Items.OBSERVATORY_LENS_603)) {
                    playerl(FaceAnim.HALF_ASKING, "How should I make this lens?").also { stage++ }
                } else {
                    playerl(FaceAnim.HALF_ASKING, "Do you like this lens? Good, huh?").also { stage = 5 }
                }

                3 -> npcl(FaceAnim.FRIENDLY, "Just use the molten glass with the mould. Simple.").also { stage++ }
                4 -> player(FaceAnim.HAPPY, "Thanks!").also { stage = END_DIALOGUE }
                5 -> npc(FaceAnim.HALF_ASKING, "Nice. What's that scratch?").also { stage++ }
                6 -> playerl(FaceAnim.FRIENDLY, "Oh, erm, that's a feature. Yes, that's it! Indubitably, it facilitates the triangulation of photonic illumination to the correct...").also { stage++ }
                7 -> npcl(FaceAnim.FRIENDLY, "Stop! You can't confuse me with big words. Just pray the professor doesn't notice.").also { stage = END_DIALOGUE }
            }
            in 12..99 -> when (stage) {
                0 -> player(FaceAnim.FRIENDLY, "Hello again.").also { stage++ }
                1 -> npcl(FaceAnim.HAPPY, "Ah, it's the telescope repairman!").also { stage++ }
                2 -> npcl(FaceAnim.FRIENDLY, "The professor is waiting for you in the Observatory.").also { stage++ }
                3 -> player(FaceAnim.HALF_ASKING, "How can I get to the Observatory?").also { stage++ }
                4 -> npcl(FaceAnim.HALF_GUILTY, "Well, since the bridge was ruined, you will have to travel through the dungeon under the goblins' settlement.").also { stage = END_DIALOGUE }
            }
            100 -> when (stage) {
                0 -> if (getAttribute(player!!, GameAttributes.RECEIVED_JUG_OF_WINE, false)) {
                    npcl(FaceAnim.HALF_ASKING, "How was the wine?").also { stage = 16 }
                } else {
                    playerl(FaceAnim.FRIENDLY, "Hi assistant.").also { stage++ }
                }
                1 -> playerl(FaceAnim.FRIENDLY, "Wait a minute.").also { stage++ }
                2 -> playerl(FaceAnim.FRIENDLY, "What's your real name?").also { stage++ }
                3 -> npcl(FaceAnim.FRIENDLY, "My real name?").also { stage++ }
                4 -> playerl(FaceAnim.FRIENDLY, "I only know you as the professor's assistant. What's your actual name?").also { stage++ }
                5 -> npcl(FaceAnim.FRIENDLY, "Patrick.").also { stage++ }
                6 -> playerl(FaceAnim.FRIENDLY, "Hi, Patrick, I'm ${player!!.username}.").also { stage++ }
                7 -> npcl(FaceAnim.FRIENDLY, "Well, hello again, and thanks for helping out the professor.").also { stage++ }
                8 -> npcl(FaceAnim.FRIENDLY, "Oh, and, believe it or not, his name is Mambo-duna-roona, but don't tell him I told you.").also { stage++ }
                9 -> playerl(FaceAnim.FRIENDLY, "You made that up!").also { stage++ }
                10 -> npcl(FaceAnim.FRIENDLY, "No, honest! Anyways, you've made my life much easier. Have a drink on me!").also { stage++ }
                11 -> {
                    sendItemDialogue(player!!, Items.JUG_OF_WINE_1993, "The assistant gives you some wine.")
                    addItemOrDrop(player!!, Items.JUG_OF_WINE_1993, 1)
                    setAttribute(player!!, GameAttributes.RECEIVED_JUG_OF_WINE, true)
                    stage = 12
                }
                12 -> player("Thanks very much.").also { stage++ }
                13 -> npcl(FaceAnim.FRIENDLY, "No problem. Scorpius would be proud.").also { stage++ }
                14 -> player(FaceAnim.HALF_ASKING, "Sorry?").also { stage++ }
                15 -> npcl(FaceAnim.FRIENDLY, "You may want to check out the book on the table, and perhaps look around for a grave...").also { stage = END_DIALOGUE }
                16 -> playerl(FaceAnim.DRUNK, "That was good stuff, *hic*! Wheresh the professher?").also { stage++ }
                17 -> npcl(FaceAnim.HALF_THINKING, "The professor? He's up in the Observatory.").also { stage++ }
                18 -> npcl(FaceAnim.FRIENDLY, "Since the bridge was ruined, you will have to travel through the dungeon under the goblins' settlement.").also { stage = END_DIALOGUE }
            }
            else -> sendDialogue(player!!, "Observatory assistant seems too busy to talk.").also { stage = END_DIALOGUE }
        }
    }
}
