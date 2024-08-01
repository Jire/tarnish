package com.osroyale;

final class HashTable {

	public HashTable() {
		int i = 1024;// was parameter
		size = i;
		cache = new Linkable[i];
		for (int k = 0; k < i; k++) {
			Linkable node = cache[k] = new Linkable();
			node.prev = node;
			node.next = node;
		}

	}

	public Linkable findNodeByID(long l) {
		Linkable node = cache[(int) (l & (long) (size - 1))];
		for (Linkable node_1 = node.prev; node_1 != node; node_1 = node_1.prev)
			if (node_1.id == l)
				return node_1;

		return null;
	}

	public void removeFromCache(Linkable node, long l) {
		try {
			if (node.next != null)
				node.unlink();
			Linkable node_1 = cache[(int) (l & (long) (size - 1))];
			node.next = node_1.next;
			node.prev = node_1;
			node.next.prev = node;
			node.prev.next = node;
			node.id = l;
			return;
		} catch (RuntimeException runtimeexception) {
			Utility.reporterror("91499, " + node + ", " + l + ", " + (byte) 7 + ", " + runtimeexception.toString());
		}
		throw new RuntimeException();
	}

	private final int size;
	private final Linkable[] cache;
}
