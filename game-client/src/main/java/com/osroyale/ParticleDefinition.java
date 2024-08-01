package com.osroyale;

import java.util.ArrayList;
import java.util.List;

public class ParticleDefinition {

	public static List<ParticleDefinition> cache = new ArrayList<>();

	static {
		// definition id 0
		final ParticleDefinition maxCape = new ParticleDefinition();
		maxCape.setStartVelocity(new ParticleVector(0, -1, 0));
		maxCape.setEndVelocity(new ParticleVector(0, -1, 0));
		maxCape.setGravity(new ParticleVector(0, 2 / 4, 0));
		maxCape.setLifeSpan(19);
		maxCape.setStartColor(0x49120d);
		maxCape.setSpawnRate(3);
		maxCape.setStartSize(1.10f);
		maxCape.setEndSize(0);
		maxCape.setStartAlpha(0.095f);
		maxCape.updateSteps();
		maxCape.setColorStep(0x000000);
		cache.add(maxCape);

		//1
		final ParticleDefinition partyhat = new ParticleDefinition();
		partyhat.setStartVelocity(new ParticleVector(0, -1, 0));
		partyhat.setEndVelocity(new ParticleVector(0, -1, 0));
		partyhat.setGravity(new ParticleVector(0, 2 / 4, 0));
		partyhat.setLifeSpan(19);
		partyhat.setStartColor(0xff0000);
		partyhat.setSpawnRate(3);
		partyhat.setStartSize(0.75f);
		partyhat.setEndSize(0);
		partyhat.setStartAlpha(0.095f);
		partyhat.updateSteps();
		partyhat.setColorStep(0x000000);
		cache.add(partyhat);

	}


	private final PointSpawnShape spawnShape = new PointSpawnShape();
	private ParticleVector gravity;
	private float startSize = 1f;
	private float endSize = 1f;
	private int startColor = -1;
	private ParticleVector startVelocity = ParticleVector.EMPTY;
	private ParticleVector endVelocity = ParticleVector.EMPTY;
	private float startAlpha = 1f;
	private final float endAlpha = 0.05f;
	private int lifeSpan = 1;
	private int spawnRate = 1;
	private ParticleVector velocityStep;
	private int colorStep;
	private float sizeStep;
	private float alphaStep;

	public int randomWithRange(int min, int max) {
		int range = (max - min) + 1;
		return (int) (Math.random() * range) + min;
	}

	public final ParticleVector getStartVelocity() {
		return new ParticleVector(this.startVelocity.getX() + randomWithRange(-1, 1), this.startVelocity.getY(), this.startVelocity.getZ() + randomWithRange(-1, 1));
	}

	private void setStartVelocity(ParticleVector startVelocity) {
		this.startVelocity = startVelocity;
	}

	public final void updateSteps() {
		this.sizeStep = (endSize - startSize) / (lifeSpan * 1f);
		this.colorStep = startColor / lifeSpan;
		this.velocityStep = endVelocity.subtract(startVelocity).divide(lifeSpan);
		this.alphaStep = (endAlpha - startAlpha) / lifeSpan;
	}

	public void setColorStep(int colorStep) {
		this.colorStep = colorStep;
	}

	private void setStartAlpha(float startAlpha) {
		this.startAlpha = startAlpha;
	}

	private void setEndSize(int endSize) {
		this.endSize = endSize;
	}

	private void setStartSize(float startSize) {
		this.startSize = startSize;
	}

	private void setStartColor(int startColor) {
		this.startColor = startColor;
	}

	private void setLifeSpan(int lifeSpan) {
		this.lifeSpan = lifeSpan;
	}

	private void setGravity(ParticleVector gravity) {
		this.gravity = gravity;
	}

	private void setEndVelocity(ParticleVector endVelocity) {
		this.endVelocity = endVelocity;
	}

	public void setVelocityStep(ParticleVector velocityStep) {
		this.velocityStep = velocityStep;
	}

	public ParticleVector getVelocityStep() {
		return velocityStep;
	}

	public int getColorStep() {
		return colorStep;
	}

	public int getSpawnRate() {
		return spawnRate;
	}

	public void setSpawnRate(int spawnRate) {
		this.spawnRate = spawnRate;
	}

	public int getLifeSpan() {
		return lifeSpan;
	}

	public float getSizeStep() {
		return sizeStep;
	}

	public float getAlphaStep() {
		return alphaStep;
	}

	public ParticleVector getGravity() {
		return gravity;
	}

	public int getStartColor() {
		return startColor;
	}

	public PointSpawnShape getSpawnShape() {
		return spawnShape;
	}

	public float getStartAlpha() {
		return startAlpha;
	}

	public float getStartSize() {
		return startSize;
	}
}