package com.alex.tools

import com.alex.Cache
import com.alex.loaders.items.ItemDefinition

class ItemCopy private constructor(private val startIdInitial: Int = 0) {

    private var startId: Int = startIdInitial
    private val copiedItems = mutableListOf<ItemDefinition>()

    companion object {
        fun create(): ItemCopy = ItemCopy()
    }

    fun startAt(id: Int): ItemCopy {
        this.startId = id
        return this
    }

    fun addItem(setup: ItemDefinition.() -> Unit): ItemCopy {
        val store = Cache.getStore() ?: throw IllegalStateException("Cache store is not loaded!")
        val item = ItemDefinition(store, startId, false)
        item.setup()
        item.write(store)
        copiedItems.add(item)
        startId++
        return this
    }

    fun addItems(vararg setups: ItemDefinition.() -> Unit): ItemCopy {
        setups.forEach { addItem(it) }
        return this
    }

    fun ItemDefinition.changeModelColor(originalColor: Int, modifiedColor: Int) {
        if (this.originalModelColors != null) {
            for (i in this.originalModelColors!!.indices) {
                if (this.originalModelColors!![i] == originalColor) {
                    this.modifiedModelColors!![i] = modifiedColor
                    return
                }
            }

            val newOriginal = originalModelColors!!.copyOf(originalModelColors!!.size + 1)
            val newModified = modifiedModelColors!!.copyOf(modifiedModelColors!!.size + 1)
            newOriginal[newOriginal.size - 1] = originalColor
            newModified[newModified.size - 1] = modifiedColor
            this.originalModelColors = newOriginal
            this.modifiedModelColors = newModified
        } else {
            this.originalModelColors = intArrayOf(originalColor)
            this.modifiedModelColors = intArrayOf(modifiedColor)
        }
    }

    fun ItemDefinition.changeTextureColor(originalColor: Short, modifiedColor: Short) {
        if (this.originalTextureColors != null) {
            for (i in this.originalTextureColors!!.indices) {
                if (this.originalTextureColors!![i] == originalColor) {
                    this.modifiedTextureColors!![i] = modifiedColor
                    return
                }
            }

            val newOriginal = originalTextureColors!!.copyOf(originalTextureColors!!.size + 1)
            val newModified = modifiedTextureColors!!.copyOf(modifiedTextureColors!!.size + 1)
            newOriginal[newOriginal.size - 1] = originalColor
            newModified[newModified.size - 1] = modifiedColor
            this.originalTextureColors = newOriginal
            this.modifiedTextureColors = newModified
        } else {
            this.originalTextureColors = shortArrayOf(originalColor)
            this.modifiedTextureColors = shortArrayOf(modifiedColor)
        }
    }

    fun save(): ItemCopy {
        val store = Cache.getStore() ?: throw IllegalStateException("Cache store is not loaded!")
        copiedItems.forEach { it.write(store) }
        return this
    }

    fun getCopiedItems(): List<ItemDefinition> = copiedItems
}