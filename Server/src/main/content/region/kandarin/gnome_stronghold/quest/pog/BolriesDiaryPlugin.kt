package content.region.kandarin.gnome_stronghold.quest.pog

import core.game.component.Component
import core.game.component.ComponentDefinition
import core.game.component.ComponentPlugin
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.plugin.Plugin
import shared.consts.Components

@Initializable
class BolriesDiaryPlugin : ComponentPlugin() {

    companion object {
        private const val CHAPTER_ONE   = 8
        private const val CHAPTER_TWO   = 13
        private const val CHAPTER_THREE = 19
    }

    override fun newInstance(arg: Any?): Plugin<Any> {
        ComponentDefinition.put(Components.BOLRIES_DIARY_617, this)
        return this
    }

    override fun handle(player: Player, component: Component, opcode: Int, button: Int, slot: Int, itemId: Int): Boolean {
        when(button){
            CHAPTER_ONE   -> {}
            CHAPTER_TWO   -> {}
            CHAPTER_THREE -> {}
        }
        return false
    }
}