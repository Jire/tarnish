package plugin.click.button;

import com.osroyale.game.plugin.PluginContext;
import com.osroyale.game.world.entity.mob.player.Player;
import com.osroyale.game.world.items.Item;
import com.osroyale.game.world.items.containers.bank.VaultCurrency;
import com.osroyale.net.packet.out.*;

public class BankButtonPlugin extends PluginContext {

    @Override
    protected boolean onClick(Player player, int button) {
        if (!player.interfaceManager.hasAnyOpen(60000, 56300, 4465))
            return false;
        if (button >= -5505 && button <= -5468) {
            final int tab = (5505 + button) / 4;
            if (button % 2 == 0) {
                player.bank.bankTab = tab;
                player.send(new SendString("", 60019));
            } else {
                player.bank.collapse(tab, tab == 0);
                player.bank.refresh();
            }
            player.bank.sendValue();
            return true;
        }
        switch (button) {
            case -5462:
                player.bankPin.open();
                return true;
            case -5522:
                player.bankVault.open();
                return true;
            case -9221:
                player.send(new SendInputAmount("Enter the amount of coins you want to deposit:", 10, input -> player.bankVault.deposit(VaultCurrency.COINS, Integer.parseInt(input))));
                return true;
            case -9220:
                player.send(new SendInputAmount("Enter the amount of coins you want to withdraw:", 10, input -> player.bankVault.withdraw(VaultCurrency.COINS, Long.parseLong(input))));
                return true;
            case -9219:
                player.send(new SendInputAmount("Enter the amount of blood money you want to deposit:", 10, input -> player.bankVault.deposit(VaultCurrency.BLOOD_MONEY, Integer.parseInt(input))));
                return true;
            case -9218:
                player.send(new SendInputAmount("Enter the amount of blood money you want to withdraw:", 10, input -> player.bankVault.withdraw(VaultCurrency.BLOOD_MONEY, Long.parseLong(input))));
                return true;
            case -9217:
                player.bank.open();
                return true;
            case -5528:
                player.bank.depositeInventory(true);
                return true;
            case -5525:
                player.bank.depositeEquipment(true);
                return true;
            case -5463:
                player.bank.placeHolder = !player.bank.placeHolder;
                player.send(new SendConfig(116, player.bank.placeHolder ? 1 : 0));
                player.send(new SendTooltip((player.bank.placeHolder ? "Disable" : "Enable") + " place holders", 60073));
                return true;
            case -5464:
                int count = 0;
                boolean toggle = player.bank.placeHolder;
                player.bank.placeHolder = false;
                player.bank.setFiringEvents(false);
                for (Item item : player.bank.toArray()) {
                    if (item != null && item.getAmount() == 0) {
                        int slot = player.bank.computeIndexForId(item.getId());
                        int tab = player.bank.tabForSlot(slot);
                        player.bank.changeTabAmount(tab, -1);
                        player.bank.remove(item);
                        player.bank.shift();
                        count++;
                    }
                }
                player.bank.placeHolder = toggle;
                player.bank.setFiringEvents(true);
                player.bank.refresh();
                player.send(new SendMessage(count == 0 ? "There are no place holders available for you to release." : "You have released " + count + " place holders."));
                return true;
            case -5530:
                player.bank.inserting = !player.bank.inserting;
                player.send(new SendConfig(304, player.bank.inserting ? 1 : 0));
                player.send(new SendTooltip((player.bank.inserting ? "Enable swapping" : "Enable inserting"), 60006));
                return true;
            case -5529:
                player.bank.noting = !player.bank.noting;
                player.send(new SendConfig(115, player.bank.noting ? 1 : 0));
                player.send(new SendTooltip((player.bank.noting ? "Disable" : "Enable") + " noting", 60007));
                return true;
        }
        return false;
    }
}
