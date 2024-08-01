package com.osroyale.game.world.items;

/**
 * The skill-related requirement.
 * 
 * @author Daniel | Obey
 */
public class SkillRequirement implements Requirable {

	/**
	 * The level that is required.
	 */
	private final int level;

	/**
	 * The skill that is required.
	 */
	private final int skill;

	/**
	 * Creates a new {@link SkillRequirement}.
	 * 
	 * @param level
	 *            The level required.
	 * 
	 * @param skill
	 *            The skill required.
	 */
	public SkillRequirement(int level, int skill) {
		this.level = level;
		this.skill = skill;
	}

	/**
	 * @return the level
	 */
	public int getLevel() {
		return level;
	}

	/**
	 * @return the skill
	 */
	public int getSkill() {
		return skill;
	}

}