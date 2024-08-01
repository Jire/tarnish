package com.osroyale.content.skill.impl.fletching.impl;

import com.osroyale.content.skill.impl.fletching.Fletchable;
import com.osroyale.content.skill.impl.fletching.FletchableItem;
import com.osroyale.content.skill.impl.fletching.Fletching;
import com.osroyale.game.world.items.Item;

public enum Battlestaff implements Fletchable {
    FIRE_BATTLESTAFF(new Item(1391, 1), new Item(569, 1), new FletchableItem(new Item(1393, 1), 62, 125.0)),
    WATER_BATTLESTAFF(new Item(1391, 1), new Item(571, 1), new FletchableItem(new Item(1395, 1), 54, 100.0)),
    AIR_BATTLESTAFF(new Item(1391, 1), new Item(573, 1), new FletchableItem(new Item(1397, 1), 66, 137.5)),
    EARTH_BATTLESTAFF(new Item(1391, 1), new Item(575, 1), new FletchableItem(new Item(1399, 1), 58, 112.5));

    private final Item use;
    private final Item with;
    private final FletchableItem[] items;

    Battlestaff(Item use, Item with, FletchableItem... items) {
        this.use = use;
        this.with = with;
        this.items = items;
    }

    public static void load() {
        for (Battlestaff battlestaff : values()) {
            Fletching.addFletchable(battlestaff);
        }
    }

    @Override
    public int getAnimation() {
        return 7531;
    }

    @Override
    public int getGraphics() {
        switch (this) {
            case WATER_BATTLESTAFF:
                return 1370;
            case EARTH_BATTLESTAFF:
                return 1371;
            case FIRE_BATTLESTAFF:
                return 1372;
            case AIR_BATTLESTAFF:
            default:
                return 306;
        }
    }

    @Override
    public Item getUse() {
        return use;
    }

    @Override
    public Item getWith() {
        return with;
    }

    @Override
    public FletchableItem[] getFletchableItems() {
        return items;
    }

    @Override
    public String getProductionMessage() {
        return null;
    }

    @Override
    public Item[] getIngredients() {
        return new Item[] { use, with };
    }
}