package plugin.click.item;

import com.osroyale.content.consume.PotionData;
import com.osroyale.game.event.impl.ItemClickEvent;
import com.osroyale.game.plugin.PluginContext;
import com.osroyale.game.world.entity.mob.player.Player;
import com.osroyale.net.packet.out.SendMessage;

public class EmptyPotionPlugin extends PluginContext {

    @Override
    protected boolean thirdClickItem(Player player, ItemClickEvent event) {
        if (PotionData.forId(event.getItem().getId()).isPresent()) {
            player.inventory.replace(event.getItem().getId(), 229, true);
            player.send(new SendMessage("You have poured out the remaining dose(s) of " + event.getItem().getName() + ".", true));
            return true;
        }
        return false;
    }

}
