package content.global.skill.gather.woodcutting

import content.data.GameAttributes
import content.data.skill.SkillingTool
import content.data.tables.BirdNestDropTable
import content.global.skill.farming.FarmingPatch
import content.global.skill.firemaking.items.Log
import core.api.*
import core.cache.def.impl.ItemDefinition
import core.game.container.impl.EquipmentContainer
import core.game.event.ResourceProducedEvent
import core.game.interaction.Clocks
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.Node
import core.game.node.entity.impl.Projectile
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.diary.DiaryType
import core.game.node.entity.skill.Skills
import core.game.node.item.Item
import core.game.node.scenery.Scenery
import core.game.node.scenery.SceneryBuilder
import core.game.world.map.RegionManager
import core.game.world.update.flag.context.Animation
import core.tools.RandomFunction
import shared.consts.*

class WoodcuttingPlugin : InteractionListener {

    private val woodcuttingSounds =
        intArrayOf(
            Sounds.WOODCUTTING_HIT_3037,
            Sounds.WOODCUTTING_HIT_3038,
            Sounds.WOODCUTTING_HIT_3039,
            Sounds.WOODCUTTING_HIT_3040,
            Sounds.WOODCUTTING_HIT_3041,
            Sounds.WOODCUTTING_HIT_3042,
        )

    private val buildingCanoeSound = Sounds.BUILD_CANOE_2729

    override fun defineListeners() {
        defineInteraction(
            IntType.SCENERY,
            ids = WoodcuttingNode.values().map { it.id }.toIntArray(),
            "chop-down",
            "chop",
            "chop down",
            "cut down",
            persistent = true,
            allowedDistance = 1,
            handler = ::handleWoodcutting,
        )
    }

    private fun handleWoodcutting(player: Player, node: Node, state: Int): Boolean {
        val resource = WoodcuttingNode.forId(node.id)
        val tool = SkillingTool.getAxe(player)

        if (!finishedMoving(player))
            return restartScript(player)


        if (state == 0) {
            if (getAttribute(player, GameAttributes.TUTORIAL_STAGE, -1) < 4 && resource == WoodcuttingNode.STANDARD_TREE_10) {
                sendNPCDialogue(player, NPCs.SURVIVAL_EXPERT_943, "I admire your enthusiasm, but let's first have a quick chat.",)
                return clearScripts(player)
            }
            if (!checkWoodcuttingRequirements(player, resource!!, node)) {
                return clearScripts(player)
            }
            if(inEquipmentOrInventory(player, Items.INFERNO_ADZE_13661) &&
                player.location.isInRegion(Regions.MISCELLANIA_10044) ||
                player.location.isInRegion(Regions.ETCETERIA_10300))
            {
                sendDialogue(player, "I don't think I should use the Inferno Adze in here, since there is a chance I might set the logs on fire.")
                return clearScripts(player)
            }
            sendMessage(player, "You swing your axe at the tree.")
        }

        if (clockReady(player, Clocks.SKILLING)) {
            animateWoodcutting(player)
            if (!checkReward(player, resource!!, tool!!)) {
                return delayClock(player, Clocks.SKILLING, 3)
            }

            val reward = resource.reward
            val rewardAmount: Int

            if (tool.id == Items.INFERNO_ADZE_13661 && RandomFunction.roll(4)) {
                sendMessage(player, "You chop some logs. The heat of the inferno adze incinerates them.")
                Projectile
                    .create(player, null, 1776, 35, 30, 20, 25)
                    .transform(player, player.location.transform(2, 0, 0), true, 25, 25)
                    .send()

                player.getSkills().addExperience(Skills.WOODCUTTING, resource.experience)

                val fire = Log.forId(reward)
                if (fire != null) {
                    player.getSkills().addExperience(Skills.FIREMAKING, fire.xp)
                }

                delayClock(player, Clocks.SKILLING, 3)
                return rollDepletion(player, node.asScenery(), resource)
            }

            if (reward > 0) {
                rewardAmount = calculateRewardAmount(player, reward)

                val experience: Double = calculateExperience(player, resource, rewardAmount)
                player.getSkills().addExperience(Skills.WOODCUTTING, experience, true)

                if (!getAttribute(player, GameAttributes.TUTORIAL_COMPLETE, false)) {
                    sendItemDialogue(player, Items.LOGS_1511, "You get some logs.")
                }
                val itemName = ItemDefinition.forId(reward).name.lowercase()
                if (resource == WoodcuttingNode.DRAMEN_TREE) {
                    sendMessage(player, "You cut a branch from the Dramen tree.")
                } else if (resource.isJungleTree()) {
                    sendMessage(player, "You get some wood.")
                    resetAnimator(player)

                    val jungleTree = getScenery(node.location)
                    val targetLocation = jungleTree?.location?.let {
                        if (player.location.y > it.y) it.transform(0, -1, 0) else it.transform(0, 1, 0)
                    }
                    targetLocation?.let { target ->
                        forceMove(player, player.location, target, 0, 60, null, Animations.HUMAN_WALK_SHORT_819) {
                            sendMessage(player, "You hack your way through the tree.")
                            sendMessage(player, "You move deeper into the jungle.")
                        }
                    }
                } else if(!getAttribute(player, GameAttributes.TUTORIAL_COMPLETE, false)) {
                    sendItemDialogue(player, Items.LOGS_1511, "You get some $itemName.")
                } else {
                    sendMessage(player, "You get some $itemName.")
                }

                player.inventory.add(Item(reward, rewardAmount))
                player.dispatch(ResourceProducedEvent(reward, rewardAmount, node, -1))

                val chance = 282
                if (RandomFunction.random(chance) == chance / 2) {

                        BirdNestDropTable.drop(player)
                    }

            }

            delayClock(player, Clocks.SKILLING, 3)
            rollDepletion(player, node.asScenery(), resource)
            if (!checkWoodcuttingRequirements(player, resource, node)) {
                return clearScripts(player)
            }
        }
        return keepRunning(player)
    }

    private fun rollDepletion(player: Player, node: Scenery, resource: WoodcuttingNode): Boolean {
        if (resource.respawnRate > 0) {
            if (RandomFunction.roll(8) || listOf(1, 2, 3, 4, 6, 19).contains(resource.identifier.toInt())) {
                if (resource.isFarming) {
                    val farmingPatch = FarmingPatch.forObject(node.asScenery())
                    if (farmingPatch != null) {
                        val patch = farmingPatch.getPatchFor(player)
                        patch.setCurrentState(patch.getCurrentState() + 1)
                    }
                    return true
                }
                if (resource.emptyId > -1) {
                    SceneryBuilder.replace(node, node.transform(resource.emptyId), resource.respawnDuration)
                } else {
                    SceneryBuilder.replace(node, node.transform(0), resource.respawnDuration)
                }
                node.setActive(false)
                playAudio(player, Sounds.TREE_FALLING_2734)
                return true
            }
        }
        return false
    }

    private fun checkReward(player: Player, resource: WoodcuttingNode, tool: SkillingTool): Boolean {
        val skill = Skills.WOODCUTTING
        val level: Int = player.getSkills().getLevel(skill) + player.familiarManager.getBoost(skill)
        val hostRatio = RandomFunction.randomDouble(100.0)
        val lowMod: Double = if (tool == SkillingTool.BLACK_AXE) resource.tierModLow / 2 else resource.tierModLow
        val low: Double = resource.baseLow + tool.ordinal * lowMod
        val highMod: Double = if (tool == SkillingTool.BLACK_AXE) resource.tierModHigh / 2 else resource.tierModHigh
        val high: Double = resource.baseHigh + tool.ordinal * highMod
        val clientRatio = RandomFunction.getSkillSuccessChance(low, high, level)
        return hostRatio < clientRatio
    }

    fun animateWoodcutting(player: Player) {
        if (!player.animator.isAnimating) {
            player.animate(SkillingTool.getAxe(player)?.let { Animation(it.animation) })
            val playersAroundMe = RegionManager
                .getLocalPlayers(player, 2)
                .filter { it.username != player.username }

            val soundIndex = RandomFunction.random(0, woodcuttingSounds.size)
            for (p in playersAroundMe) {
                playAudio(p, woodcuttingSounds[soundIndex])
            }
        }
    }


    private fun checkWoodcuttingRequirements(player: Player, resource: WoodcuttingNode, node: Node): Boolean {
        var regionId = player.location.regionId
        if (regionId == 10300 || regionId == 10044) {
            var npc = if (regionId == 10300) NPCs.CARPENTER_KJALLAK_3916 else NPCs.LUMBERJACK_LEIF_1395
            sendNPCDialogue(player, npc, "Hey! You're not allowed to chop those!")
            return false
        }
        if (getStatLevel(player, Skills.WOODCUTTING) < resource.level) {
            sendMessage(player, "You need a Woodcutting level of " + resource.level + " to chop this tree.")
            return false
        }
        if (SkillingTool.getAxe(player) == null) {
            sendMessage(player,"You do not have an axe to use.")
            return false
        }
        if (freeSlots(player) < 1 && node.isActive) {
            val resourceName = getItemName(resource.reward).lowercase()
            sendMessage(
                player,
                "Your inventory is too full to hold any more $resourceName.",
            )
            return false
        }
        return node.isActive
    }

    private fun calculateRewardAmount(player: Player, reward: Int): Int {
        var amount = 1

        // Bark drop chance.
        if (reward == Items.BARK_3239 && RandomFunction.random(100) >= 10) {
            amount = 0
        }

        // Bonus for Seers' Village diary and region.
        if (reward == Items.LOGS_1511 &&
            (0..2).any { level -> isDiaryComplete(player, DiaryType.SEERS_VILLAGE, level) } &&
            player.viewport.region?.id == 10806
        ) {
            amount = 2
        }

        // Bonus if wearing Seers' headband.
        if (reward == Items.LOGS_1511 &&
            inEquipment(player, Items.SEERS_HEADBAND_3_14641)
        ) {
            amount += 1
        }

        return amount
    }

    private fun calculateExperience(player: Player, resource: WoodcuttingNode, amount: Int): Double {
        var actualAmount = amount
        var experience: Double = resource.experience
        val reward = resource.reward

        // Minimal XP in restricted region
        if (player.location.regionId == 10300) return 1.0

        // Special case for Bark
        if (reward == Items.BARK_3239) {
            if (actualAmount >= 1) experience = 275.2 else actualAmount = 1
        }

        // Bonus for Maple logs & Seers headband & diary
        if (reward == Items.MAPLE_LOGS_1517 &&
            player.equipment.get(EquipmentContainer.SLOT_HAT)?.id == Items.SEERS_HEADBAND_3_14641 &&
            isDiaryComplete(player, DiaryType.SEERS_VILLAGE, 1)
        ) {
            experience *= 1.10
        }

        // Extra bonus for Seers headband on normal logs/
        if (reward == Items.LOGS_1511 &&
            inEquipment(player, Items.SEERS_HEADBAND_3_14641)
        ) {
            experience *= 1.05
        }

        return experience * actualAmount
    }

    private fun WoodcuttingNode.isJungleTree() = this.identifier.toInt() == 4
}
