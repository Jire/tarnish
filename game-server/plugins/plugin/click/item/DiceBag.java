package plugin.click.item;


import com.osroyale.game.Animation;
import com.osroyale.game.Graphic;
import com.osroyale.game.event.impl.ItemClickEvent;
import com.osroyale.game.plugin.PluginContext;
import com.osroyale.game.world.World;
import com.osroyale.game.world.entity.mob.player.Player;
import com.osroyale.game.world.position.Area;
import com.osroyale.game.world.position.Position;
import com.osroyale.net.packet.out.SendMessage;
import com.osroyale.util.Utility;

import java.util.concurrent.TimeUnit;

/**
 * Handles the operating the dicing bag.
 *
 * @author Daniel.
 */
public class DiceBag extends PluginContext{

    /** The dice animation. */
    private static final Animation ANIMATION = new Animation(7219);

    @Override
    protected boolean firstClickItem(Player player, ItemClickEvent event) {
        if (event.getItem().getId() != 15098) {
            return false;
        }

        roll(player, false);
        return true;
    }

    @Override
    protected boolean secondClickItem(Player player, ItemClickEvent event) {
        if (event.getItem().getId() != 15098) {
            return false;
        }

        roll(player, true);
        return true;
    }

    /** Handles rolling the dice bag. */
    public void roll(Player player, boolean clan) {
        if (player.getCombat().inCombat()) {
            player.send(new SendMessage("You can't be in combat to do this!"));
            return;
        }
        if (Area.inWilderness(player)) {
            player.send(new SendMessage("You can't be in the wilderness to do this!"));
            return;
        }
        if (clan && player.clan == null) {
            player.send(new SendMessage("You need to be in a clan chat channel to do this!"));
            return;
        }
        if (!player.diceDelay.elapsed(3, TimeUnit.SECONDS)) {
            player.send(new SendMessage("You can't do this so quickly!"));
            return;
        }

        int random = Utility.random(100);

        if (clan) {
            if (player.clanChannel == null) {
                player.message("You must be in a clan to do this!");
                return;
            }

            player.animate(ANIMATION);
            player.clanChannel.message(player.getName() + " has rolled <col=ff0000>" + random + "</col> on the percentile dice!");
            return;
        }

        player.animate(ANIMATION);
        player.diceDelay.reset();
        player.send(new SendMessage("You have rolled <col=ff0000>" + random + "</col> on the percentile dice!"));
    }
}

