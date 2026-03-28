package content.region.kandarin.gnome_stronghold.quest.itgronigen.plugin

import core.api.sendModelOnInterface
import core.api.sendString
import core.game.interaction.InterfaceListener
import shared.consts.Components

class StarChartInterface : InterfaceListener {

    private val zodiac = mapOf(
        19 to "Aquarius",
        20 to "Aries",
        21 to "Cancer",
        22 to "Capricorn",
        23 to "Gemini",
        24 to "Leo",
        25 to "Libra",
        26 to "Pisces",
        27 to "Sagittarius",
        28 to "Scorpio",
        29 to "Taurus",
        30 to "Virgo"
    )
    private val stars = mapOf(
        19 to 27064,
        20 to 27066,
        21 to 27067,
        22 to 27061,
        23 to 27068,
        24 to 27058,
        25 to 27057,
        26 to 27062,
        27 to 27056,
        28 to 27055,
        29 to 27059,
        30 to 27060
    )

    override fun defineInterfaceListeners() {

        /*
         * Handles observe the random constellation.
         */

        on(Components.STAR_CHART_104) { player, _, _, buttonID, _, _ ->
            if (buttonID !in 19..30) {
                return@on true
            }
            val modelID = stars[buttonID] ?: return@on true
            val sign    = zodiac[buttonID] ?: return@on true

            sendModelOnInterface(player, modelID, Components.STAR_CHART_104, 55, 0)
            sendString(player, sign, Components.STAR_CHART_104, 57)
            return@on true
        }
    }
}