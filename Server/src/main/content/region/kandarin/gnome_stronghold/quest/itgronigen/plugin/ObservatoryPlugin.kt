package content.region.kandarin.gnome_stronghold.quest.itgronigen.plugin

import content.data.GameAttributes
import content.region.kandarin.gnome_stronghold.quest.itgronigen.cutscene.ObservatoryCutscene
import content.region.kandarin.gnome_stronghold.quest.itgronigen.dialogue.ObservatoryAssistantDialogue
import content.region.kandarin.gnome_stronghold.quest.itgronigen.npc.GoblinGuardNPC.Companion.spawnGoblinGuard
import content.region.kandarin.gnome_stronghold.quest.itgronigen.npc.PoisonSpiderNPC.Companion.spawnPoisonSpider
import core.api.*
import core.api.utils.PlayerCamera
import core.game.global.action.DoorActionHandler
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.player.link.warning.WarningManager
import core.game.node.entity.player.link.warning.WarningType
import core.game.system.task.Pulse
import core.game.world.map.Location
import core.game.world.update.flag.context.Animation
import shared.consts.*

class ObservatoryPlugin : InteractionListener {
    companion object {
        private const val OBSERVATORY_ASSISTANT = NPCs.OBSERVATORY_ASSISTANT_6118
        private const val TELESCOPE_SCENERY = Scenery.TELESCOPE_25439
        private const val MOLTEN_GLASS = Items.MOLTEN_GLASS_1775
        private const val LENS_MOULD = Items.LENS_MOULD_602
        private const val OBSERVATORY_LENS = Items.OBSERVATORY_LENS_603
        private const val DUNGEON_STAIRS_UP = Scenery.STAIRS_25429
        private const val SLEEPING_GUARD = NPCs.SLEEPING_GUARD_6122
        private val KITCHEN_GATES = intArrayOf(Scenery.KITCHEN_GATE_2199, Scenery.KITCHEN_GATE_2200)
        private val KEY_CHEST = intArrayOf(Scenery.CHEST_25391, Scenery.CHEST_25389, Scenery.CHEST_25385, Scenery.CHEST_25387)
        private val KEY_CHEST_OPEN = intArrayOf(Scenery.CHEST_25392, Scenery.CHEST_25390, Scenery.CHEST_25386, Scenery.CHEST_25388)
        private const val GOBLIN_STOVE = Scenery.GOBLIN_STOVE_25440
        private const val EMPTY_GOBLIN_STOVE = Scenery.GOBLIN_STOVE_25441
        private const val ORRERY = Scenery.ORRERY_25401
        private val OBSERVATORY_GATES = intArrayOf(Scenery.DOOR_25526, Scenery.DOOR_25527)
        private const val OBSERVATORY_STAIRS = Scenery.STAIRS_25434
        private val STAR_CHART = intArrayOf(Scenery.STAR_CHART_25578, Scenery.STAR_CHART_25579, Scenery.STAR_CHART_25580, Scenery.STAR_CHART_25581, Scenery.STAR_CHART_25582, Scenery.STAR_CHART_25583)
        private const val DUNGEON_STAIRS = Scenery.STAIRS_25432
    }

    override fun defineListeners() {

        /*
         * Handles stairs into dungeon.
         */

        on(DUNGEON_STAIRS, IntType.SCENERY, "climb-down") { player, _ ->
            WarningManager.trigger(player, WarningType.OBSERVATORY_STAIRS) {
                teleport(player, Location(2355, 9394, 0))
            }
            return@on true
        }

        on(OBSERVATORY_ASSISTANT, IntType.NPC, "Talk-to") { player, _ ->
            openDialogue(player, ObservatoryAssistantDialogue())
            return@on true
        }

        on(DUNGEON_STAIRS_UP, IntType.SCENERY, "climb up") { player, node ->
            val questStage = getQuestStage(player, Quests.OBSERVATORY_QUEST)

            if (node.location.x == 2335 && node.location.y == 9351 && questStage in 12..13) {
                ObservatoryCutscene(player).start()
                return@on true
            }

            val destination = if (node.location.x == 2335 && node.location.y == 9351) {
                Location(2439, 3164, 0)
            } else {
                Location(2457, 3186, 0)
            }

            teleport(player, destination)
            sendMessage(player, "You climb up the stairs.")
            return@on true
        }

        on(SLEEPING_GUARD, IntType.NPC, "Prod") { player, _ ->
            val animation = Animation(Animations.HUMAN_GUARD_PROD_6839)
            if (!getAttribute(player, GameAttributes.OBSERVATORY_GOBLIN_SPAWN, false)) {
                animate(player, animation)
                runTask(player, animation.duration) {
                    spawnGoblinGuard(player)
                    setAttribute(player, GameAttributes.OBSERVATORY_GOBLIN_SPAWN, true)
                }
            } else {
                sendMessage(player, "You can't do that right now.")
            }
            return@on true
        }

        on(KEY_CHEST, IntType.SCENERY, "open") { player, node ->
            if (isQuestComplete(player, Quests.OBSERVATORY_QUEST)) {
                sendMessage(player, "It looks like this chest has already been looted.")
                return@on true
            }

            val chestIndex = KEY_CHEST.indexOf(node.id)

            val correctChest: Int =
                getAttribute(player, GameAttributes.OBSERVATORY_CHEST_FAIL_COUNTER, -1)

            if (correctChest == -1) {
                setAttribute(player, GameAttributes.OBSERVATORY_CHEST_FAIL_COUNTER, (0..3).random())
            }

            val finalCorrectChest: Int =
                getAttribute(player, GameAttributes.OBSERVATORY_CHEST_FAIL_COUNTER, -1)

            val result: Int =
                if (chestIndex == finalCorrectChest) {
                    2
                } else {
                    when ((1..100).random()) {
                        in 1..50 -> 0
                        in 51..80 -> 1
                        else -> 3
                    }
                }

            setAttribute(player, GameAttributes.OBSERVATORY_GOBLIN_KEY, result)

            animate(player, Animations.CLOSE_CHEST_539)
            replaceScenery(node.asScenery(), node.id + 1, 80)
            sendMessage(player, "You open the chest.")

            return@on true
        }

        on(KEY_CHEST_OPEN, IntType.SCENERY, "search") { player, _ ->
            animate(player, Animations.CLOSE_CHEST_539)
            sendMessage(player, "You search the chest.")
            when (getAttribute(player, GameAttributes.OBSERVATORY_GOBLIN_KEY, -1)) {
                0 -> {
                    sendMessage(player, "The chest contains a poisonous spider.")
                    spawnPoisonSpider(player)
                }

                1 -> {
                    sendMessage(player, "You find a dose of antipoison.")
                    addItem(player, Items.ANTIPOISON1_179)
                    removeAttribute(player, GameAttributes.OBSERVATORY_GOBLIN_KEY)
                }

                2 -> {
                    sendMessage(player, "You find a kitchen key.")
                    addItem(player, Items.GOBLIN_KITCHEN_KEY_601)
                    removeAttribute(player, GameAttributes.OBSERVATORY_GOBLIN_KEY)
                }

                else -> {
                    sendMessage(player, "The chest is empty.")
                }
            }

            return@on true
        }

        on(KEY_CHEST_OPEN, IntType.SCENERY, "close") { player, node ->
            animate(player, Animations.HUMAN_CLOSE_CHEST_538)
            replaceScenery(node.asScenery(), node.id - 1, -1)
            sendMessage(player, "You close the chest.")
            return@on true
        }

        on(KITCHEN_GATES, IntType.SCENERY, "open") { player, node ->
            val scenery = node.asScenery()
            val hasLensMould = getVarbit(player, Vars.VARBIT_QUEST_OBSERVATORY_GOBLIN_STOVE_LENS_MOULD_TAKEN_3837) == 1
            val gateUnlocked = getVarbit(player, Vars.VARBIT_GOBLIN_KITCHEN_GATE_UNLOCKED_3826) == 1
            val hasKey = inInventory(player, Items.GOBLIN_KITCHEN_KEY_601)

            when {
                hasLensMould -> {
                    DoorActionHandler.handleAutowalkDoor(player, scenery)
                    setVarbit(player, Vars.VARBIT_GOBLIN_KITCHEN_GATE_UNLOCKED_3826, 1, true)
                }

                !hasKey && !gateUnlocked -> {
                    sendMessage(player, "These gates are locked, you don't seem to be able to open them.")
                }

                else -> {
                    sendPlayerDialogue(player, "You had better be quick, there may be more guards about.")
                    DoorActionHandler.handleAutowalkDoor(player, scenery)
                    removeItem(player, Items.GOBLIN_KITCHEN_KEY_601)
                    sendMessage(player, "The gate unlocks.")
                    sendMessage(player, "The key is useless now. You discard it.")
                    setVarbit(player, Vars.VARBIT_GOBLIN_KITCHEN_GATE_UNLOCKED_3826, 1, true)
                }
            }
            return@on true
        }

        on(GOBLIN_STOVE, IntType.SCENERY, "inspect") { player, _ ->
            sendDialogueLines(player, "The goblins appear to have been using the lens mould to cook their", "stew!")
            addDialogueAction(player) { _, _ ->
                animate(player, Animations.CLOSE_CHEST_539)
                setVarbit(player, Vars.VARBIT_QUEST_OBSERVATORY_GOBLIN_STOVE_LENS_MOULD_TAKEN_3837, 1, true)
                sendDialogue(player, "You shake out its contents and take it with you.")
                sendChat(player, "Euuuw, that smells awful!", 3)
                addItemOrDrop(player, Items.LENS_MOULD_602)
            }
            return@on true
        }

        on(EMPTY_GOBLIN_STOVE, IntType.SCENERY, "inspect") { player, _ ->
            sendMessage(player, "Just a plain stove. Nothing there.")
            return@on true
        }

        onUseWith(IntType.ITEM, MOLTEN_GLASS, LENS_MOULD) { player, used, _ ->
            if (removeItem(player, used.asItem())) {
                addItem(player, OBSERVATORY_LENS)
                sendMessage(player, "You pour the molten glass into the mould.")
                sendMessage(player, "You clasp it together.")
                sendItemDialogue(player, OBSERVATORY_LENS, "It has produced a small, convex glass dist.")
            }
            return@onUseWith true
        }

        on(ORRERY, IntType.SCENERY, "view") { player, _ ->
            lock(player, 10000)
            lockInteractions(player, 10000)
            player.interfaceManager.removeTabs(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13)
            submitWorldPulse(
                object : Pulse(1) {
                    var counter = 0

                    override fun pulse(): Boolean {
                        when (counter++) {
                            0 -> {
                                PlayerCamera(player).setPosition(2443, 3186, 300)
                                PlayerCamera(player).rotateTo(2444, 3185, 300, 1000)
                            }

                            3 -> {
                                sendChat(player, "Oooooh, bizarre!")
                            }

                            6 -> {
                                PlayerCamera(player).reset()
                                player.interfaceManager.restoreTabs()
                                unlock(player)
                                return true
                            }
                        }
                        return false
                    }
                },
            )
            return@on true
        }

        on(OBSERVATORY_GATES, IntType.SCENERY, "open") { player, _ ->
            sendMessage(player, "This gate is locked.")
            return@on true
        }

        on(OBSERVATORY_STAIRS, IntType.SCENERY, "climb-down") { player, _ ->
            teleport(player, Location(2335, 9350, 0))
            sendMessage(player, "You climb down the stairs.")
            return@on true
        }

        on(TELESCOPE_SCENERY, IntType.SCENERY, "view") { player, _ ->
            val telescopeAnim = Animation(Animations.HUMAN_LOOK_TELESCOPE_6849)
            animate(player, telescopeAnim)
            sendMessage(player, "You look through the telescope.")
            playAudio(player, 3804)
            runTask(player, telescopeAnim.duration) {
                openInterface(player, Components.TELESCOPE_552)
                setQuestStage(player, Quests.OBSERVATORY_QUEST, 14)
            }
            return@on true
        }

        on(STAR_CHART, IntType.SCENERY, "Look-at") { player, _ ->
            openInterface(player, Components.STAR_CHART_104)
            return@on true
        }
    }
}
