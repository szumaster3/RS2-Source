package content.region.misthalin.lumbridge.plugin.gnomecopter.data.impl

import content.region.misthalin.lumbridge.plugin.gnomecopter.data.IntroductionPage
import core.tools.BLUE
import core.tools.RED

enum class CastleWarsInformationTab(
    vararg info: String,
) {
    FIRST_PAGE(
        "In Castle Wars, two teams of",
        "players indulge in no-holds-",
        "barred battle across this huge",
        "arena.",
        "",
        "The saradomin team, wearing",
        "${BLUE}blue</col> hoods, must attempt to",
        "breach the Zamorak castle,",
        "seize the ${RED}red flag</col> from the",
        "roof and survive to drag it",
    ),
    SECOND_PAGE(
        "back to their own castle",
        "without letting their own flag",
        "be stolen.",
        "",
        "The Zamorak team, in ${RED}red</col>",
        "hoods, must breach the",
        "Saradomin castle seize the",
        "${BLUE}blue flag</col> from the roof and",
        "survive to drag it back to",
        "their own castle."
    ),
    THIRD_PAGE(
        "This minigame is always",
        "popular, offering players the",
        "chance to fight tactically and",
        "use their skills to pass their",
        "enemies' defences or just to",
        "vent some frustration by",
        "unleashing all their rage on",
        "their opponents.",
    ),
    FOURTH_PAGE(
        "The teams win points by",
        "getting their opponents' flag",
        "home to their castle.",
        "",
        "After 25 minutes, the game",
        "ends and everyone on the",
        "winning team is presented",
        "with ${RED}Castle Wars tickets</col>.",
        "",
        "There's a man outside",
    ),
    FIFTH_PAGE(
        "Castle Wars arena who will",
        "exchange the tickets for",
        "colourful armour, allowing",
        "keen players to show off",
        "their dedication and skill."
    ),
    UNKNOWN_1_PAGE(
        "skirt the action and hop",
        "across the stepping-stones in",
        "the river.",
        "",
        "Either of these approaches",
        "would leave the player with",
        "the challenge of breaking into",
        "the enemy castle once they've",
        "arrived. Check out the",
        "noticeboards around the",
    ),
    UNKNOWN_2_PAGE(
        "castles to see how this may",
        "be achieved.",
        "",
        "A more subtle approach is to",
        "use the tunnels under the",
        "battlefield. The tunnels run",
        "between the two castles,",
        "allowing sneak attacks on the",
        "enemy stronghold through the",
        "floor.",
    ),
    UNKNOWN_3_PAGE(
        "This is a safe minigame.",
        "Although players die here all",
        "the time, they respawn",
        "immediately in their own",
        "castle without losing any",
        "items.",
        "",
        "This makes it a popular place",
        "for players to train their",
        "combat skills."
    );


    val lines = info.toList()

    companion object : IntroductionPage {
        override val pages: List<List<String>> =
            values().map { it.lines }
    }
}