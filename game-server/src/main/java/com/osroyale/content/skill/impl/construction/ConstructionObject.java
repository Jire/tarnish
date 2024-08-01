package com.osroyale.content.skill.impl.construction;

import com.osroyale.game.world.position.Position;

public class ConstructionObject {
	
	private final int object;
	
	private final int rotation;
	
	private final Position position;
	
	public ConstructionObject(int object, int rotation, Position position) {
		this.object = object;
		this.rotation = rotation;
		this.position = position;
	}

	public int getObject() {
		return object;
	}
	
	public int getRotation() {
		return rotation;
	}
	
	public Position getPosition() {
		return position;
	}
}
