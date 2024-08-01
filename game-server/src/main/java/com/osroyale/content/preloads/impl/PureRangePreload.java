package com.osroyale.content.preloads.impl;

import com.osroyale.content.preloads.Preload;
import com.osroyale.content.preloads.PreloadRepository;
import com.osroyale.content.skill.impl.magic.Spellbook;
import com.osroyale.game.world.items.Item;

/**
 * The pure melee equipment preload.
 *
 * @author Daniel
 */
public class PureRangePreload implements Preload {

    @Override
    public String title() {
        return "<icon=24> Pure Range";
    }

    @Override
    public Spellbook spellbook() {
        return Spellbook.MODERN;
    }

    @Override
    public Item[] equipment() {
        return new Item[]{
      /* HELM_SLOT   */ new Item(PreloadRepository.HAT),
      /* CAPE_SLOT   */ new Item(10499),
      /* AMULET_SLOT */ new Item(1704),
      /* WEAPON_SLOT */ new Item(861),
      /* CHEST_SLOT  */ new Item(PreloadRepository.ROBE_TOP),
      /* SHIELD_SLOT */ null,
      /* LEGS_SLOT   */ new Item(2497),
      /* HANDS_SLOT  */ new Item(7458),
      /* FEET_SLOT   */ new Item(3105),
      /* RING_SLOT   */ new Item(2550),
      /* ARROWS_SLOT */ new Item(892, 150)
        };
    }

    @Override
    public Item[] inventory() {
        return new Item[]{
                new Item(9185), // RUNE CROSSBOW
                new Item(868, 150), // RUNE KNIFE
                new Item(2436), // SUPER ATTACK(4)
                new Item(3024), // SUPER RESTORE(4)
                new Item(9244, 75), // DRAGON BOLTS (E)
                new Item(3841), // DAMAGED BOOK
                new Item(2440), // SUPER STRENGTH(4)
                new Item(3024), // SUPER RESTORE(4)
                new Item(385), // SHARK
                new Item(385), // SHARK
                new Item(2444), // RANGING POTION(4)
                new Item(6685), // SARADOMIN BREW(4)
                new Item(385), // SHARK
                new Item(385), // SHARK
                new Item(3144), // COOKED KARAMBWAN
                new Item(3144), // COOKED KARAMBWAN
                new Item(385), // SHARK
                new Item(385), // SHARK
                new Item(3144), // COOKED KARAMBWAN
                new Item(3144), // COOKED KARAMBWAN
                new Item(385), // SHARK
                new Item(4153), // DRAGON DAGGER(P++)
                new Item(3144), // COOKED KARAMBWAN
                new Item(3144), // COOKED KARAMBWAN
                new Item(385), // SHARK
                new Item(385), // SHARK
                new Item(385), // SHARK
                new Item(385), // SHARK
        };
    }

    @Override
    public int[] skills() {
        return new int[]{
                60, // ATTACK
                1, // DEFENCE
                99, // STRENGTH
                99, // HITPOINTS
                99, // RANGED
                52, // PRAYER
                99, // MAGIC
        };
    }

}
