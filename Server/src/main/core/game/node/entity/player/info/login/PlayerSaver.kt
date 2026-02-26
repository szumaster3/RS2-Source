package core.game.node.entity.player.info.login

import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import content.global.skill.summoning.familiar.BurdenBeast
import content.global.skill.summoning.pets.Pet
import core.ServerConstants
import core.api.PersistPlayer
import core.api.log
import core.game.container.Container
import core.game.node.entity.player.Player
import core.game.node.entity.skill.Skills
import core.game.world.map.Location
import core.tools.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileWriter
import java.io.IOException
import java.lang.Math.ceil
import java.util.*

class PlayerSaver(val player: Player) {

    companion object {
        val contentHooks = ArrayList<PersistPlayer>()
    }

    fun populate(): JsonObject {
        val saveFile = JsonObject()
        saveCoreData(saveFile)
        saveSkills(saveFile)
        saveSettings(saveFile)
        saveQuests(saveFile)
        saveAppearance(saveFile)
        saveSpellbook(saveFile)
        saveSavedData(saveFile)
        saveAutocast(saveFile)
        saveMusicPlayer(saveFile)
        saveFamiliarManager(saveFile)
        saveStateManager(saveFile)
        saveBankPinData(saveFile)
        saveHouseData(saveFile)
        saveAchievementData(saveFile)
        saveEmoteData(saveFile)
        saveStatManager(saveFile)
        saveAttributes(saveFile)
        savePouches(saveFile)
        saveHeadgear(saveFile)
        saveBoltPouch(saveFile)
        saveCostumeRoom(saveFile)
        saveVersion(saveFile)
        contentHooks.forEach { it.savePlayer(player, saveFile) }
        return saveFile
    }

    fun save() = runBlocking {
        if (!player.details.saveParsed) return@runBlocking
        val json = Gson().toJson(populate())
        val saveFile = File("${ServerConstants.PLAYER_SAVE_PATH}${player.name}.json")
        try {
            saveFile.parentFile?.mkdirs()
            if (!saveFile.exists()) saveFile.createNewFile()

            val content = saveFile.readText()
            if (content.isBlank() || content == "{}") {
                withContext(Dispatchers.IO) { FileWriter(saveFile).use { it.write(json) } }
            } else {
                withContext(Dispatchers.IO) { FileWriter(saveFile).use { it.write(json) } }
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    fun savePouches(root: JsonObject) {
        player.pouchManager.save(root)
    }

    fun saveHeadgear(root: JsonObject) {
        player.enchgearManager.save(root)
    }

    fun saveBoltPouch(root: JsonObject) {
        player.boltPouchManager.save(root)
    }

    fun saveAttributes(root: JsonObject) {
        if (player.gameAttributes.savedAttributes.isNotEmpty()) {
            val attrs = JsonArray()
            for (key in player.gameAttributes.savedAttributes) {
                val value = player.gameAttributes.attributes[key] ?: continue
                val isExpirable = player.gameAttributes.keyExpirations.containsKey(key)
                val attr = JsonObject()
                val type = when (value) {
                    is Int -> "int"
                    is Boolean -> "bool"
                    is Long -> "long"
                    is Short -> "short"
                    is String -> "str"
                    is Byte -> "byte"
                    is Location -> "location"
                    else -> {
                        log(
                            this::class.java,
                            Log.WARN,
                            "Invalid attribute type for key: $key in PlayerSaver.kt Line 115",
                        )
                        "null"
                    }
                }
                attr.addProperty("type", type)
                attr.addProperty("key", key)
                if (value is Byte) {
                    val asString = Base64.getEncoder().encodeToString(byteArrayOf(value))
                    attr.addProperty("value", asString)
                } else {
                    if (value is Boolean) {
                        attr.addProperty("value", value)
                    } else {
                        attr.addProperty("value", value.toString())
                    }
                }
                if (isExpirable) {
                    attr.addProperty("expirable", true)
                    attr.addProperty("expiration-time", player.gameAttributes.keyExpirations[key].toString())
                }
                attrs.add(attr)
            }
            root.add("attributes", attrs)
        }
    }

    fun saveStatManager(root: JsonObject) {
        val statistics = JsonArray()
        var index = 0
        root.add("statistics", statistics)
    }

    fun saveEmoteData(root: JsonObject) {
        if (player.emoteManager.isSaveRequired) {
            val emoteData = JsonArray()
            player.emoteManager.emotes.forEach {
                emoteData.add(it.ordinal.toString())
            }
            root.add("emoteData", emoteData)
        }
    }

    /**
     * Saves the player's achievement diary.
     */
    fun saveAchievementData(root: JsonObject) {
        val achievementData = JsonArray()

        player.achievementDiaryManager.diaries.forEach { diaryEntry ->
            val diary = JsonObject()

            val startedLevels = JsonArray()
            diaryEntry.levelStarted.forEach { startedLevels.add(it) }
            diary.add("startedLevels", startedLevels)

            val completedLevels = JsonArray()
            diaryEntry.taskCompleted.forEach { task ->
                val level = JsonArray()
                task.forEach { level.add(it) }
                completedLevels.add(level)
            }
            diary.add("completedLevels", completedLevels)

            val rewardedLevels = JsonArray()
            diaryEntry.levelRewarded.forEach { rewardedLevels.add(it) }
            diary.add("rewardedLevels", rewardedLevels)

            val diaryCollector = JsonObject()
            diaryCollector.add(diaryEntry.type.name, diary)
            achievementData.add(diaryCollector)
        }

        root.add("achievementDiaries", achievementData)
    }

    fun saveHouseData(root: JsonObject) {
        val manager = player.houseManager
        val houseData = JsonObject()
        houseData.addProperty("location", manager.location.ordinal.toString())
        houseData.addProperty("style", manager.style.ordinal.toString())
        if (manager.hasServant()) {
            val servant = JsonObject()
            servant.addProperty("type", manager.servant.type.ordinal.toString())
            servant.addProperty("uses", manager.servant.uses.toString())
            manager.servant.item?.let {
                val item = JsonObject()
                item.addProperty("id", it.id.toString())
                item.addProperty("amount", it.amount.toString())
                servant.add("item", item)
            }
            servant.addProperty("greet", manager.servant.isGreet)
            houseData.add("servant", servant)
        }
        val rooms = JsonArray()
        var z = 0
        for (room in manager.rooms) {
            var x = 0
            for (xr in room) {
                var y = 0
                for (yr in xr) {
                    if (yr != null) {
                        val r = JsonObject()
                        r.addProperty("z", z.toString())
                        r.addProperty("x", x.toString())
                        r.addProperty("y", y.toString())
                        r.addProperty("properties", yr.properties.ordinal.toString())
                        r.addProperty("rotation", yr.rotation.toInteger().toString())
                        val hotspots = JsonArray()
                        var hotspotIndex = 0
                        yr.hotspots.forEach {
                            if (it.decorationIndex > -1) {
                                val hotspot = JsonObject()
                                hotspot.addProperty("hotspotIndex", hotspotIndex.toString())
                                hotspot.addProperty("decorationIndex", it.decorationIndex.toString())
                                hotspots.add(hotspot)
                            }
                            hotspotIndex++
                        }
                        r.add("hotspots", hotspots)
                        rooms.add(r)
                    }
                    y++
                }
                x++
            }
            z++
        }
        houseData.add("rooms", rooms)
        root.add("houseData", houseData)
    }

    fun saveBankPinData(root: JsonObject) {
        val bankPinManager = JsonObject()
        if (player.bankPinManager.hasPin()) {
            bankPinManager.addProperty("pin", player.bankPinManager.pin.toString())
        }
        bankPinManager.addProperty("longRecovery", player.bankPinManager.isLongRecovery)
        if (player.bankPinManager.status.ordinal != 0) {
            bankPinManager.addProperty("status", player.bankPinManager.status.ordinal.toString())
        }
        if (player.bankPinManager.pendingDelay != -1L && player.bankPinManager.pendingDelay > System.currentTimeMillis()) {
            bankPinManager.addProperty("pendingDelay", player.bankPinManager.pendingDelay.toString())
        }
        if (player.bankPinManager.tryDelay > System.currentTimeMillis()) {
            bankPinManager.addProperty("tryDelay", player.bankPinManager.tryDelay.toString())
        }
        root.add("bankPinManager", bankPinManager)
    }

    fun saveCostumeRoom(root: JsonObject) {
        root.add("costumeRoom", player.getCostumeRoomState().toJson())
    }

    fun saveStateManager(root: JsonObject) {
        val states = JsonArray()
        player.states.forEach { (key, clazz) ->
            if (clazz != null && clazz.pulse != null) {
                val stateObj = JsonObject()
                stateObj.addProperty("stateKey", key)
                clazz.save(stateObj)
                states.add(stateObj)
            }
        }
        root.add("states", states)
    }

    fun saveFamiliarManager(root: JsonObject) {
        val familiarManager = JsonObject()
        val petDetails = JsonArray()
        player.familiarManager.petDetails.forEach { (key, value) ->
            val detail = JsonObject()
            detail.addProperty("petId", key.toString())
            detail.addProperty("hunger", value.hunger.toString())
            detail.addProperty("growth", value.growth.toString())
            petDetails.add(detail)
        }
        familiarManager.add("petDetails", petDetails)
        if (player.familiarManager.hasPet()) {
            familiarManager.addProperty("currentPet", (player.familiarManager.familiar as Pet).getItemIdHash().toString())
        } else if (player.familiarManager.hasFamiliar()) {
            val familiar = JsonObject()
            familiar.addProperty("originalId", player.familiarManager.familiar.originalId.toString())
            familiar.addProperty("ticks", player.familiarManager.familiar.ticks.toString())
            familiar.addProperty("specialPoints", player.familiarManager.familiar.specialPoints.toString())
            if (player.familiarManager.familiar.isBurdenBeast && !(player.familiarManager.familiar as BurdenBeast).container.isEmpty) {
                val familiarInventory = saveContainer((player.familiarManager.familiar as BurdenBeast).container)
                familiar.add("inventory", familiarInventory)
            }
            familiar.addProperty("lifepoints", player.familiarManager.familiar.skills.lifepoints)
            familiarManager.add("familiar", familiar)
        }
        root.add("familiarManager", familiarManager)
    }

    fun saveMusicPlayer(root: JsonObject) {
        val unlockedMusic = JsonArray()
        player.musicPlayer.unlocked.values.forEach {
            unlockedMusic.add(it.id.toString())
        }
        root.add("unlockedMusic", unlockedMusic)
    }

    fun saveAutocast(root: JsonObject) {
        player.properties.autocastSpell ?: return
        val spell = JsonObject()
        spell.addProperty("book", player.properties.autocastSpell!!.book.ordinal.toString())
        spell.addProperty("spellId", player.properties.autocastSpell!!.spellId.toString())
        root.add("autocastSpell", spell)
    }


    fun saveSavedData(root: JsonObject) {
        saveActivityData(root)
        saveQuestData(root)
        saveGlobalData(root)
    }

    fun saveGlobalData(root: JsonObject) {
        val globalData = JsonObject()
        globalData.addProperty("tutorialStage", player.savedData.globalData.getTutorialStage().toString())
        globalData.addProperty("homeTeleportDelay", player.savedData.globalData.getHomeTeleportDelay().toString())
        globalData.addProperty("lumbridgeRope", player.savedData.globalData.hasTiedLumbridgeRope())
        globalData.addProperty("apprentice", player.savedData.globalData.hasSpokenToApprentice())
        globalData.addProperty("assistTime", player.savedData.globalData.getAssistTime().toString())

        val assistExperience = JsonArray()
        player.savedData.globalData.getAssistExperience().forEach { assistExperience.add(it.toString()) }
        globalData.add("assistExperience", assistExperience)

        val strongholdRewards = JsonArray()
        player.savedData.globalData.getStrongHoldRewards().forEach { strongholdRewards.add(it) }
        globalData.add("strongHoldRewards", strongholdRewards)

        globalData.addProperty("chatPing", player.savedData.globalData.getChatPing().toString())
        globalData.addProperty("tutorClaim", player.savedData.globalData.getTutorClaim().toString())
        globalData.addProperty("luthasTask", player.savedData.globalData.isLuthasTask())
        globalData.addProperty("karamjaBananas", player.savedData.globalData.getKaramjaBananas().toString())

        val stallObjects = JsonObject()
        player.savedData.globalData.stallSteal.forEach { (stall, time) ->
            stallObjects.addProperty(stall, time)
        }

        globalData.add("stallSteal", stallObjects)

        globalData.addProperty("zafAmount", player.savedData.globalData.getZaffAmount().toString())
        globalData.addProperty("zafTime", player.savedData.globalData.getZaffTime().toString())
        globalData.addProperty("fritzGlass", player.savedData.globalData.isFritzGlass())
        globalData.addProperty("wydinEmployee", player.savedData.globalData.isWydinEmployee())
        globalData.addProperty("draynorRecording", player.savedData.globalData.isDraynorRecording())
        globalData.addProperty("geTutorial", player.savedData.globalData.isGeTutorial())
        globalData.addProperty("essenceTeleporter", player.savedData.globalData.getEssenceTeleporter().toString())
        globalData.addProperty("recoilDamage", player.savedData.globalData.getRecoilDamage().toString())
        globalData.addProperty("doubleExpDelay", player.savedData.globalData.getDoubleExpDelay().toString())
        globalData.addProperty("joinedMonastery", player.savedData.globalData.isJoinedMonastery())

        val readPlaques = JsonArray()
        player.savedData.globalData.readPlaques.forEach { readPlaques.add(it) }
        globalData.add("readPlaques", readPlaques)

        globalData.addProperty("forgingUses", player.savedData.globalData.getForgingUses().toString())
        globalData.addProperty("ectoCharges", player.savedData.globalData.getEctoCharges().toString())
        globalData.addProperty("dropDelay", player.savedData.globalData.getDropDelay().toString())

        val abyssData = JsonArray()
        player.savedData.globalData.getAbyssData().forEach { abyssData.add(it) }
        globalData.add("abyssData", abyssData)

        val rcDecays = JsonArray()
        player.savedData.globalData.getRcDecays().forEach { rcDecays.add(it.toString()) }
        globalData.add("rcDecays", rcDecays)

        globalData.addProperty("disableDeathScreen", player.savedData.globalData.isDeathScreenDisabled())
        globalData.addProperty("playerTestStage", player.savedData.globalData.getTestStage().toString())
        globalData.addProperty("charmingDelay", player.savedData.globalData.getCharmingDelay().toString())

        val travelLogs = JsonArray()
        player.savedData.globalData.getTravelLogs().forEach { travelLogs.add(it) }
        globalData.add("travelLogs", travelLogs)

        val godBooks = JsonArray()
        player.savedData.globalData.getGodBooks().forEach { godBooks.add(it) }
        globalData.add("godBooks", godBooks)

        globalData.addProperty("disableNews", player.savedData.globalData.isDisableNews())

        val godPages = JsonArray()
        player.savedData.globalData.godPages.forEach { godPages.add(it) }
        globalData.add("godPages", godPages)

        globalData.addProperty("overChargeDelay", player.savedData.globalData.getOverChargeDelay().toString())

        val bossCounters = JsonArray()
        player.savedData.globalData.getBossCounters().forEach { bossCounters.add(it.toString()) }
        globalData.add("bossCounters", bossCounters)

        globalData.addProperty("barrowsLoots", player.savedData.globalData.getBarrowsLoots().toString())
        globalData.addProperty("lootSharePoints", player.savedData.globalData.getLootSharePoints().toString())
        globalData.addProperty("lootShareDelay", player.savedData.globalData.getLootShareDelay().toString())
        globalData.addProperty("doubleExp", player.savedData.globalData.getDoubleExp().toString())
        globalData.addProperty("globalTeleporterDelay", player.savedData.globalData.getGlobalTeleporterDelay().toString())
        globalData.addProperty("starSpriteDelay", player.savedData.globalData.starSpriteDelay.toString())
        globalData.addProperty("runReplenishDelay", player.savedData.globalData.getRunReplenishDelay().toString())
        globalData.addProperty("runReplenishCharges", player.savedData.globalData.getRunReplenishCharges().toString())
        globalData.addProperty("lowAlchemyCharges", player.savedData.globalData.getLowAlchemyCharges().toString())
        globalData.addProperty("lowAlchemyDelay", player.savedData.globalData.getLowAlchemyDelay().toString())
        globalData.addProperty("magicSkillCapeDelay", player.savedData.globalData.getMagicSkillCapeDelay().toString())
        globalData.addProperty("hunterCapeDelay", player.savedData.globalData.getHunterCapeDelay().toString())
        globalData.addProperty("hunterCapeCharges", player.savedData.globalData.getHunterCapeCharges().toString())
        globalData.addProperty("taskAmount", player.savedData.globalData.getTaskAmount().toString())
        globalData.addProperty("taskPoints", player.savedData.globalData.getTaskPoints().toString())
        globalData.addProperty("macroDisabled", player.savedData.globalData.getMacroDisabled())

        root.add("globalData", globalData)
    }

    fun saveQuestData(root: JsonObject) {
        val questData = JsonObject()
        val draynorLever = JsonArray()
        player.savedData.questData.draynorLever.forEach { draynorLever.add(it) }
        questData.add("draynorLever", draynorLever)

        val dslayer = JsonArray()
        player.savedData.questData.dragonSlayer.forEach { dslayer.add(it) }
        questData.add("dragonSlayer", dslayer)

        questData.addProperty("dragonSlayerPlanks", player.savedData.questData.dragonSlayerPlanks.toString())

        val demonSlayer = JsonArray()
        player.savedData.questData.demonSlayer.forEach { demonSlayer.add(it) }
        questData.add("demonSlayer", demonSlayer)

        val cooksAssistant = JsonArray()
        player.savedData.questData.cooksAssistant.forEach { cooksAssistant.add(it) }
        questData.add("cooksAssistant", cooksAssistant)

        questData.addProperty("gardenerAttack", player.savedData.questData.isGardenerAttack)
        questData.addProperty("talkedDrezel", player.savedData.questData.isTalkedDrezel)

        val desertTreasureNode = JsonArray()
        player.savedData.questData.desertTreasure.forEach {
            val item = JsonObject()
            item.addProperty("id", it.id.toString())
            item.addProperty("amount", it.amount.toString())
            desertTreasureNode.add(item)
        }
        questData.add("desertTreasureNode", desertTreasureNode)

        questData.addProperty("witchsExperimentStage", player.savedData.questData.witchsExperimentStage.toString())
        questData.addProperty("witchsExperimentKilled", player.savedData.questData.isWitchsExperimentKilled)

        root.add("questData", questData)
    }

    fun saveActivityData(root: JsonObject) {
        val activityData = JsonObject()
        activityData.addProperty("pestPoints", player.savedData.activityData.pestPoints.toString())
        activityData.addProperty("warriorGuildTokens", player.savedData.activityData.warriorGuildTokens.toString())
        activityData.addProperty("bountyHunterRate", player.savedData.activityData.bountyHunterRate.toString())
        activityData.addProperty("bountyRogueRate", player.savedData.activityData.bountyRogueRate.toString())
        activityData.addProperty("barrowKills", player.savedData.activityData.barrowKills.toString())

        val barrowBrothers = JsonArray()
        player.savedData.activityData.barrowBrothers.forEach { barrowBrothers.add(it) }
        activityData.add("barrowBrothers", barrowBrothers)

        activityData.addProperty("barrowTunnelIndex", player.savedData.activityData.barrowTunnelIndex.toString())
        activityData.addProperty("kolodionStage", player.savedData.activityData.kolodionStage.toString())

        val godCasts = JsonArray()
        player.savedData.activityData.godCasts.forEach { godCasts.add(it.toString()) }
        activityData.add("godCasts", godCasts)

        activityData.addProperty("kolodionBoss", player.savedData.activityData.kolodionBoss.toString())
        activityData.addProperty("elnockSupplies", player.savedData.activityData.isElnockSupplies)
        activityData.addProperty("lastBorkBattle", player.savedData.activityData.lastBorkBattle.toString())
        activityData.addProperty("startedMta", player.savedData.activityData.isStartedMta)
        activityData.addProperty("lostCannon", player.savedData.activityData.isLostCannon)
        activityData.addProperty("bonesToPeaches", player.savedData.activityData.isBonesToPeaches)
        activityData.addProperty("solvedMazes", player.savedData.activityData.solvedMazes.toString())
        activityData.addProperty("fogRating", player.savedData.activityData.fogRating.toString())
        activityData.addProperty("borkKills", player.savedData.activityData.borkKills.toString())
        activityData.addProperty("hardcoreDeath", player.savedData.activityData.hardcoreDeath)
        activityData.addProperty("topGrabbed", player.savedData.activityData.isTopGrabbed)

        activityData.addProperty("barbFiremakingBow", player.savedData.activityData.isBarbarianFiremakingBow)
        activityData.addProperty("barbFiremakingPyre", player.savedData.activityData.isBarbarianFiremakingPyre)
        activityData.addProperty("barbFishingRod", player.savedData.activityData.isBarbarianFishingRod)
        activityData.addProperty("barbFishingBarehand", player.savedData.activityData.isBarbarianFishingBarehand)
        activityData.addProperty("barbSmithingSpear", player.savedData.activityData.isBarbarianSmithingSpear)
        activityData.addProperty("barbSmithingHasta", player.savedData.activityData.isBarbarianSmithingHasta)
        activityData.addProperty("barbHerblore", player.savedData.activityData.isBarbarianHerbloreAttackMix)

        root.add("activityData", activityData)
    }

    fun saveSpellbook(root: JsonObject) {
        root.addProperty("spellbook", player.spellBookManager.spellBook.toString())
    }

    fun saveAppearance(root: JsonObject) {
        val appearance = JsonObject()
        appearance.addProperty("gender", player.appearance.gender.toByte().toString())

        val appearanceCache = JsonArray()
        player.appearance.appearanceCache.forEach {
            val bodyPart = JsonObject()
            bodyPart.addProperty("look", it.look.toString())
            bodyPart.addProperty("color", it.color.toString())
            appearanceCache.add(bodyPart)
        }
        appearance.add("appearance_cache", appearanceCache)

        root.add("appearance", appearance)
    }

    fun saveQuests(root: JsonObject) {
        val quests = JsonObject()
        quests.addProperty("points", player.questRepository.points.toString())

        val questStages = JsonArray()
        player.questRepository.questList.forEach {
            val quest = JsonObject()
            quest.addProperty("questId", it.key.toString())
            quest.addProperty("questStage", it.value.toString())
            questStages.add(quest)
        }
        quests.add("questStages", questStages)

        root.add("quests", quests)
    }

    fun saveSettings(root: JsonObject) {
        val settings = JsonObject()
        settings.addProperty("brightness", player.settings.brightness.toString())
        settings.addProperty("musicVolume", player.settings.musicVolume.toString())
        settings.addProperty("soundEffectVolume", player.settings.soundEffectVolume.toString())
        settings.addProperty("areaSoundVolume", player.settings.areaSoundVolume.toString())
        settings.addProperty("publicChatSetting", player.settings.publicChatSetting.toString())
        settings.addProperty("privateChatSetting", player.settings.privateChatSetting.toString())
        settings.addProperty("clanChatSetting", player.settings.clanChatSetting.toString())
        settings.addProperty("tradeSetting", player.settings.tradeSetting.toString())
        settings.addProperty("assistSetting", player.settings.assistSetting.toString())
        settings.addProperty("runEnergy", player.settings.runEnergy.toString())
        settings.addProperty("specialEnergy", player.settings.specialEnergy.toString())
        settings.addProperty("attackStyle", player.settings.attackStyleIndex.toString())
        settings.addProperty("singleMouse", player.settings.isSingleMouseButton)
        settings.addProperty("disableChatEffects", player.settings.isDisableChatEffects)
        settings.addProperty("splitPrivate", player.settings.isSplitPrivateChat)
        settings.addProperty("acceptAid", player.settings.isAcceptAid)
        settings.addProperty("runToggled", player.settings.isRunToggled)
        settings.addProperty("retaliation", player.properties.isRetaliating)

        root.add("settings", settings)
    }

    fun saveSkills(root: JsonObject) {
        val skills = JsonArray()
        for (i in 0 until 24) {
            val skill = JsonObject()
            skill.addProperty("id", i.toString())
            skill.addProperty("static", player.skills.staticLevels[i].toString())
            skill.addProperty(
                "dynamic",
                when (i) {
                    Skills.HITPOINTS -> player.skills.lifepoints.toString()
                    Skills.PRAYER -> ceil(player.skills.prayerPoints).toInt().toString()
                    else -> player.skills.dynamicLevels[i].toString()
                }
            )
            skill.addProperty("experience", player.skills.getExperience(i).toString())
            skills.add(skill)
        }
        root.add("skills", skills)
        root.addProperty("totalEXP", player.skills.experienceGained.toString())
        root.addProperty("exp_multiplier", player.skills.experienceMultiplier.toString())

        if (player.skills.combatMilestone > 0 || player.skills.skillMilestone > 0) {
            val milestone = JsonObject()
            milestone.addProperty("combatMilestone", player.skills.combatMilestone.toString())
            milestone.addProperty("skillMilestone", player.skills.skillMilestone.toString())
            root.add("milestone", milestone)
        }
    }

    fun saveContainer(container: Container): JsonArray {
        val json = JsonArray()
        container.toArray().forEach {
            if (it != null) {
                val item = JsonObject()
                item.addProperty("slot", it.slot.toString())
                item.addProperty("id", it.id.toString())
                item.addProperty("amount", it.amount.toString())
                item.addProperty("charge", it.charge.toString())
                json.add(item)
            }
        }
        return json
    }

    fun saveCoreData(root: JsonObject) {
        val coreData = JsonObject()
        coreData.add("inventory", saveContainer(player.inventory))
        coreData.add("bank", saveContainer(player.bank))
        coreData.add("blastBars", saveContainer(player.blastBars))
        coreData.add("blastOre", saveContainer(player.blastOre))
        coreData.add("blastCoal", saveContainer(player.blastCoal))

        val bankTabs = JsonArray()
        player.bank.tabStartSlot.forEachIndexed { i, startSlot ->
            val tab = JsonObject()
            tab.addProperty("index", i.toString())
            tab.addProperty("startSlot", startSlot.toString())
            bankTabs.add(tab)
        }
        coreData.add("bankTabs", bankTabs)
        coreData.add("equipment", saveContainer(player.equipment))

        val locTemp = player.location
        val locStr = "${locTemp.x},${locTemp.y},${locTemp.z}"
        coreData.addProperty("location", locStr)

        val varpData = JsonArray()
        for ((index, value) in player.varpMap) {
            if (player.saveVarp[index] != true) continue
            if (value == 0) continue

            val varpObj = JsonObject()
            varpObj.addProperty("index", index.toString())
            varpObj.addProperty("value", value.toString())
            varpData.add(varpObj)
        }
        coreData.add("varp", varpData)

        val timerData = JsonObject()
        player.timers.saveTimers(timerData)
        coreData.add("timers", timerData)

        root.add("core_data", coreData)
    }

    fun saveVersion(root: JsonObject) {
        root.addProperty("version", player.version)
    }
}
