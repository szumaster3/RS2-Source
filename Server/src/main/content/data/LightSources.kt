package content.data

import core.api.inEquipment
import core.api.inInventory
import core.game.node.entity.player.Player
import shared.consts.Components
import shared.consts.Items
import shared.consts.Sounds

/**
 * Represents different types of light sources.
 *
 * @property level The required skill level to use the light source.
 * @property emptyId The item id for the empty/unlit version.
 * @property fullId The item id for the full/non-lit version (optional).
 * @property litId The item id for the lit version.
 * @property sfxId The sound effect id played when lighting the source (optional).
 * @property open Whether this light source can be extinguished (openable).
 * @property interfaceId The overlay interface id shown when this light source is active.
 */
enum class LightSources(val level: Int, val emptyId: Int, val fullId: Int = -1, val litId: Int, val sfxId: Int = -1, val open: Boolean = false, val interfaceId: Int = -1) {
    CANDLE(1, 0, Items.CANDLE_36, Items.LIT_CANDLE_33, Sounds.SKILL_LIGHT_CANDLE_3226, true, Components.DARKNESS_MEDIUM_98),
    BLACK_CANDLE(1, 0, Items.BLACK_CANDLE_38, Items.LIT_BLACK_CANDLE_32, Sounds.SKILL_LIGHT_CANDLE_3226, true, Components.DARKNESS_MEDIUM_98),
    TORCH(1, 0, Items.UNLIT_TORCH_596, Items.LIT_TORCH_594, Sounds.SLUG_TORCH_LIT_3028, true, Components.DARKNESS_MEDIUM_98),
    CANDLE_LANTERN(4, Items.CANDLE_LANTERN_4527, Items.CANDLE_LANTERN_4529, Items.CANDLE_LANTERN_4531, Sounds.LIGHT_CANDLE_2305, true, Components.DARKNESS_MEDIUM_98),
    CANDLE_LANTERN_BLACK(4, Items.CANDLE_LANTERN_4527, Items.CANDLE_LANTERN_4532, Items.CANDLE_LANTERN_4534, Sounds.LIGHT_CANDLE_2305, true, Components.DARKNESS_MEDIUM_98),
    OIL_LAMP(12, Items.OIL_LAMP_4525, Items.OIL_LAMP_4522, Items.OIL_LAMP_4524, Sounds.LIGHT_CANDLE_2305, true, Components.DARKNESS_LIGHT_97),
    OIL_LANTERN(26, Items.OIL_LANTERN_4535, Items.OIL_LANTERN_4537, Items.OIL_LANTERN_4539, Sounds.LIGHT_CANDLE_2305, false, Components.DARKNESS_LIGHT_97),
    SAPPHIRE_LANTERN(49, Items.SAPPHIRE_LANTERN_4700, Items.SAPPHIRE_LANTERN_4701, Items.SAPPHIRE_LANTERN_4702, Sounds.LIGHT_CANDLE_2305, false, -1),
    MINING_HELMET(65, 0, Items.MINING_HELMET_5014, Items.MINING_HELMET_5013, Sounds.LIGHT_CANDLE_2305, false, Components.DARKNESS_LIGHT_97),
    BULLSEYE_LANTERN(49, Items.BULLSEYE_LANTERN_4546, Items.BULLSEYE_LANTERN_4548, Items.BULLSEYE_LANTERN_4550, Sounds.LIGHT_CANDLE_2305, false, -1),
    EMERALD_LANTERN(49, Items.EMERALD_LANTERN_9064, Items.EMERALD_LANTERN_9064, Items.EMERALD_LANTERN_9065, Sounds.LIGHT_CANDLE_2305, false, -1),
    HEADBAND_1(1, 0, Items.SEERS_HEADBAND_1_14631, Items.SEERS_HEADBAND_1_14631, -1, false, Components.DARKNESS_MEDIUM_98),
    HEADBAND_2(1, 0, Items.SEERS_HEADBAND_2_14645, Items.SEERS_HEADBAND_2_14645, -1, false, Components.DARKNESS_LIGHT_97),
    HEADBAND_3(1, 0, Items.SEERS_HEADBAND_3_14646, Items.SEERS_HEADBAND_3_14646, -1, false, -1),
    GLOWING_FUNGUS(1, 0, Items.GLOWING_FUNGUS_4075, Items.GLOWING_FUNGUS_4075, -1, false, -1);

    companion object {
        private val byRaw: Map<Int, LightSources> = values()
            .flatMap { listOf(it.emptyId, it.fullId).filter { id -> id > 0 }.map { id -> id to it } }
            .toMap()

        private val byLit: Map<Int, LightSources> = values().associateBy { it.litId }

        /**
         * Gets a LightSource by its raw/unlit id.
         * @param id The raw item id.
         * @return The corresponding LightSource, or null if none.
         */
        fun forId(id: Int): LightSources? = byRaw[id]

        /**
         * Gets a LightSource by its lit item id.
         * @param id The lit item id.
         * @return The corresponding LightSource, or null if none.
         */
        fun forLitId(id: Int): LightSources? = byLit[id]

        /**
         * Checks if the light source is active.
         */
        fun isActive(player: Player, src: LightSources): Boolean = when (src) {
            GLOWING_FUNGUS -> inInventory(player, src.litId)
            HEADBAND_1, HEADBAND_2, HEADBAND_3 -> inEquipment(player, src.litId)
            else -> inInventory(player, src.litId) || inEquipment(player, src.litId)
        }

        /**
         * Returns any active light source.
         */
        fun getActiveLightSource(player: Player): LightSources? =
            values().firstOrNull { isActive(player, it) }

        /**
         * Checks if the player has any active light source.
         * @param player The player to check.
         */
        fun hasActiveLightSource(player: Player): Boolean =
            getActiveLightSource(player) != null
    }
}
