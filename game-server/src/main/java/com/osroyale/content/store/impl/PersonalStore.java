package com.osroyale.content.store.impl;

import com.osroyale.Config;
import com.osroyale.content.dialogue.DialogueFactory;
import com.osroyale.content.store.*;
import com.osroyale.content.store.currency.CurrencyType;
import com.osroyale.game.world.World;
import com.osroyale.game.world.entity.mob.player.Player;
import com.osroyale.game.world.items.Item;
import com.osroyale.game.world.items.containers.ItemContainer;
import com.osroyale.net.packet.out.*;
import com.osroyale.util.GameSaver;
import com.osroyale.util.MessageColor;
import com.osroyale.util.Utility;

import java.util.*;

/**
 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
 * @since 4-1-2017.
 */
public final class PersonalStore extends Store {

    /** The shops the player is currently viewing. */
    public static final Map<Integer, PersonalStore> FEATURED_SHOPS = new HashMap<>();

    /** A mapping of a player name with the amount of coins waiting for him. */
    public static Map<String, Long> SOLD_ITEMS = new HashMap<>();

    /** The rank of this shop on the featured list. */
    public final int rank;

    /** The title of this shop. */
    public String title;

    /** The caption of this shop. */
    public String caption;

    /** The flag is the shop is being updated by the owner. */
    private boolean updating = false;

    /** Creates a new {@link Store}. */
    public PersonalStore(String name, Optional<StoreItem[]> items, int rank, String title, String caption, CurrencyType currency) {
        super(name, ItemContainer.StackPolicy.STANDARD, currency, 28);
        this.rank = rank;
        this.title = title;
        this.caption = caption;
        items.ifPresent(i -> this.container.setItems(i, false));
    }

    public static void add(Player player, PersonalStore store) {
        STORES.put(player.getName(), store);
    }

    /** Handles claiming coins from the personal store. */
    public static void claimCoins(Player player) {
//        DialogueFactory factory = player.dialogueFactory;
//        if (!SOLD_ITEMS.containsKey(player.getName())) {
//            factory.sendItem("Personal Store", "There are no coins available for you to collect.", Config.CURRENCY).execute();
//            return;
//        }
//        long amount = SOLD_ITEMS.get(player.getName());
//        boolean added = player.bankVault.add(amount);
//        if (added) {
//            SOLD_ITEMS.remove(player.getName());
//        }
//        String message = added ? "Your collected coins have been sent to your bank vault." : "There was not enough room inside your bank vault to hold your coins.";
//        player.send(new SendMessage(message, MessageColor.DARK_GREEN));
//        openMenu(player);
//        factory.sendItem("Personal Store", message, Config.CURRENCY).execute();
    }

    public static void openMenu(Player player) {
        if (player == null) {
            return;
        }

        if (player.getName() == null) {
            return;
        }

        long collect = SOLD_ITEMS.get(player.getName()) == null ? 0 : SOLD_ITEMS.get(player.getName());
        player.send(new SendString("<col=d38537>Active stores:</col> " + getPersonalShops().size(), 38210));
        player.send(new SendString("<col=d38537>Total Items Sold:</col> " + Utility.formatPrice(GameSaver.ITEMS_SOLD), 38211));
        player.send(new SendString("<col=d38537>Total Sold Worth:</col> " + Utility.formatPrice(GameSaver.PERSONAL_ITEM_WORTH), 38213));

        player.send(new SendString(collect <= 0 ? "None" : Utility.formatDigits(collect), 38207));
        player.interfaceManager.open(38200);
    }

    /** Creates the player's shops if non-existent or will enter. */
    public static void myShop(Player player) {
        if (!Store.STORES.containsKey(player.getName())) {
            PersonalStore shop = new PersonalStore(player.getName(), Optional.empty(), player.right.getCrown(), player.getName() + "'s Store", "No caption set", CurrencyType.COINS);
            Store.STORES.put(player.getName(), shop);
        }

        STORES.get(player.getName()).open(player);
    }

    /** Changes the name of the shop. */
    public static void changeName(Player player, String input, boolean caption) {
        if (!Store.STORES.containsKey(player.getName())) {
            player.send(new SendMessage("You do not have a store. Set it up by first opening it from the previous menu."));
            return;
        }

        String type = caption ? "caption" : "title";
        if (Arrays.stream(Config.BAD_STRINGS).anyMatch(b -> input.contains(b))) {
            player.send(new SendMessage("You have entered an invalid shop " + type + "."));
            player.interfaceManager.close();
            return;
        }

        PersonalStore shop = (PersonalStore) Store.STORES.get(player.getName());
        String context = Utility.formatName(input);

        if (caption) {
            shop.caption = context;
        } else {
            shop.title = context;
            player.send(new SendString(shop.title, 40002));
        }

        player.send(new SendMessage("You have changed your shop " + type + " to: " + context + "."));
    }

    /** Handles editing the personal store. */
    public static void edit(Player player) {
        DialogueFactory f = player.dialogueFactory;
        f.sendStatement("Loading your shop details...").sendOption("Change shop name", () -> {
            f.onAction(() -> {
                player.send(new SendInputMessage("Enter the name of your shop:", 30, input -> {
                    changeName(player, input, false);
                }));
            });
        }, "Change caption", () -> {
            f.onAction(() -> {
                player.send(new SendInputMessage("Enter the caption of your shop:", 30, input -> {
                    changeName(player, input, true);
                }));
            });
        }).execute();
    }

    /** Opens the panel where a player can search for shops. */
    public static void openPanel(Player player) {
        List<PersonalStore> personalShops = getPersonalShops();
        int size = personalShops.size() < 10 ? 10 : personalShops.size();
        for (int string = 53031, index = 0; index < size; index++) {
            PersonalStore shop = index >= personalShops.size() ? null : personalShops.get(index);
            String tooltip = shop == null ? "" : "View <col=ffb000>" + shop.name + "<col=FFFFFF>'s shop";
            String name = shop == null ? "" : shop.name;
            String caption = shop == null ? "" : shop.caption;
            if (shop != null)
                player.viewing_shops.put(-(12505 - (index * 3)), shop);
            player.send(new SendTooltip(tooltip, string));
            string++;
            player.send(new SendString(name, string));
            string++;
            player.send(new SendString(caption, string));
            string++;
        }

        List<PersonalStore> featured_shops = getFeaturedShops();
        for (int index = 0, string = 53008; index < 10; index++, string++) {
            PersonalStore shop = index >= featured_shops.size() ? null : featured_shops.get(index);
            String name = shop == null ? "" : shop.name;
            String tooltip = shop == null ? "" : "View <col=ffb000>" + shop.name + "<col=FFFFFF>'s shop";
            if (shop != null)
                FEATURED_SHOPS.put(-(12528 + index), shop);

            player.send(new SendTooltip(tooltip, string));
            player.send(new SendString(name, string));
        }
        player.send(new SendString("Available stores: " + personalShops.size(), 53023));
        player.send(new SendScrollbar(53030, (size * 28)));
        player.interfaceManager.open(53000);
    }

    /** Handles adding an item to the player's personal shop. */
    public void add(Player player, Item item, int slot, boolean addX) {
        if (!player.interfaceManager.isInterfaceOpen(StoreConstant.INTERFACE_ID)) {
            return;
        }
        if (!item.isTradeable()) {
            player.send(new SendMessage("You can not sell untradeable items in your shop!"));
            return;
        }
        if (CurrencyType.isCurrency(item.getId())) {
            player.send(new SendMessage("You can not sell any currency in your shop!"));
            return;
        }

        Item invItem = player.inventory.get(slot);
        final StoreItem storeItem = new StoreItem(invItem.getId(), item.getAmount());

        if (!addX) {
            player.send(new SendInputAmount("What do you want to value your <col=027399>" + item.getName() + "</col>?", 10, value -> {
                setValue(player, invItem, storeItem, Integer.parseInt(value), slot);
                refresh(player);
            }));
            return;
        }

        player.send(new SendInputAmount("How much would you like to put in your shop?", 10, amount -> {
            storeItem.setAmount(Integer.parseInt(amount));
            player.send(new SendInputAmount("What do you want to value your <col=027399>" + item.getName() + "</col>?", 10, value -> {
                setValue(player, invItem, storeItem, Integer.parseInt(value), slot);
                refresh(player);
            }));
        }));
    }

    private void setValue(Player player, Item invItem, StoreItem storeItem, int value, int slot) {
        if (!player.interfaceManager.isInterfaceOpen(StoreConstant.INTERFACE_ID)) {
            return;
        }
        storeItem.setShopValue(value);
        int amount = player.inventory.computeAmountForId(invItem.getId());

        if (storeItem.getAmount() > amount && !storeItem.isStackable()) {
            storeItem.setAmount(amount);
        } else if (storeItem.getAmount() > player.inventory.get(slot).getAmount() && storeItem.isStackable()) {
            storeItem.setAmount(player.inventory.get(slot).getAmount());
        }

        player.inventory.remove(storeItem, slot);
        Optional<Item> contains = container.stream().filter(i -> i != null && storeItem.getId() == i.getId() && ((StoreItem) i).getShopValue() == storeItem.getShopValue()).findFirst();

        if (contains.isPresent()) {
            contains.get().incrementAmountBy(storeItem.getAmount());
        } else {
            if (!storeItem.isStackable() && storeItem.getAmount() > 1) {
                container.add(storeItem, false, true);
            } else {
                container.add(storeItem);
            }
        }
    }

    /** Handles adding an item to the player's personal shop. */
    public void remove(Player player, Item item, int slot) {
        if (!player.interfaceManager.isInterfaceOpen(StoreConstant.INTERFACE_ID)) {
            return;
        }
        final StoreItem storeItem = (StoreItem) this.container.retrieve(slot).orElse(null);

        if (storeItem == null)
            return;

        if (item.getAmount() > storeItem.getAmount())
            item.setAmount(storeItem.getAmount());
        if (!player.inventory.hasCapacityFor(item)) {
            item.setAmount(player.inventory.remaining());
            if (item.getAmount() == 0) {
                player.send(new SendMessage("You do not have enough space in your inventory to withdraw this item!"));
                return;
            }
        }

        if (player.inventory.remaining() >= item.getAmount() && !item.isStackable() || player.inventory.remaining() >= 1 && item.isStackable() || player.inventory.contains(item.getId()) && item.isStackable()) {
            if ((storeItem.getAmount() - item.getAmount()) < 1) {
                container.remove(item, slot, false, false);
                container.shift();
            } else {
                storeItem.decrementAmountBy(item.getAmount());
            }
            player.inventory.add(item);
        } else {
            player.send(new SendMessage("You don't have enough space in your inventory."));
            return;
        }
        refresh(player);
    }

    /** Modifies the player's shop item. */
    private void modify(Player player, int slot) {
        if (!player.interfaceManager.isInterfaceOpen(StoreConstant.INTERFACE_ID)) {
            return;
        }
        DialogueFactory factory = player.dialogueFactory;
        StoreItem item = (StoreItem) container.retrieve(slot).orElse(null);
        if (item == null)
            return;
        updating = true;
        factory.sendOption(
                "Change value",
                () -> factory.onAction(() -> player.send(new SendInputAmount("Enter new value", 10, input -> {
                    item.setShopValue(Integer.parseInt(input));
                    player.send(new SendMessage("You've changed " + item.getName() + "'s value to " + Utility.formatDigits(Integer.parseInt(input)) + " " + item.getShopCurrency(this)));
                    refresh(player);
                    player.dialogueFactory.clear();
                    updating = false;
                }))),

                "Change currency",
                () -> factory.onAction(() -> updating = false),

                "Nevermind",
                () -> factory.onAction(() -> {
                    updating = false;
                    player.dialogueFactory.clear();
                })).execute();
    }

    /** Checks if the player is the owner of the shop. */
    private boolean isOwner(Player player) {
        return this.name.equals(player.getName());
    }

    @Override
    public void itemContainerAction(Player player, int id, int slot, int action, boolean purchase) {
        switch (action) {
            case 1:
                if (purchase) {
                    if (this.isOwner(player)) {
                        this.modify(player, slot);
                    } else {
                        this.sendPurchaseValue(player, slot);
                    }
                } else {
                    if (this.isOwner(player)) {
                        this.add(player, new Item(id, 1), slot, false);
                    }
                }
                break;
            default:
                int count = purchase ? this.container.retrieve(slot).orElse(null).getAmount() : player.inventory.retrieve(slot).orElse(null).getAmount();
                int amount = action == 2 ? (purchase ? 1 : 5) : action == 3 ? 10 : action == 4 ? count : -100;

                if (purchase) {
                    if (this.isOwner(player)) {
                        this.remove(player, new Item(id, amount), slot);
                    } else {
                        if (updating) {
                            player.send(new SendMessage("The owner is currently updating the shop, try again in a few seconds.", MessageColor.RED));
                            return;
                        }
                        this.purchase(player, new Item(id, amount), slot);
                    }
                } else {
                    if (this.isOwner(player)) {
                        this.add(player, new Item(id, amount), slot, amount == -100);
                    }
                }
                break;
        }
    }

    @Override
    public void onPurchase(Player player, Item item) {
        if (!SOLD_ITEMS.containsKey(name))
            SOLD_ITEMS.put(name, 0L);

        long amount = SOLD_ITEMS.get(name);
        SOLD_ITEMS.replace(name, amount + item.getAmount());
        GameSaver.ITEMS_SOLD++;
        GameSaver.PERSONAL_ITEM_WORTH += (item.getValue() * item.getAmount());
        World.search(name).ifPresent(p -> p.send(new SendMessage("You have coins to collect from your shop!", MessageColor.DARK_GREEN)));
    }

    @Override
    public void refresh(Player player) {
        for (Player p : players) {
            if (p != null) {
                open(p);
                if (p != player)
                    p.send(new SendMessage("[" + title + "] The shop has been updated!"));
            }
        }
    }

    @Override
    public void open(Player player) {
        player.attributes.set("SHOP", name);
        players.add(player);
        StoreItem[] items = (StoreItem[]) this.container.getItems();

        int lastItem = 0;
        if (items.length != 0) {
            for (int i = 0; i < items.length; i++) {
                player.send(new SendString(items[i] == null ? "0" : items[i].getShopValue() + "," + 0, 40052 + i));
                lastItem = i;
            }
        }
        final int scrollBarSize = lastItem <= 32 ? 0 : (lastItem / 8) * 72;
        player.send(new SendScrollbar(40050, scrollBarSize));
        player.send(new SendString(title, 40002));
        player.send(new SendItemOnInterface(40051, items));
        player.send(new SendItemOnInterface(3823, player.inventory.toArray()));
        player.send(new SendString("Store size: " + items.length, 40007));
        player.interfaceManager.openInventory(StoreConstant.INTERFACE_ID, 3822);
    }

    @Override
    public void close(Player player) {
        players.remove(player);
        player.attributes.remove("SHOP");
        player.viewing_shops.clear();
    }

    @Override
    public StoreType type() {
        return StoreType.PERSONAL;
    }

    @Override
    public SellType sellType() {
        return SellType.NONE;
    }
}
