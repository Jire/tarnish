package plugin.click.button;

import com.osroyale.content.DropDisplay;
import com.osroyale.content.ProfileViewer;
import com.osroyale.content.achievement.AchievementWriter;
import com.osroyale.content.activity.ActivityType;
import com.osroyale.content.simulator.DropSimulator;
import com.osroyale.content.tittle.TitleManager;
import com.osroyale.content.writer.InterfaceWriter;
import com.osroyale.content.writer.impl.InformationWriter;
import com.osroyale.game.plugin.PluginContext;
import com.osroyale.game.world.entity.mob.player.Player;
import com.osroyale.net.packet.out.SendURL;

public class QuestTabButtonPlugin extends PluginContext {

    @Override
    protected boolean onClick(Player player, int button) {
        switch (button) {
            case -29408:
                AchievementWriter.write(new AchievementWriter(player));
                return true;

            case -29404:
                player.send(new SendURL("www.tarnishps.com"));
                return true;

            case 29411:
                InterfaceWriter.write(new InformationWriter(player));
                return true;

            case -30085:
                ProfileViewer.open(player, player);
                return true;

            case -30084:
                player.activityLogger.open();
                return true;

            case -30083:
                TitleManager.open(player);
                return true;

            case -30082:
                DropDisplay.open(player);
                return true;

            case -30081:
                DropSimulator.open(player);
                return true;

            case -30080:
                player.gameRecord.display(ActivityType.getFirst());
                return true;
        }
        return false;
    }
}
