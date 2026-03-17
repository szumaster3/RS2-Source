package content.region.karamja.shilo_village.plugin

import core.api.*
import core.game.dialogue.DialogueFile
import core.game.dialogue.FaceAnim
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.npc.NPC
import core.tools.END_DIALOGUE
import core.tools.START_DIALOGUE
import shared.consts.Items
import shared.consts.NPCs

private val ANTIQUE_ITEMS = AntiqueItem.values().map { it.antique }.toIntArray()

class AntiqueStorePlugin : InteractionListener {

    override fun defineListeners() {

        /*
         * Handles talk to Yanni NPC.
         */

        on(NPCs.YANNI_SALIKA_515, IntType.NPC, "talk-to")  { player, node ->
            openDialogue(player, YanniSalikaDialogue(), node)
            return@on true
        }

        /*
         * Handles antique items exchange with Yanni NPCs.
         */

        onUseWith(IntType.NPC, ANTIQUE_ITEMS, NPCs.YANNI_SALIKA_515) { player, used, _ ->
            val antique = used.id

            if (!inInventory(player, antique, 1)) {
                sendNPCDialogue(player, NPCs.YANNI_SALIKA_515, "Sorry Bwana, you have nothing I am interested in.")
                return@onUseWith true
            }

            when (antique) {
                Items.BLACK_PRISM_4808 -> openDialogue(player, BlackPrismDialogue())
                else -> openDialogue(player, YanniDialogue(antique))
            }
            return@onUseWith true
        }
    }

}

/**
 * Represents the Antique items.
 */
private enum class AntiqueItem(val antique: Int, val price: Int) {
    BONE_KEY(Items.BONE_KEY_605, 100),
    STONE_PLAQUE(Items.STONE_PLAQUE_606, 100),
    TATTERED_SCROLL(Items.TATTERED_SCROLL_607, 100),
    CRUMPLED_SCROLL(Items.CRUMPLED_SCROLL_608, 100),
    LOCATING_CRYSTAL(Items.LOCATING_CRYSTAL_611, 500),
    BEADS_OF_THE_DEAD(Items.BEADS_OF_THE_DEAD_616, 1000),
    BERVIRIUS_NOTES(Items.BERVIRIUS_NOTES_624, 100),
    BLACK_PRISM(Items.BLACK_PRISM_4808, 5000);

    companion object {
        /**
         * Finds an [AntiqueItem] by item id.
         */
        fun getAntiqueItem(id: Int): AntiqueItem? = values().find { it.antique == id }
    }
}

private class BlackPrismDialogue : DialogueFile() {
    override fun handle(componentID: Int, buttonID: Int) {
        npc = NPC(NPCs.YANNI_SALIKA_515)
        when (stage) {
            0 -> sendDialogue(player!!, "You show the black prism to Yanni.").also { stage++ }
            1 -> npcl(FaceAnim.FRIENDLY, "Ah you'd like to sell this to me would you? I can offer you 5000 coins!").also { stage++ }

            2 -> {
                setTitle(player!!, 2)
                sendOptions(player!!, "SELL THE PRISM FOR 5000 COINS?", "Yes, I'll sell it for 5000", "No, I think I'll hold on it a while longer yet.")
                stage++
            }

            3 -> when (buttonID) {
                1 -> player("Yes, I'll sell it for 5000 coins.").also { stage = 5 }
                2 -> player("No, I think I'll hold on it a while longer yet.").also { stage++ }
            }

            4 -> npc("Very well my friend, come back if you change", "your mind.").also { stage = END_DIALOGUE }
            5 -> npc("Very well my friend, let me count out your reward.").also { stage++ }
            6 -> {
                end()
                if (!removeItem(player!!, Items.BLACK_PRISM_4808)) {
                    sendMessage(player!!, "You don't have the black prism.")
                    return
                }

                sendMessage(player!!, "You sell the black prism for 5000 coins.")
                addItemOrDrop(player!!, Items.COINS_995, 5000)
                npc(FaceAnim.FRIENDLY, "Thanks!")
            }
        }
    }
}

private class YanniDialogue(antique: Int) : DialogueFile() {

    private val item = AntiqueItem.getAntiqueItem(antique)

    override fun handle(componentID: Int, buttonID: Int) {
        npc = NPC(NPCs.YANNI_SALIKA_515)

        val itemName = getItemName(item!!.antique)

        val dialogue = when (item.antique) {
            Items.BEADS_OF_THE_DEAD_616 -> "Impressive necklace there."
            Items.BLACK_PRISM_4808 -> "Ah you'd like to sell this to me would you? I can offer you 5000 coins!"
            Items.BERVIRIUS_NOTES_624 -> "That's a great copy of Bervirius notes."
            else -> "That's a great $itemName."
        }

        when (stage) {
            START_DIALOGUE -> {
                npcl(FaceAnim.FRIENDLY, dialogue)
                stage++
            }

            1 -> {
                npcl(FaceAnim.FRIENDLY, "I'll give you ${item.price} coins for your $itemName...")
                stage++
            }

            2 -> {
                setTitle(player!!, 2)
                sendOptions(player!!, "Sell the $itemName?", "Yes.", "No.")
                stage++
            }

            3 -> when (buttonID) {
                1 -> {
                    if (removeItem(player!!, item.antique)) {
                        npc("Here's ${item.price} for it.")
                        sendMessage(player!!, "You sell the $itemName for ${item.price} gold.")
                        addItemOrDrop(player!!, Items.COINS_995, item.price)
                    } else {
                        sendMessage(player!!, "You don't have the $itemName.")
                    }
                }
                2 -> end()
            }
        }
    }
}

private class YanniSalikaDialogue : DialogueFile() {
    override fun handle(componentID: Int, buttonID: Int) {
        npc = NPC(NPCs.YANNI_SALIKA_515)
        when (stage) {
            0 -> player(FaceAnim.FRIENDLY, "Hello there!").also { stage = -1 }
            -1 -> npc(FaceAnim.FRIENDLY, "Greetings Bwana! My name is Yanni and I buy and sell", "antiques.").also { stage = 1 }
            1 -> options("Tell me about this antiques business.", "Is there anything else interesting to do around here?", "Hmm, sorry, not interested.").also { stage++ }
            2 -> when (buttonID) {
                1 -> player("Tell me about this antiques business.").also { stage++ }
                2 -> player("Is there anything else interesting to do around", "here?").also { stage = 5 }
                3 -> player("Hmm, sorry, not interested.").also { stage = 9 }
            }
            3 -> npcl(FaceAnim.FRIENDLY, "I buy antiques and other interesting items. If you have any interesting items that you might want to sell me, please let me see them and I'll offer you a fair price.").also { stage++ }
            4 -> npcl(FaceAnim.FRIENDLY, "Would you like me to have a look at your items and give you a quote?").also { stage = 7 }
            5 -> npc(FaceAnim.FRIENDLY, "You sound a bit bored of Shilo Village...", "what's wrong?").also { stage = 100 }
            6 -> npcl(FaceAnim.FRIENDLY, "Fair enough, come back if you change your mind.").also { stage = END_DIALOGUE }
            7 -> options(
                "Yes, please!",
                "Maybe some other time?",
                "Is there anything else interesting to do around here?",
                "Hmm, sorry, not interested.",).also { stage++ }

            8 -> when (buttonID) {
                1 -> player("Yes, please!").also { stage = 10 }
                2 -> player("Maybe some other time?").also { stage++ }
                3 -> player("Is there anything else interesting to do around here?").also { stage = 5 }
                4 -> player("Hmm, sorry, not interested.").also { stage = 14 }
            }
            9 -> npc(FaceAnim.FRIENDLY, "Sure thing. Have a nice day Bwana.").also { stage = END_DIALOGUE }
            10 -> npc("Great Bwana!").also { stage++ }
            11 -> {
                end()
                if (!anyInInventory(player!!, *ANTIQUE_ITEMS)) {
                    npcl(FaceAnim.FRIENDLY, "Sorry Bwana, you have nothing I am interested in.")
                } else {
                    npcl(FaceAnim.FRIENDLY, "And that's the only item I am interested in. If you want to sell me that item, simply show it to me.")
                }
            }
            12 -> npcl(FaceAnim.FRIENDLY, "Those are the items I am interested in Bwana. If you want to sell me those items, simply show them to me.").also { stage++ }
            13 -> end()

            14 -> npc("Fair enough, come back if you change", "your mind.").also { stage = END_DIALOGUE }
            100 -> playerl(FaceAnim.FRIENDLY, "Nothing. I was just wondering if there was anything else to do?").also { stage++ }
            101 -> npcl(FaceAnim.FRIENDLY, "Well, if you're bored, you could do me a small favour and nip to see the jungle forester.").also { stage++ }
            102 -> npc(FaceAnim.FRIENDLY, "I need a piece of red mahogany to resurface an", "antique wardrobe.").also { stage++ }
            103 -> options(
                "You want me to do you a favour?",
                "An adventurer of my calibre, isn't that overkill.",
                "What are you going to give me if I do it?",
                "Nah thanks, I've got bigger fish to fry.",
                "Okay, see you in a tick!",).also { stage++ }

            104 -> when (buttonID) {
                1 -> playerl(FaceAnim.FRIENDLY, "You want me to do you a favour?").also { stage++ }
                2 -> playerl(FaceAnim.FRIENDLY, "An adventurer of my calibre, isn't that overkill.").also { stage = 106 }
                3 -> playerl(FaceAnim.FRIENDLY, "What are you going to give me if I do it?").also { stage = 107 }
                4 -> playerl(FaceAnim.FRIENDLY, "Nah thanks, I've got bigger fish to fry.").also { stage = 108 }
                5 -> playerl(FaceAnim.FRIENDLY, "Okay, see you in a tick!").also { stage = 109 }
            }
            105 -> npcl(FaceAnim.FRIENDLY, "Well, if you've got nothing better to do... I mean, you looked bored.").also { stage = 103 }
            106 -> npcl(FaceAnim.FRIENDLY, "Cooolll! Sorry your most amazingness, didn't realise you were too above it all to do someone a favour!").also { stage = 103 }
            107 -> npcl(FaceAnim.FRIENDLY, "Give you? I don't think you heard me... I asked for a favour! It means that I'll 'owe you one' when you need something doing. That's what a favour is!").also { stage = 103 }
            108 -> npcl(FaceAnim.FRIENDLY, "Fair enough! Hope it all goes well... but, you know, you looked bored so I just thought I'd suggest it.").also { stage = 103 }
            109 -> npcl(FaceAnim.FRIENDLY, "Great, nice of you to do it for me.").also { stage++ }
            110 -> options("I'll get going then!", "Where do I meet this jungle forester?").also { stage++ }
            111 -> when (buttonID) {
                1 -> player("That'd be helpful!").also { stage = END_DIALOGUE }
                2 -> player("Where do I meet this jungle forester?").also { stage++ }
            }
            112 -> npcl(FaceAnim.FRIENDLY, "Oh, sorry, I thought you knew the area. You'll find a jungle forester in between the southern edge of Shilo Village and the Kharazi Jungle.").also { stage++ }
            113 -> npcl(FaceAnim.FRIENDLY, "They're always trying to find ways into that place.").also { stage++ }
            114 -> npcl(FaceAnim.FRIENDLY, "I hear that some old timer from the Legends Guild keeps sending new recruits down there... only Zamorak knows why.").also { stage++ }
            115 -> end()
        }
    }
}