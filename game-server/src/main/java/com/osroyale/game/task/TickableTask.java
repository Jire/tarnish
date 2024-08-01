package com.osroyale.game.task;

public abstract class TickableTask extends Task {

    protected int tick;

    public TickableTask(boolean instant, int delay) {
        super(instant, delay);
    }

    protected abstract void tick();

    @Override
    public void execute() {
        tick();
        tick++;
    }

}
