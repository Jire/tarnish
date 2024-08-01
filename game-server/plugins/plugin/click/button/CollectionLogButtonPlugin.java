package plugin.click.button;

import com.osroyale.content.collectionlog.CollectionLog;
import com.osroyale.game.plugin.PluginContext;
import com.osroyale.game.world.entity.mob.player.Player;

public class CollectionLogButtonPlugin extends PluginContext {
    @Override
    protected boolean onClick(Player player, int button) {
        if(CollectionLog.clickButton(player, button))
            return true;

        switch (button) {

        }
        return false;
    }
}
