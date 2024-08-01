package com.osroyale.game.world.items;

import com.google.common.collect.Iterables;
import com.osroyale.game.world.entity.combat.attack.FightType;
import com.osroyale.game.world.entity.combat.ranged.RangedWeaponDefinition;
import com.osroyale.game.world.entity.combat.weapon.WeaponInterface;
import com.osroyale.game.world.items.containers.equipment.EquipmentType;
import com.osroyale.game.world.items.containers.pricechecker.PriceType;
import com.osroyale.util.Utility;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;

/**
 * The container class that represents an item that can be interacted with.
 *
 * @author lare96 <http://github.com/lare96>
 */
public class Item implements Cloneable {

    /**
     * The identification of this item.
     */
    private int id;

    /**
     * The quantity of this item.
     */
    private int amount;

    /**
     * Creates a new {@link Item}.
     */
    public Item(int id, int amount) {
        if (amount < 0) amount = 0;
        this.id = id;
        this.amount = amount;
    }

    /**
     * Creates a new {@link Item} with a random amount between {@code minAmt} and {@code maxAmt}.
     */
    public Item(int id, int minAmt, int maxAmt) {
        if (minAmt < 0 || maxAmt < 0) amount = 0;
        this.id = id;
        this.amount = Utility.random(minAmt, maxAmt, true);
    }

    public Item(int id, long amount) {
        int amount2;
        if (amount < 0) {
            amount2 = 0;
        } else if (amount > Integer.MAX_VALUE) {
            amount2 = Integer.MAX_VALUE;
        } else {
            amount2 = (int) amount;
        }
        this.id = id;
        this.amount = amount2;
    }

    public Item clone() {
        try {
            return (Item) super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return this;
    }

    /**
     * Gets the unnoted item.
     *
     * @return the unnoted item.
     */
    public Item unnoted() {
        return new Item(getDefinition().getUnnotedId(), amount);
    }

    /**
     * Gets the item note item.
     *
     * @return The item note id, or the original item item if it cannot be
     * noted.
     */
    public Item noted() {
        return new Item(getDefinition().getNotedId(), amount);
    }

    /**
     * Gets the value for this item.
     *
     * @param type the type to derive the value from.
     * @return the value of this item.
     */
    public int getValue(PriceType type) {
        ItemDefinition def = getDefinition();
        if (def == null) {
            return 0;
        }

        switch (type) {
            case VALUE:
                return def.getValue();
            case HIGH_ALCH_VALUE:
                return def.getHighAlch();
            case LOW_ALCH_VALUE:
                return def.getLowAlch();
        }

        return 0;
    }

    public int getSellValue() {
        return (int) Math.floor(getValue() / 2);
    }

    /**
     * Gets the value for this item.
     *
     * @return the value of this item.
     */
    public int getValue() {
        ItemDefinition def = getDefinition();
        if (def == null) {
            return 0;
        }
        return def.getValue();
    }

    /**
     * Creates a new item with {@code newId} and the same amount as this
     * instance. The returned {@code Item} <strong>does not</strong> hold any
     * references to this one unless {@code id == newId}. It will throw an
     * exception on an invalid id.
     *
     * @param newId The new id to set.
     * @return The newly id set {@code Item}.
     */
    public Item createWithId(int newId) {
        if (id == newId) {
            return this;
        }
        return new Item(newId, amount);
    }

    /**
     * Creates a new item with {@code newAmount} and the same identifier as this
     * instance.  The returned {@code Item} <strong>does not</strong> hold any
     * references to this one unless {@code amount == newAmount}. It will throw
     * an exception on overflows and negative values.
     *
     * @param newAmount The new amount to set.
     * @return The newly amount set {@code Item}.
     */
    public Item createWithAmount(int newAmount) {
        if (amount == newAmount) {
            return this;
        }
        return new Item(id, newAmount);
    }

    /**
     * Creates a new item with {@code amount + addAmount} and the same
     * identifier. The returned {@code Item} <strong>does not</strong> hold any
     * references to this one. It will also have a maximum amount of {@code
     * Integer.MAX_VALUE}.
     *
     * @param addAmount The amount to deposit.
     * @return The newly incremented {@code Item}.
     */
    public Item createAndIncrement(int addAmount) {
        if (addAmount < 0) { // Same effect as decrementing.
            return createAndDecrement(Math.abs(addAmount));
        }

        int newAmount = amount + addAmount;

        if (newAmount < amount) { // An overflow.
            newAmount = Integer.MAX_VALUE;
        }

        Item item = clone();
        item.setAmount(newAmount);
        return item;
    }

    /**
     * Creates a new item with {@code amount - removeAmount} and the same
     * identifier. The returned {@code Item} <strong>does not</strong> hold any
     * references to this one. It will also have a minimum amount of {@code 1}.
     *
     * @param removeAmount The amount to withdraw.
     * @return The newly incremented {@code Item}.
     */
    public Item createAndDecrement(int removeAmount) {
        if (removeAmount < 0) { // Same effect as incrementing.
            return createAndIncrement(-removeAmount);
        }

        int newAmount = amount - removeAmount;

        // Value too low, or an overflow.
        if (newAmount < 1 || newAmount > amount) {
            newAmount = 1;
        }

        Item clone = clone();
        clone.setAmount(newAmount);
        return clone;
    }

    /**
     * Creates a new {@link Item} with an quantity of {@code 1}.
     *
     * @param id the identification of this item.
     */
    public Item(int id) {
        this(id, 1);
    }

    /**
     * Converts an {@link Item} array into an Integer array.
     *
     * @param ids the array to convert into an Integer array.
     * @return the Integer array containing the values from the item array.
     */
    public static final int[] convert(Item... ids) {
        List<Integer> values = new ArrayList<>();
        for (Item identifier : ids) {
            values.add(identifier.getId());
        }
        return values.stream().mapToInt(Integer::intValue).toArray();
    }

    /**
     * Converts an int array into an {@link Item} array.
     *
     * @param id the array to convert into an item array.
     * @return the item array containing the values from the int array.
     */
    public static final Item[] convert(int... id) {
        List<Item> items = new ArrayList<>();
        for (int identifier : id) {
            items.add(new Item(identifier));
        }
        return Iterables.toArray(items, Item.class);
    }

    /**
     * Determines if {@code item} is valid. In other words, determines if
     * {@code item} is not {@code null} and the {@link Item#id}.
     *
     * @param item the item to determine if valid.
     * @return {@code true} if the item is valid, {@code false} otherwise.
     */
    public static boolean valid(Item item) {
        return item != null && item.id > 0 && item.id < ItemDefinition.DEFINITIONS.length && item.getDefinition() != null;
    }

    /**
     * A substitute for {@link Object#clone()} that creates another 'copy' of
     * this instance. The created copy <i>safe</i> meaning it does not hold
     * <b>any</b> references to the original instance.
     *
     * @return the copy of this instance that does not hold any references.
     */
    public Item copy() {
        return new Item(id, amount);
    }

    /**
     * Increments the amount by {@code 1}.
     */
    public final void incrementAmount() {
        incrementAmountBy(1);
    }

    /**
     * Decrements the amount by {@code 1}.
     */
    public final void decrementAmount() {
        decrementAmountBy(1);
    }

    /**
     * Increments the amount by {@code amount}.
     *
     * @param amount the amount to increment by.
     */
    public final void incrementAmountBy(int amount) {
        this.amount += amount;
    }

    /**
     * Decrements the amount by {@code amount}
     *
     * @param amount the amount to decrement by.
     */
    public final void decrementAmountBy(int amount) {
        if ((this.amount - amount) < 1) {
            this.amount = 0;
        } else {
            this.amount -= amount;
        }
    }

    /**
     * Gets the item definition for the item identifier.
     *
     * @return the item definition.
     */
    public ItemDefinition getDefinition() {
        return ItemDefinition.get(id);
    }

    /**
     * Gets the identification of this item.
     *
     * @return the identification.
     */
    public final int getId() {
        return id;
    }

    /**
     * Sets the identification of this item.
     *
     * @param id the new identification of this item.
     */
    public final void setId(int id) {
        this.id = id;
    }

    /**
     * Gets the quantity of this item.
     *
     * @return the quantity.
     */
    public final int getAmount() {
        return amount;
    }

    /**
     * Sets the quantity of this item.
     *
     * @param amount the new quantity of this item.
     */
    public final void setAmount(int amount) {
        if (amount < 0) amount = 0;
        this.amount = amount;
    }

    public String getName() {
        return getDefinition().getName();
    }

    public String getDestroyMessage() {
        return getDefinition().getDestroyMessage();
    }

    public boolean isStackable() {
        return getDefinition().isStackable();
    }

    public boolean isNoteable() {
        return getDefinition().isNoteable();
    }

    public boolean isNoted() {
        return getDefinition().isNoted();
    }

    public boolean isEquipable() {
        return getDefinition().getEquipmentType() != EquipmentType.NOT_WIELDABLE || getDefinition().isEquipable();
    }

    public boolean isTwoHanded() {
        return getDefinition().isTwoHanded();
    }

    public boolean isTradeable() {
        return getDefinition().isTradeable();
    }

    public boolean isDestroyable() {
        return getDefinition().isDestroyable();
    }

    public int getStandAnimation() {
        return getDefinition().getStandAnimation();
    }

    public int getWalkAnimation() {
        return getDefinition().getWalkAnimation();
    }

    public int getRunAnimation() {
        return getDefinition().getRunAnimation();
    }


    public OptionalInt getAttackAnimation(FightType type) {
        return getDefinition().getAttackAnimation(type);
    }

    public OptionalInt getBlockAnimation() {
        return getDefinition().getBlockAnimation();
    }

    public int getNotedId() {
        return getDefinition().getNotedId();
    }

    public int getUnnotedId() {
        return getDefinition().getUnnotedId();
    }

    public int getStreetValue() {
        return getDefinition().getStreetValue();
    }

    public int getBaseValue() {
        return getDefinition().getBaseValue();
    }

    public int getHighAlch() {
        return getDefinition().getHighAlch();
    }

    public int getLowAlch() {
        return getDefinition().getLowAlch();
    }

    public double getWeight() {
        return getDefinition().getWeight();
    }

    public EquipmentType getEquipmentType() {
        return getDefinition().getEquipmentType();
    }

    public Optional<RangedWeaponDefinition> getRangedDefinition() {
        return getDefinition().getRangedDefinition();
    }

    public int[] getRequirements() {
        return getDefinition().getRequirements();
    }

    public int[] getBonuses() {
        return getDefinition().getBonuses();
    }

    public int getBonus(int index) {
        return getDefinition().getBonuses()[index];
    }

    public boolean equalIds(Item other) {
        return other != null && id == other.id;
    }

    public boolean matchesId(int id) {
        return this.id == id;
    }

    @Override
    public final String toString() {
        return String.format("item[id=%d amount=%d]", id, amount);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Item) {
            Item other = (Item) obj;
            return other.id == id && other.amount == amount;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return amount << 16 | id & 0xFFFF;
    }

    public WeaponInterface getWeaponInterface() {
        return getDefinition().getWeaponInterface();
    }
}