package content.global.skill.summoning.familiar;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import content.global.skill.summoning.SummoningPouch;
import content.global.skill.summoning.pets.Pet;
import content.global.skill.summoning.pets.PetDetails;
import content.global.skill.summoning.pets.Pets;
import core.cache.def.impl.ItemDefinition;
import core.game.component.Component;
import core.game.container.Container;
import core.game.node.entity.combat.BattleState;
import core.game.node.entity.player.Player;
import core.game.node.entity.skill.Skills;
import core.game.node.item.Item;
import core.game.world.map.Location;
import core.game.world.map.zone.ZoneRestriction;
import core.game.world.update.flag.context.Animation;
import core.tools.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static core.api.ContentAPIKt.*;

/**
 * The type Familiar manager.
 */
public final class FamiliarManager {

    private static final Map<Integer, Familiar> FAMILIARS = new HashMap<>();
    private final Map<Integer, PetDetails> petDetails = new HashMap<Integer, PetDetails>();
    private final Player player;
    private Familiar familiar;
    private int summoningCombatLevel;
    private boolean hasPouch;

    public FamiliarManager(Player player) {
        this.player = player;
    }

    public final void parse(JsonObject familiarData) {
        int currentPet = -1;
        if (familiarData.has("currentPet")) {
            currentPet = familiarData.get("currentPet").getAsInt();
        }

        JsonArray petDetails = familiarData.getAsJsonArray("petDetails");
        for (int i = 0; i < petDetails.size(); i++) {
            JsonObject detail = petDetails.get(i).getAsJsonObject();
            PetDetails details = new PetDetails(0);
            details.updateHunger(detail.get("hunger").getAsDouble());
            details.updateGrowth(detail.get("growth").getAsDouble());

            int itemIdHash = detail.get("petId").getAsInt();

            if (detail.has("stage")) {
                int babyItemId = itemIdHash;
                int itemId = babyItemId;
                int stage = detail.get("stage").getAsInt();

                if (stage > 0) {
                    Pets pets = Pets.forId(babyItemId);
                    itemId = pets.getNextStageItemId(itemId);
                    if (stage > 1) {
                        itemId = pets.getNextStageItemId(itemId);
                    }
                }

                Item item = new Item(itemId);
                item.setCharge(1000);
                itemIdHash = item.getIdHash();

                if (currentPet != -1 && currentPet == babyItemId) {
                    currentPet = itemIdHash;
                }
            }

            this.petDetails.put(itemIdHash, details);
        }

        if (currentPet != -1) {
            PetDetails details = this.petDetails.get(currentPet);
            int itemId = (currentPet >> 16) & 0xFFFF;
            Pets pets = Pets.forId(itemId);

            if (details == null) {
                details = new PetDetails(pets.growthRate == 0.0 ? 100.0 : 0.0);
                this.petDetails.put(currentPet, details);
            }

            familiar = new Pet(player, details, itemId, pets.getNpcId(itemId));
        } else if (familiarData.has("familiar")) {
            JsonObject currentFamiliar = familiarData.getAsJsonObject("familiar");
            int familiarId = currentFamiliar.get("originalId").getAsInt();
            familiar = FAMILIARS.get(familiarId).construct(player, familiarId);
            familiar.ticks = currentFamiliar.get("ticks").getAsInt();
            familiar.specialPoints = currentFamiliar.get("specialPoints").getAsInt();

            JsonArray famInv = currentFamiliar.getAsJsonArray("inventory");
            if (famInv != null) {
                ((BurdenBeast) familiar).getContainer().parse(famInv);
            }

            familiar.setAttribute("hp", currentFamiliar.get("lifepoints").getAsInt());
        }
    }

    /**
     * Login.
     */
    public void login() {
        if (hasFamiliar()) {
            familiar.init();
        }
        player.getFamiliarManager().setConfig(243269632);
    }

    /**
     * Summon.
     *
     * @param item       the item
     * @param pet        the pet
     * @param deleteItem the delete item
     */
    public void summon(Item item, boolean pet, boolean deleteItem) {
        boolean renew = false;
        if (hasFamiliar()) {
            if (familiar.getPouchId() == item.getId()) {
                renew = true;
            } else {
                player.getPacketDispatch().sendMessage("You already have a follower.");
                return;
            }
        }
        if (player.getZoneMonitor().isRestricted(ZoneRestriction.FOLLOWERS) && !player.getLocks().isLocked("enable_summoning")) {
            // "Me not coming out in here! Me no want to see you fight mummy!
            player.getPacketDispatch().sendMessages("You are standing in a Summoning-free area. You must move out of this area to", "summon a familiar.");
            return;
        }
        if (pet) {
            summonPet(item, deleteItem);
            return;
        }
        final SummoningPouch pouch = SummoningPouch.get(item.getId());
        if (pouch == null) {
            return;
        }
        if (player.getSkills().getStaticLevel(Skills.SUMMONING) < pouch.getRequiredLevel()) {
            player.getPacketDispatch().sendMessage("You need a Summoning level of " + pouch.getRequiredLevel() + " to summon this familiar.");
            return;
        }
        if (player.getSkills().getLevel(Skills.SUMMONING) < pouch.getSummonCost()) {
            player.getPacketDispatch().sendMessage("You need at least " + pouch.getSummonCost() + " Summoning points to summon this familiar.");
            return;
        }
        final int npcId = pouch.getNpcId();
        Familiar fam = !renew ? FAMILIARS.get(npcId) : familiar;
        if (fam == null) {
            player.getPacketDispatch().sendMessage("Nothing interesting happens.");
            log(this.getClass(), Log.ERR, "Invalid familiar: " + npcId + ".");
            return;
        }
        if (!renew) {
            fam = fam.construct(player, npcId);
            if (fam.getSpawnLocation() == null) {
                player.getPacketDispatch().sendMessage("The spirit in this pouch is too big to summon here. You will need to move to a larger");
                player.getPacketDispatch().sendMessage("area.");
                return;
            }
        }
        if (!player.getInventory().remove(item)) {
            return;
        }
        player.getSkills().updateLevel(Skills.SUMMONING, -pouch.getSummonCost(), 0);
        player.getSkills().addExperience(Skills.SUMMONING, pouch.getSummonExperience());
        if (!renew) {
            familiar = fam;
            spawnFamiliar();
        } else {
            familiar.refreshTimer();
        }
        player.getAppearance().sync();
    }

    /**
     * Summon.
     *
     * @param item the item
     * @param pet  the pet
     */
    public void summon(final Item item, boolean pet) {
        summon(item, pet, true);
    }

    /**
     * Morph pet.
     *
     * @param item       the item
     * @param deleteItem the delete item
     * @param location   the location
     */
    public void morphPet(final Item item, boolean deleteItem, Location location) {
        if (hasFamiliar()) {
            familiar.dismiss();
        }
        summonPet(item, deleteItem, true, location);
    }

    private boolean summonPet(final Item item, boolean deleteItem) {
        return summonPet(item, deleteItem, false, null);
    }

    private boolean summonPet(final Item item, boolean deleteItem, boolean morph, Location location) {
        final int itemId = item.getId();
        int itemIdHash = item.getIdHash();
        if (itemId > 8850 && itemId < 8900) {
            return false;
        }
        Pets pets = Pets.forId(itemId);
        if (pets == null) {
            return false;
        }
        if (player.getSkills().getStaticLevel(Skills.SUMMONING) < pets.summoningLevel) {
            player.getDialogueInterpreter().sendDialogue("You need a Summoning level of " + pets.summoningLevel + " to summon this.");
            return false;
        }

        ArrayList<Integer> taken = new ArrayList<Integer>();
        Container[] searchSpace = {player.getInventory(), player.getBank()};
        for (int checkId = pets.babyItemId; checkId != -1; checkId = pets.getNextStageItemId(checkId)) {
            Item check = new Item(checkId, 1);
            for (Container container : searchSpace) {
                for (Item i : container.getAll(check)) {
                    taken.add(i.getCharge());
                }
            }
        }
        PetDetails details = petDetails.get(itemIdHash);
        int individual = item.getCharge();
        if (details != null) {

            details.setIndividual(individual);
            int count = 0;
            for (int i : taken) {
                if (i == individual) {
                    count++;
                }
            }
            if (count > 1) {

                details = null;
            }
        }
        if (details == null) {
            details = new PetDetails(pets.growthRate == 0.0 ? 100.0 : 0.0);
            for (individual = 0; taken.contains(individual) && individual < 0xFFFF; individual++) {
            }
            details.setIndividual(individual);

            Item newItem = item.copy();
            newItem.setCharge(individual);
            petDetails.put(newItem.getIdHash(), details);
        }
        int npcId = pets.getNpcId(itemId);
        if (npcId > 0) {
            familiar = new Pet(player, details, itemId, npcId);
            if (deleteItem) {
                player.animate(new Animation(827));

                int slot = player.getInventory().getSlotHash(item);

                player.getInventory().remove(item, slot, true);
            }
            if (morph) {
                morphFamiliar(location);
            } else {
                spawnFamiliar();
            }
            return true;
        }
        return true;
    }

    /**
     * Morph familiar.
     *
     * @param location the location
     */
    public void morphFamiliar(Location location) {
        familiar.init(location, false);
        player.getInterfaceManager().openTab(new Component(662));
        player.getInterfaceManager().setViewedTab(7);
    }

    /**
     * Spawn familiar.
     */
    public void spawnFamiliar() {
        familiar.init();
        player.getInterfaceManager().openTab(new Component(662));
        player.getInterfaceManager().setViewedTab(7);
    }

    /**
     * Eat.
     *
     * @param foodId the food id
     * @param npc    the npc
     */
    public void eat(int foodId, Pet npc) {
        if (npc != familiar) {
            player.getPacketDispatch().sendMessage("This isn't your pet!");
            return;
        }
        Pet pet = (Pet) familiar;
        Pets pets = Pets.forId(pet.getItemId());
        if (pets == null) {
            return;
        }
        for (int food : pets.food) {
            if (food == foodId) {
                player.getInventory().remove(new Item(foodId));
                player.getPacketDispatch().sendMessage("Your pet happily eats the " + ItemDefinition.forId(food).getName() + ".");
                player.animate(new Animation(827));
                npc.getDetails().updateHunger(-15.0);
                return;
            }
        }
        player.getPacketDispatch().sendMessage("Nothing interesting happens.");
    }

    /**
     * Pickup.
     */
    public void pickup() {
        if (player.getInventory().freeSlots() == 0) {
            player.getPacketDispatch().sendMessage("You don't have enough room in your inventory.");
            return;
        }
        Pet pet = ((Pet) familiar);
        PetDetails details = pet.getDetails();
        Item petItem = new Item(pet.getItemId());
        petItem.setCharge(details.getIndividual());
        if (player.getInventory().add(petItem)) {
            petDetails.put(pet.getItemIdHash(), details);
            player.animate(Animation.create(827));
            player.getFamiliarManager().dismiss();
        }
    }

    /**
     * Adjust battle state.
     *
     * @param state the state
     */
    public void adjustBattleState(final BattleState state) {
        if (!hasFamiliar()) {
            return;
        }
        familiar.adjustPlayerBattle(state);
    }

    /**
     * Gets boost.
     *
     * @param skill the skill
     * @return the boost
     */
    public int getBoost(int skill) {
        if (!hasFamiliar()) {
            return 0;
        }
        return familiar.getBoost(skill);
    }

    /**
     * Has familiar boolean.
     *
     * @return the boolean
     */
    public boolean hasFamiliar() {
        return familiar != null;
    }

    /**
     * Has pet boolean.
     *
     * @return the boolean
     */
    public boolean hasPet() {
        return hasFamiliar() && familiar instanceof Pet;
    }

    /**
     * Dismiss.
     */
    public void dismiss() {
        if (hasFamiliar()) {
            familiar.dismiss();
        }
    }

    /**
     * Remove details.
     *
     * @param itemIdHash the item id hash
     */
    public void removeDetails(int itemIdHash) {
        petDetails.remove(itemIdHash);
    }

    /**
     * Is owner boolean.
     *
     * @param familiar the familiar
     * @return the boolean
     */
    public boolean isOwner(Familiar familiar) {
        if (!hasFamiliar()) {
            return false;
        }
        if (this.familiar != familiar) {
            //player.getPacketDispatch().sendMessage("This is not your cat.");
            player.getPacketDispatch().sendMessage("This is not your familiar.");
            return false;
        }
        return true;
    }

    /**
     * Sets config.
     *
     * @param value the value
     */
    public void setConfig(int value) {
        int current = getVarp(player, 1160);
        int newVal = current + value;
        setVarp(player, 1160, newVal);
    }

    /**
     * Gets familiar.
     *
     * @return the familiar
     */
    public Familiar getFamiliar() {
        return familiar;
    }

    /**
     * Sets familiar.
     *
     * @param familiar the familiar
     */
    public void setFamiliar(Familiar familiar) {
        this.familiar = familiar;
    }

    /**
     * Gets familiars.
     *
     * @return the familiars
     */
    public static Map<Integer, Familiar> getFamiliars() {
        return FAMILIARS;
    }

    /**
     * Is using summoning boolean.
     *
     * @return the boolean
     */
    public boolean isUsingSummoning() {
        return hasPouch || (hasFamiliar() && !hasPet());
    }

    /**
     * Is has pouch boolean.
     *
     * @return the boolean
     */
    public boolean isHasPouch() {
        return hasPouch;
    }

    /**
     * Sets has pouch.
     *
     * @param hasPouch the has pouch
     */
    public void setHasPouch(boolean hasPouch) {
        this.hasPouch = hasPouch;
    }

    /**
     * Gets summoning combat level.
     *
     * @return the summoning combat level
     */
    public int getSummoningCombatLevel() {
        return summoningCombatLevel;
    }

    /**
     * Sets summoning combat level.
     *
     * @param summoningCombatLevel the summoning combat level
     */
    public void setSummoningCombatLevel(int summoningCombatLevel) {
        this.summoningCombatLevel = summoningCombatLevel;
    }

    /**
     * Gets pet details.
     *
     * @return the pet details
     */
    public Map<Integer, PetDetails> getPetDetails() {
        return petDetails;
    }
}
