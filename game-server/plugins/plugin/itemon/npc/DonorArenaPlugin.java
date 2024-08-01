package plugin.itemon.npc;

import com.osroyale.content.skill.SkillRepository;
import com.osroyale.game.event.impl.ItemOnNpcEvent;
import com.osroyale.game.event.impl.NpcClickEvent;
import com.osroyale.game.plugin.PluginContext;
import com.osroyale.game.world.entity.mob.player.Player;
import com.osroyale.game.world.items.Item;

public class DonorArenaPlugin extends PluginContext {
    @Override
    protected boolean firstClickNpc(Player player, NpcClickEvent event) {
        if (event.getNpc().id != 4078) {
            return false;
        }
        player.dialogueFactory.sendNpcChat(4078, "Hello, #name!" ,"Use noted items on", "me to un-note them for you").execute();
        return true;
    }
    protected boolean itemOnNpc(Player player, ItemOnNpcEvent event) {
        if (event.getNpc().id != 4078) {
            return false;
        }
        Item item = event.getUsed();

        if (!item.isNoted()) {
            player.dialogueFactory.sendNpcChat(4078, "I do not note items, I only un-note them!").execute();
            return true;
        }

        int freeSlots = player.inventory.getFreeSlots();
        int itemAmount = player.inventory.computeAmountForId(item.getId());

        if (itemAmount > freeSlots) {
            itemAmount = freeSlots;
        }

        int finalBonesAmount = itemAmount;
        player.dialogueFactory.sendNpcChat(4078, "Are you sure you want me to un-note", "these for you?").sendOption("Proceed", () -> {
            int count = 0;
            for (int i = 0; i < finalBonesAmount; i++) {
                    count++;
                    player.inventory.remove(item.getNotedId(), 1);
                    player.inventory.add(item.getUnnotedId(), 1);
            }
            player.dialogueFactory.sendNpcChat(4078, "You have successfully noted " + count + " " + item.getName() + "!");

        }, "Nevermind", () -> player.dialogueFactory.clear());
        player.dialogueFactory.execute();
        return true;
    }
}
