package com.osroyale;

public final class Cache {

	public Cache(int i) {
		emptyNodeSub = new Cacheable();
		nodeSubList = new Queue();
		initialCount = i;
		spaceLeft = i;
		nodeCache = new HashTable();
	}

	public Cacheable get(long l) {
		Cacheable nodeSub = (Cacheable) nodeCache.findNodeByID(l);
		if (nodeSub != null) {
			nodeSubList.insertHead(nodeSub);
		}
		return nodeSub;
	}

	public void put(Cacheable nodeSub, long l) {
		try {
			if (spaceLeft == 0) {
				Cacheable nodeSub_1 = nodeSubList.popTail();
				nodeSub_1.unlink();
				nodeSub_1.unlinkSub();
				if (nodeSub_1 == emptyNodeSub) {
					Cacheable nodeSub_2 = nodeSubList.popTail();
					nodeSub_2.unlink();
					nodeSub_2.unlinkSub();
				}
			} else {
				spaceLeft--;
			}
			nodeCache.removeFromCache(nodeSub, l);
			nodeSubList.insertHead(nodeSub);
			return;
		} catch (RuntimeException runtimeexception) {
			Utility.reporterror("47547, " + nodeSub + ", " + l + ", " + (byte) 2 + ", " + runtimeexception.toString());
		}
		throw new RuntimeException();
	}

	public void unlinkAll() {
		do {
			Cacheable nodeSub = nodeSubList.popTail();
			if (nodeSub != null) {
				nodeSub.unlink();
				nodeSub.unlinkSub();
			} else {
				spaceLeft = initialCount;
				return;
			}
		} while (true);
	}

	private final Cacheable emptyNodeSub;
	private final int initialCount;
	private int spaceLeft;
	private final HashTable nodeCache;
	private final Queue nodeSubList;
}
