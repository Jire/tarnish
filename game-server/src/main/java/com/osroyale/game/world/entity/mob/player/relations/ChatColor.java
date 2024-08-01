package com.osroyale.game.world.entity.mob.player.relations;

public enum ChatColor {

    YELLOW(0),
    RED(1),
    GREEN(2),
    CYAN(3),
    PURPLE(4),
    WHITE(5),
    FLASH_1(6),
    FLASH_2(7),
    FLASH_3(8),
    GLOW_1(9),
    GLOW_2(10),
    GLOW_3(11);

    private final int code;

    ChatColor(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public static final ChatColor[] values = values();

}
