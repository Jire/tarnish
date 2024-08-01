package com.osroyale.content.preloads.impl;

import com.osroyale.content.preloads.Preload;
import com.osroyale.content.preloads.PreloadRepository;
import com.osroyale.content.skill.impl.magic.Spellbook;
import com.osroyale.game.world.items.Item;

/**
 * Handles a zerker rangw equipment preload.
 *
 * @author Daniel
 */
public class ZerkerRangePreload implements Preload {
    @Override
    public String title() {
        return "<icon=24> Zerker Range";
    }

    @Override
    public Spellbook spellbook() {
        return Spellbook.LUNAR;
    }

    @Override
    public Item[] equipment() {
        return new Item[]{
      /* HELM_SLOT   */ new Item(3751), // ZERK HELM
      /* CAPE_SLOT   */ new Item(10498),  // CAPE
      /* AMULET_SLOT */ new Item(1704),  // AMULET OF STRENGTH
      /* WEAPON_SLOT */ new Item(861),  // DRAGON SCIMITAR
      /* CHEST_SLOT  */ new Item(2503), // RUNE PLATEBODY
      /* SHIELD_SLOT */ null,  // RUNE DEFENDER
      /* LEGS_SLOT   */ new Item(1079),  // RUNE PLATELEGS
      /* HANDS_SLOT  */ new Item(7461),  // DRAGON GLOVES
      /* FEET_SLOT   */ new Item(3105),  // CLIMBING BOOTS
      /* RING_SLOT   */ new Item(2550),  // RING OF RECOIL
      /* ARROWS_SLOT */ new Item(892, 150)
        };
    }

    @Override
    public Item[] inventory() {
        return new Item[]{
                new Item(9185), // RUNE CROSSBOW
                new Item(1201), // RUNE KITESHIELD
                new Item(6685), // SARADOMIN BREW(4)
                new Item(3024), // SUPER RESTORE(4)
                new Item(9244, 100), // DRAGON BOLTS (E)
                new Item(391), // MANTA RAY
                new Item(6685), // SARADOMIN BREW(4)
                new Item(3024), // SUPER RESTORE(4)
                new Item(391), // MANTA RAY
                new Item(391), // MANTA RAY
                new Item(2444), // RANGING POTION(4)
                new Item(3024), // SUPER RESTORE(4)
                new Item(3144), // COOKED KARAMBWAN
                new Item(391), // MANTA RAY
                new Item(2440), // SUPER STRENGTH(4)
                new Item(2436), // SUPER ATTACK(4)
                new Item(3144), // COOKED KARAMBWAN
                new Item(391), // MANTA RAY
                new Item(391), // MANTA RAY
                new Item(391), // MANTA RAY
                new Item(3144), // COOKED KARAMBWAN
                new Item(4153), // GRANITE MAUL
                new Item(391), // MANTA RAY
                new Item(391), // MANTA RAY
                new Item(3144), // COOKED KARAMBWAN
                new Item(557, 1000), // EARTH RUNE
                new Item(9075, 1000), // ASTRAL RUNE
                new Item(560, 1000), // DEATH RUNE
        };
    }

    @Override
    public int[] skills() {
        return new int[]{
                60, // ATTACK
                45, // DEFENCE
                99, // STRENGTH
                99, // HITPOINTS
                99, // RANGED
                55, // PRAYER
                99, // MAGIC
        };
    }
}
