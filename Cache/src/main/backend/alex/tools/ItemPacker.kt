package backend.alex.tools

import backend.alex.Cache
import backend.alex.loaders.items.ItemDefinition

class ItemPacker private constructor(private val startId: Int = 0) {

    private var currentId = startId
    private val tasks = mutableListOf<Task>()

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
        val savedItems = mutableListOf<ItemDefinition>()

        for (task in tasks) {
            val item = if (task.sourceId >= 0) {
                val srcItem = ItemDefinition(store, task.sourceId, false)
                srcItem.apply { task.modifier?.invoke(this) }
            } else {
                val newItem = ItemDefinition(store, currentId, false)
                newItem.apply { task.modifier?.invoke(this) }
            }

            item.write(store)
            println("Packed ${item.name ?: "unknown"}:${currentId}")
            savedItems.add(item)
            currentId++
        }

        return savedItems
    }
}