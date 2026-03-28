package core.game.system

import core.ServerConstants
import core.game.system.SystemManager.flag
import core.game.system.SystemManager.terminator
import core.game.system.task.Pulse
import core.game.world.GameWorld.Pulser
import core.game.world.GameWorld.majorUpdateWorker
import core.game.world.repository.Repository.players
import java.util.concurrent.Executors

/**
 * Handles system update tasks, including backup creation and notifying players.
 */
class SystemUpdate : Pulse(DEFAULT_COUNTDOWN) {
    var isCreateBackup = false

    /**
     * Executes a pulse: triggers backup if needed or terminates the system.
     * @return true if the system should stop updating, false otherwise.
     */
    override fun pulse(): Boolean {
        if (delay >= BACKUP_TICK && isCreateBackup) {
            try {
                ServerConstants.DATA_PATH?.let { terminator.save(it) }
            } catch (e: Throwable) {
                e.printStackTrace()
            }
            delay = BACKUP_TICK - 1
            return false
        }
        flag(SystemState.TERMINATED)
        return true
    }

    /**
     * Sends system update notifications to all players.
     */
    private fun notifyPlayers() {
        try {
            val time = delay + if (isCreateBackup) BACKUP_TICK else 0
            for (p in players) {
                p?.packetDispatch?.sendSystemUpdate(time)
            }
        } catch (t: Throwable) {
            t.printStackTrace()
        }
    }

    /**
     * Starts the update scheduling process.
     * Runs on a separate thread if major update worker is not started.
     */
    fun schedule() {
        super.setTicksPassed(0)
        super.start()
        if (majorUpdateWorker.started) {
            notifyPlayers()
            Pulser.submit(this)
            return
        }
        Executors.newSingleThreadExecutor().submit {
            while (isRunning) {
                try {
                    Thread.sleep(600)
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                }
                if (update()) break
            }
        }
    }

    /**
     * Sets countdown ticks for the update, adjusting for backup time if enabled.
     * @param ticks Number of ticks to countdown.
     */
    fun setCountdown(ticks: Int) {
        var adjustedTicks = ticks
        if (isCreateBackup) {
            if (adjustedTicks < BACKUP_TICK) {
                adjustedTicks = BACKUP_TICK
            }
            adjustedTicks -= BACKUP_TICK
        }
        super.setDelay(adjustedTicks)
    }

    /**
     * Cancels the update and sets system state to active.
     */
    fun cancel() {
        super.stop()
        flag(SystemState.ACTIVE)
    }

    companion object {
        const val DEFAULT_COUNTDOWN = 100
        const val BACKUP_TICK = 10
    }
}
