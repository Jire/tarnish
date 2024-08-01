package plugin.click.item;

import com.osroyale.game.event.impl.ItemClickEvent;
import com.osroyale.game.plugin.PluginContext;
import com.osroyale.game.world.entity.mob.player.Player;
import com.osroyale.net.packet.out.SendMessage;

public class ItemSecondClickPlugin extends PluginContext {

    @Override
    protected boolean secondClickItem(Player player, ItemClickEvent event) {
        switch (event.getItem().getId()) {
            case 80:
                player.message("Your whip currently has "+player.whipCharges+ " charges remaining.");
                break;
            case 81:
                player.message("Your godsword currently has "+player.agsCharges+ " charges remaining.");
            default:
                return false;
        }
        return true;
    }
}
