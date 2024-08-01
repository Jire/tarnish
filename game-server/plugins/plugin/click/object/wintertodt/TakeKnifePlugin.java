package plugin.click.object.wintertodt;

import com.osroyale.game.event.impl.ObjectClickEvent;
import com.osroyale.game.plugin.PluginContext;
import com.osroyale.game.world.entity.mob.player.Player;
import com.osroyale.game.world.items.Item;
import com.osroyale.game.world.object.GameObject;
public class TakeKnifePlugin extends PluginContext {

    @Override
    protected boolean firstClickObject(Player player, ObjectClickEvent event) {
        final GameObject gameObject = event.getObject();
        final int objectId = gameObject.getId();

        if(objectId != 29317) return false;

        if(player.inventory.contains(946)) {
            player.message("You already have a knife.");
            return true;
        }

        if(!player.inventory.hasCapacityFor(new Item(946))) {
            player.message("You need space in your inventory to take a knife.");
            return true;
        }

        player.message("You take a knife from the crate.");
        player.inventory.add(946, 1);
        player.inventory.refresh();

        return true;
    }

}