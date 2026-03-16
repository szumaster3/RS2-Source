package com.alex.tools

import com.alex.Cache
import com.alex.loaders.BasDefinition
import com.alex.io.OutputStream

class BasPacker private constructor(private val targetBasId: Int = 0) {

    private var startId: Int = targetBasId
    private val copiedBas = mutableListOf<BasDefinition>()

    companion object {
        fun create(): BasPacker = BasPacker()
    }

    fun startAt(id: Int): BasPacker {
        this.startId = id
        return this
    }

    fun addBas(setup: BasDefinition.() -> Unit): BasPacker {
        val store = Cache.getStore() ?: throw IllegalStateException("Cache store is not loaded!")
        val bas = BasDefinition(startId)
        bas.setup()
        saveBasToStore(bas, store)
        copiedBas.add(bas)
        startId++
        return this
    }

    fun addBas(vararg setups: BasDefinition.() -> Unit): BasPacker {
        setups.forEach { addBas(it) }
        return this
    }

    fun save(): BasPacker {
        val store = Cache.getStore() ?: throw IllegalStateException("Cache store is not loaded!")
        copiedBas.forEach { saveBasToStore(it, store) }
        return this
    }

    fun getCopiedBas(): List<BasDefinition> = copiedBas

    private fun saveBasToStore(bas: BasDefinition, store: com.alex.store.Store) {
        val stream = OutputStream()
        bas.encode(stream)

        val data = stream.buffer.copyOf(stream.offset)
        store.indexes[2].putFile(32, bas.id, data)
    }
}