package content.region.kandarin.gnome_stronghold.quest.itgronigen

import content.data.GameAttributes
import core.api.MapArea
import core.api.removeAttribute
import core.game.node.entity.Entity
import core.game.node.entity.player.Player
import core.game.world.map.zone.ZoneBorders

class ObservatoryDungeon : MapArea
{

    override fun defineAreaBorders() = arrayOf(
        ZoneBorders(2304, 9344, 2367, 9407)
    )

    override fun areaLeave(entity: Entity, logout: Boolean)
    {
        if (entity is Player)
        {
            removeAttribute(
                entity,
                GameAttributes.OBSERVATORY_CHEST_FAIL_COUNTER
            )
        }
    }
}