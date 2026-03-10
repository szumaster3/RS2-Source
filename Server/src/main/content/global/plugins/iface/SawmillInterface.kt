package content.global.plugins.iface

import content.global.skill.construction.items.PlankType
import core.api.*
import core.game.component.Component
import core.game.component.ComponentDefinition
import core.game.component.ComponentPlugin
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.diary.DiaryType
import core.game.node.item.Item
import core.plugin.Initializable
import core.plugin.Plugin
import shared.consts.Components
import shared.consts.Items

/**
 * Represents the sawmill plank interface.
 */
@Initializable
class SawmillInterface : ComponentPlugin() {

    companion object {
        private val buttonMap = listOf(
            102..107 to PlankType.WOOD,
            109..113 to PlankType.OAK,
            115..119 to PlankType.TEAK,
            121..125 to PlankType.MAHOGANY
        )
    }

    override fun newInstance(arg: Any?): Plugin<Any> {
        ComponentDefinition.put(Components.POH_SAWMILL_403, this)
        return this
    }

    override fun handle(
        player: Player,
        component: Component,
        opcode: Int,
        button: Int,
        slot: Int,
        itemId: Int
    ): Boolean {

        val entry = buttonMap.firstOrNull { button in it.first } ?: return true
        val range = entry.first
        val plank = entry.second

        val index = button - range.first

        val amount = when (index) {
            0 -> 1
            1 -> 5
            2 -> 10
            3 -> -1
            4 -> amountInInventory(player, plank.log)
            else -> return true
        }

        if (amount == -1) {
            sendInputDialogue(player, true, "Enter the amount:") { value ->
                val input = value as? Int ?: return@sendInputDialogue
                createPlank(player, plank, input)
            }
        } else if (amount > 0) {
            createPlank(player, plank, amount)
        }

        return true
    }

    private fun createPlank(player: Player, plank: PlankType, requestedAmount: Int) {
        closeInterface(player)

        val availableLogs = amountInInventory(player, plank.log)
        if (availableLogs <= 0) {
            sendMessage(player, "You are not carrying any logs to cut into planks.")
            return
        }

        val amount = requestedAmount.coerceAtMost(availableLogs)
        val cost = plank.price * amount

        if (!inInventory(player, Items.COINS_995, cost)) {
            sendDialogue(player, "Sorry, I don't have enough coins to pay for that.")
            return
        }

        removeItem(player, Item(Items.COINS_995, cost))
        removeItem(player, Item(plank.log, amount))
        addItem(player, plank.plank, amount)

        when {
            plank == PlankType.WOOD ->
                finishDiaryTask(player, DiaryType.VARROCK, 0, 3)

            plank == PlankType.MAHOGANY && amount >= 20 ->
                finishDiaryTask(player, DiaryType.VARROCK, 1, 15)
        }
    }
}