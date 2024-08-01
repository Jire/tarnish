package com.osroyale.game;

import java.util.Objects;

/**
 * Represents a single graphic that can be used by entities. Also known as GFX.
 * 
 * @author Michael | Chex
 */
public final class Graphic implements Comparable<Graphic> {

	public static final int HIGH_HEIGHT = 0x640000;

	public static final int NORMAL_ID = 308;
	public static final int LUNAR_ID = 747;
	public static final int RESET_ID = 0xFFFF;
	public static final Graphic RESET = new Graphic(RESET_ID, UpdatePriority.VERY_LOW);
	public static final Graphic NORMAL_TELE = new Graphic(NORMAL_ID, 43, UpdatePriority.VERY_HIGH);
	public static final Graphic LUNAR_TELE = new Graphic(LUNAR_ID, 0, UpdatePriority.VERY_HIGH);

	/** The graphic id. */
	private final int id;

	/** The delay of this graphic. */
	private final int delay;

	/** The height of this graphic. */
	private final int height;

	/** The priority of the graphic. */
	private final UpdatePriority priority;

	/**
	 * Constructs a new {@code Graphic} object with no delay, a low height, and
	 * normal priotiry.
	 * 
	 * @param id
	 *            The graphic id.
	 */
	public Graphic(int id) {
		this(id, 0, false);
	}

	/**
	 * Constructs a new {@code Graphic} object with no delay and normal
	 * priotiry.
	 * 
	 * @param id
	 *            The graphic id.
	 * 
	 * @param high
	 *            The graphic height state.
	 */
	public Graphic(int id, boolean high) {
		this(id, 0, high);
	}

	/**
	 * Constructs a new {@code Graphic} object with a low height and normal
	 * priotiry.
	 * 
	 * @param id
	 *            The graphic id.
	 * 
	 * @param delay
	 *            The graphic delay.
	 */
	public Graphic(int id, int delay) {
		this(id, delay, false);
	}

	/**
	 * Constructs a new {@code Graphic} object with a normal priotiry.
	 * 
	 * @param id
	 *            The graphic id.
	 * 
	 * @param delay
	 *            The graphic delay.
	 * 
	 * @param high
	 *            The graphic height state.
	 */
	public Graphic(int id, int delay, boolean high) {
		this(id, delay, high, UpdatePriority.NORMAL);
	}

	/**
	 * Constructs a new {@code Graphic} object with no delay and a low height.
	 * 
	 * @param id
	 *            The graphic id.
	 * @param priority
	 *            The graphic priority.
	 */
	public Graphic(int id, UpdatePriority priority) {
		this(id, 0, false, priority);
	}

	/**
	 * Constructs a new {@code Graphic} object with no delay.
	 * 
	 * @param id
	 *            The graphic id.
	 * 
	 * @param high
	 *            The graphic height state.
	 * @param priority
	 *            The graphic priority.
	 */
	public Graphic(int id, boolean high, UpdatePriority priority) {
		this(id, 0, high, priority);
	}

	/**
	 * Constructs a new {@code Graphic} object with a low height.
	 * 
	 * @param id
	 *            The graphic id.
	 * 
	 * @param delay
	 *            The graphic delay.
	 * @param priority
	 *            The graphic priority.
	 */
	public Graphic(int id, int delay, UpdatePriority priority) {
		this(id, delay, false, priority);
	}

	/**
	 * Constructs a new {@code Graphic} object.
	 * 
	 * @param id
	 *            The graphic id.
	 * 
	 * @param delay
	 *            The graphic delay.
	 * 
	 * @param high
	 *            The graphic height state.
	 * @param priority
	 *            The graphic priority.
	 */
	public Graphic(int id, int delay, boolean high, UpdatePriority priority) {
		this(id, delay, high ? HIGH_HEIGHT : 0, priority);
	}

	public Graphic(int id, int delay, int height, UpdatePriority priority) {
		this.id = id;
		this.delay = delay;
		this.height = height;
		this.priority = priority;
	}

	public Graphic(int id, int delay, int height) {
		this.id = id;
		this.delay = delay;
		this.height = height;
		this.priority = UpdatePriority.NORMAL;
	}

	/**
	 * Gets the delay of this graphic.
	 * 
	 * @return delay
	 */
	public int getDelay() {
		return delay;
	}

	/**
	 * Gets the height of this graphic.
	 * 
	 * @return height
	 */
	public int getHeight() {
		return height;
	}

	/**
	 * Gets the id of this graphic.
	 * 
	 * @return id
	 */
	public int getId() {
		return id;
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(id, delay, height, priority);
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Graphic) {
			Graphic other = (Graphic) obj;
			return id == other.id && height == other.height && delay == other.delay && priority == other.priority;
		}
		return obj == this;
	}

	@Override
	public int compareTo(Graphic other) {
		return other.priority.ordinal() - priority.ordinal();
	}

	@Override
	public String toString() {
		return String.format("Graphic[id=%s, delay=%s, height=%s, priority=%s]", id, delay, height, priority);
	}

}
