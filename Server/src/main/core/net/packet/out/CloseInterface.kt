package core.net.packet.out

import core.net.packet.IoBuffer
import core.net.packet.OutgoingPacket
import core.net.packet.context.InterfaceContext
import shared.consts.Network

/**
 * Represents the outgoing packet used for closing an interface.
 * @author Emperor
 */
class CloseInterface : OutgoingPacket<InterfaceContext> {
    override fun send(context: InterfaceContext) {
        val buffer = IoBuffer(Network.CLOSE_INTERFACE)
        buffer.putShort(context.player.interfaceManager.getPacketCount(1))
        buffer.putShort(context.windowId)
        buffer.putShort(context.componentId)
        buffer.cypherOpcode(context.player.session.isaacPair.output)
        context.player.session.write(buffer)
    }
}