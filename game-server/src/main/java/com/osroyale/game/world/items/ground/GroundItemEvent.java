package com.osroyale.game.world.items.ground;

import com.osroyale.game.task.Task;
import com.osroyale.game.world.World;
import com.osroyale.game.world.entity.mob.player.Player;
import com.osroyale.game.world.region.Region;
import com.osroyale.net.packet.out.SendGroundItem;

/**
 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
 * @since 27-12-2016.
 */
public final class GroundItemEvent extends Task {

    /** A variable which indicates how many ticks a minute is. */
    private static final int MINUTE = 100;

    /** The ground item this randomevent is running for. */
    private final GroundItem groundItem;

    /** The counter of this task. */
    private int minutes;

    /** Creates a new {@link Task} that doesn't execute instantly. */
    GroundItemEvent(GroundItem groundItem) {
        super(MINUTE);
        this.groundItem = groundItem;
    }

    @Override
    public void execute() {
        switch (groundItem.policy) {
            case GLOBAL:
                if (++minutes < 5) {
                    return;
                }

                if (groundItem.item.getId() == 12791) {
                    groundItem.player.runePouch.runes.clear();
                }

                cancel();
                break;
            case ONLY_OWNER:
                if (++minutes < 2) {
                    return;
                }

                groundItem.policy = GroundItemPolicy.GLOBAL;
                if (!groundItem.item.isTradeable()) {
                    return;
                }
                //If a player drops an item, only they will be able to view it
                if(groundItem.player.playTime < 6000) {
                    return;
                }

                Region[] regions = World.getRegions().getSurroundingRegions(groundItem.getPosition());
                for (Region region : regions) {
                    for (Player player : region.getPlayers(groundItem.getHeight())) {
                        if (!groundItem.isRegistered())
                            continue;

                        if (!groundItem.canSee(player))
                            continue;

                        if (groundItem.player.usernameLong != player.usernameLong)
                            player.send(new SendGroundItem(groundItem));
                    }
                }
                break;
        }
    }

    @Override
    public void onCancel(boolean logout) {
        groundItem.unregister();
    }

}
