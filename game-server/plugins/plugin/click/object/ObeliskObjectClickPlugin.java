package plugin.click.object;

import com.osroyale.content.Obelisks;
import com.osroyale.game.event.impl.ObjectClickEvent;
import com.osroyale.game.plugin.PluginContext;
import com.osroyale.game.world.entity.mob.player.Player;

public class ObeliskObjectClickPlugin extends PluginContext {

    @Override
    protected boolean firstClickObject(Player player, ObjectClickEvent event) {
        return Obelisks.get().activate(player, event.getObject().getId());
    }

}
