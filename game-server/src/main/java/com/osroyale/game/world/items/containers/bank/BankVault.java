package com.osroyale.game.world.items.containers.bank;

import com.osroyale.game.world.entity.mob.player.Player;
import com.osroyale.game.world.entity.mob.player.PlayerRight;
import com.osroyale.game.world.items.Item;
import com.osroyale.net.packet.out.SendItemOnInterface;
import com.osroyale.net.packet.out.SendMessage;
import com.osroyale.net.packet.out.SendString;
import com.osroyale.util.MessageColor;
import com.osroyale.util.Utility;

import static com.osroyale.game.world.items.containers.bank.VaultCurrency.BLOOD_MONEY;
import static com.osroyale.game.world.items.containers.bank.VaultCurrency.COINS;

/**
 * Handles the bank vault system.
 *
 * @author Daniel
 * @since 12-1-2017.
 */
public class BankVault {

    /** The player instance. */
    private final Player player;

    public long coinsContainer;
    public long bloodMoneyContainer;


    /** Constructs a new <code>BankVault</code>. */
    public BankVault(Player player) {
        this.player = player;
    }

    public void open() {
        long coins = getContainer(COINS);
        long bloodMoney = getContainer(BLOOD_MONEY);
        player.send(new SendString(Utility.formatDigits(coins), 56311));
        player.send(new SendString(Utility.formatDigits(bloodMoney), 56312));
        player.send(new SendItemOnInterface(56314, new Item(995, coins), new Item(13307, bloodMoney)));
        player.interfaceManager.open(56300);
    }

    private long getContainer(VaultCurrency currency) {
        return currency == COINS ? coinsContainer : bloodMoneyContainer;
    }

    /** Sends message to player about their container value. */
    public void value(VaultCurrency currency) {
        long container = getContainer(currency);
        player.send(new SendMessage("You currently have " + Utility.formatDigits(container) + " " + currency.name + " stored inside your bank vault.", MessageColor.DARK_RED));
    }

    /** Checks if player's bank container contains a certain amount. */
    public boolean contains(VaultCurrency currency, int amount) {
        return getContainer(currency) >= amount;
    }

    /** Adds an amount into the player's bank vault with no checks. Careful how you use this! */
    public boolean add(VaultCurrency currency, long amount) {
        return add(currency, amount, false);
    }

    /** Adds an amount into the player's bank vault with no checks. Careful how you use this! */
    public boolean add(VaultCurrency currency, long amount, boolean message) {
        long container = getContainer(currency);

        if (player.right == PlayerRight.ULTIMATE_IRONMAN) {
            player.inventory.addOrDrop(new Item(currency.id, (int) amount));
            return true;
        }

        if (container + amount >= Long.MAX_VALUE) {
            return false;
        }

        if (currency == COINS) {
            coinsContainer += amount;
        } else if (currency == BLOOD_MONEY) {
            bloodMoneyContainer += amount;
        }

        if (message) {
            player.send(new SendMessage(Utility.formatDigits(amount) + " " + currency.name + " have been added into your bank vault.", MessageColor.DARK_BLUE));
        }
        return true;
    }

    /** Removes a certain amount from the player's bank vault container. */
    public void remove(VaultCurrency currency, int amount) {
        if (currency == COINS) {
            coinsContainer -= amount;
        } else if (currency == BLOOD_MONEY) {
            bloodMoneyContainer -= amount;
        }
    }

    /** Deposits an an amount into the player's bank vaut. */
    public void deposit(VaultCurrency currency, int amount) {
        long container = getContainer(currency);

        if (Long.MAX_VALUE == container) {
            player.send(new SendMessage("Your vault is currently full and can no longer hold any more " + currency.name + "!", MessageColor.RED));
            return;
        }

        if (Long.MAX_VALUE - container < amount) {
            amount = (int) (Long.MAX_VALUE - container);
        }

        int invAmount = player.inventory.computeAmountForId(currency.id);
        int bankAmount = 0;

        if (invAmount < amount) {
            bankAmount = player.bank.computeAmountForId(currency.id);
            if (invAmount + bankAmount <= 0) {
                player.send(new SendMessage("You don't have any " + currency.name + " you noob!", MessageColor.RED));
                return;
            }
        }

        if (invAmount > amount) {
            invAmount = amount;
        }

        if (bankAmount > amount - invAmount) {
            bankAmount = amount - invAmount;
        }

        amount = invAmount + bankAmount;

        if (invAmount > 0)
            player.inventory.remove(currency.id, invAmount);
        if (bankAmount > 0)
            player.bank.remove(currency.id, bankAmount);

        add(currency, amount);
        player.bank.refresh();
        player.send(new SendMessage("You now have " + Utility.formatDigits(container) + " " + currency.name + " stored inside your bank vault.", MessageColor.DARK_RED));

        if (player.interfaceManager.isInterfaceOpen(56300)) {
            long coins = getContainer(COINS);
            long bloodMoney = getContainer(BLOOD_MONEY);
            player.send(new SendString(Utility.formatDigits(coins), 56311));
            player.send(new SendString(Utility.formatDigits(bloodMoney), 56312));
            player.send(new SendItemOnInterface(56314, new Item(995,  coins), new Item(13307, bloodMoney)));
        }
    }

    /** Withdraws a certain amount into the player's bank vault container. */
    public void withdraw(VaultCurrency currency, long amount) {
        long container = getContainer(currency);
        if (amount < 0) {
            return;
        }
        if (container == 0) {
            player.send(new SendMessage("Your bank vault is currently empty.", MessageColor.RED));
            return;
        }
        if (amount > Integer.MAX_VALUE) {
            player.send(new SendMessage("You can not hold more than " + Utility.formatPrice(Integer.MAX_VALUE) + " " + currency.name + " at a time.", MessageColor.RED));
            return;
        }
        if (amount > container) {
            amount = container;
        }
        int contain = player.inventory.computeAmountForId(currency.id);
        if (contain + amount > Integer.MAX_VALUE) {
            amount = contain - amount;
        }
        if (player.inventory.add(new Item(currency.id, (int) amount))) {
            remove(currency, (int) amount);
            player.send(new SendMessage("You have successfully withdrawn " + Utility.formatDigits(amount) + " " + currency.name + "."));
            player.bank.refresh();
        }
        if (player.interfaceManager.isInterfaceOpen(56300)) {
            long coins = getContainer(COINS);
            long bloodMoney = getContainer(BLOOD_MONEY);
            player.send(new SendString(Utility.formatDigits(coins), 56311));
            player.send(new SendString(Utility.formatDigits(bloodMoney), 56312));
            player.send(new SendItemOnInterface(56314, new Item(995, (int) coins), new Item(13307, (int) bloodMoney)));
        }
    }
}
