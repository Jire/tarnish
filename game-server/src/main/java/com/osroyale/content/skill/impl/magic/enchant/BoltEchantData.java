package com.osroyale.content.skill.impl.magic.enchant;

import com.osroyale.game.world.items.Item;

import java.util.Arrays;
import java.util.Optional;

/**
 * Holds all the bolt enchanting data.
 *
 * @author Daniel
 */
public enum BoltEchantData {
    OPAL(879, 9236, 4, new Item(564, 1), new Item(556, 2)),
    SAPPHIRE(9337, 9240, 7, new Item(564, 1), new Item(555, 1), new Item(558, 1)),
    JADE(9335, 9237, 14, new Item(564, 1), new Item(557, 2)),
    PEARL(880, 9238, 24, new Item(564, 1), new Item(555, 2)),
    EMERALD(9338, 9241, 27, new Item(564, 1), new Item(556, 3), new Item(561, 1)),
    RED_TOPAZ(9336, 9239, 29, new Item(564, 1), new Item(554, 2)),
    RUBY(9339, 9242, 49, new Item(564, 1), new Item(554, 5), new Item(565, 1)),
    DIAMOND(9340, 9243, 57, new Item(564, 1), new Item(557, 10), new Item(563, 2)),
    DRAGONSTONE(9341, 9244, 68, new Item(564, 1), new Item(557, 15), new Item(566, 1)),
    ONYX(9342, 9245, 87, new Item(564, 1), new Item(554, 20), new Item(560, 1));

    public final int bolt;
    public final int enchantedBolt;
    public final int levelRequired;
    public final Item[] runesRequired;
    BoltEchantData(int bolt, int enchantedBolt, int levelRequired, Item...runesRequired) {
        this.bolt = bolt;
        this.enchantedBolt = enchantedBolt;
        this.levelRequired = levelRequired;
        this.runesRequired = runesRequired;
    }

    public static Optional<BoltEchantData> forItem(int item) {
        return Arrays.stream(values()).filter(bolt -> bolt.enchantedBolt == item).findFirst();
    }
}