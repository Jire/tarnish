package com.osroyale.util;

import java.util.Collection;
import java.util.Iterator;
import java.util.Queue;

public class SortedQueue<E> implements Queue<E> {
	
	private Object[] elements;
	
	public SortedQueue(int initial) {
		elements = new Object[initial];
	}

	@Override
	public Iterator<E> iterator() {
		return null;
	}

	@Override
	public boolean contains(Object other) {
		return false;
	}

	@Override
	public boolean containsAll(Collection<?> collection) {
		return false;
	}

	@Override
	public boolean isEmpty() {
		return false;
	}

	@Override
	public int size() {
		return 0;
	}

	@Override
	public Object[] toArray() {
		return elements;
	}

	@Override
	public <T> T[] toArray(T[] array) {
		return null;
	}

	@Override
	public boolean remove(Object other) {
		return false;
	}

	@Override
	public E remove() {
		return null;
	}

	@Override
	public boolean removeAll(Collection<?> collection) {
		return false;
	}

	@Override
	public boolean retainAll(Collection<?> collection) {
		return false;
	}

	@Override
	public boolean add(E element) {
		return false;
	}

	@Override
	public boolean offer(E element) {
		return false;
	}

	@Override
	public boolean addAll(Collection<? extends E> collection) {
		return false;
	}

	@Override
	public void clear() {
	}

	@Override
	public E poll() {
		return null;
	}

	@Override
	public E element() {
		return null;
	}

	@Override
	public E peek() {
		return null;
	}

}
