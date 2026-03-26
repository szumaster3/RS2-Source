package content.global.skill.magic.spells.lunar

import content.data.consumables.Consumables
import content.global.skill.construction.items.PlankType
import content.global.skill.construction.items.PlankType.Companion.spellPrice
import content.global.skill.cooking.CookableItems
import content.global.skill.farming.CompostBins
import content.global.skill.farming.CompostType
import content.global.skill.farming.FarmingPatch
import content.global.skill.magic.SpellListener
import content.global.skill.magic.spells.LunarSpells
import core.api.*
import core.game.bots.AIPlayer
import core.game.component.CloseEvent
import core.game.component.Component
import core.game.consumable.Potion
import core.game.interaction.QueueStrength
import core.game.node.entity.combat.DeathTask
import core.game.node.entity.combat.ImpactHandler
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.diary.DiaryType
import core.game.node.entity.skill.Skills
import core.game.node.item.Item
import core.game.system.config.NPCConfigParser
import core.game.system.task.Pulse
import core.game.system.timer.impl.SkillRestore
import core.game.world.GameWorld
import core.game.world.map.RegionManager
import core.game.world.update.flag.context.Animation
import core.tools.RandomFunction
import core.tools.secondsToTicks
import shared.consts.*
import kotlin.math.floor

class LunarSpell : SpellListener("lunar") {

    override fun defineListeners() {

        /*
         * Bake pie.
         */

        onCast(LunarSpells.BAKE_PIE, NONE) { player, _ ->
            requires(player,65, arrayOf(Item(Items.ASTRAL_RUNE_9075), Item(Items.FIRE_RUNE_554,5), Item(Items.WATER_RUNE_555,4)))
            val playerPies = ArrayList<Item>()

            for(item in player.inventory.toArray()){
                if(item == null) continue
                val pie = CookableItems.forId(item.id) ?: continue
                if(!pie.name.lowercase().contains("pie")) continue
                if(player.skills.getLevel(Skills.COOKING) < pie.level) continue
                playerPies.add(item)
            }

            if(playerPies.isEmpty()){
                sendMessage(player, "You have no pies which you have the level to cook.")
                throw IllegalStateException()
            }

            player.pulseManager.run(object : Pulse() {
                var counter = 0
                override fun pulse(): Boolean {
                    if (playerPies.isEmpty()) return true
                    if (counter == 0) {
                        delay = animationDuration(Animation(4413)) + 1
                        counter++
                    }
                    val item = playerPies[0]
                    val pie = CookableItems.forId(item.id) ?: return true
                    visualizeSpell(player, 4413, 746, 75, Sounds.LUNAR_BAKE_PIE_2879)
                    addXP(player, 60.0)
                    rewardXP(player, Skills.COOKING, pie.experience)
                    setDelay(player, false)
                    player.inventory.remove(item)
                    player.inventory.add(Item(pie.cooked))
                    playerPies.remove(item)
                    if (playerPies.isNotEmpty()) {
                        removeRunes(player, false)
                    } else {
                        removeRunes(player, true)
                    }
                    if (!hasDiaryTaskComplete(player, DiaryType.FREMENNIK, 2, 6)) {
                        finishDiaryTask(player, DiaryType.FREMENNIK, 2 , 6)
                    }
                    return false
                }
            })
        }

        /*
         * Cure group.
         */

        onCast(LunarSpells.CURE_GROUP, NONE) { player, _ ->
            requires(player, 74, arrayOf(Item(Items.ASTRAL_RUNE_9075, 2), Item(Items.LAW_RUNE_563, 2), Item(Items.COSMIC_RUNE_564, 2)))
            removeRunes(player, true)
            visualizeSpell(player, Animations.HEAL_GROUP_4409, 744, 130, Sounds.LUNAR_CURE_GROUP_2882)

            curePoison(player)

            for (acct in RegionManager.getNearbyPlayers(player, 1)) {
                if (!acct.isActive || acct.locks.isInteractionLocked()) {
                    continue
                }
                if (!acct.settings.isAcceptAid) {
                    continue
                }

                curePoison(acct)
                sendMessage(acct, "You have been cured of poison.")
                visualizeSpell(acct, -1, 744, 130, Sounds.LUNAR_CURE_OTHER_INDIVIDUAL_2889)
            }

            addXP(player, 74.0)
        }

        /*
         * Cure me.
         */

        onCast(LunarSpells.CURE_ME, NONE) { player, _ ->
            requires(player, 71, arrayOf(Item(Items.ASTRAL_RUNE_9075, 2), Item(Items.LAW_RUNE_563, 1), Item(Items.COSMIC_RUNE_564, 2)))

            if (!isPoisoned(player)) {
                sendMessage(player, "You are not poisoned.")
                return@onCast
            }

            removeRunes(player, true)
            playAudio(player, Sounds.LUNAR_CURE_OTHER_INDIVIDUAL_2900)
            visualizeSpell(player, Animations.LUNAR_CURE_ME_4411, 742, 90, Sounds.LUNAR_CURE_2884)
            curePoison(player)
            addXP(player, 69.0)
            sendMessage(player, "You have been cured of poison.")
        }

        /*
         * Cure other.
         */

        onCast(LunarSpells.CURE_OTHER, PLAYER) { player, node ->
            requires(player, 68, arrayOf(Item(Items.ASTRAL_RUNE_9075, 1), Item(Items.LAW_RUNE_563), Item(Items.EARTH_RUNE_557, 10)))
            node?.let {
                if (!isPlayer(node)) {
                    sendMessage(player, "You can only cast this spell on other players.")
                    return@onCast
                }
                val p = node.asPlayer()
                if (!p.isActive || p.locks.isInteractionLocked()) {
                    sendMessage(player, "This player is busy.")
                    return@onCast
                }
                if (!p.settings.isAcceptAid) {
                    sendMessage(player, "This player is not accepting any aid.")
                    return@onCast
                }
                if (!isPoisoned(p)) {
                    sendMessage(player, "This player is not poisoned.")
                    return@onCast
                }
                player.face(p)
                visualizeSpell(player, Animations.LUNAR_CURE_ME_4411, 736, 130, Sounds.LUNAR_CURE_OTHER_2886)
                visualizeSpell(p, -1, 736, 130, Sounds.LUNAR_CURE_OTHER_INDIVIDUAL_2889)
                removeRunes(player, true)
                curePoison(p)
                addXP(player, 65.0)
                sendMessage(p, "You have been cured of poison.")
            }
        }

        /*
         * Cure plant
         */

        onCast(LunarSpells.CURE_PLANT, OBJECT) { player, node ->
            requires(player, 66, arrayOf(Item(Items.ASTRAL_RUNE_9075), Item(Items.EARTH_RUNE_557, 8)))
            if (CompostBins.forObject(node!!.asScenery()) != null) {
                sendMessage(player, "Bins don't often get diseased.")
                return@onCast
            }
            val fPatch = FarmingPatch.forObject(node.asScenery())
            if (fPatch == null) {
                sendMessage(player, "Umm... this spell won't cure that!")
                return@onCast
            }
            val patch = fPatch.getPatchFor(player)
            if (patch.isWeedy()) {
                sendMessage(player, "The weeds are healthy enough already.")
                return@onCast
            }
            if (patch.isEmptyAndWeeded()) {
                sendMessage(player, "There's nothing there to cure.")
                return@onCast
            }
            if (patch.isGrown()) {
                sendMessage(player, "That's not diseased.")
                return@onCast
            }
            if (patch.isDead) {
                sendMessage(player, "It says 'Cure' not 'Resurrect'. Although death may arise from disease, it is not in itself a disease and hence cannot be cured. So there.")
                return@onCast
            }
            if (!patch.isDiseased) {
                sendMessage(player, "It is growing just fine.")
                return@onCast
            }

            patch.cureDisease()
            removeRunes(player)
            addXP(player, 60.0)
            visualizeSpell(player, Animations.HEAL_GROUP_4409, 748, 100, Sounds.LUNAR_CURE_GROUP_2882)
            setDelay(player, false)
        }

        /*
         * Dream.
         */

        onCast(LunarSpells.DREAM, NONE) { player, _ ->
            if(player.skills.lifepoints >= getStatLevel(player, Skills.HITPOINTS)) {
                sendMessage(player, "You have no need to cast this spell since your hitpoints are already full.")
                throw IllegalStateException()
            }

            val timer = getOrStartTimer<SkillRestore>(player)
            animate(player, Animations.DREAM_SPELL_6295)
            removeRunes(player, true)
            addXP(player, 82.0)
            delayEntity(player, 4)
            queueScript(player, 4, QueueStrength.WEAK) { stage: Int ->
                if (stage == 0) {
                    sendGraphics(Graphics.SLEEPING_ZZZ_1056, player.location)
                    playAudio(player, Sounds.LUNAR_SLEEP_3619)
                    return@queueScript delayScript(player, 5)
                }
                animate(player, Animations.LUNAR_DREAM_MID_6296)
                sendGraphics(Graphics.SLEEPING_ZZZ_1056, player.location)
                if (stage.mod(10) == 0) {
                    val amt = timer.getHealAmount(player)
                    heal(player, amt)
                    if (player.skills.lifepoints >= getStatLevel(player, Skills.HITPOINTS)) {
                        animate(player, Animations.GET_UP_FROM_SLEEPING_DREAM_SPELL_6297)
                        return@queueScript stopExecuting(player)
                    }
                }
                return@queueScript delayScript(player, 5)
            }
        }

        /*
         * Energy transfer.
         */

        onCast(LunarSpells.ENERGY_TRANSFER, PLAYER) { player, node ->
            requires(player, 91, arrayOf(Item(Items.ASTRAL_RUNE_9075, 3), Item(Items.LAW_RUNE_563, 2), Item(Items.NATURE_RUNE_561, 1)))
            node?.let {
                if (!isPlayer(node)) {
                    sendMessage(player, "You can only cast this spell on other players.")
                    return@onCast
                }
                val p = node.asPlayer()
                if (!p.isActive || p.locks.isInteractionLocked()) {
                    sendMessage(player, "This player is busy.")
                    return@onCast
                }
                if (!p.settings.isAcceptAid) {
                    sendMessage(player, "This player is not accepting any aid.")
                    return@onCast
                }
                if (10 >= player.skills.lifepoints) {
                    sendMessage(player, "You need more Hitpoints to cast this spell.")
                    return@onCast
                }

                player.face(p)

                visualizeSpell(player, Animations.LUNAR_CURE_ME_4411, 738, 90, Sounds.LUNAR_ENERGY_TRANSFER_2885)
                visualize(p, -1, 738)

                val hp = floor(player.skills.lifepoints * 0.10)
                var r = hp
                if (r > (100 - p.settings.runEnergy)) {
                    r = (100 - p.settings.runEnergy)
                }
                if (r < 0) {
                    r = 0.0
                }
                p.settings.runEnergy += r
                player.settings.runEnergy -= r
                impact(player, hp.toInt(), ImpactHandler.HitsplatType.NORMAL)
                var e = 100
                e -= p.settings.specialEnergy
                if (e < 0) {
                    e = 0
                }
                if (e > player.settings.specialEnergy) {
                    e = player.settings.specialEnergy
                }
                p.settings.specialEnergy += e
                player.settings.specialEnergy -= e
                removeRunes(player, true)
                addXP(player, 100.0)
            }
        }

        /*
         * Fertile soil.
         */

        onCast(LunarSpells.FERTILE_SOIL, OBJECT) { player, node ->
            requires(player, 83, arrayOf(Item(Items.ASTRAL_RUNE_9075, 3), Item(Items.NATURE_RUNE_561, 2), Item(Items.EARTH_RUNE_557, 15)),)
            node?.let {
                if (CompostBins.forObjectID(node.id) != null) {
                    sendMessage(player, "No, that would be silly.")
                    return@onCast
                }

                val fPatch = FarmingPatch.forObject(node.asScenery())
                if (fPatch == null) {
                    sendMessage(player, "Um... I don't want to fertilize that!")
                    return@onCast
                }

                val patch = fPatch.getPatchFor(player)
                if (patch.isGrown()) {
                    sendMessage(player, "Composting isn't going to make it get any bigger.")
                    return@onCast
                }
                if (patch.isFertilized()) {
                    sendMessage(player, "This patch has already been composted.")
                    return@onCast
                }

                removeRunes(player, true)
                animate(player, Animations.FERTILE_SPELL_4413)
                sendGraphics(724, node.location)
                playGlobalAudio(node.location, Sounds.LUNAR_FERTILIZE_2891)
                patch.compost = CompostType.SUPER_COMPOST
                addXP(player, 87.0)
                sendMessage(player, "You fertilize the soil.")
            }
        }

        /*
         * Heal other.
         */

        onCast(LunarSpells.HEAL_OTHER, PLAYER) { player, node ->
            requires(player, 92, arrayOf(Item(Items.BLOOD_RUNE_565, 1), Item(Items.LAW_RUNE_563, 3), Item(Items.ASTRAL_RUNE_9075, 3)))
            node ?: return@onCast
            if (!isPlayer(node)) {
                sendMessage(player, "You can only cast this spell on players.")
                return@onCast
            }

            val other = node.asPlayer()
            val maxHp = getStatLevel(player, Skills.HITPOINTS)
            val elevenPercent = (maxHp * 0.11).toInt()

            if (player.skills.lifepoints < elevenPercent) {
                sendMessage(player, "You need at least 11 percent of your original Hitpoints in order to do this.")
                return@onCast
            }
            if (DeathTask.isDead(other)) {
                sendMessage(player, "Player is beyond saving.")
                return@onCast
            }
            if (!other.isActive || other.locks.isInteractionLocked()) {
                sendMessage(player, "This player is busy.")
                return@onCast
            }
            if (!other.settings.isAcceptAid) {
                sendMessage(player, "The player is not accepting any aid.")
                return@onCast
            }
            if (other.skills.lifepoints >= getStatLevel(other, Skills.HITPOINTS)) {
                sendMessage(player, "The player already has full hitpoints.")
                return@onCast
            }

            val transfer = floor(player.skills.lifepoints * 0.75).toInt()

            player.face(other)
            removeRunes(player, true)
            impact(player, transfer, ImpactHandler.HitsplatType.NORMAL)
            heal(other, transfer)

            visualizeSpell(player, Animations.LUNAR_CURE_ME_4411, Graphics.VECNAS_SKULL_738, 92, Sounds.LUNAR_HEAL_OTHER_2895)
            visualizeSpell(other, -1, Graphics.VECNAS_SKULL_738, 92, Sounds.LUNAR_HEAL_OTHER_INDIVIDUAL_2892)

            addXP(player, 101.0)
        }

        /*
         * Humidify.
         */

        onCast(LunarSpells.HUMIDIFY, NONE) { player, _ ->
            requires(player, 68, arrayOf(Item(Items.ASTRAL_RUNE_9075, 1), Item(Items.WATER_RUNE_555, 3), Item(Items.FIRE_RUNE_554, 1)))

            val playerEmpties = ArrayDeque<Item>()

            for (item in player.inventory.toArray()) {
                if (item == null) continue
                if (!HumidifyItems.emptyContains(item.id)) continue
                playerEmpties.add(item)
            }

            if (playerEmpties.isEmpty()) {
                sendMessage(player, "You have no vessels that this spell can fill with water.")
                throw IllegalStateException()
            }

            removeRunes(player)
            delayEntity(player, Animation(Animations.LUNAR_HUMIDIFY_6294).duration)
            visualizeSpell(player, Animations.LUNAR_HUMIDIFY_6294, Graphics.HUMIDIFY_GFX_1061, 20, Sounds.LUNAR_HUMIDIFY_3614)
            for (item in playerEmpties) {
                val filled = HumidifyItems.forId(item.id)
                removeItem(player, item.id) && addItem(player, filled)
            }
            addXP(player, 65.0)
        }

        /*
         * Hunter kit
         */

        onCast(LunarSpells.HUNTER_KIT, NONE) { player, _ ->
            requires(player, 71, arrayOf(Item(Items.ASTRAL_RUNE_9075, 2), Item(Items.EARTH_RUNE_557, 2)))
            if (freeSlots(player) == 0) {
                sendMessage(player, "You need at least one free inventory space to cast this spell.")
                return@onCast
            }

            removeRunes(player, true)

            if (!addItem(player, Items.HUNTER_KIT_11159)) {
                sendMessage(player, "You don't have enough inventory space for the hunter kit.")
                return@onCast
            }
            setDelay(player, 2)
            animate(player, Animations.LUNAR_HUNTER_KIT_6303)
            sendGraphics(core.game.world.update.flag.context.Graphics(Graphics.MAKE_HUNTER_KIT_1074), player.location)
            playAudio(player, Sounds.LUNAR_HUNTER_KIT_3615)
            addXP(player, 70.0)
        }

        /*
         * Magic Imbue.
         */

        onCast(LunarSpells.MAGIC_IMBUE, NONE) { player, _ ->
            requires(player, 82, arrayOf(Item(Items.ASTRAL_RUNE_9075, 2), Item(Items.FIRE_RUNE_554, 7), Item(Items.WATER_RUNE_555, 7)))
            val endTick = getAttribute(player, "spell:imbue", 0)
            if (endTick > GameWorld.ticks) {
                sendMessage(player, "You are already imbued.")
                return@onCast
            }

            removeRunes(player, true)
            setAttribute(player, "spell:imbue", GameWorld.ticks + secondsToTicks(20))
            animate(player, 722)
            sendGraphics(core.game.world.update.flag.context.Graphics(141, 92), player.location)
            playAudio(player, Sounds.LUNAR_EMBUE_RUNES_2888)
            addXP(player, 86.0)
            sendMessage(player, "You are charged to combine runes!")
            player.pulseManager.run(object : Pulse() {
                override fun pulse(): Boolean {
                    val remaining = getAttribute(player, "spell:imbue", 0) - GameWorld.ticks
                    when (remaining) {
                        10 -> sendMessage(player, "Magic Imbue charge running out...")
                        5  -> sendMessage(player, "Magic Imbue charge running out...")
                    }
                    if (remaining <= 0) {
                        sendMessage(player, "Your Magic Imbue charge has ended.")
                        return true
                    }
                    return false
                }
            })
        }

        /*
         * Plank make.
         */

        onCast(LunarSpells.PLANK_MAKE, ITEM) { player, node ->
            requires(player, 86, arrayOf(Item(Items.ASTRAL_RUNE_9075, 2), Item(Items.NATURE_RUNE_561, 1), Item(Items.EARTH_RUNE_557, 15)))
            if (node == null) {
                sendMessage(player, "You need to use this spell on logs.")
                return@onCast
            }

            val planks = PlankType.getForLog(node.id)
            if (planks == null) {
                sendMessage(player, "You need to use this spell on logs.")
                return@onCast
            }

            val coinsNeeded = planks.spellPrice()
            if (amountInInventory(player, Items.COINS_995) < coinsNeeded) {
                sendMessage(player, "You need $coinsNeeded coins to convert that log into a plank.")
                return@onCast
            }

            if (!removeItem(player, Item(Items.COINS_995, coinsNeeded))) {
                sendMessage(player, "Failed to remove coins.")
                return@onCast
            }

            lock(player, 3)
            setDelay(player, false)
            visualizeSpell(player, Animations.LUNAR_PLANK_MAKE_6298, Graphics.PLANK_MAKE_GFX_1063, 100, Sounds.LUNAR_MAKE_PLANK_3617)
            removeRunes(player)
            replaceSlot(player, node.asItem().slot, Item(planks.plank))
            addXP(player, 90.0)
            showMagicTab(player)
        }

        /*
         * NPC contact.
         */

        onCast(LunarSpells.NPC_CONTACT, NONE) { player, _ ->
            requires(player, 67, arrayOf(Item(Items.ASTRAL_RUNE_9075), Item(Items.COSMIC_RUNE_564), Item(Items.AIR_RUNE_556, 2)))
            openInterface(player, Components.NPC_CONTACT_429)
            player.setAttribute("contact-caller") {
                removeRunes(player)
                addXP(player, 63.0)
                setDelay(player, false)
                visualizeSpell(player, Animations.FERTILE_SPELL_4413, 728, 130, 3618)
            }
        }

        /*
         * Monster examine.
         */

        onCast(LunarSpells.MONSTER_EXAMINE, NPC) { player, node ->
            requires(player, 66, arrayOf(Item(Items.ASTRAL_RUNE_9075, 1), Item(Items.MIND_RUNE_558, 1), Item(Items.COSMIC_RUNE_564, 1)))
            val npc = node?.asNpc() ?: return@onCast
            if (!npc.location.withinDistance(player.location)) {
                sendMessage(player, "You must get closer to use this spell.")
                return@onCast
            }

            setDelay(player, false)
            faceLocation(player, npc.location)
            visualizeSpell(player, Animations.HUMAN_SPY_STAT_6293, Graphics.STAT_SPY_GFX_1060, Sounds.LUNAR_STAT_SPY_3620)
            removeRunes(player)
            addXP(player, 61.0)
            openSingleTab(player, Components.DREAM_MONSTER_STAT_522)

            val def = npc.definition
            val hp = (def.handlers[NPCConfigParser.LIFEPOINTS] as? Int) ?: 0
            val maxHit = npc.getSwingHandler(false)?.calculateHit(npc, player, 1.0) ?: 0

            val poisonStatus =
                if (npc.isPoisonImmune)
                    "This creature is immune to poison."
                else
                    "This creature is not immune to poison."

            fun line(index: Int, text: String) =
                sendString(player, text, Components.DREAM_MONSTER_STAT_522, index)

            line(0, "Monster name: ${def.name}")
            line(1, "Combat Level: ${def.combatLevel}")
            line(2, "Hitpoints: $hp")
            line(3, "Max hit: $maxHit")
            line(4, poisonStatus)
        }

        /*
         * String jewellery.
         */

        onCast(LunarSpells.STRING_JEWELLERY, NONE) { player, _ ->
            requires(player, 80, arrayOf(Item(Items.ASTRAL_RUNE_9075, 2), Item(Items.EARTH_RUNE_557, 10), Item(Items.WATER_RUNE_555, 5)))
            val playerJewellery = ArrayDeque<Item>()

            for(item in player.inventory.toArray()) {
                if(item == null) continue
                if(!StringJewelleryItems.unstrungContains(item.id)) continue
                playerJewellery.add(item)
            }

            player.pulseManager.run(object : Pulse() {
                var counter = 0
                override fun pulse(): Boolean {
                    removeAttribute(player, "spell:runes")
                    if (playerJewellery.isEmpty())
                        return true
                    requires(player, 80, arrayOf(Item(Items.ASTRAL_RUNE_9075, 2), Item(Items.EARTH_RUNE_557, 10), Item(Items.WATER_RUNE_555, 5)))
                    if(counter == 0) delay = animationDuration(Animation(Animations.LUNAR_STRING_JEWELLERY_4412)) + 1
                    val item = playerJewellery[0]
                    val strung = StringJewelleryItems.forId(item.id)
                    setDelay(player,false)
                    if(removeItem(player, item) && addItem(player, strung)) {
                        removeRunes(player, true)
                        visualizeSpell(player, Animations.LUNAR_STRING_JEWELLERY_4412, 730, 100, Sounds.LUNAR_STRING_AMULET_2903)
                        rewardXP(player, Skills.CRAFTING, 4.0)
                        addXP(player, 83.0)
                        playerJewellery.remove(item)
                        if(playerJewellery.isNotEmpty()) removeRunes(player,false) else removeRunes(player,true)
                    }
                    counter++
                    return playerJewellery.isEmpty()
                }
            })
        }

        /*
         * Boost share potion.
         */

        onCast(LunarSpells.BOOST_POTION_SHARE, ITEM) { player, node ->
            requires(player, 84, arrayOf(Item(Items.ASTRAL_RUNE_9075, 3), Item(Items.EARTH_RUNE_557, 12), Item(Items.WATER_RUNE_555, 10)))
            val item = node?.asItem() ?: return@onCast

            val consumable = Consumables.getConsumableById(item.id)
                ?: return@onCast sendMessage(player, "You can only cast this spell on a potion.")

            val potion = consumable.consumable as? Potion
                ?: return@onCast sendMessage(player, "You can only cast this spell on a valid potion.")

            val name = item.name.lowercase()

            if (!item.definition.isTradeable ||
                name.contains("restore") ||
                name.contains("zamorak") ||
                name.contains("saradomin") ||
                name.contains("combat")
            ) {
                sendMessage(player, "You can't cast this spell on that item.")
                return@onCast
            }

            val doses = potion.getDose(item)
            val nearby = RegionManager.getNearbyPlayers(player, 1).filter {
                it != player &&
                        it.isActive &&
                        (it.settings.isAcceptAid || it is AIPlayer)
            }

            if (nearby.isEmpty()) {
                sendMessage(player, "There is nobody around to share the potion with.")
                return@onCast
            }

            var shared = 0

            for (p in nearby) {
                if (shared >= doses) break
                sendGraphics(733, p.location)
                playGlobalAudio(p.location, Sounds.LUNAR_STRENGTH_SHARE2_2902)
                potion.effect.activate(p)
                shared++
            }

            if (shared == 0) {
                sendMessage(player, "Nobody nearby has accept aid enabled.")
                return@onCast
            }

            removeRunes(player, true)
            potion.effect.activate(player)
            visualizeSpell(player, Animations.FERTILE_SPELL_4413, 733, 130, Sounds.LUNAR_STRENGTH_SHARE_2901)
            player.inventory.remove(item)
            val newIndex = (potion.ids.size - doses) + shared

            if (newIndex > potion.ids.lastIndex) {
                player.inventory.add(Item(Items.VIAL_229))
            } else {
                player.inventory.add(Item(potion.ids[newIndex]))
            }

            addXP(player, 88.0)
        }

        /*
         * Stats restore.
         */

        onCast(LunarSpells.STAT_RESTORE_POT_SHARE, ITEM) { player, node ->
            requires(player, 81, arrayOf(Item(Items.ASTRAL_RUNE_9075, 2), Item(Items.EARTH_RUNE_557, 10), Item(Items.WATER_RUNE_555, 10)))
            val item = node?.asItem() ?: return@onCast

            val consumable = Consumables.getConsumableById(item.id)
                ?: return@onCast sendMessage(player, "You can only cast this spell on a potion.")

            val potion = consumable.consumable as? Potion
                ?: return@onCast sendMessage(player, "You can only cast this spell on a valid potion.")

            if (!item.definition.isTradeable() || potion !in accepted.map { it.consumable }) {
                sendMessage(player, "You can't cast this spell on that item.")
                return@onCast
            }

            player.interfaceManager.setViewedTab(6)
            val doses = potion.getDose(item)
            val nearby = RegionManager.getNearbyPlayers(player, 1).filter {
                it != player &&
                        it.isActive &&
                        (it.settings.isAcceptAid || it is AIPlayer)
            }

            if (nearby.isEmpty()) {
                sendMessage(player, "There is nobody around to share the potion with.")
                return@onCast
            }

            var shared = 0

            for (p in nearby) {
                if (shared >= doses) break
                sendGraphics(733, p.location)
                playGlobalAudio(p.location, Sounds.LUNAR_STAT_SHARE_2899)
                potion.effect.activate(p)
                shared++
            }

            if (shared == 0) {
                sendMessage(player, "There is nobody around that has accept aid on to share the potion with you.")
                return@onCast
            }

            removeRunes(player, true)
            potion.effect.activate(player)
            visualizeSpell(player, Animations.FERTILE_SPELL_4413, 733, 130, Sounds.LUNAR_STAT_SHARE_2899)
            player.inventory.remove(item)
            val newIndex = (potion.ids.size - doses) + shared

            if (newIndex > potion.ids.lastIndex) {
                player.inventory.add(Item(Items.VIAL_229))
            } else {
                player.inventory.add(Item(potion.ids[newIndex]))
            }

            addXP(player, 84.0)
        }

        /*
         * Superglass make.
         */

        onCast(LunarSpells.SUPERGLASS_MAKE, NONE) { player, _ ->
            requires(player, 77, arrayOf(Item(Items.ASTRAL_RUNE_9075, 2), Item(Items.FIRE_RUNE_554, 6), Item(Items.AIR_RUNE_556, 10)))
            val sandCount = SAND_SOURCES.sumOf { amountInInventory(player, it) }
            val weedCount = GLASS_WEEDS.sumOf { amountInInventory(player, it) }

            val amount = minOf(sandCount, weedCount)

            if (amount == 0) {
                sendMessage(player, "you don't have any soda ash, seaweed or swamp weed.")
                return@onCast
            }

            var remainingSand = amount
            for (sand in SAND_SOURCES) {
                while (remainingSand > 0 && amountInInventory(player, sand) > 0) {
                    removeItem(player, Item(sand))
                    remainingSand--
                }
            }

            var remainingWeed = amount
            for (weed in GLASS_WEEDS) {
                while (remainingWeed > 0 && amountInInventory(player, weed) > 0) {
                    removeItem(player, Item(weed))
                    remainingWeed--
                }
            }

            val bonus = RandomFunction.random(0, amount / 2)
            val output = amount + bonus
            lock(player, secondsToTicks(3))
            addItem(player, Items.MOLTEN_GLASS_1775, output)
            removeRunes(player, true)
            visualizeSpell(player, Animations.FERTILE_SPELL_4413, 729, 120, Sounds.LUNAR_HEATGLASS_2896)
            addXP(player, 78.0)
            rewardXP(player, Skills.CRAFTING, output * 10.0)
        }

        /*
         * Stats spy.
         */

        onCast(LunarSpells.STAT_SPY, PLAYER) { player, target ->
            requires(player, 75, arrayOf(Item(Items.COSMIC_RUNE_564, 2), Item(Items.ASTRAL_RUNE_9075, 2), Item(Items.BODY_RUNE_559, 5)))
            val other = target as? Player
                ?: return@onCast sendMessage(player, "You can only cast this spell on players.")

            visualizeSpell(player, Animations.HUMAN_SPY_STAT_6293, Graphics.STAT_SPY_GFX_1059, Sounds.LUNAR_STAT_SPY_3620)
            other.graphics(core.game.world.update.flag.context.Graphics(Graphics.GRAPHIC_734, 120))
            playGlobalAudio(other.location, Sounds.LUNAR_STAT_SPY_IMPACT_3621)

            Component(Components.DREAM_PLAYER_STATS_523).closeEvent = CloseEvent { p, _ ->
                p.interfaceManager.restoreTabs()
                true
            }

            for (entry in statFields) {
                val skill = entry[0]
                val currString = entry[1]
                val baseString = entry[2]

                player.packetDispatch.sendString("${other.skills.getLevel(skill)}", Components.DREAM_PLAYER_STATS_523, currString)
                player.packetDispatch.sendString("${other.skills.getStaticLevel(skill)}", Components.DREAM_PLAYER_STATS_523, baseString)
            }
            player.packetDispatch.sendString(other.username, Components.DREAM_PLAYER_STATS_523, 99)
            player.interfaceManager.openSingleTab(Component(Components.DREAM_PLAYER_STATS_523))
            removeRunes(player, true)
            addXP(player, 76.0)
        }

        /*
         * Vengeance other.
         */

        onCast(LunarSpells.VENGEANCE_OTHER, PLAYER) { player, node ->
            requires(player, 93, arrayOf(Item(Items.ASTRAL_RUNE_9075, 3), Item(Items.DEATH_RUNE_560, 2), Item(Items.EARTH_RUNE_557, 10)))
            val target = node?.asPlayer() ?: return@onCast
            val ticks = GameWorld.ticks
            val endTick = getAttribute(player, "vengeance_delay", 0)

            if (endTick > ticks) {
                sendMessage(player, "You can only cast vengeance spells once every 30 seconds.")
                return@onCast
            }

            if (!target.settings.isAcceptAid) {
                sendMessage(player, "The player is not accepting any aid.")
                return@onCast
            }

            removeRunes(player, true)
            setAttribute(player, "vengeance_delay", ticks + 50)
            setAttribute(target, "vengeance", true)

            lock(player, 3)
            visualizeSpell(player, Animation.create(4411), core.game.world.update.flag.context.Graphics(725, 96), 100, Sounds.LUNAR_VENGENCE_OTHER_2908)
            addXP(player, 108.0)
            sendMessage(player, "You cast vengeance on ${target.username}!")
        }

        /*
         * Vengeance self.
         */

        onCast(LunarSpells.VENGEANCE, NONE) { player, _ ->
            requires(player, 94, arrayOf(Item(Items.ASTRAL_RUNE_9075, 4), Item(Items.DEATH_RUNE_560, 2), Item(Items.EARTH_RUNE_557, 10)))

            val ticks = GameWorld.ticks
            val endTick = getAttribute(player, "vengeance_delay", 0)

            if (endTick > ticks) {
                sendMessage(player, "You can only cast vengeance spells once every 30 seconds.")
                return@onCast
            }

            removeRunes(player, true)
            setAttribute(player, "vengeance_delay", ticks + 50)
            setAttribute(player, "vengeance", true)
            lock(player, 3)
            visualizeSpell(player, Animation.create(4410), core.game.world.update.flag.context.Graphics(726, 92), 100, Sounds.LUNAR_VENGENCE_2907)
            addXP(player, 112.0)
            sendMessage(player, "You cast vengeance on yourself!")
        }

        /*
         * Heal Group.
         */

        onCast(LunarSpells.HEAL_GROUP, NONE) { player, _ ->
            requires(player, 95, arrayOf(Item(Items.BLOOD_RUNE_565, 3), Item(Items.LAW_RUNE_563, 6), Item(Items.ASTRAL_RUNE_9075, 4)))
            val maxHp = getStatLevel(player, Skills.HITPOINTS)
            val elevenPercent = (maxHp * 0.11).toInt()

            if (player.skills.lifepoints < elevenPercent) {
                sendMessage(player, "You need at least 11 percent of your original Hitpoints in order to do this.")
                return@onCast
            }

            val healAmount = floor(player.skills.lifepoints * 0.75).toInt()
            if (healAmount < 1) {
                sendMessage(player, "You don't have enough hitpoints left to cast this spell.")
                return@onCast
            }

            val players = RegionManager.getNearbyPlayers(player, 1).filter {
                it != player &&
                        it.isActive &&
                        it.settings.isAcceptAid &&
                        it.skills.lifepoints < getStatLevel(it, Skills.HITPOINTS)
            }

            if (players.isEmpty()) {
                sendMessage(player, "There are no players around to replenish.")
                return@onCast
            }

            removeRunes(player, true)
            player.animate(Animation(Animations.HEAL_GROUP_4409))
            playGlobalAudio(player.location, Sounds.LUNAR_HEAL_GROUP_2894)
            impact(player, healAmount, ImpactHandler.HitsplatType.NORMAL)
            for (p in players) {
                playGlobalAudio(p.location, Sounds.LUNAR_HEAL_OTHER_INDIVIDUAL_2892)
                sendGraphics(734, p.location)
                heal(p, healAmount)
            }

            addXP(player, 124.0)
        }
    }

    companion object {
        private enum class StringJewelleryItems(val unstrung: Int, val strung: Int) {
            GOLD(       unstrung = Items.GOLD_AMULET_1673,      strung = Items.GOLD_AMULET_1692),
            SAPPHIRE(   unstrung = Items.SAPPHIRE_AMULET_1675,  strung = Items.SAPPHIRE_AMULET_1694),
            EMERALD(    unstrung = Items.EMERALD_AMULET_1677,   strung = Items.EMERALD_AMULET_1696),
            RUBY(       unstrung = Items.RUBY_AMULET_1679,      strung = Items.RUBY_AMULET_1698),
            DIAMOND(    unstrung = Items.DIAMOND_AMULET_1681,   strung = Items.DIAMOND_AMULET_1700),
            DRAGONSTONE(unstrung = Items.DRAGONSTONE_AMMY_1683, strung = Items.DRAGONSTONE_AMMY_1702),
            ONYX(       unstrung = Items.ONYX_AMULET_6579,      strung = Items.ONYX_AMULET_6581),
            SALVE(      unstrung = Items.SALVE_SHARD_4082,      strung = Items.SALVE_AMULET_4081),
            HOLY(       unstrung = Items.UNSTRUNG_SYMBOL_1714,  strung = Items.UNBLESSED_SYMBOL_1716),
            UNHOLY(     unstrung = Items.UNSTRUNG_EMBLEM_1720,  strung = Items.UNPOWERED_SYMBOL_1722);

            companion object {
                private val mapping = values().associate { it.unstrung to it.strung }
                fun forId(id: Int) = mapping[id]!!
                fun unstrungContains(id: Int) = mapping.containsKey(id)
            }
        }

        private enum class HumidifyItems(val empty: Int, val full: Int) {
            VIAL(          empty = Items.VIAL_229,           full = Items.VIAL_OF_WATER_227),
            WATERSKIN0(    empty = Items.WATERSKIN0_1831,    full = Items.WATERSKIN4_1823),
            WATERSKIN1(    empty = Items.WATERSKIN1_1829,    full = Items.WATERSKIN4_1823),
            WATERSKIN2(    empty = Items.WATERSKIN2_1827,    full = Items.WATERSKIN4_1823),
            WATERSKIN3(    empty = Items.WATERSKIN3_1825,    full = Items.WATERSKIN4_1823),
            BUCKET(        empty = Items.BUCKET_1925,        full = Items.BUCKET_OF_WATER_1929),
            BOWL(          empty = Items.BOWL_1923,          full = Items.BOWL_OF_WATER_1921),
            JUG(           empty = Items.JUG_1935,           full = Items.JUG_OF_WATER_1937),
            WATERING_CAN0( empty = Items.WATERING_CAN_5331,  full = Items.WATERING_CAN8_5340),
            WATERING_CAN1( empty = Items.WATERING_CAN1_5333, full = Items.WATERING_CAN8_5340),
            WATERING_CAN2( empty = Items.WATERING_CAN2_5334, full = Items.WATERING_CAN8_5340),
            WATERING_CAN3( empty = Items.WATERING_CAN3_5335, full = Items.WATERING_CAN8_5340),
            WATERING_CAN4( empty = Items.WATERING_CAN4_5336, full = Items.WATERING_CAN8_5340),
            WATERING_CAN5( empty = Items.WATERING_CAN5_5337, full = Items.WATERING_CAN8_5340),
            WATERING_CAN6( empty = Items.WATERING_CAN6_5338, full = Items.WATERING_CAN8_5340),
            WATERING_CAN7( empty = Items.WATERING_CAN7_5339, full = Items.WATERING_CAN8_5340),
            FISHBOWL(      empty = Items.FISHBOWL_6667,      full = Items.FISHBOWL_6668),
            KETTLE(        empty = Items.KETTLE_7688,        full = Items.FULL_KETTLE_7690),
            ENCHANTED_VIAL(empty = Items.ENCHANTED_VIAL_731, full = Items.HOLY_WATER_732),
            CUP(           empty = Items.EMPTY_CUP_1980,     full = Items.CUP_OF_WATER_4458);

            companion object {
                private val productOfFill = values().associate { it.empty to it.full }
                fun forId(id: Int): Int = productOfFill[id]!!
                fun emptyContains(id: Int): Boolean = productOfFill.contains(id)
            }
        }

        private val GLASS_WEEDS = setOf(Items.SODA_ASH_1781, Items.SEAWEED_401, Items.SWAMP_WEED_10978)
        private val SAND_SOURCES = intArrayOf(Items.BUCKET_OF_SAND_1783, Items.SANDBAG_9943)

        private val accepted = setOf(
            Consumables.PRAYER,
            Consumables.PRAYERMIX,
            Consumables.RESTORE,
            Consumables.RESTO_MIX,
            Consumables.SUPER_RESTO,
            Consumables.SUPER_RESTO_MIX,
            Consumables.ENERGY,
            Consumables.SUPER_ENERGY,
            Consumables.SC_PRAYER
        )

        //{skill, currentStringId, realStringId}
        private val statFields = arrayOf(
            intArrayOf(Skills.ATTACK      ,  1,  2),
            intArrayOf(Skills.HITPOINTS   ,  5,  6),
            intArrayOf(Skills.MINING      ,  9, 10),
            intArrayOf(Skills.STRENGTH    , 13, 14),
            intArrayOf(Skills.AGILITY     , 17, 18),
            intArrayOf(Skills.SMITHING    , 21, 22),
            intArrayOf(Skills.DEFENCE     , 25, 26),
            intArrayOf(Skills.HERBLORE    , 29, 30),
            intArrayOf(Skills.FISHING     , 33, 34),
            intArrayOf(Skills.RANGE       , 37, 38),
            intArrayOf(Skills.THIEVING    , 41, 42),
            intArrayOf(Skills.COOKING     , 45, 46),
            intArrayOf(Skills.PRAYER      , 49, 50),
            intArrayOf(Skills.CRAFTING    , 53, 54),
            intArrayOf(Skills.FIREMAKING  , 57, 58),
            intArrayOf(Skills.MAGIC       , 61, 62),
            intArrayOf(Skills.FLETCHING   , 65, 66),
            intArrayOf(Skills.WOODCUTTING , 69, 70),
            intArrayOf(Skills.RUNECRAFTING, 73, 74),
            intArrayOf(Skills.SLAYER      , 77, 78),
            intArrayOf(Skills.FARMING     , 81, 82),
            intArrayOf(Skills.CONSTRUCTION, 85, 86),
            intArrayOf(Skills.HUNTER      , 89, 90),
            intArrayOf(Skills.SUMMONING   , 93, 94),
        )
    }
}