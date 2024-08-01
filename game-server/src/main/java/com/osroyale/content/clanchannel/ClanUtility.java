package com.osroyale.content.clanchannel;

import com.osroyale.content.clanchannel.content.ClanLevel;

/**
 * This class holds all the util methods for the clan channel system.
 *
 * @author Daniel
 */
public class ClanUtility {

    /** Gets the ordinal of a member based on the button. */
    public static int getRankOrdinal(int button) {
        int base_button = -32003;
        int modified_button = (base_button - button) / 4;
        return Math.abs(modified_button);
    }

    /** Gets the ordinal of a member based on the button. */
    public static int getUnbanOrdinal(int button) {
        int base_button = -14225;
        int modified_button = (base_button - button);
        return Math.abs(modified_button);
    }

    /** Gets the rewards based on the clan level. */
    public static int[] getRewardItems(ClanLevel level) {
        int[] items = new int[30];
        switch (level) {
            case BRONZE:
                items = BRONZE_ITEMS;
                break;
            case IRON:
                items = IRON_ITEMS;
                break;
            case STEEL:
                items = STEEL_ITEMS;
                break;
            case BLACK:
                items = BLACK_ITEMS;
                break;
            case MITHRIL:
                items = MITHRIL_ITEMS;
                break;
            case ADAMANT:
                items = ADAMANT_ITEMS;
                break;
            case RUNE:
                items = RUNE_ITEMS;
                break;
            case DRAGON:
                items = DRAGON_ITEMS;
                break;
            case BARROW:
                break;
            case GILDED:
                break;
            case GODLY:
                break;
            case THIRD_AGE:
                break;
            case RARE:
                break;
        }
        return items;
    }

    /** Holds all the possible bronze item rewards from the mystery box. */
    private static final int[] BRONZE_ITEMS = {
            1155,//Bronze full helm
            1117,//Bronze platebody
            1075,//Bronze platelegs
            1189,//Bronze kiteshield
            1139,//Bronze med helm
            1103,//Bronze chainbody
            1087,//Bronze plateskirt
            1173,//Bronze sq shield
            1321,//Bronze scimitar
            1307,//Bronze 2h sword
            1291,//Bronze longsword
            1277,//Bronze sword
            1265,//Bronze pickaxe
            1351,//Bronze axe
            1375,//Bronze battleaxe
            1337,//Bronze warhammer
            1205,//Bronze dagger
            1422,//Bronze mace
            3095,//Bronze claws
            3190,//Bronze halberd
            4119,//Bronze boots
            7454,//Bronze gloves
            9174,//Bronze crossbow
            8844,//Bronze defender
            12363,//Bronze dragon mask
            2349,//Bronze bar
            12211,//Bronze full helm (g)
            12205,//Bronze platebody (g)
            12207,//Bronze platelegs (g)
            12213,//Bronze kiteshield (g)
            12209,//Bronze plateskirt (g)
            12221,//Bronze full helm (t)
            12215,//Bronze platebody (t)
            12217,//Bronze platelegs (t)
            12223,//Bronze kiteshield (t)
            12219,//Bronze plateskirt (t)
    };

    /** Holds all the possible iron item rewards from the mystery box. */
    private static final int[] IRON_ITEMS = {
            1153,//Iron full helm
            1115,//Iron platebody
            1067,//Iron platelegs
            1191,//Iron kiteshield
            1137,//Iron med helm
            1101,//Iron chainbody
            1081,//Iron plateskirt
            1175,//Iron sq shield
            1323,//Iron scimitar
            1309,//Iron 2h sword
            1293,//Iron longsword
            1279,//Iron sword
            1335,//Iron warhammer
            1363,//Iron battleaxe
            1420,//Iron mace
            1203,//Iron dagger
            1267,//Iron pickaxe
            1349,//Iron axe
            4121,//Iron boots
            9177,//Iron crossbow
            8845,//Iron defender
            2351,//Iron bar
            12225,//Iron platebody (t)
            12227,//Iron platelegs (t)
            12229,//Iron plateskirt (t)
            12231,//Iron full helm (t)
            12233,//Iron kiteshield (t)
            12235,//Iron platebody (g)
            12237,//Iron platelegs (g)
            12239,//Iron plateskirt (g)
            12241,//Iron full helm (g)
            12243,//Iron kiteshield (g)
            12365,//Iron dragon mask
            7455,//Iron gloves
            3096,//Iron claws
    };

    /** Holds all the possible steel item rewards from the mystery box. */
    private static final int[] STEEL_ITEMS = {
            1157,//Steel full helm
            1119,//Steel platebody
            1069,//Steel platelegs
            1193,//Steel kiteshield
            1141,//Steel med helm
            1105,//Steel chainbody
            1083,//Steel plateskirt
            1177,//Steel sq shield
            1207,//Steel dagger
            1325,//Steel scimitar
            1311,//Steel 2h sword
            1295,//Steel longsword
            1281,//Steel sword
            1339,//Steel warhammer
            1353,//Steel axe
            1365,//Steel battleaxe
            1424,//Steel mace
            2353,//Steel bar
            1269,//Steel pickaxe
            3097,//Steel claws
            4123,//Steel boots
            7456,//Steel gloves
            8846,//Steel defender
            9179,//Steel crossbow
            20169,//Steel platebody (g)
            20172,//Steel platelegs (g)
            20175,//Steel plateskirt (g)
            20178,//Steel full helm (g)
            20181,//Steel kiteshield (g)
            20184,//Steel platebody (t)
            20187,//Steel platelegs (t)
            20190,//Steel plateskirt (t)
            20193,//Steel full helm (t)
            20196,//Steel kiteshield (t)
            12367,//Steel dragon mask
    };

    /** Holds all the possible black item rewards from the mystery box. */
    private static final int[] BLACK_ITEMS = {
            1165,//Black full helm
            1125,//Black platebody
            1077,//Black platelegs
            1195,//Black kiteshield
            1151,//Black med helm
            1107,//Black chainbody
            1179,//Black sq shield
            1089,//Black plateskirt
            1217,//Black dagger
            8847,//Black defender
            1283,//Black sword
            1297,//Black longsword
            1313,//Black 2h sword
            1327,//Black scimitar
            1341,//Black warhammer
            1361,//Black axe
            1367,//Black battleaxe
            1426,//Black mace
            4125,//Black boots
            7457,//Black gloves
            2583,//Black platebody (t)
            2585,//Black platelegs (t)
            2587,//Black full helm (t)
            2589,//Black kiteshield (t)
            2591,//Black platebody (g)
            2593,//Black platelegs (g)
            2595,//Black full helm (g)
            2597,//Black kiteshield (g)
            3472,//Black plateskirt (t)
            3473,//Black plateskirt (g)
            3098,//Black claws
    };

    /** Holds all the possible mithril item rewards from the mystery box. */
    private static final int[] MITHRIL_ITEMS = {
            1159,//Mithril full helm
            1121,//Mithril platebody
            1071,//Mithril platelegs
            1197,//Mithril kiteshield
            1143,//Mithril med helm
            1109,//Mithril chainbody
            1085,//Mithril plateskirt
            1181,//Mithril sq shield
            1209,//Mithril dagger
            1273,//Mithril pickaxe
            1285,//Mithril sword
            1299,//Mithril longsword
            1315,//Mithril 2h sword
            1329,//Mithril scimitar
            1343,//Mithril warhammer
            1355,//Mithril axe
            1369,//Mithril battleaxe
            1428,//Mithril mace
            2359,//Mithril bar
            3099,//Mithril claws
            8848,//Mithril defender
            4127,//Mithril boots
            7458,//Mithril gloves
            12369,//Mithril dragon mask
            12277,//Mithril platebody (g)
            12279,//Mithril platelegs (g)
            12281,//Mithril kiteshield (g)
            12283,//Mithril full helm (g)
            12285,//Mithril plateskirt (g)
            12287,//Mithril platebody (t)
            12289,//Mithril platelegs (t)
            12291,//Mithril kiteshield (t)
            12293,//Mithril full helm (t)
            12295,//Mithril plateskirt (t)
    };

    /** Holds all the possible adamant item rewards from the mystery box. */
    private static final int[] ADAMANT_ITEMS = {
            1161,//Adamant full helm
            1123,//Adamant platebody
            1073,//Adamant platelegs
            1199,//Adamant kiteshield
            1145,//Adamant med helm
            1111,//Adamant chainbody
            1091,//Adamant plateskirt
            1183,//Adamant sq shield
            1211,//Adamant dagger
            1271,//Adamant pickaxe
            1287,//Adamant sword
            1301,//Adamant longsword
            1317,//Adamant 2h sword
            1331,//Adamant scimitar
            1345,//Adamant warhammer
            1357,//Adamant axe
            1371,//Adamant battleaxe
            1430,//Adamant mace
            2361,//Adamantite bar
            3100,//Adamant claws
            4129,//Adamant boots
            2361,//Adamantite bar
            8849,//Adamant defender
            7459,//Adamant gloves
            2599,//Adamant platebody (t)
            2601,//Adamant platelegs (t)
            2603,//Adamant kiteshield (t)
            2605,//Adamant full helm (t)
            2607,//Adamant platebody (g)
            2609,//Adamant platelegs (g)
            2611,//Adamant kiteshield (g)
            2613,//Adamant full helm (g)
            3474,//Adamant plateskirt (t)
            3475,//Adamant plateskirt (g)
    };

    /** Holds all the possible rune item rewards from the mystery box. */
    private static final int[] RUNE_ITEMS = {
            1163,//Rune full helm
            1127,//Rune platebody
            1079,//Rune platelegs
            1201,//Rune kiteshield
            1147,//Rune med helm
            1113,//Rune chainbody
            1093,//Rune plateskirt
            1185,//Rune sq shield
            1333,//Rune scimitar
            1319,//Rune 2h sword
            1303,//Rune longsword
            1289,//Rune sword
            1275,//Rune pickaxe
            1347,//Rune warhammer
            1213,//Rune dagger
            1359,//Rune axe
            1373,//Rune battleaxe
            1432,//Rune mace
            3101,//Rune claws
            4131,//Rune boots
            7460,//Rune gloves
            8850,//Rune defender
            2363,//Runite bar
            2615,//Rune platebody (g)
            2617,//Rune platelegs (g)
            2619,//Rune full helm (g)
            2621,//Rune kiteshield (g)
            2623,//Rune platebody (t)
            2625,//Rune platelegs (t)
            2627,//Rune full helm (t)
            2629,//Rune kiteshield (t)
            3476,//Rune plateskirt (g)
            3477,//Rune plateskirt (t)
    };

    /** Holds all the possible dragon item rewards from the mystery box. */
    private static final int[] DRAGON_ITEMS = {
            11335,//Dragon full helm
            2513,//Dragon chainbody
            4087,//Dragon platelegs
            1187,//Dragon sq shield
            1149,//Dragon med helm
            4585,//Dragon plateskirt
            1215,//Dragon dagger
            1249,//Dragon spear
            1305,//Dragon longsword
            1377,//Dragon battleaxe
            1434,//Dragon mace
            4587,//Dragon scimitar
            6739,//Dragon axe
            7158,//Dragon 2h sword
            7461,//Dragon gloves
            11840,//Dragon boots
            11920,//Dragon pickaxe
            12954,//Dragon defender
            13576,//Dragon warhammer
            13652,//Dragon claws
            21009,//Dragon sword
            21012,//Dragon hunter crossbow
            21028,//Dragon harpoon
            12414,//Dragon chainbody (g)
            12415,//Dragon platelegs (g)
            12416,//Dragon plateskirt (g)
            12417,//Dragon full helm (g)
            12418,//Dragon sq shield (g)
            20000,//Dragon scimitar (or)
            19722,//Dragon defender (t)
    };
}
