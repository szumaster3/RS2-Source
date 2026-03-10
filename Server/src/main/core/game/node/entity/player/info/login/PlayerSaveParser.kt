package core.game.node.entity.player.info.login

import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import core.ServerConstants
import core.Util
import core.api.PersistPlayer
import core.api.log
import core.game.node.entity.combat.graves.GraveController
import core.game.node.entity.combat.graves.GraveType
import core.game.node.entity.combat.spell.CombatSpell
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.SpellBookManager
import core.game.node.entity.player.link.emote.Emotes
import core.game.node.entity.player.link.music.MusicEntry
import core.game.world.GameWorld
import core.game.world.map.Location
import core.tools.Log
import java.io.File
import java.io.FileReader
import java.sql.Timestamp
import java.util.*

class PlayerSaveParser(val player: Player) {
    companion object {
        val contentHooks = ArrayList<PersistPlayer>()
    }

    var reader: FileReader? = null
    var saveFile: JsonObject? = null
    var read = true

    fun parse() {
        val file = File("${ServerConstants.PLAYER_SAVE_PATH}${player.name}.json")
        if (!file.exists()) {
            player.details.saveParsed = true
            read = false
            return
        }
        runCatching {
            FileReader(file).use { saveFile = JsonParser.parseReader(it).asJsonObject }
            parseData()
            read = true
            player.details.saveParsed = true
        }.onFailure {
            log(this::class.java, Log.ERR, "Error parsing save file for ${player.name}: ${it.message}")
            read = false
            player.details.saveParsed = false
        }
    }

    fun parseData() {
        parseCore()
        parseAttributes()
        parseSkills()
        parseSettings()
        parseQuests()
        parseAppearance()
        parseGrave()
        parseVarps()
        parseStates()
        parseSpellbook()
        parseSavedData()
        parseAutocastSpell()
        parseFarming()
        parseConfigs()
        parseMusic()
        parseFamiliars()
        parseBankPin()
        parseHouse()
        parseEmoteManager()
        parseStatistics()
        parseAchievements()
        parsePouches()
        parseHeadgear()
        parseBoltPouch()
        parseCostumeRoom()
        parseVersion()
    }

    fun runContentHooks() {
        if (read)
            contentHooks.forEach { it.parsePlayer(player, saveFile!!) }
        player.details.saveParsed = true
    }

    fun parseVarps() {
        if (saveFile!!.has("varps"))
            player.varpManager.parse(saveFile!!.getAsJsonArray("varps"))
    }

    fun parseAttributes() {
        val attrData = saveFile?.getAsJsonArray("attributes") ?: run {
            player.gameAttributes.parse(player.name + ".xml")
            return
        }

        for (elem in attrData) {
            val attr = elem.asJsonObject
            val key = attr.get("key")?.asString ?: continue
            val type = attr.get("type")?.asString ?: continue
            val isExpirable = attr.get("expirable")?.asBoolean ?: false

            if (isExpirable) {
                val expireTime = attr.get("expiration-time")?.asLong ?: 0L
                if (expireTime < System.currentTimeMillis()) continue
                player.gameAttributes.keyExpirations[key] = expireTime
            }

            player.gameAttributes.savedAttributes.add(key)

            player.gameAttributes.attributes[key] = when (type) {
                "int" -> attr.get("value")?.asInt
                "str" -> attr.get("value")?.asString
                "short" -> attr.get("value")?.asShort
                "long" -> attr.get("value")?.asLong
                "bool" -> attr.get("value")?.asBoolean
                "byte" -> Base64.getDecoder().decode(attr.get("value")?.asString ?: "")?.get(0)
                "location" -> Location.fromString(attr.get("value")?.asString ?: "")
                "time" -> Timestamp.valueOf(attr.get("value")?.asString ?: "")
                else -> {
                    log(this::class.java, Log.WARN, "Invalid data type for key: $key in PlayerSaveParser.kt")
                    null
                }
            }
        }
    }

    fun parseSkills() {
        val save = saveFile ?: return

        val skillData = save.getAsJsonArray("skills")
        player.skills.parse(skillData)

        player.skills.experienceGained =
            save.get("totalEXP")?.asDouble ?: 0.0

        val defaultRate = GameWorld.settings?.default_xp_rate ?: 1.0

        player.skills.experienceMultiplier =
            save.get("exp_multiplier")?.asDouble ?: defaultRate

        if (save.has("milestone")) {
            val milestone = save.getAsJsonObject("milestone")
            player.skills.combatMilestone =
                milestone.get("combatMilestone")?.asInt ?: 0
            player.skills.skillMilestone =
                milestone.get("skillMilestone")?.asInt ?: 0
        }
    }


    fun parseSettings() {
        val settingsData = saveFile?.getAsJsonObject("settings") ?: return
        player.settings.parse(settingsData)
    }

    fun parseQuests() {
        val questData = saveFile?.getAsJsonObject("quests") ?: return
        player.questRepository.parse(questData)
    }

    fun parseAppearance() {
        val appearanceData = saveFile?.getAsJsonObject("appearance") ?: return
        player.appearance.parse(appearanceData)
    }

    fun parseGrave() {
        val graveData = saveFile?.get("grave_type")?.asString?.toIntOrNull() ?: return
        val type = GraveType.values()[graveData]
        GraveController.updateGraveType(player, type)
    }

    fun parseSpellbook() {
        val spellbookData = saveFile?.get("spellbook")?.asString?.toIntOrNull() ?: return
        SpellBookManager.SpellBook.forInterface(spellbookData)?.let { player.spellBookManager.setSpellBook(it) }
    }

    fun parseSavedData() {
        val activityData = saveFile?.getAsJsonObject("activityData") ?: return
        val questData = saveFile?.getAsJsonObject("questData") ?: return
        val globalData = saveFile?.getAsJsonObject("globalData") ?: return
        player.savedData.activityData.parse(activityData)
        player.savedData.questData.parse(questData)
        player.savedData.globalData.parse(globalData)
    }

    fun parseAutocastSpell() {
        val autocast = saveFile?.getAsJsonObject("autocastSpell") ?: return
        val book = autocast.get("book")?.asString?.toIntOrNull() ?: return
        val spellId = autocast.get("spellId")?.asString?.toIntOrNull() ?: return
        player.properties.autocastSpell = SpellBookManager.SpellBook.values()[book].getSpell(spellId) as CombatSpell
    }

    fun parseFarming() {
        /*
        val farmingData = saveFile?.getAsJsonObject("farming") ?: return

        // Equipment
        farmingData.getAsJsonArray("equipment")?.let { equipmentData ->
            player.farmingManager.equipment.container.parse(equipmentData)
        }

        // Compost bins
        farmingData.getAsJsonArray("bins")?.let { compostData ->
            player.farmingManager.compostManager.parse(compostData)
        }

        // Wrappers
        farmingData.getAsJsonArray("wrappers")?.let { wrapperData ->
            player.farmingManager.parseWrappers(wrapperData)
        }

        // Seedlings
        farmingData.getAsJsonArray("seedlings")?.let { seedlingData ->
            player.farmingManager.seedlingManager.parse(seedlingData)
        }

        // Farming amulet wrapper
        farmingData.get("farmingAmuletWrapperID")?.let { wrapperId ->
            val id = wrapperId.asInt
            player.farmingManager.amuletBoundWrapper = player.farmingManager.getPatchWrapper(id)
        }
        */
    }


    fun parseConfigs() {
        val configs = saveFile?.getAsJsonArray("configs") ?: return
        for (config in configs) {
            val c = config.asJsonObject
            val index = c.get("index")?.asString?.toIntOrNull() ?: continue
            if (index == 1048) continue
            val value = c.get("value")?.asString?.toIntOrNull() ?: continue
            player.varpMap[index] = value
        }
    }

    fun parseMusic() {
        val unlockedSongs = saveFile?.getAsJsonArray("unlockedMusic") ?: return
        for (song in unlockedSongs) {
            val s = song.asString.toIntOrNull() ?: continue
            val entry = MusicEntry.forId(s) ?: continue
            player.musicPlayer.unlocked[entry.index] = entry
        }
    }

    fun parseStates() {
        player.states.clear()
        if (saveFile != null && saveFile!!.has("states")) {
            val states: JsonArray = saveFile!!.getAsJsonArray("states")
            for (element in states) {
                val s: JsonObject = element.asJsonObject
                val stateId = s.get("stateKey").asString
                if (player.states[stateId] != null) continue
                val stateClass = player.registerState(stateId)
                stateClass?.parse(s)
                stateClass?.init()
                player.states[stateId] = stateClass
            }
        }
    }

    fun parseFamiliars() {
        val familiarData = saveFile?.getAsJsonObject("familiarManager") ?: return
        player.familiarManager.parse(familiarData)
    }

    fun parseBankPin() {
        val bpData = saveFile?.getAsJsonObject("bankPinManager") ?: return
        player.bankPinManager.parse(bpData)
    }

    fun parseHouse() {
        val houseData = saveFile?.getAsJsonObject("houseData") ?: return
        player.houseManager.parse(houseData)
    }

    fun parseEmoteManager() {
        val emoteData = saveFile?.getAsJsonArray("emoteData") ?: return
        for (emote in emoteData) {
            val eIndex = emote.asString.toIntOrNull() ?: continue
            val e = Emotes.values().getOrNull(eIndex) ?: continue
            if (!player.emoteManager.emotes.contains(e)) {
                player.emoteManager.emotes.add(e)
            }
        }
    }

    fun parseStatistics() {
        val stats = saveFile?.getAsJsonArray("statistics") ?: return
        for (stat in stats) {
            val s = stat.asJsonObject
            val index = s.get("index")?.asString?.toIntOrNull() ?: continue
            val value = s.get("value")?.asString?.toIntOrNull() ?: continue
        }
    }

    fun parseAchievements() {
        val achvData = saveFile?.getAsJsonArray("achievementDiaries")
        if (achvData != null) {
            player.achievementDiaryManager.parse(achvData)
        } else {
            player.achievementDiaryManager.resetRewards()
        }
    }

    fun parsePouches() {
        val pouchData = saveFile?.getAsJsonArray("pouches") ?: return
        player.pouchManager.parse(pouchData)
    }

    fun parseHeadgear() {
        val headgearData = saveFile?.getAsJsonArray("enchantedHeadgear") ?: return
        player.enchgearManager.parse(headgearData)
    }

    fun parseBoltPouch() {
        val boltPouchData = saveFile?.getAsJsonArray("boltPouch") ?: return
        player.boltPouchManager.parse(boltPouchData)
    }

    fun parseCostumeRoom() {
        val costumeRaw = saveFile?.get("costumeRoom") ?: return
        val costumeJson = costumeRaw.asJsonObject ?: return
        player.getCostumeRoomState().readJson(costumeJson)
    }

    fun parseVersion() {
        player.version = 0
        player.version = saveFile?.get("version")?.asString?.toIntOrNull() ?: 0
    }

    fun parseCore() {
        val coreData = saveFile?.getAsJsonObject("core_data") ?: return

        val inventory = coreData.getAsJsonArray("inventory")
        val bank = coreData.getAsJsonArray("bank")
        val equipment = coreData.getAsJsonArray("equipment")
        val bBars = coreData.getAsJsonArray("blastBars")
        val bOre = coreData.getAsJsonArray("blastOre")
        val bCoal = coreData.getAsJsonArray("blastCoal")
        val varpData = coreData.getAsJsonArray("varp")
        val timerData = coreData.getAsJsonObject("timers")
        val location = coreData.get("location")?.asString ?: ""

        val bankTabData = coreData.getAsJsonArray("bankTabs")
        if (bankTabData != null) {
            for (i in bankTabData) {
                val tab = i.asJsonObject
                val index = tab.get("index")?.asString?.toIntOrNull() ?: continue
                val startSlot = tab.get("startSlot")?.asString?.toIntOrNull() ?: continue
                player.bank.tabStartSlot[index] = startSlot
            }
        }

        player.inventory.parse(inventory)
        player.bank.parse(bank)
        player.equipment.parse(equipment)
        bBars?.let { player.blastBars.parse(it) }
        bOre?.let { player.blastOre.parse(it) }
        bCoal?.let { player.blastCoal.parse(it) }
        player.location = Util.parseLocation(location)

        if (varpData != null) {
            for (e in varpData) {
                val varp = e.asJsonObject
                val index = varp.get("index")?.asString?.toIntOrNull() ?: continue
                val value = varp.get("value")?.asString?.toIntOrNull() ?: continue
                player.varpMap[index] = value
                player.saveVarp[index] = true
            }
        }

        timerData?.let { player.timers.parseTimers(it) }
    }
}
