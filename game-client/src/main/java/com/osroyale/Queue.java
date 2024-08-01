package com.osroyale;

final class Queue {

	public Queue() {
		head = new Cacheable();
		head.prevNodeSub = head;
		head.nextNodeSub = head;
	}

	public void insertHead(Cacheable nodeSub) {
		if (nodeSub.nextNodeSub != null)
			nodeSub.unlinkSub();
		nodeSub.nextNodeSub = head.nextNodeSub;
		nodeSub.prevNodeSub = head;
		nodeSub.nextNodeSub.prevNodeSub = nodeSub;
		nodeSub.prevNodeSub.nextNodeSub = nodeSub;
	}

	public Cacheable popTail() {
		Cacheable nodeSub = head.prevNodeSub;
		if (nodeSub == head) {
			return null;
		} else {
			nodeSub.unlinkSub();
			return nodeSub;
		}
	}

	public Cacheable reverseGetFirst() {
		Cacheable nodeSub = head.prevNodeSub;
		if (nodeSub == head) {
			current = null;
			return null;
		} else {
			current = nodeSub.prevNodeSub;
			return nodeSub;
		}
	}

	public Cacheable reverseGetNext() {
		Cacheable nodeSub = current;
		if (nodeSub == head) {
			current = null;
			return null;
		} else {
			current = nodeSub.prevNodeSub;
			return nodeSub;
		}
	}

	public int getNodeCount() {
		int i = 0;
		for (Cacheable nodeSub = head.prevNodeSub; nodeSub != head; nodeSub = nodeSub.prevNodeSub)
			i++;

		return i;
	}

	private final Cacheable head;
	private Cacheable current;
}
