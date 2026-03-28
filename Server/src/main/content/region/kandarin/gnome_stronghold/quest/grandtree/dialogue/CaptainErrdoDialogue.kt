package content.region.kandarin.gnome_stronghold.quest.grandtree.dialogue

import content.global.travel.glider.GnomeGlider
import core.api.getQuestStage
import core.api.openInterface
import core.api.teleport
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.player.Player
import core.game.world.map.Location
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import shared.consts.Components
import shared.consts.NPCs
import shared.consts.Quests

/**
 * Represents the Captain Errdo dialogue.
 *
 * # Relations
 * [The Grand Tree][content.region.kandarin.gnome_stronghold.quest.grandtree.TheGrandTree]
 */
@Initializable
class CaptainErrdoDialogue(player: Player? = null) : Dialogue(player) {

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when (getQuestStage(player!!, Quests.THE_GRAND_TREE)) {
            55 -> {
                when (player!!.location.regionId) {
                    11567 -> {
                        when (stage) {
                            0 -> npcl(FaceAnim.OLD_DEFAULT, "Sorry about that.").also { stage++ }
                            1 -> npcl(FaceAnim.OLD_DEFAULT, "That turbulence over the Karamja Volcano was a bit unexpected, and the area round here isn't well suited for emergency landing.").also { stage++ }
                            2 -> npcl(FaceAnim.OLD_DEFAULT, "Still! we're still alive that's the main thing. Are you okay?").also { stage++ }
                            3 -> playerl(FaceAnim.FRIENDLY, "I'm fine, I can't say the same for your glider!").also { stage++ }
                            4 -> npcl(FaceAnim.OLD_DEFAULT, "I don't think I can fix this. Looks like we'll be heading back by foot. I might see if I can find Penwie while I'm here, I believe he's charting the area.").also { stage++ }
                            5 -> playerl(FaceAnim.FRIENDLY, "Where's the shipyard from here?").also { stage++ }
                            6 -> npcl(FaceAnim.OLD_DEFAULT, "I think I saw some buildings on the coast east of here while we were crashing. I'd have a look there.").also { stage++ }
                            7 -> npcl(FaceAnim.OLD_DEFAULT, "Take care adventurer!").also { stage++ }
                            8 -> playerl(FaceAnim.FRIENDLY, "Take care little man.").also { stage = END_DIALOGUE }
                        }
                    }
                    9782 -> {
                        when (stage) {
                            0 -> npcl(FaceAnim.OLD_DEFAULT, "Hi. The king said that you need to leave?").also { stage++ }
                            1 -> playerl(FaceAnim.FRIENDLY, "Apparently, humans are invading!").also { stage++ }
                            2 -> npcl(FaceAnim.OLD_DEFAULT, "I find that hard to believe. I have lots of human friends.").also { stage++ }
                            3 -> playerl(FaceAnim.FRIENDLY, "I don't understand it either!").also { stage++ }
                            4 -> npcl(FaceAnim.OLD_DEFAULT, "So, where to?").also { stage++ }
                            5 -> options("Take me to Karamja please!", "Not anywhere for now!").also { stage++ }
                            6 -> when (buttonId) {
                                1 -> playerl(FaceAnim.FRIENDLY, "Take me to Karamja, please.").also { stage++ }
                                2 -> playerl(FaceAnim.FRIENDLY, "Not anywhere for now.").also { stage = 10 }
                            }
                            7 -> npcl(FaceAnim.OLD_DEFAULT, "Okay, you're the boss! Hold on tight, it'll be a rough ride.").also { stage++ }
                            8 -> {end(); teleport(player!!, Location.create(2917, 3054, 0))}
                            10 -> npcl(FaceAnim.OLD_DEFAULT, "Okay. I'll be here for when you're ready.").also { stage = END_DIALOGUE }
                        }
                    }
                    else -> {
                        when (stage) {
                            0 -> playerl(FaceAnim.ASKING, "May you fly me somewhere on your glider?").also { stage++ }
                            1 -> npcl(FaceAnim.OLD_DEFAULT, "I only fly friends of the gnomes!").also { stage = END_DIALOGUE }
                        }
                    }
                }
            }

            100 -> {
                when (stage) {
                    0 -> playerl(FaceAnim.ASKING, "May you fly me somewhere on your glider?").also { stage++ }
                    1 -> npcl(FaceAnim.OLD_DEFAULT, "If you wish.").also { stage++ }
                    2 -> {end();openInterface(player, Components.GLIDERMAP_138); GnomeGlider.sendConfig(npc, player)}
                }
            }

            else -> {
                when (stage) {
                    0 -> playerl(FaceAnim.ASKING, "May you fly me somewhere on your glider?").also { stage++ }
                    1 -> npcl(FaceAnim.OLD_DEFAULT, "I only fly friends of the gnomes!").also { stage = END_DIALOGUE }
                }
            }
        }
        return true
    }

    override fun getIds(): IntArray = intArrayOf(NPCs.CAPTAIN_ERRDO_3811)
}
