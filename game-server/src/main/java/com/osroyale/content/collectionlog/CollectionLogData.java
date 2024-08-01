package com.osroyale.content.collectionlog;

import java.util.ArrayList;

public enum CollectionLogData {

    //Bosses
    BARROWS(CollectionCategory.BOSSES, "Barrows", "Barrows chests looted", new int[] {4708, 4716, 4724, 4732, 4753, 4745, 4710, 4718, 4726, 4734, 4755, 4747, 4712, 4720, 4728, 4736, 4757, 4749, 4714, 4722, 4730, 4738, 4759, 4751, 12881, 12877, 12873, 12883, 12875,  12879, 12851, 6798}),
    CALLISTO(CollectionCategory.BOSSES, "Callisto", "Callisto kills", new int[] {13178, 12603, 11920, 7158}, 6503),
    ABYSSAL_SIRE(CollectionCategory.BOSSES, "Abyssal Sire", new int[] { 13262, 13273, 7979, 13265, 13274, 13275, 13276, 13277 }, 3030, 3033, 3029, 3032),
    CERBERUS(CollectionCategory.BOSSES, "Cerberus", "Cerberus Kills", new int[] {13247, 13227, 13229, 13231, 13245, 13233 }, 5862),
    CHAOS_FANATIC(CollectionCategory.BOSSES, "Chaos Fanatic", "Chaos Fanatic Kills", new int[] {11995, 11926, 11924}, 6619),
    COMMANDER_ZILYANA(CollectionCategory.BOSSES, "Commander Zilyana", "Commander Zilyana Kills", new int[] {12651, 11785, 11814, 11838, 13256, 11818, 11820, 11822}, 2205),
    CORPOREAL_BEAST(CollectionCategory.BOSSES, "Corporeal Beast",  "Corporeal Beast Kills", new int[] {12816, 12819, 12823, 12827, 12833, 12829}, 319),
    CRAZY_ARCHAEOLOGIST(CollectionCategory.BOSSES, "Crazy Archaeologist", "Crazy Archaeologist Kills", new int[] {11926, 11924, 11990}, 6618),
    GENERAL_GRAARDOR(CollectionCategory.BOSSES, "General Graardor", "General Graardor Kills", new int[] {12650, 11832, 11834, 11836, 11812, 11818, 11820, 11822}, 2242, 2243, 2244, 2245, 2235, 2240),

    KING_BLACK_DRAGON(CollectionCategory.BOSSES, "King Black Dragon", "King Black Dragon Kills", new int[] {12653, 7980, 11920, 11286}, 239),
    KRAKEN(CollectionCategory.BOSSES, "Kraken", "Kraken Kills", new int[] {12655, 12004, 11907, 12007}, 494),
    KREE_ARRA(CollectionCategory.BOSSES, "Kree'arra", "Kree'arra Kills", new int[] {12649, 11826, 11828, 11830, 11810, 11818, 11820, 11822}, 3162),
    KRIL_TSUTSAROTH(CollectionCategory.BOSSES, "K'ril Tsutsaroth", "K'ril Tsutsaroth Kills", new int[] {12652, 11791, 11824, 11787, 11816, 11818, 11820, 11822}, 3129),
    SCORPIA(CollectionCategory.BOSSES, "Scorpia", "Scorpia Kills", new int[] {13181, 11926, 11924}, 6615),
    SKOTIZO(CollectionCategory.BOSSES, "Skotizo", "Skotizo Kills", new int[] {16014, 19701, 21275, 19685, 6571, 19677}, 7286),
    VENENATIS(CollectionCategory.BOSSES, "Venenatis", "Venenatis Kills", new int[] {13177, 12605, 11920, 7158}, 6504),
    VETION(CollectionCategory.BOSSES, "Vet'ion", "Vet'ion Kills", new int[] {13179, 12601, 11920, 7158}, 6611),
    VORKATH(CollectionCategory.BOSSES, "Vorkath", "Vorkath Kills", new int[] {21992, 2425, 11286, 22002, 22106, 22111}, 8060),
    ZULRAH(CollectionCategory.BOSSES, "Zulrah", "Zulrah Kills", new int[] { 12921, 13200, 13201, 12936, 12932, 12927, 12922, 12938, 6571, 12934 }, 2042,2043,2044),

    //Raids
    CHAMBERS_OF_XERIC(CollectionCategory.RAIDS, "Chambers of Xeric", "Chests Looted", new int[] {20784, 21000, 21043, 21015, 21018, 21021, 21024, 20997, 21003, 20851, 21079, 21034}),

    //Clues
    EASY_CLUES(CollectionCategory.CLUES, "Easy Clue Scrolls", "Scrolls Opened", new int[] { 1077, 1089, 1107, 1125, 1151, 1165, 1179, 1195, 1217, 1283, 1297, 1313, 1327,
    1341, 1361, 1367, 1426, 8778, 849, 1167, 1095, 1129, 1131, 1063, 1061, 1059, 1167, 329, 333, 1438, 1440, 1442, 1444, 1446, 1448, 1269, 1452, 1454, 1456, 1458, 1462,
    12205, 12207, 12209, 12211, 12213, 12215, 12217, 12219, 12221, 12223, 12225, 12227, 12229, 12231, 12233, 12235, 12237, 12239, 12241, 12243, 2583, 2585, 2587, 2589, 2591, 2593,
    2595, 2597, 3472, 3473, 2635, 2637, 12247, 2633, 2631, 12245, 7386, 7390, 7394, 7396, 7388, 7392, 12449, 12453, 12445, 12447, 12451, 12455, 7364, 7368, 7362, 7366, 7332, 7338,
    7344, 7350, 7356, 10306, 10308, 10310, 10312, 10314, 10404, 10406, 10424, 10426, 10412, 10414, 10432, 10434, 10408, 10410, 10428, 10430, 10316, 10318, 10320, 10322, 10324,
    10392, 10394, 10396, 10398, 10366, 12375, 12297, 10458, 10464, 12193, 12195, 12253, 12255, 12265, 12267, 20217, 20211, 20214, 12249, 12251, 20166, 20205, 20208, 20199,
            20202, 20164}),

    //Minigames
    WARRIORS_GUILD(CollectionCategory.MINIGAMES, "Warrior's Guild", "Defenders obtained", new int[]{8844, 8845, 8846, 8847, 8848, 8849, 8850, 12954}),
    WINTERTODT(CollectionCategory.MINIGAMES, "Wintertodt", "Wintertodt kills", new int[]{20693, 20716, 20718, 20708, 20704, 20706, 20712, 20710, 20720, 6739}),
    //Other
    SHOOTING_STAR(CollectionCategory.OTHER, "Shooting Star", "Shooting Star Loot", new int[]{25527}),
    ;

    private final CollectionCategory type;
    private final String name;
    private final int[] items;
    private final String counterText;
    private final int[] npcIds;

    CollectionLogData(CollectionCategory category, String name, int[] items, int... npcId) {
        this.type = category;
        this.name = name;
        this.items = items;
        this.counterText = null;
        this.npcIds = npcId;
    }

    CollectionLogData(CollectionCategory category, String name, String counterText, int[] items) {
        this.type = category;
        this.name = name;
        this.items = items;
        this.counterText = counterText;
        this.npcIds = new int[0];
    }

    CollectionLogData(CollectionCategory category, String name, String counterText, int[] items, int... npcId) {
        this.type = category;
        this.name = name;
        this.items = items;
        this.counterText = counterText;
        this.npcIds = npcId;
    }

    public static ArrayList<CollectionLogData> getPageList(CollectionLogPage page) {
        ArrayList<CollectionLogData> list = new ArrayList<>();
        for (CollectionLogData data : values()) {
            if (data.getType() == page.getCategory()) {
                list.add(data);
            }
        }
        return list;
    }

    public int[] getItems() {
        return this.items;
    }

    public int[] getNpcIds() {
        return this.npcIds;
    }

    public CollectionCategory getType() {
        return this.type;
    }

    public String getName() {
        return this.name;
    }

    public String getCounterText() {
        return this.counterText;
    }
}