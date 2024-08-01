package com.osroyale.game.world.items.containers.bank;

import com.osroyale.game.world.InterfaceConstants;
import com.osroyale.game.world.entity.mob.player.Player;
import com.osroyale.game.world.entity.mob.player.PlayerRight;
import com.osroyale.game.world.items.Item;
import com.osroyale.game.world.items.containers.ItemContainer;
import com.osroyale.net.packet.out.SendItemOnInterface;
import com.osroyale.net.packet.out.SendMessage;
import com.osroyale.net.packet.out.SendString;

import java.util.Arrays;
import java.util.Objects;

/**
 * Handles the donator deposit box.
 *
 * @author Daniel
 */
public class DonatorDeposit extends ItemContainer {

    private static final int DONATOR_DEPOSIT_BOX_DISPLAY_ID = 57207;

    /** The player instance. */
    private final Player player;



    /** * Constructs a new <code>DonatorDeposit</code>. */
    public DonatorDeposit(Player player) {
        super(28, ItemContainer.StackPolicy.STANDARD);
        this.player = player;
    }

    /** Handles opening the donator deposit box. */
    public void open() {
        if (!PlayerRight.isDonator(player)) {
            player.dialogueFactory.sendStatement("You need to be a donator to use this feature!", "The donator deposit allows donators to deposit a certain amount", "of items into their banks every 2 minutes.").execute();
            return;
        }
        refresh();
        player.attributes.set("DONATOR_DEPOSIT_KEY", Boolean.TRUE);
        player.interfaceManager.openInventory(57200, 5063);
    }

    /** Handles closing the donator deposit box. */
    public void close() {
        for (Item item : getItems()) {
            if (item == null)
                continue;
            if (remove(item))
                player.inventory.add(item);
        }
        clear(false);
        refresh();
        player.attributes.set("DONATOR_DEPOSIT_KEY", Boolean.FALSE);
    }

    /** Handles confirming the deposit. */
    public void confirm() {
        if (!player.interfaceManager.isInterfaceOpen(57200)) {
            return;
        }
        if (isEmpty()) {
            player.send(new SendMessage("There are no items to deposit!"));
            return;
        }
        if (player.bank.getFreeSlots() < this.size()) {
            player.message("You do not have enough space in your bank to deposit these items.");
            return;
        }

        player.send(new SendMessage("You have deposited " + this.size() + " items into your bank."));

        Arrays.stream(this.getItems()).filter(Objects::nonNull).forEach(item -> {
            if(player.bank.depositFromNothing(item, 0) > 0) {
                remove(item, -1, false);
            }
        });

        clear(false);
        refresh();
    }

    /** Handles deposting items into the container. */
    public boolean deposit(int id, int slot, int amount) {
        if (!player.interfaceManager.isInterfaceOpen(57200)) {
            return false;
        }

        Item item = player.inventory.get(slot);

        if (item == null || item.getId() != id) {
            return false;
        }
        int contain = player.inventory.computeAmountForId(id);

        if (contain < amount) {
            amount = contain;
        }

        int allowedSize = PlayerRight.getDepositAmount(player);
        if (size() >= allowedSize) {
            player.dialogueFactory.sendStatement("You can only deposit up to " + allowedSize + " items.", "The higher your donator rank the more spaces unlocked!").execute();
            return false;
        }

        if(!add(item.getId(), amount)) {
            return false;
        }

        Item current = new Item(item.getId(), amount);

        if (item.isStackable() || amount == 1) {
            player.inventory.remove(current, slot, false);
        } else {
            player.inventory.remove(current, -1, false);
        }

        refresh();

        return true;
    }

    /**  Handles withdrawing items from the container. */
    public boolean withdraw(int id, int slot, int amount) {
        if (!player.interfaceManager.isInterfaceOpen(57200)) {
            return false;
        }

        Item item = get(slot);

        if (item == null || item.getId() != id) {
            return false;
        }

        int contain = computeAmountForId(id);

        if (contain < amount) {
            amount = contain;
        }

        Item current = new Item(id, amount);

        if(!player.inventory.add(current)) {
            return false;
        }

        if (item.isStackable() || amount == 1) {
            remove(current, slot, false);
        } else {
            remove(current, slot, false);
        }

        shift();
        refresh();

        return true;
    }

    private void refresh() {
        refresh(player, DONATOR_DEPOSIT_BOX_DISPLAY_ID);
    }

    @Override
    public void onRefresh() {
        player.inventory.refresh();
        player.send(new SendString(this.size() + "/" + this.capacity(), 57206));
        player.send(new SendItemOnInterface(InterfaceConstants.INVENTORY_STORE, player.inventory.toArray()));
    }
}
