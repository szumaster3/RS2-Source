package com.alex.tools

import com.alex.Cache
import com.alex.io.OutputStream
import com.alex.loaders.LocDefinition

class LocPacker private constructor(private val targetLocId: Int = 0) {

    private var startId: Int = targetLocId
    private val copiedLocs = mutableListOf<LocDefinition>()

    companion object {
        fun create(): LocPacker = LocPacker()
    }

    fun startAt(id: Int): LocPacker {
        this.startId = id
        return this
    }

    fun addLoc(setup: LocDefinition.() -> Unit): LocPacker {
        val loc = LocDefinition()
        loc.id = startId

        loc.setup()

        copiedLocs.add(loc)
        startId++

        return this
    }

    fun addLocs(vararg setups: LocDefinition.() -> Unit): LocPacker {
        setups.forEach { addLoc(it) }
        return this
    }

    fun save(): LocPacker {
        val store = Cache.getStore() ?: throw IllegalStateException("Cache store is not loaded!")

        copiedLocs.forEach { loc ->

            val stream = OutputStream()
            loc.encode(stream)

            val data = stream.buffer.copyOf(stream.offset)

            store.indexes[16].putFile(
                loc.id shr 8,
                loc.id and 0xFF,
                data
            )

            println("Packed ${loc.name ?: "unknown"}:${loc.id}")
        }

        return this
    }

    fun getCopiedLocs(): List<LocDefinition> = copiedLocs
}