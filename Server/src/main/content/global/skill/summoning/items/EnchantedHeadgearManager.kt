package content.global.skill.summoning.items

import com.google.gson.JsonArray
import com.google.gson.JsonObject
import core.api.*
import core.game.container.Container
import core.game.node.entity.player.Player
import core.game.node.item.Item

/**
 * Todo: Gear stores scrolls but it's probably all code to be thrown away.
 */
class EnchantedHeadgearManager(private val player: Player) {

    val enchantedGear: MutableMap<Int, ChargedHeadgear> = mutableMapOf()

    /**
     * Adds scrolls to the enchanted headgear if valid.
     */
    fun addScrolls(chargedItemId: Int, scrollId: Int, amount: Int) {
        if (scrollId !in EnchantedHeadgearPlugin.ALLOWED_SCROLL_ID) {
            sendMessage(player, "You cannot add this scroll to the headgear.")
            return
        }

        val headgearData = EnchantedHeadgear.forItem(chargedItemId.asItem())
            ?.takeIf { it.second == EnchantedHeadgear.HeadgearType.CHARGED }?.first
            ?: return

        val capacity = headgearData.scrollCapacity
        val chargedGear = enchantedGear.getOrPut(chargedItemId) {
            ChargedHeadgear(chargedItemId, Container(capacity))
        }
        val container = chargedGear.container

        val existingScrollIds = container.toArray().filterNotNull().map { it.id }.toSet()
        if (existingScrollIds.any { it != scrollId }) {
            sendMessage(player, "Your headgear already contains different scrolls. Remove them first.")
            return
        }

        val freeSpace = container.freeSlots()
        if (freeSpace <= 0) {
            sendMessage(player, "Your headgear is already fully charged.")
            return
        }

        val inventoryCount = player.inventory.getAmount(scrollId)
        val toAdd = amount.coerceAtMost(freeSpace).coerceAtMost(inventoryCount)

        if (toAdd <= 0) {
            sendMessage(player, "You do not have any scrolls to add.")
            return
        }

        val scrollItem = Item(scrollId, toAdd)
        if (!player.inventory.remove(scrollItem)) {
            player.debug("Failed to remove scrolls from inventory.")
            return
        }

        container.add(scrollItem)
        sendMessage(player, "You charge the headgear with $toAdd scroll${if (toAdd > 1) "s" else ""}.")
    }

    /**
     * Withdraw a specified amount of scrolls from the enchanted headgear.
     */
    fun withdrawScrolls(chargedItemId: Int, scrollId: Int, amount: Int): Boolean {
        val chargedGear = enchantedGear[chargedItemId] ?: return false
        val container = chargedGear.container
        val containedAmount = container.getAmount(scrollId)
        if (containedAmount < amount) return false

        container.remove(Item(scrollId, amount))
        container.shift()
        addItem(player, scrollId, amount)

        if (container.isEmpty()) {
            val headgear = EnchantedHeadgear.forItem(chargedItemId.asItem())
                ?.takeIf { it.second == EnchantedHeadgear.HeadgearType.CHARGED }?.first
                ?: return false

            val slot = player.inventory.getSlot(chargedItemId.asItem())
            if (slot == -1) {
                player.debug("Charged headgear not found in inventory.")
                return false
            }

            player.inventory.replace(Item(headgear.enchantedItem.id, 1), slot)
            enchantedGear.remove(chargedItemId)
        }

        return true
    }

    /**
     * Withdraw all scrolls from the enchanted headgear.
     */
    fun withdrawAllScrolls(chargedItemId: Int) {
        val chargedGear = enchantedGear[chargedItemId] ?: return
        val container = chargedGear.container
        val scrolls = container.toArray().filterNotNull()

        scrolls.forEach { item ->
            container.remove(item)
            addItem(player, item.id, item.amount)
        }
        container.shift()

        sendMessages(
            player,
            "You remove the scrolls. You will need to use a Summoning scroll",
            "on it to charge the headgear up once more."
        )
    }

    /**
     * Checks if enchanted headgear contains any scrolls.
     */
    fun hasScrolls(chargedItemId: Int): Boolean {
        return enchantedGear[chargedItemId]?.container?.toArray()?.any { it != null } == true
    }

    /**
     * Gets the current amount of specific scrolls in the enchanted headgear.
     */
    fun getCurrentScrollCount(chargedItemId: Int, scrollId: Int): Int {
        return enchantedGear[chargedItemId]?.container?.getAmount(scrollId) ?: 0
    }

    /**
     * Gets enchanted headgear item ID from player's equipment (head slot).
     */
    fun getFromEquipment(): Int? {
        val headSlotItem = player.equipment[EquipmentSlot.HEAD.ordinal]
        return headSlotItem?.id?.takeIf {
            EnchantedHeadgear.forItem(it.asItem())?.second == EnchantedHeadgear.HeadgearType.CHARGED
        }
    }

    /**
     * Removes a single scroll from enchanted headgear.
     */
    fun removeScroll(chargedItemId: Int, scrollId: Int): Boolean {
        val chargedGear = enchantedGear.getOrPut(chargedItemId) {
            val headgear = EnchantedHeadgear.forItem(chargedItemId.asItem())
                ?.takeIf { it.second == EnchantedHeadgear.HeadgearType.CHARGED }?.first
                ?: return false
            ChargedHeadgear(chargedItemId, Container(headgear.scrollCapacity))
        }
        val container = chargedGear.container
        val amount = container.getAmount(scrollId)
        if (amount <= 0) return false
        container.remove(Item(scrollId, 1))
        container.shift()
        return true
    }

    /**
     * Sends player a message about current scrolls stored in the enchanted headgear.
     */
    fun checkHeadgear(chargedItemId: Int) {
        val chargedGear = enchantedGear[chargedItemId]
        if (chargedGear == null || chargedGear.container.isEmpty()) {
            sendMessage(player, "Your headgear holds no scrolls.")
            return
        }
        val message = chargedGear.container.toArray()
            .filterNotNull()
            .joinToString("\n") { item ->
                val scrollName = getItemName(item.id)
                val plural = if (item.amount == 1) "charge" else "charges"
                "The item contains ${item.amount} $plural of the $scrollName."
            }
        sendMessage(player, message)
    }

    /**
     * Clears all enchanted headgear data for the player.
     */
    fun clear() {
        enchantedGear.clear()
    }

    /**
     * Saves enchanted headgear scroll data into a JSON root object.
     */
    fun save(root: JsonObject) {
        val arr = JsonArray()
        enchantedGear.forEach { (_, chargedGear) ->
            val obj = JsonObject().apply {
                addProperty("id", chargedGear.chargedItemId.toString())
                val scrollArray = JsonArray()
                chargedGear.container.toArray().forEach { item ->
                    if (item != null) {
                        val itemObj = JsonObject().apply {
                            addProperty("itemId", item.id.toString())
                            addProperty("amount", item.amount.toString())
                        }
                        scrollArray.add(itemObj)
                    }
                }
                add("scroll", scrollArray)
            }
            arr.add(obj)
        }
        root.add("enchantedHeadgear", arr)
    }

    /**
     * Parses enchanted headgear scroll data from JSON array.
     */
    fun parse(data: JsonArray) {
        data.forEach { element ->
            val obj = element.asJsonObject
            val chargedItemId = obj.get("id").asInt
            val scrollsArr = obj.getAsJsonArray("scroll")

            val headgear = EnchantedHeadgear.forItem(chargedItemId.asItem())
                ?.takeIf { it.second == EnchantedHeadgear.HeadgearType.CHARGED }?.first
                ?: return@forEach

            val container = Container(headgear.scrollCapacity)

            scrollsArr.forEach { s ->
                val scrollObj = s.asJsonObject
                val id = scrollObj.get("itemId").asInt
                val amount = scrollObj.get("amount").asInt
                container.add(Item(id, amount))
            }
            enchantedGear[chargedItemId] = ChargedHeadgear(chargedItemId, container)
        }
    }

    data class ChargedHeadgear(
        val chargedItemId: Int,
        val container: Container,
    )

    private fun Container.isEmpty(): Boolean = this.toArray().all { it == null }
}