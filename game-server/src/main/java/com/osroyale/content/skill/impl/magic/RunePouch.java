package com.osroyale.content.skill.impl.magic;

import com.osroyale.game.world.entity.mob.player.Player;
import com.osroyale.game.world.items.Item;
import com.osroyale.game.world.position.Area;
import com.osroyale.net.packet.out.SendItemOnInterface;
import com.osroyale.util.Utility;

import java.util.LinkedList;
import java.util.List;

/**
 * Handles the rune pouch.
 *
 * @author Daniel
 */
public class RunePouch {

    /** The maximum amount of total runes the player can carry in their rune pouch. */
    private static final int MAXIMUM_RUNE_CAPACITY = 16_000;

    /** Array of all runes allowed to be inside the rune pouch. */
    private final Item[] ALLOWED_RUNES = {
            new Item(554), new Item(555), new Item(556), new Item(557), new Item(558),
            new Item(559), new Item(560), new Item(561), new Item(562), new Item(563),
            new Item(564), new Item(565), new Item(566), new Item(9075), new Item(21880)
    };

    /** The player instance. */
    private final Player player;

    /** The runes stores in the rune pouch; */
    public List<Item> runes = new LinkedList<>();

    /** Constructs a new <code>RunePouch</code>. */
    public RunePouch(Player player) {
        this.player = player;
    }

    public void open() {
        refresh();
        player.interfaceManager.open(41700);
    }

    public void clear(boolean open) {
        if(open) runes.forEach(player.inventory::add);
        runes.clear();
        if(open) player.interfaceManager.open(41700);
        refresh();
    }

    public void clear() {
        clear(true);
    }

    public void refresh() {
        player.send(new SendItemOnInterface(41710, runes.toArray(new Item[runes.size()])));
        player.send(new SendItemOnInterface(41711, player.inventory.getItems()));
    }

    public void clearInterface() {
        player.send(new SendItemOnInterface(41710, new Item[0]));
    }

    public void withdraw(int item, int amount) {
        for (Item rune : runes) {
            if (rune.getId() == item) {
                int current = player.inventory.computeAmountForId(item);
                if (rune.getAmount() - amount < 0) amount = rune.getAmount();
                player.inventory.add(item, amount);
                int newAm = player.inventory.computeAmountForId(item);
                if (newAm - current < amount) amount = newAm - current;
                rune.decrementAmountBy(amount);
                if (rune.getAmount() == 0)
                    runes.remove(rune);
                refresh();
                return;
            }
        }
    }

    public void deposit(Item item, int amount) {
        boolean allowed = false;
        for (Item rune : ALLOWED_RUNES) {
            if (rune.getId() == item.getId()) {
                allowed = true;
                break;
            }
        }
        if (!allowed) {
            player.message("You can only deposit runes into the rune pouch!");
            return;
        }

        for (Item rune : runes) {
            if (item.getId() == rune.getId()) {
                if (rune.getAmount() + amount > MAXIMUM_RUNE_CAPACITY) {
                    amount = MAXIMUM_RUNE_CAPACITY - rune.getAmount();
                    player.message("You can only have a total of " + Utility.formatDigits(MAXIMUM_RUNE_CAPACITY) + " of the same rune in your rune pouch.");
                }
                player.inventory.remove(item.getId(), amount);
                rune.incrementAmountBy(amount);
                refresh();
                return;
            }
        }

        if (runes.size() >= 3) {
            player.message("Your rune pouch is currently full and can not hold any more types of runes!");
            return;
        }

        if (amount > MAXIMUM_RUNE_CAPACITY) {
            amount = MAXIMUM_RUNE_CAPACITY;
            player.message("You can only have a total of " + Utility.formatDigits(MAXIMUM_RUNE_CAPACITY) + " of the same rune in your rune pouch.");
        }

        player.inventory.remove(item.getId(), amount);
        runes.add(new Item(item.getId(), amount));
        refresh();
    }

    public int getRuneAmount() {
        int amount = 0;
        for (Item rune : runes) {
            amount += rune.getAmount();
        }
        return amount;
    }

    public int getRuneAmount(int id) {
        int amount = 0;
        for (Item rune : runes) {
            if (rune.getId() == id)
                amount += rune.getAmount();
        }
        return amount;
    }

    public boolean contains(Item item) {
        for (Item rune : runes) {
            if (rune.getId() == item.getId() && rune.getAmount() >= item.getAmount())
                return true;
        }
        return false;
    }

    public boolean containsId(int item) {
        for (Item rune : runes) {
            if (rune.getId() == item)
                return true;
        }
        return false;
    }

    public void remove(Item item) {
        for (Item rune : runes) {
            if (rune.equalIds(item)) {
                rune.decrementAmountBy(item.getAmount());
                if (rune.getAmount() == 0)
                    runes.remove(rune);
                return;
            }
        }
    }

    public boolean death(Item item) {
        if (item.getId() == 12971 && Area.inWilderness(player)) {
            runes.clear();
            return true;
        }

        return false;
    }
}
