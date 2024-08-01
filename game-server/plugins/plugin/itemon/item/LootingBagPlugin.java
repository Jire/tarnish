package plugin.itemon.item;

import com.osroyale.game.event.impl.ItemClickEvent;
import com.osroyale.game.event.impl.ItemOnItemEvent;
import com.osroyale.game.event.impl.ItemOnObjectEvent;
import com.osroyale.game.event.impl.PickupItemEvent;
import com.osroyale.game.plugin.PluginContext;
import com.osroyale.game.world.entity.mob.player.Player;
import com.osroyale.game.world.entity.mob.player.PlayerRight;
import com.osroyale.game.world.items.Item;
import com.osroyale.game.world.position.Area;
import com.osroyale.net.packet.out.SendMessage;

public class LootingBagPlugin extends PluginContext {

    public static final int OPENED_ID = 22586;
    public static final int CLOSED_ID = 11941;

    public static boolean isLootingBag(Item item) {
        return item.getId() == OPENED_ID || item.getId() == CLOSED_ID;
    }

    @Override
    protected boolean itemOnObject(Player player, ItemOnObjectEvent event) {
        if (!isLootingBag(event.getUsed())) {
            return false;
        }

        if (!event.getObject().getDefinition().getName().equalsIgnoreCase("bank booth")) {
            return false;
        }

        if (player.right != PlayerRight.ULTIMATE_IRONMAN) {
            return false;
        }

        if (player.lootingBag.isEmpty()) {
            player.dialogueFactory.sendStatement("Your looting bag is empty!").execute();
            return true;
        }

        int inventory = player.inventory.getFreeSlots();
        int size = player.lootingBag.toNonNullArray().length;
        for (int i = 0; i < size; i++) {
            if (i >= inventory) {
                player.message("You do not have enough space to withdraw all your items!");
                break;
            }

            Item item = player.lootingBag.get(i);
            player.lootingBag.remove(item);
            player.inventory.add(item);
        }

        player.lootingBag.shift();
        return true;
    }

    @Override
    protected boolean itemOnItem(Player player, ItemOnItemEvent event) {
        final Item used = event.getUsed();
        final Item with = event.getWith();
        if (!isLootingBag(used) && !isLootingBag(with)) {
            return false;
        }

        if (isLootingBag(used)) {
            if (with.getAmount() == 1) {
                player.lootingBag.deposit(with, with.getAmount());
                return true;
            }
            player.lootingBag.depositMenu(with);
            return true;
        }

        if (used.getAmount() == 1) {
            player.lootingBag.deposit(used, used.getAmount());
            return true;
        }
        player.lootingBag.depositMenu(used);
        return true;
    }

    @Override
    protected boolean firstClickItem(Player player, ItemClickEvent event) {
        final Item item = event.getItem();
        if (!isLootingBag(item)) {
            return false;
        }

        boolean open = (item.getId() == CLOSED_ID);
        player.inventory.set(event.getSlot(), new Item((open ? OPENED_ID : CLOSED_ID)), true);
        player.send(new SendMessage(open ? "You open your looting bag, ready to fill it." : "You close your looting bag.", true));
        return true;
    }

    @Override
    protected boolean secondClickItem(Player player, ItemClickEvent event) {
        final Item item = event.getItem();
        if (!isLootingBag(item)) {
            return false;
        }

        player.lootingBag.open();
        return true;
    }

    @Override
    protected boolean onPickupItem(Player player, PickupItemEvent event) {
        if (player.inventory.contains(OPENED_ID) && Area.inWilderness(player)) {
            final Item pickup = event.getItem();
            player.lootingBag.deposit(pickup, pickup.getAmount());
            return true;
        }
        return false;
    }
}
