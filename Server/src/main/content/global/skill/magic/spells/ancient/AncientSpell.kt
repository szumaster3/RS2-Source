package content.global.skill.magic.spells.ancient

import core.api.*
import core.game.container.impl.EquipmentContainer
import core.game.node.Node
import core.game.node.entity.Entity
import core.game.node.entity.combat.BattleState
import core.game.node.entity.combat.spell.CombatSpell
import core.game.node.entity.combat.spell.SpellType
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.SpellBookManager.SpellBook
import core.game.node.entity.skill.Skills
import core.plugin.Initializable
import core.plugin.Plugin
import shared.consts.Animations
import shared.consts.Items

@Initializable
class AncientSpell private constructor(
    private val definition: AncientSpellDefinition
) : CombatSpell(
    definition.type,
    SpellBook.ANCIENT,
    definition.level,
    definition.xp,
    definition.castSound,
    definition.impactSound,
    definition.anim,
    definition.start,
    definition.projectile,
    definition.end,
    *definition.runes
) {

    constructor() : this(AncientSpellDefinition.SMOKE_RUSH)

    override fun newInstance(type: SpellType?): Plugin<SpellType?> {
        AncientSpellDefinition.values().forEach {
            SpellBook.ANCIENT.register(it.button, AncientSpell(it))
        }
        return this
    }

    override fun visualize(entity: Entity, target: Node) {
        entity.animate(animation)
        entity.graphics(graphics)
        projectile?.transform(entity, target as? Entity ?: return, false, 58, 10)?.send()
        audio?.let { playGlobalAudio(entity.location, it.id, 20) }
    }

    override fun fireEffect(entity: Entity, victim: Entity, state: BattleState) {
        val hit = state.estimatedHit.takeIf { it > -1 } ?: return
        when {
            definition.name.startsWith("BLOOD") -> {
                val heal = hit / 4
                if (heal > 0) {
                    entity.skills.heal(heal)
                    (entity as? Player)?.sendMessage("You drain some of your opponent's health.")
                }
            }

            definition.name.startsWith("ICE") -> applyIceEffect(victim, state)
            definition.name.startsWith("MIASMIC") -> applyMiasmicEffect(victim)
            definition.name.startsWith("SHADOW") -> applyShadowEffect(victim)
            definition.name.startsWith("SMOKE") -> applyPoison(victim, entity,
                if (definition.type >= SpellType.BLITZ) 4 else 2
            )
        }
    }

    private fun applyIceEffect(victim: Entity, state: BattleState) {
        val type = definition.type
        val ticks = (type.ordinal - 4) * 8
        if (hasTimerActive(victim, "frozen") || hasTimerActive(victim, "frozen:immunity")) {
            if (type == SpellType.BARRAGE) state.isFrozen = true
            return
        }
        registerTimer(victim, spawnTimer("frozen", ticks, true))
    }

    private fun applyMiasmicEffect(victim: Entity) {
        if (!hasTimerActive(victim, "miasmic:immunity")) {
            val duration = (definition.button - 15) * 20
            registerTimer(victim, spawnTimer("miasmic", duration))
        }
    }

    private fun applyShadowEffect(victim: Entity) {
        val level = victim.skills.getStaticLevel(Skills.ATTACK)
        val reduction = (level * 0.1).toInt()
        victim.skills.updateLevel(Skills.ATTACK, -reduction, level - reduction)
    }

    private fun shouldUseMultiHit(entity: Entity, target: Entity, singleTarget: Boolean) =
        entity.properties.isMultiZone && target.properties.isMultiZone && !singleTarget

    override fun getTargets(entity: Entity, target: Entity): Array<BattleState> {
        val singleTarget = when (definition.type) {
            SpellType.RUSH, SpellType.BLITZ -> true
            else -> animation?.id == Animations.CAST_SPELL_1978
        }

        if (!shouldUseMultiHit(entity, target, singleTarget)) return super.getTargets(entity, target)
        return getMultihitTargets(entity, target, 9)
            .map { BattleState(entity, it) }
            .toTypedArray()
    }

    override fun getMaximumImpact(entity: Entity, victim: Entity, state: BattleState): Int {
        val add = when {
            definition.name.startsWith("BLOOD") -> 3
            definition.name.startsWith("SHADOW") -> 2
            definition.name.startsWith("ICE") -> 4
            definition.name.startsWith("SMOKE") -> 1
            definition.name.startsWith("MIASMIC") -> when (definition.type) {
                SpellType.RUSH -> 4
                SpellType.BURST, SpellType.BLITZ -> 6
                else -> 9
            }
            else -> 0
        }
        return definition.type.getImpactAmount(entity, victim, add)
    }

    override fun cast(entity: Entity, target: Node): Boolean {
        if (definition.name.startsWith("MIASMIC") && !hasRequiredWeapon(entity as? Player)) return false
        if (!meetsRequirements(entity, true, false)) return false
        return super.cast(entity, target)
    }

    private fun hasRequiredWeapon(player: Player?): Boolean {
        val weaponId = player?.equipment?.getNew(EquipmentContainer.SLOT_WEAPON)?.id ?: return false
        val validWeapons = setOf(
            Items.ZURIELS_STAFF_13867,
            Items.ZURIELS_STAFF_DEG_13869
        )
        return if (weaponId in validWeapons || core.game.world.GameWorld.settings?.isDevMode == true) {
            true
        } else {
            sendMessage(player, "You need to be wielding Zuriel's staff in order to cast this spell.")
            false
        }
    }
}