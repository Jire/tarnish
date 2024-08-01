package plugin.click.object;

import com.osroyale.game.Graphic;
import com.osroyale.game.event.impl.ObjectClickEvent;
import com.osroyale.game.plugin.PluginContext;
import com.osroyale.game.world.World;
import com.osroyale.game.world.entity.mob.player.Player;
import com.osroyale.game.world.items.Item;
import com.osroyale.net.discord.DiscordPlugin;
import com.osroyale.util.Utility;

/**
 * Created by Daniel on 2018-02-06.
 */
public class FountainOfRunePlugin extends PluginContext {

    private static final int CHARGED_GLORY = 11978;
    public static final int ETERNAL_GLORY = 19707;

    private static final int[] AMULET_OF_GLORY = {
            1704, 1706, 1708, 1710, 1712, 11976
    };

    @Override
    protected boolean firstClickObject(Player player, ObjectClickEvent event) {
        if (event.getObject().getId() != 26782) {
            return false;
        }

        int count = 0;

        for (Item item : player.inventory) {
            if (item == null)
                continue;

            //AMULET OF GLORY
            for (int glory : AMULET_OF_GLORY) {
                if (item.getId() != glory)
                    continue;

                boolean eternal = Utility.random(25_000) == 0;

                if (eternal) {
                    player.message("<col=FF0000>You found an amulet of eternal glory!");
                    World.sendMessage("<col=BA383E>Tarnish: <col=" + player.right.getColor() + ">" + player.getName() + " </col>has just found an <col=BA383E>amulet of eternal glory </col>from the <col=BA383E>fountain of rune</col>!");
                    DiscordPlugin.sendSimpleMessage(player.getName() + " has just found an amulet of eternal glory from the fountain of rune!");
                }

                player.inventory.replace(item, new Item(eternal ? ETERNAL_GLORY : CHARGED_GLORY), false);
                count++;
            }
        }

        if (count == 0) {
            player.dialogueFactory.sendStatement("You have no items that can be charged!").execute();
            return true;
        }

        player.animate(722);
        player.graphic(new Graphic(113, true));
        player.inventory.refresh();
        player.dialogueFactory.sendStatement(count + " Items were successfully re-charged!").execute();
        return true;
    }
}
