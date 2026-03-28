package content.region.other.zanaris.plugin

import content.global.travel.fairyring.FairyRingInterface
import content.region.other.zanaris.dialogue.FairyShopkeeperDialogue
import content.region.other.zanaris.dialogue.MagicDoorDialogue
import core.api.*
import core.game.global.action.ClimbActionHandler
import core.game.global.action.DoorActionHandler
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.interaction.QueueStrength
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.TeleportManager
import core.game.world.map.Location
import core.game.world.update.flag.context.Animation
import shared.consts.*

class ZanarisPlugin : InteractionListener {

    companion object {
        private val RINGS = intArrayOf(Scenery.FAIRY_RING_12095, Scenery.FAIRY_RING_14058, Scenery.FAIRY_RING_14061, Scenery.FAIRY_RING_14064, Scenery.FAIRY_RING_14067, Scenery.FAIRY_RING_14070, Scenery.FAIRY_RING_14073, Scenery.FAIRY_RING_14076, Scenery.FAIRY_RING_14079, Scenery.FAIRY_RING_14082, Scenery.FAIRY_RING_14085, Scenery.FAIRY_RING_14088, Scenery.FAIRY_RING_14091, Scenery.FAIRY_RING_14094, Scenery.FAIRY_RING_14097, Scenery.FAIRY_RING_14100, Scenery.FAIRY_RING_14103, Scenery.FAIRY_RING_14106, Scenery.FAIRY_RING_14109, Scenery.FAIRY_RING_14112, Scenery.FAIRY_RING_14115, Scenery.FAIRY_RING_14118, Scenery.FAIRY_RING_14121, Scenery.FAIRY_RING_14124, Scenery.FAIRY_RING_14127, Scenery.FAIRY_RING_14130, Scenery.FAIRY_RING_14133, Scenery.FAIRY_RING_14136, Scenery.FAIRY_RING_14139, Scenery.FAIRY_RING_14142, Scenery.FAIRY_RING_14145, Scenery.FAIRY_RING_14148, Scenery.FAIRY_RING_14151, Scenery.FAIRY_RING_14154, Scenery.FAIRY_RING_14157, Scenery.FAIRY_RING_14160, Scenery.FAIRY_RING_16181, Scenery.FAIRY_RING_16184, Scenery.FAIRY_RING_23047, Scenery.FAIRY_RING_27325, Scenery.FAIRY_RING_37727)
        private val MAIN_RING = Scenery.FAIRY_RING_12128
        private val ENTRY_RING = Scenery.FAIRY_RING_12094
        private val MARKETPLACE_RING = Scenery.FAIRY_RING_12003

        /**
         * Checks requirements to use rings.
         */
        private fun fairyMagic(player: Player): Boolean {
            if (!hasRequirement(player, Quests.FAIRYTALE_I_GROWING_PAINS) ||
                !anyInEquipment(player, Items.DRAMEN_STAFF_772, Items.LUNAR_STAFF_9084)
            ) {
                sendMessage(player, "The fairy ring only works for those who weld fairy magic.")
                return false
            }
            return true
        }

        /**
         * Opens the fairy ring interface.
         */
        private fun openFairyRing(player: Player) {
            openInterface(player, FairyRingInterface.RINGS_IFACE)
        }
    }

    override fun defineListeners() {
        /*
         * Handles ring options.
         */

        on(RINGS, IntType.SCENERY, "use") { player, _ ->
            if (!fairyMagic(player)) return@on true
            teleport(player, Location.create(2412, 4434, 0), TeleportManager.TeleportType.FAIRY_RING)
            return@on true
        }

        on(MAIN_RING, IntType.SCENERY, "use") { player, _ ->
            if (!fairyMagic(player)) return@on true
            openFairyRing(player)
            return@on true
        }

        on(ENTRY_RING, IntType.SCENERY, "use") { player, _ ->
            if (!fairyMagic(player)) return@on true
            teleport(player, Location.create(3203, 3168, 0), TeleportManager.TeleportType.FAIRY_RING)
            return@on true
        }

        on(MARKETPLACE_RING, IntType.SCENERY, "use") { player, _ ->
            if (!fairyMagic(player)) return@on true
            teleport(player, Location.create(3262, 3167, 0), TeleportManager.TeleportType.FAIRY_RING)
            return@on true
        }

        /*
         * Handles opening the magic doors.
         */

        on(intArrayOf(Scenery.MAGIC_DOOR_12045, Scenery.MAGIC_DOOR_12047), IntType.SCENERY, "open") { player, node ->
            val isMagicDoorAAtLocation =
                node.id == Scenery.MAGIC_DOOR_12045 && node.location == Location(2469, 4438, 0)

            val isMagicDoorBAtLocation =
                node.id == Scenery.MAGIC_DOOR_12047 && node.location == Location(2465, 4434, 0)

            val conditionOne = isMagicDoorAAtLocation && player.location.x >= 2470
            val conditionTwo = player.location.y < 4434 && (isMagicDoorAAtLocation || isMagicDoorBAtLocation)
            val conditionThree = node.id == Scenery.MAGIC_DOOR_12047 && player.location.x >= 2470

            if (conditionOne || conditionTwo || conditionThree) {
                DoorActionHandler.handleAutowalkDoor(player, node.asScenery())
            } else {
                player.dialogueInterpreter.open(MagicDoorDialogue.NAME, node)
            }
            return@on true
        }

        /*
         * Handles using raw chicken & egg on chicken shrine.
         */

        onUseWith(IntType.SCENERY, intArrayOf(Items.RAW_CHICKEN_2138, Items.EGG_1944), Scenery.CHICKEN_SHRINE_12093) { player, used, _ ->
            if (used.id != Items.RAW_CHICKEN_2138) {
                sendMessage(player, "Nice idea, but nothing interesting happens.")
                return@onUseWith false
            }

            if (!removeItem(player, used.asItem())) {
                sendMessage(player, "Nothing interesting happens.")
                return@onUseWith false
            }

            val animDuration = animationDuration(Animation(Animations.DISAPPEAR_2755))

            lock(player, 3)
            player.animate(Animation(Animations.DISAPPEAR_2755))
            queueScript(player, animDuration, QueueStrength.SOFT) {
                teleport(player, Location(2461, 4356, 0))
                player.animate(Animation(Animations.APPEAR_2757))
                return@queueScript stopExecuting(player)
            }

            return@onUseWith true
        }

        /*
         * Handles scenery interactions around Zanaris.
         */

        on(Scenery.PORTAL_12260, IntType.SCENERY, "use") { player, _ ->
            val animDuration = animationDuration(Animation(Animations.DISAPPEAR_2755))

            lock(player, 3)
            player.animate(Animation(Animations.DISAPPEAR_2755))
            queueScript(player, animDuration, QueueStrength.SOFT) {
                teleport(player, Location(2453, 4476, 0))
                player.animate(Animation(Animations.APPEAR_2757))
                return@queueScript stopExecuting(player)
            }
            return@on true
        }

        onUseWith(IntType.SCENERY, Items.ROPE_954, Scenery.TUNNEL_ENTRANCE_12253) { player, used, with ->
            if (!removeItem(player, used.asItem())) {
                sendMessage(player, "Nothing interesting happens.")
                return@onUseWith false
            }

            replaceScenery(with.asScenery(), Scenery.TUNNEL_ENTRANCE_12254, 80)
            return@onUseWith true
        }

        on(Scenery.TUNNEL_ENTRANCE_12254, IntType.SCENERY, "climb-down") { player, _ ->
            ClimbActionHandler.climb(player, Animation(Animations.HUMAN_BURYING_BONES_827), Location(2441, 4381, 0))
            return@on true
        }

        on(Scenery.ROPE_12255, IntType.SCENERY, "climb-up") { player, _ ->
            ClimbActionHandler.climb(player, Animation(Animations.HUMAN_BURYING_BONES_827), Location(2457, 4380, 0))
            return@on true
        }

        /*
         * Handles talking to shopkeeper around Zanaris.
         */

        on(intArrayOf(NPCs.FAIRY_SHOPKEEPER_534, NPCs.FAIRY_SHOP_ASSISTANT_535), IntType.NPC, "talk-to") { player, node ->
            openDialogue(player, FairyShopkeeperDialogue(), node.asNpc())
            return@on true
        }
    }
}