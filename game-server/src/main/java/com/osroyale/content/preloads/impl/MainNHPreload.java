package com.osroyale.content.preloads.impl;

import com.osroyale.content.preloads.Preload;
import com.osroyale.content.preloads.PreloadRepository;
import com.osroyale.content.skill.impl.magic.Spellbook;
import com.osroyale.game.world.items.Item;

/**
 * Handles a main nh equipment preload.
 *
 * @author Daniel
 */
public class MainNHPreload implements Preload {
    @Override
    public String title() {
        return "<icon=22> Main NH";
    }

    @Override
    public Spellbook spellbook() {
        return Spellbook.ANCIENT;
    }

    @Override
    public Item[] equipment() {
        return new Item[]{
      /* HELM_SLOT   */ new Item(10828), // HELM OF NIET
      /* CAPE_SLOT   */ new Item(PreloadRepository.GOD_CAPE),  // SARA CAPE
      /* AMULET_SLOT */ new Item(1704),  // AMULET OF GLORY
      /* WEAPON_SLOT */ new Item(4675),  // ANCIENT STAFF
      /* CHEST_SLOT  */ new Item(PreloadRepository.MYSTIC_TOP), // MYSTIC ROBE TOP
      /* SHIELD_SLOT */ new Item(PreloadRepository.GOD_BOOK),  // Unholy BOOK
      /* LEGS_SLOT   */ new Item(PreloadRepository.MYSTIC_BOTTOM),  // MYSTIC ROBE BOTTOM
      /* HANDS_SLOT  */ new Item(7461),  // Dragon GLOVES
      /* FEET_SLOT   */ new Item(3105),  // CLIMBING BOOTS
      /* RING_SLOT   */ new Item(2550),  // RING OF RECOIL
      /* ARROWS_SLOT */ new Item(9244, 100),  // DRAGONSTONE BOLTS (E)
        };
    }

    @Override
    public Item[] inventory() {
        return new Item[]{
                new Item(9185), // RUNE CROSSBOW
                new Item(1201), // RUNE KITESHIELD
                new Item(4587), // DRAGON SCIMITAR
                new Item(6685), // SARADOMIN BREW(4)
                new Item(10499), // AVA'S ACCUMULATOR
                new Item(2503), // BLACK D'HIDE BODY
                new Item(5698), // DRAGON DAGGER(P++)
                new Item(6685), // SARADOMIN BREW(4)
                new Item(391), // MANTA RAY
                new Item(1079), // RUNE PLATELEGS
                new Item(3024), // SUPER RESTORE(4)
                new Item(3024), // SUPER RESTORE(4)
                new Item(391), // MANTA RAY
                new Item(391), // MANTA RAY
                new Item(391), // MANTA RAY
                new Item(2436), // SUPER ATTACK(4)
                new Item(391), // MANTA RAY
                new Item(391), // MANTA RAY
                new Item(391), // MANTA RAY
                new Item(2440), // SUPER STRENGTH(4)
                new Item(391), // MANTA RAY
                new Item(391), // MANTA RAY
                new Item(391), // MANTA RAY
                new Item(2444), // RANGING POTION(4)
                new Item(391), // MANTA RAY
                new Item(565, 1000), // BLOOD RUNE
                new Item(555, 1000), // WATER RUNE
                new Item(560, 1000), // DEATH RUNE
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
