package core.net.packet

import core.api.log
import core.game.bots.AIPlayer
import core.net.packet.out.*
import core.tools.Log
import java.io.PrintWriter
import java.io.StringWriter

/**
 * Repository for outgoing packet handlers.
 */
object PacketRepository {

    private val outgoingPackets: Map<Class<out OutgoingPacket<*>>, OutgoingPacket<*>> = buildMap {
        put(LoginPacket::class.java, LoginPacket())
        put(UpdateSceneGraph::class.java, UpdateSceneGraph())
        put(WindowsPane::class.java, WindowsPane())
        put(Interface::class.java, Interface())
        put(SkillLevel::class.java, SkillLevel())
        put(Config::class.java, Config())
        put(AccessMask::class.java, AccessMask())
        put(GameMessage::class.java, GameMessage())
        put(RunScriptPacket::class.java, RunScriptPacket())
        put(RunEnergy::class.java, RunEnergy())
        put(ContainerPacket::class.java, ContainerPacket())
        put(StringPacket::class.java, StringPacket())
        put(Logout::class.java, Logout())
        put(CloseInterface::class.java, CloseInterface())
        put(AnimateInterface::class.java, AnimateInterface())
        put(DisplayModel::class.java, DisplayModel())
        put(InterfaceConfig::class.java, InterfaceConfig())
        put(PingPacket::class.java, PingPacket())
        put(UpdateAreaPosition::class.java, UpdateAreaPosition())
        put(ConstructScenery::class.java, ConstructScenery())
        put(ClearScenery::class.java, ClearScenery())
        put(HintIcon::class.java, HintIcon())
        put(ClearMinimapFlag::class.java, ClearMinimapFlag())
        put(InteractionOption::class.java, InteractionOption())
        put(SetWalkOption::class.java, SetWalkOption())
        put(MinimapState::class.java, MinimapState())
        put(ConstructGroundItem::class.java, ConstructGroundItem())
        put(ClearGroundItem::class.java, ClearGroundItem())
        put(RepositionChild::class.java, RepositionChild())
        put(PositionedGraphic::class.java, PositionedGraphic())
        put(SystemUpdatePacket::class.java, SystemUpdatePacket())
        put(CameraViewPacket::class.java, CameraViewPacket())
        put(MusicPacket::class.java, MusicPacket())
        put(AudioPacket::class.java, AudioPacket())
        put(GrandExchangePacket::class.java, GrandExchangePacket())
        put(BuildDynamicScene::class.java, BuildDynamicScene())
        put(AnimateObjectPacket::class.java, AnimateObjectPacket())
        put(ClearRegionChunk::class.java, ClearRegionChunk())
        put(ContactPackets::class.java, ContactPackets())
        put(CommunicationMessage::class.java, CommunicationMessage())
        put(UpdateClanChat::class.java, UpdateClanChat())
        put(UpdateGroundItemAmount::class.java, UpdateGroundItemAmount())
        put(UpdateRandomFile::class.java, UpdateRandomFile())
        put(InstancedLocationUpdate::class.java, InstancedLocationUpdate())
        put(CSConfigPacket::class.java, CSConfigPacket())
        put(Varbit::class.java, Varbit())
        put(ResetInterface::class.java, ResetInterface())
        put(VarcUpdate::class.java, VarcUpdate())
        put(InterfaceSetAngle::class.java, InterfaceSetAngle())
        put(LastLoginInfo::class.java, LastLoginInfo())
    }

    /**
     * Sends a packet to the player defined in the context.
     */
    @JvmStatic
    fun send(clazz: Class<out OutgoingPacket<*>>, context: Context) {
        val player = context.player

        if (player is AIPlayer || player.session == null || player.isArtificial) {
            return
        }

        val packet = outgoingPackets[clazz]
        if (packet == null) {
            log(PacketRepository::class.java, Log.ERR, "Invalid outgoing packet [handler=$clazz, context=$context].")
            return
        }

        try {
            @Suppress("UNCHECKED_CAST")
            PacketWriteQueue.handle(packet as OutgoingPacket<Context>, context)
        } catch (e: Exception) {
            val stackTrace = StringWriter().also { e.printStackTrace(PrintWriter(it)) }
            log(PacketRepository::class.java, Log.ERR, "Error writing packet: $stackTrace")
        }
    }
}