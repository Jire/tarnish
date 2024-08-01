package plugin.click.item;

import com.osroyale.content.dialogue.DialogueFactory;
import com.osroyale.content.skill.impl.magic.teleport.Teleportation;
import com.osroyale.content.skill.impl.slayer.Slayer;
import com.osroyale.content.skill.impl.slayer.SlayerTask;
import com.osroyale.game.event.impl.ItemClickEvent;
import com.osroyale.game.plugin.PluginContext;
import com.osroyale.game.world.entity.mob.player.Player;
import com.osroyale.game.world.entity.mob.player.PlayerRight;
import com.osroyale.game.world.position.Area;
import com.osroyale.net.packet.out.SendMessage;

public class EnchantedGemPlugin extends PluginContext {

    @Override
    protected boolean firstClickItem(Player player, ItemClickEvent event) {
        if (event.getItem().getId() != 4155) {
           return false;
        }

        final Slayer slayer = player.slayer;
        final SlayerTask task = slayer.getTask();
        player.send(new SendMessage(task == null ? "You currently don't have a task, visit Nieve in edgeville to be assigned one."
                : String.format("You're assigned to kill %s; only %d more to go.", task.getName(), slayer.getAmount())));
        return true;
    }

    @Override
    protected boolean secondClickItem(Player player, ItemClickEvent event) {
        if (event.getItem().getId() != 4155) {
            return false;
        }

        final Slayer slayer = player.slayer;
        final SlayerTask task = slayer.getTask();
        if (task == null) {
            player.send(new SendMessage("You currently don't have a task to teleport to."));
            return true;
        }

        if (!PlayerRight.isKing(player) && !player.dailySlayerTaskTeleport.canUse(player)) {
            return true;
        }

        String teleportOption = "Teleport to Task";
        if (Area.inWilderness(task.getPosition())) {
            teleportOption += " <col=ff0000>(in wilderness)";
        }

        final DialogueFactory factory = player.dialogueFactory;
        factory.sendOption(teleportOption, () -> player.dialogueFactory.onAction(() -> taskLocation(player, task, () -> {
                            if (!PlayerRight.isKing(player)) {
                                player.dailySlayerTaskTeleport.use();
                                player.send(new SendMessage("Task teleports for today left: <col=ff0000>" + player.dailySlayerTaskTeleport.remainingUses(player) + "."));
                            }
                        })),
                "Cancel", () -> player.dialogueFactory.onAction(factory::clear))
                .execute();
        return true;
    }

    private void taskLocation(Player player, SlayerTask task, Runnable action) {
        Teleportation.teleport(player, task.getPosition(), 20, action);
    }
}
