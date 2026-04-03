package content.region.kandarin.gnome_stronghold.quest.pog

import core.api.getVarp
import core.api.setVarp
import core.game.component.Component
import core.game.component.ComponentDefinition
import core.game.component.ComponentPlugin
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.plugin.Plugin
import shared.consts.Components
import shared.consts.Items

@Initializable
class YewnockMachinePlugin : ComponentPlugin() {

    companion object {
        private val DISCS = setOf(
            Items.RED_CIRCLE_9597, Items.RED_TRIANGLE_9598, Items.RED_SQUARE_9599, Items.RED_PENTAGON_9600,
            Items.ORANGE_CIRCLE_9601, Items.ORANGE_TRIANGLE_9602, Items.ORANGE_SQUARE_9603, Items.ORANGE_PENTAGON_9604,
            Items.YELLOW_CIRCLE_9605, Items.YELLOW_TRIANGLE_9606, Items.YELLOW_SQUARE_9607, Items.YELLOW_PENTAGON_9608,
            Items.GREEN_CIRCLE_9609, Items.GREEN_TRIANGLE_9610, Items.GREEN_SQUARE_9611, Items.GREEN_PENTAGON_9612,
            Items.BLUE_CIRCLE_9613, Items.BLUE_TRIANGLE_9614, Items.BLUE_SQUARE_9615, Items.BLUE_PENTAGON_9616,
            Items.INDIGO_CIRCLE_9617, Items.INDIGO_TRIANGLE_9618, Items.INDIGO_SQUARE_9619, Items.INDIGO_PENTAGON_9620,
            Items.VIOLET_CIRCLE_9621, Items.VIOLET_TRIANGLE_9622, Items.VIOLET_SQUARE_9623, Items.VIOLET_PENTAGON_9624
        )

        private val ITEM_VALUE = mapOf(
            // Red
            Items.RED_CIRCLE_9597 to 1,
            Items.RED_TRIANGLE_9598 to 3,
            Items.RED_SQUARE_9599 to 4,
            Items.RED_PENTAGON_9600 to 5,
            // Orange
            Items.ORANGE_CIRCLE_9601 to 2,
            Items.ORANGE_TRIANGLE_9602 to 6,
            Items.ORANGE_SQUARE_9603 to 8,
            Items.ORANGE_PENTAGON_9604 to 10,
            // Yellow
            Items.YELLOW_CIRCLE_9605 to 3,
            Items.YELLOW_TRIANGLE_9606 to 9,
            Items.YELLOW_SQUARE_9607 to 12,
            Items.YELLOW_PENTAGON_9608 to 15,
            // Green
            Items.GREEN_CIRCLE_9609 to 4,
            Items.GREEN_TRIANGLE_9610 to 12,
            Items.GREEN_SQUARE_9611 to 16,
            Items.GREEN_PENTAGON_9612 to 20,
            // Blue
            Items.BLUE_CIRCLE_9613 to 5,
            Items.BLUE_TRIANGLE_9614 to 15,
            Items.BLUE_SQUARE_9615 to 20,
            Items.BLUE_PENTAGON_9616 to 25,
            // Indigo
            Items.INDIGO_CIRCLE_9617 to 6,
            Items.INDIGO_TRIANGLE_9618 to 18,
            Items.INDIGO_SQUARE_9619 to 24,
            Items.INDIGO_PENTAGON_9620 to 30,
            // Violet
            Items.VIOLET_CIRCLE_9621 to 7,
            Items.VIOLET_TRIANGLE_9622 to 21,
            Items.VIOLET_SQUARE_9623 to 28,
            Items.VIOLET_PENTAGON_9624 to 35
        )

        private val LEFT_SLOTS = mapOf(15 to 1068, 16 to 1069, 17 to 1070)
        private val RIGHT_TARGET_SLOTS = listOf(1065, 1066, 1067)
        private const val HAND_SLOT = 856

        fun value(itemId: Int): Int = ITEM_VALUE[itemId] ?: 0
    }

    override fun newInstance(arg: Any?): Plugin<Any> {
        ComponentDefinition.put(Components.YEWNOCK_MACHINE_613, this)
        return this
    }

    override fun handle(player: Player, component: Component, opcode: Int, button: Int, slot: Int, itemId: Int): Boolean {
        if (opcode == 155 && itemId in DISCS)
        {
            val slotId = when
            {
                getVarp(player, 1068) <= 0 -> 1068
                getVarp(player, 1069) <= 0 -> 1069
                getVarp(player, 1070) <= 0 -> 1070
                else -> return false
            }
            setVarp(player, slotId, itemId)
            return true
        }

        if (button == 23)
        {
            val discs = listOf(
                getVarp(player, 1068),
                getVarp(player, 1069),
                getVarp(player, 1070)
            ).filter { it > 0 }

            if (discs.isEmpty()) return true

            val sum = discs.sumOf { value(it) }
            val target = getVarp(player, 1065)

            if (target <= 0 || sum != target) return true
            return true
        }

        return false
    }
}