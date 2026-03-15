package content.region.karamja.quest.mm.dialogue

import core.api.finishDiaryTask
import core.api.lock
import core.api.openOverlay
import core.api.teleport
import core.game.dialogue.DialogueFile
import core.game.dialogue.FaceAnim
import core.game.node.entity.player.link.diary.DiaryType
import core.game.system.task.Pulse
import core.game.world.GameWorld
import core.game.world.map.Location
import core.tools.END_DIALOGUE
import shared.consts.Components

class MonkeyMinderDialogue : DialogueFile() {

    override fun handle(componentID: Int, buttonID: Int) {
        when (stage) {
            0 -> playerl(FaceAnim.OLD_DEFAULT, "Ook Ook!").also { stage++ }
            1 -> npc(FaceAnim.FRIENDLY, "Why do you monkeys keep trying to scape? Good", "thing I've caught you before you got away, you little", "scoundrel.").also { stage++ }
            2 -> playerl(FaceAnim.OLD_DEFAULT,"Ook!").also { stage++ }
            3 -> npcl(FaceAnim.FRIENDLY, "Let's put you back in you cage where you belong...").also { stage++ }
            4 -> playerl(FaceAnim.OLD_DEFAULT,"Ok!").also { stage++ }
            5 -> npcl(FaceAnim.FRIENDLY, "What??").also { stage++ }
            6 -> playerl(FaceAnim.OLD_DEFAULT,"Err ... Ook?").also { stage++ }
            7 -> npcl(FaceAnim.FRIENDLY, "I must be imagining things ... monkeys can't talk.").also { stage++ }
            8 -> {
                end()
                lock(player!!, 7)
                openOverlay(player!!, Components.FADE_TO_BLACK_120)
                GameWorld.Pulser.submit(
                    object : Pulse(6) {
                        override fun pulse(): Boolean {
                            openOverlay(player!!, Components.FADE_FROM_BLACK_170)
                            teleport(player!!, Location(2605, 3280, 0))
                            finishDiaryTask(player!!, DiaryType.ARDOUGNE, 2, 8)
                            return true
                        }
                    },
                )
            }
        }
    }
}

//package content.region.karamja.quest.mm.dialogue
//
//import core.api.openOverlay
//import core.api.teleport
//import core.game.dialogue.DialogueFile
//import core.game.dialogue.FaceAnim
//import core.game.system.task.Pulse
//import core.game.world.GameWorld
//import core.game.world.map.Location
//import core.tools.END_DIALOGUE
//import shared.consts.Components
//import kotlin.random.Random
//
//class MonkeyMinderDialogue : DialogueFile() {
//
//    override fun handle(componentID: Int, buttonID: Int) {
//        when (stage) {
//            0 -> playerl("Hello there.").also { stage++ }
//            1 -> npcl(FaceAnim.OLD_NEUTRAL, "Hello, sir.").also { stage++ }
//            2 -> playerl("I haven't seen Monkey Minders here before...").also { stage++ }
//            3 -> npcl(FaceAnim.OLD_NEUTRAL, "Yes, you wouldn't have. We have newly been posted here.").also { stage++ }
//            4 -> playerl("But why do we need Monkey Minders?").also { stage++ }
//            5 -> npcl(FaceAnim.OLD_NEUTRAL, "It's entirely necessary: just take a look at the monkeys. They've been multiplying out of control!").also { stage++ }
//            6 -> npcl(FaceAnim.OLD_NEUTRAL, "Every time we turn our backs another one of them tries to break free. It's almost as if they're plotting to escape!").also { stage++ }
//            7 -> playerl("Why have the monkeys been multiplying?").also { stage++ }
//            8 -> npcl(FaceAnim.OLD_NEUTRAL,
//                "We just don't know. Perhaps it's the weather. " +
//                "Perhaps it's something in the food. Perhaps it's just that time of year. " +
//                "Your guess is as good as ours."
//            ).also { stage++ }
//
//            9 -> playerl("What are you going to do about the monkeys?").also { stage++ }
//            10 -> npcl(FaceAnim.OLD_NEUTRAL,
//                "We're going to post enough men here to be able to contain the monkeys in case they manage to escape."
//            ).also { stage++ }
//            11 -> playerl("Is that very likely?").also { stage++ }
//            12 -> npcl(FaceAnim.OLD_NEUTRAL, "Stranger things have happened in Ardougne.").also { stage++ }
//            13 -> playerl("Ook! Ook!").also { stage++ }
//            14 -> npcl(FaceAnim.OLD_NEUTRAL,
//                "Why do you monkeys keep trying to escape? " +
//                "Good thing I've caught you before you got away, you little scoundrel."
//            ).also { stage++ }
//            15 -> playerl("Ook!").also { stage++ }
//            16 -> npcl(FaceAnim.OLD_NEUTRAL, "Let's put you back in your cage where you belong...").also { stage++ }
//            17 -> playerl("Ok!").also { stage++ }
//            18 -> npcl(FaceAnim.OLD_NEUTRAL, "What??").also { stage++ }
//            19 -> playerl("Er... Ook?").also { stage++ }
//            20 -> npcl(FaceAnim.OLD_NEUTRAL, "I must be imagining things... monkeys can't talk.").also { stage++ }
//            21 -> {
//                openOverlay(player!!, Components.FADE_TO_BLACK_120)
//                GameWorld.Pulser.submit(object : Pulse(8) {
//                    override fun pulse(): Boolean {
//                        openOverlay(player!!, Components.FADE_FROM_BLACK_170)
//                        teleport(player!!, Location.create(2603, 3279, 0)) // inside cage
//                        stage = END_DIALOGUE
//                        end()
//                        return true
//                    }
//                })
//            }
//
//            22 -> {
//                val messages = listOf(
//                    "Stop hassling me!",
//                    "Be quiet, you monkey!"
//                )
//                npcl(FaceAnim.OLD_NEUTRAL, messages.random())
//                stage = END_DIALOGUE
//            }
//            23 -> playerl("Ook Ook!").also { stage++ }
//            24 -> npcl(FaceAnim.OLD_NEUTRAL, "I don't know what kind of monkey you think you are, but you're not getting anywhere near my monkeys!").also { stage = END_DIALOGUE }
//
//            // =====================
//            // INSIDE CAGE AS HUMAN
//            // =====================
//            25 -> npcl(FaceAnim.OLD_NEUTRAL, "My word – what are you doing in there?").also { stage++ }
//            26 -> playerl("I ... er ... I don't know! One minute I was asleep, and the next minute I was here surrounded by monkeys!").also { stage++ }
//            27 -> npcl(FaceAnim.OLD_NEUTRAL, "Well, don't worry. We'll have you out of there shortly.").also { stage++ }
//
//            28 -> {
//                openOverlay(player!!, Components.FADE_TO_BLACK_120)
//                GameWorld.Pulser.submit(object : Pulse(8) {
//                    override fun pulse(): Boolean {
//                        openOverlay(player!!, Components.FADE_FROM_BLACK_170)
//                        teleport(player!!, Location.create(2603, 3278, 0)) // outside cage
//                        stage = END_DIALOGUE
//                        end()
//                        return true
//                    }
//                })
//            }
//
//            29 -> playerl("Thank you.").also { stage++ }
//            30 -> npcl(FaceAnim.OLD_NEUTRAL, "No problem.").also { stage = END_DIALOGUE }
//        }
//    }
//}