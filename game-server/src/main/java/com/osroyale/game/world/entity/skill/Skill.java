package com.osroyale.game.world.entity.skill;

import com.osroyale.content.event.EventDispatcher;
import com.osroyale.content.event.InteractionEvent;
import com.osroyale.content.event.InteractionEvent.InteractionType;
import com.osroyale.content.event.InteractionEventListener;
import com.osroyale.content.event.impl.*;
import com.osroyale.game.world.entity.mob.player.Player;
import com.osroyale.util.Stopwatch;

import java.util.function.Function;

/**
 * Represents a trainable and usable skill.
 *
 * @author Michael | Chex
 */
public class Skill implements InteractionEventListener {

    /** The attack skill id. */
    public static final int ATTACK = 0;

    /** The defence skill id. */
    public static final int DEFENCE = 1;

    /** The strength skill id. */
    public static final int STRENGTH = 2;

    /** The hitpoints skill id. */
    public static final int HITPOINTS = 3;

    /** The ranged skill id. */
    public static final int RANGED = 4;

    /** The prayer skill id. */
    public static final int PRAYER = 5;

    /** The magic skill id. */
    public static final int MAGIC = 6;

    /** The cooking skill id. */
    public static final int COOKING = 7;

    /** The woodcutting skill id. */
    public static final int WOODCUTTING = 8;

    /** The fletching skill id. */
    public static final int FLETCHING = 9;

    /** The fishing skill id. */
    public static final int FISHING = 10;

    /** The firemaking skill id. */
    public static final int FIREMAKING = 11;

    /** The crafting skill id. */
    public static final int CRAFTING = 12;

    /** The smithing skill id. */
    public static final int SMITHING = 13;

    /** The mining skill id. */
    public static final int MINING = 14;

    /** The herblore skill id. */
    public static final int HERBLORE = 15;

    /** The agility skill id. */
    public static final int AGILITY = 16;

    /** The thieving skill id. */
    public static final int THIEVING = 17;

    /** The slayer skill id. */
    public static final int SLAYER = 18;

    /** The farming skill id. */
    public static final int FARMING = 19;

    /** The runecrafting skill id. */
    public static final int RUNECRAFTING = 20;

    /** The construction skill id. */
    public static final int CONSTRUCTION = 21;

    /** The hunter skill id. */
    public static final int HUNTER = 22;

    /** The amount of available skills. */
    public static final int SKILL_COUNT = 23;

    /** The total skill amount. */
    public static final int TOTAL_SKILL_LEVEL = 99 * SKILL_COUNT;

    /** An array of skill names. */
    private static final String[] SKILL_NAMES = new String[]{
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
		/* 22 */ "Hunter",
    };

    /**
     * An array with the index being the skill level (from [0, 99]) and the
     * value being the minimum experience requirement for that level.
     */
    private static final int[] EXP_FOR_LEVEL = {
            0, 83, 174, 276, 388, 512, 650, 801, 969, 1154, 1358, 1584, 1833, 2107,
            2411, 2746, 3115, 3523, 3973, 4470, 5018, 5624, 6291, 7028, 7842, 8740,
            9730, 10824, 12031, 13363, 14833, 16456, 18247, 20224, 22406, 24815, 27473,
            30408, 33648, 37224, 41171, 45529, 50339, 55649, 61512, 67983, 75127, 83014,
            91721, 101333, 111945, 123660, 136594, 150872, 166636, 184040, 203254, 224466,
            247886, 273742, 302288, 333804, 368599, 407015, 449428, 496254, 547953, 605032,
            668051, 737627, 814445, 899257, 992895, 1096278, 1210421, 1336443, 1475581,
            1629200, 1798808, 1986068, 2192818, 2421087, 2673114, 2951373, 3258594, 3597792,
            3972294, 4385776, 4842295, 5346332, 5902831, 6517253, 7195629, 7944614, 8771558,
            9684577, 10692629, 11805606, 13034431
    };

    /** Skill tab string data. */
    public static final int[][] INTERFACE_DATA = {
            {4004, 4005},
            {4008, 4009},
            {4006, 4007},
            {4016, 4017},
            {4010, 4011},
            {4012, 4013},
            {4014, 4015},
            {4034, 4035},
            {4038, 4039},
            {4026, 4027},
            {4032, 4033},
            {4036, 4037},
            {4024, 4025},
            {4030, 4031},
            {4028, 4029},
            {4020, 4021},
            {4018, 4019},
            {4022, 4023},
            {12166, 12167},
            {13926, 13927},
            {4152, 4153},
            {24134, 24135},
            {-1, -1},
            {-1, -1},
            {-1, -1}};

    public transient final Stopwatch stopwatch = Stopwatch.start();

    /** The skill id. */
    private final int skill;

    /** The current level of the skill. */
    private int level;

    /** The maximum level of the skill. */
    private int maxLevel;

    /** The current skill experience. */
    private double experience;

    private boolean doingSkill = false;

    /** Constructs a new {@link Skill}. */
    public Skill(int skill, int level, double experience) {
        this.skill = skill;
        this.level = level;
        this.maxLevel = getLevelForExperience(experience);
        this.experience = experience;
    }

    /**
     * Gets the skill id.
     *
     * @return The skill id.
     */
    public int getSkill() {
        return skill;
    }

    /**
     * Gets the current skill level.
     *
     * @return The current level.
     */
    public int getLevel() {
        return level;
    }

    /**
     * Gets the maximum skill level.
     *
     * @return The max level.
     */
    public int getMaxLevel() {
        return maxLevel;
    }

    /**
     * Gets the skill experience.
     *
     * @return The skill experience.
     */
    public double getExperience() {
        return experience;
    }

    /**
     * Gets the floor {@code experience}.
     *
     * @return The floor experience.
     */
    public int getRoundedExperience() {
        return (int) experience;
    }

    /**
     * Sets the level for this skill.
     *
     * @param level The level to set.
     */
    public void setLevel(int level) {
        this.level = level;
    }

    /**
     * Sets the maximum level for this skill.
     *
     * @param maxLevel The level to set.
     */
    public void setMaxLevel(int maxLevel) {
        this.maxLevel = maxLevel;
    }

    /**
     * Sets the experience for this skill.
     *
     * @param experience The experience to set.
     */
    public void setExperience(double experience) {
        this.experience = experience;
    }

    /**
     * Determines if your level is greater than or equal to {@code level}.
     *
     * @param level the level to compare against this one.
     * @return {@code true} if this level is greater than or equal to the other
     * one, {@code false} otherwise.
     */
    public boolean reqLevel(int level) {
        return this.level >= level;
    }

    /**
     * Modifies the current level with a given function. If the level happens to
     * fall under 0, the level will be set to 0.
     *
     * @param function The function, with this current skill level as the input, and
     *                 the new level as an output.
     */
    public void modifyLevel(Function<Integer, Integer> function) {
        modifyLevel(function, 0, maxLevel);
    }

    /**
     * Modifies the current level with a given function. The level will be
     * clipped to the given boundaries as well.If the level happens to fall
     * under 0, the level will be set to 0, regardless of the given boundaries.
     *
     * @param function    The function, with this current skill level as the input, and
     *                    the new level as an output.
     * @param lowerBounds The lowest value the level can be modified to.
     * @param upperBounds The highest value the level can be modified to.
     */
    public void modifyLevel(Function<Integer, Integer> function, int lowerBounds, int upperBounds) {
        level = function.apply(level);

        if (level < 0) {
            level = 0;
        } else {
            if (level < lowerBounds) {
                level = lowerBounds;
            }

            if (level > upperBounds) {
                level = upperBounds;
            }
        }
    }

    /**
     * Adds levels to this skill by the given amount.
     *
     * @param amount The amount to deposit.
     */
    public void addLevel(int amount) {
        modifyLevel(level -> level + amount);
    }

    /**
     * Removes levels from this skill by the given amount.
     *
     * @param amount The amount to deposit.
     */
    public void removeLevel(int amount) {
        modifyLevel(level -> level - amount);
    }

    /**
     * Multiplies the current level of this skill by a given amount.
     *
     * @param amount The amount to multiply by.
     */
    public void multiplyLevel(double amount) {
        modifyLevel(level -> (int) (level * amount));
    }

    /**
     * Divides the current level of this skill by a given amount.
     *
     * @param amount The amount to divide by.
     */
    public void divideLevel(double amount) {
        modifyLevel(level -> amount == 0 ? -level : (int) (level / amount));
    }

    /**
     * Modifies the current experience with a given function. If the experience
     * happens to fall under 0, the experience will be set to 0.
     *
     * @param function The function, with this current skill experience as the input,
     *                 and the new experience as an output.
     */
    public double modifyExperience(Function<Double, Double> function) {
        return modifyExperience(function, 0, 200_000_000);
    }

    /**
     * Modifies the current experience with a given function. The experience
     * will be clipped to the given boundaries as well. If the experience
     * happens to fall under 0, the experience will be set to 0, regardless of
     * the given boundaries.
     *
     * @param function    The function, with this current skill experience as the input,
     *                    and the new experience as an output.
     * @param lowerBounds The lowest value the experience can be modified to.
     * @param upperBounds The highest value the experience can be modified to.
     */
    public double modifyExperience(Function<Double, Double> function, int lowerBounds, int upperBounds) {
        experience = function.apply(experience);

        if (experience < 0) {
            experience = 0;
        } else {
            if (experience < lowerBounds) {
                experience = lowerBounds;
            }

            if (experience > upperBounds) {
                experience = upperBounds;
            }
        }

        return experience;
    }

    /**
     * Adds experiences to this skill by the given amount.
     *
     * @param amount The amount to deposit.
     */
    public double addExperience(double amount) {
        return modifyExperience(experience -> experience + amount);
    }

    /**
     * Removes experiences from this skill by the given amount.
     *
     * @param amount The amount to deposit.
     */
    public void removeExperience(double amount) {
        modifyExperience(experience -> experience - amount);
    }

    /**
     * Multiplies the current experience of this skill by a given amount.
     *
     * @param amount The amount to multiply by.
     */
    public void multiplyExperience(double amount) {
        modifyExperience(experience -> experience * amount);
    }

    /**
     * Divides the current experience of this skill by a given amount.
     *
     * @param amount The amount to divide by.
     */
    public void divideExperience(double amount) {
        modifyExperience(experience -> amount == 0 ? -experience : (experience / amount));
    }

    /**
     * Performs a binary search to quickly search for the level at the given
     * experience.
     *
     * @param experience The experience.
     * @param min        The minimum level to search from.
     * @param max        The maximum level to search to.
     * @return The level at the given experience.
     */
    private static byte binarySearch(double experience, int min, int max) {
        int mid = (min + max) / 2;
        double value = EXP_FOR_LEVEL[mid];

        if (value > experience) {
            return binarySearch(experience, min, mid - 1);
        } else if (value == (int) experience || EXP_FOR_LEVEL[mid + 1] > experience) {
            return (byte) (mid + 1);
        }

        return binarySearch(experience, mid + 1, max);
    }

    /** Gets the level for a given experience amount. If the experience is larger than the experience at level 99, then the value will always return 99. */
    public static final byte getLevelForExperience(double experience) {
        if ((int) experience >= EXP_FOR_LEVEL[98]) {
            return 99;
        }
        return binarySearch(experience, 0, 98);
    }

    /** Gets the experience for a given level. The level is automatically clipped to be in the range [0, 99]. */
    public static final int getExperienceForLevel(int level) {
        if (level >= 99) {
            return EXP_FOR_LEVEL[98];
        }
        if (level < 1) {
            return 0;
        }
        return EXP_FOR_LEVEL[level - 1];
    }

    /** Gets the name for a skill id. */
    public static String getName(int skill) {
        return SKILL_NAMES[skill];
    }

    /** Creates a function that adds a number by an amount. */
    public static Function<Integer, Integer> add(int amount) {
        return level -> level + amount;
    }

    /** Creates a function that subtracts a number by an amount. */
    public static Function<Integer, Integer> subtract(int amount) {
        return level -> level - amount;
    }

    /** Creates a function that multiplies a number by an amount. */
    public static Function<Integer, Integer> multiply(double amount) {
        return level -> (int) (level * amount);
    }

    /** Creates a function that divides a number by an amount. */
    public static Function<Integer, Integer> divide(double amount) {
        return level -> amount == 0 ? 0 : (int) (level / amount);
    }

    public void setDoingSkill(boolean doingSkill) {
        this.doingSkill = doingSkill;
    }

    public boolean isDoingSkill() {
        return doingSkill;
    }

    protected double modifier() {
        return 0;
    }

    protected boolean clickItem(Player player, ItemInteractionEvent event) {
        return false;
    }

    protected boolean clickNpc(Player player, NpcInteractionEvent event) {
        return false;
    }

    protected boolean clickObject(Player player, ObjectInteractionEvent event) {
        return false;
    }

    protected boolean clickButton(Player player, ClickButtonInteractionEvent event) {
        return false;
    }

    protected boolean useItem(Player player, ItemOnItemInteractionEvent event) {
        return false;
    }

    protected boolean useItem(Player player, ItemOnObjectInteractionEvent event) {
        return false;
    }

    protected boolean itemContainerAction(Player player, ItemContainerInteractionEvent event) {
        return false;
    }

    @Override
    public String toString() {
        return String.format("Skill[name=%s, id=%s, level=%s, max=%s, experience=%s]", getName(skill), skill, level, maxLevel, experience);
    }

    @Override
    public boolean onEvent(Player player, InteractionEvent interactionEvent) {
        final EventDispatcher dispatcher = new EventDispatcher(interactionEvent);
        dispatcher.dispatch(InteractionType.CLICK_BUTTON, e -> clickButton(player, (ClickButtonInteractionEvent) e));
        dispatcher.dispatch(InteractionType.ITEM_ON_ITEM, e -> useItem(player, (ItemOnItemInteractionEvent) e));
        dispatcher.dispatch(InteractionType.ITEM_ON_OBJECT, e -> useItem(player, (ItemOnObjectInteractionEvent) e));

        dispatcher.dispatch(InteractionType.FIRST_ITEM_CLICK, e -> clickItem(player, (FirstItemClickInteractionEvent) e));
        dispatcher.dispatch(InteractionType.SECOND_ITEM_CLICK, e -> clickItem(player, (SecondItemClickInteractionEvent) e));
		dispatcher.dispatch(InteractionType.THIRD_ITEM_CLICK, e -> clickItem(player, (ThirdItemClickInteractionEvent) e));

        dispatcher.dispatch(InteractionType.FIRST_CLICK_NPC, e -> clickNpc(player, (FirstNpcClick) e));
        dispatcher.dispatch(InteractionType.SECOND_CLICK_NPC, e -> clickNpc(player, (SecondNpcClick) e));
        dispatcher.dispatch(InteractionType.FIRST_CLICK_OBJECT, e -> clickObject(player, (FirstObjectClick) e));
        dispatcher.dispatch(InteractionType.SECOND_CLICK_OBJECT, e -> clickObject(player, (SecondObjectClick) e));
        dispatcher.dispatch(InteractionType.THIRD_CLICK_OBJECT, e -> clickObject(player, (ThirdObjectClick) e));
        dispatcher.dispatch(InteractionType.ITEM_CONTAINER_INTERACTION_EVENT, e -> itemContainerAction(player, (ItemContainerInteractionEvent) e));
        return interactionEvent.isHandled();
    }
}
