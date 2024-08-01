package plugin.click.button;

import com.osroyale.content.Obelisks;
import com.osroyale.game.plugin.PluginContext;
import com.osroyale.game.world.entity.mob.player.Player;

public class ObeliskButtonPlugin extends PluginContext {

    @Override
    protected boolean onClick(Player player, int button) {
        int obelisk = player.attributes.get("OBELISK", Integer.class);
        if (obelisk != -1) {
            player.interfaceManager.close();
            switch (button) {
                case -14524:
                    Obelisks.get().activate(player, obelisk, Obelisks.ObeliskData.LEVEL_44);
                  //  player.interfaceManager.close();
                    return true;
                case -14523:
                    Obelisks.get().activate(player, obelisk, Obelisks.ObeliskData.LEVEL_27);
                  //  player.interfaceManager.close();
                    return true;
                case -14522:
                    Obelisks.get().activate(player, obelisk, Obelisks.ObeliskData.LEVEL_35);
                 //   player.interfaceManager.close();
                    return true;
                case -14521:
                    Obelisks.get().activate(player, obelisk, Obelisks.ObeliskData.LEVEL_13);
                 //   player.interfaceManager.close();
                    return true;
                case -14520:
                    Obelisks.get().activate(player, obelisk, Obelisks.ObeliskData.LEVEL_19);
                 //   player.interfaceManager.close();
                    return true;
                case -14519:
                    Obelisks.get().activate(player, obelisk, Obelisks.ObeliskData.LEVEL_50);
                //    player.interfaceManager.close();
                    return true;
            }
        }
        return false;
    }
}
