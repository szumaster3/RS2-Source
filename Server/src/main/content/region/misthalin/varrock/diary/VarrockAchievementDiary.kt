package content.region.misthalin.varrock.diary

import content.data.GameAttributes
import content.global.skill.magic.TeleportMethod
import content.global.skill.prayer.Bones
import content.global.travel.canoe.CanoePlugin
import content.global.travel.fairyring.FairyRing
import content.region.misthalin.varrock.dialogue.BennyDialogue
import content.region.misthalin.varrock.dialogue.ElsieDialogue
import content.region.misthalin.varrock.museum.dialogue.OrlandoSmithDialogue
import content.region.misthalin.varrock.quest.crest.dialogue.DimintheisDialogue
import content.region.misthalin.varrock.quest.phoenixgang.dialogue.CuratorHaigHalenDialogue
import core.api.*
import core.game.diary.DiaryAreaTask
import core.game.diary.DiaryEventHookBase
import core.game.diary.DiaryLevel
import core.game.event.*
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.SpellBookManager
import core.game.node.entity.player.link.diary.DiaryType
import core.game.node.entity.skill.Skills
import core.game.node.item.Item
import core.game.world.map.zone.ZoneBorders
import core.game.world.map.zone.impl.WildernessZone
import shared.consts.*

class VarrockAchievementDiary : DiaryEventHookBase(DiaryType.VARROCK) {
    companion object {
        private const val ATTRIBUTE_CRAFT_AIR_BATTLESTAFF = "diary:varrock:craft-air-battlestaff"

        private val VARROCK_ROOF_AREA = ZoneBorders(3201, 3467, 3225, 3497, 3, true)
        private val SOS_LEVEL_2_AREA = ZoneBorders(2040, 5241, 2046, 5246)
        private val CHAMPIONS_GUILD_AREA = ZoneBorders(3188, 3361, 3194, 3362)
        private val OZIACH_SHOP_AREA = ZoneBorders(3066, 3514, 3070, 3518)
        private val AIR_OBELISK_AREA = ZoneBorders(3087, 3568, 3089, 3570)
        private val FARMING_PATCH_AREA = ZoneBorders(3180, 3356, 3183, 3359)
        private val EARTH_ALTAR_AREA = getRegionBorders(10571)

        private val STRAY_DOGS = arrayOf(NPCs.STRAY_DOG_4766, NPCs.STRAY_DOG_4767, NPCs.STRAY_DOG_5917, NPCs.STRAY_DOG_5918)

        object EasyTasks {
            const val THESSALIA_BROWSE_CLOTHES = 0
            const val AUBURY_TELEPORT_ESSENCE_MINE = 1
            const val MINE_IRON_SOUTHEAST = 2
            const val MAKE_PLANK_SAWMILL = 3
            const val VISIT_SOS_LEVEL2 = 4
            const val JUMP_OVER_FENCE_SOUTH = 5
            const val LUMBERYARD_CHOP_DYING_TREE = 6
            const val BUY_VARROCK_HERALD = 7
            const val GIVE_STRAY_DOG_A_BONE = 8
            const val BARBARIAN_VILLAGE_SPIN_A_BOWL = 9
            const val EDGEVILLE_ENTER_DUNGEON_SOUTH = 10
            const val MOVE_POH_TO_DIFFERENT_LOCATION = 11
            const val SPEAK_TO_HAIG_HALEN_50QP = 12
            const val ENTER_EARTH_ALTAR = 13
            const val ELSIE_TELL_A_STORY = 14
            const val PATERDOMUS_MINE_LIMESTONE = 15
            const val BARBARIAN_VILLAGE_CATCH_TROUT = 16
            const val SEWERS_CUT_COBWEB = 17
            const val FIND_HIGHEST_POINT = 18
        }

        object MediumTasks {
            const val APOTHECARY_MAKE_STRENGTH_POTION = 0
            const val CHAMPIONS_GUILD_VISIT = 1
            const val TAKE_DAGONHAI_CHAOS_PORTAL_SHORTCUT = 2
            const val RAT_POLE_FULL_RAT_COMPLEMENT = 3
            const val SEWER_GATHER_RED_SPIDERS_EGGS = 4
            const val USE_SPIRIT_TREE_NORTH = 5
            const val PERFORM_ALL_SOS_EMOTES = 6
            const val SELECT_KITTEN_COLOR = 7
            const val USE_GE_UNDER_WALL_SHORTCUT = 8
            const val ENTER_SOULBANE_RIFT = 9
            const val DIGSITE_PENDANT_TELEPORT = 10
            const val CRAFT_EARTH_TIARA = 11
            const val PALACE_PICKPOCKET_GUARD = 12
            const val CAST_VARROCK_TELEPORT_SPELL = 13
            const val VANNAKA_GET_SLAYER_TASK = 14
            const val SAWMILL_BUY_20_MAHOGANY_PLANKS = 15
            const val PICK_FROM_WHITE_TREE = 16
            const val HOTAIR_BALLOON_TRAVEL_SOMEWHERE = 17
            const val GERTRUDE_GET_CAT_TRAINING_MEDAL = 18
            const val DIAL_FAIRY_RING_WEST = 19
            const val OZIACH_BROWSE_STORE = 20
        }

        object HardTasks {
            const val PICK_POISON_IVY_FARMING_PATCH = 0
            const val USE_MOSS_GIANT_PIPE_SHORTCUT = 1
            const val FANCY_DRESS_SELLER_TRADE_FURS = 2
            const val SMITH_ADAMANT_MED_HELM_SOUTHEAST = 3
            const val SPEAK_TO_ORLANDO_SMITH_153_KUDOS = 4
            const val GIVE_WEAKLAX_A_PIE = 5
            const val CRAFT_AIR_BATTLESTAFF = 6
            const val GIVE_POH_TROPICAL_WOOD_OR_FANCY_STONE_FINISH = 7
            const val MAKE_VARROCK_TELEPORT_TABLET_OR_MAHOGANY_LECTERN = 8
            const val OBTAIN_NEW_SET_OF_FAMILY_CREST_GAUNTLETS = 9
            const val EDGEVILLE_MAKE_WAKA_CANOE = 10
            const val EDGEVILLE_TELEPORT_USING_ANCIENT_MAGICKS = 11
            const val BARBARIAN_VILLAGE_TELEPORT_USING_SKULL_SCEPTRE = 12
        }
    }

    override val areaTasks
        get() =
            arrayOf(
                DiaryAreaTask(
                    VARROCK_ROOF_AREA,
                    DiaryLevel.EASY,
                    EasyTasks.FIND_HIGHEST_POINT,
                ),
                DiaryAreaTask(
                    SOS_LEVEL_2_AREA,
                    DiaryLevel.EASY,
                    EasyTasks.VISIT_SOS_LEVEL2,
                ),
                DiaryAreaTask(
                    CHAMPIONS_GUILD_AREA,
                    DiaryLevel.MEDIUM,
                    MediumTasks.CHAMPIONS_GUILD_VISIT,
                ),
                DiaryAreaTask(
                    EARTH_ALTAR_AREA,
                    DiaryLevel.EASY,
                    EasyTasks.ENTER_EARTH_ALTAR,
                ),
            )

    override fun onResourceProduced(player: Player, event: ResourceProducedEvent) {
        when (player.viewport.region!!.id) {
            12341 -> {
                when (event.itemId) {
                    Items.RAW_TROUT_335 ->
                        finishTask(
                            player,
                            DiaryLevel.EASY,
                            EasyTasks.BARBARIAN_VILLAGE_CATCH_TROUT,
                        )
                }
            }

            12853 -> {
                when (event.itemId) {
                    Items.ADAMANT_MED_HELM_1145 ->
                        finishTask(
                            player,
                            DiaryLevel.HARD,
                            HardTasks.SMITH_ADAMANT_MED_HELM_SOUTHEAST,
                        )
                }
            }

            13108 -> {
                when (event.itemId) {
                    Items.IRON_ORE_440 ->
                        finishTask(
                            player,
                            DiaryLevel.EASY,
                            EasyTasks.MINE_IRON_SOUTHEAST,
                        )
                }
            }

            13110 -> {
                when (event.itemId) {
                    Items.LOGS_1511 ->
                        if (event.source.id == Scenery.DYING_TREE_24168) {
                            finishTask(
                                player,
                                DiaryLevel.EASY,
                                EasyTasks.LUMBERYARD_CHOP_DYING_TREE,
                            )
                        }
                }
            }

            13366 -> {
                when (event.itemId) {
                    Items.LIMESTONE_3211 ->
                        finishTask(
                            player,
                            DiaryLevel.EASY,
                            EasyTasks.PATERDOMUS_MINE_LIMESTONE,
                        )
                }
            }

            10571 -> {
                when (event.itemId) {
                    Items.EARTH_TIARA_5535 ->
                        finishTask(
                            player,
                            DiaryLevel.MEDIUM,
                            MediumTasks.CRAFT_EARTH_TIARA,
                        )
                }
            }
        }
    }

    override fun onTeleported(player: Player, event: TeleportEvent) {
        when (event.source) {
            is NPC -> {
                if (event.method == TeleportMethod.NPC && event.source.id == NPCs.AUBURY_553) {
                    finishTask(
                        player,
                        DiaryLevel.EASY,
                        EasyTasks.AUBURY_TELEPORT_ESSENCE_MINE,
                    )
                }
            }

            is Item -> {
                if (event.source.id in 11190..11194) {
                    finishTask(
                        player,
                        DiaryLevel.MEDIUM,
                        MediumTasks.DIGSITE_PENDANT_TELEPORT,
                    )
                }
            }
        }
    }

    override fun onInteracted(player: Player, event: InteractionEvent) {
        when (player.viewport.region!!.id) {
            12342 -> {
                if (event.target.id == Scenery.TRAPDOOR_26934) {
                    finishTask(
                        player,
                        DiaryLevel.EASY,
                        EasyTasks.EDGEVILLE_ENTER_DUNGEON_SOUTH,
                    )
                }
            }

            12698 -> {
                if (event.target.id == Scenery.PIPE_29370 && hasLevelStat(player, Skills.AGILITY, 51)) {
                    finishTask(
                        player,
                        DiaryLevel.HARD,
                        HardTasks.USE_MOSS_GIANT_PIPE_SHORTCUT,
                    )
                }
            }
        }

        if (isQuestComplete(player, Quests.DRAGON_SLAYER)) {
            if (event.target.id == NPCs.OZIACH_747 && event.option == "trade" && inBorders(player, OZIACH_SHOP_AREA)) {
                finishTask(
                    player,
                    DiaryLevel.MEDIUM,
                    MediumTasks.OZIACH_BROWSE_STORE,
                )
            }
        }

        if (event.target.id == Items.SKULL_SCEPTRE_9013 && event.option == "invoke") {
            finishTask(
                player,
                DiaryLevel.HARD,
                HardTasks.BARBARIAN_VILLAGE_TELEPORT_USING_SKULL_SCEPTRE,
            )
        }
    }

    override fun onButtonClicked(player: Player, event: ButtonClickEvent) {
        player.viewport.region?.let {
            when (it.id) {
                12342 -> {
                    if (event.iface == CanoePlugin.IFACE_CANOE_SHAPING &&
                        event.buttonId ==
                        CanoePlugin.SHAPING_BUTTONS[CanoePlugin.CanoeType.WAKA.ordinal]
                    ) {
                        finishTask(
                            player,
                            DiaryLevel.HARD,
                            HardTasks.EDGEVILLE_MAKE_WAKA_CANOE,
                        )
                    }
                }
            }
        }
    }

    override fun onDialogueOptionSelected(player: Player, event: DialogueOptionSelectionEvent) {
        when (event.dialogue) {
            is BennyDialogue -> {
                if (event.currentStage == 4) {
                    if (inInventory(player, Items.COINS_995, 50)) {
                        finishTask(
                            player,
                            DiaryLevel.EASY,
                            EasyTasks.BUY_VARROCK_HERALD,
                        )
                    }
                }
            }
            is ElsieDialogue -> {
                if (event.currentStage == 4) {
                    finishTask(
                        player,
                        DiaryLevel.EASY,
                        EasyTasks.ELSIE_TELL_A_STORY,
                    )
                }
            }
            is DimintheisDialogue -> {
                if (event.currentStage == 6000) {
                    finishTask(
                        player,
                        DiaryLevel.HARD,
                        HardTasks.OBTAIN_NEW_SET_OF_FAMILY_CREST_GAUNTLETS,
                    )
                }
            }
            is OrlandoSmithDialogue -> {
                if (event.currentStage >= 1) {
                    finishTask(
                        player,
                        DiaryLevel.HARD,
                        HardTasks.SPEAK_TO_ORLANDO_SMITH_153_KUDOS,
                    )
                }
            }
        }
    }

    override fun onUsedWith(player: Player, event: UseWithEvent) {
        when (event.used) {
            in Bones.array -> {
                if (event.with in STRAY_DOGS) {
                    finishTask(
                        player,
                        DiaryLevel.EASY,
                        EasyTasks.GIVE_STRAY_DOG_A_BONE,
                    )
                }
            }
        }
        when {
            inBorders(player, AIR_OBELISK_AREA) -> {
                if (event.used == Items.BATTLESTAFF_1391 && event.with == Items.AIR_ORB_573) {
                    whenTaskRequirementFulfilled(player, ATTRIBUTE_CRAFT_AIR_BATTLESTAFF) {
                        finishTask(
                            player,
                            DiaryLevel.HARD,
                            HardTasks.CRAFT_AIR_BATTLESTAFF,
                        )
                    }
                }
            }
        }
    }

    override fun onInterfaceOpened(player: Player, event: InterfaceOpenEvent) {
        when (event.component.id) {
            Components.THESSALIA_CLOTHES_MALE_591, Components.THESSALIA_CLOTHES_FEMALE_594 -> {
                if (player.viewport.region!!.id == 12853) {
                    finishTask(
                        player,
                        DiaryLevel.EASY,
                        EasyTasks.THESSALIA_BROWSE_CLOTHES,
                    )
                }
            }
        }
    }

    override fun onSpellCast(player: Player, event: SpellCastEvent) {
        if (event.spellBook == SpellBookManager.SpellBook.MODERN &&
            event.spellId == 15 &&
            getStatLevel(player, Skills.MAGIC) >= 25 &&
            inInventory(player, Items.FIRE_RUNE_554, 1) &&
            inInventory(player, Items.LAW_RUNE_563, 1) &&
            inInventory(player, Items.AIR_RUNE_556, 3) &&
            !hasTimerActive(player, GameAttributes.TELEBLOCK_TIMER)) {
            if (WildernessZone.isInZone(player)) {
                return
            }
            finishTask(
                player,
                DiaryLevel.MEDIUM,
                MediumTasks.CAST_VARROCK_TELEPORT_SPELL,
            )
        }
        when {
            inBorders(player, AIR_OBELISK_AREA) -> {
                if (event.spellBook == SpellBookManager.SpellBook.MODERN && event.spellId == 49) {
                    fulfillTaskRequirement(
                        player,
                        DiaryLevel.HARD,
                        HardTasks.CRAFT_AIR_BATTLESTAFF,
                        ATTRIBUTE_CRAFT_AIR_BATTLESTAFF,
                    )
                }
            }
        }
    }

    override fun onFairyRingDialed(player: Player, event: FairyRingDialEvent) {
        if (event.fairyRing == FairyRing.DKR) {
            finishTask(
                player,
                DiaryLevel.MEDIUM,
                MediumTasks.DIAL_FAIRY_RING_WEST,
            )
        }
    }

    override fun onPickedUp(player: Player, event: PickUpEvent) {
        when {
            inBorders(player, FARMING_PATCH_AREA) -> {
                if (event.itemId == Items.POISON_IVY_BERRIES_6018) {
                    finishTask(
                        player,
                        DiaryLevel.HARD,
                        HardTasks.PICK_POISON_IVY_FARMING_PATCH,
                    )
                }
            }
        }
    }

    override fun onDialogueOpened(player: Player, event: DialogueOpenEvent) {
        when (event.dialogue) {
            is CuratorHaigHalenDialogue -> {
                if (getQuestPoints(player) == 50) {
                    finishTask(
                        player,
                        DiaryLevel.EASY,
                        EasyTasks.SPEAK_TO_HAIG_HALEN_50QP,
                    )
                }
            }
        }
    }
}
