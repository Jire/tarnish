package com.osroyale.content.prestige;

import com.google.common.collect.ImmutableMap;

import java.util.HashMap;
import java.util.Map;

/**
 * Handles the perk rewards from prestiging.
 *
 * @author Daniel.
 */
public enum PrestigePerk {
    ARROWHEAD("Arrowhead", "Using arrows will not break.", 6798),
    MASTERBAIRTER("Masterbaiter", "15% chance of catching extra fish.", 6799),
    DOUBLE_WOOD("Double wood", "15% chance to receive an additional log.", 6800),
    LITTLE_BIRDY("Little Birdy", "Increase the woodcutting rate of bird nest drops by 15%.", 6801),
    THE_ROCK("The Rock", "10% chance to receive an additional ore.", 6802),
    FLAME_ON("Flame On", "25% chance of burning an extra log.", 6803);

    public static final PrestigePerk[] values = values();
    private static final Map<Integer, PrestigePerk> perkItemMap;
    private static final Map<Integer, PrestigePerk> perkIdMap;

    static {
        final Map<Integer, PrestigePerk> itemMap = new HashMap<>();
        final Map<Integer, PrestigePerk> idMap = new HashMap<>();
        for (PrestigePerk p : values) {
            itemMap.put(p.item, p);
            idMap.put(p.ordinal(), p);
        }

        perkItemMap = ImmutableMap.copyOf(itemMap);
        perkIdMap = ImmutableMap.copyOf(idMap);
    }

    /**
     * The name of the perk.
     */
    public final String name;

    /**
     * The description of the perk.
     */
    public final String description;

    /**
     * The item identification of the perk.
     */
    public final int item;

    /**
     * Constructs a new <code>PrestigePerk</code>.
     */
    PrestigePerk(String name, String description, int item) {
        this.name = name;
        this.description = description;
        this.item = item;
    }

    public static PrestigePerk forItem(int item) {
        return perkItemMap.get(item);
    }

    public static PrestigePerk forId(int id) {
        return perkIdMap.get(id);
    }
}
