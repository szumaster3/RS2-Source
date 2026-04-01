package content.region.misthalin.lumbridge.plugin.gnomecopter

import content.region.misthalin.lumbridge.plugin.gnomecopter.data.IntroductionPage
import content.region.misthalin.lumbridge.plugin.gnomecopter.data.impl.*
import core.game.world.map.Location

enum class GnomeCopterDestination(
    val id: Int,
    val displayName: String,
    val startLocation: Location,
    val region: Int,
    val tab: IntroductionPage,
    val autoPilot: Array<Location> = emptyArray()
) {
    CASTLE_WARS(
        id = 0,
        displayName = "Castle Wars",
        startLocation = Location.create(2017, 5395, 0),
        region = 8020,
        tab = CastleWarsInformationTab
    ),
    LLETYA(
        id = 1,
        displayName = "Lletya",
        startLocation = Location.create(2778, 5468, 0),
        region = 11093,
        tab = LletyaInformationTab
    ),
    TROLLWEISS_RELLEKKA_HUNTER(
        id = 2,
        displayName = "Trollweiss and Rellekka<br>Hunter area",
        startLocation = Location.create(2523, 5393, 0),
        region = 10068,
        tab = TrollweissAndRellekkaInformationTab
    ),
    BURTHORPE_GAMES_ROOM(
        id = 3,
        displayName = "Burthorpe Games Room",
        startLocation = Location.create(2397, 5402, 0),
        region = 9556,
        tab = BurthorpeInformationTab
    ),
    BURGH_DE_ROTT(
        id = 4,
        displayName = "Burgh de Rott",
        startLocation = Location.create(2132, 5411, 0),
        region = 8532,
        tab = BurghDeRottInformationTab
    ),
    PEST_CONTROL(
        id = 5,
        displayName = "Pest Control",
        startLocation = Location.create(2274, 5421, 0),
        region = 9044,
        tab = PestControlInformationTab
    );

    companion object {

        fun default() = CASTLE_WARS

        fun forId(id: Int): GnomeCopterDestination {
            return values().firstOrNull { it.id == id } ?: default()
        }
    }
}