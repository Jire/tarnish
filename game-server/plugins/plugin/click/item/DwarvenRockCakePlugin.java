package plugin.click.item;

import com.osroyale.game.event.impl.ItemClickEvent;
import com.osroyale.game.plugin.PluginContext;
import com.osroyale.game.world.entity.combat.hit.Hit;
import com.osroyale.game.world.entity.mob.player.Player;
import com.osroyale.game.world.position.Area;

import java.util.concurrent.TimeUnit;

public class DwarvenRockCakePlugin extends PluginContext {

    @Override
    protected boolean firstClickItem(Player player, ItemClickEvent event) {
        if (event.getItem().getId() == 7509) {
            if (!player.itemDelay.elapsed(599, TimeUnit.MILLISECONDS))
                return true;

            if (player.getCombat().inCombat()) {
                player.message("You can not eat this while in combat!");
                return true;
            }

            if (Area.inWilderness(player)) {
                player.message("You better not eat this while in the wilderness!");
                return true;
            }

            int health = player.getCurrentHealth();
            int damage = health - 1;

            if (damage <= 0) {
                player.message("You better not eat that!");
                return true;
            }

            player.speak("Ouch!");
            player.damage(new Hit(player.getCurrentHealth() - 1));
            player.itemDelay.reset();
            return true;
        }
        return false;
    }
}
