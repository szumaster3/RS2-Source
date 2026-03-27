package content.global.activity.penguinhns

import content.data.GameAttributes
import core.api.*
import core.cache.def.impl.NPCDefinition
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.system.task.Pulse
import core.game.world.GameWorld.Pulser
import core.game.world.update.flag.context.Animation
import core.tools.BLUE
import shared.consts.Items
import shared.consts.NPCs

/**
 * Represents the interaction for the Penguin Hunter activity.
 */
class PenguinHNSPlugin : InteractionListener {
    override fun defineListeners() {

        /*
         * Handles spying the penguin NPCs.
         */

        on(sceneryPenguinIDs, IntType.NPC, "spy-on") { player, node ->
            val npc = node.asNpc()
            if (!getAttribute(player, GameAttributes.ACTIVITY_PENGUINS_HNS, false)) {
                sendMessage(player, "You must speak to Larry at Ardougne Zoo before spying on penguins.")
                return@on true
            }

            if (PenguinManager.hasTagged(player, npc.location)) {
                sendMessage(player, "You've already tagged this penguin.")
            } else {
                Pulser.submit(SpyPulse(player, npc))
                playJingle(player, 345)
            }
            return@on true
        }

        /*
         * Handles read the spy notebook.
         */

        on(Items.SPY_NOTEBOOK_13732, IntType.ITEM, "read") { player, _ ->
            val total = getAttribute(player, GameAttributes.ACTIVITY_PENGUINS_HNS_SCORE, 0)
            val username = player.username.lowercase()
            val tagged = PenguinLocation.values().filter { loc ->
                PenguinManager.tagMapping[loc.ordinal]
                    ?.any { it.asString == username } == true
            }
            val count = tagged.size
            val details = tagged.joinToString(", ") { loc ->
                val name = NPCDefinition.forId(loc.npcId)?.name ?: "Unknown"
                "$name ${loc.hint}"
            }
            sendDialogueLines(
                player,
                "You have recently spotted $count penguins.",
                if (details.isNotEmpty()) "You have recently spotted $details" else "You haven't spotted any penguins.",
                "You have $BLUE$total</col> Penguin Points to spend with Larry."
            )
            return@on true
        }
    }

    /**
     * The NPC IDs of scenery penguins that can be spied on in the Penguin Hunter activity.
     */
    private val sceneryPenguinIDs = intArrayOf(NPCs.BARREL_8104, NPCs.BUSH_8105, NPCs.CACTUS_8107, NPCs.CRATE_8108, NPCs.ROCK_8109, NPCs.TOADSTOOL_8110)

    /**
     * Represents the pulse of spying on a penguin.
     *
     * @property player The player spying on the penguin.
     * @property npc The penguin NPC being spied on.
     */
    class SpyPulse(
        val player: Player,
        val npc: NPC,
    ) : Pulse() {
        var stage = 0
        // The activity score.
        val totalPoints = getAttribute(player, GameAttributes.ACTIVITY_PENGUINS_HNS_SCORE, 0)
        // Animation ID used during spying.
        val animationID = Animation(10355)

        /**
         * Called on each pulse tick to progress the spying action.
         *
         * @return true when the pulse is complete and should stop, false otherwise.
         */
        override fun pulse(): Boolean {
            when (stage++) {
                0 -> {
                    lock(player, 1000)
                    animate(player, animationID)
                }

                2 -> {
                    sendMessage(player, "You manage to spy on the penguin.")
                    val rand = (1..100).random()
                    val awardedPoints = when {
                        rand <= 2 -> 3    // 2% chance for 3 points.
                        rand <= 7 -> 2    // 5% chance for 2 points.
                        else -> 1         // 93% chance for 1 point.
                    }
                    setAttribute(player, GameAttributes.ACTIVITY_PENGUINS_HNS_SCORE, totalPoints + awardedPoints)
                    PenguinManager.registerTag(player, npc.location)
                }

                3 -> {
                    unlock(player)
                    return true
                }
            }
            return false
        }

        /**
         * Called when the pulse is stopped.
         */
        override fun stop() {
            super.stop()
            unlock(player)
        }
    }
}
