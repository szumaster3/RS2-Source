package content.global.plugins.inter.with_item

import content.data.Dyes
import core.api.*
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.item.Item
import shared.consts.Animations
import shared.consts.Items

/**
 * Plugin handling item coloring (dyeing), including mixing dyes,
 * dyeing capes, and dyeing goblin armor.
 */
class ItemColoringPlugin : InteractionListener {

    companion object {
        private val DYES = Dyes.values().map { it.dyeId }.toIntArray()
        private val CAPES = Dyes.values().map { it.capeId }.toIntArray()
        private val GOBLIN_MAIL = Dyes.values().map { it.goblinMailId }.toIntArray()
    }

    override fun defineListeners() {

        /*
         * Handles mix two dyes together.
         */

        onUseWith(IntType.ITEM, DYES, *DYES) { player, used, with ->

            val first = Dyes.forId(used.id) ?: return@onUseWith false
            val second = Dyes.forId(with.id) ?: return@onUseWith false

            if (first == second) return@onUseWith false

            val mix = when (setOf(first, second)) {
                setOf(Dyes.RED, Dyes.YELLOW)  -> Dyes.ORANGE
                setOf(Dyes.YELLOW, Dyes.BLUE) -> Dyes.GREEN
                setOf(Dyes.RED, Dyes.BLUE)    -> Dyes.PURPLE
                else -> {
                    sendMessage(player, "Those dyes don't mix together.")
                    return@onUseWith true
                }
            }

            val article = if (mix.name.first().lowercaseChar() in "aeiou") "an" else "a"

            if (removeItem(player, Item(first.dyeId)) && removeItem(player, Item(second.dyeId))) {
                animate(player, Animations.DYE_COMBINE_4348)
                sendMessage(player, "You mix the two dyes and make $article ${mix.name.lowercase()} dye.")
                addItemOrDrop(player, mix.dyeId, 1)
            }

            return@onUseWith true
        }

        /*
         * Handles coloring the capes with a dye.
         */

        onUseWith(IntType.ITEM, DYES, *CAPES) { player, used, with ->
            val dye = Dyes.forId(used.id) ?: return@onUseWith false
            if (!removeItem(player, Item(dye.dyeId))) return@onUseWith false
            replaceSlot(player, with.asItem().slot, Item(dye.capeId))
            sendMessage(player, "You dye the cape ${dye.name.lowercase()}.")
            return@onUseWith true
        }

        /*
         * Handles dyeing goblin mail (Goblin Diplomacy quest).
         */

        onUseWith(IntType.ITEM, DYES, *(GOBLIN_MAIL + Items.GOBLIN_MAIL_288)) { player, used, with ->
            val dye = Dyes.forId(used.id) ?: return@onUseWith false
            when (with.id) {

                Items.GOBLIN_MAIL_288 -> {
                    if (!removeItem(player, Item(dye.dyeId))) return@onUseWith false
                    replaceSlot(player, with.asItem().slot, Item(dye.goblinMailId))
                    sendMessage(player, "You dye the goblin armour ${dye.name.lowercase()}.")
                }

                else -> sendMessage(player, "That item is already dyed.")
            }

            return@onUseWith true
        }

        /*
         * Handles message when trying to wear goblin armor.
         */
        on(GOBLIN_MAIL + Items.GOBLIN_MAIL_288, IntType.ITEM, "wear") { player, _ ->
            sendMessage(player, "That armour is too small for a human.")
            return@on false
        }
    }
}