package plugin;

import com.osroyale.content.dialogue.DialogueFactory;
import com.osroyale.content.store.Store;
import com.osroyale.game.event.impl.NpcClickEvent;
import com.osroyale.game.plugin.PluginContext;
import com.osroyale.game.world.entity.mob.player.Player;

public class SkillingPointPlugin extends PluginContext {

    @Override
    protected boolean firstClickNpc(Player player, NpcClickEvent event) {
        if (event.getNpc().id == 6533) {
            DialogueFactory factory = player.dialogueFactory;
            factory.sendNpcChat(6533, "Why, hello #name!", "I am the official Tarnish skilling NPC.", "How may I help you today?");

            factory.sendOption("How do I get skilling points?", () -> {
                factory.sendNpcChat(6533,
                        "Getting skilling points is easy!",
                        "All you have to do is any skilling activity",
                        "that will give you some sort of produce.",
                        "While doing those skills you will have");
                factory.sendNpcChat(6533,
                        "a 1 in 150 chance of getting 1 skill point.");
            }, "Open store", () -> {
                Store.STORES.get("The Skilling Store").open(player);
            }, "Nevermind", factory::clear);

            factory.execute();
            return true;
        }
        return false;
    }

    @Override
    protected boolean secondClickNpc(Player player, NpcClickEvent event) {
        if (event.getNpc().id == 6533) {
            Store.STORES.get("The Skilling Store").open(player);
            return true;
        }
        return false;
    }
}
