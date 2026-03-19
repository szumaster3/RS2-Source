package content.global.plugins.item

import core.cache.def.impl.ItemDefinition
import core.game.interaction.InteractionListener
import core.game.world.GameWorld
import shared.consts.Items

/**
 * Applies negative weight modifiers to specific equipment items.
 */
class ReduceWeightItemPlugin : InteractionListener {

    override fun defineListeners() {
        if (GameWorld.settings?.isMembers != true) {
            return
        }
        items()
    }

    private fun items() {
        applyWeight(Items.AGILE_TOP_14647,      -12.0)
        applyWeight(Items.AGILE_LEGS_14648,     -10.0)
        applyWeight(Items.BOOTS_OF_LIGHTNESS_88,-4.534)
    }

    /**
     * Applies a weight modifier to a single item id.
     */
    private fun applyWeight(itemId: Int, weight: Double) {
        val def = ItemDefinition.forId(itemId) ?: return
        def.handlers["weight"] = weight
    }
}