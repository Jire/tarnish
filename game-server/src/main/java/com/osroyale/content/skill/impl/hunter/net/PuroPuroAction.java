package com.osroyale.content.skill.impl.hunter.net;

import com.osroyale.game.action.Action;
import com.osroyale.game.action.policy.WalkablePolicy;
import com.osroyale.game.world.entity.mob.player.Player;
import com.osroyale.game.world.object.GameObject;

public class PuroPuroAction extends Action<Player> {
    private final GameObject object;
    private boolean run = false;
    private int walkAnimation;

    public PuroPuroAction(Player player, GameObject object) {
        super(player, 3, false);
        this.object = object;
    }

    @Override
    public void onSchedule() {
        run = getMob().movement.isRunningToggled();
        walkAnimation = getMob().mobAnimation.getWalk();
        getMob().mobAnimation.setWalk(6594);

        getMob().locking.lock();
        getMob().movement.setRunningToggled(false);
        getMob().message("You use your strength to push through the wheat.");

        int dx = 0;
        int dy = 0;

        if (getMob().getX() > object.getX()) {
            dx = -2;
        } else if (getMob().getX() < object.getX()) {
            dx = 2;
        }

        if (getMob().getY() > object.getY()) {
            dy = -2;
        } else if (getMob().getY() < object.getY()) {
            dy = 2;
        }

        getMob().walk(getMob().getPosition().transform(dx, dy), true);
    }

    @Override
    public void execute() {
        cancel();
    }

    @Override
    public void onCancel(boolean logout) {
        getMob().locking.unlock();
        getMob().mobAnimation.setWalk(walkAnimation);
        getMob().movement.setRunningToggled(run);
    }

    @Override
    public WalkablePolicy getWalkablePolicy() {
        return WalkablePolicy.NON_WALKABLE;
    }

    @Override
    public String getName() {
        return "puro-puro-action";
    }
}
