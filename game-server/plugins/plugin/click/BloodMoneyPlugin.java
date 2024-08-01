package plugin.click;

import com.osroyale.content.activity.impl.barrows.BarrowsUtility;
import com.osroyale.content.bloodmoney.BloodMoneyChest;
import com.osroyale.game.action.impl.ChestAction;
import com.osroyale.game.event.impl.ObjectClickEvent;
import com.osroyale.game.plugin.PluginContext;
import com.osroyale.game.world.entity.mob.player.Player;
import com.osroyale.game.world.items.Item;
import com.osroyale.game.world.position.Area;
import com.osroyale.net.packet.out.SendURL;
import com.osroyale.util.Utility;

/**
 * Created by Daniel on 2018-02-03.
 */
public class BloodMoneyPlugin extends PluginContext {

    @Override
    protected boolean firstClickObject(Player player, ObjectClickEvent event) {
        if (event.getObject().getId() == 27290) {
            if (BloodMoneyChest.active && Area.inWilderness(player)) {
                BloodMoneyChest.open(player);
                return true;
            }

            if (player.inventory.contains(20608)) {
                Item bloodMoney = new Item(13307, Utility.random(5000, 8000));
                Item barrowPiece = new Item(Utility.randomElement(BarrowsUtility.BARROWS));
                player.action.execute(new ChestAction(player, 20608, bloodMoney, barrowPiece));
                return true;
            }

            player.dialogueFactory.sendItem("Blood Money Key", "You must have a Blood Money Key to open this chest!", 20608).execute();
            return true;
        }

        return false;
    }

    @Override
    protected boolean secondClickObject(Player player, ObjectClickEvent event) {
        if (event.getObject().getId() == 27290) {
            player.dialogueFactory.sendStatement(BloodMoneyChest.getInformation());
            player.dialogueFactory.execute();
            return true;
        }

        return false;
    }

    @Override
    protected boolean thirdClickObject(Player player, ObjectClickEvent event) {
        if (event.getObject().getId() == 27290) {
            player.send(new SendURL("https://www.tarnishps.com/community/index.php?/topic/792-blood-money-chest-guide/"));
            return true;
        }

        return false;
    }
}
