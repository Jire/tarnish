package plugin.itemon.npc;

import com.osroyale.content.skill.SkillRepository;
import com.osroyale.game.event.impl.ItemOnNpcEvent;
import com.osroyale.game.event.impl.NpcClickEvent;
import com.osroyale.game.plugin.PluginContext;
import com.osroyale.game.world.entity.mob.player.Player;
import com.osroyale.game.world.items.Item;

public class BonesPlugin extends PluginContext {
    @Override
    protected boolean firstClickNpc(Player player, NpcClickEvent event) {
        if (event.getNpc().id != 7995) {
            return false;
        }
        player.dialogueFactory.sendNpcChat(7995, "Hello, #name!" ,"Use bones on me for", "me to un-note them for you").execute();
        return true;
    }
    protected boolean itemOnNpc(Player player, ItemOnNpcEvent event) {
        if (event.getNpc().id != 7995) {
            return false;
        }
        Item item = event.getUsed();
        if (!SkillRepository.isBones(item.getId())) {
            player.dialogueFactory.sendNpcChat(7995, "You can only un-note bones with me").execute();
            return true;
        }

        if (!item.isNoted()) {
            player.dialogueFactory.sendNpcChat(7995, "I do not note items, I only un-note them!").execute();
            return true;
        }

        int freeSlots = player.inventory.getFreeSlots();
        int cost = 1000;
        int bonesAmount = player.inventory.computeAmountForId(item.getId());

        if (!item.isNoted()) {
            player.dialogueFactory.sendNpcChat(7995, "I do not note items, I only un-note them!").execute();
            return true;
        }
        if (bonesAmount > freeSlots) {
            bonesAmount = freeSlots;
        }

        int finalBonesAmount = bonesAmount;
        player.dialogueFactory.sendNpcChat(7995, "Are you sure you want me to un-note", "these for you? It will cost you", "1,000gp per bone").sendOption("Proceed", () -> {
           int count = 0;
            for (int i = 0; i < finalBonesAmount; i++) {
               if(player.inventory.contains(995, cost)) {
                   count++;
                   player.inventory.remove(995, cost);
                   player.inventory.remove(item.getId(), 1);
                   player.inventory.add(item.getId() - 1, 1);
               }else{
                   player.dialogueFactory.sendNpcChat(7995, "You have run out of coins to do this!");
                   break;
               }
           }
            player.dialogueFactory.sendNpcChat(7995, "You have successfully noted " + count + " " + item.getName() + "!");

        }, "Nevermind", () -> player.dialogueFactory.clear());
        player.dialogueFactory.execute();
        return true;
    }
}
