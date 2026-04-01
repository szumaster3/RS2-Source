package content.region.misthalin.lumbridge.plugin.gnomecopter.data.impl

import content.region.misthalin.lumbridge.plugin.gnomecopter.data.IntroductionPage
import core.tools.RED

enum class TrollweissAndRellekkaInformationTab(
    vararg info: String,
) {
    FIRST_PAGE(
        "In the far north of the world,",
        "players face the biting cold to",
        "hunt those creatures that",
        "thrive in such a harsh",
        "environment. Many of these",
        "animals are too dangerous or",
        "too sneaky to be fought",
        "normally, so the only way to",
        "catch them for their useful",
        "properties and valuable pelts"
    ),
    SECOND_PAGE(
        "is to use the ${RED}Hunter skill</col>.",
        "",
        "This particular Hunter area",
        "features a wide variety of",
        "creatures, each of which must",
        "be caught in a particular way:",
        "polar kebbits, snowy knights",
        "and sapphire glacialis",
        "butterflies, cerulean twitches, and",
        "sabre-toothed kyatts."
    ),
    THIRD_PAGE(
        "$RED Polar kebbits</col>",
        "",
        "Kebbits tend to be timid",
        "creatures, using their",
        "incredible speed to escapes as",
        "soon as they feel threatened.",
        "The only way to catch a polar",
        "kebbit is to search for tracks",
        "leading from its burrow to",
        "wherever the creature's"
    ),
    FOURTH_PAGE(
        "hiding. Once it's been found, a",
        "skillful hunter can seize it",
        "with a noose wand.",
        "",
        "The fur of a polar kebbit can",
        "be made into clothing to help",
        "the aspiring hunter blend in",
        "with their surroundings,",
        "making it easier to sneak up",
        "on creatures in this area."
    ),
    FIFTH_PAGE(
        "${RED}Butterflies",
        "",
        "Two kinds of butterfly have",
        "adapted to life in this frozen",
        "land; the snowy knight and",
        "the sapphire glacialis.",
        "Requiring different Hunter",
        "levels, these can be caught",
        "with a suitable net and stored",
        "in jars."
    ),
    SIXTH_PAGE(
        "",
        "The powers of the butterflies",
        "can be unlocked by using the",
        "jars on other players;",
        "depending on the type of",
        "butterfly, the target player",
        "may gain a boost to a combat",
        "stat or may have some",
        "Hitpoints restored."
    ),
    SEVENTH_PAGE(
        "${RED}Birds",
        "",
        "The cerulean twitch may be",
        "found in this area. Humans",
        "tend not to fly very well, so",
        "the best way to catch these",
        "birds is to set a trap designed",
        "to catch them when they land",
        "on it."
    ),
    EIGHTH_PAGE(
        "The birds arent completely",
        "stupid, so it helps if the",
        "hunter is wearing clothes",
        "made from the pelts, of local",
        "animals. This gets them blend",
        "into the scenery so that the",
        "bird doesn't suspect anything's",
        "wrong."
    ),
    NINTH_PAGE(
        "${RED}Sabre-Toothed Kyatts",
        "",
        "The only way to kill a kyatt",
        "is to lure it into a pit. Hunters",
        "will construct pit traps on",
        "suitable sites with the aid of",
        "logs, then tease the kyatt until",
        "it follows them to the trap.",
        "",
        "The signposts in the southern"
    ),
    TENTH_PAGE(
        "end of this are will let you",
        "get a closer look at this",
        "dangerous activity."
    ),
    ELEVENTH_PAGE(
        "Elsewhere in the world, there",
        "are Hunter areas in the woodland,",
        "jungle and desert regions, all",
        "featuring their own native",
        "creatures.",
        "",
        "Players can trap salamanders",
        "to use as flamethrowing",
        "weapons, lure the colourful",
        "tropical wagtail to get brighter"
    ),
    TWELFTH_PAGE(
        "feathers to help when fishing",
        "box up the volatiles",
        "chinchompas to throw as",
        "grenades; catch imps that will",
        "carry items to the bnk in",
        "exchange for their freedom",
        "and far more besides."
    );

    val lines = info.toList()

    companion object : IntroductionPage {
        override val pages: List<List<String>> =
            values().map { it.lines }
    }
}