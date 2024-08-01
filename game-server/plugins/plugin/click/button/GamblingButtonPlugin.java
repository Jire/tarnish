package plugin.click.button;

import com.osroyale.content.gambling.GambleType;
import com.osroyale.game.plugin.PluginContext;
import com.osroyale.game.world.entity.mob.player.Player;

public class GamblingButtonPlugin extends PluginContext {

    @Override
    protected boolean onClick(final Player player, final int button) {
        switch(button) {
            case -20784: //Flower poker
                player.getGambling().handleModeSelection(player, GambleType.FLOWER_POKER);
                return true;
            case -20781: //55x2
                player.getGambling().handleModeSelection(player, GambleType.FIFTY_FIVE);
                return true;
            case -20776: //Accept
                player.getGambling().accept(player);
                return true;
            case -20773: //Decline
                player.getGambling().decline(player);
                return true;
        }
        return false;
    }

}
