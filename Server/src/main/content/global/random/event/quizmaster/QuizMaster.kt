package content.global.random.event.quizmaster

import content.data.RandomEvent
import core.api.*
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.TeleportManager
import core.game.world.map.Location
import shared.consts.Animations
import shared.consts.Items

/**
 * Utils for quiz master.
 * @author Vexia, szu
 */
object QuizMaster {
    const val COINS = Items.COINS_995
    const val MYSTERY_BOX = Items.RANDOM_EVENT_GIFT_14649
    const val SIT_ANIMATION = Animations.QUIZMASTER_CHAIR_2378
    private val EVENT_LOCATION: Location = Location.create(1952, 4764, 1)

    val CORRECT =
        arrayOf(
            "Hey, you're good at this! CORRECT!",
            "Absolutely RIGHT! Keep going for the win!",
            "Wow, you're a smart one! You're absolutely right!",
            "COR-RECT! Okay, next question!",
            "DING DING DING That's RIGHT! Good for you!",
            "YES! You're RIGHT!",
        )

    val WRONG =
        arrayOf(
            "Huh...? Didn't you know that one?",
            "No. No, that's not right at all.",
            "No, sorry, Try harder!",
            "Better luck next time!",
            "WRONG! That's just WRONG!",
            "WRONG!",
            "No, no, no... That's completely WRONG!",
            "Nope, that's not it.",
            "BZZZZZZZ! WRONG!",
        )

    fun cleanup(player: Player) {
        player.unlock()
        player.locks.unlockTeleport()
        player.locks.unlockInteraction()
        player.properties.teleportLocation = getAttribute(player, RandomEvent.save(), null)
        clearLogoutListener(player, RandomEvent.logout())
        removeAttributes(player, QuizMasterDialogue.QUIZMASTER_ATTRIBUTE_QUESTIONS_CORRECT, RandomEvent.save())
        player.interfaceManager.restoreTabs()
        player.animator.reset()
    }

    fun init(player: Player) {
        lockTeleport(player)
        setAttribute(player, RandomEvent.save(), player.location)
        registerLogoutListener(player, RandomEvent.logout()) { p ->
            p.location = getAttribute(p, RandomEvent.save(), player.location)
        }
        teleport(player, EVENT_LOCATION, TeleportManager.TeleportType.NORMAL)
    }

}
