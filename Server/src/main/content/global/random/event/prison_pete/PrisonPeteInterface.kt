package content.global.random.event.prison_pete

import core.api.sendAnimationOnInterface
import core.api.sendModelOnInterface
import core.game.interaction.InterfaceListener
import shared.consts.Components

/**
 * Represents the Prison Pete random event interface.
 */
class PrisonPeteInterface : InterfaceListener {
    private val modelIDs = intArrayOf(10734, 10735, 10736, 10737).random()
    private val animationIDs = intArrayOf(3048, 3049, 3050, 3051, 3052).random()
    private val rotatingAnimation = 3047

    override fun defineInterfaceListeners() {
        onOpen(Components.MACRO_PRISON_PETE_273) { player, _ ->
            sendModelOnInterface(player = player, iface = 273, child = 3, model = modelIDs, zoom = 230)
            sendAnimationOnInterface(player = player, anim = animationIDs, iface = 273, child = 3)
            return@onOpen true
        }

        on(Components.MACRO_PRISON_PETE_273) { _, _, _, _, _, _ ->
            return@on true
        }
    }
}
