package content.region.misthalin.lumbridge.plugin.gnomecopter.data.impl

import content.region.misthalin.lumbridge.plugin.gnomecopter.data.IntroductionPage
import core.tools.RED

// Complete
enum class BurghDeRottInformationTab(vararg info: String) {
    FIRST_PAGE(
        "Burgh de Rott is a small",
        "village situated deep in the",
        "four swamps of Morytania, a",
        "wast land east of the River",
        "Salve.",
        "",
        "At this time, Morytania is",
        "largely controlled by powerful",
        "vampyres led by Lord Drakan",
        "and his family. The vampyres"
    ),
    SECOND_PAGE(
        "rule almost the entire eastern",
        "side of Morytania. Most of the",
        "remaining land is a putrid",
        "swamp infested with sinister",
        "and grotesque creatures of",
        "darkness, so there are very",
        "few places where humans",
        "can even survive."
    ),
    THIRD_PAGE(
        "Burgh de Rott is one such",
        "place out it's far from being a",
        "desirable home. There is very",
        "little food, many of the locals",
        "have been reduced to eating",
        "rats and they live in constant",
        "fear of the evil vampyres",
        "collecting tithes of their blood."
    ),
    FOURTH_PAGE(
        "Adventures come here to do",
        "a quest called ${RED}In Aid of the",
        "${RED}Myreque. They are asked to",
        "rebuild the town so that it can",
        "be used as a base by a",
        "brave group of humans who",
        "are trying to rebel against the",
        "vampyres."
    ),
    FIFTH_PAGE(
        "A hero's life isn't a fighting",
        "and glamour if you look",
        "around this town, you'll see",
        "adventurers busily repairing",
        "the bank, stocking the general",
        "store, lighting the town's",
        "furnace and creating",
        "campfires to warm the poor,",
        "inhabitants of the Burgh."
    ),
    SIXTH_PAGE(
        "Some of the villagers are so",
        "afraid that anyone could be a",
        "vampyre tht they attack the",
        "adventurers by throwing",
        "rotten tomatoes at them. They",
        "are right to be afraid; the",
        "vampyres hunger for blood is",
        "insatiable and they show no",
        "mercy."
    ),
    SEVENTH_PAGE(
        "It is to be hoped that the",
        "efforts of these adventures",
        "and their rebel friends will",
        "eventually drive the vampyric",
        "foes out of these lands",
        "completely.",
        "",
        "Many of the villagers are so",
        "desperate to escape from",
        "Burgh de Rott that they will"
    ),
    EIGHT_PAGE(
        "pay adventurers to guide them",
        "through the perilous swamps",
        "to the Paterdomus Temple on",
        "the River Salve. This is a",
        "very dangerous minigame",
        "known as ${RED}Temple Trekking</col>.",
        "",
        "As news of this terrible",
        "situation reaches the civilised",
        "land of Misthalin, mercenary"
    ),
    NINTH_PAGE(
        "fighters are coming into the",
        "Burgh from Varrock, hoping to",
        "lend their strength to the fight",
        "against the vampyres.",
        "",
        "Adventurers are rewarded for",
        "bringing the mercenaries here",
        "from the Paterdomus Temple",
        "in a similar minigame known",
        "as the ${RED}Burgh de Rott Ramble</col>."
    ),
    TENTH_PAGE(
        "Burgh de Rott has a few",
        "useful features for",
        "adventurers looking to train",
        "their skills.",
        "",
        "In particular, the nearby",
        "coastline is teeming with",
        "sharks, providing excellent",
        "food for anyone skilled,",
        "enough to harpoon them and",
    ),
    ELEVENTH_PAGE(
        "cook them.",
        "",
        "The swamps of Morytania",
        "also provides plenty of",
        "ingredients for potions,",
        "including nail beast claws",
        "which can be used to make",
        "the Sanfew serum - assuming",
        "you're tough enough to collect",
        "them from the nail beast"
    ),
    TWELFTH_PAGE(
        "before the nail beast kills you."
    );

    val lines = info.toList()

    companion object : IntroductionPage {
        override val pages: List<List<String>> =
            values().map { it.lines }
    }
}