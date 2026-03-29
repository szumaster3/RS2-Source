package content.data

import core.api.*
import core.game.interaction.QueueStrength
import core.game.node.entity.player.Player
import core.game.node.item.GroundItemManager
import core.game.node.scenery.Scenery
import core.game.world.map.Direction
import core.game.world.update.flag.context.Graphics
import shared.consts.Animations
import shared.consts.Items
import shared.consts.NPCs
import core.tools.StringUtils

/**
 * Represents god books.
 */
enum class GodType(val capeId: Int, val staffId: Int, val statueId: Int, val npcId: Int, val dropMessage: String) {
    SARADOMIN(
        Items.SARADOMIN_CAPE_2412,
        Items.SARADOMIN_STAFF_2415,
        shared.consts.Scenery.STATUE_OF_SARADOMIN_2873,
        NPCs.BATTLE_MAGE_913,
        "The cape disappears in a flash of light as it touches the ground."
    ),
    GUTHIX(
        Items.GUTHIX_CAPE_2413,
        Items.GUTHIX_STAFF_2416,
        shared.consts.Scenery.STATUE_OF_GUTHIX_2875,
        NPCs.BATTLE_MAGE_914,
        "The cape disintegrates as it touches the earth."
    ),
    ZAMORAK(
        Items.ZAMORAK_CAPE_2414,
        Items.ZAMORAK_STAFF_2417,
        shared.consts.Scenery.STATUE_OF_ZAMORAK_2874,
        NPCs.BATTLE_MAGE_912,
        "The cape ignites and burns up as it touches the ground."
    );

    /**
     * The method handle obtaining a god cape when interacting with a statue.
     */
    fun pray(player: Player, statue: Scenery) {
        queueScript(player, 1, QueueStrength.NORMAL) { stage: Int ->
            when(stage) {
                0 -> {
                    forceWalk(player, player.location.transform(Direction.SOUTH, 1), "")
                    return@queueScript delayScript(player, 3)
                }
                1 -> {
                    faceLocation(player, player.location.transform(Direction.NORTH))
                    return@queueScript delayScript(player, 2)
                }
                2 -> {
                    sendDialogue(player, "You kneel and begin to chant to ${getName()}...")
                    animate(player, Animations.HUMAN_PRAY_645)
                    return@queueScript delayScript(player, 3)
                }
                3 -> {
                    val dropLocation = statue.location.transform(0, -1, 0)
                    val g = GroundItemManager.get(capeId, dropLocation, player)
                    if (g == null && !hasCape(player)) {
                        sendDialogueLines(player, "You feel a rush of energy charge through your veins. Suddenly a", "cape appears before you.")
                        GroundItemManager.create(capeId.asItem(), dropLocation, player)
                        sendGraphics(Graphics(shared.consts.Graphics.BIG_PUFF_OF_SMOKE_188, 100), dropLocation)
                    } else {
                        sendMessage(player, "...but there is no response.")
                    }
                    return@queueScript stopExecuting(player)
                }
                else -> return@queueScript stopExecuting(player)
            }
        }
    }

    companion object {
        /**
         * Gets the [GodType] associated with the given scenery id.
         * @param scenery The ID of the scenery object.
         * @return The matching [GodType] or null if none found.
         */
        fun forStatue(scenery: Int): GodType? = values().find { it.statueId == scenery }

        /**
         * Helper function to find a god cape in the player stash.
         * @param player The player to check.
         * @param inv If true, only check the inventory; otherwise check equipment and bank.
         * @return The matching [GodType] or null if none found.
         */
        private fun getCape(player: Player, inv: Boolean): GodType? =
            values().find { god ->
                if (inv) {
                    inInventory(player, god.capeId)
                } else {
                    inEquipment(player, god.capeId) || inBank(player, god.capeId)
                }
            }

        /**
         * Gets the [GodType] of a cape that the player currently has equipped or in the bank.
         * @param player The player to check.
         * @return The matching [GodType] or null if none found.
         */
        fun getCape(player: Player): GodType? = getCape(player, false)

        /**
         * Gets the [GodType] for the given cape item id.
         * @param capeId The id of the cape item.
         * @return The matching [GodType] or null if none found.
         */
        fun forCape(capeId: Int): GodType? = values().find { it.capeId == capeId }

        /**
         * Gets the [GodType] associated with the given NPC id.
         * @param id The NPC id.
         * @return The matching [GodType] or null if none found.
         */
        fun forId(id: Int): GodType? = values().find { it.npcId == id }

        /**
         * Checks if the player has any god-related item (cape) in stash.
         * @param player The player to check.
         * @return True if the player has at least one god item, false otherwise.
         */
        fun hasCape(player: Player): Boolean = values().any { hasAnItem(player, it.capeId).exists() }
    }

    /**
     * Checks if the player is friendly to this god (i.e., wearing the god's cape).
     * @param player The player to check.
     * @return True if the player has the cape equipped, false otherwise.
     */
    fun isFriendly(player: Player): Boolean = inEquipment(player, capeId)

    /**
     * Gets a formatted display name for the god.
     * @return The formatted god name.
     */
    fun getName(): String = StringUtils.formatDisplayName(name.lowercase())
}