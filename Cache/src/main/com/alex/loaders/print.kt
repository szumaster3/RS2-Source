package com.alex.loaders

import com.alex.store.Store
import java.io.File

fun print(cache: Store, outputFile: String) {
    val defs = BasDefinition.load(cache) ?: return
    val file = File(outputFile)
    file.printWriter().use { writer ->
        for ((id, bas) in defs.withIndex()) {
            if (!bas.isLoaded) continue
            writer.println("[bas_$id]")
            if (bas.anInt1037 != -1) {
                writer.println("readyanim_l=seq_${bas.anInt1037}")
                writer.println("readyanim_r=seq_${bas.anInt1037}")
            }
            if (bas.anInt1051 != -1) {
                writer.println("walkanim_b=seq_${bas.anInt1051}")
                writer.println("walkanim_l=seq_${bas.anInt1051}")
                writer.println("walkanim_r=seq_${bas.anInt1051}")
            }
            if (bas.anInt1063 != 0) writer.println("runanim=seq_${bas.anInt1063}")
            if (bas.anInt1064 != 0) writer.println("runanim_b=seq_${bas.anInt1064}")
            if (bas.anInt1065 != 0) writer.println("runanim_l=seq_${bas.anInt1065}")
            if (bas.anInt1066 != -1) writer.println("runanim_r=seq_${bas.anInt1066}")
            val ready1 = if (bas.anInt1037 != -1) bas.anInt1037 else 0
            val ready2 = if (bas.anInt1051 != -1) bas.anInt1051 else 0
            writer.println("readyanim=seq_$ready1,seq_$ready2")

            writer.println()
        }
    }
}