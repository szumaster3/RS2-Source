package content.global.activity.penguinhns

import com.google.gson.JsonArray
import com.google.gson.JsonObject
import core.api.log
import core.cache.def.impl.NPCDefinition
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.world.map.Location
import core.tools.Log

/**
 * Manages the Penguin Hunter activity state.
 */

class PenguinManager {
    companion object {
        var penguins: MutableList<Int> = mutableListOf()
        var npcs = mutableListOf<NPC>()
        val spawner = PenguinSpawner()

        var tagMapping: MutableMap<Int, JsonArray> = mutableMapOf()

        fun registerTag(player: Player, location: Location) {
            val penguin = PenguinLocation.forLocation(location) ?: return
            val ordinal = penguin.ordinal

            val list = tagMapping[ordinal] ?: JsonArray()

            list.add(player.username.lowercase())
            tagMapping[ordinal] = list
            updateStoreFile()
        }

        fun hasTagged(player: Player, location: Location): Boolean {
            val ordinal = PenguinLocation.forLocation(location)?.ordinal
            return ordinal != null && tagMapping[ordinal]?.any { it.asString == player.username.lowercase() } == true
        }

        private fun updateStoreFile() {
            val jsonTags = JsonArray()
            tagMapping.filter { it.value.size() > 0 }.forEach { (ordinal, taggers) ->
                log(this::class.java, Log.FINE, "$ordinal - ${taggers[0].asString}")

                val tag = JsonObject()
                tag.addProperty("ordinal", ordinal)
                tag.add("taggers", taggers)
                jsonTags.add(tag)
            }

            PenguinHNSEvent.getStoreFile().add("tag-mapping", jsonTags)
        }
    }

    fun rebuildVars() {
        val store = PenguinHNSEvent.getStoreFile()
        if (!store.has("spawned-penguins")) {
            penguins = spawner.spawnPenguins(10)
            val ja = JsonArray()
            penguins.forEach { ja.add(it) }
            store.add("spawned-penguins", ja)

            tagMapping.clear()
            for (p in penguins) {
                tagMapping[p] = JsonArray()
            }
            updateStoreFile()
        } else {
            val spawnedOrdinals = store.getAsJsonArray("spawned-penguins").map { it.asInt }.toMutableList()
            spawner.spawnPenguins(spawnedOrdinals)

            val storedTags = store.getAsJsonArray("tag-mapping")?.associate { jRaw ->
                val jObj = jRaw.asJsonObject
                jObj.get("ordinal").asInt to jObj.getAsJsonArray("taggers")
            }?.toMutableMap() ?: mutableMapOf()

            tagMapping = storedTags
            penguins = spawnedOrdinals
        }
    }

    /*
     * fun hasFoundAgent(player: Player): Boolean {
     *     val username = player.username.lowercase()
     *
     *     return PenguinLocation.values()
     *         .filter { it.isAgent }
     *         .any { loc ->
     *             val ordinal = loc.ordinal
     *             tagMapping[ordinal]?.any { it.asString == username } == true
     *         }
     * }
     */
}