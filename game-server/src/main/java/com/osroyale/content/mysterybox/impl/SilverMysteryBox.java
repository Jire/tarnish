package com.osroyale.content.mysterybox.impl;

import com.osroyale.content.mysterybox.MysteryBox;
import com.osroyale.content.mysterybox.MysteryItem;
import com.osroyale.game.world.items.Item;
import com.osroyale.util.Items;

import static com.osroyale.content.mysterybox.MysteryRarity.*;

/**
 * The silver (tier 2) mystery box.
 *
 * @author Daniel
 */
public class SilverMysteryBox extends MysteryBox {
    @Override
    protected String name() {
        return "Silver mystery box";
    }

    @Override
    protected int item() {
        return 12955;
    }

    @Override
    protected MysteryItem[] rewards() {
        return new MysteryItem[]{
                new MysteryItem(Items.COINS, 500_000, COMMON),
                new MysteryItem(Items.SHARK, 100, COMMON),
                new MysteryItem(Items.GUTHANS_ARMOUR_SET, 1, COMMON),
                new MysteryItem(Items.VERACS_ARMOUR_SET, 1, COMMON),
                new MysteryItem(Items.KARILS_ARMOUR_SET, 1, COMMON),
                new MysteryItem(Items.DHAROKS_ARMOUR_SET, 1, COMMON),
                new MysteryItem(Items.AHRIMS_ARMOUR_SET, 1, COMMON),
                new MysteryItem(Items.ABYSSAL_WHIP, 1, COMMON),
                new MysteryItem(Items.DRAGON_FULL_HELM, 1, COMMON),
                new MysteryItem(Items.RANGERS_TUNIC, 1, COMMON),
                new MysteryItem(Items.SARADOMIN_GODSWORD, 1, UNCOMMON),
                new MysteryItem(Items.BANDOS_GODSWORD, 1, UNCOMMON),
                new MysteryItem(Items.ARMADYL_GODSWORD, 1, UNCOMMON),
                new MysteryItem(Items.ZAMORAK_GODSWORD, 1, UNCOMMON),
                new MysteryItem(Items.BANDOS_CHESTPLATE, 1, UNCOMMON),
                new MysteryItem(Items.BANDOS_TASSETS, 1, UNCOMMON),
                new MysteryItem(Items.BANDOS_BOOTS, 1, UNCOMMON),
                new MysteryItem(Items.FIGHTER_TORSO, 1, UNCOMMON),
                new MysteryItem(Items.FIGHTER_HAT, 1, UNCOMMON),
                new MysteryItem(Items.OLD_DEMON_MASK, 1, RARE),
                new MysteryItem(Items.JUNGLE_DEMON_MASK, 1, RARE),
                new MysteryItem(Items.GREATER_DEMON_MASK, 1, RARE),
                new MysteryItem(Items.LESSER_DEMON_MASK, 1, RARE),
                new MysteryItem(Items.TOXIC_BLOWPIPE, 1, EXOTIC),
                new MysteryItem(Items.ARMADYL_CHAINSKIRT, 1, EXOTIC),
                new MysteryItem(Items.ARMADYL_CHESTPLATE, 1, EXOTIC),
                new MysteryItem(Items.ARMADYL_HELMET, 1, EXOTIC),
                new MysteryItem(Items.TRIDENT_OF_THE_SEAS, 1, EXOTIC),
                new MysteryItem(Items.ABYSSAL_BLUDGEON, 1, EXOTIC),
                new MysteryItem(Items.DRAGON_HARPOON, 1, EXOTIC)
        };
    }
}
