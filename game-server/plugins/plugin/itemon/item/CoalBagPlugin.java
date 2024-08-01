package plugin.itemon.item;

import com.osroyale.game.event.impl.ItemClickEvent;
import com.osroyale.game.event.impl.PickupItemEvent;
import com.osroyale.game.plugin.PluginContext;
import com.osroyale.game.world.entity.mob.player.Player;
import com.osroyale.game.world.items.Item;

public class CoalBagPlugin extends PluginContext {
    @Override
    protected boolean onPickupItem(Player player, PickupItemEvent event) {
        final Item pickup = event.getItem();

        if(player.inventory.contains(24480) && player.coalBag.container.hasCapacityFor(pickup) && pickup.getId() == 453) {
            player.coalBag.container.add(pickup);
            player.inventory.remove(pickup);
            return true;
        }

        return false;
    }



    @Override
    protected boolean firstClickItem(Player player, ItemClickEvent event) {
        switch(event.getItem().getId()) {
            case 12019: //Coal bag
            case 24480: //Open coal bag
                player.coalBag.fill(player);
                return true;
        }
        return false;
    }

    @Override
    protected boolean secondClickItem(Player player, ItemClickEvent event) {
        switch(event.getItem().getId()) {
            case 12019: //Coal bag
            case 24480: //Open coal bag
                player.coalBag.check(player);
                return true;
        }
        return false;
    }

    @Override
    protected boolean thirdClickItem(Player player, ItemClickEvent event) {
        switch(event.getItem().getId()) {
            case 12019: //Coal bag
            case 24480: //Open coal bag
                player.coalBag.empty(player);
                return true;
        }
        return false;
    }

    @Override
    protected boolean fourthClickItem(Player player, ItemClickEvent event) {
        switch(event.getItem().getId()) {
            case 12019: //Coal bag
                player.inventory.set(event.getSlot(), new Item(24480), true);
                return true;
            case 24480: //Open coal bag
                player.inventory.set(event.getSlot(), new Item(12019), true);
                return true;
        }
        return false;
    }

}