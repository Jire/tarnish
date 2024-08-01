package com.osroyale.content.preloads;


import com.osroyale.util.Utility;

public enum RandomItem {
    GOD_CAPE(2412, 2413, 2414),
    BOOKS(3839, 3841, 3843),
    HAT(656, 658, 660, 662, 664, 740, 1017, 2900, 2910, 2920, 2930, 2940),
    ROBE_TOP(544, 546, 636, 638, 640, 642, 644, 2896, 2906, 2916, 2926, 2936),
    ROBE_BOTTOM(542, 548, 646, 648, 650, 652, 654, 2898, 2908, 2918, 2928, 2938),
    MYSTIC_TOP(4091, 4101, 4111),
    MYSTIC_BOTTOM(4093, 4113, 4103),
    CAPE(4315, 4317, 4319, 4321, 4323, 4325, 4327, 4329, 4331, 4333, 4335, 4337, 4339, 4341, 4343, 4345, 4347, 4349,
            4351, 4353, 4355, 4357, 4359, 4361, 4363, 4365, 4367, 4369, 4371, 4373, 4375, 4377, 4379, 4381, 4383, 4385,
            4387, 4389, 4391, 4393, 4395, 4397, 4399, 4401, 4403, 4405, 4407, 4409, 4411, 4413);

    private final int[] items;

    RandomItem(int... items) {
        this.items = items;
    }

    public int getItem() {
        return Utility.randomElement(items);
    }
}
