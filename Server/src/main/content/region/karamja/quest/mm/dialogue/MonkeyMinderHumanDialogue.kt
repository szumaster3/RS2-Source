package content.region.karamja.quest.mm.dialogue

import core.api.lock
import core.api.openOverlay
import core.api.teleport
import core.game.dialogue.DialogueFile
import core.game.dialogue.FaceAnim
import core.game.system.task.Pulse
import core.game.world.GameWorld
import core.game.world.map.Location
import shared.consts.Components

class MonkeyMinderHumanDialogue : DialogueFile() {
    override fun handle(componentID: Int, buttonID: Int) {
        when (stage) {
            0 -> npcl(FaceAnim.HALF_THINKING,"My word - what are you doing in there?").also { stage++ }
            1 -> player(FaceAnim.NEUTRAL, "I ... er ... I don't know! One minute I was asleep, and", "the next minute I was here surrounded by monkeys!").also { stage++ }
            2 -> npcl(FaceAnim.FRIENDLY,"Well, don't worry. We'll have you out of there shortly.").also { stage++ }
            3 -> {
                lock(player!!, 7)
                openOverlay(player!!, Components.FADE_TO_BLACK_120)
                GameWorld.Pulser.submit(
                    object : Pulse(6) {
                        override fun pulse(): Boolean {
                            openOverlay(player!!, Components.FADE_FROM_BLACK_170)
                            teleport(player!!, Location(2608, 3280, 0))
                            return true
                        }
                    },
                ).also { stage++ }
            }
            4 -> playerl(FaceAnim.HAPPY, "Thank you.").also { stage++ }
            5 -> npcl(FaceAnim.FRIENDLY,"No problem.").also { stage++ }
            6 -> end()
        }
    }
}
