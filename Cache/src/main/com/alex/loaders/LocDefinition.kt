package com.alex.loaders

import com.alex.io.InputStream
import com.alex.io.OutputStream
import com.alex.store.Store
import com.alex.utils.Utils
import java.io.BufferedWriter
import java.io.File
import java.io.FileWriter
import java.io.PrintWriter
import java.lang.reflect.Modifier

class LocDefinition {
    var id = -1
    var name: String? = null
    var desc: String? = null
    var sizeX = 1
    var sizeY = 1
    var delayShading: Boolean = false
    @JvmField var interactable = false
    var blockProjectile = true
    var clipType = 2
    var mapIconId = -1
    var blocksProjectile = false
    var models: IntArray? = null
    var modelTypes: IntArray? = null

    var actions = arrayOfNulls<String>(5)
    var animations: IntArray? = null
    var percents: IntArray? = null
    var objectAnimation = -1
    var alternateModelIds: IntArray? = null

    var recolourOriginal: IntArray? = null
    var recolourModified: IntArray? = null
    var textures: IntArray? = null
    var colourPalette: IntArray? = null

    var modelSizeX = 128
    var modelSizeY = 128
    var modelSizeZ = 128

    var offsetX = 0
    var offsetY = 0
    var offsetZ = 0
    var offsetMultiplier = 0

    var blocksLand = false
    var blocksSky = true
    var solid = 1
    var ignoreOnRoute = false
    var mirrored = false
    var castsShadow = true
    var animateImmediately = true
    var isSolidFlag = true
    var isMembers = false
    var isWalkable = false
    var adjustMapSceneRotation = false
    var hasAnimation = false
    var invertMapScene = false

    var contouredGround: Byte = 0
    var cullingType = 1
    var brightness = 0
    var contrast = 0
    var brightnessOverride = 255
    var mapScene = -1
    var mapSceneRotation = 0
    var mapSceneId = -1
    var mapDefinitionId = -1
    var configId = 0

    var ambientSoundId = -1
    var ambientSoundMinDelay = 0
    var ambientSoundMaxDelay = 0
    var animationId = 0
    var ambientSoundVolume: Int = 0
    var ambientSoundPitch: Int = 0
    var ambientSoundRadius: Int = 0
    var ambientSoundEffectType: Int = 0

    var unknownAnimationField = -1
    var unknownField1 = -1
    var unknownInt1 = 0
    var unknownInt2 = 0
    var unknownInt3 = 0
    var unknownInt4 = 0
    var unknownInt5 = 0
    var unknownInt6 = 0
    var modelBrightness = 256
    var modelShadow = 256
    var aBoolean2961 = false
    var aBoolean2993 = false
    var transformVarbitIds: IntArray? = null
    var hideMinimap = false           // opcode 82
    var mapFunction = -1              // opcode 60
    var category = -1                 // opcode 61
    var supportItems = 0              // opcode 75
    var animationFrameCount = 0       // opcode 99
    var animationDuration = 0         // opcode 99
    var isInteractable = false        // opcode 90
    var blockFlag = 0                 // opcode 69
    var mapSceneBrightness = 0        // opcode 170
    var parameters: Map<Int, Any>? = null  // opcode 249

    var loaded = false

    fun load(stream: InputStream) {
        while (true) {
            val opcode = stream.readUnsignedByte()
            if (opcode == 0) break
            decode(opcode, stream)
        }
        loaded = true
    }

    private fun decode(opcode: Int, buffer: InputStream) {
        when (opcode) {
            1, 5 -> {
                val count = buffer.readUnsignedByte()
                if (count > 0) {
                    models = IntArray(count)
                    modelTypes = if (opcode == 1) IntArray(count) else null
                    for (i in 0 until count) {
                        models!![i] = buffer.readUnsignedShort()
                        if (opcode == 1) modelTypes!![i] = buffer.readUnsignedByte()
                    }
                }
            }

            2 -> name = buffer.readString()
            3 -> desc = buffer.readString()
            14 -> sizeX = buffer.readUnsignedByte()
            15 -> sizeY = buffer.readUnsignedByte()
            17 -> {
                blocksSky = false
                solid = 0
            }

            18 -> blockProjectile = false
            19 -> interactable = buffer.readUnsignedByte() == 1
            21 -> contouredGround = 1
            22 -> delayShading = true
            23 -> cullingType = 1
            24 -> {
                val length = buffer.readUnsignedShort()
                if (length != 65535) {
                    animations = intArrayOf(length)
                }
            }

            27 -> solid = 1
            28 -> offsetMultiplier = buffer.readUnsignedByte()
            29 -> brightness = buffer.readByte()
            in 30..34 -> actions[opcode - 30] = buffer.readString().takeIf { it.lowercase() != "hidden" }
            39 -> contrast = buffer.readUnsignedByte() * 5
            40 -> readColours(buffer)
            41 -> readTextures(buffer)
            42 -> readColourPalette(buffer)
            60 -> mapFunction = buffer.readUnsignedShort()
            61 -> category = buffer.readUnsignedShort()
            62 -> mirrored = true
            64 -> castsShadow = false
            65 -> modelSizeX = buffer.readUnsignedShort()
            66 -> modelSizeZ = buffer.readUnsignedShort()
            67 -> modelSizeY = buffer.readUnsignedShort()
            69 -> blockFlag = buffer.readUnsignedByte()
            70 -> offsetX = buffer.readUnsignedShort() shl 2
            71 -> offsetZ = buffer.readUnsignedShort() shl 2
            72 -> offsetY = buffer.readUnsignedShort() shl 2
            73 -> blocksLand = true
            74 -> ignoreOnRoute = true
            75 -> supportItems = buffer.readUnsignedByte()
            77, 92 -> readTransforms(buffer, opcode == 92)
            78 -> {
                ambientSoundId = buffer.readUnsignedShort()
                ambientSoundMinDelay = buffer.readUnsignedByte()
            }

            79 -> {
                ambientSoundMaxDelay = buffer.readUnsignedShort()
                animationId = buffer.readUnsignedShort()
                ambientSoundMinDelay = buffer.readUnsignedByte()
                val length = buffer.readUnsignedByte()
                alternateModelIds = IntArray(length)
                repeat(length) { i -> alternateModelIds!![i] = buffer.readUnsignedShort() }
            }

            81 -> {
                contouredGround = 2
                configId = buffer.readUnsignedByte() * 256
            }

            82 -> hideMinimap = true
            88 -> isSolidFlag = false
            89 -> animateImmediately = false
            90 -> isInteractable = true
            91 -> isMembers = true
            93 -> {
                contouredGround = 3
                configId = buffer.readUnsignedShort()
            }

            94 -> contouredGround = 4
            95 -> contouredGround = 5
            96 -> blocksProjectile = true
            97 -> adjustMapSceneRotation = true
            98 -> hasAnimation = true
            99 -> {
                animationFrameCount = buffer.readUnsignedByte()
                animationDuration = buffer.readUnsignedShort()
            }

            100 -> {
                unknownAnimationField = buffer.readUnsignedByte()
                unknownField1 = buffer.readUnsignedShort()
            }

            101 -> mapSceneRotation = buffer.readUnsignedByte()
            102 -> mapSceneId = buffer.readUnsignedShort()
            103 -> cullingType = 0
            104 -> brightnessOverride = buffer.readUnsignedByte()
            105 -> invertMapScene = true
            106 -> {
                val len = buffer.readUnsignedByte()
                animations = IntArray(len)
                percents = IntArray(len)
                var total = 0
                repeat(len) { i ->
                    animations!![i] = buffer.readUnsignedShort()
                    if (animations!![i] == 65535) animations!![i] = -1
                    percents!![i] = buffer.readUnsignedByte()
                    total += percents!![i]
                }
                repeat(len) { i -> percents!![i] = 65535 * percents!![i] / total }
            }

            107 -> mapDefinitionId = buffer.readUnsignedShort()
            in 150..154 -> actions[opcode - 150] = buffer.readString().takeIf { isMembers }
            160 -> {
                val len = buffer.readUnsignedByte()
                transformVarbitIds = IntArray(len)
                repeat(len) { i -> transformVarbitIds!![i] = buffer.readUnsignedShort() }
            }

            162 -> {
                contouredGround = 3
                configId = buffer.readInt()
            }

            163 -> {
                ambientSoundVolume = buffer.readByte()
                ambientSoundPitch = buffer.readByte()
                ambientSoundRadius = buffer.readByte()
                ambientSoundEffectType = buffer.readByte()
            }

            164 -> unknownInt1 = buffer.readUnsignedShort()
            165 -> unknownInt2 = buffer.readUnsignedShort()
            166 -> unknownInt3 = buffer.readUnsignedShort()
            167 -> unknownInt4 = buffer.readShort()
            168 -> aBoolean2961 = true
            169 -> aBoolean2993 = true
            170 -> mapSceneBrightness = buffer.readUnsignedSmart()
            171 -> unknownInt5 = buffer.readUnsignedSmart()
            173 -> {
                modelBrightness = buffer.readShort()
                modelShadow = buffer.readShort()
            }

            177 -> isWalkable = true
            178 -> unknownInt6 = buffer.readUnsignedByte()
            249 -> readParameters(buffer)
            else -> {
                // println("Unknown opcode $opcode for loc $id")
            }
        }
    }

    fun encode(stream: OutputStream) {
        fun write(opcode: Int, block: OutputStream.() -> Unit) {
            stream.writeByte(opcode)
            stream.block()
        }

        if (models != null) {
            if (modelTypes != null) {
                write(1) {
                    writeByte(models!!.size)
                    for (i in models!!.indices) {
                        writeShort(models!![i])
                        writeByte(modelTypes!![i])
                    }
                }
            } else {
                write(5) {
                    writeByte(models!!.size)
                    for (model in models!!) writeShort(model)
                }
            }
        }

        name?.let { write(2) { writeString(it) } }
        if (sizeX != 1) write(14) { writeByte(sizeX) }
        if (sizeY != 1) write(15) { writeByte(sizeY) }

        if (!blocksSky || solid == 0) stream.writeByte(17)
        if (!blockProjectile) stream.writeByte(18)
        if (interactable) write(19) { writeByte(1) }
        if (contouredGround != 0.toByte()) {
            when (contouredGround.toInt()) {
                1 -> write(21) {}
                2 -> write(81) { writeByte(((configId shr 8) and 0xFF)) }
                3 -> write(93) { writeUnsignedShort(configId) }
                4 -> write(94) {}
                5 -> write(95) {}
            }
        }
        if (delayShading) stream.writeByte(22)
        if (cullingType != 0) write(23) {}
        if (brightness != 0) write(29) { writeByte(brightness) }
        actions.forEachIndexed { i, a -> a?.let { write(30 + i) { writeString(it) } } }

        if (contrast != 0) write(39) { writeByte(contrast / 5) }

        recolourOriginal?.let {
            write(40) {
                writeByte(it.size)
                for (i in it.indices) {
                    writeShort(it[i])
                    writeShort(recolourModified!![i])
                }
            }
        }

        textures?.let {
            write(41) {
                writeByte(it.size / 2)
                for (i in it.indices step 2) {
                    writeShort(it[i])
                    writeShort(it[i + 1])
                }
            }
        }

        if (mapFunction != -1) write(60) { writeShort(mapFunction) }
        if (category != -1) write(61) { writeShort(category) }
        if (mirrored) stream.writeByte(62)
        if (!castsShadow) stream.writeByte(64)

        if (modelSizeX != 128) write(65) { writeShort(modelSizeX) }
        if (modelSizeZ != 128) write(66) { writeShort(modelSizeZ) }
        if (modelSizeY != 128) write(67) { writeShort(modelSizeY) }

        if (blockFlag != 0) write(69) { writeByte(blockFlag) }
        if (offsetX != 0) write(70) { writeShort(offsetX shr 2) }
        if (offsetZ != 0) write(71) { writeShort(offsetZ shr 2) }
        if (offsetY != 0) write(72) { writeShort(offsetY shr 2) }

        if (blocksLand) stream.writeByte(73)
        if (ignoreOnRoute) stream.writeByte(74)
        if (supportItems != 0) write(75) { writeByte(supportItems) }

        alternateModelIds?.let {
            write(79) {
                writeUnsignedShort(animationId)
                writeUnsignedShort(ambientSoundMaxDelay)
                writeByte(ambientSoundMinDelay)
                writeByte(it.size)
                it.forEach { id -> writeShort(id) }
            }
        }

        if (hideMinimap) stream.writeByte(82)
        if (!isSolidFlag) stream.writeByte(88)
        if (!animateImmediately) stream.writeByte(89)
        if (isInteractable) stream.writeByte(90)
        if (isMembers) stream.writeByte(91)

        if (blocksProjectile) stream.writeByte(96)
        if (adjustMapSceneRotation) stream.writeByte(97)
        if (hasAnimation) stream.writeByte(98)

        if (animationFrameCount != 0) write(99) { writeByte(animationFrameCount); writeShort(animationDuration) }
        if (unknownAnimationField != 0 || unknownField1 != 0) write(100) { writeByte(unknownAnimationField); writeShort(unknownField1) }

        if (mapSceneRotation != 0) write(101) { writeByte(mapSceneRotation) }
        if (mapSceneId != -1) write(102) { writeShort(mapSceneId) }
        if (cullingType == 0) stream.writeByte(103)
        if (brightnessOverride != 255) write(104) { writeByte(brightnessOverride) }
        if (invertMapScene) stream.writeByte(105)

        animations?.let {
            if (percents != null) {
                write(106) {
                    writeByte(it.size)
                    for (i in it.indices) {
                        writeShort(if (it[i] == -1) 65535 else it[i])
                        writeByte(percents!![i])
                    }
                }
            }
        }

        if (mapDefinitionId != -1) write(107) { writeShort(mapDefinitionId) }

        transformVarbitIds?.let {
            write(160) {
                writeByte(it.size)
                it.forEach { id -> writeShort(id) }
            }
        }

        if (ambientSoundVolume != 0 || ambientSoundPitch != 0 || ambientSoundRadius != 0 || ambientSoundEffectType != 0) {
            write(163) {
                writeByte(ambientSoundVolume)
                writeByte(ambientSoundPitch)
                writeByte(ambientSoundRadius)
                writeByte(ambientSoundEffectType)
            }
        }

        if (modelBrightness != 0 || modelShadow != 0) write(173) { writeShort(modelBrightness); writeShort(modelShadow) }

        if (isWalkable) stream.writeByte(177)
        if (unknownInt6 != 0) write(178) { writeByte(unknownInt6) }

        parameters?.let {
            write(249) {
                writeByte(it.size)
                for ((key, value) in it) {
                    if (value is String) {
                        writeByte(1)
                        writeMedium(key)
                        writeString(value)
                    } else {
                        writeByte(0)
                        writeMedium(key)
                        writeInt(value as Int)
                    }
                }
            }
        }

        stream.writeByte(0)
    }

    private fun readColours(buffer: InputStream) {
        val length = buffer.readUnsignedByte()
        recolourOriginal = IntArray(length)
        recolourModified = IntArray(length)
        repeat(length) { i ->
            recolourOriginal!![i] = buffer.readUnsignedShort()
            recolourModified!![i] = buffer.readUnsignedShort()
        }
    }

    private fun readTextures(buffer: InputStream) {
        val length = buffer.readUnsignedByte()
        textures = IntArray(length * 2)
        repeat(length) { i ->
            textures!![i * 2] = buffer.readUnsignedShort()
            textures!![i * 2 + 1] = buffer.readUnsignedShort()
        }
    }

    private fun readColourPalette(buffer: InputStream) {
        val length = buffer.readUnsignedByte()
        colourPalette = IntArray(length * 2)
        repeat(length) { i ->
            colourPalette!![i * 2] = buffer.readUnsignedShort()
            colourPalette!![i * 2 + 1] = buffer.readUnsignedShort()
        }
    }

    private fun readTransforms(buffer: InputStream, var1: Boolean) {
        configId = buffer.readUnsignedShort()
        val len = if (var1) buffer.readUnsignedShort() else buffer.readUnsignedByte()
        alternateModelIds = IntArray(len)
        repeat(len) { i -> alternateModelIds!![i] = buffer.readUnsignedShort() }
    }

    private fun readParameters(buffer: InputStream) {
        val length = buffer.readUnsignedByte()
        val params = mutableMapOf<Int, Any>()
        repeat(length) {
            val isString = buffer.readUnsignedByte() == 1
            val key = buffer.readMedium()
            val value = if (isString) buffer.readJagString() else buffer.readInt()
            params[key] = value
        }
    }


    companion object {
        fun load(cache: Store, id: Int): LocDefinition? {
            val loc = LocDefinition()
            loc.id = id
            val data: ByteArray? = cache.indexes[16].getFile(loc.getArchiveId(), loc.getFileId())
            if (data != null) {
                try {
                    loc.load(InputStream(data))
                    loc.loaded = true
                    return loc
                } catch (e: RuntimeException) {
                    e.printStackTrace()
                }
            }
            return null
        }

        fun print(cache: Store, outputFile: String) {
            try {
                val file = File(outputFile)
                file.parentFile?.takeIf { !it.exists() }?.mkdirs()
                if (!file.exists()) file.createNewFile()

                PrintWriter(BufferedWriter(FileWriter(file))).use { writer ->
                    val size = Utils.getObjectDefinitionsSize(cache)

                    for (id in 0 until size) {
                        val loc = load(cache, id) ?: continue
                        if (!loc.loaded) continue

                        val fields = loc::class.java.declaredFields
                        var wroteSomething = false

                        val buffer = StringBuilder()
                        buffer.appendLine("========== OBJECT ${loc.id} ==========")

                        for (field in fields) {
                            if (Modifier.isStatic(field.modifiers)) continue

                            field.isAccessible = true
                            val value = try { field.get(loc) } catch (e: Exception) { null }

                            if (value == null) continue
                            if (value is Int && value == 0) continue
                            if (value is Int && value == -1) continue
                            if (value is Boolean && !value) continue
                            if (value is String && value.isEmpty()) continue
                            if (value is IntArray && value.isEmpty()) continue
                            if (value is ByteArray && value.isEmpty()) continue
                            if (value is Array<*> && value.isEmpty()) continue
                            if (value is Map<*, *> && value.isEmpty()) continue

                            val valueString = when (value) {
                                is IntArray -> value.joinToString(prefix = "[", postfix = "]")
                                is Array<*> -> value.joinToString(prefix = "[", postfix = "]")
                                is ByteArray -> value.joinToString(prefix = "[", postfix = "]")
                                is Map<*, *> -> value.entries.joinToString(prefix = "{", postfix = "}") { "${it.key}=${it.value}" }
                                else -> value.toString()
                            }

                            buffer.appendLine("${field.name} = $valueString")
                            wroteSomething = true
                        }

                        if (wroteSomething) {
                            writer.println(buffer.toString())
                        }
                    }
                }

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun getArchiveId(): Int = this.id shr 8
    private fun getFileId(): Int = this.id and 0xFF
}