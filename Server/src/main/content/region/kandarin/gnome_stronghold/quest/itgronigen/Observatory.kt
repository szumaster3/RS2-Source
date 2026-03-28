package content.region.kandarin.gnome_stronghold.quest.itgronigen

import content.data.GameAttributes
import core.api.*
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.quest.Quest
import core.game.node.entity.skill.Skills
import core.plugin.Initializable
import shared.consts.Items
import shared.consts.Quests
import shared.consts.Vars

@Initializable
class Observatory : Quest(Quests.OBSERVATORY_QUEST, 96, 95, 2, Vars.VARP_QUEST_OBSERVATORY_QUEST_PROGRESS_112, 0, 1, 7)
{
    override fun drawJournal(player: Player, stage: Int) {
        super.drawJournal(player, stage)
        var line = 11

        if(stage == 0)
        {
            line(player, "I can start this quest by talking to !!professor??, in the", line++)
            line(player, "Observatory reception, !!south-west of Ardougne??.", line++)
        }

        if(stage == 1)
        {
            line(player, "Seems the Observatory telescope needs repairing,", line++)
            line(player, "due to the nearby goblins.", line++)
            line++
        }

        if(stage in 2..9)
        {
            line(player, "Seems the Observatory telescope needs repairing,", line++, true)
            line(player, "due to the nearby goblins.", line++, true)
            line++
            line(player, "The !!professor?? wants me to help by getting the", line++, )
            line(player, "following, with the help of his !!assistant??:", line++, )
            line++
            line(player, "!!3 plain wooden planks??", line++, amountInInventory(player, Items.PLANK_960) < 3)
            line(player, "!!1 bronze bar??", line++, inInventory(player, Items.BRONZE_BAR_2349))
            line(player, "!!1 molten glass??", line++, inInventory(player, Items.MOLTEN_GLASS_1775))
            line(player, "!!1 lens mould??", line++, inInventory(player, Items.LENS_MOULD_602))
            line++
        }

        if(stage >= 10)
        {
            line(player, "Seems the Observatory telescope needs repairing,", line++, true)
            line(player, "due to the nearby goblins.", line++, true)
            line++
            line(player, "The !!professor?? was pleased to have all the pieces needed", line++, true)
            line(player, "to fix the telescope.", line++, true)
            line++
            line(player, "Apparently, the professor's last attempt at Crafting ended", line++, stage >= 12)
            line(player, "in disaster. So, he wants me to create the !!lens?? by using", line++, stage >= 12)
            line(player, "the !!molten glass?? with the !!mould??.", line++, stage >= 12)
            line(player, "Fine by me!", line++, stage >= 12)
        }

        if(stage in 11..13)
        {
            line(player, "Seems the Observatory telescope needs repairing,", line++, true)
            line(player, "due to the nearby goblins.", line++, true)
            line++
            line(player, "The !!professor?? was pleased to have all the pieces needed", line++, true)
            line(player, "to fix the telescope.", line++, true)
            line++
            line(player, "Apparently, the professor's last attempt at Crafting ended", line++, true)
            line(player, "in disaster. So, he wants me to create the !!lens?? by using", line++, true)
            line(player, "the !!molten glass?? with the !!mould??.", line++, true)
            line(player, "Fine by me!", line++, true)
            line++
            line(player, "The !!professor?? has gone ahead to the !!Observatory??.", line++)
            line++
            line(player, "He wants me to meet him there by travelling through", line++)
            line(player, "the dungeon below it.", line++)
            line(player, "I hope I get to look through the telescope!", line++)
            line++
        }

        if (stage == 100)
        {
            line++
            line(player, "<col=FF0000>QUEST COMPLETE!</col>", line++, false)
            line++
            line(player, "I should probably see what the !!assistant?? thinks of all this.", line++, false)
            line(player, "He should be pleased.", line++, false)
            line++
            line(player, "I had a word with the professor's assistant and he gave me", line++, false)
            line(player, "some !!wine??! What a pleasant chap. He also mentioned about", line++, false)
            line(player, "!!Scorpius?? and a grave - most cryptic indeed!", line, false)
        }

    }

    override fun finish(player: Player)
    {
        super.finish(player)
        var ln = 10
        displayQuestItem(player, Items.NULL_4557)
        drawReward(player, "2 Quest Points", ln++)
        drawReward(player, "2,250 Crafting XP", ln++)
        drawReward(player, "A payment depending on", ln++)
        drawReward(player, "which constellation you", ln++)
        drawReward(player, "observed", ln)
        rewardXP(player, Skills.CRAFTING, 2250.0)
        removeAttribute(player, GameAttributes.OBSERVATORY_GOBLIN_SPAWN)
        setVarbit(player, Vars.VARBIT_SCENERY_MUSEUM_DISPLAY_17_3652, 1, true)
    }

    override fun newInstance(`object`: Any?): Quest = this
}
