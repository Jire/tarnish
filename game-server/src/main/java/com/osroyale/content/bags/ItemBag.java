package com.osroyale.content.bags;

import com.osroyale.game.world.entity.mob.player.Player;
import com.osroyale.game.world.items.Item;
import com.osroyale.game.world.items.ItemDefinition;
import com.osroyale.game.world.items.containers.ItemContainer;

import java.util.Arrays;
import java.util.function.Predicate;

public abstract class ItemBag {

    public final ItemContainer container;

    public ItemBag(ItemContainer container) {
        this.container = container;
    }

    public abstract String getItem();

    public abstract String getName();

    public abstract Predicate<Item> isAllowed();

    public abstract String getIndication();

    public void fill(Player player) {
        player.message("You search your inventory for "+getItem()+" to put into the "+getName()+"...");

        if (Arrays.stream(player.inventory.getItems()).noneMatch(isAllowed())) {
            player.message("There "+getIndication()+" no "+getItem()+" in your inventory that can be added to the "+getName()+".");
            return;
        }

        Arrays.stream(player.inventory.getItems()).filter(isAllowed()).forEach(item -> {
            player.inventory.remove(item);
            container.add(item);
        });

        player.message("You add the "+getItem()+" to your "+getName()+".");
        player.inventory.refresh();
    }

    public void empty(Player player) {
        if (container.getFreeSlots() == container.capacity()) {
            player.message("The "+getName()+" is already empty.");
            return;
        }
        if (player.inventory.getFreeSlots() <= 0) {
            player.message("You don't have enough inventory space to empty the contents of this "+getName()+".");
            return;
        }
        for(Item item : container.getItems()) {
            int freeSlots = player.inventory.getFreeSlots();
            if (freeSlots <= 0) {
                player.inventory.refresh();
                return;
            }
            if(item == null) continue;

            int amount = item.getAmount();

            if(amount > freeSlots)
                amount = freeSlots;

            container.remove(new Item(item.getId(), amount));
            player.inventory.add(new Item(item.getId(), item.getAmount()));
        }
        player.inventory.refresh();
    }

    public void check(Player player) {
        player.message("You look in your "+getName()+" and see:");

        if (container.getFreeSlots() == container.capacity()) {
            player.message("The "+getName()+" is empty.");
            return;
        }
        for (Item item : container.getItems()) {
            if(item == null) continue;
            player.message(item.getAmount() + "x " + ItemDefinition.get(item.getId()).getName());
        }
    }

}