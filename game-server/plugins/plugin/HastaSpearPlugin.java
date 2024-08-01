package plugin;

import com.osroyale.content.dialogue.DialogueFactory;
import com.osroyale.content.dialogue.Expression;
import com.osroyale.game.event.impl.NpcClickEvent;
import com.osroyale.game.plugin.PluginContext;
import com.osroyale.game.world.entity.mob.player.Player;
import com.osroyale.net.packet.out.SendMessage;
import com.osroyale.util.Items;
import com.osroyale.util.Utility;

public class HastaSpearPlugin extends PluginContext {

    private static final int COST = 1_000_000;

    @Override
    protected boolean firstClickNpc(Player player, NpcClickEvent event) {
        if(event.getNpc().id != 2914) return false;

        DialogueFactory factory = player.dialogueFactory;
        factory.sendNpcChat(2914, "Welcome back #name!", "I exchange hasta's and spears", "for only " + Utility.formatDigits(COST) + "gp.");

        factory.sendOption("Convert Zamorakian hasta to spear", () -> {
            if (player.inventory.contains(Items.ZAMORAKIAN_HASTA) && player.inventory.contains(Items.COINS, COST)) {
                player.inventory.remove(Items.ZAMORAKIAN_HASTA, 1);
                player.inventory.remove(Items.COINS, COST);
                player.inventory.add(Items.ZAMORAKIAN_SPEAR, 1);
            } else
                player.send(new SendMessage("You do not have the required items to do this."));
        }, "Convert Zamorakian spear to hasta", () -> {
            if (player.inventory.contains(Items.ZAMORAKIAN_SPEAR) && player.inventory.contains(Items.COINS, COST)) {
                player.inventory.remove(Items.COINS, COST);
                player.inventory.remove(Items.ZAMORAKIAN_SPEAR, 1);
                player.inventory.add(Items.ZAMORAKIAN_HASTA, 1);
            } else
                player.send(new SendMessage("You do not have the required items to do this."));
        }, "Nevermind", () -> {
            factory.sendNpcChat(2914, Expression.HAPPY, "Hope to see you soon, #name!");
        });

        factory.execute();
        return true;
    }
}