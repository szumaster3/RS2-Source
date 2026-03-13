package content.region.other.tutorial_island.dialogue

import content.data.GameAttributes
import content.region.other.tutorial_island.plugin.TutorialStage
import core.api.*
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import shared.consts.Items
import shared.consts.NPCs

@Initializable
class MiningInstructorDialogue(player: Player? = null) : Dialogue(player) {

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        when (getAttribute(player, GameAttributes.TUTORIAL_STAGE, 0)) {
            30 -> npc(FaceAnim.FRIENDLY, "Hi there. You must be new around here.", "What do I call you? 'Newcomer' seems so impersonal, and if we're", "going to be working together. I'd rather call you oy", "name." )
            34 -> playerl(FaceAnim.FRIENDLY, "I prospected both types of rock! One has tin and the other copper.")
            40 -> playerl(FaceAnim.ASKING, "How do I make a weapon out of this?")
            in 43..50 -> npc(FaceAnim.HALF_ASKING, "Would you like me to recap anything?")
        }
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when (getAttribute(player, GameAttributes.TUTORIAL_STAGE, 0)) {
            30 -> when (stage++) {
                0 -> playerl(FaceAnim.FRIENDLY, "You can call me ${player.username}.")
                1 -> npc(FaceAnim.FRIENDLY, "Ok then, ${player.username}.", "My name is Dezzick and I'm a", "miner by trade. Let's prospect some rocks.")
                2 -> {
                    end()
                    setAttribute(player, GameAttributes.TUTORIAL_STAGE, 31)
                    TutorialStage.load(player, 31)
                }
            }

            34 -> when (stage++) {
                0 -> player(FaceAnim.HAPPY, "I prospected both types of rock! One set contains tin,","and the other has copper ore inside.")
                2 -> npc(FaceAnim.FRIENDLY, "Absolutely right, ${player.username}. These two ore types can", "be smelted together to make bronze.")
                3 -> npc(FaceAnim.FRIENDLY, "So now you know wht ore is in the rocks over there,", "why don't you have a go at mining some tin and", "copper? Here you'll need this to start with.")
                4 -> {
                    addItemOrDrop(player, Items.BRONZE_PICKAXE_1265)
                    sendItemDialogue(player, Items.BRONZE_PICKAXE_1265, "Dezzick gives you a <col=0000FF>bronze pickaxe!</col>")
                }
                5 -> {
                    end()
                    setAttribute(player, GameAttributes.TUTORIAL_STAGE, 35)
                    TutorialStage.load(player, 35)
                }
            }

            35,36 -> {
                npcl(FaceAnim.FRIENDLY, "The rocks around here contain tin and copper. You should try mining some.")
                stage = END_DIALOGUE
            }

            37,38 -> {
                npcl(FaceAnim.FRIENDLY, "Now that you have some ore, you should smelt it into a bronze bar. You can use the furnace over there to do so.")
                stage = END_DIALOGUE
            }

            40 -> when (stage++) {
                0 -> npcl(FaceAnim.FRIENDLY, "Now that you've got a bar, you can smith it into a weapon. To smith something, you need a hammer and an anvil. There's some anvils just here that you can use. See if you can make a bronze dagger.")
                1 -> {
                    addItemOrDrop(player, Items.HAMMER_2347)
                    sendItemDialogue(player, Items.HAMMER_2347, "Dezzick gives you a <col=0000FF>hammer</col>!")
                }
                2 -> {
                    end()
                    setAttribute(player, GameAttributes.TUTORIAL_STAGE, 41)
                    TutorialStage.load(player, 41)
                }
            }

            in 43..50 -> when (stage) {
                0 -> {
                    setTitle(player, 4)
                    sendOptions(
                        player!!,
                        title = "What would you like to hear more about?",
                        "Tell me about mining again.",
                        "Tell me about smelting again.",
                        "Tell me about Smithing again.",
                        "Nope, I'm ready to move on!"
                    )
                }

                1 -> when (buttonId) {
                    1 -> player("Tell me about mining again.").also { stage++ }
                    2 -> player("Tell me about smelting again.").also { stage = 9 }
                    3 -> player("Tell me about Smithing again.").also { stage = 14 }
                    4 -> player("Nope, I'm ready to move on!").also { stage = 19 }
                }

                2 -> npc(FaceAnim.FRIENDLY, "Certainly. To mine you need a pickaxe. Different pickaxes let you mine more efficiently.").also { stage++ }
                3 -> npc(FaceAnim.FRIENDLY, "Earlier I gave you a bronze pickaxe, which is the most inefficient pickaxe available, but is perfect for a beginner.").also { stage++ }
                4 -> npc(FaceAnim.FRIENDLY, "To mine, simply click on a rock that contains ore while you have a pickaxe with you, and you will keep mining the rock until you manage to get some ore, or until it is empty.").also { stage++ }
                5 -> npc(FaceAnim.FRIENDLY, "The better the pickaxe you use, the faster you will get ore from the rock you're mining.").also { stage++ }
                6 -> npc(FaceAnim.FRIENDLY, "You will be able to buy better pickaxes from the Dwarven Mine when you reach the mainland, but they can be expensive.").also { stage++ }
                7 -> npc(FaceAnim.FRIENDLY, "Also, the better the pickaxe the higher the Mining level required to use it will be.").also { stage++ }

                8 -> npc(FaceAnim.FRIENDLY, "Was there anything else you wanted to hear?").also { stage = 0 }

                9  -> npc(FaceAnim.FRIENDLY, "Smelting is very easy. Simply take the ores required to make a metal to a furnace, then use the ores on the furnace to smelt them into a bar of metal.").also { stage++ }
                10 -> npc(FaceAnim.FRIENDLY, "Furnaces are expensive to build and maintain, so there are not that many scattered around the world. I suggest when you find one you remember its location for future use.").also { stage++ }
                11 -> npc(FaceAnim.FRIENDLY, "An alternative to using a furnace to smelt your ore is to use high-level magic to do it.").also { stage++ }
                12 -> npc(FaceAnim.FRIENDLY, "As well as letting you smelt ore anywhere, it has a guaranteed success rate in smelting all ores.").also { stage++ }
                13 -> npc(FaceAnim.FRIENDLY, "Some metals, such as iron, contain impurities and can be destroyed during the smelting process in a traditional furnace, but magical heat does not destroy them.").also { stage = 8 }


                14 -> npc(FaceAnim.FRIENDLY, "When you have acquired enough bars of the metal you wish to work with, you are ready to begin smithing.").also { stage++ }
                15 -> npc(FaceAnim.FRIENDLY, "Take the hammer I gave you, or buy a new one from a general store, and proceed to a nearby anvil.").also { stage++ }
                16 -> npc(FaceAnim.FRIENDLY, "By using a metal bar on an anvil you will be presented with a screen showing the objects you are able to smith at your current level.").also { stage++ }
                17 -> npc(FaceAnim.FRIENDLY, "It's a pretty straightforward skill as I'm sure you discovered while making me that lovely bronze dagger.").also { stage++ }
                18 -> npc(FaceAnim.FRIENDLY, "The higher Smithing level you are, the better quality the metal you can work with. You start off on bronze and work your way up as your smithing skills increase.").also { stage = 8 }

                19 -> npc(FaceAnim.FRIENDLY, "Okay then.").also { stage++ }
                20 -> end()
            }
        }
        return true
    }

    override fun newInstance(player: Player?): Dialogue =
        MiningInstructorDialogue(player)

    override fun getIds(): IntArray =
        intArrayOf(NPCs.MINING_INSTRUCTOR_948)
}