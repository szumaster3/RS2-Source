package content.items

import backend.alex.tools.ItemPacker

object `RandomEventGift(14649)` {
    fun add() {
        val copy = ItemPacker.create().startAt(14649)

        copy.addItems(
            {
                name = "Random event gift"
                modelId = 2426
                zoom2d = 1180
                xan2d = 97
                yan2d = 1895
                zan2d = 0
                xOffset2d = 0
                yOffset2d = -8
                stackable = 0
                cost = 100
                membersOnly = false
                equipSlot = -1
                equipType = -1
                maleEquip1 = -1
                maleEquip2 = -1
                maleEquipModelId3 = -1
                femaleEquip1 = -1
                femaleEquip2 = -1
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
                ambience = 5
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
                changeModelColor(22410,60325)
                inventoryOptions = arrayOfNulls<String>(5).apply {
                    this[0] = "Open"
                    this[4] = "Drop"
                }
                groundOptions = arrayOfNulls<String>(5).apply {
                    this[2] = "Take"
                }
                clientScriptData = HashMap<Any, Any>().apply { put(59, "1") }
            }
        )
        copy.save()
    }
}