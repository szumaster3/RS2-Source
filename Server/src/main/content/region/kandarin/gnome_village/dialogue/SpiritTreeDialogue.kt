package content.region.kandarin.gnome_village.dialogue

import core.api.*
import core.game.dialogue.DialogueFile
import core.game.dialogue.FaceAnim
import core.game.dialogue.Topic
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.link.diary.DiaryType
import core.game.system.task.Pulse
import core.game.world.map.Location
import core.game.world.update.flag.context.Animation
import core.game.world.update.flag.context.Graphics
import core.tools.END_DIALOGUE
import shared.consts.Animations
import shared.consts.NPCs
import shared.consts.Quests
import shared.consts.Graphics as Gfx

/**
 * Spirit Tree Dialogue (Travel System).
 */
class SpiritTreeDialogue(private val showIntro: Boolean) : DialogueFile() {

    private val LOCATIONS = arrayOf(
        Location(2542, 3170, 0), // Village
        Location(2461, 3444, 0), // Stronghold
        Location(2556, 3259, 0), // Khazard
        Location(3184, 3508, 0), // GE
        Location(3060, 3256, 0), // Port Sarim
        Location(2613, 3856, 0), // Etceteria
        Location(2800, 3203, 0)  // Brimhaven
    )

    override fun handle(componentID: Int, buttonID: Int) {
        npc = NPC(NPCs.SPIRIT_TREE_3636)
        when (stage) {
            0 -> {
                if (!showIntro) {
                    options()
                } else {
                    npcl(FaceAnim.CHILD_NEUTRAL,
                        "If you are a friend of the gnome people, you are a friend of mine. Do you wish to travel?"
                    )
                    stage++
                }
            }
            1 -> options()
            2 -> teleport(LOCATIONS[0])
            3 -> teleport(LOCATIONS[1])
            4 -> teleport(LOCATIONS[2])
            5 -> teleport(LOCATIONS[3])
            6 -> handlePlantedTree()
        }
    }

    private fun getPlantedTree(): String? {
        return when {
            getVarbit(player!!, 720) == 20 -> "Port Sarim"
            getVarbit(player!!, 722) == 20 -> "Etceteria"
            getVarbit(player!!, 724) == 20 -> "Brimhaven"
            else -> null
        }
    }

    private fun options() {
        val planted = getPlantedTree()
        if (planted == null) {
            showTopics(
                Topic("Tree Gnome Village", 2, true),
                Topic("Tree Gnome Stronghold", 3, true),
                Topic("Battlefield of Khazard", 4, true),
                Topic("Grand Exchange", 5, true),
                Topic("Nevermind.", END_DIALOGUE, true)
            )
        } else {
            showTopics(
                Topic("Tree Gnome Village", 2, true),
                Topic("Tree Gnome Stronghold", 3, true),
                Topic("Battlefield of Khazard", 4, true),
                Topic("Grand Exchange", 5, true),
                Topic(planted, 6, true),
            )
        }
    }

    private fun handlePlantedTree() {
        when (getPlantedTree()) {
            "Port Sarim" -> teleport(LOCATIONS[4])
            "Etceteria"  -> teleport(LOCATIONS[5])
            "Brimhaven"  -> teleport(LOCATIONS[6])
            else -> stage = END_DIALOGUE
        }
    }

    private fun teleport(location: Location) {
        player?.let { p ->
            p.lock(8)
            submitWorldPulse(object : Pulse(1, p) {
                var count = 0
                override fun pulse(): Boolean {
                    when (count) {
                        0 -> visualize(p, Animation(Animations.HUMAN_SPIRIT_TREE_TELEPORT_7082),
                            Graphics(Gfx.GRAND_TREE_TP_A_1228))
                        3 -> teleport(p, location)
                        5 -> {
                            visualize(p, Animation(Animations.HUMAN_SPIRIT_TREE_TELEPORT_FROM_7084),
                                Graphics(Gfx.GRAND_TREE_TP_B_1229))
                            p.face(null)
                            if (withinDistance(p, location)) {
                                finishDiaryTask(p, DiaryType.VARROCK, 1, 5)
                            }
                            return true
                        }
                    }
                    count++
                    return false
                }
            })
            end()
        }
    }
}