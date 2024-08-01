package com.osroyale.content.activity.impl.godwars;

import com.osroyale.Config;
import com.osroyale.content.activity.Activity;
import com.osroyale.content.activity.ActivityType;
import com.osroyale.content.activity.panel.ActivityPanel;
import com.osroyale.content.event.impl.ObjectInteractionEvent;
import com.osroyale.game.Animation;
import com.osroyale.game.Graphic;
import com.osroyale.game.world.entity.mob.Mob;
import com.osroyale.game.world.entity.mob.player.Player;
import com.osroyale.game.world.entity.mob.player.PlayerRight;
import com.osroyale.game.world.object.GameObject;
import com.osroyale.game.world.position.Area;
import com.osroyale.game.world.position.Position;

import java.util.Optional;

/**
 * Handles the godwars activity.
 *
 * TODO: Add a way to get back up after climbing down to Saradomin chambers
 *
 * @author Daniel
 */
public class GodwarsActivity extends Activity {
    /** The indexes for the god wars kill count. */
    static final int ARMADYL = 0, BANDOS = 1, SARADOMIN = 2, ZAMORAK = 3;

    /** The player instance of this activity. */
    private final Player player;

    /** The kill count array. */
    private int[] killCount = new int[4];

    /** The combat listener for the god wars activity. */
    private final GodwarsListener listener = new GodwarsListener(this);

    /** Constructs a new <code>GodwarsActivity</code>. */
    private GodwarsActivity(Player player) {
        super(1, Mob.DEFAULT_INSTANCE);
        this.player = player;
    }

    /** Creates the godwars activity for the player. */
    public static GodwarsActivity create(Player player) {
        GodwarsActivity activity = new GodwarsActivity(player);
        activity.add(player);
        activity.update();
        return activity;
    }

    /** Increments the godwars kill count and updates the interface. */
    void increment(int index) {
        killCount[index]++;
        update();
    }

    /** Checks if the player can enter the godwars chambers. */
    private boolean canEnter(int index) {
        if (PlayerRight.isOwner(player)) {
            return true;
        }
        if (player.inventory.contains(11942, 1)) {
            player.inventory.remove(11942);
            player.message("You have used your Ecumencial key to enter the room.");
            return true;
        }
        if (killCount[index] < 5) {
            player.message("You need 5 kills before accessing this door!");
            return false;
        }
        return true;
    }

    @Override
    protected void start() {
        pause();
    }

    @Override
    public void finish() {
        remove(player);
    }

    @Override
    public void cleanup() {
        ActivityPanel.clear(player);
    }

    @Override
    public void update() {
        String armadyl = "Armadyl kills: <col=ffffff>" + killCount[ARMADYL];
        String bandos = "Bandos kills: <col=ffffff>" + killCount[BANDOS];
        String saradomin = "Saradomin kills: <col=ffffff>" + killCount[SARADOMIN];
        String zamorak = "Zamorak kills: <col=ffffff>" + killCount[ZAMORAK];
        ActivityPanel.update(player, -1, "God Wars", "", "", armadyl, bandos, saradomin, zamorak);
    }

    @Override
    protected boolean clickObject(Player player, ObjectInteractionEvent event) {
        GameObject object = event.getObject();
        int identification = object.getId();

        switch (identification) {
            case 26502:
                if (canEnter(ARMADYL) && player.getPosition().getY() < object.getPosition().getY()) {
                    player.move(new Position(player.getX(), player.getY() + 2, 2));
                } else if (player.getPosition().getY() > object.getPosition().getY()) {
                    player.move(new Position(player.getX(), player.getY() - 2, 2));
                }
                return true;
            case 26503:
                if (canEnter(BANDOS) && player.getPosition().getX() < object.getPosition().getX()) {
                    player.move(new Position(player.getX() + 2, player.getY(), player.getHeight()));
                } else if (player.getPosition().getX() > object.getPosition().getX()) {
                    player.move(new Position(player.getX() - 2, player.getY(), player.getHeight()));
                }
                return true;
            case 26504:
                if (player.getPosition().getX() < object.getPosition().getX()) {
                    player.move(new Position(player.getX() + 2, player.getY(), 0));
                } else if (canEnter(SARADOMIN) && player.getPosition().getX() > object.getPosition().getX()) {
                    player.move(new Position(player.getX() - 2, player.getY(), 0));
                }
                return true;
            case 26505:
                if (canEnter(ZAMORAK) && player.getPosition().getY() > object.getPosition().getY()) {
                    player.move(new Position(player.getX(), player.getY() - 2, 2));
                } else if (player.getPosition().getY() < object.getPosition().getY()) {
                    player.move(new Position(player.getX(), player.getY() + 2, 2));
                }
                return true;
            case 26518:
                if (player.getY() == 5332) {
                    player.move(new Position(2885, 5345, 2));
                } else {
                    player.move(new Position(2885, 5332, 2));
                }
                return true;
            case 26380:
                if (player.getY() == 5269) {
                    player.move(new Position(2871, 5279, 2));
                } else {
                    player.move(new Position(2871, 5269, 2));
                }
                return true;
            case 26461:
                if (player.getX() >= 2851) {
                    player.move(new Position(2850, 5333, 2));
                } else {
                    player.move(new Position(2851, 5333, 2));
                }
                return true;
            case 26561:
                player.move(new Position(2918, 5300, 1));
                return true;
            case 26562:
                player.move(new Position(2919, 5274, 0));
                return true;
        }
        return false;
    }

    @Override
    public void onRegionChange(Player player) {
        if (!Area.inGodwars(player)) {
            finish();
        }
    }

    @Override
    public boolean canTeleport(Player player) {
        return true;
    }
    @Override
    public void onDeath(Mob mob) {
            if (mob.isPlayer()) {
                cleanup();
                mob.move(Config.DEFAULT_POSITION);
                mob.animate(Animation.RESET, true);
                mob.graphic(Graphic.RESET, true);
            }
        }
    @Override
    public ActivityType getType() {
        return ActivityType.GODWARS;
    }

    @Override
    protected Optional<GodwarsListener> getListener() {
        return Optional.of(listener);
    }
}
