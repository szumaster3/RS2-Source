package content.global.random.event.prison_pete

import core.api.*
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.interaction.QueueStrength
import core.game.world.map.Location
import core.game.world.map.zone.ZoneRestriction
import core.game.world.update.flag.context.Graphics
import shared.consts.*

/**
 * Handles the interactions for Prison pete random event.
 * @author szu
 */
class PrisonPetePlugin : InteractionListener, MapArea {

    init {
        val sceneryData = listOf(
            Triple(4408, Location(2085, 4457, 0), 22),
            Triple(26191, Location(2083, 4460, 0), 10),
            Triple(26184, Location(2084, 4459, 0), 0),
            Triple(26186, Location(2085, 4459, 0), 0),
            Triple(26188, Location(2086, 4459, 0), 0),
            Triple(26184, Location(2087, 4459, 0), 0)
        )
        sceneryData.forEach { (id, loc, rot) -> addScenery(id, loc, 0, rot) }
    }

    override fun defineListeners() {
        on(NPCs.PRISON_PETE_3118, IntType.NPC, "talk-to") { player, _ ->
            findNPC(NPCs.PRISON_PETE_3118)?.location?.let { face(player, it) }
            openDialogue(player, PrisonPeteDialogue(0))
            return@on true
        }

        on(Items.PRISON_KEY_6966, IntType.ITEM, "return") { player, _ ->
            openDialogue(player, PrisonPeteDialogue(1))
            return@on true
        }

        on(Scenery.LEVER_26191, IntType.SCENERY, "pull") { player, _ ->
            animate(player, Animations.PULL_LEVER_798)
            openInterface(player, Components.MACRO_PRISON_PETE_273)
            return@on true
        }

        on(PrisonPeteUtils.ANIMAL_ID, IntType.NPC, "pop") { player, node ->
            if (getUsedOption(player) == "pop") {
                val keyAttribute = getAttribute(player, PrisonPeteUtils.POP_KEY_VALUE, -1)
                val validIdToIndex = mapOf(
                    3119 to 0,
                    3120 to 1,
                    3121 to 2,
                    3122 to 3,
                )
                if (validIdToIndex[node.id] == keyAttribute) {
                    player.incrementAttribute(PrisonPeteUtils.POP_KEY)
                } else {
                    setAttribute(player, PrisonPeteUtils.POP_KEY_FALSE, true)
                }
            }

            queueScript(player, 1, QueueStrength.SOFT) { stage ->
                when (stage) {
                    0 -> {
                        lock(player, 3)
                        visualize(
                            player,
                            Animations.STOMP_BALLOON_794,
                            Graphics(shared.consts.Graphics.WHITE_SPIKE_BALL_POPS_524, 0, 10)
                        )
                        playAudio(player, Sounds.POP3_3252, 5)
                        return@queueScript delayScript(player, 4)
                    }

                    1 -> {
                        animate(player, Animations.HUMAN_BURYING_BONES_827)
                        addItem(player, Items.PRISON_KEY_6966)
                        node.asNpc().clear()
                        findNPC(NPCs.PRISON_PETE_3118)?.location?.let { face(player, it) }
                        openDialogue(player, PrisonPeteDialogue(dialOpt = 1))
                        stopExecuting(player)
                    }

                    else -> return@queueScript stopExecuting(player)
                }
            }
            return@on true
        }
    }

    override fun defineAreaBorders() = arrayOf(PrisonPeteUtils.PRISON_ZONE)

    override fun getRestrictions() = arrayOf(
        ZoneRestriction.RANDOM_EVENTS, ZoneRestriction.CANNON, ZoneRestriction.FOLLOWERS
    )

    override fun defineDestinationOverrides() {
        setDest(IntType.NPC, intArrayOf(NPCs.PRISON_PETE_3118), "talk-to") { _, _ ->
            return@setDest Location.create(2084, 4461, 0)
        }
    }
}
