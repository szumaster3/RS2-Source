package com.alex.tools

import com.alex.Cache
import com.alex.loaders.items.ItemDefinition

class ItemPacker private constructor(private val targetItemId: Int = 0) {

    private var startId: Int = targetItemId
    private val copiedItems = mutableListOf<ItemDefinition>()

    companion object {
        fun create(): ItemPacker = ItemPacker()
    }

    fun startAt(id: Int): ItemPacker {
        this.startId = id
        return this
    }

    fun addItem(setup: ItemDefinition.() -> Unit): ItemPacker {
        val store = Cache.getStore() ?: throw IllegalStateException("Cache store is not loaded!")
        val item = ItemDefinition(store, startId, false)
        item.setup()
        item.write(store)
        copiedItems.add(item)
        startId++
        return this
    }

    fun addItems(vararg setups: ItemDefinition.() -> Unit): ItemPacker {
        setups.forEach { addItem(it) }
        return this
    }

    fun save(): ItemPacker {
        val store = Cache.getStore() ?: throw IllegalStateException("Cache store is not loaded!")

        copiedItems.forEach { item ->
            item.write(store)
            println("Packed ${item.name ?: "unknown"}:${item.id}")
        }

        return this
    }

    fun getCopiedItems(): List<ItemDefinition> = copiedItems
}