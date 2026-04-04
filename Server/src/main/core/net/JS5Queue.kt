package core.net

import java.util.concurrent.LinkedBlockingDeque
import java.util.concurrent.atomic.AtomicBoolean

/**
 * Handles the JS5 queue for a session.
 *
 * Converted and modernized (original by Techdaan).
 */
class JS5Queue(
    val session: IoSession
) {

    fun queue(container: Int, archive: Int, highPriority: Boolean) {
        val request = Js5Request(this, container, archive, highPriority)
        handler.requests.put(request)
    }

    companion object {
        val RUNNING = AtomicBoolean(true)
        private val handler = Js5QueueHandler()

        init {
            try {
                handler.start()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private class Js5QueueHandler : Thread("JS5QueueHandler") {
        val requests = LinkedBlockingDeque<Js5Request>()

        override fun run() {
            while (RUNNING.get()) {
                try {
                    val request = requests.take()
                    val queue = request.queue

                    if (queue.session.isActive) {
                        queue.session.write(
                            intArrayOf(
                                request.index,
                                request.archive,
                                if (request.priority) 1 else 0
                            )
                        )
                    }
                } catch (e: InterruptedException) {
                    if (!RUNNING.get()) break
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }

    private data class Js5Request(
        val queue: JS5Queue,
        val index: Int,
        val archive: Int,
        val priority: Boolean
    )
}