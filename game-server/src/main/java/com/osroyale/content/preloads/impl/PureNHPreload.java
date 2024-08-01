package com.osroyale.content.preloads.impl;

import com.osroyale.content.preloads.Preload;
import com.osroyale.content.preloads.PreloadRepository;
import com.osroyale.content.skill.impl.magic.Spellbook;
import com.osroyale.game.world.items.Item;

/**
 * Handles a pure nh equipment preload.
 *
 * @author Daniel
 */
public class PureNHPreload implements Preload {

    @Override
    public String title() {
        return "<icon=22> Pure NH";
    }

    @Override
    public Spellbook spellbook() {
        return Spellbook.ANCIENT;
    }

    @Override
    public Item[] equipment() {
        return new Item[]{
      /* HELM_SLOT   */ new Item(PreloadRepository.HAT), // HAT
      /* CAPE_SLOT   */ new Item(PreloadRepository.GOD_CAPE),  // CAPE
      /* AMULET_SLOT */ new Item(1704),  // AMULET OF GLORY
      /* WEAPON_SLOT */ new Item(4675),  // ANCIENT STAFF
      /* CHEST_SLOT  */ new Item(6107), // GHOSTLY ROBE TOP
      /* SHIELD_SLOT */ new Item(PreloadRepository.GOD_BOOK),  // DAMAGED BOOK
      /* LEGS_SLOT   */ new Item(6108),  // GHOSTLY ROBE BOTTOM
      /* HANDS_SLOT  */ new Item(7458),  // MITHRIL GLOVES
      /* FEET_SLOT   */ new Item(3105),  // CLIMBING BOOTS
      /* RING_SLOT   */ new Item(2550),  // RING OF RECOIL
      /* ARROWS_SLOT */ new Item(9244, 75),  // DRAGONSTONE BOLTS (E)
        };
    }

    @Override
    public Item[] inventory() {
        return new Item[]{
                new Item(9185), // RUNE CROSSBOW
                new Item(10499), // AVA'S ACCUMULATOR
                new Item(2444), // RANGING POTION
                new Item(6685), // SARADOMIN BREW(4)
                new Item(2497), // BLACK D'HIDE CHAPS
                new Item(5698), // DRAGON DAGGER(P++)
                new Item(2436), // SUPER ATTACK(4)
                new Item(6685), // SARADOMIN BREW(4)
                new Item(4587), // DRAGON SCIMITAR
                new Item(2440), // SUPER STRENGTH(4)
                new Item(3024), // SUPER RESTORE(4)
                new Item(3024), // SUPER RESTORE(4)
                new Item(3144), // COOKED KARAMBWAN
                new Item(3144), // COOKED KARAMBWAN
                new Item(3144), // COOKED KARAMBWAN
                new Item(3144), // COOKED KARAMBWAN
                new Item(391), // MANTA
                new Item(391), // MANTA
                new Item(391), // MANTA
                new Item(391), // MANTA
                new Item(391), // MANTA
                new Item(391), // MANTA
                new Item(391), // MANTA
                new Item(391), // MANTA
                new Item(391), // MANTA
                new Item(555, 500), // WATER RUNE
                new Item(560, 500), // DEATH RUNE
                new Item(565, 500), // BLOOD RUNE
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
                55, // PRAYER
                99, // MAGIC
        };
    }
}
