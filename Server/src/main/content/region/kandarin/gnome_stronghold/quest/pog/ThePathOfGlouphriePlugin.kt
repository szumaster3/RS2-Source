package content.region.kandarin.gnome_stronghold.quest.pog

import core.api.openInterface
import core.api.sendMessages
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.interaction.InterfaceListener
import shared.consts.Components
import shared.consts.Music
import shared.consts.Scenery

class ThePathOfGlouphriePlugin : InteractionListener, InterfaceListener {

    override fun defineListeners() {
        on(Scenery.LECTERN_26802, IntType.SCENERY, "read") { p,_ ->
            openInterface(p, Components.BOLRIES_DIARY_617)
            return@on true
        }

        on(Scenery.MACHINE_PANEL_17272, IntType.SCENERY, "look-at") { p,_ ->
            sendMessages(p,
                "The picture has 7 columns that are: red, orange, yellow, green, blue, indigo and violet.",
                        "The number of boxes in each column goes up progressively from red to violet. Maybe",
                        "it's a clue.")
            return@on true
        }

        on(Scenery.YEWNOCK_S_MACHINE_26791, IntType.SCENERY, "operate") { p,_ ->
            openInterface(p,Components.YEWNOCK_MACHINE_613)
            return@on true
        }

        on(Scenery.YEWNOCK_S_EXCHANGER_26792, IntType.SCENERY, "use") { p,_ ->
            return@on true
        }
    }

    override fun defineInterfaceListeners() {
        onClose(Components.BOLRIES_DIARY_617) {p,_->
            if (!p.musicPlayer.hasUnlocked(Music.BOLRIES_DIARY_427))
            {
                p.musicPlayer.unlock(Music.BOLRIES_DIARY_427)
            }
            return@onClose true
        }
    }
}