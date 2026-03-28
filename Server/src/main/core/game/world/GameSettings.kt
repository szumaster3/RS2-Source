package core.game.world

import com.google.gson.JsonObject
import core.ServerConstants
import java.io.FileInputStream
import java.io.IOException
import java.util.*

/**
 * Represents the game settings used for this game instance.
 * @author Vexia
 */
class GameSettings internal constructor(
    var name: String,
    var isBeta: Boolean,
    var isDevMode: Boolean,
    var worldId: Int,
    var countryIndex: Int,
    var activity: String,
    var isMembers: Boolean,
    var isPvp: Boolean,
    var isQuickChat: Boolean,
    var isLootshare: Boolean,
    var msAddress: String,
    var default_xp_rate: Double,
    var enable_default_clan: Boolean,
    var enable_bots: Boolean,
    var autostock_ge: Boolean,
    var allow_token_purchase: Boolean,
    var enabled_botting: Boolean,
    var max_adv_bots: Int,
    var enable_doubling_money_scammers: Boolean,
    var wild_pvp_enabled: Boolean,
    var jad_practice_enabled: Boolean,
    var ge_announcement_limit: Int,
    var smartpathfinder_bfs: Boolean,
    var enable_castle_wars: Boolean,
    var message_model: Int,
    var message_string: String,
) {
    val isHosted: Boolean
        get() = !isDevMode

    override fun toString(): String =
        "GameSettings [name=$name, debug=$isBeta, devMode=$isDevMode, worldId=$worldId]"

    companion object {
        fun parse(data: JsonObject): GameSettings {
            val name = ServerConstants.SERVER_NAME
            val debug = data.get("debug")?.asBoolean ?: false
            val dev = data.get("dev")?.asBoolean ?: false
            val worldId = data.get("worldID")?.asInt ?: 0
            val countryId = data.get("countryID")?.asInt ?: 0
            val activity = data.get("activity")?.asString ?: ""
            val pvpWorld = data.get("pvpWorld")?.asBoolean ?: false
            val msip = data.get("msip")?.asString ?: ""
            val default_xp_rate = data.get("default_xp_rate")?.asDouble ?: 1.0
            val enable_default_clan = data.get("enable_default_clan")?.asBoolean ?: false
            val enable_bots = data.get("enable_bots")?.asBoolean ?: false
            val autostock_ge = data.get("autostock_ge")?.asBoolean ?: false
            val enable_botting = data.get("botting_enabled")?.asBoolean ?: false
            val max_adv_bots = data.get("max_adv_bots")?.asInt ?: 100
            val enable_doubling_money_scammers = data.get("enable_doubling_money_scammers")?.asBoolean ?: false
            val wild_pvp_enabled = data.get("wild_pvp_enabled")?.asBoolean ?: true
            val jad_practice_enabled = data.get("jad_practice_enabled")?.asBoolean ?: true
            val ge_announcement_limit = data.get("ge_announcement_limit")?.asInt ?: 0
            val smartpathfinder_bfs = data.get("smartpathfinder_bfs")?.asBoolean ?: false
            val enable_castle_wars = data.get("enable_castle_wars")?.asBoolean ?: false
            val allow_token_purchase = data.get("allow_token_purchase")?.asBoolean ?: false
            val message_of_the_week_identifier = data.get("message_of_the_week_identifier")?.asInt ?: 0
            val message_of_the_week_text = data.get("message_of_the_week_text")?.asString ?: ""

            return GameSettings(
                name,
                debug,
                dev,
                worldId,
                countryId,
                activity,
                true,
                pvpWorld,
                false,
                false,
                msip,
                default_xp_rate,
                enable_default_clan,
                enable_bots,
                autostock_ge,
                allow_token_purchase,
                enable_botting,
                max_adv_bots,
                enable_doubling_money_scammers,
                wild_pvp_enabled,
                jad_practice_enabled,
                ge_announcement_limit,
                smartpathfinder_bfs,
                enable_castle_wars,
                message_of_the_week_identifier,
                message_of_the_week_text,
            )
        }


        /**
         * Gets the properties.
         * @param path the path.
         * @return the properties.
         */
        private fun getProperties(path: String): Properties {
            val properties = Properties()
            try {
                FileInputStream(path).use { file -> properties.load(file) }
            } catch (e: IOException) {
                println("Warning: Could not find file at path: $path")
                e.printStackTrace()
            }
            return properties
        }
    }
}
