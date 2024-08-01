package plugin.click.button;

import com.osroyale.content.ItemsKeptOnDeath;
import com.osroyale.game.plugin.PluginContext;
import com.osroyale.game.world.entity.mob.player.Player;

public class EquipmentButtonPlugin extends PluginContext {

    @Override
    protected boolean onClick(Player player, int button) {
        if (button == 27653) {
            player.equipment.openInterface();
            return true;
        }
        if (button == 27654) {
            ItemsKeptOnDeath.open(player);
            return true;
        }
        return false;
    }
}