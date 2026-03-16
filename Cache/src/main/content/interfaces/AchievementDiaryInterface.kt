package content.interfaces

import com.alex.loaders.interfaces.ComponentType
import com.alex.tools.IfacePacker
import consts.Color
import consts.InterfaceFont
import shared.consts.Components

object AchievementDiaryInterface {
    fun add() {
        IfacePacker.to(Components.AREA_TASK_259)
            .from(Components.AREA_TASK_259)
            .startAt(31)
            .copy(31)
            .modify {
                text               = "Ardougne"
                onVarpTransmit     = arrayOf(58)
            }
            .save()

        val baseY = 455
        val step = 14
        val labels = listOf("Easy", "Medium", "Hard")
        val prefix = "area_task_5_"

        labels.forEachIndexed { index, label ->
            IfacePacker.to(Components.AREA_TASK_259)
                .startAt(34 + index)
                .addComponent {
                    type               = ComponentType.TEXT
                    parentId           = 7
                    baseX              = 28
                    this.baseY         = baseY + step * index
                    baseWidth          = 152
                    baseHeight         = 15
                    fontId             = InterfaceFont.P11_FULL
                    color              = Color.RED
                    text               = label
                    shadow             = true
                    optionMask         = 2
                    rightClickOptions  = arrayOf("Read Journal")
                    onMouseHoverScript = arrayOf(45, componentHash, Color.WHITE)
                    onMouseLeaveScript = arrayOf(45, componentHash, Color.RED)
                    onVarpTransmit     = arrayOf(58)
                }
                .save()

        }
    }
}