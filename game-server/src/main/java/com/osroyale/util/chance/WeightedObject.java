package com.osroyale.util.chance;

/**
 * Represents a weighted object.
 * 
 * @author Michael | Chex
 */
public interface WeightedObject<T> extends Comparable<WeightedObject<T>> {

    /** Gets the object's weight. */
    double getWeight();

    /**  Gets the representation of the weighted chance. */
    T get();

    @Override
    String toString();
}
