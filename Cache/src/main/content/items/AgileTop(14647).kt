package content.items

import backend.alex.tools.ItemPacker

object `AgileTop(14647)` {

    fun add() {
        val copy = ItemPacker.create().startAt(14647)

        copy.addItems(
            {
                name = "Agile top"
                modelId = 45475
                zoom2d = 1663
                xan2d = 593
                yan2d = 0
                zan2d = 0
                xOffset2d = 0
                yOffset2d = 1
                stackable = 0
                cost = 1
                membersOnly = true
                equipSlot = -1
                equipType = -1
                maleEquip1 = 45476
                maleEquip2 = 45478
                maleEquipModelId3 = -1
                femaleEquip1 = 45477
                femaleEquip2 = 45479
                femaleEquipModelId3 = -1
                primaryMaleDialogueHead = -1
                primaryFemaleDialogueHead = -1
                secondaryMaleDialogueHead = -1
                secondaryFemaleDialogueHead = -1
                dummyItem = 0
                notedItemId = -1
                switchNoteItemId = -1
                lendedItemId = -1
                switchLendItemId = -1
                unnoted = false
                floorScaleX = 128
                floorScaleY = 128
                floorScaleZ = 128
                ambience = 0
                diffusion = 0
                teamId = 0
                maleWieldX = 0
                maleWieldY = 0
                maleWieldZ = 0
                femaleWieldX = 0
                femaleWieldY = 0
                femaleWieldZ = 0
                unknownInt18 = 0
                unknownInt19 = 0
                unknownInt20 = 0
                unknownInt21 = 0
                unknownInt22 = 0
                unknownInt23 = 0
                unknownValue1 = 0
                unknownValue2 = 0
                groundOptions = arrayOfNulls<String>(5).apply { this[2] = "Take" }
                inventoryOptions = arrayOfNulls<String>(5).apply {
                    this[1] = "Wear"
                    this[4] = "Destroy"
                }
            }
        )
        copy.save()
    }
}