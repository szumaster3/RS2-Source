package content.global.skill.agility.courses.werewolf

import core.api.*
import core.game.dialogue.DialogueFile
import core.game.dialogue.FaceAnim
import core.game.global.action.ClimbActionHandler
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.skill.Skills
import core.game.node.item.Item
import core.game.world.map.Location
import core.game.world.update.flag.context.Animation
import shared.consts.Animations
import shared.consts.Items
import shared.consts.NPCs
import shared.consts.Scenery

class WerewolfCourseListener : InteractionListener {

    override fun defineListeners() {
        on(TRAPDOOR, IntType.SCENERY, "open") { player, node ->
            sendMessage(player, "The trapdoor opens...")
            replaceScenery(node.asScenery(), OPEN_TRAPDOOR, -1)
            return@on true
        }

        on(OPEN_TRAPDOOR, IntType.SCENERY, "close") { _, node ->
            replaceScenery(node.asScenery(), TRAPDOOR, -1)
            return@on true
        }

        on(OPEN_TRAPDOOR, IntType.SCENERY, "climb-down") { player, node ->
            val werewolf = findLocalNPC(player, NPCs.WEREWOLF_1665)
            werewolf?.let {
                face(player, it)
                face(it, player, 1)
            }

            if (!anyInEquipment(player, Items.RING_OF_CHAROS_4202, Items.RING_OF_CHAROSA_6465)
                || getStatLevel(player, Skills.AGILITY) < 60) {
                sendNPCDialogueLines(
                    player,
                    NPCs.WEREWOLF_1665,
                    FaceAnim.FAMILIAR_NEUTRAL,
                    false,
                    "You can't go down there human. If it wasn't my duty",
                    "to guard this trapdoor, I would be relieving you of the",
                    "burden of your life right now."
                )
            } else {
                sendNPCDialogueLines(
                    player,
                    NPCs.WEREWOLF_1665,
                    FaceAnim.FAMILIAR_NEUTRAL,
                    false,
                    "Good luck down there, my friend. Remember, to the",
                    "west is the main agility course, while to the east is a",
                    "skullball course."
                )

                addDialogueAction(player) { _, _ ->
                    face(player, node)
                    sendMessage(player, "You climb down through the trapdoor.")
                    ClimbActionHandler.climb(
                        player,
                        Animation(Animations.HUMAN_BURYING_BONES_827),
                        Location(3549, 9865, 0)
                    )
                }
            }

            return@on true
        }

        on(AGILITY_TRAINER, IntType.NPC, "Give-Stick") { player, node ->
            if (inInventory(player, Items.STICK_4179)) {
                val stickAmount = amountInInventory(player, Items.STICK_4179)
                removeAll(player, Item(Items.STICK_4179, stickAmount), Container.INVENTORY)
                rewardXP(player, Skills.AGILITY, 190.0)
                sendMessage(player, "You give the stick to the werewolf.")
            } else {
                openDialogue(player, AgilityTrainerDialogue(), node.asNpc())
            }
            return@on true
        }
    }

    private class AgilityTrainerDialogue : DialogueFile() {
        override fun handle(componentID: Int, buttonID: Int) {
            when(stage) {
                0 -> npcl(FaceAnim.CHILD_NORMAL, "Have you brought the stick yet?").also { stage++ }
                1 -> player(FaceAnim.EXTREMELY_SHOCKED, "What stick?").also { stage++ }
                2 -> npcl(FaceAnim.CHILD_NORMAL, "Come on, get round that course - I need something to chew!").also { stage++ }
                3 -> end()
            }
        }
    }

    override fun defineDestinationOverrides() {
        setDest(IntType.SCENERY, ZIP_LINE, "teeth-grip") { _, _ ->
            return@setDest Location(3528, 9910, 0)
        }
    }

    companion object {
        private const val TRAPDOOR = Scenery.TRAPDOOR_5131
        private const val OPEN_TRAPDOOR = Scenery.TRAPDOOR_5132
        private const val AGILITY_TRAINER = NPCs.AGILITY_TRAINER_1664
        private val ZIP_LINE = intArrayOf(Scenery.ZIP_LINE_5139, Scenery.ZIP_LINE_5140, Scenery.ZIP_LINE_5141)
    }

}
