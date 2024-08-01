package plugin.itemon.player;

import com.osroyale.game.event.impl.ItemOnPlayerEvent;
import com.osroyale.game.plugin.PluginContext;
import com.osroyale.game.world.entity.mob.player.Player;

public class ItemOnPlayerPlugin extends PluginContext {

    @Override
    protected boolean itemOnPlayer(Player player, ItemOnPlayerEvent event) {
        return false;
    }

}
