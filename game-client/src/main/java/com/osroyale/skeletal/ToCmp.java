package com.osroyale.skeletal;

import com.osroyale.misc.SerialEnum;

public enum ToCmp implements SerialEnum {
    NULL(0, 0, null, -1, -1),
    ARX(1, 1, null, 0, 2),
    ARY(2, 2, null, 1, 2),
    ARZ(3, 3, null, 2, 2),
    ATX(4, 4, null, 3, 1),
    ATY(5, 5, null, 4, 1),
    ATZ(6, 6, null, 5, 1),
    ATSX(7, 7, null, 6, 3),
    ATSY(8, 8, null, 7, 3),
    ATSZ(9, 9, null, 8, 3),
    CH(10, 10, null, 0, 7),
    CS(11, 11, null, 1, 7),
    CSL(12, 12, null, 2, 7),
    CR(13, 13, null, 3, 7),
    CG(14, 14, null, 4, 7),
    CB(15, 15, null, 5, 7),
    TP(16, 16, null, 0, 5);

    final int ordinal;

    private final int serialId;

    private final int component;

    ToCmp(int ordinal, int serialId, String var3, int component, int var5) {
        this.ordinal = ordinal;
        this.serialId = serialId;
        this.component = component;
    }


    public int id() {
        return this.serialId;
    }

    public int component() {
        return this.component;
    }

    public static final ToCmp[] VALUES = values();

    public static ToCmp lookup(int id) {
        ToCmp toCmp = SerialEnum.findEnumerated(ToCmp.VALUES, id);
        if (toCmp == null) {
            toCmp = ToCmp.NULL;
        }
        return toCmp;
    }
}