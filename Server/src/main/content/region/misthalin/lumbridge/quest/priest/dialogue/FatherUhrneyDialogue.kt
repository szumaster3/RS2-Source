package content.region.misthalin.lumbridge.quest.priest.dialogue

import core.api.*
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import shared.consts.Items
import shared.consts.NPCs
import shared.consts.Quests

@Initializable
class FatherUhrneyDialogue(player: Player? = null) : Dialogue(player) {

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        npc("Go away! I'm meditating!")
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when (stage) {
            0 -> {
                if (getQuestStage(player, Quests.THE_RESTLESS_GHOST) == 0) {
                    options("Well, that's friendly.", "I've come to repossess your house.").also { stage++ }
                } else if (getQuestStage(player, Quests.THE_RESTLESS_GHOST) == 10) {
                    options("Well, that's friendly.", "I've come to repossess your house.", "Father Aereck sent me to talk to you.").also { stage = 13 }
                } else if (getAttribute(player, "restless-ghost:urhney", false) || isQuestComplete(player, Quests.THE_RESTLESS_GHOST)) {
                    options("Well, that's friendly.", "I've come to repossess your house.", "I've lost the Amulet of Ghostspeak.").also { stage = 26 }
                }
            }
            1 -> when (buttonId) {
                1 -> player("Well, that's friendly.").also { stage++ }
                2 -> player("I've come to repossess your house.").also { stage = 4 }
            }
            2 -> npc(FaceAnim.ANNOYED, "I SAID go AWAY.").also { stage++ }
            3 -> player("Ok, ok... sheesh, what a grouch.").also { stage = END_DIALOGUE }
            4 -> npc("Under what grounds???").also { stage++ }
            5 -> options("Repeated failure on mortgage repayments.", "I don't know, I just wanted this house.").also { stage++ }
            6 -> when (buttonId) {
                1 -> player("Repeated failure on mortgage repayments.").also { stage++ }
                2 -> player("I don't know. I just wanted this house...").also { stage = 12 }
            }
            7 -> npc("What?").also { stage++ }
            8 -> npc("I don't have a mortgage! I built this house.").also { stage++ }
            9 -> player("Sorry. I must have got the wrong address. All the", "houses look the same around here.").also { stage++ }
            10 -> npc("What? What houses? What ARE you talking about???").also { stage++ }
            11 -> player("Never mind.").also { stage = END_DIALOGUE }
            12 -> npc("Oh... go away and stop wasting my time!").also { stage = END_DIALOGUE }
            13 -> when (buttonId) {
                1 -> player("Well, that's friendly.").also { stage = 2 }
                2 -> player("I've come to repossess your house.").also { stage = 4 }
                3 -> player("Father Aereck sent me to talk to you.").also { stage++ }
            }
            14 -> npc("I suppose I'd better talk to you then. What problems", "has he got himself into this time?").also { stage++ }
            15 -> player("He's got a ghost haunting his graveyard.").also { stage++ }
            16 -> npc("Oh, the silly fool.").also { stage++ }
            17 -> npc("I leave town for just five months, and ALREADY he", "can't manage.").also { stage++ }
            18 -> npc("(sigh)").also { stage++ }
            19 ->
                npc("Well, I can't go back and exorcise it. I vowed not to", "leave this place. Until I had done a full two years of", "prayer and meditation.").also { stage++ }
            20 -> npc("Tell you what I can do though; take this amulet.").also { stage++ }
            21 -> {
                if (freeSlots(player) == 0) {
                    sendMessage(player, "You don't have enough inventory space to accept this amulet.")
                    return true
                }

                sendItemDialogue(player, Items.GHOSTSPEAK_AMULET_552, "Father Urhney hands you an amulet.")
                addItem(player, Items.GHOSTSPEAK_AMULET_552, 1)
                setQuestStage(player, Quests.THE_RESTLESS_GHOST, 20)
                setAttribute(player, "/save:restless-ghost:urhney", true)

                stage++
            }
            22 -> npc("It is an Amulet of Ghostspeak.").also { stage++ }
            23 -> npc("So called, because when you wear it you can speak to", "ghosts. A lot of ghosts are doomed to be ghosts because", "they have left some important task uncompleted.").also { stage++ }
            24 -> npc("Maybe if you know what this task is, you can get rid of", "the ghost. I'm not making any guarantees mind you,", "but it is the best I can do right now.").also { stage++ }
            25 -> player("Thank you. I'll give it a try!").also { stage = END_DIALOGUE }
            26 -> when (buttonId) {
                1 -> player("Well, that's friendly.").also { stage = 2 }
                2 -> player("I've come to repossess your house.").also { stage = 4 }
                3 -> player("I've lost the Amulet of Ghostpeak.").also { stage++ }
            }

            27 -> sendDialogue(player, "Father Urhney sighs.").also { stage++ }
            28 -> {
                val amulet = Items.GHOSTSPEAK_AMULET_552
                val check = inInventory(player, amulet, 1) || inEquipment(player, amulet, 1)
                val hasAmuletInBank = inBank(player, amulet, 1)
                when {
                    check -> {
                        npc("What are you talking about? I can see you've got it", "with you!")
                        stage = END_DIALOGUE
                    }

                    hasAmuletInBank -> {
                        npc("What are you talking about? I can see you've got it", "in your bank!")
                        stage = END_DIALOGUE
                    }

                    else -> {
                        npc("How careless can you get? Those things aren't easy to", "come by you know! It's a good job I've got a spare.")
                        stage++
                    }
                }
            }
            29 -> {
                addItem(player, Items.GHOSTSPEAK_AMULET_552, 1)
                sendItemDialogue(player, Items.GHOSTSPEAK_AMULET_552, "Father Urhney hands you an amulet.").also { stage++ }
            }
            30 -> npc("Be more careful this time.").also { stage++ }
            31 -> player("Ok, I'll try to be.").also { stage = END_DIALOGUE }
        }
        return true
    }

    override fun getIds(): IntArray = intArrayOf(NPCs.FATHER_URHNEY_458)
}
