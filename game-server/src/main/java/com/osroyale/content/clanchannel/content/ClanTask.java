package com.osroyale.content.clanchannel.content;

import com.osroyale.content.clanchannel.ClanType;
import com.osroyale.content.clanchannel.channel.ClanChannel;
import com.osroyale.util.Difficulty;
import com.osroyale.util.InclusiveRandom;
import com.osroyale.util.Utility;

import java.util.HashSet;
import java.util.Set;

import static com.osroyale.content.clanchannel.content.ClanTaskKey.*;

/**
 * Holds all the clan task data.
 * @author Daniel
 */
public enum ClanTask {
    /** PvP */
    /*          ->     Easy     <-          */
    KILL_PLAYERS_I(ClanType.PVP, Difficulty.EASY, "Kill % players", PLAYER_KILLING, new InclusiveRandom(10, 25)),

    /*          ->     Medium     <-          */
    KILL_PLAYERS_II(ClanType.PVP, Difficulty.MEDIUM, "Kill % players", PLAYER_KILLING, new InclusiveRandom(25, 50)),

    /*          ->     Hard     <-          */
    KILL_PLAYERS_III(ClanType.PVP, Difficulty.HARD, "Kill % players", PLAYER_KILLING, new InclusiveRandom(50, 125)),

    /** PvM */
    /*          ->     Easy     <-          */
    KILL_HILL_GIANTS(ClanType.PVM, Difficulty.EASY, "Kill % hill giants", HILL_GIANT, new InclusiveRandom(100, 200)),
    KILL_ROCK_CRABS(ClanType.PVM, Difficulty.EASY, "Kill % rock crabs", ROCK_CRAB, new InclusiveRandom(100, 200)),
    KILL_SAND_CRABS(ClanType.PVM, Difficulty.EASY, "Kill % sand crabs", SAND_CRAB, new InclusiveRandom(100, 200)),
    KILL_GREEN_DRAGONS_I(ClanType.PVM, Difficulty.MEDIUM, "Kill % green dragons", GREEN_DRAGON, new InclusiveRandom(50, 150)),
    KILL_GHOST(ClanType.PVM, Difficulty.EASY, "Kill % ghosts", GHOST, new InclusiveRandom(100, 200)),
    KILL_SKELETON(ClanType.PVM, Difficulty.EASY, "Kill % skeletons", SKELETON, new InclusiveRandom(100, 200)),//NEW
    KILL_BLACK_KNIGHT(ClanType.PVM, Difficulty.EASY, "Kill % black knights", BLACK_KNIGHT, new InclusiveRandom(100, 200)),//NEW
    KILL_BABY_BLUE_DRAGON(ClanType.PVM, Difficulty.EASY, "Kill % baby blue dragons", BABY_BLUE_DRAGON, new InclusiveRandom(100, 200)),//NEW
    KILL_BAT(ClanType.PVM, Difficulty.EASY, "Kill % bats", BAT, new InclusiveRandom(100, 200)),//NEW
    KILL_CHAOS_DWARF(ClanType.PVM, Difficulty.EASY, "Kill % chaos dwarves", CHAOS_DWARF, new InclusiveRandom(100, 200)),//NEW
    KILL_MAGIC_AXE(ClanType.PVM, Difficulty.EASY, "Kill % magic axes", MAGIC_AXE, new InclusiveRandom(100, 200)),//NEW
    KILL_CAVE_CRAWLER(ClanType.PVM, Difficulty.EASY, "Kill % cave crawlers", CAVE_CRAWLER, new InclusiveRandom(100, 200)),//NEW
    KILL_CRAWLING_HAND(ClanType.PVM, Difficulty.EASY, "Kill % crawling hands", CRAWLING_HAND, new InclusiveRandom(100, 200)),//NEW
    KILL_BANSHEE(ClanType.PVM, Difficulty.EASY, "Kill % banshees", BANSHEE, new InclusiveRandom(100, 200)),//NEW
    KILL_ICE_FIEND(ClanType.PVM, Difficulty.EASY, "Kill % ice fiends", ICE_FIEND, new InclusiveRandom(100, 200)),//NEW


    /*          ->     Medium     <-          */
    KILL_BLACK_DEMONS(ClanType.PVM, Difficulty.MEDIUM, "Kill % black demons", BLACK_DEMON, new InclusiveRandom(150, 250)),
    KILL_GREATER_DEMONS(ClanType.PVM, Difficulty.MEDIUM, "Kill % greater demons", GREATER_DEMON, new InclusiveRandom(150, 250)),
    KILL_BLUE_DRAGONS(ClanType.PVM, Difficulty.MEDIUM, "Kill % blue dragons", BLUE_DRAGON, new InclusiveRandom(151, 250)),
    KILL_BLACK_DRAGONS(ClanType.PVM, Difficulty.MEDIUM, "Kill % black dragons", BLACK_DRAGON, new InclusiveRandom(151, 250)),
    KILL_RED_DRAGONS(ClanType.PVM, Difficulty.MEDIUM, "Kill % red dragons", RED_DRAGON, new InclusiveRandom(151, 250)),
    KILL_GREEN_DRAGONS_II(ClanType.PVM, Difficulty.MEDIUM, "Kill % green dragons", GREEN_DRAGON, new InclusiveRandom(151, 250)),
    KILL_HELLHOUND(ClanType.PVM, Difficulty.MEDIUM, "Kill % hellhounds", HELLHOUND, new InclusiveRandom(150, 250)),//NEW
    KILL_CAVE_HORROR(ClanType.PVM, Difficulty.MEDIUM, "Kill % cave horrors", CAVE_HORROR, new InclusiveRandom(150, 250)),//NEW
    KILL_STEEL_DRAGON(ClanType.PVM, Difficulty.MEDIUM, "Kill % steel dragons", STEEL_DRAGON, new InclusiveRandom(150, 250)),//NEW
    KILL_PYRE_FIEND(ClanType.PVM, Difficulty.MEDIUM, "Kill % pyre fiends", PYRE_FIEND, new InclusiveRandom(150, 250)),//NEW
    KILL_FIRE_GIANT(ClanType.PVM, Difficulty.MEDIUM, "Kill % fire giants", FIRE_GIANT, new InclusiveRandom(150, 250)),//NEW
    KILL_BASILISK(ClanType.PVM, Difficulty.MEDIUM, "Kill % basilisks", BASILISK, new InclusiveRandom(150, 250)),//NEW
    KILL_COCKATRICE(ClanType.PVM, Difficulty.MEDIUM, "Kill % cockatrices", COCKATRICE, new InclusiveRandom(150, 250)),//NEW
    KILL_DUST_DEVIL(ClanType.PVM, Difficulty.MEDIUM, "Kill % dust devils", DUST_DEVIL, new InclusiveRandom(100, 300)),//NEW
    KILL_SPIRITUAL_RANGER(ClanType.PVM, Difficulty.MEDIUM, "Kill % spiritual rangers", SPIRITUAL_RANGER, new InclusiveRandom(150, 250)),//NEW
    KILL_SPIRITUAL_WARRIOR(ClanType.PVM, Difficulty.MEDIUM, "Kill % spiritual warriors", SPIRITUAL_WARRIOR, new InclusiveRandom(150, 250)),//NEW
    KILL_BLOODVELD(ClanType.PVM, Difficulty.MEDIUM, "Kill % bloodvels", BLOODVELD, new InclusiveRandom(150, 250)),//NEW

    /*          ->     Hard     <-          */
    KILL_ABYSSAL_DEMONS(ClanType.PVM, Difficulty.HARD, "Kill % abyssal demons", ABYSSAL_DEMON, new InclusiveRandom(250, 500)),
    KILL_SKELETAL_WYVERNS(ClanType.PVM, Difficulty.HARD, "Kill % skeletal wyverns", SKELETAL_WYVERN, new InclusiveRandom(250, 500)),
    KILL_KING_BLACK_DRAGONS(ClanType.PVM, Difficulty.HARD, "Kill % king black dragons", KING_BLACK_DRAGON, new InclusiveRandom(50, 200)),
    KILL_SCORPIA(ClanType.PVM, Difficulty.HARD, "Kill % scorpias", SCORPIA, new InclusiveRandom(25, 75)),
    KILL_VENENATIS(ClanType.PVM, Difficulty.HARD, "Kill % venenatis", VENNANTIS, new InclusiveRandom(25, 75)),
    KILL_CALLISTO(ClanType.PVM, Difficulty.HARD, "Kill % callistos", CALLISTO, new InclusiveRandom(25, 75)),
    KILL_CRAZY_ARCHAEOLOGIST(ClanType.PVM, Difficulty.HARD, "Kill % crazy archaeologists", CRAZY_ARCHAEOLOGIST, new InclusiveRandom(50, 200)),
    KILL_CHAOS_FANATIC(ClanType.PVM, Difficulty.HARD, "Kill % chaos fanatics", CHAOS_FANATIC, new InclusiveRandom(25, 75)),
//    KILL_CHAOS_ELEMENTAL(ClanType.PVM, Difficulty.HARD, "Kill % chaos elementals", CHAOS_ELEMENTAL, new InclusiveRandom(25, 75)),
    KILL_SPIRITUAL_MAGE(ClanType.PVM, Difficulty.HARD, "Kill % spiritual mages", SPIRITUAL_MAGE, new InclusiveRandom(25, 75)),//NEW
    KILL_VETION(ClanType.PVM, Difficulty.HARD, "Kill % vet'ions", VETION, new InclusiveRandom(25, 75)),//NEW
    KILL_MYTHRIL_DRAGON(ClanType.PVM, Difficulty.HARD, "Kill % king black dragons", KING_BLACK_DRAGON, new InclusiveRandom(25, 100)),//NEW
    KILL_NECHRYAEL(ClanType.PVM, Difficulty.HARD, "Kill % nachryaels", NECHRYAEL, new InclusiveRandom(50, 100)),//NEW
    KILL_SMOKE_DEVIL(ClanType.PVM, Difficulty.HARD, "Kill % smoke devils", SMOKE_DEVIL, new InclusiveRandom(50, 100)),//NEW
    KILL_DEMONIC_GORILLA(ClanType.PVM, Difficulty.HARD, "Kill % demonic gorillas", DEMONIC_GORILLA, new InclusiveRandom(50, 100)),//NEW
    KILL_LIZARD_SHAMAN(ClanType.PVM, Difficulty.HARD, "Kill % lizard shamans", LIZARD_SHAMAN, new InclusiveRandom(50, 100)),//NEW
    KILL_GENERAL_GRAARDOR(ClanType.PVM, Difficulty.HARD, "Kill % general graardors", GENERAL_GRAARDOR, new InclusiveRandom(25, 75)),//NEW
    KILL_COMMANDER_ZILYANA(ClanType.PVM, Difficulty.HARD, "Kill % commander zilyanas", COMMANDER_ZILYANA, new InclusiveRandom(25, 75)),//NEW
    KILL_KREEARRA(ClanType.PVM, Difficulty.HARD, "Kill % kree'arras", KREEARRA, new InclusiveRandom(25, 75)),//NEW
    KILL_KRIL_TSUTSAROTH(ClanType.PVM, Difficulty.HARD, "Kill % k'ril tsutsaroths", KRIL_TSUTSAROTH, new InclusiveRandom(25, 75)),//NEW


    /** Skilling */
    /*          ->     Easy     <-          */
    THIEVE_FROM_STALL_I(ClanType.SKILLING, Difficulty.EASY, "Thieve from stall % times", THIEVING_STALL, new InclusiveRandom(300, 500)),
    CHOP_WILLOW(ClanType.SKILLING, Difficulty.EASY, "Chop % willow logs", CHOP_WILLOW_LOG, new InclusiveRandom(300, 500)),
    COMPLETE_AGILITY_LAP_I(ClanType.SKILLING, Difficulty.EASY, "Complete % non-rooftop course", AGILITY_COURSE, new InclusiveRandom(300, 500)),
    BURN_YEW_LOG(ClanType.SKILLING, Difficulty.EASY, "Burn % willow logs", BURN_WILLOW_LOG, new InclusiveRandom(300, 500)),

    /*          ->     Medium     <-          */
    CATCH_SHARK(ClanType.SKILLING, Difficulty.MEDIUM, "Catch % sharks", SHARK, new InclusiveRandom(500, 750)),
    CHOP_YEW(ClanType.SKILLING, Difficulty.MEDIUM, "Chop % yew logs", YEW_LOG, new InclusiveRandom(500, 750)),
    FLETCH_YEW_SHORTBOW(ClanType.SKILLING, Difficulty.MEDIUM, "Fletch % yew shortbow", YEW_SHORTBOW, new InclusiveRandom(500, 750)),
    CRAFT_DEATH_RUNE(ClanType.SKILLING, Difficulty.MEDIUM, "Craft % death runes", DEATH_RUNE, new InclusiveRandom(500, 750)),
    CREATE_SUPER_RESTORE_POTION(ClanType.SKILLING, Difficulty.MEDIUM, "Create % super restore potions", SUPER_RESTORE_POTION, new InclusiveRandom(500, 750)),
    COMPLETE_AGILITY_LAP_II(ClanType.SKILLING, Difficulty.MEDIUM, "Complete % non-rooftop course", AGILITY_COURSE, new InclusiveRandom(500, 750)),
    THIEVE_FROM_STALL_II(ClanType.SKILLING, Difficulty.MEDIUM, "Thieve from stall % times", THIEVING_STALL, new InclusiveRandom(500, 750)),

    /*          ->     Hard     <-          */
    //    SMITH_RUNITE_BAR(ClanType.SKILLING, Difficulty.HARD, "Smith % runite bars", RUNITE_BAR, new InclusiveRandom(600, 900)),
    FLETCH_MAGIC_SHORTBOW(ClanType.SKILLING, Difficulty.HARD, "Fletch % magic shortbow", MAGIC_SHORTBOW, new InclusiveRandom(1250, 2000)),
    MINE_RUNITE_ORE(ClanType.SKILLING, Difficulty.HARD, "Mine % runite ores", RUNITE_ORES, new InclusiveRandom(650, 900)),
//    CREATE_SUPER_COMBAT_POTION(ClanType.SKILLING, Difficulty.HARD, "Create % super combat potions", SUPER_COMBAT_POTION, new InclusiveRandom(, 2000)),
    COMPLETE_AGILITY_LAP_III(ClanType.SKILLING, Difficulty.HARD, "Complete % non-rooftop course", AGILITY_COURSE, new InclusiveRandom(750, 1000)),
    CATCH_ANGLERFISH(ClanType.SKILLING, Difficulty.HARD, "Catch % dark crabs", DARK_CRAB, new InclusiveRandom(750, 1000)),
    CRAFT_BLOOD_RUNE(ClanType.SKILLING, Difficulty.HARD, "Craft % blood runes", BLOOD_RUNE, new InclusiveRandom(750, 1000)),
    CHOP_MAGIC(ClanType.SKILLING, Difficulty.HARD, "Chop % magic logs", MAGIC_LOG, new InclusiveRandom(600, 800));

    public final String task;
    public final ClanTaskKey key;
    public final InclusiveRandom amount;
    public final Difficulty difficulty;
    public final ClanType type;

    ClanTask(ClanType type, Difficulty difficulty, String task, ClanTaskKey key, InclusiveRandom amount) {
        this.task = task;
        this.key = key;
        this.amount = amount;
        this.difficulty = difficulty;
        this.type = type;
    }

    public double getProgressExperience() {
        switch (type) {
            case PVP:
                return 1250;
            case PVM:
                return difficulty == Difficulty.HARD ? 350 : 50;
            case SKILLING:
                return 5;
        }
        return 0;
    }

    public String getName(ClanChannel channel) {
        return task.replace("%", "" + channel.getDetails().taskAmount);
    }

    public int getAmount() {
        return Utility.random(amount.minimum, amount.maximum);
    }

    public static Set<ClanTask> getTasks(ClanType type, Difficulty difficulty) {
        Set<ClanTask> tasks = new HashSet<>();
        for (ClanTask task : values()) {
            if (task.type == type && task.difficulty == difficulty)
                tasks.add(task);
        }
        return tasks;
    }

    public static ClanTask getAssignment(ClanType type, Difficulty difficulty) {
        if (type == ClanType.SOCIAL) {
            Set<ClanTask> tasks = new HashSet<>();
            tasks.addAll(getTasks(ClanType.PVM, difficulty));
            tasks.addAll(getTasks(ClanType.PVP, difficulty));
            tasks.addAll(getTasks(ClanType.SKILLING, difficulty));
            return Utility.randomElement(tasks);
        }

        if (type == ClanType.IRON_MAN) {
            Set<ClanTask> tasks = new HashSet<>();
            tasks.addAll(getTasks(ClanType.PVM, difficulty));
            tasks.addAll(getTasks(ClanType.SKILLING, difficulty));
            return Utility.randomElement(tasks);
        }

        return Utility.randomElement(getTasks(type, difficulty));
    }

    public int getReward() {
        return (difficulty == Difficulty.EASY) ? 2 : (difficulty == Difficulty.MEDIUM) ? 3 : 5;
    }
}
