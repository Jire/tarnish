package com.osroyale.content.lms.loadouts.impl;

import com.osroyale.content.lms.loadouts.LMSLoadout;
import com.osroyale.content.skill.impl.magic.Spellbook;

public class Zerk extends LMSLoadout {

    @Override
    public int getAttackLevel() {
        return 75;
    }

    @Override
    public int getStrengthLevel() {
        return 99;
    }

    @Override
    public int getDefenceLevel() {
        return 45;
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
                3751, 33585 - 11792, 1704, 9243, 9185, 2503, 8850, 1079, 7462, 3105, 6737
        };
    }

    @Override
    public int[] getInventory() {
        return new int[] {
                4091, 4710, 4151, 1215,
                4093, 12829, 12695, 2444,
                -1, -1, 6685, 3024,
                -1, -1, 3144, 3144,
                385, 385, 385, 385,
                385, 385, 385, 385,
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
        return new int[] { 10330, 10332, 10334, 10338, 10340, 10342, 6585, 24780, 23246, 6920, 24419, 24420, 24421, 299, 19547, 19544 };
    }

    @Override
    public int[] getOffensiveItemUpgrades() {
        return new int[] { 22613, 24424, 22647 };
    }

}
