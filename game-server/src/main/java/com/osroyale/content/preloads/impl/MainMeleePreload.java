package com.osroyale.content.preloads.impl;

import com.osroyale.content.preloads.Preload;
import com.osroyale.content.preloads.PreloadRepository;
import com.osroyale.content.skill.impl.magic.Spellbook;
import com.osroyale.game.world.items.Item;

/**
 * Handles a basic melee equipment preload.
 *
 * @author Daniel
 */
public class MainMeleePreload implements Preload {

    @Override
    public String title() {
        return "<icon=23> Main Melee";
    }

    @Override
    public Spellbook spellbook() {
        return Spellbook.LUNAR;
    }

    @Override
    public Item[] equipment() {
        return new Item[]{
      /* HELM_SLOT   */ new Item(10828), // HELM OF NEITZ
      /* CAPE_SLOT   */ new Item(PreloadRepository.CAPE),  // CAPE
      /* AMULET_SLOT */ new Item(1725),  // AMULET OF STRENGTH
      /* WEAPON_SLOT */ new Item(4587),  // DRAGON SCIMITAR
      /* CHEST_SLOT  */ new Item(1127), // RUNE_PLATEBODY
      /* SHIELD_SLOT */ new Item(8850),  // RUNE DEFENDER
      /* LEGS_SLOT   */ new Item(1079),  // RUNE PLATELEGS
      /* HANDS_SLOT  */ new Item(7461),  // DRAGON GLOVES
      /* FEET_SLOT   */ new Item(3105),  // CLIMBING BOOTS
      /* RING_SLOT   */ new Item(2550),  // RING OF RECOIL
      /* ARROWS_SLOT */ null
        };
    }

    @Override
    public Item[] inventory() {
        return new Item[]{
                new Item(5698), // DRAGON DAGGER(P++)
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
                new Item(6685), // SARADOMIN BREW(4)
                new Item(385), // SHARK
                new Item(385), // SHARK
                new Item(3144), // COOKED KARAMBWAN
                new Item(3024), // SUPER RESTORE(4)
                new Item(3024), // SUPER RESTORE(4)
                new Item(2440), // SUPER STRENGTH(4)
                new Item(2442), // SUPER DEFENCE(4)
                new Item(560, 500), // DEATH RUNE
                new Item(9075, 500), // ASTRAL RUNE
                new Item(557, 1000), // EARTH RUNE
                new Item(2436), // SUPER ATTACK(4)
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
