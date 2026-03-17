package content

import backend.alex.Cache
import backend.alex.loaders.BasDefinition
import backend.alex.loaders.LocDefinition
import backend.alex.loaders.items.ItemDefinition
import backend.alex.tools.*
import content.items.*
import content.objects.`Obelisk(42004)`

object ContentLoader {
    @JvmStatic
    fun main(args: Array<String>) {
        runCatching {
            Cache.init()
            //print()
            load()
        }.onFailure { e ->
            e.printStackTrace()
        }
    }

    private fun load() {
        models()
        sprites()
        objects()
        interfaces()
        items()
    }

    private fun interfaces() {
        content.interfaces.AchievementDiaryInterface.add()
    }

    private fun items() {
        `FixSeersHeadband(14631)`.add()
        `ArdougneCloaks(14638-14640)`.add()
        `AntiqueLamps(14641-14643)`.add()
        `AntiqueLamps(14641-14643)`.add()
        `SummoningObelisk(14644)`.add()
        `SeersHeadbands(14645-14646)`.add()
        `AgileTop(14647)`.add()
        `AgileLegs(14648)`.add()
        `RandomEventGift(14649)`.add()
        `Afro(14650-14700)`.add()
    }

    private fun objects() {
        `Obelisk(42004)`.add()
    }

    private fun models() {
        ModelPacker.add()
    }

    private fun sprites() {
        //SpritePacker.add()
    }

    private fun print() {
        LocDefinition.print(Cache.getStore(), "dumps/object_dumps.txt")
        ItemDefinition.print(Cache.getStore(), "dumps/item_dumps.txt")
        BasDefinition.print(Cache.getStore(), "dumps/bas_dumps.txt")
    }

    private fun dump() {
        MapDumper.dump()
        SpriteDumper.dump()
        ModelDumper.dump()
    }
}