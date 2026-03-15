package content.region.karamja.quest.mm.plugin

import content.region.karamja.quest.mm.dialogue.*
import content.region.other.ape_atoll.dialogue.ZooknockAfterBattleDialogueFile
import content.region.other.ape_atoll.dialogue.ZooknockDialogue
import content.region.other.ape_atoll.dialogue.ZooknockDialogueFile
import core.api.*
import core.game.dialogue.FaceAnim
import core.game.global.action.DoorActionHandler
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.combat.DeathTask
import core.game.node.entity.npc.NPC
import core.game.node.item.Item
import core.game.world.map.Location
import core.game.world.map.RegionManager.getLocalNpcs
import core.game.world.map.zone.ZoneBorders
import core.game.world.update.flag.context.Graphics
import core.net.packet.PacketRepository
import core.net.packet.context.CameraContext
import core.net.packet.out.CameraViewPacket
import shared.consts.*

class MonkeyMadnessPlugin : InteractionListener {
    companion object {
        val mspeakAmuletUnstrung = Items.MSPEAK_AMULET_4022
        val monkeyMadnessPuzzleComponent = Components.TRAIL_PUZZLE_363
        val itemIds = IntArray(4031 - 4024 + 1) { it + 4024 }
    }

    override fun defineListeners() {
        on(intArrayOf(NPCs.ZOOKNOCK_1425, NPCs.ZOOKNOCK_1426), IntType.NPC, "talk-to") { player, npc ->
            if (getQuestStage(player, Quests.MONKEY_MADNESS) == 96) {
                openDialogue(player, ZooknockAfterBattleDialogueFile(), npc)
            } else if (getQuestStage(player, Quests.MONKEY_MADNESS) == 30 ||
                getQuestStage(
                    player,
                    Quests.MONKEY_MADNESS,
                ) == 31
            ) {
                openDialogue(player, ZooknockDialogue(), npc)
            } else {
                openDialogue(player, ZooknockDialogueFile(0), npc)
            }
            return@on true
        }

        on(intArrayOf(NPCs.WAYDAR_1408, NPCs.WAYDAR_1409, NPCs.WAYDAR_1410), IntType.NPC, "talk-to") { player, npc ->
            when (player.location.getRegionId()) {
                9626 -> openDialogue(player, WaydarDialogue(), npc)
                11562 -> openDialogue(player, WaydarCrashIslandDialogue(), npc)
            }
            return@on true
        }

        on(NPCs.MONKEY_1463, IntType.NPC, "talk-to") { player, npc ->
            if (player.equipment.containsAtLeastOneItem(IntArray(4031 - 4024 + 1) { it + 4024 }) &&
                player.equipment.containsAtLeastOneItem(IntArray(4022 - 4021 + 1) { it + 4021 }) &&
                getQuestStage(player, Quests.MONKEY_MADNESS) == 33
            ) {
                openDialogue(player, MonkeyDialogue(), npc)
            }
            return@on true
        }

        on(NPCs.MONKEY_CHILD_1452, IntType.NPC, "talk-to") { player, npc ->
            if (getAttribute(player, "mm:first-talk", false)) {
                openDialogue(player, MonkeyChildSecondDialogue(), npc)
            }
            if (getAttribute(player, "mm:second-talk", false)) {
                openDialogue(player, MonkeyChildThirdDialogue(), npc)
            }
            if (getAttribute(player, "mm:third-talk", false)) {
                openDialogue(player, MonkeyChildBananasDialogue(), npc)
            }
            if (getAttribute(player, "mm:talk-banana", false)) {
                openDialogue(player, MonkeyChildTalismanDialogue(), npc)
            }
            if (player.equipment.containsItem(Item(Items.MSPEAK_AMULET_4022))) {
                openDialogue(player, MonkeyChildFirstDialogue(), npc)
            } else {
                sendNPCDialogue(player, npc.id, "How dare you come near my farmhouse! Guards! Guards! Remove this human!", FaceAnim.OLD_DEFAULT)
            }
            return@on true
        }

        on(NPCs.LUMDO_1419, IntType.NPC, "talk-to") { player, npc ->
            openDialogue(player, LumdoDialogue(), npc)
            return@on true
        }

        on(NPCs.KRUK_1441, IntType.NPC, "talk-to") { player, npc ->
            if (player.questRepository.getQuest(Quests.MONKEY_MADNESS).getStage(player) >= 35) {
                openDialogue(player, KrukDialogue(), npc)
            }
            return@on true
        }

        on(intArrayOf(4787, 4788), IntType.SCENERY, "Open") { player, _ ->
            if (player.equipment.containsItem(Item(Items.MONKEY_GREEGREE_4031))) {
                sendNPCDialogue(player, NPCs.KRUK_1441, "Open the gate! A monkey wishes to pass!")

                var walkToY = 0
                if (player.location.y > 2766) {
                    walkToY = 2764
                } else {
                    walkToY = 2768
                }

                DoorActionHandler.handleAutowalkDoor(
                    player,
                    core.game.node.scenery.Scenery(
                        4788,
                        Location.create(2721, 2766, 0),
                        2,
                    ),
                    Location.create(2721, walkToY, 0),
                )
            }
            return@on true
        }

        on(NPCs.DAERO_1407, IntType.NPC, "talk-to") { player, npc ->
            if (getQuestStage(player, Quests.MONKEY_MADNESS) == 100 &&
                !getAttribute(player, "mm:xp_reward", false)
            ) {
                openDialogue(player, DaeroTrainingPostQuestDialogue(), npc)
            } else if (getAttribute(player, "mm:xp_reward", false)) {
            } else {
                when (player.location.regionId) {
                    9782 -> openDialogue(player, DaeroDialogue(), npc)
                    9626 -> openDialogue(player, DaeroHangarDialogue(), npc)
                }
            }
            return@on true
        }

        on(Scenery.AWOWOGEI_4771, IntType.SCENERY, "talk-to") { player, _ ->
            if (getQuestStage(player, Quests.MONKEY_MADNESS) == 32) {
                openDialogue(player, AwowogeiDialogue(), NPC(NPCs.AWOWOGEI_1448))
            } else {
                openDialogue(player, AwowogeiChallengeDialogue(), NPC(NPCs.AWOWOGEI_1448))
            }
            return@on true
        }

        on(NPCs.MONKEY_MINDER_1469, IntType.NPC, "talk-to") { player, npc ->
            if (!inEquipment(player, Items.MONKEY_GREEGREE_4031)) {
                openDialogue(player, MonkeyMinderHumanDialogue(), npc)
            } else {
                openDialogue(player, MonkeyMinderDialogue(), npc)
            }
            return@on true
        }

        on(Scenery.CRATE_4746, IntType.SCENERY, "Search") { player, npc ->
            openDialogue(player, HangarCrateDialogue(), npc)
            return@on true
        }

        onUseWith(IntType.SCENERY, Items.ENCHANTED_BAR_4007, 37726) { player, _, _ ->
            if (!inInventory(player, Items.MAMULET_MOULD_4020)) {
                sendItemDialogue(
                    player,
                    Items.MAMULET_MOULD_4020,
                    "You need the M'amulet Mould in order to make a M'speak amulet",
                )
            } else {
                animate(player, Animations.HUMAN_FURNACE_SMELT_3243)
                removeItem(player, Items.ENCHANTED_BAR_4007)
                addItem(player, mspeakAmuletUnstrung)
            }
            return@onUseWith true
        }

        onUseWith(IntType.ITEM, Items.BALL_OF_WOOL_1759, mspeakAmuletUnstrung) { player, _, _ ->
            removeItem(player, Item(Items.BALL_OF_WOOL_1759))
            removeItem(player, Item(mspeakAmuletUnstrung))
            addItem(player, Items.MSPEAK_AMULET_4021)
            return@onUseWith true
        }

        on(Scenery.BANANA_TREE_4749, IntType.SCENERY, "Search") { player, npc ->
            addItem(player, Items.BANANA_1963)
            return@on true
        }

        val itemIds = IntArray(4031 - 4024 + 1) { it + 4024 }
        onEquip(itemIds) { player, _ ->
            player.graphics(Graphics(134, 96))
            player.appearance.transformNPC(NPCs.MONKEY_1463)
            val localNpcs = getLocalNpcs(player, 20)
            localNpcs.forEach { npc: NPC ->
                npc.isAggressive = false
            }
            return@onEquip true
        }

        onUnequip(itemIds) { player, _ ->
            player.graphics(Graphics(134, 96))
            player.appearance.transformNPC(-1)
            val localNpcs = getLocalNpcs(player, 20)
            localNpcs.forEach { npc: NPC ->
                npc.isAggressive = true
            }
            return@onUnequip true
        }

        onEquip(Items.TENTH_SQUAD_SIGIL_4035) { player, _ ->
            /*
            sendOptions(player, "Let the sigil teleport you when worn?", "Yes", "No")
            setQuestStage(player, Quests.MONKEY_MADNESS, 94)

            PacketRepository.send(
                CameraViewPacket::class.java,
                CameraContext(player, CameraContext.CameraType.SHAKE, 4, 4, 0, 4, 4),
            )

            teleport(player, Location.create(2705, 9175, 1))

            val localNpcs = getLocalNpcs(player.location, 10)
            localNpcs.forEach { npc: NPC ->
                if (npc.name.equals("Jungle demon")) {
                    npc.isAggressive = true
                }
                npc.attack(player)

                if (DeathTask.isDead(npc)) {
                    setQuestStage(player, Quests.MONKEY_MADNESS, 95)
                }
            }

            */
            return@onEquip true
        }
    }

    override fun defineDestinationOverrides() {
        setDest(IntType.NPC, NPCs.MONKEY_MINDER_1469) { player, node ->
            val bottomMonkeyCage = ZoneBorders(2598, 3274, 2601, 3278)
            val centerMonkeyCage = ZoneBorders(2600, 3276, 2606, 3282)
            if (inBorders(player, bottomMonkeyCage) || inBorders(player, centerMonkeyCage)) {
                return@setDest player.location
            } else {
                return@setDest node.location
            }
        }
    }
}
