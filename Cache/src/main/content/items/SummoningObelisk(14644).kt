package content.items

import backend.alex.tools.ItemPacker
import shared.consts.Items

object `SummoningObelisk(14644)` {

    fun add() {
        val copy = ItemPacker.create().startAt(14644)
        copy.addItems(
            {
                name = "Summoning obelisk"
                modelId = 31686
                zoom2d = 5456
                xan2d = 210
                yan2d = 1880
                zan2d = 0
                xOffset2d = 0
                yOffset2d = -100
                stackable = 0
                cost = 1
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
                dummyItem = 1
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
                inventoryOptions = arrayOfNulls<String>(5).apply {
                    this[4] = "Drop"
                }
                groundOptions = arrayOfNulls<String>(5).apply {
                    this[2] = "Take"
                }
                clientScriptData = HashMap<Any, Any>().apply {
                    put(211, "${Items.MARBLE_BLOCK_8786}")
                    put(212, "1")    // Amount
                    put(213, "${Items.SPIRIT_SHARDS_12183}")
                    put(214, "1000") // Amount
                    put(215, "${Items.CRIMSON_CHARM_12160}")
                    put(216, "10")   // Amount
                    put(217, "${Items.BLUE_CHARM_12163}")
                    put(218, "10")   // Amount

                }
            },
        )

        copy.save()
    }
}