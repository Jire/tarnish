package plugin.click.button;

import com.osroyale.game.plugin.PluginContext;
import com.osroyale.game.world.entity.mob.player.Player;
import com.osroyale.net.packet.out.SendInputMessage;
import com.osroyale.util.Utility;

public class PlayerRelationButtonPlugin extends PluginContext {

    @Override
    protected boolean onClick(Player player, int button) {
        if (button == 5068) {
            player.send(new SendInputMessage("Enter the name of the friend you want to add:", 12, input -> {
                long name = Utility.nameToLong(input);
                player.relations.addFriend(name);
            }));
            return true;
        }
        if (button == 5069) {
            player.send(new SendInputMessage("Enter the name of the friend you want to delete:", 12, input -> {
                long name = Utility.nameToLong(input);
                player.relations.deleteFriend(name);
            }));
            return true;
        }
        return false;
    }
}