package com.osroyale.util.chance;

import com.osroyale.game.world.items.Item;

import java.util.LinkedList;
import java.util.List;

/**
 * Handles a random chance.
 *
 * @param <T> - The representation type.
 * @author Michael | Chex
 */
public class Chance<T> {

    public enum ChanceType {
        ALWAYS(100),
        COMMON(100),
        UNCOMMON(75),
        RARE(.6),
        VERY_RARE(.2);

        private final double weight;

        ChanceType(double weight) {
            this.weight = weight;
        }

        public double getWeight() {
            return weight;
        }
    }

    /** The list of weighted object. */
    private final List<WeightedObject<T>> objects;

    /** The sum of the weights. */
    private double sum;

    /** Creates a new instance of the class. */
    public Chance(List<WeightedObject<T>> objects) {
        this.objects = objects;
        sum = objects.stream().mapToDouble(WeightedObject::getWeight).sum();
        objects.sort((first, second) -> (int) Math.signum(second.getWeight() - first.getWeight()));
    }

    /** Creates a new instance of the class. */
    public Chance() {
        this.objects = new LinkedList<>();
        sum = 0;
    }

    /** Adds a new {@code WeightedObject} to the {@code #object} list. */
    public final void add(double weight, T t) {
        objects.add(new WeightedChance<>(weight, t));
        sum += weight;
        objects.sort((first, second) -> (int) Math.signum(second.getWeight() - first.getWeight()));
    }

    /** Adds a new {@code WeightedObject} to the {@code #object} list. */
    public final void add(ChanceType type, T t) {
        add(type.getWeight(), t);
    }

    /** Generates a {@code WeightedObject}. */
    public T next() {
        double rnd = Math.random() * sum;
        double hit = 0;

        for (WeightedObject<T> obj : objects) {
            hit += obj.getWeight();

            if (hit >= rnd) {
                return obj.get();
            }
        }

        throw new AssertionError("The random number [" + rnd + "] is too large!");
    }

    /** Generates a {@code WeightedObject}. */
    public WeightedObject<T> next(double boost) {
        if (boost <= 0 || boost > 1) {
            throw new IllegalArgumentException("Boost is outside of the domain: (0, 1]");
        }

        double rnd = Math.random() * sum;
        double hit = 0;

        for (WeightedObject<T> obj : objects) {
            hit += obj.getWeight() + boost;

            if ((int) (hit * (1 + boost)) >= (int) rnd) {
                return obj;
            }
        }

        throw new AssertionError("The random number [" + rnd + "] is too large!");
    }

    public Item[] toItemArray() {
        int count = 0;
        Item[] array = new Item[objects.size()];
        for (WeightedObject<T> obj : objects) {
            array[count] = (Item) obj.get();
            count++;
        }
        return array;
    }

    @Override
    public String toString() {
        return objects.toString();
    }

}
