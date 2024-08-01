package com.osroyale.content.skill.impl.construction;

import com.osroyale.game.world.entity.mob.Direction;
import com.osroyale.game.world.entity.mob.player.Player;
import com.osroyale.game.world.object.CustomGameObject;
import com.osroyale.game.world.position.Position;
import com.osroyale.util.generic.GenericVoid;

public enum BuildableMap implements GenericVoid<Player> {
	SMALL_CAVE("Small cave", 0, 0, new Position(3305, 9833)) {
		@Override
		public void execute(Player player) {
			player.face(Direction.SOUTH);
			new CustomGameObject(4525, new Position(3305, 9832, player.house.getHeight())).register();//Portal
		}
	},
	THRONE_ROOM("Throne room", 90, 15_000000, new Position(2036, 4538)) {
		@Override
		public void execute(Player player) {
			player.face(Direction.SOUTH);
			new CustomGameObject(4525, new Position(2036, 4539, player.house.getHeight())).register();//Portal
		}
	};

	private final String name;
	private final int level;
	private final int cost;
	private final Position position;
	
	BuildableMap(String name, int level, int cost, Position position) {
		this.name = name;
		this.level = level;
		this.cost = cost;
		this.position = position;
	}
	
	public String getName() {
		return name;
	}
	
	public int getLevel() {
		return level;
	}
	
	public int getCost() {
		return cost;
	}
	
	public Position getPosition() {
		return position;
	}
}
