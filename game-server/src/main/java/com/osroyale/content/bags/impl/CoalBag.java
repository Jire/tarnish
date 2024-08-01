package com.osroyale.content.bags.impl;

import com.osroyale.content.bags.ItemBag;
import com.osroyale.game.world.entity.mob.player.Player;
import com.osroyale.game.world.items.Item;
import com.osroyale.game.world.items.ItemDefinition;
import com.osroyale.game.world.items.containers.ItemContainer;

import java.util.Objects;
import java.util.function.Predicate;

public class CoalBag extends ItemBag {

    public static final int SIZE = 27;

    public CoalBag() {
        super(new ItemContainer(SIZE, ItemContainer.StackPolicy.NEVER));
    }

    @Override
    public String getItem() {
        return "coal";
    }

    @Override
    public String getName() {
        return "coal bag";
    }

    @Override
    public Predicate<Item> isAllowed() {
        return coal -> Objects.nonNull(coal) && (ItemDefinition.get(coal.getId()).getName().startsWith("Coal") && !ItemDefinition.get(coal.getId()).getName().contains("bag")) && !ItemDefinition.get(coal.getId()).isStackable();
    }

    @Override
    public String getIndication() {
        return "is";
    }

    @Override
    public void check(Player player) {
        if(container.getFreeSlots() == SIZE) {
            player.message("The "+getName()+" is empty.");
            return;
        }

        player.message("The "+getName()+" contains "+container.computeAmountForId(453)+" pieces of coal.");
    }
}