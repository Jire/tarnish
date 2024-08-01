package plugin.click.object;

import com.osroyale.game.event.impl.ObjectClickEvent;
import com.osroyale.game.plugin.PluginContext;
import com.osroyale.game.world.entity.mob.player.Player;

public class ObjectThirdClickPlugin extends PluginContext {

    @Override
    protected boolean thirdClickObject(Player player, ObjectClickEvent event) {
        final int id = event.getObject().getId();

        switch (id) {

        }

        return false;
    }

}
