/*package content.region.fremennik.jatizso.quest.fris

import core.api.rewardXP
import core.api.setVarbit
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.quest.Quest
import core.game.node.entity.skill.Skills
import core.plugin.Initializable
import shared.consts.Items
import shared.consts.Quests
import shared.consts.Vars

@Initializable
class TheFremennikIsles : Quest(Quests.THE_FREMENNIK_ISLES, 133, 132, 1, Vars.VARBIT_QUEST_FREMENNIK_ISLES_PROGRESS_3311, 0, 1, 340) {

    /*
     * Sources:
     * https://www.youtube.com/watch?v=h3vSEnb9V6o,
     * https://www.youtube.com/watch?v=obVyvwGy3hI,
     * https://www.youtube.com/watch?v=MJuqbU2n_xw,
     * https://www.youtube.com/watch?v=yB_OaWlramc
     * Boss:
     * https://www.youtube.com/watch?v=xE915tBCMmc
     */
    override fun drawJournal(player: Player, stage: Int) {
        super.drawJournal(player, stage)
        var line = 11
        if (stage == 0) {
            line++
            line(player, "I have spoken to Mord Gunnars on Rellekka's northern", line)
            line(player, "docks.", line)
            line(player, "!!Mord?? told me to speak with !!King Gjuki Sorvott IV?? of", line)
            line(player, "!!Jatizso??.", line)
            line(player, "I have fetched some tuna for Hrafn.", line)
            line(player, "I have gathered someore for Jatizso's Chancellor.", line)
            line(player, "King Gjuki Sorvott IV has told me about spying on Neitiznot.", line)
            line(player, "There is a jester costume for me in the King's chest.", line)
            line(player, "I have talked to a man in a jester suit about spying", line)
            line(player, " on the Burgher of Neitiznot.", line)
            line(player, "I have performed as a jester for the Burgher, eavesdropping", line)
            line(player, " for useful information.", line)
            line(player, "I have made my report on activities in Neitiznot to the", line)
            line(player, " Spymaster.", line)
            line(player, "I have been instructed to gain the Burgher of", line)
            line(player, "Neitiznot's confidence and find out about the", line)
            line(player, "bridge-building programme.", line)
            line(player, "The Burgher of Neitiznot has asked me to get supplies for", line)
            line(player, " repairing the bridges between the islands.", line)
            line(player, "I have found the supplies the Burgher of Neitiznot", line)
            line(player, " has asked me to find.", line)
            line(player, "Burgher of Neitiznot has asked me", line)
            line(player, "to get 8 split arctic pine logs.", line)
            line(player, "I have found the 8 split arctic pine logs I need.", line)
            line(player, "I have been asked to repair the two bridges for", line)
            line(player, "Thakkrad of Neitiznot.", line)
            line(player, "I have repaired the bridges, despite harrassment", line)
            line(player, "from the local trolls.", line)
            line(player, "Having repaired the bridges, I have reported back to", line)
            line(player, "the Burgher of Neitiznot.", line)
            line(player, "The Burgher of Neitiznot has asked me to find out if", line)
            line(player, "King Gjuki Sorvott IV of Jatizso has learnt of his plans.", line)
            line(player, "King Gjuki Sorvott IV has asked me to collect taxes", line)
            line(player, "from his citizens.", line)
            line(player, "I have collected the Window Tax.", line)
            line(player, "I have collected the Beard Tax! At last, enough money", line)
            line(player, "has been raised.", line)
            line(player, "King Gjuki Sorvott IV has asked me to perform another", line)
            line(player, "spying mission on Neitiznot.", line)
            line(player, "I have talked to the Spymaster and been given my new task.", line)
            line(player, "I have performed for the Burgher of Neitiznot a second time.", line)
            line(player, "I have made my report to the Spymaster and incriminated", line)
            line(player, "myself while I was at it!", line)
            line(player, "I have survived a rather awkward conversation about my", line)
            line(player, "involvement with Neitiznot.", line)
            line(player, "I have delivered King Gjuki Sorvott IV's decree to", line)
            line(player, "Mawnis Burowgar of Neitiznot.", line)
            line(player, "Mawnis Burowgar has asked me to make yak-hide", line)
            line(player, "body and leg armour.", line)
            line(player, "I have completed making a set of yak-leather armour.", line)
            line(player, "Mawnis Burowgar of Neitiznot has asked me to make a", line)
            line(player, "Fremennik round shield.", line)
            line(player, "", line)
            line(player, "I have completed making a Fremennik round shield.", line)
            line(player, "I have been appointed Champion of Neitiznot.", line)
            line(player, "I have entered the troll caves and been told to", line)
            line(player, "kill 10 trolls before attempting to kill the Troll King.", line)
            line(player, "I have defeated many trolls and heroically slain the Troll King.", line)
            line(player, "I have brought back the head of the Troll King as a trophy.", line)
        }

        if (stage == 100) {
            line++
            line(player, "<col=FF0000>QUEST COMPLETE!", line, false)
        }
    }

    override fun finish(player: Player) {
        super.finish(player)
        var ln = 10
        player.packetDispatch.sendItemZoomOnInterface(Items.HELM_OF_NEITIZNOT_10828, 230, 277, 5)
        drawReward(player, "1 Quest Point", ln++)
        drawReward(player, "5,000 Construction XP", ln++)
        drawReward(player, "5,000 Crafting XP", ln++)
        drawReward(player, "10,000 Woodcutting XP", ln++)
        rewardXP(player, Skills.CONSTRUCTION, 5000.0)
        rewardXP(player, Skills.CRAFTING, 5000.0)
        rewardXP(player, Skills.WOODCUTTING, 10000.0)
        setVarbit(player, Vars.VARBIT_QUEST_FREMENNIK_ISLES_PROGRESS_3311, 340, true)
    }

    override fun newInstance(`object`: Any?): Quest {
        return this
    }

}
//1 Quest points Quest point
//Construction 5,000 Construction experience
//Crafting 5,000 Crafting experience
//Woodcutting 10,000 Woodcutting experience
//Two lots of Combat level 10,000 Combat experience (choose between Strength Strength, Attack Attack, Defence Defence or Hitpoints Hitpoints).
//A Fremennik royal helm known as the helm of Neitiznot, which is the equivalent of a berserker helm with a +3 Prayer bonus, slightly better Magic and crush Defence, and no negative bonuses. If a player loses the helm, they can talk to Mawnis Burowgar to buy another one for 50,000 coins.
//Access to arctic pine trees.
//Access to an island with a runite rock north-east of Neitiznot, which also has seven (7) coal rocks nearby.
//Access to the Jatizso mine, which contains up to adamantite rocks.
//Around 20,000 coins in assorted rewards during quest.
//The banks in Neitiznot and Jatizso are close to water, range, furnace, fishing spots, mining ores; a spinning wheel is also nearby.
//If the jester outfit is lost, a player can retrieve another one from the chest behind King Gjuki Sorvott IV's throne.
//Learn the ability to create yak-hide armour and Neitiznot shields.
//The Contraband Yak Produce shop is available on Jatizso if a player refunds the 5,000 coin tax to Vanligga Gastfrihet, who is north of the king.

 */