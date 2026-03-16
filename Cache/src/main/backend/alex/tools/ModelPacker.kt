package backend.alex.tools

import backend.alex.Cache
import backend.alex.store.Index
import java.io.File
import java.nio.file.Files

object ModelPacker {

    private val modelIndex: Index
        get() = Cache.getStore()!!.indexes[7]

    @JvmStatic
    fun add() {
        val folder = File("../Assets/models/")

        if (!folder.exists() || !folder.isDirectory) return
        folder.walk().forEach { file ->
            if (!file.isFile || file.extension.lowercase() != "dat") return@forEach
            try {
                val id = file.nameWithoutExtension.toIntOrNull() ?: return@forEach
                val data = Files.readAllBytes(file.toPath())
                modelIndex.putFile(id, 0, data)
                println("Packed model $id")
            } catch (_: Exception) {

            }
        }
    }
}