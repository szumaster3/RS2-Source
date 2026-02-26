package core.game.node.entity.player.link.diary

import com.google.gson.JsonObject
import core.api.*
import core.cache.def.impl.NPCDefinition
import core.game.diary.DiaryLevel
import core.game.node.entity.player.Player
import core.game.node.item.Item
import shared.consts.Components
import java.util.*

/**
 * Represents an Achievement Diary.
 *
 * @property type The type of the diary.
 */
class Diary(
    /**
     * The type of this diary.
     */
    val type: DiaryType
) {
    /**
     * Flags indicating whether each diary level has been started.
     * Indexed by level ordinal.
     */
    val levelStarted: BooleanArray = BooleanArray(3)

    /**
     * Flags indicating whether the reward for each diary level has been claimed.
     * Indexed by level ordinal.
     */
    val levelRewarded: BooleanArray = BooleanArray(3)

    /**
     * Flags indicating whether each task at each level has been completed.
     * Indexed as [level][taskIndex].
     */
    val taskCompleted: Array<BooleanArray> = Array(type.achievements.size) { BooleanArray(25) }

    /**
     * Updates the diary interface with the current completion status of each level.
     *
     * @param player The player whose diary interface will be updated.
     */
    fun drawStatus(player: Player) {
        // Do nothing if no levels have been started.
        if (!isStarted) return

        // Display area name with overall completion color.
        sendString(player,
            "${if (isComplete) GREEN else YELLOW}${type.displayName}", Components.AREA_TASK_259, type.child
        )

        // Display status for each individual level.
        (0..2).forEach { i ->
            val statusColor = when {
                isComplete(i) -> GREEN
                isStarted(i) -> YELLOW
                else -> "<col=FF0000>"
            }

            sendString(player,
                "$statusColor${getLevel(i)}", Components.AREA_TASK_259, type.child + (i + 1)
            )
        }
    }

    /**
     * Opens the achievement diary interface for the given player and displays all tasks.
     *
     * @param player The player for whom the diary interface is opened.
     */
    fun open(player: Player) {
        clear(player)
        sendString(player, "<red>Achievement Diary - ${type.displayName}", 2)

        var child = 12
        sendString(
            player,
            "${if (isComplete) GREEN else if (isStarted) YELLOW else "<red>"}${type.displayName} Area Tasks",
            child++
        )

        if (type.info.isNotEmpty() && !isStarted) {
            sendString(player, type.info, child++)
            child += type.info.split("<br><br>").count { it.isNotEmpty() }
        }
        child++

        type.achievements.forEachIndexed { level, achievements ->
            sendString(player, "${getStatus(level)}${getLevel(level)}", child++)
            child++
            achievements.forEachIndexed { i, task ->
                val complete = isComplete(level, i)
                task.split("<br><br>").forEach { line ->
                    if (line.isNotBlank()) {
                        sendString(player, if (complete) "<str><str>$line" else line, child++)
                    }
                }
                sendString(player, "*", child++)
            }
            child++
        }

        if (!player.interfaceManager.isOpened()) {
            openInterface(player, DIARY_COMPONENT)
        }
        player.packetDispatch.sendRunScript(1207, "ii", 1, child - 10)
    }

    /**
     * Clears all strings from the diary component.
     *
     * @param player The player whose diary interface will be cleared.
     */
    private fun clear(player: Player) {
        (0..310).forEach { sendString(player, "", DIARY_COMPONENT, it) }
    }

    /**
     * Parses saved diary data from JSON and updates the diary's internal state.
     *
     * @param data The JSON object containing startedLevels, completedLevels, and rewardedLevels.
     */
    fun parse(data: JsonObject) {
        data.getAsJsonArray("startedLevels")?.forEachIndexed { i, element ->
            levelStarted[i] = element.asBoolean
        }

        data.getAsJsonArray("completedLevels")?.forEachIndexed { i, levelArray ->
            val completed = levelArray.asJsonArray.mapIndexed { j, task ->
                taskCompleted[i][j] = task.asBoolean
                taskCompleted[i][j]
            }.all { it }
            if (completed) completedLevels.add(i)
        }

        data.getAsJsonArray("rewardedLevels")?.forEachIndexed { i, element ->
            levelRewarded[i] = element.asBoolean
        }
    }

    /**
     * Updates the task status in the interface.
     *
     * @param player The player whose task is being updated.
     * @param level The level index of the task.
     * @param index The task index within the level.
     * @param complete True if the task has been completed, false if only updated.
     */
    fun updateTask(player: Player, level: Int, index: Int, complete: Boolean) {
        if (!levelStarted[level]) levelStarted[level] = true

        if (!complete) {
            sendMessage(player, "Well done! A ${type.displayName} task has been updated.")
        } else {
            taskCompleted[level][index] = true
            val tempLevel = if (type == DiaryType.LUMBRIDGE) level - 1 else level
            sendMessages(player,
                "Well done! You have completed ${
                    when (tempLevel) {
                        -1 -> "a beginner"
                        0 -> "an easy"
                        1 -> "a medium"
                        else -> "a hard"
                    }
                } task in the ${type.displayName} area. Your", "Achievement Diary has been updated."
            )
        }

        if (isComplete(level)) {
            val npcName = NPCDefinition.forId(type.getNpc(level)).name
            val msg =
                "Congratulations! You have completed all of the ${getLevel(level).lowercase()} tasks in the ${type.displayName} area."
            sendMessage(player, "$msg Speak to $npcName to claim your reward.")
            sendDialogue(player, "$msg Speak to $npcName to claim your reward.")
        }

        drawStatus(player)
    }

    /**
     * Marks a task as finished for the player and updates the completion status of the level.
     *
     * @param player The player whose task is finished.
     * @param level The level index of the task.
     * @param index The task index within the level.
     */
    fun finishTask(player: Player, level: Int, index: Int) {
        if (!isComplete(level, index)) {
            updateTask(player, level, index, true)
            if (taskCompleted[level].all { it }) {
                completedLevels.add(level)
            } else {
                completedLevels.remove(level)
            }
        }
    }

    /**
     * Resets a specific task to incomplete and updates level start/reward status.
     *
     * @param player The player whose task is being reset.
     * @param level The level index of the task.
     * @param index The task index within the level.
     */
    fun resetTask(player: Player, level: Int, index: Int) {
        taskCompleted[level][index] = false
        if (!isStarted(level)) levelStarted[level] = false
        if (!isComplete(level)) levelRewarded[level] = false
        drawStatus(player)
    }

    /**
     * Checks whether a given diary level has been completed.
     *
     * @param level The diary level to check.
     * @return True if the level is complete, false otherwise.
     */
    fun checkComplete(level: DiaryLevel): Boolean {
        return if (level == DiaryLevel.BEGINNER && type != DiaryType.LUMBRIDGE) {
            false
        } else if (level == DiaryLevel.BEGINNER) {
            completedLevels.contains(level.ordinal)
        } else {
            completedLevels.contains(level.ordinal - 1)
        }
    }

    /**
     * Sends a formatted string to the diary interface for a specific child component.
     *
     * @param player The player to whom the string will be sent.
     * @param string The text to display.
     * @param child The interface child component index.
     */
    private fun sendString(player: Player, string: String, child: Int) {
        sendString(player, string.replace("<blue>", BLUE).replace("<red>", RED), DIARY_COMPONENT, child)
    }

    /**
     * Checks if a level has been started.
     *
     * @param level The level index.
     * @return True if the level has been started, false otherwise.
     */
    fun isStarted(level: Int): Boolean = levelStarted[level]

    /**
     * Checks if any level in this diary has been started.
     */
    val isStarted: Boolean
        get() = type.levelNames.indices.any { isStarted(it) }

    /**
     * Checks if a specific task has been completed.
     *
     * @param level The level index.
     * @param index The task index.
     * @return True if the task is complete.
     */
    fun isComplete(level: Int, index: Int): Boolean = taskCompleted[level][index]

    /**
     * Checks if all tasks in a level are complete.
     *
     * @param level The level index.
     * @return True if the level is complete.
     */
    fun isComplete(level: Int): Boolean = type.getAchievements(level).indices.all { taskCompleted[level][it] }

    /**
     * Checks if a level is complete, optionally checking cumulatively previous levels.
     *
     * @param level The level index.
     * @param cumulative Whether to include previous levels in the check.
     * @return True if the level (and optionally previous levels) are complete.
     */
    fun isComplete(level: Int, cumulative: Boolean): Boolean =
        isComplete(level) && (!cumulative || level <= 0 || isComplete(level - 1, true))


    /**
     * Checks if all levels in this diary are complete.
     */
    val isComplete: Boolean
        get() = taskCompleted.indices.all { level ->
            type.getAchievements(level).indices.all { taskCompleted[level][it] }
        }

    /**
     * Returns the highest completed level, or -1 if none.
     */
    val level: Int
        get() = (2 downTo 0).firstOrNull { isComplete(it) } ?: -1

    /**
     * Returns the highest rewarded level, or -1 if none.
     */
    val reward: Int
        get() = (2 downTo 0).firstOrNull { isLevelRewarded(it) } ?: -1

    /**
     * Gets the name of a level.
     *
     * @param level The level index.
     * @return The display name of the level.
     */
    fun getLevel(level: Int): String = type.levelNames[level]

    /**
     * Returns the color code representing the status of a level.
     *
     * @param level The level index.
     * @return GREEN if complete, YELLOW if started, RED if not started.
     */
    fun getStatus(level: Int): String = when {
        !isStarted(level) -> RED
        isComplete(level) -> GREEN
        else -> YELLOW
    }

    /**
     * Marks a level as rewarded.
     *
     * @param level The level index.
     */
    fun setLevelRewarded(level: Int) {
        levelRewarded[level] = true
    }

    /**
     * Checks if a level has been rewarded.
     *
     * @param level The level index.
     * @return True if the level reward has been claimed.
     */
    fun isLevelRewarded(level: Int): Boolean = levelRewarded[level]

    companion object {
        /**
         * The interface component for the diary.
         * */
        const val DIARY_COMPONENT: Int = Components.QUESTJOURNAL_SCROLL_275

        /**
         * Tracks completed levels globally.
         */
        val completedLevels: ArrayList<Int> = ArrayList()

        private const val RED = "<col=8A0808>"
        private const val BLUE = "<col=08088A>"
        private const val YELLOW = "<col=F7FE2E>"
        private const val GREEN = "<col=3ADF00>"

        /**
         * Removes the reward for a specific level from the player.
         *
         * @param player The player.
         * @param type The diary type.
         * @param level The level index.
         * @return True if a reward was removed.
         */
        fun removeRewardsFor(player: Player, type: DiaryType, level: Int): Boolean {
            val reward = type.getRewards(level)[0] // Only first reward is removable.
            val removed =
                removeItem(player,reward, Container.INVENTORY) || removeItem(player,reward, Container.BANK) || removeItem(player,reward, Container.EQUIPMENT)

            if (removed) player.debug("Removed previous reward")
            return removed
        }

        /**
         * Adds rewards for a specific level to the player's inventory.
         *
         * @param player The player.
         * @param type The diary type.
         * @param level The level index.
         * @return True if all rewards were successfully added.
         */
        fun addRewardsFor(player: Player, type: DiaryType, level: Int): Boolean {
            val rewards = type.getRewards(level)
            if (freeSlots(player) < rewards.size) return false

            val allAdded = rewards.all { addItem(player, it.id) }
            if (!allAdded) rewards.forEach { removeItem(player, it) }
            return allAdded
        }

        /**
         * Flags a level as rewarded and handles reward replacement.
         *
         * @param player The player.
         * @param type The diary type.
         * @param level The level index.
         * @return True if the reward was successfully granted.
         */
        fun flagRewarded(player: Player, type: DiaryType, level: Int): Boolean {
            if (level > 0) removeRewardsFor(player, type, level - 1)

            return if (addRewardsFor(player, type, level)) {
                player.achievementDiaryManager.getDiary(type)?.setLevelRewarded(level)
                true
            } else {
                sendMessage(player, "You do not have enough space in your inventory to claim these rewards.")
                false
            }
        }

        /**
         * Checks if the reward for a level can be replaced.
         *
         * @param player The player.
         * @param type The diary type.
         * @param level The level index.
         * @return True if replacement is possible.
         */
        fun canReplaceReward(player: Player, type: DiaryType, level: Int): Boolean {
            val reward = type.getRewards(level)[0]
            val claimed = hasCompletedLevel(player, type, level) && hasClaimedLevelRewards(
                player,
                type,
                level
            ) && !player.hasItem(reward)

            return if (level == 2) claimed else claimed && !hasClaimedLevelRewards(player, type, level + 1)
        }

        /**
         * Grants a replacement reward if possible.
         *
         * @param player The player.
         * @param type The diary type.
         * @param level The level index.
         * @return True if the replacement was successfully granted.
         */
        fun grantReplacement(player: Player, type: DiaryType, level: Int): Boolean {
            val reward = type.getRewards(level)[0]
            return canReplaceReward(player, type, level) && addItem(player, reward.id)
        }

        /**
         * Checks if a level has been completed.
         *
         * @param player The player.
         * @param type The diary type.
         * @param level The level index.
         * @return True if the level is complete.
         */
        fun hasCompletedLevel(player: Player, type: DiaryType, level: Int): Boolean {
            if (level >= type.levelNames.size) return false
            return player.achievementDiaryManager.getDiary(type)?.isComplete(level, cumulative = true) == true
        }

        /**
         * Checks if a level's reward has been claimed.
         *
         * @param player The player.
         * @param type The diary type.
         * @param level The level index.
         * @return True if the reward has been claimed.
         */
        fun hasClaimedLevelRewards(player: Player, type: DiaryType?, level: Int): Boolean {
            return player.achievementDiaryManager.getDiary(type)?.isLevelRewarded(level) == true
        }

        /**
         * Checks if a level's rewards can be claimed.
         *
         * @param player The player.
         * @param type The diary type.
         * @param level The level index.
         * @return True if rewards can be claimed.
         */
        fun canClaimLevelRewards(player: Player, type: DiaryType, level: Int): Boolean {
            val nextLevelClaimed = level < 2 && hasClaimedLevelRewards(player, type, level + 1)
            val currentClaimed = hasClaimedLevelRewards(player, type, level)
            val completed = hasCompletedLevel(player, type, level)
            return completed && !currentClaimed && !nextLevelClaimed
        }

        /**
         * Returns the rewards for a given diary type and level.
         *
         * @param type The diary type.
         * @param level The level index.
         * @return Array of items rewarded at this level.
         */
        fun getRewards(type: DiaryType, level: Int): Array<Item> = type.getRewards(level)
    }
}
