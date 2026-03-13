package content.region.other.tutorial_island.plugin

import content.data.GameAttributes
import content.region.other.tutorial_island.dialogue.BankerGuideDialogue
import core.api.*
import core.game.dialogue.FaceAnim
import core.game.global.action.ClimbActionHandler
import core.game.global.action.DoorActionHandler
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.player.link.TeleportManager
import core.game.system.task.Pulse
import core.game.world.GameWorld
import core.game.world.map.Location
import core.game.world.repository.Repository
import shared.consts.Animations
import shared.consts.Items
import shared.consts.NPCs
import shared.consts.Scenery

class TutorialPlugin : InteractionListener {

    override fun defineListeners() {

        /*
         * Handles first doors at tutorial island.
         */

        on(RS_GUIDE_DOOR, IntType.SCENERY, "open") { player, node ->
            val tutorialStage = getAttribute(player, GameAttributes.TUTORIAL_STAGE, 0)

            if (tutorialStage != 3) {
                if(player.location.x < 3097) {
                    sendPlainDialogue(player,false,
                        "",
                        "You need to talk to the ${GameWorld.settings?.name ?: "Gielinor"} Guide before you are allowed to",
                        "proceed through this door.",
                        ""
                    )
                } else {
                    sendDialogue(player, "You've already done that. Perhaps you should move on.")
                }
                return@on false
            }


            setAttribute(player, GameAttributes.TUTORIAL_STAGE, 4)
            TutorialStage.load(player, 4)

            val door = node as? core.game.node.scenery.Scenery ?: return@on false
            DoorActionHandler.handleAutowalkDoor(player, door, Location.create(3098, 3107, 0))
            return@on true
        }

        /*
         * Handles wooden gate after survival tasks.
         */

        on(WOODEN_GATE, IntType.SCENERY, "open") { player, node ->
            if (getAttribute(player, GameAttributes.TUTORIAL_STAGE, 0) != 16) {
                sendPlainDialogue(player,false,
                    "",
                    "You need to talk to the Survival Guide and",
                    "complete her tasks before you are allowed to",
                    "proceed through this gate."
                )
                return@on false
            }
            setAttribute(player, GameAttributes.TUTORIAL_STAGE, 17)
            TutorialStage.load(player, 17)
            val gatePair = when (node.id) {
                Scenery.GATE_3015 -> Pair(Scenery.GATE_3015, Scenery.GATE_3016)
                Scenery.GATE_3016 -> Pair(Scenery.GATE_3016, Scenery.GATE_3015)
                else -> null
            }
            if (gatePair != null) {
                DoorActionHandler.autowalkFence(
                    player, node.asScenery(), gatePair.first, gatePair.second
                )
            }
            return@on true
        }

        /*
         * Handles cook guide door during tutorial.
         */

        on(COOK_GUIDE_DOOR, IntType.SCENERY, "open") { player, node ->
            if (getAttribute(player, GameAttributes.TUTORIAL_STAGE, 0) != 17) {
                sendPlainDialogue(player,false, "You may not pass this door yet. Try following the instructions.")
                return@on false
            }
            setAttribute(player, GameAttributes.TUTORIAL_STAGE, 18)
            TutorialStage.load(player, 18)
            DoorActionHandler.handleAutowalkDoor(player, node.asScenery())
            return@on true
        }

        /*
         * Handles cook guide door exit during tutorial.
         */

        on(COOK_GUIDE_DOOR_EXIT, IntType.SCENERY, "open") { player, node ->
            val stage = getAttribute(player, GameAttributes.TUTORIAL_STAGE, 0)

            when {
                stage < 21 -> {
                    sendPlainDialogue(player,false, "You need to finish the Master Chef's tasks first.")
                    return@on false
                }

                stage in 21..22 -> {
                    setAttribute(player, GameAttributes.TUTORIAL_STAGE, 23)
                    TutorialStage.load(player, 23)
                    DoorActionHandler.handleAutowalkDoor(player, node.asScenery())
                    return@on true
                }

                else -> {
                    sendPlainDialogue(player, false, "Follow the path to the home of the quest guide.")
                    return@on false
                }
            }
        }

        /*
         * Handles quest guide door during tutorial.
         */

        on(QUEST_GUIDE_DOOR, IntType.SCENERY, "open") { player, node ->
            if (getAttribute(player, GameAttributes.TUTORIAL_STAGE, 0) != 26) {
                sendPlainDialogue(player,false, "You need to finish the Master Chef's tasks first.")
                return@on false
            }
            setAttribute(player, GameAttributes.TUTORIAL_STAGE, 27)
            TutorialStage.load(player, 27)
            DoorActionHandler.handleAutowalkDoor(player, node.asScenery())
            return@on true
        }

        /*
         * Handles ladder down to next (mining) area.
         */

        on(QUEST_LADDER_DOWN, IntType.SCENERY, "climb-down") { player, node ->
            if (getAttribute(player, GameAttributes.TUTORIAL_STAGE, 0) < 29) {
                sendNPCDialogue(player, NPCs.QUEST_GUIDE_949, "I don't think you're ready to go down there yet.", FaceAnim.HALF_GUILTY)
                return@on false
            }

            if (getAttribute(player, GameAttributes.TUTORIAL_STAGE, 0) == 29) {
                setAttribute(player, GameAttributes.TUTORIAL_STAGE, 30)
                TutorialStage.load(player, 30)
            }
            ClimbActionHandler.climbLadder(player, node.asScenery(), "climb-down")
            return@on true
        }

        /*
         * Handles ladder up from mining area.
         */

        on(QUEST_LADDER_UP, IntType.SCENERY, "climb-up") { player, node ->
            ClimbActionHandler.climbLadder(player, node.asScenery(), "climb-up")
            submitWorldPulse(
                object : Pulse(2) {
                    override fun pulse(): Boolean {
                        val questTutor = Repository.findNPC(NPCs.QUEST_GUIDE_949) ?: return true
                        sendChat(questTutor, "What are you doing, ${player.username}? Get back down the ladder.")
                        return true
                    }
                },
            )

            return@on true
        }

        /*
         * Handles combat gate during tutorial.
         */

        on(COMBAT_GATE, IntType.SCENERY, "open") { player, node ->
            val stage = getAttribute(player, GameAttributes.TUTORIAL_STAGE, -1)
            if (stage < 42) {
                sendPlainDialogue(player,false, "You need to finish with Mining and Smithing first.")
                return@on false
            }
            if (stage >= 44) {
                sendPlainDialogue(player,false, "Follow the path to the Combat Instructor.")
                return@on false
            }
            setAttribute(player, GameAttributes.TUTORIAL_STAGE, 44)
            TutorialStage.load(player, 44)
            DoorActionHandler.handleAutowalkDoor(player, node.asScenery())
            return@on true
        }

        /*
         * Handles giant rat gates during combat tutorial.
         */

        on(GIANT_RAT_GATE, IntType.SCENERY, "open") { player, node ->
            val stage = getAttribute(player, GameAttributes.TUTORIAL_STAGE, 0)
            if(stage == 54) {
                sendNPCDialogueLines(player, NPCs.COMBAT_INSTRUCTOR_944, FaceAnim.ANNOYED, false, "No, don't enter the pit. Range the rats from outside the cage.")
                return@on false
            }
            if (stage !in 50..53) {
                sendNPCDialogueLines(player, NPCs.COMBAT_INSTRUCTOR_944, FaceAnim.ANNOYED, false, "Oi, get away from there!", "Don't enter my rat pen unless I say so!")
                return@on false
            }
            if (stage == 50) {
                setAttribute(player, GameAttributes.TUTORIAL_STAGE, 51)
                TutorialStage.load(player, 51)
            }
            DoorActionHandler.handleAutowalkDoor(player, node.asScenery())
            return@on true
        }

        /*
         * Handles combat ladder climb up.
         */

        on(COMBAT_LADDER, IntType.SCENERY, "climb-up") { player, _ ->
            if (getAttribute(player, GameAttributes.TUTORIAL_STAGE, 0) != 55) {
                sendPlainDialogue(player,false,
                    "You're not ready to continue yet. You need to know",
                    "about combat before you go on.",
                )
                return@on false
            }

            setAttribute(player, GameAttributes.TUTORIAL_STAGE, 56)
            TutorialStage.load(player, 56)
            animate(player, Animations.HUMAN_CLIMB_STAIRS_828)
            teleport(player, Location.create(3111, 3127, 0), TeleportManager.TeleportType.INSTANT, 2)
            return@on true
        }

        /*
         * Handles ladder down from bank area after combat tutorial.
         */

        on(Scenery.LADDER_3031, IntType.SCENERY, "climb-down") { player, _ ->
            sendDialogue(player, "You've already done that. Perhaps you should move on.")
            return@on false
        }

        /*
         * Handles bank guide dialogue.
         */

        on(NPCs.BANKER_953, IntType.NPC, "talk-to") { player, _ ->
            openDialogue(player, BankerGuideDialogue())
            return@on true
        }

        /*
         * Handles use the bank booth.
         */

        on(Scenery.BANK_BOOTH_3045, IntType.SCENERY, "use") { player, _ ->
            openDialogue(player, BankerGuideDialogue())
            return@on true
        }

        /*
         * Handles bank guide door during tutorial.
         */

        on(BANK_GUIDE_DOOR, IntType.SCENERY, "open") { player, node ->
            if (getAttribute(player, GameAttributes.TUTORIAL_STAGE, 0) != 57) {
                sendPlainDialogue(player,false, "", "You need to open your bank first.", "")
                return@on false
            }
            if(getAttribute(player, GameAttributes.TUTORIAL_STAGE, 0) == 58) {
                sendPlainDialogue(player,false, "", "You've already done that. Perhaps you should move on.")
                return@on false
            }
            setAttribute(player, GameAttributes.TUTORIAL_STAGE, 58)
            TutorialStage.load(player, 58)
            DoorActionHandler.handleAutowalkDoor(player, node.asScenery())
            return@on true
        }

        /*
         * Handles finance guide doors.
         */

        on(FINANCE_GUIDE_DOOR, IntType.SCENERY, "open") { player, node ->
            if (getAttribute(player, GameAttributes.TUTORIAL_STAGE, 0) != 59) {
                sendPlainDialogue(player,false,
                    "You should complete your objective before",
                    "talking to Finance Advisor."
                )
                return@on false
            }
            setAttribute(player, GameAttributes.TUTORIAL_STAGE, 60)
            TutorialStage.load(player, 60)
            DoorActionHandler.handleAutowalkDoor(player, node as core.game.node.scenery.Scenery)
            return@on true
        }


        /*
         * Handles exit from church.
         */

        on(CHURCH_DOOR_EXIT, IntType.SCENERY, "open") { player, node ->
            if (getAttribute(player, GameAttributes.TUTORIAL_STAGE, 0) != 66) {
                sendPlainDialogue(player,false,
                    "You need to finish Brother Brace's tasks before you",
                    "are allowed to proceed through this door.",
                )
                return@on false
            }
            setAttribute(player, GameAttributes.TUTORIAL_STAGE, 67)
            TutorialStage.load(player, 67)
            DoorActionHandler.handleAutowalkDoor(player, node.asScenery())
            return@on true
        }

        /*
         * Handles restriction with wielding weapons/tools on tutorial island.
         */

        onEquip(intArrayOf(BRONZE_AXE, BRONZE_PICKAXE, BRONZE_DAGGER)) { player, _ ->
            val restriction = getAttribute(player, GameAttributes.TUTORIAL_STAGE, -1)
            if (restriction < 47) {
                sendDialogue(player, "You'll be told how to equip items later.")
                return@onEquip false
            }
            return@onEquip true
        }
    }

    override fun defineDestinationOverrides() {
        setDest(IntType.NPC, intArrayOf(NPCs.BANKER_953), "talk-to") { _, npc ->
            return@setDest npc.location.transform(npc.direction, 2)
        }
    }

    companion object {
        private const val RS_GUIDE_DOOR = Scenery.DOOR_3014
        private const val COOK_GUIDE_DOOR = Scenery.DOOR_3017
        private const val COOK_GUIDE_DOOR_EXIT = Scenery.DOOR_3018
        private const val QUEST_GUIDE_DOOR = Scenery.DOOR_3019
        private const val QUEST_LADDER_DOWN = Scenery.LADDER_3029
        private const val QUEST_LADDER_UP = Scenery.LADDER_3028
        private const val COMBAT_LADDER = Scenery.LADDER_3030
        private const val BANK_GUIDE_DOOR = Scenery.DOOR_3024
        private const val FINANCE_GUIDE_DOOR = Scenery.DOOR_3025
        private const val CHURCH_DOOR_EXIT = Scenery.DOOR_3026
        private const val BRONZE_AXE = Items.BRONZE_AXE_1351
        private const val BRONZE_PICKAXE = Items.BRONZE_PICKAXE_1265
        private const val BRONZE_DAGGER = Items.BRONZE_DAGGER_1205
        private val WOODEN_GATE = intArrayOf(Scenery.GATE_3015, Scenery.GATE_3016)
        private val COMBAT_GATE = intArrayOf(Scenery.GATE_3020, Scenery.GATE_3021)
        private val GIANT_RAT_GATE = intArrayOf(Scenery.GATE_3022, Scenery.GATE_3023)
    }

}
