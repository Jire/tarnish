package com.osroyale.skeletal;


import com.osroyale.misc.SerialEnum;

public enum Toca implements SerialEnum {

    VR(0, 0),
    VT(1, 1),
    aClass146_1(2, 2),
    VS(3, 3),
    field1506(4, 4),
    field1507(5, 5),
    field1508(6, 6),
    field1509(7, 7),
    NULL(8, 8);

    final int field1512;

    final int serialId;

    Toca(int var3, int serialId) {
        this.field1512 = var3;
        this.serialId = serialId;
    }

    public int id() {
        return this.serialId;
    }

    public static final Toca[] VALUES = values();
}
