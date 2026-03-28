package content.global.skill.magic.iface

import content.region.kandarin.yanille.dialogue.BertDialogue
import content.region.kandarin.yanille.quest.handsand.TheHandintheSand.Companion.getStoreFile
import core.api.*
import core.game.dialogue.DialogueFile
import core.game.dialogue.FaceAnim
import core.game.interaction.InterfaceListener
import core.game.node.entity.npc.NPC
import core.game.world.GameWorld
import core.game.world.map.Location
import core.game.world.update.flag.context.Graphics
import core.tools.END_DIALOGUE
import core.tools.RandomFunction
import shared.consts.Components
import shared.consts.NPCs
import shared.consts.Quests

/**
 * Represents the NPC contact interface.
 * @author szu
 */
class NPCContactInterface : InterfaceListener {
    private val contactNPCs = arrayOf(
        NPCs.HONEST_JIMMY_4362,
        NPCs.BERT_3108,
        NPCs.ADVISOR_GHRIM_1375,
        NPCs.TURAEL_8273,
        NPCs.LANTHUS_1526,
        NPCs.SUMONA_7780,
        NPCs.MAZCHNA_8274,
        NPCs.DURADEL_8275,
        NPCs.VANNAKA_1597,
        NPCs.MURPHY_464,
        NPCs.CHAELDAR_1598,
        NPCs.CYRISUS_432,
        NPCs.LARRY_5424
    )

    private val randomDialogue = arrayOf(
        Blurberry(),
        Bert(),
        EvilBob(),
        EvilDave(),
        Camel(),
        CaptainCain(),
        Cyrisus(),
        DrunkenDwarf(),
        FatherUrhney(),
        GeneralWartface(),
        GypsyAris(),
        Hans(),
        Homunculus(),
        KGPAgent(),
        LumbridgeCow(),
        LumbridgeGuide(),
        LumbridgeSheep(),
        Man(),
        MyArm(),
        Osman(),
        PartyPete(),
        Romeo(),
        WiseOldMan()
    )

    val random = randomDialogue.random()

    override fun defineInterfaceListeners() {
        on(Components.NPC_CONTACT_429) { player, _, _, buttonID, _, _ ->
            when (buttonID) {
                35 -> {
                    closeInterface(player)

                    val dialogue = randomDialogue.filter { dialogue ->
                        dialogue.npc?.id != NPCs.BERT_3108 || hasRequirement(player, Quests.THE_HAND_IN_THE_SAND)
                    }

                    val random = dialogue.randomOrNull()
                    if (random != null) {
                        openDialogue(player, random)
                    }

                    return@on true
                }
            }

            var index = when (buttonID) {
                10, 38 -> 0
                11, 39 -> 1
                12, 40 -> 2
                13, 41 -> 3
                17, 43 -> 4
                27, 42 -> 5
                18, 44 -> 6
                23, 45 -> 7
                28, 46 -> 8
                30, 47 -> 9
                29, 48 -> 10
                32, 33 -> 11
                34, 49 -> 12
                else -> -1
            }
            if (index == -1) index = RandomFunction.random(contactNPCs.size)
            player.getAttribute<() -> Unit>("contact-caller")?.invoke()
            player.interfaceManager.close()
            var npcId = contactNPCs[index]

            if (npcId == NPCs.BERT_3108 && !hasRequirement(player, Quests.THE_HAND_IN_THE_SAND)) {
                npcId = contactNPCs.filter { it != NPCs.BERT_3108 }.random()
            }

            player.dialogueInterpreter.open(npcId, NPC(npcId, Location(0, 0)))
            return@on true
        }
    }

    class Blurberry : DialogueFile() {
        override fun handle(
            componentID: Int,
            buttonID: Int,
        ) {
            npc = NPC(NPCs.BLURBERRY_848)
            when (stage) {
                0 -> player(FaceAnim.HALF_ASKING, "Hello?").also { stage++ }
                1 -> npcl(FaceAnim.OLD_NORMAL, "Blurberry here! Can I help?").also { stage++ }
                2 -> player(FaceAnim.FRIENDLY, "He He. Yes you can. I'm looking for", "a Gnome. Gnome Mates.").also { stage++ }
                3 -> npcl(FaceAnim.OLD_NORMAL, "Okay, I'll ask. Everybody shush! Is there a Gnome Mates here? Did anyone come in with Gnome Mates?").also { stage++ }
                4 -> sendDialogue(player!!, "You hear laughter in the background.").also { stage++ }
                5 -> npcl(FaceAnim.OLD_NORMAL, "Ooooh, who is this? I'll get you!").also { stage++ }
                6 -> player(FaceAnim.LAUGH, "Hahaha.").also { stage = END_DIALOGUE }
            }
        }
    }

    class Camel : DialogueFile() {
        override fun handle(
            componentID: Int,
            buttonID: Int,
        ) {
            npc = NPC(NPCs.AL_THE_CAMEL_2809)
            when (stage) {
                0 -> player(FaceAnim.FRIENDLY, "Hi there!").also { stage++ }
                1 -> npcl(FaceAnim.CHILD_NORMAL, "Why, helloooo!").also { stage++ }
                2 -> player(FaceAnim.HALF_ASKING, "A camel?").also { stage++ }
                3 -> npcl(FaceAnim.CHILD_NORMAL, "Well this is grand darling. How nice it is to make your acquaintance!").also { stage++ }
                4 -> player(FaceAnim.FRIENDLY, "Thank you. Nice to meet you too.").also { stage++ }
                5 -> player(FaceAnim.HALF_THINKING, "Wait a minute. How am I talking to you without", "a Camulet?").also { stage++ }
                6 -> npcl(FaceAnim.CHILD_NORMAL, "My dear, I'm a camel, how should I know?").also { stage++ }
                7 -> player(FaceAnim.THINKING, "Hmmm. I suppose this spell must tap directly into", "your mind. So, there's no need for language to get", "in the way.").also { stage++ }
                8 -> npcl(FaceAnim.CHILD_NORMAL, "I think I am somewhat confused.").also { stage++ }
                9 -> npcl(FaceAnim.CHILD_NORMAL, "May one ask how you contacted me?").also { stage++ }
                10 -> player(FaceAnim.NEUTRAL, "I'm just using one of the Lunar spells.").also { stage++ }
                11 -> npcl(FaceAnim.CHILD_NORMAL, "Oh my, oh my. These youngsters today and their new-fangled gadgets!").also { stage = END_DIALOGUE }
            }
        }
    }

    class CaptainCain : DialogueFile() {
        override fun handle(
            componentID: Int,
            buttonID: Int,
        ) {
            npc = NPC(NPCs.CAPTAIN_CAIN_5030)
            when (stage) {
                0 -> player(FaceAnim.HALF_ASKING, "Who's that?").also { stage++ }
                1 -> npcl(FaceAnim.ASKING, "What? Such insolence! How dare you speak like that, you poor excuse for goblin-dribble!").also { stage++ }
                2 -> player(FaceAnim.HALF_ASKING, "What?").also { stage++ }
                3 -> npcl(FaceAnim.FRIENDLY, "Drop and give me twenty!").also { stage++ }
                4 -> player("Okay, okay. I'm doing them now.").also { stage++ }
                5 -> npcl(FaceAnim.FRIENDLY, "...").also { stage++ }
                6 -> npcl(FaceAnim.FRIENDLY, "Liar!").also { stage++ }
                7 -> player("Wait a minute, I don't have to put up with this. You", "do twenty!").also { stage++ }
                8 -> npcl(FaceAnim.FRIENDLY, "Why I oughtta!").also { stage++ }
                9 -> player("Erm, yeah. Bye!").also { stage = END_DIALOGUE }
            }
        }
    }

    class Bert : DialogueFile() {
        override fun handle(
            componentID: Int,
            buttonID: Int,
        ) {
            val store = getStoreFile()
            val username = player?.username?.lowercase() ?: ""
            val alreadyClaimed = store[username]?.asBoolean ?: false
            npc = NPC(NPCs.BERT_3108)
            when ((0..1).random()) {
                0 -> when (stage) {
                    0 -> playerl(FaceAnim.FRIENDLY, "Hi there, Bert.").also { stage++ }
                    1 -> npcl(FaceAnim.HALF_ASKING, "Huh? What tha' doin' in marn 'ed?").also { stage++ }
                    2 -> playerl(FaceAnim.FRIENDLY, "This is the voice of your conscience. Hand all your money to Player! Wooo wooo!").also { stage++ }
                    3 -> npcl(FaceAnim.HALF_ASKING, "Be away wit' ya!").also { stage++ }
                    4 -> playerl(FaceAnim.FRIENDLY, "Just kidding, I'm using magic to talk to you. I'm totally 'armless.").also { stage++ }
                    5 -> if (inBorders(player!!, getRegionBorders(10032))) { // nearby the Dragon Inn.
                        npcl(FaceAnim.HALF_ASKING, "Oh ar. Hey, then why ya standon' so close? Speak in person please!").also { stage = END_DIALOGUE }
                    } else {
                        end()
                    }
                }

                1 -> when (stage) {
                    0  -> npcl(FaceAnim.HALF_ASKING, "Oh ar. So wha' do thee want?").also { stage++ }
                    1  -> playerl(FaceAnim.FRIENDLY, "Could you hand some sand into my bank?").also { stage++ }
                    2 -> if(alreadyClaimed){
                        npcl(FaceAnim.HALF_ASKING, "I be a wee bi' busy to 'elp wit' yon sand, come back tomorra.").also { stage++ }
                    } else {
                        npcl(FaceAnim.HALF_ASKING, "Ah, keep yer 'air on, I'll leg I' to tha bank wit' some sand fer ya.").also { stage = 4 }
                    }
                    3 -> playerl(FaceAnim.FRIENDLY, "Okay, Bert, I'll come back for my sand a bit later on.").also { stage = END_DIALOGUE }
                    4 -> {
                        end()
                        BertDialogue.rewardSand(player!!,84)
                        stage = END_DIALOGUE
                    }
                }
            }
        }
    }

    class Cyrisus : DialogueFile() {
        override fun handle(
            componentID: Int,
            buttonID: Int,
        ) {
            npc = NPC(NPCs.CYRISUS_8279)
            when ((0..5).random()) {
                0 -> when (stage) {
                    0 -> playerl(FaceAnim.FRIENDLY, "Hi, Cyrisus. I'm contacting you using magic.").also { stage++ }
                    1 -> npcl(FaceAnim.FRIENDLY, "I'm quite familiar with it. Can I help with something?").also { stage = END_DIALOGUE }
                }

                1 -> when (stage) {
                    0 -> playerl(FaceAnim.FRIENDLY, "Hello!").also { stage++ }
                    1 -> npcl(FaceAnim.FRIENDLY, "Oh, hi.").also { stage++ }
                    2 -> playerl(FaceAnim.FRIENDLY, "What adventure are you now on?").also { stage++ }
                    3 -> npcl(FaceAnim.FRIENDLY, "Well, I just dispatched TzTok-Jad. Pretty tough! I'm now on my way to the desert. I'm going to see if I can hunt down this Kalphite Queen everyone's been telling me about.").also { stage++ }
                    4 -> playerl(FaceAnim.FRIENDLY, "I should warn you, you'll need a team to take that beast on.").also { stage++ }
                    5 -> npcl(FaceAnim.FRIENDLY, "I've already thought of that. I've arranged for a couple of the slayer masters to join me.").also { stage++ }
                    6 -> playerl(FaceAnim.FRIENDLY, "Wow! I wish I could witness that!").also { stage = END_DIALOGUE }
                }

                2 -> when (stage) {
                    0 -> playerl(FaceAnim.FRIENDLY, "Hello again!").also { stage++ }
                    1 -> npcl(FaceAnim.FRIENDLY, "That's funny, I was just talking about you.").also { stage++ }
                    2 -> playerl(FaceAnim.FRIENDLY, "You were?").also { stage++ }
                    3 -> npcl(FaceAnim.FRIENDLY, "Yes, I've been arranging various leaders of ${GameWorld.settings?.name} to discuss some political issues. It seems you've certainly been around a bit!").also { stage++ }
                    4 -> playerl(FaceAnim.FRIENDLY, "I suppose I have. What issues are you suppose to discuss in this meeting? Who will be there?").also { stage++ }
                    5 -> npcl(FaceAnim.FRIENDLY, "Sorry, that's top secret information.").also { stage++ }
                    6 -> playerl(FaceAnim.FRIENDLY, "That's fine, I understand.").also { stage = END_DIALOGUE }
                }

                3 -> when (stage) {
                    0 -> playerl(FaceAnim.FRIENDLY, "Hi there, where are you now?").also { stage++ }
                    1 -> npcl(FaceAnim.FRIENDLY, "I am in the clan wars free for all arena: the one with the pretty red portal. I'm meeting lots of interesting people.").also { stage++ }
                    2 -> playerl(FaceAnim.FRIENDLY, "Oh? That's a funny place to meet people.").also { stage++ }
                    3 -> npcl(FaceAnim.FRIENDLY, "Well, I thought that too, but this guy told me to bring all my most valuable items and follow him in there to meet his friends").also { stage++ }
                    4 -> playerl(FaceAnim.FRIENDLY, "Um, Cyrisus, aren't you afraid that they might kill you so that you lose all your stuff?").also { stage++ }
                    5 -> npcl(FaceAnim.FRIENDLY, "Oh yes, they did try that. So I killed all of them instead. But it was very nice to meet them anyway.").also { stage++ }
                    6 -> playerl(FaceAnim.FRIENDLY, "I guess you're fine.").also { stage = END_DIALOGUE }
                }

                4 -> when (stage) {
                    0 -> playerl(FaceAnim.FRIENDLY, "Hey. How's it going?").also { stage++ }
                    1 -> npcl(FaceAnim.FRIENDLY, "Summoning, summoning and more summoning!").also { stage++ }
                    2 -> playerl(FaceAnim.FRIENDLY, "Really? Nice one. SO what's your choice of a pet at the moment?").also { stage++ }
                    3 -> npcl(FaceAnim.FRIENDLY, "Dragon!").also { stage++ }
                    4 -> playerl(FaceAnim.FRIENDLY, "No way! I'm so jealous.").also { stage++ }
                    5 -> npcl(FaceAnim.FRIENDLY, "It's certainly taken a lot of hard work, but it's very much worthwhile!").also { stage++ }
                    6 -> playerl(FaceAnim.FRIENDLY, "I'll bet!").also { stage = END_DIALOGUE }
                }

                5 -> when (stage) {
                    0 -> playerl(FaceAnim.FRIENDLY, "Hello mate. What are you up to now?").also { stage++ }
                    1 -> npcl(FaceAnim.FRIENDLY, "Erm, I'm a bit busy at the moment.").also { stage++ }
                    2 -> playerl(FaceAnim.FRIENDLY, "Oooh, I bet you're travelling the seas. Or maybe you're visiting world leaders?").also { stage++ }
                    3 -> npcl(FaceAnim.FRIENDLY, "Well, I'm trying to fight TzTok-Jad at the moment.").also { stage++ }
                    4 -> playerl(FaceAnim.FRIENDLY, "He must be really tough!").also { stage++ }
                    5 -> npcl(FaceAnim.FRIENDLY, "Yes, he's got a combat level of 702.").also { stage++ }
                    6 -> playerl(FaceAnim.FRIENDLY, "Enjoy!").also { stage++ }
                    7 -> npcl(FaceAnim.FRIENDLY, "Thanks. Arghhhh, death to you, foul beast!").also { stage = END_DIALOGUE }
                }
            }
        }
    }

    class DrunkenDwarf : DialogueFile() {
        override fun handle(
            componentID: Int,
            buttonID: Int,
        ) {
            npc = NPC(NPCs.DRUNKEN_DWARF_956)
            when (stage) {
                0 -> player(FaceAnim.FRIENDLY, "Howdy.").also { stage++ }
                1 -> npcl(FaceAnim.OLD_DRUNK_LEFT, "Waaahaaay! It's ${player!!.username}! *hic* Maate!").also { stage++ }
                2 -> player(FaceAnim.HALF_ASKING,"How's things?").also { stage++ }
                3 -> npcl(FaceAnim.OLD_DRUNK_LEFT, "I tink oiv drunk a bit toooo much.").also { stage++ }
                4 -> player(FaceAnim.FRIENDLY, "So, same as always.").also { stage++ }
                5 -> npcl(FaceAnim.OLD_DRUNK_LEFT, "Wont a kebab?").also { stage++ }
                6 -> player(FaceAnim.FRIENDLY, "Uh, no, I'm good thanks.").also { stage++ }
                7 -> npcl(FaceAnim.OLD_DRUNK_LEFT, "Well, oym goona pass owt now. Have one on me!").also { stage++ }
                8 -> player(FaceAnim.NEUTRAL, "Oh, okay.").also { stage++ }
                9 -> npcl(FaceAnim.OLD_DRUNK_LEFT, ".....*").also { stage++ }
                10 -> player(FaceAnim.THINKING, "Erm, bye.").also { stage = END_DIALOGUE }
            }
        }
    }

    class EvilBob : DialogueFile() {
        override fun handle(
            componentID: Int,
            buttonID: Int,
        ) {
            npc = NPC(NPCs.EVIL_BOB_2478)
            when (stage) {
                0 -> player(FaceAnim.HALF_ASKING, "Hello?").also { stage++ }
                1 -> npcl(FaceAnim.CHILD_NORMAL, "Hello.").also { stage++ }
                2 -> player(FaceAnim.HAPPY, "Ah, Bob! I've been meaning to ask you some questions").also { stage++ }
                3 -> npcl(FaceAnim.CHILD_NORMAL, "Okay, but be quick.").also { stage++ }
                4 -> player(FaceAnim.HALF_ASKING, "Well, where did you come from? Why do you wander", "around ${GameWorld.settings!!.name}? I've heard lots of rumours", "about your past!").also { stage++ }
                5 -> npcl(FaceAnim.CHILD_NORMAL, "It's simple really...").also { stage++ }
                6 -> npcl(FaceAnim.CHILD_NORMAL, "cough* *cough*.").also { stage++ }
                7 -> player(FaceAnim.AFRAID, "Are you okay?").also { stage++ }
                8 -> npcl(FaceAnim.CHILD_NORMAL, "cough* I'm *cough* just...").also { stage++ }
                9 -> player(FaceAnim.AFRAID, "Bob?").also { stage++ }
                10 -> player(FaceAnim.EXTREMELY_SHOCKED, "Bob! Don't die! Noooooo!").also { stage++ }
                11 -> npcl(FaceAnim.CHILD_NORMAL, "...").also { stage++ }
                12 -> npcl(FaceAnim.CHILD_NORMAL, "Sorry, I had a hairball.").also { stage++ }
                13 -> player(FaceAnim.DISGUSTED, "Euw! That's nasty.").also { stage++ }
                14 -> npcl(FaceAnim.CHILD_NORMAL, "Hey, it's normal. I'm off now!").also { stage++ }
                15 -> player(FaceAnim.AFRAID, "Bob?").also { stage++ }
                16 -> player(FaceAnim.SAD, "Oh no, Bob, please don't go.").also { stage++ }
                17 -> npcl(FaceAnim.CHILD_NORMAL, "Good bye.").also { stage = END_DIALOGUE }
            }
        }
    }

    class EvilDave : DialogueFile() {
        override fun handle(
            componentID: Int,
            buttonID: Int,
        ) {
            npc = NPC(NPCs.EVIL_DAVE_2909)
            when (stage) {
                0 -> player(FaceAnim.FRIENDLY,"Hallo!").also { stage++ }
                1 -> npcl(FaceAnim.FRIENDLY, "Whoa! A voice in my head! What do you want?").also { stage++ }
                2 -> player(FaceAnim.FRIENDLY,"I am your evil master!").also { stage++ }
                3 -> npcl(FaceAnim.FRIENDLY, "You're using dark magic to contact me! That is SO evil! What is thy bidding, O Master?").also { stage++ }
                4 -> player(FaceAnim.FRIENDLY,"Build me an army worthy of Zamorak!").also { stage++ }
                5 -> npcl(FaceAnim.FRIENDLY, "Yes, O master! As soon as I get this summoning thing to work").also { stage++ }
                6 -> npcl(FaceAnim.EVIL_LAUGH, "my army of evil").also { stage++ }
                7 -> npc(FaceAnim.EVIL_LAUGH, "UNDEAD CHAOS ZOMBIE DEMON ASSASSINS", "OF DARKNESS will overrun the world!", " *Mwuhahahahaaa!").also { stage = END_DIALOGUE }
            }
        }
    }

    class FatherUrhney : DialogueFile() {
        override fun handle(
            componentID: Int,
            buttonID: Int,
        ) {
            npc = NPC(NPCs.FATHER_URHNEY_458)
            when (stage) {
                0 -> player(FaceAnim.HALF_ASKING, "Hello?").also { stage++ }
                1 -> npcl(FaceAnim.FRIENDLY, "At last! The voice of Saradomin! My years of meditation have paid off!").also { stage++ }
                2 -> player(FaceAnim.STRUGGLE, "Um...").also { stage++ }
                3 -> npcl(FaceAnim.FRIENDLY, "O mighty Saradomin! What is your message to me?").also { stage++ }
                4 -> player(FaceAnim.ANNOYED,"Stop sitting around in a house in a swamp", "and go and do something useful!").also { stage++ }
                5 -> npcl(FaceAnim.FRIENDLY, "Oh, thank you Saradomin! I will meditate on what this message means!").also { stage = END_DIALOGUE }
            }
        }
    }

    class GeneralWartface : DialogueFile() {
        override fun handle(
            componentID: Int,
            buttonID: Int,
        ) {
            npc = NPC(NPCs.GENERAL_WARTFACE_3391)
            when (stage) {
                0 -> player(FaceAnim.FRIENDLY,"Hello!").also { stage++ }
                1 -> npcl(FaceAnim.CHILD_NORMAL, "What you want Bentnoze?").also { stage++ }
                2 -> npcl(FaceAnim.CHILD_NORMAL, "....").also { stage++ }
                3 -> npcl(FaceAnim.CHILD_NORMAL, "Then who did say something? Who that?").also { stage++ }
                4 -> player(FaceAnim.FRIENDLY,"It's ${player!!.username}! I'm talking to you", "by magic!").also { stage++ }
                5 -> npcl(FaceAnim.CHILD_NORMAL, "Hello, ${player!!.username}!").also { stage++ }
                6 -> npcl(FaceAnim.CHILD_NORMAL, "...").also { stage++ }
                7 -> npcl(FaceAnim.CHILD_NORMAL, "Bentnoze, " + (if (player!!.isMale) "he" else "she") + " is here! I hear " + (if (player!!.isMale) "him" else "her") + " in head!").also { stage++ }
                8 -> npcl(FaceAnim.CHILD_NORMAL, "Me not crazy, Bentnose! " + (if (player!!.isMale) "he" else "she") + "talk to me by magic!").also { stage++ }
                9 -> npcl(FaceAnim.CHILD_NORMAL, "...").also { stage++ }
                10 -> npcl(FaceAnim.CHILD_NORMAL, "You shut up Bentnoze! You stupid! You just jealous ${player!!.username} talk to me not you!").also { stage++ }
                11 -> npcl(FaceAnim.CHILD_NORMAL, "......").also { stage++ }
                12 -> npcl(FaceAnim.CHILD_NORMAL, "Shut up Bentnoze! You stupid!").also { stage++ }
                13 -> npcl(FaceAnim.CHILD_NORMAL, ".....").also { stage++ }
                14 -> npcl(FaceAnim.CHILD_NORMAL, "SHUT UP!").also { stage = END_DIALOGUE }
            }
        }
    }

    class GypsyAris : DialogueFile() {
        override fun handle(
            componentID: Int,
            buttonID: Int,
        ) {
            npc = NPC(NPCs.GYPSY_ARIS_882)
            when (stage) {
                0 -> player(FaceAnim.FRIENDLY,"Hello.").also { stage++ }
                1 -> npcl(FaceAnim.FRIENDLY, "Because I can tell the future.").also { stage++ }
                2 -> player(FaceAnim.HALF_ASKING, "Um?").also { stage++ }
                3 -> npcl(FaceAnim.FRIENDLY, "That is the answer to your next question.").also { stage++ }
                4 -> player(FaceAnim.HALF_ASKING, "But how did you know what I would ask?").also { stage++ }
                5 -> npcl(FaceAnim.FRIENDLY, "Because I can tell the future.").also { stage++ }
                6 -> player(FaceAnim.NEUTRAL, "Ah, that's very clever.").also { stage++ }
                7 -> npcl(FaceAnim.FRIENDLY, "Thanks. Oh and be careful in the Wilderness. Tonight is not your night.").also { stage++ }
                8 -> player(FaceAnim.FRIENDLY, "Cheers!").also { stage = END_DIALOGUE }
            }
        }
    }

    class Hans : DialogueFile() {
        override fun handle(
            componentID: Int,
            buttonID: Int,
        ) {
            npc = NPC(NPCs.HANS_0)
            when (stage) {
                0 -> player("Hiya.").also { stage++ }
                1 -> npcl(FaceAnim.STRUGGLE, "Oooh! Who are you?").also { stage++ }
                2 -> player(FaceAnim.JOLLY, "I'm a brave knight! I am coming to kill anyone", "in the vicinity of Lumbridge Castle!").also { stage++ }
                3 -> npcl(FaceAnim.PANICKED, "Aaaaargh! Run away, run away!").also { stage++ }
                4 -> player(FaceAnim.LAUGH, "Hehe. Fool!").also { stage = END_DIALOGUE }
            }
        }
    }

    class Homunculus : DialogueFile() {
        override fun handle(
            componentID: Int,
            buttonID: Int,
        ) {
            npc = NPC(NPCs.HOMUNCULUS_5581)
            when (stage) {
                0 -> player(FaceAnim.HALF_ASKING, "Hello?").also { stage++ }
                1 -> npcl(FaceAnim.CHILD_NORMAL, "It's ${player!!.username}. How are you?").also { stage++ }
                2 -> player(FaceAnim.HALF_ASKING, "Fine, thanks. How about you?").also { stage++ }
                3 -> npcl(FaceAnim.CHILD_NORMAL, "Alchemists return the other day. Me turn them into animals.").also { stage++ }
                4 -> player(FaceAnim.HALF_ASKING, "No! Really?").also { stage++ }
                5 -> npcl(FaceAnim.CHILD_NORMAL, "Yeah, me made a uniman and a chickalchemist!").also { stage++ }
                6 -> player(FaceAnim.FRIENDLY, "Nice work!").also { stage = END_DIALOGUE }
            }
        }
    }

    class KGPAgent : DialogueFile() {
        override fun handle(
            componentID: Int,
            buttonID: Int,
        ) {
            npc = NPC(NPCs.KGP_AGENT_5441)
            when ((0..1).random()) {
                0 -> when (stage) {
                    0 -> playerl(FaceAnim.FRIENDLY, "Anyone there?").also { stage++ }
                    1 -> npcl(FaceAnim.CHILD_NORMAL, "INTRUDER! Red alert! Batten down the hatches! DIVE, DIVE, DIVE!").also { stage++ }
                    2 -> playerl(FaceAnim.FRIENDLY, "Err, maybe I'll call back later.").also { stage = END_DIALOGUE }
                }

                1 -> when (stage) {
                    0 -> player("Hello.").also { stage++ }
                    1 -> npcl(FaceAnim.CHILD_NORMAL, "The fish cannot ride the gravy train.").also { stage++ }
                    2 -> player("Sorry?").also { stage++ }
                    3 -> npcl(FaceAnim.CHILD_NORMAL, "The cyclops cannot see his inner walrus?").also { stage++ }
                    4 -> player("Is this code?").also { stage++ }
                    5 -> npcl(FaceAnim.CHILD_NORMAL, "Waddle into a sunset of flaking mackerel?").also { stage++ }
                    6 -> player("I'm going now.").also { stage++ }
                    7 -> npcl(FaceAnim.CHILD_NORMAL, "The sardine is leaving the tin?").also { stage = END_DIALOGUE }
                }
            }
        }
    }

    class LumbridgeCow : DialogueFile() {
        override fun handle(
            componentID: Int,
            buttonID: Int,
        ) {
            npc = NPC(NPCs.COW_3309)
            when (stage) {
                0 -> player(FaceAnim.ASKING,"Hello?").also { stage++ }
                1 -> npcl(FaceAnim.OLD_DEFAULT, "Moooooo.").also { stage++ }
                2 -> player("Hey, I know you can talk!").also { stage++ }
                3 -> npcl(FaceAnim.OLD_DEFAULT, "Moooo.").also { stage++ }
                4 -> player(FaceAnim.ANNOYED, "Stop ignoring me!").also { stage++ }
                5 -> npcl(FaceAnim.OLD_DEFAULT, "Look, mate. Leave me alone!").also { stage++ }
                6 -> playerl(FaceAnim.FRIENDLY, "You talked! You talked!").also { stage++ }
                7 -> npcl(FaceAnim.OLD_DEFAULT, "Moooo.").also { stage++ }
                8 -> player(FaceAnim.THINKING, "I think I'll have some beef tonight.").also { stage = END_DIALOGUE }
            }
        }
    }

    class LumbridgeGuide : DialogueFile() {
        override fun handle(
            componentID: Int,
            buttonID: Int,
        ) {
            npc = NPC(NPCs.LUMBRIDGE_GUIDE_2244)
            when (stage) {
                0 -> player("Hello!").also { stage++ }
                1 -> npcl(FaceAnim.HALF_ASKING, "What, who's that?").also { stage++ }
                2 -> player(FaceAnim.NEUTRAL, "Hehe... ahem...").also { stage++ }
                3 -> player(FaceAnim.FRIENDLY, "This is the voice of your conscience! You've", "been a very naughty boy haven't you?").also { stage++ }
                4 -> npcl(FaceAnim.THINKING, "What? No I haven't!").also { stage++ }
                5 -> player(FaceAnim.NEUTRAL,"You know what I'm talking about! Don't lie!").also { stage++ }
                6 -> npcl(FaceAnim.GUILTY, "Okay, okay! I'm sorry, I'll never do it again!").also { stage++ }
                7 -> player(FaceAnim.NEUTRAL,"If you do it again, you'll be in deep", "trouble.").also { stage = END_DIALOGUE }
            }
        }
    }

    class LumbridgeSheep : DialogueFile() {
        override fun handle(
            componentID: Int,
            buttonID: Int,
        ) {
            npc = NPC(NPCs.SHEEP_1529)
            when (stage) {
                0 -> player(FaceAnim.HALF_ASKING, "Hello?").also { stage++ }
                1 -> npcl(FaceAnim.OLD_DEFAULT, "Baaaa.").also { stage++ }
                2 -> player(FaceAnim.STRUGGLE,"Huh? Okay.").also { stage++ }
                3 -> player(FaceAnim.HALF_ASKING, "Baa, baaa. BAA!").also { stage++ }
                4 -> npcl(FaceAnim.OLD_DEFAULT, "Baaa?").also { stage = END_DIALOGUE }
            }
        }
    }

    class Man : DialogueFile() {
        override fun handle(
            componentID: Int,
            buttonID: Int,
        ) {
            npc = NPC(NPCs.MAN_1)
            when (stage) {
                0 -> player(FaceAnim.NEUTRAL,"Hello.").also { stage++ }
                1 -> npcl(FaceAnim.ANGRY, "Excuse me!").also { stage++ }
                2 -> player(FaceAnim.THINKING, "Oh sorry, what did I do?").also { stage++ }
                3 -> npcl(FaceAnim.ANGRY_WITH_SMILE, "Can't you see I'm on the toilet?").also { stage++ }
                4 -> player(FaceAnim.HALF_THINKING,"Wait a minute... a toilet?").also { stage++ }
                5 -> npcl(FaceAnim.ANNOYED, "Yeah. A toilet.").also { stage++ }
                6 -> player("Riiiight. Oh, man, you didn't wipe your hands?").also { stage++ }
                7 -> npcl(FaceAnim.ANNOYED, "Get over it.").also { stage = END_DIALOGUE }
            }
        }
    }

    class MyArm : DialogueFile() {
        override fun handle(
            componentID: Int,
            buttonID: Int,
        ) {
            npc = NPC(NPCs.MY_ARM_4947)
            when (stage) {
                0 -> player("Hi there.").also { stage++ }
                1 -> {
                    if (!isQuestComplete(player!!, Quests.MY_ARMS_BIG_ADVENTURE)) {
                        npcl(FaceAnim.CHILD_FRIENDLY, "Is dat you, ${player!!.username}? My Arm can hear you but you not here.").also { stage++ }
                    } else {
                        npcl(FaceAnim.CHILD_THINKING, "Voice in my head?").also { stage = 3 }
                    }
                }
                2 -> player("I'm using magic to talk to you.", "How's the goutweed?").also { stage = END_DIALOGUE }
                3 -> player("Huh?").also { stage++ }
                4 -> npcl(FaceAnim.CHILD_SUSPICIOUS, "Go away voice, leave My Arm.").also { stage++ }
                5 -> player("Now that's the most weird conversation I've", "had in a long time.").also { stage = END_DIALOGUE }
            }
        }
    }

    class Osman : DialogueFile() {
        override fun handle(
            componentID: Int,
            buttonID: Int,
        ) {
            npc = NPC(NPCs.OSMAN_924)
            when (stage) {
                0 -> player("Hello.").also { stage++ }
                1 -> npcl(FaceAnim.FRIENDLY, "Reveal yourself!").also { stage++ }
                2 -> player("Ha, call yourself a spymaster! You", "can't see me!").also { stage++ }
                3 -> npcl(FaceAnim.FRIENDLY, "Ahh, I'm hallucinating! Must cut back on sq'irks!").also { stage = END_DIALOGUE }
            }
        }
    }

    class PartyPete : DialogueFile() {
        override fun handle(
            componentID: Int,
            buttonID: Int,
        ) {
            npc = NPC(NPCs.PARTY_PETE_659)
            when (stage) {
                0 -> player(FaceAnim.HALF_ASKING,"Hello?").also { stage++ }
                1 -> npcl(FaceAnim.HAPPY, "Yo! Party, party, party!").also { stage++ }
                2 -> player(FaceAnim.HALF_ASKING,"Um, Party Pete?").also { stage++ }
                3 -> npcl(FaceAnim.HAPPY, "Yeah! Party's the name, and partying is the game! Actually it's a full-time profession. You can't stop the party.").also { stage++ }
                4 -> player(FaceAnim.HALF_ASKING,"Party Pete. Do you ever want to just", "relax?").also { stage++ }
                5 -> npcl(FaceAnim.HAPPY, "Relax, don't do it! Yeah! Party mania! Yeah!").also { stage++ }
                6 -> player(FaceAnim.NEUTRAL,"Bye, Party.").also { stage++ }
                7 -> npcl(FaceAnim.HAPPY, "You may leave the party, but the party never leaves you! Party!").also { stage = END_DIALOGUE }
            }
        }
    }

    class Romeo : DialogueFile() {
        override fun handle(
            componentID: Int,
            buttonID: Int,
        ) {
            npc = NPC(NPCs.ROMEO_639)
            when (stage) {
                0 -> player(FaceAnim.HALF_ASKING,"Hello?").also { stage++ }
                1 -> npcl(FaceAnim.FRIENDLY, "Oh, hello. You sound lovely.").also { stage++ }
                2 -> player(FaceAnim.HALF_ASKING,"Sorry?").also { stage++ }
                3 -> npcl(FaceAnim.FRIENDLY, "You sound lovely. You have a beautiful...twang to your voice.").also { stage++ }
                4 -> player(FaceAnim.HALF_ASKING,"Twang? It's Romeo, isn't it?").also { stage++ }
                5 -> npcl(FaceAnim.FRIENDLY, "May I compare thee to some pineapple chunks?").also { stage++ }
                6 -> player(FaceAnim.NEUTRAL,"No, Romeo. Goodbye.").also { stage = END_DIALOGUE }
            }
        }
    }

    class WiseOldMan : DialogueFile() {
        override fun handle(
            componentID: Int,
            buttonID: Int,
        ) {
            npc = NPC(NPCs.WISE_OLD_MAN_2253)
            when (stage) {
                0 -> {
                    player("Hello!")
                    visualize(player!!, -1, Graphics(shared.consts.Graphics.FIRE_WAVE_IMPACT_157, 50))
                    stage++
                }

                1 -> player(FaceAnim.SCARED, "Ow!").also { stage++ }
                2 -> npcl(FaceAnim.GUILTY, "Keep your foreign magicks out of my head!").also { stage = END_DIALOGUE }
            }
        }
    }
}
