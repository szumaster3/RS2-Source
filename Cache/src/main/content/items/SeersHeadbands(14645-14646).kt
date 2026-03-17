package content.items

import backend.alex.tools.ItemPacker

object `SeersHeadbands(14645-14646)` {

    fun add() {
        val copy = ItemPacker.create().startAt(14645)

        copy.addItems(
            {
                name = "Seer's headband 2"
                modelId = 45487
                zoom2d = 724
                xan2d = 111
                yan2d = 1841
                zan2d = 0
                xOffset2d = -3
                yOffset2d = -8
                stackable = 0
                cost = 1
                membersOnly = true
                equipSlot = -1
                equipType = -1
                maleEquip1 = 45490
                maleEquip2 = -1
                maleEquipModelId3 = -1
                femaleEquip1 = 45491
                femaleEquip2 = -1
                femaleEquipModelId3 = -1
                primaryMaleDialogueHead = 45488
                primaryFemaleDialogueHead = 45489
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
                val originalModelColors = arrayOf(8997,8994,8990,8988,8986,8982)
                val modifiedModelColors = arrayOf(30803,30798,30793,30788,30783,30778)
                for (i in originalModelColors.indices) {
                    changeModelColor(originalModelColors[i], modifiedModelColors[i])
                }
            },
            {
                name = "Seer's headband 3"
                modelId = 45492
                zoom2d = 852
                xan2d = 111
                yan2d = 1841
                zan2d = 0
                xOffset2d = -3
                yOffset2d = -3
                stackable = 0
                cost = 1
                membersOnly = true
                equipSlot = -1
                equipType = -1
                maleEquip1 = 45495
                maleEquip2 = -1
                maleEquipModelId3 = -1
                femaleEquip1 = 45496
                femaleEquip2 = -1
                femaleEquipModelId3 = -1
                primaryMaleDialogueHead = 45493
                primaryFemaleDialogueHead = 45494
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
                val originalModelColors = arrayOf(8997,8994,8990,8988,8986,8982)
                val modifiedModelColors = arrayOf(9043,9038,9033,9023,9018,9013)
                for (i in originalModelColors.indices) {
                    changeModelColor(originalModelColors[i], modifiedModelColors[i])
                }
            }
        )
        copy.save()
    }
}