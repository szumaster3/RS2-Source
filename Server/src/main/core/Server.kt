package core

import core.api.log
import core.game.system.SystemManager
import core.game.system.SystemState
import core.game.system.config.ServerConfigParser
import core.game.world.GameWorld
import core.net.NioReactor
import core.net.amsc.WorldCommunicator
import core.tools.Log
import core.tools.NetworkReachability
import core.tools.TimeStamp
import kotlinx.coroutines.*
import java.io.File
import java.io.FileWriter
import java.lang.management.ManagementFactory
import java.lang.management.ThreadMXBean
import java.net.BindException
import java.net.URL
import java.util.*
import kotlin.math.max
import kotlin.system.exitProcess

/**
 * The main server object responsible for initializing and managing the game server.
 * @author Emperor, Ceikry
 */
object Server {
    /**
     * The server's start time in milliseconds.
     */
    @JvmField
    var startTime: Long = 0

    /**
     * Timestamp of the last server heartbeat.
     */
    private var lastHeartbeat = System.currentTimeMillis()

    /**
     * Indicates whether the server is running.
     */
    @JvmStatic
    var running = false

    /**
     * The server's networking reactor.
     */
    @JvmStatic
    var reactor: NioReactor? = null

    /**
     * Current network reachability status.
     */
    var networkReachability = NetworkReachability.REACHABLE

    /**
     * Main entry point for the server.
     */
    @Throws(Throwable::class)
    @JvmStatic
    fun main(args: Array<String>) {
        if (args.isNotEmpty()) {
            log(this::class.java, Log.INFO, "Using config file: [${args[0]}]")
            ServerConfigParser.parse(args[0])
        } else {
            log(this::class.java, Log.INFO, "Using config file: ${"worldprops" + File.separator + "default.conf"}")
            ServerConfigParser.parse("worldprops" + File.separator + "default.conf")
        }
        startTime = System.currentTimeMillis()
        val t = TimeStamp()
        GameWorld.prompt(true)
        Runtime.getRuntime().addShutdownHook(ServerConstants.SHUTDOWN_HOOK)
        log(this::class.java, Log.INFO, "Starting networking...")
        try {
            reactor = NioReactor.configure(43594 + GameWorld.settings?.worldId!!)
            reactor!!.start()
        } catch (e: BindException) {
            log(this::class.java, Log.ERR, "Port " + (43594 + GameWorld.settings?.worldId!!) + " is already in use!")
            throw e
        }

        // Ammunition.main()
        // AutoStock.autostock()
        // WorldCommunicator.connect()

        log(this::class.java, Log.INFO, "${GameWorld.settings?.name} flags ${GameWorld.settings?.toString()}")
        log(this::class.java, Log.INFO, "${GameWorld.settings?.name} started in ${t.duration(false, "")} milliseconds.")

        val scanner = Scanner(System.`in`)
        running = true

        GlobalScope.launch {
            while (scanner.hasNextLine()) {
                val command = scanner.nextLine()
                when (command) {
                    "stop" -> exitProcess(0)
                    "update" -> SystemManager.flag(SystemState.UPDATING)
                    "help", "commands" -> printCommands()
                    "restartworker" -> SystemManager.flag(SystemState.ACTIVE)
                }
            }
        }

        if (ServerConstants.WATCHDOG_ENABLED) {
            GlobalScope.launch {
                delay(20000)
                while (running) {
                    val timeStart = System.currentTimeMillis()
                    networkReachability = if (!checkConnectivity())
                        NetworkReachability.UNREACHABLE
                    else
                        NetworkReachability.REACHABLE
                    if (System.currentTimeMillis() - lastHeartbeat > 7200 && running) {
                        log(this::class.java, Log.ERR, "Triggering reboot due to heartbeat timeout")
                        log(this::class.java, Log.ERR, "Creating thread dump...")
                        val dump = threadDump(true, true)

                        withContext(Dispatchers.IO) {
                            FileWriter("latest_dump.txt").use {

                                if (dump != null) {
                                    it.write(dump)
                                }

                                it.flush()
                                it.close()
                            }
                        }

                        if (!SystemManager.isTerminated)
                            exitProcess(0)
                    }
                    val timeNow = System.currentTimeMillis()
                    delay(max(0L, 625 - (timeNow - timeStart)))
                }
            }
        }
    }

    /**
     * Checks if the server has internet connectivity by pinging predefined URLs.
     *
     * @return `true` if the server is online, `false` otherwise.
     */
    private fun checkConnectivity(): Boolean {
        val urls = ServerConstants.CONNECTIVITY_CHECK_URL.split(",")
        var timeout = ServerConstants.CONNECTIVITY_TIMEOUT
        if (timeout * urls.size > 5000)
            timeout = 5000 / urls.size
        for (targetUrl in urls) {
            try {
                val url = URL(targetUrl)
                val conn = url.openConnection()
                conn.connectTimeout = timeout
                conn.connect()
                conn.getInputStream().close()
                return true
            } catch (e: Exception) {
                log(this::class.java, Log.WARN, "${targetUrl} failed to respond. Are we offline?")
                continue
            }
        }
        return false
    }

    /**
     * Updates the last heartbeat timestamp.
     */
    @JvmStatic
    fun heartbeat() {
        lastHeartbeat = System.currentTimeMillis()
    }

    /**
     * Prints available server commands.
     */
    private fun printCommands() {
        println("stop - stop the server (saves all accounts and such)")
        println("players - show online player count")
        println("update - initiate an update with a countdown visible to players")
        println("help, commands - show this")
        println("restartworker - Reboots the major update worker in case of a travesty.")
    }

    /**
     * Handles automatic reconnection logic (currently unimplemented).
     */
    fun autoReconnect() {
        WorldCommunicator.connect()
    }

    /**
     * Returns the server's start time.
     */
    fun getStartTime(): Long = startTime

    /**
     * Generates a thread dump.
     *
     * @param lockedMonitors Whether to include locked monitors.
     * @param lockedSynchronizers Whether to include locked synchronizers.
     * @return The thread dump as a string.
     */
    private fun threadDump(lockedMonitors: Boolean, lockedSynchronizers: Boolean): String? {
        val threadMXBean: ThreadMXBean = ManagementFactory.getThreadMXBean()
        return threadMXBean.dumpAllThreads(lockedMonitors, lockedSynchronizers).joinToString(System.lineSeparator())
    }

    /**
     * Sets the server's start time.
     */
    fun setStartTime(startTime: Long) {
        Server.startTime = startTime
    }
}