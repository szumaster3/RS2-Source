package content.region.other.braindeath.plugin

import core.api.*
import core.game.dialogue.DialogueFile
import core.game.dialogue.FaceAnim
import core.game.global.action.DoorActionHandler
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.Node
import core.game.node.entity.npc.NPC
import core.game.system.task.Pulse
import core.game.world.map.Location
import shared.consts.NPCs
import shared.consts.Scenery

class BraindeathIslandPlugin : InteractionListener {

    companion object {
        private val zombieNPC = intArrayOf(
            NPCs.ZOMBIE_SWAB_2843,
            NPCs.ZOMBIE_SWAB_2844,
            NPCs.ZOMBIE_SWAB_2845,
            NPCs.ZOMBIE_SWAB_2846,
            NPCs.ZOMBIE_SWAB_2847,
            NPCs.ZOMBIE_SWAB_2848
        )
    }

    override fun defineListeners() {

        /*
         * Handles trying to open the bridge gate.
         */

        on(Scenery.GATE_10172, IntType.SCENERY, "open") { player, node ->
            val lukeNPC = findLocalNPC(player, NPCs.LUKE_50PERCENT_2828)
            if (lukeNPC != null)
                faceLocation(lukeNPC, player.location)

            if (player.location.y > 5098)
                openDialogue(player, LukeFiftyPercentDialogue(node))
             else
                openDialogue(player, LukeDistractionDialogue(node))
            return@on true
        }

        /*
         * Handles talking to Zombie swab NPC.
         */

        on(zombieNPC, IntType.NPC, "talk-to") { player, _ ->
            sendMessage(player, "I don't think he wants to talk to you.")
            return@on true
        }
    }

    inner class LukeFiftyPercentDialogue(val n: Node) : DialogueFile() {
        override fun handle(componentID: Int, buttonID: Int) {
            when (stage) {
                0 -> sendNPCDialogueLines(player!!, NPCs.LUKE_50PERCENT_2828, FaceAnim.OLD_DEFAULT, false, "Hey! What are you doing out there?").also { stage++ }
                1 -> player("Nothing.").also { stage++ }
                2 -> sendNPCDialogueLines(player!!, NPCs.LUKE_50PERCENT_2828, FaceAnim.OLD_DEFAULT, false, "Well Cap'n Donnie said no livin' landlubbers were", "allowed out of the compound.").also { stage++ }
                3 -> sendNPCDialogueLines(player!!, NPCs.LUKE_50PERCENT_2828, FaceAnim.OLD_DEFAULT, false, "So get yerself back in here, or yer for it!").also { stage++ }
                4 -> {
                    end()
                    DoorActionHandler.handleAutowalkDoor(player!!, n.asScenery())
                }
            }
        }
    }

    inner class LukeDistractionDialogue(val n: Node) : DialogueFile() {
        override fun handle(componentID: Int, buttonID: Int) {
            val distractionDialogue = arrayOf(
                "Hey you! Look over there!",
                "Who's that making faces behind you?",
                "Oh my! Is that a genuine 3rd Age Diversion?",
                "Is that your distraction?",
                "Who is that behind you?",
                "That is the most amazing thing I have ever seen!"
            )

            val index = distractionDialogue.random()

            when (stage) {
                0 -> sendNPCDialogueLines(player!!, NPCs.LUKE_50PERCENT_2828, FaceAnim.OLD_DEFAULT, false, "Arr! Tryin' ter get away eh? Well ye'll never sneak", "past me, I'm the best lookout this crew has ever seen!").also { stage++ }
                1 -> playerl(FaceAnim.FRIENDLY, index).also { stage++ }
                2 -> {
                    end()
                    val npc = NPC(NPCs.LUKE_50PERCENT_2828)
                    submitIndividualPulse(player!!, object : Pulse(2) {
                        override fun pulse(): Boolean {
                            faceLocation(npc, Location(2120, 5095, 0))
                            sendChat(npc, "Where?", 1)
                            DoorActionHandler.handleAutowalkDoor(player!!, n.asScenery())
                            return true
                        }
                    })
                }
            }
        }
    }
}