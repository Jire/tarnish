package plugin.click.button;

import com.osroyale.Config;
import com.osroyale.game.plugin.PluginContext;
import com.osroyale.game.world.entity.mob.player.Player;
import com.osroyale.net.packet.out.SendConfig;
import com.osroyale.net.packet.out.SendExpCounter;
import com.osroyale.net.packet.out.SendExpCounterSetting;
import com.osroyale.net.packet.out.SendMessage;

public class ExperienceCounterButtonPlugin extends PluginContext {

    @Override
    protected boolean onClick(Player player, int button) {
        switch (button) {
            case 475:
                player.send(new SendExpCounterSetting(0, 0));
                player.interfaceManager.open(56500);
                return true;
            case 476:
                player.dialogueFactory.sendStatement("Are you sure you want to do this?", "This action can not be undone!").sendOption("Reset EXP counter", () -> {
                    player.dialogueFactory.onAction(() -> {
                        player.send(new SendExpCounter((int) (player.skills.experienceCounter = 0)));
                        player.send(new SendMessage("Your experience has been reset."));
                        player.dialogueFactory.clear();
                    });
                }, "Nevermind", () -> player.dialogueFactory.onAction(() -> player.dialogueFactory.clear())).execute();
                return true;
            case 477:
                player.dialogueFactory.sendOption("Lock experience", () -> {
                    player.settings.lockExperience = true;
                    player.dialogueFactory.sendStatement("You will now not gain any experience.").execute();
                }, "Unlock experience", () -> {
                    player.settings.lockExperience = false;
                    player.dialogueFactory.sendStatement("You will now gain experience.").execute();
                }).execute();
                return true;
            case -9029:
            case -9026:
                player.send(new SendExpCounterSetting(2, 0));
                return true;
            case -9028:
            case -9025:
                player.send(new SendExpCounterSetting(2, 1));
                return true;
            case -9027:
            case -9024:
                player.send(new SendExpCounterSetting(2, 2));
                return true;
            case -9022:
            case -9019:
                player.send(new SendExpCounterSetting(3, 0));
                return true;
            case -9021:
            case -9018:
                player.send(new SendExpCounterSetting(3, 1));
                return true;
            case -9020:
            case -9017:
                player.send(new SendExpCounterSetting(3, 2));
                return true;
            case -8988:
            case -8986:
                player.send(new SendExpCounterSetting(5, 0));
                return true;
            case -8987:
            case -8985:
                player.send(new SendExpCounterSetting(5, 1));
                return true;
            case -9015:
            case -9009:
                player.send(new SendConfig(772, 0));
                player.send(new SendExpCounterSetting(1, 0xFFFFFF));
                return true;
            case -9014:
            case -9008:
                player.send(new SendConfig(772, 1));
                player.send(new SendExpCounterSetting(1, 0x00FFFF));
                return true;
            case -9013:
            case -9007:
                player.send(new SendConfig(772, 2));
                player.send(new SendExpCounterSetting(1, 0xCD7EF2));
                return true;
            case -9012:
            case -9006:
                player.send(new SendConfig(772, 3));
                player.send(new SendExpCounterSetting(1, 0x3BF576));
                return true;
            case -9011:
            case -9005:
                player.send(new SendConfig(772, 4));
                player.send(new SendExpCounterSetting(1, 0xFC7312));
                return true;
            case -9010:
            case -9004:
                player.send(new SendConfig(772, 5));
                player.send(new SendExpCounterSetting(1, 0xDE2352));
                return true;
            case -8998:
            case -8996:
                player.settings.lockExperience = true;
                player.send(new SendConfig(775, 0));
                player.send(new SendMessage("Your experience is now locked."));
                return true;
            case -8997:
            case -8995:
                player.settings.lockExperience = false;
                player.send(new SendConfig(775, 1));
                player.send(new SendMessage("Your experience is now un-locked."));
                return true;
            case -8993:
            case -8991:
                player.experienceRate = Config.COMBAT_MODIFICATION;
                player.send(new SendConfig(776, 0));
                player.send(new SendMessage("Your experience rate is now normal (" + Config.COMBAT_MODIFICATION + "x)."));
                return true;
            case -8992:
            case -8990:
                player.experienceRate = 1;
                player.send(new SendConfig(776, 1));
                player.send(new SendMessage("Your experience is now runescape (4x)."));
                return true;
        }
        return false;
    }
}
