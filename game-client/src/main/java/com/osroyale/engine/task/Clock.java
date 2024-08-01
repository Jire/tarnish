package com.osroyale.engine.task;

import net.runelite.rs.api.RSClock;

public abstract class Clock implements RSClock {
    public abstract void mark();

    public abstract int wait(int var1, int var2);

}
