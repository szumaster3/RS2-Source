package content.global.skill.magic.spells.modern

import content.global.skill.magic.SpellListener
import content.global.skill.magic.spells.ModernSpells
import core.api.*
import core.game.event.ItemAlchemizationEvent
import core.game.interaction.MovementPulse
import core.game.node.entity.combat.spell.MagicStaff
import core.game.node.entity.impl.Animator
import core.game.node.entity.player.Player
import core.game.node.item.Item
import core.game.world.update.flag.context.Animation
import core.game.world.update.flag.context.Graphics
import core.tools.colorize
import shared.consts.Animations
import shared.consts.Items
import shared.consts.Sounds
import shared.consts.Graphics as Graphic

/**
 * Represents the Alchemy spell.
 */
class AlchemySpell : SpellListener("modern") {

    override fun defineListeners() {
        onCast(ModernSpells.LOW_ALCHEMY, ITEM) { player, node ->
            val item = node?.asItem() ?: return@onCast
            requires(player = player, magicLevel = 21, runes = arrayOf(Item(Items.FIRE_RUNE_554, 3), Item(Items.NATURE_RUNE_561)))
            alchemize(player, item, high = false)
        }
        onCast(ModernSpells.HIGH_ALCHEMY, ITEM) { player, node ->
            val item = node?.asItem() ?: return@onCast
            requires(player = player, magicLevel = 55, runes = arrayOf(Item(Items.FIRE_RUNE_554, 5), Item(Items.NATURE_RUNE_561, 1)))
            alchemize(player, item, high = true)
        }
    }

    fun alchemize(player: Player, item: Item, high: Boolean, explorersRing: Boolean = false): Boolean {
        if (item.name == "Coins") {
            sendMessage(player, "Coins are already made of gold...")
            return false
        }
        if (!item.definition.isTradeable && !item.definition.isAlchemizable) {
            sendMessage(player, "You can't cast this spell on something like that.")
            return false
        }

        if(item.id == Items.BOLT_POUCH_9433)
        {
            sendMessage(player, colorize("%RThe dwarven pouch resists your magic."))
            return false
        }

        if (player.zoneMonitor.isInZone("Alchemists' Playground")) {
            sendMessage(player, "You can only alch items from the cupboards!")
            return false
        }

        val coins = Item(Items.COINS_995, item.definition.getAlchemyValue(high))
        if (item.amount > 1 && coins.amount > 0 && !hasSpaceFor(player, coins)) {
            sendMessage(player, "Not enough space in your inventory.")
            return false
        }

        if (player.pulseManager.current !is MovementPulse) {
            player.pulseManager.clear()
        }

        val explorersRingGraphics = Graphics(Graphic.EXPLORERS_RING_ALCH_1698)

        val weapon = getItemFromEquipment(player, EquipmentSlot.WEAPON)
        val staff = weapon?.let { w -> MagicStaff.values().find { it.staves.contains(w.id) } }

        val (animation, graphics) = when {
            explorersRing -> lowAlchemyAnimation to explorersRingGraphics
            staff != null -> {
                if (high) highAlchemyStaffAnimation to highAlchemyStaffGraphics
                else lowAlchemyStaffAnimation to lowAlchemyStaffGraphics
            }
            else -> {
                if (high) highAlchemyAnimation to highAlchemyGraphics
                else lowAlchemyAnimation to lowAlchemyGraphics
            }
        }

        playAudio(player, if (high) Sounds.HIGH_ALCHEMY_97 else Sounds.LOW_ALCHEMY_98)
        visualize(player, animation, graphics)

        player.dispatch(ItemAlchemizationEvent(item.id, high))
        if (player.inventory.remove(Item(item.id, 1)) && coins.amount > 0) {
            player.inventory.add(coins)
        }

        removeRunes(player)
        addXP(player, if (high) 65.0 else 31.0)
        showMagicTab(player)
        setDelay(player, if (high) 5 else 3)

        return true
    }

    companion object {
        private val lowAlchemyAnimation = Animation(9623, Animator.Priority.HIGH)
        private val lowAlchemyGraphics = Graphics(Graphic.LOW_ALCHEMY_BY_HAND_763)
        private val lowAlchemyStaffAnimation = Animation(Animations.HUMAN_CAST_LOW_ALCH_SPELL_9625, Animator.Priority.HIGH)
        private val lowAlchemyStaffGraphics = Graphics(Graphic.LOW_ALCH_WITH_STAFF_1692)

        private val highAlchemyAnimation = Animation(9631, Animator.Priority.HIGH)
        private val highAlchemyGraphics = Graphics(Graphic.ALCH_1691)
        private val highAlchemyStaffAnimation = Animation(Animations.ALCH_WITH_STAFF_9633, Animator.Priority.HIGH)
        private val highAlchemyStaffGraphics = Graphics(Graphic.HIGH_ALCH_WITH_STAFF_1693)
    }
}
