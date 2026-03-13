package content.global.skill.smithing

import content.data.GameAttributes
import content.global.skill.smithing.items.Bars
import core.api.*
import core.game.event.ResourceProducedEvent
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.diary.DiaryType
import core.game.node.entity.skill.SkillPulse
import core.game.node.entity.skill.Skills
import core.game.node.item.Item
import core.game.world.map.Location
import core.game.world.update.flag.context.Animation
import core.tools.StringUtils.isPlusN
import shared.consts.Animations
import shared.consts.Items
import shared.consts.Quests
import java.util.*

class SmithingPulse(
    player: Player?,
    private val item: Item?,
    private val bar: Bars,
    private var amount: Int,
) : SkillPulse<Item?>(player, item) {
    override fun checkRequirements(): Boolean {
        if (!player.inventory.contains(bar.barType.barType, bar.smithingType.required * amount)) {
            amount = amountInInventory(player, bar.barType.barType)
        }
        player.interfaceManager.close()
        if(item?.id != Items.BRONZE_DAGGER_1205 && !getAttribute(player, GameAttributes.TUTORIAL_COMPLETE, false)) {
            sendDialogue(player, "You cannot make this on Tutorial Island.")
            return false
        }
        if (getStatLevel(player, Skills.SMITHING) < bar.level) {
            sendDialogue(player, "You need a Smithing level of " + bar.level + " to make a " + getItemName(bar.product) + ".")
            return false
        }
        if (!anyInInventory(player, bar.barType.barType, bar.smithingType.required)) {
            sendDialogue(player,
                "You don't have enough " +
                        getItemName(bar.barType.barType).lowercase(
                            Locale.getDefault(),
                        ) + "s to make a " +
                        bar.smithingType.name
                            .replace("TYPE_", "")
                            .replace("_", " ")
                            .lowercase(Locale.getDefault()) + ".",
            )
            return false
        }
        if (!inInventory(player, Items.HAMMER_2347, 1)) {
            sendDialogue(player, "You need a hammer to work the metal with.")
            return false
        }
        if (!isQuestComplete(player, Quests.THE_TOURIST_TRAP) && bar.smithingType == SmithingType.TYPE_DART_TIP) {
            sendDialogue(player,"You need to complete Tourist Trap to smith dart tips.")
            return false
        }
        if (!isQuestComplete(player, Quests.DEATH_PLATEAU) && bar.smithingType == SmithingType.TYPE_CLAWS) {
            sendDialogue(player, "You need to complete Death Plateau to smith claws.")
            return false
        }
        if (!isQuestComplete(player, Quests.THE_KNIGHTS_SWORD) && bar.smithingType == SmithingType.TYPE_Crossbow_Bolt) {
            sendDialogue(player, "You need to complete Knights' Sword to smith bolts.")
            return false
        }
        if (!isQuestComplete(player, Quests.THE_KNIGHTS_SWORD) && bar.smithingType == SmithingType.TYPE_Crossbow_Limb) {
            sendDialogue(player, "You need to complete Knights' Sword to smith limb.")
            return false
        }
        return true
    }

    override fun animate() {
        player.animate(ANIMATION)
    }

    override fun reward(): Boolean {
        if (delay == 1) {
            delay = 4
            return false
        }
        player.lock(4)
        player.inventory.remove(Item(bar.barType.barType, bar.smithingType.required))
        val item = Item(node!!.id, bar.smithingType.productAmount)
        player.inventory.add(item)
        player.dispatch(ResourceProducedEvent(item.id, 1, player, bar.barType.barType))
        rewardXP(player, Skills.SMITHING, bar.barType.experience * bar.smithingType.required)
        val message = if (isPlusN(getItemName(bar.product).lowercase(Locale.getDefault()))) "an" else "a"
        sendMessage(player,
            "You hammer the " +
                    bar.barType.barName.lowercase(Locale.getDefault()).replace(
                        "smithing",
                        "",
                    ) + "and make " + message + " " +
                    getItemName(bar.product).lowercase(
                        Locale.getDefault(),
                    ) + ".",
        )

        if (bar == Bars.BLURITE_CROSSBOW_LIMBS && withinDistance(player, Location(3000, 3145, 0), 10)) {
            finishDiaryTask(player, DiaryType.FALADOR, 1, 9)
        }

        if (bar == Bars.STEEL_LONGSWORD && withinDistance(player, Location.create(3112, 9688, 0))) {
            finishDiaryTask(player, DiaryType.LUMBRIDGE, 2, 0)
        }

        if (bar == Bars.ADAMANT_MEDIUM_HELM && withinDistance(player, Location.create(3247, 3404, 0))) {
            finishDiaryTask(player, DiaryType.VARROCK, 2, 3)
        }

        amount--
        return amount < 1
    }

    override fun message(type: Int) {
    }

    companion object {
        private val ANIMATION = Animation(Animations.SMITH_HAMMER_898)
    }
}
