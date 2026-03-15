package content.data

/**
 * The main and most important attributes for various interactions,
 * quests and also other categories.
 */
internal object GameAttributes {
    // Timers.
    const val TELEBLOCK_TIMER = "teleblock"

    // Location save.
    const val ORIGINAL_LOCATION = "/save:original-loc"
    const val LOGOUT = "/save:logout"

    // Tutorial island.
    const val TUTORIAL_COMPLETE = "/save:tutorial:complete"
    const val TUTORIAL_STAGE = "/save:tutorial:stage"

    // Statistics.
    const val NPC_KILLCOUNT = "/save:stats_manager:enemies_killed"

    // Prayer lock.
    const val PRAYER_LOCK = "prayer:lock"

    // Construction skill.
    const val CON = "con"
    const val PORTAL = "portal"
    const val CON_HOTSPOT = "con:hotspot"
    const val POH_PORTAL = "poh_entry"
    const val CON_OBJ = "construction:objs"
    const val CON_CLOCKMAKER_DIAL = "construction:clockbench"
    const val CON_PORTAL_DIR = "con:directportal"
    const val CON_GAZE_INTO = "con:gaze-into"
    const val CON_REMOVE_DECO = "construction:remove_decoration"
    const val CON_TOOLS = "construction:tools"
    const val CON_ROOM = "construction:room"
    const val CON_REMOVE = "construction:remove_dialogue"
    const val CON_SERVANT_CALL = "con:call"
    const val CON_LAST_TASK = "construction:servant_last_task"
    const val CON_TASK = "construction:servant_task"
    const val CON_FLATPACK_TIER = "construction:flatpack:tier"
    const val CON_LECTERN_OBJECT = "ttb:objectid"

    const val FAMILY_CREST = "/save:construction:family_crest"

    // Basic random event attributes.
    const val RE = "random"
    const val RE_PAUSE = "random:pause"
    const val RE_LOCK = "random:locked"
    const val RE_REWARD = "random:reward"


    // Prison pete random event.
    const val RE_PRISON_1 = "/save:random:prison_pete_start"
    const val RE_PRISON_2 = "/save:random:prison_pete_index"
    const val RE_PRISON_3 = "/save:random:prison_pete_interactions"
    const val RE_PRISON_4 = "/save:random:prison_pete_wrong"

    // Pinball random event.
    const val RE_PINBALL_START = "/save:random:pinball_start"
    const val RE_PINBALL_INTER = "/save:random:pinball_interactions"
    const val RE_PINBALL_OBJ = "/save:random:pinball:objs"

    // Pattern recognition random event.
    const val RE_PATTERN_INDEX = "random:pattern_index"
    const val RE_PATTERN_CORRECT = "random:pattern_correct"
    const val RE_PATTERN_OBJ = "random:pattern:objs"

    // Freaky forester random event.
    const val RE_FREAK_TASK = "/save:random:freaky_forester:task"
    const val RE_FREAK_COMPLETE = "/save:random:freaky_forester_complete"
    const val RE_FREAK_KILLS = "/save:random:freaky_forester:kills"

    // Pillory random event.
    const val RE_PILLORY_KEYS = "/save:random:pillory_item_1"
    const val RE_PILLORY_PADLOCK = "/save:random:pillory_item_2"
    const val RE_PILLORY_CORRECT = "/save:random:pillory_correct"
    const val RE_PILLORY_TARGET = "/save:random:pillory_target"
    const val RE_PILLORY_SCORE = "/save:random:pillory_score"

    // Evil twin random event.
    const val RE_TWIN_START = "/save:random:5:start"
    const val RE_TWIN_DIAL = "/save:random:evil_twin_dialogue"
    const val RE_TWIN_OBJ_LOC_X = "/save:random:evil_twin_loc:x"
    const val RE_TWIN_OBJ_LOC_Y = "/save:random:evil_twin_loc:y"

    // Evil bob random event.
    const val RE_BOB_START = "/save:random:evil_bob_start"
    const val RE_BOB_COMPLETE = "/save:random:evil_bob_complete"
    const val RE_BOB_ZONE = "/save:random:evil_bob_zone"
    const val RE_BOB_SCORE = "/save:random:evil_bob_score"
    const val RE_BOB_ALERT = "/save:random:evil_bob_temporary"
    const val RE_BOB_DIAL = "/save:random:evil_bob_dialogue"
    const val RE_BOB_DIAL_INDEX = "/save:random:evil_bob_index"
    const val RE_BOB_OBJ = "/save:random:evil_bob:objs"

    // Mime random event.
    const val RE_MIME_EMOTE = "random:mime_emote"
    const val RE_MIME_INDEX = "random:mime_index"
    const val RE_MIME_CORRECT = "random:mime_correct"
    const val RE_MIME_WRONG = "random:mime_wrong"

    // Quiz master random event.
    const val RE_QUIZ_REWARD = "/save:quiz:random_reward"
    const val RE_QUIZ_SCORE = "/save:quiz:score"

    // Maze random event.
    const val MAZE_ATTRIBUTE_TICKS_LEFT = "maze:percent-ticks-left"
    const val MAZE_ATTRIBUTE_CHESTS_OPEN = "/save:maze:chests-opened"

    // Sandwich lady random event.
    const val S_LADY_ITEM = "random:sandwich_lady:item"
    const val S_LADY_ITEM_VALUE = "random:sandwich_lady:item_value"

    // Plant random event.
    const val PLANT_NPC = "/save:random:plant"
    const val PLANT_NPC_VALUE = "/save:random:fruits"

    // Certers random event.
    const val CERTER_REWARD = "random:certer_reward"
    const val CERTER_CORRECT = "random:certer_stage"
    const val CERTER_INDEX = "random:certer_index"

    // Gravedigger random event.

    const val GRAVEDIGGER_SCORE = "random:gravedigger:score"

    // Drill demon random event.
    const val DRILL_OFFSET = "/save:random:drill_offset"
    const val DRILL_TASK = "/save:random:drill_task"
    const val DRILL_COUNTER = "/save:random:drill_score"

    // Kiss the frog random event.
    const val KTF_KISS_FAIL = "/save:random:kiss_the_frog:fail"

    // Swept away continuation (mini-quest).
    const val MINI_PURPLE_CAT = "/save:mini-quest:purple-cat"
    const val MINI_PURPLE_CAT_COMPLETE = "/save:mini-quest:purple-cat:complete"
    const val MINI_PURPLE_CAT_PROGRESS = "/save:mini-quest:purple-cat:score"

    // Swept away quest.
    const val QUEST_SWEPT_AWAY = "quest:swept-away"
    const val QUEST_SWEPT_AWAY_BROOM_POTION = "/save:quest:swept-away:hetty-enchantment"
    const val QUEST_SWEPT_AWAY_BETTY_ENCH = "/save:quest:swept-away:betty-enchantment"

    const val QUEST_SWEPT_AWAY_BABA_YAGA_ENCH_RECEIVED = "/save:quest:swept-away:betty-enchantment-completed"
    const val QUEST_SWEPT_AWAY_OLD_CRONE_ENCH_RECEIVED = "/save:quest:swept-away:old-crone-completed"
    const val QUEST_SWEPT_AWAY_ALI_THE_HAG_ENCH_RECEIVED = "/save:quest:swept-away:ali-the-hag-completed"
    const val QUEST_SWEPT_AWAY_KARDIA_ENCH_RECEIVED = "/save:quest:swept-away:kardia-completed"

    const val QUEST_SWEPT_AWAY_BETTY_WAND = "quest:swept-away:item"
    const val QUEST_SWEPT_AWAY_CREATURE_INTER = "/save:quest:sweep-away:creature_interactions"
    const val QUEST_SWEPT_AWAY_LABELS = "/save:quest:swept-away:labels"
    const val QUEST_SWEPT_AWAY_LOTTIE = "/save:quest:sweep-away:lottie-puzzle"

    const val QUEST_SWEPT_AWAY_LABELS_COMPLETE = "/save:quest:swept-away:labels:complete"

    const val TALK_ABOUT_SQ_IRKJUICE = "/save:broomstick:osman-talk"
    const val BROOM_ENCHANTMENT_TP = "/save:broomstick:teleport"

    // Phoenix Lair quest.
    const val PHOENIX_LAIR_VISITED = "/save:location:phoenix-lair:visit"
    const val TALK_WITH_PRIEST = "/save:quest:phoenix:talk-with-priest"
    const val PHOENIX_LAIR_ACTIVITY_REWARD = "/save:phoenix-activity:quills"
    const val PHOENIX_LAIR_FIRST_KILL = "/save:phoenix-activity:first-kill"

    // Tears of Guthix quest.
    const val QUEST_TOG_LAST_DATE = "/save:quest:tog:last_date"
    const val QUEST_TOG_LAST_XP_AMOUNT = "/save:quest:tog:xp_amount"
    const val QUEST_TOG_LAST_QP = "/save:quest:tog:last_qp"

    // Temple of Ikov quest.
    const val QUEST_IKOV_SELECTED_END = "/save:quest:ikov:end"
    const val QUEST_IKOV_DISABLED_TRAP = "/save:quest:ikov:disabled_trap"
    const val QUEST_IKOV_WINELDA_INTER = "/save:quest:ikov:winelda_interactions"
    const val QUEST_IKOV_BRIDGE_INTER = "/save:quest:ikov:bridge_interactions"
    const val QUEST_IKOV_ICE_CHAMBER_ACCESS = "/save:quest:ikov:ice_chamber_access"
    const val QUEST_IKOV_ICE_ARROWS = "/save:quest:ikov:ice_arrows"
    const val QUEST_IKOV_CHEST_INTER = "quest:ikov:chest_interactions"
    const val QUEST_IKOV_WARRIOR_INST = "quest:ikov:warrior_instance"

    // Horror from the deep quest.
    const val QUEST_HFTD_LIGHTHOUSE_MECHANISM = "hftd:lighthouse-fixed"
    const val QUEST_HFTD_UNLOCK_BRIDGE = "/save:hftd:lighthouse-bridge"
    const val QUEST_HFTD_STRANGE_WALL_DISCOVER = "/save:hftd:strange-wall"
    const val QUEST_HFTD_UNLOCK_DOOR = "/save:hftd:item-placed"

    // God books.
    const val GOD_BOOKS = "/save:god_books:access"

    const val QUEST_HFTD_USE_AIR_RUNE = "/save:hftd:air"
    const val QUEST_HFTD_USE_FIRE_RUNE = "/save:hftd:fire"
    const val QUEST_HFTD_USE_EARTH_RUNE = "/save:hftd:earth"
    const val QUEST_HFTD_USE_WATER_RUNE = "/save:hftd:water"
    const val QUEST_HFTD_USE_ARROW = "/save:hftd:arrow"
    const val QUEST_HFTD_USE_SWORD = "/save:hftd:sword"

    // Black knights fortress quest.
    const val QUEST_BKF_DOSSIER_INTER = "/save:quest:fortress:read_dossier"

    // Wolf Whistle quest.
    const val WOLF_WHISTLE_STIKKLEBRIX = "/save:quest:wolf:searched-body"

    // Penguins: Hide & Seek activity.
    const val ACTIVITY_PENGUINS_HNS = "/save:phns:spy-on:enabled"
    const val ACTIVITY_PENGUINS_HNS_SCORE = "/save:phns:points"

    // Animal Magnetism quest.
    const val ITEM_AVA_DEVICE = "item:ava-device:burping"

    // Witch house quest.
    const val ITEM_TOY_MOUSE_RELEASE = "item:released:toy-mouse"

    // RuneCrafting guild.
    const val RC_GUILD_TALISMAN = "/save:rcguild:shown_talisman"
    const val RC_GUILD_TALISMAN_TASK_START = "/save:rcguild:talisman_task"
    const val RC_GUILD_TALISMAN_TASK_COMPLETE = "/save:rcguild:omni-access"

    // The lost tribe quest.
    const val QUEST_LOST_TRIBE_LUMBRIDGE_CELLAR = "/save:tlt-hole-cleared"

    // Player safety training centre access.
    const val PLAYER_SAFETY_TRAINING_CENTRE_ACCESS = "/save:training-centre-access-unlocked"

    // Agile top.
    const val BARBARIAN_OUTPOST_GUNNJORN_TALK = "/save:gunnjorn:first-talk"
    const val BARBARIAN_OUTPOST_COURSE_LAPS = "/save:barbarian_outpost:obstacle_count"
    const val BARBARIAN_OUTPOST_PERFECT_LAPS = "/save:barbarian_outpost:perfect_laps_counter"
    const val BARBARIAN_OUTPOST_LAST_OBSTACLE = "barbarian-outpost-obstacle"
    const val BARBARIAN_OUTPOST_COURSE_REWARD = "/save:barbarian_course_reward:unlocked"

    // Agile legs.
    const val GNOME_STRONGHOLD_GNOME_TALK = "/save:gnome:first-talk"
    const val GNOME_STRONGHOLD_COURSE_LAPS = "/save:gnome_stronghold:obstacle_count"
    const val GNOME_STRONGHOLD_PERFECT_LAPS = "/save:gnome_stronghold:perfect_laps_counter"
    const val GNOME_STRONGHOLD_LAST_OBSTACLE = "gnome-stronghold-obstacle"
    const val GNOME_STRONGHOLD_COURSE_REWARD = "/save:gnome_stronghold_reward:unlocked"

    // Holy Grail quest.
    const val BLACK_KNIGHT_TITAN = "/save:failed_to_kill_titan"

    // Merlin Crystal quest.
    const val TEMP_ATTR_MORGAN = "morgan"
    const val TEMP_ATTR_BEGGAR = "beggar"
    const val TEMP_ATTR_THRANTAX = "thrantax"
    const val TEMP_ATTR_THRANTAX_OWNER = "thrantax_owner"
    const val TEMP_ATTR_LADY = "temp_lady"
    const val TEMP_ATTR_MERLIN = "temp_merlin"

    const val ATTR_STATE_ALTAR_FINISH = "/save:finished_altar"
    const val ATTR_STATE_CLAIM_EXCALIBUR = "/save:received_excalibur"
    const val ATTR_STATE_TALK_LADY = "/save:mc_talked_lady_lake"
    const val ATTR_STATE_TALK_BEGGAR = "/save:mc_talked_beggar"
    const val ATTR_STATE_TALK_CANDLE = "/save:mc_talked_candle_maker"

    // Sea slug quest.
    const val QUEST_SEA_SLUG_TALK_WITH_KENT = "/save:seaslug:kent-dialogue"

    // Achievements alternative teleportation options.
    const val ATTRIBUTE_VARROCK_ALT_TELE = "/save:diaries:varrock:alttele"
    const val ATTRIBUTE_CAMELOT_ALT_TELE = "/save:diaries:camelot:alttele"
    const val ATTRIBUTE_WATCHTOWER_ALT_TELE = "/save:diaries:watchtower:alttele"

    // Priest in Peril quest.
    const val QUEST_PRIEST_IN_PERIL = "/save:priest_in_peril"

    // Fremennik Trials quest.
    const val QUEST_VIKING_VOTES = "/save:fremtrials:votes"

    const val QUEST_VIKING_SIGMUND_START = "/save:sigmund-started"
    const val QUEST_VIKING_SIGMUND_PROGRESS = "/save:sigmund-steps"
    const val QUEST_VIKING_SIGMUND_RETURN = "/save:sigmundreturning"
    const val QUEST_VIKING_SIGMUND_VOTE = "/save:fremtrials:sigmund-vote"

    const val QUEST_VIKING_PEER_START = "/save:PeerStarted"
    const val QUEST_VIKING_PEER_RIDDLE = "/save:PeerRiddle"
    const val QUEST_VIKING_PEER_RIDDLE_SOLVED = "/save:riddlesolved"
    const val QUEST_VIKING_PEER_VOTE = "/save:fremtrials:peer-vote"

    const val QUEST_VIKING_MANI_START = "/save:fremtrials:manni-accepted"
    const val QUEST_VIKING_MANI_KEG = "/save:fremtrials:keg-mixed"
    const val QUEST_VIKING_MANI_BOMB = "/save:fremtrials:cherrybomb"
    const val QUEST_VIKING_MANI_VOTE = "/save:fremtrials:manni-vote"

    const val QUEST_VIKING_SWENSEN_START = "/save:fremtrials:swensen-accepted"
    const val QUEST_VIKING_SWENSEN_MAZE = "/save:fremtrials:maze-complete"
    const val QUEST_VIKING_SWENSEN_VOTE = "/save:fremtrials:swensen-vote"

    const val QUEST_VIKING_SIGLI_DRAUGEN_SPAWN = "/save:fremtrials:draugen-spawned"
    const val QUEST_VIKING_SIGLI_DRAUGEN_LOCATION = "/save:fremtrials:draugen-loc"
    const val QUEST_VIKING_SIGLI_DRAUGEN_KILL = "/save:fremtrials:draugen-killed"
    const val QUEST_VIKING_SIGLI_VOTE = "/save:fremtrials:sigli-vote"

    const val QUEST_VIKING_OLAF_START = "/save:fremtrials:olaf-accepted"
    const val QUEST_VIKING_OLAF_CONCERT = "/save:lyreConcertPlayed"

    const val QUEST_VIKING_LYRE = "/save:LyreEnchanted"
    const val QUEST_VIKING_PLAYER_ON_STAGE = "/save:onStage"

    const val QUEST_VIKING_OLAF_VOTE = "/save:fremtrials:olaf-vote"

    const val QUEST_VIKING_STEW_START = "/save:fremtrials:lalli-talkedto"
    const val QUEST_VIKING_STEW_INGREDIENTS_ONION = "/save:lalliStewOnionAdded"
    const val QUEST_VIKING_STEW_INGREDIENTS_POTATO = "/save:lalliStewPotatoAdded"
    const val QUEST_VIKING_STEW_INGREDIENTS_CABBAGE = "/save:lalliStewCabbageAdded"
    const val QUEST_VIKING_STEW_INGREDIENTS_ROCK = "/save:lalliStewRockAdded"
    const val QUEST_VIKING_EAT_STEW = "/save:lalliEatStew"

    const val QUEST_VIKING_HAS_WOOL = "/save:hasWool"
    const val QUEST_VIKING_ASKELADDEN_TALK = "/save:fremtrials:askeladden-talkedto"

    const val QUEST_VIKING_THORVALD_START = "/save:fremtrials:warrior-accepted"
    const val QUEST_VIKING_THORVALD_VOTE = "/save:fremtrials:thorvald-vote"

    // Varrock achievement (Who Ate All the Pie?).
    const val DIARY_VARROCK_ROMILY_WEAKLAX_PIE_AMOUNT = "/save:romily-weaklax:pie-amt"
    const val DIARY_VARROCK_ROMILY_WEAKLAX_PIE_ASSIGN = "/save:romily-weaklax:pie-assigned"

    // Phoenix Lair.
    const val PHOENIX_LAIR_EGGLING_CUTE = "/save:phoenix-familiar:0"
    const val PHOENIX_LAIR_EGGLING_MEAN = "/save:phoenix-familiar:1"

    // Fishing contest quest.
    const val QUEST_FISHINGCOMPO_WON = "/save:fishing_contest:won"
    const val QUEST_FISHINGCOMPO_STASH_GARLIC = "/save:fishing_contest:garlic-stuffed"
    const val QUEST_FISHINGCOMPO_CONTEST = "fishing_contest:contest-start"

    // Curse of zaros mini-quest.
    const val ZAROS_PATH_SEQUENCE = "/save:zaros:sequence"
    const val ZAROS_COMPLETE = "/save:zaros:complete"
    const val ZAROS_DHALAK_TALK = "/save:zaros:dhalak-talk"
    const val ZAROS_VALDEZ_TALK = "/save:zaros:valdez-talk"
    const val ZAROS_LENNISSA_TALK = "/save:zaros:lennissa-talk"
    const val ZAROS_RENNARD_TALK = "/save:zaros:rennard-talk"
    const val ZAROS_KHARRIM_TALK = "/save:zaros:kharrim-talk"

    // Ancient pages (lore).
    const val LORE_ANCIENT_PAGES_TRANSCRIPT = "/save:ancient_page"

    // Clue Scrolls.
    const val PUZZLE_BOX = "/save:clue:puzzle-box"
    const val PUZZLE_BOX_COMPLETE = "/save:clue:puzzle-box-completed"

    // Keldagrim travel.
    const val MINECART_TRAVEL_UNLOCK = "/save:keldagrim-visited"

    // Beehive cleaning.
    const val BEEHIVE_INTERACTION = "cleared-beehives"

    // Balloon base destination.
    const val BALLOON_ORIGIN = "balloon-origin-location"

    // Tribal totem quest.
    const val QUEST_TRIBAL_TOTEM_STAIRS = "/save:TT:StairsChecked"
    const val QUEST_TRIBAL_TOTEM_DOORS = "/save:TT:DoorUnlocked"

    // Plague City quest.
    const val ARDOUGNE_TELEPORT = "/save:ardougne_teleport"

    // Watchtower relics.
    const val WATCHTOWER_RELIC_1 = "/save:quest:watchtower:relic_1"
    const val WATCHTOWER_RELIC_2 = "/save:quest:watchtower:relic_2"
    const val WATCHTOWER_RELIC_3 = "/save:quest:watchtower:relic_3"

    const val WATCHTOWER_TOBAN_GOLD = "/save:quest:watchtower:gold_bar"
    const val WATCHTOWER_TOBAN_KEY = "/save:quest:watchtower:toban_key"
    const val WATCHTOWER_GORAD_TOOTH = "/save:quest:watchtower:ogre_tooth"
    const val WATCHTOWER_DRAGON_BONES = "/save:quest:watchtower:dragon_bones"
    const val WATCHTOWER_ROCK_CAKE = "/save:quest:watchtower:rock_cake"
    const val WATCHTOWER_RIDDLE = "/save:quest:watchtower:ogre_riddle"

    const val WATCHTOWER_GATE_UNLOCK = "/save:quest:watchtower:ogre_gate"
    const val WATCHTOWER_BRING_GOLD = "/save:quest:watchtower:bring_gold_to_the_guard"
    const val WATCHTOWER_GOLD_GATE_UNLOCK = "/save:quest:watchtower:ogre_gold_gate"
    const val WATCHTOWER_DARK_AREA = "quest:watchtower:dark_section"
    const val WATCHTOWER_SKAVID_UPSET = "quest:watchtower:upset_skavid"

    // Watchtower quest.
    const val WATCHTOWER_TELEPORT = "/save:watchtower_teleport"
    const val WATCHTOWER_SHAMAN_SPAWN = "/save:watchtower:shamans_spawned"
    const val WATCHTOWER_OGRE_DESTROY_COUNT = "/save:watchtower:ogres_destroyed"
    const val WATCHTOWER_SYSTEM_ACTIVATED = "/save:watchtower:done"

    // Knight's wave activity.
    const val KW_SPAWN = "/save:knights-training:spawn"
    const val KW_TIER = "/save:knights-training:wave"
    const val KW_BEGIN = "/save:knights-training:complete-tutorial"
    const val KW_COMPLETE = "/save:knights-training:complete"

    // Scorpion Catcher quest.
    const val LABEL_SCORPION_TAVERLEY = "/save:scorpcatcher:taverley"
    const val LABEL_SCORPION_OUTPOST = "/save:quest:scorpcatcher:outpost"
    const val LABEL_SCORPION_MONASTERY = "/save:scorpcatcher:monastery"
    const val LABEL_THORMAC_INTERACTION = "/save:scorpcatcher:thormac_talk_label"

    // Champions challenge.
    const val ACTIVITY_CHAMPIONS_CHALLENGE_DEFEAT_ALL = "/save:championsarena:final_boss"
    const val ACTIVITY_CHAMPIONS_COMPLETE = "/save:championsarena:complete"

    // Smoking kills miniquest.
    const val MINIQUEST_SMKI_GUARDIANS = "/save:quest:smki-guardians"

    // Draynor.
    const val TREE_GUARD = "draynor:tree_guard:feed"

    // Tower of life.
    const val TOL_BUILDER_COSTUME = "/save:tol:builder_costume"
    const val TOL_TOWER_ACCESS = "/save:tol:tower-enter"
    const val TOL_CUTSCENE = "/save:tol:cutscene-start"

    // Biohazard quest.
    const val FIRST_VIAL_CORRECT = "/save:biohazard-vial:1"
    const val SECOND_VIAL_CORRECT = "/save:biohazard-vial:2"
    const val THIRD_VIAL_CORRECT = "/save:biohazard-vial:3"

    const val FIRST_VIAL_WRONG = "/save:biohazard-vial:4"
    const val SECOND_VIAL_WRONG = "/save:biohazard-vial:5"
    const val THIRD_VIAL_WRONG = "/save:biohazard-vial:6"

    const val ELENA_REPLACE = "/save:biohazard-items"
    const val FEED_ON_FENCE = "/save:biohazard:fed-fence"

    // Fred the farmer.
    const val FRED_SEEN_THE_THING = "/save:activity-shapeshifter"

    // Fight Arena quest.
    const val FIGHT_ARENA_OPTIONAL_KILL = "/save:quest:arena-optional-task"

    // Observatory quest.
    const val RECEIVED_JUG_OF_WINE = "/save:observatory:assistant-gift"

    // Barbarian fishing
    const val BARBARIAN_BAREHAND_FISHING = "/save:fishing:technique:barehand"

    // Returning Clarence miniquest.
    const val RETURNING_CLARENCE_COMPLETE = "/save:hansand2:complete"
    const val RETURNING_CLARENCE_CHECKPOINT = "/save:handsand2:checkpoint"

    // Cooks Assistant quest.
    const val EGG_SUBMITTED = "cooks_assistant:egg_submitted"
    const val FLOUR_SUBMITTED = "cooks_assistant:flour_submitted"
    const val MILK_SUBMITTED = "cooks_assistant:milk_submitted"
    const val ALL_SUBMITTED = "cooks_assistant:all_submitted"
    const val PART_SUBMITTED = "cooks_assistant:submitted_some_items"

    // Back to my Roots quest.
    const val VINE_FIGHT = "/save:jade-vine-fight"

    // The Hand in the Sand quest.
    const val HAND_SAND_BETTY_POTION = "/save:handsand:betty_shine_potion"
    const val HAND_SAND_SANDY_ANSWERS = "/save:handsand:sandy_answers"
    const val HAND_SAND_LAST_SAND_CLAIM = "/save:sand_claim_timer"

    // The Observatory quest.
    const val OBSERVATORY_GOBLIN_SPAWN = "/save:observatory:goblin-spawn"
    const val OBSERVATORY_GOBLIN_KEY = "/save:observatory:goblin-key"
    const val OBSERVATORY_CONSTELLATION = "/save:observatory:constellation"
    const val OBSERVATORY_CHEST_FAIL_COUNTER = "/save:observatory:fail-counter"

    // Varrock Museum.
    const val NATURAL_HISTORIAN_FIRST_TALK = "/save:varrock_museum:natural-historian-meet"
    const val MUSEUM_FLOOR_MAP_ATTRIBUTE = "iface:527:floor"
    const val MUSEUM_INTERFACE_534_MODEL = "iface:534:model"
    const val MUSEUM_ARTIFACT_MODEL = "small$MUSEUM_INTERFACE_534_MODEL"

    // Recruitment drive
    const val RECRUITMENT_DRIVE_TEST_OF_PATIENCE = "rd:test_of_patience"
    const val RECRUITMENT_DRIVE_NPC_SPAWN = "rd:generatedsirleye"

    // Tog
    const val TOG_TIMER = "minigame:tearsofguthix-ticksremaining"
    const val TOG_TEARS_TTL = "minigame:tearsofguthix-tearscollected"
    const val TOG_ACTVITY = "minigame:tearsofguthix-iscollecting"
}
