package content.region.kandarin.gnome_stronghold.quest.makinghistory

import content.region.kandarin.gnome_stronghold.quest.makinghistory.plugin.EnchantedKeyTreasures
import core.api.*
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.quest.Quest
import core.game.node.entity.skill.Skills
import core.game.world.GameWorld
import core.plugin.Initializable
import shared.consts.Items
import shared.consts.Quests
import shared.consts.Vars

@Initializable
class MakingHistory : Quest(Quests.MAKING_HISTORY, 86, 85, 3, Vars.VARBIT_QUEST_MAKING_HISTORY_PROGRESS_1383, 0, 1, 4) {

    override fun drawJournal(player: Player, stage: Int) {
        super.drawJournal(player, stage)
        var line = 11

        if (stage == 0) {
            line(player, "I can start this quest by talking to !!Jorral?? in the !!Outpost??", line++, false)
            line(player, "!!North West of West Ardougne??.", line++, false)
            line += 1
            line(player, "Minimum requirements:", line++, false)
            line(player, "!!I must have completed the ${Quests.PRIEST_IN_PERIL} Quest??", line++, isQuestComplete(player, Quests.PRIEST_IN_PERIL))
            line(player, "It will be easier with", line++, false)
            line(player, "!!Crafting lvl 24??", line++, getStatLevel(player, Skills.CRAFTING) >= 24)
            line(player, "!!Smithing lvl 40??", line++, getStatLevel(player, Skills.SMITHING) >= 40)
            line(player, "!!Mining lvl 40??", line++, getStatLevel(player, Skills.MINING) >= 40)
            line++
        }

        if (stage >= 1) {
            line(player, "Jorral wants to save the outpost from King Lathas plans.", line++)
            line++
        }

        if (getVarbit(player, MHUtils.PROGRESS) >= 3) {
            line(player, "I have gathered the parts of history. I have been given a letter.", line++)
            line++
        }

        if (stage >= 99) {
            line(player, "The king was pleased to see the history. He gave me a letter for Jorral", line++)
            line++
        }

        if (stage == 100) {
            line(player, "The outpost is saved!", line++, true)
            line++
            line(player, "<col=FF0000>QUEST COMPLETE!</col>", line, false)
            line++
            if (getAttribute(player, EnchantedKeyTreasures.ENCHANTED_KEY_ATTR, -1) >= 0) {
                line(player, "I Should see what else I can find with the help of the key.", line, getAttribute(player, EnchantedKeyTreasures.ENCHANTED_KEY_ATTR, -1) >= 10)
            }
        }
    }

    override fun finish(player: Player) {
        super.finish(player)
        var ln = 10
        displayQuestItem(player, Items.ENCHANTED_KEY_6754)
        drawReward(player, "3 Quest points, 1000 Prayer", ln++)
        drawReward(player, "1000 Crafting XP, 750", ln++)
        drawReward(player, "gold coins. Use the enchanted", ln++)
        drawReward(player, "key all over " + GameWorld.settings!!.name + ". Visit", ln++)
        drawReward(player, "the silver trader for help.", ln)
        rewardXP(player, Skills.CRAFTING, 1000.0)
        rewardXP(player, Skills.PRAYER, 1000.0)
        addItemOrDrop(player, Items.COINS_995, 750)
        setVarbit(player, Vars.VARBIT_MAKING_HISTORY_MUSEUM_BUILT_1390, 1, true)
        removeAttributes(player, MHUtils.ATTRIBUTE_ERIN_PROGRESS, MHUtils.ATTRIBUTE_DROALAK_PROGRESS, MHUtils.ATTRIBUTE_DRON_PROGRESS)
    }

    override fun newInstance(`object`: Any?): Quest = this
}
