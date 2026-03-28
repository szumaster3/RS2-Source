package content.global.plugins.iface

import content.region.other.tutorial_island.plugin.CharacterDesign
import core.api.*
import core.game.component.Component
import core.game.dialogue.DialogueFile
import core.game.dialogue.FaceAnim
import core.game.interaction.InterfaceListener
import core.game.interaction.QueueStrength
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.appearance.Gender
import core.game.node.item.Item
import core.tools.Log
import shared.consts.Components
import shared.consts.Items
import shared.consts.NPCs

/**
 * Handles the player kit interfaces
 * @author Emperor, Vexia, Ceikry
 */
class AppearanceInterface : InterfaceListener {
    override fun defineInterfaceListeners() {

        /*
         * Start appearance changer.
         */

        onOpen(APPEARANCE_INTERFACE_ID) { player, _ ->
            if (player.interfaceManager.isResizable) openOverlay(player, Components.BLACK_OVERLAY_333)
            return@onOpen true
        }
        on(APPEARANCE_INTERFACE_ID) { player, _, _, buttonID, _, _ ->
            CharacterDesign.handleButtons(player, buttonID)
            return@on true
        }

        /*
         * Hairdresser.
         */

        listOf(HAIRDRESSER_MALE_INTERFACE_ID, HAIRDRESSER_FEMALE_INTERFACE_ID).forEach { iface ->
            onOpen(iface) { player, c ->
                openHairdresserShop(player, c.id)
                return@onOpen true
            }
            on(iface) { player, _, _, button, _, _ ->
                handleHairdresserButtons(player, iface, button)
                return@on true
            }
            onClose(iface) { p, _ ->
                closeHairdresserShop(p)
                return@onClose true
            }
        }

        /*
         * Makeover (gender + skin).
         */

        onOpen(MAKEOVER_MAGE_INTERFACE_ID) { player, component ->
            openMakeoverShop(player, component)
            return@onOpen true
        }
        on(MAKEOVER_MAGE_INTERFACE_ID) { player, _, _, button, _, _ ->
            when (button) {
                in SKIN_COLOR_BUTTON_COMPONENT_IDS -> updateSkin(player, button)
                113,
                101 -> updateGender(player, true)
                114,
                103 -> updateGender(player, false)
                MAKEOVER_CONFIRM_COMPONENT_ID -> makeoverPay(player)
            }
            return@on true
        }

        /*
         * Clothes.
         */

        listOf(THESSALIA_MALE_INTERFACE_ID to true, THESSALIA_FEMALE_INTERFACE_ID to false).forEach {
                (iface, male) ->
            onOpen(iface) { player, _ ->
                openClothesShop(player, male)
                return@onOpen true
            }
            on(iface) { player, c, _, button, _, _ ->
                handleClothesButtons(player, c.id, button, male)
                return@on true
            }
            onClose(iface) { player, _ -> closeClothesShop(player) }
        }

        /*
         * Shoe store.
         */

        onOpen(YRSA_SHOE_STORE_INTERFACE_ID) { player, c ->
            c.setUncloseEvent { p, _ ->
                closeShoeShop(p)
                true
            }
            openShoeShop(player)
            return@onOpen true
        }
        on(YRSA_SHOE_STORE_INTERFACE_ID) { player, _, _, button, _, _ ->
            when (button) {
                14 -> shoePay(player)
                in YRSA_SELECT_BUTTONS_COMPONENT_IDS -> updateFeet(player, button)
            }
            return@on true
        }

        /*
         * Rainald arm guards.
         */

        onOpen(REINALD_BRACELETS_INTERFACE_ID) { player, component ->
            setAttribute(player, PLAYER_KIT_WRIST_SAVE_ATTRIBUTE, player.appearance.wrists.look)
            player.toggleWardrobe(true)
            component.setUncloseEvent { p, _ ->
                closeBraceShop(p)
                true
            }
            return@onOpen true
        }
        on(REINALD_BRACELETS_INTERFACE_ID) { player, _, _, buttonId, _, _ ->
            WRISTS_MODELS[buttonId]?.let { modelId -> updateArmguards(player, modelId) }
                ?: run { if (buttonId == 117) pay(player, REINALD_BRACELETS_INTERFACE_ID) }
            return@on true
        }
    }

    /**
     * Opens the hairdresser interface.
     */
    private fun openHairdresserShop(player: Player, iface: Int) {
        player.toggleWardrobe(true)
        val female = iface == HAIRDRESSER_FEMALE_INTERFACE_ID
        val childModel = if (female) 17 else 62
        val childHead = if (female) 146 else 61

        setAttribute(player, PLAYER_KIT_HAIR_SAVE_ATTRIBUTE, player.appearance.hair.look)
        setAttribute(player, PLAYER_KIT_BEARD_SAVE_ATTRIBUTE, player.appearance.beard.look)
        setAttribute(player, PLAYER_KIT_HAIR_COLOR_SAVE_ATTRIBUTE, player.appearance.hair.color)
        setAttribute(player, PLAYER_KIT_BEARD_SETTINGS_ATTRIBUTE, false)

        sendPlayerOnInterface(player, iface, childModel)
        sendPlayerOnInterface(player, iface, childHead)
        sendAnimationOnInterface(player, FaceAnim.HAPPY.animationId, iface, childHead)
        Component(iface).setUncloseEvent { p, _ ->
            closeHairdresserShop(p)
            true
        }
    }

    /**
     * Handles button interactions for hairdresser.
     */
    private fun handleHairdresserButtons(player: Player, comp: Int, button: Int) {
        when (button) {
            199 -> setAttribute(player, PLAYER_KIT_BEARD_SETTINGS_ATTRIBUTE, false)
            200 -> setAttribute(player, PLAYER_KIT_BEARD_SETTINGS_ATTRIBUTE, true)
            196, 274, 100, 68 -> pay(player, comp)
            else -> if (comp == HAIRDRESSER_MALE_INTERFACE_ID) updateHairMale(player, button)
                else updateHairFemale(player, button)
        }
    }

    /**
     * Closes the hairdresser shop.
     */
    private fun closeHairdresserShop(player: Player) {
        player.toggleWardrobe(false)
        playJingle(player, 266)
        val paid = getAttribute(player, PLAYER_KIT_PAID_ATTRIBUTE, false)
        if (!paid) {
            updateHairLook(player, getAttribute(player, PLAYER_KIT_HAIR_SAVE_ATTRIBUTE, 0))
            updateHairColor(player, getAttribute(player, PLAYER_KIT_HAIR_COLOR_SAVE_ATTRIBUTE, 0))
            val beard = getAttribute(player, PLAYER_KIT_BEARD_SAVE_ATTRIBUTE, -1)
            if (beard != -1) updateBeardLook(player, beard)
            refreshAppearance(player)
        }
        removeAttribute(player, PLAYER_KIT_PAID_ATTRIBUTE)
    }

    /**
     * Closes the bracelet shop.
     */
    private fun closeBraceShop(player: Player) {
        val original = getAttribute(player, PLAYER_KIT_WRIST_SAVE_ATTRIBUTE, defaultBraceletAppearance(player))
        if (!getAttribute(player, PLAYER_KIT_PAID_ATTRIBUTE, false)) {
            updateWristsLook(player, original)
            refreshAppearance(player)
        }
        removeAttribute(player, PLAYER_KIT_PAID_ATTRIBUTE)
        player.toggleWardrobe(false)
        playJingle(player, 266)
    }

    /**
     * Converts a bracelet model id into the appearance index for the players gender.
     *
     * @param id The bracelet model.
     * @param p The player.
     * @return The appearance index.
     */
    private fun calculateBraceletIndex(id: Int, p: Player): Int {
        var base =
            when (id) {
                27704 -> 117
                27708 -> 118
                27697 -> 119
                27700 -> 120
                27699 -> 123
                27709 -> 124
                27707 -> 121
                27705 -> 122
                27706 -> 125
                27702 -> 126
                27703 -> if (p.isMale) 33 else 67
                27698 -> if (p.isMale) 84 else 127
                0 -> if (p.isMale) 34 else 68
                else -> 0
            }
        if (!p.isMale && id !in listOf(27703, 27698, 0)) base += 42
        return base
    }

    /**
     * Return the default wrists based on players gender.
     */
    private fun defaultBraceletAppearance(player: Player) = if (player.isMale) 34 else 68

    /**
     * Opens the makeover mage interface.
     */
    private fun openMakeoverShop(player: Player, component: Component) {
        sendNpcOnInterface(player, 1, component.id, MAKEOVER_MODEL_MALE_COMPONENT_ID)
        sendNpcOnInterface(player, 5, component.id, MAKEOVER_MODEL_FEMALE_COMPONENT_ID)
        sendAnimationOnInterface(
            player,
            FaceAnim.NEUTRAL.animationId,
            component.id, MAKEOVER_MODEL_MALE_COMPONENT_ID
        )
        sendAnimationOnInterface(
            player,
            FaceAnim.NEUTRAL.animationId,
            component.id, MAKEOVER_MODEL_FEMALE_COMPONENT_ID
        )
        if (inInventory(player, Items.MAKEOVER_VOUCHER_5606)) {
            sendString(player, "USE MAKEOVER VOUCHER", component.id, MAKEOVER_CONFIRM_COMPONENT_ID)
        }
        setAttribute(player, PLAYER_KIT_SKIN_COLOR_SAVE_ATTRIBUTE, player.appearance.skin.color)
        setVarp(player, 262, player.appearance.skin.color)
        player.toggleWardrobe(true)
        component.setUncloseEvent { p, _ ->
            p.toggleWardrobe(false)

            val paid = getAttribute(p, PLAYER_KIT_PAID_ATTRIBUTE, false)

            if (paid) {
                val newColor = getAttribute(player, PLAYER_KIT_SKIN_COLOR_SAVE_ATTRIBUTE, -1)
                val newGender = getAttribute(player, PLAYER_KIT_GENDER_SAVE_ATTIBUTE, -1)
                if (newGender > -1) mapAppearance(p, Gender.values()[newGender])
                if (newColor > -1) updateSkinColor(player, newColor)
                refreshAppearance(p)
                removeAttribute(p, PLAYER_KIT_PAID_ATTRIBUTE)
            }
            removeAttribute(p, PLAYER_KIT_SKIN_COLOR_SAVE_ATTRIBUTE)
            removeAttribute(p, PLAYER_KIT_GENDER_SAVE_ATTIBUTE)
            true
        }
    }

    /**
     * Opens clothes shop interface.
     */
    private fun openClothesShop(player: Player, male: Boolean) {
        setAttribute(player, PLAYER_KIT_TORSO_SAVE_ATTRIBUTE, player.appearance.torso.look)
        setAttribute(player, PLAYER_KIT_TORSO_COLOR_SAVE_ATTRIBUTE, player.appearance.torso.color)
        setAttribute(player, PLAYER_KIT_ARMS_SAVE_ATTRIBUTE, player.appearance.arms.look)
        setAttribute(player, PLAYER_KIT_ARMS_COLOR_SAVE_ATTRIBUTE, player.appearance.arms.color)
        setAttribute(player, PLAYER_KIT_LEGS_SAVE_ATTRIBUTE, player.appearance.legs.look)
        setAttribute(player, PLAYER_KIT_LEGS_COLOR_SAVE_ATTRIBUTE, player.appearance.legs.color)
        player.toggleWardrobe(true)
        val componentId = CLOTHES_DISPLAY_COMPONENT_ID
        val iface = if (male) THESSALIA_MALE_INTERFACE_ID else THESSALIA_FEMALE_INTERFACE_ID
        sendPlayerOnInterface(player, iface, componentId)
    }

    /**
     * Handles button interactions for clothes shop.
     */
    private fun handleClothesButtons(player: Player, comp: Int, button: Int, male: Boolean) {
        if (button in listOf(180, 181, 297)) {
            pay(player, comp)
            return
        }
        val part =
            when (comp to button) {
                THESSALIA_MALE_INTERFACE_ID to 182, THESSALIA_FEMALE_INTERFACE_ID to 183 -> BodyPart.TORSO

                THESSALIA_MALE_INTERFACE_ID to 183, THESSALIA_FEMALE_INTERFACE_ID to 184 -> BodyPart.ARMS

                THESSALIA_MALE_INTERFACE_ID to 184, THESSALIA_FEMALE_INTERFACE_ID to 185 -> BodyPart.LEGS
                else -> null
            }
        part?.let { setAttribute(player, PLAYER_KIT_TYPE_ATTRIBUTE, it) }
        when (button) {
            in (if (male) maleTorsoButtonRange else femaleTorsoButtonRange) ->
                updateAppearance(player, button, male, BodyPart.TORSO)

            in (if (male) maleArmsButtonRange else femaleArmsButtonRange) ->
                updateAppearance(player, button, male, BodyPart.ARMS)

            in (if (male) maleLegsButtonRange else femaleLegsButtonRange) ->
                updateAppearance(player, button, male, BodyPart.LEGS)

            in (if (male) maleClothesColorButtonRange else femaleClothesColorButtonRange) -> {
                val type = getAttribute(player, PLAYER_KIT_TYPE_ATTRIBUTE, BodyPart.TORSO)
                updateColor(player, button, male, type)
            }
        }
    }

    /**
     * Closes clothes shop.
     */
    private fun closeClothesShop(player: Player): Boolean {
        player.toggleWardrobe(false)
        removeAttribute(player, PLAYER_KIT_TYPE_ATTRIBUTE)
        playJingle(player, 266)

        if (!getAttribute(player, PLAYER_KIT_PAID_ATTRIBUTE, false)) {
            updateTorsoLook(player, getAttribute(player, PLAYER_KIT_TORSO_SAVE_ATTRIBUTE, 0))
            updateTorsoColor(player, getAttribute(player, PLAYER_KIT_TORSO_COLOR_SAVE_ATTRIBUTE, 0))
            updateArmsLook(player, getAttribute(player, PLAYER_KIT_ARMS_SAVE_ATTRIBUTE, 0))
            updateArmsColor(player, getAttribute(player, PLAYER_KIT_ARMS_COLOR_SAVE_ATTRIBUTE, 0))
            updateLegsLook(player, getAttribute(player, PLAYER_KIT_LEGS_SAVE_ATTRIBUTE, 0))
            updateLegsColor(player, getAttribute(player, PLAYER_KIT_LEGS_COLOR_SAVE_ATTRIBUTE, 0))
            refreshAppearance(player)
            runTask(player, 2) { sendNPCDialogue(player, NPCs.THESSALIA_548, "A marvellous choice. You look splendid!") }
        }

        removeAttribute(player, PLAYER_KIT_PAID_ATTRIBUTE)
        return true
    }

    /**
     * Opens Yrsa shoe store.
     */
    private fun openShoeShop(player: Player) {
        val original = player.appearance.feet.color
        player.toggleWardrobe(true)
        setAttribute(player, PLAYER_KIT_FEET_SAVE_ATTRIBUTE, original)
        for (i in YRSA_FEET_MODEL_IDS.indices) {
            sendItemOnInterface(
                player, YRSA_SHOE_STORE_INTERFACE_ID, YRSA_SELECT_BUTTONS_COMPONENT_IDS[i], YRSA_FEET_MODEL_IDS[i]
            )
        }
        val text = if (!player.houseManager.isInHouse(player)) "CONFIRM (500 GOLD)" else "CONFIRM (FREE)"
        sendString(player, text, YRSA_SHOE_STORE_INTERFACE_ID, 14)
        refreshAppearance(player)
    }

    /**
     * Closes Yrsa shoe store.
     */
    private fun closeShoeShop(player: Player) {
        player.toggleWardrobe(false)
        playJingle(player, 266)
        if (!getAttribute(player, PLAYER_KIT_PAID_ATTRIBUTE, false)) {
            val original = getAttribute(player, PLAYER_KIT_FEET_SAVE_ATTRIBUTE, player.appearance.feet.color)
            updateFeetColor(player, original)
            refreshAppearance(player)
        }

        removeAttribute(player, PLAYER_KIT_PAID_ATTRIBUTE)
        removeAttribute(player, PLAYER_KIT_FEET_SAVE_ATTRIBUTE)
        refreshAppearance(player)
    }

    private enum class BodyPart {
        TORSO,
        ARMS,
        LEGS
    }

    /**
     * Updates the appearance.
     *
     * @param player The player.
     * @param button The button.
     * @param male Whether the interface version is male.
     * @param part The body part being updated.
     */
    private fun updateAppearance(player: Player, button: Int, male: Boolean, part: BodyPart) {
        val (range, array) =
            when (part to male) {
                BodyPart.TORSO to true -> maleTorsoButtonRange to maleTorsoIDs
                BodyPart.ARMS to true -> maleArmsButtonRange to maleSleeveIDs
                BodyPart.LEGS to true -> maleLegsButtonRange to maleLegIDs
                BodyPart.TORSO to false -> femaleTorsoButtonRange to femaleTopIDs
                BodyPart.ARMS to false -> femaleArmsButtonRange to femaleArmIDs
                BodyPart.LEGS to false -> femaleLegsButtonRange to femaleLegIDs
                else -> return
            }
        val index = button - range.first
        if (index !in array.indices) return

        when (part) {
            BodyPart.TORSO -> player.appearance.torso.changeLook(array[index])
            BodyPart.ARMS -> player.appearance.arms.changeLook(array[index])
            BodyPart.LEGS -> player.appearance.legs.changeLook(array[index])
        }
        refreshAppearance(player)
    }

    /**
     * Updates wrist appearance preview.
     *
     * @param player The player previewing bracelets.
     * @param modelId The bracelet.
     */
    private fun updateArmguards(player: Player, modelId: Int) {
        val appearanceIndex = calculateBraceletIndex(modelId, player)
        sendModelOnInterface(
            player,
            Components.REINALD_SMITHING_EMPORIUM_593, BRACELET_PREVIEW_COMPONENT_ID,
            modelId,
            1
        )
        setComponentVisibility(
            player,
            Components.REINALD_SMITHING_EMPORIUM_593, BRACELET_PREVIEW_COMPONENT_ID,
            modelId == 0
        )
        updateWristsLook(player, appearanceIndex)
        player.debug("Using wrist appearance id =[$appearanceIndex]")
        refreshAppearance(player)
        sendPlayerOnInterface(player, Components.REINALD_SMITHING_EMPORIUM_593, 60)
    }

    /**
     * Updates the color of the selected body part.
     *
     * @param player The player.
     * @param button The button id.
     * @param male Whether the interface version is male.
     * @param type The body part whose color is being updated.
     */
    private fun updateColor(player: Player, button: Int, male: Boolean, type: BodyPart) {
        val subtract = if (male) maleClothesColorButtonRange.first else femaleClothesColorButtonRange.first
        val index = button - subtract
        val colorArray =
            when (type) {
                BodyPart.ARMS, BodyPart.TORSO -> torsoColors

                BodyPart.LEGS -> legColors
            }
        if (index in colorArray.indices) {
            when (type) {
                BodyPart.ARMS,
                BodyPart.TORSO -> player.appearance.torso.changeColor(colorArray[index])
                BodyPart.LEGS -> player.appearance.legs.changeColor(colorArray[index])
            }
            refreshAppearance(player)
        } else player.debug("Invalid color: button=$button, index=$index, male=$male, type=$type")
    }

    /**
     * Updates hair or hair-color selection for the female hairdresser interface.
     *
     * @param player The player.
     * @param button The button id.
     */
    private fun updateHairFemale(player: Player, button: Int) {
        when (button) {
            in femaleColorButtonRange -> updateHairColor(player, button, HAIRDRESSER_FEMALE_INTERFACE_ID)

            in femaleStyleButtonRange -> updateHair(player, button, HAIRDRESSER_FEMALE_INTERFACE_ID)
        }
    }

    /**
     * Updates hair, beard, or hair-color for the male hairdresser interface.
     *
     * @param player The player.
     * @param button The button id.
     */
    private fun updateHairMale(player: Player, button: Int) {
        val beardMode = getAttribute(player, PLAYER_KIT_BEARD_SETTINGS_ATTRIBUTE, false)
        when {
            beardMode && button !in maleColorButtonRange -> updateBeard(player, button)
            button in maleColorButtonRange -> updateHairColor(player, button, HAIRDRESSER_MALE_INTERFACE_ID)

            button in maleStyleButtonRange -> updateHair(player, button, HAIRDRESSER_MALE_INTERFACE_ID)
        }
    }

    /**
     * Updates beard style.
     *
     * @param player The player.
     * @param button The button id.
     */
    private fun updateBeard(player: Player, button: Int) {
        var offset = 105
        when (button) {
            123 -> offset += 2
            126 -> offset += 4
            129 -> offset += 6
        }
        val index = MALE_FACIAL[button - offset]
        updateBeardLook(player, index)
        refreshAppearance(player)
    }

    /**
     * Updates hairstyle look.
     *
     * @param player The player.
     * @param button The button id.
     * @param iface The interface version (male/female).
     */
    private fun updateHair(player: Player, button: Int, iface: Int) {
        val base = if (iface == HAIRDRESSER_MALE_INTERFACE_ID) 65 else 148
        val array = if (iface == HAIRDRESSER_MALE_INTERFACE_ID) MALE_HAIR else FEMALE_HAIR
        val subtractor = if (button in listOf(89, 90)) base + 2 else base
        updateHairLook(player, array[button - subtractor])
        refreshAppearance(player)
    }

    /**
     * Updates hair color.
     *
     * @param player The player.
     * @param button The button id.
     * @param iface The interface version (male/female).
     */
    private fun updateHairColor(player: Player, button: Int, iface: Int) {
        val offset = if (iface == HAIRDRESSER_MALE_INTERFACE_ID) 229 else 73
        updateHairColor(player, HAIR_COLORS[button - offset])
        refreshAppearance(player)
    }

    /**
     * Updates the player feet color.
     *
     * @param player The player.
     * @param button The button id.
     */
    private fun updateFeet(player: Player, button: Int) {
        val idx = button - 15
        setVarp(player, 261, button - 14)
        updateFeetColor(player, YRSA_COLOR_BUTTONS_COMPONENT_IDS[idx])
        refreshAppearance(player)
    }

    /**
     * Saves the selected gender.
     *
     * @param player The player.
     * @param male True for male.
     */
    private fun updateGender(player: Player, male: Boolean) {
        setAttribute(
            player, PLAYER_KIT_GENDER_SAVE_ATTIBUTE,
            if (male) Gender.MALE.ordinal else Gender.FEMALE.ordinal
        )
    }

    /**
     * Updates the skin color.
     *
     * @param player The player.
     * @param button The button id.
     */
    private fun updateSkin(player: Player, button: Int) {
        val newIndex =
            when (button) {
                in 93..99 -> button - 92
                100 -> 8
                else -> return
            }
        val newSkin = button - SKIN_COLOR_BUTTON_COMPONENT_IDS.first
        setAttribute(player, PLAYER_KIT_SKIN_COLOR_SAVE_ATTRIBUTE, newSkin)
        setVarp(player, 262, newIndex)
        refreshAppearance(player)
    }

    /**
     * Saves previous appearance for makeover mage.
     *
     * @param player The player.
     * @param newGender The gender.
     */
    private fun mapAppearance(player: Player, newGender: Gender) {
        val appearance = player.appearance
        val oldGender = appearance.gender
        if (oldGender == newGender) return

        val oldCache = appearance.appearanceCache.map { it.look to it.color }
        appearance.setGender(newGender)

        val src = if (oldGender == Gender.MALE) MALE_LOOK_IDS else FEMALE_LOOK_IDS
        val dst = if (newGender == Gender.MALE) MALE_LOOK_IDS else FEMALE_LOOK_IDS

        val newCache = appearance.appearanceCache
        for (i in newCache.indices) {
            val (look, col) = oldCache.getOrNull(i) ?: continue
            val s = src.getOrNull(i)
            val d = dst.getOrNull(i)
            if (s == null || d == null || s.isEmpty() || d.isEmpty()) continue
            val idx = s.indexOf(look)
            val mapped = if (idx != -1 && idx < d.size) d[idx] else d.first()
            newCache[i].changeLook(mapped)
            newCache[i].changeColor(col)
        }
        appearance.sync()
    }

    /**
     * Processes payment for hair, arm guards, and clothes update.
     *
     * @param player The player.
     * @param shop The interface id.
     */
    private fun pay(player: Player, shop: Int) {
        val changed =
            when (shop) {
                HAIRDRESSER_FEMALE_INTERFACE_ID, HAIRDRESSER_MALE_INTERFACE_ID -> {
                    val hair = getAttribute(player, PLAYER_KIT_HAIR_SAVE_ATTRIBUTE, -1)
                    val hairColor = getAttribute(player, PLAYER_KIT_HAIR_COLOR_SAVE_ATTRIBUTE, -1)
                    val beard = getAttribute(player, PLAYER_KIT_BEARD_SAVE_ATTRIBUTE, -1)
                    hair != player.appearance.hair.look ||
                            hairColor != player.appearance.hair.color ||
                            beard != player.appearance.beard.look
                }

                REINALD_BRACELETS_INTERFACE_ID -> {
                    val original =
                        getAttribute(player, PLAYER_KIT_WRIST_SAVE_ATTRIBUTE, defaultBraceletAppearance(player))
                    original != player.appearance.wrists.look
                }

                THESSALIA_FEMALE_INTERFACE_ID, THESSALIA_MALE_INTERFACE_ID -> {
                    val torso = getAttribute(player, PLAYER_KIT_TORSO_SAVE_ATTRIBUTE, -1)
                    val arms = getAttribute(player, PLAYER_KIT_ARMS_SAVE_ATTRIBUTE, -1)
                    val legs = getAttribute(player, PLAYER_KIT_LEGS_SAVE_ATTRIBUTE, -1)
                    torso != player.appearance.torso.look ||
                            arms != player.appearance.arms.look ||
                            legs != player.appearance.legs.look
                }
                else -> {
                    log(this.javaClass, Log.WARN, "Invalid shop =[$shop].")
                    return
                }
            }

        if (!changed) {
            sendMessage(player, "You must select an option first.")
            return
        }

        val price =
            when (shop) {
                HAIRDRESSER_FEMALE_INTERFACE_ID, HAIRDRESSER_MALE_INTERFACE_ID -> HAIR_CHANGE_PRICE

                REINALD_BRACELETS_INTERFACE_ID -> WRISTS_CHANGE_PRICE
                THESSALIA_FEMALE_INTERFACE_ID, THESSALIA_MALE_INTERFACE_ID -> CLOTHES_PRICE
                else -> 0
            }

        val inHouse = player.houseManager.isInHouse(player)
        if (!inHouse && !removeItem(player, price)) {
            val message =
                when (shop) {
                    THESSALIA_FEMALE_INTERFACE_ID, THESSALIA_MALE_INTERFACE_ID -> "You need 1,000 gold coins to change your clothes."

                    REINALD_BRACELETS_INTERFACE_ID -> "You need 500 gold coins to change your armguards."
                    HAIRDRESSER_FEMALE_INTERFACE_ID, HAIRDRESSER_MALE_INTERFACE_ID -> "You need 2,000 gold coins to change your hairstyle."
                    else -> "You cannot afford this."
                }
            sendMessage(player, message)
            return
        }

        setAttribute(player, PLAYER_KIT_PAID_ATTRIBUTE, true)
        closeInterface(player)
    }

    /**
     * Processes payment for feet update.
     *
     * @param player The player.
     */
    private fun shoePay(player: Player) {
        val newColor = getAttribute(player, PLAYER_KIT_FEET_SAVE_ATTRIBUTE, player.appearance.feet.color)

        if (newColor == player.appearance.feet.color) {
            closeInterface(player)
            return
        }
        if (!player.houseManager.isInHouse(player)) {
            if (!removeItem(player, FEET_CHANGE_PRICE)) {
                sendMessage(player, "You need 500 gold coins to change your shoes.")
                return
            }
        }

        setAttribute(player, PLAYER_KIT_PAID_ATTRIBUTE, true)
        closeInterface(player)
        openDialogue(player, EndDialogue())
    }

    /**
     * Processes payment for gender and skin update.
     *
     * @param player The player.
     */
    private fun makeoverPay(player: Player) {
        val oldGender = player.appearance.gender
        val oldSkin = player.appearance.skin.color

        val newSkin = getAttribute(player, PLAYER_KIT_SKIN_COLOR_SAVE_ATTRIBUTE, oldSkin)
        val newGender = Gender.values()[player.getAttribute(PLAYER_KIT_GENDER_SAVE_ATTIBUTE, oldGender.ordinal)]

        if (newSkin == oldSkin && newGender == oldGender) {
            sendMessage(player, "You must select an option first.")
            closeInterface(player)
            return
        }

        val currency =
            if (inInventory(player, Items.MAKEOVER_VOUCHER_5606)) {
                MAKEOVER_VOUCHER
            } else {
                MAKEOVER_PRICE
            }

        if (!removeItem(player, currency)) {
            sendMessage(player, "You cannot afford this.")
            closeInterface(player)
            return
        }

        setAttribute(player, PLAYER_KIT_PAID_ATTRIBUTE, true)
        closeInterface(player)

        val npc = findNPC(NPCs.MAKE_OVER_MAGE_2676)
        if (npc != null && oldGender != newGender) {

            when {
                oldGender == Gender.MALE && newGender == Gender.FEMALE -> {
                    sendChat(npc, "Ooh!")
                    npc.transform(NPCs.MAKE_OVER_MAGE_2676)
                }
                oldGender == Gender.FEMALE && newGender == Gender.MALE -> {
                    sendChat(npc, "Aha!")
                    npc.transform(NPCs.MAKE_OVER_MAGE_599)
                }
            }

            queueScript(player, 5, QueueStrength.SOFT) {
                npc.reTransform()
                return@queueScript stopExecuting(player)
            }
        }
    }

    /**
     * Shoe store dialogue after purchase.
     */
    inner class EndDialogue : DialogueFile() {
        override fun handle(componentID: Int, buttonID: Int) {
            when (stage) {
                0 -> npc(NPCs.YRSA_1301, FaceAnim.FRIENDLY, "I think they suit you.").also { stage++ }
                1 -> player(FaceAnim.HAPPY, "Thanks!").also { stage++ }
                2 -> end()
            }
        }
    }

    companion object {
        /* ╔════════════════════════════════════════════╗
         * ║ GLOBAL ATTRIBUTES                          ║
         * ╚════════════════════════════════════════════╝ */
        val PLAYER_KIT_PAID_ATTRIBUTE = "player_kit:paid"
        val PLAYER_KIT_TYPE_ATTRIBUTE = "player_kit:type"
        val PLAYER_KIT_GENDER_SAVE_ATTIBUTE = "player_kit:gender"
        val PLAYER_KIT_SKIN_COLOR_SAVE_ATTRIBUTE = "player_kit:skin-color"
        val PLAYER_KIT_WRIST_SAVE_ATTRIBUTE = "player_kit:wrists"
        val PLAYER_KIT_HAIR_SAVE_ATTRIBUTE = "player_kit:hair"
        val PLAYER_KIT_HAIR_COLOR_SAVE_ATTRIBUTE = "player_kit:hair-color"
        val PLAYER_KIT_BEARD_SAVE_ATTRIBUTE = "player_kit:beard"
        val PLAYER_KIT_BEARD_SETTINGS_ATTRIBUTE = "player_kit:beard-setting"
        val PLAYER_KIT_TORSO_SAVE_ATTRIBUTE = "player_kit:torso"
        val PLAYER_KIT_TORSO_COLOR_SAVE_ATTRIBUTE = "player_kit:torso-color"
        val PLAYER_KIT_ARMS_SAVE_ATTRIBUTE = "player_kit:arms"
        val PLAYER_KIT_ARMS_COLOR_SAVE_ATTRIBUTE = "player_kit:arms-color"
        val PLAYER_KIT_LEGS_SAVE_ATTRIBUTE = "player_kit:legs"
        val PLAYER_KIT_LEGS_COLOR_SAVE_ATTRIBUTE = "player_kit:legs-color"
        val PLAYER_KIT_FEET_SAVE_ATTRIBUTE = "player_kit:feet"

        /* ╔════════════════════════════════════════════╗
         * ║ INTERFACE IDS                              ║
         * ╚════════════════════════════════════════════╝ */
        const val APPEARANCE_INTERFACE_ID           = Components.APPEARANCE_771 // All.
        const val YRSA_SHOE_STORE_INTERFACE_ID      = Components.YRSA_SHOE_STORE_200 // Feet appearance.
        const val MAKEOVER_MAGE_INTERFACE_ID        = Components.MAKEOVER_MAGE_205 // Skin & gender appearance.
        const val REINALD_BRACELETS_INTERFACE_ID    = Components.REINALD_SMITHING_EMPORIUM_593 // Wrists appearance.
        const val THESSALIA_MALE_INTERFACE_ID       = Components.THESSALIA_CLOTHES_MALE_591 // Clothes (male) appearance.
        const val THESSALIA_FEMALE_INTERFACE_ID     = Components.THESSALIA_CLOTHES_FEMALE_594 // Clothes (female) appearance.
        const val HAIRDRESSER_MALE_INTERFACE_ID     = Components.HAIRDRESSER_MALE_596 // Hair (male) appearance.
        const val HAIRDRESSER_FEMALE_INTERFACE_ID   = Components.HAIRDRESSER_FEMALE_592 // Hair (female) appearance.

        /* ╔════════════════════════════════════════════╗
         * ║ PRICES                                     ║
         * ╚════════════════════════════════════════════╝ */
        val WRISTS_CHANGE_PRICE = Item(Items.COINS_995, 500)
        val FEET_CHANGE_PRICE   = Item(Items.COINS_995, 500)
        val CLOTHES_PRICE       = Item(Items.COINS_995, 1000)
        val HAIR_CHANGE_PRICE   = Item(Items.COINS_995, 2000)
        val MAKEOVER_PRICE      = Item(Items.COINS_995, 3000)
        val MAKEOVER_VOUCHER    = Item(Items.MAKEOVER_VOUCHER_5606, 1)

        /* ╔════════════════════════════════════════════╗
         * ║ YRSA SHOE STORE INTERFACE COMPONENT IDS    ║
         * ╚════════════════════════════════════════════╝ */
        val YRSA_SELECT_BUTTONS_COMPONENT_IDS   = intArrayOf(15, 16, 17, 18, 19, 20)
        val YRSA_COLOR_BUTTONS_COMPONENT_IDS    = intArrayOf(0, 1, 2, 3, 4, 5)
        val YRSA_FEET_MODEL_IDS                 = intArrayOf(3680, 3681, 3682, 3683, 3684, 3685)// Pictures

        /* ╔════════════════════════════════════════════╗
         * ║ MAKEOVER MAGE INTERFACE COMPONENT IDS      ║
         * ╚════════════════════════════════════════════╝ */
        const val MAKEOVER_MODEL_MALE_COMPONENT_ID      = 90
        const val MAKEOVER_MODEL_FEMALE_COMPONENT_ID    = 92
        const val MAKEOVER_CONFIRM_COMPONENT_ID         = 88
        val SKIN_COLOR_BUTTON_COMPONENT_IDS             = 93..100

        /* ╔════════════════════════════════════════════╗
         * ║ THESSALIA CLOTHES INTERFACE COMPONENT IDS  ║
         * ╚════════════════════════════════════════════╝ */
        const val CLOTHES_DISPLAY_COMPONENT_ID  = 59
        val maleTorsoButtonRange                = (185..198)
        val femaleTorsoButtonRange              = (186..196)
        val maleArmsButtonRange                 = (199..210)
        val femaleArmsButtonRange               = (197..207)
        val maleLegsButtonRange                 = (211..221)
        val femaleLegsButtonRange               = (208..222)
        val maleClothesColorButtonRange         = (252..280)
        val femaleClothesColorButtonRange       = (253..281)

        val maleTorsoIDs    = intArrayOf(111, 113, 114, 115, 21, 116, 18, 19, 20, 112, 24, 23, 24, 25)
        val maleSleeveIDs   = intArrayOf(107, 108, 29, 106, 110, 109, 28, 26, 27, 105, 30, 31)
        val maleLegIDs      = intArrayOf(36, 85, 37, 40, 89, 90, 86, 88, 39, 38, 87)
        val femaleTopIDs    = intArrayOf(153, 155, 156, 157, 154, 158, 56, 57, 58, 59, 60)
        val femaleArmIDs    = intArrayOf(149, 150, 65, 148, 151, 152, 64, 61, 63, 147, 62)
        val femaleLegIDs    = intArrayOf(129, 130, 128, 74, 133, 134, 71, 131, 132, 75, 73, 76)
        val legColors       = intArrayOf(24, 23, 3, 22, 13, 12, 7, 19, 5, 1, 10, 14, 25, 9, 0, 26, 21, 8, 20, 15, 11, 28, 27, 4, 6, 18, 17, 2, 16)
        val torsoColors     = intArrayOf(24, 23, 2, 22, 12, 11, 6, 19, 4, 0, 9, 13, 25, 8, 15, 26, 21, 7, 20, 14, 10, 28, 27, 3, 5, 18, 17, 1, 16)

        /* ╔════════════════════════════════════════════╗
         * ║ HAIRDRESSER INTERFACE COMPONENT IDS        ║
         * ╚════════════════════════════════════════════╝ */
        val maleColorButtonRange    = 229..253
        val femaleColorButtonRange  = 73..97
        val maleStyleButtonRange    = 65..90
        val femaleStyleButtonRange  = 148..181

        private val FEMALE_HAIR_STYLES  = intArrayOf(45, 46, 47, 48, 49, 50, 51, 52, 53, 54, 135, 136, 137, 138, 139, 140, 141, 142, 143, 144, 145, 242, 269, 270, 271, 272, 273, 274, 275, 276, 277, 278, 279, 280)
        private val MALE_HAIR_STYLES    = intArrayOf(0, 1, 2, 3, 4, 5, 6, 7, 8, 91, 92, 93, 94, 95, 96, 97, 246, 262, 251, 265, 252, 257, 247, 253)
        private val MALE_FACIAL_STYLES  = intArrayOf(10, 11, 12, 13, 14, 15, 16, 17, 98, 99, 100, 101, 102, 103, 104, 305, 306, 307, 308)
        val HAIR_COLORS                 = intArrayOf(20, 19, 10, 18, 4, 5, 15, 7, 0, 6, 21, 9, 22, 17, 8, 16, 11, 24, 23, 3, 2, 1, 14, 13, 12)

        // Full arrays.
        val MALE_HAIR = MALE_HAIR_STYLES
        val FEMALE_HAIR = FEMALE_HAIR_STYLES
        val MALE_FACIAL = MALE_FACIAL_STYLES

        /* ╔════════════════════════════════════════════╗
         * ║ REINALD BRACELETS INTERFACE COMPONENT IDS  ║
         * ╚════════════════════════════════════════════╝ */
        const val BRACELET_PREVIEW_COMPONENT_ID = 69
        val WRISTS_MODELS = mapOf(
            122 to 0,
            123 to 27703,
            124 to 27704,
            125 to 27706,
            126 to 27707,
            127 to 27697,
            128 to 27699,
            129 to 0,
            130 to 27698,
            131 to 27708,
            132 to 27702,
            133 to 27705,
            134 to 27700,
            135 to 27709
        )

        /* ╔════════════════════════════════════════════╗
         * ║ APPEARANCE INTERFACE COMPONENT IDS         ║
         * ╚════════════════════════════════════════════╝ */
        val SKIN_COLORS = intArrayOf(7, 6, 5, 4, 3, 2, 1, 0)
        val COLOR_MAPPINGS = listOf(
            Triple(0, HAIR_COLORS, 100..124),
            Triple(2, torsoColors, 189..217),
            Triple(5, legColors, 248..276),
            Triple(6, YRSA_COLOR_BUTTONS_COMPONENT_IDS, 307..312),
            Triple(4, SKIN_COLORS, 151..158)
        )

        val MALE_LOOK_IDS = arrayOf(
            // head component ids.
            intArrayOf(0, 1, 2, 3, 4, 5, 6, 7, 8, 91, 92, 93, 94, 95, 96, 97, 261, 262, 263, 264, 265, 266, 267, 268),
            // jaw component ids.
            intArrayOf(10, 11, 12, 13, 14, 15, 16, 17, 98, 99, 100, 101, 102, 103, 104, 305, 306, 307, 308),
            // torso component ids.
            intArrayOf(18, 19, 20, 21, 22, 23, 24, 25, 111, 112, 113, 114, 115, 116),
            // arms component ids.
            intArrayOf(26, 27, 28, 29, 30, 31, 105, 106, 107, 108, 109, 110),
            // hand component ids.
            intArrayOf(33, 34, 84, 117, 118, 119, 120, 121, 122, 123, 124, 125, 126),
            // legs component ids.
            intArrayOf(36, 37, 38, 39, 40, 85, 86, 87, 88, 89, 90),
            // feet component ids.
            intArrayOf(42, 43)
        )

        val FEMALE_LOOK_IDS = arrayOf(
            // head component ids.
            intArrayOf(45, 46, 47, 48, 49, 50, 51, 52, 53, 54, 135, 136, 137, 138, 139, 140, 141, 142, 143, 144, 145, 146, 269, 270, 271, 272, 273, 274, 275, 276, 277, 278, 279, 280),
            // jaw component ids.
            intArrayOf(1000),
            // torso component ids.
            intArrayOf(56, 57, 58, 59, 60, 153, 154, 155, 156, 157, 158),
            // arms component ids.
            intArrayOf(61, 62, 63, 64, 65, 147, 148, 149, 150, 151, 152),
            // hand component ids.
            intArrayOf(67, 68, 127, 159, 160, 161, 162, 163, 164, 165, 166, 167, 168),
            // legs component ids.
            intArrayOf(70, 71, 72, 73, 74, 75, 76, 77, 128, 129, 130, 131, 132, 133, 134),
            // feet component ids.
            intArrayOf(79, 80)
        )
    }
}