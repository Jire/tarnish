package plugin.click.object.wintertodt;

import com.osroyale.content.wintertodt.Wintertodt;
import com.osroyale.game.event.impl.ObjectClickEvent;
import com.osroyale.game.plugin.PluginContext;
import com.osroyale.game.world.entity.mob.player.Player;
import com.osroyale.game.world.object.GameObject;

public class FixBrazierPlugin extends PluginContext {

    @Override
    protected boolean firstClickObject(Player player, ObjectClickEvent event) {
        final GameObject gameObject = event.getObject();
        final int objectId = gameObject.getId();

        if(objectId != Wintertodt.BROKEN_BRAZIER_ID) return false;

        Wintertodt.fixBrazier(player, gameObject);

        return true;
    }

}