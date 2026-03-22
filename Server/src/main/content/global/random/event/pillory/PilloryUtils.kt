package content.global.random.event.pillory

import content.data.GameAttributes
import content.data.RandomEvent
import core.api.*
import core.game.dialogue.FaceAnim
import core.game.node.entity.player.Player
import core.game.system.timer.impl.AntiMacro
import core.game.world.map.Location
import shared.consts.Components
import shared.consts.NPCs
import shared.consts.Sounds

/**
 * Utility object for handling the Pillory random event.
 */
object PilloryUtils {

    /**
     * The interface for this event
     */
    const val INTERFACE = Components.MACRO_PILLORY_GUARD_189

    /**
     * The locations where the event can take place.
     */
    val LOCATIONS = arrayOf(
        Location(3226, 3407, 0), Location(3228, 3407, 0), Location(3230, 3407, 0),
        Location(2681, 3489, 0), Location(2683, 3489, 0), Location(2685, 3489, 0),
        Location(2604, 3105, 0), Location(2606, 3105, 0), Location(2608, 3105, 0)
    )

    /**
     * Cleans up the event.
     *
     * @param player The player.
     */
    fun cleanup(player: Player) {
        lock(player, 3)
        closeAllInterfaces(player)
        player.properties.teleportLocation = getAttribute(player, RandomEvent.save(), null)
        clearLogoutListener(player, RandomEvent.logout())
        removeAttributes(
            player,
            RandomEvent.save(),
            RandomEvent.logout(),
            GameAttributes.RE_PILLORY_KEYS,
            GameAttributes.RE_PILLORY_PADLOCK,
            GameAttributes.RE_PILLORY_CORRECT,
            GameAttributes.RE_PILLORY_SCORE,
            GameAttributes.RE_PILLORY_TARGET
        )
        sendMessage(player, "You've escaped!")
        restoreTabs(player)
        AntiMacro.terminateEventNpc(player)
    }

    /**
     * Generates a random task lock and key configuration.
     */
    fun randomPillory(player: Player) {
        val keys = (0..3).shuffled().toIntArray()
        val lock = keys.drop(1).random()

        setAttribute(player, GameAttributes.RE_PILLORY_KEYS, keys)
        setAttribute(player, GameAttributes.RE_PILLORY_PADLOCK, lock)

        sendModelOnInterface(player, INTERFACE, 4, 9753 + lock)
        keys.drop(1).forEachIndexed { index, key ->
            sendModelOnInterface(player, INTERFACE, 5 + index, 9749 + key)
        }

        val numberToGetCorrect = getAttribute(player, GameAttributes.RE_PILLORY_CORRECT, 3)
        val correctCount = getAttribute(player, GameAttributes.RE_PILLORY_CORRECT, 0)

        (1..6).forEach { i ->
            sendModelOnInterface(player, INTERFACE, 10 + i, if (i <= correctCount) 9758 else 9757)
            sendInterfaceConfig(player, INTERFACE, 10 + i, i > numberToGetCorrect)
        }
    }

    /**
     * Handles the key selection in the interface.
     */
    fun selectedKey(player: Player, buttonID: Int) {
        val keys = getAttribute(player, GameAttributes.RE_PILLORY_KEYS, intArrayOf(0, 0, 0))
        val lock = getAttribute(player, GameAttributes.RE_PILLORY_PADLOCK, -1)
        val score = getAttribute(player, GameAttributes.RE_PILLORY_SCORE, 0)
        val correctTarget = getAttribute(player, GameAttributes.RE_PILLORY_CORRECT, 3)

        if (keys[buttonID] == lock) {
            val newScore = score + 1
            setAttribute(player, GameAttributes.RE_PILLORY_SCORE, newScore)

            if (newScore >= correctTarget) {
                cleanup(player)
                return
            }

            randomPillory(player)
            sendPlainDialogue(
                player,
                true,
                "",
                "Correct!",
                "$newScore down, ${correctTarget - newScore} to go!"
            )
            sendInterfaceConfig(player, INTERFACE, 16 + newScore, false)
        } else {
            setAttribute(player, GameAttributes.RE_PILLORY_CORRECT, 0)
            closeDialogue(player)
            playAudio(player, Sounds.INTERFACE_WRONG_2268)
            sendNPCDialogueLines(
                player,
                NPCs.TRAMP_2794,
                FaceAnim.OLD_ANGRY1,
                false,
                "Bah, that's not right.",
                "Use the key that matches the hole",
                "in the spinning lock."
            )
            addDialogueAction(player) { _, _ -> openInterface(player, INTERFACE) }
        }
    }
}