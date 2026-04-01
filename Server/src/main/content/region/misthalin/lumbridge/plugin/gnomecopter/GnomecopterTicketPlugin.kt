package content.region.misthalin.lumbridge.plugin.gnomecopter

import core.api.sendString
import core.cache.def.impl.ItemDefinition
import core.game.component.Component
import core.game.interaction.OptionHandler
import core.game.node.Node
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.plugin.Plugin
import core.tools.RandomFunction
import shared.consts.Components
import shared.consts.Items

@Initializable
class GnomecopterTicketPlugin : OptionHandler() {

    override fun newInstance(arg: Any?): Plugin<Any> {
        ItemDefinition.forId(Items.GNOMECOPTER_TICKET_12843).handlers["option:read"] = this
        return this
    }

    override fun handle(
        player: Player,
        node: Node,
        option: String,
    ): Boolean {

        val destination = player.getAttribute(
            "gc:route",
            GnomeCopterDestination.CASTLE_WARS
        )

        player.interfaceManager.open(Component(Components.CARPET_TICKET_729))

        var info = "Gnomecopter ticket:"
        info += "<br>" + destination.displayName
        info += "<br>Ref. #" + destination.id.toString().padStart(3, '0')

        for (i in 3 until 8) {
            info += RandomFunction.randomize(10)
        }
        sendString(player, info, Components.CARPET_TICKET_729, 2)
        return true
    }
}