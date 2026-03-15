package content.items

import com.alex.tools.ItemCopy

object AntiqueLamps {

    fun importItems() {
        val copy = ItemCopy.create().startAt(14704)

        copy.addItems(
            {
                name = "Antique lamp"
                invModelId = 3348
                cost = 1
                stackable = 0
                isMembersOnly = true
                invModelZoom = 840
                xan2d = 28
                yan2d = 228
                xOffset2d = 2
                yOffset2d = -2
                changeModelColor(11191, 840)
                changeModelColor(11183, 563)
                teamId = -1
                inventoryOptions = arrayOfNulls<String>(5).apply {
                    this[0] = "Rub"
                    this[4] = "Destroy"
                }
                groundOptions = arrayOfNulls<String>(5).apply {
                    this[2] = "Take"
                }
                clientScriptData = HashMap<Any, Any>().apply { put(59, "1") }
            },
            {
                name = "Antique lamp"
                invModelId = 3348
                cost = 1
                stackable = 0
                isMembersOnly = true
                invModelZoom = 840
                xan2d = 28
                yan2d = 228
                xOffset2d = 2
                yOffset2d = -2
                changeModelColor(11191, 840)
                changeModelColor(11183, 563)
                teamId = -1
                inventoryOptions = arrayOfNulls<String>(5).apply {
                    this[0] = "Rub"
                    this[4] = "Destroy"
                }
                groundOptions = arrayOfNulls<String>(5).apply {
                    this[2] = "Take"
                }
                clientScriptData = HashMap<Any, Any>().apply { put(59, "1") }
            },
            {
                name = "Antique lamp"
                invModelId = 3348
                cost = 1
                stackable = 0
                isMembersOnly = true
                invModelZoom = 840
                xan2d = 28
                yan2d = 228
                xOffset2d = 2
                yOffset2d = -2
                changeModelColor(11191, 840)
                changeModelColor(11183, 563)
                teamId = -1
                inventoryOptions = arrayOfNulls<String>(5).apply {
                    this[0] = "Rub"
                    this[4] = "Destroy"
                }
                groundOptions = arrayOfNulls<String>(5).apply {
                    this[2] = "Take"
                }
                clientScriptData = HashMap<Any, Any>().apply { put(59, "1") }
            }
        )

        copy.save()
        copy.getCopiedItems().forEach { println("Packed item: ${it.name}:${it.id}") }
    }
}