package content.region.wilderness.plugin.rogue_castle

import core.api.*
import core.cache.def.impl.ItemDefinition
import core.game.dialogue.DialogueFile
import core.game.dialogue.FaceAnim
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.item.Item
import shared.consts.Items
import shared.consts.NPCs
import shared.consts.Quests
import kotlin.math.min

class RoguesJewelleryPlugin : InteractionListener {

    override fun defineListeners() {

        /*
         * Handles jewellery selling interaction with Rogue NPC.
         */

        onUseWith(IntType.NPC, JEWELLERY, NPCs.ROGUE_8122) { player, used, _ ->
            if (!hasRequirement(player, Quests.SUMMERS_END)) return@onUseWith false

            val amount = amountInInventory(player, used.id)
            if (amount <= 0) return@onUseWith false
            val def = ItemDefinition.forId(used.id)

            openDialogue(
                player,
                RogueJewelleryDialogue(
                    itemId   = used.id,
                    itemName = def.name,
                    price    = def.value
                )
            )

            return@onUseWith true
        }
    }

    companion object {

        private val BASE_JEWELLERY = intArrayOf(
            Items.GOLD_RING_1635, Items.SAPPHIRE_RING_1637, Items.EMERALD_RING_1639,
            Items.RUBY_RING_1641, Items.DIAMOND_RING_1643, Items.DRAGONSTONE_RING_1645,
            Items.GOLD_NECKLACE_1654, Items.SAPPHIRE_NECKLACE_1656, Items.EMERALD_NECKLACE_1658,
            Items.RUBY_NECKLACE_1660, Items.DIAMOND_NECKLACE_1662, Items.DRAGON_NECKLACE_1664,
            Items.GOLD_BRACELET_11069, Items.SAPPHIRE_BRACELET_11072, Items.EMERALD_BRACELET_11076,
            Items.RUBY_BRACELET_11085, Items.DIAMOND_BRACELET_11092, Items.DRAGON_BRACELET_11115,
            Items.GOLD_AMULET_1692, Items.SAPPHIRE_AMULET_1694, Items.EMERALD_AMULET_1696,
            Items.RUBY_AMULET_1698, Items.DIAMOND_AMULET_1700, Items.DRAGONSTONE_AMMY_1702
        )

        /**
         * Allowed items.
         */
        val JEWELLERY: IntArray by lazy {
            val temp = IntArray(BASE_JEWELLERY.size * 2)
            var idx = 0

            for (id in BASE_JEWELLERY) {
                temp[idx++] = id

                val noteId = ItemDefinition.forId(id).noteId
                if (noteId > 0) {
                    temp[idx++] = noteId
                }
            }

            temp.copyOf(idx)
        }
    }
}