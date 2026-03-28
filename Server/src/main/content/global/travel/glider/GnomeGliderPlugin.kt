package content.global.travel.glider

import core.api.*
import core.api.utils.PlayerCamera
import core.game.dialogue.DialogueFile
import core.game.dialogue.FaceAnim
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.interaction.InterfaceListener
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.diary.DiaryType
import core.game.system.task.Pulse
import core.game.world.map.Location
import core.net.packet.PacketRepository
import core.net.packet.context.CameraContext
import core.net.packet.out.CameraViewPacket
import core.tools.END_DIALOGUE
import shared.consts.Components
import shared.consts.NPCs
import shared.consts.Quests
import shared.consts.Vars

enum class GnomeGlider(val button: Int, val location: Location, val config: Int, val npc: Int) {
    CRASH_ISLAND(14, Location(2894, 2726, 0), 8, NPCs.CAPTAIN_ERRDO_3811),
    // Jungle
    GANDIUS(15, Location(2972, 2969, 0), 8, NPCs.CAPTAIN_KLEMFOODLE_3812),
    // The Guardian Tree
    TA_QUIR_PRIW(16, Location(2465, 3501, 3), 9, NPCs.CAPTAIN_DALBUR_3809),
    // Big Rocks
    SINDARPOS(17, Location(2848, 3497, 0), 1, NPCs.CAPTAIN_BLEEMADGE_3810),
    // Man City
    LEMANTO_ADRA(18, Location(3321, 3427, 0), 3, NPCs.CAPTAIN_ERRDO_3811),
    // No Grass
    KAR_HEWO(19, Location(3278, 3212, 0), 4, NPCs.CAPTAIN_KLEMFOODLE_3812),
    // Stupid Man Lands
    LEMANTOLLY_UNDRI(20, Location(2544, 2970, 0), 10, NPCs.GNORMADIUM_AVLAFRIM_1800);
    // Stupid Monkey Lands
    // OOKOOKOLLY_UNDRI(-1, Location.create(2390, 9886, 0), 11, NPCs.DAERO_1407);

    companion object {
        fun sendConfig(npc: NPC?, player: Player) {
            npc?.id?.let { id ->
                forNpc(id)?.let { g ->
                    setVarp(player, Vars.VARP_IFACE_GLIDER_CONFIG_153, g.config)
                }
            }
        }
        private fun forNpc(npcId: Int): GnomeGlider? = values().firstOrNull { it.npc == npcId }
        fun forButtonId(id: Int): GnomeGlider? = values().firstOrNull { it.button == id }
    }
}

class GnomeGliderPlugin : InteractionListener, InterfaceListener {

    companion object {
        private val CAPTAIN_NPC_ID = intArrayOf(
            NPCs.CAPTAIN_DALBUR_3809,
            NPCs.CAPTAIN_BLEEMADGE_3810,
            NPCs.CAPTAIN_ERRDO_3811,
            NPCs.CAPTAIN_KLEMFOODLE_3812
        )
    }

    override fun defineListeners() {

        /*
         * Handles glider configs.
         */

        on(CAPTAIN_NPC_ID, IntType.NPC, "glider") { player, _ ->
            if (!isQuestComplete(player, Quests.THE_GRAND_TREE)) {
                sendMessage(player, "You must complete The Grand Tree Quest to access the gnome glider.")
            } else {
                openInterface(player, Components.GLIDERMAP_138)
            }
            return@on true
        }

        /*
         * Handling a conversation with the captain npcs.
         */

        on(CAPTAIN_NPC_ID, IntType.NPC, "talk-to") { player, node ->
            node.asNpc()?.let { npc ->
                openDialogue(player, GnomeCaptainDialogue(), npc)
            }
            return@on true
        }
    }

    override fun defineInterfaceListeners() {
        onOpen(Components.GLIDERMAP_138) { player, _ ->
            setVarp(player, Vars.VARP_IFACE_GLIDER_CONFIG_153, 0)
            return@onOpen true
        }
        on(Components.GLIDERMAP_138) { player, _, _, buttonID, _, _ ->
            GnomeGlider.forButtonId(buttonID)?.let { glider ->
                submitWorldPulse(GliderPulse(1, player, glider))
            }
            return@on true
        }
        onClose(Components.GLIDERMAP_138) { player, _ ->
            unlock(player)
            return@onClose true
        }
    }
}

class GliderPulse(
    delay: Int,
    private val player: Player,
    private val glider: GnomeGlider,
) : Pulse(delay, player) {

    private var count = 0

    init {
        lock(player, 100)
    }

    override fun pulse(): Boolean {
        val crash = glider == GnomeGlider.LEMANTO_ADRA

        when (count) {
            1 -> {
                setVarp(player, Vars.VARP_IFACE_GLIDER_CONFIG_153, glider.config)
                setMinimapState(player, 2)
            }
            2 -> if (crash) {
                PacketRepository.send(
                    CameraViewPacket::class.java,
                    CameraContext(player, CameraContext.CameraType.SHAKE, 4, 4, 1200, 4, 4)
                )
                sendMessage(player, "The glider almost gets blown from its path as it withstands heavy winds.")
            }
            3 -> openOverlay(player, Components.FADE_TO_BLACK_115)
            4 -> {
                unlock(player)
                teleport(player, glider.location)
            }
            5 -> {
                if (crash) {
                    PlayerCamera(player).reset()
                    sendMessage(player, "The glider becomes uncontrollable and crashes down...")
                }
                closeOverlay(player)
                closeInterface(player)
                setMinimapState(player, 0)
                setVarp(player, Vars.VARP_IFACE_GLIDER_CONFIG_153, 0)
                if (!crash && glider == GnomeGlider.GANDIUS) {
                    finishDiaryTask(player, DiaryType.KARAMJA, 1, 11)
                }
                return true
            }
        }
        count++
        return false
    }
}

class GnomeCaptainDialogue : DialogueFile() {
    override fun handle(componentID: Int, buttonID: Int) {
        when(stage) {
            0 -> npcl(FaceAnim.OLD_DEFAULT, "What do you want human?").also { stage++ }
            1 -> playerl(FaceAnim.HALF_GUILTY, "May you fly me somewhere on your glider?").also { stage++ }
            2 -> if (!isQuestComplete(player!!, Quests.THE_GRAND_TREE)) {
                end()
                npcl(FaceAnim.OLD_ANGRY3, "I only fly friends of the gnomes!")
                sendMessage(player!!, "You must complete The Grand Tree Quest to access the gnome glider.")
                stage = END_DIALOGUE
            } else {
                npc(FaceAnim.OLD_DEFAULT, "If you wish.")
                stage++
            }
            3 -> {
                end()
                openInterface(player!!, Components.GLIDERMAP_138)
                stage = END_DIALOGUE
            }
        }
    }
}