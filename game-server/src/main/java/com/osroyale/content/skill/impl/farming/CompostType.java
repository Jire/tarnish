package com.osroyale.content.skill.impl.farming;

public enum CompostType {
    NONE(-1, 100, 1.00, 0),
    COMPOST(6032, 110, 0.60, 18),
    SUPERCOMPOST(6034, 115, 0.30, 26),
    ULTRACOMPOST(21483, 120, 0.10, 32);

    private final int item;
    private final int produceIncrease;
    private final double protection;
    private final double exp;

    CompostType(int item, int produceIncrease, double protection, double exp) {
        this.item = item;
        this.produceIncrease = produceIncrease;
        this.protection = protection;
        this.exp = exp;
    }

    public double getProtection() {
        return protection;
    }

    public double getExp() {
        return exp;
    }

    public static CompostType forItem(int item) {
        for (CompostType type : values()) {
            if (type.item == item) {
                return type;
            }
        }
        return NONE;
    }

    public int produceIncrease() {
        return produceIncrease;
    }

}
