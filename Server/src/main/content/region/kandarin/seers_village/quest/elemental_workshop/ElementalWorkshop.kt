package content.region.kandarin.seers_village.quest.elemental_workshop

import content.region.kandarin.seers_village.quest.elemental_workshop.plugin.EWUtils
import core.api.*
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.quest.Quest
import core.game.node.entity.skill.Skills
import core.game.system.command.Privilege
import core.game.world.map.Location
import core.plugin.Initializable
import shared.consts.Items
import shared.consts.Quests
import shared.consts.Vars

@Initializable
class ElementalWorkshop :
    Quest(Quests.ELEMENTAL_WORKSHOP_I, 52, 51, 1),
    Commands {
    override fun newInstance(`object`: Any?): Quest = this

    override fun drawJournal(
        player: Player,
        stage: Int,
    ) {
        super.drawJournal(player, stage)
        player ?: return
        var line = 11
        if (stage == 0) {
            line(player, "I can start this quest by reading a", line++)
            line(player, "!!book?? found in !!Seers village??.", line++)
            line++
            line(player, "Minimum requirements:", line++)
            line(
                player,
                if (getStatLevel(player, Skills.MINING) >=
                    20
                ) {
                    "---Level 20 Mining/--"
                } else {
                    "!!Level 20 Mining??"
                },
                line++,
            )
            line(
                player,
                if (getStatLevel(player, Skills.SMITHING) >=
                    20
                ) {
                    "---Level 20 Smithing/--"
                } else {
                    "!!Level 20 Smithing??"
                },
                line++,
            )
            line(
                player,
                if (getStatLevel(player, Skills.CRAFTING) >=
                    20
                ) {
                    "---Level 20 Crafting/--"
                } else {
                    "!!Level 20 Crafting??"
                },
                line++,
            )
        } else {
            if (stage < 100) {
                if (stage >= 1) {
                    line(player, "---I have found a battered book in a house in Seers Village./--", line++)
                    line(player, "---It tells of magic ore and a workshop created to fashion it./--", line++)
                    line++
                    if (stage <= 2) {
                        line(player, "Where is the workshop and how do I get in?", line++)
                    }
                }

                if (stage >= 3) {
                    line(player, "---Cutting open the spine of the book with a knife,/--", line++)
                    line(player, "---I found a key hidden under the leather binding./--", line++)
                    line++
                    if (stage <= 4) {
                        line(player, "Where is the workshop and how do I get in?", line++)
                    }
                }

                if (stage >= 5) {
                    line(player, "---I have found a secret door in the Seers Village smithy/--", line++)
                    line++
                    line(player, "---Where is the workshop and how do I get in?/--", line++)
                    line++
                }

                if (stage == 7) {
                    line(player, "There is obviously lots to do here.", line++)
                }
            } else {
                line(player, "---I have found a battered book in a house in Seers Village./--", line++)
                line(player, "---It tells of magic ore and a workshop created to fashion it./--", line++)
                line++
                line(player, "---After fixing up the old workshop machinery, collecting ore", line++)
                line(player, "---and smelting it I was able to create an Elemental Shield./--", line++)
                line++
                line(player, "<col=FF0000>QUEST COMPLETE!", line, false)
            }
        }
    }

    override fun finish(player: Player) {
        super.finish(player)
        player ?: return
        var ln = 10

        sendItemZoomOnInterface(player, 277, 5, Items.ELEMENTAL_SHIELD_2890, 235)
        drawReward(player, "1 Quest Point,", ln++)
        drawReward(player, "5,000 Crafting XP", ln++)
        drawReward(player, "5,000 Smithing XP", ln++)
        drawReward(player, "The ability to make", ln++)
        drawReward(player, "elemental shields.", ln)
        rewardXP(player, Skills.CRAFTING, 5000.0)
        rewardXP(player, Skills.SMITHING, 5000.0)
        removeAttributes(player, "got_needle", "got_leather")
    }

    override fun getConfig(
        player: Player?,
        stage: Int,
    ): IntArray {
        if (stage >= 100) return intArrayOf(Vars.VARP_QUEST_ELEMENTAL_WORKSHOP_PROGRESS_299, 1048576)
        if (stage > 0) {
            return intArrayOf(Vars.VARP_QUEST_ELEMENTAL_WORKSHOP_PROGRESS_299, 3)
        } else {
            return intArrayOf(Vars.VARP_QUEST_ELEMENTAL_WORKSHOP_PROGRESS_299, 0)
        }
    }

    override fun defineCommands() {
        define("resetew", Privilege.ADMIN) { player, _ ->
            setAttribute(player, "/save:ew1:got_needle", false)
            setAttribute(player, "/save:ew1:got_leather", false)
            setAttribute(player, "/save:ew1:bellows_fixed", false)
            player.questRepository.setStageNonmonotonic(player.questRepository.forIndex(52), 0)
            setVarp(player, Vars.VARP_QUEST_ELEMENTAL_WORKSHOP_PROGRESS_299, 0)
            player.teleport(Location.create(2715, 3481, 0))
            player.inventory.clear()
            addItem(player, Items.KNIFE_946)
            addItem(player, Items.BRONZE_PICKAXE_1265)
            addItem(player, Items.NEEDLE_1733)
            addItem(player, Items.THREAD_1734)
            addItem(player, Items.LEATHER_1741)
            addItem(player, Items.HAMMER_2347)
            addItem(player, Items.COAL_453, 4)
        }
        define("readyew", Privilege.ADMIN) { player, _ ->
            val enabled = 1
            setAttribute(player, "/save:ew1:got_needle", true)
            setAttribute(player, "/save:ew1:got_leather", true)
            setAttribute(player, "/save:ew1:bellows_fixed", true)
            player.questRepository.setStageNonmonotonic(player.questRepository.forIndex(52), 95)
            setVarbit(player, EWUtils.BELLOWS_STATE, enabled, true)
            setVarbit(player, EWUtils.FURNACE_STATE, enabled, true)
            setVarbit(player, EWUtils.WATER_WHEEL_STATE, enabled, true)
            setVarbit(player, EWUtils.RIGHT_WATER_CONTROL_STATE, enabled, true)
            setVarbit(player, EWUtils.LEFT_WATER_CONTROL_STATE, enabled, true)
        }
    }
}
