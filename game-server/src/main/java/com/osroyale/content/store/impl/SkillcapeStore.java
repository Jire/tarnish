package com.osroyale.content.store.impl;


import com.osroyale.content.emote.Skillcape;
import com.osroyale.content.store.*;
import com.osroyale.content.store.currency.CurrencyType;
import com.osroyale.game.world.entity.mob.player.Player;
import com.osroyale.game.world.entity.skill.Skill;
import com.osroyale.game.world.items.Item;
import com.osroyale.game.world.items.containers.ItemContainer;
import com.osroyale.net.packet.out.SendItemOnInterface;
import com.osroyale.net.packet.out.SendScrollbar;
import com.osroyale.net.packet.out.SendString;

import java.util.Arrays;
import java.util.Objects;

import static com.osroyale.content.store.currency.CurrencyType.COINS;

/**
 * Handles the recipe for disaster store
 *
 * @author Daniel
 */
public class SkillcapeStore extends Store {

    private final StoreItem[] items = Skillcape.getItems();

    public SkillcapeStore() {
        super("Skillcape Store", ItemContainer.StackPolicy.ALWAYS, COINS, Skill.SKILL_COUNT);
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
    public boolean purchase(Player player, Item item, int slot) {
        Skillcape cape = Skillcape.forId(item.getId());

        if (cape == null) {
            player.message("Something went wrong with your purchase!");
            return false;
        }

        if (player.skills.getMaxLevel(cape.getSkill()) < 99) {
            player.message("You need a " + Skill.getName(cape.getSkill()) + " level of 99 to purchase this cape!");
            return false;
        }

        if (super.purchase(player, item, slot)) {
            player.inventory.addOrDrop(new Item(item.getId() + 1, 1));
            refresh(player);
            return true;
        }

        return false;
    }

    @Override
    public void refresh(Player player) {
        player.send(new SendString(CurrencyType.getValue(player, currencyType), 47508));
        player.send(new SendItemOnInterface(3823, player.inventory.toArray()));
    }

    @Override
    public void open(Player player) {
        player.attributes.set("SHOP", name);

        if (!STORES.containsKey(name)) {
            STORES.put(name, this);
        }

        for (int index = 0; index < Skill.SKILL_COUNT; index++) {
            player.send(new SendString(100000 + "," + currencyType.getId(), 47552 + index));
        }

        player.send(new SendString(name, 47502));
        player.send(new SendString("Store size: " + Skill.SKILL_COUNT, 47507));
        player.send(new SendString(CurrencyType.getValue(player, currencyType), 47508));
        player.send(new SendItemOnInterface(47551, items));
        player.send(new SendScrollbar(47550, 0));
        player.send(new SendItemOnInterface(3823, player.inventory.toArray()));
        player.interfaceManager.openInventory(StoreConstant.INTERFACE_ID, 3822);
    }

    @Override
    public void close(Player player) {
//        players.remove(player);
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
