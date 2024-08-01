package com.osroyale.content.mysterybox;

import com.osroyale.content.mysterybox.impl.BronzeMysteryBox;
import com.osroyale.content.mysterybox.impl.GoldMysteryBox;
import com.osroyale.content.mysterybox.impl.PetMysteryBox;
import com.osroyale.content.mysterybox.impl.SilverMysteryBox;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * The mystery box class.
 *
 * @author Daniel
 */
public abstract class MysteryBox {

    /** The map containing all the mystery boxes. */
    private static Map<Integer, MysteryBox> MYSTERY_BOXES = new HashMap<>();

    /** Handles loading the mystery boxes. */
    public static void load() {
        MysteryBox BRONZE_BOX = new BronzeMysteryBox();
        MysteryBox SILVER_BOX = new SilverMysteryBox();
        MysteryBox GOLD_BOX = new GoldMysteryBox();
        MysteryBox PET_BOX = new PetMysteryBox();

        MYSTERY_BOXES.put(BRONZE_BOX.item(), BRONZE_BOX);
        MYSTERY_BOXES.put(SILVER_BOX.item(), SILVER_BOX);
        MYSTERY_BOXES.put(GOLD_BOX.item(), GOLD_BOX);
        MYSTERY_BOXES.put(PET_BOX.item(), PET_BOX);
    }

    /** Handles getting the mystery box. */
    static Optional<MysteryBox> getMysteryBox(int item) {
        return MYSTERY_BOXES.containsKey(item) ? Optional.of(MYSTERY_BOXES.get(item)) : Optional.empty();
    }

    /** The name of the mystery box. */
    protected abstract String name();

    /** The item identification of the mystery box. */
    protected abstract int item();

    /** The rewards for the mystery box. */
    protected abstract MysteryItem[] rewards();
}
