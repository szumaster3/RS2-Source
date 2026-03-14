package content

import com.alex.Cache
import com.alex.loaders.print

object ContentLoader {
    @JvmStatic
    fun main(args: Array<String>)
    {
        runCatching {
            println("Initializing cache...")

            Cache.init()
            println("Populating cache...")
            print(Cache.getStore(), "bas_dump.txt")
//          load()
            println("Cache populated successfully.")
        }.onFailure { e ->
            e.printStackTrace()
        }

    }

    private fun load()
    {
        models()
        sprites()
        interfaces()
        items()
    }

    private fun models()
    {
        ModelLoader.importModels()
    }

    private fun sprites()
    {
        SpriteLoader.importSprites()
    }

    private fun interfaces()
    {
        content.interfaces.CustomSpellBookInterface.add()
    }

    private fun items()
    {
        ItemLoader.importItems()
    }
}