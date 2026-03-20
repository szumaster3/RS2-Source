package content.region.misthalin.lumbridge.plugin

import content.data.GameAttributes
import content.region.misthalin.lumbridge.dialogue.tutors.BankTutorDialogue
import content.region.misthalin.lumbridge.plugin.cellar.CulinaromancerChestPlugin
import core.GlobalStatistics
import core.api.*
import core.game.activity.ActivityManager
import core.game.dialogue.FaceAnim
import core.game.global.action.ClimbActionHandler.climb
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.Entity
import core.game.node.entity.combat.equipment.Ammunition
import core.game.node.entity.combat.equipment.WeaponInterface
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.warning.WarningManager
import core.game.node.entity.player.link.warning.WarningType
import core.game.node.entity.skill.Skills
import core.game.node.item.Item
import core.game.node.scenery.Scenery
import core.game.system.task.Pulse
import core.game.world.GameWorld
import core.game.world.map.Location
import core.game.world.update.flag.context.Animation
import shared.consts.Animations
import shared.consts.Components
import shared.consts.Items
import shared.consts.NPCs
import kotlin.math.floor
import shared.consts.Scenery as Objects

class LumbridgePlugin : InteractionListener {

    companion object {
        val COMBAT_TUTORS = intArrayOf(NPCs.MAGIC_TUTOR_4707, NPCs.RANGED_TUTOR_1861)
        var flagInUse: Boolean = false
    }

    override fun defineListeners() {
        /*
         * Handles bank guide dialogue.
         */

        on(NPCs.BANK_TUTOR_7961, IntType.NPC, "talk-to") { player, node ->
            openDialogue(player, BankTutorDialogue())
            return@on true
        }

        /*
         * Handles warning toggles.
         */

        on(NPCs.DOOMSAYER_3777, IntType.NPC, "toggle-warnings") { player, _ ->
            openInterface(player, Components.CWS_DOOMSAYER_583)
            return@on true
        }

        /*
         * Handles claiming interactions with combat tutors.
         */

        on(COMBAT_TUTORS, IntType.NPC, "claim") { player, node ->
            val npc = node as NPC
            openDialogue(player, npc.id, npc, true)
            return@on true
        }

        /*
         * Handles reading the gnome advertisement.
         */

        on(Objects.ADVERTISEMENT_30037, IntType.SCENERY, "read") { player, _ ->
            sendDialogue(player, "Come check our gnome copters up north!")
            return@on true
        }

        /*
         * Handles interaction with penguin sheep.
         * TODO: Mission report (Lumbridge)
         */

        on(NPCs.SHEEP_3579, IntType.NPC, "talk-to") { player, _ ->
            sendPlayerDialogue(player, "That's a sheep...I think. I can't talk to sheep.", FaceAnim.THINKING)
            setAttribute(player, GameAttributes.FRED_SEEN_THE_THING, true)
            return@on true
        }

        /*
         * Handles reading the cow field signpost.
         */

        on(Objects.SIGNPOST_31297, IntType.SCENERY, "read") { player, _ ->
            val cowDeaths = GlobalStatistics.getDailyCowDeaths()
            if (cowDeaths > 0) {
                sendDialogue(player, "Local cowherders have reported that $cowDeaths cows have been slain in this field today by passing adventurers. Farmers throughout the land fear this may be an epidemic.")
            } else {
                sendDialogue(player, "The Lumbridge cow population has been thriving today, without a single cow death to worry about!")
            }
            return@on true
        }

        /*
         * Handles reading the church signpost.
         */

        on(Objects.SIGNPOST_31299, IntType.SCENERY, "read") { player, _ ->
            val deaths = GlobalStatistics.getDailyDeaths()
            if (deaths > 0) {
                sendDialogue(player, "So far today $deaths unlucky adventurers have died on ${GameWorld.settings?.name} and been sent to their respawn location. Be careful out there.")
            } else {
                sendDialogue(player, "So far today not a single adventurer on ${GameWorld.settings?.name} has met their end grisly or otherwise. Either the streets are getting safer or adventurers are getting warier.")
            }
            return@on true
        }

        /*
         * Handles reading the warning signpost.
         */

        on(Objects.WARNING_SIGN_15566, IntType.SCENERY, "read") { player, _ ->
            openInterface(player, Components.MESSAGESCROLL_220).also {
                sendString(player, "<col=8A0808>~-~-~ WARNING ~-~-~", 220, 5)
                sendString(player, "<col=8A0808>Noxious gases vent into this cave.", 220, 7)
                sendString(player, "<col=8A0808>Naked flames may cause an explosion!", 220, 8)
                sendString(player, "<col=8A0808>Beware of vicious head-grabbing beasts!", 220, 10)
                sendString(player, "<col=8A0808>Contact a Slayer master for protective headgear.", 220, 11)
            }
            return@on true
        }

        on(Objects.HOLE_6905, IntType.SCENERY, "squeeze-through") { player, _ ->
            WarningManager.trigger(player, WarningType.LUMBRIDGE_CELLAR) {
                val targetLocation: Location
                val direction: core.game.world.map.Direction

                if (player.location.x >= 3221) {
                    targetLocation = Location.create(3219, 9618, 0)
                    direction = core.game.world.map.Direction.WEST
                } else {
                    targetLocation = Location.create(3222, 9618, 0)
                    direction = core.game.world.map.Direction.EAST
                }

                sendMessage(player, "You squeeze through the hole.")
                forceMove(player, player.location, targetLocation, 0, 90, direction, 10578)
            }
            return@on true
        }

        /*
         * Handles interaction with the RFD chest for buying items or food.
         */

        on(Objects.CHEST_12309, IntType.SCENERY, "buy-items", "buy-food") { player, _ ->
            CulinaromancerChestPlugin.openShop(player, food = getUsedOption(player).lowercase() == "buy-food")
            return@on true
        }

        /*
         * Handles opening the bank account from the RFD chest.
         */

        on(Objects.CHEST_12309, IntType.SCENERY, "bank") { player, _ ->
            openBankAccount(player)
            return@on true
        }

        /*
         * Handles playing the organ in the church.
         */

        on(Objects.ORGAN_36978, IntType.SCENERY, "play") { player, _ ->
            ActivityManager.start(player, "organ cutscene", false)
            return@on true
        }

        /*
         * Handles ringing the church bell.
         */

        on(Objects.BELL_36976, IntType.SCENERY, "ring") { player, _ ->
            sendMessage(player, "The townspeople wouldn't appreciate you ringing their bell.")
            return@on true
        }

        /*
         * Handles raising the castle flag.
         */

        on(Objects.FLAG_37335, IntType.SCENERY, "raise") { player, node ->
            if (!flagInUse) {
                flagInUse = true
                player.lock()
                submitIndividualPulse(
                    player,
                    object : Pulse(1, player) {
                        var counter: Int = 0

                        override fun pulse(): Boolean {
                            when (counter++) {
                                0 -> {
                                    sendMessage(player, "You start cranking the lever.")
                                    sendMessage(player, "The flag reaches the top...", 8)
                                    sendChat(player, "All Hail the Duke!", 9)
                                    sendMessage(player, "...and slowly descends.", 12)
                                    animateScenery(node.asScenery(), 9979)
                                    animate(player, Animations.LUMBRIDG_FLAG_CRAKING_A_9977)
                                }

                                8 -> animate(player, Animations.LUMBRIDG_FLAG_CRAKING_B_9978)
                            }
                            return counter >= 20
                        }

                        override fun stop() {
                            super.stop()
                            flagInUse = false
                            player.unlock()
                        }
                    },
                )
            }

            return@on true
        }

        /*
         * Handles viewing the tutor map.
         */

        on(Objects.LUMBRIDGE_MAP_37655, IntType.SCENERY, "view") { player, _ ->
            openInterface(player, Components.TUTOR_MAP_270)
            return@on true
        }

        /*
         * Handles shooting at the archery target.
         */

        on(Objects.ARCHERY_TARGET_37095, IntType.SCENERY, "shoot-at") { player, node ->
            if (!anyInEquipment(player, Items.TRAINING_ARROWS_9706, Items.TRAINING_BOW_9705)) {
                sendMessage(player, "You need a training bow and arrow to practice here.")
                return@on true
            }
            player.pulseManager.run(ArcheryTargetPulse(player, node.asScenery()))
            return@on true
        }

        /*
         * Handles taking tools from the scenery.
         */

        on(Objects.TOOLS_10375, IntType.SCENERY, "take") { player, node ->
            if (freeSlots(player) < 2) {
                sendMessage(player, "You do not have enough inventory space.")
                return@on true
            }
            addItem(player, Items.RAKE_5341)
            addItem(player, Items.SPADE_952)
            replaceScenery(node.asScenery(), 10373, 300)
            return@on true
        }

        on(Objects.CLIMBING_ROPE_5946, IntType.SCENERY, "climb") {player, _ ->
            climb(
                player,
                null,
                Location.create(3169, 3171, 0)
            )
            return@on true
        }
    }

    override fun defineDestinationOverrides() {
        setDest(IntType.NPC, intArrayOf(NPCs.BANK_TUTOR_7961), "talk-to") { _, npc ->
            return@setDest npc.location.transform(npc.direction, 2)
        }

        setDest(IntType.SCENERY, Objects.BELL_36976) { player, _ ->
            return@setDest player.location
        }

        setDest(IntType.SCENERY, shared.consts.Scenery.HOLE_6905) { p, _ ->
            if (p.location.x < 3221)
                Location.create(3219, 9618, 0)
            else
                Location.create(3222, 9618, 0)
        }
    }

    private class ArcheryTargetPulse(
        private val player: Player,
        private val node: Scenery,
    ) : Pulse(1, player, node) {
        override fun pulse(): Boolean {
            if (delay == 1) {
                delay = player.properties.attackSpeed
            }

            if (player.equipment.remove(Item(Items.TRAINING_ARROWS_9706, 1))) {
                val p = Ammunition.get(Items.TRAINING_ARROWS_9706)?.projectile?.transform(player, node.location)
                p?.endLocation = node.location
                p?.endHeight = 25
                p?.send()
                player.animate(Animation(426))
                val entity: Entity = player
                val level = entity.getSkills().getLevel(Skills.RANGE)
                val bonus = entity.properties.bonuses[14]
                var prayer = 1.0
                if (entity is Player) {
                    prayer += entity.prayer.getSkillBonus(Skills.RANGE)
                }
                var cumulativeStr = floor(level * prayer)
                if (entity.properties.attackStyle!!.style == WeaponInterface.STYLE_RANGE_ACCURATE) {
                    cumulativeStr += 3.0
                } else if (entity.properties.attackStyle!!.style == WeaponInterface.STYLE_LONG_RANGE) {
                    cumulativeStr += 1.0
                }
                cumulativeStr *= 1.0
                val hit =
                    (14.0 + cumulativeStr + (bonus.toDouble() / 8) + ((cumulativeStr * bonus) * 0.016865)).toInt() / 10 +
                        1
                player.getSkills().addExperience(Skills.RANGE, ((hit * 1.33) / 10))
                return !inEquipment(player, Items.TRAINING_ARROWS_9706, 1) ||
                    !inEquipment(player, Items.TRAINING_BOW_9705, 1)
            } else {
                return true
            }
        }
    }
}
