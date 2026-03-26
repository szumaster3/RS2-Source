package content.global.skill.magic.spells.teleport

import content.global.skill.magic.SpellListener
import content.global.skill.magic.spells.LunarSpells
import core.api.playGlobalAudio
import core.game.component.Component
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.TeleportManager
import core.game.node.item.Item
import core.game.world.map.Location
import core.game.world.map.RegionManager
import shared.consts.Components
import shared.consts.Items
import shared.consts.Sounds

class LunarGroupTeleport : SpellListener("lunar") {
    override fun defineListeners() {
        /*
         * Handles moonclan group teleport.
         */

        onCast(LunarSpells.MOONCLAN_GROUP_TELEPORT, NONE) { player, _ ->
            requires(player, 70, arrayOf(Item(Items.ASTRAL_RUNE_9075, 2), Item(Items.LAW_RUNE_563, 1), Item(Items.EARTH_RUNE_557, 4)))
            if (!player.isTeleBlocked) playGlobalAudio(player.location, Sounds.TP_ALL_200)
            sendGroupTeleport(player, 67.0, "Moonclan Island", Location.create(2111, 3916, 0))
        }

        /*
         * Handles waterbirth group teleport.
         */

        onCast(LunarSpells.WATERBIRTH_GROUP_TELEPORT, NONE) { player, _ ->
            requires(player, 73, arrayOf(Item(Items.ASTRAL_RUNE_9075, 2), Item(Items.LAW_RUNE_563), Item(Items.WATER_RUNE_555, 5)))
            if (!player.isTeleBlocked) playGlobalAudio(player.location, Sounds.TP_ALL_200)
            sendGroupTeleport(player, 72.0, "Waterbirth Island", Location.create(2527, 3739, 0))
        }

        /*
         * Handles barbarian group teleport.
         */

        onCast(LunarSpells.BARBARIAN_GROUP_TELEPORT, NONE) { player, _ ->
            requires(player, 76, arrayOf(Item(Items.ASTRAL_RUNE_9075, 2), Item(Items.LAW_RUNE_563, 2), Item(Items.FIRE_RUNE_554, 6)))
            if (!player.isTeleBlocked) playGlobalAudio(player.location, Sounds.TP_ALL_200)
            sendGroupTeleport(player, 77.0, "Barbarian Outpost", Location.create(2544, 3572, 0))
        }

        /*
         * Handles khazard group teleport.
         */

        onCast(LunarSpells.KHAZARD_GROUP_TELEPORT, NONE) { player, _ ->
            requires(player, 79, arrayOf(Item(Items.ASTRAL_RUNE_9075, 2), Item(Items.LAW_RUNE_563, 2), Item(Items.WATER_RUNE_555, 8)))
            if (!player.isTeleBlocked) playGlobalAudio(player.location, Sounds.TP_ALL_200)
            sendGroupTeleport(player, 81.0, "Port Khazard", Location.create(2656, 3157, 0))
        }

        /*
         * Handles fishing guild group teleport.
         */

        onCast(LunarSpells.FISHING_GUILD_GROUP_TELEPORT, NONE) { player, _ ->
            requires(player, 86, arrayOf(Item(Items.ASTRAL_RUNE_9075, 3), Item(Items.LAW_RUNE_563, 3), Item(Items.WATER_RUNE_555, 14)))
            if (!player.isTeleBlocked) playGlobalAudio(player.location, Sounds.TP_ALL_200)
            sendGroupTeleport(player, 90.0, "Fishing Guild", Location.create(2611, 3393, 0))
        }

        /*
         * Handles catherby group teleport.
         */

        onCast(LunarSpells.CATHERBY_GROUP_TELEPORT, NONE) { player, _ ->
            requires(player, 88, arrayOf(Item(Items.ASTRAL_RUNE_9075, 3), Item(Items.LAW_RUNE_563, 3), Item(Items.WATER_RUNE_555, 15)))
            if (!player.isTeleBlocked) playGlobalAudio(player.location, Sounds.TP_ALL_200)
            sendGroupTeleport(player, 93.0, "Catherby", Location.create(2804, 3433, 0))
        }

        /*
         * Handles ice plateau group teleport.
         */

        onCast(LunarSpells.ICE_PLATEAU_GROUP_TELEPORT, NONE) { player, _ ->
            requires(player, 90, arrayOf(Item(Items.ASTRAL_RUNE_9075, 3), Item(Items.LAW_RUNE_563, 3), Item(Items.WATER_RUNE_555, 16)))
            if (!player.isTeleBlocked) playGlobalAudio(player.location, Sounds.TP_ALL_200)
            sendGroupTeleport(player, 99.0, "Ice Plateau", Location.create(2972, 3873, 0))
        }
    }

    private fun sendGroupTeleport(player: Player, xp: Double, destName: String, loc: Location) {
        RegionManager.getNearbyPlayers(player, 1).forEach {
            if (it == player) return@forEach
            if (it.isTeleBlocked) return@forEach
            if (!it.isActive) return@forEach
            if (!it.settings.isAcceptAid) return@forEach
            it.setAttribute("t-o_location", loc)
            it.interfaceManager.open(Component(Components.TP_OTHER_326))
            it.packetDispatch.sendString(player.username, Components.TP_OTHER_326, 1)
            it.packetDispatch.sendString(destName, Components.TP_OTHER_326, 3)
        }
        if (player.teleporter.send(loc, TeleportManager.TeleportType.LUNAR)) {
            addXP(player, xp)
            removeRunes(player)
            setDelay(player, true)
        }
    }
}
