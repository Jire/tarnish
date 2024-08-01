package com.osroyale.content.mysterybox;

import com.osroyale.game.world.items.Item;

import static com.osroyale.content.mysterybox.MysteryRarity.COMMON;

public class MysteryItem extends Item {

    final MysteryRarity rarity;

    public MysteryItem(int id, int amount, MysteryRarity rarity) {
        super(id, amount);
        this.rarity = rarity;
    }

    public MysteryItem(int id, int amount) {
        this(id, amount, COMMON);
    }
}
