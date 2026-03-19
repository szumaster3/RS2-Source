package content.region.kandarin.gnome_stronghold.dialogue

import content.data.GameAttributes
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
import shared.consts.Vars

/**
 * Represents the Gunnjorn dialogue.
 */
@Initializable
class GunnjornDialogue(player: Player? = null) : Dialogue(player) {

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        if (getVarbit(player, Vars.VARBIT_QUEST_HORROR_FROM_THE_DEEP_PROGRESS_34) >= 1 && !inInventory(
                player, Items.LIGHTHOUSE_KEY_3848
            )
        ) {
            options(
                "Talk about Horror from the Deep.",
                "Talk about the Agility course.",
                "Talk about the wall after the log balance.",
                "Nothing.",
            ).also {
                stage = 1
            }
        } else {
            options(
                "Talk about the Agility course.",
                "Talk about the wall after the log balance.",
                "Can I talk about rewards?",
                "Nothing.",
            ).also {
                stage = 2
            }
        }
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when (stage) {
            1 -> when (buttonId) {
                1 -> npc(FaceAnim.FRIENDLY, "Haha welcome to my obstacle course. Have fun, but", "remember this isn't a child's playground. People have", "died here.").also { stage = 20 }
                2 -> playerl(FaceAnim.FRIENDLY, "Hey there. What is this place?").also { stage = 3 }
                3 -> playerl(FaceAnim.FRIENDLY, "What's wrong with the wall after the log balance?").also { stage = 6 }
                4 -> playerl(FaceAnim.FRIENDLY, "Nothing.").also { stage = 15 }
            }

            2 -> when (buttonId) {
                1 -> playerl(FaceAnim.FRIENDLY, "Hey there. What is this place?").also { stage = 3 }
                2 -> playerl(FaceAnim.FRIENDLY, "What's wrong with the wall after the log balance?").also { stage = 6 }
                3 -> playerl(FaceAnim.FRIENDLY, "Can I talk about rewards?").also { stage = 16 }
                4 -> playerl(FaceAnim.FRIENDLY, "Nothing.").also { stage = 15 }
            }
            3 -> npc(FaceAnim.FRIENDLY, "Haha welcome to my obstacle course. Have fun, but", "remember this isn't a child's playground. People have", "died here.").also { stage++ }
            4 -> npcl(FaceAnim.FRIENDLY, "This course starts at the ropeswings to the east. When you've done the swing, head across the slippery log to the building. When you've traversed the obstacles inside, you'll come out on the other side.").also { stage++ }
            5 -> npcl(FaceAnim.FRIENDLY, "From there, head across the low walls to finish. If you've done all the obstacles as I've described, and in order, you'll get a lap bonus.").also { stage = END_DIALOGUE }
            6 -> npcl(FaceAnim.FRIENDLY, "The wall after the log balance? Nothing, really. I just put some tough material on it, giving some people something to grip hold of.").also { stage++ }
            7 -> playerl(FaceAnim.FRIENDLY, "Why would you do that?").also { stage++ }
            8 -> npcl(FaceAnim.FRIENDLY, "So people like you can have a tougher route round this course. Me and a mate get together and set up a new challenge that only the truly agile will conquer.").also { stage++ }
            9 -> npcl(FaceAnim.FRIENDLY, "The extra stuff starts at that wall; so, if you think you're up to it, I suggest you scramble up there after the log balance.").also { stage++ }
            10 -> playerl(FaceAnim.FRIENDLY, "Sounds interesting. Anything else I should know?").also { stage++ }
            11 -> npcl(FaceAnim.FRIENDLY, "Nothing, really. Just make sure you complete the other obstacles before ya do. If you finish a full lap, you'll get an increased bonus for doing the tougher route.").also { stage++ }
            12 -> npcl(FaceAnim.FRIENDLY, "If you manage to do 250 laps of this advanced route without a single mistake, I'll let you have a special item.").also { stage++ }
            13 -> npcl(FaceAnim.FRIENDLY, "I'll keep track of your lap tallies, so you can check how you're getting on with me at any time.").also { stage++ }
            14 -> playerl(FaceAnim.FRIENDLY, "That's all I need for now. Bye.").also { stage++ }
            15 -> npcl(FaceAnim.FRIENDLY, "Bye for now. Come back if you need any help.").also { stage = END_DIALOGUE }
            16 -> {
                val p = player ?: return true
                val firstTalk = getAttribute(p, GameAttributes.BARBARIAN_OUTPOST_GUNNJORN_TALK, false)
                val perfectLaps = getAttribute(p, GameAttributes.BARBARIAN_OUTPOST_PERFECT_LAPS, 0)
                val completeLaps = getAttribute(p, GameAttributes.BARBARIAN_OUTPOST_COURSE_REWARD, false)
                val hasAgileTop = hasAnItem(p, Items.AGILE_TOP_14647).container != null

                when {
                    completeLaps && !hasAgileTop -> {
                        if (!firstTalk) {
                            npcl(FaceAnim.FRIENDLY, "Sure, and congratulations, ${p.username}! That took dedication and great dexterity to complete that many laps.")
                            stage = 17
                        } else {
                            npcl(FaceAnim.FRIENDLY, "Of course. How can I help?")
                            stage = 26
                        }
                    }

                    else -> {
                        npcl(FaceAnim.SAD, "There's no reward for you just yet. Your lap count is only $perfectLaps. It's 250 successful laps or no reward.")
                        stage = 28
                    }
                }
            }
            17 -> npcl(FaceAnim.FRIENDLY, "As promised, I'll give you an item you may find useful - an Agile top. You'll find yourself lighter than usual while wearing it.").also { stage++ }
            18 -> npcl(FaceAnim.FRIENDLY, "We barbarians are tough folks, as you know, so it'll even keep you safe if you get drawn into combat.").also { stage++ }
            19 -> {
                end()
                val p = player ?: return true
                if (freeSlots(p) == 0) {
                    npc(FaceAnim.HALF_GUILTY, "Well, I would give you the reward, but apparently you", "don't have any room.")
                    return true
                }
                addItem(p, Items.AGILE_TOP_14647)
                npcl(FaceAnim.HAPPY, "There you go. Enjoy!")
                setAttribute(p, GameAttributes.BARBARIAN_OUTPOST_GUNNJORN_TALK, true)
                stage = 28
            }

            /*
             * Horror from the deep dialogue
             */
            20 -> playerl(FaceAnim.HALF_ASKING, "Hi, are you called Gunnjorn?").also { stage++ }
            21 -> npc(FaceAnim.FRIENDLY, "Why, indeed I am. I own this agility course, it can be", "very dangerous!").also { stage++ }
            22 -> player(FaceAnim.FRIENDLY, "Yeah, that's great. Anyway, I understand you have a", "cousin named Larrissa who gave you a key...?").also { stage++ }
            23 -> npc(FaceAnim.FRIENDLY, "Yes, she did! How did you know of this? She said she", "probably wouldn't need it, but gave it to me for safe", "keeping just in case.").also { stage++ }
            24 -> player(FaceAnim.FRIENDLY, "Well, something has happened at the lighthouse, and she", "has been locked out. I need you to give me her key.").also { stage++ }

            25 -> {
                end()
                val p = player ?: return true
                if (freeSlots(p) == 0) {
                    npc(FaceAnim.HALF_GUILTY, "Well, I would give you the key, but apparently you", "don't have any room.")
                    return true
                }
                addItem(p, Items.LIGHTHOUSE_KEY_3848)
                setQuestStage(p, Quests.HORROR_FROM_THE_DEEP, 5)
                npc(FaceAnim.HAPPY, "Sure. Here you go.")
                stage = 28
            }

            26 -> player("Any chance of another Agile top?").also { stage++ }

            27 -> {
                end()
                val p = player ?: return true
                if (freeSlots(p) == 0) {
                    npc(FaceAnim.HALF_GUILTY, "Well, I would give you the reward, but apparently you", "don't have any room.")
                    return true
                }
                addItem(p, Items.AGILE_TOP_14647)
                npcl(FaceAnim.HAPPY, "Here you go.")
                stage = 28
            }
            28 -> end()
        }
        return true
    }

    override fun newInstance(player: Player?): Dialogue = GunnjornDialogue(player)

    override fun getIds(): IntArray = intArrayOf(NPCs.GUNNJORN_607)
}