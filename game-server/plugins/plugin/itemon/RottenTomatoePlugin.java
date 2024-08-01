package plugin.itemon;

import com.osroyale.content.dialogue.DialogueFactory;
import com.osroyale.content.staff.PlayerManagement;
import com.osroyale.game.event.impl.ItemClickEvent;
import com.osroyale.game.event.impl.ItemOnNpcEvent;
import com.osroyale.game.event.impl.ItemOnObjectEvent;
import com.osroyale.game.event.impl.ItemOnPlayerEvent;
import com.osroyale.game.plugin.PluginContext;
import com.osroyale.game.world.entity.mob.npc.Npc;
import com.osroyale.game.world.entity.mob.player.Player;
import com.osroyale.game.world.entity.mob.player.PlayerRight;
import com.osroyale.net.packet.out.SendString;
import com.osroyale.net.packet.out.SendTooltip;
import com.osroyale.util.Utility;

import java.util.Optional;

/**
 * Handles the rotten tomatoe plugin.
 *
 * @author Daniel.
 */
public class RottenTomatoePlugin extends PluginContext {
    private static final int ROTTEN_TOMATOE = 5733;

    @Override
    protected boolean firstClickItem(Player player, ItemClickEvent event) {
        if (event.getItem().getId() == ROTTEN_TOMATOE && canUse(player)) {
            player.message("Coming soon - Settings");
            return true;
        }

        return false;
    }

    @Override
    protected boolean itemOnPlayer(Player player, ItemOnPlayerEvent event) {
        if (event.getUsed().getId() == ROTTEN_TOMATOE && canUse(player)) {
            open(player, event.getOther());
            return true;
        }

        return false;
    }

    @Override
    protected boolean itemOnNpc(Player player, ItemOnNpcEvent event) {
        if (event.getUsed().getId() == ROTTEN_TOMATOE && canUse(player)) {
            if (!PlayerRight.isManager(player)) {
                player.message("Only managers can do this!");
                return true;
            }
            Npc npc = event.getNpc();
            String name = "<col=255>" + npc.getName() + "</col>";
            DialogueFactory factory = player.dialogueFactory;

            factory.sendOption("Get " + name + " information", () -> {
                factory.sendInformationBox(name + " Information", "ID: " + npc.id, "Instance: " + npc.instance, "Spawn Position: " + npc.spawnPosition);
            }, "Unregister " + name, () -> {
                npc.unregister();
                factory.clear();
            });

            factory.execute();
            return true;
        }

        return false;
    }

    @Override
    protected boolean itemOnObject(Player player, ItemOnObjectEvent event) {
        if (event.getUsed().getId() == ROTTEN_TOMATOE && canUse(player)) {
            player.message("Coming soon - Settings");
            return true;
        }

        return false;
    }

    @Override
    protected boolean onClick(Player player, int button) {
        if (button >= -28805 && button <= -28767) {
            int index = (button - -28805) / 2;

            if (index > PlayerManagement.values().length) {
                return false;
            }

            PlayerManagement management = PlayerManagement.forOrdinal(index);

            if (management == null) {
                return false;
            }

            management.handle(player);
            return true;

        }
        return false;
    }

    public static void open(Player player, Player other) {
        int string = 36732;

        for (PlayerManagement management : PlayerManagement.values()) {
            player.send(new SendString(management.string, string));
            player.send(new SendTooltip("Execute action: " + management.string, string - 1));
            string += 2;
        }

        player.send(new SendString(other.getName(), 36706));
        player.send(new SendString(other.right.getName(), 36708));
        player.send(new SendString(other.created, 36710));
        player.send(new SendString(Utility.getTime(other.playTime), 36712));
        player.send(new SendString(other.lastHost, 36714));
        player.interfaceManager.open(36700);
        player.managing = Optional.of(other);
    }

    private boolean canUse(Player player) {
        if (!PlayerRight.isModerator(player)) {
            player.inventory.remove(ROTTEN_TOMATOE);
            player.message("You aren't suppose to have that!");
            return false;
        }
        return true;
    }
}
