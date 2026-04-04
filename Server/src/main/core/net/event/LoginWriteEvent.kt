package core.net.event

import core.auth.AuthResponse
import core.net.EventProducer
import core.net.IoSession
import core.net.IoWriteEvent
import core.net.producer.GameEventProducer
import java.nio.ByteBuffer

/**
 * Handles login writing events.
 *
 * @author Emperor.
 */
class LoginWriteEvent(
    session: IoSession,
    context: Any
) : IoWriteEvent(session, context) {

    override fun write(session: IoSession, context: Any) {
        val response = context as AuthResponse
        val buffer = ByteBuffer.allocate(500)

        buffer.put(response.ordinal.toByte())

        when (response.ordinal) {
            2 -> { // Successful login
                buffer.put(worldResponse(session))
                session.producer = GAME_PRODUCER
            }

            21 -> { // Moving world
                buffer.put(session.serverKey.toByte())
            }
        }

        buffer.flip()
        session.queue(buffer)
    }

    companion object {
        private val GAME_PRODUCER: EventProducer = GameEventProducer()

        /**
         * Builds the world response for a successful login.
         */
        private fun worldResponse(session: IoSession): ByteBuffer {
            val player = session.player
            return ByteBuffer.allocate(150).apply {
                put(player.details.rights.ordinal.toByte())
                repeat(3) { put(0) }
                put(1)
                put(0)
                put(0)
                putShort(player.index.toShort())
                put(1) // Enable all G.E boxes.
                put(1)
                flip()
            }
        }
    }
}