package content.region.misthalin.lumbridge.plugin.gnomecopter.sign

import core.game.node.entity.player.Player
import core.game.world.map.Location

object InformationSignManager {
    private val SIGNS: Map<Location, InformationSign> = buildMap {
        register(GnomecopterSignDefinition.values())
    }

    private fun MutableMap<Location, InformationSign>.register(signs: Array<out InformationSign>) {
        signs.forEach { put(it.location, it) }
    }

    fun handle(player: Player, location: Location): Boolean {
        return SIGNS[location]?.also { it.read(player) } != null
    }
}