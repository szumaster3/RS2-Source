package content.region.misthalin.lumbridge.dialogue

import core.api.*
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.diary.DiaryType
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import shared.consts.Items
import shared.consts.NPCs

/**
 * Represents the Sasquine Huburns dialogue.
 */
@Initializable
class SasquineHuburnsDialogue(player: Player? = null) : Dialogue(player) {

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        if (getAttribute(player, "gnomecopter:unlocked", false)) {
            player("Can I go for a ride on the gnomecopters now, please?").also { stage = 2 }
        } else if (player.achievementDiaryManager.hasCompletedTask(DiaryType.LUMBRIDGE, 2, 1)) {
            player("I wanna ride the gnomecopters again!").also { stage = 15 }
        } else {
            npc(FaceAnim.OLD_HAPPY, "Hiya. I bet you've never seen anything like", "Gnomecopter Tours before!")
        }
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when (stage) {
            0 -> player("What's Gnomecopter Tours?").also { stage++ }
            1 -> npc(FaceAnim.OLD_HAPPY, "Oh, haven't you spoken to Hieronymus?", "He's over by the table. Go and ask him.").also { stage = END_DIALOGUE }
            2 -> npc(FaceAnim.OLD_HAPPY, "Sure thing. Since you're a first-time filer,", "I'll just run you through a few points.").also { stage++ }
            3 -> npc(FaceAnim.OLD_HAPPY, "Firstly, you're not allowed to get off. When you've had", "enough, click the ${core.tools.DARK_RED}Return to Lumbridge</col> button on your", "controls, and you'll be brought back here.").also { stage++ }
            4 -> npc(FaceAnim.OLD_HAPPY, "Secondly, the gnomecopter's autopilot will fly you slowly", "around the areas. You can use the button on your", "controls to pilot the gnomecopter yourself.").also { stage++ }
            5 -> npc(FaceAnim.OLD_HAPPY, "Thirdly, I'll be giving you an introduction to whichever", "area you visit. You can use the ${core.tools.DARK_RED}Next page</col> and", "${core.tools.DARK_RED}Previous page</col> buttons on your controls to read it at", "your own pace.").also { stage++ }
            6 -> npc(FaceAnim.OLD_HAPPY, "Fourthly, we've put a few signposts in the areas", "you'll be visiting. They'll tell you more about what", "you're seeing. Turn off the autopilot feature if you", "have trouble reading them.").also { stage++ }
            7 -> npc(FaceAnim.OLD_HAPPY, "Fifthly, whatever happens, you MUST NOT touch the").also { stage++ }
            8 -> npc(FaceAnim.OLD_HAPPY, "Oh, forget it, you'll be fine.").also { stage++ }
            9 -> player("Don't touch what? What's going to happen to me if I", "touch it? Are these things safe?").also { stage++ }
            10 -> npc(FaceAnim.OLD_LAUGH1, "Hee hee! People fall for that every time.").also { stage++ }
            11 -> player("I bet I could pick you and hammer your chin into", "the ground if I tried.").also { stage++ }
            12 -> npc(FaceAnim.OLD_HAPPY, "Oh you humans have no sense of humour.", "Anyway, where would you like to go first?").also { stage++ }
            13 -> {
                setTitle(player, 5)
                sendOptions(player, "Pick a destination:", "Lletya", "Castle Wars", "Trollweiss and Rellekka Hunter area", "- NEXT -", "Nowhere.").also { stage++ }
            }
            14 -> when (buttonId) {
                1 -> end().also { sendMessage(player, "Not implemented.") }
                2 -> npc(FaceAnim.OLD_HAPPY, "Take this ticket. It'll tell the gnomecopter to take you to", "Castle Wars. Just sit on a gnomecopter and off you", "go!").also { stage = 16 }
                3 -> end().also { sendMessage(player, "Not implemented.") }
                4 -> {
                    setTitle(player, 5)
                    sendOptions(player, "Pick a destination:", "Pest Control", "Burthorpe Games Room", "Burgh de Rott", "- PREVIOUS -", "Nowhere.").also { stage = 17 }
                }
                5 -> end().also { sendMessage(player, "Not implemented.") }
            }
            15 -> npc(FaceAnim.OLD_NORMAL, "That's the spirit!", "Where would you like to go this time?").also { stage = 13 }
            16 -> {
                end()
                sendItemDialogue(player, Items.GNOMECOPTER_TICKET_12843, "Sasquine Huburns gives you a gnomecopter ticket.")
                addItem(player, Items.GNOMECOPTER_TICKET_12843)
            }
            17 -> when (buttonId) {
                1 -> end().also { sendMessage(player, "Not implemented.") }
                2 -> end().also { sendMessage(player, "Not implemented.") }
                3 -> end().also { sendMessage(player, "Not implemented.") }
                4 -> {
                    setTitle(player, 5)
                    sendOptions(player, "Pick a destination:", "Lletya", "Castle Wars", "Trollweiss and Rellekka Hunter area", "- NEXT -", "Nowhere.").also { stage = 14 }
                }

                5 -> end().also { sendMessage(player, "Not implemented.") }
            }
        }
        return true
    }

    override fun getIds(): IntArray = intArrayOf(NPCs.SASQUINE_HUBURNS_7421)
}
