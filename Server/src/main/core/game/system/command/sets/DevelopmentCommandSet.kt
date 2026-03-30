package core.game.system.command.sets

import content.data.GameAttributes
import content.data.RespawnPoint
import content.global.activity.jobs.JobManager
import content.global.activity.phoenix.PhoenixSpawnCutscene
import content.global.skill.summoning.SummoningPouch
import content.region.kandarin.baxtorian.BarbarianTraining
import content.region.kandarin.piscatoris.quest.phoenix.custcene.FuneralPyreCutscene
import content.region.kandarin.piscatoris.quest.phoenix.custcene.WoundedPhoenixCutscene
import content.region.kandarin.witchaven.quest.seaslug.cutscene.KennithCutscene
import content.region.other.tutorial_island.plugin.TutorialStage
import core.api.*
import core.cache.def.impl.NPCDefinition
import core.cache.def.impl.VarbitDefinition
import core.game.node.Node
import core.game.node.entity.combat.ImpactHandler.HitsplatType
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.HintIconManager
import core.game.node.entity.player.link.SpellBookManager
import core.game.node.entity.player.link.warning.WarningType
import core.game.node.entity.player.link.diary.DiaryType
import core.game.system.command.Privilege
import core.game.system.task.Pulse
import core.game.world.map.Location
import core.game.world.map.build.DynamicRegion
import core.game.world.repository.Repository
import core.net.packet.PacketWriteQueue
import core.net.packet.context.PlayerContext
import core.net.packet.out.ResetInterface
import core.plugin.Initializable
import core.tools.GREEN
import core.tools.RED
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import shared.consts.Items

@Initializable
class DevelopmentCommandSet : CommandSet(Privilege.ADMIN) {

    override fun defineCommands() {

        define("kennith",privilege = Privilege.ADMIN)  { player, args ->
            KennithCutscene(player).start()
        }
        define("phoenix1",privilege = Privilege.ADMIN)  { player, args ->
            WoundedPhoenixCutscene(player).start()
        }
        define("phoenix2",privilege = Privilege.ADMIN)  { player, args ->
            FuneralPyreCutscene(player).start()
        }
        define("phoenix3",privilege = Privilege.ADMIN)  { player, args ->
            PhoenixSpawnCutscene(player).start()
        }

        /*
         * Command for allowing to load a dynamic region and move to it.
         */

        define(
            name = "loadregion",
            privilege = Privilege.ADMIN,
            usage = "::loadregion <id>",
            description = "Load a dynamic region."
        ) { player, args ->

            if (args.size < 2) {
                sendMessage(player, "Usage: ::loadregion <region_id>")
                return@define
            }

            val regionId = args[1].toIntOrNull()
            if (regionId == null) {
                sendMessage(player, "Invalid region id.")
                return@define
            }

            val dyn = DynamicRegion.create(regionId)
            dyn.add(player)
            registerLogoutListener(player, "before-teleport") {
                player.location = Location.create(2500,2500,0)
            }
            teleport(player, dyn.baseLocation)

            sendMessage(player, "Dynamic region $regionId loaded.")
        }

        /*
         * Command for spawning items for summoning pouches creation.
         */

        define(
            name = "additempouch",
            privilege = Privilege.ADMIN,
            usage = "::additempouch <lt>slot id<gt>",
            description = "Adds all items needed to create summoning pouches for interface slot id."
        ) { player, args ->
            if (args.size < 2) {
                sendMessage(player, "Usage: ::additempouch slotId")
                return@define
            }

            val pouchId = args[1].toIntOrNull()
            if (pouchId == null) {
                player.debug("Pouch id must be a valid number.")
                return@define
            }

            val pouch = SummoningPouch.values().find { it.slot == pouchId }
            if (pouch == null) {
                sendMessage(player, "No pouch for id=$pouchId.")
                return@define
            }
            player.debug("----Pouch=[${pouch.pouchId}] for NPC=[${pouch.npcId}](Slot=${pouch.slot})-----")
            pouch.items.forEach { item ->
                player.inventory.add(item)
                player.debug("-> [${item.id}][${item.name}]:[${item.amount}]")
            }
        }

        define(name = "unlock", privilege = Privilege.ADMIN, usage = "::unlock", description = "") { p, _ ->
            p.unlock()
        }

        /*
         * Command for resetting warnings.
         */

        define(
            name = "resetwarnings",
            privilege = Privilege.ADMIN,
            usage = "::resetwarnings",
            description = "Resets all warnings"
        ) { player, _ ->
            val resetList = mutableListOf<WarningType>()

            WarningType.values.forEach { warning ->
                val currentValue = getVarbit(player, warning.varbit)
                if (currentValue != 0) {
                    setVarbit(player, warning.varbit, 0)
                    resetList.add(warning)
                }
            }

            if (resetList.isEmpty()) {
                player.debug("You don't have any.")
            } else {
                player.debug("Reset ${resetList.size} warnings:")
                resetList.forEach { w ->
                    player.debug(" - ${w.name}")
                }
            }
        }

        /*
         * Command to enable barehand fishing.
         */

        define(name = "barehand", Privilege.ADMIN) { p, _ ->
            val newState = !getAttribute(p, GameAttributes.BARBARIAN_BAREHAND_FISHING, false)
            setAttribute(p, GameAttributes.BARBARIAN_BAREHAND_FISHING, newState)
            p.savedData.activityData.isBarbarianFishingBarehand = newState
            p.debug("Barehand fishing ${if (newState) "$GREEN enabled" else "$RED disabled"}</col>.")
        }

        /*
         * Command to complete tutorial.
         */

        define(name = "exit", Privilege.ADMIN) { p, _ ->
            TutorialStage.completeTutorial(p)
            p.teleporter.send(Location.create(3233, 3230, 0))
        }

        /*
         * Command to send model on the interface.
         */

        define(
            name = "model",
            privilege = Privilege.ADMIN,
            usage = "::model <lt>interfaceId<gt> <lt>componentId<gt> <lt>modelId<gt> <lt>zoom<gt>",
            description = "Send a model on the interface component."
        ) { player, args ->
            if (args.size < 4) {
                return@define reject(player, "Usage: ::model interfaceId componentId modelId (optional zoom)")
            }

            val interfaceId = args[1].toIntOrNull()
            val componentId = args[2].toIntOrNull()
            val modelId = args[3].toIntOrNull()
            val zoom = args.getOrNull(4)?.toIntOrNull() ?: 800

            if (interfaceId == null || componentId == null || modelId == null) {
                return@define reject(player, "All arguments must be valid integers.")
            }

            player.packetDispatch.sendModelOnInterface(modelId, interfaceId, componentId, zoom)
            player.debug("model=[$modelId], iface=[$interfaceId], comp=[$componentId], zoom=[$zoom].")
        }

        /*
         * Command to send animation on the interface.
         */

        define(
            name = "com",
            privilege = Privilege.ADMIN,
            usage = "::com <lt>interfaceId<gt> <lt>animationId<gt> <lt>componentId<gt> <lt>loop (optional)<gt>",
            description = "Send an animation on the interface component. Use optional 'loop' to repeat.",
        ) { player, args ->
            if (args.size < 4) {
                return@define reject(player, "Usage: ::com interfaceId animationId componentId (loop optional)")
            }

            val interfaceId = args[1].toIntOrNull()
            val animationIdStart = args[2].toIntOrNull()
            val componentId = args[3].toIntOrNull()
            val loop = args.getOrNull(4)?.toBooleanStrictOrNull() ?: false

            if (interfaceId == null || animationIdStart == null || componentId == null) {
                return@define reject(player, "All arguments must be valid integers.")
            }

            var animationId = animationIdStart

            val pulse = object : Pulse(3) {
                override fun pulse(): Boolean {
                    player.packetDispatch.sendAnimationInterface(animationId, interfaceId, componentId)
                    player.debug("Played animation [$animationId] on interface [$interfaceId] component [$componentId].")

                    if (loop) {
                        animationId++
                        return false
                    }

                    return true
                }
            }

            (player.attributes.remove("com_pulse") as? Pulse)?.stop()

            if (loop) {
                player.attributes["com_pulse"] = pulse
            }

            submitWorldPulse(pulse)
        }

        define(
            name = "stopcom",
            privilege = Privilege.ADMIN,
            usage = "::stopcom",
            description = "Stops the looped interface animation started with ::com.",
        ) { player, _ ->
            val pulse = player.attributes.remove("com_pulse") as? Pulse
            if (pulse != null) {
                pulse.stop()
                player.debug("Animation loop stopped.")
            } else {
                player.debug( "No animation loop was running.")
            }
        }

        /*
         * Command to spawn all Ancient Pages into the inventory.
         */

        define(
            name = "ancientpages",
            privilege = Privilege.ADMIN,
            usage = "::ancientpages",
            description = "Spawn all ancient pages into the inventory.",
        ) { player, _ ->
            for (pages in (Items.ANCIENT_PAGE_11341..Items.ANCIENT_PAGE_11366).toIntArray())
            addItem(player, pages)
            addItem(player, Items.MY_NOTES_11339)
            player.debug("Ancient pages added to the inventory.")
            return@define
        }

        /*
         * Command to toggle all display cases in Varrock Museum.
         */

        define(
            name = "displaycase",
            privilege = Privilege.ADMIN,
            usage = "::displaycase",
            description = "Toggle all display cases at Varrock Museum.",
        ) { player, _ ->
            val displayCaseVarbit =
                intArrayOf(5091, 3661, 3660, 3657, 3655, 3652, 3651, 3650, 3649, 3648, 3647, 3646, 3645, 3644, 3643)
            for (varbit in displayCaseVarbit) {
                setVarbit(player, varbit, 1)
            }
            player.debug("Toggled display cases on.")
            return@define
        }

        /*
         * Command to register a hint icon on a node or at a location.
         */

        define(
            name = "hinticon",
            privilege = Privilege.ADMIN,
            usage = "::hinticon <lt>npcId<gt> or <lt>x<gt> <lt>y<gt> <lt>height<gt>",
            description = "Register a hint icon on a node or at a location."
        ) { player, args ->
            when (args.size - 1) {
                1 -> { // node
                    val nodeId = args[1].toIntOrNull() ?: reject(player, "Please provide a valid npc ID")
                    val node = Repository.findNPC(nodeId as Int) ?: reject(player, "Node not found for ID $nodeId")
                    setAttribute(player, "tutorial:hinticon", HintIconManager.registerHintIcon(player, node as Node))
                    player.debug("Registered hint icon on node $nodeId")
                }
                3 -> { // location
                    val x = args[1].toIntOrNull() ?: reject(player, "Invalid x coord")
                    val y = args[2].toIntOrNull() ?: reject(player, "Invalid y coord")
                    val height = args[3].toIntOrNull() ?: reject(player, "Invalid height")
                    val location = Location.create(x as Int, y  as Int, height as Int)
                    setAttribute(player, "tutorial:hinticon", HintIconManager.registerHintIcon(player, location, 1, -1, player.hintIconManager.freeSlot(), height, 3))
                    player.debug("Registered hint icon at location $x,$y,$height")
                }
                else -> reject(player, "Invalid usage. Use ::hinticon <npcId> or ::hinticon <x> <y> <height>")
            }
        }

        /*
         * Command to remove all active hint icons for a player.
         */

        define(
            name = "hintclear",
            privilege = Privilege.ADMIN,
            usage = "::hintclear",
            description = "Removes all active hint icons."
        ) { player, _ ->
            player.hintIconManager.clear()
            player.debug("All hint icons have been removed.")
        }

        /*
         * Command for setting tutorial stages.
         */

        define(
            name = "settutorialstage",
            privilege = Privilege.ADMIN,
            usage = "::settutorialstage <lt>stage<gt>",
            description = "Set tutorial stage."
        ) { player, args ->
            if (args.size < 2) {
                reject(player, "Usage: ::tutorialstage stage")
            }

            val stage = args[1].toIntOrNull() ?: reject(player, "Please use a valid integer.")
            setAttribute(player, GameAttributes.TUTORIAL_STAGE, stage)
            player.debug("Stage set to [${getAttribute(player, GameAttributes.TUTORIAL_STAGE, 0)}]")
        }

        /*
         * Command for check tutorial stage.
         */

        define(
            name = "tutorialstage",
            privilege = Privilege.ADMIN,
            usage = "::tutorialstage",
            description = "Check which tutorial stage you on."
        ) { player, _ ->
            getAttribute(player, GameAttributes.TUTORIAL_STAGE, -1)
            player.debug("Tutorial stage=${getAttribute(player, GameAttributes.TUTORIAL_STAGE, 0)}")
        }

        /*
         * Command for buying a house at Rimmington (+10M cash).
         */

        define(
            name = "buyhouse",
            privilege = Privilege.ADMIN,
            usage = "::buyhouse",
            description = "Allows you to buy house.",
        ) { player, _ ->
            player.houseManager.createNewHouseAt(content.global.skill.construction.HouseLocation.RIMMINGTON)
            player.debug(RED + "The house has been bought.")
            addItem(player, Items.COINS_995, 10000000)
        }

        /*
         * Command to execute CS2 scripts dynamically.
         */

        define(
            name = "cs2",
            privilege = Privilege.ADMIN,
            usage = "::cs2 id args",
            description = "Allows you to call arbitrary cs2 scripts during runtime",
        ) { player, args ->
            var scriptArgs = ArrayList<Any>()
            if (args.size == 2) {
                runcs2(player, args[1].toIntOrNull() ?: return@define)
                return@define
            } else if (args.size > 2) {
                for (i in 2 until args.size) {
                    scriptArgs.add(args[i].toIntOrNull() ?: args[i])
                }
                runcs2(
                    player,
                    args[1].toIntOrNull() ?: return@define,
                    *(scriptArgs.toTypedArray().also { player.debug(it.contentToString()) }),
                )
            }
        }

        /*
         * Command for clearing all achievement diaries.
         */

        define(
            name = "cleardiary",
            privilege = Privilege.ADMIN,
            usage = "::cleardiary",
            description = "Clear all the achievements.",
        ) { player, _ ->
            for (type in DiaryType.values()) {
                val diary = player.achievementDiaryManager.getDiary(type)
                if (diary != null) {
                    for (level in 0 until diary.levelStarted.size) {
                        for (task in 0 until diary.taskCompleted[level].size) {
                            diary.resetTask(player, level, task)
                        }
                    }
                }
            }
            player.debug("All achievement diaries cleared successfully.")
        }

        /*
         * Command to clear the player's current job assignment.
         */

        define(
            name = "clearjob",
            privilege = Privilege.ADMIN,
            usage = "::clearjob",
            description = "Clear the actually job.",
        ) { player, _ ->
            val playerJobManager = JobManager.getInstance(player)
            playerJobManager.job = null
            playerJobManager.jobAmount = -1
            playerJobManager.jobOriginalAmount = -1

            player.debug("Job cleared successfully.")
        }

        /*
         * Command for printing the player's current Region ID.
         */

        define(
            name = "region",
            privilege = Privilege.ADMIN,
            usage = "::region",
            description = "Prints your current Region ID.",
        ) { player, _ ->
            sendMessage(player, "Region ID: ${player.viewport.region!!.regionId}")
        }

        /*
         * Command for swapping the spellbook.
         */

        define(
            name = "spellbook",
            privilege = Privilege.ADMIN,
            usage = "::spellbook <lt>book ID<gt> (0 = MODERN, 1 = ANCIENTS, 2 = LUNARS)",
            description = "Swaps your spellbook to the given book ID.",
        ) { player, args ->
            if (args.size < 2) {
                reject(player, "Usage: ::spellbook [int]. 0 = MODERN, 1 = ANCIENTS, 2 = LUNARS")
            }
            val spellBook = SpellBookManager.SpellBook.values()[args[1].toInt()]
            player.spellBookManager.setSpellBook(spellBook)
            player.spellBookManager.update(player)
        }

        /*
         * Command that instantly kills the player by dealing damage equal to their current life points.
         */

        define(
            name = "killme",
            privilege = Privilege.ADMIN,
            usage = "::killme",
            description = "Does exactly what it says on the tin.",
        ) { player, _ ->
            player.impactHandler.manualHit(player, player.skills.lifepoints, HitsplatType.NORMAL)
        }

        /*
         * Command for rolling the drop table of a specified NPC a given number of times.
         */

        define(
            name = "rolldrops",
            privilege = Privilege.ADMIN,
            usage = "::rolldrops <lt>NPC ID<gt> <lt>AMOUNT<gt>",
            description = "Rolls the given NPC drop table AMOUNT times.",
        ) { player, args ->
            if (args.size < 2) {
                reject(player, "Usage: ::rolldrops npcid amount")
            }

            val container = player.dropLog
            val npcId = args[1].toInt()
            val amount = args[2].toInt()

            container.clear()
            val drops =
                NPCDefinition
                    .forId(npcId)
                    .dropTables.table
                    .roll(player, amount)
            for (drop in drops) container.add(drop, false)
            container.open(player)
        }

        /*
         * Command for listing all varbits assigned to a given varp ID.
         */

        define(
            name = "varbits",
            privilege = Privilege.ADMIN,
            usage = "::varbits <lt>Varp ID<gt>",
            description = "Lists all the varbits assigned to the given varp.",
        ) { player, args ->
            if (args.size < 2) {
                reject(player, "Usage: ::varbits varpIndex")
            }

            val varp = args[1].toIntOrNull() ?: reject(player, "Please use a valid int for the varpIndex.")
            GlobalScope.launch {
                player.debug("========== Found Varbits for Varp $varp ==========")
                for (id in 0 until 10000) {
                    val def = VarbitDefinition.forId(id)
                    if (def.varpId == varp) {
                        player.debug( "${def.id} -> [offset: ${def.startBit}, upperBound: ${def.endBit}]")
                    }
                }
                player.debug("=========================================")
            }
        }

        /*
         * Command for testing a packet by writing a ResetInterface packet to the queue.
         */

        define(name = "testpacket", Privilege.ADMIN) { player, _ ->
            PacketWriteQueue.write(ResetInterface(), PlayerContext(player))
        }

        /*
         * Command for searching NPCs by name, including child NPCs.
         */

        define(
            name = "npcsearch",
            privilege = Privilege.ADMIN,
            usage = "npcsearch name",
            description = "Searches for NPCs that match the name either in main or children.",
        ) { player, strings ->
            val name = strings.slice(1 until strings.size).joinToString(" ").lowercase()
            for (id in 0 until 9000) {
                val def = NPCDefinition.forId(id)
                if (def.name.isNotBlank() &&
                    (
                        def.name
                            .lowercase()
                            .contains(name) ||
                            name.contains(def.name.lowercase())
                    )
                ) {
                    notify(player, "$id - ${def.name}")
                } else {
                    for ((childId, index) in def.childNPCIds?.withIndex() ?: continue) {
                        val childDef = NPCDefinition.forId(childId)
                        if (childDef.name.lowercase().contains(name) || name.contains(childDef.name.lowercase())) {
                            notify(player, "$childId child($id) index $index - ${childDef.name}")
                        }
                    }
                }
            }
        }

        /*
         * Command for searching items by name.
         */

        define(
            name = "itemsearch",
            privilege = Privilege.ADMIN,
            usage = "::itemsearch",
            description = "Search for items by name",
        ) { player, args ->
            val itemName = args.copyOfRange(1, args.size).joinToString(" ").lowercase()
            for (i in 0 until 15000) {
                val name = getItemName(i).lowercase()
                if (name.contains(itemName) || itemName.contains(name)) {
                    notify(player, "$i: $name")
                }
            }
        }

        /*
         * Command for toggling the visualization of chunk borders.
         */

        define(
            name = "drawchunks",
            privilege = Privilege.ADMIN,
            usage = "::drawchunks",
            description = "Draws the border of the chunk you're standing in",
        ) { player, _ ->
            setAttribute(player, "chunkdraw", !getAttribute(player, "chunkdraw", false))
        }

        /*
         * Command for toggling the visualization of clipping flags in the current region.
         */

        define(
            name = "drawclipping",
            privilege = Privilege.ADMIN,
            usage = "::drawclipping",
            description = "Draws the clipping flags of the region you're standing in",
        ) { player, _ ->
            setAttribute(player, "clippingdraw", !getAttribute(player, "clippingdraw", false))
        }

        /*
         * Command for toggling the visualization of region borders.
         */

        define(
            name = "drawregions",
            privilege = Privilege.ADMIN,
            usage = "::drawregions",
            description = "Draws the border of the region you're standing in",
        ) { player, _ ->
            setAttribute(player, "regiondraw", !getAttribute(player, "regiondraw", false))
        }

        /*
         * Command for visualizing the player's movement path.
         */

        define(
            name = "drawroute",
            privilege = Privilege.ADMIN,
            usage = "::drawroute",
            description = "Visualizes the path your player is taking",
        ) { player, _ ->
            setAttribute(player, "routedraw", !getAttribute(player, "routedraw", false))
        }

        /*
         * Command for setting the starting location for a movement feature.
         */

        define(
            name = "fmstart",
            privilege = Privilege.ADMIN,
            usage = "::fmstart",
            description = "Set the starting location for a movement feature",
        ) { player, _ ->
            setAttribute(player, "fmstart", Location.create(player.location))
        }

        /*
         * Command for setting the ending location for a movement feature.
         */

        define(
            name = "fmend",
            privilege = Privilege.ADMIN,
            usage = "::fmend",
            description = "Set the ending location for a movement feature",
        ) { player, _ ->
            setAttribute(player, "fmend", Location.create(player.location))
        }

        /*
         * Command for setting the movement speed for a movement feature.
         */

        define(
            name = "fmspeed",
            privilege = Privilege.ADMIN,
            usage = "::fmspeed",
            description = "Set the speed for a movement feature",
        ) { player, args ->
            setAttribute(player, "fmspeed", args[1].toIntOrNull() ?: 10)
        }

        /*
         * Command for setting the ending speed for a movement feature.
         */

        define(
            name = "fmspeedend",
            privilege = Privilege.ADMIN,
            usage = "::fmspeedend",
            description = "Set the ending speed for a movement feature",
        ) { player, args ->
            setAttribute(player, "fmspeedend", args[1].toIntOrNull() ?: 10)
        }

        /*
         * Command for testing the movement features.
         */

        define(
            name = "testfm",
            privilege = Privilege.ADMIN,
            usage = "::testfm",
            description = "Test the movement feature with specified parameters",
        ) { player, _ ->
            val start = getAttribute(player, "fmstart", Location.create(player.location))
            val end = getAttribute(player, "fmend", Location.create(player.location))
            val speed = getAttribute(player, "fmspeed", 10)
            val speedEnd = getAttribute(player, "fmspeedend", 10)
            val ani = getAttribute(player, "fmanim", -1)
            forceMove(player, start, end, speed, speedEnd, anim = ani)
        }

        /*
         * Command for setting the animation for the movement feature.
         */

        define(
            name = "fmanim",
            privilege = Privilege.ADMIN,
            usage = "::fmanim",
            description = "Set the animation for the movement feature",
        ) { player, args ->
            setAttribute(player, "fmanim", args[1].toIntOrNull() ?: -1)
        }

        /*
         * Command for visualizing the predicted intersection point with an NPC.
         */

        define(
            name = "drawintersect",
            privilege = Privilege.ADMIN,
            usage = "::drawintersect",
            description = "Visualizes the predicted intersection point with an NPC",
        ) { player, _ ->
            setAttribute(player, "draw-intersect", !getAttribute(player, "draw-intersect", false))
        }

        /*
         * Command for printing out the player's active and new timers.
         */

        define(
            name = "timers",
            privilege = Privilege.ADMIN,
            usage = "::timers",
            description = "Print out timers",
        ) { player, _ ->
            sendMessage(player, "Active timers:")
            for (timer in player.timers.activeTimers) {
                sendMessage(player, "  ${timer.identifier} ${timer.nextExecution}")
            }
            sendMessage(player, "New timers:")
            for (timer in player.timers.newTimers) {
                sendMessage(player, "  ${timer.identifier}")
            }
        }

        /*
         * Command for opening an overlay interface using a specified ID.
         */

        define(name = "overlay", privilege = Privilege.ADMIN, usage = "::overlay <lt>Overlay ID<gt>") { player, args ->
            val overlayInt = args[1].toInt()
            openOverlay(player, overlayInt)
        }

        /*
         * Command for toggling the completion status of Barbarian Firemaking training.
         */

        define(
            name = "barbfm",
            privilege = Privilege.ADMIN,
            usage = "::barbfm",
            description = "Completes barbarian fm training.",
        ) { player: Player, _ ->
            if (getAttribute(player, BarbarianTraining.FM_FULL, false)) {
                removeAttribute(player, BarbarianTraining.FM_FULL)
                notify(player, "Barbarian firemaking method:%R Disabled.")
            } else {
                setAttribute(player, BarbarianTraining.FM_FULL, true)
                notify(player, "Barbarian firemaking method:%DP Enabled.")
            }
        }

        /*
         * Command for testing the respawn points.
         */

        define(
            name = "respawn",
            privilege = Privilege.ADMIN,
            usage = "::respawn <point>",
            description = "Change the player's respawn point.",
        ) { player, args ->

            if (args.isEmpty()) {
                player.debug("Please specify a respawn point. Usage: ::respawn <lumbridge|falador|camelot>")
                return@define
            }

            val respawnPoint =
                when (args[1].lowercase()) {
                    "lumbridge" -> RespawnPoint.LUMBRIDGE
                    "falador" -> RespawnPoint.FALADOR
                    "camelot" -> RespawnPoint.CAMELOT
                    else -> {
                        player.debug("Invalid respawn point. Valid options are: lumbridge, falador, camelot.")
                        return@define
                    }
                }

            player.setRespawnLocation(respawnPoint)

            player.debug("Your respawn point has been set to: [${respawnPoint.name}]")
        }
    }
}
