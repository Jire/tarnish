package com.osroyale.content.store.impl;

import com.osroyale.content.store.*;
import com.osroyale.content.store.currency.CurrencyType;
import com.osroyale.game.task.TickableTask;
import com.osroyale.game.world.World;
import com.osroyale.game.world.entity.mob.player.Player;
import com.osroyale.game.world.entity.mob.player.PlayerRight;
import com.osroyale.game.world.items.Item;
import com.osroyale.game.world.items.containers.ItemContainer;
import com.osroyale.net.packet.out.*;

import java.util.Arrays;
import java.util.Objects;

/**
 * The default shop which are owned by the server.
 *
 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
 * @since 4-1-2017.
 */
public final class DefaultStore extends Store {

    /** The items in this shop. */
    public final StoreItem[] items;

    /** The original item container this shop started with. */
    final ItemContainer original;

    /** Determines if this shop restocks. */
    final boolean restock;

    private final SellType sellType;

    /** The shop restock task that will restock the shops. */
    private StoreRestockTask restockTask;

    /** Creates a new {@link Store}. */
    public DefaultStore(StoreItem[] items, String name, SellType sellType, boolean restock, CurrencyType currency) {
        super(name, ItemContainer.StackPolicy.ALWAYS, currency, sellType == SellType.ANY ? 80 : items.length);
        this.items = items;
        this.restock = restock;
        this.sellType = sellType;
        this.original = new ItemContainer(items.length, ItemContainer.StackPolicy.ALWAYS);
        this.original.setItems(items, false);
        this.container.setItems(items, false);
        Arrays.stream(items).filter(Objects::nonNull).forEach(item -> itemCache.put(item.getId(), item.getAmount()));
    }

    /** Determines if the items in the container need to be restocked. */
    private boolean needsRestock() {
        return container.stream().filter(Objects::nonNull).anyMatch(i -> !itemCache.containsKey(i.getId()) || (itemCache.containsKey(i.getId()) && i.getAmount() < itemCache.get(i.getId())));
    }

    /** Determines if the items in the container no longer need to be restocked. */
    boolean restockCompleted() {
        return container.stream().filter(Objects::nonNull).allMatch(i -> {
            if (itemCache.containsKey(i.getId()) && i.getAmount() >= itemCache.get(i.getId())) {//shop item.
                return true;
            } else if (!itemCache.containsKey(i.getId())) {//unique item.
                return false;
            }
            return false;
        });
    }

    @Override
    public void itemContainerAction(Player player, int id, int slot, int action, boolean purchase) {
        switch (action) {
            case 1:
                if (purchase) {
                    this.sendPurchaseValue(player, slot);
                } else {
                    this.sendSellValue(player, slot);
                }
                break;
            case 5:
                player.send(new SendInputAmount("Enter amount", 10, amount -> {
                    if (purchase) {
                        this.purchase(player, new Item(id, Integer.parseInt(amount)), slot);
                    } else {
                        this.sell(player, new Item(id, Integer.parseInt(amount)), slot, true);
                    }
                }));
                break;
            default:
                int amount = 0;

                if (action == 2) {
                    amount = 1;
                }
                if (action == 3) {
                    amount = 10;
                }
                if (action == 4) {
                    amount = 100;
                }

                if (purchase) {
                    this.purchase(player, new Item(id, amount), slot);
                } else {
                    this.sell(player, new Item(id, amount), slot, false);
                }
                break;
        }
    }

    @Override
    public void open(Player player) {
        if (PlayerRight.isIronman(player)) {
            if (Arrays.stream(StoreConstant.IRON_MAN_STORES).noneMatch(s -> s.equalsIgnoreCase(name))) {
                player.send(new SendMessage("As an iron man you do not have access to this store!"));
                return;
            }
        }

        player.attributes.set("SHOP", name);

        if (!STORES.containsKey(name)) {
            STORES.put(name, this);
        }

        players.add(player);
        player.inventory.refresh();
        refresh(player);
        player.send(new SendString(name, 47502));
        player.interfaceManager.openInventory(StoreConstant.INTERFACE_ID, 3822);
    }

    @Override
    public void close(Player player) {
        players.remove(player);
        player.attributes.remove("SHOP");
    }

    @Override
    public void refresh(Player player) {
        player.send(new SendString("Store size: " + items.length, 47507));
        player.send(new SendString(CurrencyType.getValue(player, currencyType), 47508));

        final Item[] items = container.toArray();

        int lastItem = 0;
        for (int i = 0; i < items.length; i++) {
            Item item = items[i];

            if (item == null) {
                continue;
            }

            if (item instanceof StoreItem) {
                StoreItem storeItem = (StoreItem) items[i];
                player.send(new SendString(storeItem.getShopValue() + "," + storeItem.getShopCurrency(this).getId(), 47552 + i));
                lastItem = i;
            }
        }

        final int scrollBarSize = lastItem <= 32 ? 0 : (lastItem / 8) * 72;
        player.send(new SendScrollbar(47550, scrollBarSize));
        player.send(new SendItemOnInterface(3823, player.inventory.toArray()));
        players.stream().filter(Objects::nonNull).forEach(p -> player.send(new SendItemOnInterface(47551, container.toArray())));
        if (restock) {
            if (restockTask != null && restockTask.isRunning()) {
                return;
            }
            if (!needsRestock()) {
                return;
            }
            restockTask = new StoreRestockTask(this);
            World.schedule(restockTask);
        }
    }

    @Override
    public StoreType type() {
        return StoreType.DEFAULT;
    }

    @Override
    public SellType sellType() {
        return sellType;
    }


    /** The task that will restock items in shop containers when needed. */
    private static final class StoreRestockTask extends TickableTask {

        /** The container that will be restocked. */
        private final DefaultStore container;

        /** Creates a new {@link StoreRestockTask}. */
        StoreRestockTask(DefaultStore container) {
            super(false, 0);
            this.container = container;
        }

        @Override
        protected void tick() {
            if (container.restockCompleted() || !container.restock) {
                this.cancel();
                return;
            }

            if (tick >= 15) {
                final Item[] items = container.container.toArray();
                boolean restocked = false;
                for (Item item : items) {
                    if (item == null) {
                        continue;
                    }
                    if (item instanceof StoreItem) {
                        if (restock((StoreItem) item)) {
                            restocked = true;
                        }
                    }
                }
                if (restocked) {
                    for (Player player : container.players) {
                        if (player != null) {
                            player.send(new SendItemOnInterface(47551, container.container.toArray()));
                        }
                    }
                }
                tick = 0;
            }

        }

        /** Attempts to restock {@code item} for the container. */
        private boolean restock(StoreItem item) {
            if (!item.canReduce()) {
                return false;
            }

            final int reduceAmount = item.getAmount() > 100 ? (int) ((double) item.getAmount() * 0.05D) : 1;

            // if the item is not an original item
            if (!container.original.contains(item.getId())) {
                if (item.getAmount() - 1 <= 0) {
                    container.container.remove(item);
                } else {
                    item.decrementAmountBy(reduceAmount);
                }
                return true;
            } else {
                // the item is an original item
                final boolean originalItem = container.itemCache.containsKey(item.getId());
                final int originalAmount = container.itemCache.get(item.getId());
                // increment the original item if its not fully stocked
                if (originalItem && item.getAmount() < originalAmount) {
                    item.incrementAmount();
                    return true;
                } else if (originalItem && item.getAmount() > originalAmount) { // decrement original item if its over stocked
                    item.decrementAmountBy(reduceAmount);
                    return true;
                }
            }
            return false;
        }
    }
}
