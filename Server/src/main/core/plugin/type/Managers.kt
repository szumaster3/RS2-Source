package core.plugin.type

/**
 * Holds and updates all registered [ManagerPlugin] instances.
 */
object Managers {

    /**
     * Registered manager plugins.
     */
    private val plugins: MutableList<ManagerPlugin> = ArrayList(20)

    /**
     * Registers a manager plugin.
     *
     * @param plugin The plugin to register.
     */
    fun register(plugin: ManagerPlugin?) {
        if (plugin != null) {
            plugins.add(plugin)
        }
    }

    /**
     * Ticks all registered manager plugins.
     */
    fun tick() {
        for (p in plugins) {
            p.tick()
        }
    }
}