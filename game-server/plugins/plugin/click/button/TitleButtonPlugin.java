package plugin.click.button;

import com.osroyale.content.tittle.TitleManager;
import com.osroyale.game.plugin.PluginContext;
import com.osroyale.game.world.entity.mob.player.Player;

public class TitleButtonPlugin extends PluginContext {

    @Override
    protected boolean onClick(Player player, int button) {
        if (button >= -26485 && button <= -26420) {
            TitleManager.click(player, button);
            return true;
        }
        switch (button) {
            case -26525:
                TitleManager.redeem(player);
                break;
            case -26528:
                TitleManager.reset(player);
                break;
        }
        return false;
    }
}
