package content.global.activity.phoenix

import core.api.registerLogoutListener
import core.api.resetAnimator
import core.api.visualize
import core.game.activity.Cutscene
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.TeleportManager
import core.game.world.map.Direction
import core.game.world.map.Location
import shared.consts.NPCs
import shared.consts.Regions

class PhoenixSpawnCutscene (player: Player) : Cutscene(player) {

    override fun setup() {
        setExit(Location.create(3533, 5204, 0))
        loadRegion(REGION)
        addNPC(PHOENIX, 14, 13, Direction.NORTH)
    }

    override fun runStage(stage: Int)
    {
        when (stage)
        {
            0 -> {
                teleport(player, 13, 20, 0)
                resetAnimator(player)
                timedUpdate(2)
            }
            1 -> {
                rotateCamera(16, 13)
                visualize(getNPC(PHOENIX)!!, -1, 1982)
                timedUpdate(6)
            }

            2 -> {
                endWithoutFade {
                    val npc = PhoenixNPC(PHOENIX, Location.create(3534, 5197, 0))
                    npc.init()
                    npc.teleporter.send(Location.create(3534, 5197, 0), TeleportManager.TeleportType.INSTANT)
                    registerLogoutListener(player, "phoenix-activity")
                    {
                        npc.clear()
                    }
                    resetCamera()
                }
            }
        }
    }

    companion object {
        private const val PHOENIX = NPCs.PHOENIX_8549
        private const val REGION = Regions.PHOENIX_LAIR_14161
    }
}
