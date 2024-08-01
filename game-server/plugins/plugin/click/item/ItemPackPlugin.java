package plugin.click.item;

import com.osroyale.content.ItemPack;
import com.osroyale.game.event.impl.ItemClickEvent;
import com.osroyale.game.event.impl.ItemOnItemEvent;
import com.osroyale.game.plugin.PluginContext;
import com.osroyale.game.world.entity.mob.player.Player;
import com.osroyale.game.world.items.Item;
import com.osroyale.net.packet.out.SendMessage;
import com.osroyale.util.Utility;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

public class ItemPackPlugin extends PluginContext {

    @Override
    protected boolean itemOnItem(Player player, ItemOnItemEvent event) {
        if (!event.getUsed().matchesId(1755) && !event.getWith().matchesId(1755)) {
            return false;
        }

        Optional<ItemPack> pack = ItemPack.forItem(event.getUsed().getId());

        if (!pack.isPresent()) {
            pack = ItemPack.forItem(event.getWith().getId());
        }

        if (!pack.isPresent()) {
            return false;
        }

        ItemPack data = pack.get();

        if (!player.inventory.hasCapacityFor(data.items)) {
            player.send(new SendMessage("You need at least " + data.items.length + " available inventory spaces to do this!"));
            return true;
        }

        if (!player.inventory.contains(data.armour)) {
            player.send(new SendMessage("You actually need the pack to open it lol"));
            return true;
        }

        if (data.chisel && !player.inventory.contains(1755)) {
            player.send(new SendMessage("You need a chisel to open this pack."));
            return true;
        }

        player.inventory.remove(new Item(data.armour));
        player.inventory.addAll(data.items);
        player.send(new SendMessage("You successfully open the " + Utility.formatEnum(data.name()) + ".", true));
        return true;
    }

    @Override
    protected boolean firstClickItem(Player player, ItemClickEvent event) {
        Optional<ItemPack> pack = ItemPack.forItem(event.getItem().getId());

        if (!pack.isPresent()) {
            return false;
        }

        if (!player.itemDelay.elapsed(1, TimeUnit.SECONDS)) {
            return true;
        }

        ItemPack data = pack.get();

        if (!player.inventory.hasCapacityFor(data.items)) {
            player.send(new SendMessage("You need at least " + data.items.length + " available inventory spaces to do this!"));
            return true;
        }

        if (!player.inventory.contains(data.armour)) {
            player.send(new SendMessage("You actually need the pack to open it lol"));
            return true;
        }

        if (data.chisel && !player.inventory.contains(1755)) {
            player.send(new SendMessage("You need a chisel to open this pack."));
            return true;
        }

        player.inventory.remove(new Item(data.armour));
        player.inventory.addAll(data.items);
        player.send(new SendMessage("You successfully open the " + Utility.formatEnum(data.name()) + ".", true));
        player.itemDelay.reset();
        return true;
    }

}
