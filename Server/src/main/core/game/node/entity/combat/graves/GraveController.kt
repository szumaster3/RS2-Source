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
import core.game.world.update.flag.context.Animation
import core.tools.secondsToTicks
import core.tools.colorize
import shared.consts.*
import kotlin.math.min

class GraveController : PersistWorld, TickListener, InteractionListener, Commands {
    override fun defineListeners() {
        on(GraveType.ids, IntType.NPC, "read", handler = this::onRead)
        on(GraveType.ids, IntType.NPC, "bless", handler = this::onBless)
        on(GraveType.ids, IntType.NPC, "repair", handler = this::onRepair)
        on(GraveType.ids, IntType.NPC, "demolish", handler = this::onDemolish)
    }

    override fun defineCommands() {
        define("forcegravedeath", Privilege.ADMIN, "", "Forces a death that should produce a grave.") { player, _ ->
            player.details.rights = Rights.REGULAR_PLAYER
            setAttribute(player, GameAttributes.TUTORIAL_COMPLETE, true)
            impact(player, player.skills.lifepoints, ImpactHandler.HitsplatType.NORMAL)
            notify(player, "Grave created at ${player.getAttribute(GameAttributes.ORIGINAL_LOCATION, player.location)}")
            queueScript(player, 15, QueueStrength.SOFT) {
                player.details.rights = Rights.ADMINISTRATOR
                sendMessage(player, "Rights restored.")
                return@queueScript stopExecuting(player)
            }
        }
    }

    override fun tick() {
        for (grave in activeGraves.values) {
            if (grave.ticksRemaining == -1) continue
            if (grave.ticksRemaining == secondsToTicks(30) || grave.ticksRemaining == secondsToTicks(90)) {
                grave.transform(grave.id + 1)
            }
            if (grave.ticksRemaining <= 0) {
                grave.collapse()
                continue
            }
            grave.ticksRemaining--
        }
    }

    private fun onRead(player: Player, node: Node): Boolean {
        val grave = node as? Grave ?: return false

        val isGranite = grave.type !in GraveType.MemorialPlaque..GraveType.Flag

        setVarbit(player, Vars.VARBIT_IFACE_GRAVE_DISPLAY_4191, if (isGranite) 1 else 0)

        openInterface(player, Components.GRAVESTONE_266)
        sendString(player, grave.retrieveFormattedText(), Components.GRAVESTONE_266, 23)
        val baseMessage = "It looks like it'll survive another ${grave.getFormattedTimeRemaining()}."

        if (player.details.uid == grave.ownerUid) {
            sendMessage(player, "$baseMessage Isn't there something a bit odd about reading your own gravestone?")
        } else {
            sendMessage(player, baseMessage)
        }
        return true
    }

    private fun onBless(player: Player, node: Node): Boolean {
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

        val maxDrain = 60
        val available = player.skills.prayerPoints.toInt() - 10
        val amount = min(maxDrain, available)

        if (amount <= 0) {
            sendMessage(player, "You need to recharge your Prayer at an altar.")
            return true
        }

        g.addTime(secondsToTicks(amount * 60))
        player.skills.prayerPoints -= amount

        setAttribute(g, "blessed", true)

        playAudio(player, Sounds.PRAYER_RECHARGE_2674)
        animate(player, Animations.HUMAN_PRAY_645)

        Repository.uid_map[g.ownerUid]?.let {
            sendMessage(it, colorize("%RYour grave has been blessed."))
        }

        return true
    }

    private fun onRepair(player: Player, node: Node): Boolean {
        val g = node as? Grave ?: return false

        if (getAttribute(g, "repaired", false)) {
            sendMessage(player, "This grave has already been repaired.")
            return true
        }

        if (getStatLevel(player, Skills.PRAYER) < 2) {
            sendMessage(player, "You need a Prayer level of 2 to repair a grave.")
            return true
        }

        if (player.skills.prayerPoints < 1.0) {
            sendMessage(player, "You need to recharge your Prayer at an altar.")
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

    private fun onDemolish(player: Player, node: Node): Boolean {
        val g = node as? Grave ?: return false

        if (player.details.uid != g.ownerUid) {
            sendMessage(player, "You cannot demolish this grave.")
            return true
        }

        // g.animate(Animation.create(Animations.GRAVE_BROKEN_7396))
        g.demolish()

        return true
    }

    override fun save() = serialize()
    override fun parse() = deserialize()

    companion object {

        val activeGraves = HashMap<Int, Grave>()
        val ATTR_GTYPE = "/save:gravetype"

        @JvmStatic
        fun produceGrave(type: GraveType): Grave {
            return Grave().apply { configureType(type) }
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

        @JvmStatic
        fun getGraveType(player: Player): GraveType {
            val index = getAttribute(player, ATTR_GTYPE, 0)
            return GraveType.values().getOrElse(index) { GraveType.DEFAULT }
        }

        @JvmStatic
        fun updateGraveType(player: Player, type: GraveType) {
            setAttribute(player, ATTR_GTYPE, type.ordinal)
        }

        @JvmStatic
        fun hasGraveAt(loc: Location): Boolean {
            return activeGraves.values.any { it.location == loc }
        }

        fun serialize() {
            val archive = ServerStore.getArchive("active-graves") as JsonObject
            archive.entrySet().clear()

            for ((uid, grave) in activeGraves) {
                val g = JsonObject()

                g.addProperty("ticksRemaining", grave.ticksRemaining)
                g.addProperty("location", grave.location.toString())
                g.addProperty("type", grave.type.ordinal)
                g.addProperty("username", grave.ownerUsername)

                val items = JsonArray()

                grave.getItems().forEach {
                    val i = JsonObject()
                    i.addProperty("id", it.id)
                    i.addProperty("amount", it.amount)
                    i.addProperty("charge", it.charge)
                    items.add(i)
                }

                g.add("items", items)
                archive.add(uid.toString(), g)
            }
        }

        fun deserialize() {
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