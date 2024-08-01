package com.osroyale.game.world.entity.mob.npc.drop;

import java.util.NavigableMap;
import java.util.Random;
import java.util.TreeMap;

public class WeightedCollection<E> {
    private final NavigableMap<Double, E> map = new TreeMap<>();
    private final Random random;
    private double total = 0;

    public double getTotal() {
        return total;
    }

    public WeightedCollection() {
        this(new Random());
    }

    public WeightedCollection(Random random) {
        this.random = random;
    }

    public WeightedCollection<E> add(double weight, E result) {
        if (weight <= 0) return this;
        total += weight;
        map.put(total, result);
        return this;
    }

    public E next() {
        double value = random.nextDouble() * total;
        return map.higherEntry(value).getValue();
    }
}
