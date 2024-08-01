package com.osroyale.content;

import java.util.Optional;

public enum ValueIcon {
    NONE(-1),
    BRONZE(0),
    SILVER(1),
    GREEN(2),
    BLUE(3),
    RED(4);

    private final int code;

    ValueIcon(int code) {
        this.code = code;
    }

    public static Optional<ValueIcon> lookup(int code) {
        if (code < -1 || code > 4) {
            return Optional.empty();
        }

        return Optional.of(ValueIcon.values()[code + 1]);
    }

    public int getCode() {
        return code;
    }

}