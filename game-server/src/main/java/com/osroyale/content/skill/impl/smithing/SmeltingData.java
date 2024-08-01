package com.osroyale.content.skill.impl.smithing;

/**
 * Created by Daniel on 2017-12-31.
 */

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import com.osroyale.game.world.items.Item;

import java.util.EnumSet;
import java.util.Optional;

/** The enumerated type whose elements represent definitions for each smeltable bar. */
public enum SmeltingData {
    BRONZE(3987, new Item[]{new Item(438), new Item(436)}, new Item[]{new Item(2349)}, 6.25, 1),
    BRONZE_5(3986, BRONZE, 5),
    BRONZE_10(2807, BRONZE, 10),
    BRONZE_X(2414, BRONZE, -1),
    IRON(3991, new Item[]{new Item(440)}, new Item[]{new Item(2351)}, 12.5, 15),
    IRON_5(3990, IRON, 5),
    IRON_10(3989, IRON, 10),
    IRON_X(3988, IRON, -1),
    SILVER(3995, new Item[]{new Item(442)}, new Item[]{new Item(2355)}, 13.67, 20),
    SILVER_5(3994, SILVER, 5),
    SILVER_10(3993, SILVER, 10),
    SILVER_X(3992, SILVER, -1),
    STEEL(3999, new Item[]{new Item(440), new Item(453, 2)}, new Item[]{new Item(2353)}, 17.5, 30),
    STEEL_5(3998, STEEL, 5),
    STEEL_10(3997, STEEL, 10),
    STEEL_X(3996, STEEL, -1),
    GOLD(4003, new Item[]{new Item(444)}, new Item[]{new Item(2357)}, 22.5, 40),
    GOLD_5(4002, GOLD, 5),
    GOLD_10(4001, GOLD, 10),
    GOLD_X(4000, GOLD, -1),
    MITHRIL(7441, new Item[]{new Item(447), new Item(453, 4)}, new Item[]{new Item(2359)}, 30, 50),
    MITHRIL_5(7440, MITHRIL, 5),
    MITHRIL_10(6397, MITHRIL, 10),
    MITHRIL_X(4158, MITHRIL, -1),
    ADAMANT(7446, new Item[]{new Item(449), new Item(453, 6)}, new Item[]{new Item(2361)}, 37.5, 70),
    ADAMANT_5(7444, ADAMANT, 5),
    ADAMANT_10(7443, ADAMANT, 10),
    ADAMANT_X(7442, ADAMANT, -1),
    RUNITE(7450, new Item[]{new Item(451), new Item(453, 8)}, new Item[]{new Item(2363)}, 50, 85),
    RUNITE_5(7449, RUNITE, 5),
    RUNITE_10(7448, RUNITE, 10),
    RUNITE_X(7447, RUNITE, -1);

    /** Caches our enum values. */
    private static final ImmutableSet<SmeltingData> VALUES = Sets.immutableEnumSet(EnumSet.allOf(SmeltingData.class));

    /** The button identification. */
    private final int buttonId;

    /** The required items to smelt this bar. */
    public final Item[] required;

    /** he produced items for smelting the required items. */
    public final Item[] produced;

    /** The experience gained upon smelting one bar. */
    public final double experience;

    /** The requirement required to smelt the bar. */
    public final int requirement;

    /** The amount we're producing. */
    public final int amount;

    /** Constructs a new {@link SmeltingData} enumerator. */
    SmeltingData(int buttonId, Item[] required, Item[] produced, double experience, int requirement) {
        this.buttonId = buttonId;
        this.required = required;
        this.produced = produced;
        this.experience = experience;
        this.requirement = requirement;
        this.amount = 1;
    }

    /** Constructs a new {@link SmeltingData} enumerator. */
    SmeltingData(int buttonId, SmeltingData definition, int amount) {
        this.buttonId = buttonId;
        this.required = definition.required;
        this.produced = definition.produced;
        this.experience = definition.experience;
        this.requirement = definition.requirement;
        this.amount = amount;
    }

    /** Searches for a match for the internal button identification. */
    public static Optional<SmeltingData> getDefinition(int buttonId) {
        return VALUES.stream().filter(def -> def.buttonId == buttonId).findAny();
    }

    /** Searches for a match for the internal required items. */
    public static Optional<SmeltingData> getDefinitionByItem(int itemId) {
        for (SmeltingData data : VALUES) {
            for (Item item : data.required) {
                if (item.getId() == itemId) {
                    return Optional.of(data);
                }
            }
        }
        return Optional.empty();
    }

    public static SmeltingData getSmeltData(int itemId) {
        switch(itemId) {
            case 438:
            case 436:
                return BRONZE;
            case 440:
                return IRON;
            case 442:
                return SILVER;
            case 444:
                return GOLD;
            case 447:
                return MITHRIL;
            case 449:
                return ADAMANT;
            case 451:
                return RUNITE;
        }
        return null;
    }
}
