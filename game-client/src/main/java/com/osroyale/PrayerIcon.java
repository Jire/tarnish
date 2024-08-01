package com.osroyale;

public enum PrayerIcon {

	/**
	 * The Thick Skin prayer.
	 */
	THICK_SKIN(1, 83, "Thick Skin", "Increases your defence by 5%."),

	/**
	 * The Burst of Strength prayer.
	 */
	BURST_OF_STRENGTH(4, 84, "Burst of Strength", "Increases your strength by 5%."),

	/**
	 * The Clarity of Thought prayer.
	 */
	CLARITY_OF_THOUGHT(7, 85, "Clarity of Thought", "Increases your attack by 5%."),

	/**
	 * The Sharp Eye prayer.
	 */
	SHARP_EYE(8, 86, "Sharp Eye", "Increases your ranging by 5%."),

	/**
	 * The Mystic Will prayer.
	 */
	MYSTIC_WILL(9, 87, "Mystic Will", "Increases your magical \\nattack and defence by 5%."),

	/**
	 * The Rock Skin prayer.
	 */
	ROCK_SKIN(10, 88, "Rock Skin", "Increases defence by 10%."),

	/**
	 * The Superhuman Strength prayer.
	 */
	SUPERHUMAN_STRENGTH(13, 89, "Superhuman Strength", "Increases strength by 10%."),

	/**
	 * The Improved Reflexes prayer.
	 */
	IMPOROVED_REFLEXES(16, 90, "Improved Reflexes", "Increases attack by 10%."),

	/**
	 * The Rapid Restore prayer.
	 */
	RAPID_RESTORE(19, 91, "Rapid Restore", "2x restore rate for all stats \\nexcept Hitpoints and Prayer."),

	/**
	 * The Rapid Heal prayer.
	 */
	RAPID_HEAL(22, 92, "Rapid Heal", "2x restore rate for \\nHitpoints stat."),

	/**
	 * The Protect Item prayer.
	 */
	PROTECT_ITEM(25, 93, "Protect Item", "Keep 1 extra item if you die."),

	/**
	 * The Hawk Eye prayer.
	 */
	HAWK_EYE(26, 94, "Hawk Eye", "Increases your Ranging by 10%."),

	/**
	 * The Mystic Lore prayer.
	 */
	MYSTIC_LORE(27, 95, "Mystic Lore", "Increases your magical attack \\nand defence by 10%."),

	/**
	 * The Steel Skin prayer.
	 */
	STEEL_SKIN(28, 96, "Steel Skin", "Increases your defence by 15%."),

	/**
	 * The Ultimate strength prayer.
	 */
	ULTIMATE_strength(31, 97, "Ultimate strength", "Increases your strength by \\n15%."),

	/**
	 * The Incredible Reflexes prayer.
	 */
	INCREDIBLE_REFLEXES(34, 98, "Incredible Reflexes", "Increases your attack by 15%."),

	/**
	 * The Protect from Magic prayer.
	 */
	PROTECT_FROM_MAGIC(37, 99, "Protect from Magic", "Protection from magical attacks."),

	/**
	 * The Protect from Missles prayer.
	 */
	PROTECT_FROM_MISSILES(40, 100, "Protect from Missles", "Protection from ranged attacks."),

	/**
	 * The Protect from Melee prayer.
	 */
	PROTECT_FROM_MELEE(43, 101, "Protect from Melee", "Protection from close attacks."),

	/**
	 * The Eagle Eye prayer.
	 */
	EAGLE_EYE(44, 102, "Eagle Eye", "Increases your Ranging by 15%."),

	/**
	 * The Mystic Might prayer.
	 */
	MYSTIC_MIGHT(45, 103, "Mystic Might", "Increases your magical attack \\nand defence by 15%."),

	/**
	 * The Retribution prayer.
	 */
	RETRIBUTION(46, 104, "Retribution", "Inflicts damage to \\nnearby targets if you die."),

	/**
	 * The Redemption prayer.
	 */
	REDEMPTION(49, 105, "Redemption", "Heals you when damaged \\nand health falls \\nbelow 10%."),

	/**
	 * The Smite prayer.
	 */
	SMITE(52, 106, "Smite", "1/4 of damage dealt is \\nalso removed from \\nopponent's Prayer."),

	/**
	 * The Preserve prayer.
	 */
	PRESERVE(55, 107, "Preserve", "Boosted stats last 20% longer."),

	/**
	 * The Chivalry prayer.
	 */
	CHIVALRY(60, 108, "Chivalry", "Increases your defence by \\n20%, strength by 18%, and \\nattack by 15%."),

	/**
	 * The Piety prayer.
	 */
	PIETY(70, 109, "Piety", "Increases your defence by \\n25%, strength by 23%, and \\nattack by 20%."),

	/**
	 * The Rigour prayer.
	 */
	RIGOUR(74, 110, "Rigour", "Increases your ranged attack \\nby 20% and damage by 23%, \\nand your defence by 25%."),

	/**
	 * The Augury prayer.
	 */
	AUGURY(77, 111, "Augury", "Increases your magical attack \\nand defence by 25%, and \\nyour defence by 25%."),;

	/**
	 * The prayer required level.
	 */
	private int level;

	/**
	 * The config id.
	 */
	private int config;

	/**
	 * The prayer name.
	 */
	private String name;

	/**
	 * The prayer description.
	 */
	private String description;

	/**
	 * Construct a new {@link PrayerIcon} object instance.
	 *
	 * @param level       the prayer required level.
	 * @param config      the prayer config id.
	 * @param name        the prayer name.
	 * @param description the prayer description.
	 */
	PrayerIcon(int level, int config, String name, String description) {
		this.name = name;
		this.description = description;
		this.level = level;
		this.config = config;
	}

	/**
	 * Gets the prayer required level.
	 *
	 * @return the prayer required level.
	 */
	public int getLevel() {
		return level;
	}

	/**
	 * Gets the prayer config.
	 *
	 * @return the prayer config.
	 */
	public int getConfig() {
		return config;
	}

	/**
	 * Gets the prayer name.
	 *
	 * @return the prayer name.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Gets the prayer description.
	 *
	 * @return the prayer description.
	 */
	public String getDescription() {
		return description;
	}

}

