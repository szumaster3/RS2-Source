package content.global.skill.construction;

import core.game.node.entity.player.Player;
import core.game.node.item.Item;
import core.game.node.scenery.Scenery;
import core.game.world.map.Location;
import shared.consts.Items;

public enum Decoration {
    PORTAL(13405, 8168, 1, 100, new Item[]{new Item(Items.IRON_BAR_2351, 10)}),
    ROCK(13406, 8169, 5, 100, new Item[]{new Item(Items.LIMESTONE_BRICK_3420, 5)}),
    POND(13407, 8170, 10, 100, new Item[]{new Item(Items.SOFT_CLAY_1761, 10)}),
    IMP_STATUE(13408, 8171, 15, 150, new Item[]{new Item(Items.LIMESTONE_BRICK_3420, 5), new Item(Items.SOFT_CLAY_1761, 5)}),
    SMALL_OBELISK(42004, Items.SUMMONING_OBELISK_14644, 41, 676, new Item[]{new Item(Items.MARBLE_BLOCK_8786), new Item(Items.SPIRIT_SHARDS_12183, 1000), new Item(Items.CRIMSON_CHARM_12160, 10), new Item(Items.BLUE_CHARM_12163, 10)}),
    DUNGEON_ENTRANCE(13409, 8172, 70, 500, new Item[]{new Item(Items.MARBLE_BLOCK_8786)}),

    BIG_DEAD_TREE(13411, 8173, 5, 31, new int[]{BuildingUtils.WATERING_CAN}, new Item[]{new Item(Items.BAGGED_DEAD_TREE_8417)}),
    BIG_TREE(13412, 8174, 10, 44, new int[]{BuildingUtils.WATERING_CAN}, new Item[]{new Item(Items.BAGGED_NICE_TREE_8419)}),
    BIG_OAK_TREE(13413, 8175, 15, 70, new int[]{BuildingUtils.WATERING_CAN}, new Item[]{new Item(Items.BAGGED_OAK_TREE_8421)}),
    BIG_WILLOW_TREE(13414, 8176, 30, 100, new int[]{BuildingUtils.WATERING_CAN}, new Item[]{new Item(Items.BAGGED_WILLOW_TREE_8423)}),
    BIG_MAPLE_TREE(13415, 8177, 45, 122, new int[]{BuildingUtils.WATERING_CAN}, new Item[]{new Item(Items.BAGGED_MAPLE_TREE_8425)}),
    BIG_YEW_TREE(13416, 8178, 60, 141, new int[]{BuildingUtils.WATERING_CAN}, new Item[]{new Item(Items.BAGGED_YEW_TREE_8427)}),
    BIG_MAGIC_TREE(13417, 8179, 75, 223, new int[]{BuildingUtils.WATERING_CAN}, new Item[]{new Item(Items.BAGGED_MAGIC_TREE_8429)}),

    DEAD_TREE(13418, 8173, 5, 31, new int[]{BuildingUtils.WATERING_CAN}, new Item[]{new Item(Items.BAGGED_DEAD_TREE_8417)}),
    TREE(13419, 8174, 10, 44, new int[]{BuildingUtils.WATERING_CAN}, new Item[]{new Item(Items.BAGGED_NICE_TREE_8419)}),
    OAK_TREE(13420, 8175, 15, 70, new int[]{BuildingUtils.WATERING_CAN}, new Item[]{new Item(Items.BAGGED_OAK_TREE_8421)}),
    WILLOW_TREE(13421, 8176, 30, 100, new int[]{BuildingUtils.WATERING_CAN}, new Item[]{new Item(Items.BAGGED_WILLOW_TREE_8423)}),
    MAPLE_TREE(13423, 8177, 45, 122, new int[]{BuildingUtils.WATERING_CAN}, new Item[]{new Item(Items.BAGGED_MAPLE_TREE_8425)}),
    YEW_TREE(13422, 8178, 60, 141, new int[]{BuildingUtils.WATERING_CAN}, new Item[]{new Item(Items.BAGGED_YEW_TREE_8427)}),
    MAGIC_TREE(13424, 8179, 75, 223, new int[]{BuildingUtils.WATERING_CAN}, new Item[]{new Item(Items.BAGGED_MAGIC_TREE_8429)}),

    FERN(13425, 8186, 1, 31, new int[]{BuildingUtils.WATERING_CAN}, new Item[]{new Item(Items.BAGGED_PLANT_1_8431)}),
    BUSH(13426, 8187, 6, 70, new int[]{BuildingUtils.WATERING_CAN}, new Item[]{new Item(Items.BAGGED_PLANT_2_8433)}),
    TALL_PLANT(13427, 8188, 12, 100, new int[]{BuildingUtils.WATERING_CAN}, new Item[]{new Item(Items.BAGGED_PLANT_3_8435)}),

    SHORT_PLANT(13428, 8189, 1, 31, new int[]{BuildingUtils.WATERING_CAN}, new Item[]{new Item(Items.BAGGED_PLANT_1_8431)}),
    LARGE_LEAF_PLANT(13429, 8190, 6, 70, new int[]{BuildingUtils.WATERING_CAN}, new Item[]{new Item(Items.BAGGED_PLANT_2_8433)}),
    HUGE_PLANT(13430, 8191, 12, 100, new int[]{BuildingUtils.WATERING_CAN}, new Item[]{new Item(Items.BAGGED_PLANT_3_8435)}),

    PLANT(13431, 8180, 1, 31, new int[]{BuildingUtils.WATERING_CAN}, new Item[]{new Item(Items.BAGGED_PLANT_1_8431)}),
    SMALL_FERN(13432, 8181, 6, 70, new int[]{BuildingUtils.WATERING_CAN}, new Item[]{new Item(Items.BAGGED_PLANT_2_8433)}),
    FERN_SP(13433, 8182, 12, 100, new int[]{BuildingUtils.WATERING_CAN}, new Item[]{new Item(Items.BAGGED_PLANT_3_8435)}),

    DOCK_LEAF(13434, 8183, 1, 31, new int[]{BuildingUtils.WATERING_CAN}, new Item[]{new Item(Items.BAGGED_PLANT_1_8431)}),
    THISTLE(13435, 8184, 6, 70, new int[]{BuildingUtils.WATERING_CAN}, new Item[]{new Item(Items.BAGGED_PLANT_2_8433)}),
    REEDS(13436, 8185, 12, 100, new int[]{BuildingUtils.WATERING_CAN}, new Item[]{new Item(Items.BAGGED_PLANT_3_8435)}),

    CRUDE_CHAIR(13581, 8309, 1, Items.CRUDE_WOODEN_CHAIR_8496, 66, new Item[]{new Item(Items.PLANK_960, 2)}),
    WOODEN_CHAIR(13582, 8310, 8, Items.WOODEN_CHAIR_8498, 96, new Item[]{new Item(Items.PLANK_960, 3)}),
    ROCKING_CHAIR(13583, 8311, 14, Items.ROCKING_CHAIR_8500, 96, new Item[]{new Item(Items.PLANK_960, 3)}),
    OAK_CHAIR(13584, 8312, 19, Items.OAK_CHAIR_8502, 120, new Item[]{new Item(Items.OAK_PLANK_8778, 2)}),
    OAK_ARMCHAIR(13585, 8313, 26, Items.OAK_ARMCHAIR_8504, 180, new Item[]{new Item(Items.OAK_PLANK_8778, 3)}),
    TEAK_ARMCHAIR(13586, 8314, 35, Items.TEAK_ARMCHAIR_8506, 180, new Item[]{new Item(Items.TEAK_PLANK_8780, 2)}),
    MAHOGANY_ARMCHAIR(13587, 8315, 50, Items.MAHOGANY_ARMCHAIR_8508, 280, new Item[]{new Item(Items.MAHOGANY_PLANK_8782, 2)}),

    BROWN_RUG_CORNER(13588, 8316, 2, 30, new Item[]{new Item(Items.BOLT_OF_CLOTH_8790, 2)}),
    RED_RUG_CORNER(13591, 8317, 13, 60, new Item[]{new Item(Items.BOLT_OF_CLOTH_8790, 4)}),
    OPULENT_RUG_CORNER(13594, 8318, 65, 360, new Item[]{new Item(Items.BOLT_OF_CLOTH_8790, 4), new Item(Items.GOLD_LEAF_8784)}),
    BROWN_RUG_END(13589, 8316, 2, 30, new Item[]{new Item(Items.BOLT_OF_CLOTH_8790, 2)}),
    RED_RUG_END(13592, 8317, 13, 60, new Item[]{new Item(Items.BOLT_OF_CLOTH_8790, 4)}),
    OPULENT_RUG_END(13595, 8318, 65, 360, new Item[]{new Item(Items.BOLT_OF_CLOTH_8790, 4), new Item(Items.GOLD_LEAF_8784)}),
    BROWN_RUG_CENTER(13590, 8316, 2, 30, new Item[]{new Item(Items.BOLT_OF_CLOTH_8790, 2)}),
    RED_RUG_CENTER(13593, 8317, 13, 60, new Item[]{new Item(Items.BOLT_OF_CLOTH_8790, 4)}),
    OPULENT_RUG_CENTER(13596, 8318, 65, 360, new Item[]{new Item(Items.BOLT_OF_CLOTH_8790, 4), new Item(Items.GOLD_LEAF_8784)}),

    CLAY_FIREPLACE(13609, 8325, 3, 30, new Item[]{new Item(Items.SOFT_CLAY_1761, 3)}),
    STONE_FIREPLACE(13611, 8326, 33, 40, new Item[]{new Item(Items.LIMESTONE_BRICK_3420, 2)}),
    MARBLE_FIREPLACE(13613, 8327, 63, 500, new Item[]{new Item(Items.MARBLE_BLOCK_8786)}),

    TORN_CURTAINS(13603, 8322, 2, 132, new Item[]{new Item(Items.PLANK_960, 3), new Item(Items.BOLT_OF_CLOTH_8790, 3)}),
    CURTAINS(13604, 8323, 18, 225, new Item[]{new Item(Items.OAK_PLANK_8778, 3), new Item(Items.BOLT_OF_CLOTH_8790, 3)}),
    OPULENT_CURTAINS(13605, 8324, 40, 315, new Item[]{new Item(Items.TEAK_PLANK_8780, 3), new Item(Items.BOLT_OF_CLOTH_8790, 3)}),

    WOODEN_BOOKCASE(13597, 8319, 4, Items.WOODEN_BOOKCASE_8510, 132, new Item[]{new Item(Items.PLANK_960, 4)}),
    OAK_BOOKCASE(13598, 8320, 29, Items.OAK_BOOKCASE_8512, 225, new Item[]{new Item(Items.OAK_PLANK_8778, 3)}),
    MAHOGANY_BOOKCASE(13599, 8321, 40, Items.MAHOGANY_BKCASE_8514, 315, new Item[]{new Item(Items.MAHOGANY_PLANK_8782, 3)}),

    BASIC_BEER_BARREL(13568, 8239, 7, Items.BEER_BARREL_8516, 87, new Item[]{new Item(Items.PLANK_960, 3)}),
    CIDER_BARREL(13569, 8240, 12, Items.CIDER_BARREL_8518, 91, new Item[]{new Item(Items.PLANK_960, 3), new Item(Items.CIDER_5763, 8)}),
    ASGARNIAN_ALE_BARREL(13570, 8241, 18, Items.ASGARNIAN_ALE_8520, 184, new Item[]{new Item(Items.OAK_PLANK_8778, 3), new Item(Items.ASGARNIAN_ALE_1905, 8)}),
    GREENMANS_ALE_BARREL(13571, 8242, 26, Items.GREENMANS_ALE_8522, 184, new Item[]{new Item(Items.OAK_PLANK_8778, 3), new Item(Items.GREENMANS_ALE_1909, 8)}),
    DRAGON_BITTER_BARREL(13572, 8243, 36, Items.DRAGON_BITTER_8524, 224, new Item[]{new Item(Items.OAK_PLANK_8778, 3), new Item(Items.DRAGON_BITTER_1911, 8), new Item(Items.STEEL_BAR_2353, 2)}),
    CHEFS_DELIGHT_BARREL(13573, 8244, 48, Items.CHEFS_DELIGHT_8526, 224, new Item[]{new Item(Items.OAK_PLANK_8778, 3), new Item(Items.CHEFS_DELIGHT_5755, 8), new Item(Items.STEEL_BAR_2353, 2)}),

    KITCHEN_WOODEN_TABLE(13577, 8246, 12, Items.WOOD_KITCHEN_TABLE_8528, 87, new Item[]{new Item(Items.PLANK_960, 3)}),
    KITCHEN_OAK_TABLE(13578, 8247, 32, Items.OAK_KITCHEN_TABLE_8530, 180, new Item[]{new Item(Items.OAK_PLANK_8778, 3)}),
    KITCHEN_TEAK_TABLE(13579, 8248, 52, Items.TEAK_KITCHEN_TABLE_8532, 270, new Item[]{new Item(Items.TEAK_PLANK_8780, 3)}),

    BASIC_FIREPIT(13528, 8216, 5, 40, new Item[]{new Item(Items.SOFT_CLAY_1761, 2), new Item(Items.STEEL_BAR_2353)}),
    FIREPIT_WITH_HOOK(13529, 8217, 11, 60, new Item[]{new Item(Items.SOFT_CLAY_1761, 2), new Item(Items.STEEL_BAR_2353, 2)}),
    FIREPIT_WITH_POT(13531, 8218, 17, 80, new Item[]{new Item(Items.SOFT_CLAY_1761, 2), new Item(Items.STEEL_BAR_2353, 3)}),
    SMALL_OVEN(13533, 8219, 24, 80, new Item[]{new Item(Items.STEEL_BAR_2353, 4)}),
    LARGE_OVEN(13536, 8220, 29, 100, new Item[]{new Item(Items.STEEL_BAR_2353, 5)}),
    BASIC_RANGE(13539, 8221, 34, 120, new Item[]{new Item(Items.STEEL_BAR_2353, 6)}),
    FANCY_RANGE(13542, 8222, 42, 160, new Item[]{new Item(Items.STEEL_BAR_2353, 8)}),

    WOODEN_LARDER(13565, 8233, 9, 228, new Item[]{new Item(Items.PLANK_960, 8)}),
    OAK_LARDER(13566, 8234, 33, 480, new Item[]{new Item(Items.OAK_PLANK_8778, 8)}),
    TEAK_LARDER(13567, 8235, 43, 750, new Item[]{new Item(Items.TEAK_PLANK_8780, 8), new Item(Items.BOLT_OF_CLOTH_8790, 2)}),

    WOODEN_SHELVES_1(13545, 8223, 6, 87, new Item[]{new Item(Items.PLANK_960, 3)}),
    WOODEN_SHELVES_2(13546, 8224, 12, 147, new Item[]{new Item(Items.PLANK_960, 3), new Item(Items.SOFT_CLAY_1761, 6)}),
    WOODEN_SHELVES_3(13547, 8225, 23, 147, new Item[]{new Item(Items.PLANK_960, 3), new Item(Items.SOFT_CLAY_1761, 6)}),
    OAK_SHELVES_1(13548, 8226, 34, 240, new Item[]{new Item(Items.OAK_PLANK_8778, 3), new Item(Items.SOFT_CLAY_1761, 6)}),
    OAK_SHELVES_2(13549, 8227, 45, 240, new Item[]{new Item(Items.OAK_PLANK_8778, 3), new Item(Items.SOFT_CLAY_1761, 6)}),
    TEAK_SHELVES_1(13550, 8228, 56, 330, new Item[]{new Item(Items.TEAK_PLANK_8780, 3), new Item(Items.SOFT_CLAY_1761, 6)}),
    TEAK_SHELVES_2(13551, 8229, 67, 930, new Item[]{new Item(Items.TEAK_PLANK_8780, 3), new Item(Items.SOFT_CLAY_1761, 6), new Item(Items.GOLD_LEAF_8784, 2)}),

    PUMP_AND_DRAIN(13559, 8230, 7, 100, new Item[]{new Item(Items.STEEL_BAR_2353, 5)}),
    PUMP_AND_TUB(13561, 8231, 27, 200, new Item[]{new Item(Items.STEEL_BAR_2353, 10)}),
    SINK(13563, 8232, 47, 300, new Item[]{new Item(Items.STEEL_BAR_2353, 15)}),

    CAT_BLANKET(13574, 8236, 5, 15, new Item[]{new Item(Items.BOLT_OF_CLOTH_8790)}),
    CAT_BASKET(13575, 8237, 19, 58, new Item[]{new Item(Items.PLANK_960, 2)}),
    CAST_BASKET_CUSHIONED(13576, 8238, 33, 58, new Item[]{new Item(Items.PLANK_960, 2), new Item(Items.WOOL_1737, 2)}),

    DINING_TABLE_WOOD(13293, 8115, 10, Items.WOOD_DINING_TABLE_8548, 115, new Item[]{new Item(Items.PLANK_960, 4)}),
    DINING_TABLE_OAK(13294, 8116, 22, Items.OAK_DINING_TABLE_8550, 240, new Item[]{new Item(Items.OAK_PLANK_8778, 4)}),
    DINING_TABLE_CARVED_OAK(13295, 8117, 31, Items.CARVED_OAK_TABLE_8552, 360, new Item[]{new Item(Items.OAK_PLANK_8778, 6)}),
    DINING_TABLE_TEAK(13296, 8118, 38, Items.TEAK_TABLE_8554, 360, new Item[]{new Item(Items.TEAK_PLANK_8780, 4)}),
    DINING_TABLE_CARVED_TEAK(13297, 8119, 45, Items.CARVED_TEAK_TABLE_8556, 600, new Item[]{new Item(Items.TEAK_PLANK_8780, 6), new Item(Items.BOLT_OF_CLOTH_8790, 4)}),
    DINING_TABLE_MAHOGANY(13298, 8120, 52, Items.MAHOGANY_TABLE_8558, 840, new Item[]{new Item(Items.MAHOGANY_PLANK_8782, 6)}),
    DINING_TABLE_OPULENT(13299, 8121, 72, Items.OPULENT_TABLE_8560, 3100, new Item[]{new Item(Items.MAHOGANY_PLANK_8782, 6), new Item(Items.BOLT_OF_CLOTH_8790, 4), new Item(Items.GOLD_LEAF_8784, 4), new Item(Items.MARBLE_BLOCK_8786, 2)}),

    BENCH_WOODEN(13300, 8108, 10, Items.WOODEN_BENCH_8562, 115, new Item[]{new Item(Items.PLANK_960, 4)}),
    BENCH_OAK(13301, 8109, 22, Items.OAK_BENCH_8564, 240, new Item[]{new Item(Items.OAK_PLANK_8778, 4)}),
    BENCH_CARVED_OAK(13302, 8110, 31, Items.CARVED_OAK_BENCH_8566, 240, new Item[]{new Item(Items.OAK_PLANK_8778, 4)}),
    BENCH_TEAK(13303, 8111, 38, Items.TEAK_DINING_BENCH_8568, 360, new Item[]{new Item(Items.TEAK_PLANK_8780, 4)}),
    BENCH_CARVED_TEAK(13304, 8112, 44, Items.CARVED_TEAK_BENCH_8570, 360, new Item[]{new Item(Items.TEAK_PLANK_8780, 4)}),
    BENCH_MAHOGANY(13305, 8113, 52, Items.MAHOGANY_BENCH_8572, 560, new Item[]{new Item(Items.MAHOGANY_PLANK_8782, 6)}),
    BENCH_GILDED(13306, 8114, 61, Items.GILDED_BENCH_8574, 1760, new Item[]{new Item(Items.MAHOGANY_PLANK_8782, 4), new Item(Items.GOLD_LEAF_8784, 4)}),

    ROPE_PULL(13307, 8099, 5, 15, new Item[]{new Item(Items.ROPE_954), new Item(Items.OAK_PLANK_8778)}),
    BELL_PULL(13308, 8100, 19, 58, new Item[]{new Item(Items.TEAK_PLANK_8780), new Item(Items.BOLT_OF_CLOTH_8790, 2)}),
    FANCY_BELL_PULL(13309, 8101, 33, 58, new Item[]{new Item(Items.TEAK_PLANK_8780), new Item(Items.BOLT_OF_CLOTH_8790, 2), new Item(Items.GOLD_LEAF_8784)}),

    WORKBENCH_WOODEN(13704, 8375, 17, 143, new Item[]{new Item(Items.PLANK_960, 5)}),
    WORKBENCH_OAK(13705, 8376, 32, 300, new Item[]{new Item(Items.OAK_PLANK_8778, 5)}),
    WORKBENCH_STEEL_FRAME(13706, 8377, 46, 440, new Item[]{new Item(Items.OAK_PLANK_8778, 6), new Item(Items.STEEL_BAR_2353, 4)}),
    WORKBENCH_WITH_VICE(13707, 8378, 62, 750, new Item[]{new Item(Items.STEEL_FRAMED_BENCH_8377), new Item(Items.OAK_PLANK_8778, 2), new Item(Items.STEEL_BAR_2353)}),
    WORKBENCH_WITH_LATHE(13708, 8379, 77, 1000, new Item[]{new Item(Items.OAK_WORKBENCH_8376), new Item(Items.OAK_PLANK_8778, 2), new Item(Items.STEEL_BAR_2353)}),

    REPAIR_BENCH(13713, 8389, 15, 120, new Item[]{new Item(Items.OAK_PLANK_8778, 2)}),
    WHETSTONE(13714, 8390, 35, 260, new Item[]{new Item(Items.OAK_PLANK_8778, 4), new Item(Items.LIMESTONE_BRICK_3420)}),
    ARMOUR_STAND(13715, 8391, 55, 500, new Item[]{new Item(Items.OAK_PLANK_8778, 8), new Item(Items.LIMESTONE_BRICK_3420)}),

    PLUMING_STAND(13716, 8392, 16, 120, new Item[]{new Item(Items.OAK_PLANK_8778, 2)}),
    SHIELD_EASEL(13717, 8393, 41, 240, new Item[]{new Item(Items.OAK_PLANK_8778, 4)}),
    BANNER_EASEL(13718, 8394, 66, 510, new Item[]{new Item(Items.OAK_PLANK_8778, 8), new Item(Items.BOLT_OF_CLOTH_8790, 2)}),

    CRAFTING_TABLE_1(13709, 8380, 16, 240, new Item[]{new Item(Items.OAK_PLANK_8778, 4)}),
    CRAFTING_TABLE_2(13710, 8381, 25, 1, new Item[]{new Item(Items.MOLTEN_GLASS_1775)}),
    CRAFTING_TABLE_3(13711, 8382, 34, 2, new Item[]{new Item(Items.MOLTEN_GLASS_1775, 2)}),
    CRAFTING_TABLE_4(13712, 8383, 42, 120, new Item[]{new Item(Items.OAK_PLANK_8778, 2)}),

    TOOL_STORE_1(13699, 8384, 15, 120, new Item[]{new Item(Items.OAK_PLANK_8778, 2)}),
    TOOL_STORE_2(13700, 8385, 25, 120, new Item[]{new Item(Items.OAK_PLANK_8778, 2)}),
    TOOL_STORE_3(13701, 8386, 35, 120, new Item[]{new Item(Items.OAK_PLANK_8778, 2)}),
    TOOL_STORE_4(13702, 8387, 44, 120, new Item[]{new Item(Items.OAK_PLANK_8778, 2)}),
    TOOL_STORE_5(13703, 8388, 55, 120, new Item[]{new Item(Items.OAK_PLANK_8778, 2)}),


    OAK_DECORATION(13606, 8102, 16, 120, new Item[]{new Item(Items.OAK_PLANK_8778, 2)}),
    TEAK_DECORATION(13606, 8103, 36, 180, new Item[]{new Item(Items.TEAK_PLANK_8780, 2)}),
    GILDED_DECORATION(13607, 8104, 56, 1020, new Item[]{new Item(Items.MAHOGANY_PLANK_8782, 3), new Item(Items.GOLD_LEAF_8784, 2)}),

    OAK_STAIRCASE(13497, 8249, 27, 680, new Item[]{new Item(Items.OAK_PLANK_8778, 10), new Item(Items.STEEL_BAR_2353, 4)}),
    TEAK_STAIRCASE(13499, 8252, 48, 980, new Item[]{new Item(Items.TEAK_PLANK_8780, 10), new Item(Items.STEEL_BAR_2353, 4)}),
    SPIRAL_STAIRCASE(13503, 8258, 67, 1040, new Item[]{new Item(Items.TEAK_PLANK_8780, 10), new Item(Items.LIMESTONE_BRICK_3420, 7)}),
    MARBLE_STAIRCASE(13501, 8257, 82, 3200, new Item[]{new Item(Items.MAHOGANY_PLANK_8782, 5), new Item(Items.MARBLE_BLOCK_8786, 5)}),
    MARBLE_SPIRAL(13505, 8259, 97, 4400, new Item[]{new Item(Items.TEAK_PLANK_8780, 10), new Item(Items.MARBLE_BLOCK_8786, 7)}),

    OAK_STAIRS_DOWN(13498, 8249, 27, 680, new Item[]{new Item(Items.OAK_PLANK_8778, 10), new Item(Items.STEEL_BAR_2353, 4)}),
    TEAK_STAIRS_DOWN(13500, 8252, 48, 980, new Item[]{new Item(Items.TEAK_PLANK_8780, 10), new Item(Items.STEEL_BAR_2353, 4)}),
    SPIRAL_STAIRS_DOWN(13504, 8258, 67, 1040, new Item[]{new Item(Items.TEAK_PLANK_8780, 10), new Item(Items.LIMESTONE_BRICK_3420, 7)}),
    MARBLE_STAIRS_DOWN(13502, 8257, 82, 3200, new Item[]{new Item(Items.MAHOGANY_PLANK_8782, 5), new Item(Items.MARBLE_BLOCK_8786, 5)}),
    MARBLE_SPIRAL_DOWN(13506, 8259, 97, 4400, new Item[]{new Item(Items.TEAK_PLANK_8780, 10), new Item(Items.MARBLE_BLOCK_8786, 7)}),

    TEAK_PORTAL(13636, 8328, 50, 270, new Item[]{new Item(Items.TEAK_PLANK_8780, 3)}),
    MAHOGANY_PORTAL(13637, 8329, 65, 420, new Item[]{new Item(Items.MAHOGANY_PLANK_8782, 3)}),
    MARBLE_PORTAL(13638, 8330, 80, 1500, new Item[]{new Item(Items.MARBLE_BLOCK_8786, 3)}),
    TELEPORT_FOCUS(13640, 8331, 50, 40, new Item[]{new Item(Items.LIMESTONE_BRICK_3420, 2)}),
    GREATER_TELEPORT_FOCUS(13641, 8332, 65, 500, new Item[]{new Item(Items.MARBLE_BLOCK_8786)}),
    SCRYING_POOL(13639, 8333, 80, 2000, new Item[]{new Item(Items.MARBLE_BLOCK_8786, 4)}),
    TEAK_VARROCK_PORTAL(13615, true),
    MAHOGANY_VARROCK_PORTAL(13622, true),
    MARBLE_VARROCK_PORTAL(13629, true),
    TEAK_LUMBRIDGE_PORTAL(13616, true),
    MAHOGANY_LUMBRIDGE_PORTAL(13623, true),
    MARBLE_LUMBRIDGE_PORTAL(13630, true),
    TEAK_FALADOR_PORTAL(13617, true),
    MAHOGANY_FALADOR_PORTAL(13624, true),
    MARBLE_FALADOR_PORTAL(13631, true),
    TEAK_CAMELOT_PORTAL(13618, true),
    MAHOGANY_CAMELOT_PORTAL(13625, true),
    MARBLE_CAMELOT_PORTAL(13632, true),
    TEAK_ARDOUGNE_PORTAL(13619, true),
    MAHOGANY_ARDOUGNE_PORTAL(13626, true),
    MARBLE_ARDOUGNE_PORTAL(13633, true),
    TEAK_YANILLE_PORTAL(13620, true),
    MAHOGANY_YANILLE_PORTAL(13627, true),
    MARBLE_YANILLE_PORTAL(13634, true),
    TEAK_KHARYRLL_PORTAL(13621, true),
    MAHOGANY_KHARYRLL_PORTAL(13628, true),
    MARBLE_KHARYRLL_PORTAL(13635, true),

    MITHRIL_ARMOUR(13491, 8270, 28, 135, new Item[]{new Item(Items.OAK_PLANK_8778, 2), new Item(Items.MITHRIL_FULL_HELM_1159, 1), new Item(Items.MITHRIL_PLATEBODY_1121, 1), new Item(Items.MITHRIL_PLATESKIRT_1085, 1)}, new Item[]{new Item(Items.MITHRIL_FULL_HELM_1159, 1), new Item(Items.MITHRIL_PLATEBODY_1121, 1), new Item(Items.MITHRIL_PLATESKIRT_1085, 1)}),
    ADAMANT_ARMOUR(13492, 8271, 28, 150, new Item[]{new Item(Items.OAK_PLANK_8778, 2), new Item(Items.ADAMANT_FULL_HELM_1161, 1), new Item(Items.ADAMANT_PLATEBODY_1123, 1), new Item(Items.ADAMANT_PLATESKIRT_1091, 1)}, new Item[]{new Item(Items.ADAMANT_FULL_HELM_1161, 1), new Item(Items.ADAMANT_PLATEBODY_1123, 1), new Item(Items.ADAMANT_PLATESKIRT_1091, 1)}),
    RUNE_ARMOUR(13493, 8272, 28, 165, new Item[]{new Item(Items.OAK_PLANK_8778, 2), new Item(Items.RUNE_FULL_HELM_1163, 1), new Item(Items.RUNE_PLATEBODY_1127, 1), new Item(Items.RUNE_PLATESKIRT_1093, 1)}, new Item[]{new Item(Items.RUNE_FULL_HELM_1163, 1), new Item(Items.RUNE_PLATEBODY_1127, 1), new Item(Items.RUNE_PLATESKIRT_1093, 1)}),
    CRAWLING_HAND(13481, 8260, 38, 211, new Item[]{new Item(Items.TEAK_PLANK_8780, 2), new Item(Items.CRAWLING_HAND_7982)}),
    COCKATRICE_HEAD(13482, 8261, 38, 224, new Item[]{new Item(Items.TEAK_PLANK_8780, 2), new Item(Items.COCKATRICE_HEAD_7983)}),
    BASILISK_HEAD(13483, 8262, 38, 243, new Item[]{new Item(Items.TEAK_PLANK_8780, 2), new Item(Items.BASILISK_HEAD_7984)}),
    KURASK_HEAD(13484, 8263, 58, 357, new Item[]{new Item(Items.MAHOGANY_PLANK_8782, 2), new Item(Items.KURASK_HEAD_7985)}),
    ABYSSAL_DEMON_HEAD(13485, 8264, 58, 389, new Item[]{new Item(Items.MAHOGANY_PLANK_8782, 2), new Item(Items.ABYSSAL_HEAD_7986)}),
    KBD_HEAD(13486, 8265, 78, 1103, new Item[]{new Item(Items.MAHOGANY_PLANK_8782, 2), new Item(Items.KBD_HEADS_7987)}),
    KQ_HEAD(13487, 8266, 78, 1103, new Item[]{new Item(Items.MAHOGANY_PLANK_8782, 2), new Item(Items.KQ_HEAD_7988)}),
    MOUNTED_BASS(13488, 8267, 36, 151, new Item[]{new Item(Items.OAK_PLANK_8778, 2), new Item(Items.BIG_BASS_7990)}),
    MOUNTED_SWORDFISH(13489, 8268, 56, 230, new Item[]{new Item(Items.TEAK_PLANK_8780, 2), new Item(Items.BIG_SWORDFISH_7992)}),
    MOUNTED_SHARK(13490, 8269, 76, 350, new Item[]{new Item(Items.MAHOGANY_PLANK_8782, 2), new Item(Items.BIG_SHARK_7994)}),
    RUNE_CASE1(13507, 8095, 41, 190, new Item[]{new Item(Items.TEAK_PLANK_8780, 2), new Item(Items.MOLTEN_GLASS_1775, 2), new Item(Items.FIRE_RUNE_554, 1), new Item(Items.AIR_RUNE_556, 1), new Item(Items.EARTH_RUNE_557, 1), new Item(Items.WATER_RUNE_555, 1)}),
    RUNE_CASE2(13508, 8095, 41, 212, new Item[]{new Item(Items.TEAK_PLANK_8780, 2), new Item(Items.MOLTEN_GLASS_1775, 2), new Item(Items.BODY_RUNE_559, 1), new Item(Items.COSMIC_RUNE_564, 1), new Item(Items.CHAOS_RUNE_562, 1), new Item(Items.NATURE_RUNE_561, 1)}),

    CLAY_STONE(13392, 8153, 39, 100, new Item[]{new Item(Items.SOFT_CLAY_1761, 10)}),
    LIMESTONE_STONE(13393, 8154, 59, 200, new Item[]{new Item(Items.LIMESTONE_BRICK_3420, 10)}),
    MARBLE_STONE(13394, 8155, 79, 2000, new Item[]{new Item(Items.MARBLE_BLOCK_8786, 4)}),
    HOOP_AND_STICK(13398, 8162, 30, 120, new Item[]{new Item(Items.OAK_PLANK_8778, 2)}),
    DARTBOARD(13400, 8163, 54, 290, new Item[]{new Item(Items.TEAK_PLANK_8780, 3), new Item(Items.STEEL_BAR_2353)}),
    ARCHERY_TARGET(13402, 8164, 81, 600, new Item[]{new Item(Items.TEAK_PLANK_8780, 6), new Item(Items.STEEL_BAR_2353, 3)}),
    BALANCE_1(13395, 8156, 37, 176, new Item[]{new Item(Items.FIRE_RUNE_554, 500), new Item(Items.AIR_RUNE_556, 500), new Item(Items.EARTH_RUNE_557, 500), new Item(Items.WATER_RUNE_555, 500)}),
    BALANCE_2(13396, 8157, 57, 252, new Item[]{new Item(Items.FIRE_RUNE_554, 1000), new Item(Items.AIR_RUNE_556, 1000), new Item(Items.EARTH_RUNE_557, 1000), new Item(Items.WATER_RUNE_555, 1000)}),
    BALANCE_3(13397, 8158, 77, 356, new Item[]{new Item(Items.FIRE_RUNE_554, 2000), new Item(Items.AIR_RUNE_556, 2000), new Item(Items.EARTH_RUNE_557, 2000), new Item(Items.WATER_RUNE_555, 2000)}),
    OAK_CHEST(13385, 8165, 34, 240, new Item[]{new Item(Items.OAK_PLANK_8778, 4)}),
    TEAK_CHEST(13387, 8166, 44, 660, new Item[]{new Item(Items.TEAK_PLANK_8780, 4), new Item(Items.GOLD_LEAF_8784)}),
    MAHOGANY_CHEST(13389, 8167, 54, 860, new Item[]{new Item(Items.MAHOGANY_PLANK_8782, 4), new Item(Items.GOLD_LEAF_8784)}),
    JESTER(13390, 8159, 39, 360, new Item[]{new Item(Items.TEAK_PLANK_8780, 4)}),
    TREASURE_HUNT(13379, 8160, 49, 800, new Item[]{new Item(Items.TEAK_PLANK_8780, 8), new Item(Items.STEEL_BAR_2353, 4)}),
    HANGMAN(13404, 8161, 59, 1200, new Item[]{new Item(Items.TEAK_PLANK_8780, 12), new Item(Items.STEEL_BAR_2353, 6)}),

    BOXING_RING(13129, 8023, 32, 570, new Item[]{new Item(Items.OAK_PLANK_8778, 6), new Item(Items.BOLT_OF_CLOTH_8790, 4)}),
    FENCING_RING(13133, 8024, 41, 570, new Item[]{new Item(Items.OAK_PLANK_8778, 8), new Item(Items.BOLT_OF_CLOTH_8790, 6)}),
    COMBAT_RING(13137, 8025, 51, 630, new Item[]{new Item(Items.TEAK_PLANK_8780, 6), new Item(Items.BOLT_OF_CLOTH_8790, 6)}),
    BALANCE_BEAM_LEFT(13143, 8027, 81, 1000, new Item[]{new Item(Items.TEAK_PLANK_8780, 10), new Item(Items.STEEL_BAR_2353, 5)}),
    BALANCE_BEAM_CENTER(13142, 8027, 81, 1000, new Item[]{new Item(Items.TEAK_PLANK_8780, 10), new Item(Items.STEEL_BAR_2353, 5)}),
    BALANCE_BEAM_RIGHT(13144, 8027, 81, 1000, new Item[]{new Item(Items.TEAK_PLANK_8780, 10), new Item(Items.STEEL_BAR_2353, 5)}),
    RANGING_PEDESTALS(13147, 8026, 71, 720, new Item[]{new Item(Items.TEAK_PLANK_8780, 8)}),
    MAGIC_BARRIER(13145, 8026, 71, 720, new Item[]{new Item(Items.TEAK_PLANK_8780, 8)}),
    NOTHING(13721, 8027, 81, 1000, new Item[]{new Item(Items.TEAK_PLANK_8780, 10), new Item(Items.STEEL_BAR_2353, 5)}),
    NOTHING2(13721, 8026, 71, 720, new Item[]{new Item(Items.TEAK_PLANK_8780, 8)}),
    INVISIBLE_WALL(15283, 8023, 32, 570, new Item[]{new Item(Items.OAK_PLANK_8778, 6), new Item(Items.BOLT_OF_CLOTH_8790, 4)}),
    INVISIBLE_WALL2(15284, 8023, 32, 570, new Item[]{new Item(Items.OAK_PLANK_8778, 6), new Item(Items.BOLT_OF_CLOTH_8790, 4)}),
    INVISIBLE_WALL3(15285, 8023, 32, 570, new Item[]{new Item(Items.OAK_PLANK_8778, 6), new Item(Items.BOLT_OF_CLOTH_8790, 4)}),
    GLOVE_RACK(13381, 8028, 34, 120, new Item[]{new Item(Items.OAK_PLANK_8778, 2)}),
    WEAPONS_RACK(13382, 8029, 44, 180, new Item[]{new Item(Items.TEAK_PLANK_8780, 2)}),
    EXTRA_WEAPONS_RACK(13383, 8030, 54, 440, new Item[]{new Item(Items.TEAK_PLANK_8780, 4), new Item(Items.STEEL_BAR_2353, 4)}),
    BOXING_MAT_CORNER(13126, 8023, 32, 570, new Item[]{new Item(Items.OAK_PLANK_8778, 6), new Item(Items.BOLT_OF_CLOTH_8790, 4)}),
    FENCING_MAT_CORNER(13135, 8024, 41, 570, new Item[]{new Item(Items.OAK_PLANK_8778, 8), new Item(Items.BOLT_OF_CLOTH_8790, 6)}),
    COMBAT_MAT_CORNER(13138, 8025, 51, 630, new Item[]{new Item(Items.TEAK_PLANK_8780, 6), new Item(Items.BOLT_OF_CLOTH_8790, 6)}),
    BOXING_MAT_SIDE(13128, 8023, 32, 570, new Item[]{new Item(Items.OAK_PLANK_8778, 6), new Item(Items.BOLT_OF_CLOTH_8790, 4)}),
    FENCING_MAT_SIDE(13134, 8024, 41, 570, new Item[]{new Item(Items.OAK_PLANK_8778, 8), new Item(Items.BOLT_OF_CLOTH_8790, 6)}),
    COMBAT_MAT_SIDE(13139, 8025, 51, 630, new Item[]{new Item(Items.TEAK_PLANK_8780, 6), new Item(Items.BOLT_OF_CLOTH_8790, 6)}),
    BOXING_MAT(13127, 8023, 32, 570, new Item[]{new Item(Items.OAK_PLANK_8778, 6), new Item(Items.BOLT_OF_CLOTH_8790, 4)}),
    FENCING_MAT(13136, 8024, 41, 570, new Item[]{new Item(Items.OAK_PLANK_8778, 8), new Item(Items.BOLT_OF_CLOTH_8790, 6)}),
    COMBAT_MAT(13140, 8025, 51, 630, new Item[]{new Item(Items.TEAK_PLANK_8780, 6), new Item(Items.BOLT_OF_CLOTH_8790, 6)}),

    GAZEBO(13477, 8192, 65, 1200, new Item[]{new Item(Items.MAHOGANY_PLANK_8782, 8), new Item(Items.STEEL_BAR_2353, 4)}),
    SMALL_FOUNTAIN(13478, 8193, 71, 500, new Item[]{new Item(Items.MARBLE_BLOCK_8786)}),
    LARGE_FOUNTAIN(13479, 8194, 75, 1000, new Item[]{new Item(Items.MARBLE_BLOCK_8786, 2)}),
    POSH_FOUNTAIN(13480, 8195, 81, 1500, new Item[]{new Item(Items.MARBLE_BLOCK_8786, 3)}),
    SUNFLOWER(13446, 8213, 66, 70, new int[]{BuildingUtils.WATERING_CAN}, new Item[]{new Item(Items.BAGGED_SUNFLOWER_8457)}),
    MARIGOLDS(13447, 8214, 71, 100, new int[]{BuildingUtils.WATERING_CAN}, new Item[]{new Item(Items.BAGGED_MARIGOLDS_8459)}),
    ROSES(13448, 8215, 76, 122, new int[]{BuildingUtils.WATERING_CAN}, new Item[]{new Item(Items.BAGGED_ROSES_8461)}),
    SUNFLOWER_BIG(13443, 8213, 66, 70, new int[]{BuildingUtils.WATERING_CAN}, new Item[]{new Item(Items.BAGGED_SUNFLOWER_8457)}),
    MARIGOLDS_BIG(13444, 8214, 71, 100, new int[]{BuildingUtils.WATERING_CAN}, new Item[]{new Item(Items.BAGGED_MARIGOLDS_8459)}),
    ROSES_BIG(13445, 8215, 76, 122, new int[]{BuildingUtils.WATERING_CAN}, new Item[]{new Item(Items.BAGGED_ROSES_8461)}),
    ROSEMARY(13440, 8210, 66, 70, new int[]{BuildingUtils.WATERING_CAN}, new Item[]{new Item(Items.BAGGED_ROSEMARY_8451)}),
    DAFFODILS(13441, 8211, 71, 100, new int[]{BuildingUtils.WATERING_CAN}, new Item[]{new Item(Items.BAGGED_DAFFODILS_8453)}),
    BLUEBELLS(13442, 8212, 76, 122, new int[]{BuildingUtils.WATERING_CAN}, new Item[]{new Item(Items.BAGGED_BLUEBELLS_8455)}),
    ROSEMARY_BIG(13437, 8210, 66, 70, new int[]{BuildingUtils.WATERING_CAN}, new Item[]{new Item(Items.BAGGED_ROSEMARY_8451)}),
    DAFFODILS_BIG(13438, 8211, 71, 100, new int[]{BuildingUtils.WATERING_CAN}, new Item[]{new Item(Items.BAGGED_DAFFODILS_8453)}),
    BLUEBELLS_BIG(13439, 8212, 76, 122, new int[]{BuildingUtils.WATERING_CAN}, new Item[]{new Item(Items.BAGGED_BLUEBELLS_8455)}),
    THORNY_HEDGE1(13456, 8203, 56, 70, new int[]{BuildingUtils.WATERING_CAN}, new Item[]{new Item(Items.THORNY_HEDGE_8437)}),
    THORNY_HEDGE2(13457, 8203, 56, 70, new int[]{BuildingUtils.WATERING_CAN}, new Item[]{new Item(Items.THORNY_HEDGE_8437)}),
    THORNY_HEDGE3(13458, 8203, 56, 70, new int[]{BuildingUtils.WATERING_CAN}, new Item[]{new Item(Items.THORNY_HEDGE_8437)}),
    NICE_HEDGE1(13459, 8204, 60, 100, new int[]{BuildingUtils.WATERING_CAN}, new Item[]{new Item(Items.NICE_HEDGE_8439)}),
    NICE_HEDGE2(13461, 8204, 60, 100, new int[]{BuildingUtils.WATERING_CAN}, new Item[]{new Item(Items.NICE_HEDGE_8439)}),
    NICE_HEDGE3(13460, 8204, 60, 100, new int[]{BuildingUtils.WATERING_CAN}, new Item[]{new Item(Items.NICE_HEDGE_8439)}),
    SMALL_BOX_HEDGE1(13462, 8205, 64, 122, new int[]{BuildingUtils.WATERING_CAN}, new Item[]{new Item(Items.SMALL_BOX_HEDGE_8441)}),
    SMALL_BOX_HEDGE2(13464, 8205, 64, 122, new int[]{BuildingUtils.WATERING_CAN}, new Item[]{new Item(Items.SMALL_BOX_HEDGE_8441)}),
    SMALL_BOX_HEDGE3(13463, 8205, 64, 122, new int[]{BuildingUtils.WATERING_CAN}, new Item[]{new Item(Items.SMALL_BOX_HEDGE_8441)}),
    TOPIARY_HEDGE1(13465, 8206, 68, 141, new int[]{BuildingUtils.WATERING_CAN}, new Item[]{new Item(Items.TOPIARY_HEDGE_8443)}),
    TOPIARY_HEDGE2(13467, 8206, 68, 141, new int[]{BuildingUtils.WATERING_CAN}, new Item[]{new Item(Items.TOPIARY_HEDGE_8443)}),
    TOPIARY_HEDGE3(13466, 8206, 68, 141, new int[]{BuildingUtils.WATERING_CAN}, new Item[]{new Item(Items.TOPIARY_HEDGE_8443)}),
    FANCY_HEDGE1(13468, 8207, 72, 158, new int[]{BuildingUtils.WATERING_CAN}, new Item[]{new Item(Items.FANCY_HEDGE_8445)}),
    FANCY_HEDGE2(13470, 8207, 72, 158, new int[]{BuildingUtils.WATERING_CAN}, new Item[]{new Item(Items.FANCY_HEDGE_8445)}),
    FANCY_HEDGE3(13469, 8207, 72, 158, new int[]{BuildingUtils.WATERING_CAN}, new Item[]{new Item(Items.FANCY_HEDGE_8445)}),
    TALL_FANCY_HEDGE1(13471, 8208, 76, 223, new int[]{BuildingUtils.WATERING_CAN}, new Item[]{new Item(Items.TALL_FANCY_HEDGE_8447)}),
    TALL_FANCY_HEDGE2(13473, 8208, 76, 223, new int[]{BuildingUtils.WATERING_CAN}, new Item[]{new Item(Items.TALL_FANCY_HEDGE_8447)}),
    TALL_FANCY_HEDGE3(13472, 8208, 76, 223, new int[]{BuildingUtils.WATERING_CAN}, new Item[]{new Item(Items.TALL_FANCY_HEDGE_8447)}),
    TALL_BOX_HEDGE1(13474, 8209, 80, 316, new int[]{BuildingUtils.WATERING_CAN}, new Item[]{new Item(Items.TALL_BOX_HEDGE_8449)}),
    TALL_BOX_HEDGE2(13476, 8209, 80, 316, new int[]{BuildingUtils.WATERING_CAN}, new Item[]{new Item(Items.TALL_BOX_HEDGE_8449)}),
    TALL_BOX_HEDGE3(13475, 8209, 80, 316, new int[]{BuildingUtils.WATERING_CAN}, new Item[]{new Item(Items.TALL_BOX_HEDGE_8449)}),
    BOUNDARY_STONES(13449, 8196, 55, 100, new Item[]{new Item(Items.SOFT_CLAY_1761, 10)}),
    WOODEN_FENCE(13450, 8197, 59, 280, new Item[]{new Item(Items.PLANK_960, 10)}),
    STONE_WALL(13451, 8198, 63, 200, new Item[]{new Item(Items.LIMESTONE_BRICK_3420, 10)}),
    IRON_RAILINGS(13452, 8199, 67, 220, new Item[]{new Item(Items.IRON_BAR_2351, 10), new Item(Items.LIMESTONE_BRICK_3420, 6)}),
    PICKET_FENCE(13453, 8200, 71, 640, new Item[]{new Item(Items.OAK_PLANK_8778, 10), new Item(Items.STEEL_BAR_2353, 2)}),
    GARDEN_FENCE(13454, 8201, 75, 940, new Item[]{new Item(Items.TEAK_PLANK_8780, 10), new Item(Items.STEEL_BAR_2353, 2)}),
    MARBLE_WALL(13455, 8202, 79, 4000, new Item[]{new Item(Items.MARBLE_BLOCK_8786, 10)}),

    WOODEN_BED(13148, 8031, 20, Items.WOODEN_BED_8576, 117, new Item[]{new Item(Items.PLANK_960, 3), new Item(Items.BOLT_OF_CLOTH_8790, 2)}),
    OAK_BED(13149, 8032, 30, Items.OAK_BED_8578, 210, new Item[]{new Item(Items.OAK_PLANK_8778, 3), new Item(Items.BOLT_OF_CLOTH_8790, 2)}),
    LARGE_OAK_BED(13150, 8033, 34, Items.LARGE_OAK_BED_8580, 330, new Item[]{new Item(Items.OAK_PLANK_8778, 5), new Item(Items.BOLT_OF_CLOTH_8790, 2)}),
    TEAK_BED(13151, 8034, 40, Items.TEAK_BED_8582, 300, new Item[]{new Item(Items.TEAK_PLANK_8780, 3), new Item(Items.BOLT_OF_CLOTH_8790, 2)}),
    LARGE_TEAK_BED(13152, 8035, 45, Items.LARGE_TEAK_BED_8584, 480, new Item[]{new Item(Items.TEAK_PLANK_8780, 5), new Item(Items.BOLT_OF_CLOTH_8790, 2)}),
    FOUR_POSTER(13153, 8036, 53, Items.FOUR_POSTER_8586, 450, new Item[]{new Item(Items.MAHOGANY_PLANK_8782, 3), new Item(Items.BOLT_OF_CLOTH_8790, 2)}),
    GILDED_FOUR_POSTER(13154, 8037, 60, Items.GILDED_4_POSTER_8588, 1330, new Item[]{new Item(Items.MAHOGANY_PLANK_8782, 5), new Item(Items.BOLT_OF_CLOTH_8790, 2), new Item(Items.GOLD_LEAF_8784, 2)}),
    OAK_CLOCK(13169, 8052, 25, Items.OAK_CLOCK_8590, 142, new Item[]{new Item(Items.OAK_PLANK_8778, 2), new Item(Items.CLOCKWORK_8792)}),
    TEAK_CLOCK(13170, 8053, 55, Items.TEAK_CLOCK_8592, 202, new Item[]{new Item(Items.TEAK_PLANK_8780, 2), new Item(Items.CLOCKWORK_8792)}),
    GILDED_CLOCK(13171, 8054, 85, Items.GILDED_CLOCK_8594, 602, new Item[]{new Item(Items.MAHOGANY_PLANK_8782, 2), new Item(Items.CLOCKWORK_8792), new Item(Items.GOLD_LEAF_8784)}),
    SHAVING_STAND(13162, 8045, 21, Items.SHAVING_STAND_8596, 30, new Item[]{new Item(Items.PLANK_960), new Item(Items.MOLTEN_GLASS_1775)}),
    OAK_SHAVING_STAND(13163, 8046, 29, Items.OAK_SHAVING_STAND_8598, 61, new Item[]{new Item(Items.OAK_PLANK_8778), new Item(Items.MOLTEN_GLASS_1775)}),
    OAK_DRESSER(13164, 8047, 37, Items.OAK_DRESSER_8600, 121, new Item[]{new Item(Items.OAK_PLANK_8778, 2), new Item(Items.MOLTEN_GLASS_1775)}),
    TEAK_DRESSER(13165, 8048, 46, Items.TEAK_DRESSER_8602, 181, new Item[]{new Item(Items.TEAK_PLANK_8780, 2), new Item(Items.MOLTEN_GLASS_1775)}),
    FANCY_TEAK_DRESSER(13166, 8049, 56, Items.FANCY_TEAK_DRESSER_8604, 182, new Item[]{new Item(Items.TEAK_PLANK_8780, 2), new Item(Items.MOLTEN_GLASS_1775, 2)}),
    MAHOGANY_DRESSER(13167, 8050, 64, Items.MAHOGANY_DRESSER_8606, 281, new Item[]{new Item(Items.MAHOGANY_PLANK_8782, 2), new Item(Items.MOLTEN_GLASS_1775)}),
    GILDED_DRESSER(13168, 8051, 74, Items.GILDED_DRESSER_8608, 582, new Item[]{new Item(Items.MAHOGANY_PLANK_8782, 2), new Item(Items.MOLTEN_GLASS_1775, 2), new Item(Items.GOLD_LEAF_8784)}),
    SHOE_BOX(13155, 8038, 20, Items.SHOE_BOX_8610, 58, new Item[]{new Item(Items.PLANK_960, 2)}),
    OAK_DRAWERS(13156, 8039, 27, Items.OAK_DRAWERS_8612, 120, new Item[]{new Item(Items.OAK_PLANK_8778, 2)}),
    OAK_WARDROBE(13157, 8040, 39, Items.OAK_WARDROBE_8614, 180, new Item[]{new Item(Items.OAK_PLANK_8778, 3)}),
    TEAK_DRAWERS(13158, 8041, 51, Items.TEAK_DRAWERS_8616, 180, new Item[]{new Item(Items.TEAK_PLANK_8780, 2)}),
    TEAK_WARDROBE(13159, 8042, 63, Items.TEAK_WARDROBE_8618, 270, new Item[]{new Item(Items.TEAK_PLANK_8780, 3)}),
    MAHOGANY_WARDROBE(13160, 8043, 75, Items.MAHOGANY_DROBE_8620, 420, new Item[]{new Item(Items.MAHOGANY_PLANK_8782, 2)}),
    GILDED_WARDROBE(13161, 8044, 87, Items.GILDED_WARDROBE_8622, 720, new Item[]{new Item(Items.MAHOGANY_PLANK_8782, 2), new Item(Items.GOLD_LEAF_8784, 1)}),
    ANTIDRAGON_SHIELD(13522, 8282, 47, 280, new Item[]{new Item(Items.TEAK_PLANK_8780, 3), new Item(Items.ANTI_DRAGON_SHIELD_1540)}, new Item[]{new Item(Items.ANTI_DRAGON_SHIELD_1540)}),
    AMULET_OF_GLORY(13523, 8283, 47, 290, new Item[]{new Item(Items.TEAK_PLANK_8780, 3), new Item(Items.AMULET_OF_GLORY_1704)}, new Item[]{new Item(Items.AMULET_OF_GLORY_1704)}),
    CAPE_OF_LEGENDS(13524, 8284, 47, 300, new Item[]{new Item(Items.TEAK_PLANK_8780, 3), new Item(Items.CAPE_OF_LEGENDS_1052)}, new Item[]{new Item(Items.CAPE_OF_LEGENDS_1052)}),
    KING_ARTHUR(13510, 8285, 35, 211, new Item[]{new Item(Items.TEAK_PLANK_8780, 3), new Item(Items.ARTHUR_PORTRAIT_7995)}),
    ELENA(13511, 8286, 35, 211, new Item[]{new Item(Items.TEAK_PLANK_8780, 3), new Item(Items.ELENA_PORTRAIT_7996)}),
    GIANT_DWARF(13512, 8287, 35, 211, new Item[]{new Item(Items.TEAK_PLANK_8780, 3), new Item(Items.KELDAGRIM_PORTRAIT_7997)}),
    MISCELLANIANS(13513, 8288, 35, 311, new Item[]{new Item(Items.TEAK_PLANK_8780, 3), new Item(Items.MISC_PORTRAIT_7998)}),
    LUMBRIDGE(13517, 8289, 44, 314, new Item[]{new Item(Items.TEAK_PLANK_8780, 3), new Item(Items.LUMBRIDGE_PAINTING_8002)}),
    THE_DESERT(13514, 8290, 44, 314, new Item[]{new Item(Items.TEAK_PLANK_8780, 3), new Item(Items.DESERT_PAINTING_7999)}),
    MORYTANIA(13518, 8291, 44, 314, new Item[]{new Item(Items.TEAK_PLANK_8780, 3), new Item(Items.MORYTANIA_PAINTING_8003)}),
    KARAMJA(13516, 8292, 65, 464, new Item[]{new Item(Items.MAHOGANY_PLANK_8782, 3), new Item(Items.KARAMJA_PAINTING_8001)}),
    ISAFDAR(13515, 8293, 65, 464, new Item[]{new Item(Items.MAHOGANY_PLANK_8782, 3), new Item(Items.ISAFDAR_PAINTING_8000)}),
    SILVERLIGHT(13519, 8279, 42, 187, new Item[]{new Item(Items.TEAK_PLANK_8780, 3), new Item(Items.SILVERLIGHT_2402)}, new Item[]{new Item(Items.SILVERLIGHT_2402)}),
    EXCALIBUR(13521, 8280, 42, 194, new Item[]{new Item(Items.TEAK_PLANK_8780, 3), new Item(Items.EXCALIBUR_35)}, new Item[]{new Item(Items.EXCALIBUR_35)}),
    DARKLIGHT(13520, 8281, 42, 202, new Item[]{new Item(Items.TEAK_PLANK_8780, 3), new Item(Items.DARKLIGHT_6746)}, new Item[]{new Item(Items.DARKLIGHT_6746)}),
    SMALL_MAP(13525, 8294, 38, 211, new Item[]{new Item(Items.TEAK_PLANK_8780, 3), new Item(Items.SMALL_MAP_8004)}),
    MEDIUM_MAP(13526, 8295, 58, 451, new Item[]{new Item(Items.MAHOGANY_PLANK_8782, 3), new Item(Items.MEDIUM_MAP_8005)}),
    LARGE_MAP(13527, 8296, 78, 591, new Item[]{new Item(Items.MAHOGANY_PLANK_8782, 4), new Item(Items.LARGE_MAP_8006)}),
    MINI_OBELISK(42004, Items.SUMMONING_OBELISK_14644, 41, 676, new Item[]{new Item(Items.MARBLE_BLOCK_8786, 1), new Item(Items.SPIRIT_SHARDS_12183, 1000), new Item(Items.GOLD_CHARM_12158, 10), new Item(Items.GREEN_CHARM_12159, 10), new Item(Items.CRIMSON_CHARM_12160, 10), new Item(Items.BLUE_CHARM_12163, 10)}),
    //OAK_PET_FEEDER(44834, 15233, 37, 240, new Item[]{new Item(Items.OAK_PLANK_8778, 4)}),
    //TEAK_PET_FEEDER(44835, 15234, 52, 380, new Item[]{new Item(Items.TEAK_PLANK_8780, 4)}),
    //MAHOGANY_PET_FEEDER(44836, 15235, 67, 880, new Item[]{new Item(Items.MAHOGANY_PLANK_8782, 4), new Item(Items.GOLD_LEAF_8784, 1)}),
    //OAK_PET_HOUSE(44828, 15227, 37, 240, new Item[]{new Item(Items.OAK_PLANK_8778, 4)}),
    //TEAK_PET_HOUSE(44829, 15228, 52, 380, new Item[]{new Item(Items.TEAK_PLANK_8780, 4)}),
    //MAHOGANY_PET_HOUSE(44830, 15229, 67, 580, new Item[]{new Item(Items.MAHOGANY_PLANK_8782, 4)}),
    //CONSECRATED_PET_HOUSE(44831, 15230, 92, 1580, new Item[]{new Item(Items.MAHOGANY_PLANK_8782, 4), new Item(Items.MAGIC_STONE_8788, 1)}),
    //DESECRATED_PET_HOUSE(44832, 15231, 92, 1580, new Item[]{new Item(Items.MAHOGANY_PLANK_8782, 4), new Item(Items.MAGIC_STONE_8788, 1)}),
    //NATURAL_PET_HOUSE(44833, 15232, 92, 1580, new Item[]{new Item(Items.MAHOGANY_PLANK_8782, 4), new Item(Items.MAGIC_STONE_8788, 1)}),
    //GARDEN_HABITAT(new int[]{4497, 4498, 44500, 44501, 44502, 44503, 44504, 44505, 44506, 44507, 44508, 44509, 44510, 44511, 44512, 44513, 44514, 44515, 44516, 44517, 44518, 44519, 44520, 44521, 44522, 44523, 44524, 44525, 44526, 44527, 44528, 44529, 44530, 44531, 44532, 44533, 44534, 44535, 44536, 44537, 44538, 44539, 44540, 44541, 44542, 44543, 44544, 44545, 44546, 44547, 44548, 44549, 44550, 44551, 44552, 44553, 44554, 44555, 44556, 44557, 44558, 44559, 44560, 44561, 44562, 44563}, 15222, 37, 201, new Item[]{new Item(Items.BAGGED_PLANT_1_8431, 1), new Item(Items.BAGGED_PLANT_2_8433, 1), new Item(Items.BAGGED_PLANT_3_8435, 1)}),
    //JUNGLE_HABITAT(new int[]{44564, 44565, 44566, 44567, 44568, 44569, 44570, 44571, 44572, 44573, 44574, 44575, 44576, 44577, 44578, 44579, 44580, 44581, 44582, 44583, 44584, 44585, 44586, 44587, 44588, 44589, 44590, 44591, 44592, 44593, 44594, 44595, 44596, 44597, 44598, 44599, 44600, 44601, 44602, 44603, 44604, 44605, 44606, 44607, 44608, 44609, 44610, 44611, 44612, 44613, 44614, 44615, 44616, 44617, 44618, 44619, 44620, 44621, 44622, 44623, 44624, 44625, 44626, 44627, 44628, 44629}, 15223, 47, 278, new Item[]{new Item(Items.BAGGED_PLANT_3_8435, 3), new Item(Items.BAGGED_WILLOW_TREE_8423, 1), new Item(Items.BUCKET_OF_WATER_1929, 5)}),
    //DESERT_HABITAT(new int[]{44630, 44631, 44632, 44633, 44634, 44635, 44636, 44637, 44638, 44639, 44640, 44641, 44642, 44643, 44644, 44645, 44646, 44647, 44648, 44649, 44650, 44651, 44652, 44653, 44654, 44655, 44656, 44657, 44658, 44659, 44660, 44661, 44662, 44663, 44664, 44665, 44666, 44667, 44668, 44669, 44670, 44671, 44672, 44673, 44674, 44675, 44676, 44677, 44678, 44679, 44680, 44681, 44682, 44683, 44684, 44685, 44686, 44687, 44688, 44689, 44690, 44691, 44692, 44693, 44694, 44695}, 15224, 57, 238, new Item[]{new Item(Items.BUCKET_OF_SAND_1783, 10), new Item(Items.LIMESTONE_BRICK_3420, 5), new Item(15237, 1)}),
    //POLAR_HABITAT(new int[]{44696, 44697, 44698, 44699, 44700, 44701, 44702, 44703, 44704, 44705, 44706, 44707, 44708, 44709, 44710, 44711, 44712, 44713, 44714, 44715, 44716, 44717, 44718, 44719, 44720, 44721, 44722, 44723, 44724, 44725, 44726, 44727, 44728, 44729, 44730, 44731, 44732, 44733, 44734, 44735, 44736, 44737, 44738, 44739, 44740, 44741, 44742, 44743, 44744, 44745, 44746, 44747, 44748, 44749, 44750, 44751, 44752, 44753, 44754, 44755, 44756, 44757, 44758, 44759, 44760, 44761}, 15225, 67, 373, new Item[]{new Item(Items.AIR_RUNE_556, 1000), new Item(Items.WATER_RUNE_555, 1000), new Item(15239, 1)}),
    //VOLCANIC_HABITAT(new int[]{44762, 44763, 44764, 44765, 44766, 44767, 44768, 44769, 44770, 44771, 44772, 44773, 44774, 44775, 44776, 44777, 44778, 44779, 44780, 44781, 44782, 44783, 44784, 44785, 44786, 44787, 44788, 44789, 44790, 44791, 44792, 44793, 44794, 44795, 44796, 44797, 44798, 44799, 44800, 44801, 44802, 44803, 44804, 44805, 44806, 44807, 44808, 44809, 44810, 44811, 44812, 44813, 44814, 44815, 44816, 44817, 44818, 44819, 44820, 44821, 44822, 44823, 44824, 44825, 44826, 44827}, 15226, 77, 77, new Item[]{new Item(Items.FIRE_RUNE_554, 1000), new Item(Items.EARTH_RUNE_557, 1000), new Item(Items.BAGGED_DEAD_TREE_8417, 1), new Item(Items.STONE_SLAB_13245, 5)}),
    GLOBE(13649, 8341, 41, Items.GLOBE_8630, 180, new Item[]{new Item(Items.OAK_PLANK_8778, 3)}),
    ORNAMENTAL_GLOBE(13650, 8342, 50, Items.ORNAMENTAL_GLOBE_8632, 270, new Item[]{new Item(Items.TEAK_PLANK_8780, 3)}),
    LUNAR_GLOBE(13651, 8343, 59, Items.LUNAR_GLOBE_8634, 570, new Item[]{new Item(Items.TEAK_PLANK_8780, 3), new Item(Items.GOLD_LEAF_8784, 1)}),
    CELESTIAL_GLOBE(13652, 8344, 68, Items.CELESTIAL_GLOBE_8636, 570, new Item[]{new Item(Items.TEAK_PLANK_8780, 3), new Item(Items.GOLD_LEAF_8784, 1)}),
    ARMILLARY_SPHERE(13653, 8345, 77, Items.ARMILLARY_SPHERE_8638, 960, new Item[]{new Item(Items.MAHOGANY_PLANK_8782, 2), new Item(Items.GOLD_LEAF_8784, 2), new Item(Items.STEEL_BAR_2353, 4)}),
    SMALL_ORREY(13654, 8346, 86, Items.SMALL_ORRERY_8640, 1320, new Item[]{new Item(Items.MAHOGANY_PLANK_8782, 3), new Item(Items.GOLD_LEAF_8784, 3)}),
    LARGE_ORREY(13655, 8347, 95, Items.LARGE_ORRERY_8642, 1420, new Item[]{new Item(Items.MAHOGANY_PLANK_8782, 3), new Item(Items.GOLD_LEAF_8784, 5)}),
    OAK_LECTERN(13642, 8334, 40, Items.OAK_LECTERN_8534, 60, new Item[]{new Item(Items.OAK_PLANK_8778, 1)}),
    EAGLE_LECTERN(13643, 8335, 47, Items.EAGLE_LECTERN_8536, 120, new Item[]{new Item(Items.OAK_PLANK_8778, 2)}),
    DEMON_LECTERN(13644, 8336, 47, Items.DEMON_LECTERN_8538, 120, new Item[]{new Item(Items.OAK_PLANK_8778, 2)}),
    TEAK_EAGLE_LECTERN(13645, 8337, 57, Items.TEAK_EAGLE_LECTERN_8540, 180, new Item[]{new Item(Items.TEAK_PLANK_8780, 2)}),
    TEAK_DEMON_LECTERN(13646, 8338, 57, Items.TEAK_DEMON_LECTERN_8542, 180, new Item[]{new Item(Items.TEAK_PLANK_8780, 2)}),
    MAHOGANY_EAGLE_LECTERN(13647, 8339, 67, Items.MAHOGANY_EAGLE_8544, 580, new Item[]{new Item(Items.MAHOGANY_PLANK_8782, 2), new Item(Items.GOLD_LEAF_8784, 1)}),
    MAHOGANY_DEMON_LECTERN(13648, 8340, 67, Items.MAHOGANY_DEMON_8546, 580, new Item[]{new Item(Items.MAHOGANY_PLANK_8782, 2), new Item(Items.GOLD_LEAF_8784, 1)}),
    CRYSTAL_BALL(13659, 8351, 42, Items.CRYSTAL_BALL_8624, 280, new Item[]{new Item(Items.TEAK_PLANK_8780, 3), new Item(Items.UNPOWERED_ORB_567, 1)}),
    ELEMENTAL_SPHERE(13660, 8352, 54, Items.ELEMENTAL_SPHERE_8626, 580, new Item[]{new Item(Items.TEAK_PLANK_8780, 3), new Item(Items.UNPOWERED_ORB_567, 1), new Item(Items.GOLD_LEAF_8784, 1)}),
    CRYSTAL_OF_POWER(13661, 8353, 66, Items.CRYSTAL_OF_POWER_8628, 890, new Item[]{new Item(Items.MAHOGANY_PLANK_8782, 2), new Item(Items.UNPOWERED_ORB_567, 1), new Item(Items.GOLD_LEAF_8784, 2)}),
    ALCHEMICAL_CHART(13662, 8354, 43, 30, new Item[]{new Item(Items.BOLT_OF_CLOTH_8790, 2)}),
    ASTRONOMICAL_CHART(13663, 8355, 63, 45, new Item[]{new Item(Items.BOLT_OF_CLOTH_8790, 3)}),
    INFERNAL_CHART(13664, 8356, 83, 60, new Item[]{new Item(Items.BOLT_OF_CLOTH_8790, 4)}),
    TELESCOPE1(13656, 8348, 44, Items.WOODEN_TELESCOPE_8644, 121, new Item[]{new Item(Items.OAK_PLANK_8778, 2), new Item(Items.MOLTEN_GLASS_1775, 1)}),
    TELESCOPE2(13657, 8349, 64, Items.TEAK_TELESCOPE_8646, 181, new Item[]{new Item(Items.TEAK_PLANK_8780, 2), new Item(Items.MOLTEN_GLASS_1775, 1)}),
    TELESCOPE3(13658, 8350, 84, Items.MAHOGANY_SCOPE_8648, 580, new Item[]{new Item(Items.MAHOGANY_PLANK_8782, 2), new Item(Items.MOLTEN_GLASS_1775, 1)}),

    OAK_TREASURE_CHEST(18804, 9839, 48, Items.OAK_TREASURE_CHEST_9862, 120, new Item[]{new Item(Items.OAK_PLANK_8778, 2)}),
    TEAK_TREASURE_CHEST(18806, 9840, 66, Items.TEAK_TREAS_CHEST_9863, 180, new Item[]{new Item(Items.TEAK_PLANK_8780, 2)}),
    MAHOGANY_TREASURE_CHEST(18808, 9841, 84, Items.MGANY_TREAS_CHEST_9864, 280, new Item[]{new Item(Items.MAHOGANY_PLANK_8782, 2)}),
    OAK_ARMOUR_CASE(18778, 9826, 46, Items.OAK_ARMOUR_CASE_9859, 180, new Item[]{new Item(Items.OAK_PLANK_8778, 3)}),
    TEAK_ARMOUR_CASE(18780, 9827, 64, Items.TEAK_ARMOUR_CASE_9860, 270, new Item[]{new Item(Items.TEAK_PLANK_8780, 3)}),
    MGANY_ARMOUR_CASE(18782, 9828, 82, Items.MGANY_ARMR_CASE_9861, 420, new Item[]{new Item(Items.MAHOGANY_PLANK_8782, 3)}),
    OAK_MAGIC_WARDROBE(18784, 9829, 42, Items.OAK_MAGIC_WARDROBE_9852, 240, new Item[]{new Item(Items.OAK_PLANK_8778, 4)}),
    C_OAK_MAGIC_WARDROBE(18786, 9830, 51, Items.CARVED_OAK_MAGIC_WARDROBE_9853, 360, new Item[]{new Item(Items.OAK_PLANK_8778, 6)}),
    TEAK_MAGIC_WARDROBE(18788, 9831, 60, Items.TEAK_MAGIC_WARDROBE_9854, 360, new Item[]{new Item(Items.TEAK_PLANK_8780, 4)}),
    C_TEAK_MAGIC_WARDROBE(18790, 9832, 69, Items.CARVED_TEAK_MAGIC_WARDROBE_9855, 540, new Item[]{new Item(Items.TEAK_PLANK_8780, 6)}),
    MGANY_MAGIC_WARDROBE(18792, 9833, 78, Items.MAHOGANY_MAGIC_WARDROBE_9856, 560, new Item[]{new Item(Items.MAHOGANY_PLANK_8782, 4)}),
    GILDED_MAGIC_WARDROBE(18794, 9834, 87, Items.GILDED_MAGIC_WARDROBE_9857, 860, new Item[]{new Item(Items.MAHOGANY_PLANK_8782, 4), new Item(Items.GOLD_LEAF_8784)}),
    MARBLE_MAGIC_WARDROBE(18796, 9835, 96, Items.MARBLE_MAGIC_WARDROBE_9858, 500, new Item[]{new Item(Items.MARBLE_BLOCK_8786)}),
    OAK_CAPE_RACK(18766, 9817, 54, Items.OAK_CAPE_RACK_9843, 240, new Item[]{new Item(Items.OAK_PLANK_8778, 4)}),
    TEAK_CAPE_RACK(18767, 9818, 63, Items.TEAK_CAPE_RACK_9844, 360, new Item[]{new Item(Items.TEAK_PLANK_8780, 4)}),
    MGANY_CAPE_RACK(18768, 9819, 72, Items.MGANY_CAPE_RACK_9845, 560, new Item[]{new Item(Items.MAHOGANY_PLANK_8782, 4)}),
    GILDED_CAPE_RACK(18769, 9820, 81, Items.GILDED_CAPE_RACK_9846, 860, new Item[]{new Item(Items.MAHOGANY_PLANK_8782, 4), new Item(Items.GOLD_LEAF_8784)}),
    MARBLE_CAPE_RACK(18770, 9821, 90, Items.MARBLE_CAPE_RACK_9847, 500, new Item[]{new Item(Items.MARBLE_BLOCK_8786)}),
    MAGIC_CAPE_RACK(18771, 9822, 99, Items.MAGICAL_CAPE_RACK_9848, 1000, new Item[]{new Item(Items.MAGIC_STONE_8788)}),
    OAK_TOY_BOX(18798, 9836, 50, Items.OAK_TOY_BOX_9849, 120, new Item[]{new Item(Items.OAK_PLANK_8778, 2)}),
    TEAK_TOY_BOX(18800, 9837, 68, Items.TEAK_TOY_BOX_9850, 180, new Item[]{new Item(Items.TEAK_PLANK_8780, 2)}),
    MAHOGANY_TOY_BOX(18802, 9838, 86, Items.MAHOGANY_TOY_BOX_9851, 280, new Item[]{new Item(Items.MAHOGANY_PLANK_8782, 2)}),
    OAK_COSTUME_BOX(18772, 9823, 44, Items.OAK_COSTUME_BOX_9865, 120, new Item[]{new Item(Items.OAK_PLANK_8778, 2)}),
    TEAK_COSTUME_BOX(18774, 9824, 62, Items.TEAK_COSTUME_BOX_9866, 180, new Item[]{new Item(Items.TEAK_PLANK_8780, 2)}),
    MAHOGANY_COSTUME_BOX(18776, 9825, 80, Items.MAHOGANY_COS_BOX_9867, 280, new Item[]{new Item(Items.MAHOGANY_PLANK_8782, 2)}),

    OAK_ALTAR(13179, 8062, 45, 240, new Item[]{new Item(Items.OAK_PLANK_8778, 4)}),
    TEAK_ALTAR(13182, 8063, 50, 360, new Item[]{new Item(Items.TEAK_PLANK_8780, 4)}),
    CLOTH_ALTAR(13185, 8064, 56, 390, new Item[]{new Item(Items.TEAK_PLANK_8780, 4), new Item(Items.BOLT_OF_CLOTH_8790, 2)}),
    MAHOGANY_ALTAR(13188, 8065, 60, 590, new Item[]{new Item(Items.MAHOGANY_PLANK_8782, 4), new Item(Items.BOLT_OF_CLOTH_8790, 2)}),
    LIMESTONE_ALTAR(13191, 8066, 64, 910, new Item[]{new Item(Items.MAHOGANY_PLANK_8782, 6), new Item(Items.BOLT_OF_CLOTH_8790, 2), new Item(Items.LIMESTONE_BRICK_3420, 2)}),
    MARBLE_ALTAR(13194, 8067, 70, 1030, new Item[]{new Item(Items.MARBLE_BLOCK_8786, 2), new Item(Items.BOLT_OF_CLOTH_8790, 2)}),
    GILDED_ALTAR(13197, 8068, 75, 2230, new Item[]{new Item(Items.MARBLE_BLOCK_8786, 2), new Item(Items.BOLT_OF_CLOTH_8790, 2), new Item(Items.GOLD_LEAF_8784, 4)}),
    SMALL_STATUE(13271, 8082, 49, 40, new Item[]{new Item(Items.LIMESTONE_BRICK_3420, 2)}),
    MEDIUM_STATUE(13272, 8083, 69, 500, new Item[]{new Item(Items.MARBLE_BLOCK_8786)}),
    LARGE_STATUE(13282, 8084, 89, 1500, new Item[]{new Item(Items.MARBLE_BLOCK_8786, 3)}),
    WINDCHIMES(13214, 8079, 49, 323, new Item[]{new Item(Items.OAK_PLANK_8778, 4), new Item(Items.STEEL_BAR_2353, 4)}),
    BELLS(13215, 8080, 58, 480, new Item[]{new Item(Items.TEAK_PLANK_8780, 4), new Item(Items.STEEL_BAR_2353, 6)}),
    ORGAN(13216, 8081, 69, 680, new Item[]{new Item(Items.MAHOGANY_PLANK_8782, 4), new Item(Items.STEEL_BAR_2353, 6)}),
    SARADOMIN_SYMBOL(13172, 8055, 48, 120, new Item[]{new Item(Items.OAK_PLANK_8778, 2)}),
    ZAMORAK_SYMBOL(13173, 8056, 48, 120, new Item[]{new Item(Items.OAK_PLANK_8778, 2)}),
    GUTHIX_SYMBOL(13174, 8057, 48, 120, new Item[]{new Item(Items.OAK_PLANK_8778, 2)}),
    SARADOMIN_ICON(13175, 8058, 59, 960, new Item[]{new Item(Items.TEAK_PLANK_8780, 4), new Item(Items.GOLD_LEAF_8784, 2)}),
    ZAMORAK_ICON(13176, 8059, 59, 960, new Item[]{new Item(Items.TEAK_PLANK_8780, 4), new Item(Items.GOLD_LEAF_8784, 2)}),
    GUTHIX_ICON(13177, 8060, 59, 960, new Item[]{new Item(Items.TEAK_PLANK_8780, 4), new Item(Items.GOLD_LEAF_8784, 2)}),
    ICON_OF_BOB(13178, 8061, 71, 1160, new Item[]{new Item(Items.MAHOGANY_PLANK_8782, 4), new Item(Items.GOLD_LEAF_8784, 2)}),
    STEEL_TORCHES(13202, 8070, 45, 80, new Item[]{new Item(Items.STEEL_BAR_2353, 2)}),
    WOODEN_TORCHES(13200, 8069, 49, 58, new Item[]{new Item(Items.PLANK_960, 2)}),
    STEEL_CANDLESTICKS(13204, 8071, 53, 124, new Item[]{new Item(Items.STEEL_BAR_2353, 6), new Item(Items.CANDLE_36, 6)}),
    GOLD_CANDLESTICKS(13206, 8072, 57, 46, new Item[]{new Item(Items.GOLD_BAR_2357, 6), new Item(Items.CANDLE_36, 6)}),
    INCENSE_BURNERS(13208, 8073, 61, 280, new Item[]{new Item(Items.OAK_PLANK_8778, 4), new Item(Items.STEEL_BAR_2353, 2)}),
    MAHOGANY_BURNERS(13210, 8074, 65, 600, new Item[]{new Item(Items.MAHOGANY_PLANK_8782, 4), new Item(Items.STEEL_BAR_2353, 2)}),
    MARBLE_BURNERS(13212, 8075, 69, 1600, new Item[]{new Item(Items.MARBLE_BLOCK_8786, 2), new Item(Items.STEEL_BAR_2353, 2)}),
    SHUTTERED_WINDOW(new int[]{13253, 13226, 13235, 13244, 13217, 13262}, 8076, 49, 228, new Item[]{new Item(Items.PLANK_960, 8)}),
    DECORATIVE_WINDOW(new int[]{13254, 13227, 13236, 13245, 13218, 13263}, 8077, 69, 200, new Item[]{new Item(Items.MOLTEN_GLASS_1775, 8)}),
    STAINED_GLASS(new int[]{13255, 13228, 13237, 13246, 13219, 13264}, 8078, 89, 400, new Item[]{new Item(Items.MOLTEN_GLASS_1775, 16)}),

    OAK_THRONE(13665, 8357, 60, 800, new Item[]{new Item(Items.OAK_PLANK_8778, 5), new Item(Items.MARBLE_BLOCK_8786)}),
    TEAK_THRONE(13666, 8358, 67, 1450, new Item[]{new Item(Items.TEAK_PLANK_8780, 5), new Item(Items.MARBLE_BLOCK_8786, 2)}),
    MAHOGANY_THRONE(13667, 8359, 74, 2200, new Item[]{new Item(Items.MAHOGANY_PLANK_8782, 5), new Item(Items.MARBLE_BLOCK_8786, 3)}),
    GILDED_THRONE(13668, 8360, 81, 1700, new Item[]{new Item(Items.MAHOGANY_PLANK_8782, 5), new Item(Items.MARBLE_BLOCK_8786, 2), new Item(Items.GOLD_LEAF_8784, 3)}),
    SKELETON_THRONE(13669, 8361, 88, 7003, new Item[]{new Item(Items.MAGIC_STONE_8788, 5), new Item(Items.MARBLE_BLOCK_8786, 4), new Item(Items.BONES_526, 5), new Item(Items.SKULL_964, 2)}),
    CRYSTAL_THRONE(13670, 8362, 95, 15000, new Item[]{new Item(Items.MAGIC_STONE_8788, 15)}),
    DEMONIC_THRONE(13671, 8363, 99, 25000, new Item[]{new Item(Items.MAGIC_STONE_8788, 25)}),
    OAK_LEVER(13672, 8364, 68, 300, new Item[]{new Item(Items.OAK_PLANK_8778, 5)}),
    TEAK_LEVER(13673, 8365, 78, 450, new Item[]{new Item(Items.TEAK_PLANK_8780, 5)}),
    MAHOGANY_LEVER(13674, 8366, 88, 700, new Item[]{new Item(Items.MAHOGANY_PLANK_8782, 5)}),
    FLOOR_DECORATION(new int[]{13689, 13686, 13687, 13688, 13684, 13685}, 8370, 61, 700, new Item[]{new Item(Items.MAHOGANY_PLANK_8782, 5)}),
    STEEL_CAGE(new int[]{13689, 13686, 13687, 13688, 13684, 13685}, 8371, 68, 1100, new Item[]{new Item(Items.MAHOGANY_PLANK_8782, 5), new Item(Items.STEEL_BAR_2353, 20)}),
    FLOOR_TRAP(new int[]{13689, 13686, 13687, 13688, 13684, 13685}, 8372, 74, 770, new Item[]{new Item(Items.MAHOGANY_PLANK_8782, 5), new Item(Items.CLOCKWORK_8792, 10)}),
    MAGIC_CIRCLE(new int[]{13689, 13686, 13687, 13688, 13684, 13685}, 8373, 82, 2700, new Item[]{new Item(Items.MAHOGANY_PLANK_8782, 5), new Item(Items.MAGIC_STONE_8788, 2)}),
    MAGIC_CAGE(new int[]{13689, 13686, 13687, 13688, 13684, 13685}, 8374, 89, 4700, new Item[]{new Item(Items.MAHOGANY_PLANK_8782, 5), new Item(Items.MAGIC_STONE_8788, 4)}),
    OAK_TRAPDOOR(13675, 8367, 68, 300, new Item[]{new Item(Items.OAK_PLANK_8778, 5)}),
    TEAK_TRAPDOOR(13676, 8368, 78, 450, new Item[]{new Item(Items.TEAK_PLANK_8780, 5)}),
    MAHOGANY_TRAPDOOR(13677, 8369, 88, 700, new Item[]{new Item(Items.MAHOGANY_PLANK_8782, 5)}),
    CARVED_TEAK_BENCH(13694, 8112, 44, 360, new Item[]{new Item(Items.TEAK_PLANK_8780, 4)}),
    MAHOGANY_BENCH(13695, 8113, 52, 560, new Item[]{new Item(Items.MAHOGANY_PLANK_8782, 4)}),
    GILDED_BENCH(13696, 8114, 61, 1760, new Item[]{new Item(Items.MAHOGANY_PLANK_8782, 4), new Item(Items.GOLD_LEAF_8784, 4)}),
    OAK_DECO(13798, 8102, 16, 120, new Item[]{new Item(Items.OAK_PLANK_8778, 2)}),
    TEAK_DECO(13814, 8103, 36, 180, new Item[]{new Item(Items.TEAK_PLANK_8780, 2)}),
    GILDED_DECO(13782, 8104, 56, 1020, new Item[]{new Item(Items.MAHOGANY_PLANK_8782, 3), new Item(Items.GOLD_LEAF_8784, 2)}),
    ROUND_SHIELD(13734, 8105, 66, 120, new Item[]{new Item(Items.OAK_PLANK_8778, 2)}),
    SQUARE_SHIELD(13766, 8106, 76, 360, new Item[]{new Item(Items.TEAK_PLANK_8780, 4)}),
    KITE_SHIELD(13750, 8107, 86, 420, new Item[]{new Item(Items.MAHOGANY_PLANK_8782, 3)}),

    SPIKES_MID(13334, 8302, 65, 623, new Item[]{new Item(Items.STEEL_BAR_2353, 20), new Item(Items.COINS_995, 50000)}),
    SPIKES_SIDE(13335, 8302, 65, 623, new Item[]{new Item(Items.STEEL_BAR_2353, 20), new Item(Items.COINS_995, 50000)}),
    SPIKES_CORNER(13336, 8302, 65, 623, new Item[]{new Item(Items.STEEL_BAR_2353, 20), new Item(Items.COINS_995, 50000)}),
    SPIKES_FL(13338, 8302, 65, 623, new Item[]{new Item(Items.STEEL_BAR_2353, 20), new Item(Items.COINS_995, 50000)}),
    TENTACLE_MID(13331, 8303, 71, 326, new Item[]{new Item(Items.BUCKET_OF_WATER_1929, 20), new Item(Items.COINS_995, 100000)}),
    TENTACLE_SIDE(13332, 8303, 71, 326, new Item[]{new Item(Items.BUCKET_OF_WATER_1929, 20), new Item(Items.COINS_995, 100000)}),
    TENTACLE_CORNER(13333, 8303, 71, 326, new Item[]{new Item(Items.BUCKET_OF_WATER_1929, 20), new Item(Items.COINS_995, 100000)}),
    TENTACLE_FL(13338, 8303, 71, 326, new Item[]{new Item(Items.BUCKET_OF_WATER_1929, 20), new Item(Items.COINS_995, 100000)}),
    FP_FLOOR_MID(13371, 8304, 77, 357, new Item[]{new Item(Items.TINDERBOX_590, 20), new Item(Items.COINS_995, 125000)}),
    FP_FLOOR_SIDE(13371, 8304, 77, 357, new Item[]{new Item(Items.TINDERBOX_590, 20), new Item(Items.COINS_995, 125000)}),
    FP_FLOOR_CORNER(13371, 8304, 77, 357, new Item[]{new Item(Items.TINDERBOX_590, 20), new Item(Items.COINS_995, 125000)}),
    FLAME_PIT(13337, 8304, 77, 357, new Item[]{new Item(Items.TINDERBOX_590, 20), new Item(Items.COINS_995, 125000)}),
    ROCNAR_FLOOR_MID(13371, 8305, 83, 387, new Item[]{new Item(Items.COINS_995, 150000)}),
    ROCNAR_FLOOR_SIDE(13371, 8305, 83, 387, new Item[]{new Item(Items.COINS_995, 150000)}),
    ROCNAR_FLOOR_CORNER(13371, 8305, 83, 387, new Item[]{new Item(Items.COINS_995, 150000)}),
    ROCNAR(13373, 8305, 83, 387, new Item[]{new Item(Items.COINS_995, 150000)}),
    ROCNAR_FL(13338, 8305, 83, 387, new Item[]{new Item(Items.COINS_995, 150000)}),
    OAK_CAGE(13313, 8297, 65, 640, new Item[]{new Item(Items.OAK_PLANK_8778, 10), new Item(Items.STEEL_BAR_2353, 2)}),
    OAK_CAGE_DOOR(13314, 8297, 65, 640, new Item[]{new Item(Items.OAK_PLANK_8778, 10), new Item(Items.STEEL_BAR_2353, 2)}),
    OAK_STEEL_CAGE(13316, 8298, 70, 800, new Item[]{new Item(Items.OAK_PLANK_8778, 10), new Item(Items.STEEL_BAR_2353, 10)}),
    OAK_STEEL_CAGE_DOOR(13317, 8298, 70, 800, new Item[]{new Item(Items.OAK_PLANK_8778, 10), new Item(Items.STEEL_BAR_2353, 10)}),
    STEEL_CAGE_OU(13319, 8299, 75, 400, new Item[]{new Item(Items.STEEL_BAR_2353, 20)}),
    STEEL_CAGE_DOOR(13320, 8299, 75, 400, new Item[]{new Item(Items.STEEL_BAR_2353, 20)}),
    SPIKED_CAGE(13322, 8300, 80, 500, new Item[]{new Item(Items.STEEL_BAR_2353, 25)}),
    SPIKED_CAGE_DOOR(13323, 8300, 80, 500, new Item[]{new Item(Items.STEEL_BAR_2353, 25)}),
    BONE_CAGE(13325, 8301, 85, 603, new Item[]{new Item(Items.OAK_PLANK_8778, 10), new Item(Items.BONES_526, 10)}),
    BONE_CAGE_DOOR(13326, 8301, 85, 603, new Item[]{new Item(Items.OAK_PLANK_8778, 10), new Item(Items.BONES_526, 10)}),
    SKELETON_GUARD(13366, 8131, 70, 223, new Item[]{new Item(Items.COINS_995, 50000)}),
    GUARD_DOG(13367, 8132, 74, 273, new Item[]{new Item(Items.COINS_995, 75000)}),
    HOBGOBLIN(13368, 8133, 78, 316, new Item[]{new Item(Items.COINS_995, 100000)}),
    BABY_RED_DRAGON(13372, 8134, 82, 387, new Item[]{new Item(Items.COINS_995, 150000)}),
    HUGE_SPIDER(13370, 8135, 86, 447, new Item[]{new Item(Items.COINS_995, 200000)}),
    TROLL(13369, 8136, 90, 1000, new Item[]{new Item(Items.COINS_995, 1000000)}),
    HELLHOUND(2715, 8137, 94, 2236, new Item[]{new Item(Items.COINS_995, 5000000)}),
    OAK_LADDER(13328, 8306, 68, 300, new Item[]{new Item(Items.OAK_PLANK_8778, 5)}),
    TEAK_LADDER(13329, 8307, 78, 450, new Item[]{new Item(Items.TEAK_PLANK_8780, 5)}),
    MAHOGANY_LADDER(13330, 8308, 88, 700, new Item[]{new Item(Items.MAHOGANY_PLANK_8782, 5)}),
    DECORATIVE_BLOOD(13312, 8125, 72, 4, new Item[]{new Item(Items.RED_DYE_1763, 4)}),
    DECORATIVE_PIPE(13311, 8126, 83, 120, new Item[]{new Item(Items.STEEL_BAR_2353, 6)}),
    HANGING_SKELETON(13310, 8127, 94, 3, new Item[]{new Item(Items.SKULL_964, 2), new Item(Items.BONES_526, 6)}),
    CANDLE(13342, 8128, 72, 243, new Item[]{new Item(Items.OAK_PLANK_8778, 4), new Item(Items.LIT_CANDLE_33, 4)}),
    TORCH(13341, 8129, 84, 244, new Item[]{new Item(Items.OAK_PLANK_8778, 4), new Item(Items.LIT_TORCH_594, 4)}),
    SKULL_TORCH(13343, 8130, 94, 246, new Item[]{new Item(Items.OAK_PLANK_8778, 4), new Item(Items.LIT_TORCH_594, 4), new Item(Items.SKULL_964, 4)}),

    OAK_DOOR_LEFT(13344, 8122, 74, 600, new Item[]{new Item(Items.OAK_PLANK_8778, 10)}),
    OAK_DOOR_RIGHT(13345, 8122, 74, 600, new Item[]{new Item(Items.OAK_PLANK_8778, 10)}),
    STEEL_DOOR_LEFT(13346, 8123, 84, 800, new Item[]{new Item(Items.OAK_PLANK_8778, 10), new Item(Items.STEEL_BAR_2353, 10)}),
    STEEL_DOOR_RIGHT(13347, 8123, 84, 800, new Item[]{new Item(Items.OAK_PLANK_8778, 10), new Item(Items.STEEL_BAR_2353, 10)}),
    MARBLE_DOOR_LEFT(13348, 8124, 94, 2000, new Item[]{new Item(Items.MARBLE_BLOCK_8786, 4)}),
    MARBLE_DOOR_RIGHT(13349, 8124, 94, 2000, new Item[]{new Item(Items.MARBLE_BLOCK_8786, 4)}),
    SPIKE_TRAP(13356, 8143, 72, 223, new Item[]{new Item(Items.COINS_995, 50000)}),
    MAN_TRAP(13357, 8144, 76, 273, new Item[]{new Item(Items.COINS_995, 75000)}),
    TANGLE_TRAP(13358, 8145, 80, 316, new Item[]{new Item(Items.COINS_995, 100000)}),
    MARBLE_TRAP(13359, 8146, 84, 387, new Item[]{new Item(Items.COINS_995, 150000)}),
    TELEPORT_TRAP(13360, 8147, 88, 447, new Item[]{new Item(Items.COINS_995, 200000)}),
    PIT_DOG(39260, 18791, 70, 200, new Item[]{new Item(Items.COINS_995, 40000)}),
    PIT_OGRE(39261, 18792, 73, 234, new Item[]{new Item(Items.COINS_995, 55000)}),
    PIT_ROCK_PROTECTOR(39262, 18793, 79, 300, new Item[]{new Item(Items.COINS_995, 90000)}),
    PIT_SCABARITE(39263, 18794, 84, 387, new Item[]{new Item(Items.COINS_995, 150000)}),
    PIT_BLACK_DEMON(39264, 18795, 89, 547, new Item[]{new Item(Items.COINS_995, 300000)}),
    PIT_IRON_DRAGON(39265, 18796, 97, 2738, new Item[]{new Item(Items.COINS_995, 7500000)}),

    DEMON(13378, 8138, 75, 707, new Item[]{new Item(Items.COINS_995, 500000)}),
    KALPHITE_SOLDIER(13374, 8139, 80, 866, new Item[]{new Item(Items.COINS_995, 750000)}),
    TOK_XIL(13377, 8140, 85, 2236, new Item[]{new Item(Items.COINS_995, 5000000)}),
    DAGANNOTH(13376, 8141, 90, 2738, new Item[]{new Item(Items.COINS_995, 7500000)}),
    STEEL_DRAGON(13375, 8142, 95, 3162, new Item[]{new Item(Items.COINS_995, 1000000)}),
    WOODEN_CRATE(13283, 8148, 75, 143, new Item[]{new Item(Items.PLANK_960, 5)}),
    OAK_T_CHEST(13285, 8149, 79, 340, new Item[]{new Item(Items.OAK_PLANK_8778, 5), new Item(Items.STEEL_BAR_2353, 2)}),
    TEAK_T_CHEST(13287, 8150, 83, 530, new Item[]{new Item(Items.TEAK_PLANK_8780, 5), new Item(Items.STEEL_BAR_2353, 4)}),
    MGANY_T_CHEST(13289, 8151, 87, 1000, new Item[]{new Item(Items.MAHOGANY_PLANK_8782, 5), new Item(Items.GOLD_LEAF_8784)}),
    MAGIC_CHEST(13291, 8152, 91, 1000, new Item[]{new Item(Items.MAGIC_STONE_8788)}),

    BASIC_WOOD_WINDOW(13099, -1, 1, 0),
    BASIC_STONE_WINDOW(13091, -1, 1, 0),
    WHITEWASHED_STONE_WINDOW(13005, -1, 1, 0),
    FREMENNIK_WINDOW(13112, -1, 1, 0),
    TROPICAL_WOOD_WINDOW(10816, -1, 1, 0),
    FANCY_STONE_WINDOW(13117, -1, 1, 0),

    FLATPACK(-1, -1, 1, 0),
    ;

    private final int objectId;
    private final int interfaceItem;
    private final int level;
    private final int flatpackItemId;
    private final int experience;
    private final Item[] items;
    private final Item[] refundItems;
    private final int[] tools;
    private final int[] objectIds;
    private boolean invisibleNode;

    private Decoration(Integer objectId, int[] objectIds, int interfaceItem, int level, Integer flatpackItemId, int experience, int[] tools, Item[] items, Item[] refundItems, boolean invisibleNode) {
        this.objectId = (objectId != null) ? objectId : (objectIds != null && objectIds.length > 0 ? objectIds[0] : -1);
        this.objectIds = objectIds;
        this.interfaceItem = interfaceItem;
        this.level = level;
        this.flatpackItemId = (flatpackItemId != null) ? flatpackItemId : this.objectId;
        this.experience = experience;
        this.tools = (tools != null) ? tools : new int[]{Items.HAMMER_2347, Items.SAW_8794};
        this.items = (items != null) ? items : new Item[]{};
        this.refundItems = (refundItems != null) ? refundItems : new Item[]{};
        this.invisibleNode = invisibleNode;
    }

    Decoration(int objectId, int interfaceItem, int level, int experience) {
        this(objectId, null, interfaceItem, level, null, experience, null, null, null, false);
    }

    Decoration(int objectId, int interfaceItem, int level, int experience, Item[] items) {
        this(objectId, null, interfaceItem, level, null, experience, null, items, null, false);
    }

    Decoration(int objectId, int interfaceItem, int level, int experience, int[] tools, Item[] items) {
        this(objectId, null, interfaceItem, level, null, experience, tools, items, null, false);
    }

    Decoration(int objectId, int interfaceItem, int level, int experience, Item[] items, Item[] refundItems) {
        this(objectId, null, interfaceItem, level, null, experience, null, items, refundItems, false);
    }

    Decoration(int objectId, int interfaceItem, int level, int flatpackItemId, int experience, Item... items) {
        this(objectId, null, interfaceItem, level, flatpackItemId, experience, null, items, null, false);
    }

    Decoration(int objectId, boolean invisibleNode) {
        this(objectId, null, -1, -1, null, -1, null, null, null, invisibleNode);
    }

    Decoration(int[] objectIds, int interfaceItem, int level, int experience, Item[] items) {
        this(null, objectIds, interfaceItem, level, null, experience, null, items, null, false);
    }

    public static Decoration getDecoration(Player player, Scenery object) {
        Location l = object.getLocation();
        int z = l.getZ();
        if (HouseManager.isInDungeon(player)) {
            z = 3;
        }
        Room room = player.getHouseManager().getRooms()[z][l.getChunkX()][l.getChunkY()];
        for (Hotspot h : room.getHotspots()) {
            if (h.getCurrentX() == l.getChunkOffsetX() && h.getCurrentY() == l.getChunkOffsetY()) {
                if (h.getDecorationIndex() != -1) {
                    Decoration deco = h.getHotspot().getDecorations()[h.getDecorationIndex()];
                    if (deco.getObjectId(player.getHouseManager().getStyle()) == object.getId()) {
                        return deco;
                    }
                }
            }
        }
        return null;
    }

    public static Decoration forObjectId(int objectId) {
        for (Decoration d : Decoration.values()) {
            if (d.getObjectId() == objectId) {
                return d;
            }
        }
        return null;
    }

    public static Decoration forInterfaceItemId(int interfaceId) {
        for (Decoration d : Decoration.values()) {
            if (d.getInterfaceItem() == interfaceId) {
                return d;
            }
        }
        return null;
    }

    /**
     * For flatpack item id decoration.
     *
     * @param flatpackId the flatpack id
     * @return the decoration
     */
    public static Decoration forFlatpackItemId(int flatpackId) {
        for (Decoration d : Decoration.values()) {
            if (d.getFlatpackItemID() == flatpackId) {
                return d;
            }
        }
        return null;
    }

    /**
     * For name decoration.
     *
     * @param name the name
     * @return the decoration
     */
    public static Decoration forName(String name) {
        for (Decoration d : Decoration.values()) {
            if (d.name().equals(name)) {
                return d;
            }
        }
        return null;
    }

    /**
     * Gets nail amount.
     *
     * @return the nail amount
     */
    public int getNailAmount() {
        for (Item item : items) {
            if (item.getId() == 960) {
                return item.getAmount();
            }
        }
        return 0;
    }

    /**
     * Gets object id.
     *
     * @param style the style
     * @return the object id
     */
    public int getObjectId(HousingStyle style) {
        if (objectIds != null) {
            return objectIds[style.ordinal()];
        }
        return objectId;
    }

    /**
     * Gets object id.
     *
     * @return the object id
     */
    public int getObjectId() {
        return objectId;
    }

    /**
     * Gets level.
     *
     * @return the level
     */
    public int getLevel() {
        return level;
    }

    /**
     * Gets experience.
     *
     * @return the experience
     */
    public int getExperience() {
        return experience;
    }

    /**
     * Get items item.
     *
     * @return the item.
     */
    public Item[] getItems() {
        return items;
    }

    /**
     * Get refund items item.
     *
     * @return the item.
     */
    public Item[] getRefundItems() {
        return refundItems;
    }

    /**
     * Get tools.
     *
     * @return the tool id.
     */
    public int[] getTools() {
        return tools;
    }

    /**
     * Gets interface item.
     *
     * @return the interface item
     */
    public int getInterfaceItem() {
        return interfaceItem;
    }

    /**
     * Get object ids.
     *
     * @return the object id
     */
    public int[] getObjectIds() {
        return objectIds;
    }

    /**
     * Is invisible node boolean.
     *
     * @return the boolean
     */
    public boolean isInvisibleNode() {
        return invisibleNode;
    }

    /**
     * Gets flatpack item id.
     *
     * @return the flatpack item id
     */
    public int getFlatpackItemID() {
        return flatpackItemId;
    }
}
