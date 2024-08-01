package plugin.click.item;

import com.osroyale.game.event.impl.ItemClickEvent;
import com.osroyale.game.plugin.PluginContext;
import com.osroyale.game.world.entity.mob.player.Player;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @author Settings 07/14/2023
 */
public class ImbuedHeartPlugin extends PluginContext {
    private static final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    private String name;
    private String description;


    public ImbuedHeartPlugin() {
        this.name = "Imbued Heart";
        this.description = "A magic-boosting item dropped by superior slayer monsters.";

    }

    public String getName() {
        return name;
    }

    public int getCooldown(Player player) {
        return player.cooldown;
    }

    @Override
    protected boolean firstClickItem(Player player, ItemClickEvent event) {
        if(event.getItem().getId() != 20724) {
            return false;
        }
        invigorate(player.skills.getLevel(6), player);
        return true;
    }

    public void invigorate(int magicLevel, Player player) {
        long currentTime = System.currentTimeMillis();
        if ((currentTime - player.lastUsed) < player.cooldown) {
            long remainingCooldown = player.cooldown - (currentTime - player.lastUsed);
            long remainingMinutes = (remainingCooldown / (60 * 1000)) % 60;
            long remainingSeconds = (remainingCooldown / 1000) % 60;
            player.message("The heart is still drained of its power.", "Judging by how it feels, it will be ready in around @red@"
                    + remainingMinutes + " minutes and " + remainingSeconds + " seconds.");
        } else {
            player.lastUsed = currentTime;
            int boost = calculateBoost(magicLevel);
            player.graphic(1316);
            player.skills.setLevel(6, magicLevel + boost);
            scheduleCooldownReset(player, 7);
        }
    }

    public int calculateBoost(int magicLevel) {
        return (magicLevel / 10) + 1;
    }

    public void scheduleCooldownReset(Player player, long minutes) {
        final Runnable cooldownReset = () -> resetCooldown(player);
        scheduler.schedule(cooldownReset, minutes, TimeUnit.MINUTES);
    }


    public static void resetCooldown(Player player) {
        player.lastUsed = 0;
        player.skills.restore(6);
    }
}
