package com.osroyale.game.world.object;

/**
 * Represents a single type of object.
 *
 * @author Graham Edgecombe
 */
public class GameObjectDefinition {

	/** The maximum number of object definitions */
	public static final int MAX_DEFINITIONS = 50046;

	/** The definitions array. */
	public static final GameObjectDefinition[] definitions = new GameObjectDefinition[MAX_DEFINITIONS];

	/**
	 * Adds a definition. TODO better way?
	 *
	 * @param def
	 *            The definition.
	 */


	public static void addDefinition(GameObjectDefinition def) {
		definitions[def.getId()] = def;
	}

	/**
	 * Gets an object definition by its id.
	 *
	 * @param id
	 *            The id.
	 * @return The definition.
	 */
	public static GameObjectDefinition forId(int id) {
		if (id < 0 || id >= definitions.length) {
			return null;
		}
		return definitions[id];
	}

	/** The id. */
	private int id;

	/** The name. */
	private String name;

	/** The description. */
	private String desc;

	/** X size. */
	private int width;

	/** Y size. */
	private int length;
	/**
	 * Custom distance check
	 */
	private int distance = 1;

	/** Solid flag. */
	private boolean solid;

	/** Walkable flag. */
	private boolean impenetrable;

	/** 'Has actions' flag. */
	private boolean hasActions;

	/** The wall flag, {@code false} by default. */
	private final boolean wall;

	/** The decoration flag, {@code false} by default. */
	private final boolean decoration;

	private final int walkingFlag;

	/** Creates the definition. */
	public GameObjectDefinition(int id, String name, String desc, int width, int length, int distance, boolean solid, boolean impenetrable, boolean hasActions, boolean wall, boolean decoration, int walkingFlag) {
		this.id = id;
		this.name = name;
		this.desc = desc;
		this.width = width;
		this.length = length;
		this.distance = distance;
		this.solid = solid;
		this.impenetrable = impenetrable;
		this.hasActions = hasActions;
		this.wall = wall;
		this.decoration = decoration;
		this.walkingFlag = walkingFlag;
	}

	/** @return The id. */
	public int getId() {
		return this.id;
	}

	/** @return The name. */
	public String getName() {
		return name;
	}

	/** @return The description. */
	public String getDescription() {
		return desc;
	}

	/** @return The x size. */
	public int getWidth() {
		return width;
	}

	/** @return The y size. */
	public int getLength() {
		return length;
	}

	public int getDistance() {
		return distance;
	}

	/** @return The solid flag. */
	public boolean isSolid() {
		return solid;
	}

	/** @return The impenetrable flag. */
	public boolean isImpenetrable() {
		return impenetrable;
	}

	/** @return A flag indicating that this object has some actions. */
	public boolean hasActions() {
		return hasActions;
	}

	public boolean isWall() {
		return wall;
	}

	public boolean isDecoration() {
		return decoration;
	}

	public int getWalkingFlag() {
		return walkingFlag;
	}

	@Override
	public String toString() {
		return String.format("[id=%s, name=%s, width=%s, length=%s, distance=%s, wall=%s, impenetrable=%s, solid=%s", id, name, width, length, distance, wall, impenetrable, solid);
	}
}
