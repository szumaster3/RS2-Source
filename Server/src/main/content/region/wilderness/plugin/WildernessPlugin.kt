package content.region.wilderness.plugin

import content.data.GameAttributes
import content.region.wilderness.dialogue.CapeMerchantDialogue
import content.region.wilderness.dialogue.FatTonyDialogue
import content.region.wilderness.dialogue.NoterazzoDialogue
import core.api.*
import core.game.dialogue.DialogueFile
import core.game.global.action.ClimbActionHandler
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.interaction.QueueStrength
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.TeleportManager
import core.game.node.entity.player.link.TeleportManager.TeleportType
import core.game.node.entity.player.link.diary.DiaryType
import core.game.node.entity.player.link.warning.WarningManager
import core.game.node.entity.player.link.warning.WarningType
import core.game.node.item.Item
import core.game.world.map.Location
import shared.consts.*

class WildernessPlugin : InteractionListener {

    companion object {
        val MUDDY_CHEST_LOOT = arrayOf(Item(Items.UNCUT_RUBY_1619), Item(Items.MITHRIL_BAR_2359), Item(Items.MITHRIL_DAGGER_1209), Item(Items.ANCHOVY_PIZZA_2297), Item(Items.LAW_RUNE_563, 2), Item(Items.DEATH_RUNE_560, 2), Item(Items.CHAOS_RUNE_562, 10), Item(Items.COINS_995, 50))
        private val WILDERNESS_LEVER_IDS = intArrayOf(Scenery.LEVER_1814, Scenery.LEVER_1815, Scenery.LEVER_5959, Scenery.LEVER_5960, Scenery.LEVER_9706, Scenery.LEVER_9707)
        private val KBD_LADDER = intArrayOf(Scenery.LEVER_1816, Scenery.LEVER_1817)

        private fun teleport(player: Player, location: Location) {
            player.teleporter.send(location, TeleportType.NORMAL, TeleportManager.WILDERNESS_TELEPORT)
        }
    }

    override fun defineListeners() {
        on(Scenery.CLOSED_CHEST_170, IntType.SCENERY, "open") { player, node ->
            val key = Item(Items.MUDDY_KEY_991)
            if (!removeItem(player, key)) {
                sendMessage(player, "This chest is locked and needs some sort of key.")
            } else {
                animate(player, Animations.HUMAN_OPEN_CHEST_536)
                replaceScenery(node.asScenery(), Scenery.OPEN_CHEST_171, 3, node.direction, node.location)
                for (item in MUDDY_CHEST_LOOT) {
                    addItemOrDrop(player, item.id, item.amount)
                }
            }
            return@on true
        }

        on(Scenery.LADDER_1765, IntType.SCENERY, "climb-down") { player, _ ->
            ClimbActionHandler.climb(player, null, Location(3069, 10257, 0))
            return@on true
        }

        on(Scenery.LADDER_1767, IntType.SCENERY, "climb-down") { player, _ ->
            ClimbActionHandler.climb(player, null, Location(3017, 10250, 0))
            return@on true
        }

        /*
         * Handles Corporeal Beast passages.
         */

        on(Scenery.PASSAGE_37929, IntType.SCENERY, "go-through") { player, _ ->
            val offset = if (player.location.x > 2917) -4 else 4
            player.properties.teleportLocation = player.location.transform(offset, 0, 0)
            return@on true
        }

        on(Scenery.PASSAGE_38811, IntType.SCENERY, "go-through") { player, _ ->
            WarningManager.trigger(player, WarningType.CORPOREAL_BEAST_DANGEROUS) {
                val offset = if (player.location.x > 2970) -4 else 4
                player.properties.teleportLocation =
                    player.location.transform(offset, 0, 0)
            }
            return@on true
        }

        /*
         * Handles interaction with wilderness ladders.
         */

        on(WILDERNESS_LEVER_IDS, IntType.SCENERY, "pull") { player, node ->
            when (node.id) {
                Scenery.LEVER_1814 -> openDialogue(player, WildernessLeverDialogue())

                Scenery.LEVER_1815, Scenery.LEVER_5959, Scenery.LEVER_5960, Scenery.LEVER_9706, Scenery.LEVER_9707 -> {
                    if (!player.savedData.activityData.hasKilledKolodion() && node.id == Scenery.LEVER_9706) {
                        sendNPCDialogue(player, NPCs.KOLODION_905, "You're not allowed in there. Come downstairs if you want to enter my arena.")
                        return@on false
                    }

                    val destinations = mapOf(
                        Scenery.LEVER_1815 to Location(2562, 3311, 0),
                        Scenery.LEVER_5959 to Location(2539, 4712, 0),
                        Scenery.LEVER_5960 to Location(3090, 3956, 0),
                        Scenery.LEVER_9706 to Location(3105, 3951, 0),
                        Scenery.LEVER_9707 to Location(3105, 3956, 0)
                    )

                    val messages = mapOf(
                        Scenery.LEVER_1815 to "out of the wilderness.",
                        Scenery.LEVER_5959 to "into the mage's cave.",
                        Scenery.LEVER_5960 to "out of the mage's cave.",
                        Scenery.LEVER_9706 to "out of the arena.",
                        Scenery.LEVER_9707 to "into the arena."
                    )

                    animate(player, Animations.PULL_DOWN_LEVER_2140)
                    playAudio(player, Sounds.PULL_LEVER_GENERAL_2400)
                    sendMessage(player, "You pull the lever...")

                    queueScript(player, 1, QueueStrength.NORMAL) {
                        if (!hasTimerActive(player, GameAttributes.TELEBLOCK_TIMER)) {
                            val destination = destinations[node.id] ?: return@queueScript stopExecuting(player)
                            teleport(player, destination)
                            sendMessage(player, "...And teleport ${messages[node.id] ?: "somewhere mysterious."}", 5)
                        } else {
                            sendMessage(player, "A magical force has stopped you from teleporting.")
                        }
                        return@queueScript stopExecuting(player)
                    }
                }

                else -> sendMessage(player, "Nothing interesting happens.")
            }
            return@on true
        }

        /*
         * Handles interaction with kbd ladders.
         */

        on(KBD_LADDER, IntType.SCENERY, "pull") { player, node ->
            if (!finishedMoving(player)) return@on false

            sendMessage(player, "You pull the lever...")
            animate(player, Animations.PULL_DOWN_LEVER_2140, false)
            playAudio(player, Sounds.PULL_LEVER_GENERAL_2400)

            when (node.id) {
                // Enter to lair.
                Scenery.LEVER_1816 -> {
                    if (node.location == Location(3067, 10252, 0)) {
                        if (player.zoneMonitor.isInZone("Wilderness") && hasTimerActive(player, GameAttributes.TELEBLOCK_TIMER)) {
                            sendMessage(player, "A magical force has stopped you from teleporting.")
                        } else {
                            sendMessage(player, "... and teleport into the lair of the King Black Dragon!", 5)
                            teleport(player, Location(2272, 4680, 0))
                        }
                    }
                }

                // Exit from lair.
                Scenery.LEVER_1817 -> {
                    sendMessage(player, "... and teleport out of the lair of the King Black Dragon!", 5)
                    teleport(player, Location(3067, 10253, 0))
                }
            }

            return@on true
        }


        on(Scenery.ENTRANCE_37749, IntType.SCENERY, "go-through") { player, _ ->
            teleport(player, Location(2885, 4372, 2), TeleportType.INSTANT)
            return@on true
        }

        on(Scenery.EXIT_37928, IntType.SCENERY, "go-through") { player, _ ->
            teleport(player, Location(3214, 3782, 0), TeleportType.INSTANT)
            return@on true
        }

        on(Scenery.TRAPDOOR_39188, IntType.SCENERY, "open") { player, _ ->
            if (!hasRequirement(player, Quests.DEFENDER_OF_VARROCK)) return@on false
            ClimbActionHandler.climb(
                player,
                ClimbActionHandler.CLIMB_DOWN,
                Location(3241, 9991, 0),
                "You descend into the cavern below.",
            )
            return@on true
        }

        on(Scenery.LADDER_39191, IntType.SCENERY, "climb-up") { player, _ ->
            ClimbActionHandler.climb(
                player,
                ClimbActionHandler.CLIMB_UP,
                Location(3239, 3606, 0),
                "You climb up the ladder to the surface.",
            )
            return@on true
        }

        on(Scenery.DOOR_39200, IntType.SCENERY, "open") { player, _ ->
            sendMessage(player, "The door doesn't open.")
            return@on true
        }

        /*
         * Handles talking to npc around Wilderness.
         */

        on(intArrayOf(
            NPCs.WILLIAM_1778,
            NPCs.IAN_1779,
            NPCs.LARRY_1780,
            NPCs.DARREN_1781,
            NPCs.EDWARD_1782,
            NPCs.RICHARD_1783,
            NPCs.NEIL_1784,
            NPCs.EDMOND_1785,
            NPCs.SIMON_1786,
            NPCs.SAM_1787,
        ), IntType.NPC, "talk-to") { player, node ->
            openDialogue(player, CapeMerchantDialogue(), node)
            return@on true
        }

        on(NPCs.FAT_TONY_596, IntType.NPC, "talk-to") { player, node ->
            openDialogue(player, FatTonyDialogue(), node)
            return@on true
        }

        on(NPCs.NOTERAZZO_597, IntType.NPC, "talk-to") { player, node ->
            openDialogue(player, NoterazzoDialogue(), node)
            return@on true
        }

        on(intArrayOf(NPCs.BLACK_HEATHER_202, NPCs.DONNY_THE_LAD_203, NPCs.SPEEDY_KEITH_204), IntType.NPC, "talk-to") { player, node ->
            val npc = node.asNpc()
            sendDialogue(player, "${npc.name} is not interested in talking.")
            return@on true
        }
    }

    private class WildernessLeverDialogue : DialogueFile() {

        init { stage = 0 }

        override fun handle(componentID: Int, buttonID: Int) {
            when(stage) {
                0 -> {
                    sendDialogue(player!!, "Warning! Pulling the lever will teleport you deep into the wilderness.")
                    stage = 1
                }
                1 -> {
                    setTitle(player!!, 2)
                    options("Yes I'm brave.", "Eep! The wilderness... No thank you.")
                    stage = 2
                }
                2 -> when(buttonID) {
                    1 -> {
                        end()
                        animate(player!!, Animations.PULL_DOWN_LEVER_2140)
                        playAudio(player!!, Sounds.PULL_LEVER_GENERAL_2400)
                        sendMessage(player!!, "You pull the lever...")
                        queueScript(player!!, 1, QueueStrength.NORMAL) {
                            if (!hasTimerActive(player!!, GameAttributes.TELEBLOCK_TIMER)) {
                                teleport(player!!, Location(3154, 3923, 0))
                                sendMessage(player!!, "...And teleport into the wilderness.", 5)
                                if(hasDiaryTaskComplete(player!!, DiaryType.ARDOUGNE, 2, 2)) {
                                    finishDiaryTask(player!!, DiaryType.ARDOUGNE, 2, 2)
                                }
                            } else {
                                sendMessage(player!!, "A magical force has stopped you from teleporting.")
                            }
                            return@queueScript stopExecuting(player!!)
                        }
                    }
                    2 -> end()
                }
            }
        }
    }
}