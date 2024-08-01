package com.osroyale.content.preloads.impl;

import com.osroyale.content.preloads.Preload;
import com.osroyale.content.preloads.PreloadRepository;
import com.osroyale.content.skill.impl.magic.Spellbook;
import com.osroyale.game.world.items.Item;

/**
 * The Zerker NH equipment preload.
 *
 * @author Daniel
 */
public class ZerkerNHPreload implements Preload {

    @Override
    public String title() {
        return "<icon=22> Zerker NH";
    }

    @Override
    public Spellbook spellbook() {
        return Spellbook.ANCIENT;
    }

    @Override
    public Item[] equipment() {
        return new Item[]{
      /* HELM_SLOT   */ new Item(3751), // BERSERKER HELM
      /* CAPE_SLOT   */ new Item(PreloadRepository.GOD_CAPE),  // SARADOMIN CAPE
      /* AMULET_SLOT */ new Item(1704),  // AMULET OF GLORY
      /* WEAPON_SLOT */ new Item(4675),  // ANCIENT STAFF
      /* CHEST_SLOT  */ new Item(PreloadRepository.MYSTIC_TOP), // MYSTIC ROBE TOP
      /* SHIELD_SLOT */ new Item(PreloadRepository.GOD_BOOK),  // DAMAGED BOOK
      /* LEGS_SLOT   */ new Item(PreloadRepository.MYSTIC_BOTTOM),  // MYTIC ROBBE BOTTOM
      /* HANDS_SLOT  */ new Item(7461),  // BARROWS GLOVES
      /* FEET_SLOT   */ new Item(3105),  // CLIMBING BOOTS
      /* RING_SLOT   */ new Item(2550),  // RING OF RECOIL
      /* ARROWS_SLOT */ new Item(9244, 100),  // DRAGONSTONE BOLTS (E)
        };
    }

    @Override
    public Item[] inventory() {
        return new Item[]{
                new Item(9185), // RUNE CROSSBOW
                new Item(2503), // BLACK D'HIDE BODY
                new Item(4587), // DRAGON SCIMITAR
                new Item(3024), // SUPER RESTORE(4)
                new Item(10499), // AVA'S ACCUMULATOR
                new Item(1079), // RUNE PLATELEGS
                new Item(1201), // RUNE KITESHIELD
                new Item(3024), // SUPER RESTORE(4)
                new Item(2444), // RANGING POTION
                new Item(5698), // DRAGON DAGGER(P++)
                new Item(2436), // SUPER ATTACK(4)
                new Item(3024), // SUPER RESTORE(4)
                new Item(3144), // COOKED KARAMBWAN
                new Item(2440), // SUPER STRENGTH(4)
                new Item(3144), // COOKED KARAMBWAN
                new Item(3024), // SUPER RESTORE(4)
                new Item(6685), // SARADOMIN BREW(4)
                new Item(6685), // SARADOMIN BREW(4)
                new Item(391), // MANTA
                new Item(3024), // SUPER RESTORE(4)
                new Item(6685), // SARADOMIN BREW(4)
                new Item(6685), // SARADOMIN BREW(4)
                new Item(391), // MANTA
                new Item(555, 500), // WATER RUNE
                new Item(6685), // SARADOMIN BREW(4)
                new Item(6685), // SARADOMIN BREW(4)
                new Item(560, 500), // DEATH RUNE
                new Item(565, 500), // BLOOD RUNE
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
