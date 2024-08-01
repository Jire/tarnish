package com.osroyale;

public class Deque {

	public Deque() {
		head = new Linkable();
		head.prev = head;
		head.next = head;
	}

	public void insertHead(Linkable node) {
		if (node.next != null)
			node.unlink();
		node.next = head.next;
		node.prev = head;
		node.next.prev = node;
		node.prev.next = node;
	}

	public void insertTail(Linkable node) {
		if (node.next != null)
			node.unlink();
		node.next = head;
		node.prev = head.prev;
		node.next.prev = node;
		node.prev.next = node;
	}

	public Linkable popHead() {
		Linkable node = head.prev;
		if (node == head) {
			return null;
		} else {
			node.unlink();
			return node;
		}
	}

	public Linkable reverseGetFirst() {
		Linkable node = head.prev;
		if (node == head) {
			current = null;
			return null;
		} else {
			current = node.prev;
			return node;
		}
	}

	public Linkable getFirst() {
		Linkable node = head.next;
		if (node == head) {
			current = null;
			return null;
		} else {
			current = node.next;
			return node;
		}
	}

	public Linkable reverseGetNext() {
		Linkable node = current;
		if (node == head) {
			current = null;
			return null;
		} else {
			current = node.prev;
			return node;
		}
	}

	public Linkable getNext() {
		Linkable node = current;
		if (node == head) {
			current = null;
			return null;
		}
		current = node.next;
		return node;
	}

	public void removeAll() {
		if (head.prev == head)
			return;
		do {
			Linkable node = head.prev;
			if (node == head)
				return;
			node.unlink();
		} while (true);
	}

	private final Linkable head;
	private Linkable current;
}
