package content.items

import backend.alex.tools.ItemPacker
import shared.consts.Items

object `FixSeersHeadband(14631)` {

    fun add() {
        ItemPacker.create()
            .copyItem(Items.SEERS_HEADBAND_1_14631) {
                name                        = "Seer's headband 1"
                primaryMaleDialogueHead     = 45483
                primaryFemaleDialogueHead   = 45484
                maleEquipModelId1           = 45485
                femaleEquipModelId1         = 45486
                maleEquipModelId2           = -1
                femaleEquipModelId2         = -1

                val originalModelColors     = arrayOf(8997,8994,8990,8988,8986,8982)
                val modifiedModelColors     = arrayOf(5425,5420,5415,5410,5405,5400)

                for (i in originalModelColors.indices) {
                    changeModelColor(originalModelColors[i], modifiedModelColors[i])
                }
            }
            .save()
    }
}