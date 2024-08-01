package com.osroyale.content.lms.loadouts.impl;

import com.osroyale.content.lms.loadouts.LMSLoadout;
import com.osroyale.content.skill.impl.magic.Spellbook;

public class Max extends LMSLoadout {

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
        return 75;
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
                10828, 33585 - 11792, 1704, 9243, 9185, 2503, 12954, 1079, 7462, 3105, 6737
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
        return new int[] { 11785, 11802, 11235, 13652, 21003, 22324, 4153, 19481, 21295, 24417, 21006, 6889, 12002, 11770, 11791 };
    }

    @Override
    public int[] getDefensiveItem() {
        return new int[] { 4712, 4714, 6585, 21018, 21021, 21024, 11834, 12831, 13235, 4724, 4736, 299, 4745, 4753, 4759 };
    }

    @Override
    public int[] getOffensiveItemUpgrades() {
        return new int[] { 22613, 22622, 24424, 22647 };
    }

}
