package content.region.karamja.quest.roots

import content.data.GameAttributes
import content.data.skill.SkillingTool
import content.region.karamja.quest.roots.npc.HoracioNPC
import content.region.karamja.quest.roots.npc.WildJadeVineNPC
import core.api.*
import core.game.dialogue.DialogueFile
import core.game.dialogue.FaceAnim
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.scenery.SceneryBuilder
import core.game.world.map.RegionManager
import core.game.world.map.build.DynamicRegion
import core.game.world.repository.Repository
import core.game.world.update.flag.context.Animation
import core.tools.END_DIALOGUE
import shared.consts.*

class BackToMyRootsPlugin : InteractionListener {

    override fun defineListeners() {

        /*
         * Handle smelly package pickup.
         */

        on(Scenery.SMELLY_PACKAGE_27055, IntType.SCENERY, "open") { player, _ ->
            player.animate(Animation(Animations.HUMAN_WITHDRAW_833))
            val npc = Repository.findNPC(NPCs.RPDT_EMPLOYEE_843)
            npc?.sendChat("Oh great, back to work.")
            setVarbit(player, Vars.VARBIT_QUEST_BACK_TO_MY_ROOTS_PROGRESS_4055, 20, true)
            sendItemDialogue(player, Items.HAND_11763, "You find a hand with a scrap of a wizard's robe attached.")
            addItemOrDrop(player, Items.HAND_11763)
            return@on true
        }

        /*
         * Handle planting the sealed pot.
         */

        onUseWith(IntType.SCENERY, Items.SEALED_POT_11777, Scenery.HORACIO_S_JADE_VINE_PATCH_27061) { player, used, _ ->
            if (removeItem(player, used.asItem())) {
                lock(player, 7)
                openInterface(player, 795)
                sendString(player, "The small cutting slowly grows and matures....", 795, 1)
                runTask(player, 6) {
                    closeInterface(player)
                    setVarbit(player, Vars.VARBIT_QUEST_BACK_TO_MY_ROOTS_PROGRESS_4055, 55)
                    val horacioNPC = HoracioNPC(NPCs.HORACIO_845, player.location)
                    openDialogue(player, HoracioDialogueFile(horacioNPC))
                }
            }
            return@onUseWith true
        }

        /*
         * Handle wild jade vine battle.
         */

        on(Scenery.WILD_JADE_VINE_27062, IntType.SCENERY, "attack") { player, _ ->
            val axe = SkillingTool.getAxe(player)
            if (axe == null || !inEquipment(player, axe.id)) {
                sendDialogue(player, "You need an axe equipped to kill the vine!")
                return@on false
            }

            if (!inInventory(player, Items.SECATEURS_5329)) {
                sendDialogue(player, "You need secateurs to help with the vines.")
                return@on false
            }

            lock(player, 10)
            openOverlay(player, Components.FADE_TO_BLACK_120)
            setAttribute(player, GameAttributes.VINE_FIGHT, player.location)
            registerLogoutListener(player, GameAttributes.VINE_FIGHT) { p ->
                p.location = getAttribute(p, GameAttributes.VINE_FIGHT, player.location)
            }

            setupBattle(player)
            return@on true
        }
    }

    private fun setupBattle(player: Player) {
        val base = region.baseLocation
        val horacioLocation = base.transform(12, 49, 0)
        val vineLocation = base.transform(14, 50, 0)
        val playerLocation = base.transform(14, 49, 0)

        val horacio = HoracioNPC(NPCs.HORACIO_845, horacioLocation)
        val wildJade = WildJadeVineNPC(NPCs.WILD_JADE_VINE_3409, vineLocation).apply {
            target = player
        }

        horacio.init()
        wildJade.init()

        region.add(player)
        region.add(horacio)
        region.add(wildJade)

        teleport(player, playerLocation)
        setVarbit(player, Vars.VARBIT_QUEST_BACK_TO_MY_ROOTS_PROGRESS_4055, 60)
        player.locks.lockMovement(10000)
        SceneryBuilder.remove(RegionManager.getObject(0, 142, 50))

        runTask(player, 3) {
            wildJade.attack(player)
            horacio.face(wildJade)
            horacio.sendChat("ARG! It's tangled me!", 3)
            horacio.sendChat("Kill it! You're my only hope!", 5)
            player.locks.unlockMovement()
        }
    }

    companion object {
        val region: DynamicRegion = DynamicRegion.create(10547)
    }
}

/**
 * Represents Horacio dialogue.
 */
private class HoracioDialogueFile(private val horacio: NPC) : DialogueFile() {

    override fun handle(componentID: Int, buttonID: Int) {
        npc = horacio
        when(stage) {
            0 -> npcl(FaceAnim.SCARED, "ARG! It's grown out of control! You should have pruned it yesterday... Now we'll need a hatchet and secateurs to deal with it. Can you help fight it?").also { stage++ }
            1 -> playerl(FaceAnim.FRIENDLY, "Okay, I'm ready for combat.").also { stage++ }
            2 -> npcl(FaceAnim.SCARED, "It doesn't look like you're prepared properly. You'll need to be wielding a hatchet to fight the vine!").also { stage++ }
            3 -> playerl(FaceAnim.FRIENDLY, "Okay, I'll go grab one.").also { stage = END_DIALOGUE }
        }
    }
}