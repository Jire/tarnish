package plugin.itemon.object;

import com.osroyale.game.event.impl.ItemOnObjectEvent;
import com.osroyale.game.plugin.PluginContext;
import com.osroyale.game.world.entity.mob.player.Player;
import com.osroyale.game.world.items.Item;
import com.osroyale.game.world.object.GameObjectDefinition;

public class BankUnnotePlugin extends PluginContext {


    @Override
    protected boolean itemOnObject(Player player, ItemOnObjectEvent event) {
        final GameObjectDefinition def = event.getObject().getDefinition();
        if (def.getName() != null && !def.getName().toLowerCase().contains("bank")) {
            return false;
        }
        Item item = event.getUsed();
        if (!item.isNoted()) {
            player.dialogueFactory.sendNpcChat(2898,"I do not note items, I only un-note them!").execute();
            return true;
        }

        int freeSlots = player.inventory.getFreeSlots();
        int bonesAmount = player.inventory.computeAmountForId(item.getId());

        if (!item.isNoted()) {
            player.dialogueFactory.sendNpcChat(2898, "I do not note items, I only un-note them!").execute();
            return true;
        }
        if (bonesAmount > freeSlots) {
            bonesAmount = freeSlots;
        }

        int finalBonesAmount = bonesAmount;
        player.dialogueFactory.sendNpcChat(2898,"Would you like to un-note these?").sendOption("Proceed", () -> {
            int count = 0;
            for (int i = 0; i < finalBonesAmount; i++) {
                if(player.isPlayer()) {
                    count++;
                    player.inventory.remove(item.getId(), 1);
                    player.inventory.add(item.getId() - 1, 1);
                }else{
                    break;
                }
                if (freeSlots == 0) {
                    player.dialogueFactory.sendNpcChat(2898, "You have run out of space to do this!");
                    break;
                }
            }
            player.dialogueFactory.sendNpcChat(2898, "You have successfully un-noted " + count + " " + item.getName() + "!");

        }, "Nevermind", () -> player.dialogueFactory.clear());
        player.dialogueFactory.execute();
        return true;
    }

}
