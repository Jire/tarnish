package com.osroyale.content.store;

import com.osroyale.content.store.currency.CurrencyType;
import com.osroyale.content.store.impl.PersonalStore;
import com.osroyale.game.world.entity.mob.player.Player;
import com.osroyale.game.world.entity.mob.player.PlayerRight;
import com.osroyale.game.world.items.Item;
import com.osroyale.game.world.items.containers.ItemContainer;
import com.osroyale.net.packet.out.SendMessage;
import com.osroyale.util.Utility;

import java.util.*;

/**
 * The class which holds support for further abstraction for shops.
 *
 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
 * @since 4-1-2017.
 */
public abstract class Store {

    /** A mapping of each shop by it's name. */
    public static Map<String, Store> STORES = new HashMap<>();

    /**  The name of this shop.  */
    public final String name;

    /** The current item container which contains the current items from this shop.   */
    public ItemContainer container;

    /**  The currency for this shop.   */
    protected final CurrencyType currencyType;

    /**  The map of cached shop item identifications and their amounts.  */
    public Map<Integer, Integer> itemCache;

    /** The set of players that are currently viewing this shop. */
    public final Set<Player> players = new HashSet<>();

    public Store(String name, ItemContainer.StackPolicy policy, CurrencyType currencyType, int capacity) {
        this.name = name;
        this.currencyType = currencyType;
        this.container = new ItemContainer(capacity, policy, new StoreItem[capacity]);
        this.itemCache = new HashMap<>(container.capacity());
    }

    public static void closeShop(Player player) {
        if (!player.interfaceManager.isInterfaceOpen(StoreConstant.INTERFACE_ID) || !player.attributes.has("SHOP")) {
            return;
        }

        Store store = STORES.get(player.attributes.get("SHOP"));

        if (store == null) {
            return;
        }

        store.close(player);
    }

    public static void exchange(Player player, int id, int slot, int action, boolean purchase) {
        if (!player.interfaceManager.isInterfaceOpen(StoreConstant.INTERFACE_ID) || !player.attributes.has("SHOP")) {
            return;
        }

        Store store = STORES.get(player.attributes.get("SHOP"));

        if (store == null) {
            return;
        }

        store.itemContainerAction(player, id, slot, action, purchase);
    }

    protected static List<PersonalStore> getPersonalShops() {
        List<PersonalStore> personal_shops = new ArrayList<>();
        STORES.values().stream().filter(s -> s.type().equals(StoreType.PERSONAL)).forEach(s -> personal_shops.add((PersonalStore) s));
        return personal_shops;
    }

    protected static List<PersonalStore> getFeaturedShops() {
        List<PersonalStore> featured_shops = new ArrayList<>();
        STORES.values().stream().filter(s -> s.type().equals(StoreType.PERSONAL) && ((PersonalStore) s).rank > 0).forEach(s -> featured_shops.add((PersonalStore) s));
        return featured_shops;
    }

    public abstract void itemContainerAction(Player player, int id, int slot, int action, boolean purchase);

    public boolean purchase(Player player, Item item, int slot) {
        if (item == null || !Item.valid(item)) {
            return false;
        }

        Optional<Item> find = container.retrieve(slot);

        if (!find.isPresent()) {
            return false;
        }

        Item found = find.get();

        if (!(found instanceof StoreItem)) {
            return false;
        }

        if (!found.matchesId(item.getId())) {
            player.send(new SendMessage("Something went wrong."));
            return false;
        }

        StoreItem storeItem = (StoreItem) find.get();

        if (storeItem.getAmount() < 1) {
            player.send(new SendMessage("There is none of this item left in stock!"));
            return false;
        }
        if(PlayerRight.isIronman(player)) {
            player.send(new SendMessage("Ironman-players cannot buy items sold by players."));
            return false;
        }

        if (item.getAmount() > storeItem.getAmount())
            item.setAmount(storeItem.getAmount());
        if (!player.inventory.hasCapacityFor(item)) {
            item.setAmount(player.inventory.remaining());

            if (item.getAmount() == 0) {
                player.send(new SendMessage("You do not have enough space in your inventory to buy this item!"));
                return false;
            }
        }

        final int value = storeItem.getShopValue();

        if (!(currencyType.currency.currencyAmount(player) >= (value * item.getAmount()))) {
            player.send(new SendMessage("You do not have enough " + currencyType.toString() + " to buy this item."));
            return false;
        }

        if (player.inventory.remaining() >= item.getAmount() && !item.isStackable()
                || player.inventory.remaining() >= 1 && item.isStackable()
                || player.inventory.contains(item.getId()) && item.isStackable()) {

            if (value > 0 && !currencyType.currency.takeCurrency(player, item.getAmount() * value)) {
                return false;
            }

            if (type().equals(StoreType.PERSONAL) && container.retrieve(slot).isPresent() && (container.retrieve(slot).get().getAmount() - item.getAmount() > 0)) {
                container.retrieve(slot).get().decrementAmountBy(item.getAmount());
            } else if (itemCache.containsKey(item.getId()) && container.retrieve(slot).isPresent()) {
                if (decrementStock()) {
                    container.retrieve(slot).get().decrementAmountBy(item.getAmount());
                }
            } else if (!itemCache.containsKey(item.getId())) {
                if (decrementStock()) {
                    container.remove(item);
                }
                if (type().equals(StoreType.PERSONAL)) {
                    container.shift();
                }
            }
            player.inventory.add(new Item(item.getId(), item.getAmount()));
        } else {
            player.send(new SendMessage("You don't have enough space in your inventory."));
            return false;
        }
        onPurchase(player, new Item(item.getId(), item.getAmount() * value));
        refresh(player);
        return true;
    }

    public void onPurchase(Player player, Item item) {

    }

    protected final void sell(Player player, Item item, int slot, boolean addX) {
        if (item == null || !Item.valid(item)) {
            return;
        }

        final Item inventoryItem = player.inventory.get(slot);

        if (inventoryItem == null) {
            player.send(new SendMessage("This item does not exist."));
            return;
        }

        if (sellType() == SellType.NONE) {
            player.send(new SendMessage("This store won't buy any items."));
            return;
        }

        if (!item.isTradeable()) {
            player.send(new SendMessage("This item can't be sold to shops."));
            return;
        }

        final boolean contains = container.contains(item.getId());

        if (!contains && sellType() == SellType.CONTAINS) {
            player.send(new SendMessage("You can't sell " + item.getName() + " to this shop."));
            return;
        }
        if (!container.hasCapacityFor(item)) {
            player.send(new SendMessage("There is no room in this store for the item you are trying to sell!"));
            return;
        }

        if (player.inventory.remaining() == 0 && !currencyType.currency.canRecieveCurrency(player) && inventoryItem.getAmount() > 1) {
            player.send(new SendMessage("You do not have enough space in your inventory to sell this item!"));
            return;
        }

        if (CurrencyType.isCurrency(item.getId())) {
            player.send(new SendMessage("You can not sell currency to this shop!"));
            return;
        }

        final int sellValue = item.getSellValue();

        if (sellValue >= StoreConstant.MAXIMUM_SELL_VALUE) {
            player.send(new SendMessage("This item can not be sold as it has a value greater than 500,000 coins!"));
            return;
        }

        final int amount = player.inventory.computeAmountForId(item.getId());

        if (item.getAmount() > amount && !item.isStackable()) {
            item.setAmount(amount);
        } else if (item.getAmount() > inventoryItem.getAmount() && item.isStackable()) {
            item.setAmount(inventoryItem.getAmount());
        }

        player.inventory.remove(item, slot);

        if (sellValue > 0) {
            currencyType.currency.recieveCurrency(player, item.getAmount() * sellValue);
        }

        final StoreItem converted = new StoreItem(item.getId(), item.getAmount());

        container.add(converted);

        refresh(player);
    }

    public abstract void refresh(Player player);

    protected final void sendSellValue(Player player, int slot) {
        Item item = player.inventory.get(slot);

        if (item == null) {
            return;
        }

        if (!item.isTradeable()) {
            player.send(new SendMessage("This item can't be sold to shops."));
            return;
        }

        if (CurrencyType.isCurrency(item.getId())) {
            player.send(new SendMessage("You can not sell currency to this shop!"));
            return;
        }

        final int value = item.getSellValue();

        if (value <= 0) {
            player.send(new SendMessage(String.format("This store will buy %s for free!", item.getName())));
            return;
        }

        final String message = this.sellType() != SellType.NONE ? String.format("This store will buy %s for %s %s.", item.getName(), Utility.formatDigits(value), currencyType.toString()) : String.format("[%s] will not buy any items.", name);
        player.send(new SendMessage(message));
    }

    protected void sendPurchaseValue(Player player, int slot) {
        Optional<Item> find = container.retrieve(slot);

        if (!find.isPresent()) {
            return;
        }

        Item item = find.get();

        if (item instanceof StoreItem) {
            StoreItem storeItem = (StoreItem) item;
            final int value = storeItem.getShopValue();
            String message = "This store will sell " + item.getName() + " for " + (value <= 0 ? "free!" : Utility.formatDigits(value) + " " + storeItem.getShopCurrency(this).toString() + ".");
            player.message(message);
        }

    }

    public abstract void open(Player player);

    public abstract void close(Player player);

    public abstract StoreType type();

    public abstract SellType sellType();

    public boolean decrementStock() {
        return true;
    }

    @Override
    public final int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        return result;
    }

    @Override
    public final boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (!(obj instanceof Store))
            return false;
        Store other = (Store) obj;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        return true;
    }
}
