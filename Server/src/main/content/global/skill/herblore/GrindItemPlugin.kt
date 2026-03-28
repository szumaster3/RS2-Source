package content.global.skill.herblore

import core.api.*
import core.game.dialogue.SkillDialogueHandler
import core.game.interaction.Clocks
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.interaction.QueueStrength
import core.game.node.item.Item
import core.game.world.update.flag.context.Animation
import shared.consts.Animations
import shared.consts.Items

class GrindItemPlugin : InteractionListener {

    override fun defineListeners() {
        onUseWith(IntType.ITEM, Items.PESTLE_AND_MORTAR_233, *GRIND_ITEM_IDS) { player, _, with ->
            val grind = GrindItem.forID(with.id) ?: return@onUseWith false
            val sourceId = with.id
            val productId = grind.product

            val invAmount = amountInInventory(player, sourceId)

            if (!clockReady(player, Clocks.SKILLING)) return@onUseWith true
            if (invAmount == 1) {
                if (removeItem(player, Item(sourceId, 1))) {
                    player.animate(Animation.create(Animations.HUMAN_USE_PESTLE_AND_MORTAR_364))
                    addItem(player, productId, 1)
                    sendMessage(player, grind.message)
                    delayClock(player, Clocks.SKILLING, 2)
                }
                return@onUseWith true
            }

            val handler = object : SkillDialogueHandler(player, SkillDialogue.ONE_OPTION, Item(productId)) {
                override fun create(amount: Int, index: Int) {
                    var remaining = amount
                    val maxAmount = amountInInventory(player, sourceId)
                    remaining = remaining.coerceAtMost(maxAmount)

                    if (sourceId == FISHING_BAIT) remaining = remaining.coerceAtMost(10)

                    queueScript(player, 0, QueueStrength.WEAK) {
                        if (!clockReady(player, Clocks.SKILLING)) return@queueScript stopExecuting(player)

                        val currentSource = amountInInventory(player, sourceId)
                        if (remaining <= 0 || currentSource <= 0) return@queueScript stopExecuting(player)

                        player.animate(Animation.create(Animations.HUMAN_USE_PESTLE_AND_MORTAR_364))
                        delayClock(player, Clocks.SKILLING, 2)

                        val removeQty = if (sourceId == FISHING_BAIT) 10.coerceAtMost(currentSource) else 1
                        if (removeItem(player, Item(sourceId, removeQty))) {
                            addItem(player, productId, removeQty)
                            sendMessage(player, grind.message)
                        } else return@queueScript stopExecuting(player)

                        remaining -= removeQty

                        if (remaining > 0) {
                            delayClock(player, Clocks.SKILLING, 2)
                            setCurrentScriptState(player, 0)
                            delayScript(player, 2)
                        } else {
                            stopExecuting(player)
                        }
                    }
                }

                override fun getAll(index: Int): Int {
                    val invAmount = amountInInventory(player, sourceId)
                    return if (sourceId == FISHING_BAIT) 10 else invAmount
                }
            }

            val maxAmount = handler.getAll(0)
            if (maxAmount <= 1) handler.create(1, 0) else handler.open()
            return@onUseWith true
        }
    }

    companion object {
        private val GRIND_ITEM_IDS = GrindItem.values().flatMap { it.items }.toSet().toIntArray()
        private const val FISHING_BAIT = Items.FISHING_BAIT_313
    }


    /**
     * Represents an item that can be ground down.
     */
    enum class GrindItem(val items: Set<Int>, val product: Int, val message: String) {
        UNICORN_HORN(setOf(Items.UNICORN_HORN_237), Items.UNICORN_HORN_DUST_235, "You grind the Unicorn horn to dust."),
        KEBBIT_TEETH(setOf(Items.KEBBIT_TEETH_10109), Items.KEBBIT_TEETH_DUST_10111, "You grind the Kebbit teeth to dust."),
        BIRDS_NEST(setOf(Items.BIRDS_NEST_5070, Items.BIRDS_NEST_5071, Items.BIRDS_NEST_5072, Items.BIRDS_NEST_5073, Items.BIRDS_NEST_5074, Items.BIRDS_NEST_5075), Items.CRUSHED_NEST_6693, "You grind the Bird's nest down."),
        GOAT_HORN(setOf(Items.DESERT_GOAT_HORN_9735), Items.GOAT_HORN_DUST_9736, "You grind the goat's horn to dust."),
        MUD_RUNE(setOf(Items.MUD_RUNE_4698), Items.GROUND_MUD_RUNES_9594, "You grind the mud runes into a powder."),
        ASHES(setOf(Items.ASHES_592), Items.GROUND_ASHES_8865, "You grind down the ashes."),
        RAW_KARAMBWAN(setOf(Items.RAW_KARAMBWAN_3142), Items.KARAMBWAN_PASTE_3152, "You grind the raw Karambwan to form a sticky paste."),
        POISON_KARAMBWAN(setOf(Items.POISON_KARAMBWAN_3146), Items.KARAMBWAN_PASTE_3153, "You grind the cooked Karambwan to form a sticky paste."),
        FISHING_BAIT(setOf(Items.FISHING_BAIT_313), Items.GROUND_FISHING_BAIT_12129, "You grind down the Fishing bait."),
        SEAWEED(setOf(Items.SEAWEED_401), Items.GROUND_SEAWEED_6683, "You grind down the seaweed."),
        BAT_BONES(setOf(Items.BAT_BONES_530), Items.GROUND_BAT_BONES_2391, "You grind the bones to a powder."),
        CHARCOAL(setOf(Items.CHARCOAL_973), Items.GROUND_CHARCOAL_704, "You grind the charcoal to a powder."),
        ASTRAL_RUNE_SHARDS(setOf(Items.ASTRAL_RUNE_SHARDS_11156), Items.GROUND_ASTRAL_RUNE_11155, "You grind down the Astral Rune shard's."),
        GARLIC(setOf(Items.GARLIC_1550), Items.GARLIC_POWDER_4668, "You grind the Garlic into a powder."),
        DRAGON_SCALE(setOf(Items.BLUE_DRAGON_SCALE_243), Items.DRAGON_SCALE_DUST_241, "You grind the Dragon scale to dust."),
        ANCHOVIES(setOf(Items.ANCHOVIES_319), Items.ANCHOVY_PASTE_11266, "You grind the anchovies into a fishy, sticky paste."),
        CHOCOLATE_BAR(setOf(Items.CHOCOLATE_BAR_1973), Items.CHOCOLATE_DUST_1975, "You grind the chocolate into dust."),
        SULPHUR(setOf(Items.SULPHUR_3209), Items.GROUND_SULPHUR_3215, "You grind down the sulphur."),
        SUQAH_TOOTH(setOf(Items.SUQAH_TOOTH_9079), Items.GROUND_TOOTH_9082, "You grind the suqah tooth to dust."),
        RUNE_SHARDS(setOf(Items.RUNE_SHARDS_6466), Items.RUNE_DUST_6467, "You grind the runes shards into dust."),
        GUAM_LEAF(setOf(Items.CLEAN_GUAM_249), Items.GROUND_GUAM_6681, "You grind down the guam.");

        companion object {
            private val map = GrindItem.values().flatMap { g -> g.items.map { it to g } }.toMap()
            fun forID(id: Int): GrindItem? = map[id]
        }
    }
}
