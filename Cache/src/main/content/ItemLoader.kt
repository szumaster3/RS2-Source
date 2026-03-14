package content

import com.alex.tools.ItemCopy
import shared.consts.Items

object ItemLoader {
    fun importItems() {
        val copy = ItemCopy.create().startAt(ItemCopy.create().load())

        copy.addItems({
            //id = Items.ARDOUGNE_CLOAK_1_14701
            name = "Ardougne cloak 1"
            invModelId = 45499
            cost = 0
            stackable = 0
            isMembersOnly = true
            equipSlot = 1
            equipType = 0
            invModelZoom = 2140
            xan2d = 400
            yan2d = 948
            xOffset2d = 3
            yOffset2d = 6
            originalModelColors = intArrayOf(4)
            modifiedModelColors = intArrayOf(181)
            floorScaleX = 128
            floorScaleY = 128
            floorScaleZ = 128
            maleEquipModelId1 = 45497
            femaleEquipModelId1 = 45498
            inventoryOptions = arrayOf(null, "Wear", null, "Teleports", "Destroy")
            groundOptions = arrayOf(null, null, "Take", null, null)
        })

        copy.addItems({
            //id = Items.ARDOUGNE_CLOAK_2_14702
            name = "Ardougne cloak 2"
            invModelId = 45499
            cost = 0
            stackable = 0
            isMembersOnly = true
            equipSlot = 1
            equipType = 0
            invModelZoom = 2140
            xan2d = 400
            yan2d = 948
            xOffset2d = 3
            yOffset2d = 6
            originalModelColors = intArrayOf(4, -19424, -19426)
            modifiedModelColors = intArrayOf(168, -19427, -19429)
            floorScaleX = 128
            floorScaleY = 128
            floorScaleZ = 128
            maleEquipModelId1 = 45497
            femaleEquipModelId1 = 45498
            inventoryOptions = arrayOf(null, "Wear", null, "Teleports", "Destroy")
            groundOptions = arrayOf(null, null, "Take", null, null)
        })

        copy.addItems({
            //id = Items.ARDOUGNE_CLOAK_3_14703
            name = "Ardougne cloak 3"
            invModelId = 45499
            cost = 0
            stackable = 0
            isMembersOnly = true
            equipSlot = 1
            equipType = 0
            invModelZoom = 2140
            xan2d = 400
            yan2d = 948
            xOffset2d = 3
            yOffset2d = 6
            floorScaleX = 128
            floorScaleY = 128
            floorScaleZ = 128
            maleEquipModelId1 = 45497
            femaleEquipModelId1 = 45498
            inventoryOptions = arrayOf(null, "Wear", null, "Teleports", "Destroy")
            groundOptions = arrayOf(null, null, "Take", null, null)
        })

        copy.addItems({
            //id = Items.ANTIQUE_LAMP_14704
            name = "Antique lamp"
            invModelId = 3348
            cost = 1
            stackable = 0
            isMembersOnly = true
            equipSlot = -1
            equipType = 0
            invModelZoom = 840
            xan2d = 28
            yan2d = 228
            xOffset2d = 2
            yOffset2d = -2
            originalModelColors = intArrayOf(11191, 11183)
            modifiedModelColors = intArrayOf(840, 563)
            floorScaleX = 128
            floorScaleY = 128
            floorScaleZ = 128
            inventoryOptions = arrayOf("Rub", null, null, null, "Destroy")
            groundOptions = arrayOf(null, null, "Take", null, null)
            clientScriptData = mutableMapOf(59 to "1")
        })

        copy.addItems({
            //id = Items.ANTIQUE_LAMP_14705
            name = "Antique lamp"
            invModelId = 3348
            cost = 1
            stackable = 0
            isMembersOnly = true
            equipSlot = -1
            equipType = 0
            invModelZoom = 840
            xan2d = 28
            yan2d = 228
            xOffset2d = 2
            yOffset2d = -2
            originalModelColors = intArrayOf(11191, 11183)
            modifiedModelColors = intArrayOf(840, 563)
            floorScaleX = 128
            floorScaleY = 128
            floorScaleZ = 128
            inventoryOptions = arrayOf("Rub", null, null, null, "Destroy")
            groundOptions = arrayOf(null, null, "Take", null, null)
            clientScriptData = mutableMapOf(59 to "1")
        })

        copy.addItems({
            //id = Items.ANTIQUE_LAMP_14706
            name = "Antique lamp"
            invModelId = 3348
            cost = 1
            stackable = 0
            isMembersOnly = true
            equipSlot = -1
            equipType = 0
            invModelZoom = 840
            xan2d = 28
            yan2d = 228
            xOffset2d = 2
            yOffset2d = -2
            originalModelColors = intArrayOf(11191, 11183)
            modifiedModelColors = intArrayOf(840, 563)
            floorScaleX = 128
            floorScaleY = 128
            floorScaleZ = 128
            inventoryOptions = arrayOf("Rub", null, null, null, "Destroy")
            groundOptions = arrayOf(null, null, "Take", null, null)
            clientScriptData = mutableMapOf(59 to "1")
        })

        copy.save()
    }
}