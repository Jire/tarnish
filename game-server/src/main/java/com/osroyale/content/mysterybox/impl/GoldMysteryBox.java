package com.osroyale.content.mysterybox.impl;

import com.osroyale.content.mysterybox.MysteryBox;
import com.osroyale.content.mysterybox.MysteryItem;
import com.osroyale.util.Items;

import static com.osroyale.content.mysterybox.MysteryRarity.*;
import static com.osroyale.content.mysterybox.MysteryRarity.UNCOMMON;

/**
 * The gold (tier 3) mystery box.
 *
 * @author Daniel
 */
public class GoldMysteryBox extends MysteryBox {
    @Override
    protected String name() {
        return "Gold mystery box";
    }

    @Override
    protected int item() {
        return 11739;
    }

    @Override
    protected MysteryItem[] rewards() {
        return new MysteryItem[]{
                new MysteryItem(Items.ARCHERS_RING_I_, 1, COMMON),
                new MysteryItem(Items.BERSERKER_RING_I_, 1, COMMON),
                new MysteryItem(Items.TOXIC_BLOWPIPE, 1, COMMON),
                new MysteryItem(Items.TRIDENT_OF_THE_SEAS, 1, COMMON),
                new MysteryItem(Items.TRIDENT_OF_THE_SWAMP, 1, COMMON),
                new MysteryItem(Items.ABYSSAL_BLUDGEON, 1, COMMON),
                new MysteryItem(Items.BANDOS_CHESTPLATE, 1, COMMON),
                new MysteryItem(Items.BANDOS_TASSETS, 1, COMMON),
                new MysteryItem(Items.BANDOS_BOOTS, 1, COMMON),
                new MysteryItem(Items.BANDOS_GODSWORD, 1, COMMON),
                new MysteryItem(Items.ARMADYL_HELMET, 1, COMMON),
                new MysteryItem(Items.ARMADYL_CHESTPLATE, 1, COMMON),
                new MysteryItem(Items.ARMADYL_CHAINSKIRT, 1, COMMON),
                new MysteryItem(Items.ARMADYL_CROSSBOW, 1, COMMON),
                new MysteryItem(Items.ARMADYL_CHESTPLATE, 1, COMMON),
                new MysteryItem(Items.PEGASIAN_BOOTS, 1, COMMON),
                new MysteryItem(Items.PRIMORDIAL_BOOTS, 1, COMMON),
                new MysteryItem(Items.ETERNAL_BOOTS, 1, COMMON),
                new MysteryItem(Items.SMOULDERING_STONE, 1, COMMON),
                new MysteryItem(Items.SARADOMIN_GODSWORD, 1, UNCOMMON),
                new MysteryItem(Items.RING_OF_SUFFERING, 1, UNCOMMON),
                new MysteryItem(Items.TORMENTED_BRACELET, 1, UNCOMMON),
                new MysteryItem(Items.AMULET_OF_TORTURE, 1, UNCOMMON),
                new MysteryItem(Items.NECKLACE_OF_ANGUISH, 1, UNCOMMON),
                new MysteryItem(22616, 1, UNCOMMON), //VESTA TOP
                new MysteryItem(22619, 1, UNCOMMON), //VESTA LEGS
                new MysteryItem(22625, 1, UNCOMMON), //STAT HELM
                new MysteryItem(22628, 1, UNCOMMON), //STAT TOP
                new MysteryItem(22631, 1, UNCOMMON), //STAT BOTTOMS
                new MysteryItem(22622, 1, UNCOMMON), //STAT WARHAMMER
                new MysteryItem(22647, 1, UNCOMMON), //ZURIELS TOP
                new MysteryItem(22650, 1, UNCOMMON), //ZURIELS BOTTOMS
                new MysteryItem(22653, 1, UNCOMMON), //ZURIELS HOOD
                new MysteryItem(22656, 1, UNCOMMON), //ZURIELS STAFF
                new MysteryItem(22634, 100, UNCOMMON), //MORRIGANS JAVELIN
                new MysteryItem(22636, 100, UNCOMMON), //MORRIGANS THROWING AXE
                new MysteryItem(22638, 1, UNCOMMON), //MORRIGANS COIF
                new MysteryItem(22641, 1, UNCOMMON), //MORRIGANS TOP
                new MysteryItem(22644, 1, UNCOMMON), //MORRIGANS BOTTOMS
                new MysteryItem(Items.DRAGON_HUNTER_CROSSBOW, 1, RARE),
                new MysteryItem(Items.DEXTEROUS_PRAYER_SCROLL, 1, RARE),
                new MysteryItem(Items.ARCANE_PRAYER_SCROLL, 1, RARE),
                new MysteryItem(Items.DINHS_BULWARK, 1, RARE),
                new MysteryItem(22978, 1, RARE), //DRAGON HUNTER LANCE
                new MysteryItem(Items.TWISTED_BUCKLER, 1, RARE),
                new MysteryItem(Items.DRAGON_CLAWS, 1, RARE),
                new MysteryItem(Items.ELDER_MAUL_3, 1, RARE),
                new MysteryItem(22324, 1, RARE), //GHRAZI RAPIER
                new MysteryItem(24271, 1, RARE), //HELM OF NEITIZNOT FACEGUARD
                new MysteryItem(22322, 1, RARE), //AVERNIC DEFENDER
                new MysteryItem(Items.DRAGON_WARHAMMER, 1, RARE),
                new MysteryItem(Items.RED_PARTYHAT, 1, EXOTIC),
                new MysteryItem(Items.YELLOW_PARTYHAT, 1, EXOTIC),
                new MysteryItem(Items.GREEN_PARTYHAT, 1, EXOTIC),
                new MysteryItem(Items.PURPLE_PARTYHAT, 1, EXOTIC),
                new MysteryItem(Items.BLUE_PARTYHAT, 1, EXOTIC),
                new MysteryItem(Items.WHITE_PARTYHAT, 1, EXOTIC),
                new MysteryItem(1053, 1, EXOTIC), //GREEN HALLOWEEN MASK
                new MysteryItem(1055, 1, EXOTIC), //BLUE HALLOWEEN MASK
                new MysteryItem(1057, 1, EXOTIC), //RED HALLOWEEN MASK
                new MysteryItem(11847, 1, EXOTIC), //BLACK HALOWEEN MASK
                new MysteryItem(Items.SANTA_HAT, 1, EXOTIC)
        };
    }
}
