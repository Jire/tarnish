package com.osroyale.game.world.position.impl;

import com.osroyale.game.world.position.Area;
import com.osroyale.game.world.position.Position;

/**
 * The location type that models any area in a circle or oval shape.
 *
 * @author lare96 <http://github.com/lare96>
 */
public class CircleArea extends Area {

	private final String name;

	private final int x;

	private final int y;

	private final int z;

	private final int radius;

	public CircleArea(String name, int x, int y, int radius) {
		this(name, x, y, 0, radius);
	}

	public CircleArea(String name, int x, int y, int height, int radius) {
		this.name = name;
		this.x = x;
		this.y = y;
		this.z = height;
		this.radius = radius;
	}

	public int getHeight() {
		return z;
	}

	public String getName() {
		return name;
	}

	public int getRadius() {
		return radius;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	@Override
	public boolean inArea(Position position) {
		if (position.getHeight() != z) {
			return false;
		}
		return Math.pow((position.getX() - x), 2) + Math.pow((position.getY() - y), 2) <= Math.pow(radius, 2);
	}

	@Override
	public Position getRandomLocation() {
		return null;
	}

}
