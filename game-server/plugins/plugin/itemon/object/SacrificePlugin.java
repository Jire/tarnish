package plugin.itemon.object;

import com.osroyale.content.ActivityLog;
import com.osroyale.content.dialogue.DialogueFactory;
import com.osroyale.game.event.impl.ItemOnObjectEvent;
import com.osroyale.game.plugin.PluginContext;
import com.osroyale.game.world.entity.mob.player.Player;
import com.osroyale.game.world.entity.skill.Skill;
import com.osroyale.game.world.items.Item;
import com.osroyale.game.world.object.GameObjectDefinition;
import com.osroyale.net.packet.out.SendMessage;
import com.osroyale.util.Utility;

public class SacrificePlugin extends PluginContext {

    final int[] SACRIFICED_EGGS = {5076, 5077, 5078};
    final Item[] OUTFIT_PIECES = new Item[]{new Item(20433), new Item(20436), new Item(20439), new Item(20442)};

    @Override
    protected boolean itemOnObject(Player player, ItemOnObjectEvent event) {
        final GameObjectDefinition def = event.getObject().getDefinition();
        if (def.getName() != null && !def.getName().toLowerCase().contains("shrine")) {
            return false;
        }
            final Item usedItem = event.getUsed();
            for (int item : SACRIFICED_EGGS) {
                if (usedItem.getId() == item) {
                    DialogueFactory factory = player.dialogueFactory;
                    factory.sendStatement(
                            "Are you sure you want to sacrifice <col=255>" + usedItem.getName() + "</col>?",
                            "You will have a chance at getting an evil chicken outfit piece!");
                    factory.sendOption("Yes, sacrifice " + "<col=255>" + usedItem.getName() + "</col>!", () -> {
                        player.activityLogger.add(ActivityLog.EGG_SACRIFICE);
                        player.inventory.remove(usedItem.getId());
                        player.skills.addExperience(Skill.PRAYER, 3000);
                        if(Utility.hasOneOutOf(100)) {
                            player.inventory.add(Utility.randomElement(OUTFIT_PIECES));
                        } else {
                            player.send(new SendMessage("You failed to obtain a piece! Good luck on your next try!"));
                        }
                    }, "Never-mind, Exit", factory::clear);
                    factory.execute();
                    return true;
                }
                if(!event.getUsed().matchesId(item)) {
                    player.dialogueFactory.sendStatement("You can only sacrifice evil chicken eggs here!").execute();
                    return true;

                }
            }
        return false;
        }
    }

