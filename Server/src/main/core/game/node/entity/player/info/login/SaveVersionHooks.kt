package core.game.node.entity.player.info.login

import core.ServerConstants
import core.api.*
import core.game.node.entity.player.Player
import core.game.node.item.Item
import shared.consts.Items
import shared.consts.Music
import shared.consts.Quests

class SaveVersionHooks : LoginListener {
    override fun login(player: Player) {
        if (player.version < ServerConstants.CURRENT_SAVEFILE_VERSION) {
            sendMessage(player, "<col=CC6600>Migrating save file version [${player.version}] to current save file version [${ServerConstants.CURRENT_SAVEFILE_VERSION}].</col>")

            if (player.version < 1) {
                var hasHoods = 0
                var hasCapes = 0
                val searchSpace = arrayOf(player.inventory, player.bank)
                for (container in searchSpace) {
                    for (hood in container.getAll(Item(Items.CRAFTING_HOOD_9782))) {
                        hasHoods += hood.amount
                    }
                    for (id in arrayOf(Items.CRAFTING_CAPE_9780, Items.CRAFTING_CAPET_9781)) {
                        for (cape in container.getAll(Item(id))) {
                            hasCapes += cape.amount
                        }
                    }
                }
                val need = hasCapes - hasHoods
                if (need > 0) {
                    sendMessage(player, "<col=CC6600>You are being given $need crafting hood(s), because we think you bought $need crafting cape(s) when the hoods were still unobtainable.</col>")
                    addItemOrBank(player, Items.CRAFTING_HOOD_9782, need)
                }

                if (getQuestStage(player, Quests.WHAT_LIES_BELOW) > 70) {
                    player.musicPlayer.unlock(Music.SUROKS_THEME_250, false)
                }
                if(getVarbit(player, 5766) == 0 && isQuestComplete(player, Quests.IN_PYRE_NEED))
                    setVarbit(player, 766, 1, true)
            }

            player.version = ServerConstants.CURRENT_SAVEFILE_VERSION
        }
    }
}
