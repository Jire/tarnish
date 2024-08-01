package com.osroyale.content.skill.impl.slayer;

import com.osroyale.game.world.entity.mob.npc.definition.NpcDefinition;
import com.osroyale.game.world.entity.mob.player.Player;
import com.osroyale.game.world.entity.skill.Skill;
import com.osroyale.game.world.position.Position;
import com.osroyale.util.Utility;

import java.util.ArrayList;

/**
 * Holds all the slayer task data.
 *
 * @author Daniel
 * @author Ethan
 */
public enum SlayerTask implements TaskInterface<Player> {
    SAND_CRAB(new String[]{"Sand Crab"}, 1, false, TaskDifficulty.EASY, new Position(1726, 3463), "") {
        @Override
        public boolean canAssign(Player player) {
            return true;
        }

    },
    ROCK_CRAB(new String[]{"Rock Crab"}, 1, false, TaskDifficulty.EASY, new Position(2676, 3711), "Camelot") {
        @Override
        public boolean canAssign(Player player) {
            return true;
        }

    },
    CRAWLING_HAND(new String[]{"Crawling hand"}, 1, false, TaskDifficulty.EASY, new Position(3424, 3543), "Slayer Tower") {
        @Override
        public boolean canAssign(Player player) {
            return true;
        }

    },
    HILL_GIANT(new String[]{"Hill Giant"}, 1, false, TaskDifficulty.EASY, new Position(3117, 9856), "") {
        @Override
        public boolean canAssign(Player player) {
            return true;
        }

    },
    FIRE_GIANT(new String[]{"Fire giant"}, 5, false, TaskDifficulty.MEDIUM, new Position(2655, 9489), "Slayer Cave") {
        @Override
        public boolean canAssign(Player player) {
            return true;
        }

    },
    GREEN_DRAGON(new String[]{"Green dragon", "Baby green dragon"}, 5, false, TaskDifficulty.MEDIUM, new Position(3339, 3668), "WILDERNESS") {
        @Override
        public boolean canAssign(Player player) {
            return true;
        }

    },
    BANSHEE(new String[]{"Banshee"}, 15, false, TaskDifficulty.MEDIUM, new Position(3340, 3560), "Pollnivneach Smoke Dungeon") {
        @Override
        public boolean canAssign(Player player) {
            return true;
        }
    },
    BLOODVELDS(new String[]{"Bloodveld"}, 15, false, TaskDifficulty.MEDIUM, new Position(3419, 3562), "Slayer Tower") {
        @Override
        public boolean canAssign(Player player) {
            return true;
        }
    },
    INFERNAL_MAGES(new String[]{"Infernal Mage"}, 45, false, TaskDifficulty.MEDIUM, new Position(3445, 3560), "Pollnivneach Well Dungeon") {
        @Override
        public boolean canAssign(Player player) {
            return true;
        }

    },
    AVIANSIES(new String[]{"Aviansie"}, 50, false, TaskDifficulty.MEDIUM, new Position(2882, 5287, 2), "Stronghold of Security") {
        @Override
        public boolean canAssign(Player player) {
            return true;
        }

    },
    DUST_DEVILS(new String[]{"Dust devil"}, 65, true, TaskDifficulty.MEDIUM, new Position(3239, 9364), "Pollnivneach Smoke Dungeon") {
        @Override
        public boolean canAssign(Player player) {
            return true;
        }

    },
    ABERRANT_SPECTRES(new String[]{"Aberrant spectre"}, 60, true, TaskDifficulty.MEDIUM, new Position(3420, 3553, 1), "Slayer Tower") {
        @Override
        public boolean canAssign(Player player) {
            return true;
        }
    },
    ANKOU(new String[]{"Ankou"}, 1, false, TaskDifficulty.HARD, new Position(2359, 5236), "Stronghold of Security") {
        @Override
        public boolean canAssign(Player player) {
            return true;
        }
    },
    BLACK_DEMONS(new String[]{"Black demon"}, 12, false, TaskDifficulty.HARD, new Position(2876, 9790), "Taverly Dungeon") {
        @Override
        public boolean canAssign(Player player) {
            return true;
        }
    },
    DAGANNOTHS(new String[]{"Dagannoth", "Dagannoth mother", "Dagannoth Supreme", "Dagannoth Prime", "Dagannoth Rex"}, 1, false, TaskDifficulty.HARD, new Position(1891, 4369), "Fremennik Province Light House") {
        @Override
        public boolean canAssign(Player player) {
            return true;
        }

    },
    HELLHOUNDS(new String[]{"Hellhound", "Cerberus"}, 12, false, TaskDifficulty.HARD, new Position(2827, 9847), "Taverly Dungeon") {
        @Override
        public boolean canAssign(Player player) {
            return true;
        }

    },
    MITHRIL_DRAGON(new String[]{"Mithril dragon"}, 1, false, TaskDifficulty.HARD, new Position(1748, 5326), "Ancient Caverns") {
        @Override
        public boolean canAssign(Player player) {
            return true;
        }

    },
    STEEL_DRAGONS(new String[]{"Steel dragon"}, 35, false, TaskDifficulty.HARD, new Position(2629, 9422), "Karamja Dungeon") {
        @Override
        public boolean canAssign(Player player) {
            return true;
        }
    },
    IRON_DRAGONS(new String[]{"Iron dragon"}, 35, false, TaskDifficulty.HARD, new Position(2682, 9463), "Karamja Dungeon") {
        @Override
        public boolean canAssign(Player player) {
            return true;
        }
    },
    BLUE_DRAGONS(new String[]{"Blue dragon", "Baby blue dragon"}, 35, false, TaskDifficulty.MEDIUM, new Position(2886, 9799), "Taverly Dungeon") {
        @Override
        public boolean canAssign(Player player) {
            return true;
        }
    },
    GARGOYLES(new String[]{"Gargoyle"}, 75, true, TaskDifficulty.HARD, new Position(3437, 3542), "Slayer Tower") {
        @Override
        public boolean canAssign(Player player) {
            return player.skills.getMaxLevel(Skill.SLAYER) >= 75;
        }
    },
    CRAZY_ARCHAEOLOGIST(new String[]{"Crazy archaeologist"}, 60, false, TaskDifficulty.HARD, new Position(2966, 3698), "Wilderness") {
        @Override
        public boolean canAssign(Player player) {
            return player.slayer.getUnlocked().contains(SlayerUnlockable.CRAZY_ARCHAEOLOGIST);
        }
    },
    //    CHAOS_ELEMENTAL("Chaos elemental", 1, 150, TaskDifficulty.HARD, new Position(1, 1), "Wilderness", 2054) {
//        @Override
//        public boolean canAssign(Player player) {
//            return player.slayer.getUnlocked().contains(SlayerUnlockable.CHAOS_ELEMENT);
//        }
//
//        @Override
//        public boolean canAttack(Player player) {
//            return true;
//        }
//    },
    SPIRTUAL_RANGERS(new String[]{"Spiritual Ranger"}, 63, false, TaskDifficulty.HARD, new Position(2882, 5287, 2), "Godwars Dungeon") {
        @Override
        public boolean canAssign(Player player) {
            return true;
        }
    },
    SPIRTUAL_WARRIORS(new String[]{"Spiritual Warrior"}, 68, false, TaskDifficulty.HARD, new Position(2882, 5287, 2), "Godwars Dungeon") {
        @Override
        public boolean canAssign(Player player) {
            return true;
        }
    },
    SPIRTUAL_MAGES(new String[]{"Spiritual Mage"}, 83, false, TaskDifficulty.HARD, new Position(2882, 5287, 2), "Godwars Dungeon") {
        @Override
        public boolean canAssign(Player player) {
            return true;
        }
    },
    SKELETAL_WYVERN(new String[]{"Skeletal wyvern"}, 72, true, TaskDifficulty.HARD, new Position(3056, 9562), "Asgarnia Ice Dungeon") {
        @Override
        public boolean canAssign(Player player) {
            return player.skills.getMaxLevel(Skill.SLAYER) >= 72;
        }
    },
    ABYSSAL_DEMONS(new String[]{"Abyssal Demon"}, 85, true, TaskDifficulty.HARD, new Position(3414, 3558), "Slayer Tower") {
        @Override
        public boolean canAssign(Player player) {
            return player.skills.getMaxLevel(Skill.SLAYER) >= 85;
        }
    },
    DARK_BEAST(new String[]{"Dark beast"}, 90, true, TaskDifficulty.HARD, new Position(2010, 4644), "Temple of Light dungeon") {
        @Override
        public boolean canAssign(Player player) {
            return player.skills.getMaxLevel(Skill.SLAYER) >= 90;
        }
    },
    KRAKEN(new String[]{"Kraken"}, 87, true, TaskDifficulty.BOSS, new Position(2276, 10000, 0), "Kraken cave") {
        @Override
        public boolean canAssign(Player player) {
            return player.skills.getMaxLevel(Skill.SLAYER) >= getLevel();
        }
    },
    KING_BLACK_DRAGON(new String[]{"King black dragon"}, 1, false, TaskDifficulty.BOSS, new Position(3000, 3849), "Wilderness") {
        @Override
        public boolean canAssign(Player player) {
            return player.slayer.getUnlocked().contains(SlayerUnlockable.KING_BLACK_DRAGON);
        }
    },
    ZULRAH(new String[]{"Zulrah"}, 95, false, TaskDifficulty.BOSS, new Position(2206, 3056, 0), "Zulrah's Shrine") {
        @Override
        public boolean canAssign(Player player) {
            return player.slayer.getUnlocked().contains(SlayerUnlockable.ZULRAH);
        }
    },
    CERBERUS(new String[]{"Cerberus"}, 91, true, TaskDifficulty.BOSS, new Position(1310, 1268, 0), "Cerberus' Lair") {
        public boolean canAssign(Player player) {
            return player.skills.getMaxLevel(Skill.SLAYER) >= 91;
        }
    };

    /** The slayer level required to receive the task. */
    private final int level;

    private final boolean levelNeeded;

    /** The difficulty of the task. */
    private TaskDifficulty difficulty;

    /** The teleport position for the task. */
    private final Position position;

    /** The string for the position. */
    private final String location;

    /** Array of the slayer npc. */
    private final String[] npc;

    /** Constructs a new <code>SlayerTask<code>. */
    SlayerTask(String[] npc, int level, boolean levelNeeded, TaskDifficulty difficulty, Position position, String location) {
        this.level = level;
        this.levelNeeded = levelNeeded;
        this.difficulty = difficulty;
        this.position = position;
        this.location = location;
        this.npc = npc;
    }

    /** Gets the name of the task. */
    public String getName() {
        return npc[0];
    }

    /** Gets the level of the task. */
    public int getLevel() {
        return level;
    }

    public boolean isLevelNeeded() {
        return levelNeeded;
    }

    /** Gets the difficulty of the slayer task. */
    public TaskDifficulty getDifficulty() {
        return difficulty;
    }

    /** Gets the position of the task. */
    public Position getPosition() {
        return position;
    }

    /** Gets the teleport string. */
    public String getLocation() {
        return location;
    }

    /** Gets the npcs of the task. */
    public String[] getNpc() {
        return npc;
    }

    /** Gets the combat level of the npc. */
    public String getCombatLevel() {
        return NpcDefinition.getCombatLevelRange(npc[0]);
    }

    /** Checks if the npc is a slayer task assignment. */
    public boolean valid(String name) {
        for (String npc : npc) {
            if (npc.equalsIgnoreCase(name))
                return true;
        }
        return false;
    }

    /** Checks if a player can attack a slayer monster. */
    public static boolean canAttack(Player player, String npc) {
        for (SlayerTask task : values()) {
            for (String name : task.getNpc()) {
                if (npc.equalsIgnoreCase(name) && task.isLevelNeeded() && player.skills.getLevel(Skill.SLAYER) < task.getLevel()) {
                    return false;
                }
            }
        }
        return true;
    }

    /** Assigns a slayer task for the player. */
    public static SlayerTask assign(Player player, TaskDifficulty difficulty) {
        ArrayList<SlayerTask> tasks = asList(player, difficulty);

        if (tasks.isEmpty()) {
            return null;
        }

        return Utility.randomElement(tasks);
    }

    /** Returns all the available slayer tasks for the player as a list. */
    public static ArrayList<SlayerTask> asList(Player player, TaskDifficulty difficulty) {
        ArrayList<SlayerTask> tasks = new ArrayList<>();
        for (SlayerTask task : values()) {
            if (task.getLevel() > player.skills.getLevel(Skill.SLAYER))
                continue;
            if (!task.canAssign(player))
                continue;
            if (player.slayer.getBlocked().contains(task))
                continue;
            if (task.difficulty != difficulty)
                continue;
            tasks.add(task);
        }
        return tasks;
    }

    public static int getPoints(TaskDifficulty difficulty) {
        switch (difficulty) {
            case EASY:
                return 15;
            case MEDIUM:
                return 20;
            case HARD:
                return 30;
            case BOSS:
                return 35;
        }
        return 0;
    }

    public static int getCompletionExperience(TaskDifficulty difficulty) {
        switch (difficulty) {
            case EASY:
                return 5000;
            case MEDIUM:
                return 10000;
            case HARD:
                return 15000;
            case BOSS:
                return 25000;
        }
        return 0;
    }
}

interface TaskInterface<T> {
    boolean canAssign(final T player);
}