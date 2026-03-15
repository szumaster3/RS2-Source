package content

import com.alex.Cache

object ContentLoader {
    @JvmStatic
    fun main(args: Array<String>)
    {
        runCatching {
            println("Initializing cache...")
            Cache.init()
            println("Populating cache...")
            load()
            println("Cache populated successfully.")
        }.onFailure { e ->
            e.printStackTrace()
        }

    }

    private fun load()
    {
        models()
//      sprites()
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
//      content.interfaces.CustomSpellBookInterface.add()
        content.interfaces.AchievementDiaryInterface.add()
    }

    private fun items()
    {
        ItemLoader.importItems()
    }
}