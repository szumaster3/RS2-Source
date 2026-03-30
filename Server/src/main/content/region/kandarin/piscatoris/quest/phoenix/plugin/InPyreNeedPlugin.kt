package content.region.kandarin.piscatoris.quest.phoenix.plugin

import com.google.gson.JsonObject
import content.data.GameAttributes
import content.global.activity.phoenix.PhoenixSpawnCutscene
import content.region.kandarin.piscatoris.quest.phoenix.InPyreNeed
import content.region.kandarin.piscatoris.quest.phoenix.custcene.FuneralPyreCutscene
import content.region.kandarin.piscatoris.quest.phoenix.custcene.LostCutscene
import content.region.kandarin.piscatoris.quest.phoenix.custcene.WoundedPhoenixCutscene
import content.region.kandarin.piscatoris.quest.phoenix.dialogue.PhoenixEgglingDialogue
import core.ServerStore
import core.ServerStore.Companion.getBoolean
import core.api.*
import core.api.utils.PlayerCamera
import core.game.dialogue.FaceAnim
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.interaction.QueueStrength
import core.game.node.entity.player.link.TeleportManager
import core.game.node.entity.skill.Skills
import core.game.node.item.Item
import core.game.world.map.Location
import core.game.world.update.flag.context.Animation
import shared.consts.*

class InPyreNeedPlugin : InteractionListener {

    override fun defineListeners() {
        /*
         * Handles entering the Phoenix Lair.
         */

        on(Scenery.CAVE_ENTRANCE_41900, IntType.SCENERY, "enter") { player, _ ->
            val familiar = player.familiarManager.familiar

            if (player.familiarManager.hasFamiliar() && familiar != null) {
                when (familiar.id) {
                    NPCs.PHOENIX_8575, NPCs.PHOENIX_8576 -> {
                        sendNPCDialogue(player, familiar.id, "${player.username}, this is my lair. You must dismiss my summoned form if you wish to enter; I will only duel you when I am at full strength.", FaceAnim.FAMILIAR_NEUTRAL)
                        return@on true
                    }

                    NPCs.PHOENIX_EGGLING_8577, NPCs.PHOENIX_EGGLING_8578 -> {
                        sendNPCDialogue(player, familiar.id, "Why am you bringing me here? I no want to see you fight my mummy! Put me in your bag if you want to go in.", FaceAnim.FAMILIAR_NEUTRAL)
                        return@on true
                    }
                }
            }

            if (getQuestStage(player, Quests.IN_PYRE_NEED) < 1) {
                sendNPCDialogue(player, NPCs.PRIEST_OF_GUTHIX_8555, "Hey, wait! I have something to ask you before going down there.", FaceAnim.FRIENDLY)
                addDialogueAction(player) { p, _ ->
                    sendPlayerDialogue(p, "Maybe I should see what he has to say before entering this dark, scary, and unreasonably warm cave.", FaceAnim.THINKING)
                }
            } else {
                teleport(player, Location.create(3461, 5218, 0), TeleportManager.TeleportType.INSTANT)
            }
            return@on true
        }

        /*
         * Handles random cave teleportation logic.
         */

        on(intArrayOf(Scenery.CAVE_ENTRANCE_41901, Scenery.CAVE_ENTRANCE_41902), IntType.SCENERY, "look-at", "enter") { player, _ ->
            val caveLocations = arrayOf(
                Location.create(3466, 5213, 0),
                Location.create(3493, 5186, 0), // Sassafras
                Location.create(3543, 5218, 0),
                Location.create(3476, 5186, 0),
                Location.create(3516, 5224, 0) // Authentic
            )

            fun crawlIntoCave(targetLocation: Location) {
                lock(player, 3)
                openInterface(player, Components.FADE_TO_BLACK_120)
                player.animate(Animation.create(Animations.HUMAN_CRAWL_INTO_CAVE_11042), 1)
                queueScript(player, 3, QueueStrength.SOFT) {
                    teleport(player, targetLocation)
                    resetAnimator(player)
                    openInterface(player, Components.FADE_FROM_BLACK_170)
                    return@queueScript stopExecuting(player)
                }
            }

            registerLogoutListener(player, InPyreNeed.LOGOUT_LISTENER) { p ->
                player.location = InPyreNeed.CAVE_EXIT_LOCATION
            }

            val inPhoenixChamber = player.location.equals(InPyreNeed.PHOENIX_CHAMBER) ||
                    player.location.equals(Location.create(3535, 5186, 0))

            if (inPhoenixChamber) {
                setTitle(player, 2)
                sendOptions(player, "Are you sure you want to leave?", "Yes", "No")
                addDialogueAction(player) { _, button ->
                    val targetLocation = when (button) {
                        2 -> {
                            sendMessage(player, "You make your way back to the phoenix's roost.")
                            if(player.location != Location.create(3566, 5224, 0)) Location(3566, 5224, 0) else Location.create(2294, 3626, 0)
                        }
                        3 -> caveLocations.random()
                        else -> null
                    }
                    targetLocation?.let { crawlIntoCave(it) } ?: closeDialogue(player)
                }
                return@on true
            }

            if (InPyreNeed.CHANCE_TO_RECEIVE_PET == 0) {
                val largeEgg = core.game.node.entity.npc.NPC.create(NPCs.LARGE_EGG_8552, Location.create(3567, 5230, 0))
                registerLogoutListener(player, "large-egg") {
                    largeEgg.clear()
                }
                largeEgg.init()
                LostCutscene(player).start()
                return@on true
            }

            val newLocation = when {
                getVarbit(player, Vars.VARBIT_QUEST_IN_PYRE_NEED_PROGRESS_5761) == 10 -> InPyreNeed.CAVE_EXIT_LOCATION
                allInInventory(player, *InPyreNeed.TWIG_ID) -> Location.create(3535, 5186, 0)
                else -> caveLocations.random()
            }

            crawlIntoCave(newLocation)

            if (newLocation.equals(Location.create(3535, 5186, 0))) {
                player.musicPlayer?.let { music ->
                    if (!music.hasUnlocked(Music.THE_PHOENIX_596)) {
                        music.unlock(Music.THE_PHOENIX_596)
                    }
                }
            }

            return@on true
        }

        /*
         * Handles escaping from the Phoenix lair.
         */

        on(intArrayOf(Scenery.CAVE_ENTRANCE_41901, Scenery.CAVE_ENTRANCE_41902), IntType.SCENERY, "escape") { player, _ ->
            lock(player, 3)
            openInterface(player, Components.FADE_TO_BLACK_120)
            animate(player, Animations.HUMAN_CRAWL_INTO_CAVE_11042)
            runTask(player, 3) {
                resetAnimator(player)
                openInterface(player, Components.FADE_FROM_BLACK_170)
                teleport(player, Location.create(2294, 3626, 0))
                if (!isQuestComplete(player, Quests.IN_PYRE_NEED)) {
                    player.dialogueInterpreter.open(NPCs.BRIAN_TWITCHER_8556)
                }
            }
            return@on true
        }

        /*
         * Handles harvesting twigs from trees.
         */

        on(InPyreNeed.itemMap.keys.toIntArray(), IntType.SCENERY, "harvest-twigs") { player, node ->
            val item = InPyreNeed.itemMap[node.id] ?: Items.MASTIC_TWIGS_14610
            val itemAnimation = if (inInventory(player, Items.MAGIC_SECATEURS_7409)) Animation(Animations.PRUNE_WITH_MAGIC_SECATEURS_11089)
            else Animation(Animations.PRUNE_WITH_SECATEURS_11088)

            if (!player.inventory.contains(item, 1)) {
                lock(player, 3)
                queueScript(player, 1, QueueStrength.SOFT) {
                    player.animate(itemAnimation)
                    addItemOrDrop(player, item, 1)
                    val twigs = getSceneryName(node.id).lowercase().replace("tree", "twigs")
                    sendMessage(player, "You harvest some $twigs.")
                    return@queueScript stopExecuting(player)
                }
            } else {
                sendMessage(player, "You already have ${node.name.lowercase()}.")
            }
            return@on true
        }

        /*
         * Handles weaving ribbons into a basket.
         */

        onUseWith(IntType.SCENERY, InPyreNeed.RIBBON_ID, Scenery.PYRE_41908) { player, _, _ ->
            val progress = getVarbit(player, Vars.VARBIT_QUEST_IN_PYRE_NEED_PROGRESS_5761)

            if (!isQuestComplete(player, Quests.IN_PYRE_NEED) && progress < 5) {
                sendMessage(player, "Nothing interesting happens.")
                return@onUseWith true
            }

            if (isQuestComplete(player, Quests.IN_PYRE_NEED) && getStoreFile().getBoolean(player.username.lowercase())) {
                sendMessage(player, "This can only be done once per day.")
                return@onUseWith true
            }

            if (InPyreNeed.RIBBON_ID.any { !inInventory(player, it) }) {
                sendMessage(player, "You need all 5 types of wooden ribbons to weave the basket.")
                return@onUseWith true
            }

            val anim = if (inInventory(player, Items.MAGIC_SECATEURS_7409))
                Animations.PRUNE_WITH_MAGIC_SECATEURS_11089
            else
                Animations.PRUNE_WITH_SECATEURS_11088

            lock(player, 3)
            animate(player, anim)
            sendMessage(player, "You weave a large basket from the five wooden ribbons and add it to the pyre base.")

            InPyreNeed.RIBBON_ID.forEach {
                removeItem(player, Item(it, 1), Container.INVENTORY)
            }

            if (isQuestComplete(player, Quests.IN_PYRE_NEED) && !getAttribute(player, "phoenix-spawned", false)) {
                setAttribute(player, "phoenix-spawned", true)
                PhoenixSpawnCutscene(player).start(true)
            } else {
                setVarbit(player, 5774, 1, true)
                WoundedPhoenixCutscene(player).start(true)
            }
            return@onUseWith true
        }

        /*
         * Handles lighting the pyre.
         */

        onUseWith(IntType.SCENERY, Items.TINDERBOX_590, Scenery.PYRE_41909) { player, _, _ ->
            if (getStatLevel(player, Skills.FIREMAKING) < 55) {
                sendMessage(player, "You need at least 55 Firemaking to light the funeral pyre.")
                return@onUseWith true
            }

            val progress = getVarbit(player, Vars.VARBIT_QUEST_IN_PYRE_NEED_PROGRESS_5761)
            if (progress < 5) {
                sendMessage(player, "You are not ready to light the pyre yet.")
                return@onUseWith true
            }

            lock(player, 3)
            animate(player, Animations.HUMAN_LIGHT_FIRE_WITH_TINDERBOX_733)
            FuneralPyreCutscene(player).start()
            return@onUseWith true
        }

        /*
         * Prevents players from lighting the wounded Phoenix.
         */

        onUseWith(IntType.NPC, Items.TINDERBOX_590, NPCs.WOUNDED_PHOENIX_8547) { player, _, _ ->
            sendNPCDialogue(player, NPCs.WOUNDED_PHOENIX_8547, "Light the pyre, not me!", FaceAnim.FAMILIAR_NEUTRAL)
            return@onUseWith true
        }

        /*
         * Handles fletch twigs into ribbons.
         */

        onUseWith(IntType.ITEM, Items.KNIFE_946, *InPyreNeed.TWIG_ID) { player, _, with ->
            if (getDynLevel(player, Skills.CRAFTING) < 52) {
                sendMessage(player, "You need at least level 52 Crafting to make these ribbons.")
                return@onUseWith true
            }

            if (hasAnItem(player, with.id).container == player.inventory) {
                lock(player, 3)
                animate(player, Animations.FLETCH_LOGS_4433)
                queueScript(player, 1, QueueStrength.SOFT) {
                    val index = InPyreNeed.TWIG_ID.indexOf(with.id)
                    if (index != -1) {
                        removeItem(player, Item(with.id, 1), Container.INVENTORY)
                        setVarbit(player, Vars.VARBIT_QUEST_IN_PYRE_NEED_PROGRESS_5761, 5, true)
                        sendItemDialogue(
                            player, with.id, "You craft the ${with.name.lowercase()} into a weaving ribbon."
                        )
                        addItem(player, InPyreNeed.RIBBON_ID[index], 1, Container.INVENTORY)
                    }
                    return@queueScript stopExecuting(player)
                }
                return@onUseWith true
            }
            sendMessage(player, "You don't have the required twigs to fletch.")
            return@onUseWith false
        }

        /*
         * Handles interactions with large egg & hatching.
         */

        on(InPyreNeed.PHOENIX_EGGLING_ID, IntType.NPC, "Investigate", "Interact") { player, node ->
            val phoenixEggling =
                core.game.node.entity.npc.NPC.create(NPCs.PHOENIX_EGGLING_8550, Location.create(3567, 5230, 0))
            if (node.id == NPCs.LARGE_EGG_8552) {
                sendOptions(
                    player,
                    "What would you like to do?",
                    "Clap at the egg to make it hatch.",
                    "Kick the egg to make it hatch."
                )
                addDialogueAction(player) { _, button ->
                    lock(player, 12)
                    queueScript(player, 1, QueueStrength.SOFT) { stage: Int ->
                        when (stage) {
                            0 -> {
                                animate(player, if (button == 2) Animations.CLAP_11091 else Animations.KICK_423)
                                sendChat(player, "*Gasp!* It's...it's hatching!", 2)
                                return@queueScript delayScript(player, 3)
                            }

                            1 -> {
                                // Camera-independent zoom.
                                PlayerCamera(player).panTo(3567, 5226, 300, 100)
                                PlayerCamera(player).rotateTo(3567, 5228, 300, 100)
                                return@queueScript delayScript(player, 4)
                            }

                            2 -> {
                                visualize(findNPC(NPCs.LARGE_EGG_8552)!!, -1,1974)
                                return@queueScript delayScript(player, 3)
                            }

                            3 -> {
                                node.asNpc().clear()
                                phoenixEggling.init()
                                // sendGraphics(0000, phoenixEggling.location)
                                sendChat(phoenixEggling, if (button == 2) "Cheeeep!" else "Bwaaark!", 2)
                                return@queueScript delayScript(player, 4)
                            }

                            4 -> {
                                resetCamera(player)
                                return@queueScript stopExecuting(player)
                            }

                            else -> return@queueScript stopExecuting(player)
                        }
                    }
                }
            } else {
                val cutePet = getAttribute(player, GameAttributes.PHOENIX_LAIR_EGGLING_CUTE, false)
                val meanPet = getAttribute(player, GameAttributes.PHOENIX_LAIR_EGGLING_MEAN, false)
                if (cutePet && meanPet) {
                    sendMessage(player, "You already catch this pet.")
                    if (findLocalNPC(player, node.id) != null) {
                        runTask(player, 1) { node.asNpc().clear() }
                    }
                    sendMessage(player, "The phoenix eggling disappears.")
                    return@on true
                }
                openDialogue(player, PhoenixEgglingDialogue())
            }
            return@on true
        }
    }

    companion object {
        private fun getStoreFile(): JsonObject = ServerStore.getArchive("daily-phoenix-lair-activity")
    }
}