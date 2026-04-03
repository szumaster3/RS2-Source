package content.global.skill.slayer.dungeon

import core.api.teleport
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.world.map.Location
import shared.consts.Scenery

class PoisonWasteDungeonPlugin : InteractionListener {
    companion object {
        val SEWER_ENTRANCES = intArrayOf(Scenery.SEWER_ENTRANCE_26685, Scenery.SEWER_ENTRANCE_26684)
    }

    override fun defineListeners() {
        on(SEWER_ENTRANCES, IntType.SCENERY, "enter") { player, _ ->
            val dungeonLocation = Location.create(1985, 4174, 0)
            teleport(player, dungeonLocation)
            return@on true
        }
    }
}
