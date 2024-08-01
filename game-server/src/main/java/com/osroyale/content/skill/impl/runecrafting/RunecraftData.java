package com.osroyale.content.skill.impl.runecrafting;

import java.util.Arrays;
import java.util.Optional;

/** Holds the runecrafting data - @author Daniel */
public enum RunecraftData {
	AIR(34760, 556, 6.0D, new int[] { 1, 11, 22, 33, 44, 55, 66, 77, 88, 99 }),
	MIND(34761, 558, 6.5D, new int[] { 1, 14, 28, 42, 56, 70, 84, 98 }),
	WATER(34762, 555, 7.0D, new int[] { 5, 19, 38, 57, 76, 95 }),
	EARTH(34763, 557, 7.5D, new int[] { 9, 26, 52, 78 }),
	FIRE(34764, 554, 8.0D, new int[] { 14, 35, 70 }),
	BODY(34765, 559, 8.5D, new int[] { 20, 46, 92 }),
	COSMIC(34766, 564, 10.0D, new int[] { 27, 59 }),
	CHAOS(34769, 562, 10.5D, new int[] { 35, 74 }),
	NATURE(34768, 561, 11.0D, new int[] { 44, 91 }),
	LAW(34767, 563, 10.5D, new int[] { 54 }),
	DEATH(34770, 560, 12.0D, new int[] { 65 }),
	BLOOD(27978, 565, 12.5D, new int[] { 77 }),
	ASTRAL(34771, 9075, 21.5D, new int[] { 82 }),
	SOUL(27980, 566, 29.7, new int[] { 90 }),
	WARTH(34772, 21880, 52.5D, new int[] { 95 }),
	OURNIA(29631, -1, -1, new int[] { 55 });

	/** The object identification. */
	private final int object;

	/** The runes obtained. */
	private final int runes;

	/** The experience rewarded. */
	private final double experience;

	/** The multiplier. */
	private final int[] multiplier;

	/** The runecrafting data. */
	RunecraftData(int object, int runes, double experience, int[] multiplier) {
		this.object = object;
		this.runes = runes;
		this.experience = experience;
		this.multiplier = multiplier;
	}

	/** Gets the object identification. */
	public int getObject() {
		return object;
	}

	/** Gets the level required. */
	public int getLevel() {
		return multiplier[0];
	}

	/** Gets the multiplier. */
	public int[] getMultiplier() {
		return multiplier;
	}

	/** Gets the runes obtained. */
	public int getRunes() {
		return runes;
	}

	/** Gets the experience rewarded. */
	public double getExperience() {
		return experience;
	}

	/** Gets the runecrafting data based on the object. */
	public static Optional<RunecraftData> forId(int id) {
		return Arrays.stream(values()).filter(a -> a.object == id).findAny();
	}
}
