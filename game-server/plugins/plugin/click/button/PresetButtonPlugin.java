package plugin.click.button;

import com.osroyale.game.plugin.PluginContext;
import com.osroyale.game.world.entity.mob.player.Player;
import com.osroyale.net.packet.out.SendMessage;
import com.osroyale.net.packet.out.SendURL;

public class PresetButtonPlugin extends PluginContext {

    @Override
    protected boolean onClick(Player player, int button) {
        switch (button) {
            case -8511:
            case -8530:
                player.presetManager.openSettings();
                return true;
            case -8490:
                player.presetManager.open(0);
                return true;
            case -8486:
                player.presetManager.open(1);
                return true;
            case -8482:
                player.presetManager.open(2);
                return true;
            case -8478:
                player.presetManager.open(3);
                return true;
            case -8474:
                player.presetManager.open(4);
                return true;
            case -8470:
                player.presetManager.open(5);
                return true;
            case -8466:
                player.presetManager.open(6);
                return true;
            case -8462:
                player.presetManager.open(7);
                return true;
            case -8458:
                player.presetManager.open(8);
                return true;
            case -8454:
                player.presetManager.open(9);
                return true;
            case -8501:
                player.presetManager.activate();
                player.interfaceManager.close();
                return true;
            case -8507:
                player.presetManager.upload();
                return true;
            case -8504:
                player.presetManager.delete();
                return true;
            case -8498:
                player.send(new SendMessage("Please wait as the preset gear guide loads..."));
                player.send(new SendURL("http://www.tarnishps.com/index.php?/topic/8-preloading-gear-guide/"));
                return true;
        }
        return false;
    }
}
