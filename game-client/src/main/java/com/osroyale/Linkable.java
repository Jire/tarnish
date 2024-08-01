package com.osroyale;

import net.runelite.rs.api.RSNode;

public class Linkable implements RSNode {

	@Override
	public RSNode getNext() {
		return next;
	}

	@Override
	public long getHash() {
		return id;
	}

	@Override
	public RSNode getPrevious() {
		return prev;
	}

	public final void unlink() {
		if (next == null) {

		} else {
			next.prev = prev;
			prev.next = next;
			prev = null;
			next = null;
		}
	}

	@Override
	public void onUnlink() {

	}

	public long id;
	public Linkable prev;
	public Linkable next;
}
