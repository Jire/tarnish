package com.osroyale.content.skill.impl.firemaking;

import java.util.Arrays;
import java.util.Optional;

public enum FiremakingData {
	NORMAL_LOG(1511, 50, 1, 150.0D),
	ACHEY_LOG(2862, 25, 1, 40.0D),
	OAK_LOG(1521, 55, 15, 300.0D),
	WILLOW_LOG(1519, 60, 30, 450.0D),
	TEAK_LOG(6333, 80, 35, 105.0D),
	ARCTIC_PINE_LOG(10810, 100, 42, 125.0D),
	MAPLE_LOG(1517, 300, 45, 523.0D),
	MOHOGANY_LOG(6332, 400, 50, 300.5D),
	EUCALYPTUS_LOG(12581, 500, 58, 300.5D),
	YEW_LOG(1515, 750, 60, 600.5D),
	MAGIC_LOG(1513, 2500, 75, 690.9D);

	private final int log;
	
	private final int coins;

	private final int level;

	private final double exp;

	FiremakingData(int log, int coins, int level, double exp) {
		this.log = log;
		this.coins = coins;
		this.level = level;
		this.exp = exp;
	}

	public int getLog() {
		return log;
	}
	
	public int getCoins() {
		return coins;
	}

	public int getLevel() {
		return level;
	}

	public double getExperience() {
		return exp;
	}

	public static Optional<FiremakingData> forId(int id) {
		return Arrays.stream(values()).filter(a -> a.log == id).findAny();
	}
	
}