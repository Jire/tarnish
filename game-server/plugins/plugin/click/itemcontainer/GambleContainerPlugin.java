package plugin.click.itemcontainer;

import com.osroyale.game.event.impl.ItemContainerContextMenuEvent;
import com.osroyale.game.plugin.PluginContext;
import com.osroyale.game.world.entity.mob.player.Player;
import com.osroyale.net.packet.out.SendInputAmount;

public class GambleContainerPlugin extends PluginContext {

    @Override
    protected boolean firstClickItemContainer(Player player, ItemContainerContextMenuEvent event) {
        final int interfaceId = event.getInterfaceId();
        final int removeSlot = event.getRemoveSlot();
        final int removeId = event.getRemoveId();

        switch(interfaceId) {
            case 44776:
                player.getGambling().deposit(player, removeId, removeSlot, 1);
                return true;
            case 44770:
                player.getGambling().withdraw(player, removeId, removeSlot, 1);
                return true;
        }

        return false;
    }

    @Override
    protected boolean secondClickItemContainer(Player player, ItemContainerContextMenuEvent event) {
        final int interfaceId = event.getInterfaceId();
        final int removeSlot = event.getRemoveSlot();
        final int removeId = event.getRemoveId();

        switch(interfaceId) {
            case 44776:
                player.getGambling().deposit(player, removeId, removeSlot, 5);
                return true;
            case 44770:
                player.getGambling().withdraw(player, removeId, removeSlot, 5);
                return true;
        }

        return false;
    }

    @Override
    protected boolean thirdClickItemContainer(Player player, ItemContainerContextMenuEvent event) {
        final int interfaceId = event.getInterfaceId();
        final int removeSlot = event.getRemoveSlot();
        final int removeId = event.getRemoveId();

        switch(interfaceId) {
            case 44776:
                player.getGambling().deposit(player, removeId, removeSlot, 10);
                return true;
            case 44770:
                player.getGambling().withdraw(player, removeId, removeSlot, 10);
                return true;
        }

        return false;
    }

    @Override
    protected boolean fourthClickItemContainer(Player player, ItemContainerContextMenuEvent event) {
        final int interfaceId = event.getInterfaceId();
        final int removeSlot = event.getRemoveSlot();
        final int removeId = event.getRemoveId();

        switch(interfaceId) {
            case 44776:
                player.send(new SendInputAmount(amount -> player.getGambling().deposit(player, removeId, removeSlot, Integer.parseInt(amount))));
                return true;
            case 44770:
                player.send(new SendInputAmount(amount -> player.getGambling().withdraw(player, removeId, removeSlot, Integer.parseInt(amount))));
                return true;
        }

        return false;
    }

    @Override
    protected boolean fifthClickItemContainer(Player player, ItemContainerContextMenuEvent event) {
        final int interfaceId = event.getInterfaceId();
        final int removeSlot = event.getRemoveSlot();
        final int removeId = event.getRemoveId();

        switch(interfaceId) {
            case 44776:
                player.getGambling().deposit(player, removeId, removeSlot, player.inventory.get(removeSlot).getAmount());
                return true;
            case 44770:
                player.getGambling().withdraw(player, removeId, removeSlot, player.getGambling().getContainer().get(removeSlot).getAmount());
                return true;
        }

        return false;
    }

}
