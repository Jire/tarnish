package com.osroyale.game.world.entity.mob.player.relations;

import java.util.Optional;

public enum PrivacyChatMode {
    ON(0),
    FRIENDS_ONLY(1),
    OFF(2),
    HIDE(3);

    private final int code;

    PrivacyChatMode(int code) {
        this.code = code;
    }

    public static boolean isValid(int code) {
        return code >= 0 && code < PrivacyChatMode.values().length;
    }

    public static Optional<PrivacyChatMode> get(int code) {
        if (!isValid(code)) {
            return Optional.empty();
        }
        return Optional.of(PrivacyChatMode.values()[code]);
    }

    public int getCode() {
        return code;
    }

}