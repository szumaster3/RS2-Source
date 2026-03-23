package content.region.other.keldagrim.dialogue

import core.api.hasRequirement
import core.api.openInterface
import core.api.sendOptions
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.dialogue.Topic
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import shared.consts.Components
import shared.consts.NPCs
import shared.consts.Quests

/**
 * Represents the Blasidar The Sculptor dialogue.
 */
@Initializable
class BlasidarTheSculptorDialogue(player: Player? = null) : Dialogue(player) {

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        if(!hasRequirement(player, Quests.THE_GIANT_DWARF)){
            npc(FaceAnim.OLD_NORMAL, "Hello!")
        } else {
            showTopics(
                Topic("Could you change my gravestone, please?", 8),
                Topic("The weather's lovely, don't you think?", 10)
            )
        }
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when (stage) {
            0 -> player(FaceAnim.FRIENDLY, "Ho there! Who are you?").also { stage++ }
            1 -> npc(FaceAnim.OLD_NORMAL, "Only the most famous sculptor in all of Keldagrim.").also { stage++ }
            2 -> player(FaceAnim.HALF_ASKING, "Does Keldagrim have a lot of sculptors then?").also { stage++ }
            3 -> npc(FaceAnim.OLD_NORMAL, "Well... I'm the best outside of Keldagrim too, I tell you! Especially here in the north.").also { stage++ }
            4 -> if(!hasRequirement(player, Quests.MOUNTAIN_DAUGHTER)) {
                player("Sure, sure, of course.").also { stage = END_DIALOGUE }
            } else {
                player(FaceAnim.HAPPY, "Yeah, maybe you should visit the mountain people topside of this mountain some time... all they have is a big lump of stone!").also { stage++ }
            }

            5 -> npc(FaceAnim.OLD_NORMAL, "Ah, the philistines!").also { stage++ }
            6 -> npc(FaceAnim.OLD_NORMAL, "But I'd rather not go up there, they don't like dwarves much.").also { stage++ }
            7 -> player("Oh yes, good point.").also { stage = END_DIALOGUE }

            8 -> npc(FaceAnim.OLD_NORMAL, "Certainly, if you can afford to pay for the materials! I'm sure there'll be something in that catalogue to suit your taste.").also { stage++ }
            9 -> {
                end()
                openInterface(player, Components.GRAVESTONE_SHOP_652)
            }
            10 -> player(FaceAnim.NEUTRAL, "Oh yes, quite.").also { stage++ }
            11 -> npc(FaceAnim.OLD_NORMAL, "My finest piece of work, without a doubt.").also { stage++ }
            12 -> player(FaceAnim.HALF_ASKING, "Say, I was wondering, did you do the statues down", "in the mines as well?").also { stage++ }
            13 -> npc(FaceAnim.OLD_NORMAL, "What, out with the trolls?").also { stage++ }
            14 -> npc(FaceAnim.OLD_NORMAL, "If only! But no, they've been there for many thousands of years,", "actually. Been there before dwarven recorded history.").also { stage++ }
            15 -> player(FaceAnim.HALF_WORRIED, "No no, I'm sure they weren't there before!").also { stage++ }
            16 -> npc(FaceAnim.OLD_NORMAL, "Of course they were, don't be silly!").also { stage++ }
            17 -> player("Alright... my mistake then.").also { stage = END_DIALOGUE }

            /*
            9 -> npc(FaceAnim.OLD_NORMAL, "Why, yes, yes, that I am!").also { stage++ }
            10 -> player(FaceAnim.FRIENDLY, "Oh, well, I'm ${player.username}. Veldaban of the Black Guard", "sent me to you, you see. Said you might need some help with the statue.").also { stage++ }
            11 -> npc(FaceAnim.OLD_NORMAL, "Oh, I don't need an assistant sculptor, really,", "I can do just fine on my own.").also { stage++ }
            12 -> player(FaceAnim.NEUTRAL, "I'm not a sculptor.").also { stage++ }
            13 -> npc(FaceAnim.OLD_NORMAL, "And I've also got a model here, you see,", "to base my statue on.").also { stage++ }
            14 -> player(FaceAnim.ANNOYED, "I'm not a model!").also { stage++ }
            15 -> npc(FaceAnim.OLD_NORMAL, "But come to think of it, I do need some errands run.", "Yes, quite important errands too.").also { stage++ }
            16 -> npc(FaceAnim.OLD_NORMAL, "Yes, you will do just fine.").also { stage++ }
            17 -> player(FaceAnim.NEUTRAL, "Ehm, right, okay.").also { stage++ }
            18 -> player(FaceAnim.HALF_ASKING, "So what did you need doing?").also { stage++ }
            19 -> npc(FaceAnim.OLD_NORMAL, "Well, see... the Consortium has commissioned me to rebuild", "the statue exactly the way it was.").also { stage++ }
            20 -> npc(FaceAnim.OLD_NORMAL, "But the statue itself has broken into a million pieces...", "and we dwarves aren't very good painters, you know, so we don't", "have any paintings of it either.").also { stage++ }
            21 -> player(FaceAnim.HALF_ASKING, "Not a very artistic race, then, are you?").also { stage++ }
            22 -> npc(FaceAnim.OLD_NORMAL, "No, that's not what I said,", "I said we're not very good painters.").also { stage++ }
            23 -> npc(FaceAnim.OLD_NORMAL, "We're excellent with, for example, stone. Oh yes,", "quite good if I say so myself.").also { stage++ }
            24 -> npc(FaceAnim.OLD_NORMAL, "But I digress... simply speaking, I need some help", "with getting some items that I can base the new", "statue on.").also { stage++ }
            25 -> npcl(FaceAnim.OLD_NORMAL, "Will you be able to help me with this, ${player.username}?").also { stage++ }
            26 -> options("No, I can't be bothered.", "Yes, I will do this.").also { stage++ }
            27 -> when (buttonId) {
                1 -> player(FaceAnim.NEUTRAL, "No, I can't be bothered.").also { stage++ }
                2 -> player(FaceAnim.FRIENDLY, "Yes, I will do this.").also { stage = 29 }
            }
            28 -> npc(FaceAnim.OLD_NORMAL, "How rude is that? I don't think the Black Guard will be", "very pleased with you!").also { stage = END_DIALOGUE }
            29 -> npc(FaceAnim.OLD_NORMAL, "Excellent, excellent! Now what I need is the following...").also { stage++ }
            30 -> npc(FaceAnim.OLD_NORMAL, "I will need a pair of boots, the most exquisite boots you can find.", "The more they look like the original the better,", "but I don't think you'll be able to find an exact match.").also { stage++ }
            31 -> npc(FaceAnim.OLD_NORMAL, "For the torso, I need clothes that look like King Alvis' clothes.", "I'm not sure where you would be able to find that,", "but I'm sure you'll manage.").also { stage++ }
            32 -> npc(FaceAnim.OLD_NORMAL, "Also, I need a battleaxe. Now this is particularly important.", "You are in luck, however, as I believe King Alvis'", " original axe still survives.").also { stage++ }
            33 -> player(FaceAnim.HALF_ASKING, "Right, so I need a pair of boots, a piece of clothing", "and a battleaxe. Is that all?").also { stage++ }
            34 -> npc(FaceAnim.OLD_NORMAL, "For now, ${player.username}, for now! I wish you good luck,", "and hurry back with the items!").also { stage++ }
            35 -> player(FaceAnim.HALF_ASKING, "But... wait, wait, how am I supposed to find all this?").also { stage++ }
            36 -> npc(FaceAnim.OLD_NORMAL, "I don't know! You're supposed to find that out for me!").also { stage++ }
            37 -> npc(FaceAnim.OLD_NORMAL, "But it's a beautiful city, Keldagrim is,", "I'm sure you'll see many things while you do these tasks!").also { stage = END_DIALOGUE }
            38 -> player(FaceAnim.SAD, "I don't have your boots yet.").also { stage++ }
            39 -> npc(FaceAnim.OLD_NORMAL, "What else?").also { stage++ }
            40 -> player(FaceAnim.NEUTRAL, "I also don't have your clothes yet,", "I'm afraid.").also { stage++ }
            41 -> npc(FaceAnim.OLD_NORMAL, "And? The battleaxe?").also { stage++ }
            42 -> player(FaceAnim.NEUTRAL, "Sorry, no, I'm still looking for it.").also { stage++ }
            43 -> npc(FaceAnim.OLD_NORMAL, "Look a little harder please!").also { stage = END_DIALOGUE }

             */
        }
        return true
    }

    override fun newInstance(player: Player?): Dialogue = BlasidarTheSculptorDialogue(player)

    override fun getIds(): IntArray = intArrayOf(NPCs.BLASIDAR_THE_SCULPTOR_2141)
}
