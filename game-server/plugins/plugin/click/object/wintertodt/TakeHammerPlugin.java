package plugin.click.object.wintertodt;

import com.osroyale.game.event.impl.ObjectClickEvent;
import com.osroyale.game.plugin.PluginContext;
import com.osroyale.game.world.entity.mob.player.Player;
import com.osroyale.game.world.items.Item;
import com.osroyale.game.world.object.GameObject;

public class TakeHammerPlugin extends PluginContext {

    @Override
    protected boolean firstClickObject(Player player, ObjectClickEvent event) {
        final GameObject gameObject = event.getObject();
        final int objectId = gameObject.getId();

        if(objectId != 29316) return false;

        if(player.inventory.contains(2347)) {
            player.message("You already have a hammer.");
            return true;
        }

        if(!player.inventory.hasCapacityFor(new Item(2347))) {
            player.message("You need space in your inventory to take a hammer.");
            return true;
        }

        player.message("You take a hammer from the crate.");
        player.inventory.add(2347, 1);
        player.inventory.refresh();

        return true;
    }

}