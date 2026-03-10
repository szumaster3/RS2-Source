package content.global.plugins.item.equipment.bolt_pouch

import com.google.gson.JsonArray
import com.google.gson.JsonObject
import core.game.container.Container
import core.game.container.ContainerType
import core.game.container.impl.EquipmentContainer
import core.game.node.entity.player.Player
import core.game.node.item.Item
import shared.consts.Items

class BoltPouchManager(val player: Player) {

    companion object {
        const val SIZE = 255
        const val SLOTS = 4

        val ALLOWED_BOLT_IDS = intArrayOf(
            Items.BRONZE_BOLTS_877, Items.OPAL_BOLTS_879, Items.PEARL_BOLTS_880, Items.BARBED_BOLTS_881,
            Items.BRONZE_BOLTSP_878, Items.BRONZE_BOLTSP_PLUS_6061, Items.BRONZE_BOLTSP_PLUS_PLUS_6062,
            Items.BLURITE_BOLTS_9139, Items.IRON_BOLTS_9140, Items.STEEL_BOLTS_9141, Items.MITHRIL_BOLTS_9142,
            Items.ADAMANT_BOLTS_9143, Items.RUNE_BOLTS_9144, Items.SILVER_BOLTS_9145, Items.BLURITE_BOLTSP_9286,
            Items.IRON_BOLTS_P_9287, Items.STEEL_BOLTS_P_9288, Items.MITHRIL_BOLTS_P_9289, Items.ADAMANT_BOLTS_P_9290,
            Items.RUNITE_BOLTS_P_9291, Items.SILVER_BOLTSP_PLUS_9299, Items.BLURITE_BOLTSP_PLUS_9293,
            Items.IRON_BOLTSP_PLUS_9294, Items.STEEL_BOLTSP_PLUS_9295, Items.MITHRIL_BOLTSP_PLUS_9296,
            Items.ADAMANT_BOLTSP_PLUS_9297, Items.RUNITE_BOLTSP_PLUS_9298, Items.BLURITE_BOLTSP_PLUS_PLUS_9300,
            Items.IRON_BOLTSP_PLUS_PLUS_9301, Items.STEEL_BOLTSP_PLUS_PLUS_9302, Items.MITHRIL_BOLTSP_PLUS_PLUS_9303,
            Items.ADAMANT_BOLTSP_PLUS_PLUS_9304, Items.RUNITE_BOLTSP_PLUS_PLUS_9305, Items.JADE_BOLTS_9335,
            Items.TOPAZ_BOLTS_9336, Items.SAPPHIRE_BOLTS_9337, Items.EMERALD_BOLTS_9338, Items.RUBY_BOLTS_9339,
            Items.DIAMOND_BOLTS_9340, Items.DRAGON_BOLTS_9341, Items.ONYX_BOLTS_9342, Items.OPAL_BOLTS_E_9236,
            Items.JADE_BOLTS_E_9237, Items.PEARL_BOLTS_E_9238, Items.TOPAZ_BOLTS_E_9239, Items.SAPPHIRE_BOLTS_E_9240,
            Items.EMERALD_BOLTS_E_9241, Items.RUBY_BOLTS_E_9242, Items.DIAMOND_BOLTS_E_9243, Items.DRAGON_BOLTS_E_9244,
            Items.ONYX_BOLTS_E_9245, Items.KEBBIT_BOLTS_10158, Items.LONG_KEBBIT_BOLTS_10159, Items.BLACK_BOLTS_13083,
            Items.BLACK_BOLTSP_13084, Items.BLACK_BOLTSP_PLUS_13085, Items.BLACK_BOLTSP_PLUS_PLUS_13086,
            Items.BROAD_TIPPED_BOLTS_13280
        )
    }

    private val boltPouchContainers = mutableMapOf<Int, Container>()

    private fun getContainer(pouchId: Int): Container =
        boltPouchContainers.getOrPut(pouchId) { Container(SLOTS, ContainerType.NEVER_STACK) }

    fun getPouchContainer(pouchId: Int): Container? =
        boltPouchContainers[pouchId]

    fun hasBolts(pouchId: Int, slot: Int): Boolean =
        getContainer(pouchId).get(slot)?.amount?.let { it > 0 } ?: false

    fun getBolt(slot: Int): Int =
        getContainer(Items.BOLT_POUCH_9433).get(slot)?.id ?: -1

    fun getAmount(slot: Int): Int =
        getContainer(Items.BOLT_POUCH_9433).get(slot)?.amount ?: 0

    fun addBolts(pouchId: Int, boltId: Int, amount: Int): Int {
        if (boltId !in ALLOWED_BOLT_IDS) return 0

        val container = getContainer(pouchId)

        for (slot in 0 until container.capacity()) {
            val item = container.get(slot)
            if (item != null && item.id == boltId) {
                val space = SIZE - item.amount
                if (space <= 0) {
                    return 0
                }
                val add = minOf(space, amount)
                item.amount += add
                container.update()
                return add
            }
        }

        for (slot in 0 until container.capacity()) {
            if (container.get(slot) == null) {
                val add = minOf(SIZE, amount)
                container.replace(Item(boltId, add), slot)
                container.update()
                return add
            }
        }
        return 0
    }

    fun wieldBolts(pouchId: Int, slot: Int, player: Player): Boolean {
        val container = getContainer(pouchId)
        val item = container.get(slot) ?: return false
        val arrowSlot = EquipmentContainer.SLOT_ARROWS
        val current = player.equipment.get(arrowSlot)
        if (current != null && current.amount > 0) return false
        container.replace(null, slot)
        container.update()
        player.equipment.add(item, true, arrowSlot)
        return true
    }

    fun removeBolts(pouchId: Int, slot: Int, player: Player): Boolean {
        val container = getContainer(pouchId)
        val item = container.get(slot) ?: return false
        if (!player.inventory.hasSpaceFor(item)) return false

        container.remove(item, slot, true)
        container.shift()
        for (i in 0 until container.capacity()) {
            if (container.get(i)?.amount == 0) container.replace(null, i)
        }

        player.inventory.add(item)
        return true
    }

    fun save(root: JsonObject) {
        val pouchArray = JsonArray()
        for ((pouchId, container) in boltPouchContainers) {
            val obj = JsonObject()
            obj.addProperty("pouch", pouchId.toString())
            val bolts = JsonArray()

            for (i in 0 until container.capacity()) {
                val item = container.get(i)
                val it = JsonObject()
                it.addProperty("slot", i.toString())
                if (item != null) {
                    it.addProperty("id", item.id.toString())
                    it.addProperty("amount", item.amount.toString())
                } else {
                    it.addProperty("id", "0")
                    it.addProperty("amount", "0")
                }
                bolts.add(it)
            }

            obj.add("bolts", bolts)
            pouchArray.add(obj)
        }
        root.add("boltPouch", pouchArray)
    }

    fun parse(data: JsonArray) {
        data.forEach { element ->
            val obj = element.asJsonObject
            val pouchId = obj.get("pouch").asInt
            val container = getContainer(pouchId)
            container.clear()

            val boltsArray = obj.getAsJsonArray("bolts")
            boltsArray?.forEach { boltElement ->
                val b = boltElement.asJsonObject
                val slot = b.get("slot").asInt
                val id = b.get("id").asInt
                val amount = b.get("amount").asInt
                if (id != 0) {
                    container.replace(Item(id, amount), slot)
                }
            }
        }
    }
}