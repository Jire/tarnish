package com.osroyale.util.parser.old.defs;

import com.osroyale.Config;

/**
 * Represents all of an in-game Item's attributes.
 *
 * @author Daniel | Obey
 */
public class ItemDefinition {

    /** An array of item definitions. */
    public static final ItemDefinition[] DEFINITIONS = new ItemDefinition[Config.ITEM_DEFINITION_LIMIT];

    /** The item identification definition. */
    private int id;

    /** The item name definition. */
    private String name;

    /** The gameMembers item state. */
    private boolean members;

    /** The tradable item state. */
    private boolean tradeable;

    /** The stackable item state definition. */
    private boolean stackable;

    /** The droppable item state definition. */
    private boolean droppable;

    /** The item notability state definition. */
    private boolean noteable;

    /** The noted item state definition. */
    private boolean noted;

    /** The noted item identification definition. */
    private int notedId;

    /** The item street value definition. */
    private int street_value;

    /** The item base value definition. */
    private int base_value;

    /** The high alchemy value definition. */
    private int highAlch;

    /** The low alchemy value definition. */
    private int lowAlch;

    /** The item examine definition. */
    private String examine;

    /** The item destroy message definition. */
    private String destroyMessage;

    /** The item's weight definition. */
    private double weight;

    public ItemDefinition(int id, String name, boolean members, boolean tradeable, boolean stackable, boolean droppable, boolean noteable, boolean noted, int notedId, int street_value, int base_value, int highAlch, int lowAlch, String examine, String destroyMessage, double weight) {
        this.id = id;
        this.name = name;
        this.members = members;
        this.tradeable = tradeable;
        this.stackable = stackable;
        this.droppable = droppable;
        this.noteable = noteable;
        this.noted = noted;
        this.notedId = notedId;
        this.street_value = street_value;
        this.base_value = base_value;
        this.highAlch = highAlch;
        this.lowAlch = lowAlch;
        this.examine = examine;
        this.destroyMessage = destroyMessage;
        this.weight = weight;
    }

    /**
     * Gets an item definition.
     *
     * @param id The definition's item id.
     * @return The item definition for the item id, or null if the item id is
     * out of bounds.
     */
    public static final ItemDefinition get(int id) {
        if (id < 0 || id >= DEFINITIONS.length) {
            return null;
        }

        return DEFINITIONS[id];
    }

    /**
     * Gets the item id.
     *
     * @return The item id.
     */
    public int getId() {
        return id;
    }

    /**
     * Gets the item name.
     *
     * @return The item name.
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the item examine.
     *
     * @return The item examine.
     */
    public String getExamine() {
        return examine;
    }

    /**
     * Gets the item destroy message.
     *
     * @return The item destroy message.
     */
    public String getDestroyMessage() {
        return destroyMessage;
    }

    /**
     * Gets the item note state.
     *
     * @return {@code True} if the item is noted;
     */
    public boolean isNoted() {
        return noted;
    }

    /**
     * Gets the item notability state.
     *
     * @return {@code} if the item can be turned into a note.
     */
    public boolean isNoteable() {
        return noteable;
    }

    /**
     * Gets the parent id.
     *
     * @return The unnoted id of this item if this item is noted, or the
     * original item id if it is already unnoted.
     */
    public int getParentId() {
        if (!noteable) {
            return id;
        }
        return noted ? notedId : get(notedId).notedId;
    }

    /**
     * Gets the item note id.
     *
     * @return The item note id, or the original item id if it cannot be noted.
     */
    public int getNotedId() {
        if (notedId == -1) {
            return id;
        }
        return notedId;
    }

    /**
     * Gets the item stackability state.
     *
     * @return {@code True} if the item can be stacked.
     */
    public boolean isStackable() {
        return stackable;
    }

    /**
     * Gets the item destroyability state.
     *
     * @return {@code True} if the item can be destroyed.
     */
    public boolean isDroppable() {
        return droppable;
    }

    /**
     * Gets the item tradability state.
     *
     * @return {@code True} if the item is tradable.
     */
    public boolean isTradeable() {
        return tradeable;
    }

    /**
     * Gets the gameMembers item state.
     *
     * @return {@code True} if this item is a gameMembers item.
     */
    public boolean isMembers() {
        return members;
    }

    /**
     * Gets the item value.
     *
     * @return The value.
     */
    public int getStreetValue() {
        return street_value;
    }

    /**
     * Gets the item value.
     *
     * @return The value.
     */
    public int getBaseValue() {
        return base_value;
    }

    /**
     * Gets the item value.
     *
     * @return The value.
     */
    public int getValue() {
        return getStreetValue();
    }

    /**
     * Gets the high alchemy item value.
     *
     * @return The value.
     */
    public int getHighAlch() {
        return highAlch;
    }

    /**
     * Gets the low alchemy item value.
     *
     * @return The value.
     */
    public int getLowAlch() {
        return lowAlch;
    }

    /**
     * Gets the item's weight.
     *
     * @return The item weight.
     */
    public double getWeight() {
        return weight;
    }

}
