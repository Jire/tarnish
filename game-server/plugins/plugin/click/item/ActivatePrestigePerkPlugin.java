package plugin.click.item;

import com.osroyale.content.prestige.PrestigePerk;
import com.osroyale.game.event.impl.ItemClickEvent;
import com.osroyale.game.plugin.PluginContext;
import com.osroyale.game.world.entity.mob.player.Player;
import com.osroyale.net.packet.out.SendMessage;
import com.osroyale.util.MessageColor;

import java.util.HashSet;

public class ActivatePrestigePerkPlugin extends PluginContext {

    @Override
    protected boolean firstClickItem(Player player, ItemClickEvent event) {
        final PrestigePerk perk = PrestigePerk.forItem(event.getItem().getId());
        if (perk == null) {
            return false;
        }

        if (player.prestige.activePerks == null) {
            player.prestige.activePerks = new HashSet<>();
        }

        if (player.prestige.activePerks.contains(perk)) {
            player.send(new SendMessage("The Perk: " + perk.name + " perk is already active on your account!", MessageColor.DARK_BLUE));
            return true;
        }

        player.inventory.remove(event.getItem());
        player.prestige.activePerks.add(perk);
        player.send(new SendMessage("You have successfully activated the " + perk.name + " perk.", MessageColor.DARK_BLUE));
        return true;
    }
}
