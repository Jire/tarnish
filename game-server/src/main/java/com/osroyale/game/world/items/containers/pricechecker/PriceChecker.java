package com.osroyale.game.world.items.containers.pricechecker;

import com.osroyale.game.world.InterfaceConstants;
import com.osroyale.game.world.entity.mob.player.Player;
import com.osroyale.game.world.items.Item;
import com.osroyale.game.world.items.ItemDefinition;
import com.osroyale.game.world.items.containers.ItemContainer;
import com.osroyale.game.world.items.containers.ItemContainerAdapter;
import com.osroyale.net.packet.out.*;
import com.osroyale.util.Utility;

import java.text.NumberFormat;
import java.util.Optional;

/**
 * The price checker container.
 *
 * @author Daniel
 */
public class PriceChecker extends ItemContainer {

    /** Holds all the string identifications */
    private final int[] STRINGS = {48550, 48551, 48552, 48553, 48554, 48555, 48556, 48557, 48558, 48559, 48560, 48561, 48562, 48563, 48564, 48565, 48566, 48567, 48568, 48569, 48570, 48571, 48572, 48573, 48574, 48575, 48576, 48577,};

    /** The price checker display widget identifier. */
    private static final int PRICE_CHECKER_DISPLAY_ID = 48542;

    /** The player instance. */
    public Player player;

    /** The price type. */
    private PriceType priceType;

    /** The item being searched. */
    private Item searchedItem;

    /** Creates a new <code>PriceChecker<code>. */
    public PriceChecker(Player player) {
        super(28, StackPolicy.STANDARD);
        this.player = player;
        this.priceType = PriceType.VALUE;
        addListener(new PriceCheckerListener());
    }

    /** Opens the price checker interface. */
    public void open() {
        refresh();
        player.send(new SendString("", 48583));
        player.send(new SendString("\\nSelect item to search", 48582));
        player.send(new SendItemOnInterface(48581));
        player.attributes.set("PRICE_CHECKER_KEY", true);
        player.interfaceManager.openInventory(48500, 5063);
    }

    /** Closes the price checker interface. */
    public void close() {
        withdrawAll();
        searchedItem = null;
        player.attributes.set("PRICE_CHECKER_KEY", false);
    }

    /** Searches for an item. */
    public void search(String name) {
        ItemDefinition found = null;

        for (ItemDefinition definition : ItemDefinition.DEFINITIONS) {
            if (definition == null)
                continue;
            String searched = definition.getName().toLowerCase().replace("'", "");
            if (!searched.contains(name.toLowerCase()))
                continue;
            found = definition;
            break;
        }

        if (found == null) {
            player.dialogueFactory.sendStatement("There were no results found for", "<col=255>" + name).execute();
            return;
        }

        player.send(new SendString(Utility.formatDigits(found.getValue()), 48582));
        player.send(new SendString(found.getName(), 48583));
        player.send(new SendItemOnInterface(48581, new Item(found.getId())));
    }

    /** Sets the calculating value of the price checker. */
    public void setValue(PriceType type) {
        priceType = type;
        refresh();
    }

    /** Searches for an item. */
    public void searchItem(String name) {
        Item searched = null;

        for (ItemDefinition item : ItemDefinition.DEFINITIONS) {
            if (item != null && (item.getName().equalsIgnoreCase(name) && !item.isNoted())) {
                searched = new Item(item.getId(), 1);
                break;
            }
        }

        if (searched == null) {
            player.send(new SendMessage("There was no item found under the name of " + name + "."));
        } else {
            searchedItem = searched;
            player.send(new SendItemOnInterfaceSlot(48581, searchedItem, 0));
            player.send(new SendString(searchedItem == null ? "" : "<col=ffb000>" + searchedItem.getName() + ":", 48582));
            player.send(new SendString(searchedItem == null ? "" : Utility.formatDigits(searchedItem.getValue(priceType)), 48583));
        }
    }

    /** Deposits an item into the price checker. */
    public void deposit(int slot, int amount) {
        Item item = player.inventory.get(slot);
        if (item == null)
            return;

        if (!item.isTradeable()) {
            player.send(new SendMessage("This is item is untradeable!"));
            return;
        }

        int id = item.getId();

        int invAmount = player.inventory.computeAmountForId(id);

        if (invAmount < amount) {
            amount = invAmount;
        }

        setFiringEvents(false);
        add(id, amount);
        player.inventory.remove(item.getId(), amount);
        setFiringEvents(true);
        refresh();
    }

    /** Withdraws an item from the price checker. */
    public void withdraw(int itemId, int amount) {
        int slot = computeIndexForId(itemId);
        if (itemId < 0) return;

        Item item = get(slot);
        if (item == null || itemId != item.getId()) {
            return;
        }

        int contains = computeAmountForId(itemId);

        if (contains < amount) {
            amount = contains;
        }

        int id = item.getId();
        setFiringEvents(false);
        if (!new Item(id).isStackable() && amount > player.inventory.getFreeSlots()) {
            amount = player.inventory.getFreeSlots();
        }

        int fuckingSlot = player.inventory.computeIndexForId(id);
        if (fuckingSlot != -1) {
            Item fuckingStan = player.inventory.get(fuckingSlot);
            if (Integer.MAX_VALUE - fuckingStan.getAmount() < amount) {
                amount = Integer.MAX_VALUE - fuckingStan.getAmount();
                player.send(new SendMessage("Your inventory didn't have enough space to withdraw all that!"));
            }
        }


        if (remove(item.getId(), amount)) {
            player.inventory.add(id, amount);
            shift();
        }

        setFiringEvents(true);
        refresh();
    }

    /** Deposits all the items into the price checker. */
    public void depositAll() {
        Item[] items = player.inventory.toArray();
        for (int slot = 0; slot < items.length; slot++) {
            Item item = items[slot];
            if (item == null) {
                continue;
            }

            deposit(slot, item.getAmount());
        }
        refresh();
    }

    /** Withdraw all the items from the price checker. */
    public void withdrawAll() {
        for (Item item : getItems()) {
            if (item != null) {
                if (this.remove(item)) {
                    player.inventory.add(item, -1, false);
                }
            }
        }
        refresh();
    }

    public void refresh() {
        refresh(player, PRICE_CHECKER_DISPLAY_ID);
    }

    @Override
    public void onRefresh() {
        for (int index = 0; index < STRINGS.length; index++) {
            String value = "";

            if (getItems()[index] != null) {
                int price = getItems()[index].getValue(priceType);
                int amount = getItems()[index].getAmount();

                value = getItems()[index].isStackable() ? Utility.formatDigits(amount) + " x " + Utility.formatDigits(price) + "\\n" + "= " + Utility.formatDigits(price * amount) : Utility.formatDigits(price);
            }

            player.send(new SendString(value, STRINGS[index]));
        }

        player.inventory.refresh();
        player.send(new SendString("", 48582));
        player.send(new SendString("", 48583));
        player.send(new SendItemOnInterfaceSlot(48581, searchedItem, 0));
        player.send(new SendConfig(237, priceType == PriceType.VALUE ? 1 : 0));
        player.send(new SendItemOnInterface(InterfaceConstants.INVENTORY_STORE, player.inventory.toArray()));
        player.send(new SendString(searchedItem == null ? "" : "<col=ffb000>" + searchedItem.getName() + ":", 48582));
        player.send(new SendString(searchedItem == null ? "" : Utility.formatDigits(searchedItem.getValue(priceType)), 48583));
        player.send(new SendString("" + (priceType == PriceType.VALUE ? NumberFormat.getInstance().format(containerValue(PriceType.VALUE)) : NumberFormat.getInstance().format(containerValue(PriceType.HIGH_ALCH_VALUE))), 48513));
    }

    private final class PriceCheckerListener extends ItemContainerAdapter {

        PriceCheckerListener() {
            super(player);
        }

        @Override
        public int getWidgetId() {
            return 5063;
        }

        @Override
        public String getCapacityExceededMsg() {
            return "Your price checker is currently full!";
        }

        @Override
        public void itemUpdated(ItemContainer container, Optional<Item> oldItem, Optional<Item> newItem, int index, boolean refresh, boolean login) {
        }

        @Override
        public void bulkItemsUpdated(ItemContainer container) {
            refresh();
        }
    }
}
