package com.osroyale.game.world.entity.combat;

public class CombatConstants {
	/** The amount of time it takes in seconds for cached damage to timeout. */
	public static final long DAMAGE_CACHE_TIMEOUT = 60;
	
	/** The amount of time it takes in seconds for combat to reset. */
	public static final int COMBAT_TIMER_COOLDOWN = 4_200;

	/** The amount of time it takes in milliseconds it takes to logout. */
	public static final int COMBAT_LOGOUT_COOLDOWN = 10_000;

	public static final String[] SKILL_REQUIREMENT_CONFIG_FIELD_NAMES = {
		"req-attack",
		"req-defence",
		"req-strength",
		"req-hitpoints",
		"req-ranged",
		"req-prayer",
		"req-magic",
		"req-cooking",
		"req-woodcutting",
		"req-fletching",
		"req-fishing",
		"req-firemaking",
		"req-crafting",
		"req-smithing",
		"req-mining",
		"req-herblore",
		"req-agility",
		"req-thieving",
		"req-slayer",
		"req-farming",
		"req-runecrafting",
		"req-construction",
		"req-hunter"
	};

	public static final String[] BONUS_CONFIG_FIELD_NAMES = {
		// Accuracy bonuses
		"attack-stab",
		"attack-slash",
		"attack-crush",
		"attack-magic",
		"attack-ranged",

		// Defensive bonuses
		"defence-stab",
		"defence-slash",
		"defence-crush",
		"defence-magic",
		"defence-ranged",

		// Other bonuses
		"bonus-strength",
		"ranged-strength",
		"magic-damage",
		"bonus-prayer"
	};

	public static final String[] SKILL_FIELD_NAMES = {
		"attack-level",
		"defence-level",
		"strength-level",
		"hitpoints-level",
		"ranged-level",
		"prayer-level",
		"magic-level"
	};

	public  static final int[] EMPTY_REQUIREMENTS = new int[SKILL_REQUIREMENT_CONFIG_FIELD_NAMES.length];
	public static final int[] EMPTY_BONUSES = new int[BONUS_CONFIG_FIELD_NAMES.length];
	public static final int[] EMPTY_SKILLS = new int[SKILL_FIELD_NAMES.length];



	/**
	 * The default constructor.
	 * @throws UnsupportedOperationException if this class is instantiated.
	 */
	private CombatConstants() {
		throw new UnsupportedOperationException("This class cannot be instantiated!");
	}
}
