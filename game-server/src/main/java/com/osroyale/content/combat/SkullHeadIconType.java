package com.osroyale.content.combat;

import java.util.Optional;

public enum SkullHeadIconType {
    NO_SKULL(-1),
    WHITE_SKULL(0),
    RED_SKULL(1);

    private final int cocde;

    SkullHeadIconType(int code) {
        this.cocde = code;
    }

    public static Optional<SkullHeadIconType> lookup(int code) {
        if (code < -1 || code > 1) {
            return Optional.empty();
        }

        return Optional.of(SkullHeadIconType.values()[code + 1]);
    }

    public int getCode() {
        return cocde;
    }

}
