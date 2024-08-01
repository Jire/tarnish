package com.osroyale;

/**
 * Holds all the skill constants
 *
 * @author Daniel
 */
final class SkillConstants {

	/**  The total skill count. */
	static final int SKILL_COUNT = 23;

	/** An array of skill names.  */

	static final String[] SKILL_NAMES = {
	    /* 00 */ "Attack",
        /* 01 */ "Defence",
        /* 02 */ "Strength",
		/* 03 */ "Hitpoints",
		/* 04 */ "Ranged",
		/* 05 */ "Prayer",
		/* 06 */ "Magic",
		/* 07 */ "Cooking",
		/* 08 */ "Woodcutting",
		/* 09 */ "Fletching",
		/* 10 */ "Fishing",
		/* 11 */ "Firemaking",
		/* 12 */ "Crafting",
		/* 13 */ "Smithing",
		/* 14 */ "Mining",
		/* 15 */ "Herblore",
		/* 16 */ "Agility",
		/* 17 */ "Thieving",
		/* 18 */ "Slayer",
		/* 19 */ "Farming",
		/* 20 */ "Runecrafting",
		/* 21 */ "Construction",
		/* 22 */ "Hunter",};

	/**
	 * All the skills enabled.
	 */
	static final boolean[] SKILLS_ENABLED = {
            /* 00 */ true,
            /* 01 */ true,
            /* 02 */ true,
            /* 03 */ true,
            /* 04 */ true,
            /* 05 */ true,
            /* 06 */ true,
            /* 07 */ true,
            /* 08 */ true,
            /* 09 */ true,
            /* 10 */ true,
            /* 11 */ true,
            /* 12 */ true,
            /* 13 */ true,
            /* 14 */ true,
            /* 15 */ true,
            /* 16 */ true,
            /* 17 */ true,
            /* 18 */ true,
            /* 19 */ true,
            /* 20 */ true,
            /* 21 */ true,
            /* 22 */ true,};

	/**
	 * The experience for each level.
	 */
	private static final int[] EXP_FOR_LEVEL =
			{0, 83, 174, 276, 388, 512, 650, 801, 969, 1154, 1358, 1584, 1833, 2107,
			2411, 2746, 3115, 3523, 3973, 4470, 5018, 5624, 6291, 7028, 7842, 8740,
			9730, 10824, 12031, 13363, 14833, 16456, 18247, 20224, 22406, 24815, 27473,
					30408, 33648, 37224, 41171, 45529, 50339, 55649, 61512, 67983, 75127, 83014,
					91721, 101333, 111945, 123660, 136594, 150872, 166636, 184040, 203254, 224466,
					247886, 273742, 302288, 333804, 368599, 407015, 449428, 496254, 547953, 605032,
					668051, 737627, 814445, 899257, 992895, 1096278, 1210421, 1336443, 1475581,
					1629200, 1798808, 1986068, 2192818, 2421087, 2673114, 2951373, 3258594, 3597792,
					3972294, 4385776, 4842295, 5346332, 5902831, 6517253, 7195629, 7944614, 8771558,
					9684577, 10692629, 11805606, 13034431};

	private static byte binarySearch(double experience, int min, int max) {
		final int mid = (min + max) / 2;
		final double value = EXP_FOR_LEVEL[mid];
		if (value > experience) {
			return binarySearch(experience, min, mid - 1);
		} else if (value == experience || EXP_FOR_LEVEL[mid + 1] > experience) {
			return (byte) (mid + 1);
		}
		return binarySearch(experience, mid + 1, max);
	}

	static int getExperienceForLevel(int level) {
		if (level >= 99)
			return EXP_FOR_LEVEL[98];
		if (level < 1)
			return 0;
		return EXP_FOR_LEVEL[level - 1];
	}

	public static byte getLevelForExperience(double experience) {
		if (experience >= EXP_FOR_LEVEL[98])
			return 99;
		return binarySearch(experience, 0, 98);
	}
}
