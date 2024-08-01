package com.osroyale.content.store.impl;


import com.osroyale.content.store.*;
import com.osroyale.content.store.currency.CurrencyType;
import com.osroyale.game.world.entity.mob.player.Player;
import com.osroyale.game.world.items.Item;
import com.osroyale.game.world.items.containers.ItemContainer;
import com.osroyale.net.packet.out.SendItemOnInterface;
import com.osroyale.net.packet.out.SendScrollbar;
import com.osroyale.net.packet.out.SendString;

import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;
import java.util.OptionalInt;

/**
 * Handles the recipe for disaster store
 *
 * @author Daniel
 */
public class RecipeForDisasterStore extends Store {

    public final StoreItem[] items = {
            new StoreItem(7453, 1, OptionalInt.of(10000), Optional.of(CurrencyType.COINS)),
            new StoreItem(7454, 1, OptionalInt.of(20000), Optional.of(CurrencyType.COINS)),
            new StoreItem(7455, 1, OptionalInt.of(30000), Optional.of(CurrencyType.COINS)),
            new StoreItem(7456, 1, OptionalInt.of(40000), Optional.of(CurrencyType.COINS)),
            new StoreItem(7457, 1, OptionalInt.of(50000), Optional.of(CurrencyType.COINS)),
            new StoreItem(7458, 1, OptionalInt.of(60000), Optional.of(CurrencyType.COINS)),
            new StoreItem(7459, 1, OptionalInt.of(70000), Optional.of(CurrencyType.COINS)),
            new StoreItem(7460, 1, OptionalInt.of(80000), Optional.of(CurrencyType.COINS)),
            new StoreItem(7461, 1, OptionalInt.of(90000), Optional.of(CurrencyType.COINS)),
            new StoreItem(7462, 1, OptionalInt.of(100000), Optional.of(CurrencyType.COINS))
    };

    public RecipeForDisasterStore() {
        super("Recipe For Disaster Store", ItemContainer.StackPolicy.ALWAYS, CurrencyType.COINS, 10);
        this.container.setItems(items, true);
        Arrays.stream(items).filter(Objects::nonNull).forEach(item -> itemCache.put(item.getId(), item.getAmount()));
    }

    @Override
    public void itemContainerAction(Player player, int id, int slot, int action, boolean purchase) {
        switch (action) {
            case 1:
                if (purchase) {
                    this.sendPurchaseValue(player, slot);
                }
                break;
            default:
                int amount = action == 2 ? 1 : action == 3 ? 10 : action == 4 ? 100 : -100;
                if (amount == -100) {
                    throw new IllegalArgumentException("The action given was invalid. [ACTION=" + action + "]");
                }
                if (purchase) {
                    this.purchase(player, new Item(id, amount), slot);
                }
                break;
        }
    }

    @Override
    public void refresh(Player player) {
        player.send(new SendString("Store size: " + items.length, 47507));
        player.send(new SendString(CurrencyType.getValue(player, currencyType), 47508));
        player.send(new SendItemOnInterface(3823, player.inventory.toArray()));
        players.stream().filter(Objects::nonNull).forEach(p -> player.send(new SendItemOnInterface(47551, container.toArray())));
    }

    @Override
    public void open(Player player) {
        player.attributes.set("SHOP", name);

        if (!STORES.containsKey(name)) {
            STORES.put(name, this);
        }

        StoreItem[] storeItems = new StoreItem[10];

        int unlocked = player.glovesTier;

        int lastItem = 0;
        for (int index = 0; index < unlocked; index++) {
            if (index > unlocked)
                break;
            storeItems[index] = items[index];
            lastItem = index;
        }
        for (int i = 0; i < unlocked; i++) {
            player.send(new SendString(storeItems[i].getShopValue() + "," + currencyType.getId(), 47552 + i));
        }
        player.send(new SendString(name, 47502));
        player.send(new SendString("Store size: " + unlocked, 47507));
        player.send(new SendString(CurrencyType.getValue(player, currencyType), 47508));
        player.send(new SendItemOnInterface(47551, storeItems));
        final int scrollBarSize = lastItem <= 32 ? 0 : (lastItem / 8) * 72;
        player.send(new SendScrollbar(47550, scrollBarSize));
        player.send(new SendItemOnInterface(3823, player.inventory.toArray()));
        player.interfaceManager.openInventory(StoreConstant.INTERFACE_ID, 3822);
    }

    @Override
    public void close(Player player) {
        players.remove(player);
        player.attributes.remove("SHOP");
    }

    @Override
    public StoreType type() {
        return StoreType.DEFAULT;
    }

    @Override
    public SellType sellType() {
        return SellType.NONE;
    }

    @Override
    public boolean decrementStock() {
        return false;
    }
}
