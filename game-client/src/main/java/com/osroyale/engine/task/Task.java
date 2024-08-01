package com.osroyale.engine.task;

import net.runelite.rs.api.RSTask;

public class Task implements RSTask {

    Task next;
    public volatile int status;
    int type;
    public int intArgument;
    Object objectArgument;
    public volatile Object result;

    public Task() {
        this.status = 0;
    }
}
