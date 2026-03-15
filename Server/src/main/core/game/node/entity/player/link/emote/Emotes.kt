package core.game.node.entity.player.link.emote

import core.api.*
import core.game.container.impl.EquipmentContainer
import core.game.node.entity.player.Player
import core.game.node.entity.player.info.Rights
import core.game.node.entity.player.link.diary.DiaryType
import core.game.system.task.Pulse
import core.game.world.map.Direction
import core.game.world.map.Location
import core.game.world.map.RegionManager.getLocalNpcs
import core.game.world.update.flag.context.Animation
import core.game.world.update.flag.context.Graphics
import shared.consts.Animations
import shared.consts.Items
import shared.consts.NPCs
import shared.consts.Quests

enum class Emotes(
    val buttonId: Int,
    val animation: Int? = -1,
    val graphics: Int? = -1,
    val lockedMessage: String? = null,
) {
    YES(
        buttonId = 2,
        animation = Animations.HUMAN_SHAKE_HEAD_YES_855
    ),
    NO(
        buttonId = 3,
        animation = Animations.HUMAN_SHAKE_HEAD_NO_856
    ),
    BOW(
        buttonId = 4,
        animation = Animations.BOW_858
    ) {
        override fun play(player: Player) {
            val hat = player.equipment[EquipmentContainer.SLOT_LEGS]
            if (hat?.id == Items.PANTALOONS_10396) {
                forceEmote(player, Animation(Animations.HUMAN_CURTSY_5312), Graphics(-1))
            } else {
                super.play(player)
            }
        }
    },
    ANGRY(
        buttonId = 5,
        animation = Animations.ANGRY_859
    ) {
        override fun play(player: Player) {
            val hat = player.equipment[EquipmentContainer.SLOT_HAT]
            if (hat?.id == Items.A_POWDERED_WIG_10392) {
                forceEmote(player, Animation(Animations.HUMAN_ANGRY_ENHANCED_5315), Graphics(-1))
            } else {
                super.play(player)
            }
        }
    },
    THINK(
        buttonId = 6,
        animation = Animations.HUMAN_THINK_857
    ),
    WAVE(
        buttonId = 7,
        animation = Animations.HUMAN_WAVE_863
    ) {
        override fun play(player: Player) {
            val weapon = player.equipment[EquipmentContainer.SLOT_WEAPON]
            if (weapon?.id == Items.SLED_4084) {
                forceEmote(player, Animation(Animations.WAVE_ON_SLED_1483), Graphics(-1))
            } else {
                super.play(player)
            }
        }
    },
    SHRUG(
        buttonId = 8,
        animation = Animations.HUMAN_SHRUG_2113
    ),
    CHEER(
        buttonId = 9,
        animation = Animations.CHEER_862
    ) {
        override fun play(player: Player) {
            val weapon = player.equipment[EquipmentContainer.SLOT_WEAPON]
            if (weapon?.id == Items.SLED_4084) {
                forceEmote(player, Animation(Animations.CHEER_ON_SLED_1482), Graphics(-1))
            } else {
                super.play(player)
            }
        }
    },
    BECKON(
        buttonId = 10,
        animation = Animations.BECKON_864
    ) {
        override fun play(player: Player) {
            val weapon = player.equipment[EquipmentContainer.SLOT_WEAPON]
            val requiredItem = anyInEquipment(player, Items.HARD_HAT_10862, Items.BUILDERS_SHIRT_10863, Items.BUILDERS_TROUSERS_10864, Items.BUILDERS_BOOTS_10865, Items.PLAIN_SATCHEL_10877, Items.GREEN_SATCHEL_10878, Items.RED_SATCHEL_10879, Items.BLACK_SATCHEL_10880, Items.GOLD_SATCHEL_10881, Items.RUNE_SATCHEL_10882)
            when {
                weapon?.id == Items.SLED_4084 -> forceEmote(player, Animation(Animations.BECKON_ON_SLED_1484), Graphics(-1))
                requiredItem -> forceEmote(player, Animation(Animations.EMOTE_BECKON_5845), Graphics(-1))
                else -> super.play(player)
            }
        }
    },
    JUMP_FOR_JOY(
        buttonId = 11,
        animation = Animations.HUMAN_JUMP_FOR_JOY_2109
    ),
    LAUGH(
        buttonId = 12,
        animation = Animations.HUMAN_LAUGH_861
    ),
    YAWN(
        buttonId = 13,
        animation = Animations.HUMAN_YAWN_2111
    ) {
        override fun play(player: Player) {
            val hat = player.equipment[EquipmentContainer.SLOT_HAT]
            if (hat?.id == Items.SLEEPING_CAP_10398) {
                forceEmote(player, Animation(Animations.HUMAN_YAWN_ENHANCED_5313), Graphics(-1))
            } else {
                super.play(player)
            }
        }
    },
    DANCE(
        buttonId = 14,
        animation = Animations.DANCE_866
    ) {
        override fun play(player: Player) {
            val legs = player.equipment[EquipmentContainer.SLOT_LEGS]
            if (legs?.id == Items.FLARED_TROUSERS_10394) {
                forceEmote(player, Animation(Animations.FLARED_TROUSERS_DANCE_EMOTE_E_5316), Graphics(-1))
            } else {
                super.play(player)
            }
        }
    },
    JIG(
        buttonId = 15,
        animation = Animations.HUMAN_JIG_2106
    ),
    SPIN(
        buttonId = 16,
        animation = Animations.HUMAN_TWIRL_2107
    ),
    HEADBANG(
        buttonId = 17,
        animation = Animations.HEADBANG_EMOTE_2108
    ),
    CRY(
        buttonId = 18,
        animation = Animations.CRY_860
    ),
    BLOW_KISS(
        buttonId = 19,
        animation = Animations.HUMAN_BLOW_KISS_1374,
        graphics = shared.consts.Graphics.HEART_574
    ),
    PANIC(
        buttonId = 20,
        animation = Animations.HUMAN_PANIC_2105
    ),
    RASPBERRY(
        buttonId = 21,
        animation = Animations.HUMAN_BLOW_RASPBERRY_2110
    ),
    CLAP(
        buttonId = 22,
        animation = Animations.CLAP_865
    ) {
        override fun play(player: Player) {
            val weapon = player.equipment[EquipmentContainer.SLOT_WEAPON]
            if (weapon?.id == Items.SLED_4084) {
                forceEmote(player, Animation(Animations.CLAP_ON_SLED_1485), Graphics(-1))
            } else {
                super.play(player)
            }
        }
    },
    SALUTE(
        buttonId = 23,
        animation = Animations.HUMAN_SALUTE_2112
    ) {
        override fun play(player: Player) {
            if (isEligibleForSalute(player)) {
                forceEmote(player, Animation(Animations.HUMAN_SALUTE_2112), Graphics(-1))
                finishDiaryTask(player, DiaryType.FALADOR, 1, 8)
            } else {
                super.play(player)
            }
        }
    },
    GOBLIN_BOW(
        buttonId = 24,
        animation = Animations.GOBLIN_BOW_EMOTE_E_2127,
        lockedMessage = "This emote can be unlocked during the Lost Tribe quest.",
    ) {
        override fun play(player: Player) {
            if (isEligibleForGoblinBow(player) &&
                player.getQuestRepository().getQuest(Quests.THE_LOST_TRIBE).getStage(player) == 45
            ) {
                val mistag = getLocalNpcs(player).firstOrNull { npc ->
                    npc.id == NPCs.MISTAG_2084 && withinDistance(npc, player.location, 3)
                }

                mistag?.let { npc ->
                    player.dialogueInterpreter.open(NPCs.MISTAG_2084, npc, "greeting")
                    setAttribute(player, "/save:mistag-greeted", true)
                }
            }
            super.play(player)
        }
    },
    GOBLIN_SALUTE(
        buttonId = 25,
        animation = Animations.GOBLIN_SALUTE_EMOTE_E_2128,
        lockedMessage = "This emote can be unlocked during the Lost Tribe quest.",
    ),
    GLASS_BOX(
        buttonId = 26,
        animation = Animations.GLASS_BOX_EMOTE_1131,
        lockedMessage = "This emote can be unlocked during the Mime random event.",
    ),
    CLIMB_ROPE(
        buttonId = 27,
        animation = Animations.CLIMB_ROPE_EMOTE_1130,
        lockedMessage = "This emote can be unlocked during the Mime random event.",
    ),
    LEAN_ON_AIR(
        buttonId = 28,
        animation = Animations.HUMAN_LEAN_1129,
        lockedMessage = "This emote can be unlocked during the Mime random event.",
    ),
    GLASS_WALL(
        buttonId = 29,
        animation = Animations.GLASS_WALL_EMOTE_1128,
        lockedMessage = "This emote can be unlocked during the Mime random event.",
    ),
    IDEA(
        buttonId = 33,
        animation = Animations.HUMAN_IDEA_4276,
        graphics = shared.consts.Graphics.LIGHT_BULB_IDEA_EMOTE_712,
        lockedMessage = "You can't use this emote yet. Visit the Stronghold of Security to unlock it.",
    ),
    STOMP(
        buttonId = 31,
        animation = Animations.HUMAN_STOMP_4278,
        lockedMessage = "You can't use this emote yet. Visit the Stronghold of Security to unlock it.",
    ),
    FLAP(
        buttonId = 32,
        animation = Animations.FLAP_EMOTE_E_4280,
        lockedMessage = "You can't use this emote yet. Visit the Stronghold of Security to unlock it.",
    ) {
        override fun play(player: Player) {
            if (isWearingChickenOutfit(player)) {
                forceEmote(player, Animation(Animations.CHICKEN_SUIT_FLAP_EMOTE_E_3859), Graphics(-1))
            } else {
                super.play(player)
            }
        }
    },
    SLAP_HEAD(
        buttonId = 30,
        animation = Animations.HUMAN_SLAP_HEAD_4275,
        lockedMessage = "You can't use this emote yet. Visit the Stronghold of Security to unlock it.",
    ),
    ZOMBIE_WALK(
        buttonId = 34,
        animation = Animations.HUMAN_ZOMBIE_WALK_3544,
        lockedMessage = "This emote can be unlocked during the Gravedigger random event.",
    ),
    ZOMBIE_DANCE(
        buttonId = 35,
        animation = Animations.HUMAN_ZOMBIE_DANCE_3543,
        lockedMessage = "This emote can be unlocked during the Gravedigger random event.",
    ),
    ZOMBIE_HAND(
        buttonId = 36,
        animation = Animations.HUMAN_ZOMBIE_HAND_7272,
        graphics = shared.consts.Graphics.ZOMBIE_HAND_EMOTE_TAB_1244,
        lockedMessage = "This emote can be unlocked during the Gravedigger random event.",
    ),
    SCARED(
        buttonId = 37,
        animation = Animations.HUMAN_SCARED_2836,
        lockedMessage = "This emote can be unlocked by playing a Halloween holiday event.",
    ),
    BUNNY_HOP(
        buttonId = 38,
        animation = Animations.BUNNY_HOP_EMOTE_E_6111,
        lockedMessage = "This emote can be unlocked by playing an Easter holiday event.",
    ),
    SKILLCAPE(
        buttonId = 39
    ) {
        override fun play(player: Player) {
            val cape = player.equipment[EquipmentContainer.SLOT_CAPE]
            if (cape == null) {
                sendMessage(player, "You need to be wearing a skillcape in order to perform this emote.")
                return
            }
            val capeId = cape.id
            SKILLCAPE_INFO.firstOrNull { capeId == it[0] || capeId == it[1] }?.let { info ->
                visualize(player, info[3], info[2])
                player.locks.lock("emote", Animation.create(info[3]).duration)
            } ?: sendMessage(player, "You need to be wearing a skillcape in order to perform this emote.")
        }
    },
    SNOWMAN_DANCE(
        buttonId = 40,
        animation = Animations.HUMAN_SNOWMAN_DANCE_7531,
        lockedMessage = "This emote can be unlocked by playing a Christmas holiday event.",
    ),
    AIR_GUITAR(
        buttonId = 41,
        animation = Animations.HUMAN_AIR_GUITAR_2414,
        graphics = 1537,
        lockedMessage = "This emote can be accessed by unlocking 200 pieces of music.",
    ) {
        override fun play(player: Player) {
            playJingle(player, 302)
            if(!hasDiaryTaskComplete(player, DiaryType.ARDOUGNE, 2, 10)) {
                finishDiaryTask(player, DiaryType.ARDOUGNE, 2, 10)
            }
            super.play(player)
        }
    },
    SAFETY_FIRST(
        buttonId = 42,
        animation = Animations.HUMAN_SAFETY_FIRST_8770,
        graphics = shared.consts.Graphics.SAFETY_FIRST_1553,
        lockedMessage = "You can't use this emote yet. Visit the Stronghold of Player safety to unlock it.",
    ),
    EXPLORE(
        buttonId = 43,
        animation = Animations.HUMAN_EXPLORE_9990,
        graphics = shared.consts.Graphics.EXPLORE_EMOTE_1734,
        lockedMessage = "You can't use this emote yet. You must complete all the Lumbridge and Draynor beginner tasks to unlock it.",
    ),
    TRICK(
        buttonId = 44,
        animation = Animations.HUMAN_TRICK_10530,
        graphics = shared.consts.Graphics.TRICK_1863,
        lockedMessage = "This emote can be unlocked by playing a Halloween holiday event.",
    ),
    FREEZE(
        buttonId = 45,
        animation = Animations.HUMAN_FREEZE_MELT_11044,
        graphics = shared.consts.Graphics.FREEZE_EMOTE_1973,
        lockedMessage = "This emote can be unlocked by playing a Christmas holiday event.",
    ),
    GIVE_THANKS(
        buttonId = 46,
        lockedMessage = "This emote can be unlocked by playing a Thanksgiving holiday event.",
    ) {
        override fun play(player: Player) {
            lock(player, 17)
            submitWorldPulse(
                object : Pulse(1, player) {
                    var counter: Int = 0

                    override fun pulse(): Boolean {
                        when (counter++) {
                            1 -> forceEmote(
                                player,
                                Animation(Animations.GIVE_THANKS_BEGIN_10994),
                                Graphics(shared.consts.Graphics.RE_PUFF_86),
                            )

                            3 -> player.appearance.transformNPC(NPCs.THANKSGIVING_TURKEY_8499).also {
                                forceEmote(player, Animation(Animations.HUMAN_TURKEY_DANCE_10996), Graphics(-1))
                            }

                            13 -> player.appearance.transformNPC(NPCs.TURKEY_8501)
                            16 -> player.appearance.transformNPC(-1).also {
                                forceEmote(
                                    player,
                                    Animation(Animations.GIVE_THANKS_END_10995),
                                    Graphics(shared.consts.Graphics.RE_PUFF_86),
                                )
                            }
                        }
                        return false
                    }
                },
            )
        }
    }, ;

    /**
     * Plays the emote.
     *
     * @param player the player.
     */
    open fun play(player: Player) {
        forceEmote(player, Animation(animation!!), Graphics(graphics!!))
    }

    constructor(buttonId: Int, animation: Int?, lockedMessage: String?) : this(
        buttonId = buttonId,
        animation = animation,
        graphics = -1,
        lockedMessage = lockedMessage,
    )

    constructor(buttonId: Int, lockedMessage: String?) : this(
        buttonId = buttonId,
        animation = -1,
        graphics = -1,
        lockedMessage = lockedMessage,
    )

    companion object {
        private val SKILLCAPE_INFO = arrayOf(
            intArrayOf(
                Items.ATTACK_CAPE_9747,
                Items.ATTACK_CAPET_9748,
                shared.consts.Graphics.ATTACK_SKILLCAPE_823,
                Animations.ATTACK_SKILLCAPE_4959,
            ),
            intArrayOf(
                Items.STRENGTH_CAPE_9750,
                Items.STRENGTH_CAPET_9751,
                shared.consts.Graphics.STRENGTH_SKILLCAPE_828,
                Animations.STRENGTH_SKILLCAPE_4981,
            ),
            intArrayOf(
                Items.DEFENCE_CAPE_9753,
                Items.DEFENCE_CAPET_9754,
                shared.consts.Graphics.DEFENCE_SKILLCAPE_824,
                Animations.DEFENCE_SKILLCAPE_4961,
            ),
            intArrayOf(
                Items.RANGING_CAPE_9756,
                Items.RANGING_CAPET_9757,
                shared.consts.Graphics.RANGED_SKILLCAPE_832,
                Animations.RANGED_SKILLCAPE_4973,
            ),
            intArrayOf(
                Items.PRAYER_CAPE_9759,
                Items.PRAYER_CAPET_9760,
                shared.consts.Graphics.PRAYER_SKILLCAPE_829,
                Animations.PRAYER_SKILLCAPE_4979,
            ),
            intArrayOf(
                Items.MAGIC_CAPE_9762,
                Items.MAGIC_CAPET_9763,
                shared.consts.Graphics.MAGIC_SKILLCAPE_813,
                Animations.MAGIC_SKILLCAPE_4939,
            ),
            intArrayOf(
                Items.RUNECRAFT_CAPE_9765,
                Items.RUNECRAFT_CAPET_9766,
                shared.consts.Graphics.RC_SKILLCAPE_817,
                Animations.RC_SKILLCAPE_4947,
            ),
            intArrayOf(
                Items.HITPOINTS_CAPE_9768,
                Items.HITPOINTS_CAPET_9769,
                shared.consts.Graphics.HP_SKILLCAPE_MALE_833,
                Animations.HP_SKILLCAPE_4971,
            ),
            intArrayOf(
                Items.AGILITY_CAPE_9771,
                Items.AGILITY_CAPET_9772,
                shared.consts.Graphics.AGILITY_SKILLCAPE_830,
                Animations.AGILITY_SKILLCAPE_4977,
            ),
            intArrayOf(
                Items.HERBLORE_CAPE_9774,
                Items.HERBLORE_CAPET_9775,
                shared.consts.Graphics.HERBLORE_SKILLCAPE_835,
                Animations.HERBLORE_SKILLCAPE_4969,
            ),
            intArrayOf(
                Items.THIEVING_CAPE_9777,
                Items.THIEVING_CAPET_9778,
                shared.consts.Graphics.THIEVING_SKILLCAPE_826,
                Animations.THIEVING_SKILLCAPE_4965,
            ),
            intArrayOf(
                Items.CRAFTING_CAPE_9780,
                Items.CRAFTING_CAPET_9781,
                shared.consts.Graphics.CRAFTING_SKILLCAPE_818,
                Animations.CRAFTING_SKILLCAPE_4949,
            ),
            intArrayOf(
                Items.FLETCHING_CAPE_9783,
                Items.FLETCHING_CAPET_9784,
                shared.consts.Graphics.FLETCHING_SKILLCAPE_812,
                Animations.FLETCHING_SKILLCAPE_4937,
            ),
            intArrayOf(
                Items.SLAYER_CAPE_9786,
                Items.SLAYER_CAPET_9787,
                shared.consts.Graphics.SLAYER_ACCOMPLISHMENT_CAPE_1656,
                Animations.SLAYER_SKILLCAPE_4967,
            ),
            intArrayOf(
                Items.CONSTRUCT_CAPE_9789,
                Items.CONSTRUCT_CAPET_9790,
                shared.consts.Graphics.CON_SKILLCAPE_820,
                Animations.CONSTRUCTION_SKILLCAPE_4953,
            ),
            intArrayOf(
                Items.MINING_CAPE_9792,
                Items.MINING_CAPET_9793,
                shared.consts.Graphics.MINING_SKILLCAPE_814,
                Animations.MINING_SKILLCAPE_4941,
            ),
            intArrayOf(
                Items.SMITHING_CAPE_9795,
                Items.SMITHING_CAPET_9796,
                shared.consts.Graphics.SMITHING_SKILLCAPE_815,
                Animations.SMITHING_SKILLCAPE_4943,
            ),
            intArrayOf(
                Items.FISHING_CAPE_9798,
                Items.FISHING_CAPET_9799,
                shared.consts.Graphics.FISHING_SKILLCAPE_819,
                Animations.FISHING_SKILLCAPE_4951,
            ),
            intArrayOf(
                Items.COOKING_CAPE_9801,
                Items.COOKING_CAPET_9802,
                shared.consts.Graphics.COOKING_SKILLCAPE_821,
                Animations.COOKING_SKILLCAPE_4955,
            ),
            intArrayOf(
                Items.FIREMAKING_CAPE_9804,
                Items.FIREMAKING_CAPET_9805,
                shared.consts.Graphics.FIREMAKING_SKILLCAPE_831,
                Animations.FIREMAKING_SKILLCAPE_4975,
            ),
            intArrayOf(
                Items.WOODCUTTING_CAPE_9807,
                Items.WOODCUT_CAPET_9808,
                shared.consts.Graphics.WC_SKILLCAPE_822,
                Animations.WOODCUTING_SKILLCAPE_4957,
            ),
            intArrayOf(
                Items.FARMING_CAPE_9810,
                Items.FARMING_CAPET_9811,
                shared.consts.Graphics.FARMING_SKILLCAPE_825,
                Animations.FARMING_SKILLCAPE_4963,
            ),
            intArrayOf(
                Items.SUMMONING_CAPE_12169,
                Items.SUMMONING_CAPET_12170,
                shared.consts.Graphics.SUMMONING_SKILLCAPE_EMOTE_1515,
                Animations.SUMMONING_SKILLCAPE_8525,
            ),
            intArrayOf(
                Items.QUEST_POINT_CAPE_9813,
                -1,
                shared.consts.Graphics.QUEST_SKILLCAPE_816,
                Animations.QUEST_SKILLCAPE_4945),
            intArrayOf(
                Items.HUNTER_CAPE_9948,
                Items.HUNTER_CAPET_9949,
                shared.consts.Graphics.HUNTER_SKILLCAPE_EMOTE_907,
                Animations.HUNTER_SKILLCAPE_5158,
            ),
        )

        /**
         * Handles the emote button interaction by the player.
         *
         * @param player The player performing the emote.
         * @param buttonId The button id corresponding to the emote clicked.
         */
        fun handle(
            player: Player,
            buttonId: Int,
        ) {
            if (player.locks.isLocked("emote")) {
                sendMessage(player, "You're already doing an emote!")
                return
            }
            if (player.properties.combatPulse.isAttacking || player.inCombat()) {
                sendMessage(player, "You can't perform an emote while being in combat.")
                return
            }
            val emote = forId(buttonId) ?: run {
                player.debug("Unhandled emote for button id - $buttonId")
                return
            }
            if (!hasEmote(player, emote)) {
                if (player.rights == Rights.ADMINISTRATOR) {
                    unlockEmote(player, emote)
                    player.pulseManager.clear()
                    emote.play(player)
                    return
                }
                sendDialogue(player, emote.lockedMessage ?: "You can't use this emote.")
                return
            }
            if (!player.achievementDiaryManager.getDiary(DiaryType.VARROCK)!!.isComplete(1, 6) && (buttonId in 30..33)) {
                if (!player.getAttribute("emote-$buttonId", false)) {
                    setAttribute(player, "emote-$buttonId", true)
                }
                val good = (30..33).all { player.getAttribute("emote-$it", false) }
                player.achievementDiaryManager.getDiary(DiaryType.VARROCK)!!.updateTask(player, 1, 6, good)
            }
            player.pulseManager.clear()
            emote.play(player)
        }

        /**
         * Forces the player to perform an emote animation and optionally a graphic.
         * Locks the emote action for the duration of the animation.
         *
         * @param player The player performing the emote.
         * @param animation The animation to perform.
         * @param graphic The graphic to display with the animation.
         */
        private fun forceEmote(player: Player, animation: Animation?, graphic: Graphics) {
            if (animation != null) {
                player.animator.animate(animation, graphic)
                player.locks.lock("emote", animation.duration)
            }
        }

        /**
         * Retrieves the [Emotes] instance associated with the given button id.
         *
         * @param buttonId The button id clicked by the player.
         * @return The matching [Emotes] instance, or `null` if none found.
         */
        @JvmStatic
        fun forId(buttonId: Int): Emotes? = values().find { it.buttonId == buttonId }

        /**
         * Checks if the player is wearing the full chicken outfit.
         *
         * @param player The player to check.
         * @return `True` if the full chicken set is equipped, `false` otherwise.
         */
        private fun isWearingChickenOutfit(player: Player): Boolean {
            val head = player.equipment[EquipmentContainer.SLOT_HAT]
            val wings = player.equipment[EquipmentContainer.SLOT_CHEST]
            val legs = player.equipment[EquipmentContainer.SLOT_LEGS]
            val feet = player.equipment[EquipmentContainer.SLOT_FEET]
            return head?.id == Items.CHICKEN_HEAD_11021 && wings?.id == Items.CHICKEN_WINGS_11020 && legs?.id == Items.CHICKEN_LEGS_11022 && feet?.id == Items.CHICKEN_FEET_11019
        }

        /**
         * Checks if the player is eligible to complete the falador task.
         *
         * @param player The player to check.
         * @return `True` if the player meets the requirements.
         */
        private fun isEligibleForSalute(player: Player): Boolean =
            !player.achievementDiaryManager.hasCompletedTask(DiaryType.FALADOR, 1, 8) && player.location == Location(
                2997, 3374, 0
            ) && player.direction == Direction.SOUTH && anyInEquipment(
                player,
                Items.INITIATE_SALLET_5574,
                Items.INITIATE_HAUBERK_5575,
                Items.INITIATE_CUISSE_5576,
            )

        /**
         * Checks if the player is eligible for the goblin bow requirement.
         *
         * @param player The player to check.
         * @return True if the player can use it.
         */
        private fun isEligibleForGoblinBow(player: Player): Boolean =
            player.location.regionId == 13206 && !player.getAttribute("mistag-greeted", false)
    }
}
