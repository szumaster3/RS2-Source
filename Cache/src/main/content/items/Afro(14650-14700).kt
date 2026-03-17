package content.items

import backend.alex.tools.ItemPacker

object `Afro(14650-14700)` {
    fun add() {
        val packer = ItemPacker.create().startAt(14650)
        val afroData = listOf(
            Triple("Burgundy afro",     908,    899),
            Triple("Red afro",          571,    581),
            Triple("Vermilion afro",    2983,   2973),
            Triple("Pink afro",         468,    478),
            Triple("Orange afro",       5698,   5688),
            Triple("Yellow afro",       7752,   7742),
            Triple("Peach afro",        3650,   3640),
            Triple("Brown afro",        4634,   4624),
            Triple("Dark brown afro",   6540,   6535),
            Triple("Light brown afro",  5677,   5667),
            Triple("Mint green afro",   21830,  21820),
            Triple("Green afro",        22433,  22423),
            Triple("Dark green afro",   28946,  28936),
            Triple("Dark blue afro",    43691,  43681),
            Triple("Turquoise afro",    33697,  33687),
            Triple("Light blue afro",   38362,  38352),
            Triple("Purple afro",       51526,  51516),
            Triple("Violet afro",       54193,  54183),
            Triple("Indigo afro",       49835,  49825),
            Triple("Dark grey afro",    20,     15),
            Triple("Military grey afro",10295,  10285),
            Triple("White afro",        107,    98),
            Triple("Light grey afro",   10438,  10428),
            Triple("Taupe afro",        5281,   5271),
            Triple("Black afro",        8,      1)
        )

        afroData.forEach { (name, recol1d, recol2d) ->
            packer.addItem {
                this.name = name
                modelId = 45467
                zoom2d = 728
                xan2d = 30
                yan2d = 180
                zan2d = 0
                xOffset2d = 0
                yOffset2d = 1
                stackable = 0
                cost = 50
                membersOnly = true
                equipSlot = -1
                equipType = -1
                maleEquip1 = 45470
                maleEquip2 = -1
                maleEquipModelId3 = -1
                femaleEquip1 = 45471
                femaleEquip2 = -1
                femaleEquipModelId3 = -1
                primaryMaleDialogueHead = 45468
                primaryFemaleDialogueHead = 45469
                secondaryMaleDialogueHead = -1
                secondaryFemaleDialogueHead = -1
                dummyItem = 0
                notedItemId = this.id + 1
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
                groundOptions = arrayOfNulls<String>(5).apply { this[2] = "Take" }
                inventoryOptions = arrayOfNulls<String>(5).apply { this[1] = "Wear"; this[4] = "Drop" }
                changeModelColor(6805, recol1d)
                changeModelColor(6794, recol2d)
            }

            packer.addNoteItem()
        }

        packer.save()
    }
}