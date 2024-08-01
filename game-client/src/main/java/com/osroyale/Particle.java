package com.osroyale;

public class Particle {

	private final ParticleDefinition def;
	private final ParticleVector velocity;
	private final ParticleVector position;

	private int age;
	private int color;
	private float size;
	private float alpha;
	private boolean dead;

	public Particle(ParticleDefinition def, ParticleVector position) {
		this.def = def;
		this.age = 0;
		this.dead = false;
		this.color = def.getStartColor();
		this.size = def.getStartSize();
		this.velocity = def.getStartVelocity();
		this.position = def.getSpawnShape().vector().copy().addLocal(position);
		this.alpha = def.getStartAlpha();
	}

	public final void tick() {
		if ((++age) >= def.getLifeSpan()) {
			dead = true;
		} else {
			color += def.getColorStep();
			size += def.getSizeStep();
			position.addLocal(velocity);
			velocity.addLocal(def.getVelocityStep());
			alpha += def.getAlphaStep();
			if (def.getGravity() != null) {
				position.addLocal(def.getGravity());
			}
			alpha = Math.max(0f, alpha);
		}
	}

	public boolean isDead() {
		return dead;
	}

	public ParticleVector getPosition() {
		return position;
	}

	public float getSize() {
		return size;
	}

	public int getColor() {
		return color;
	}

	public float getAlpha() {
		return alpha;
	}
}