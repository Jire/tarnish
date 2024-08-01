package plugin.click.object;

import com.osroyale.content.bot.BotUtility;
import com.osroyale.game.event.impl.ObjectClickEvent;
import com.osroyale.game.plugin.PluginContext;
import com.osroyale.game.world.entity.mob.player.Player;
import com.osroyale.game.world.items.Item;
import com.osroyale.net.packet.out.SendItemOnInterfaceSlot;
import com.osroyale.net.packet.out.SendString;
import com.osroyale.util.MutableNumber;
import com.osroyale.util.Utility;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class BotLootViewerPlugin extends PluginContext {

    @Override
    protected boolean firstClickObject(Player player, ObjectClickEvent event) {
        if (event.getObject().getId() != 24099)
            return false;

        long value = 0;

        List<Item> items = new LinkedList<>();

        for (Map.Entry<Integer, MutableNumber> entry : BotUtility.BOOT_LOOT.entrySet()) {
            int id = entry.getKey();
            int amount = entry.getValue().get();

            Item item = new Item(id, amount);
            items.add(item);
            value += item.getValue() * item.getAmount();
        }


        items.sort((first, second) -> second.getValue() - first.getValue());

        int index = 0;
        for (Item item : items) {
            player.send(new SendItemOnInterfaceSlot(37560, item, index++));
        }

        player.send(new SendString("Total value: " + Utility.formatPrice(value), 37553));
        player.send(new SendString("Total items: " + Utility.formatDigits(index), 37554));
        player.interfaceManager.open(37550);
        return true;
    }

}
