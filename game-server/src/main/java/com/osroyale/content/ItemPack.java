package com.osroyale.content;

import com.osroyale.game.world.items.Item;

import java.util.Arrays;
import java.util.Optional;

/**
 * Handles the opening of armour sets.
 *
 * @author Daniel
 */
public enum ItemPack {
    // barrows
    GUTHAN_ARMOUR_SET(12873, true, new Item(4724), new Item(4728), new Item(4730), new Item(4726)),
    VERAC_ARMOUR_SET(12875, true, new Item(4753), new Item(4757), new Item(4759), new Item(4755)),
    DHAROK_ARMOUR_SET(12877, true, new Item(4716), new Item(4720), new Item(4722), new Item(4718)),
    TORAG_ARMOUR_SET(12879, true, new Item(4745), new Item(4749), new Item(4751), new Item(4747)),
    AHRIM_ARMOUR_SET(12881, true, new Item(4708), new Item(4712), new Item(4714), new Item(4710)),
    KARIL_ARMOUR_SET(12883, true, new Item(4732), new Item(4736), new Item(4738), new Item(4734)),

    // misc
    DWARF_CANNON_SET(12863, true, new Item(10), new Item(12), new Item(6), new Item(8)),
    SUPER_POTION_SET(13066, true, new Item(2436), new Item(2440), new Item(2442)),
    PARTY_HAT_SET(13173, true, new Item(1038), new Item(1040), new Item(1044), new Item(1046), new Item(1048), new Item(2422)),
    HALLOWEEN_MASK_SET(13175, true, new Item(1053), new Item(1055), new Item(1057)),
    OBSIDIAN_ARMOUR_SET(21279, true, new Item(21298), new Item(21301), new Item(21304)),
    ANCESTRAL_ROBE_SET(21049, true, new Item(21018), new Item(21024), new Item(21021)),

    /* Regular Armour Sets */
    BRONZE_ARMOUR_LG(12960, true, new Item(1155), new Item(1117), new Item(1075), new Item(1189)),
    BRONZE_ARMOUR_SK(12962, true, new Item(1155), new Item(1117), new Item(1087), new Item(1189)),
    IRON_ARMOUR_LG(12972, true, new Item(1153), new Item(1115), new Item(1067), new Item(1191)),
    IRON_ARMOUR_SK(12974, true, new Item(1153), new Item(1115), new Item(1081), new Item(1191)),
    STEEL_ARMOUR_LG(12984, true, new Item(1157), new Item(1119), new Item(1069), new Item(1193)),
    STEEL_ARMOUR_SK(12986, true, new Item(1157), new Item(1119), new Item(1083), new Item(1193)),
    BLACK_ARMOUR_LG(12988, true, new Item(1165), new Item(1125), new Item(1077), new Item(1195)),
    BLACK_ARMOUR_SK(12990, true, new Item(1165), new Item(1125), new Item(1089), new Item(1195)),
    MITHRIL_ARMOUR_LG(13000, true, new Item(1159), new Item(1121), new Item(1071), new Item(1197)),
    MITHRIL_ARMOUR_SK(13002, true, new Item(1159), new Item(1121), new Item(1085), new Item(1197)),
    ADAMANT_ARMOUR_LG(13012, true, new Item(1161), new Item(1123), new Item(1073), new Item(1199)),
    ADAMANT_ARMOUR_SK(13014, true, new Item(1161), new Item(1123), new Item(1091), new Item(1199)),
    RUNE_ARMOUR_LG(13024, true, new Item(1163), new Item(1127), new Item(1079), new Item(1201)),
    RUNE_ARMOUR_SK(13026, true, new Item(1163), new Item(1127), new Item(1093), new Item(1201)),
    PROSELYTE_M(9666, true, new Item(9672), new Item(9674), new Item(9676)),
    PROSELYTE_F(9670, true, new Item(9672), new Item(9674), new Item(9678)),
    GREEN_DHIDE(12865, true, new Item(1065), new Item(1099), new Item(1135)),
    BLUE_DHIDE(12867, true, new Item(2487), new Item(2493), new Item(2499)),
    RED_DHIDE(12869, true, new Item(2489), new Item(2495), new Item(2501)),
    BLACK_DHIDE(12871, true, new Item(2491), new Item(2497), new Item(2503)),

    /* Trimmed Armour sets */
    BRONZE_TRIMMED_LG(12964, true, new Item(12215), new Item(12217), new Item(12221), new Item(12223)),
    BRONZE_TRIMMED_SK(12966, true, new Item(12215), new Item(12219), new Item(12221), new Item(12223)),
    BRONZE_GOLD_TRIMMED_LG(12968, true, new Item(12205), new Item(12207), new Item(12211), new Item(12213)),
    BRONZE_GOLD_TRIMMED_SK(12970, true, new Item(12205), new Item(12209), new Item(12211), new Item(12213)),
    IRON_TRIMMED_LG(12976, true, new Item(12225), new Item(12227), new Item(12231), new Item(12233)),
    IRON_TRIMMED_SK(12978, true, new Item(12225), new Item(12229), new Item(12231), new Item(12233)),
    IRON_GOLD_TRIMMED_LG(12980, true, new Item(12235), new Item(12237), new Item(12241), new Item(12243)),
    IRON_GOLD_TRIMMED_SK(12982, true, new Item(12235), new Item(12239), new Item(12241), new Item(12243)),
    BLACK_TRIMMED_LG(12992, true, new Item(2583), new Item(2585), new Item(2587), new Item(2589)),
    BLACK_TRIMMED_SK(12994, true, new Item(2583), new Item(3472), new Item(2587), new Item(2589)),
    BLACK_GOLD_TRIMMED_LG(12996, true, new Item(2591), new Item(2593), new Item(2595), new Item(2597)),
    BLACK_GOLD_TRIMMED_SK(12998, true, new Item(2591), new Item(3473), new Item(2595), new Item(2597)),
    MITRHIL_TRIMMED_LG(13004, true, new Item(12287), new Item(12289), new Item(12291), new Item(12293)),
    MITRHIL_TRIMMED_SK(13006, true, new Item(12287), new Item(12295), new Item(12291), new Item(12293)),
    MITRHIL_GOLD_TRIMMED_LG(13008, true, new Item(12277), new Item(12279), new Item(12281), new Item(12283)),
    MITRHIL_GOLD_TRIMMED_SK(13010, true, new Item(12277), new Item(12285), new Item(12281), new Item(12283)),
    ADAMANT_TRIMMED_LG(13016, true, new Item(2259), new Item(2601), new Item(2603), new Item(2605)),
    ADAMANT_TRIMMED_SK(13018, true, new Item(2259), new Item(3474), new Item(2603), new Item(2605)),
    ADAMANT_GOLD_TRIMMED_LG(13020, true, new Item(2607), new Item(2609), new Item(2611), new Item(2613)),
    ADAMANT_GOLD_TRIMMED_SK(13022, true, new Item(2607), new Item(3475), new Item(2611), new Item(2613)),
    RUNE_TRIMMED_LG(13028, true, new Item(2623), new Item(2625), new Item(2627), new Item(2629)),
    RUNE_TRIMMED_SK(13030, true, new Item(2623), new Item(3477), new Item(2627), new Item(2629)),
    RUNE_GOLD_TRIMMED_LG(13032, true, new Item(2615), new Item(2617), new Item(2619), new Item(2621)),
    RUNE_GOLD_TRIMMED_SK(13034, true, new Item(2615), new Item(3476), new Item(2619), new Item(2621)),
    GILDED_ARMOUR_LG(13036, true, new Item(3481), new Item(3483), new Item(3486), new Item(3488)),
    GILDED_ARMOUR_SK(13038, true, new Item(3481), new Item(3485), new Item(3486), new Item(3488)),

    /* Packs */
    EMPTY_VIAL_PACK(11877, false, new Item(230, 100)),
    WATER_FILLED_VIAL_PACK(11879, false, new Item(228, 100)),
    FEATHER_PACK(11881, false, new Item(314, 100)),
    BAIT_PACK(11883, false, new Item(313, 100)),
    SOFT_CLAY_PACK(12009, false, new Item(1762, 100)),
    BROAD_ARROWHEAD_PACK(11885, false, new Item(11874, 100)),
    AIR_RUNE_PACK(12728, false, new Item(556, 100)),
    WATER_RUNE_PACK(12730, false, new Item(555, 100)),
    EARTH_RUNE_PACK(12732, false, new Item(557, 100)),
    FIRE_RUNE_PACK(12734, false, new Item(554, 100)),
    MIND_RUNE_PACK(12736, false, new Item(558, 100)),
    CHAOS_RUNE_PACK(12738, false, new Item(562, 100)),
    BIRD_SNARE_PACK(12740, false, new Item(10007, 100)),
    BOX_SNARE_PACK(12742, false, new Item(10009, 100)),
    MAGIC_BOX_PACK(12744, false, new Item(10026, 100)),
    EYE_OF_NEW_PACK(12859, false, new Item(222, 100)),

    /* Magic Box */
    MAGIC_BOX(10025, false, new Item(554, 250), new Item(555, 250), new Item(556, 250), new Item(557, 250), new Item(558, 250), new Item(563, 50), new Item(561, 30), new Item(562, 100)),;

    /** The armour set identification. */
    public final int armour;

    /** If item set must be opened by chisel. */
    public final boolean chisel;

    /** The items inside the armour set. */
    public final Item items[];

    /** Constructs a new <code>ItemPack</code>. */
    ItemPack(int armour, boolean chisel, Item... items) {
        this.armour = armour;
        this.chisel = chisel;
        this.items = items;
    }

    /** Streams through the armour set to find the data based on the item identification. */
    public static Optional<ItemPack> forItem(int id) {
        return Arrays.stream(values()).filter(a -> a.armour == id).findAny();
    }
}
