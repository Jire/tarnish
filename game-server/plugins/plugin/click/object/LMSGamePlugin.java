package plugin.click.object;

import com.osroyale.content.lms.LMSGame;
import com.osroyale.content.lms.lobby.LMSLobby;
import com.osroyale.game.event.impl.ObjectClickEvent;
import com.osroyale.game.plugin.PluginContext;
import com.osroyale.game.world.entity.mob.player.Player;
import com.osroyale.game.world.items.Item;
import com.osroyale.game.world.position.Position;

public class LMSGamePlugin extends PluginContext {

    @Override
    protected boolean firstClickObject(Player player, ObjectClickEvent event) {
        final int objectId = event.getObject().getId();

        if(objectId == 39640) {
            if(player.getPosition().getY() == 3638)
                LMSLobby.joinLobby(player);
            else if(player.getPosition().getY() >= 3639)
                LMSLobby.leaveLobby(player, false);
            return true;
        }

        if (!LMSGame.inGameArea(player) || !LMSGame.isActivePlayer(player))
            return false;

        if (objectId == 34955) {
            /**
             * Rope
             */
            player.inventory.add(new Item(954));
            return true;
        }
        if (objectId == 29102) {
            /**
             * Rocks
             */
            if (!player.inventory.contains(954)) {
                player.message("You need a rope to get to the other side!");
                return false;
            }
            if (player.lastLMSRopeCross > System.currentTimeMillis()) {
                player.message("You cannot do this right now.");
                return false;
            }
            boolean cross = player.getPosition().getX() <= 3466;
            player.lastLMSRopeCross = System.currentTimeMillis() + 5_000;
            player.move(new Position(cross ? 3470 : 3466, cross ? 5773 : 5771));
            return true;
        }
        if (objectId == 29081) {
            /**
             * Loot crate
             */
            LMSGame.rollChest(player, -1, true);
            return true;
        }
        /**
         * Normal chests
         */
        LMSGame.rollChest(player, 20526, false);
        LMSGame.rollChest(player, 20608, false);

        return false;
    }

}
