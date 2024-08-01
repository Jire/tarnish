package plugin.itemon.npc;

import com.osroyale.game.event.impl.ItemOnNpcEvent;
import com.osroyale.game.event.impl.NpcClickEvent;
import com.osroyale.game.plugin.PluginContext;
import com.osroyale.game.world.entity.mob.player.Player;
import com.osroyale.game.world.items.Item;

public class CookingGlovesPlugin extends PluginContext {

    @Override
    protected boolean firstClickNpc(Player player, NpcClickEvent event) {
        if (event.getNpc().id != 4986) {
            return false;
        }

        player.dialogueFactory.sendNpcChat(4986, "Hello, #name!", "Use 100 cooked lobsters on", "me to receive a pair of cooking gloves.").execute();
        return true;
    }

    @Override
    protected boolean itemOnNpc(Player player, ItemOnNpcEvent event) {
        if (event.getNpc().id != 4986) {
            return false;
        }

        Item itemUsed = event.getUsed();

        if (itemUsed.getId() != 380) {
            player.dialogueFactory.sendNpcChat(4986, "You can only use noted lobsters on me!").execute();
            return true;
        }

        if (!itemUsed.isNoted()) {
            player.dialogueFactory.sendNpcChat(4986, "You can only use noted lobsters on me!").execute();
            return true;
        }

        player.dialogueFactory.sendNpcChat(4986, "Are you sure you want to give up", "100 lobsters for cooking gloves?").sendOption("Proceed", () -> {
            if(!player.inventory.contains(380, 100)) {
                player.dialogueFactory.sendNpcChat(4986, "You do not have 100 cooked lobsters!");
                return;
            }
            player.inventory.remove(itemUsed.getId(), 100);
            player.inventory.add(775, 1);
            player.dialogueFactory.sendNpcChat(4986, "You have successfully traded", "100 cooked lobsters for cooking gloves!");
        }, "Nevermind", () -> player.dialogueFactory.clear());

        player.dialogueFactory.execute();
        return true;
    }
}