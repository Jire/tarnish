package com.osroyale.content.preloads.impl;

import com.osroyale.content.preloads.Preload;
import com.osroyale.content.preloads.PreloadRepository;
import com.osroyale.content.skill.impl.magic.Spellbook;
import com.osroyale.game.world.items.Item;

/**
 * The F2P melee equipment preload.
 *
 * @author Daniel
 */
public class F2PMeleePreload implements Preload {

    @Override
    public String title() {
        return "<icon=23> F2P Melee";
    }

    @Override
    public Spellbook spellbook() {
        return Spellbook.MODERN;
    }

    @Override
    public Item[] equipment() {
        return new Item[]{
      /* HELM_SLOT   */ new Item(579),
      /* CAPE_SLOT   */ new Item(PreloadRepository.CAPE),
      /* AMULET_SLOT */ new Item(1725),
      /* WEAPON_SLOT */ new Item(1333),
      /* CHEST_SLOT  */ new Item(544),
      /* SHIELD_SLOT */ null,
      /* LEGS_SLOT   */ new Item(542),
      /* HANDS_SLOT  */ new Item(1059),
      /* FEET_SLOT   */ new Item(1061),
      /* RING_SLOT   */ null,
      /* ARROWS_SLOT */ null
        };
    }

    @Override
    public Item[] inventory() {
        return new Item[]{
                new Item(1319), // RUNE 2H SWORD
                new Item(113), // STRENGTH POTION(4)
                new Item(373), // SWORDFISH
                new Item(373), // SWORDFISH
                new Item(373), // SWORDFISH
                new Item(373), // SWORDFISH
                new Item(373), // SWORDFISH
                new Item(373), // SWORDFISH
                new Item(373), // SWORDFISH
                new Item(373), // SWORDFISH
                new Item(373), // SWORDFISH
                new Item(373), // SWORDFISH
                new Item(373), // SWORDFISH
                new Item(373), // SWORDFISH
                new Item(373), // SWORDFISH
                new Item(373), // SWORDFISH
                new Item(373), // SWORDFISH
                new Item(373), // SWORDFISH
                new Item(373), // SWORDFISH
                new Item(373), // SWORDFISH
                new Item(373), // SWORDFISH
                new Item(373), // SWORDFISH
                new Item(373), // SWORDFISH
                new Item(373), // SWORDFISH
                new Item(373), // SWORDFISH
                new Item(373), // SWORDFISH
                new Item(373), // SWORDFISH
                new Item(373), // SWORDFISH
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
                34, // PRAYER
                99, // MAGIC
        };
    }
}
