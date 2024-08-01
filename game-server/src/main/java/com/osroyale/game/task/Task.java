package com.osroyale.game.task;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A game representing a cyclic unit of work.
 *
 * @author lare96 <http://github.org/lare96>
 * @author Jire
 */
public abstract class Task {

    private static final Logger logger = LoggerFactory.getLogger(Task.class);

    /**
     * If execution happens instantly upon being scheduled.
     */
    private final boolean instant;

    /**
     * The cyclic delay.
     */
    private int delay;

    /**
     * If registration has taken place.
     */
    private boolean running;

    /**
     * A counter that determines when execution should take place.
     */
    private int counter;

    /**
     * An optional attachment.
     */
    private volatile Object key;

    /**
     * Creates a new {@link Task}.
     */
    public Task(boolean instant, int delay) {
        if (delay <= 0) {
            instant = true;
            delay = 1;
        }
        this.instant = instant;
        this.delay = delay;
    }

    /**
     * Creates a new {@link Task} that doesn't execute instantly.
     */
    public Task(int delay) {
        this(false, delay);
    }

    public final void process() {
        if (++counter >= delay && running) {
            baseExecute();
            counter = 0;
        }
    }

    /***
     * Executes the abstract execute method ensuring any methods are called before and after
     */
    public final void baseExecute() {
        execute();
    }

    /**
     * A function representing the unit of work that will be carried out.
     */
    public abstract void execute();

    /**
     * Determines if the task can be ran.
     */
    public boolean canRun() {
        return true;
    }

    /**
     * Cancels all subsequent executions. Does nothing if already cancelled.
     *
     * @return `true` if the task was cancelled & is no longer running.
     */
    public final boolean cancel() {
        return cancel(false);
    }

    /**
     * Cancels all subsequent executions. Does nothing if already cancelled.
     *
     * @return `true` if the task was cancelled & is no longer running.
     */
    public final boolean cancel(boolean logout) {
        if (!running) return true;

        try {
            onCancel(logout);
        } catch (Exception e) {
            if (logger.isWarnEnabled()) {
                logger.warn("Failed to run task onCancel(logout=" + logout + ")", e);
            }
        }

        running = false;

        return true;
    }

    /**
     * A function executed on registration.
     */
    public void onSchedule() {
    }

    /**
     * A function executed on registration.
     */
    public boolean canSchedule() {
        return true;
    }

    /**
     * A function executed on cancellation.
     */
    public void onCancel(boolean logout) {
    }

    /**
     * Attaches a new key.
     */
    public final Task attach(Object newKey) {
        key = newKey;
        return this;
    }

    /**
     * @return {@code true} if execution happens instantly upon being scheduled.
     */
    public final boolean isInstant() {
        return instant;
    }

    /**
     * @return The cyclic delay.
     */
    public final int getDelay() {
        return delay;
    }

    /**
     * Sets the cyclic delay.
     */
    public final void setDelay(int delay) {
        this.delay = delay;
    }

    /**
     * @return {@code true} if registration has taken place.
     */
    public final boolean isRunning() {
        return running;
    }

    /**
     * @return An optional attachment.
     */
    public final Object getAttachment() {
        return key;
    }

    public final void setRunning(boolean running) {
        this.running = running;
    }

    /**
     * Executes after checking {@link Task ::canSchedule}
     */
    public void beforeSchedule() {
    }

}
