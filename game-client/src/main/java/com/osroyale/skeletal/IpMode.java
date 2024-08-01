package com.osroyale.skeletal;

import com.osroyale.misc.SerialEnum;

public enum IpMode implements SerialEnum {

    NULL(0, 0),
    field1423(1, 1),
    field1424(2, 2),
    field1425(3, 3),
    field1422(4, 4);

    final int ordinal;

    final int serialId;

    IpMode(int ordinal, int serielId) {
        this.ordinal = ordinal;
        this.serialId = serielId;
    }

    public int id() {
        return this.serialId;
    }

    public static final IpMode[] VALUES = values();
}
