package content.objects

import com.alex.tools.LocPacker
import shared.consts.Scenery

object `Obelisk(42004)` {

    fun add() {
        LocPacker.create()
            .startAt(Scenery.OBELISK_42004)
            .copyLoc(Scenery.OBELISK_28716)
            .modify {
                actions = arrayOfNulls<String>(5).apply {
                    this[0] = "Renew-points"
                    this[4] = "Remove"
                }
            }
            .save()
    }
}