package com.osroyale.game.task.impl;

import com.osroyale.game.task.Task;
import com.osroyale.game.world.object.GameObject;

/**
 * An randomevent which replaces an object with another object.
 *
 * @author Daniel | Obey
 * @author Michael | Chex
 */
public class ObjectReplacementEvent extends Task {

    /** The original game object. */
    private final GameObject original;

    /** The replacement game object. */
    private final int originalId;

    /** The replacement game object. */
    private final int replacement;

    /** The on event end runnable. */
    private final Runnable onEndRun;

    public ObjectReplacementEvent(GameObject original, int delay) {
        super(false, delay);
        this.original = original;
        this.originalId = original.getId();
        this.replacement = -1;
        this.onEndRun = () -> {
        };
    }

    public ObjectReplacementEvent(GameObject original, int replacement, int delay) {
        super(false, delay);
        this.original = original;
        this.originalId = original.getId();
        this.replacement = replacement;
        this.onEndRun = () -> {
        };
    }

    public ObjectReplacementEvent(GameObject original, int replacement, int delay, Runnable onEndRun) {
        super(false, delay);
        this.original = original;
        this.originalId = original.getId();
        this.replacement = replacement;
        this.onEndRun = onEndRun;
    }

    @Override
    public void onSchedule() {
        if (replacement == -1) {
            original.unregister();
            return;
        }
        original.transform(replacement);
    }

    @Override
    public void execute() {
        cancel();
    }

    @Override
    public void onCancel(boolean logout) {
        if (replacement == -1) {
            original.register();
        } else {
            original.transform(originalId);
        }

        onEndRun.run();
    }
}
