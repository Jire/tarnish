package com.osroyale.game.world.entity.mob.player.relations;

public enum ChatEffect {

    NONE(0),
    WAVE(1),
    WAVE_2(2),
    SHAKE(3),
    SCROLL(4),
    SLIDE(5);

    private final int code;

    ChatEffect(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public static final ChatEffect[] values = values();

}
