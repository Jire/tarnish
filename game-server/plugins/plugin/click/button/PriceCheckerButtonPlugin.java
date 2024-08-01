package plugin.click.button;

import com.osroyale.content.clanchannel.channel.ClanChannelHandler;
import com.osroyale.game.plugin.PluginContext;
import com.osroyale.game.world.entity.mob.player.Player;
import com.osroyale.game.world.items.containers.pricechecker.PriceType;
import com.osroyale.net.packet.out.SendInputMessage;
import com.osroyale.net.packet.out.SendMessage;

public class PriceCheckerButtonPlugin extends PluginContext {

    @Override
    protected boolean onClick(Player player, int button) {
        switch (button) {
            case 27651:
                player.priceChecker.open();
                return true;
            case -17031:
                player.priceChecker.depositAll();
                return true;
            case -16958:
                player.priceChecker.withdrawAll();
                return true;
            case -17034:
                player.priceChecker.close();
                return true;
            case -16952:
                player.priceChecker.setValue(PriceType.VALUE);
                return true;
            case -16951:
                player.priceChecker.setValue(PriceType.HIGH_ALCH_VALUE);
                return true;
            case -17028:
                player.send(new SendInputMessage("Enter the item you would like to price check:", 20, input -> player.priceChecker.search(input.toLowerCase())));
                return true;
        }
        return false;
    }
}
