package com.osroyale.content.skill.impl.hunter.net.impl;

import java.util.Arrays;
import java.util.Optional;

public enum Butterfly {
    RUBY(5556, 15, 24, 10020, 30),
    SAPPHIRE(5555, 25, 34, 10018, 35),
    SNOWY(5554, 35, 44, 10016, 40),
    BLACKWARLOCK(5553, 45, 54, 10014, 80);

    public final int butterfly;
    public final int level;
    public final int experience;
    public final int reward;
    public final int delay;

    Butterfly(int butterfly, int level, int experience, int reward, int delay) {
        this.butterfly = butterfly;
        this.level = level;
        this.experience = experience;
        this.reward = reward;
        this.delay =delay;
    }

    public static Optional<Butterfly> forId(int butterfly) {
        return Arrays.stream(values()).filter(a -> a.butterfly == butterfly).findAny();
    }
}
