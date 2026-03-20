package core.game.node.entity.player.link.warning

import core.game.interaction.InterfaceListener

class WarningListener : InterfaceListener {
    override fun defineInterfaceListeners() {
        WarningType.values.forEach { warning ->
            if (warning.component != -1) {
                on(warning.component) { player, _, _, buttonId, _, _ ->
                    WarningManager.handleButton(player, warning, buttonId)
                    return@on true
                }
            }
        }
    }
}