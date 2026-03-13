package content

import com.alex.Cache
import com.alex.store.Index
import java.io.File
import java.nio.file.Files

object ModelLoader {

    private val modelIndex: Index
        get() = Cache.getStore()!!.indexes[7]

    @JvmStatic
    fun importModels() {
        val folder = File("../Assets/models/")
        if (!folder.exists() || !folder.isDirectory) return

        val files = folder.listFiles { f -> f.extension.lowercase() == "dat" }?.sorted() ?: return
        if (files.isEmpty()) return

        var archiveId = modelIndex.lastArchiveId + 1

        files.forEach { file ->
            try {
                val data = Files.readAllBytes(file.toPath())
                val id = file.nameWithoutExtension.toIntOrNull() ?: archiveId
                modelIndex.putFile(id, 0, data)
                archiveId++
            } catch (_: Exception) {
            }
        }
    }
}