package com.osroyale.content.mysterybox.impl;

import com.osroyale.content.mysterybox.MysteryBox;
import com.osroyale.content.mysterybox.MysteryItem;

import static com.osroyale.content.mysterybox.MysteryRarity.EXOTIC;

/**
 * The pet mystery box.
 *
 * @author Daniel
 */
public class PetMysteryBox extends MysteryBox {
    @Override
    protected String name() {
        return "Pet mystery box";
    }

    @Override
    protected int item() {
        return 8038;
    }

    @Override
    protected MysteryItem[] rewards() {
        return new MysteryItem[]{
                new MysteryItem(13247, 1, EXOTIC),
                new MysteryItem(20851, 1, EXOTIC),
                new MysteryItem(12653, 1, EXOTIC),
                new MysteryItem(12651, 1, EXOTIC),
                new MysteryItem(12643, 1, EXOTIC),
                new MysteryItem(12644, 1, EXOTIC),
                new MysteryItem(12645, 1, EXOTIC),
                new MysteryItem(12646, 1, EXOTIC),
                new MysteryItem(12647, 1, EXOTIC),
                new MysteryItem(12648, 1, EXOTIC),
                new MysteryItem(12649, 1, EXOTIC),
                new MysteryItem(12650, 1, EXOTIC),
                new MysteryItem(12652, 1, EXOTIC),
                new MysteryItem(12654, 1, EXOTIC),
                new MysteryItem(12655, 1, EXOTIC),
                new MysteryItem(13178, 1, EXOTIC),
                new MysteryItem(13177, 1, EXOTIC),
                new MysteryItem(13179, 1, EXOTIC),
                new MysteryItem(13181, 1, EXOTIC),
                new MysteryItem(11995, 1, EXOTIC),
                new MysteryItem(12921, 1, EXOTIC),
                new MysteryItem(12816, 1, EXOTIC),
                new MysteryItem(13225, 1, EXOTIC),
                new MysteryItem(13321, 1, EXOTIC),
                new MysteryItem(13320, 1, EXOTIC),
                new MysteryItem(13323, 1, EXOTIC),
                new MysteryItem(20659, 1, EXOTIC),
                new MysteryItem(20661, 1, EXOTIC),
                new MysteryItem(20663, 1, EXOTIC),
                new MysteryItem(20665, 1, EXOTIC),
                new MysteryItem(13322, 1, EXOTIC)
        };
    }
}
