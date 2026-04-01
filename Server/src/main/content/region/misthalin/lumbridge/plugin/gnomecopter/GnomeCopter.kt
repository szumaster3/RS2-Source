package content.region.misthalin.lumbridge.plugin.gnomecopter

import core.api.MapArea
import core.game.world.map.zone.ZoneBorders
import core.game.world.map.zone.ZoneRestriction
import shared.consts.Regions

class GnomeCopter : MapArea {
    override fun defineAreaBorders(): Array<ZoneBorders> {
        return arrayOf(
            ZoneBorders.forRegion(Regions.GNOMECOPTER_CASTLE_WARS_8020),
            ZoneBorders.forRegion(Regions.GNOMECOPTER_HUNTER_AREA_10068),
            ZoneBorders.forRegion(Regions.GNOMECOPTER_BURTHORPE_9556),
            ZoneBorders.forRegion(Regions.GNOMECOPTER_LLETYA_11093),
            ZoneBorders.forRegion(Regions.GNOMECOPTER_PEST_CONTROL_9044),
            ZoneBorders.forRegion(Regions.GNOMECOPTER_BURGH_DE_ROTT_8532),
        )
    }

    override fun getRestrictions(): Array<ZoneRestriction> {
        return arrayOf(
            ZoneRestriction.RANDOM_EVENTS,
            ZoneRestriction.CANNON,
            ZoneRestriction.FOLLOWERS
        )
    }
}