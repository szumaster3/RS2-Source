package com.alex.tools.pack

import com.alex.Cache
import com.alex.store.Index
import java.io.File

object ModelPacker {

    private val modelIndex: Index
        get() = Cache.getStore()!!.indexes[7]

    @JvmStatic
    fun add() {
        val baseFolder = File("../Assets/models/")
        if (!baseFolder.exists() || !baseFolder.isDirectory) return
        val files = baseFolder.walk()
            .filter { it.isFile && it.extension.lowercase() == "dat" }
            .toList()

        val sortedFiles = files.sortedBy { it.nameWithoutExtension.toIntOrNull() ?: Int.MAX_VALUE }
        val dataList = sortedFiles.map { file ->
            val id = file.nameWithoutExtension.toInt()
            val data = file.readBytes()
            id to data
        }
        dataList.forEach { (id, data) ->
            try {
                modelIndex.putFile(id, 0, data)
                println("Packed model $id")
            } catch (ex: Exception) {
                println("Failed to pack model $id: ${ex.message}")
            }
        }
    }
}