package plugin.itemon.npc;

import com.osroyale.content.skill.SkillRepository;
import com.osroyale.game.event.impl.ItemOnNpcEvent;
import com.osroyale.game.event.impl.NpcClickEvent;
import com.osroyale.game.plugin.PluginContext;
import com.osroyale.game.world.entity.mob.player.Player;
import com.osroyale.game.world.items.Item;

public class ToolLeperchaun extends PluginContext {

    @Override
    protected boolean firstClickNpc(Player player, NpcClickEvent event) {
        if (event.getNpc().id != 0) {
            return false;
        }

        player.dialogueFactory.sendNpcChat(0, "Hello, #name!", "Any grimy/clean herbs you use on me", "I can note them for", "Just use the item on me.").execute();
        return true;
    }

    @Override
    protected boolean itemOnNpc(Player player, ItemOnNpcEvent event) {
        if (event.getNpc().id != 0) {
            return false;
        }

        Item itemUsed = event.getUsed();

        if (!SkillRepository.isHerbloreItem(itemUsed.getId())) {
            player.dialogueFactory.sendNpcChat(0, "You can only note grimy/clean herbs").execute();
            return true;
        }

        if (itemUsed.isNoted()) {
            player.dialogueFactory.sendNpcChat(0, "I do not un-note items, I only note them!").execute();
            return true;
        }

        int itemAmount = player.inventory.computeAmountForId(itemUsed.getId());
        player.dialogueFactory.sendNpcChat(0, "Are you sure you want me to note", "these for you?").sendOption("Proceed", () -> {
            player.inventory.remove(itemUsed.getId(), itemAmount);
            player.inventory.add(itemUsed.getNotedId(), itemAmount);
            player.dialogueFactory.sendNpcChat(0, "You have successfully noted " + itemAmount + " " + itemUsed.getName() + "!");
            }, "Nevermind", () -> player.dialogueFactory.clear());

        player.dialogueFactory.execute();
        return true;
    }
}