package com.alex.tools

import com.alex.Cache
import com.alex.loaders.MapIndex
import com.alex.utils.Utils
import java.io.File
import java.nio.file.Files

object MapPacker {

    @JvmStatic
    fun pack() {
        val store = Cache.getStore()
        val mapIndex = store.indexes[5]

        val mapDir = File("Maps/map_files/")
        val landDir = File("Maps/land_files/")

        if (!mapDir.exists() || !landDir.exists()) {
            println("Folders not found!")
            return
        }

        val mapFiles = mapDir.listFiles { f -> f.extension == "dat" } ?: return

        var nextArchiveId = mapIndex.lastArchiveId + 1

        for (mapFile in mapFiles) {

            val regionId = mapFile.nameWithoutExtension.toIntOrNull()
            if (regionId == null) {
                println("Invalid map file name: ${mapFile.name}")
                continue
            }

            val landFile = File(landDir, "$regionId.dat")
            if (!landFile.exists()) {
                println("Missing land file for region $regionId")
                continue
            }

            val x = regionId shr 8
            val y = regionId and 0xFF

            val mapName = "m${x}_$y"
            val landName = "l${x}_$y"

            val mapArchiveId = mapIndex.getArchiveId(mapName).takeIf { it != -1 }
                ?: nextArchiveId++

            val landArchiveId = mapIndex.getArchiveId(landName).takeIf { it != -1 }
                ?: nextArchiveId++

            val mapData = Files.readAllBytes(mapFile.toPath())
            val landData = Files.readAllBytes(landFile.toPath())

            if (mapData.isEmpty() || landData.isEmpty()) {
                println("Empty data for region $regionId")
                continue
            }

            mapIndex.putFile(
                mapArchiveId,
                0,
                0,
                mapData,
                null,
                false,
                false,
                Utils.getNameHash(mapName),
                0
            )

            mapIndex.putFile(
                landArchiveId,
                0,
                0,
                landData,
                null,
                false,
                false,
                Utils.getNameHash(landName),
                0
            )

            MapIndex.setRegion(regionId, landArchiveId, mapArchiveId)

            println("Packed region $regionId [$mapName, $landName]")
        }

        mapIndex.rewriteTable()
        mapIndex.resetCachedFiles()

        println("Done packing maps.")
    }
}