package com.osroyale.content;

/**
 * Created by Daniel on 2018-02-06.
 */
public enum EmblemData {
    TIER_1(12746, 50),
    TIER_2(12748, 75),
    TIER_3(12749, 100),
    TIER_4(12750, 150),
    TIER_5(12751, 225),
    TIER_6(12752, 300),
    TIER_7(12753, 400),
    TIER_8(12754, 525),
    TIER_9(12755, 750),
    TIER_10(12756, 1000),
    ANCIENT(21807, 1);

    public final int item;
    public final int bloodMoney;

    EmblemData(int item, int bloodMoney) {
        this.item = item;
        this.bloodMoney = bloodMoney;
    }

    public static EmblemData forId(int id) {
        for (EmblemData emblem : values()) {
            if (emblem.item == id)
                return emblem;
        }
        return null;
    }

    public int getNext() {
        if (this == TIER_10 || this == ANCIENT)
            return -1;
        return values()[this.ordinal() + 1].item;
    }
}
