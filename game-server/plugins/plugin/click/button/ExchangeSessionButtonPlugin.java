package plugin.click.button;

import com.osroyale.game.plugin.PluginContext;
import com.osroyale.game.world.entity.mob.player.Player;
import com.osroyale.game.world.entity.mob.player.exchange.ExchangeSession;

public class ExchangeSessionButtonPlugin extends PluginContext {

    @Override
    protected boolean onClick(Player player, int button) {
        ExchangeSession session = ExchangeSession.getSession(player).orElse(null);
        return session != null && session.onButtonClick(player, button);
    }
}
