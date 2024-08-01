package com.osroyale;

public class PointSpawnShape {

	private final ParticleVector vector;

	public PointSpawnShape() {
		this.vector = ParticleVector.EMPTY;
	}

	public final ParticleVector vector() {
		return vector;
	}
}