package com.osroyale.content.skill.impl.cooking;

import java.util.Arrays;
import java.util.Optional;

/**
 * Holds the cooking data.
 * 
 * @author Daniel
 */
public enum CookData {
	RAW_SHRIMP(317, 1, 34, 315, 323, 30.0D),
	SARDINE(327, 1, 38, 325, 369, 40.0D),
	ANCHOVIES(321, 1, 34, 319, 323, 30.0D),
	HERRING(345, 5, 41, 347, 353, 50.0D),
	MACKEREL(353, 10, 45, 355, 353, 60.0D),
	TROUT(335, 15, 50, 333, 343, 70.0D),
	COD(341, 18, 52, 339, 343, 75.0D),
	PIKE(349, 20, 53, 351, 343, 80.0D),
	SALMON(331, 25, 58, 329, 343, 90.0D),
	SLIMY_EEL(3379, 28, 58, 3381, 3383, 95.0D),
	TUNA(359, 30, 65, 361, 367, 100.0D),
	KARAMBWAN(3142, 30, 200, 3144, 3148, 190.0D),
	RAINBOW_FISH(10138, 35, 60, 10136, 10140, 110.0D),
	CAVE_EEL(5001, 38, 40, 4003, 5002, 115.0D),
	LOBSTER(377, 40, 74, 379, 381, 120.0D),
	BASS(363, 43, 80, 365, 367, 130.0D),
	SWORDFISH(371, 45, 86, 373, 375, 140.0D),
	LAVA_EEL(2148, 53, 53, 2149, 3383, 30.0D),
	MONKFISH(7944, 62, 92, 7946, 7948, 150.0D),
	SHARK(383, 80, 99, 385, 387, 210.0D),
	SEA_TURTLE(395, 82, 150, 397, 399, 212.0D),
	CAVEFISH(15264, 88, 150, 15266, 15268, 214.0D),
	MANTA_RAY(389, 91, 150, 391, 393, 216.0D),
	DARK_CRAB(11934, 90, 185, 11936, 11938, 225.0D);

	private final int item;
	
	private final int level;
	
	private final int cooked;
	
	private final double exp;
	
	private final int noBurn;
	
	private final int burnt;

	CookData(int item, int level, int noBurn, int cooked, int burnt, double exp) {
		this.item = item;
		this.level = level;
		this.noBurn = noBurn;
		this.cooked = cooked;
		this.burnt = burnt;
		this.exp = exp; 
	}

	public double getExp() {
		return exp;
	}

	public int getItem() {
		return item;
	}

	public int getLevel() {
		return level;
	}

	public int getCooked() {
		return cooked;
	}
	
	public int getNoBurn() {
		return noBurn;
	}
	
	public int getBurnt() {
		return burnt;
	}
	
	public static Optional<CookData> forId(int id) {
		return Arrays.stream(values()).filter(a -> a.item == id).findAny();
	}
}
