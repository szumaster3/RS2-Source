package content

import com.alex.Cache
import com.alex.loaders.BasDefinition
import com.alex.loaders.ItemDefinition
import com.alex.loaders.LocDefinition
import com.alex.loaders.NpcDefinition
import com.alex.tools.dump.MapDumper
import com.alex.tools.dump.ModelDumper
import com.alex.tools.dump.SpriteDumper
import com.alex.tools.pack.ModelPacker
import com.alex.tools.pack.SpritePacker
import content.items.*
import content.objects.`Obelisk(42004)`
import java.nio.file.Paths

object ContentLoader {
    @JvmStatic
    fun main(args: Array<String>) {
        runCatching {
            Cache.init()
            load()
            // print()
            // dumpMaps()
        }.onFailure { e -> e.printStackTrace() }
    }

    private fun load() {
        models()
        sprites()
        interfaces()
        objects()
        items()
        npcs()
    }

    private fun interfaces() {
        content.interfaces.`AreaTask(259)`.add()
    }

    private fun items() {
        `FixSeersHeadband(14631)`.add()
        `ArdougneCloaks(14638-14640)`.add()
        `AntiqueLamps(14641-14643)`.add()
        `SummoningObelisk(14644)`.add()
        `SeersHeadbands(14645-14646)`.add()
        `AgileTop(14647)`.add()
        `AgileLegs(14648)`.add()
        `RandomEventGift(14649)`.add()
        `Afro(14650-14699)`.add()
    }

    private fun objects() {
        `Obelisk(42004)`.add()
    }

    private fun models() {
        ModelPacker.add()
    }

    private fun sprites() {
        SpritePacker.add()
    }

    private fun npcs() {

    }

    private fun print() {
        val store = Cache.getStore()
        LocDefinition.print(store, "../Dumps/object_dumps.txt")
        ItemDefinition.print(store, "../Dumps/item_dumps.txt")
        ItemDefinition.printParams(store, "../Dumps/item_params.txt")
        BasDefinition.print(store, "../Dumps/bas_dumps.txt")
        NpcDefinition.print(store, "../Dumps/npc_dumps.txt")
    }

    private fun dump() {
        SpriteDumper.dump()
        ModelDumper.dump()
    }

    private fun dumpMaps() {
        MapDumper.dump(Cache.getStore(), "../Dumps/maps/", Paths.get("../Server/data/configs/xteas.json"))
        MapDumper.verify(Cache.getStore(), "../Dumps/maps/")
    }
}
