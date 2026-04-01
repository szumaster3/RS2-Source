package content.region.misthalin.lumbridge.plugin.gnomecopter.data.impl

import content.region.misthalin.lumbridge.plugin.gnomecopter.data.IntroductionPage
import core.game.world.GameWorld
import core.tools.RED

// Completed
enum class PestControlInformationTab(
    vararg info: String,
) {
    FIRST_PAGE(
        "Deep in the southern waters",
        "of ${GameWorld.settings?.name}, the ${RED}Void</col>",
        "${RED}Knights</col>, devoted followers of",
        "the god Guthix, have",
        "discovered a series of islands",
        "where portals have opened,",
        "allowing strange creatures to",
        "pour into this world from",
        "another realm."
    ),
    SECOND_PAGE(
        "In order to preserve the",
        "balance of the world, the",
        "Knights are launching",
        "expeditions from their ${RED}Outpost</col>",
        "to these islands in an attempt",
        "to shut the portals down."
    ),
    THIRD_PAGE(
        "Each expedition is led by an",
        "experienced ${RED}Void Knight</col>, who",
        "stands in the centre of the",
        "island casting spells to",
        "weaken the portals.",
        "",
        "The invading pests will",
        "naturally target this figure, so",
        "the priority for players is to",
        "protect the Void Knight from"
    ),
    FOURTH_PAGE(
        "anything that poses a threat.",
        "If the Void Knight dies before",
        "the portals can be destroyed",
        "the island is lost and the",
        "players re transported back",
        "to the ${RED}Void Knights' Outpost</col>",
        "in defeat."
    ),
    FIFTH_PAGE(
        "The four portals appear at the",
        "west, south-west, south-east",
        "and eastern ends of the",
        "island. Although the Void",
        "Knight's spells will banish",
        "them completely in 20",
        "minutes, players will generally",
        "destroy the portals much",
        "quicker by force, earning a",
        "${RED}nice cash reward</col> from the"
    ),
    SIXTH_PAGE(
        "Void Knights for their",
        "assistance, as well as some",
        "${RED}Commendation Points</col> that can",
        "be traded at the Outpost for",
        "combat training, useful supplies and unique ${RED}Void",
        "${RED}Knight armour</col>."
    ),
    SEVENTH_PAGE(
        "The creatures pouring through",
        "the portals include:",
        "",
        "${RED}Shifters:",
        "These brightly coloured",
        "menaces have the ability to",
        "teleport through walls to reach",
        "their targets, completely",
        "bypassing the defences of",
        "this island."
    ),
    EIGHTH_PAGE(
        "${RED}Spinners:",
        "",
        "Floating, delicately like a",
        "jellyfish, these disgusting",
        "creatures will make it their",
        "business to heal any portal",
        "that's under attack. Players",
        "must attack the spinners to",
        "distract them from this task if",
        "they want ny chance of"
    ),
    NINTH_PAGE(
        "destroying the portals. When a",
        "spinner dies, it explodes in a",
        "cloud of poison.",
        "",
        "${RED}Torchers and defilers:",
        "",
        "These vicious creatures can",
        "use powerful Magic and",
        "Ranged attacks over",
        "extremely long distances."
    ),
    TENTH_PAGE(
        "Once they've locked their",
        "sights on the Void Knight, it's",
        "essential that they be",
        "destroyed as quickly as",
        "possible before they defeat",
        "the entire expedition. Players",
        "tend to guard the fortress",
        "walls, specifically targeting",
        "these creatures."
    ),
    ELEVENTH_PAGE(
        "${RED}Ravagers:",
        "",
        "Small and squat in",
        "appearance, these pests are",
        "capable of tearing down the",
        "gates of the fortress! That",
        "would permit a flood of pests",
        "to approach the Void Knight,",
        "quickly spelling disaster for",
        "the expedition, so players will"
    ),
    TWELFTH_PAGE(
        "make a point of attacking any",
        "ravager that's taking too much",
        "of an interest in the gates.",
        "",
        "${RED}Brawlers:",
        "",
        "These colossal pests like",
        "using their crushing melee",
        "attacks to squish anyone who",
        "gets too close. Despite their"
    ),
    THIRTEENTH_PAGE(
        "stupendous strength and",
        "weight, they don't pose much",
        "of a direct threat to the",
        "expedition because they're",
        "more interested in mindlessly",
        "clobbering players than",
        "strategically attacking the Void",
        "Knight."
    ),
    FOURTEENTH_PAGE(
        "${RED}Splatters:",
        "",
        "Bouncing merrily around the",
        "island, these beachball-like",
        "monsters will explode if killed,",
        "dealing considerable damage",
        "to everything in the area!"
    ),
    FIFTEENTH_PAGE(
        "A typical Pest Control team",
        "will consist of three distinct",
        "classes of player:",
        "",
        "${RED}1.</col> Players who attack the",
        "portals directly as soon as",
        "their shields are down. The",
        "portals' high defence levels",
        "make this role suitable for",
        "only the best fighters."
    ),
    SIXTEENTH_PAGE(
        "${RED}2.</col> Players who defend the",
        "Void Knight from any",
        "creatures that succeed in",
        "breaching the defences and",
        "reaching the centre of the",
        "island. Ideally, the fortress",
        "will keep out everything",
        "except the teleporting shifters,",
        "but additional creatures often",
        "manage to sneak through the",
    ),
    SEVENTEENTH_PAGE(
        "gates. It's usually best to",
        "keep the gates closed!",
        "",
        "${RED}3.</col> Players who camp outside",
        "the gates, targeting their",
        "attacks against anything that's",
        "threatening the Void Knight.",
        "Hordes of pests gather",
        "around the gates of the",
        "fortress. The torchers and"
    ),
    EIGHTEENTH_PAGE(
        "defilers can use their Magic",
        "and Ranged attacks to harm",
        "the Void Knight from there."
    );

    val lines = info.toList()

    companion object : IntroductionPage {
        override val pages: List<List<String>> =
            values().map { it.lines }
    }
}