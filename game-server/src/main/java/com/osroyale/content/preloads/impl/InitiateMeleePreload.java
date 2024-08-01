package com.osroyale.content.preloads.impl;

import com.osroyale.content.preloads.Preload;
import com.osroyale.content.preloads.PreloadRepository;
import com.osroyale.content.skill.impl.magic.Spellbook;
import com.osroyale.game.world.items.Item;

/**
 * The initiate melee equipment preload.
 *
 * @author Daniel
 */
public class InitiateMeleePreload implements Preload {

    @Override
    public String title() {
        return "<icon=23> Initiate Melee";
    }

    @Override
    public Spellbook spellbook() {
        return Spellbook.MODERN;
    }

    @Override
    public Item[] equipment() {
        return new Item[]{
      /* HELM_SLOT   */ new Item(5574),
      /* CAPE_SLOT   */ new Item(PreloadRepository.CAPE),
      /* AMULET_SLOT */ new Item(1725),
      /* WEAPON_SLOT */ new Item(4587),
      /* CHEST_SLOT  */ new Item(5575),
      /* SHIELD_SLOT */ new Item(8848),
      /* LEGS_SLOT   */ new Item(5576),
      /* HANDS_SLOT  */ new Item(7458),
      /* FEET_SLOT   */ new Item(3105),
      /* RING_SLOT   */ new Item(2550),
      /* ARROWS_SLOT */ null
        };
    }

    @Override
    public Item[] inventory() {
        return new Item[]{
                new Item(3024), // SUPER RESTORE(4)
                new Item(6685), // SARADOMIN BREW(4)
                new Item(2436), // SUPER ATTACK(4)
                new Item(2440), // SUPER STRENGTH(4)
                new Item(3024), // SUPER RESTORE(4)
                new Item(385), // SHARK
                new Item(385), // SHARK
                new Item(3144), // COOKED KARAMBWAN
                new Item(3024), // SUPER RESTORE(4)
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
                new Item(5698), // DRAGON DAGGER(P++)
                new Item(385), // SHARK
                new Item(385), // SHARK
                new Item(385), // SHARK
                new Item(385), // SHARK
                new Item(385), // SHARK
                new Item(385), // SHARK
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
