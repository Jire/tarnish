package com.osroyale.content.skill.impl.hunter.net.impl;

import java.util.Arrays;
import java.util.Optional;

/**
 * Holds all the impling data.
 * 
 * @author Daniel
 */
public enum Impling {
	BABY_IMPLING(1635, 1, 25, 11238, 20),
	YOUNG_IMPLING(1636, 22, 65, 11240, 25),
	GOURMET_IMPLING(1637, 28, 113, 11242, 30),
	EARTH_IMPLING(1638, 36, 177, 11244, 35),
	ESSENCE_IMPLING(1639, 42, 255, 11246, 40),
	ECLECTIC_IMPLING(1640, 50, 289, 11248, 45),
	NATURE_IMPLING(1641, 58, 353, 11250, 50),
	MAGPIE_IMPLING(1642, 65, 409, 11252, 55),
	NINJA_IMPLING(1643, 74, 481, 11254, 60),
	DRAGON_IMPLING(1644, 83, 553, 11256, 180),
	DRAGON_IMPLING2(1654, 83, 553, 11256, 180);

	public final int impling;
	public final int level;
	public final int experience;
	public final int reward;
	public final int delay;

	Impling(int impling, int level, int experience, int reward, int delay) {
		this.impling = impling;
		this.level = level;
		this.experience = experience;
		this.reward = reward;
		this.delay = delay;
	}

	public static Optional<Impling> forId(int impling) {
		return Arrays.stream(values()).filter(a -> a.impling == impling).findAny();
	}
}