package com.osroyale.skeletal;

import com.osroyale.misc.SerialEnum;

public enum ToType implements SerialEnum {
    NULL(0, 0, null, 0),
    TV(1, 1, null, 9),
    TB(2, 2, null, 3),
    TC(3, 3, null, 6),
    TT(4, 4, null, 1),
    field1452(5, 5, null, 3);

    final int serialId;

    private final int id;

    private final int dimensions;

    ToType(int serialId, int id, String name, int dimensions) {
        this.serialId = serialId;
        this.id = id;
        this.dimensions = dimensions;
    }

    private static final ToType[] VALUES = values();

    public static ToType lookUpById(int id) {
        ToType type = SerialEnum.findEnumerated(VALUES, id);
        if (type == null) {
            type = ToType.NULL;
        }
        return type;
    }

    public int id() {
        return this.id;
    }

    int getDimensions() {
        return this.dimensions;
    }
}