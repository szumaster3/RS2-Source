package content.global.skill.cooking

import core.api.*
import core.game.event.ResourceProducedEvent
import core.game.node.entity.impl.Animator
import core.game.node.entity.player.Player
import core.game.node.entity.skill.Skills
import core.game.node.item.GroundItemManager
import core.game.node.item.Item
import core.game.node.scenery.Scenery
import core.game.system.task.Pulse
import core.game.world.update.flag.context.Animation
import core.tools.RandomFunction
import shared.consts.Animations
import shared.consts.Items
import shared.consts.Quests
import shared.consts.Sounds

/**
 * Handles the cooking pulse.
 */
open class CookingPulse(
    open val player: Player,
    open val scenery: Scenery,
    open val initial: Int,
    open val product: Int,
    open var amount: Int,
) : Pulse() {
    private var experience = 0.0
    private var burned = false

    var properties: CookableItems? = null

    /**
     * Starts the cooking pulse, checks requirements, and processes the cooking.
     */
    override fun start() {
        properties = CookableItems.forId(initial)
        if (checkRequirements()) {
            super.start()
            cook(player, scenery, properties != null && burned, initial, product)
            amount--
        }
    }

    /**
     * Performs the cooking pulse, rewards the player, and decreases the item amount.
     *
     * @return true if the pulse is completed, false if still ongoing.
     */
    override fun pulse(): Boolean {
        if (amount < 1 || !checkRequirements()) {
            return true
        }
        return reward()
    }

    /**
     * Triggers the animation for the cooking process.
     */
    fun animate() {
        player.animate(getAnimation(scenery))
    }

    /**
     * Checks the requirements for cooking, including cooking level and quest completion.
     *
     * @return `true` if the requirements are met, `false` otherwise.
     */
    open fun checkRequirements(): Boolean {
        this.experience = 0.0
        if (properties != null) {
            if (scenery.id == LUMBRIDGE_RANGE && !isQuestComplete(player, Quests.COOKS_ASSISTANT)) {
                sendDialogue(player, "That requires completion of the Cook's Assistant quest in order to use it.")
                return false
            }

            if (getStatLevel(player, Skills.COOKING) < properties!!.level) {
                sendDialogue(player, "You need a Cooking level of " + properties!!.level + " to cook this.")
                return false
            }

            this.experience = properties!!.experience
            this.burned = isBurned(player, scenery, initial)
        }
        if (amount < 1) {
            return false
        }

        return scenery.isActive
    }

    /**
     * Rewards the player with the cooked item, experience, and updates the inventory.
     *
     * @return `true` if the cooking was successful, `false` otherwise.
     */
    open fun reward(): Boolean {
        if (delay == 1) {
            var delay = if (scenery.name.lowercase().contains("range")) 5 else 4
            setDelay(delay)
            return false
        }

        if (cook(player, scenery, burned, initial, product)) {
            amount--
        } else {
            return true
        }
        return amount < 1
    }

    /**
     * Determines if the cooked food has been burned based on the player's cooking level,
     * whether cooking gauntlets are worn, and the type of scenery being used.
     *
     * @return `true` if the food was burned, `false` otherwise.
     */
    open fun isBurned(
        player: Player,
        scenery: Scenery,
        food: Int,
    ): Boolean {
        val hasGauntlets = player.equipment.containsItem(Item(Items.COOKING_GAUNTLETS_775))
        var effectiveCookingLevel = player.getSkills().getLevel(Skills.COOKING)

        val item = CookableItems.forId(food)
        val low: Int
        val high: Int

        if (hasGauntlets && CookableItems.gauntletValues.containsKey(food)) {
            val successValues = CookableItems.gauntletValues[food]
            low = successValues!![0]
            high = successValues[1]
        } else if (scenery.id == LUMBRIDGE_RANGE) {
            val successValues =
                CookableItems.lumbridgeRangeValues.getOrDefault(food, intArrayOf(item!!.lowRange, item.highRange))
            low = successValues[0]
            high = successValues[1]
        } else {
            val isFire = scenery.name.lowercase().contains("fire")
            low = if (isFire) item!!.low else item!!.lowRange
            high = if (isFire) item.high else item.highRange
        }
        val host_ratio = RandomFunction.randomDouble(100.0)
        val client_ratio = RandomFunction.getSkillSuccessChance(low.toDouble(), high.toDouble(), effectiveCookingLevel)
        return host_ratio > client_ratio
    }

    /**
     * Cooks the item by removing it from the player's inventory and adding the cooked product.
     * If burned, the burnt item is added instead.
     *
     * @return `true` if the cooking was successful, `false` otherwise.
     */
    open fun cook(
        player: Player,
        sceneryId: Scenery?,
        burned: Boolean,
        initial: Int,
        product: Int,
    ): Boolean {
        val initialItem = Item(initial)
        val productItem = Item(product)
        animate()

        when (initial) {
            Items.SKEWERED_CHOMPY_7230, Items.SKEWERED_RABBIT_7224, Items.SKEWERED_BIRD_MEAT_9984, Items.SKEWERED_BEAST_9992, Items.IRON_SPIT_7225 ->
                if (RandomFunction.random(15) == 5) {
                    sendMessage(player, "Your iron spit seems to have broken in the process.")
                } else {
                    if (!player.inventory.add(Item(Items.IRON_SPIT_7225))) {
                        GroundItemManager.create(Item(Items.IRON_SPIT_7225), player.location, player)
                    }
                }

            Items.UNCOOKED_CAKE_1889 ->
                if (!player.inventory.add(Item(Items.CAKE_TIN_1887))) {
                    GroundItemManager.create(Item(Items.CAKE_TIN_1887), player)
                }
        }
        if (player.inventory.remove(initialItem)) {
            if (!burned) {
                player.inventory.add(productItem)
                player.dispatch(ResourceProducedEvent(productItem.id, 1, sceneryId!!, initialItem.id))
                player.getSkills().addExperience(Skills.COOKING, experience, true)
            } else {
                player.dispatch(
                    ResourceProducedEvent(
                        itemId = CookableItems.getBurnt(initial).id,
                        amount = 1,
                        source = sceneryId!!,
                        original = initialItem.id,
                    ),
                )
                player.inventory.add(CookableItems.getBurnt(initial))
            }
            getMessage(initialItem, productItem, burned)?.let { sendMessage(player, it) }
            playAudio(player, Sounds.FRY_2577)
            return true
        }
        return false
    }

    /**
     * Returns the message that will be displayed when the cooking is completed.
     *
     * @param food The initial raw food item.
     * @param product The resulting cooked product.
     * @param burned Whether the food was burned.
     * @return The message to be sent to the player.
     */
    open fun getMessage(food: Item, product: Item, burned: Boolean): String {
        // Handle by product id
        val specialMessages = mapOf(
            Items.RAW_OOMLIE_2337 to "The meat is far too delicate to cook like this. Perhaps you should wrap something around it to protect it from the heat.",
            Items.BURNT_OOMLIE_WRAP_2345 to "The meat is far too delicate to cook like this. Perhaps you should wrap something around it to protect it from the heat.",
            Items.BURNT_PIE_2329 to "You accidentally burn the pie.",
            Items.BURNT_CHOMPY_7226 to "You accidentally burn the skewered chompy.",
            Items.RUINED_CHOMPY_2880 to "You accidentally burn the chompy.",
            Items.NETTLE_TEA_4239 to "You boil the water and make nettle tea.",
            Items.COOKED_CHICKEN_2140 to "You cook some chicken.",
            Items.BAKED_POTATO_6701 to "You successfully bake a potato.",
            Items.REDBERRY_PIE_2325 to "You successfully bake a delicious redberry pie.",
            Items.MEAT_PIE_2327 to "You successfully bake a tasty meat pie.",
            Items.APPLE_PIE_2323 to "You successfully bake a traditional apple pie.",
            Items.MUD_PIE_7170 to "You successfully bake a mucky mud pie.",
            Items.SCRAMBLED_EGG_7078 to "You successfully scramble the egg."
        )

        // Handle soda ash special case
        if (product.id == Items.SODA_ASH_1781 && food.id in listOf(Items.SEAWEED_401, Items.SWAMP_WEED_10978)) {
            return "You burn the ${food.name.lowercase()} into soda ash."
        }

        // Handle boiling water
        if (product.id in listOf(Items.BOWL_OF_HOT_WATER_4456, Items.CUP_OF_HOT_WATER_4460)) {
            return if (burned) "You accidentally let the water boil over." else "You boil the water."
        }

        // Handle intentional burn
        if (CookableItems.intentionalBurn(food.id)) {
            return "You deliberately burn the perfectly good piece of meat."
        }

        // Check the special messages map
        specialMessages[product.id]?.let {
            return it
        }

        // Default message
        val foodName = food.name.replace("Raw ", "").lowercase()
        return if (burned) "You accidentally burn some $foodName." else "You manage to cook some $foodName."
    }

    /**
     * Returns the animation to be used during the cooking process.
     *
     * @param scenery The scenery (e.g., fire or range) being used for cooking.
     * @return The animation ID.
     */
    private fun getAnimation(scenery: Scenery): Animation =
        if (!scenery.name.equals("fire", ignoreCase = true)) RANGE_ANIMATION else FIRE_ANIMATION

    companion object {
        private val RANGE_ANIMATION = Animation(Animations.HUMAN_MAKE_PIZZA_883, Animator.Priority.HIGH)
        private val FIRE_ANIMATION = Animation(Animations.OLD_COOK_FIRE_897, Animator.Priority.HIGH)
        private const val LUMBRIDGE_RANGE = 114
    }
}
