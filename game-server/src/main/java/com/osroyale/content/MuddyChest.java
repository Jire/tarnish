package com.osroyale.content;

import com.osroyale.game.world.entity.mob.player.Player;
import com.osroyale.game.world.items.Item;
import com.osroyale.util.chance.Chance;
import com.osroyale.util.chance.WeightedChance;

import java.util.Arrays;

/**
 * Handles opening the crystal chest.
 *
 * @author Daniel
 */
public class MuddyChest {


    /** The item key to enter the crystal chest. */
    public static final Item KEY = new Item(993);


    /** Handles getting an item reward from the chest. */
    public static Item getReward() {
        return ITEMS.next();
    }

    /** Holds all the crystal chest rewards. */
    private static final Chance<Item> ITEMS = new Chance<>(Arrays.asList(
            new WeightedChance<>(8, new Item(1163, 1)), // RUNE_FULL_HELM
            new WeightedChance<>(6, new Item(1147, 1)), // RUNE_MED_HELM
            new WeightedChance<>(6, new Item(1127, 1)), // RUNE_PLATEBODY
            new WeightedChance<>(6, new Item(1093, 1)), // RUNE_PLATESKIRT
            new WeightedChance<>(6, new Item(1201, 1)), // RUNE_KITESHIELD
            new WeightedChance<>(6, new Item(1185, 1)), // RUNE_SQ_SHIELD
            new WeightedChance<>(6, new Item(1333, 1)), // RUNE_SCIMITAR
            new WeightedChance<>(6, new Item(1079, 1)), // RUNE_PLATELEGS
            new WeightedChance<>(5, new Item(10828, 1)), // HELM_OF_NEITZ
            new WeightedChance<>(5, new Item(1632, 1)), // UNCUT_DAGONSTONE
            new WeightedChance<>(5, new Item(4099, 1)), // MYSTIC_HAT
            new WeightedChance<>(5, new Item(4089, 1)), // MYSTIC_HAT
            new WeightedChance<>(5, new Item(4109, 1)), // MYSTIC_HAT
            new WeightedChance<>(5, new Item(4101, 1)), // MYSTIC_TOP
            new WeightedChance<>(5, new Item(4091, 1)), // MYSTIC_TOP
            new WeightedChance<>(5, new Item(4111, 1)), // MYSTIC_TOP
            new WeightedChance<>(5, new Item(4103, 1)), // MYSTIC_BOTTOM
            new WeightedChance<>(5, new Item(4093, 1)), // MYSTIC_BOTTOM
            new WeightedChance<>(5, new Item(4113, 1)), // MYSTIC_BOTTOM
            new WeightedChance<>(4, new Item(995, 35_000)), // COINS
            new WeightedChance<>(4, new Item(13307, 2_500)), // BLOOD_MONEY
            new WeightedChance<>(3.5, new Item(13307, 350)), // BLOOD_MONEY
            new WeightedChance<>(3.5, new Item(4153, 1)), // GRANITE_MAUL
            new WeightedChance<>(3.5, new Item(10589, 1)), // GRANITE_HELM
            new WeightedChance<>(3.5, new Item(10564, 1)), // GRANITE_BODY
            new WeightedChance<>(3.5, new Item(6809, 1)), // GRANITE_LEGS
            new WeightedChance<>(3.5, new Item(21643, 1)), // GRANITE_BOOTS
            new WeightedChance<>(3.5, new Item(21646, 1)), // GRANITE_LONGSWORD
            new WeightedChance<>(3, new Item(1149, 1)), // DRAGON_MED_HELM
            new WeightedChance<>(3, new Item(1187, 1)), // DRAGON_SQ_SHIELD
            new WeightedChance<>(3, new Item(1215, 1)), // DRAGON_DAGGER
            new WeightedChance<>(3, new Item(4587, 1)), // DRAGON_SCIMITAR
            new WeightedChance<>(3, new Item(4087, 1)), // DRAGON_PLATELEGS
            new WeightedChance<>(3, new Item(1434, 1)), // DRAGON_MACE
            new WeightedChance<>(3, new Item(4585, 1)), // DRAGON_PLATESKIRT
            new WeightedChance<>(3, new Item(3204, 1)), // DRAGON_HALBERD
            new WeightedChance<>(3, new Item(1249, 1)), // DRAGON_SPEAR
            new WeightedChance<>(3, new Item(1305, 1)), // DRAGON_LONGSWORD
            new WeightedChance<>(3, new Item(1377, 1)), // DRAGON_BATTLEAXE
            new WeightedChance<>(2, new Item(7158, 1)), // DRAGON_2H_SWORD
            new WeightedChance<>(1.5, new Item(6199, 1)), // BRONZE_MYSTERY_BOX
            new WeightedChance<>(1, new Item(11840, 1)), // DRAGON_BOOTS
            new WeightedChance<>(1, new Item(994, 5)), // DRAGON_BOOTS
            new WeightedChance<>(1, new Item(6571, 1)) // UNCUT_ONYX
    ));
}
