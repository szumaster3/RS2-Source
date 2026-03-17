package backend.alex.tools

import backend.alex.Cache
import backend.alex.loaders.items.ItemDefinition

class ItemPacker private constructor(private val startId: Int = 0) {

    private var currentId = startId
    private val tasks = mutableListOf<Task>()
    private val copiedItems = mutableListOf<ItemDefinition>()

    private data class Task(
        val sourceId: Int,
        val modifier: (ItemDefinition.() -> Unit)?
    )

    companion object {
        fun create(): ItemPacker = ItemPacker()
    }

    fun startAt(id: Int): ItemPacker {
        currentId = id
        return this
    }

    fun addItem(modifier: ItemDefinition.() -> Unit): ItemPacker {
        tasks.add(Task(-1, modifier))
        return this
    }

    fun addItems(vararg modifiers: ItemDefinition.() -> Unit): ItemPacker {
        modifiers.forEach { addItem(it) }
        return this
    }

    fun copyItem(sourceId: Int, modifier: ItemDefinition.() -> Unit = {}): ItemPacker {
        tasks.add(Task(sourceId, modifier))
        return this
    }

    fun copyRange(fromId: Int, toId: Int, modifier: ItemDefinition.() -> Unit = {}): ItemPacker {
        for (id in fromId..toId) copyItem(id, modifier)
        return this
    }

    fun save(): List<ItemDefinition> {
        val store = Cache.getStore() ?: throw IllegalStateException("Cache store not loaded")
        copiedItems.clear()

        for (task in tasks) {
            val item = if (task.sourceId >= 0) {
                val srcItem = ItemDefinition(store, task.sourceId, false)
                srcItem.apply { task.modifier?.invoke(this) }
            } else {
                val newItem = ItemDefinition(store, currentId, false)
                newItem.apply { task.modifier?.invoke(this) }
            }

            item.write(store)
            copiedItems.add(item)
            println("Packed ${item.name ?: "unknown"}:${currentId}")
            currentId++
        }

        return copiedItems
    }

    fun addNoteItem(templateId: Int = 799): ItemDefinition {
        val store = Cache.getStore() ?: error("Store not initialized")
        val baseItem = copiedItems.lastOrNull() ?: error("No base item to note")
        val newId = currentId

        val noteItem = ItemDefinition(store, newId, false).apply {
            id = newId
            notedItemId = baseItem.id
            switchNoteItemId = templateId
        }

        noteItem.write(store)
        copiedItems.add(noteItem)
        currentId++

        return noteItem
    }

    fun getCopiedItems(): List<ItemDefinition> = copiedItems
}