package com.osroyale.game.world.items.containers.equipment;

import it.unimi.dsi.fastutil.objects.Object2ObjectMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;

import java.util.Arrays;

/**
 * The enumerated types of a players equipped item slots.
 *
 * @author Daniel
 */
public enum EquipmentType {
    NOT_WIELDABLE(-1, "NONE"),
    HAT(0, "head"),
    HELM(0, "head"),
    MASK(0, "head"),
    FACE(0, "head"),
    CAPE(1, "cape"),
    SHIELD(5, "shield"),
    GLOVES(9, "hands"),
    BOOTS(10, "feet"),
    AMULET(2, "neck"),
    RING(12, "ring"),
    ARROWS(13, "ammo"),
    BODY(4, "body"),
    TORSO(4, "body"),
    LEGS(7, "legs"),
    WEAPON(3, "weapon");

    private final int slot;
    private final String newItemDefName;

    EquipmentType(final int slot, final String newItemDefName) {
        this.slot = slot;
        this.newItemDefName = newItemDefName;
    }

    public int getSlot() {
        return slot;
    }

    public String getNewItemDefName() {
        return newItemDefName;
    }

    public static final EquipmentType[] values = values();

    private static final Object2ObjectMap<String, EquipmentType> newNameToType
            = new Object2ObjectOpenHashMap<>(values.length);

    static {
        for (EquipmentType value : values) {
            newNameToType.putIfAbsent(value.getNewItemDefName(), value);
        }
    }

    public static EquipmentType forNewName(final String newItemDefName) {
        return newNameToType.get(newItemDefName);
    }

    public static EquipmentType lookup(int slot) {
        return Arrays.stream(values()).filter(it -> it.slot == slot).findFirst().orElse(EquipmentType.NOT_WIELDABLE);
    }

}
