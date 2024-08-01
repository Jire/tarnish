package com.osroyale.content.preloads.impl;

import com.osroyale.content.preloads.Preload;
import com.osroyale.content.skill.impl.magic.Spellbook;
import com.osroyale.game.world.items.Item;

/**
 * Handles a range main equipment preload.
 *
 * @author Daniel
 */
public class MainRangePreload implements Preload {

    @Override
    public String title() {
        return "<icon=24> Main Range";
    }

    @Override
    public Spellbook spellbook() {
        return Spellbook.LUNAR;
    }

    @Override
    public Item[] equipment() {
        return new Item[]{
      /* HELM_SLOT   */ new Item(10828), // HELM OF NEITZ
      /* CAPE_SLOT   */ new Item(10499),  // AVAS ACCUM
      /* AMULET_SLOT */ new Item(1704),  // AMULET OF GLORY
      /* WEAPON_SLOT */ new Item(861),  // MAGIC SHORTBOW
      /* CHEST_SLOT  */ new Item(2503), // BLACK D'HIDE BODY
      /* SHIELD_SLOT */ null,
      /* LEGS_SLOT   */ new Item(1079),  // RUNE PLATELEGS
      /* HANDS_SLOT  */ new Item(7461),  // BLACK D'HIDE VAMBS
      /* FEET_SLOT   */ new Item(3105),  // CLIMBING BOOTS
      /* RING_SLOT   */ new Item(2550),  // RING OF RECOIL
      /* ARROWS_SLOT */ new Item(892, 500)    // RUNE ARROWS
        };
    }

    @Override
    public Item[] inventory() {
        return new Item[]{
                new Item(9185), // RUNE CROSSBOW
                new Item(1201), // RUNE KITESHIELD
                new Item(385), // SHARK
                new Item(3144), // COOKED KARAMBWAN
                new Item(9244, 50), // DRAGON BOLTS (E)
                new Item(385), // SHARK
                new Item(385), // SHARK
                new Item(3144), // COOKED KARAMBWAN
                new Item(385), // SHARK
                new Item(385), // SHARK
                new Item(385), // SHARK
                new Item(3144), // COOKED KARAMBWAN
                new Item(385), // SHARK
                new Item(385), // SHARK
                new Item(385), // SHARK
                new Item(3144), // COOKED KARAMBWAN
                new Item(385), // SHARK
                new Item(385), // SHARK
                new Item(385), // SHARK
                new Item(3144), // COOKED KARAMBWAN
                new Item(3024), // SUPER RESTORE(4)
                new Item(3024), // SUPER RESTORE(4)
                new Item(6685), // SARADOMIN BREW(4)
                new Item(2442), // SUPER DEFENCE(4)
                new Item(560, 500), // DEATH RUNE
                new Item(9075, 500), // ASTRAL RUNE
                new Item(557, 1000), // EARTH RUNE
                new Item(2444), // RANGING POTION(4)
        };
    }

    @Override
    public int[] skills() {
        return new int[]{
                99, // ATTACK
                99, // DEFENCE
                99, // STRENGTH
                99, // HITPOINTS
                99, // RANGED
                99, // PRAYER
                99, // MAGIC
        };
    }
}
