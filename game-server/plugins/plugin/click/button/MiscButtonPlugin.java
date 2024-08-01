package plugin.click.button;

import com.osroyale.Config;
import com.osroyale.content.WellOfGoodwill;
import com.osroyale.content.skill.impl.agility.Agility;
import com.osroyale.content.skill.impl.construction.BuildableInterface;
import com.osroyale.content.skill.impl.construction.BuildableType;
import com.osroyale.content.skill.impl.magic.teleport.TeleportType;
import com.osroyale.content.teleport.TeleportHandler;
import com.osroyale.game.plugin.PluginContext;
import com.osroyale.game.world.entity.mob.player.Player;
import com.osroyale.net.packet.out.SendInputAmount;
import com.osroyale.net.packet.out.SendURL;
import plugin.itemon.RottenTomatoePlugin;

public class MiscButtonPlugin extends PluginContext {

    @Override
    protected boolean onClick(Player player, int button) {
        if (Agility.clickButton(player, button)) {
            return true;
        }
        if (player.overrides.handleButtons(button)) {
            return true;
        }
        switch (button) {
            case 31710:
                player.interfaceManager.close();
                break;
            case 32303:
            case -22534:
            case 2422:
            case 26802:
            case 28502:
            case -7534:
            case -23829:
            case -8733:
                player.interfaceManager.close(false);
                return true;
            case 21301:
                player.interfaceManager.close(false);
                player.setVisible(true);
                return true;
            case 2458:
                player.logout();
                return true;
            case 21304:
                player.send(new SendURL(Config.LATEST_ANNOUNCEMENT_THREAD));
                return true;
            case 21307:
                player.send(new SendURL(Config.LATEST_UPDATE_THREAD));
                return true;
            case 850:
                if (player.house.isInside()) {
                    BuildableInterface.open(player, BuildableType.MAIN_OBJECT);
                } else {
                    TeleportHandler.open(player, TeleportType.FAVORITES);
                }
                return true;
            case 14176:
                player.dialogueFactory.clear();
                return true;
            case 14175:
                player.playerAssistant.handleDestroyItem();
                return true;
            case 26702:
                player.lootingBag.close();
                return true;
            case -6027:
                player.mysteryBox.spin();
                return true;
            case -21823:
                player.send(new SendInputAmount("How much would you like to contribute?", 8, input -> {
                    input = input.toLowerCase();
                    input = input.replaceAll("k", "000");
                    input = input.replaceAll("m", "000000");
                    input = input.replaceAll("b", "000000000");

                    try {
                        long amount = Long.parseLong(input);

                        if (amount > Integer.MAX_VALUE) {
                            amount = Integer.MAX_VALUE;
                        }

                        WellOfGoodwill.contribute(player, (int) amount);
                    } catch (Exception ex) {
                        player.message("This amount is invalid.");
                    }
                }));
                return true;
            case -13725:
                if (player.managing != null) {
                    player.managing.ifPresent(other -> RottenTomatoePlugin.open(player, other));
                }
                return true;
        }
        return false;
    }
}
