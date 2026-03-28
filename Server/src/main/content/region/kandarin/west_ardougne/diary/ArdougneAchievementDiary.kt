package content.region.kandarin.west_ardougne.diary

import content.global.skill.gather.fishing.Fish
import content.global.skill.magic.TeleportMethod
import content.global.travel.fairyring.FairyRing
import content.region.kandarin.port_khazard.dialogue.TindelMerchantDialogue
import content.region.kandarin.west_ardougne.dialogue.ChiefServantDialogue
import content.region.kandarin.west_ardougne.dialogue.CivilianDialogue
import content.region.kandarin.west_ardougne.dialogue.RecruiterDialogue
import content.region.kandarin.witchaven.dialogue.EzekialLovecraftDialogue
import content.region.kandarin.yanille.dialogue.AleckDialogue
import core.api.hasRequirement
import core.api.inBorders
import core.game.diary.DiaryAreaTask
import core.game.diary.DiaryEventHookBase
import core.game.diary.DiaryLevel
import core.game.event.*
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.diary.DiaryType
import core.game.node.item.Item
import core.game.world.map.Location
import core.game.world.map.zone.ZoneBorders
import shared.consts.*

class ArdougneAchievementDiary : DiaryEventHookBase(DiaryType.ARDOUGNE) {
    private val COMBAT_TRAINING_CAMP_AREA = ZoneBorders(2516, 3357, 2519, 3360)
    private val ARDOUGNE_NATURE_RUNE_CHEST_AREA = ZoneBorders(2670, 3298, 2678, 3304, 1, true)
    private val POISON_ARROW_AREA = ZoneBorders(2658, 3271, 2667, 3277)

    private val RING_OF_DUELING = arrayOf(Items.RING_OF_DUELLING8_2552, Items.RING_OF_DUELLING7_2554, Items.RING_OF_DUELLING6_2556, Items.RING_OF_DUELLING5_2558, Items.RING_OF_DUELLING4_2560, Items.RING_OF_DUELLING3_2562, Items.RING_OF_DUELLING2_2564, Items.RING_OF_DUELLING1_2566)
    private val WIZARD_PORTAL = intArrayOf(Scenery.MAGIC_PORTAL_2156, Scenery.MAGIC_PORTAL_2157, Scenery.MAGIC_PORTAL_2158)

    private val CASTLE_WARS_LOCATION = Location(2442, 3089)
    private val THORMAC_TOWER_LOCATION = Location(2703, 3406)

    private val MONASTERY_REGION = 10290
    private val BATTLEFIELD_REGION = 10034
    private val CREATURE_CREATION_UNDERGROUND_REGION = 12100
    private val CASTLE_WAR_REGION = 9776
    private val OBSERVATORY_REGION = 9777
    private val PLAGUE_CITY_REGION = 10035
    private val LEGEND_GUILD_BASEMENT = 10904
    private val OURANIA_SALAMANDERS_REGION = 9778
    private val FISHING_PLATFORM_REGION = Regions.FISHING_PLATFORM_11059
    private val CHAOS_DRUID_TOWER_UNDERGROUND_REGION = 10392
    private val FIGHT_ARENA_REGION = 10545

    companion object {
        object EasyTasks {
            const val WIZARD_CROMPERTY_TELEPORT_TO_ESSENCE_MINE = 0
            const val STEAL_FROM_STALL_OR_GUARD = 1 // Outside
            const val SELL_SILK_TO_TRADER = 2
            const val USE_ALTAR_IN_WEST_ARD = 3
            const val ENTER_CASTLE_WARS_WAITING_ROOM = 4
            const val FISH_ON_FISHING_TRAWLER = 5 // Outside
            const val ENTER_COMBAT_TRAINING_CAMP = 6
            const val TALK_TO_CIVILIAN_ABOUT_CAT = 7
            const val KILL_UNICOW_IN_TOWER_OF_LIFE = 8
            const val GET_CIVILIAN_TO_THROW_TOMATO = 9
            const val SAIL_TO_KARAMJA = 10 // Outside
            const val PICK_LOCK_DOOR_EAST_OF_GEM_STALL = 11 // Outside
            const val SPEND_PENGUIN_POINTS_AT_ZOO = 12 // Outside
            const val USE_SUMMONING_OBELISK = 13 // Outside
            const val POP_BALLOON_IN_MONASTERY = 14 // Outside
            const val BUY_WATER_VIAL_FROM_GENERAL_STORE = 15
            const val USE_NOTICEBOARD_NEAR_OBSERVATORY = 16
            const val KILL_SOMETHING_ON_KHAZARD_BATTLEFIELD = 17
            const val BUY_SKEWERED_KEBAB_AT_POISON_ARROW = 18
            const val VIEW_HUNTER_EQUIPMENT_IN_ALECKS_SHOP = 19
            const val TALK_TO_HEAD_SERVANT_AT_SERVANTS_GUILD = 20
            const val USE_RING_OF_DUELING_TO_CASTLE_WARS = 21
            const val TALK_TO_TINDEL_MERCHANT_ABOUT_SWORDS = 22
        }

        object MediumTasks {
            const val ENTER_UNICORN_PEN_USING_FAIRY_RINGS = 0
            const val TELEPORT_TO_WILDERNESS_USING_LEVER = 1 // Outside
            const val GRAPPLE_OVER_YANILLE_SOUTHERN_WALL = 2 // Outside
            const val CRAFT_RUNES_AT_OURANIA_ALTAR = 3 // Outside
            const val SELL_RUBIUM_TO_EZEKIAL_LOVECRAFT = 4
            const val PICK_WATERMELONS_FROM_FARMING_PATCH = 5 // Outside
            const val CAST_WATCHTOWER_TELEPORT = 6 // Outside
            const val TRAVEL_TO_CASTLE_WARS_BY_BALLOON = 7 // Outside
            const val CLAIM_BUCKETS_OF_SAND_FROM_BERT = 8 // Outside
            const val RETURN_TO_PAST_AND_TALK_TO_SARAH = 9 // Outside
            const val CATCH_FISH_AT_FISHING_PLATFORM = 10
            const val CROSS_RIVER_DOUGNE_USING_LOG_BALANCE = 11 // Outside
            const val PICKPOCKET_MASTER_FARMER = 12 // Outside
            const val STEAL_NATURE_RUNE_FROM_CHEST = 13
            const val MINE_COAL_EAST_OF_ARD = 14
            const val KILL_SWORDCHICK_IN_TOWER_OF_LIFE = 15
        }

        object HardTasks {
            const val RECHARGE_BRACELET_OR_NECKLACE_AT_LEGENDS_GUILD = 0 // Outside
            const val KILL_SHADOW_WARRIOR_IN_LEGENDS_GUILD_BASEMENT = 1
            const val ENTER_MAGIC_GUILD_IN_YANILLE = 2 // Outside
            const val USE_MAGIC_GUILD_PORTAL_TO_THORMACS_TOWER = 3
            const val BE_ON_WINNING_SIDE_IN_CASTLE_WARS = 4
            const val CAST_OURANIA_TELEPORT_SPELL = 5 // Outside
            const val PICKPOCKET_WATCHMAN_WHILE_WEARING_GLOVES = 6 // Outside
            const val KILL_FROGEEL_IN_TOWER_OF_LIFE = 7
            const val ZOOKEEPER_PUT_YOU_IN_MONKEY_CAGE = 8 // Outside
            const val KILL_OWN_JADE_VINE_AFTER_BACK_TO_MY_ROOTS = 9
            const val USE_AIR_GUITAR_EMOTE_NEAR_MUSICIAN = 10 // Outside
            const val CAST_ARD_TELEPORT_SPELL = 11 // Outside
            const val CROSS_MONKEY_BARS_IN_AGILITY_DUNGEON = 12 // Outside
            const val CATCH_RED_SALAMANDER_OUTSIDE_OURANIA_ALTAR = 13
            const val PICK_PAPAYA_OR_COCONUT_NEAR_TREE_GNOME_VILLAGE = 14 // Outside
            const val STEAL_BLOOD_RUNES_FROM_CHAOS_DRUID_TOWER = 15
            const val USE_CATAPULT_IN_CASTLE_WARS_AFTER_CONSTRUCTION = 16
        }
    }

    override val areaTasks
        get() =
            arrayOf(
                DiaryAreaTask(
                    COMBAT_TRAINING_CAMP_AREA,
                    DiaryLevel.EASY,
                    EasyTasks.ENTER_COMBAT_TRAINING_CAMP,
                )
                ,
                DiaryAreaTask(
                    POISON_ARROW_AREA,
                    DiaryLevel.EASY,
                    EasyTasks.BUY_SKEWERED_KEBAB_AT_POISON_ARROW,
                )
            )

    override fun onPrayerPointsRecharged(player: Player, event: PrayerPointsRechargeEvent) {
        if (player.viewport.region!!.id == PLAGUE_CITY_REGION && event.altar.id == Scenery.ALTAR_34616) {
            finishTask(
                player,
                DiaryLevel.EASY,
                EasyTasks.USE_ALTAR_IN_WEST_ARD
            )
        }
    }

    override fun onInteracted(player: Player, event: InteractionEvent) {
        when (player.viewport.region!!.id) {
            OBSERVATORY_REGION -> if (event.target.id == Scenery.SHOOTING_STAR_NOTICEBOARD_38669 && event.option == "read") {
                finishTask(
                    player,
                    DiaryLevel.EASY,
                    EasyTasks.USE_NOTICEBOARD_NEAR_OBSERVATORY,
                )
            }
            CASTLE_WAR_REGION -> if(event.target.id in intArrayOf(Scenery.ZAMORAK_PORTAL_4388, Scenery.GUTHIX_PORTAL_4408, Scenery.SARADOMIN_PORTAL_4387)  && event.option == "enter") {
                finishTask(
                    player,
                    DiaryLevel.EASY,
                    EasyTasks.ENTER_CASTLE_WARS_WAITING_ROOM,
                )
                finishTask(
                    player,
                    DiaryLevel.HARD,
                    HardTasks.BE_ON_WINNING_SIDE_IN_CASTLE_WARS,
                )
                if(hasRequirement(player, Quests.CATAPULT_CONSTRUCTION)) {
                    finishTask(
                        player,
                        DiaryLevel.HARD,
                        HardTasks.USE_CATAPULT_IN_CASTLE_WARS_AFTER_CONSTRUCTION
                    )
                }
            }
         }
    }

    override fun onDialogueOptionSelected(player: Player, event: DialogueOptionSelectionEvent) {
        when (event.dialogue) {
            is CivilianDialogue -> {
                if (event.currentStage == 3) {
                    finishTask(
                        player,
                        DiaryLevel.EASY,
                        EasyTasks.TALK_TO_CIVILIAN_ABOUT_CAT,
                    )
                }
            }

            is TindelMerchantDialogue -> {
                if (event.currentStage == 5) {
                    finishTask(
                        player,
                        DiaryLevel.EASY,
                        EasyTasks.TALK_TO_TINDEL_MERCHANT_ABOUT_SWORDS,
                    )
                }
            }

            is AleckDialogue -> {
                if (event.currentStage == 10) {
                    finishTask(
                        player,
                        DiaryLevel.EASY,
                        EasyTasks.VIEW_HUNTER_EQUIPMENT_IN_ALECKS_SHOP,
                    )
                }
            }

            is RecruiterDialogue -> {
                if(event.currentStage == 3) {
                    finishTask(
                        player,
                        DiaryLevel.EASY,
                        EasyTasks.GET_CIVILIAN_TO_THROW_TOMATO,
                    )
                }
            }
        }
    }

    override fun onNpcKilled(player: Player, event: NPCKillEvent) {
        if (event.npc.id == NPCs.WILD_JADE_VINE_3412) {
            finishTask(
                player,
                DiaryLevel.HARD,
                HardTasks.KILL_OWN_JADE_VINE_AFTER_BACK_TO_MY_ROOTS,
            )
        }

        when (player.viewport.region!!.id) {
            CREATURE_CREATION_UNDERGROUND_REGION -> {
                if (event.npc.id == NPCs.UNICOW_5603) {
                    finishTask(
                        player,
                        DiaryLevel.EASY,
                        EasyTasks.KILL_UNICOW_IN_TOWER_OF_LIFE,
                    )
                }
                if (event.npc.id == NPCs.SWORDCHICK_5595) {
                    finishTask(
                        player,
                        DiaryLevel.MEDIUM,
                        MediumTasks.KILL_SWORDCHICK_IN_TOWER_OF_LIFE,
                    )
                }
                if (event.npc.id == NPCs.FROGEEL_5593) {
                    finishTask(
                        player,
                        DiaryLevel.HARD,
                        HardTasks.KILL_FROGEEL_IN_TOWER_OF_LIFE,
                    )
                }
            }

            BATTLEFIELD_REGION -> {
                val name = event.npc.name.lowercase()
                if (name.contains("gnome") || name.contains("khazard")) {
                    finishTask(
                        player,
                        DiaryLevel.EASY,
                        EasyTasks.KILL_SOMETHING_ON_KHAZARD_BATTLEFIELD
                    )
                }
            }

            LEGEND_GUILD_BASEMENT -> if (event.npc.id == NPCs.SHADOW_WARRIOR_158) {
                finishTask(
                    player,
                    DiaryLevel.HARD,
                    HardTasks.KILL_SHADOW_WARRIOR_IN_LEGENDS_GUILD_BASEMENT
                )
            }
        }
    }

    override fun onItemSoldToShop(player: Player, event: ItemShopSellEvent) {
        if (event.itemId == Items.SILK_950) {
            finishTask(
                player,
                DiaryLevel.EASY,
                EasyTasks.SELL_SILK_TO_TRADER,
            )
        }
    }

    override fun onItemPurchasedFromShop(player: Player, event: ItemShopPurchaseEvent) {
        if (event.itemId == Items.VIAL_OF_WATER_227) {
            finishTask(
                player,
                DiaryLevel.EASY,
                EasyTasks.BUY_WATER_VIAL_FROM_GENERAL_STORE,
            )
        }
    }

    override fun onTeleported(player: Player, event: TeleportEvent) {
        when (event.source) {
            is Item -> if (event.method == TeleportMethod.JEWELRY && event.source.id in RING_OF_DUELING) {
                if (event.location == CASTLE_WARS_LOCATION) {
                    finishTask(
                        player,
                        DiaryLevel.EASY,
                        EasyTasks.USE_RING_OF_DUELING_TO_CASTLE_WARS,
                    )
                }
            }

            is core.game.node.scenery.Scenery -> if (event.source.id in WIZARD_PORTAL) {
                if (event.location == THORMAC_TOWER_LOCATION) {
                    finishTask(
                        player,
                        DiaryLevel.HARD,
                        HardTasks.USE_MAGIC_GUILD_PORTAL_TO_THORMACS_TOWER,
                    )
                }
            }

            is NPC ->
                if (event.method == TeleportMethod.NPC && event.source.id == NPCs.WIZARD_CROMPERTY_2328) {
                    finishTask(
                        player,
                        DiaryLevel.EASY,
                        EasyTasks.WIZARD_CROMPERTY_TELEPORT_TO_ESSENCE_MINE,
                    )
                }
        }
    }

    override fun onResourceProduced(player: Player, event: ResourceProducedEvent) {
        when (player.viewport.region!!.id) {
            MONASTERY_REGION -> {
                if (event.source.id == Scenery.ROCKS_2097 && event.itemId == Items.COAL_453) {
                    finishTask(
                        player,
                        DiaryLevel.MEDIUM,
                        MediumTasks.MINE_COAL_EAST_OF_ARD,
                    )
                }
            }

            OURANIA_SALAMANDERS_REGION -> {
                if (event.source.id == Scenery.YOUNG_TREE_19663 && event.itemId == Items.RED_SALAMANDER_10147) {
                    finishTask(
                        player,
                        DiaryLevel.HARD,
                        HardTasks.CATCH_RED_SALAMANDER_OUTSIDE_OURANIA_ALTAR,
                    )
                }
            }

            FISHING_PLATFORM_REGION -> {
                if (event.source.id == NPCs.FISHING_SPOT_316) {
                    if (event.itemId == Fish.SARDINE.id || event.itemId == Fish.HERRING.id || event.itemId == Fish.ANCHOVY.id) {
                        finishTask(
                            player,
                            DiaryLevel.MEDIUM,
                            MediumTasks.CATCH_FISH_AT_FISHING_PLATFORM,
                        )
                    }
                }
            }

            CHAOS_DRUID_TOWER_UNDERGROUND_REGION -> {
                if (event.source.id == Scenery.CHEST_2569 && event.itemId == Items.BLOOD_RUNE_565) {
                    finishTask(
                        player,
                        DiaryLevel.HARD,
                        HardTasks.STEAL_BLOOD_RUNES_FROM_CHAOS_DRUID_TOWER,
                    )
                }
            }
        }

        if (inBorders(player, ARDOUGNE_NATURE_RUNE_CHEST_AREA)) {
            if (event.source.id == Scenery.CHEST_2567 && event.itemId == Items.NATURE_RUNE_561) {
                finishTask(
                    player,
                    DiaryLevel.MEDIUM,
                    MediumTasks.STEAL_NATURE_RUNE_FROM_CHEST,
                )
            }
        }
    }

    override fun onDialogueOpened(player: Player, event: DialogueOpenEvent) {
        when (event.dialogue) {
            is ChiefServantDialogue -> {
                finishTask(
                    player,
                    DiaryLevel.EASY,
                    EasyTasks.TALK_TO_HEAD_SERVANT_AT_SERVANTS_GUILD,
                )
            }
            is EzekialLovecraftDialogue -> {
                finishTask(
                    player,
                    DiaryLevel.MEDIUM,
                    MediumTasks.SELL_RUBIUM_TO_EZEKIAL_LOVECRAFT,
                )
            }
        }
    }

    override fun onFairyRingDialed(player: Player, event: FairyRingDialEvent) {
        if (event.fairyRing == FairyRing.BIS) {
            finishTask(
                player,
                DiaryLevel.MEDIUM,
                MediumTasks.ENTER_UNICORN_PEN_USING_FAIRY_RINGS,
            )
        }
    }

    override fun onSummoningPointsRecharged(player: Player, event: SummoningPointsRechargeEvent) {
        when (player.viewport.region!!.id) {
            FIGHT_ARENA_REGION -> {
                if (event.obelisk.id == Scenery.SMALL_OBELISK_29954) {
                    finishTask(
                        player,
                        DiaryLevel.EASY,
                        EasyTasks.USE_SUMMONING_OBELISK,
                    )
                }
            }
        }
    }

}