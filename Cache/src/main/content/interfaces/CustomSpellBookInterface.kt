package content.interfaces

import com.alex.loaders.interfaces.ComponentDefinition
import com.alex.loaders.interfaces.ComponentType
import com.alex.loaders.interfaces.IComponentSettings
import com.alex.tools.IfacePacker
import consts.OptionMask
import consts.Sprite
import shared.consts.Items

// TODO.
object CustomSpellBookInterface {

    fun add() {

        val tabLayer = ComponentDefinition.getInterfaceComponent(834,16)?.componentHash

        IfacePacker.to(834)
            .startAt(0)
            .addComponent {
                name               = "guild_home_teleport"
                version            = 3
                parentId           = -1
                type               = 5
                baseX              = 19
                baseY              = 8
                baseWidth          = 24
                baseHeight         = 24
                spriteId           = 356
                optionMask         = 2
                settings           = IComponentSettings(2, -1)
                hasScripts         = true
                rightClickOptions  = arrayOf("Cast")
                onLoadScript       = arrayOf(6, -2147483645, tabLayer, 356, 406, 0, "Guild Hall Home Teleport", "Requires no runes - recharge time 30 mins. Warning: This spell takes a long time to cast and will be interrupted by combat.", -1, 0, -1, 0, -1, 0, -1, 0)
            }.save()

        IfacePacker.to(834)
            .startAt(1)
            .addComponent {
                name               = "blizzard_spell"
                version            = 3
                parentId           = -1
                type               = ComponentType.SPRITE
                baseX              = 64
                baseY              = 8
                baseWidth          = 24
                baseHeight         = 24
                spriteId           = Sprite.BLIZZARD_SPELL_OFF
                optionCircumfix    = "Cast"
                optionMask         = OptionMask.COMBAT_SPELL
                settings           = IComponentSettings(OptionMask.COMBAT_SPELL, -1)
                hasScripts         = true
                onLoadScript       = arrayOf(6, -2147483645, tabLayer, 356, 406, 0, Sprite.BLIZZARD_SPELL_ON, Sprite.BLIZZARD_SPELL_OFF, 9, "Blizzard", "A low level Ice missile", Items.MIST_RUNE_4695, 1, -1, 0, -1, 0, -1, 0)
            }.save()

        IfacePacker.to(834)
            .startAt(2)
            .addComponent {
                name               = "invisible_spell_lv1"
                version            = 3
                parentId           = -1
                type               = ComponentType.SPRITE
                baseX              = 110
                baseY              = 8
                baseWidth          = 24
                baseHeight         = 24
                spriteId           = Sprite.INVISIBLE_SPELL_OFF
                optionCircumfix    = "Cast"
                optionMask         = OptionMask.CAST_ON_SELF
                settings           = IComponentSettings(OptionMask.CAST_ON_SELF, -1)
                hasScripts         = true
                onLoadScript       = arrayOf(6, -2147483645, tabLayer, Sprite.INVISIBLE_SPELL_ON, Sprite.INVISIBLE_SPELL_OFF, 50, "Lvl-1 Invisible", "Become invisible for 5 seconds, avoiding opponent's attacks", Items.GHOSTLY_GLOVES_6110, 1, Items.STEAM_RUNE_4694, 4, -1, 0, -1, 0)
            }.save()

        IfacePacker.to(834)
            .startAt(3)
            .addComponent {
                name               = "invisible_spell_lv2"
                version            = 3
                parentId           = -1
                type               = ComponentType.SPRITE
                baseX              = 151
                baseY              = 8
                baseWidth          = 24
                baseHeight         = 24
                spriteId           = Sprite.INVISIBLE_SPELL_LV2_OFF
                optionCircumfix    = "Cast"
                optionMask         = OptionMask.CAST_ON_SELF
                settings           = IComponentSettings(OptionMask.CAST_ON_SELF, -1)
                hasScripts         = true
                onLoadScript       = arrayOf(6, -2147483645, tabLayer, Sprite.INVISIBLE_SPELL_LV2_ON, Sprite.INVISIBLE_SPELL_LV2_OFF, 70, "Lvl-2 Invisible", "Become invisible for 10 seconds, avoiding opponent's attacks", Items.GHOSTLY_GLOVES_6110, 1, Items.STEAM_RUNE_4694, 8, -1, 0, -1, 0)
            }.save()

        IfacePacker.to(834)
            .startAt(4)
            .addComponent {
                name               = "invisible_spell_lv3"
                version            = 3
                parentId           = -1
                type               = ComponentType.SPRITE
                baseX              = 19
                baseY              = 36
                baseWidth          = 24
                baseHeight         = 24
                spriteId           = Sprite.INVISIBLE_SPELL_LV3_OFF
                optionCircumfix    = "Cast"
                optionMask         = OptionMask.CAST_ON_SELF
                settings           = IComponentSettings(OptionMask.CAST_ON_SELF, -1)
                hasScripts         = true
                onLoadScript       = arrayOf(6, -2147483645, tabLayer, Sprite.INVISIBLE_SPELL_LV3_ON, Sprite.INVISIBLE_SPELL_LV3_OFF, 90, "Lvl-3 Invisible", "Become invisible for 15 seconds, avoiding opponent's attacks", Items.GHOSTLY_GLOVES_6110, 1, Items.STEAM_RUNE_4694, 12, -1, 0, -1, 0)
            }.save()

        IfacePacker.to(834)
            .startAt(5)
            .addComponent {
                name               = "god_wars_dungeon_teleport"
                version            = 3
                parentId           = -1
                type               = ComponentType.SPRITE
                baseX              = 64
                baseY              = 36
                baseWidth          = 24
                baseHeight         = 24
                spriteId           = Sprite.GWD_TELEPORT_SPELL_OFF
                optionCircumfix    = "Cast"
                optionMask         = OptionMask.CAST_ON_SELF
                settings           = IComponentSettings(OptionMask.CAST_ON_SELF, -1)
                hasScripts         = true
                onLoadScript       = arrayOf(6, -2147483645, tabLayer, Sprite.GWD_TELEPORT_SPELL_ON, Sprite.GWD_TELEPORT_SPELL_OFF, 99, "God Wars Dungeon Teleport", "Teleports you to God Wars Dungeon", 0, 1, 0, 1, -1, 0, -1, 0)
            }.save()

        IfacePacker.to(834)
            .startAt(6)
            .addComponent {
                name               = "guild_warehouse_teleport"
                version            = 3
                parentId           = -1
                type               = ComponentType.SPRITE
                baseX              = 110
                baseY              = 36
                baseWidth          = 24
                baseHeight         = 24
                spriteId           = Sprite.MISSION_TELEPORT_SPELL_OFF
                optionCircumfix    = "Cast"
                optionMask         = OptionMask.CAST_ON_SELF
                settings           = IComponentSettings(OptionMask.CAST_ON_SELF, -1)
                hasScripts         = true
                onLoadScript       = arrayOf(6, -2147483645, tabLayer, Sprite.MISSION_TELEPORT_SPELL_ON, Sprite.MISSION_TELEPORT_SPELL_OFF, 25, "Guild Teleport", "Teleport you to Guild Warehouse", 0, 1, Items.LAW_RUNE_563, 1, Items.AIR_RUNE_556, 1, -1, 0)
            }.save()

        IfacePacker.to(834)
            .startAt(7)
            .addComponent {
                name               = "create_food_spell"
                version            = 3
                parentId           = -1
                type               = ComponentType.SPRITE
                baseX              = 151
                baseY              = 36
                baseWidth          = 24
                baseHeight         = 24
                spriteId           = Sprite.CREATE_FOOD_SPELL_OFF
                optionCircumfix    = "Cast"
                optionMask         = OptionMask.CAST_ON_SELF
                settings           = IComponentSettings(OptionMask.CAST_ON_SELF, -1)
                hasScripts         = true
                onLoadScript       = arrayOf(6, -2147483645, tabLayer, Sprite.CREATE_FOOD_SPELL_ON, Sprite.CREATE_FOOD_SPELL_OFF, 80, "Create Meat", "Creates random meat. Reduces Magic Level by 3.", Items.LAVA_RUNE_4699, 2, Items.NATURE_RUNE_561, 1, 0, -1, 0)
            }.save()

        IfacePacker.to(834)
            .startAt(8)
            .addComponent {
                name               = "sort_button_highlight"
                version            = 3
                parentId           = -1
                type               = ComponentType.SPRITE
                baseX              = 43
                baseY              = 5
                baseWidth          = 20
                baseHeight         = 20
                xMode              = 2
                yMode              = 2
                spriteId           = 1701
                optionMask         = 1026
                settings           = IComponentSettings(1026, -1)
                hasScripts         = true
                varpTriggers       = intArrayOf(1376)// VarbitID: 357
                rightClickOptions  = arrayOf("Sort", "", "", "", "", "", "", "", "", "  ")
                onMouseLeaveScript = arrayOf(2060, -2147483645)
                onVarpTransmit     = arrayOf(2057)
                onMouseRepeat      = arrayOf(2061, -2147483645)
                onClickRepeat      = arrayOf(2058, 0)
            }.save()

        IfacePacker.to(834)
            .startAt(9)
            .addComponent {
                name               = "sort_button_v1"
                version            = 3
                parentId           = -1
                type               = ComponentType.SPRITE
                baseX              = 24
                baseY              = 5
                baseWidth          = 20
                baseHeight         = 20
                xMode              = 2
                yMode              = 2
                spriteId           = 1702
                optionMask         = 1026
                settings           = IComponentSettings(1026, -1)
                hasScripts         = true
                rightClickOptions  = arrayOf("Sort", "", "", "", "", "", "", "", "", "   ")
                onMouseLeaveScript = arrayOf(2060, -2147483645)
                onMouseRepeat      = arrayOf(2061, -2147483645)
                onClickRepeat      = arrayOf(2058, 1)
            }.save()

        IfacePacker.to(834)
            .startAt(10)
            .addComponent {
                name               = "sort_button_v2"
                version            = 3
                parentId           = -1
                type               = ComponentType.SPRITE
                baseX              = 5
                baseY              = 5
                baseWidth          = 20
                baseHeight         = 20
                xMode              = 2
                yMode              = 2
                spriteId           = 1702
                optionMask         = 1026
                settings           = IComponentSettings(1026, -1)
                hasScripts         = true
                rightClickOptions  = arrayOf("Sort", "", "", "", "", "", "", "", "", "   ")
                onMouseLeaveScript = arrayOf(2060, -2147483645)
                onMouseRepeat      = arrayOf(2061, -2147483645)
                onClickRepeat      = arrayOf(2058, 2)
            }.save()

        IfacePacker.to(834)
            .startAt(11)
            .addComponent {
                name = "sort_sprite_v1"
                version = 3
                parentId = -1
                type = ComponentType.SPRITE
                baseX = 43
                baseY = 5
                baseWidth = 20
                baseHeight = 20
                xMode = 2
                yMode = 2
                spriteId = 1704
                optionMask = 1024
                settings = IComponentSettings(1024, -1)
                rightClickOptions = arrayOf("", "", "", "", "", "", "", "", "", "   ")
            }.save()

        IfacePacker.to(834)
            .startAt(12)
            .addComponent {
                name               = "sort_sprite_v2"
                version            = 3
                parentId           = -1
                type               = ComponentType.SPRITE
                baseX              = 24
                baseY              = 5
                baseWidth          = 20
                baseHeight         = 20
                xMode              = 2
                yMode              = 2
                spriteId           = 1705
                optionMask         = 1024
                settings           = IComponentSettings(1024, -1)
                rightClickOptions  = arrayOf("", "", "", "", "", "", "", "", "", "   ")
            }.save()

        IfacePacker.to(834)
            .startAt(13)
            .addComponent {
                name               = "sort_sprite_v3"
                version            = 3
                parentId           = -1
                type               = ComponentType.SPRITE
                baseX              = 5
                baseY              = 5
                baseWidth          = 20
                baseHeight         = 20
                xMode              = 2
                yMode              = 2
                spriteId           = 1706
                optionMask         = 1024
                settings           = IComponentSettings(1024, -1)
                rightClickOptions  = arrayOf("", "", "", "", "", "", "", "", "", "   ")
            }.save()

        IfacePacker.to(834)
            .startAt(14)
            .addComponent {
                name               = "layer_v1"
                version            = 3
                type               = ComponentType.LAYER
                baseY              = 29
                baseWidth          = 10
                baseHeight         = 20
                xMode              = 2
                yMode              = 1
                widthMode          = 1
                optionMask         = 1024
                settings           = IComponentSettings(1024, -1)
                rightClickOptions  = arrayOf("", "", "", "", "", "", "", "", "", "  ")
            }.save()

        val parentLayer = ComponentDefinition.getInterfaceComponent(834, 14)!!.componentHash

        IfacePacker.to(834)
            .startAt(15)
            .addComponent {
                name               = "layer_v2"
                version            = 3
                parentId           = parentLayer
                type               = ComponentType.LAYER
                baseX              = 3
                baseY              = 2
                baseWidth          = 42
                baseHeight         = 14
                optionMask         = 1024
                settings           = IComponentSettings(1024, -1)
                rightClickOptions  = arrayOf("", "", "", "", "", "", "", "", "", "  ")
            }.save()

        IfacePacker.to(834)
            .startAt(16)
            .addComponent {
                name               = "tab_border"
                version            = 3
                parentId           = -1
                type               = ComponentType.LAYER
                baseX              = 0
                baseY              = 0
                baseWidth          = 190
                baseHeight         = 261
                settings           = IComponentSettings(-1, -1)
            }.save()

        IfacePacker.to(834)
            .startAt(999999999)
            .addComponent {
                name               = "arctic_dig_spell"
                version            = 3
                type               = ComponentType.SPRITE
                baseX              = 19
                baseY              = 92
                baseWidth          = 24
                baseHeight         = 24
                spriteId           = 999999999
                optionCircumfix    = "Cast"
                optionMask         = OptionMask.CAST_ON_SELF
                settings           = IComponentSettings(OptionMask.CAST_ON_SELF, -1)
                hasScripts         = true
                onLoadScript       = arrayOf(6, -2147483645, tabLayer, -1, -1, 64, "Arctic Dig", "Allows you to dig up treasure in the snow without a spade", Items.ELEMENTAL_RUNE_12850, 4, Items.DUST_RUNE_4696, 3, -1, 0, -1, 0)
            }.save()

        IfacePacker.to(834)
            .startAt(999999999)
            .addComponent {
                name               = "arctic_agility_spell"
                version            = 3
                type               = ComponentType.SPRITE
                baseX              = 64
                baseY              = 92
                baseWidth          = 24
                baseHeight         = 24
                spriteId           = 999999999
                optionCircumfix    = "Cast"
                optionMask         = OptionMask.CAST_ON_SELF
                settings           = IComponentSettings(OptionMask.CAST_ON_SELF, -1)
                hasScripts         = true
                onLoadScript       = arrayOf(6, -2147483645, tabLayer, -1, -1, 86, "Arctic Agility", "Enchants your steps, allowing you to walk across ice", Items.MIST_RUNE_4695, 2, Items.MIND_RUNE_558, 20, Items.ICE_GLOVES_1580, 1, -1, 0)
            }.save()


        // Frost bolt
        // Frost blast
        // Frost wave

        // Storm
        // Thunder storm

    }
}