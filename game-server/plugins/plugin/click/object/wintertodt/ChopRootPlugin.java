package plugin.click.object.wintertodt;

import com.osroyale.game.event.impl.ObjectClickEvent;
import com.osroyale.game.plugin.PluginContext;
import com.osroyale.game.world.entity.mob.player.Player;
import com.osroyale.game.world.object.GameObject;
import com.osroyale.content.wintertodt.Wintertodt;

public class ChopRootPlugin extends PluginContext {

    @Override
    protected boolean firstClickObject(Player player, ObjectClickEvent event) {
        final GameObject gameObject = event.getObject();
        final int objectId = gameObject.getId();

        if(objectId != 29311) return false;

        Wintertodt.chopRoot(player);

        return true;
    }

}