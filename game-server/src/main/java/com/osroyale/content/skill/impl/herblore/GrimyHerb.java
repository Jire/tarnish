package com.osroyale.content.skill.impl.herblore;

import com.osroyale.game.world.items.Item;

import java.util.Arrays;
import java.util.Optional;

/**
 * Holds all the grimy herb data.
 *
 * @author Daniel
 * @author Michael | Chex
 */
public enum GrimyHerb {
    GUAM(199, 249, 1, 2.5),
    MARRENTILL(201, 251, 5, 3.8),
    TARROMIN(203, 253, 11, 5),
    HARRALANDER(205, 255, 20, 6.3),
    RANARR(207, 257, 25, 7.5),
    TOADFLAX(3049, 2998, 30, 8),
    SPIRITWEED(12174, 12172, 35, 8),
    IRIT(209, 259, 40, 8.8),
    WERGALI(14836, 14854, 30, 8),
    AVANTOE(211, 261, 48, 10),
    KWUARM(213, 263, 54, 11.3),
    SNAPDRAGON(3051, 3000, 59, 11.8),
    CADANTINE(215, 265, 65, 12.5),
    LANTADYME(2485, 2481, 67, 13.1),
    DWARFWEED(217, 267, 70, 13.8),
    TORSTOL(219, 269, 75, 15);

    /**
     * The grimy herb.
     */
    public final int grimy;


    /**
     * The clean herb.
     */
    public final int clean;

    /**
     * The level to clean the herb.
     */
    public final int level;

    /**
     * The experience for cleaning the herb.
     */
    public final double experience;

    /**
     * The grimy herb data.
     *
     * @param grimy      The grimy herb.
     * @param clean      The clean herb.
     * @param level      The level required to clean the herb.
     * @param experience The experience given for cleaning the herb.
     */
    GrimyHerb(int grimy, int clean, int level, double experience) {
        this.grimy = grimy;
        this.clean = clean;
        this.level = level;
        this.experience = experience;
    }

    /**
     * Gets the grimy herb.
     *
     * @return The grimy herb.
     */
    public Item getGrimy() {
        return new Item(grimy);
    }

    /**
     * Gets the clean herb.
     *
     * @return The clean herb.
     */
    public Item getClean() {
        return new Item(clean);
    }

    /**
     * Gets the experience for cleaning a herb.
     *
     * @return The experience.
     */
    public double getExperience() {
        return experience;
    }

    /**
     * Gets the level to clean the herb.
     *
     * @return The level required.
     */
    public int getLevel() {
        return level;
    }

    /**
     * Gets the herb data from the item identification.
     *
     * @param id The item identification.
     * @return The grimy herb data.
     */
    public static Optional<GrimyHerb> forId(int id) {
        return Arrays.stream(values()).filter(a -> a.grimy == id).findAny();
    }

    public int getGrimyID() {
        return grimy;
    }
    public int getCleanID() {
        return clean;
    }
}
