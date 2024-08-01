package com.osroyale;

import net.runelite.rs.api.RSVertexNormal;

final class VertexNormal implements RSVertexNormal {

	VertexNormal() {

	}

	VertexNormal(VertexNormal other) {
		this.x = other.x;
		this.y = other.y;
		this.z = other.z;
		this.magnitude = other.magnitude;
	}

	int x;
	int y;
	int z;
	int magnitude;

	@Override
	public int getX() {
		return x;
	}

	@Override
	public int getY() {
		return y;
	}

	@Override
	public int getZ() {
		return z;
	}
}
