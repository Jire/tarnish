package plugin.click.item;

import com.osroyale.game.event.impl.ItemClickEvent;
import com.osroyale.game.plugin.PluginContext;
import com.osroyale.game.world.entity.mob.player.Player;
import com.osroyale.game.world.items.Item;
import com.osroyale.net.packet.out.SendMessage;

/**
 * Created by Daniel on 2018-01-04.
 */
public class PackOpeningPlugin extends PluginContext {
    public enum  PackData {
        /* Regular Armour Sets */
        BRONZE_ARMOUR_LG(12960, new Item(1155), new Item(1117), new Item(1075), new Item(1189)),
        BRONZE_ARMOUR_SK(12962, new Item(1155), new Item(1117), new Item(1087), new Item(1189)),
        IRON_ARMOUR_LG(12972, new Item(1153), new Item(1115), new Item(1067), new Item(1191)),
        IRON_ARMOUR_SK(12974, new Item(1153), new Item(1115), new Item(1081), new Item(1191)),
        STEEL_ARMOUR_LG(12984, new Item(1157), new Item(1119), new Item(1069), new Item(1193)),
        STEEL_ARMOUR_SK(12986, new Item(1157), new Item(1119), new Item(1083), new Item(1193)),
        BLACK_ARMOUR_LG(12988, new Item(1165), new Item(1125), new Item(1077), new Item(1195)),
        BLACK_ARMOUR_SK(12990, new Item(1165), new Item(1125), new Item(1089), new Item(1195)),
        MITHRIL_ARMOUR_LG(13000, new Item(1159), new Item(1121), new Item(1071), new Item(1197)),
        MITHRIL_ARMOUR_SK(13002, new Item(1159), new Item(1121), new Item(1085), new Item(1197)),
        ADAMANT_ARMOUR_LG(13012, new Item(1161), new Item(1123), new Item(1073), new Item(1199)),
        ADAMANT_ARMOUR_SK(13014, new Item(1161), new Item(1123), new Item(1091), new Item(1199)),
        RUNE_ARMOUR_LG(13024, new Item(1163), new Item(1127), new Item(1079), new Item(1201)),
        RUNE_ARMOUR_SK(13026, new Item(1163), new Item(1127), new Item(1093), new Item(1201)),
        PROSELYTE_M(9666, new Item(9672), new Item(9674), new Item(9676)),
        PROSELYTE_F(9670, new Item(9672), new Item(9674), new Item(9678)),
        GREEN_DHIDE(12865, new Item(1065), new Item(1099), new Item(1135)),
        BLUE_DHIDE(12867, new Item(2487), new Item(2493), new Item(2499)),
        RED_DHIDE(12869, new Item(2489), new Item(2495), new Item(2501)),
        BLACK_DHIDE(12871, new Item(2491), new Item(2497), new Item(2503)),

        /* Trimmed Armour sets */
        BRONZE_TRIMMED_LG(12964, new Item(12215), new Item(12217), new Item(12221), new Item(12223)),
        BRONZE_TRIMMED_SK(12966, new Item(12215), new Item(12219), new Item(12221), new Item(12223)),
        BRONZE_GOLD_TRIMMED_LG(12968, new Item(12205), new Item(12207), new Item(12211), new Item(12213)),
        BRONZE_GOLD_TRIMMED_SK(12970, new Item(12205), new Item(12209), new Item(12211), new Item(12213)),
        IRON_TRIMMED_LG(12976, new Item(12225), new Item(12227), new Item(12231), new Item(12233)),
        IRON_TRIMMED_SK(12978, new Item(12225), new Item(12229), new Item(12231), new Item(12233)),
        IRON_GOLD_TRIMMED_LG(12980, new Item(12235), new Item(12237), new Item(12241), new Item(12243)),
        IRON_GOLD_TRIMMED_SK(12982, new Item(12235), new Item(12239), new Item(12241), new Item(12243)),
        BLACK_TRIMMED_LG(12992, new Item(2583), new Item(2585), new Item(2587), new Item(2589)),
        BLACK_TRIMMED_SK(12994, new Item(2583), new Item(3472), new Item(2587), new Item(2589)),
        BLACK_GOLD_TRIMMED_LG(12996, new Item(2591), new Item(2593), new Item(2595), new Item(2597)),
        BLACK_GOLD_TRIMMED_SK(12998, new Item(2591), new Item(3473), new Item(2595), new Item(2597)),
        MITRHIL_TRIMMED_LG(13004, new Item(12287), new Item(12289), new Item(12291), new Item(12293)),
        MITRHIL_TRIMMED_SK(13006, new Item(12287), new Item(12295), new Item(12291), new Item(12293)),
        MITRHIL_GOLD_TRIMMED_LG(13008, new Item(12277), new Item(12279), new Item(12281), new Item(12283)),
        MITRHIL_GOLD_TRIMMED_SK(13010, new Item(12277), new Item(12285), new Item(12281), new Item(12283)),
        ADAMANT_TRIMMED_LG(13016, new Item(2259), new Item(2601), new Item(2603), new Item(2605)),
        ADAMANT_TRIMMED_SK(13018, new Item(2259), new Item(3474), new Item(2603), new Item(2605)),
        ADAMANT_GOLD_TRIMMED_LG(13020, new Item(2607), new Item(2609), new Item(2611), new Item(2613)),
        ADAMANT_GOLD_TRIMMED_SK(13022, new Item(2607), new Item(3475), new Item(2611), new Item(2613)),
        RUNE_TRIMMED_LG(13028, new Item(2623), new Item(2625), new Item(2627), new Item(2629)),
        RUNE_TRIMMED_SK(13030, new Item(2623), new Item(3477), new Item(2627), new Item(2629)),
        RUNE_GOLD_TRIMMED_LG(13032, new Item(2615), new Item(2617), new Item(2619), new Item(2621)),
        RUNE_GOLD_TRIMMED_SK(13034, new Item(2615), new Item(3476), new Item(2619), new Item(2621)),
        GILDED_ARMOUR_LG(13036, new Item(3481), new Item(3483), new Item(3486), new Item(3488)),
        GILDED_ARMOUR_SK(13038, new Item(3481), new Item(3485), new Item(3486), new Item(3488)),

        /* Packs */
        EMPTY_VIAL_PACK(11877, new Item(230, 100)),
        SUPER_COMBAT_POTION_PACK(13066, new Item(2436), new Item(2440), new Item(2442)),
        COMBAT_POTION_PACK(13064, new Item(2428), new Item(113), new Item(2432)),
        WATER_FILLED_VIAL_PACK(11879, new Item(228, 100)),
        FEATHER_PACK(11881, new Item(314, 100)),
        BAIT_PACK(11883, new Item(313, 100)),
        SOFT_CLAY_PACK(12009, new Item(1762, 100)),
        BROAD_ARROWHEAD_PACK(11885, new Item(11874, 100)),
        AIR_RUNE_PACK(12728, new Item(556, 100)),
        WATER_RUNE_PACK(12730, new Item(555, 100)),
        EARTH_RUNE_PACK(12732, new Item(557, 100)),
        FIRE_RUNE_PACK(12734, new Item(554, 100)),
        MIND_RUNE_PACK(12736, new Item(558, 100)),
        CHAOS_RUNE_PACK(12738, new Item(562, 100)),
        BIRD_SNARE_PACK(12740, new Item(10007, 100)),
        BOX_SNARE_PACK(12742, new Item(10009, 100)),
        MAGIC_BOX_PACK(12744, new Item(10026, 100)),
        EYE_OF_NEW_PACK(12859, new Item(222, 100));

        public final int itemID;
        public final Item items[];
        PackData(int itemID, Item... items) {
            this.itemID = itemID;
            this.items = items;
        }

        public static PackData forItem(Item item) {
            for (PackData pack : values()) {
                if (pack.itemID == item.getId())
                    return pack;
            }
            return null;
        }
    }

    @Override
    protected boolean firstClickItem(Player player, ItemClickEvent event) {
        PackData pack = PackData.forItem(event.getItem());

        if (pack == null) {
            return false;
        }

        if (!player.inventory.hasCapacityFor(pack.items)) {
            player.message("You need at least " + pack.items.length + " available inventory spaces to do this!");
            return true;
        }

        player.inventory.remove(pack.itemID);
        player.inventory.addAll(pack.items);
        player.send(new SendMessage("You successfully open the " + event.getItem().getName() + ".", true));
        return true;
    }
}
