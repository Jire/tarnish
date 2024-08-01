package com.osroyale.content.lms.loadouts.impl;

import com.osroyale.content.lms.loadouts.LMSLoadout;
import com.osroyale.content.skill.impl.magic.Spellbook;

public class Pure extends LMSLoadout {


    @Override
    public int getAttackLevel() {
        return 99;
    }

    @Override
    public int getStrengthLevel() {
        return 99;
    }

    @Override
    public int getDefenceLevel() {
        return 1;
    }

    @Override
    public int getRangedLevel() {
        return 99;
    }

    @Override
    public int getPrayerLevel() {
        return 99;
    }

    @Override
    public int getMagicLevel() {
        return 99;
    }

    @Override
    public int getHitpointsLevel() {
        return 99;
    }

    @Override
    public int[] getEquipment() {
        return new int[] {
                6109, //Helm
                33585 - 11792, //Cape
                23794 - 11792, //Amulet
                9243, //Arrows
                4675, //Weapon
                6107, //Body
                3842, //Shield
                6108, //Legs
                7458, //Gloves
                3105, //Boots
                6737 //Ring
        };
    }

    @Override
    public int[] getInventory() {
        return new int[] {
                3144, -1, -1, 3024,
                3144, -1, 2444, 12695,
                -1, -1, 385, 6685,
                9185, 1704, 385, 385,
                2497, 4587, 385, 385,
                1215, 385, 385, 385,
                385, 385, 385, 12791
        };
    }

    @Override
    public Spellbook getSpellbook() {
        return Spellbook.ANCIENT;
    }

    @Override
    public int[][] getRunePouchRunes() {
        return new int[][] {
                {555, 6000},
                {560, 4000},
                {565, 2000}
        };
    }

    @Override
    public int[] getOffensiveItem() {
        return new int[] { 11802, 11785, 11235, 13652, 21003, 19481, 21295, 21006, 6889, 12002, 11770, 11791 };
    }

    @Override
    public int[] getDefensiveItem() {
        return new int[] { 19553, 10372, 20595, 23246, 12638, 19547, 12596, 23389, 19544, 2579 };
    }

    @Override
    public int[] getOffensiveItemUpgrades() {
        return new int[] { 22613, 24424, 22647 };
    }

}