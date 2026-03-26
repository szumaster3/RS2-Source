package content.global.plugins.item

import core.api.*
import core.cache.def.impl.ItemDefinition
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.item.Item
import shared.consts.Items
import shared.consts.Sounds

/**
 * Handles item emptying actions such as emptying potions, bowls, and other containers.
 */
class EmptyItemOptionPlugin: InteractionListener {
    override fun defineListeners() {

        /*
         * Handles interactions for emptying items.
         */

        on(EmptyItem.emptyItemList.toIntArray(), IntType.ITEM, "empty", "empty bowl", "empty dish") { player, node ->
            if (node.name.contains("brew") ||
                node.name.contains("potion") ||
                node.name.lowercase().contains("poison") ||
                node.name.lowercase().contains("serum") ||
                node.name.contains("cure") ||
                node.name.contains("mix") ||
                node.name.contains("balm")
            ) {
                replaceSlot(player, node.asItem().slot, Item(EmptyItem.getEmpty(Items.POTION_195)!!), node.asItem())
                playAudio(player, EmptyItem.getEmptyAudio(Items.POTION_195)!!)
                return@on true
            }
            if (EmptyItem.emptyItemMap[node.id] != null) {
                replaceSlot(player, node.asItem().slot, Item(EmptyItem.getEmpty(node.id)!!), node.asItem())
                if (EmptyItem.getEmptyAudio(node.id) != -1) playAudio(player, EmptyItem.getEmptyAudio(node.id)!!)
                EmptyItem.getEmptyMessage(node.id)?.let { sendMessage(player, it) }
            }
            return@on true
        }

    }

    enum class EmptyItem(
        var fullId: Int,
        var emptyId: Int,
        var emptyMessage: String,
        var audioId: Int = -1,
    ) {
        BONE_MEAL(
            Items.BONEMEAL_4255,
            Items.EMPTY_POT_1931,
            "You empty the pot of crushed bones."
        ),
        BOWL_OF_HOT_WATER(
            Items.BOWL_OF_HOT_WATER_4456,
            Items.BOWL_1923,
            "You empty the bowl of hot water onto the floor.",
        ),
        BOWL_OF_WATER(
            Items.BOWL_OF_WATER_1921,
            Items.BOWL_1923,
            "You empty the contents of the bowl onto the floor.",
            Sounds.LIQUID_2401,
        ),
        BROOM_OINTMENT(
            Items.BROOM_OINTMENT_14062,
            Items.VIAL_229,
            "You empty the vial.",
            Sounds.LIQUID_2401
        ),
        BUCKET_OF_COMPOST(
            Items.COMPOST_6032,
            Items.BUCKET_1925,
            "You empty the bucket of compost."
        ),
        BUCKET_OF_MILK(
            Items.BUCKET_OF_MILK_1927,
            Items.BUCKET_1925,
            "You empty the contents of the bucket onto the floor.",
            Sounds.LIQUID_2401,
        ),
        BUCKET_OF_SAND(
            Items.BUCKET_OF_SAND_1783,
            Items.BUCKET_1925,
            "You empty the contents of the bucket onto the floor.",
            Sounds.SAND_BUCKET_2584,
        ),
        BUCKET_OF_SLIME(
            Items.BUCKET_OF_SLIME_4286,
            Items.BUCKET_1925,
            "You empty the contents of the bucket on the floor.",
            Sounds.LIQUID_2401,
        ),
        BUCKET_OF_SUPERCOMPOST(
            Items.SUPERCOMPOST_6034,
            Items.BUCKET_1925,
            "You empty the bucket of supercompost."
        ),
        BUCKET_OF_WATER(
            Items.BUCKET_OF_WATER_1929,
            Items.BUCKET_1925,
            "You empty the contents of the bucket onto the floor.",
            Sounds.LIQUID_2401,
        ),
        BURNT_CURRY(
            Items.BURNT_CURRY_2013,
            Items.BOWL_1923,
            "You empty the contents of the bowl onto the floor.",
            Sounds.LIQUID_2401,
        ),
        BURNT_EGG(
            Items.BURNT_EGG_7090,
            Items.BOWL_1923,
            "You empty the contents of the bowl onto the floor."
        ),
        BURNT_GNOMEBOWL(
            Items.BURNT_GNOMEBOWL_2175,
            Items.GNOMEBOWL_MOULD_2166,
            "You empty the contents of the gnomebowl onto the floor.",
        ),
        BURNT_MUSHROOM(
            Items.BURNT_MUSHROOM_7094,
            Items.BOWL_1923,
            "You empty the contents of the bowl onto the floor.",
        ),
        BURNT_ONION(
            Items.BURNT_ONION_7092,
            Items.BOWL_1923,
            "You empty the contents of the bowl onto the floor."
        ),
        BURNT_PIE(
            Items.BURNT_PIE_2329,
            Items.PIE_DISH_2313,
            "You empty the pie dish."
        ),
        BURNT_STEW(
            Items.BURNT_STEW_2005,
            Items.BOWL_1923,
            "You empty the contents of the bowl onto the floor.",
            Sounds.LIQUID_2401,
        ),
        CUP_OF_WATER(
            Items.CUP_OF_WATER_4458,
            Items.EMPTY_CUP_1980,
            "You empty the cup of water onto the floor."
        ),
        CUP_OF_HOT_WATER(
            Items.CUP_OF_HOT_WATER_4460,
            Items.EMPTY_CUP_1980,
            "You empty the cup of hot water onto the floor.",
        ),
        CUP_OF_NETTLE_TEA(
            Items.CUP_OF_TEA_4242,
            Items.EMPTY_CUP_1980,
            "You empty the cup of water onto the floor."
        ),
        CUP_OF_TEA_CLAY(
            Items.CUP_OF_TEA_7730,
            Items.EMPTY_CUP_7728,
            "You empty the cup of tea onto the floor."
        ),
        CUP_OF_TEA_CLAY_MILKY(
            Items.CUP_OF_TEA_7731,
            Items.EMPTY_CUP_7728,
            "You empty the cup of milky tea onto the floor.",
        ),
        CUP_OF_TEA_WHITE(
            Items.CUP_OF_TEA_7733,
            Items.PORCELAIN_CUP_7732,
            "You empty the porcelain cup of tea onto the floor.",
        ),
        CUP_OF_TEA_WHITE_MILKY(
            Items.CUP_OF_TEA_7734,
            Items.PORCELAIN_CUP_7732,
            "You empty the porcelain cup of milky tea onto the floor.",
        ),
        CUP_OF_TEA_GOLD(
            Items.CUP_OF_TEA_7736,
            Items.PORCELAIN_CUP_7735,
            "You empty the cup."
        ),
        CUP_OF_TEA_GOLD_MILKY(
            Items.CUP_OF_TEA_7737,
            Items.PORCELAIN_CUP_7735,
            "You empty the cup."
        ),
        NETTLE_WATER(
            Items.NETTLE_WATER_4237,
            Items.BOWL_1923,
            "You empty the contents of the bowl onto the floor.",
            Sounds.LIQUID_2401,
        ),
        NETTLE_TEA(
            Items.NETTLE_TEA_4239,
            Items.BOWL_1923,
            "You empty the contents of the bowl onto the floor.",
            Sounds.LIQUID_2401,
        ),
        NETTLE_TEA_MILKY(
            Items.NETTLE_TEA_4240,
            Items.BOWL_1923,
            "You empty the contents of the bowl onto the floor.",
            Sounds.LIQUID_2401,
        ),
        NETTLE_TEA_PORCELAIN(
            Items.CUP_OF_TEA_4245,
            Items.PORCELAIN_CUP_4244,
            "You empty the cup."
        ),
        NETTLE_TEA_MILKY_PORCELAIN(
            Items.CUP_OF_TEA_4246,
            Items.PORCELAIN_CUP_4244,
            "You empty the porcelain cup."
        ),
        MAGIC_UNGUENT(
            Items.MAGIC_UNGUENT_14061,
            Items.VIAL_229,
            "You empty the vial.",
            Sounds.LIQUID_2401
        ),
        JUG_OF_WATER(
            Items.JUG_OF_WATER_1937,
            Items.JUG_1935,
            "You empty the contents of the jug onto the floor.",
            Sounds.LIQUID_2401,
        ),
        POTION(
            Items.POTION_195,
            Items.VIAL_229,
            "You empty the vial.",
            Sounds.LIQUID_2401
        ),
        POT_OF_CORNFLOUR(
            Items.POT_OF_CORNFLOUR_7468,
            Items.EMPTY_POT_1931,
            "You empty the contents of the pot onto the floor.",
        ),
        POT_OF_FLOUR(
            Items.POT_OF_FLOUR_1933,
            Items.EMPTY_POT_1931,
            "You empty the contents of the pot onto the floor.",
        ),
        FISHBOWL(
            Items.FISHBOWL_6668,
            Items.FISHBOWL_6667,
            "You pour out the contents of your fishbowl."
        ),
        FISHBOWL_WITH_SEAWEED(
            Items.FISHBOWL_6669,
            Items.FISHBOWL_6667,
            "You pour out the contents of your fishbowl."
        ),
        FIREWORK(
            Items.FIREWORK_3006,
            Items.VIAL_229,
            "You empty the vial.",
            Sounds.LIQUID_2401
        ),
        VIAL_OF_COCONUT(
            Items.COCONUT_MILK_5935,
            Items.VIAL_229,
            "You empty the vial.",
            Sounds.LIQUID_2401
        ),
        SANDBAG(
            Items.SANDBAG_9943,
            Items.EMPTY_SACK_5418,
            "You empty the sack of sand.",
        ),
        VIAL_OF_WATER(
            Items.VIAL_OF_WATER_227,
            Items.VIAL_229,
            "You empty the vial.",
            Sounds.LIQUID_2401
        ),
        GOLD_BOWL(
            Items.GOLDEN_BOWL_723,
            Items.GOLD_BOWL_721,
            "You empty the contents of the bowl onto the floor."
        ),
        BLESSED_GOLDEN_BOWL(
            Items.GOLDEN_BOWL_724,
            Items.BLESSED_GOLD_BOWL_722,
            "You empty the contents of the bowl onto the floor."
        ),
        GOLDEN_BOWL(
            Items.GOLDEN_BOWL_725,
            Items.GOLD_BOWL_721,
            "You empty the contents of the bowl onto the floor."
        ),
        GOLDEN_BOWL_2(
            Items.GOLDEN_BOWL_726,
            Items.GOLD_BOWL_721,
            "You empty the contents of the bowl onto the floor."
        ),
        ;

        companion object {
            var emptyItemMap: MutableMap<Int, Int> = HashMap()
            var emptyMessageMap: MutableMap<Int, String> = HashMap()
            var emptyAudioMap: MutableMap<Int, Int> = HashMap()
            var emptyItemList: MutableList<Int> = mutableListOf()

            init {
                for (item in values()) {
                    emptyItemMap.putIfAbsent(item.fullId, item.emptyId)
                    emptyMessageMap.putIfAbsent(item.fullId, item.emptyMessage)
                    emptyAudioMap.putIfAbsent(item.fullId, item.audioId)
                    emptyItemList.add(item.fullId)
                }
                for (item in ItemDefinition.getDefinitions().values) {
                    if (item.name.contains("potion") ||
                        item.name.contains("brew") ||
                        item.name.contains("poison") ||
                        item.name.lowercase().contains("serum") ||
                        item.name.contains("cure") ||
                        item.name.contains("mix") ||
                        item.name.contains("balm")
                    ) {
                        emptyItemList.add(item.id)
                    }
                }
            }

            /**
             * Gets the empty item id for full item id.
             */
            fun getEmpty(id: Int): Int? = emptyItemMap[id]

            /**
             * Gets the emptying message for the full item id
             */
            fun getEmptyMessage(id: Int): String? = emptyMessageMap[id]

            /**
             * Gets the audio id for emptying the full item id
             */
            fun getEmptyAudio(id: Int): Int? = emptyAudioMap[id]
        }
    }
}
