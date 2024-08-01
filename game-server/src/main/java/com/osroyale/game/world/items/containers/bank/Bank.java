package com.osroyale.game.world.items.containers.bank;

import com.osroyale.game.world.InterfaceConstants;
import com.osroyale.game.world.entity.mob.player.Player;
import com.osroyale.game.world.entity.mob.player.PlayerRight;
import com.osroyale.game.world.items.Item;
import com.osroyale.game.world.items.ItemDefinition;
import com.osroyale.game.world.items.containers.ItemContainer;
import com.osroyale.game.world.items.containers.ItemContainerAdapter;
import com.osroyale.game.world.items.containers.pricechecker.PriceType;
import com.osroyale.net.packet.out.*;
import com.osroyale.util.Utility;
import plugin.itemon.item.LootingBagPlugin;

import java.util.Arrays;
import java.util.Optional;

/**
 * Handles the Bank container.
 *
 * @author Michael
 * @author Daniel
 */
public class Bank extends ItemContainer {

    /** The size of all equipment instances. */
    public static final int SIZE = 360;

    /** The tab amount array. */
    public int[] tabAmounts = new int[10];

    /** The player instance. */
    private final Player player;

    /** The current bank tab. */
    public int bankTab = 0;

    /** The noting flag. */
    public boolean noting = false;

    /** The inserting flag. */
    public boolean inserting = false;

    /** The place holder flag. */
    public boolean placeHolder;

    /** Constructs a new <code>Bank<code>. */
    public Bank(Player player) {
        super(SIZE, StackPolicy.ALWAYS);
        this.player = player;
        this.placeHolder = false;
        addListener(new BankListener());
    }

    /** Opens the bank itemcontainer. */
    public void open() {
        if (player.right.equals(PlayerRight.ULTIMATE_IRONMAN)) {
            player.send(new SendMessage("As an ultimate iron man you may not use banks!"));
            return;
        }
        if (player.bankPin.hasPin() && !player.bankPin.entered) {
            player.bankPin.enter();
            return;
        }
        noting = false;
        player.send(new SendString("360", 60018));
        player.attributes.set("BANK_KEY", Boolean.TRUE);
        player.interfaceManager.openInventory(60000, InterfaceConstants.INVENTORY_STORE - 1);
        refresh();
    }

    /** Refreshes the bank itemcontainer. */
    public void refresh() {
        player.send(new SendConfig(304, inserting ? 1 : 0));
        player.send(new SendTooltip((inserting ? "Enable swapping" : "Enable inserting"), 60006));
        player.send(new SendConfig(115, noting ? 1 : 0));
        player.send(new SendTooltip((noting ? "Disable" : "Enable") + " noting", 60007));
        player.send(new SendConfig(116, placeHolder ? 1 : 0));
        player.send(new SendTooltip((placeHolder ? "Disable" : "Enable") + " place holders", 60073));
        player.send(new SendItemOnInterface(InterfaceConstants.WITHDRAW_BANK, tabAmounts, toArray()));
        player.inventory.refresh(player, InterfaceConstants.INVENTORY_STORE);
        player.inventory.refresh();
        sendValue();
    }


    public void sendValue() {
        long value = 0;
        int start = slotForTab(bankTab);
        int end = start + tabAmounts[bankTab];
        if (bankTab == 0) {
            start = 0;
            end = size();
        }
        for (int slot = start; slot < end; slot++) {
            Item item = get(slot);
            if (item == null) continue;
            int price = item.getValue(PriceType.VALUE);
            if (value >= Long.MAX_VALUE - price * item.getAmount()) {
                value = Long.MAX_VALUE;
            }
            value += price * item.getAmount();
        }
        player.send(new SendString((bankTab == 0 ? "Bank" : "Tab " + bankTab) + " value: <col=FF5500>" + Utility.formatPrice(value) + "</col>", 60_079));
    }

    /** Closes the bank itemcontainer  */
    public void close() {
        player.attributes.set("BANK_KEY", Boolean.FALSE);

        if (player.pvpInstance) {
            player.playerAssistant.setValueIcon();
        }
    }

    /** Handles the place holder option for the contianer.  */
    public void placeHolder(int item, int slot) {
        boolean hold = placeHolder;
        placeHolder = true;
        setFiringEvents(false);
        withdraw(item, slot, Integer.MAX_VALUE);
        setFiringEvents(true);
        placeHolder = hold;
        refresh();
    }

    /** Deposits item into bank. */
    public void deposit(int slot, int amount) {
        boolean withinBank = player.interfaceManager.isInterfaceOpen(60000) || player.interfaceManager.isInterfaceOpen(4465);
        if (!withinBank) {
            System.out.println("interface: " + player.interfaceManager.getMain());
            return;
        }

        Item item = player.inventory.get(slot);
        if (item == null)
            return;

        int id = item.getId();

        if (LootingBagPlugin.isLootingBag(item) && !player.lootingBag.isEmpty()) {
            player.interfaceManager.openInventory(60000, 26700);
            return;
        }

        int invAmount = player.inventory.computeAmountForId(id);

        if (invAmount < amount) {
            amount = invAmount;
        }

        setFiringEvents(false);

        if (item.isNoted())
            id = item.getUnnotedId();

        if (!contains(id)) {
            if (size() + 1 > capacity()) {
                player.message("Your bank is full! You need to clear some items from your bank.");
                setFiringEvents(true);
                return;
            }

            changeTabAmount(bankTab, 1);
            add(id, amount);
            int from = computeIndexForId(id);
            int to = slotForTab(bankTab);
            swap(true, from, to, false);
        } else {
            Item fucking = get(computeIndexForId(id));
            if (Integer.MAX_VALUE - fucking.getAmount() < amount) {
                amount = Integer.MAX_VALUE - fucking.getAmount();
                player.send(new SendMessage("Your bank didn't have enough space to deposit all that!"));
            }
            fucking.incrementAmountBy(amount);
        }

        if (amount > 0) {
            if(item.getId() == 12791) player.runePouch.clearInterface();
            player.inventory.remove(item.getId(), amount);
        }
        setFiringEvents(true);

        refresh();
    }

    /** Withdraws item from bank. */
    public void withdraw(int itemId, int slot, int amount) {
        if (!player.interfaceManager.isInterfaceOpen(60000)) {
            return;
        }
        slot = computeIndexForId(itemId);
        if (itemId < 0) return;

        Item item = get(slot);
        if (item == null || itemId != item.getId())
            return;

        if (item.getAmount() == 0) {//Releasing place holders
            boolean hold = placeHolder;
            placeHolder = false;
            int tabSlot = computeIndexForId(item.getId());
            int tab = tabForSlot(tabSlot);
            changeTabAmount(tab, -1);
            remove(item);
            shift();
            placeHolder = hold;
            refresh();
            return;
        }

        if (item.getAmount() < amount) {
            amount = item.getAmount();
        }

        int id = item.getId();
        if (noting) {
            if (!item.isNoteable()) {
                player.send(new SendMessage("This item cannot be withdrawn as a note."));
            } else {
                id = item.getNotedId();
            }
        }

        setFiringEvents(false);
        if (!new Item(id).isStackable() && amount > player.inventory.getFreeSlots()) {
            amount = player.inventory.getFreeSlots();
        } else if (ItemDefinition.get(id).isStackable() && player.inventory.getFreeSlots() == 0) {
            if (!player.inventory.contains(id)) {
                amount = 0;
            } else if (player.inventory.computeAmountForId(id) + amount > Integer.MAX_VALUE) {
                amount = Integer.MAX_VALUE - player.inventory.computeAmountForId(id);
            }
        }

        if (amount == 0) {
            player.send(new SendMessage("You do not have enough inventory spaces to withdraw this item."));
            return;
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
            if(id == 12791) player.runePouch.refresh();
            // when an item is taken out of the bank completely, it removes one amount from the tab amounts array
            if (!contains(item.getId())) {
                int tab = tabForSlot(slot);
                changeTabAmount(tab, -1);
                shift();
            }
        }
        setFiringEvents(true);
        refresh();
    }

    /**
     * Collapses a tab and shifts other tabs one slot to the left.
     *
     * @param tab The initial tab.
     */
    public void collapse(int tab, boolean collapseAll) {
        if (!player.interfaceManager.isInterfaceOpen(60000)) {
            return;
        }
        if (tab == 0 && collapseAll) {
            Arrays.fill(tabAmounts, 0);
            tabAmounts[0] = size();
            shift();
            return;
        }

        /* Move the remaining items to the main tab. */
        int tabAmount = tabAmounts[tab];
        if (tabAmount > 0) moveTab(tab, 0);

        /* Shift the remaining tabs to the left to fill the gap. */
        recursiveCollapse(tab);
        player.send(new SendConfig(211, bankTab = 0));
    }

    private void recursiveCollapse(int tab) {
        if (tab == tabAmounts.length - 1) return;
        moveTab(tab + 1, tab);
        recursiveCollapse(tab + 1);
    }

    private void moveTab(int tab, int toTab) {
        int tabAmount = tabAmounts[tab];
        int fromSlot = slotForTab(tab);
        int toSlot = slotForTab(toTab) + 1;
        tabAmounts[tab] -= tabAmount;
        tabAmounts[toTab] += tabAmount;

        setFiringEvents(false);
        for (int i = 0; i < tabAmount; i++) {
            swap(true, fromSlot, toSlot, false);
        }
        setFiringEvents(true);
    }

    /**
     * Changes the amount of items stored in a tab.
     *
     * @param tab    The tab to modify.
     * @param amount The amount to change.
     */
    public void changeTabAmount(int tab, int amount) {
        if (tab < 0 || tab >= tabAmounts.length) {
            return;
        }

        tabAmounts[tab] += amount;
        if (tabAmounts[tab] == 0) {
            collapse(tab, false);
        }
    }

    public int depositFromNothing(Item item, int tab) {
        setFiringEvents(false);
        item = item.copy();
        int id = item.getUnnotedId();
        if (!contains(id)) {
            if (size() + 1 > capacity()) {
                return 0;
            }

            add(id, item.getAmount());
            changeTabAmount(tab, 1);
            int from = computeIndexForId(id);
            int to = slotForTab(tab);
            swap(true, from, to, false);
        } else {
            Item fucking = get(computeIndexForId(id));
            if (Integer.MAX_VALUE - fucking.getAmount() < item.getAmount()) {
                item.setAmount(Integer.MAX_VALUE - fucking.getAmount());
            }
            fucking.incrementAmountBy(item.getAmount());
        }
        setFiringEvents(true);
        return item.getAmount();
    }

    /** Handles depositing the entire inventory. */
    public void depositeInventory(boolean message) {
        int tab = bankTab;
        while (tab > 0 && tabAmounts[tab - 1] <= 0) tab--;
        Item[] items = player.inventory.getItems();
        for (int index = 0; index < items.length; index++) {
            if (items[index] == null) continue;
            Item item = items[index].copy();
            item.setAmount(depositFromNothing(item, tab));
            if(item.getId() == 12791) player.runePouch.clearInterface();
            if (item.getAmount() > 0)
                player.inventory.remove(item, index, false);
        }
        if (message) {
            player.send(new SendMessage("You deposited all your inventory items."));
        }
        refresh();
    }

    /** Handles depositing all the equipment. */
    public void depositeEquipment(boolean message) {
        int tab = bankTab;
        while (tab > 0 && tabAmounts[tab - 1] <= 0) tab--;
        Item[] items = player.equipment.getItems();
        for (int index = 0; index < items.length; index++) {
            if (items[index] == null) continue;
            Item item = items[index].copy();
            item.setAmount(depositFromNothing(item, tab));
            if (item.getAmount() > 0)
                player.equipment.remove(item, index, false);
        }
        if (message) {
            player.send(new SendMessage("You deposited all your worn equipment."));
        }
        player.equipment.login();
        refresh();
    }

    private void itemToTab(int slot, int toTab) {
        int fromTab = tabForSlot(slot);

        /* Item is already in this tab. */
        if (fromTab == toTab) return;

        /*
         * The tab to the left of the chosen tab is empty,
         * so don't create a new tab.
         */
        if (toTab > 1 && tabAmounts[toTab - 1] == 0 && tabAmounts[toTab] == 0) {
            return;
        }

        tabAmounts[toTab]++;
        tabAmounts[fromTab]--;
        int toSlot = slotForTab(toTab);

        if (tabAmounts[fromTab] == 0) {
            collapse(fromTab, false);
        }

        swap(true, slot, toSlot, false);
        refresh();
    }

    /**
     * Moves an item within the bank. <p> An opcode of 0 performs a swap
     * operation, with {@code from} being the origin slot and {@code to} being
     * the destination slot. </p> <p> An opcode of 1 performs an insert
     * operation, with {@code from} being the origin slot and {@code to} being
     * the destination slot. </p> <p> An opcode of 2 moves an item from one tab
     * to another, with {@code from} being the origin <b>slot</b> and {@code to}
     * being the destination <b>tab</b>. </p>
     *
     * @param opcode The opcode.
     * @param from   The origin slot or tab.
     * @param to     The destination slot or tab.
     */
    public void moveItem(int opcode, int from, int to) {
        if (opcode == 2) {
            itemToTab(from, to);
        } else if (opcode == 1) {
            swap(true, from, to, false);
            int fromTab = tabForSlot(from);
            int toTab = tabForSlot(to);
            if (fromTab != toTab) {
                changeTabAmount(toTab, 1);
                changeTabAmount(fromTab, -1);
                refresh();
            }
        } else {
            swap(from, to);
        }
    }

    public int tabForSlot(int slot) {
        if (slot <= -1)
            return -1;
        int passed = -1;
        for (int tab = 0; tab < tabAmounts.length; tab++) {
            if (slot <= passed + tabAmounts[tab])
                return tab;
            passed += tabAmounts[tab];
        }
        return -1;
    }

    private int slotForTab(int tab) {
        int passed = -1;
        for (int index = tab; index >= 0; index--) {
            passed += tabAmounts[index];
        }
        return passed;
    }

    @Override
    public void clear() {
        Arrays.fill(tabAmounts, 0);
        super.clear();
    }

    @Override
    public void shift() {
        int[] amounts = Arrays.copyOf(tabAmounts, tabAmounts.length);
        super.shift();
        amounts = Arrays.copyOf(amounts, amounts.length);
        System.arraycopy(amounts, 0, tabAmounts, 0, amounts.length);
    }

    @Override
    public boolean allowZero() {
        return placeHolder;
    }

    private final class BankListener extends ItemContainerAdapter {

        BankListener() {
            super(player);
        }

        @Override
        public int getWidgetId() {
            return InterfaceConstants.WITHDRAW_BANK;
        }

        @Override
        public String getCapacityExceededMsg() {
            return "Your bank is currently full!";
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
