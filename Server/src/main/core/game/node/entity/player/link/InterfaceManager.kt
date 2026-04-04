package core.game.node.entity.player.link

import content.data.GameAttributes
import content.region.other.tutorial_island.plugin.TutorialStage
import content.region.other.tutorial_island.plugin.TutorialStage.hideTabs
import core.api.isQuestComplete
import core.api.log
import core.api.setVarp
import core.game.component.CloseEvent
import core.game.component.Component
import core.game.component.InterfaceType
import core.game.event.InterfaceCloseEvent
import core.game.event.InterfaceOpenEvent
import core.game.node.entity.combat.equipment.WeaponInterface
import core.game.node.entity.player.Player
import core.game.world.GameWorld.settings
import core.net.packet.PacketRepository
import core.net.packet.context.InterfaceContext
import core.net.packet.context.WindowsPaneContext
import core.net.packet.out.CloseInterface
import core.net.packet.out.Interface
import core.net.packet.out.WindowsPane
import core.tools.Log
import shared.consts.Components
import shared.consts.Quests

/**
 * Manages interface components for a player.
 *
 * @property player The player associated with this interface manager.
 */
class InterfaceManager(
    /**
     * The player associated with this interface manager.
     */
    val player: Player
) {
    private var packetCount = 0

    /**
     * The root windows pane interface currently open.
     */
    var windowsPane: Component? = null
        private set

    /**
     * The currently opened component, if any.
     */
    var opened: Component? = null

    /**
     * The set of tab interfaces.
     */
    @JvmField
    var tabs: Array<Component?> = arrayOfNulls(15)

    /**
     * The chatbox interface component.
     */
    @JvmField
    var chatbox: Component? = null


    /**
     * The single tab interface component.
     */
    var singleTab: Component? = null
        private set

    /**
     * The overlay interface component.
     */
    @JvmField
    var overlay: Component? = null

    private val wildyOverlay: Component? = null

    /**
     * The index of the current active tab.
     */
    @JvmField
    var currentTabIndex: Int = 3

    /**
     * Opens the specified windows pane interface.
     *
     * @param windowsPane The component to be set as the window pane.
     * @param overlap Whether to use overlap mode (default is false).
     * @return The component that was opened as the window pane.
     */
    @JvmOverloads
    fun openWindowsPane(windowsPane: Component, overlap: Boolean = false): Component {
        this.windowsPane = windowsPane
        if (windowsPane.definition!!.type != InterfaceType.WINDOW_PANE) {
            log(
                this.javaClass,
                Log.WARN,
                "Set interface type to WINDOW_PANE for component " + windowsPane.id + ", definition requires updating!"
            )
            windowsPane.definition.type = InterfaceType.WINDOW_PANE
        }

        PacketRepository.send(
            WindowsPane::class.java,
            WindowsPaneContext(player, windowsPane.id, if (overlap) 1 else 0)
        )
        windowsPane.open(player)

        if (opened != null) player.dispatch(InterfaceOpenEvent(opened!!))

        return windowsPane
    }

    /**
     * Opens the specified windows pane with a custom display type.
     *
     * @param windowsPane The component to be opened.
     * @param type The display type (e.g., resize mode or fixed).
     */
    fun openWindowsPane(windowsPane: Component, type: Int) {
        this.windowsPane = windowsPane
        if (windowsPane.definition!!.type != InterfaceType.WINDOW_PANE) {
            log(
                this.javaClass,
                Log.WARN,
                "Set interface type to WINDOW_PANE for component " + windowsPane.id + ", definition requires updating!"
            )
            windowsPane.definition.type = InterfaceType.SINGLE_TAB
        }

        PacketRepository.send(WindowsPane::class.java, WindowsPaneContext(player, windowsPane.id, type))
        windowsPane.open(player)

        if (opened != null) player.dispatch(InterfaceOpenEvent(opened!!))
    }

    /**
     * Opens a component using its ID.
     *
     * @param componentId The ID of the component to open.
     * @return The opened component or null if the open failed.
     */
    fun openComponent(componentId: Int): Component? {
        return open(Component(componentId))
    }

    /**
     * Opens the given component, closing any existing one.
     *
     * @param component The component to open.
     * @return The opened component, or null if the previous one couldn't be closed.
     */
    fun open(component: Component): Component? {
        if (!close()) {
            return null
        }
        component.open(player)

        opened = component
        player.dispatch(InterfaceOpenEvent(opened!!))

        return opened
    }

    /**
     * Checks if any component is currently opened.
     *
     * @return True if a component is open, false otherwise.
     */
    fun isOpened(): Boolean {
        return opened != null
    }

    /**
     * Checks if a valid chatbox component is active.
     *
     * @return True if chatbox is non-null and not the default, false otherwise.
     */
    fun hasChatbox(): Boolean {
        return chatbox != null && chatbox!!.id != DEFAULT_CHATBOX
    }

    /**
     * Closes the currently opened component and overlay if applicable.
     *
     * @return True if successfully closed or nothing was open, false if closure failed.
     */
    fun close(): Boolean {
        val wasOpened = opened

        // Component 333 is an immediate(no-fading) full-screen HD-mode black screen which auto-clears when interrupted.
        if (overlay != null && overlay!!.id == Components.BLACK_OVERLAY_333) {
            closeOverlay()
        }

        if (opened != null && opened!!.close(player)) {
            if (!opened!!.definition!!.isWalkable || opened!!.id == 14) {
                PacketRepository.send(
                    CloseInterface::class.java,
                    InterfaceContext(
                        player,
                        opened!!.definition!!.getWindowPaneId(isResizable),
                        opened!!.definition!!.getChildId(isResizable),
                        opened!!.id,
                        opened!!.definition!!.isWalkable
                    )
                )
                player.dispatch(InterfaceCloseEvent(opened!!))
            }
            opened = null
        }

        if (wasOpened != null && opened == null) {
            val tutorialComplete = player.getAttribute(GameAttributes.TUTORIAL_COMPLETE, false)
            val tutorialStage = player.getAttribute(GameAttributes.TUTORIAL_STAGE, -1)
            if (!tutorialComplete && tutorialStage < 72) {
                if (!player.isArtificial) {
                    TutorialStage.load(player, tutorialStage, false)
                }
            } else {
                if (player.getAttribute<Any?>("runscript", null) != null) {
                    player.removeAttribute("runscript")
                    player.packetDispatch.sendRunScript(101, "")
                }
            }
        }

        return wasOpened == null || opened == null
    }

    /**
     * Indicates whether the currently opened interface is walkable.
     *
     * @return True if no component is open or the current one is walkable, false otherwise.
     */
    val isWalkable: Boolean
        get() {
            if (opened != null) {
                if (opened!!.id == Components.EXCHANGE_SEARCH_389) {
                    return false
                }
                if (opened!!.definition!!.isWalkable) {
                    return true
                }
            }
            return true
        }

    /**
     * Closes the specified component.
     *
     * @param component The component to close.
     * @return True if successfully closed, false otherwise.
     */
    fun close(component: Component): Boolean {
        if (component.close(player)) {
            if (component.id == DEFAULT_CHATBOX) {
                return true
            }
            if (component.definition!!.type == InterfaceType.TAB) {
                PacketRepository.send(
                    CloseInterface::class.java, InterfaceContext(
                        player,
                        component.definition.getWindowPaneId(
                            isResizable
                        ),
                        component.definition.getChildId(isResizable) + component.definition.tabIndex,
                        component.id,
                        component.definition.isWalkable
                    )
                )
                return true
            }
            PacketRepository.send(
                CloseInterface::class.java, InterfaceContext(
                    player, component.definition.getWindowPaneId(
                        isResizable
                    ), component.definition.getChildId(isResizable), component.id, component.definition.isWalkable
                )
            )
            return true
        }
        return false
    }

    /**
     * Closes the current chatbox and reopens the default one.
     */
    fun closeChatbox() {
        if (chatbox != null && chatbox!!.id != DEFAULT_CHATBOX) {
            if (close(chatbox!!)) {
                openChatbox(DEFAULT_CHATBOX)
                player.packetDispatch.sendRunScript(101, "")
            }
        }
    }

    /**
     * Opens a single-tab component.
     *
     * @param component The component to open.
     * @return The opened component.
     */
    fun openSingleTab(component: Component): Component {
        if (component.definition!!.type != InterfaceType.SINGLE_TAB) {
            log(
                this.javaClass,
                Log.WARN,
                "Set interface type to SINGLE_TAB for component " + component.id + ", definition requires updating!"
            )
            component.definition.type = InterfaceType.SINGLE_TAB
        }
        component.open(player)
        if (component.closeEvent == null) {
            component.closeEvent = CloseEvent { player: Player?, c: Component? -> true }
        }
        return component.also { singleTab = it }
    }

    /**
     * Closes the currently opened single-tab component.
     *
     * @return Always returns true.
     */
    fun closeSingleTab(): Boolean {
        if (singleTab != null && close(singleTab!!)) {
            singleTab = null
        }
        return true
    }

    /**
     * Removes the specified tabs.
     *
     * @param tabs The indices of the tabs to remove.
     */
    fun removeTabs(vararg tabs: Int) {
        var changeViewedTab = false
        for (slot in tabs) {
            if (slot == currentTabIndex) {
                changeViewedTab = true
            }
            val tab = this.tabs[slot]
            if (tab != null) {
                close(tab)
                this.tabs[slot] = null
            }
        }
        if (changeViewedTab) {
            var currentIndex = -1
            if (this.tabs[3] == null) {
                for (i in this.tabs.indices) {
                    if (this.tabs[i] != null) {
                        currentIndex = i
                        break
                    }
                }
            } else {
                currentIndex = 3
            }
            if (currentIndex > -1) {
                setViewedTab(currentIndex)
            }
        }
    }

    /**
     * Restores all default tabs if missing or hidden.
     */
    fun restoreTabs() {
        for (i in tabs.indices) {
            val tab = tabs[i]
            if (tab == null) {
                when (i) {
                    0 -> {
                        var inter = player.getExtension<WeaponInterface>(WeaponInterface::class.java)
                        if (inter == null) {
                            player.addExtension(
                                WeaponInterface::class.java,
                                WeaponInterface(player).also { inter = it })
                        }
                        openTab(0, inter)
                    }

                    6 -> openTab(6, Component(player.spellBookManager.spellBook)) // Magic
                    7 -> if (player.familiarManager.hasFamiliar()) {
                        openTab(7, Component(Components.LORE_STATS_SIDE_662))
                    }

                    else -> openTab(i, Component(DEFAULT_TABS[i]))
                }
            } else if (tab.isHidden) {
                val child = (if (i < 7) 38 else 13) + i
                //				boolean resize = isResizable(); //TODO:
                player.packetDispatch.sendInterfaceConfig(windowPaneId, child, false)
                player.packetDispatch.sendInterfaceConfig(windowPaneId, child + 7, false)
                tabs[i]!!.isHidden = false
            }
        }
    }

    /**
     * Opens the default tab components for the player.
     */
    fun openDefaultTabs() {
        var inter = player.getExtension<WeaponInterface>(WeaponInterface::class.java)
        if (inter == null) {
            player.addExtension(WeaponInterface::class.java, WeaponInterface(player).also { inter = it })
        }
        openTab(0, inter)
        openTab(1, Component(Components.STATS_320))
        openTab(2, Component(Components.QUESTJOURNAL_V2_274))
        openTab(3, Component(Components.INVENTORY_149))
        openTab(4, Component(Components.WORNITEMS_387))
        openTab(5, Component(Components.PRAYER_271))
        openTab(6, Component(player.spellBookManager.spellBook))
        if (player.familiarManager.hasFamiliar()) {
            openTab(7, Component(Components.LORE_STATS_SIDE_662))
        }
        openTab(8, Component(Components.FRIENDS2_550))
        openTab(9, Component(Components.IGNORE2_551))
        openTab(10, Component(Components.CLANJOIN_589))
        openTab(11, Component(Components.OPTIONS_261))
        openTab(12, Component(Components.EMOTES_464))
        openTab(13, Component(Components.MUSIC_V3_187))
        openTab(14, Component(Components.LOGOUT_182))
        if (player.properties.autocastSpell != null) {
            inter!!.selectAutoSpell(inter!!.getAutospellId(player.properties.autocastSpell!!.spellId), true)
        }
    }

    /**
     * Opens status orbs and top bar interfaces.
     */
    fun openInfoBars() {
        //Hp orb
        PacketRepository.send(
            Interface::class.java,
            InterfaceContext(player, windowPaneId, if (isResizable) 13 else 70, Components.TOPSTAT_HITPOINTS_748, true)
        )
        //Prayer orb
        PacketRepository.send(
            Interface::class.java,
            InterfaceContext(player, windowPaneId, if (isResizable) 14 else 71, Components.TOPSTAT_PRAYER_749, true)
        )
        //Energy orb
        PacketRepository.send(
            Interface::class.java,
            InterfaceContext(player, windowPaneId, if (isResizable) 15 else 72, Components.TOPSTAT_RUN_750, true)
        )
        //Summoning bar
        if (isQuestComplete(player, Quests.WOLF_WHISTLE) && settings?.isMembers == true) {
            PacketRepository.send(
                Interface::class.java,
                InterfaceContext(player, windowPaneId, if (isResizable) 16 else 73, Components.TOPSTAT_LORE_747, true)
            )
        }
        //Split PM
        PacketRepository.send(
            Interface::class.java,
            InterfaceContext(player, windowPaneId, if (isResizable) 71 else 10, Components.PMCHAT_754, true)
        )
    }

    /**
     * Closes all default tab components.
     */
    fun closeDefaultTabs() {
        val inter = player.getExtension<WeaponInterface>(WeaponInterface::class.java)
        if (inter != null) {
            close(inter) // Attack
        }
        close(Component(Components.STATS_320)) // Skills
        close(Component(Components.QUESTJOURNAL_V2_274)) // Quest
        close(Component(Components.AREA_TASK_259)) // Diary
        close(Component(Components.INVENTORY_149)) // inventory
        close(Component(Components.WORNITEMS_387)) // Equipment
        close(Component(Components.PRAYER_271)) // Prayer
        close(Component(player.spellBookManager.spellBook))
        close(Component(Components.LORE_STATS_SIDE_662)) // summoning.
        close(Component(Components.FRIENDS2_550)) // Friends
        close(Component(Components.IGNORE2_551)) // Ignores
        close(Component(Components.CLANJOIN_589)) // Clan chat
        close(Component(Components.OPTIONS_261)) // Settings
        close(Component(Components.EMOTES_464)) // Emotes
        close(Component(Components.MUSIC_V3_187)) // Music
        //close(new Component(Components.LOGOUT_182)); // Logout
    }

    /**
     * Opens a tab component in a given slot.
     *
     * @param slot The tab index.
     * @param component The component to open.
     */
    fun openTab(slot: Int, component: Component?) {
        check(!(component!!.id == Components.WEAPON_FISTS_SEL_92 && component !is WeaponInterface)) { "Attack tab can only be instanced as " + WeaponInterface::class.java.canonicalName + "!" }
        if (component.definition!!.tabIndex != slot) {
            log(this.javaClass, Log.WARN, "Set tab index to " + slot + " for component " + component.id + ", definition requires updating!")
            component.definition.tabIndex = slot
        }
        if (component.definition.type != InterfaceType.TAB) {
            log(this.javaClass, Log.WARN, "Set interface type to TAB for component " + component.id + ", definition requires updating!")
            component.definition.type = InterfaceType.TAB
        }
        component.open(player)
        tabs[slot] = component
    }

    /**
     * Opens a tab using the component's defined tab index.
     *
     * @param component The component to open.
     */
    fun openTab(component: Component) {
        if (component.definition!!.tabIndex < 0) {
            log(this.javaClass, Log.WARN, "No component definitions found for tab " + component.id + "!")
            return
        }
        openTab(component.definition.tabIndex, component)
    }

    /**
     * Opens a chatbox interface by id.
     *
     * @param componentId The ID of the component to open.
     */
    fun openChatbox(componentId: Int) {
        openChatbox(Component(componentId))
    }

    /**
     * Opens a chatbox interface.
     *
     * @param component The component to open.
     */
    fun openChatbox(component: Component) {
        if (component.id == DEFAULT_CHATBOX) {
            if (chatbox == null || (chatbox!!.id != DEFAULT_CHATBOX && chatbox!!.definition!!.type == InterfaceType.CHATBOX)) {
                PacketRepository.send(Interface::class.java, InterfaceContext(player, windowPaneId, if (isResizable) 23 else 14, Components.FILTERBUTTONS_751, true))
                PacketRepository.send(Interface::class.java, InterfaceContext(player, windowPaneId, if (isResizable) 70 else 75, Components.CHATTOP_752, true))
                PacketRepository.send(Interface::class.java, InterfaceContext(player, InterfaceType.CHATBOX.fixedPaneId, InterfaceType.CHATBOX.fixedChildId, Components.CHATDEFAULT_137, true))
            }
            chatbox = component
            setVarp(player, 334, 1)
        } else {
            chatbox = component
            if (chatbox!!.definition!!.type != InterfaceType.DIALOGUE && chatbox!!.definition!!.type != InterfaceType.CHATBOX && chatbox!!.definition!!.type != InterfaceType.CS_CHATBOX) {
                log(this.javaClass, Log.WARN, "Set interface type to CHATBOX for component " + component.id + ", definition requires updating!")
                chatbox!!.definition!!.type = InterfaceType.DIALOGUE
            }
            chatbox!!.open(player)
        }
    }

    /**
     * Switches the window mode (e.g., fullscreen or fixed).
     *
     * @param windowMode The desired window mode.
     */
    fun switchWindowMode(windowMode: Int) {
        if (windowMode != player.session.clientInfo!!.windowMode) {
            player.session.clientInfo!!.windowMode = windowMode
            openWindowsPane(Component(if (isResizable) Components.TOPLEVEL_FULLSCREEN_746 else Components.TOPLEVEL_548))
            if (!player.getAttribute(GameAttributes.TUTORIAL_COMPLETE, false)) {
                hideTabs(player, false)
            } else {
                openDefaultTabs()
            }
            openInfoBars()
            PacketRepository.send(Interface::class.java, InterfaceContext(player, windowPaneId, if (isResizable) 23 else 14, Components.FILTERBUTTONS_751, true))
            PacketRepository.send(Interface::class.java, InterfaceContext(player, windowPaneId, if (isResizable) 70 else 75, Components.CHATTOP_752, true))
        }
    }

    /**
     * Gets a currently active component by its id.
     *
     * @param componentId The id of the component.
     * @return The matching component, or `null` if not found.
     */
    fun getComponent(componentId: Int): Component? {
        if (opened != null && opened!!.id == componentId) {
            return opened
        }
        if (chatbox != null && chatbox!!.id == componentId) {
            return chatbox
        }
        if (singleTab != null && singleTab!!.id == componentId) {
            return singleTab
        }
        if (overlay != null && overlay!!.id == componentId) {
            return overlay
        }
        if (windowsPane!!.id == componentId) {
            return windowsPane
        }
        for (c in tabs) {
            if (c != null && c.id == componentId) {
                return c
            }
        }
        if (componentId == Components.FILTERBUTTONS_751 || componentId == Components.TOPSTAT_RUN_750 || componentId == Components.TOPSTAT_LORE_747) {
            //Chatbox settings, run orb & summoning orb.
            return Component(componentId)
        }
        return null
    }

    /**
     * Sets the currently viewed tab.
     *
     * @param tabIndex The tab index to view.
     */
    fun setViewedTab(tabIndex: Int) {
        var tabIndex = tabIndex
        if (tabs[tabIndex] == null) {
            return
        }
        currentTabIndex = tabIndex
        when (tabIndex) {
            0 -> tabIndex = 1
            1 -> tabIndex = 2
            2 -> tabIndex = 3
        }
        if (tabIndex > 9) {
            tabIndex--
        }
        player.packetDispatch.sendRunScript(115, "i", tabIndex)
    }

    /**
     * Checks if the main (opened) component matches the given ID.
     *
     * @param id The component ID to check.
     * @return True if the currently opened component matches the given ID.
     */
    fun hasMainComponent(id: Int): Boolean {
        return opened != null && opened!!.id == id
    }

    /**
     * Opens an overlay interface.
     *
     * @param component The overlay component to open.
     */
    fun openOverlay(component: Component) {
        if (overlay != null && !overlay!!.close(player)) {
            return
        }
        overlay = component
        if (overlay!!.definition!!.type != InterfaceType.OVERLAY && overlay!!.definition!!.type != InterfaceType.OVERLAY_B) {
            log(
                this.javaClass,
                Log.WARN,
                "Set interface type to OVERLAY for component " + component.id + ", definition requires updating!"
            )
            overlay!!.definition!!.type = InterfaceType.OVERLAY
            overlay!!.definition!!.isWalkable = true
        }
        overlay!!.open(player)
    }

    /**
     * Closes the currently opened overlay interface.
     */
    fun closeOverlay() {
        if (overlay != null && close(overlay!!)) {
            overlay = null
        }
    }

    /**
     * Gets the player's current weapon tab interface.
     */
    val weaponTab: WeaponInterface
        get() = player.getExtension(WeaponInterface::class.java)

    /**
     * Gets the current window pane id.
     *
     * @return The id of the current window pane, or the default if not set.
     */
    val windowPaneId: Int
        get() {
            if (windowsPane == null) {
                return Components.TOPLEVEL_548
            }
            return windowsPane!!.id
        }


    /**
     * Gets the default child id used for interface placement.
     *
     * @return 6 if resizable, otherwise 11.
     */
    val defaultChildId: Int
        get() = if (isResizable) 6 else 11

    /**
     * Checks if the client is in resizable mode.
     *
     * @return `True` if resizable mode is active, `false` otherwise.
     */
    val isResizable: Boolean
        get() {
            if (player.session.clientInfo == null) {
                return false
            }
            return player.session.clientInfo.isResizable
        }

    /**
     * Increments and returns the current packet count.
     *
     * @param increment The amount to increment by.
     * @return The packet count before incrementing.
     */
    fun getPacketCount(increment: Int): Int {
        val count = packetCount
        packetCount += increment
        return count
    }

    companion object {
        /**
         * The default windows pane component id.
         */
        val WINDOWS_PANE: Int = Components.TOPLEVEL_548

        /**
         * The default chatbox component id.
         */
        @JvmField
        val DEFAULT_CHATBOX: Int = Components.CHATDEFAULT_137

        /**
         * Default tab component ids.
         */
        val DEFAULT_TABS: IntArray = intArrayOf(
            Components.WEAPON_FISTS_SEL_92,
            Components.STATS_320,
            Components.QUESTJOURNAL_V2_274,
            Components.INVENTORY_149,
            Components.WORNITEMS_387,
            Components.PRAYER_271,
            Components.MAGIC_192,
            Components.LORE_STATS_SIDE_662,
            Components.FRIENDS2_550,
            Components.IGNORE2_551,
            Components.CLANJOIN_589,
            Components.OPTIONS_261,
            Components.EMOTES_464,
            Components.MUSIC_V3_187,
            Components.LOGOUT_182
        )
    }
}
