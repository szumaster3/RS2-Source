package content

import content.items.AntiqueLamps
import content.items.ArdougneCloaks

object ItemLoader {
    fun importItems() {
        ArdougneCloaks.importItems() // 14701 - 14703
        AntiqueLamps.importItems()   // 14704 - 14706
    }
}