package content.items

import com.alex.tools.ItemCopy

object ArdougneCloaks {

    fun importItems() {
        val copy = ItemCopy.create().startAt(14701)

        copy.addItems(
            {
                name = "Ardougne cloak 1"
                invModelId = 45499
                cost = 0
                stackable = 0
                isMembersOnly = true
                invModelZoom = 2140
                xan2d = 400
                yan2d = 948
                xOffset2d = 3
                yOffset2d = 6
                changeModelColor(4, 181)
                maleEquipModelId1 = 45497
                femaleEquipModelId1 = 45498
                teamId = 0
                inventoryOptions = arrayOfNulls<String>(5).apply {
                    this[1] = "Wear"
                    this[3] = "Teleports"
                    this[4] = "Destroy"
                }
                groundOptions = arrayOfNulls<String>(5).apply { this[2] = "Take" }
            },
            {
                name = "Ardougne cloak 2"
                invModelId = 45499
                cost = 0
                stackable = 0
                isMembersOnly = true
                invModelZoom = 2140
                xan2d = 400
                yan2d = 948
                xOffset2d = 3
                yOffset2d = 6
                changeModelColor(4, 168)
                changeModelColor(46112, 46109)
                changeModelColor(46110, 46107)
                maleEquipModelId1 = 45497
                femaleEquipModelId1 = 45498
                teamId = 0
                inventoryOptions = arrayOfNulls<String>(5).apply {
                    this[1] = "Wear"
                    this[3] = "Teleports"
                    this[4] = "Destroy"
                }
                groundOptions = arrayOfNulls<String>(5).apply { this[2] = "Take" }
            },
            {
                name = "Ardougne cloak 3"
                invModelId = 45499
                cost = 0
                stackable = 0
                isMembersOnly = true
                invModelZoom = 2140
                xan2d = 400
                yan2d = 948
                xOffset2d = 3
                yOffset2d = 6
                maleEquipModelId1 = 45497
                femaleEquipModelId1 = 45498
                teamId = 0
                inventoryOptions = arrayOfNulls<String>(5).apply {
                    this[1] = "Wear"
                    this[3] = "Teleports"
                    this[4] = "Destroy"
                }
                groundOptions = arrayOfNulls<String>(5).apply { this[2] = "Take" }
            }
        )

        copy.save()
    }
}