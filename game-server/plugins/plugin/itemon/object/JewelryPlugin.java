package plugin.itemon.object;


import com.osroyale.content.skill.impl.crafting.impl.Jewellery;
import com.osroyale.game.event.impl.ItemOnObjectEvent;
import com.osroyale.game.plugin.PluginContext;
import com.osroyale.game.world.entity.mob.player.Player;
import com.osroyale.game.world.items.Item;
import com.osroyale.game.world.object.GameObjectDefinition;

public class JewelryPlugin extends PluginContext {


    @Override
    protected boolean itemOnObject(Player player, ItemOnObjectEvent event) {
        final GameObjectDefinition def = event.getObject().getDefinition();
        if (def.getName() != null && !def.getName().toLowerCase().contains("furnace")) {
            return false;
        }
        final Item usedItem = event.getUsed();
            if (usedItem.getId() == 2357) {
                Jewellery.open(player);
                return true;
            }
        return false;
    }
}

