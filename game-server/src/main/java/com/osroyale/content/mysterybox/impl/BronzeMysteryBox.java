package com.osroyale.content.mysterybox.impl;

import com.osroyale.content.mysterybox.MysteryBox;
import com.osroyale.content.mysterybox.MysteryItem;
import com.osroyale.util.Items;

import static com.osroyale.content.mysterybox.MysteryRarity.*;

/**
 * The bronze (tier 1) mystery box.
 *
 * @author Daniel
 */
public class BronzeMysteryBox extends MysteryBox {
    @Override
    protected String name() {
        return "Bronze mystery box";
    }

    @Override
    protected int item() {
        return 6199;
    }
    @Override
    protected MysteryItem[] rewards() {
        return new MysteryItem[]{
                new MysteryItem(Items.COINS, 150_000, COMMON),
                new MysteryItem(Items.MANTA_RAY_2, 100, COMMON),
                new MysteryItem(Items.DRAGON_BOOTS, 1, COMMON),
                new MysteryItem(Items.ABYSSAL_WHIP, 1, COMMON),
                new MysteryItem(Items.DRAGON_PLATELEGS, 1, COMMON),
                new MysteryItem(Items.DRAGON_CHAINBODY, 1, COMMON),
                new MysteryItem(Items.DRAGON_PLATESKIRT, 1, COMMON),
                new MysteryItem(Items.BERSERKER_RING, 1, COMMON),
                new MysteryItem(Items.SEERS_RING, 1, COMMON),
                new MysteryItem(Items.ARCHERS_RING, 1, COMMON),
                new MysteryItem(Items.INFINITY_HAT, 1, UNCOMMON),
                new MysteryItem(Items.INFINITY_TOP, 1, UNCOMMON),
                new MysteryItem(Items.INFINITY_BOTTOMS, 1, UNCOMMON),
                new MysteryItem(Items.INFINITY_GLOVES, 1, UNCOMMON),
                new MysteryItem(Items.INFINITY_BOOTS, 1, UNCOMMON),
                new MysteryItem(Items.DARK_BOW, 1, UNCOMMON),
                new MysteryItem(Items.GNOME_SCARF, 1, UNCOMMON),
                new MysteryItem(Items.AMULET_OF_FURY, 1, COMMON),
                new MysteryItem(Items.AMULET_OF_THE_DAMNED, 1, RARE),
                new MysteryItem(Items.DRAGON_FULL_HELM, 1, RARE),
                new MysteryItem(Items.FLIPPERS, 1, RARE),
                new MysteryItem(Items.DRAGON_PICKAXE, 1, RARE),
                new MysteryItem(Items.TRIDENT_OF_THE_SEAS, 1, RARE),
                new MysteryItem(Items.GOLDEN_APRON, 1, EXOTIC),
                new MysteryItem(Items.GOLDEN_CHEFS_HAT, 1, EXOTIC),
                new MysteryItem(Items.SAMURAI_BOOTS, 1, EXOTIC),
                new MysteryItem(Items.SAMURAI_GLOVES, 1, EXOTIC),
                new MysteryItem(Items.SAMURAI_GREAVES, 1, EXOTIC),
                new MysteryItem(Items.SAMURAI_SHIRT, 1, EXOTIC),
                new MysteryItem(Items.SAMURAI_KASA, 1, EXOTIC)
        };
    }
}
