package com.osroyale.game.world.items.containers.impl;

import com.osroyale.Config;
import com.osroyale.game.world.InterfaceConstants;
import com.osroyale.game.world.entity.mob.player.Player;
import com.osroyale.game.world.items.Item;
import com.osroyale.game.world.items.containers.ItemContainer;
import com.osroyale.game.world.items.containers.ItemContainerAdapter;
import com.osroyale.game.world.items.containers.pricechecker.PriceType;
import com.osroyale.game.world.position.Area;
import com.osroyale.net.packet.out.*;
import com.osroyale.util.Utility;
import plugin.itemon.item.LootingBagPlugin;

/**
 * Handles the looting bag container.
 *
 * @author Daniel.
 */
public class LootingBag extends ItemContainer {

    /**
     * The player instance.
     */
    private final Player player;

    /**
     * Constructs a new <code>LootingBag</code>.
     */
    public LootingBag(Player player) {
        super(28, StackPolicy.STANDARD);
        this.addListener(new LootingBagListener(player));
        this.player = player;
    }

    public void open() {
        onRefresh();
        player.interfaceManager.setSidebar(Config.INVENTORY_TAB, 26700);
        player.send(new SendForceTab(Config.INVENTORY_TAB));
    }

    /**
     * Handles closing the looting bag.
     */
    public void close() {
        if (player.attributes.get("BANK_KEY", Boolean.class)) {
            player.interfaceManager.openInventory(60000, InterfaceConstants.INVENTORY_STORE - 1);
            return;
        }
        player.interfaceManager.setSidebar(Config.INVENTORY_TAB, 3213);
    }

    /**
     * Checks if the player is allowed to deposit items into the looting bag.
     */
    private boolean allowed(Item item) {
        if (!Area.inWilderness(player)) {
            player.send(new SendMessage("You can't put items in the bag unless you're in the Wilderness."));
            return false;
        }
        if (!item.isTradeable()) {
            player.send(new SendMessage("You can't deposit un-tradeable items into the looting bag."));
            return false;
        }
        if (!player.inventory.contains(item)) {
            player.send(new SendMessage("You can not deposit an item that you do not have!"));
            return false;
        }
        return true;
    }

    public Item[] getDeathItems() {
        if (!player.inventory.contains(LootingBagPlugin.OPENED_ID) && !player.inventory.contains(LootingBagPlugin.CLOSED_ID)) {
            return null;
        }
        return toNonNullArray();
    }

    /**
     * Handles opening the looting bag menu.
     */
    public void depositMenu(Item item) {
        player.dialogueFactory.sendOption(
                "Deposit 1", () -> deposit(item, 1),
                "Deposit 5", () -> deposit(item, 5),
                "Deposit All", () -> deposit(item, item.getAmount()),
                "Deposit X", () ->
                        player.dialogueFactory.onAction(() -> player.send(new SendInputMessage("Enter the amount you would like to deposit into the looting bag:", 12, input -> {
                            player.dialogueFactory.clear();
                            deposit(item, Integer.parseInt(input));
                        })))).execute();
    }

    /**
     * Handles the actual depositing of looting bag.
     */
    public boolean deposit(Item item, int amount) {
        if (!allowed(item)) {
            return false;
        }
        int contain = player.inventory.computeAmountForId(item.getId());
        if (contain < amount) {
            amount = contain;
        }
        Item current = new Item(item.getId(), amount);
        if (!add(current)) {
            return false;
        }
        player.inventory.remove(current);
        player.send(new SendMessage("You have deposited " + amount + " " + current.getName() + " into the looting bag."));
        onRefresh();
        return true;
    }

    /**
     * Handles the actual depositing of looting bag.
     */
    public void withdrawBank(Item item, int slot) {
        int amount = computeAmountForId(item.getId());
        if (item.getAmount() > amount) {
            item = item.createWithAmount(amount);
        }
        int removed = 0;
        if (item.isStackable()) {
            removed = player.bank.depositFromNothing(item, player.bank.bankTab);
        } else {
            while (removed < item.getAmount()) {
                int deposited = player.bank.depositFromNothing(item, player.bank.bankTab);
                if (deposited == 0) {
                    break;
                }
                removed += deposited;
            }
        }
        if (removed > 0) {
            remove(item.createWithAmount(removed), slot, false);
        }
        player.bank.refresh();
        onRefresh();
    }

    @Override
    public void onRefresh() {
        player.send(new SendItemOnInterface(26706, toArray()));
        player.send(new SendString("Value: " + Utility.formatDigits(containerValue(PriceType.VALUE)) + " coins", 26707));
    }

    /**
     * An {@link ItemContainerAdapter} implementation that listens for changes to the looting bag.
     */
    private static final class LootingBagListener extends ItemContainerAdapter {

        LootingBagListener(Player player) {
            super(player);
        }

        @Override
        public int getWidgetId() {
            return 26706;
        }

        @Override
        public String getCapacityExceededMsg() {
            return "You do not have enough space in your looting bag.";
        }
    }
}
