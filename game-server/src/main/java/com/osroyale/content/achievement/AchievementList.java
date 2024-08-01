package com.osroyale.content.achievement;

import com.osroyale.game.world.entity.skill.Skill;
import com.osroyale.game.world.items.Item;

import java.util.ArrayList;
import java.util.List;

import static com.osroyale.content.achievement.AchievementKey.*;
import static com.osroyale.content.achievement.AchievementType.*;

/**
 * Holds all the achievements
 *
 * @author Daniel
 */
public enum AchievementList {
    /** PvP achievements */
    KILL_10_PLAYERS("Kill 10 players using anything", 10, KILLER, PVP, 2500),
    KILL_50_PLAYERS("Kill 50 players using anything", 50, KILLER, PVP, 6500),
    KILL_150_PLAYERS("Kill 150 players using anything", 150, KILLER, PVP, 12500),
    KILL_500_PLAYERS("Kill 500 players using anything", 500, KILLER, PVP, 20000),
    KILL_1000_PLAYERS("Kill 1,000 players using anything", 1000, KILLER, PVP, 35000),
    KILL_10_WITH_DHAROK("Kill 10 players using full d'haroks", 10, DHAROK, PVP, 2500),
    KILL_50_WITH_DHAROK("Kill 50 players using full d'haroks", 50, DHAROK, PVP, 6500),
    KILL_150_WITH_DHAROK("Kill 150 players using full d'haroks", 150, DHAROK, PVP, 12500),


    HIT_80_WITH_AGS("Hit 80+ with AGS special on player 10 times", 10, AGS_SPEC, PVP, 5000),
    HIT_45_WITH_DRAGON_MACE("Hit 45+ with Dragon mace special\\non player 10 times", 10, DMACE_MAX, PVP, 5000),
    KILL_10_WITH_DRAGON_MACE("Kill 10 players using Dragon mace special", 10, DMACE_SPEC, PVP, 2500),
    KILL_75_WITH_DRAGON_MACE("Kill 75 players using Dragon mace special", 75, DMACE_SPEC, PVP, 5000),
    KILL_10_WITH_DDS("Kill 10 players using DDS special", 10, DDS_SPEC, PVP, 2500),
    KILL_150_WITH_DDS("Kill 150 players using DDS special", 150, DDS_SPEC, PVP, 5000),
    KILL_350_WITH_DDS("Kill 350 players using DDS special", 350, DDS_SPEC, PVP, 8000),
    KILL_10_WITH_AGS("Kill 10 players using AGS special", 10, AGS_SPEC, PVP, 2500),
    KILL_150_WITH_AGS("Kill 150 players using AGS special", 150, AGS_SPEC, PVP, 5000),
    KILL_350_WITH_AGS("Kill 350 players using AGS special", 350, AGS_SPEC, PVP, 8000),
    KILL_10_WITH_DCLAWS("Kill 10 players using Dragon claws special", 10, DCLAWS_SPEC, PVP, 2500),
    KILL_150_WITH_DCLAWS("Kill 150 players using Dragon claws special", 150, DCLAWS_SPEC, PVP, 5000),
    KILL_350_WITH_DCLAWS("Kill 350 players using Dragon claws special", 350, DCLAWS_SPEC, PVP, 8000),
    KILL_10_WITH_GMAUL("Kill 10 players with a Granite maul special", 10, GMAULED_SPEC, PVP, 2500),
    KILL_150_WITH_GMAUL("Kill 150 players with a Granite maul special", 150, GMAULED_SPEC, PVP, 5000),
    KILL_350_WITH_GMAUL("Kill 350 players with a Granite maul special", 350, GMAULED_SPEC, PVP, 8000),
    KILL_10_WITH_DARK_BOW("Kill 10 players using Dark bow special", 10, DARKBOW_SPEC, PVP, 2500),
    KILL_75_WITH_DARK_BOW("Kill 75 players using Dark bow special", 75, DARKBOW_SPEC, PVP, 5000),
    GET_A_5_KILLSTREAK("Kill 5 players without dying", 1, KILLSTKREAK_5, PVP, 1000),
    GET_A_10_KILLSTREAK("Kill 10 players without dying", 1, KILLSTKREAK_10, PVP, 2500),
    GET_A_15_KILLSTREAK("Kill 15 players without dying", 1, KILLSTKREAK_15, PVP, 5000),
    GET_A_25_KILLSTREAK("Kill 25 players without dying", 1, KILLSTKREAK_25, PVP, 8500),

    /** PvM achievements */
    KILL_10_KBD("Kill King Black Dragons 10 times", 10, KING_BLACK_DRAGON, PVM, 1000),
    KILL_100_KBD("Kill King Black Dragons 100 times", 100, KING_BLACK_DRAGON, PVM, 3000),
    KILL_350_KBD("Kill King Black Dragons 350 times", 350, KING_BLACK_DRAGON, PVM, 6000),
    KILL_800_KBD("Kill King Black Dragons 800 times", 800, KING_BLACK_DRAGON, PVM, 10000),
    KILL_10_KRAKENS("Kill Kraken 10 times", 10, KRAKEN, PVM, 1000),
    KILL_100_KRAKENS("Kill Kraken 100 times", 100, KRAKEN, PVM, 3000),
    KILL_350_KRAKENS("Kill Kraken 350 times", 350, KRAKEN, PVM, 6000),
    KILL_800_KRAKENS("Kill Kraken 800 times", 800, KRAKEN, PVM, 10000),
    KILL_10_ZULRAH("Kill Zulrah 10 times", 10, ZULRAH, PVM, 1000),
    KILL_50_ZULRAH("Kill Zulrah 50 times", 50, ZULRAH, PVM, 3000),
    KILL_300_ZULRAHS("Kill Zulrah 300 times", 300, ZULRAH, PVM, 6000),
    KILL_500_ZULRAHS("Kill Zulrah 500 times", 500, ZULRAH, PVM, 10000),
    KILL_10_COMMANDERS("Kill Commander Zilyana 10 times", 10, ZILYANA, PVM, 1000),
    KILL_35_COMMANDERS("Kill Commander Zilyana 35 times", 35, ZILYANA, PVM, 3000),
    KILL_125_COMMANDERS("Kill Commander Zilyana 125 times", 125, ZILYANA, PVM, 6000),
    KILL_500_COMMANDERS("Kill Commander Zilyana 500 times", 500, ZILYANA, PVM, 10000),
    KILL_10_GENERARS("Kill General Graardor 10 times", 10, GRAARDOR, PVM, 1000),
    KILL_35_GENERALS("Kill General Graardor 35 times", 35, GRAARDOR, PVM, 3000),
    KILL_125_GENERALS("Kill General Graardor 125 times", 125, GRAARDOR, PVM, 6000),
    KILL_500_GENERALS("Kill General Graardor 500 times", 500, GRAARDOR, PVM, 10000),
    KILL_10_KREES("Kill Kree Arra 10 times", 10, KREE, PVM, 1000),
    KILL_35_KREES("Kill Kree Arra 35 times", 35, KREE, PVM, 3000),
    KILL_125_KREES("Kill Kree Arra 125 times", 125, KREE, PVM, 6000),
    KILL_500_KREES("Kill Kree Arra 500 times", 500, KREE, PVM, 10000),
    KILL_10_KRILS("Kill K'ril Tsutaroth 10 times", 10, KRIL, PVM, 1000),
    KILL_35_KRILS("Kill K'ril Tsutaroth 35 times", 35, KRIL, PVM, 3000),
    KILL_125_KRILS("Kill K'ril Tsutaroth 125 times", 125, KRIL, PVM, 6000),
    KILL_500_KRILS("Kill K'ril Tsutaroth 500 times", 500, KRIL, PVM, 10000),
    KILL_10_CERBERUS("Kill Cerberus 10 times", 10, CERBERUS, PVM, 1000),
    KILL_35_CERBERUS("Kill Cerberus 35 times", 35, CERBERUS, PVM, 3000),
    KILL_125_CERBERUS("Kill Cerberus 125 times", 125, CERBERUS, PVM, 6000),
    KILL_500_CERBERUS("Kill Cerberus 500 times", 500, CERBERUS, PVM, 10000),
    KILL_10_VORKATHS("Kill Vorkath 10 times", 10, VORKATH, PVM, 1000),
    KILL_35_VORKATHS("Kill Vorkath 35 times", 35, VORKATH, PVM, 3000),
    KILL_125_VORKATHS("Kill Vorkath 125 times", 125, VORKATH, PVM, 6000),
    KILL_500_VORKATHS("Kill Vorkath 500 times", 500, VORKATH, PVM, 10000),
    KILL_100_REVS("Kill 100 revenants", 100, REVS, PVM, 10000),
    KILL_250_REVS("Kill 250 revenants", 250, REVS, PVM, 10000),
    KILL_500_REVS("Kill 500 revenants", 500, REVS, PVM, 10000),
    KILL_1000_REVS("Kill 1000 revenants", 1000, REVS, PVM, 10000),

    /** Skilling achievements */
    MAX_1_SKILL("Achieve level 99 in 1 skill", 1, SKILL_MASTERY, SKILLING, 2000),
    MAX_3_SKILLS("Achieve level 99 in 3 skills", 3, SKILL_MASTERY, SKILLING, 4000),
    MAX_ALL_SKILLS("Achieve level 99 in all skills", Skill.SKILL_COUNT, SKILL_MASTERY, SKILLING, 10000),
    GET_1000_TOTAL_LEVELS("Achieve 1,000 total level", 1000, TOTAL_LEVEL, SKILLING, 3000),
    GET_1500_TOTAL_LEVEL("Achieve 1,500 total level", 1500, TOTAL_LEVEL, SKILLING, 5000),
    GET_2000_TOTAL_LEVELS("Achieve 2,000 total level", 2000, TOTAL_LEVEL, SKILLING, 8000),
    EXPERIENCE_MASTERY("Earn 200M EXP in 5 skills", 5, AchievementKey.EXPERIENCE_MASTERY, SKILLING, 7500),
    MAX_200M_EXPERIENCE("Earn 200M EXP in all skills", Skill.SKILL_COUNT, AchievementKey.EXPERIENCE_MASTERY, SKILLING, 30000),
    CAST_100_HIGH_ALCHEMY("Cast high alchemy spell 100 times", 100, HIGH_ALCHEMY, SKILLING, 750),
    CHOP_100_TREES("Chop down 100 trees", 100, WOODCUTTING, SKILLING, 1000),
    CHOP_500_TREES("Chop down 500 trees", 500, WOODCUTTING, SKILLING, 4000),
    CHOP_1500_TREES("Chop down 1,500 trees", 1500, WOODCUTTING, SKILLING, 4000),
    CHOP_5000_TREES("Chop down 5,000 trees", 5000, WOODCUTTING, SKILLING, 7000),
    MINE_100_ROCKS("Mine 100 rocks", 100, MINNING, SKILLING, 1000),
    MINE_500_ROCKS("Mine 500 rocks", 500, MINNING, SKILLING, 2500),
    MINE_1500_ROCKS("Mine 1,500 rocks", 1500, MINNING, SKILLING, 4000),
    MINE_5000_ROCKS("Mine 5,000 rocks", 5000, MINNING, SKILLING, 7000),
    CRAFT_100_OF_ANY_RUNES("Craft 100 of any runes", 100, RUNECRAFTING, SKILLING, 500),
    CRAFT_500_OF_ANY_RUNES("Craft 500 of any runes", 500, RUNECRAFTING, SKILLING, 1500),
    CRAFT_1500_OF_ANY_RUNES("Craft 1500 of any runes", 1500, RUNECRAFTING, SKILLING, 4000),
    CRAFT_5000_OF_ANY_RUNES("Craft 5000 of any runes", 5000, RUNECRAFTING, SKILLING, 7000),
    RUN_50_COURSES("Complete 50 agility courses", 50, AGILITY, SKILLING, 5000),
    RUN_150_COURSES("Complete 150 agility courses", 150, AGILITY, SKILLING, 10000),
    RUN_500_COURSES("Complete 500 agility courses", 500, AGILITY, SKILLING, 25000),
    RUN_1000_COURSES("Complete 1,000\\nagility courses", 1000, AGILITY, SKILLING, 50000),
    ENCHANT_2500_BOLTS("Enchant 2,500 bolts", 2500, ENCHANT_BOLTS, SKILLING, 2500),
    ENCHANT_10000_BOLTS("Enchant 10,000 bolts", 10000, ENCHANT_BOLTS, SKILLING, 5000),
    FLETCH_2500_ARROWS("Fletch 2,500 arrows", 2500, FLETCHING, SKILLING, 2500),
    FLETCH_10000_ARROWS("Fletch 10,000 arrows", 10000, FLETCHING, SKILLING, 5000),
    MAKE_2500_POTIONS("Make 2,500 potions with herblore", 2500, HERBLORE, SKILLING, 3250),
    MAKE_10000_POTIONS("Make 10,000 potions with herblore", 10000, HERBLORE, SKILLING, 7500),

    /** Minigame achievements */
    GET_YOUR_FIRST_FIRE_CAPE("Get your first fire cape", 1, FIRE_CAPE, MINIGAME, 150),
    GET_10_FIRE_CAPES("Get 10 fire capes", 10, FIRE_CAPE, MINIGAME, 1000),
    COMPLETE_5_BARROWS("Complete 5 Barrows runs", 5, BARROWS, MINIGAME, 500),
    COMPLETE_50_BARROWS("Complete 50 Barrows runs", 50, BARROWS, MINIGAME, 1500),
    COMPLETE_100_BARROW("Complete 100 Barrows runs", 100, BARROWS, MINIGAME, 3500),
    COMPLETE_500_BARROWS("Complete 500 Barrows runs", 500, BARROWS, MINIGAME, 7500),
    GET_1_ITEM_FROM_BARROWS("Get 1 unique item from barrows", 1, BARROWS_UNIQUE, MINIGAME, 250),
    GET_10_ITEM_FROM_BARROWS("Get 10 unique item from barrows", 10, BARROWS_UNIQUE, MINIGAME, 1000),
    COMPLETE_5_PC_GAME("Complete 5 PC game", 5, PEST_CONTROL, MINIGAME, 500),
    COMPLETE_25_PC_GAME("Complete 25 PC games", 25, PEST_CONTROL, MINIGAME, 1500),
    COMPLETE_50_PC_GAME("Complete 50 PC games", 50, PEST_CONTROL, MINIGAME, 3500),
    COMPLETE_150_PC_GAME("Complete 150 PC games", 150, PEST_CONTROL, MINIGAME, 7500),

    /** Misc achievements */
    CHANGE_APPEARANCE("Change your appearance once", 1, AchievementKey.CHANGE_APPEARANCE, MISCELLANEOUS, 500),
    BURY_10_BONES("Bury 10 bones (any)", 10, BURY_BONES, MISCELLANEOUS, 500),
    COMPLETE_10_SLAYER_TASKS("Complete 10 slayer tasks", 10, SLAYER_TASKS, MISCELLANEOUS, 10000),
    COMPLETE_50_SLAYER_TASKS("Complete 50 slayer tasks", 50, SLAYER_TASKS, MISCELLANEOUS, 10000),
    COMPLETE_100_SLAYER_TASKS("Complete 100 slayer tasks", 100, SLAYER_TASKS, MISCELLANEOUS, 10000),
    COMPLETE_250_SLAYER_TASKS("Complete 250 slayer tasks", 250, SLAYER_TASKS, MISCELLANEOUS, 10000),
    CLAIM_100_VOTE("Claim 100 vote reward", 100, VOTE, MISCELLANEOUS, 1000),
    CLAIM_250_VOTES("Claim 250 vote rewards", 250, VOTE, MISCELLANEOUS, 2500),
    CLAIM_500_VOTES("Claim 500 vote rewards", 500, VOTE, MISCELLANEOUS, 5000),
    CLAIM_1000_VOTES("Claim 1,000 vote rewards", 1000, VOTE, MISCELLANEOUS, 10000),
    ANSWER_10_TRIVIA_BOTS("Answer 10 TriviaBot questions", 10, TRIVIABOT, MISCELLANEOUS, 2500),
    ANSWER_30_TRIVIA_BOTS("Answer 30 TriviaBot questions", 30, TRIVIABOT, MISCELLANEOUS, 5000),
    ANSWER_100_TRIVIA_BOTS("Answer 100 TriviaBot questions", 100, TRIVIABOT, MISCELLANEOUS, 7500),
    ANSWER_500_TRIVIA_BOTS("Answer 500 TriviaBot questions", 500, TRIVIABOT, MISCELLANEOUS, 10000),
    OPEN_10_CRYSTAL_CHESTS("Open 10 crystal chests", 10, CRYSTAL_CHEST, MISCELLANEOUS, 1000),
    OPEN_50_CRYSTAL_CHESTS("Open 50 crystal chests", 50, CRYSTAL_CHEST, MISCELLANEOUS, 2500),
    OPEN_150_CRYSTAL_CHESTS("Open 150 crystal chests", 150, CRYSTAL_CHEST, MISCELLANEOUS, 5000),
    OPEN_500_CRYSTAL_CHESTS("Open 500 crystal chests", 500, CRYSTAL_CHEST, MISCELLANEOUS, 7500),
    COMPLETE_10_CLUE_SCROLLS("Complete 10 clue scrolls", 10, CLUE_SCROLLS, MISCELLANEOUS, 1500),
    COMPLETE_25_CLUE_SCROLLS("Complete 25 clue scrolls", 25, CLUE_SCROLLS, MISCELLANEOUS, 1500),
    COMPLETE_50_CLUE_SCROLLS("Complete 50 clue scrolls", 50, CLUE_SCROLLS, MISCELLANEOUS, 1500),
    COMPLETE_100_CLUE_SCROLLS("Complete 100 clue scrolls", 100, CLUE_SCROLLS, MISCELLANEOUS, 1500),
    OPEN_10_CASKETS("Open 10 caskets", 10, CASKET, MISCELLANEOUS, 1000),
    OPEN_50_CASKETS("Open 50 caskets", 50, CASKET, MISCELLANEOUS, 2500),
    OPEN_150_CASKETS("Open 150 caskets", 150, CASKET, MISCELLANEOUS, 5000),
    OPEN_500_CASKETS("Open 500 caskets", 500, CASKET, MISCELLANEOUS, 7500),
    KILL_1000_NPCS("Kill 1000 NPC's", 1000, NPCS, MISCELLANEOUS, 10000),
    KILL_2500_NPCS("Kill 2500 NPC's", 2500, NPCS, MISCELLANEOUS, 10000),
    KILL_5000_NPCS("Kill 5000 NPC's", 5000, NPCS, MISCELLANEOUS, 10000),
    KILL_10000_NPCS("Kill 10000 NPC's", 10000, NPCS, MISCELLANEOUS, 10000),
    OBTAIN_50_RARE_DROPS("Obtain 50 rare drops", 50, RARE_DROPS, MISCELLANEOUS, 1000),
    OBTAIN_100_RARE_DROPS("Obtain 100 rare drops", 100, RARE_DROPS, MISCELLANEOUS, 1000),
    OBTAIN_250_RARE_DROPS("Obtain 250 rare drops", 250, RARE_DROPS, MISCELLANEOUS, 1000),
    OBTAIN_500_RARE_DROPS("Obtain 500 rare drops", 500, RARE_DROPS, MISCELLANEOUS, 1000),
    OBTAIN_YOUR_FIRST_PET("Obtain 1 of any pets", 1, OBTAIN_PET, MISCELLANEOUS, 1000),
    OBTAIN_5_PETS("Obtain 5 of any pets", 5, OBTAIN_PET, MISCELLANEOUS, 2500),
    OBTAIN_10_PETS("Obtain 10 of any pets", 10, OBTAIN_PET, MISCELLANEOUS, 5000),;

    private final int amount;
    private final AchievementKey key;
    private final AchievementType type;
    private final String task;
    private final int reward;

    AchievementList(String task, int amount, AchievementKey key, AchievementType type, int reward) {
        this.amount = amount;
        this.key = key;
        this.type = type;
        this.task = task;
        this.reward = reward;
    }

    public int getAmount() {
        return amount;
    }

    public AchievementKey getKey() {
        return key;
    }

    public AchievementType getType() {
        return type;
    }

    public String getTask() {
        return task;
    }

    public Item getReward() {
        return new Item(13307, reward);
    }

    public static List<AchievementList> asList(AchievementType difficulty) {
        List<AchievementList> list = new ArrayList<>();

        for (AchievementList achievement : values()) {
            if (achievement.type == difficulty) {
                list.add(achievement);
            }
        }

        return list;
    }

    public static int getTotal() {
        return values().length;
    }

}
