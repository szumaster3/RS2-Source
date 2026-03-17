package content.region.other.tutorial_island.npc

import core.game.node.entity.npc.AbstractNPC
import core.game.node.entity.player.Player
import core.game.world.GameWorld
import core.game.world.map.Location
import core.plugin.Initializable
import core.tools.RandomFunction
import shared.consts.NPCs

/**
 * Handles the NPC skippy.
 *
 * @author Vexia
 */
@Initializable
class SkippyNPC @JvmOverloads constructor(id: Int = NPCs.SKIPPY_2796, location: Location? = null) :
    AbstractNPC(id, location) {

    override fun construct(id: Int, location: Location, vararg objects: Any): AbstractNPC {
        return SkippyNPC(id, location)
    }

    override fun tick() {
        if (RandomFunction.random(100) < 15 && asNpc().zoneMonitor.isInZone("tutorial")) {
            sendChat("You can skip the tutorial by talking to me!")
        }
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.SKIPPY_2796)
    }
}
