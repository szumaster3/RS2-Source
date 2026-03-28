package content.region.kandarin.gnome_village.plugin

import content.region.kandarin.gnome_village.dialogue.SpiritTreeDialogue
import core.api.getUsedOption
import core.api.isQuestComplete
import core.api.openDialogue
import core.api.sendDialogue
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import shared.consts.Quests
import shared.consts.Scenery

/**
 * Handles Gnome Spirit Tree transportation system.
 */
class SpiritTreePlugin : InteractionListener {

    private val spiritTrees = intArrayOf(Scenery.SPIRIT_TREE_1317, Scenery.SPIRIT_TREE_1293, Scenery.SPIRIT_TREE_1294, Scenery.SPIRIT_TREE_8355)

    override fun defineListeners() {

        /*
         * Handles spirit tree options.
         */

        on(spiritTrees, IntType.SCENERY, "talk-to", "teleport") { player, _ ->
            if (!isQuestComplete(player, Quests.TREE_GNOME_VILLAGE)) {
                sendDialogue(player, "The tree doesn't feel like talking.")
                return@on true
            }
            val showIntro = getUsedOption(player) == "talk-to"
            openDialogue(player, SpiritTreeDialogue(showIntro))
            return@on true
        }
    }
}