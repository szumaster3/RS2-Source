package core.game.system

import core.Server
import core.ServerConstants
import core.ServerStore
import core.api.log
import core.game.bots.AIRepository.Companion.clearAllBots
import core.game.node.entity.player.info.PlayerMonitor.flushRemainingEventsImmediately
import core.game.world.GameWorld.majorUpdateWorker
import core.game.world.GameWorld.shutdownListeners
import core.game.world.GameWorld.worldPersists
import core.game.world.repository.Repository.disconnectionQueue
import core.game.world.repository.Repository.players
import core.tools.Log
import java.io.File

/**
 * Manages server termination and data saving.
 */
class SystemTermination {
    /**
     * Runs the termination sequence: stops networking, bots, saves players and world data.
     */
    fun terminate() {
        log(javaClass, Log.INFO, "Initializing termination sequence - do not shutdown!")
        try {
            log(javaClass, Log.INFO, "Shutting down networking...")
            Server.running = false
            log(javaClass, Log.INFO, "Stopping all bots...")
            clearAllBots()
            Server.reactor?.terminate()
            log(javaClass, Log.INFO, "Stopping all pulses...")
            majorUpdateWorker.stop()
            // Save players
            for (p in players) {
                try {
                    if (p != null && !p.isArtificial) {
                        p.details.save()
                        p.clear()
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
            // Shutdown listeners
            shutdownListeners.forEach { it.shutdown() }
            flushRemainingEventsImmediately()
            // Save world data
            var serverStore: ServerStore? = null
            for (wld in worldPersists) {
                if (wld is ServerStore) {
                    serverStore = wld
                } else {
                    wld.save()
                }
            }
            serverStore?.save()
            // Save to disk
            ServerConstants.DATA_PATH?.let { save(it) }
        } catch (e: Throwable) {
            e.printStackTrace()
        }
        log(javaClass, Log.INFO, "Server successfully terminated!")
    }

    /**
     * Saves data to the specified directory and processes disconnection queue.
     * @param directory Path to save data.
     */
    fun save(directory: String) {
        val file = File(directory)
        log(javaClass, Log.INFO, "Saving data [dir=${file.absolutePath}]...")

        if (!file.isDirectory) {
            file.mkdirs()
        }
        Server.reactor?.terminate()
        val start = System.currentTimeMillis()
        while (!disconnectionQueue.isEmpty() &&
            System.currentTimeMillis() - start < 5000L)
        {
            disconnectionQueue.update()
            try {
                Thread.sleep(100)
            } catch (_: Exception) {
            }
        }
        disconnectionQueue.update()
        disconnectionQueue.clear()
    }
}