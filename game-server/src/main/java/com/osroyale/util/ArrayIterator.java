package com.osroyale.util;

import com.google.common.collect.PeekingIterator;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * An {@link Iterator} implementation that will iterate over the elements in
 * an array without the overhead of the {@link ArrayList}s {@code withdraw}
 * operation.
 * @param <E> the type of array being iterated over.
 * @author lare96 <http://github.com/lare96>
 */
public final class ArrayIterator<E> implements PeekingIterator<E> {

    /**
     * The array that is storing the elements.
     */
    private final E[] array;

    /**
     * The current index that the iterator is iterating over.
     */
    private int index;

    /**
     * The last index that the iterator iterated over.
     */
    private int lastIndex = -1;

    /**
     * Creates a new {@link ArrayIterator}.
     * @param array the array that is storing the elements.
     */
    public ArrayIterator(E[] array) {
        this.array = array;
    }

    @Override
    public boolean hasNext() {
        return !(index + 1 > array.length);
    }

    @Override
    public E peek() {
        return array[index];
    }

    @Override
    public E next() {
        if(index >= array.length)
            throw new ArrayIndexOutOfBoundsException("There are no elements left to iterate over!");
        lastIndex = index;
        index++;
        return array[lastIndex];
    }

    @Override
    public void remove() {
        if(lastIndex == -1)
            throw new IllegalStateException("This method can only be called once after \"next\".");
        array[lastIndex] = null;
        lastIndex = -1;
    }
}