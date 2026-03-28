package content.global.plugins.iface

import core.api.*
import core.game.interaction.InterfaceListener
import core.game.node.entity.player.Player
import core.game.node.item.Item
import shared.consts.Components
import shared.consts.Items

/**
 * Represents the Sewing interface.
 * @author szu
 */
class SewInterface : InterfaceListener {

    private val cost = 500

    override fun defineInterfaceListeners() {
        on(Components.SEW_INTERFACE_419) { player, _, opcode, button, _, _ ->
            val stock = PirateClothes.fromButtonId(button) ?: return@on false

            if (opcode == 155) {
                create(player, stock)
            }
            return@on true
        }
    }

    private fun create(player: Player, stock: PirateClothes) {
        if (freeSlots(player) < 1) {
            sendMessage(player, "You don't have enough inventory space for this.")
            return
        }

        if (amountInInventory(player, Items.COINS_995) < cost) {
            sendMessage(player, "You can't afford that.")
            return
        }

        if (!inInventory(player, stock.firstItem) || !inInventory(player, stock.secondItem)) {
            sendMessage(player, "You don't have required items in your inventory.")
            return
        }

        removeItem(player, stock.firstItem, Container.INVENTORY)
        removeItem(player, stock.secondItem, Container.INVENTORY)
        removeItem(player, Item(Items.COINS_995, cost), Container.INVENTORY)

        addItem(player, stock.product, 1)
        sendMessage(player, "Your items have been combined.")
    }
}

private enum class PirateClothes(val firstItem: Int, val secondItem: Int, val product: Int, val buttonId: Int) {
    WHITE_RIGHT_EYE(Items.PIRATE_BANDANA_7112, Items.EYE_PATCH_1025, Items.BANDANA_AND_EYEPATCH_8924, 38),
    WHITE_DOUBLE_EYE(Items.PIRATE_BANDANA_7112, Items.DOUBLE_EYEPATCHES_13353, Items.BANDANA_AND_EYEPATCHES_13340, 66),
    WHITE_LEFT_EYE(Items.PIRATE_BANDANA_7112, Items.LEFT_EYEPATCH_13355, Items.BANDANA_AND_EYEPATCH_13339, 68),
    RED_RIGHT_EYE(Items.PIRATE_BANDANA_7124, Items.EYE_PATCH_1025, Items.BANDANA_AND_EYEPATCH_8925, 40),
    RED_DOUBLE_EYE(Items.PIRATE_BANDANA_7124, Items.DOUBLE_EYEPATCHES_13353, Items.BANDANA_AND_EYEPATCHES_13342, 60),
    RED_LEFT_EYE(Items.PIRATE_BANDANA_7124, Items.LEFT_EYEPATCH_13355, Items.BANDANA_AND_EYEPATCH_13341, 70),
    BLUE_RIGHT_EYE(Items.PIRATE_BANDANA_7130, Items.EYE_PATCH_1025, Items.BANDANA_AND_EYEPATCH_8926, 42),
    BLUE_DOUBLE_EYE(Items.PIRATE_BANDANA_7130, Items.DOUBLE_EYEPATCHES_13353, Items.BANDANA_AND_EYEPATCHES_13344, 64),
    BLUE_LEFT_EYE(Items.PIRATE_BANDANA_7130, Items.LEFT_EYEPATCH_13355, Items.BANDANA_AND_EYEPATCH_13343, 72),
    BROWN_RIGHT_EYE(Items.PIRATE_BANDANA_7136, Items.EYE_PATCH_1025, Items.BANDANA_AND_EYEPATCH_8927, 44),
    BROWN_DOUBLE_EYE(Items.PIRATE_BANDANA_7136, Items.DOUBLE_EYEPATCHES_13353, Items.BANDANA_AND_EYEPATCHES_13346, 62),
    BROWN_LEFT_EYE(Items.PIRATE_BANDANA_7136, Items.LEFT_EYEPATCH_13355, Items.BANDANA_AND_EYEPATCH_13345, 74),
    GREY_RIGHT_EYE(Items.BANDANA_13370, Items.EYE_PATCH_1025, Items.BANDANA_AND_EYEPATCH_13378, 46),
    GREY_LEFT_EYE(Items.BANDANA_13370, Items.LEFT_EYEPATCH_13355, Items.BANDANA_AND_EYEPATCH_13347, 76),
    GREY_DOUBLE_EYE(Items.BANDANA_13370, Items.DOUBLE_EYEPATCHES_13353, Items.BANDANA_AND_EYEPATCHES_13348, 52),
    PURPLE_RIGHT_EYE(Items.BANDANA_13372, Items.EYE_PATCH_1025, Items.BANDANA_AND_EYEPATCH_13376, 48),
    PURPLE_LEFT_EYE(Items.BANDANA_13372, Items.LEFT_EYEPATCH_13355, Items.BANDANA_AND_EYEPATCH_13349, 78),
    PURPLE_DOUBLE_EYE(Items.BANDANA_13372, Items.DOUBLE_EYEPATCHES_13353, Items.BANDANA_AND_EYEPATCHES_13350, 54),
    ORANGE_RIGHT_EYE(Items.BANDANA_13374, Items.BANDANA_13374, Items.BANDANA_AND_EYEPATCH_13377, 50),
    ORANGE_DOUBLE_EYE(Items.BANDANA_13374, Items.DOUBLE_EYEPATCHES_13353, Items.BANDANA_AND_EYEPATCH_13351, 56),
    ORANGE_LEFT_DOUBLE_EYE(Items.BANDANA_13374, Items.LEFT_EYEPATCH_13355, Items.BANDANA_AND_EYEPATCHES_13352, 80),
    PIRATE_HAT_RIGHT_EYE(Items.PIRATES_HAT_2651, Items.EYE_PATCH_1025, Items.HAT_AND_EYEPATCH_8928, 82),
    PIRATE_HAT_DOUBLE_EYE(Items.PIRATES_HAT_2651, Items.DOUBLE_EYEPATCHES_13353, Items.PIRATE_HAT_AND_EYEPATCHES_13354, 58),
    PIRATE_HAT_LEFT_EYE(Items.PIRATES_HAT_2651, Items.LEFT_EYEPATCH_13355, Items.PIRATE_HAT_AND_EYEPATCH_13357, 84),
    DOUBLE_PATCH(Items.LEFT_EYEPATCH_13355, Items.EYE_PATCH_1025, Items.DOUBLE_EYEPATCHES_13353, 86),
    CRAB_HAND(Items.CRAB_CLAW_7537, Items.PIRATES_HOOK_2997, Items.CRABCLAW_AND_HOOK_8929, 88),
    CAVALIER_AND_MASK(Items.HIGHWAYMAN_MASK_2631, Items.BLACK_CAVALIER_2643, Items.CAVALIER_AND_MASK_11280, 90),
    BERET_AND_MASK(Items.MIME_MASK_3057, Items.BLACK_BERET_2635, Items.BERET_AND_MASK_11282, 92),
    ;

    companion object {
        private val buttonMap: Map<Int, PirateClothes> = values().associateBy { it.buttonId }
        fun fromButtonId(buttonId: Int): PirateClothes? = buttonMap[buttonId]
    }
}
