package com.osroyale.game.world.items.containers.equipment;

import com.google.common.collect.ImmutableSet;
import com.osroyale.content.emote.Skillcape;
import com.osroyale.content.skillcape.SkillCape;
import com.osroyale.game.Graphic;
import com.osroyale.game.UpdatePriority;
import com.osroyale.game.world.entity.combat.CombatType;
import com.osroyale.game.world.entity.combat.attack.listener.CombatListenerManager;
import com.osroyale.game.world.entity.combat.ranged.RangedAmmunition;
import com.osroyale.game.world.entity.combat.ranged.RangedWeaponDefinition;
import com.osroyale.game.world.entity.combat.ranged.RangedWeaponType;
import com.osroyale.game.world.entity.combat.weapon.WeaponInterface;
import com.osroyale.game.world.entity.mob.UpdateFlag;
import com.osroyale.game.world.entity.mob.player.Player;
import com.osroyale.game.world.items.Item;
import com.osroyale.game.world.items.containers.ItemContainer;
import com.osroyale.game.world.items.containers.ItemContainerAdapter;
import com.osroyale.game.world.items.containers.inventory.Inventory;
import com.osroyale.game.world.items.ground.GroundItem;
import com.osroyale.net.packet.out.SendItemOnInterface;
import com.osroyale.net.packet.out.SendMessage;
import com.osroyale.net.packet.out.SendString;
import com.osroyale.util.Items;
import com.osroyale.util.Utility;

import java.util.Arrays;
import java.util.Optional;
import java.util.stream.IntStream;

import static com.osroyale.game.world.entity.mob.MobAnimation.*;

/**
 * The container that manages the equipment for a player.
 *
 * @author lare96 <http://github.com/lare96>
 */
public final class Equipment extends ItemContainer {

    /** The size of all equipment instances. */
    public static final int SIZE = 14;

    /** The equipment item display widget identifier. */
    private static final int EQUIPMENT_DISPLAY_ID = 1688;

    /** Equipment slot constants. */
    public static final int
            HEAD_SLOT = 0,
            HELM_SLOT = 0,
            CAPE_SLOT = 1,
            AMULET_SLOT = 2,
            WEAPON_SLOT = 3,
            CHEST_SLOT = 4,
            SHIELD_SLOT = 5,

    LEGS_SLOT = 7,
            HANDS_SLOT = 9,
            FEET_SLOT = 10,
            RING_SLOT = 12,
            ARROWS_SLOT = 13;

    /** Equipment bonus constants. */
    public static final int
            STAB_OFFENSE = 0,
            SLASH_OFFENSE = 1,
            CRUSH_OFFENSE = 2,
            MAGIC_OFFENSE = 3,
            RANGED_OFFENSE = 4,
            STAB_DEFENSE = 5,
            SLASH_DEFENSE = 6,
            CRUSH_DEFENCE = 7,
            MAGIC_DEFENSE = 8,
            RANGED_DEFENSE = 9,
            STRENGTH_BONUS = 10,
            RANGED_STRENGTH = 11,
            MAGIC_STRENGTH = 12,
            PRAYER_BONUS = 13;

    /** An array of bonus ids. */
    private static final int[] BONUS_IDS = IntStream.rangeClosed(15130, 15143).toArray();

    /** Item bonus names. */
    private static final String[] BONUS_NAMES = {
     /* 00 */ "Stab",
     /* 01 */ "Slash",
     /* 02 */ "Crush",
     /* 03 */ "Magic",
     /* 04 */ "Range",
     /* - */
     /* 05 */ "Stab",
     /* 06 */ "Slash",
     /* 07 */ "Crush",
     /* 08 */ "Magic",
     /* 09 */ "Range",
     /* - */
     /* 10 */ "Strength",
     /* 11 */ "Ranged Strength",
     /* 12 */ "Magic Strength",
     /* 13 */ "Prayer"
    };

    /**
     * The error message printed when certain functions from the superclass are
     * utilized.
     */
    private static final String EXCEPTION_MESSAGE = "Please use { equipment.set(index, Item) } instead";

    /**
     * An {@link ImmutableSet} containing equipment indexes that don't require
     * appearance updates.
     */
    private static final ImmutableSet<Integer> NO_APPEARANCE = ImmutableSet.of(RING_SLOT, ARROWS_SLOT);

    /** The player who's equipment is being managed. */
    private final Player player;

    private boolean login;

    /** Creates a new {@link Equipment}. */
    public Equipment(Player player) {
        super(SIZE, StackPolicy.STANDARD);
        this.player = player;
        addListener(new EquipmentListener());
    }

    /**
     * Handles refreshing all the equipment items.
     */
    public void login() {
        login = true;
        Arrays.fill(player.getBonuses(), 0);
        for (int index = 0; index < getItems().length; index++) {
            fireItemUpdatedEvent(null, get(index), index, false, true);
        }
        login = false;
        updateWeight();
        refresh();
    }

    /** Handles opening the equipment screen itemcontainer. */
    public void openInterface() {
        player.send(new SendString(Utility.formatDigits(updateWeight()) + " kg", 15145));
        player.send(new SendString("Melee Maxhit: <col=ff7000>" + player.playerAssistant.getMaxHit(player, CombatType.MELEE) + "</col>", 15116));
        player.send(new SendString("Range Maxhit: <col=ff7000>" + player.playerAssistant.getMaxHit(player, CombatType.RANGED) + "</col>", 15117));
        player.send(new SendString(Utility.formatDigits(player.playerAssistant.weight()) + " kg", 15145));
        writeBonuses();
        player.interfaceManager.open(15106);
    }

    /**
     * Adds an item to the equipment container.
     *
     * @param item           The {@link Item} to deposit.
     * @param preferredIndex The preferable index to deposit {@code item} to.
     * @param refresh        The condition if we will be refreshing our
     *                       container.
     */
    @Override
    public boolean add(Item item, int preferredIndex, boolean refresh) {
        return true;
    }

    /**
     * Removes an item from the equipment container.
     *
     * @param item           The {@link Item} to withdraw.
     * @param preferredIndex The preferable index to withdraw {@code item}
     *                       from.
     * @param refresh        The condition if we will be refreshing our
     *                       container.
     */
    @Override
    public boolean remove(Item item, int preferredIndex, boolean refresh) {
        boolean removed = super.remove(item, preferredIndex, refresh);
        if (removed && !contains(item)) {
            this.appearanceForIndex(item.getEquipmentType().getSlot());
        }
        return removed;
    }

    /** @return the current weight value of the player */
    private double updateWeight() {
        double weight = 0;
        for (Item equipment : toArray()) {
            if (equipment == null)
                continue;
            weight += equipment.getWeight();
        }
        for (Item item : player.inventory.toArray()) {
            if (item == null)
                continue;
            weight += item.getWeight();
        }
        return weight;
    }

    /**
     * Manually wears multiple items (does not have any restrictions).
     *
     * @param items The items to wear.
     */
    public void manualWearAll(Item[] items) {
        for (Item item : items) {
            manualWear(item);
        }
    }

    /**
     * Manually wears an item (does not have any restrictions).
     *
     * @param item The item to wear.
     */
    public void manualWear(Item item) {
        if (item == null)
            return;
        if (!item.isEquipable())
            return;
        EquipmentType type = item.getEquipmentType();
        if (type.getSlot() == -1)
            return;
        set(type.getSlot(), item, false);
        appearanceForIndex(type.getSlot());
    }

    public boolean equip(Item item) {
        int index = player.inventory.computeIndexForId(item.getId());
        return equip(index);
    }

    public boolean equip(int inventoryIndex) {
        if (inventoryIndex == -1)
            return false;

        Inventory inventory = player.inventory;
        Item item = inventory.get(inventoryIndex);

        if (!Item.valid(item))
            return false;

        if (!item.isEquipable())
            return false;

        if (!Utility.checkRequirements(player, item.getRequirements(), "to equip this item."))
            return false;

        if (!Skillcape.equip(player, item))
            return false;

        if (item.getId() == 21633)
            player.graphic(new Graphic(1395, true, UpdatePriority.VERY_HIGH));

        EquipmentType type = item.getEquipmentType();
        Item current = get(type.getSlot());
        Item toRemove = null;

        if (current != null && item.isStackable() && isItem(type.getSlot(), item.getId())) {
            int amount = item.getAmount();
            if (Integer.MAX_VALUE - current.getAmount() < amount) {
                amount = Integer.MAX_VALUE - current.getAmount();
            }
            set(type.getSlot(), current.createAndIncrement(amount), true);
            inventory.remove(new Item(item.getId(), amount), inventoryIndex, true);
            return true;
        }

        if (hasWeapon() && type.equals(EquipmentType.SHIELD))
            if (item.isTwoHanded() || getWeapon().isTwoHanded())
                toRemove = getWeapon();

        if (hasShield() && type.equals(EquipmentType.WEAPON))
            if (item.isTwoHanded() || getShield().isTwoHanded())
                toRemove = getShield();


        if (toRemove != null && !inventory.hasCapacityFor(toRemove)) {
            player.send(new SendMessage("You do not have enough space in your inventory."));
            return false;
        }
        inventory.remove(item, inventoryIndex);
        set(type.getSlot(), item, true);
        if (current != null) {
            inventory.add(current, inventoryIndex);
        }
        appearanceForIndex(type.getSlot());

//        if (player.getCombat().isAttacking(player.getCombat().getDefender()) && !player.getCombat().checkWithin(player, player.getCombat().getDefender(), player.getStrategy())) {
//            Mob defender = player.getCombat().getDefender();
//            player.getCombat().reset();
//            System.out.println("ka");
//            player.movement.dijkstraPath(defender);
//        } else {
        player.getCombat().reset();
//        }

        if (toRemove != null) {
            int slot = toRemove.getEquipmentType().getSlot();
            set(slot, null, true);
            appearanceForIndex(slot);
            inventory.add(toRemove, inventoryIndex, true);
        }

        if (player.interfaceManager.isInterfaceOpen(15106)) {
            openInterface();
        }

        return true;
    }
    /**
     * Unequips an {@link Item} from the underlying player's {@code Equipment}.
     *
     * @param equipmentIndex The {@code Equipment} index to unequip the {@code
     *                       Item} from.
     * @return {@code true} if the item was unequipped, {@code false} otherwise.
     */
    public boolean unequip(int equipmentIndex) {
        return unequip(equipmentIndex, -1, player.inventory);
    }

    /**
     * Unequips an {@link Item} from the underlying player's {@code Equipment}.
     *
     * @param equipmentIndex The {@code Equipment} index to unequip the {@code
     *                       Item} from.
     * @param preferredIndex The preferred inventory slot.
     * @param container      The container to which we are putting the items
     *                       on.
     * @return {@code true} if the item was unequipped, {@code false} otherwise.
     */
    private boolean unequip(int equipmentIndex, int preferredIndex, ItemContainer container) {
        if (equipmentIndex == -1)
            return false;

        Item unequip = get(equipmentIndex);
        if (unequip == null)
            return false;

        if (!container.add(unequip, preferredIndex, true)) {
            return false;
        }

        set(equipmentIndex, null, true);
        appearanceForIndex(equipmentIndex);

//        if (player.getCombat().isAttacking(player.getCombat().getDefender())
//                && !player.getCombat().checkWithin(player, player.getCombat().getDefender(), player.getStrategy())) {
//            Mob defender = player.getCombat().getDefender();
//            player.getCombat().reset();
//            player.movement.dijkstraPath(defender);
//        } else {
        player.getCombat().reset();
//        }

        if (!player.interfaceManager.isClear() && !player.interfaceManager.isInterfaceOpen(15106)) {
            player.interfaceManager.close(false);
        }

        if (player.interfaceManager.isInterfaceOpen(15106)) {
            openInterface();
        }

        return true;
    }

    /**
     * Flags the {@code APPEARANCE} update block, only if the equipment piece on
     * {@code equipmentIndex} requires an appearance update.
     */
    private void appearanceForIndex(int equipmentIndex) {
        if (!NO_APPEARANCE.contains(equipmentIndex)) {
            player.updateFlags.add(UpdateFlag.APPEARANCE);
        }
    }

    private void addBonus(Item item) {
        for (int index = 0; index < item.getBonuses().length; index++) {
            player.appendBonus(index, item.getBonus(index));
        }
    }

    private void removeBonus(Item item) {
        for (int index = 0; index < item.getBonuses().length; index++) {
            player.appendBonus(index, -item.getBonus(index));
        }
    }

    /** Writes a specific the bonus value on the equipment itemcontainer. */
    private void writeBonuses() {
        for (int i = 0; i < player.getBonuses().length; i++) {
            String bonus = BONUS_NAMES[i] + ": ";

            if (player.getBonus(i) >= 0)
                bonus += "+";

            bonus += player.getBonus(i);

            if (i == 12)
                bonus += "%";

            player.send(new SendString(bonus, BONUS_IDS[i]));
        }
    }

    public boolean hasHead() {
        return get(HEAD_SLOT) != null;
    }

    public boolean hasAmulet() {
        return get(AMULET_SLOT) != null;
    }

    public boolean hasAmmo() {
        return get(ARROWS_SLOT) != null;
    }

    public boolean hasChest() {
        return get(CHEST_SLOT) != null;
    }

    public boolean hasLegs() {
        return get(LEGS_SLOT) != null;
    }

    public boolean hasHands() {
        return get(HANDS_SLOT) != null;
    }

    public boolean hasFeet() {
        return get(FEET_SLOT) != null;
    }

    public boolean hasRing() {
        return get(RING_SLOT) != null;
    }

    public Item getAmuletSlot() {
        return get(AMULET_SLOT);
    }

    public boolean hasWeapon() {
        return get(WEAPON_SLOT) != null;
    }

    public boolean hasCape() {
        return get(CAPE_SLOT) != null;
    }

    public Item getWeapon() {
        return get(WEAPON_SLOT);
    }

    public Item getCape() {
        return get(CAPE_SLOT);
    }

    public boolean hasShield() {
        return get(SHIELD_SLOT) != null;
    }

    public Item getShield() {
        return get(SHIELD_SLOT);
    }

    public Item[] getEquipment() {
        Item[] equipment = new Item[15];
        equipment[1] = player.equipment.get(Equipment.HELM_SLOT);
        equipment[3] = player.equipment.get(Equipment.CAPE_SLOT);
        equipment[4] = player.equipment.get(Equipment.AMULET_SLOT);
        equipment[5] = player.equipment.get(Equipment.ARROWS_SLOT);
        equipment[6] = player.equipment.get(Equipment.WEAPON_SLOT);
        equipment[7] = player.equipment.get(Equipment.CHEST_SLOT);
        equipment[8] = player.equipment.get(Equipment.SHIELD_SLOT);
        equipment[10] = player.equipment.get(Equipment.LEGS_SLOT);
        equipment[12] = player.equipment.get(Equipment.HANDS_SLOT);
        equipment[13] = player.equipment.get(Equipment.FEET_SLOT);
        equipment[14] = player.equipment.get(Equipment.RING_SLOT);
        return equipment;
    }


    /**
     * Forces a refresh of {@code Equipment} items to the {@code
     * EQUIPMENT_DISPLAY_ID} widget.
     */
    public void refresh() {
        refresh(player, EQUIPMENT_DISPLAY_ID);
    }

    /**
     * Forces a refresh of {@code Equipment} items to the {@code
     * EQUIPMENT_DISPLAY_ID} widget.
     */
    @Override
    public void refresh(Player player, int widget) {
        player.send(new SendItemOnInterface(widget, toArray()));
    }

    @Override
    public void clear() {
        super.clear();
        Arrays.fill(player.getBonuses(), 0);
    }

    private boolean isItem(int slot, int itemId) {
        Item item = get(slot);
        return item != null && item.getId() == itemId;
    }

    public void unEquip(Item item) {
        if (item == null) {
            return;
        }
        for (Item equip : getItems()) {
            if (equip != null && equip.getId() == item.getId()) {
                EquipmentType type = item.getEquipmentType();
                set(type.getSlot(), null, true);
                appearanceForIndex(type.getSlot());
                if (!player.inventory.add(equip))
                    GroundItem.create(player, equip);
            }
        }
    }

    private void onEquip(Item item) {
        EquipmentType type = item.getEquipmentType();
        double boostedExperience = 0.2;
        for (int index = 0; index < LUMBERJACK_PIECES.length; index++) {
            if (contains(LUMBERJACK_PIECES[index][0])) {
                boostedExperience *= SKILLING_SETS_EXPERIENCE_BOOST_PER_PIECE;
            }
        }

        if (type == EquipmentType.SHIELD || type == EquipmentType.WEAPON) {
            updateRangedEquipment();

            if (item.matchesId(12926) && player.blowpipeDarts != null) {
                addBonus(player.blowpipeDarts);
                retrieve(ARROWS_SLOT).ifPresent(this::removeBonus);
                return;
            }

            item.getRangedDefinition()
                    .filter(def -> def.getType().equals(RangedWeaponType.THROWN))
                    .ifPresent(def -> retrieve(ARROWS_SLOT).ifPresent(this::removeBonus));
        } else if (type == EquipmentType.ARROWS) {
            updateRangedEquipment();

            if (!hasWeapon())
                return;

            if (getWeapon().matchesId(12_926) && !login) {
                removeBonus(item);
                return;
            }

            getWeapon().getRangedDefinition()
                    .filter(def -> def.getType().equals(RangedWeaponType.THROWN))
                    .ifPresent(def -> removeBonus(item));
        }
    }

    private void onRemove(Item item) {
        EquipmentType type = item.getEquipmentType();

        if (type == EquipmentType.SHIELD || type == EquipmentType.WEAPON) {
            boolean isBlowpipe = item.matchesId(12_926);

            if (isBlowpipe && player.blowpipeDarts != null) {
                removeBonus(player.blowpipeDarts);
            }

            if (isBlowpipe || item.getRangedDefinition()
                    .filter(def -> def.getType().equals(RangedWeaponType.THROWN))
                    .isPresent()) {
                retrieve(ARROWS_SLOT).ifPresent(this::addBonus);
            }
            updateRangedEquipment();
        } else if (type == EquipmentType.ARROWS) {
            if (!hasWeapon())
                return;

            boolean isBlowpipe = getWeapon().matchesId(12_926);

            if (isBlowpipe || getWeapon().getRangedDefinition()
                    .filter(def -> def.getType().equals(RangedWeaponType.THROWN))
                    .isPresent()) {
                addBonus(item);
            }

            updateRangedEquipment();
        }
    }

    public void updateRangedEquipment() {
        if (!hasWeapon() || !getWeapon().getRangedDefinition().isPresent()) {
            if (hasWeapon() && getWeapon().matchesId(12_926) && player.blowpipeDarts != null) {
                player.rangedAmmo = RangedAmmunition.find(getWeapon(), player.blowpipeDarts);
            } else {
                player.rangedAmmo =
                        retrieve(ARROWS_SLOT)
                                .map(arrow -> RangedAmmunition.find(getWeapon(), arrow))
                                .orElse(null);
            }
            player.rangedDefinition = null;
            return;
        }

        RangedWeaponDefinition def = getWeapon().getRangedDefinition().get();
        player.rangedDefinition = def;

        switch (def.getType()) {

            case SHOT:
                player.rangedAmmo =
                        retrieve(ARROWS_SLOT)
                                .map(arrow -> RangedAmmunition.find(getWeapon(), arrow))
                                .orElse(null);
                break;

            case THROWN:
                player.rangedAmmo = RangedAmmunition.find(getWeapon(), getWeapon());
                break;
        }
    }

    /**
     * Updates the weapon animation.
     */
    public void updateAnimation() {
        int stand = PLAYER_STAND;
        int walk = PLAYER_WALK;
        int run = PLAYER_RUN;

        if (hasWeapon()) {
            Item weapon = getWeapon();
            stand = weapon.getStandAnimation();
            walk = weapon.getWalkAnimation();
            run = weapon.getRunAnimation();
        }

        if (player.overrides.hasOverride(Equipment.WEAPON_SLOT)) {
            Item weapon = player.overrides.get(Equipment.WEAPON_SLOT);
            stand = weapon.getStandAnimation();
            walk = weapon.getWalkAnimation();
            run = weapon.getRunAnimation();
        }

        player.mobAnimation.setStand(stand);
        player.mobAnimation.setWalk(walk);
        player.mobAnimation.setRun(run);
    }

    public static boolean isWearingDFS(Player player) {
        if (!player.equipment.hasShield()) {
            return false;
        }

        Item shield = player.equipment.getShield();

        return shield.getId() == 11283 || shield.getId() == 11284 ||
                shield.getId() == 21633 || shield.getId() == 21634 ||
                shield.getId() == 22002 || shield.getId() == 22003;
    }

    public static boolean hasAttractor(Player player) {
        Item cape = player.equipment.getCape();
        return cape != null && (cape.matchesId(10498) || cape.matchesId(13337) || cape.matchesId(27363) || cape.matchesId(27365 ) || SkillCape.isEquipped(player, SkillCape.RANGED));
    }

    public static boolean hasAccumulator(Player player) {
        Item cape = player.equipment.getCape();
        return cape != null && (cape.matchesId(10499) || cape.matchesId(13337) || cape.matchesId(27363) || cape.matchesId(27365 ) || SkillCape.isEquipped(player, SkillCape.RANGED));
    }

    public static boolean hasAssembler(Player player) {
        Item cape = player.equipment.getCape();
        return cape != null && (cape.matchesId(22109) || cape.matchesId(21898));
    }

    public static final int[][] LUMBERJACK_PIECES =
            {
                    {10933, Equipment.FEET_SLOT},
                    {10939, Equipment.CHEST_SLOT},
                    {10940, Equipment.LEGS_SLOT},
                    {10941, Equipment.HEAD_SLOT},
            };
    public static final int [][] SHAYZIEN_PIECES =
            {
                    {Items.SHAYZIEN_GLOVES_5_, Equipment.HANDS_SLOT},
                    {Items.SHAYZIEN_BOOTS_5_, Equipment.FEET_SLOT},
                    {Items.SHAYZIEN_HELM_5_, Equipment.HEAD_SLOT},
                    {Items.SHAYZIEN_GREAVES_5_, Equipment.LEGS_SLOT},
                    {Items.SHAYZIEN_PLATEBODY_5_, Equipment.CHEST_SLOT},
            };

    /**
     * Fishing.
     */
    public static final int[][] ANGLER_PIECES =
            {
                    {13258, Equipment.HEAD_SLOT},
                    {13259, Equipment.CHEST_SLOT},
                    {13260, Equipment.LEGS_SLOT},
                    {13261, Equipment.FEET_SLOT},
            };

    /**
     * Mining.
     */
    public static final int[][] PROSPECTOR_PIECES =
            {
                    {12013, Equipment.HEAD_SLOT},
                    {12014, Equipment.CHEST_SLOT},
                    {12015, Equipment.LEGS_SLOT},
                    {12016, Equipment.FEET_SLOT},
            };

    public final static double SKILLING_SETS_EXPERIENCE_BOOST_PER_PIECE = 1.02;

    private static final int[] ZAMORAK_ITEMS = {1033, 1035, 2414, 2417, 2653, 2655, 2657, 2659, 3478, 4039, 6764, 10368, 10370, 10372, 10374, 10444, 10450, 10456, 10460, 10468, 10474, 10776, 10786, 10790, 11808, 11824, 11889, 11892, 12638, 13333, 13334, 19936, 20374, 21780, 21782, 21795, 1724, 3842, 20223, 11791, 12904};
    private static final int[] SARADOMIN_ITEMS = {2412, 2415, 2661, 2663, 2665, 2667, 3479, 4037, 6762, 10384, 10386, 10388, 10390, 10440, 10446, 10452, 10458, 10464, 10470, 10778, 10784, 10792, 11806, 11838, 11891, 12637, 12809, 13331, 13332, 19933, 20372, 21776, 21778, 21791, 3840, 12598, 20220, 19997};
    private static final int[] BANDOS_ITEMS = {11804, 11832, 11834, 11836, 12265, 12267, 12269, 12271, 12273, 12275, 12480, 12482, 12484, 12486, 12488, 12498, 12500, 12502, 12504, 19924, 20370, 20782, 20232, 11061, 12608, 21733};
    private static final int[] ARMADYL_ITEMS = {84, 87, 11785, 11802, 11826, 11830, 12253, 12255, 12257, 12259, 12261, 12263, 12470, 12472, 12474, 12476, 12478, 12506, 12508, 12511, 12512, 19930, 20368, 20229, 12610};
    public static final int [] SHAYZIEN_ITEMS = {13377, 13378, 13379, 13380, 13381};
    public static final int [] PICKAXES = {1265, 1267, 1269, 1273, 1271, 1275, 13243, 20014, 11920, 12797};

    public boolean hasShayzien() {
        return containsAny(SHAYZIEN_ITEMS);
    }
    public boolean hasArmadyl() {
        return containsAny(ARMADYL_ITEMS);
    }

    public boolean hasBandos() {
        return containsAny(BANDOS_ITEMS);
    }

    public boolean hasSaradomin() {
        return containsAny(SARADOMIN_ITEMS);
    }

    public boolean hasZamorak() {
        return containsAny(ZAMORAK_ITEMS);
    }

    public boolean hasRow() {
        return containsAny(2572, 12785);
    }

    public boolean contains(int[] bowsWithNoArrowsRequired) {
        return containsAny(22550, 25865, 25867, 25884, 25886, 25890, 25892, 25894, 25896, 25888);
    }

    /**  An {@link ItemContainerAdapter} implementation that listens for changes to equipment. */
    private final class EquipmentListener extends ItemContainerAdapter {

        /** Creates a new {@link EquipmentListener}. */
        EquipmentListener() {
            super(player);
        }

        @Override
        public int getWidgetId() {
            return EQUIPMENT_DISPLAY_ID;
        }

        @Override
        public String getCapacityExceededMsg() {
            throw new IllegalStateException(EXCEPTION_MESSAGE);
        }

        @Override
        public void itemUpdated(ItemContainer container, Optional<Item> oldItem, Optional<Item> newItem, int index, boolean refresh, boolean login) {
            if (oldItem.equals(newItem))
                return;

            boolean weapon =
                    oldItem.filter(item -> item.getWeaponInterface() != null)
                            .orElse(newItem.filter(item -> item.getWeaponInterface() != null)
                                    .orElse(null)) != null;

            oldItem.ifPresent(item -> {
                removeBonus(item);
                onRemove(item);
                CombatListenerManager.removeListener(player, item.getId());
            });

            newItem.ifPresent(item -> {
                addBonus(item);
                onEquip(item);
                CombatListenerManager.addListener(player, item.getId());
            });

            if (weapon && !login)
                WeaponInterface.execute(player, getWeapon());

            if (refresh)
                sendItemsToWidget(container);
        }

        @Override
        public void bulkItemsUpdated(ItemContainer container) {
            sendItemsToWidget(container);
        }
    }
}