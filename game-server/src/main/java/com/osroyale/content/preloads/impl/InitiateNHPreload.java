package com.osroyale.content.preloads.impl;

import com.osroyale.content.preloads.Preload;
import com.osroyale.content.preloads.PreloadRepository;
import com.osroyale.content.skill.impl.magic.Spellbook;
import com.osroyale.game.world.items.Item;

/**
 * The initiate NH equipment preload.
 *
 * @author Daniel
 */
public class InitiateNHPreload implements Preload {

    @Override
    public String title() {
        return "<icon=22> Initiate NH";
    }

    @Override
    public Spellbook spellbook() {
        return Spellbook.ANCIENT;
    }

    @Override
    public Item[] equipment() {
        return new Item[]{
      /* HELM_SLOT   */ new Item(7400),
      /* CAPE_SLOT   */ new Item(PreloadRepository.GOD_CAPE),
      /* AMULET_SLOT */ new Item(1704),
      /* WEAPON_SLOT */ new Item(4675),
      /* CHEST_SLOT  */ new Item(7399),
      /* SHIELD_SLOT */ new Item(PreloadRepository.GOD_BOOK),
      /* LEGS_SLOT   */ new Item(7398),
      /* HANDS_SLOT  */ new Item(7462),
      /* FEET_SLOT   */ new Item(3105),
      /* RING_SLOT   */ new Item(2550),
      /* ARROWS_SLOT */ new Item(9244, 100),
        };
    }

    @Override
    public Item[] inventory() {
        return new Item[]{
                new Item(9185), // RUNE CROSSBOW
                new Item(5574), // INITIATE SALLET
                new Item(1133), // STUDDED BODY
                new Item(2444), // RANGING POTION(4)
                new Item(10498), // AVA'S ATTRACTOR
                new Item(5576), // INITIATE CUISSE
                new Item(4587), // DRAGON SCIMITAR
                new Item(2436), // SUPER ATTACK(4)
                new Item(3024), // SUPER RESTORE(4)
                new Item(3024), // SUPER RESTORE(4)
                new Item(6685), // SARADOMIN BREW(4)
                new Item(2440), // SUPER STRENGTH(4)
                new Item(3024), // SUPER RESTORE(4)
                new Item(385), // SHARK
                new Item(385), // SHARK
                new Item(3144), // COOKED KARAMBWAN
                new Item(385), // SHARK
                new Item(385), // SHARK
                new Item(385), // SHARK
                new Item(3144), // COOKED KARAMBWAN
                new Item(385), // SHARK
                new Item(5698), // DRAGON DAGGER(P++)
                new Item(385), // SHARK
                new Item(3144), // COOKED KARAMBWAN
                new Item(560, 500), // DEATH RUNE
                new Item(565, 500), // BLOOD RUNE
                new Item(555, 1000), // WATER RUNE
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
