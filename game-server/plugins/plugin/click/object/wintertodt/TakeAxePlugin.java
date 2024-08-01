package plugin.click.object.wintertodt;

import com.osroyale.content.skill.impl.woodcutting.AxeData;
import com.osroyale.game.event.impl.ObjectClickEvent;
import com.osroyale.game.plugin.PluginContext;
import com.osroyale.game.world.entity.mob.player.Player;
import com.osroyale.game.world.items.Item;
import com.osroyale.game.world.object.GameObject;
public class TakeAxePlugin extends PluginContext {

    @Override
    protected boolean firstClickObject(Player player, ObjectClickEvent event) {
        final GameObject gameObject = event.getObject();
        final int objectId = gameObject.getId();

        if(objectId != 29318) return false;

        if(AxeData.getDefinition(player).orElse(null) != null) {
            player.message("You already have a axe.");
            return true;
        }

        if(!player.inventory.hasCapacityFor(new Item(1351))) {
            player.message("You need space in your inventory to take a axe.");
            return true;
        }

        player.message("You take a axe from the crate.");
        player.inventory.add(1351, 1);
        player.inventory.refresh();

        return true;
    }

}