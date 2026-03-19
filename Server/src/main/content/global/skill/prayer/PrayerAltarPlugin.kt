package content.global.skill.prayer

import core.api.*
import core.game.event.PrayerPointsRechargeEvent
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.interaction.QueueStrength
import core.game.node.Node
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.TeleportManager
import core.game.node.entity.skill.Skills
import core.game.world.map.Location
import shared.consts.*

class PrayerAltarPlugin : InteractionListener {
    override fun defineListeners() {
        on(ALTAR, IntType.SCENERY, "pray", "pray-at") { player, node ->
            if (node.id == Scenery.TRIBAL_STATUE_3863 && !hasRequirement(player, Quests.TAI_BWO_WANNAI_TRIO)) {
                return@on true
            }

            if (!pray(player, node)) {
                if (node.id != Scenery.ELIDINIS_STATUETTE_10439) return@on true
            }

            if (node.id == Scenery.ELIDINIS_STATUETTE_10439) {
                setTempLevel(player, Skills.HITPOINTS, getStatLevel(player, Skills.HITPOINTS).plus(7))
                sendMessage(player, "You feel much healthier after praying in the shrine.")
            }

            if (node.id == Scenery.CHAOS_ALTAR_412) {
                lock(player, 4)
                queueScript(player, 4, QueueStrength.STRONG) {
                    sendMessage(player, "It's a trap!")
                    teleport(player, Location(2583, 9576, 0), TeleportManager.TeleportType.INSTANT)
                    return@queueScript stopExecuting(player)
                }
            }
            return@on true
        }
    }

    private fun pray(player: Player, node: Node): Boolean {
        val hasPrayerBoost =
            node.id in BOOSTED_ALTAR ||
                    (inEquipment(player, Items.SEERS_HEADBAND_3_14646) &&
                            node.id == Scenery.ALTAR_409)

        val prayerLevel = getStatLevel(player, Skills.PRAYER) + if (hasPrayerBoost) 2 else 0

        if (player.skills.prayerPoints >= prayerLevel.toDouble()) {
            sendMessage(player, "You already have full Prayer points.")
            return false
        }

        lock(player, 3)
        animate(player, Animations.HUMAN_PRAY_645)
        playAudio(player, Sounds.PRAYER_RECHARGE_2674)
        setTempLevel(player, Skills.PRAYER, prayerLevel)
        when (node.id) {
            Scenery.ALTAR_OF_NATURE_3521 -> sendMessage(player, "You recharge your Prayer points at the altar of nature.")
            else -> sendMessage(player, "You recharge your Prayer points.")
        }
        player.dispatch(PrayerPointsRechargeEvent(node))
        return true
    }

    companion object {
        private val ALTAR = intArrayOf(
            Scenery.ALTAR_409, Scenery.ALTAR_2640, Scenery.ALTAR_4008, Scenery.ALTAR_8749,
            Scenery.ALTAR_10639, Scenery.ALTAR_10640, Scenery.ALTAR_13179, Scenery.ALTAR_13180,
            Scenery.ALTAR_13181, Scenery.ALTAR_13182, Scenery.ALTAR_13183, Scenery.ALTAR_13184,
            Scenery.ALTAR_13185, Scenery.ALTAR_13186, Scenery.ALTAR_13187, Scenery.ALTAR_13188,
            Scenery.ALTAR_13189, Scenery.ALTAR_13190, Scenery.ALTAR_13191, Scenery.ALTAR_13192,
            Scenery.ALTAR_13193, Scenery.ALTAR_13194, Scenery.ALTAR_13195, Scenery.ALTAR_13196,
            Scenery.ALTAR_13197, Scenery.ALTAR_13198, Scenery.ALTAR_13199, Scenery.ALTAR_15050,
            Scenery.ALTAR_15051, Scenery.ALTAR_18254, Scenery.ALTAR_19145, Scenery.ALTAR_20377,
            Scenery.ALTAR_20378, Scenery.ALTAR_20379, Scenery.ALTAR_24343, Scenery.ALTAR_27306,
            Scenery.ALTAR_27307, Scenery.ALTAR_27308, Scenery.ALTAR_27309, Scenery.ALTAR_27334,
            Scenery.ALTAR_27338, Scenery.ALTAR_27339, Scenery.ALTAR_27661, Scenery.ALTAR_30726,
            Scenery.ALTAR_34616, Scenery.ALTAR_36972, Scenery.ALTAR_37630, Scenery.ALTAR_37901,
            Scenery.ALTAR_37902, Scenery.ALTAR_37903, Scenery.ALTAR_37904, Scenery.ALTAR_37905,
            Scenery.ALTAR_37906, Scenery.ALTAR_37907, Scenery.ALTAR_37908, Scenery.ALTAR_37909,
            Scenery.ALTAR_37910, Scenery.ALTAR_37911, Scenery.ALTAR_37912, Scenery.ALTAR_39547,
            Scenery.ALTAR_39842, Scenery.CHAOS_ALTAR_61, Scenery.CHAOS_ALTAR_411,
            Scenery.CHAOS_ALTAR_412, Scenery.CHAOS_ALTAR_32079, Scenery.CHAOS_ALTAR_37990,
            Scenery.GORILLA_STATUE_4858, Scenery.GORILLA_STATUE_4859, Scenery.ALTAR_OF_GUTHIX_410,
            Scenery.ALTAR_OF_GUTHIX_28698, Scenery.ALTAR_OF_NATURE_3521, Scenery.TRIBAL_STATUE_3863,
            Scenery.ELIDINIS_STATUETTE_10439, Scenery.DECAYED_ALTAR_37985
        )

        private val BOOSTED_ALTAR = intArrayOf(Scenery.ALTAR_2640, Scenery.ALTAR_OF_NATURE_3521)
    }
}
