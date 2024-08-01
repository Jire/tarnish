package plugin.click.button;

import com.osroyale.content.skill.impl.magic.teleport.TeleportType;
import com.osroyale.content.teleport.TeleportHandler;
import com.osroyale.game.plugin.PluginContext;
import com.osroyale.game.world.entity.mob.player.Player;

public class TeleportButtonPlugin extends PluginContext {

    @Override
    protected boolean onClick(Player player, int button) {
        if (button >= -7484 && button <= -7440) {
            TeleportHandler.click(player, button);
            return true;
        }
        switch (button) {
            case -7501:
                TeleportHandler.teleport(player);
                return true;
            case -7530:
                TeleportHandler.open(player, TeleportType.FAVORITES);
                return true;
            case 13079:
            case -7526:
                TeleportHandler.open(player, TeleportType.MINIGAMES);
                return true;
            case 13069:
            case -7522:
                TeleportHandler.open(player, TeleportType.SKILLING);
                return true;
            case 13053:
            case -7518:
                TeleportHandler.open(player, TeleportType.MONSTER_KILLING);
                return true;
            case 13035:
            case -7514:
                TeleportHandler.open(player, TeleportType.PLAYER_KILLING);
                return true;
            case 13061:
            case -7510:
                TeleportHandler.open(player, TeleportType.BOSS_KILLING);
                return true;
            case -7497:
            case -7496:
                TeleportHandler.favorite(player);
                return true;
        }
        return false;
    }
}
