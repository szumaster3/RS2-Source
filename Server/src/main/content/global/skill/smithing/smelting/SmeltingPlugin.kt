package content.global.skill.smithing.smelting

import content.global.skill.smithing.items.Bar
import core.api.*
import core.game.dialogue.FaceAnim
import core.game.event.ResourceProducedEvent
import core.game.interaction.Clocks
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.interaction.QueueStrength
import core.game.node.entity.player.link.diary.DiaryType
import core.game.node.entity.skill.Skills
import core.game.node.item.Item
import core.game.world.GameWorld
import core.game.world.update.flag.context.Animation
import shared.consts.*

class SmeltingPlugin : InteractionListener {

    override fun defineListeners() {

        /*
         * Handles perfect gold smelting (Family crest quest special interaction).
         */

        onUseWith(IntType.SCENERY, Items.PERFECT_GOLD_ORE_446, *FURNACE_IDS) { player, used, _ ->
            queueScript(player, 1, QueueStrength.SOFT) { stage: Int ->
                when (stage) {
                    0 -> {
                        if (removeItem(player, used.asItem())) {
                            sendMessage(player, "You place a lump of gold in the furnace.")
                            lock(player, 4)
                            lockInteractions(player, 4)
                            animate(player, Animations.HUMAN_FURNACE_SMELT_3243)
                        }
                        return@queueScript delayScript(player, 2)
                    }

                    1 -> {
                        sendMessage(player, "You retrieve a bar of gold from the furnace.")
                        addItem(player, Items.PERFECT_GOLD_BAR_2365)
                        rewardXP(player, Skills.SMITHING, 22.5)
                        return@queueScript stopExecuting(player)
                    }

                    else -> return@queueScript stopExecuting(player)
                }
            }
            return@onUseWith true
        }

        /*
         * Handles creating cannonballs.
         */

        onUseWith(IntType.SCENERY, Items.STEEL_BAR_2353, *FURNACE_IDS) { player, _, _ ->
            if (!clockReady(player, Clocks.SKILLING)) return@onUseWith true

            if (!isQuestComplete(player, Quests.DWARF_CANNON)) {
                sendDialogue(player, "You need to complete the ${Quests.DWARF_CANNON} quest in order to do this.")
                return@onUseWith true
            }

            if (getDynLevel(player, Skills.SMITHING) < 35) {
                sendDialogue(player, "You need a Smithing level of at least 35 in order to do this.")
                return@onUseWith true
            }

            if (!inInventory(player, Items.AMMO_MOULD_4)) {
                sendDialogue(player, "You need an ammo mould in order to make a cannonball.")
                return@onUseWith true
            }

            if (!hasSpaceFor(player, Item(Items.CANNONBALL_2, 4))) {
                sendDialogue(player, "You do not have enough inventory space.")
                return@onUseWith true
            }

            sendSkillDialogue(player) {
                withItems(Items.STEEL_BAR_2353)

                create { _, amount ->
                    var remaining = amount
                    queueScript(player, 0, QueueStrength.NORMAL) { stage: Int ->
                        if (remaining <= 0) return@queueScript stopExecuting(player)
                        if (amountInInventory(player, Items.STEEL_BAR_2353) <= 0) {
                            sendMessage(player, "You have run out of steel bars.")
                            return@queueScript stopExecuting(player)
                        }

                        when (stage) {
                            0 -> {
                                sendMessage(player, "You heat the steel bar into a liquid state.")
                                playAudio(player, Sounds.FURNACE_2725)
                                animate(player, Animations.HUMAN_FURNACE_SMELT_3243)
                                return@queueScript delayScript(player, 3)
                            }
                            1 -> {
                                sendMessage(player, "You pour the molten metal into your cannonball mould.")
                                animate(player, Animations.HUMAN_BURYING_BONES_827)
                                return@queueScript delayScript(player, 1)
                            }
                            2 -> {
                                sendMessage(player, "The molten metal cools slowly to form 4 cannonballs.")
                                return@queueScript delayScript(player, 3)
                            }
                            3 -> {
                                if (removeItem(player, Item(Items.STEEL_BAR_2353, 1))) {
                                    addItem(player, Items.CANNONBALL_2, 4)
                                    rewardXP(player, Skills.SMITHING, 25.6)
                                }
                                animate(player, Animations.HUMAN_BURYING_BONES_827)

                                remaining--

                                if (remaining > 0 && amountInInventory(player, Items.STEEL_BAR_2353) > 0) {
                                    delayClock(player, Clocks.SKILLING, 3)
                                    setCurrentScriptState(player, 0)
                                    return@queueScript delayScript(player, 3)
                                }

                                return@queueScript stopExecuting(player)
                            }
                            else -> return@queueScript stopExecuting(player)
                        }
                    }
                }

                calculateMaxAmount { amountInInventory(player, Items.STEEL_BAR_2353) }
            }

            return@onUseWith true
        }

        /*
         * Handles furnace options interaction.
         */

        on(IntType.SCENERY, "smelt", "smelt-ore") { player, node ->
            if (node.id == Scenery.FURNACE_26814 && !isDiaryComplete(player, DiaryType.VARROCK, 0)) {
                if (!GameWorld.settings!!.isMembers) {
                    sendNPCDialogue(player, NPCs.JEFFERY_6298, "Keep away from that! It's dangerous!")
                } else {
                    sendNPCDialogue(player, NPCs.JEFFERY_6298, "You want to use my furnace?", FaceAnim.HALF_ASKING)
                    addDialogueAction(player) { _, _ ->
                        sendNPCDialogue(
                            player,
                            NPCs.JEFFERY_6298,
                            "No one can use my furnace! Only I can use my furnace!",
                            FaceAnim.ANNOYED
                        )
                    }
                    sendMessage(
                        player,
                        "You need to have completed the easy tasks in the Varrock Diary in order to use this."
                    )
                }
                return@on true
            }
            openChatbox(player, Components.SMELTING_311)
            return@on true
        }

        /*
         * Handles use option for tutorial furnace.
         */

        on(specialFurnace, IntType.SCENERY, "use") { player, _ ->
            if (inBorders(player, getRegionBorders(TUTORIAL_REGION))) {
                if (!anyInInventory(player, *tutorialOres)) {
                    sendPlainDialogue(player,
                        false,
                        "This is a furnace for smelting metal. To use it simply click on the",
                        "ore you wish to smelt then click on the furnace you would like to",
                        "use.",
                    )
                    return@on true
                }
                if (!inInventory(player, Items.TIN_ORE_438) || !inInventory(player, Items.COPPER_ORE_436)) {
                    sendPlainDialogue(player,
                        false,
                        "",
                        "You do not have the required ores to make this bar.",
                        "",
                    )
                } else {
                    sendPlainDialogue(player,
                        false,
                        "This is a furnace for smelting metal. To use it simply click on the",
                        "ore you wish to smelt then click on the furnace you would like to",
                        "use.",
                    )
                }
            } else if (!isDiaryComplete(player, DiaryType.VARROCK, 0)) {
                sendMessage(
                    player,
                    "You need to have completed the easy tasks in the Varrock Diary in order to use this."
                )
            } else {
                openChatbox(player, Components.SMELTING_311)
            }
            return@on true
        }

        /*
         * Handles use the ores on furnaces.
         */

        onUseWith(IntType.SCENERY, getOreIds(), *FURNACE_IDS) { player, _, with ->
            if (with.asScenery().id == Scenery.FURNACE_26814 && !isDiaryComplete(player, DiaryType.VARROCK, 0)) {
                if (!GameWorld.settings!!.isMembers) {
                    sendNPCDialogue(player, NPCs.JEFFERY_6298, "Keep away from that! It's dangerous!")
                } else {
                    sendNPCDialogue(player, NPCs.JEFFERY_6298, "You want to use my furnace?", FaceAnim.HALF_ASKING)
                    addDialogueAction(player) { _, _ ->
                        sendNPCDialogue(
                            player,
                            NPCs.JEFFERY_6298,
                            "No one can use my furnace! Only I can use my furnace!",
                            FaceAnim.ANNOYED
                        )
                    }
                }
                return@onUseWith false
            }
            openChatbox(player, Components.SMELTING_311)
            return@onUseWith true
        }

        /*
         * Handles tutorial island interaction.
         */

        onUseWith(IntType.SCENERY, tutorialOres, *specialFurnace) { player, used, _ ->
            if (!inInventory(player, Items.TIN_ORE_438) || !inInventory(player, Items.COPPER_ORE_436)) {
                player.dialogueInterpreter.sendPlainMessage(
                    false,
                    "",
                    "You do not have the required ores to make this bar.",
                    "",
                )
                return@onUseWith false
            }
            if (removeItem(player, Item(Items.TIN_ORE_438, 1)) && removeItem(player, Item(Items.COPPER_ORE_436, 1))) {
                animate(player, smeltAnimation)
                playAudio(player, Sounds.FURNACE_2725, 1)
                player.packetDispatch.sendRunScript(102, "s", "You smelt the copper and tin together in the furnace.")
                addItem(player, Items.BRONZE_BAR_2349)
                queueScript(player, 4, QueueStrength.SOFT) {
                    rewardXP(player, Skills.SMITHING, Bar.BRONZE.experience)
                    player.dispatch(
                        ResourceProducedEvent(
                            Bar.BRONZE.product.id,
                            1,
                            used.asItem(),
                            original = if (used.id != Items.TIN_ORE_438) Items.COPPER_ORE_436 else Items.TIN_ORE_438,
                        ),
                    )
                    player.packetDispatch.sendRunScript(102, "s", "You retrieve a bar of bronze from the furnace.")
                    return@queueScript stopExecuting(player)
                }
            }
            return@onUseWith true
        }
    }

    companion object {
        private val smeltAnimation = Animation(Animations.HUMAN_FURNACE_SMELT_3243)
        private val tutorialOres = intArrayOf(Items.TIN_ORE_438, Items.COPPER_ORE_436)
        private val specialFurnace = intArrayOf(Scenery.CLAY_FORGE_21303, Scenery.FURNACE_3044)

        private const val TUTORIAL_REGION = 12436

        private val FURNACE_IDS = intArrayOf(
            Scenery.FURNACE_4304,
            Scenery.FURNACE_6189,
            Scenery.FURNACE_11010,
            Scenery.FURNACE_11666,
            Scenery.FURNACE_12100,
            Scenery.FURNACE_12809,
            Scenery.SMALL_FURNACE_14921,
            Scenery.FURNACE_18497,
            Scenery.FURNACE_26814,
            Scenery.FURNACE_30021,
            Scenery.FURNACE_30510,
            Scenery.FURNACE_36956,
            Scenery.FURNACE_37651
        )

        private fun getOreIds(): IntArray {
            val ids = mutableListOf<Int>()
            for (bar in Bar.values()) {
                for (item in bar.ores) {
                    ids.add(item.id)
                }
            }
            return ids.toIntArray()
        }
    }
}
