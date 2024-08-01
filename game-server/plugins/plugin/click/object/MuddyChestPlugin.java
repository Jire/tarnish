package plugin.click.object;

import com.osroyale.content.MuddyChest;
import com.osroyale.game.action.impl.ChestAction;
import com.osroyale.game.event.impl.ItemOnObjectEvent;
import com.osroyale.game.event.impl.ObjectClickEvent;
import com.osroyale.game.plugin.PluginContext;
import com.osroyale.game.world.entity.mob.player.Player;
import com.osroyale.net.packet.out.SendMessage;

public class MuddyChestPlugin extends PluginContext {


    private int key = 993;
    private int chest = 170;
    @Override
    protected boolean firstClickObject(Player player, ObjectClickEvent event) {
        if (event.getObject().getId() != chest) {
            return false;
        }

        if (!player.inventory.contains(key)) {
            player.dialogueFactory.sendItem("Sinister Key", "You need a Sinister key to enter this chest!", key);
            player.send(new SendMessage("You need a Sinister key to enter this chest!"));
            return true;
        }

        if (player.inventory.remaining() < 3) {
            player.send(new SendMessage("You need at lest 3 free inventory spaces to enter the chest."));
            return true;
        }

        player.action.execute(new ChestAction(player, key, MuddyChest.getReward()), true);
        return true;
    }

    @Override
    protected boolean itemOnObject(Player player, ItemOnObjectEvent event) {
        if (event.getUsed().getId() == key && event.getObject().getId() == chest) {
            if (!player.inventory.contains(key)) {
                player.dialogueFactory.sendItem("Sinister Key", "You need a Sinister key to enter this chest!", key);
                player.send(new SendMessage("You need a Sinister key to enter this chest!"));
                return true;
            }

            if (player.inventory.remaining() < 3) {
                player.send(new SendMessage("You need at least 3 free inventory spaces to enter the chest."));
                return true;
            }

            player.action.execute(new ChestAction(player, key, MuddyChest.getReward()), true);
            return true;
        }

        return false;
    }

}
