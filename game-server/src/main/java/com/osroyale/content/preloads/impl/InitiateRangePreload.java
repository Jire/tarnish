package com.osroyale.content.preloads.impl;

import com.osroyale.content.preloads.Preload;
import com.osroyale.content.preloads.PreloadRepository;
import com.osroyale.content.skill.impl.magic.Spellbook;
import com.osroyale.game.world.items.Item;

/**
 * The initiate range equipment preload.
 *
 * @author Daniel
 */
public class InitiateRangePreload implements Preload {

    @Override
    public String title() {
        return "<icon=24> Initiate Range";
    }

    @Override
    public Spellbook spellbook() {
        return Spellbook.MODERN;
    }

    @Override
    public Item[] equipment() {
        return new Item[]{
      /* HELM_SLOT   */ new Item(5574),
      /* CAPE_SLOT   */ new Item(10499),
      /* AMULET_SLOT */ new Item(1704),
      /* WEAPON_SLOT */ new Item(861),
      /* CHEST_SLOT  */ new Item(1133),
      /* SHIELD_SLOT */ null,
      /* LEGS_SLOT   */ new Item(5576),
      /* HANDS_SLOT  */ new Item(7458),
      /* FEET_SLOT   */ new Item(4127),
      /* RING_SLOT   */ new Item(2550),
      /* ARROWS_SLOT */ new Item(892, 200)
        };
    }

    @Override
    public Item[] inventory() {
        return new Item[]{
                new Item(9244, 100), // DRAGON BOLTS (E)
                new Item(9185), // RUNE CROSSBOW
                new Item(3024), // SUPER RESTORE(4)
                new Item(2444), // RANGING POTION(4)
                new Item(1197), // MITHRIL KITESHIELD
                new Item(4153), // DRAGON DAGGER(P++)
                new Item(3024), // SUPER RESTORE(4)
                new Item(2436), // SUPER ATTACK(4)
                new Item(385), // SHARK
                new Item(385), // SHARK
                new Item(3024), // SUPER RESTORE(4)
                new Item(2440), // SUPER STRENGTH(4)
                new Item(385), // SHARK
                new Item(385), // SHARK
                new Item(6685), // SARADOMIN BREW(4)
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
        };
    }

    @Override
    public int[] skills() {
        return new int[]{
                70, // ATTACK
                20, // DEFENCE
                99, // STRENGTH
                99, // HITPOINTS
                99, // RANGED
                52, // PRAYER
                99, // MAGIC
        };
    }

}
