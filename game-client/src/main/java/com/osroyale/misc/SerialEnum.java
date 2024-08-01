package com.osroyale.misc;

import java.util.Arrays;

public interface SerialEnum {

    int id();

    static <E extends SerialEnum> E findEnumerated(E[] values, int serialId) {
        return Arrays.stream(values).filter(e -> serialId == e.id()).findFirst().orElse(null);
    }
}