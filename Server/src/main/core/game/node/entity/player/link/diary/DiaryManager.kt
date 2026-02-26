package core.game.node.entity.player.link.diary

import com.google.gson.JsonArray
import content.global.skill.smithing.items.Bar
import core.api.*
import core.game.component.Component
import core.game.container.impl.EquipmentContainer
import core.game.node.entity.player.Player
import shared.consts.Components
import shared.consts.Items

/**
 * The Diary manager.
 */
class DiaryManager(val player: Player) {

    val diaries: Array<Diary> = arrayOf(
        Diary(DiaryType.KARAMJA),
        Diary(DiaryType.VARROCK),
        Diary(DiaryType.LUMBRIDGE),
        Diary(DiaryType.FALADOR),
        Diary(DiaryType.FREMENNIK),
        Diary(DiaryType.SEERS_VILLAGE)
    )

    /**
     * Parse a JsonArray into diaries.
     */
    fun parse(data: JsonArray) {
        data.forEach { element ->
            if (!element.isJsonObject) return@forEach
            val diaryJson = element.asJsonObject
            if (diaryJson.entrySet().isEmpty()) return@forEach

            val key = diaryJson.entrySet().first().key
            diaries.find { it.type.name.equals(key, ignoreCase = true) }?.let { diary ->
                diaryJson[key]?.takeIf { it.isJsonObject }?.asJsonObject?.let(diary::parse)
            }
        }
    }

    /**
     * Opens the diary tab in the interface.
     */
    fun openTab() {
        player.interfaceManager.openTab(2, Component(Components.AREA_TASK_259))
        diaries.forEach { it.drawStatus(player) }
    }

    /**
     * Updates a task in a diary.
     */
    fun updateTask(player: Player?, type: DiaryType?, level: Int, index: Int, complete: Boolean) {
        if (player == null || type == null) return
        getDiary(type)?.updateTask(player, level, index, complete)
    }

    /**
     * Marks a diary task as finished.
     */
    fun finishTask(player: Player, type: DiaryType?, level: Int, index: Int) {
        if (!player.isArtificial) getDiary(type)?.finishTask(player, level, index)
    }

    /**
     * Checks if a diary task has been completed.
     */
    fun hasCompletedTask(type: DiaryType?, level: Int, index: Int): Boolean =
        getDiary(type)?.isComplete(level, index) ?: false

    /**
     * Retrieves the diary for a given type.
     */
    fun getDiary(type: DiaryType?): Diary? =
        diaries.find { it.type == type }

    /**
     * Returns the equipped Karamja glove index.
     */
    val karamjaGlove: Int
        get() = getRewardIndex(DiaryType.KARAMJA, EquipmentContainer.SLOT_HANDS)

    /**
     * Returns the equipped Varrock armour index.
     */
    val armour: Int
        get() = getRewardIndex(DiaryType.VARROCK, EquipmentContainer.SLOT_CHEST)

    /**
     * Returns the equipped Seer's headband index.
     */
    val headband: Int
        get() = getRewardIndex(DiaryType.SEERS_VILLAGE, EquipmentContainer.SLOT_HAT)

    /**
     * Checks smithing reward eligibility.
     */
    fun checkSmithReward(type: Bar): Boolean {
        val level = player.achievementDiaryManager.armour
        if (level == -1) return false
        return when (level) {
            0 -> type.ordinal <= Bar.STEEL.ordinal
            1 -> type.ordinal <= Bar.MITHRIL.ordinal
            2 -> type.ordinal <= Bar.ADAMANT.ordinal
            else -> false
        }
    }

    /**
     * Checks if player has a Karamja glove equipped.
     */
    fun hasGlove(): Boolean =
        hasEquipment(DiaryType.KARAMJA, EquipmentContainer.SLOT_HANDS)

    /**
     * Checks if player has Seer's headband equipped.
     */
    fun hasHeadband(): Boolean =
        hasEquipment(DiaryType.SEERS_VILLAGE, EquipmentContainer.SLOT_HAT)

    /**
     * Checks if a diary is fully complete.
     */
    fun isComplete(type: DiaryType): Boolean =
        diaries[type.ordinal].isComplete

    /**
     * Resets all diary rewards from inventory, bank, and equipment.
     */
    fun resetRewards() {
        diaries.forEach { diary ->
            diary.type.rewards.flatten().forEach { item ->
                if (inInventory(player, item.id)) removeItem(player,item, Container.INVENTORY)
                if (inBank(player, item.id)) removeItem(player,item, Container.BANK)
                if (inEquipment(player, item.id)) removeItem(player,item, Container.EQUIPMENT)
            }
        }
    }

    /**
     * Utility to get the index of a reward item equipped.
     */
    private fun getRewardIndex(type: DiaryType, slot: Int): Int {
        val item = player.equipment[slot] ?: return -1
        return type.rewards.indexOfFirst { rewardArray -> rewardArray.any { it.id == item.id } }
    }

    /**
     * Utility to check if a reward item is equipped in a specific slot.
     */
    private fun hasEquipment(type: DiaryType, slot: Int): Boolean {
        val item = player.equipment[slot] ?: return false
        return type.rewards.any { rewardArray -> rewardArray.any { it.id == item.id } }
    }
}