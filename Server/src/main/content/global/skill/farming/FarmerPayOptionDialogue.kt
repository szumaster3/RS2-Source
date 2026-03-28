package content.global.skill.farming

import core.api.inInventory
import core.api.note
import core.api.removeItem
import core.api.sendDialogue
import core.game.dialogue.DialogueFile
import core.game.dialogue.FaceAnim
import core.game.dialogue.Topic
import core.game.node.item.Item
import core.tools.END_DIALOGUE
import core.tools.START_DIALOGUE
import shared.consts.Items
import shared.consts.NPCs

/**
 * Represents the Farmer protection dialogue.
 */
class FarmerPayOptionDialogue(val patch: Patch, private val quickPay: Boolean = false) : DialogueFile() {
    var item: Item? = null
    var itemSecondary: Item? = null
    var itemTertiary: Item? = null

    fun hasAllItems(): Boolean {
        return (inInventory(player!!, item!!.id, item!!.amount) || inInventory(player!!, note(item!!).id, note(item!!).amount))
                && (inInventory(player!!, itemSecondary!!.id, itemSecondary!!.amount) || inInventory(player!!, note(itemSecondary!!).id, note(itemSecondary!!).amount))
                && (inInventory(player!!, itemTertiary!!.id, itemTertiary!!.amount) || inInventory(player!!, note(itemTertiary!!).id, note(itemTertiary!!).amount))
    }

    private val OLD_FACE_NPCS = setOf(
        NPCs.PRISSY_SCILLA_1037,
        NPCs.BOLONGO_2343,
        NPCs.FRIZZY_SKERNIP_4560
    )

    override fun handle(componentID: Int, buttonID: Int) {
        val faceAnim = if (npc?.id in OLD_FACE_NPCS)
            FaceAnim.OLD_NORMAL
        else
            FaceAnim.HALF_GUILTY
        when (stage) {
            START_DIALOGUE -> {
                if (patch.patch.type == PatchType.TREE_PATCH && patch.plantable != null && patch.isGrown()) {
                    showTopics(
                        Topic("Yes, get rid of the tree.", 300, true),
                        Topic("No thanks.", END_DIALOGUE, true),
                        title = "Pay 200 gp to have the tree chopped down?"
                    )
                } else if (patch.protectionPaid) {
                    npc(faceAnim, "I don't know what you're talking about - I'm already", "looking after that patch for you.").also { stage = 100 }
                } else if (patch.isDead) {
                    npc(faceAnim, "That patch is dead - it's too late for me to do", "anything about it now.").also { stage = END_DIALOGUE }
                } else if (patch.isDiseased) {
                    npc(faceAnim, "That patch is diseased - I can't look after it", "until it has been cured.").also { stage = END_DIALOGUE }
                } else if (patch.isWeedy() || patch.isEmptyAndWeeded()) {
                    npc(faceAnim, "You don't have anything planted in that patch. Plant", "something and I might agree to look after it for you.").also { stage = END_DIALOGUE }
                } else if (patch.isGrown()) {
                    npc(faceAnim, "That patch is already fully grown!", "I don't know what you want me to do with it!").also { stage = END_DIALOGUE }
                } else {
                    item = patch.plantable?.protectionItem
                    itemSecondary = patch.plantable?.protectionItemSecondary
                    itemTertiary = patch.plantable?.protectionItemTertiary
                    val protectionText: String? = when (item?.id) {
                        Items.COMPOST_6032        -> pluralize("bucket of compost", item)
                        Items.POTATOES10_5438     -> pluralize("sack of potatoes", item)
                        Items.ONIONS10_5458       -> pluralize("sack of onions", item)
                        Items.CABBAGES10_5478     -> pluralize("sack of cabbages", item)
                        Items.APPLES5_5386        -> pluralize("basket of apples", item)
                        Items.TOMATOES5_5968      -> pluralize("basket of tomatoes", item)
                        Items.ORANGES5_5396       -> pluralize("basket of oranges", item)
                        Items.STRAWBERRIES5_5406  -> pluralize("basket of strawberries", item)
                        Items.BANANAS5_5416       -> pluralize("basket of bananas", item)

                        Items.JUTE_FIBRE_5931     -> "jute fibres"
                        Items.MARIGOLDS_6010      -> "harvest of marigold"
                        Items.COCONUT_5974        -> "coconuts"
                        Items.CACTUS_SPINE_6016   -> "cactus spines"

                        else                      -> item?.name?.lowercase()
                    }
                    if (item == null) {
                        if (patch.plantable!!.harvestItem == Items.POISON_IVY_BERRIES_6018) {
                            npc(faceAnim, "There is no need for me to look after that poison ivy.")
                            stage = 400
                        } else {
                            npc(faceAnim, "Sorry, I won't protect that.").also { stage = END_DIALOGUE }
                        }
                    } else if (quickPay && !(inInventory(player!!, item!!.id, item!!.amount) || inInventory(player!!, note(item!!).id, note(item!!).amount))) {
                        val amount = if (item?.amount == 1) "one" else item?.amount
                        npc(faceAnim, "I want $amount $protectionText for that.")
                        stage = 200
                    } else if (patch.patch.type == PatchType.SPIRIT_TREE_PATCH && quickPay && !hasAllItems()) {
                        val amount = if (item?.amount == 1) "one" else item?.amount
                        npc(FaceAnim.HAPPY, "I want $amount $protectionText, one monkey bar,", "and one ground tooth for that.")
                        stage = 200
                    } else if (quickPay) {
                        val amount = if (item?.amount == 1) "one" else item?.amount
                        showTopics(
                            Topic("Yes", 20, true),
                            Topic("No", END_DIALOGUE, true),
                            title = "Pay $amount $protectionText?",
                        )
                    } else if (patch.patch.type == PatchType.SPIRIT_TREE_PATCH) {
                        val amount = if (item?.amount == 1) "one" else item?.amount
                        npc(faceAnim,"If you like, but I want $amount $protectionText,", "one monkey bar, and one ground tooth for that.")
                        stage++
                    } else {
                        val amount = if (item?.amount == 1) "one" else item?.amount
                        npc(faceAnim, "If you like, but I want $amount $protectionText for that.")
                        stage++
                    }
                }
            }

            1 -> {
                if (patch.patch.type == PatchType.SPIRIT_TREE_PATCH && !hasAllItems()) {
                    player("I'm afraid I don't have any of those at the moment.").also { stage = 10 }
                } else if (!(inInventory(player!!, item!!.id, item!!.amount) || inInventory(player!!, note(item!!).id, note(item!!).amount))) {
                    player("I'm afraid I don't have any of those at the moment.").also { stage = 10 }
                } else {
                    showTopics(
                        Topic(FaceAnim.NEUTRAL, "Okay, it's a deal.", 20),
                        Topic(FaceAnim.NEUTRAL, "No, that's too much.", 10),
                    )
                }
            }

            10 -> npc(faceAnim, "Well, I'm not wasting my time for free.").also { stage = END_DIALOGUE }

            20 -> {
                if (patch.patch.type == PatchType.SPIRIT_TREE_PATCH
                    && (removeItem(player!!, item) || removeItem(player!!, note(item!!)))
                    && (removeItem(player!!, itemSecondary) || removeItem(player!!, note(itemSecondary!!)))
                    && (removeItem(player!!, itemTertiary) || removeItem(player!!, note(itemTertiary!!)))) {
                    patch.protectionPaid = true
                    npc("That'll do nicely, ${if (player!!.isMale) "sir" else "madam"}. Leave it with me - I'll make sure", "that tree grows up good and strong.").also { stage = END_DIALOGUE }
                } else if (patch.patch.type != PatchType.SPIRIT_TREE_PATCH && removeItem(player!!, item) || removeItem(player!!, note(item!!))) {
                    patch.protectionPaid = true
                    npc(faceAnim, "That'll do nicely, ${if (player!!.isMale) "sir" else "madam"}. Leave it with me - I'll make sure", "those crops grow for you.").also { stage = END_DIALOGUE }
                } else {
                    npc(faceAnim, "This shouldn't be happening. Please report this.").also { stage = END_DIALOGUE }
                }
            }

            100 -> player("Oh sorry, I forgot.").also { stage = END_DIALOGUE }

            200 -> player(FaceAnim.NEUTRAL, "Thanks, maybe another time.").also { stage = END_DIALOGUE }

            300 -> if (removeItem(player!!, Item(Items.COINS_995, 200))) {
                patch.clear()
                sendDialogue(player!!,"The gardener obligingly removes your tree.").also { stage = END_DIALOGUE }
            } else {
                sendDialogue(player!!, "You need 200 gp to pay for that.").also { stage = END_DIALOGUE }
            }
            400 -> player("Really?").also { stage++ }
            401 -> npc("Yes, poison ivy is pretty hardy stuff, and most animals", "will avoid eating it.").also { stage++ }
            402 -> npc("Hence, there is no reason to worry about it.").also { stage++ }
            403 -> player("Great.").also { stage = END_DIALOGUE }
        }
    }

    private fun pluralize(base: String, item: Item?): String =
        if (item?.amount == 1) base else "${base}s"
}
