package content.region.misthalin.lumbridge.plugin.gnomecopter.sign

import core.game.node.entity.player.Player
import core.game.world.map.Location
import core.game.component.Component
import shared.consts.Components

interface InformationSign {
    val location: Location
    val info: Array<String>
    val scrollable: Boolean
    val button: String
        get() = "~ Gnomecopter Tours ~"

    fun read(player: Player) {
        val text = info.joinToString("<br>")
        val component = if (scrollable) Components.CARPET_INFO_835 else Components.CARPET_INFO_723
        player.interfaceManager.openSingleTab(Component(component))
        player.packetDispatch.sendString(button, component, 9)
        player.packetDispatch.sendString(text, component, 10)
    }
}