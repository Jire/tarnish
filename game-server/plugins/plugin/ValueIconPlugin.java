package plugin;

import com.osroyale.game.event.impl.DropItemEvent;
import com.osroyale.game.event.impl.MovementEvent;
import com.osroyale.game.event.impl.PickupItemEvent;
import com.osroyale.game.plugin.PluginContext;
import com.osroyale.game.world.entity.mob.UpdateFlag;
import com.osroyale.game.world.entity.mob.player.Player;
import com.osroyale.game.world.items.Item;
import com.osroyale.game.world.position.Area;
import com.osroyale.game.world.position.Position;

public class ValueIconPlugin extends PluginContext {

    @Override
    protected boolean onMovement(Player player, MovementEvent event) {
        if (player.valueIcon != -1 && (!Area.inWilderness(player) && !player.pvpInstance)) {
            player.valueIcon = -1;
            player.updateFlags.add(UpdateFlag.APPEARANCE);
            return false; // if this is true any other plugins that use this method wont work
        }

        if (player.valueIcon == -1 && (Area.inWilderness(player) || player.pvpInstance)) {
            player.valueIcon = player.playerAssistant.getValueIcon(player).getCode();
            player.updateFlags.add(UpdateFlag.APPEARANCE);
            return false; // if this is true any other plugins that use this method wont work
        }
        return false;
    }

    @Override
    protected boolean onPickupItem(Player player, PickupItemEvent event) {
        final Item item = event.getItem();

        if (item.getValue() * item.getAmount() <= 10_000) {
            return false; // if this is true any other plugins that use this method wont work
        }

        final Position position = event.getPosition();

        if (Area.inWilderness(position) || player.pvpInstance) {
            player.playerAssistant.setValueIcon();
        }

        return false;
    }

    @Override
    protected boolean onDropItem(Player player, DropItemEvent event) {
        final Item item = event.getItem();

        if (item.getValue() * item.getAmount() <= 10_000) {
            return false; // if this is true any other plugins that use this method wont work
        }

        final Position position = event.getPosition();

        if (Area.inWilderness(position) || player.pvpInstance) {
            player.playerAssistant.setValueIcon();
        }
        return false;
    }
}
