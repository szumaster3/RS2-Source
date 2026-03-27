package com.alex.loaders;

import com.alex.io.InputStream;
import com.alex.io.OutputStream;
import com.alex.store.Store;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;

public final class SpotAnimDefinition {
    public final int id;
    public boolean isLoaded = false;
    public int modelId;
    public int seqId = -1;
    public int resizeXZ = 128;
    public int resizeY = 128;
    public int angle = 0;
    public int ambient = 0;
    public int contrast = 0;
    public boolean aBoolean100 = false;
    public short[] recol_s;
    public short[] recol_d;
    public short[] retex_s;
    public short[] retex_d;

    public SpotAnimDefinition(int id) {
        this.id = id;
    }

    public SpotAnimDefinition(Store cache, int id) {
        this.id = id;
        load(cache);
    }

    public void load(Store cache) {
        try {
            byte[] data = cache.getIndexes()[2].getFile(4, id);

            if (data != null) {
                parse(new InputStream(data));
                isLoaded = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void parse(InputStream stream) {
        while (true) {
            int opcode = stream.readUnsignedByte();
            if (opcode == 0) {
                break;
            }
            decode(stream, opcode);
        }
    }

    private void decode(InputStream stream, int opcode) {
        int size;
        switch (opcode) {
            case 1:
                modelId = stream.readUnsignedShort();
                break;
            case 2:
                seqId = stream.readUnsignedShort();
                break;
            case 4:
                resizeXZ = stream.readUnsignedShort();
                break;
            case 5:
                resizeY = stream.readUnsignedShort();
                break;
            case 6:
                angle = stream.readUnsignedShort();
                break;
            case 7:
                ambient = stream.readUnsignedByte();
                break;
            case 8:
                contrast = stream.readUnsignedByte();
                break;
            case 9:
                aBoolean100 = true;
                break;
            case 40:
                size = stream.readUnsignedByte();
                recol_s = new short[size];
                recol_d = new short[size];
                for (int i = 0; i < size; i++) {
                    recol_s[i] = (short) stream.readUnsignedShort();
                    recol_d[i] = (short) stream.readUnsignedShort();
                }
                break;
            case 41:
                size = stream.readUnsignedByte();
                retex_s = new short[size];
                retex_d = new short[size];
                for (int i = 0; i < size; i++) {
                    retex_s[i] = (short) stream.readUnsignedShort();
                    retex_d[i] = (short) stream.readUnsignedShort();
                }
                break;
        }
    }

    public byte[] encode() {
        OutputStream out = new OutputStream();

        if (modelId != 0) {
            out.writeByte(1);
            out.writeShort(modelId);
        }

        if (seqId != -1) {
            out.writeByte(2);
            out.writeShort(seqId);
        }

        if (resizeXZ != 128) {
            out.writeByte(4);
            out.writeShort(resizeXZ);
        }

        if (resizeY != 128) {
            out.writeByte(5);
            out.writeShort(resizeY);
        }

        if (angle != 0) {
            out.writeByte(6);
            out.writeShort(angle);
        }

        if (ambient != 0) {
            out.writeByte(7);
            out.writeByte(ambient);
        }

        if (contrast != 0) {
            out.writeByte(8);
            out.writeByte(contrast);
        }

        if (aBoolean100) {
            out.writeByte(9);
        }

        if (recol_s != null && recol_d != null) {
            out.writeByte(40);
            out.writeByte(recol_s.length);

            for (int i = 0; i < recol_s.length; i++) {
                out.writeShort(recol_s[i]);
                out.writeShort(recol_d[i]);
            }
        }

        if (retex_s != null && retex_d != null) {
            out.writeByte(41);
            out.writeByte(retex_s.length);

            for (int i = 0; i < retex_s.length; i++) {
                out.writeShort(retex_s[i]);
                out.writeShort(retex_d[i]);
            }
        }

        out.writeByte(0);

        byte[] data = new byte[out.getOffset()];
        out.setOffset(0);
        out.getBytes(data, 0, data.length);
        return data;
    }

    public static void print(Store cache, String outputFile) {
        try {
            File file = new File(outputFile);

            if (file.getParentFile() != null) {
                file.getParentFile().mkdirs();
            }

            if (!file.exists()) {
                file.createNewFile();
            }

            int archive = 4;

            try (PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(file)))) {

                int max = cache.getIndexes()[2].getLastFileId(archive);

                for (int id = 0; id <= max; id++) {

                    try {
                        byte[] data = cache.getIndexes()[2].getFile(archive, id);
                        if (data == null) continue;

                        SpotAnimDefinition def = new SpotAnimDefinition(id);
                        def.parse(new InputStream(data));

                        boolean wrote = false;

                        StringBuilder sb = new StringBuilder();
                        sb.append("========== SPOTANIM ").append(id).append(" ==========\n");

                        if (def.modelId != 0) {
                            sb.append("modelId = ").append(def.modelId).append("\n");
                            wrote = true;
                        }

                        if (def.seqId != -1) {
                            sb.append("seqId = ").append(def.seqId).append("\n");
                            wrote = true;
                        }

                        if (def.resizeXZ != 128) {
                            sb.append("resizeXZ = ").append(def.resizeXZ).append("\n");
                            wrote = true;
                        }

                        if (def.resizeY != 128) {
                            sb.append("resizeY = ").append(def.resizeY).append("\n");
                            wrote = true;
                        }

                        if (def.angle != 0) {
                            sb.append("angle = ").append(def.angle).append("\n");
                            wrote = true;
                        }

                        if (def.ambient != 0) {
                            sb.append("ambient = ").append(def.ambient).append("\n");
                            wrote = true;
                        }

                        if (def.contrast != 0) {
                            sb.append("contrast = ").append(def.contrast).append("\n");
                            wrote = true;
                        }

                        if (def.aBoolean100) {
                            sb.append("aBoolean100 = true\n");
                            wrote = true;
                        }

                        if (def.recol_s != null && def.recol_d != null) {
                            boolean header = false;

                            for (int i = 0; i < def.recol_s.length; i++) {
                                if (def.recol_s[i] != def.recol_d[i]) {
                                    if (!header) {
                                        sb.append("===== RECOLORS =====\n");
                                        header = true;
                                    }

                                    sb.append(def.recol_s[i])
                                            .append(" -> ")
                                            .append(def.recol_d[i])
                                            .append("\n");

                                    wrote = true;
                                }
                            }
                        }

                        if (def.retex_s != null && def.retex_d != null) {
                            boolean header = false;

                            for (int i = 0; i < def.retex_s.length; i++) {
                                if (def.retex_s[i] != def.retex_d[i]) {
                                    if (!header) {
                                        sb.append("-- retextures --\n");
                                        header = true;
                                    }

                                    sb.append(def.retex_s[i])
                                            .append(" -> ")
                                            .append(def.retex_d[i])
                                            .append("\n");

                                    wrote = true;
                                }
                            }
                        }

                        if (wrote) {
                            writer.println(sb);
                        }

                    } catch (Exception ignored) {
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*
     * public Object constructModel(Object modelCache,
     *                              Object modelsArchive,
     *                              Object seqTypeList,
     *                              int frameId,
     *                              int frame2) {
     *
     *     Object rawModel = ModelCache.get(modelCache, id);
     *
     *     if (rawModel == null) {
     *
     *         Object base = RawModel.create(modelsArchive, modelId);
     *
     *         if (base == null) {
     *             return null;
     *         }
     *
     *         if (recol_s != null) {
     *             for (int i = 0; i < recol_s.length; i++) {
     *                 base.recolor(recol_s[i], recol_d[i]);
     *             }
     *         }
     *
     *         if (retex_s != null) {
     *             for (int i = 0; i < retex_s.length; i++) {
     *                 base.retexture(retex_s[i], retex_d[i]);
     *             }
     *         }
     *
     *         rawModel = base.createModel(
     *                 ambient + 64,
     *                 contrast + 850,
     *                 -30, -50, -30
     *         );
     *
     *         ModelCache.put(modelCache, rawModel, id);
     *     }
     *
     *     Object model;
     *
     *     if (seqId == -1 || frame2 == -1) {
     *         model = ModelUtil.copy(rawModel);
     *     } else {
     *         model = SeqUtil.apply(seqTypeList, seqId, frameId, frame2, rawModel);
     *     }
     *
     *     if (resizeXZ != 128 || resizeY != 128) {
     *         ModelUtil.resize(model, resizeXZ, resizeY, resizeXZ);
     *     }
     *
     *     if (angle != 0) {
     *         if (angle == 90) ModelUtil.rotateCCW(model);
     *         else if (angle == 180) ModelUtil.rotate180(model);
     *         else if (angle == 270) ModelUtil.rotateCW(model);
     *     }
     *
     *     return model;
     * }
     */
}