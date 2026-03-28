package content.region.kandarin.gnome_stronghold.quest.itgronigen.dialogue

import content.data.GameAttributes
import core.api.*
import core.game.dialogue.Dialogue
import core.game.dialogue.DialogueFile
import core.game.dialogue.FaceAnim
import core.game.dialogue.Topic
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.item.Item
import core.plugin.Initializable
import core.tools.DARK_RED
import core.tools.END_DIALOGUE
import shared.consts.Animations
import shared.consts.Items
import shared.consts.NPCs
import shared.consts.Quests

/**
 * Represents the Professor dialogue.
 *
 * # Relations:
 * - [Observatory quest][content.region.kandarin.gnome_stronghold.quest.itgronigen.Observatory]
 */
@Initializable
class ProfessorDialogue(player: Player? = null) : Dialogue(player) {

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        if (!isQuestComplete(player, Quests.OBSERVATORY_QUEST)) {
            setTitle(player, 2)
            sendOptions(player, "What would you like to talk about?", "Talk about the Observatory Quest.", "Talk about Treasure Trails.").also { stage = 4 }
        } else {
            npc("What would you like to talk about?").also { stage = -1 }
        }
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when (stage) {
           -1 -> options("Talk about Treasure Trails.", "I'm just passing through.").also { stage = 1 }
            0 -> options("Talk about the Observatory Quest.", "Talk about Treasure Trails.").also { stage++ }
            1 -> when (buttonId) {
                1 -> options("I've lost my chart.", "How do these work again?").also { stage = 10 }
                2 -> player("I'm just passing through.").also { stage++ }
            }
            2 -> npcl(FaceAnim.NEUTRAL, "Fair enough. Not everyone is interested in this place, I suppose.").also { stage++ }
            3 -> sendDialogue(player, "The professor carries on with his studies.").also { stage = 21 }
            4 -> when (buttonId) {
                1 -> if (getQuestStage(player, Quests.OBSERVATORY_QUEST) < 100) {
                    end().also { openDialogue(player, ProfessorDialogueFile()) }
                } else {
                    npcl(FaceAnim.ASKING, "Hello, friend. Welcome back. Thanks for all your help with the telescope. What can I do for you?").also { stage++ }
                }
                2 -> options("I've lost my chart.", "How do these work again?").also { stage = 10 }
            }

            5 -> options("Do you need any more help with the telescope?", "Nothing, thanks.").also { stage++ }
            6 -> when (buttonId) {
                1 -> playerl(FaceAnim.ASKING, "Do you need any more help with the telescope?").also { stage = 8 }
                2 -> playerl(FaceAnim.CALM, "Nothing, thanks.").also { stage++ }
            }
            7 -> npcl(FaceAnim.NEUTRAL, "Okay, no problem. See you again.").also { stage = 21 }
            8 -> npcl(FaceAnim.NEUTRAL, "Not right now, but the stars may hold a secret for you.").also { stage = 21 }
            9 -> options("I've lost my chart.", "How do these work again?").also { stage++ }
            10 -> when (buttonId) {
                1 -> if (!inInventory(player, Items.CHART_2576)) {
                    player("I've lost my chart.")
                    stage = 19
                } else {
                    npcl(FaceAnim.THINKING, "Um... Are you sure? I think you've got one stored somewhere.")
                    stage = 21
                }
                2 -> player("How do these work again?").also { stage++ }
            }
            11 -> npc(FaceAnim.FRIENDLY, "Ah, I get asked about Treasure Trails all the time!", "Listen carefully and I shall tell you what I know.", "Lots of clues have " + DARK_RED + "degrees</col> and " + DARK_RED + "minutes</col> written", "on them.").also { stage++ }
            12 -> npcl(FaceAnim.FRIENDLY, "These are coordinates of the place where the treasure is buried. You will have to walk to the correct spot, so that your coordinates are exactly the same as the values written on the clue scroll.").also { stage++ }
            13 -> npc(FaceAnim.FRIENDLY, "To do this, you must use a " + DARK_RED + "sextant</col>, a " + DARK_RED + "watch</col> and ", "a$DARK_RED chart</col> to find the coordinates of where you are.").also { stage++ }
            14 -> npcl(FaceAnim.FRIENDLY, "Once you know the coordinates of your position, you know which way you have to walk to get to the treasure's coordinates!").also { stage++ }
            15 -> playerl(FaceAnim.THINKING, "Riiight. So, where do I get these items from?").also { stage++ }
            16 -> npcl(FaceAnim.FRIENDLY, "I think Murphy, the owner of the Fishing Trawler moored at Port Khazard, might be able to spare you a sextant. After that, the nearest clock tower is south of Ardougne - you could probably get a watch there.").also { stage++ }
            17 -> npcl(FaceAnim.FRIENDLY, "I've got plenty of charts myself; just come back here when you've got the sextant and watch, and I'll give you one and teach you how to use them.").also { stage++ }
            18 -> playerl(FaceAnim.FRIENDLY, "Thanks, I'll see you later.").also { stage = 21 }
            19 -> {
                if (freeSlots(player) == 0) {
                    end()
                    npcl(FaceAnim.NEUTRAL, "You don't have enough space for the chart. Come back to me when you do.")
                    return true
                }
                npcl(FaceAnim.HAPPY, "That's not a problem, I've got lots of copies.").also { stage++ }
            }
            20 -> {
                end()
                sendItemDialogue(player, Items.CHART_2576, "The professor has given you a navigation chart.")
                addItem(player, Items.CHART_2576, 1)
            }
            21 -> end()
        }
        return true
    }

    override fun newInstance(player: Player?): Dialogue = ProfessorDialogue(player)

    override fun getIds(): IntArray = intArrayOf(NPCs.OBSERVATORY_PROFESSOR_488)
}

private class ProfessorDialogueFile : DialogueFile() {
    override fun handle(componentID: Int, buttonID: Int) {
        npc = NPC(NPCs.OBSERVATORY_PROFESSOR_488)
        when (stage) {
            0 -> when {
                getQuestStage(player!!, Quests.OBSERVATORY_QUEST) in 2..3 -> player("Hi again!").also { stage = 40 }
                getQuestStage(player!!, Quests.OBSERVATORY_QUEST) in 4..5 -> npcl("The traveller returns!").also { stage = 48 }
                getQuestStage(player!!, Quests.OBSERVATORY_QUEST) in 7..8 -> npcl("How are you getting on finding me some molten glass?").also { stage = 61 }
                getQuestStage(player!!, Quests.OBSERVATORY_QUEST) in 9..10 -> npcl("Did you bring me the mould?").also { stage = 72 }
                getQuestStage(player!!, Quests.OBSERVATORY_QUEST) == 11 -> npc("Is the lens finished?").also { stage = 82 }
                getQuestStage(player!!, Quests.OBSERVATORY_QUEST) == 13 -> npc("Hello, friend.").also { stage = 92 }
                getQuestStage(player!!, Quests.OBSERVATORY_QUEST) == 14 -> npc("Hello, friend.").also { stage = 94 }
                else -> player("Hi, I was...").also { stage++ }
            }
            1 -> npcl(FaceAnim.FRIENDLY, "Welcome to the magnificent wonder of the Observatory, where wonder is all around you, where the stars can be clutched from the heavens!").also { stage++ }
            2 -> player("Wow, nice intro.").also { stage++ }
            3 -> npcl(FaceAnim.HAPPY, "Why, thanks! How might I help you?").also { stage++ }
            4 -> options("I'm totally lost.", "An Observatory?", "I'm just passing through.").also { stage++ }
            5 -> when (buttonID) {
                1 -> player("I'm totally lost.").also { stage = 8 }
                2 -> player("An Observatory?").also { stage = 11 }
                3 -> player("I'm just passing through.").also { stage++ }
            }
            6 -> npc("Fair enough. Not everyone is interested in", "this place, I suppose.").also { stage++ }
            7 -> sendDialogue(player!!, "The professor carries on with his studies.").also { stage = END_DIALOGUE }
            8 -> npcl(FaceAnim.HALF_ASKING, "Lost? It must have been those pesky goblins that led you astray. Head north-east to find the city of Ardougne.").also { stage++ }
            9 -> player("I'm sure I'll find the way. Thanks for all", "your help.").also { stage++ }
            10 -> npcl(FaceAnim.HAPPY, "No problem at all. Come and visit again!").also { stage = 7 }
            11 -> npcl(FaceAnim.HALF_GUILTY, "Of course. We have a superb telescope up in the Observatory, on the hill.").also { stage++ }
            12 -> npcl(FaceAnim.HALF_GUILTY, "A truly marvellous invention, the likes of which you'll never behold again.").also { stage++ }
            13 -> sendNPCDialogue(player!!, NPCs.OBSERVATORY_ASSISTANT_6118, "Well, it would be if it worked.").also { stage++ }
            14 -> npcl(FaceAnim.HALF_GUILTY, "Don't interrupt!").also { stage++ }
            15 -> playerl(FaceAnim.HALF_GUILTY, "What? It doesn't work?").also { stage++ }
            16 -> npcl(FaceAnim.HALF_GUILTY, "Oh, no, no, no. Don't listen to him, he's joking. Aren't you, my FAITHFUL assistant?").also { stage++ }
            17 -> sendNPCDialogue(player!!, NPCs.OBSERVATORY_ASSISTANT_6118, "Nope, dead serious. Hasn't been working for a long time.").also { stage++ }
            18 -> npcl(FaceAnim.HALF_GUILTY, "Arghhh! Get back to work and stop sticking your nose in!").also { stage++ }
            19 -> playerl(FaceAnim.HALF_GUILTY, "So, it's broken. How come?").also { stage++ }
            20 -> npcl(FaceAnim.HALF_GUILTY, "Oh, I suppose there's no use keeping it secret. Did you see those houses outside?").also { stage++ }
            21 -> playerl(FaceAnim.HALF_GUILTY, "Up on the hill? Yes, I've seen them.").also { stage++ }
            22 -> npcl(FaceAnim.HALF_GUILTY, "It's a horde of goblins. Since they moved here they have caused nothing but trouble. Last week, my telescope was tampered with.").also { stage++ }
            23 -> npcl(FaceAnim.HALF_GUILTY, "Now, parts need replacing before it can be used again. They've even been messing around in the dungeons under this area. Something needs to be done.").also { stage++ }
            24 -> sendNPCDialogue(player!!, NPCs.OBSERVATORY_ASSISTANT_6118, "Strikes me that this visitor could help us.").also { stage++ }
            25 -> npcl(FaceAnim.HALF_GUILTY, "Stop being so rude. ... Although, he has a point. What do you say?").also { stage++ }
            26 -> playerl(FaceAnim.HALF_GUILTY, "What, me?").also { stage++ }
            27 -> options("Not right now", "Sounds interesting").also { stage++ }
            28 -> when (buttonID) {
                1 -> player("Oh, sorry, I don't have time for that.").also { stage++ }
                2 -> player("Sounds interesting, what can I do for you?").also { stage = 30 }
            }
            29 -> npcl(FaceAnim.HALF_GUILTY, "Oh dear. I really do need some help. If you see anyone who can help then please send them my way.").also { stage = END_DIALOGUE }
            30 -> npcl(FaceAnim.HALF_GUILTY, "Oh, thanks so much. I shall need some materials for the telescope, so it can be used again. Let's start with three planks of wood for the telescope base.").also { stage++ }
            31 -> npcl(FaceAnim.HALF_GUILTY, "My assistant will help with obtaining these, won't you?").also { stage++ }
            32 -> sendNPCDialogue(player!!, NPCs.OBSERVATORY_ASSISTANT_6118, "As if I don't have enough to do. Seems I don't have a choice.").also { stage++ }
            33 -> npcl(FaceAnim.HALF_GUILTY, "Go talk to him if you need some advice.").also { stage++ }
            34 -> playerl(FaceAnim.HALF_GUILTY, "Okay, I'll be right back.").also { stage++ }
            35 -> {
                end()
                setQuestStage(player!!, Quests.OBSERVATORY_QUEST, 2)
            }
            40 -> npcl(FaceAnim.HAPPY, "It's my helping hand, back again.").also { stage++ }
            41 -> npcl(FaceAnim.ASKING, "Do you have the planks yet?").also { stage++ }
            42 -> if (amountInInventory(player!!, Items.PLANK_960) < 3) {
                player(FaceAnim.HALF_GUILTY, "Sorry, not yet. Three planks was it?").also { stage++ }
            } else {
                player(FaceAnim.NOD_YES, "Yes, I've got them. Here they are.").also {
                    removeItem(player!!, Item(Items.PLANK_960, 3))
                    runTask(player!!, 0) {
                        animate(player!!, 4540)
                        findLocalNPC(player!!, NPCs.OBSERVATORY_PROFESSOR_488)?.let { animate(it, 4540) }
                    }
                    stage += 2
                }
            }
            43 -> npc(FaceAnim.FRIENDLY, "It was indeed.").also { stage = END_DIALOGUE }
            44 -> npcl(FaceAnim.FRIENDLY, "Well done. This will make a big difference.").also { stage++ }
            45 -> npcl("Now, the bronze for the tube. Oh, assistant!").also { stage++ }
            46 -> npcl(FaceAnim.FRIENDLY, "Okay, okay, ask me if you need any help, ${player!!.username}.").also { stage++ }
            47 -> {
                end()
                setQuestStage(player!!, Quests.OBSERVATORY_QUEST, 4)
            }
            48 -> player("Still working hard?").also { stage++ }
            49 -> sendNPCDialogue(player!!, NPCs.OBSERVATORY_ASSISTANT_6118, "Some of us are.").also { stage++ }
            50 -> npcl(FaceAnim.ANNOYED, "What did I tell you about speaking when spoken to?").also { stage++ }
            51 -> npcl(FaceAnim.HAPPY, "So, ${player!!.username}, you have the bronze bar?").also { stage++ }
            52 -> if (!inInventory(player!!, Items.BRONZE_BAR_2349)) {
                player("Not yet.").also { stage++ }
            } else {
                player("I certainly do. Here you go.").also {
                    removeItem(player!!, Item(Items.BRONZE_BAR_2349, 1))
                    runTask(player!!, 0) {
                        animate(player!!, 4540)
                        findLocalNPC(player!!, NPCs.OBSERVATORY_PROFESSOR_488)?.let { animate(it, 4540) }
                    }
                    stage += 2
                }
            }
            53 -> npcl("Please bring me one, then.").also { stage = END_DIALOGUE }
            54 -> npcl(FaceAnim.HAPPY, "Great. Now all I need is the lens made.").also { stage++ }
            55 -> npcl(FaceAnim.HAPPY, "Please get me some molten glass.").also { stage++ }
            56 -> npcl(FaceAnim.NEUTRAL, "Oi! Lazy bones!").also { stage++ }
            57 -> player(FaceAnim.SCARED, "What? I'm not lazy.").also { stage++ }
            58 -> npcl(FaceAnim.NEUTRAL, "Not you! I'm talking to my assistant.").also { stage++ }
            59 -> sendNPCDialogue(player!!, NPCs.OBSERVATORY_ASSISTANT_6118, "Calm down old man, I heard. ${player!!.username}, I'm here if you need any help.").also { stage++ }
            60 -> npcl(FaceAnim.NEUTRAL, "Thank you. Wait a minute, who are you calling 'old'?").also {
                end()
                setQuestStage(player!!, Quests.OBSERVATORY_QUEST, 6)
            }
            61 -> if (!inInventory(player!!, Items.MOLTEN_GLASS_1775)) {
                player("Still working on it.").also { stage++ }
            } else {
                player("Here it is.").also {
                    removeItem(player!!, Item(Items.MOLTEN_GLASS_1775, 1))
                    runTask(player!!, 0) {
                        animate(player!!, 4540)
                        findLocalNPC(player!!, NPCs.OBSERVATORY_PROFESSOR_488)?.let { animate(it, 4540) }
                    }
                    stage += 2
                }
            }
            62 -> npcl("I really need it. Please hurry.").also { stage = END_DIALOGUE }
            63 -> npcl("Excellent work, let's make the lens.").also { stage++ }
            64 -> sendNPCDialogue(player!!, NPCs.OBSERVATORY_ASSISTANT_6118, "It'll need to be made to an exact shape and size.").also { stage++ }
            65 -> npcl("Well, obviously, hence why we have a lens mould.").also { stage++ }
            66 -> sendNPCDialogue(player!!, NPCs.OBSERVATORY_ASSISTANT_6118, "Not any more. One of those goblins took it.").also { stage++ }
            67 -> npcl(FaceAnim.SAD, "Great, just what I need. ${player!!.username}, I don't suppose you could find it?").also { stage++ }
            68 -> playerl("I'll have a look - where should I start?").also { stage++ }
            69 -> npcl(FaceAnim.HAPPY, "No idea. You could ask my USELESS assistant if you want.").also { stage++ }
            70 -> sendNPCDialogue(player!!, NPCs.OBSERVATORY_ASSISTANT_6118, "What have I done to deserve this?").also { stage++ }
            71 -> {
                end()
                setQuestStage(player!!, Quests.OBSERVATORY_QUEST, 8)
            }
            72 -> if (!inInventory(player!!, Items.LENS_MOULD_602)) {
                player("Still looking for it.").also { stage++ }
            } else {
                player("I certainly have. You'll never guess what they were", "doing with it.").also { stage += 2 }
            }
            73 -> npcl("Please try and find it; my assistant may be able to help.").also { stage = END_DIALOGUE }
            74 -> npc(FaceAnim.HALF_THINKING, "Well, from the smell I'd guess cooking some vile", "concoction.").also { stage++ }
            75 -> player("Wow, good guess. Well, here you go.").also { stage++ }
            76 -> sendNPCDialogue(player!!, NPCs.OBSERVATORY_ASSISTANT_6118, "Please don't give that to him. Last time he tried any Crafting, I had to spend a week cleaning up after the explosion.").also { stage++ }
            77 -> player(FaceAnim.SCARED, "Explosion?").also { stage++ }
            78 -> npc("Erm, yes. I think in this instance you had probably", "better do it.").also { stage++ }
            79 -> player("I suppose it's better I don't ask.").also { stage++ }
            80 -> npc("You can use the mould with molten glass to make a", "new lens.").also { stage++ }
            81 -> {
                end()
                runTask(player!!, 0) {
                    animate(player!!, Animations.TAKE_THING_OUT_OF_POCKET_AND_GIVE_IT_4540)
                    findLocalNPC(player!!, NPCs.OBSERVATORY_PROFESSOR_488)?.let { animate(it, 4540) }
                }.also {
                    sendItemDialogue(
                        player!!,
                        Items.MOLTEN_GLASS_1775,
                        "The professor gives you back the molten glass.",
                    )
                    addItemOrDrop(player!!, Items.MOLTEN_GLASS_1775)
                    setQuestStage(player!!, Quests.OBSERVATORY_QUEST, 11)
                }
            }
            82 -> if (!inInventory(player!!, Items.OBSERVATORY_LENS_603)) {
                player("How do I make it again?").also { stage++ }
            } else {
                player("I certainly have. You'll never guess what they were", "doing with it.").also { stage += 3 }
            }
            83 -> npc("Use the molten glass with the mould.").also { stage++ }
            84 -> player("Huh. Simple.").also { stage = END_DIALOGUE }
            85 -> playerl("Yes, here it is. You may as well take this mould too.").also { stage++ }
            86 -> {
                val npcId = findLocalNPC(player!!, NPCs.OBSERVATORY_PROFESSOR_488)
                runTask(player!!, 1) {
                    animate(player!!, Animations.TAKE_THING_OUT_OF_POCKET_AND_GIVE_IT_4540)
                    if (npcId != null) {
                        animate(npcId, Animations.TAKE_THING_OUT_OF_POCKET_AND_GIVE_IT_4540)
                    }
                }
                npcl("Wonderful, at last I can fix the telescope.").also { stage++ }
            }
            87 -> npc("Would you accompany me to the Observatory? You", "simply must see the telescope in operation.").also { stage++ }
            88 -> player("Sounds interesting. Count me in.").also { stage++ }
            89 -> npc("Superb. You'll have to go via the dungeon under the", "goblin settlement, seeing as the bridge is broken. You'll", "find stairs up to the Observatory from there.").also { stage++ }
            90 -> player("Okay. See you there.").also { stage++ }
            91 -> {
                end()
                setAttribute(player!!, GameAttributes.OBSERVATORY_CHEST_FAIL_COUNTER, 0)
                setQuestStage(player!!, Quests.OBSERVATORY_QUEST, 12)
            }
            92 -> player("Hi, this really is impressive.").also { stage++ }
            93 -> npc("Certainly is. Please, take a look through the telescope", "and tell me what you see.").also { stage = END_DIALOGUE }
            94 -> player("I've had a look through the telescope.").also { stage++ }
            95 -> npc("What did you see? If you're not sure, you can find", "out by looking at the star charts dotted around the", "walls downstairs.").also { stage++ }
            96 -> player("it was...").also { stage++ }
            97 -> {
                end()
                openDialogue(player!!, ProfessorConstellationsDialogue())
            }
        }
    }
}

private class ProfessorConstellationsDialogue : DialogueFile() {
    override fun handle(componentID: Int, buttonID: Int) {
        npc = NPC(NPCs.OBSERVATORY_PROFESSOR_488)

        val constellations = mapOf(
            1 to "Aquarius",
            2 to "Capricorn",
            3 to "Sagittarius",
            4 to "Scorpio",
            6 to "Libra",
            7 to "Virgo",
            8 to "Leo",
            10 to "Cancer",
            11 to "Gemini",
            12 to "Taurus",
            14 to "Aries",
            15 to "Pisces"
        )

        val stageToConstellation = mapOf(
            1 to 1, 2 to 2, 3 to 3, 4 to 4,
            6 to 6, 7 to 7, 8 to 8,
            10 to 10, 11 to 11, 12 to 12,
            14 to 14, 15 to 15
        )

        when (stage) {
            0 -> showTopics(
                Topic("Aquarius", 1, true),
                Topic("Capricorn", 2, true),
                Topic("Sagittarius", 3, true),
                Topic("Scorpio", 4, true),
                Topic("~ next ~", 5, true),
            )

            5 -> showTopics(
                Topic("~ previous ~", 0, true),
                Topic("Libra", 6, true),
                Topic("Virgo", 7, true),
                Topic("Leo", 8, true),
                Topic("~ next ~", 9, true),
            )

            9 -> showTopics(
                Topic("~ previous ~", 5, true),
                Topic("Cancer", 10, true),
                Topic("Gemini", 11, true),
                Topic("Taurus", 12, true),
                Topic("~ next ~", 13, true),
            )

            13 -> showTopics(
                Topic("~ previous ~", 9, true),
                Topic("Aries", 14, true),
                Topic("Pisces", 15, true),
            )

            in stageToConstellation.keys -> {
                val constellationId = stageToConstellation[stage]!!
                player(FaceAnim.HAPPY, "${constellations[constellationId]}!")
                setAttribute(player!!, GameAttributes.OBSERVATORY_CONSTELLATION, constellationId)
                stage = 16
            }

            16 -> npc("That's exactly it!").also { stage++ }
            17 -> {
                animate(player!!, Animations.CHEER_862)
                player("Yes! Woo hoo!").also { stage++ }
            }

            18 -> {
                val chosenId = getAttribute(player!!, GameAttributes.OBSERVATORY_CONSTELLATION, -1)
                val explanation = when (chosenId) {
                    1 -> "That's Aquarius, the water-bearer."
                    2 -> "That's Capricorn, the goat."
                    3 -> "That's Sagittarius, the centaur."
                    4 -> "That's Scorpio, the scorpion."
                    6 -> "That's Libra, the scales."
                    7 -> "That's Virgo, the virtuous."
                    8 -> "That's Leo, the lion."
                    10 -> "That's Cancer, the crab."
                    11 -> "That's Gemini, the twins."
                    12 -> "That's Taurus, the bull."
                    14 -> "That's Aries, the ram."
                    15 -> "That's Pisces, the fish."
                    else -> "I'm afraid not. Have another look. Remember, you can check the star charts on the walls for reference."
                }

                if (chosenId in constellations.keys) {
                    npcl(FaceAnim.HALF_GUILTY, explanation)
                    stage = 19 + (chosenId - 1)
                } else {
                    npcl(FaceAnim.HALF_GUILTY, explanation)
                    stage = END_DIALOGUE
                }
            }

            in 19..30 -> {
                val rewardMessage = when (stage) {
                    19 -> "It seems suitable, then, to award you with water runes!"
                    20 -> "Capricorn will surely reward your insight with an increase to your Strength."
                    21 -> "As you've spotted the archer, I shall reward you with a maple longbow."
                    22 -> "I think weapon poison would make a suitable reward."
                    23 -> "Hmmm, balance, law, order - I shall award you with law runes!"
                    24 -> "Virgo will surely provide you with an increase to Defense."
                    25 -> "I think the majestic power of the lion will improve your Hitpoints."
                    26 -> "An armoured creature - I think I shall reward you with an amulet of protection."
                    27 -> "With the double nature of Gemini, I can't offer you anything more suitable than a two-handed weapon."
                    28 -> "This Strength potion should be a suitable reward."
                    29 -> "A fierce fighter. I'm sure he'll look down on you and improve your Attack for such insight."
                    30 -> "What's more suitable as a reward than some tuna?"
                    else -> null
                }
                rewardMessage?.let { npcl(it) }
                stage = 100
            }

            100 -> {
                end()
                removeAttribute(player!!, GameAttributes.OBSERVATORY_CONSTELLATION)
                finishQuest(player!!, Quests.OBSERVATORY_QUEST)
            }
        }
    }

}
