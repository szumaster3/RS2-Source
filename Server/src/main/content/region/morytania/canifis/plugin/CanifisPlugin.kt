package content.region.morytania.canifis.plugin

import core.api.findLocalNPC
import core.api.sendNPCDialogue
import core.game.global.action.DoorActionHandler
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.world.map.Location
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import shared.consts.NPCs
import shared.consts.Quests
import shared.consts.Scenery

class CanifisPlugin : InteractionListener {

    override fun defineListeners() {

        /*
         * Handles interaction with the Mort Myre gates.
         */

        on(Scenery.GATE_3506, IntType.SCENERY, "go-through") { player, node ->
            val scenery = node.asScenery()
            if (player.location.y < 3458) {
                DoorActionHandler.handleAutowalkDoor(player, scenery)
                if (player.location.y == 3457) {
                    GlobalScope.launch {
                        findLocalNPC(player, NPCs.ULIZIUS_1054)
                            ?.sendChat("Oh my! You're still alive!", 2)
                    }
                } else if (!player.questRepository.hasStarted(Quests.NATURE_SPIRIT)) {
                    sendNPCDialogue(
                        player,
                        NPCs.ULIZIUS_1054,
                        "I'm sorry, but I'm afraid it's too dangerous to let you through this gate right now."
                    )
                }
            }

            return@on true
        }

        on(Scenery.GATE_3507, IntType.SCENERY, "go-through") { player, node ->
            val scenery = node.asScenery()

            if (player.location.y < 3458) {
                DoorActionHandler.handleAutowalkDoor(player, scenery)
            }

            return@on true
        }
    }
}