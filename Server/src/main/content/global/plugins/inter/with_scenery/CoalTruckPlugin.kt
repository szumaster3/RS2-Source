package content.global.plugins.inter.with_scenery

import core.api.*
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.player.Player
import core.game.node.item.Item
import shared.consts.Items
import shared.consts.Scenery

class CoalTruckPlugin : InteractionListener {

    companion object {
        private const val ATTRIBUTE_COAL_TRUCK_INVENTORY = "/save:coal-truck-inventory"
    }

    private fun maxCoalCapacity(player: Player) = when {
        inEquipment(player, Items.SEERS_HEADBAND_3_14646) -> 196
        inEquipment(player, Items.SEERS_HEADBAND_2_14645) -> 168
        inEquipment(player, Items.SEERS_HEADBAND_1_14631) -> 140
        else -> 120
    }

    private fun availableSpace(player: Player, coalInTruck: Int) = maxCoalCapacity(player) - coalInTruck

    override fun defineListeners() {

        onUseWith(IntType.SCENERY, Items.COAL_453, Scenery.COAL_TRUCK_2114) { player, _, _ ->
            val coalInTruck = getAttribute(player, ATTRIBUTE_COAL_TRUCK_INVENTORY, 0)
            val coalInInventory = amountInInventory(player, Items.COAL_453)
            val maxCapacity = maxCoalCapacity(player)
            val toDeposit = (coalInInventory).coerceAtMost(maxCapacity - coalInTruck)

            if (toDeposit > 0 && removeItem(player, Item(Items.COAL_453, toDeposit))) {
                setAttribute(player, ATTRIBUTE_COAL_TRUCK_INVENTORY, coalInTruck + toDeposit)
                if (coalInTruck + toDeposit == maxCapacity) sendMessage(player, "You have filled up the coal truck.")
            }
            return@onUseWith true
        }

        on(Scenery.COAL_TRUCK_2114, IntType.SCENERY, "remove-coal") { player, _ ->
            var coalInTruck = getAttribute(player, ATTRIBUTE_COAL_TRUCK_INVENTORY, 0)
            if (coalInTruck == 0) {
                sendDialogue(player, "The coal truck is empty.")
            } else {
                val toRemove = freeSlots(player).coerceAtMost(coalInTruck)
                if (addItem(player, Items.COAL_453, toRemove)) {
                    coalInTruck -= toRemove
                    setAttribute(player, ATTRIBUTE_COAL_TRUCK_INVENTORY, coalInTruck)
                }
            }
            return@on true
        }

        on(Scenery.COAL_TRUCK_2114, IntType.SCENERY, "investigate") { player, _ ->
            val coalInTruck = getAttribute(player, ATTRIBUTE_COAL_TRUCK_INVENTORY, 0)
            sendDialogue(player, "There is currently $coalInTruck coal in the truck. " +
                    "The truck has space for ${availableSpace(player, coalInTruck)} more coal.")
            return@on true
        }

        /*
         * Handles chest in Galahad house.
         */

        on(Scenery.CLOSED_CHEST_375, IntType.SCENERY, "open") { player, _ ->
            sendMessage(player, "The chest is locked.")
            return@on true
        }
    }
}
