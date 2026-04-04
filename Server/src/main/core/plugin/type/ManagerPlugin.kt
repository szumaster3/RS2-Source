package core.plugin.type

import core.plugin.Plugin

/**
 * Base class for manager-type plugins.
 */
abstract class ManagerPlugin : Plugin<Any> {

    /**
     * Called once per server tick.
     */
    abstract fun tick()

    /**
     * Handles plugin events.
     */
    override fun fireEvent(identifier: String, vararg args: Any): Any? {
        return null
    }
}