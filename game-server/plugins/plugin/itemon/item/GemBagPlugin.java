package plugin.itemon.item;

import com.osroyale.game.event.impl.ItemClickEvent;
import com.osroyale.game.event.impl.PickupItemEvent;
import com.osroyale.game.plugin.PluginContext;
import com.osroyale.game.world.entity.mob.player.Player;
import com.osroyale.game.world.items.Item;
import com.osroyale.game.world.items.ItemDefinition;

public class GemBagPlugin extends PluginContext {
    @Override
    protected boolean onPickupItem(Player player, PickupItemEvent event) {
        final Item pickup = event.getItem();
        final ItemDefinition def = ItemDefinition.get(pickup.getId());

        if((!def.getName().startsWith("Uncut") && !def.isStackable()) || !player.inventory.contains(24481) || !player.gemBag.container.hasCapacityFor(pickup)) {
            return false;
        }

        player.gemBag.container.add(pickup);
        player.inventory.remove(pickup);
        return true;
    }

    @Override
    protected boolean firstClickItem(Player player, ItemClickEvent event) {
        switch(event.getItem().getId()) {
            case 12020: //Gem bag
            case 24481: //Open gem bag
                player.gemBag.fill(player);
                return true;
        }
        return false;
    }

    @Override
    protected boolean secondClickItem(Player player, ItemClickEvent event) {
        switch(event.getItem().getId()) {
            case 12020: //Gem bag
            case 24481: //Open gem bag
                player.gemBag.check(player);
                return true;
        }
        return false;
    }

    @Override
    protected boolean thirdClickItem(Player player, ItemClickEvent event) {
        switch(event.getItem().getId()) {
            case 12020: //Gem bag
            case 24481: //Open gem bag
                player.gemBag.empty(player);
                return true;
        }
        return false;
    }

    @Override
    protected boolean fourthClickItem(Player player, ItemClickEvent event) {
        switch(event.getItem().getId()) {
            case 12020: //Gem bag
                player.inventory.set(event.getSlot(), new Item(24481), true);
                return true;
            case 24481: //Open gem bag
                player.inventory.set(event.getSlot(), new Item(12020), true);
                return true;
        }
        return false;
    }

}
