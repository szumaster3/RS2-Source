package core.game.node.entity.combat.graves

import com.google.gson.JsonArray
import com.google.gson.JsonObject
import content.data.GameAttributes
import core.api.*
import core.game.node.Node
import core.game.node.entity.combat.ImpactHandler
import core.game.node.entity.player.Player
import core.game.node.entity.player.info.Rights
import core.game.node.entity.skill.Skills
import core.game.node.item.Item
import core.game.world.map.Location
import core.game.world.map.zone.ZoneRestriction
import core.ServerStore
import core.game.interaction.InteractionListener
import core.game.interaction.IntType
import core.game.interaction.QueueStrength
import core.game.system.command.Privilege
import core.game.world.map.zone.impl.WildernessZone
import core.game.world.repository.Repository
import core.tools.secondsToTicks
import core.tools.colorize
import shared.consts.*
import kotlin.math.min

class GraveController : PersistWorld, TickListener, InteractionListener, Commands {
    override fun defineListeners() {
        on(GraveType.ids, IntType.NPC, "read", handler = this::onGraveReadOption)
        on(GraveType.ids, IntType.NPC, "bless", handler = this::onGraveBlessed)
        on(GraveType.ids, IntType.NPC, "repair", handler = this::onGraveRepaired)
        on(GraveType.ids, IntType.NPC, "demolish", handler = this::onGraveDemolished)
    }

    override fun defineCommands() {
        define("forcegravedeath", Privilege.ADMIN, "", "Forces a death that should produce a grave.") { player, _ ->
            player.details.rights = Rights.REGULAR_PLAYER
            setAttribute(player, GameAttributes.TUTORIAL_COMPLETE, true)
            player.impactHandler.manualHit(player, player.skills.lifepoints, ImpactHandler.HitsplatType.NORMAL)
            notify(player, "Grave created at ${player.getAttribute("/save:original-loc", player.location)}")
            queueScript(player, 15, QueueStrength.SOFT) { stage: Int ->
                player.details.rights = Rights.ADMINISTRATOR
                sendMessage(player, "Rights restored")
                return@queueScript stopExecuting(player)
            }
        }
    }


    override fun tick() {
        for (grave in activeGraves.values.toTypedArray()) {
            if (grave.ticksRemaining == -1) return

            if (grave.ticksRemaining == secondsToTicks(30) || grave.ticksRemaining == secondsToTicks(90)) {
                grave.transform(grave.id + 1)
            }

            if (grave.ticksRemaining == 0) {
                grave.collapse()
            }

            grave.ticksRemaining--
        }
    }

    private fun onGraveReadOption(player: Player, node: Node) : Boolean {
        val grave = node as? Grave ?: return false

        var isGraniteBackground = false

        when (grave.type) {
            in GraveType.SMALL_GS..GraveType.ANGEL_DEATH -> isGraniteBackground = true
            else -> {}
        }

        if (isGraniteBackground)
            setVarbit(player, Vars.VARBIT_IFACE_GRAVE_DISPLAY_4191, 1)
        else
            setVarbit(player, Vars.VARBIT_IFACE_GRAVE_DISPLAY_4191, 0)

        openInterface(player, Components.GRAVESTONE_266)
        sendString(player, grave.retrieveFormattedText(), Components.GRAVESTONE_266, 23)

        sendMessage(player, "It looks like it'll survive another ${grave.getFormattedTimeRemaining()}.")
        if (player.details.uid == grave.ownerUid) {
            sendMessage(player, "Isn't there something a bit odd about reading your own gravestone?")
        }
        return true
    }

    private fun onGraveBlessed(player: Player, node: Node) : Boolean {
        val g = node as? Grave ?: return false

        if (getAttribute(g, "blessed", false)) {
            sendMessage(player, "This grave has already been blessed.")
            return true
        }
        if (player.details.uid == g.ownerUid) {
            sendMessage(player, "The gods don't seem to approve of people attempting to bless their own gravestones.")
            return true
        }
        if (getStatLevel(player, Skills.PRAYER) < 70) {
            sendMessage(player, "You need a Prayer level of 70 to bless a grave.")
            return true
        }

        val blessAmount = min(60, player.skills.prayerPoints.toInt() - 10)

        if (blessAmount <= 0) {
            sendMessage(player, "You need to recharge your Prayer at an altar.")
            return true
        }

        g.addTime(secondsToTicks(blessAmount * 60))
        player.skills.prayerPoints -= blessAmount
        setAttribute(g, "blessed", true)

        playAudio(player, Sounds.PRAYER_RECHARGE_2674)
        animate(player, Animations.HUMAN_PRAY_645)

        val gOwner = Repository.uid_map[g.ownerUid]
        if (gOwner != null) {
            sendMessage(gOwner, colorize("%RYour grave has been blessed."))
        }
        return true
    }

    private fun onGraveRepaired(player: Player, node: Node) : Boolean {
        val g = node as? Grave ?: return false

        if (getAttribute(g, "repaired", false)) {
            sendMessage(player, "This grave has already been repaired.")
            return true
        }

        if (getStatLevel(player, Skills.PRAYER) < 2) {
            sendMessage(player, "You need a Prayer level of 2 to bless a grave.")
            return true
        }

        if (player.skills.prayerPoints < 1.0) {
            sendMessage(player, "You do not have enough prayer points to do that.")
            return true
        }

        val restoreAmount = min(5, player.skills.prayerPoints.toInt())
        g.addTime(secondsToTicks(restoreAmount * 60))
        player.skills.prayerPoints -= restoreAmount
        setAttribute(g, "repaired", true)
        playAudio(player, Sounds.PRAYER_RECHARGE_2674)
        animate(player, Animations.HUMAN_PRAY_645)
        return true
    }

    private fun onGraveDemolished(player: Player, node: Node) : Boolean {
        val g = node as? Grave ?: return false

        if (player.details.uid != g.ownerUid) {
            sendMessage(player, "You cannot demolish someone else's gravestone!")
            return true
        }

        g.demolish()
        return true
    }

    override fun save() {
        serializeToServerStore()
    }

    override fun parse() {
        deserializeFromServerStore()
    }

    companion object {
        val activeGraves = HashMap<Int, Grave>()
        var childCounter = 0
        val ATTR_GTYPE = "/save:gravetype"

        @JvmStatic fun produceGrave(type: GraveType): Grave {
            val g = Grave()
            g.configureType(type)
            return g
        }

        @JvmStatic fun shouldCrumble(item: Int) : Boolean {
            when (item) {
                Items.ECTOPHIAL_4251 -> return true
                in Items.SMALL_POUCH_5509..Items.GIANT_POUCH_5515 -> return true
            }

            return itemDefinition(item).hasAction("destroy")
        }

        @JvmStatic fun shouldRelease(item: Int) : Boolean {
            when (item) {
                Items.CHINCHOMPA_9976 -> return true
                Items.CHINCHOMPA_10033 -> return true
                in Items.BABY_IMPLING_JAR_11238..Items.DRAGON_IMPLING_JAR_11257 -> return itemDefinition(item).isUnnoted
            }

            return false
        }

        @JvmStatic fun checkTransform(item: Item) : Item {
            if (item.hasItemPlugin())
                return item.plugin.getDeathItem(item)
            return item
        }

        @JvmStatic fun allowGenerate(player: Player) : Boolean {
            if (player.skullManager.isSkulled)
                return false
            if (player.skullManager.isWilderness)
                return false
            if (WildernessZone.isInZone(player))
                return false
            if (player.zoneMonitor.isRestricted(ZoneRestriction.GRAVES))
                return false
            return true
        }

        @JvmStatic fun getGraveType(player: Player) : GraveType {
            return GraveType.values()[getAttribute(player, ATTR_GTYPE, 0)]
        }

        @JvmStatic fun updateGraveType(player: Player, type: GraveType) {
            setAttribute(player, ATTR_GTYPE, type.ordinal)
        }

        @JvmStatic fun hasGraveAt(loc: Location) : Boolean {
            return activeGraves.values.toTypedArray().any { it.location == loc }
        }

        fun serializeToServerStore() {
            val archive = ServerStore.getArchive("active-graves") as JsonObject

            val keysToRemove = archive.keySet().toList()
            for (key in keysToRemove) {
                archive.remove(key)
            }

            for ((uid, grave) in activeGraves) {
                val g = JsonObject()
                g.addProperty("ticksRemaining", grave.ticksRemaining)
                g.addProperty("location", grave.location.toString())
                g.addProperty("type", grave.type.ordinal)
                g.addProperty("username", grave.ownerUsername)

                val items = JsonArray()
                for (item in grave.getItems()) {
                    val i = JsonObject()
                    i.addProperty("id", item.id)
                    i.addProperty("amount", item.amount)
                    i.addProperty("charge", item.charge)
                    items.add(i)
                }
                g.add("items", items)

                archive.add(uid.toString(), g)
            }
        }

        fun deserializeFromServerStore() {
            val archive = ServerStore.getArchive("active-graves") as JsonObject
            for ((key, value) in archive.entrySet()) {
                val g = value.asJsonObject
                val uid = key.toInt()
                val type = g.get("type").asInt
                val ticks = g.get("ticksRemaining").asInt
                val location = Location.fromString(g.get("location").asString)
                val username = g.get("username").asString

                val itemsRaw = g.getAsJsonArray("items")
                val items = ArrayList<Item>()
                for (itemRaw in itemsRaw) {
                    val item = itemRaw.asJsonObject
                    val id = item.get("id").asInt
                    val amount = item.get("amount").asInt
                    val charge = item.get("charge").asInt
                    items.add(Item(id, amount, charge))
                }

                val grave = produceGrave(GraveType.values()[type])
                grave.setupFromJsonParams(uid, ticks, location, items.toTypedArray(), username)
                activeGraves[uid] = grave
            }
        }
    }
}