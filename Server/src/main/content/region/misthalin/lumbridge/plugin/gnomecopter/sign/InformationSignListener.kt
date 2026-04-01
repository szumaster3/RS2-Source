package content.region.misthalin.lumbridge.plugin.gnomecopter.sign

import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import shared.consts.Scenery

class InformationSignListener : InteractionListener {
    companion object{
        private val SIGN_POST_IDS = intArrayOf(Scenery.SIGNPOST_30040,Scenery.SIGNPOST_30039,Scenery.ENTRANCE_SIGN_30036)
    }
    override fun defineListeners() {
        on(SIGN_POST_IDS, IntType.SCENERY, "read") { player, node ->
            return@on InformationSignManager.handle(player, node.location)
        }
    }
}