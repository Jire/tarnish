package plugin.click.item;

import com.osroyale.game.event.impl.ItemClickEvent;
import com.osroyale.game.plugin.PluginContext;
import com.osroyale.game.world.entity.mob.player.Player;
import com.osroyale.game.world.items.Item;
import com.osroyale.util.Utility;
import com.osroyale.util.chance.Chance;
import com.osroyale.util.chance.WeightedChance;

import java.util.Arrays;

/**
 * Created by Daniel on 2018-01-09.
 */
public class HerbloreBox extends PluginContext {

    private static final Chance<Item> ITEMS = new Chance<>(Arrays.asList(
            new WeightedChance<>(10, new Item(200)), // guam
            new WeightedChance<>(10, new Item(202)), // marrentill
            new WeightedChance<>(8, new Item(204)), // tarromin
            new WeightedChance<>(8, new Item(206)), // harralander
            new WeightedChance<>(3, new Item(208)), // ranaar
            new WeightedChance<>(4, new Item(210)), // irit
            new WeightedChance<>(4, new Item(212)), // avantoe
            new WeightedChance<>(4, new Item(214)), // kwuarm
            new WeightedChance<>(4, new Item(216)), // cadantine
            new WeightedChance<>(3, new Item(2486)), // lantadyme
            new WeightedChance<>(4, new Item(218)) // dwarf
    ));

    @Override
    protected boolean firstClickItem(Player player, ItemClickEvent event) {
        Item item = event.getItem();

        if (item.getId() != 11738) {
            return false;
        }

        if (player.inventory.getFreeSlots() < 5) {
            player.message("Clear up some inventory spaces before doing this!");
            return true;
        }

        int length = Utility.random(10,15) + 15;

        player.inventory.remove(11738, 1);

        for (int index = 0; index < length; index++) {
            player.inventory.add(ITEMS.next());
        }

        player.message("You have opened the herblore box.");
        return true;
    }
}
