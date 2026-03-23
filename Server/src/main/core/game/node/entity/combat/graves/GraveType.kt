package core.game.node.entity.combat.graves

import core.cache.def.impl.DataMap
import shared.consts.Quests

enum class GraveType(val text: String) {
    MemorialPlaque("In memory of @name,<br>who died here."),
    Flag(MemorialPlaque.text),
    SmallGravestone("In loving memory of our dear friend @name,<br>who died in this place @mins ago."),
    OrnateGravestone(SmallGravestone.text),
    FontofLife("In your travels, pause awhile to remember @name,<br>who passed away at this spot."),
    Stele(FontofLife.text),
    SymbolofSaradomin("@name,<br>an enlightened servant of Saradomin,<br>perished in this place."),
    SymbolofZamorak("@name,<br>a most bloodthirsty follower of Zamorak,<br>perished in this place."),
    SymbolofGuthix("@name,<br>who walked with the Balance of Guthix,<br>perished in this place."),
    SymbolofBandos("@name,<br>a vicious warrior dedicated to Bandos,<br>perished in this place."),
    SymbolofArmadyl("@name,<br>a follower of the Law of Armadyl,<br>perished in this place."),
    AncientZarosSymbol("@name,<br>servant of the Unknown Power,<br>perished in this place."),
    AngelofDeath("Ye frail mortals who gaze upon this sight, forget not<br>the fate of @name, once mighty, now<br>surrendered to the inescapable grasp of destiny.<br>Requiescat in pace.");

    private val type: Int?
        get() = when (this)
        {
            MemorialPlaque -> null
            Flag -> 1
            SmallGravestone -> 2
            OrnateGravestone -> 3
            FontofLife -> 4
            Stele -> 5
            SymbolofSaradomin -> 6
            SymbolofZamorak -> 7
            SymbolofGuthix -> 8
            SymbolofBandos -> 9
            SymbolofArmadyl -> 10
            AncientZarosSymbol -> 11
            AngelofDeath -> 12
        }

    val npcId: Int
        get() = type?.let { npcMap.getInt(it) } ?: npcMap.defaultInt

    val cost: Int
        get() = type?.let { costMap.getInt(it) } ?: costMap.defaultInt

    val duration: Int
        get() = when (this)
        {
            MemorialPlaque, Flag -> 2
            SmallGravestone -> 2
            OrnateGravestone -> 3
            FontofLife, Stele -> 4
            SymbolofSaradomin, SymbolofZamorak, SymbolofGuthix,
            SymbolofBandos, SymbolofArmadyl, AncientZarosSymbol -> 4
            AngelofDeath -> 5
        }

    val isMembers: Boolean
        get() = this != MemorialPlaque && this != Flag && this != SmallGravestone && this != OrnateGravestone

    val prerequisite: String?
        get() = when (this)
        {
            SymbolofBandos     -> Quests.LAND_OF_THE_GOBLINS
            SymbolofArmadyl    -> Quests.TEMPLE_OF_IKOV
            AncientZarosSymbol -> Quests.DESERT_TREASURE
            else -> null
        }

    companion object {
        val npcMap: DataMap = DataMap.get(1098)
        val costMap: DataMap = DataMap.get(1101)

        val DEFAULT: GraveType = MemorialPlaque

        val ids: IntArray = (values()
            .map { it.npcId } + npcMap.defaultInt)
            .distinct()
            .toIntArray()
    }
}