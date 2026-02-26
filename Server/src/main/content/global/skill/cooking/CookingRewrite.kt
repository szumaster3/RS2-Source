package content.global.skill.cooking

import core.api.amountInInventory
import core.api.sendMessage
import core.api.submitIndividualPulse
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.impl.PulseType
import core.game.node.entity.player.Player
import core.game.node.item.Item
import core.game.node.scenery.Scenery
import shared.consts.Items
import shared.consts.Scenery as Objects

class CookingRewrite : InteractionListener {

    private val RAW_FOODS: IntArray = intArrayOf(
        *CookableItems.values().map { it.raw }.toIntArray(),
        Items.COOKED_MEAT_2142,
        Items.RAW_BEEF_2132,
        Items.RAW_BEAR_MEAT_2136,
        Items.SEAWEED_401,
        Items.BOWL_OF_WATER_1921,
        Items.CUP_OF_WATER_4458
    )

    override fun defineListeners() {
        onUseWith(IntType.SCENERY, RAW_FOODS, *COOKING_OBJS) { player, used, with ->
            val item = used.asItem()
            val obj = with.asScenery()
            val isRange = obj.id in RANGE_OBJS

            when (item.id) {
                Items.RAW_BEEF_2132, Items.RAW_BEAR_MEAT_2136 -> {
                    if (isRange) {
                        player.dialogueInterpreter.open(CookingDialogue(item.id, 9436, true, obj, item.id))
                    } else {
                        sendMessage(player, "You need to cook this on a range.")
                    }
                    return@onUseWith true
                }

                Items.BREAD_DOUGH_2307, Items.UNCOOKED_CAKE_1889 -> {
                    if (!isRange) {
                        sendMessage(player, "You need to cook this on a range.")
                        return@onUseWith false
                    }
                }
                Items.BOWL_OF_WATER_1921 -> {
                    cook(player, obj, Items.BOWL_OF_WATER_1921, Items.BOWL_OF_HOT_WATER_4456, 1)
                    return@onUseWith true
                }
                Items.CUP_OF_WATER_4458 -> {
                    cook(player, obj, Items.CUP_OF_WATER_4458, Items.CUP_OF_HOT_WATER_4460, 1)
                    return@onUseWith true
                }
            }

            if (amountInInventory(player, item.id) > 1) {
                player.dialogueInterpreter.open(CookingDialogue(item.id, obj))
            } else {
                val product =
                    CookableItems.forId(item.id)?.let {
                        if (CookableItems.intentionalBurn(item.id)) {
                            CookableItems.getIntentionalBurn(item.id).id
                        } else {
                            it.cooked
                        }
                    } ?: throw IllegalArgumentException("Invalid item ID.")

                cook(player, obj, item.id, product, 1)
            }
            return@onUseWith true
        }
    }

    companion object {
        val COOKING_OBJS = intArrayOf(
            // FIRES
            Objects.FIRE_2732,
            Objects.FIRE_3038,
            Objects.FIRE_3769,
            Objects.FIRE_3775,
            Objects.FIRE_4265,
            Objects.FIRE_4266,
            Objects.FIRE_5249,
            Objects.FIRE_5499,
            Objects.FIRE_5631,
            Objects.FIRE_5632,
            Objects.FIRE_5981,
            Objects.FIRE_10433,
            Objects.FIRE_11404,
            Objects.FIRE_11405,
            Objects.FIRE_11406,
            Objects.FIRE_12796,
            Objects.FIRE_13337,
            Objects.FIRE_13881,
            Objects.FIRE_14169,
            Objects.FIRE_15156,
            Objects.FIRE_20000,
            Objects.FIRE_20001,
            Objects.FIRE_21620,
            Objects.FIRE_23046,
            Objects.FIRE_25155,
            Objects.FIRE_25156,
            Objects.FIRE_25465,
            Objects.FIRE_27297,
            Objects.FIRE_29139,
            Objects.FIRE_30017,
            Objects.FIRE_32099,
            Objects.FIRE_37597,
            Objects.FIRE_37726,

            // FIREPLACES
            Objects.FIREPLACE_2724,
            Objects.FIREPLACE_2725,
            Objects.FIREPLACE_2726,
            Objects.FIREPLACE_4618,
            Objects.FIREPLACE_4650,
            Objects.FIREPLACE_5165,
            Objects.FIREPLACE_6093,
            Objects.FIREPLACE_6094,
            Objects.FIREPLACE_6095,
            Objects.FIREPLACE_6096,
            Objects.FIREPLACE_8712,
            Objects.FIREPLACE_9439,
            Objects.FIREPLACE_9440,
            Objects.FIREPLACE_9441,
            Objects.FIREPLACE_10824,
            Objects.FIREPLACE_17640,
            Objects.FIREPLACE_17641,
            Objects.FIREPLACE_17642,
            Objects.FIREPLACE_17643,
            Objects.FIREPLACE_18039,
            Objects.FIREPLACE_21795,
            Objects.FIREPLACE_24285,
            Objects.FIREPLACE_24329,
            Objects.FIREPLACE_27251,
            Objects.FIREPLACE_33498,
            Objects.FIREPLACE_35449,
            Objects.FIREPLACE_36815,
            Objects.FIREPLACE_36816,
            Objects.FIREPLACE_37426,

            // RANGES
            Objects.RANGE_2728,
            Objects.RANGE_2729,
            Objects.RANGE_2730,
            Objects.RANGE_2731,
            Objects.RANGE_3039,
            Objects.RANGE_9682,
            Objects.RANGE_12102,
            Objects.RANGE_14919,
            Objects.RANGE_21792,
            Objects.RANGE_22713,
            Objects.RANGE_22714,
            Objects.RANGE_24283,
            Objects.RANGE_24284,
            Objects.RANGE_25730,
            Objects.RANGE_33500,
            Objects.RANGE_34495,
            Objects.RANGE_34546,
            Objects.RANGE_36973,
            Objects.RANGE_37629,
            Objects.RANGE_40110,

            // COOKING RANGES
            Objects.COOKING_RANGE_114,
            Objects.COOKING_RANGE_2859,
            Objects.COOKING_RANGE_4172,
            Objects.COOKING_RANGE_5275,
            Objects.COOKING_RANGE_8750,
            Objects.COOKING_RANGE_16893,
            Objects.COOKING_RANGE_22154,
            Objects.COOKING_RANGE_34410,
            Objects.COOKING_RANGE_34565,

            // STOVES
            Objects.STOVE_9085,
            Objects.STOVE_9086,
            Objects.STOVE_9087,
            Objects.STOVE_12269,
            Objects.GOBLIN_STOVE_25440,
            Objects.GOBLIN_STOVE_25441,

            // OVENS
            Objects.CLAY_OVEN_10377,
            Objects.CLAY_OVEN_21302,
            Objects.OVEN_24313,
            Objects.SMALL_OVEN_13533,
            Objects.LARGE_OVEN_13536,
            Objects.STEEL_RANGE_13539,
            Objects.FANCY_RANGE_13542,

            // FIREPITS
            Objects.FIREPIT_13528,
            Objects.FIREPIT_WITH_HOOK_13529,
            Objects.FIREPIT_WITH_POT_13531,

            // SPECIAL
            Objects.SULPHUR_VENT_9374,
        )

        private val RANGE_OBJS = setOf(
            Objects.RANGE_2728,
            Objects.RANGE_2729,
            Objects.RANGE_2730,
            Objects.RANGE_2731,
            Objects.RANGE_3039,
            Objects.RANGE_9682,
            Objects.RANGE_12102,
            Objects.RANGE_14919,
            Objects.RANGE_21792,
            Objects.RANGE_22713,
            Objects.RANGE_22714,
            Objects.RANGE_24283,
            Objects.RANGE_24284,
            Objects.RANGE_25730,
            Objects.RANGE_33500,
            Objects.RANGE_34495,
            Objects.RANGE_34546,
            Objects.RANGE_36973,
            Objects.RANGE_37629,
            Objects.RANGE_40110
        )

        @JvmStatic
        fun cook(player: Player, scenery: Scenery?, initial: Int, product: Int, amount: Int) {
            val food = Item(initial)
            val foodName = food.name.lowercase()

            val cookingPulse =
                when {
                    foodName.contains("pizza") -> PizzaCookingPulse(player, scenery!!, initial, product, amount)
                    foodName.contains("pie") -> PieCookingPulse(player, scenery!!, initial, product, amount)
                    CookableItems.intentionalBurn(initial) -> BurnPulse(player, scenery!!, initial, product, amount)
                    else -> CookingPulse(player, scenery!!, initial, product, amount)
                }

            submitIndividualPulse(player, cookingPulse, type = PulseType.STANDARD)
        }
    }
}
