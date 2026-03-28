package content.global.travel.canoe

import content.data.skill.SkillingTool
import core.api.*
import core.game.interaction.*
import core.game.node.entity.player.Player
import core.game.node.entity.skill.Skills
import core.game.node.scenery.SceneryBuilder
import core.game.system.task.Pulse
import core.game.world.map.Location
import core.game.world.update.flag.context.Animation
import core.tools.RandomFunction
import shared.consts.*
import kotlin.math.abs

class CanoePlugin : InteractionListener, InterfaceListener {
    companion object {
        // Attributes.
        const val CANOE_STATION_VARBIT = "canoeStationVarbit"
        const val CANOE_SELECTED = "canoeSelected"

        // Interfaces.
        const val IFACE_CANOE_SHAPING = Components.CANOE_52
        const val IFACE_CANOE_DEST = Components.CANOE_STATIONS_MAP_53
        const val IFACE_CANOE_TRAVEL = Components.CANOE_TRAVEL_758

        // Components for shaping.
        val SHAPING_SILHOUETTE = arrayOf(0, 9, 10, 8)
        val SHAPING_TEXT = arrayOf(0, 3, 2, 5)
        val SHAPING_BUTTONS = arrayOf(30, 31, 32, 33)

        // Destination interface components.
        private val DEST_BUTTONS = arrayOf(47, 48, 3, 6, 49)
        private val DEST_HIDE_ROW = arrayOf(10, 11, 12, 20, 18)

        // Animations.
        private val TREE_FALLING_ANIM = Animation(Animations.CANOE_TREE_FALL_3304)
        private val PLAYER_PUSHING_ANIM = Animation(Animations.HUMAN_PUSH_CANOE_3301)
        private val PUSHING_ANIM = Animation(Animations.CANOE_TREE_FALL_3304)
        private val SINKING_ANIM = Animation(Animations.CANOE_SINK_3305)

        private val TRAVEL_ANIMATIONS = arrayOf(
            arrayOf(0, 9890, 9889, 9888, 9887),
            arrayOf(9906, 0, 9893, 9892, 9891),
            arrayOf(9904, 9905, 0, 9895, 9894),
            arrayOf(9901, 9902, 9903, 0, 9896),
            arrayOf(9897, 9898, 9899, 9900, 0),
        )

        fun getAxeAnimation(axe: SkillingTool) = when (axe) {
            SkillingTool.BRONZE_AXE -> Animation(Animations.BRONZE_AXE_6744)
            SkillingTool.IRON_AXE -> Animation(Animations.IRON_AXE_6743)
            SkillingTool.STEEL_AXE -> Animation(Animations.STEEL_AXE_ALT2_6742)
            SkillingTool.BLACK_AXE -> Animation(Animations.BLACK_AXE_6741)
            SkillingTool.MITHRIL_AXE -> Animation(Animations.MITHRIL_AXE_6740)
            SkillingTool.ADAMANT_AXE -> Animation(Animations.ADAMANT_AXE_6739)
            SkillingTool.RUNE_AXE -> Animation(Animations.RUNE_AXE_6738)
            SkillingTool.DRAGON_AXE -> Animation(Animations.DRAGON_AXE_6745)
            else -> Animation(axe.animation)
        }

        fun checkSuccess(player: Player, canoe: CanoeType, axe: SkillingTool): Boolean {
            val level = getDynLevel(player, Skills.WOODCUTTING) + getFamiliarBoost(player, Skills.WOODCUTTING)
            val hostRatio = RandomFunction.randomDouble(100.0)
            val lowMod = if (axe == SkillingTool.BLACK_AXE) canoe.tierModLow / 2 else canoe.tierModLow
            val highMod = if (axe == SkillingTool.BLACK_AXE) canoe.tierModHigh / 2 else canoe.tierModHigh
            val low = canoe.baseLow + axe.ordinal * lowMod
            val high = canoe.baseHigh + axe.ordinal * highMod
            val chance = RandomFunction.getSkillSuccessChance(low, high, level)
            return hostRatio < chance
        }

        fun chopDuration(level: Int, minTicks: Int = 5, maxTicks: Int = 20): Int {
            return (maxTicks * ((99 - level) / 98.0) + minTicks * ((level - 1) / 98.0)).toInt()
        }

        fun animateCanoePush(player: Player, scenery: core.game.node.scenery.Scenery, canoe: CanoeType) {
            queueScript(player, 0, QueueStrength.SOFT) { stage ->
                when (stage) {
                    0 -> {
                        animate(player, PLAYER_PUSHING_ANIM)
                        animateScenery(player, scenery, PUSHING_ANIM.id)
                        return@queueScript delayScript(player, PLAYER_PUSHING_ANIM.duration)
                    }

                    1 -> {
                        setVarbit(player, scenery.definition.configFile!!, canoe.floating.varbit)
                        resetAnimator(player)
                        return@queueScript stopExecuting(player)
                    }

                    else -> return@queueScript stopExecuting(player)
                }
            }
        }
    }

    enum class Station(val sceneryId: Int, val varbit: Int) {
        TREE_STANDING(Scenery.CANOE_STATION_12144, 0),
        TREE_FALLING(Scenery.CANOE_STATION_12145, 9),
        TREE_FALLEN(Scenery.CANOE_STATION_12146, 10),
        TREE_SHAPED_LOG(Scenery.CANOE_STATION_12147, 1),
        TREE_SHAPED_DUGOUT(Scenery.CANOE_STATION_12148, 2),
        TREE_SHAPED_STABLE_DUGOUT(Scenery.CANOE_STATION_12149, 3),
        TREE_SHAPED_WAKA(Scenery.CANOE_STATION_12150, 4),
        CANOE_PUSHING_LOG(Scenery.CANOE_STATION_12151, 5),
        CANOE_PUSHING_DUGOUT(Scenery.CANOE_STATION_12152, 6),
        CANOE_PUSHING_STABLE_DUGOUT(Scenery.CANOE_STATION_12153, 7),
        CANOE_PUSHING_WAKA(Scenery.CANOE_STATION_12154, 8),
        CANOE_FLOATING_LOG(Scenery.CANOE_STATION_12155, 11),
        CANOE_FLOATING_DUGOUT(Scenery.CANOE_STATION_12156, 12),
        CANOE_FLOATING_STABLE_DUGOUT(Scenery.CANOE_STATION_12157, 13),
        CANOE_FLOATING_WAKA(Scenery.CANOE_STATION_12158, 14),
        CANOE_SINKING_LOG(Scenery.A_SINKING_CANOE_12159, 0),
        CANOE_SINKING_DUGOUT(Scenery.A_SINKING_CANOE_12160, 0),
        CANOE_SINKING_STABLE_DUGOUT(Scenery.A_SINKING_CANOE_12161, 0),
        CANOE_SINKING_WAKA(Scenery.A_SINKING_CANOE_12162, 0);

        companion object {
            val stationID = values().map { it.sceneryId }.toIntArray()
        }
    }

    enum class CanoeStation(val region: Int, val varbit: Int, val chopLocation: Location, val floatLocation: Location, val facing: Location, val sinkLocation: Location, val destination: Location, val destName: String) {
        LUMBRIDGE(Regions.LUMBRIDGE_12850, Vars.VARBIT_CANOE_STATE_LUMBRIDGE_1839, Location(3243, 3235), Location(3243, 3237), Location(-1, 0), Location(3239, 3242, 0), Location(3240, 3242, 0), "Lumbridge"),
        CHAMPIONS(12852, Vars.VARBIT_CANOE_STATE_CHAMPIONS_GUILD_1840, Location(3204, 3343), Location(3202, 3343), Location(0, -1), Location(3199, 3344, 0), Location(3199, 3344, 0), "the Champion's Guild"),
        BARBARIAN(12341, Vars.VARBIT_CANOE_STATE_BARBARIAN_VILLAGE_1841, Location(3112, 3409), Location(3112, 3411), Location(-1, 0), Location(3109, 3411, 0), Location(3109, 3415), "Barbarian Village"),
        EDGEVILLE(12342, Vars.VARBIT_CANOE_STATE_EDGEVILLE_1842, Location(3132, 3508), Location(3132, 3510), Location(-1, 0), Location(3132, 3510, 0), Location(3129, 3501, 0), "Edgeville"),
        WILDERNESS(12603, 0, Location(0, 0), Location(0, 0), Location(0, 0), Location(3142, 3795, 0), Location(3141, 3796, 0), "the Wilderness Pond");

        companion object {
            private val map = values().associateBy { it.region }
            fun get(loc: Location) = map[loc.regionId]!!
        }
    }

    enum class CanoeType(val level: Int, val xp: Double, val maxDist: Int, val baseLow: Double, val baseHigh: Double, val tierModLow: Double, val tierModHigh: Double, val treeShaped: Station, val pushing: Station, val floating: Station, val sinking: Station) {
        LOG(12, 30.0, 1, 32.0, 100.0, 16.0, 50.0,
            Station.TREE_SHAPED_LOG,
            Station.CANOE_PUSHING_LOG,
            Station.CANOE_FLOATING_LOG,
            Station.CANOE_SINKING_LOG
        ),
        DUGOUT(27, 60.0, 2, 16.0, 50.0, 8.0, 25.0,
            Station.TREE_SHAPED_DUGOUT,
            Station.CANOE_PUSHING_DUGOUT,
            Station.CANOE_FLOATING_DUGOUT,
            Station.CANOE_SINKING_DUGOUT
        ),
        STABLE_DUGOUT(42, 90.0, 3, 8.0, 25.0, 4.0, 12.5,
            Station.TREE_SHAPED_STABLE_DUGOUT,
            Station.CANOE_PUSHING_STABLE_DUGOUT,
            Station.CANOE_FLOATING_STABLE_DUGOUT,
            Station.CANOE_SINKING_STABLE_DUGOUT
        ),
        WAKA(57, 150.0, 4, 4.0, 12.5, 2.0, 6.25,
            Station.TREE_SHAPED_WAKA,
            Station.CANOE_PUSHING_WAKA,
            Station.CANOE_FLOATING_WAKA,
            Station.CANOE_SINKING_WAKA
        );

        companion object {
            val map = values().associateBy { it.ordinal }
        }
    }

    override fun defineDestinationOverrides() {
        setDest(IntType.SCENERY, Station.stationID, "chop-down") { _, node ->
            return@setDest CanoeStation.get(node.location).chopLocation
        }

        setDest(IntType.SCENERY, Station.stationID, "shape-canoe", "float canoe", "float log", "float waka") { _, node ->
            return@setDest CanoeStation.get(node.location).floatLocation
        }
    }

    override fun defineListeners() {
        /*
         * Handles chopping down a canoe tree.
         */

        on(Station.stationID, IntType.SCENERY, "chop-down") { player, node ->
            val station = CanoeStation.get(node.location)
            val axe = SkillingTool.getAxe(player)
            if (getStatLevel(player, Skills.WOODCUTTING) < 12) {
                sendMessage(player, "You need at least level 12 Woodcutting to chop this tree.")
                return@on true
            }
            if (axe == null) {
                sendMessage(player, "You do not have a usable axe.")
                return@on true
            }
            if (!clockReady(player, Clocks.SKILLING)) return@on true

            player.animate(Animation.create(axe.animation))
            val varbit = station.varbit
            queueScript(player, chopDuration(getStatLevel(player, Skills.WOODCUTTING)), QueueStrength.WEAK) { stage ->
                when (stage) {
                    0 -> {
                        playGlobalAudio(player.location, Sounds.TREE_FALLING_2734)
                        setVarbit(player, varbit, Station.TREE_FALLING.varbit)
                        animateScenery(player, node.asScenery(), TREE_FALLING_ANIM.id)
                        return@queueScript delayScript(player, 1)
                    }

                    1 -> {
                        setVarbit(player, varbit, Station.TREE_FALLEN.varbit)
                        return@queueScript delayScript(player, TREE_FALLING_ANIM.duration)
                    }

                    2 -> {

                        return@queueScript stopExecuting(player)
                    }

                    else -> return@queueScript stopExecuting(player)
                }
            }
            return@on true
        }

        /*
         * Handles opening the canoe shaping interface.
         */

        on(Station.stationID, IntType.SCENERY, "shape-canoe") { player, node ->
            val station = CanoeStation.get(node.location)
            setAttribute(player, CANOE_STATION_VARBIT, station.varbit)
            face(player, station.floatLocation.transform(station.facing))
            openInterface(player, IFACE_CANOE_SHAPING)
            openOverlay(player, Components.BLACK_OVERLAY_333)
            return@on true
        }

        /*
         * Handles floating a canoe into the water.
         */

        on(Station.stationID, IntType.SCENERY, "float canoe", "float log", "float waka") { player, node ->
            val station = CanoeStation.get(node.location)
            val canoe = CanoeType.map[getAttribute(player, CANOE_SELECTED, 0)]!!
            setVarbit(player, station.varbit, canoe.pushing.varbit)
            lock(player, PLAYER_PUSHING_ANIM.duration)
            playAudio(player, Sounds.CANOE_ROLL_2731)
            face(player, station.floatLocation.transform(station.facing))
            animateCanoePush(player, node.asScenery(), canoe)
            return@on true
        }

        /*
         * Handles starting the paddle action to open destination interface.
         */

        on(Station.stationID, IntType.SCENERY, "paddle log", "paddle canoe") { player, _ ->
            closeInterface(player)
            openInterface(player, IFACE_CANOE_DEST)
            openOverlay(player, Components.BLACK_OVERLAY_333)
            return@on true
        }
    }

    override fun defineInterfaceListeners() {
        /*
         * Handles pre-config the shape canoe interface.
         */

        onOpen(IFACE_CANOE_SHAPING) { player, _ -> // 10217
            CanoeType.values().forEach {
                if (getStatLevel(player, Skills.WOODCUTTING) >= it.level && it != CanoeType.LOG) {
                    setComponentVisibility(
                        player,
                        IFACE_CANOE_SHAPING,
                        SHAPING_SILHOUETTE[it.ordinal],
                        true
                    )
                    setComponentVisibility(
                        player,
                        IFACE_CANOE_SHAPING,
                        SHAPING_TEXT[it.ordinal],
                        false
                    )
                }
            }
            return@onOpen true
        }

        /*
         * Handles canoe shaping buttons.
         */

        on(IFACE_CANOE_SHAPING) { player, _, _, buttonID, _, _ ->
            closeInterface(player)
            val canoe = CanoeType.map[SHAPING_BUTTONS.indexOf(buttonID)]!!
            val varbit = getAttribute(player, CANOE_STATION_VARBIT, 1839)
            val axe =
                SkillingTool.getAxe(player)
                    ?: run {
                        sendMessage(player, "You do not have a usable axe.")
                        return@on true
                    }

            lock(player, 4)
            animate(player, getAxeAnimation(axe))
            submitIndividualPulse(
                player,
                object : Pulse(3) {
                    override fun pulse(): Boolean {
                        if (checkSuccess(player, canoe, axe)) {
                            setAttribute(player, CANOE_SELECTED, SHAPING_BUTTONS.indexOf(buttonID))
                            setVarbit(player, varbit, canoe.treeShaped.varbit)
                            rewardXP(player, Skills.WOODCUTTING, canoe.xp)
                            unlock(player)
                            return true
                        }
                        animate(player, getAxeAnimation(axe))
                        return false
                    }
                }
            )
            return@on true
        }

        /*
         * Handles visibility of canoe destination interface
         * depending on canoe type and distance.
         */

        onOpen(IFACE_CANOE_DEST) { player, component ->
            val canoe = CanoeType.map[getAttribute(player, CANOE_SELECTED, 0)]!!
            val origin = CanoeStation.get(player.location)
            CanoeStation.values().forEach { station ->
                val hide =
                    when {
                        station == CanoeStation.WILDERNESS && canoe != CanoeType.WAKA -> true
                        station == origin -> false
                        abs(station.ordinal - origin.ordinal) <= canoe.maxDist -> false
                        else -> true
                    }
                setComponentVisibility(player, component.id, DEST_HIDE_ROW[station.ordinal], hide)
            }
            return@onOpen true
        }

        /*
         * Handles canoe travel to selected destination,
         * including animations, locking, and teleportation.
         */

        on(IFACE_CANOE_DEST) { player, _, _, buttonID, _, _ ->
            for (i in intArrayOf(31,32,33,34,41)) {
                sendAnimationOnInterface(player, 3308, IFACE_CANOE_DEST, i)
            }
            val origin = CanoeStation.get(player.location)
            val destination =
                DEST_BUTTONS.indexOf(buttonID).let { index ->
                    when (index) {
                        0 -> CanoeStation.LUMBRIDGE
                        1 -> CanoeStation.CHAMPIONS
                        2 -> CanoeStation.BARBARIAN
                        3 -> CanoeStation.EDGEVILLE
                        4 -> CanoeStation.WILDERNESS
                        else -> origin
                    }
                }

            if (player.familiarManager.hasFamiliar() || player.familiarManager.hasPet()) {
                sendMessage(player, "You cannot take a canoe with a pet or familiar with you. It is not safe!")
                return@on true
            }

            val animId = TRAVEL_ANIMATIONS[origin.ordinal][destination.ordinal]
            val travelAnim = Animation(animId)
            val fadeAnim = Animation(Components.FADE_FROM_BLACK_170)
            val totalLock = travelAnim.duration + fadeAnim.duration + 1

            registerLogoutListener(player, "canoe-travel") {
                player.location = player.location
            }

            lock(player, totalLock)
            submitIndividualPulse(
                player,
                object : Pulse() {
                    var counter = 0

                    override fun pulse(): Boolean {
                        when (counter++) {
                            0 -> {
                                // Start travel animation.
                                openInterface(player, IFACE_CANOE_TRAVEL)
                                openOverlay(player, Components.BLACK_OVERLAY_333)
                                animateInterface(player, IFACE_CANOE_TRAVEL, 3, animId)
                                setMinimapState(player, 2)
                                removeTabs(player, 0, 1, 2, 3, 4, 5, 6, 11, 12)
                                return false
                            }
                            travelAnim.duration + 1 -> {
                                // Teleport player to destination.
                                teleport(player, destination.destination)
                                closeInterface(player)
                                closeOverlay(player)
                                openOverlay(player, Components.FADE_FROM_BLACK_170)
                                return false
                            }
                            travelAnim.duration + fadeAnim.duration + 3 -> {
                                // Finish travel.
                                unlock(player)
                                restoreTabs(player)
                                setMinimapState(player, 0)

                                val sinking =
                                    SceneryBuilder.add(
                                        core.game.node.scenery.Scenery(
                                            Scenery.A_SINKING_CANOE_12159,
                                            destination.sinkLocation,
                                            1
                                        ),
                                        10
                                    )
                                        .asScenery()
                                animateScenery(sinking, SINKING_ANIM.id)
                                clearLogoutListener(player, "canoe-travel")
                                sendMessage(player, "You arrive at ${destination.destName}.")
                                sendMessage(player, "Your canoe sinks from the long journey.")
                                if (destination == CanoeStation.WILDERNESS) {
                                    sendMessage(
                                        player,
                                        "There are no trees nearby to make a new canoe. Guess you're walking."
                                    )
                                }

                                setVarbit(player, origin.varbit, 0)
                                return true
                            }
                            else -> return false
                        }
                    }
                }
            )

            return@on true
        }
    }
}
