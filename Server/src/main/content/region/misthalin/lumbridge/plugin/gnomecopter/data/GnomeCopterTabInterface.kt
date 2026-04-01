package content.region.misthalin.lumbridge.plugin.gnomecopter.data

import content.region.misthalin.lumbridge.plugin.gnomecopter.GnomeCopterDestination
import core.api.getVarbit
import core.api.sendMessage
import core.api.setVarbit
import core.game.interaction.InterfaceListener
import shared.consts.Components
import shared.consts.Vars

class GnomeCopterTabInterface : InterfaceListener {

    override fun defineInterfaceListeners() {

        on(Components.CARPET_MAIN_728) { player, _, _, button, _, _ ->

            val destination = player.getAttribute(
                "gc:route",
                GnomeCopterDestination.default()
            )

            var page = player.getAttribute("gc:page", 0)
            val max = destination.tab.pages.size - 1

            when (button) {
                FIRST_PAGE -> page = 0
                LAST_PAGE -> page = max
                NEXT_PAGE -> if (page < max) page++
                PREVIOUS_PAGE -> if (page > 0) page--
                TOGGLE_AUTO_PILOT -> {
                    val turnOff = getVarbit(player, Vars.VARBIT_GNOMECOPTER_TURN_ON_AUTOPILOT_4536) == 1
                    val toggle = if (turnOff) 0 else 1
                    setVarbit(player, Vars.VARBIT_GNOMECOPTER_TURN_ON_AUTOPILOT_4536, toggle)
                    sendMessage(player, "The gnomecopter's autopilot function has been ${if (turnOff) "enabled" else "disabled"}.")
                }
                RETURN_TO_LUMBRIDGE -> player.interfaceManager.openDefaultTabs()
            }
            IntroductionPageManager.sendPage(player, destination.tab, page)
            return@on true
        }
    }

    companion object {
        const val TOGGLE_AUTO_PILOT = 17
        const val RETURN_TO_LUMBRIDGE = 18
        const val FIRST_PAGE = 20
        const val PREVIOUS_PAGE = 21
        const val NEXT_PAGE = 22
        const val LAST_PAGE = 23
    }
}