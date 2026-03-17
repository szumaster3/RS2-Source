package content.items

import backend.alex.tools.ItemPacker

object `ArdougneCloaks(14638-14640)` {

    fun add() {
        val copy = ItemPacker.create().startAt(14638)

        copy.addItems(
            {
                name = "Ardougne cloak 1"
                invModelId = 45482
                cost = 0
                stackable = 0
                isMembersOnly = true
                invModelZoom = 2140
                xan2d = 400
                yan2d = 948
                xOffset2d = 3
                yOffset2d = 6
                changeModelColor(4, 181)
                maleEquipModelId1 = 45480
                femaleEquipModelId1 = 45481
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
                invModelId = 45482
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
                maleEquipModelId1 = 45480
                femaleEquipModelId1 = 45481
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
                invModelId = 45482
                cost = 0
                stackable = 0
                isMembersOnly = true
                invModelZoom = 2140
                xan2d = 400
                yan2d = 948
                xOffset2d = 3
                yOffset2d = 6
                maleEquipModelId1 = 45480
                femaleEquipModelId1 = 45481
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