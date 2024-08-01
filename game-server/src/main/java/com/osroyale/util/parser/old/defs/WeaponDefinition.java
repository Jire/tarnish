package com.osroyale.util.parser.old.defs;

import com.osroyale.Config;
import com.osroyale.game.world.entity.combat.ranged.RangedWeaponDefinition;

import java.util.Optional;

public class WeaponDefinition {
    public static final WeaponDefinition[] DEFINITIONS = new WeaponDefinition[Config.ITEM_DEFINITION_LIMIT];
    private final int item;
    private final String name;
    private final boolean twoHanded;
    private final String weaponType;
    private final RangedWeaponDefinition rangedDefinition;
    private final int stand;
    private final int walk;
    private final int run;

    public WeaponDefinition(int item, String name, boolean twoHanded, String weaponType, RangedWeaponDefinition rangedDefinition, int stand, int walk, int run) {
        this.item = item;
        this.name = name;
        this.twoHanded = twoHanded;
        this.weaponType = weaponType;
        this.rangedDefinition = rangedDefinition;
        this.stand = stand;
        this.walk = walk;
        this.run = run;
    }

    public int getItem() {
        return item;
    }

    public String getName() {
        return name;
    }

    public String getWeaponType() {
        return weaponType;
    }

    public Optional<RangedWeaponDefinition> getRangedDefinition() {
        return Optional.ofNullable(rangedDefinition);
    }

    public boolean isTwoHanded() {
        return twoHanded;
    }

    public int getStand() {
        return stand;
    }

    public int getWalk() {
        return walk;
    }

    public int getRun() {
        return run;
    }

    public static boolean isPoisonous(String name) {
        return name.endsWith("p)") || name.endsWith("p+)") || name.endsWith("p++)");
    }

    public static int poisonStrength(String name) {
        return name.endsWith("p)") ? 2 : name.endsWith("p+)") ? 4 : name.endsWith("p++)") ? 6 : 2;
    }

    /**
     * Gets an item definition.
     *
     * @param id The definition's item id.
     * @return The item definition for the item id, or null if the item id is
     * out of bounds.
     */
    public static WeaponDefinition get(int id) {
        if (id < 0 || id >= DEFINITIONS.length) {
            return null;
        }

        return DEFINITIONS[id];
    }

}
