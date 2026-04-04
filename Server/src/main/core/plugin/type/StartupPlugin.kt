package core.plugin.type

import core.plugin.Plugin

/**
 * Plugin executed during server startup sequence.
 */
abstract class StartupPlugin : Plugin<Any?> {

    /**
     * Executes startup logic.
     */
    abstract fun run()
}