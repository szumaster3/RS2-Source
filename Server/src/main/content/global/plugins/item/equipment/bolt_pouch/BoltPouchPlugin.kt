package content.global.plugins.item.equipment.bolt_pouch

import core.api.*
import core.cache.def.impl.ItemDefinition
import core.game.container.impl.EquipmentContainer
import core.game.event.EventHook
import core.game.event.ItemEquipEvent
import core.game.event.ItemUnequipEvent
import core.game.global.action.DropListener
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.interaction.InterfaceListener
import core.game.node.entity.Entity
import core.game.node.entity.player.Player
import core.game.node.item.GroundItemManager
import core.game.node.item.Item
import core.game.system.config.ItemConfigParser
import core.tools.colorize
import shared.consts.Components
import shared.consts.Items

private const val BOLT_POUCH = Items.BOLT_POUCH_9433
private const val MAX_SLOTS = 4
private const val ARROWS_SLOT = EquipmentContainer.SLOT_ARROWS
private val WIELD_BOLT_SLOTS = intArrayOf(3, 7, 11, 15)
private val REMOVE_BOLT_SLOTS = intArrayOf(4, 8, 12, 16)
private const val UNWIELD_BOLT_SLOT = 19

private val boltAmountTextIds = intArrayOf(20, 21, 22, 23)
private val boltNameTextIds = intArrayOf(25, 26, 27, 28)
private val modelComponentIds = intArrayOf(2, 6, 10, 14)

class BoltPouchPlugin : InterfaceListener, InteractionListener
{
    private val equipHook =
        object : EventHook<ItemEquipEvent>
        {
            override fun process(entity: Entity, event: ItemEquipEvent)
            {
                val p = entity as Player
                if (event.slotId == ARROWS_SLOT) updateBoltPouchDisplay(p)
            }
        }

    private val unequipHook =
        object : EventHook<ItemUnequipEvent>
        {
            override fun process(entity: Entity, event: ItemUnequipEvent)
            {
                val p = entity as Player
                if (event.slotId == ARROWS_SLOT) updateBoltPouchDisplay(p)
            }
        }

    override fun defineListeners() {
        on(BOLT_POUCH, IntType.ITEM, "open") { player, _ ->
            openInterface(player, Components.XBOWS_POUCH_433)
            player.hook(ItemEquipEvent::class.java, equipHook)
            player.hook(ItemUnequipEvent::class.java, unequipHook)
            return@on true
        }

        on(BOLT_POUCH, IntType.ITEM, "destroy") { player, item ->
            val itemDef = ItemDefinition.forId(item.id)
            sendDestroyItemDialogue(
                player,
                itemDef.id,
                itemDef.getConfiguration(ItemConfigParser.DESTROY_MESSAGE)
            )

            addDialogueAction(player) { player, button ->
                if (button == 3) {
                    closeDialogue(player)

                    val manager = player.boltPouchManager
                    val container = manager.getPouchContainer(item.id)

                    container?.let {
                        for (i in 0 until it.capacity()) {
                            val bolt = it.get(i)
                            if (bolt != null && bolt.amount > 0) {
                                val dropped = bolt.dropItem
                                GroundItemManager.create(dropped, player.location, player)
                                it.replace(null, i)
                            }
                        }
                    }

                    removeItem(player, item.id)
                    updateBoltPouchDisplay(player)
                }
            }
            return@on true
        }

        onUseWith(IntType.ITEM, BoltPouchManager.ALLOWED_BOLT_IDS, BOLT_POUCH) { player, used, with ->
            val pouch = with.asItem()
            val bolts = used.asItem()
            val manager = player.boltPouchManager

            if (used.id !in BoltPouchManager.ALLOWED_BOLT_IDS)
            {
                sendMessage(player, "You can't add that type of bolt to the pouch.")
            }
            else
            {
                val added = manager.addBolts(pouch.id, bolts.id, bolts.asItem().amount)

                if (added > 0)
                {
                    player.inventory.remove(Item(bolts.id, added))
                    sendMessage(player, "You place some bolts into the bolt pouch.")
                }

                else
                {
                    val container = manager.getPouchContainer(pouch.id)
                    container?.let {
                        val existingSlot = (0 until it.capacity()).firstOrNull { slot ->
                            val itemInSlot = it.get(slot)
                            itemInSlot?.id == bolts.id && itemInSlot.amount >= BoltPouchManager.SIZE
                        }
                        if (existingSlot != null)
                        {
                            sendMessage(player, "You can't add more bolts of this type.")
                        }
                        else if (it.isFull)
                        {
                            sendMessage(player, "The bolt pouch is already full.")
                        }
                    }
                }
            }

            updateBoltPouchDisplay(player)
            return@onUseWith true
        }
    }

    override fun defineInterfaceListeners()
    {
        on(Components.XBOWS_POUCH_433) { player, _, _, buttonID, _, _ ->
            val manager = player.boltPouchManager

            when (buttonID)
            {
                in WIELD_BOLT_SLOTS -> {
                    val pouchSlot = WIELD_BOLT_SLOTS.indexOf(buttonID)

                    when {
                        !manager.hasBolts(BOLT_POUCH, pouchSlot) ->
                            sendMessage(player, "You don't have any bolts in this slot.")
                        freeSlots(player) == 0 ->
                            sendMessage(player, "You don't have enough space in your inventory.")
                        manager.wieldBolts(BOLT_POUCH, pouchSlot, player) ->
                            sendMessage(player, "You wield some bolts from your bolt pouch.")
                    }

                    updateBoltPouchDisplay(player)
                }

                in REMOVE_BOLT_SLOTS -> {
                    val pouchSlot = REMOVE_BOLT_SLOTS.indexOf(buttonID)

                    when {
                        !manager.hasBolts(BOLT_POUCH, pouchSlot) ->
                            sendMessage(player, "There's nothing to remove in this slot.")
                        freeSlots(player) == 0 ->
                            sendMessage(player, "You don't have enough space in your inventory.")
                        manager.removeBolts(BOLT_POUCH, pouchSlot, player) -> {
                            val ordinal = arrayOf("first", "second", "third", "fourth")[pouchSlot]
                            sendMessage(
                                player,
                                "You remove all the bolts from the $ordinal slot of your bolt pouch."
                            )
                        }
                    }

                    updateBoltPouchDisplay(player)
                }

                UNWIELD_BOLT_SLOT -> {
                    val ammo = player.equipment.get(ARROWS_SLOT)

                    when {
                        ammo == null || ammo.amount == 0 ->
                            sendMessage(player, "You're not wielding any bolts.")
                        freeSlots(player) == 0 ->
                            sendMessage(player, "You don't have enough space in your inventory.")
                        else -> {
                            player.equipment.remove(ammo)
                            player.inventory.add(ammo)
                        }
                    }

                    updateBoltPouchDisplay(player)
                }
                else -> return@on false
            }

            return@on true
        }

        onOpen(Components.XBOWS_POUCH_433) { player, _ ->
            updateBoltPouchDisplay(player)
            return@onOpen true
        }

        onClose(Components.XBOWS_POUCH_433) { player, _ ->
            player.unhook(equipHook)
            player.unhook(unequipHook)
            return@onClose true
        }
    }

    fun updateBoltPouchDisplay(player: Player) {
        val manager = player.boltPouchManager
        val current = player.equipment.get(ARROWS_SLOT)
        val iface = Components.XBOWS_POUCH_433

        if (current != null && current.id > 0 && current.amount > 0) {
            player.packetDispatch.sendItemZoomOnInterface(current.id, current.amount, 190, iface, 18)
            sendString(player, current.name, iface, 29)
            sendString(player, colorize("%G${current.amount}"), iface, 24)
        } else {
            sendModelOnInterface(player, Components.XBOWS_POUCH_433, 18, -1)
            sendString(player, "Nothing", iface, 29)
            sendString(player, "0", iface, 24)
        }

        for (i in 0 until MAX_SLOTS) {
            val boltId = manager.getBolt(i)
            val amount = manager.getAmount(i)

            val name = if (boltId != -1) getItemName(boltId) else "Nothing"
            val amountText = if (amount > 0) colorize("%G$amount") else "0"

            sendString(player, name, iface, boltNameTextIds[i])
            sendString(player, amountText, iface, boltAmountTextIds[i])

            if (boltId != -1) {
                player.packetDispatch.sendItemZoomOnInterface(boltId, amount, 190, iface, modelComponentIds[i])
            } else {
                sendModelOnInterface(player, Components.XBOWS_POUCH_433, modelComponentIds[i], -1)
            }
        }
    }

}