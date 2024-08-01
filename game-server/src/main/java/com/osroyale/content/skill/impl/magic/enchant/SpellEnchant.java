package com.osroyale.content.skill.impl.magic.enchant;

import com.osroyale.game.world.items.Item;

public enum SpellEnchant {
    SAPPHIRE(1155, new Item(555, 1), new Item(564, 1)),
    EMERALD(1165, new Item(556, 3), new Item(564, 1)),
    RUBY(1176, new Item(554, 5), new Item(564, 1)),
    DIAMOND(1180, new Item(557, 10), new Item(564, 1)),
    DRAGONSTONE(1187, new Item(555, 15), new Item(557, 15), new Item(564, 1)),
    ONYX(6003, new Item(557, 20), new Item(554, 20), new Item(564, 1)),
    ZENYTE(40180, new Item(565, 20), new Item(566, 20), new Item(564));

    private final int spell;

    private final Item[] runes;

    SpellEnchant(int spell, Item... runes) {
        this.spell = spell;
        this.runes = runes;
    }

    public int getSpell() {
        return spell;
    }

    public Item[] getRunes() {
        return runes;
    }

    public static SpellEnchant forSpell(int id) {
        for (SpellEnchant spellEnchant : values()) {
            if (spellEnchant.getSpell() == id)
                return spellEnchant;
        }
        return null;
    }
}

