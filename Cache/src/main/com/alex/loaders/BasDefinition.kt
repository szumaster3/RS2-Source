package com.alex.loaders

import com.alex.io.InputStream
import com.alex.io.OutputStream
import com.alex.store.Store
import com.alex.utils.Utils

class BasDefinition(var id: Int) {
    var isLoaded: Boolean = false
    private var anIntArrayArray7: Array<IntArray?>? = null
    private var anInt1036: Int = -1
    var anInt1037: Int = -1
    private var anInt1043: Int = -1
    private var anInt1041: Int = 0
    private var anInt1042: Int = -1
    private var anInt1031: Int = 0
    private var anInt1050: Int = 0
    private var anInt1048: Int = -1
    private var anInt1054: Int = -1
    private var anInt1055: Int = 0
    private var anInt1035: Int = -1
    private var anInt1056: Int = -1
    private var anInt1032: Int = -1
    var anInt1051: Int = -1
    private var anInt1059: Int = 0
    private var anInt1045: Int = -1
    private var anInt1038: Int = 0
    var anInt1065: Int = 0
    private var anInt1062: Int = -1
    private var anInt1040: Int = 0
    private var anInt1058: Int = -1
    var anInt1066: Int = -1
    var anInt1063: Int = 0
    private var anInt1057: Int = -1
    private var anInt1067: Int = -1
    var anInt1064: Int = 0

    fun decode(stream: InputStream)
    {
        while (true) {
            val opcode = stream.readUnsignedByte()
            if (opcode == 0) break
            decodeOpcode(opcode, stream)
        }
    }

    private fun decodeOpcode(opcode: Int, stream: InputStream) {
        when (opcode) {
            1 -> {
                anInt1037 = stream.readUnsignedShort()
                anInt1051 = stream.readUnsignedShort()
                if (anInt1037 == 65535) anInt1037 = -1
                if (anInt1051 == 65535) anInt1051 = -1
            }
            2 -> anInt1062 = stream.readUnsignedShort()
            3 -> anInt1042 = stream.readUnsignedShort()
            4 -> anInt1066 = stream.readUnsignedShort()
            5 -> anInt1048 = stream.readUnsignedShort()
            6 -> anInt1058 = stream.readUnsignedShort()
            7 -> anInt1054 = stream.readUnsignedShort()
            8 -> anInt1043 = stream.readUnsignedShort()
            9 -> anInt1045 = stream.readUnsignedShort()
            26 -> {
                anInt1059 = stream.readUnsignedByte() * 4
                anInt1050 = stream.readUnsignedByte() * 4
            }
            27 -> {
                if (anIntArrayArray7 == null) {
                    anIntArrayArray7 = Array(12) { null }
                }
                val arrays = anIntArrayArray7!!
                val idx = stream.readUnsignedByte()
                val arr = IntArray(6) { stream.readShortSmart() }
                arrays[idx] = arr
            }
            29 -> anInt1038 = stream.readUnsignedByte()
            30 -> anInt1031 = stream.readUnsignedShort()
            31 -> anInt1055 = stream.readUnsignedByte()
            32 -> anInt1040 = stream.readUnsignedShort()
            33 -> anInt1064 = stream.readShortSmart()
            34 -> anInt1065 = stream.readUnsignedByte()
            35 -> anInt1063 = stream.readUnsignedShort()
            36 -> anInt1041 = stream.readShortSmart()
            37 -> anInt1032 = stream.readUnsignedByte()
            38 -> anInt1036 = stream.readUnsignedShort()
            39 -> anInt1067 = stream.readUnsignedShort()
            40 -> anInt1056 = stream.readUnsignedShort()
            41 -> anInt1057 = stream.readUnsignedShort()
            42 -> anInt1035 = stream.readUnsignedShort()
            43, 44, 45 -> { stream.readUnsignedShort() }
            else -> throw RuntimeException("Unknown opcode=$opcode for=$id")
        }
    }

    fun encode(stream: OutputStream) {
        fun write(opcode: Int, block: OutputStream.() -> Unit) {
            stream.writeByte(opcode)
            stream.block()
        }

        if (anInt1037 != -1 || anInt1051 != -1) write(1) {
            writeShort(if (anInt1037 == -1) 65535 else anInt1037)
            writeShort(if (anInt1051 == -1) 65535 else anInt1051)
        }
        if (anInt1062 != -1) write(2) { writeShort(anInt1062) }
        if (anInt1042 != -1) write(3) { writeShort(anInt1042) }
        if (anInt1066 != -1) write(4) { writeShort(anInt1066) }
        if (anInt1048 != -1) write(5) { writeShort(anInt1048) }
        if (anInt1058 != -1) write(6) { writeShort(anInt1058) }
        if (anInt1054 != -1) write(7) { writeShort(anInt1054) }
        if (anInt1043 != -1) write(8) { writeShort(anInt1043) }
        if (anInt1045 != -1) write(9) { writeShort(anInt1045) }
        if (anInt1059 != 0 || anInt1050 != 0) write(26) {
            writeByte(anInt1059 / 4)
            writeByte(anInt1050 / 4)
        }
        anIntArrayArray7?.forEachIndexed { idx, arr ->
            if (arr != null) write(27) {
                writeByte(idx)
                arr.forEach { writeShortSmart(it) }
            }
        }
        if (anInt1038 != 0)  write(29) { writeByte(anInt1038) }
        if (anInt1031 != 0)  write(30) { writeShort(anInt1031) }
        if (anInt1055 != 0)  write(31) { writeByte(anInt1055) }
        if (anInt1040 != 0)  write(32) { writeShort(anInt1040) }
        if (anInt1064 != 0)  write(33) { writeShortSmart(anInt1064) }
        if (anInt1065 != 0)  write(34) { writeByte(anInt1065) }
        if (anInt1063 != 0)  write(35) { writeShort(anInt1063) }
        if (anInt1041 != 0)  write(36) { writeShortSmart(anInt1041) }
        if (anInt1032 != 0)  write(37) { writeByte(anInt1032) }
        if (anInt1036 != -1) write(38) { writeShort(anInt1036) }
        if (anInt1067 != -1) write(39) { writeShort(anInt1067) }
        if (anInt1056 != -1) write(40) { writeShort(anInt1056) }
        if (anInt1057 != -1) write(41) { writeShort(anInt1057) }
        if (anInt1035 != -1) write(42) { writeShort(anInt1035) }

        stream.writeByte(0)
    }

    companion object {
        fun load(cache: Store): Array<BasDefinition>?
        {
            return try {
                val index = cache.indexes[2]
                val size = Utils.getRenderAnimationDefinitionsSize(cache)
                val definitions = Array(size) { BasDefinition(it) }
                for (id in definitions.indices)
                {
                    val data = index.getFile(32, id)
                    if (data != null)
                    {
                        definitions[id].decode(InputStream(data))
                        definitions[id].isLoaded = true
                    }
                }
                definitions
            } catch (e: Throwable) {
                e.printStackTrace()
                null
            }
        }
    }
}