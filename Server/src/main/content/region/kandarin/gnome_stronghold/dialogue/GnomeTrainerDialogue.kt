package content.region.kandarin.gnome_stronghold.dialogue

import content.data.GameAttributes
import core.api.*
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import core.tools.RandomFunction
import shared.consts.Items
import shared.consts.NPCs

/**
 * Represents the Gnome Trainer dialogue.
 */
@Initializable
class GnomeTrainerDialogue(player: Player? = null) : Dialogue(player) {

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        val rand = RandomFunction.random(0, 3)
        when (rand) {
            0 -> player(FaceAnim.HALF_GUILTY, "Hello there.").also { stage = 0 }
            1 -> player(FaceAnim.HALF_GUILTY, "Hello, what is this place?").also { stage = 3 }
            2 -> player(FaceAnim.HALF_GUILTY, "Hello how are you?").also { stage = 7 }
            3 -> player(FaceAnim.HALF_GUILTY, "This is fun!").also { stage = 10 }
        }
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when (stage) {
            // Hello there.
            0 -> npc(FaceAnim.OLD_NORMAL, "This isn't a grannies' tea party, let's see some sweat", "human. Go! Go! Go!").also { stage = 11 }
            // Hello, what is this place?
            3 -> npc(FaceAnim.OLD_NORMAL, "This, my friend, is where we train. Here we improve", "out agility. It's an essential skill.").also { stage++ }
            4 -> player(FaceAnim.HALF_GUILTY, "It looks easy enough.").also { stage++ }
            5 -> npc(FaceAnim.OLD_NORMAL, "If you complete the course in order from the slippery", "log to the end, your agility will increase much faster", "than by repeating just one obstacle.").also { stage = 11 }
            // Hello how are you?
            7 -> npc(FaceAnim.OLD_NORMAL, "I'm amazed by how much humans chat. The sign over", "there says training area, not pointless conversation area.").also { stage = 11 }
            // This is fun!
            10 -> npc(FaceAnim.OLD_NORMAL, "This is training soldier. If you want fun go make some", "cocktails.").also { stage = 11 }
            11 -> options("What is this place?", "What's so special about this course, then?", "Can I talk about rewards?", "I'm done for now.").also { stage++ }
            12 -> when (buttonId) {
                1 -> player("What is this place?").also { stage++ }
                2 -> player("What's so special about this course, then?").also { stage = 16 }
                3 -> player("Can I talk about rewards?").also { stage = 17 }
                4 -> player("I'm done for now.").also { stage = 21 }
            }
            13 -> npcl(FaceAnim.OLD_NORMAL, "This, my friend, is where we train and improve our Agility. It's an essential skill.").also { stage++ }
            14 -> player(FaceAnim.NEUTRAL, "It looks easy enough.").also { stage++ }
            15 -> npcl(FaceAnim.OLD_NORMAL, "If you complete the course, from the slippery log to the end, your Agility will increase more rapidly than by repeating just one obstacle.").also { stage = END_DIALOGUE }
            16 -> npcl(FaceAnim.OLD_NORMAL, "Well, it's where most people tend to start training.").also { stage = END_DIALOGUE }
            17 -> {
                val count = getAttribute(player, GameAttributes.GNOME_STRONGHOLD_PERFECT_LAPS, 0)
                val firstTalk = getAttribute(player, GameAttributes.GNOME_STRONGHOLD_GNOME_TALK, false)
                val completeLaps = getAttribute(player, GameAttributes.GNOME_STRONGHOLD_COURSE_REWARD, false)
                val hasAgileLegs = hasAnItem(player, Items.AGILE_LEGS_14648).container != null

                when {
                    completeLaps && !hasAgileLegs -> {
                        if (!firstTalk) {
                            npcl(FaceAnim.OLD_DEFAULT, "Well, it looks like you've completed our challenge! Take this as a reward: some Agile legs. You'll find yourself much lighter than usual while wearing them. They are made from the toughest material we gnomes could find, so it might even protect you in combat.")
                            stage = 18
                        } else {
                            npcl(FaceAnim.OLD_DEFAULT, "Of course. How can I help?")
                            stage = 19
                        }
                    }

                    else -> {
                        npc(FaceAnim.OLD_DEFAULT, "Well, you've still got work to do. Your lap count is $count.", "It's 250 successful laps for reward!")
                        stage = 22
                    }
                }
            }

            18 -> {
                end()
                val p = player ?: return true
                if (freeSlots(p) == 0) {
                    npc(FaceAnim.OLD_DEFAULT, "Well, I would give you the reward, but apparently you", "don't have any room.")
                    return true
                }
                addItem(p,  Items.AGILE_LEGS_14648)
                npcl(FaceAnim.OLD_NORMAL, "There you go. Enjoy!")
                setAttribute(p, GameAttributes.GNOME_STRONGHOLD_GNOME_TALK, true)
                stage = 22
            }

            19 -> player(FaceAnim.HALF_ASKING,"Any chance of some more Agile legs?").also { stage++ }

            20 -> {
                end()
                val p = player ?: return true
                if (freeSlots(p) == 0) {
                    npc(FaceAnim.OLD_DEFAULT, "Well, I would give you the reward, but apparently you", "don't have any room.")
                    return true
                }
                addItem(p,  Items.AGILE_LEGS_14648)
                npcl(FaceAnim.OLD_NORMAL, "Here you go, try not to lose them.")
                stage = 22
            }

            21 -> npcl(FaceAnim.OLD_NORMAL, "Bye for now. Come back if you need any assistance.").also { stage = 22 }
            22 -> end()
        }
        return true
    }

    override fun newInstance(player: Player?): Dialogue = GnomeTrainerDialogue(player)

    override fun getIds(): IntArray = intArrayOf(NPCs.GNOME_TRAINER_162)
}