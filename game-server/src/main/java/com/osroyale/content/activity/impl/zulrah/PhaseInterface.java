package com.osroyale.content.activity.impl.zulrah;

/**
 * The Zulrah phase interface.
 *
 * @author Daniel
 */
public interface PhaseInterface<T> {

    /** Handles what happens in the phase. */
    void execute(final T activity);
}
