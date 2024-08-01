package plugin.click.item;

import com.osroyale.content.collectionlog.CollectionLog;
import com.osroyale.content.collectionlog.CollectionLogData;
import com.osroyale.content.pet.Pets;
import com.osroyale.game.event.impl.ItemClickEvent;
import com.osroyale.game.plugin.PluginContext;
import com.osroyale.game.world.entity.mob.player.Player;
import com.osroyale.game.world.items.Item;
import com.osroyale.game.world.items.ItemDefinition;
import com.osroyale.util.Items;
import com.osroyale.util.Utility;

public class WintertodtSupplyCrate extends PluginContext {

    @Override
    protected boolean firstClickItem(Player player, ItemClickEvent event) {
        Item item = event.getItem();

        if (item.getId() != 20703) {
            return false;
        }

        if (player.inventory.getFreeSlots() < 5) {
            player.message("You need at least 5 free inventory spaces to open this crate.");
            return true;
        }

        player.inventory.remove(20703, 1);

        handleRewards(player);
        return true;
    }

    private static void handleRewards(Player player) {
        Pets.onReward(player, 20693, 1000);
        player.message("You open the supply crate and find:");
        var rarity = Utility.hasOneOutOf(50) ? "UNCOMMON" : "COMMON";
        switch (rarity) {
            case "COMMON" -> {
                var timesToRoll = Utility.random(3, 5, true);
                for (int index = 0; index < timesToRoll; index++) {
                    //there is a 1/9 chance to hit specific table, rest roll on 'other' table
                    var possibleLootTable = new int[][][]{LOGS, GEMS, ORES, HERBS, SEEDS, FISH, OTHER, OTHER, OTHER};
                    var selectRandomTable = Utility.random(possibleLootTable.length);
                    var itemFromTable = Utility.random(possibleLootTable[selectRandomTable].length);

                    var finalSelection = possibleLootTable[selectRandomTable][itemFromTable];
                    var amountToGive = Utility.random(finalSelection[1], finalSelection[2], true);

                    player.inventory.add(finalSelection[0], amountToGive);
                    player.message("@red@" + amountToGive + " x " + ItemDefinition.get(finalSelection[0]).getName());
                    CollectionLog.logItem(player, CollectionLogData.WINTERTODT, finalSelection[0], amountToGive);
                }
            }
            case "UNCOMMON" -> {
                var randomUncommon = Utility.random(UNCOMMON.length);
                var uncommonItem = UNCOMMON[randomUncommon];
                player.inventory.add(uncommonItem[0], uncommonItem[1]);
                player.message("@red@" + uncommonItem[1] + " x " + ItemDefinition.get(uncommonItem[0]).getName());
                CollectionLog.logItem(player, CollectionLogData.WINTERTODT, uncommonItem[0], uncommonItem[1]);
            }
        }
    }

    //these always only give 1 of the item
    private static final int[][] UNCOMMON = {
            {Items.WARM_GLOVES, 1},
            {Items.BRUMA_TORCH, 1},
            {Items.PYROMANCER_HOOD, 1},
            {Items.PYROMANCER_GARB, 1},
            {Items.PYROMANCER_ROBE, 1},
            {Items.PYROMANCER_BOOTS, 1},
            {Items.TOME_OF_FIRE_EMPTY_, 1},
            {Items.DRAGON_AXE, 1}
    };

    //{ItemID, minAmount, maxAmount}
    private static final int[][] LOGS = {
            {Items.OAK_LOGS_2, 10, 20},
            {Items.WILLOW_LOGS_2, 10, 20},
            {Items.TEAK_LOGS_2, 10, 20},
            {Items.MAPLE_LOGS_2, 10, 20},
            {Items.MAHOGANY_LOGS_2, 10, 20},
            {Items.YEW_LOGS_2, 10, 20},
            {Items.MAGIC_LOGS_2, 10, 20},
    };

    private static final int[][] GEMS = {
            {Items.UNCUT_SAPPHIRE_2, 1, 3},
            {Items.UNCUT_EMERALD_2, 1, 3},
            {Items.UNCUT_RUBY_2, 2, 4},
            {Items.UNCUT_DIAMOND_2, 1, 3},
    };

    private static final int[][] ORES = {
            {Items.LIMESTONE_2, 3, 7},
            {Items.SILVER_ORE_2, 10, 12},
            {Items.IRON_ORE_2, 5, 15},
            {Items.COAL_2, 10, 14},
            {Items.GOLD_ORE_2, 8, 11},
            {Items.MITHRIL_ORE_2, 3, 5},
            {Items.ADAMANTITE_ORE_2, 2, 3},
            {Items.RUNITE_ORE_2, 1, 2},
    };

    private static final int[][] HERBS = {
            {Items.GUAM_LEAF_2, 3, 6},
            {Items.MARRENTILL_2, 3, 6},
            {Items.TARROMIN_2, 3, 6},
            {Items.HARRALANDER_2, 3, 6},
            {Items.RANARR_WEED_2, 1, 3},
            {Items.IRIT_LEAF_2, 3, 5},
            {Items.AVANTOE_2, 3, 5},
            {Items.KWUARM_2, 2, 4},
            {Items.CADANTINE_2, 2, 4},
            {Items.LANTADYME_2, 2, 4},
            {Items.DWARF_WEED_2, 2, 4},
            {Items.TORSTOL_2, 1, 3},
    };

    private static final int[][] SEEDS = {
            {Items.ACORN, 1},
            {Items.WILLOW_SEED, 1, 3},
            {Items.MAPLE_SEED, 1, 2},
            {Items.BANANA_TREE_SEED, 1, 2},
            {21486, 1, 2},
            {21488, 1, 2},
            {Items.YEW_SEED, 1, 2},
            {Items.MAGIC_SEED, 1, 3},
            {Items.RANARR_SEED, 1, 3},
            {Items.TARROMIN_SEED, 1, 3},
            {Items.HARRALANDER_SEED, 1, 3},
            {Items.TOADFLAX_SEED, 1, 3},
            {Items.IRIT_SEED, 1, 3},
            {Items.AVANTOE_SEED, 1, 3},
            {Items.KWUARM_SEED, 1, 3},
            {Items.SNAPDRAGON_SEED, 1, 3},
            {Items.CADANTINE_SEED, 1, 3},
            {Items.LANTADYME_SEED, 1, 3},
            {Items.DWARF_WEED_SEED, 1, 3},
            {Items.TORSTOL_SEED, 1, 3},
            {Items.WATERMELON_SEED, 3, 7},
            {22879, 3, 7},
            {Items.SPIRIT_SEED, 1, 1},
    };

    private static final int[][] FISH = {
            {Items.RAW_ANCHOVIES_2, 6, 11},
            {Items.RAW_TROUT_2, 6, 11},
            {Items.RAW_SALMON_2, 6, 11},
            {Items.RAW_TUNA_2, 6, 11},
            {Items.RAW_LOBSTER_2, 6, 11},
            {Items.RAW_SWORDFISH_2, 6, 11},
            {Items.RAW_SHARK_2, 6, 11},
    };

    private static final int[][] OTHER = {
            {Items.COINS, 2000, 5000},
            {Items.SALTPETRE_2, 3, 5},
            {Items.DYNAMITE_2, 3, 5},
            {Items.BURNT_PAGE, 7, 29},
            {Items.PURE_ESSENCE_2, 20, 70},
    };

}
