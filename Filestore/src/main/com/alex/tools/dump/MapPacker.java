package com.alex.tools.dump;

import com.alex.store.Index;
import com.alex.store.Store;
import com.alex.util.XTEAManager;
import com.alex.utils.CompressionUtils;

import java.io.File;
import java.nio.file.Files;

public final class MapPacker {

    public static void pack(Store cache, String path,
                            int regionId, int mapId, int objId) {

        Index mapIndex = cache.getIndexes()[5];

        File map = new File(path, "map_files");
        File obj = new File(path, "land_files");

        if (!map.exists() || !obj.exists()) {
            System.out.println("Missing data.");
            return;
        }

        int x = (regionId >> 8) & 0xFF;
        int y = regionId & 0xFF;

        try {
            File mapData = getFile(map, mapId);
            if (mapData != null) {
                byte[] data = Files.readAllBytes(mapData.toPath());
                if (mapData.getName().endsWith(".gz"))
                    data = CompressionUtils.gunzip(data);
                mapIndex.putFile(mapId, 0, data);
            } else {
                System.out.println("Missing map file: " + mapId);
            }
            File objData = getFile(obj, objId);
            if (objData != null) {
                byte[] data = Files.readAllBytes(objData.toPath());
                if (objData.getName().endsWith(".gz"))
                    data = CompressionUtils.gunzip(data);
                int[] keys = XTEAManager.lookup(regionId);
                mapIndex.putFile(objId, 0, data, keys);
            } else {
                System.out.println("Missing obj file: " + objId);
            }
        } catch (Exception e) {
            System.out.println("Failed region " + regionId + ": " + e.getMessage());
        }
    }

    private static File getFile(File file, int id) {
        File gz = new File(file, id + ".gz");
        if (gz.exists()) return gz;
        File dat = new File(file, id + ".dat");
        if (dat.exists()) return dat;
        return null;
    }
}