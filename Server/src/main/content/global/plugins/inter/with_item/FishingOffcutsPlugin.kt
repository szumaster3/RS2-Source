package content.global.plugins.inter.with_item

import core.api.*
import core.game.interaction.Clocks
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.interaction.QueueStrength
import core.game.node.entity.skill.Skills
import core.tools.RandomUtils
import shared.consts.Animations
import shared.consts.Items

class FishingOffcutsPlugin : InteractionListener {

    private enum class FishData(val fishId: Int, val roe: Int, val xp: Double, val successDivider: Double, val offcutsChance: Double) {
        TROUT(Items.LEAPING_TROUT_11328, Items.ROE_11324, 10.0, 150.0, 0.5),
        SALMON(Items.LEAPING_SALMON_11330, Items.ROE_11324, 10.0, 80.0, 0.75),
        STURGEON(Items.LEAPING_STURGEON_11332, Items.CAVIAR_11326, 15.0, 80.0, 5.0 / 6.0);

        companion object {
            private val map = values().associateBy { it.fishId }
            fun forId(id: Int) = map[id]
            val ids = values().map { it.fishId }.toIntArray()
        }
    }

    override fun defineListeners() {
        /*
         * Handles cut open a leaping trout, leaping salmon, or leaping sturgeon using a knife.
         */

        onUseWith(IntType.ITEM, Items.KNIFE_946, *FishData.ids) { player, _, fishItem ->

            val fish = FishData.forId(fishItem.id) ?: return@onUseWith false
            val level = getStatLevel(player, Skills.COOKING)

            if (level < 1) {
                sendDialogue(player, "You need a Cooking level to attempt cutting this fish.")
                return@onUseWith true
            }

            val slots = freeSlots(player)
            val hasOffcuts = inInventory(player, Items.FISH_OFFCUTS_11334)

            if (slots < 2 && (slots < 1 || !hasOffcuts)) {
                sendMessage(player, "You don't have enough space in your pack to attempt cutting open the fish.")
                return@onUseWith true
            }

            queueScript(player, 1, QueueStrength.WEAK) {
                if (!clockReady(player, Clocks.SKILLING)) {
                    return@queueScript keepRunning(player)
                }
                if (amountInInventory(player, fish.fishId) <= 0) {
                    return@queueScript clearScripts(player)
                }

                animate(player, Animations.OFFCUTS_6702)
                removeItem(player, fish.fishId)

                val successChance = (level.coerceAtMost(99) / fish.successDivider)
                val success = RandomUtils.randomDouble() < successChance

                if (success) {
                    addItem(player, fish.roe)
                    if (RandomUtils.randomDouble() < fish.offcutsChance) {
                        addItem(player, Items.FISH_OFFCUTS_11334)
                    }
                    rewardXP(player, Skills.COOKING, fish.xp)
                    sendMessage(player, "You cut open the fish and extract some roe, but the rest is discarded.")
                } else {
                    sendMessage(player, "You fail to cut the fish properly and ruin it.")
                }

                delayClock(player, Clocks.SKILLING, 2)
                keepRunning(player)
            }
            return@onUseWith true
        }
    }
}