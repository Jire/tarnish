package plugin.click.item;

import com.osroyale.content.skill.impl.magic.teleport.Teleportation;
import com.osroyale.content.skill.impl.magic.teleport.TeleportationData;
import com.osroyale.content.teleport.TeleportTablet;
import com.osroyale.game.event.impl.ItemClickEvent;
import com.osroyale.game.plugin.PluginContext;
import com.osroyale.game.world.entity.mob.player.Player;
import com.osroyale.game.world.items.Item;
import com.osroyale.net.packet.out.SendMessage;

public class TeletabPlugin extends PluginContext {

    @Override
    protected boolean firstClickItem(Player player, ItemClickEvent event) {
        if (!TeleportTablet.forId(event.getItem().getId()).isPresent()) {
            return false;
        }

        final TeleportTablet tablet = TeleportTablet.forId(event.getItem().getId()).get();

        if (player.house.isInside()) {
            player.send(new SendMessage("Please leave the house before teleporting."));
            return true;
        }

        player.inventory.remove(new Item(event.getItem().getId(), 1));
        Teleportation.teleport(player, tablet.getPosition(), 20, TeleportationData.TABLET);
        return true;
    }
}
