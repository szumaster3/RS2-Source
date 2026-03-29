package content.data

import core.ServerStore
import core.api.hasAnItem
import core.api.sendMessage
import core.game.node.entity.player.Player
import shared.consts.Items

/**
 * Handles global item checks.
 */
object GlobalStore {

    private const val RING_ARCHIVE = "daily-explorer-ring"

    fun check(player: Player) {
        val archive = ServerStore.getArchive(RING_ARCHIVE)
        val username = player.username.lowercase()

        val hasRing = hasAnItem(
            player,
            Items.EXPLORERS_RING_1_13560,
            Items.EXPLORERS_RING_2_13561,
            Items.EXPLORERS_RING_3_13562
        ).container != null

        val runCount = archive["${username}run"]?.asInt ?: 0
        val alchsCount = archive["${username}alchs"]?.asInt ?: 0

        if (hasRing && (runCount != 0 || alchsCount >= 30)) {
            sendMessage(player, "Your explorer's ring has been recharged.")
        }
    }
}
