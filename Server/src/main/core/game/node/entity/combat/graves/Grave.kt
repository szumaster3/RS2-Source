package core.game.node.entity.combat.graves

import content.global.plugins.item.equipment.BarrowsEquipment
import core.api.clearHintIcon
import core.api.registerHintIcon
import core.api.sendMessage
import core.game.node.entity.npc.AbstractNPC
import core.game.node.entity.player.Player
import core.game.node.item.GroundItem
import core.game.node.item.GroundItemManager
import core.game.node.item.Item
import core.game.world.GameWorld
import core.game.world.map.Location
import core.game.world.repository.Repository
import core.game.world.update.flag.context.Animation
import core.plugin.Initializable
import core.tools.colorize
import core.tools.secondsToTicks
import core.tools.ticksToSeconds
import shared.consts.NPCs

@Initializable
class Grave : AbstractNPC {
    lateinit var type: GraveType
    private val items = ArrayList<GroundItem>()
    var ownerUsername: String = ""
    var ownerUid: Int = -1
    var ticksRemaining = -1

    constructor() : super(NPCs.GRAVESTONE_6571, Location.create(0, 0, 0), false)
    private constructor(id: Int, location: Location) : super(id, location)

    override fun construct(id: Int, location: Location, vararg objects: Any): AbstractNPC = Grave(id, location)

    override fun getIds(): IntArray = GraveType.ids

    private fun GraveType.npc(variant: Int): Int {
        val base = when (this) {
            GraveType.MemorialPlaque -> GraveType.npcMap.defaultInt
            else -> npcId
        }
        return base + variant
    }

    private fun transform() {
        when {
            ticksRemaining < 30 -> transform(type.npc(2))
            ticksRemaining < 90 -> transform(type.npc(1))
            else -> transform(type.npc(0))
        }
    }

    fun configureType(type: GraveType) {
        this.type = type
        transform(type.npc(0))
        this.ticksRemaining = secondsToTicks(type.duration * 60)
    }

    fun initialize(player: Player, location: Location, inventory: Array<Item>) {
        if (!GraveController.allowGenerate(player)) return

        this.ownerUid = player.details.uid
        this.ownerUsername = player.username
        this.location = player.getAttribute("/save:original-loc", location)
        this.isRespawn = false
        this.isWalks = false
        this.isNeverWalks = true

        for (item in inventory) {
            if (GraveController.shouldRelease(item.id)) {
                sendMessage(player, "Your ${item.name.lowercase().replace("jar", "")} has escaped.")
                continue
            }

            if (GraveController.shouldCrumble(item.id)) {
                sendMessage(player, "Your ${item.name.lowercase()} has crumbled to dust.")
                continue
            }

            val finalItem = if (BarrowsEquipment.isBarrowsItem(item.id) && !BarrowsEquipment.isFullyRepaired(item.id)) {
                BarrowsEquipment.graveDeathDurabilityReduction(item) ?: item
            } else {
                GraveController.checkTransform(item)
            }

            val gi = GroundItemManager.create(finalItem, this.location, player)
            gi.isRemainPrivate = true
            gi.decayTime = secondsToTicks(type.duration * 60)
            this.items.add(gi)
        }

        if (items.isEmpty()) {
            clear()
            return
        }

        this.init()
        // this.animate(Animation.create(7375))
        if (GraveController.activeGraves[ownerUid] != null) {
            val oldGrave = GraveController.activeGraves[ownerUid]
            oldGrave?.collapse()
        }

        GraveController.activeGraves[ownerUid] = this
        sendMessage(
            player,
            colorize("%RBecause of your current gravestone, you have ${type.duration} minutes to get your items back.")
        )
    }

    fun setupFromJsonParams(playerUid: Int, ticks: Int, location: Location, items: Array<Item>, username: String) {
        this.ownerUid = playerUid
        this.ticksRemaining = ticks
        this.location = location
        this.isRespawn = false
        this.isWalks = false
        this.isNeverWalks = true
        this.ownerUsername = username

        for (item in items) {
            val gi = GroundItemManager.create(item, location, playerUid, GameWorld.ticks + ticksRemaining)
            gi.isRemainPrivate = true
            this.items.add(gi)
        }

        this.transform()
        this.init()
    }

    override fun tick() {
        if (Repository.uid_map[ownerUid] != null) {
            val p = Repository.uid_map[ownerUid] ?: return
            registerHintIcon(p, this)
        }
    }

    fun addTime(ticks: Int) {
        ticksRemaining += ticks

        for (gi in items) {
            gi.decayTime = ticksRemaining
        }

        transform()
    }

    fun collapse() {
        // this.animate(Animation.create(7490))
        for (item in items) {
            GroundItemManager.destroy(item)
        }
        clear()
        GraveController.activeGraves.remove(ownerUid)
        if (Repository.uid_map[ownerUid] != null) {
            val p = Repository.uid_map[ownerUid] ?: return
            clearHintIcon(p)
        }
    }

    fun demolish() {
        val owner = Repository.uid_map[ownerUid] ?: return
        for (item in items) {
            if (!item.isRemoved)
                item.decayTime = secondsToTicks(45)
        }
        clear()
        sendMessage(owner, "It looks like it'll last another ${getFormattedTimeRemaining()}.")
        sendMessage(owner, "You demolish it anyway.")
        GraveController.activeGraves.remove(ownerUid)
        clearHintIcon(owner)
    }

    fun getItems() : Array<GroundItem> {
        return this.items.toTypedArray()
    }

    fun retrieveFormattedText(): String {
        return type.text
            .replace("@name", ownerUsername)
            .replace("@mins", getFormattedTimeRemaining())
    }

    fun getFormattedTimeRemaining(): String {
        val seconds = ticksToSeconds(ticksRemaining)
        val timeQty = if (seconds / 60 > 0) seconds / 60 else seconds
        val timeUnit = (if (seconds / 60 > 0) "minute" else "second") + if (timeQty > 1) "s" else ""
        return "$timeQty $timeUnit"
    }
}