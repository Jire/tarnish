package plugin.click.button;

import com.osroyale.game.Animation;
import com.osroyale.game.plugin.PluginContext;
import com.osroyale.game.world.entity.mob.player.Player;
import com.osroyale.net.packet.out.SendMessage;

public class RestButtonPlugin extends PluginContext {

    @Override
    protected boolean onClick(Player player, int button) {
        if (button == 1059) {
            String allowed = "";
            if (player.getCombat().inCombat())
                allowed = "You can not rest while in combat.";
            if (player.playerAssistant.busy())
                allowed = "You can't do this rest now!";
            if (player.locking.locked())
                allowed = "You can't do this rest now!";
            if (!allowed.equals("")) {
                player.send(new SendMessage(allowed));
                return true;
            }
            int animation = player.right.getRestAnimation();
            player.resting = true;
            player.animate(new Animation(animation));
            player.movement.reset();
            player.send(new SendMessage("You are now resting."));
            return true;
        }
        return false;
    }
}