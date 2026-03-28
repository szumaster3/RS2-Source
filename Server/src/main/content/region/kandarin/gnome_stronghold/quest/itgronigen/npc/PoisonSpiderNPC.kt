package content.region.kandarin.gnome_stronghold.quest.itgronigen.npc

import core.api.applyPoison
import core.game.node.entity.Entity
import core.game.node.entity.npc.AbstractNPC
import core.game.node.entity.player.Player
import core.game.system.task.Pulse
import core.game.world.GameWorld
import core.game.world.map.Location
import core.tools.RandomFunction
import shared.consts.NPCs

class PoisonSpiderNPC(id: Int = 0, location: Location? = null) : AbstractNPC(id, location) {

    override fun construct(id: Int, location: Location, vararg objects: Any): AbstractNPC =
        PoisonSpiderNPC(id, location)

    override fun getIds(): IntArray = intArrayOf(NPCs.POISON_SPIDER_1009)

    override fun handleTickActions() {
        super.handleTickActions()
    }

    companion object {
        fun spawnPoisonSpider(player: Player) {
            val spider = PoisonSpiderNPC(NPCs.POISON_SPIDER_1009)
            spider.location = player.location
            spider.isWalks = false
            spider.isRespawn = false
            spider.isAggressive = true
            spider.isActive = false

            if (spider.asNpc() != null && spider.isActive) {
                spider.properties.teleportLocation = spider.properties.spawnLocation
            }
            spider.isActive = true
            GameWorld.Pulser.submit(
                object : Pulse(0, spider) {
                    override fun pulse(): Boolean {
                        spider.init()
                        spider.attack(player)
                        return true
                    }
                },
            )
        }
    }

    override fun onAttack(target: Entity?) {
        super.onAttack(target)
        if (target is Player) {
            if (RandomFunction.roll(10)) {
                applyPoison(target, this, 13)
            }
        }
    }

    override fun finalizeDeath(killer: Entity?) {
        clear()
        super.finalizeDeath(killer)
    }
}
