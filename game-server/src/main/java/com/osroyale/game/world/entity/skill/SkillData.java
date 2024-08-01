package com.osroyale.game.world.entity.skill;

import com.osroyale.util.Utility;

/**
 * The enumerated type whose elements represent data for the skills.
 *
 * @author lare96 <http://github.com/lare96>
 */
public enum SkillData {
	ATTACK(Skill.ATTACK, 6248, 6249, 6247, true),//correct
	DEFENCE(Skill.DEFENCE, 6254, 6255, 6253, true),//correct
	STRENGTH(Skill.STRENGTH, 6207, 6208, 6206, true),//correct
	HITPOINTS(Skill.HITPOINTS, 6217, 6218, 6216, true),//correct
	RANGED(Skill.RANGED, 5453, 6114, 4443, true),//correct
	PRAYER(Skill.PRAYER, 6243, 6244, 6242, true),//correct
	MAGIC(Skill.MAGIC, 6212, 6213, 6211, true),//correct
	COOKING(Skill.COOKING, 6227, 6228, 6226, false),//correct
	WOODCUTTING(Skill.WOODCUTTING, 4273, 4274, 4272, false),//correct
	FLETCHING(Skill.FLETCHING, 6232, 6233, 6231, false),//correct
	FISHING(Skill.FISHING, 6259, 6260, 6258, false),//correct
	FIREMAKING(Skill.FIREMAKING, 4283, 4284, 4282, false),//correct
	CRAFTING(Skill.CRAFTING, 6264, 6265, 6263, false),//correct
	SMITHING(Skill.SMITHING, 6222, 6223, 6221, false),//correct
	MINING(Skill.MINING, 4417, 4438, 4416, false),//correct
	HERBLORE(Skill.HERBLORE, 6238, 6239, 6237, false),//correct
	AGILITY(Skill.AGILITY, 4278, 4279, 4277, false),//correct
	THIEVING(Skill.THIEVING, 4263, 4264, 4261, false),//correct
	SLAYER(Skill.SLAYER, 12123, 12124, 12122, false),//correct
	FARMING(Skill.FARMING, 313, 312, 310, false),//correct
	RUNECRAFTING(Skill.RUNECRAFTING, 4268, 4269, 4267, false),//correct
	CONSTRUCTION(Skill.CONSTRUCTION, 4268, 4269, 4267, false),//missing
	HUNTER(Skill.HUNTER, 313, 312, 310, false);//correct

	/** The identification for this skill in the skills array. */
	private final int id;

	/** The first line that level up text will be printed on. */
	private final int firstLine;

	/** The second line that level up text will be printed on. */
	private final int secondLine;

	/** The chatbox itemcontainer displayed on level up. */
	private final int chatbox;
	
	/** The state of the skill being related to combat. */
	private final boolean combatSkill;
	
	/**
	 * Creates a new {@code SkillData}.
	 *
	 * @param id
	 *            the identification for this skill in the skills array.
	 * @param firstLine
	 *            the first line that level up text will be printed on.
	 * @param secondLine
	 *            the second line that level up text will be printed on.
	 * @param chatbox
	 *            the chatbox itemcontainer displayed on level up.
	 * @param combatSkill
	 *            the state of the skill being related to combat.
	 */
    SkillData(int id, int firstLine, int secondLine, int chatbox, boolean combatSkill) {
		this.id = id;
		this.firstLine = firstLine;
		this.secondLine = secondLine;
		this.chatbox = chatbox;
		this.combatSkill = combatSkill;
	}

	/**
	 * Gets the identification for this skill in the skills array.
	 *
	 * @return the identification for this skill.
	 */
	public final int getId() {
		return id;
	}

	/**
	 * Gets the first line that level up text will be printed on.
	 *
	 * @return the first line.
	 */
	public final int getFirstLine() {
		return firstLine;
	}

	/**
	 * Gets the second line that level up text will be printed on.
	 *
	 * @return the second line.
	 */
	public final int getSecondLine() {
		return secondLine;
	}

	/**
	 * Gets the chatbox itemcontainer displayed on level up.
	 *
	 * @return the chatbox itemcontainer.
	 */
	public final int getChatbox() {
		return chatbox;
	}
	
	/**
	 * Gets if the skill is combat related.
	 *
	 * @return the state.
	 */
	public final boolean isCombatSkill() {
		return combatSkill;
	}

	@Override
	public final String toString() {
		return Utility.capitalizeSentence(name().toLowerCase().replace("_", " "));
	}
}
