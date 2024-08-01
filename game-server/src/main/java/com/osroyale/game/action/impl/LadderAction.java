package com.osroyale.game.action.impl;

import com.osroyale.game.Animation;
import com.osroyale.game.UpdatePriority;
import com.osroyale.game.action.Action;
import com.osroyale.game.action.policy.WalkablePolicy;
import com.osroyale.game.world.entity.mob.player.Player;
import com.osroyale.game.world.object.GameObject;
import com.osroyale.game.world.position.Position;
import com.osroyale.net.packet.out.SendMessage;
import com.osroyale.util.MessageColor;

import java.util.function.Predicate;

/**
 * Created by Daniel on 2017-10-12.
 */
public class LadderAction extends Action<Player> {
    private int count;
    private final GameObject ladder;
    private final Position position;
    private final Predicate<Player> condition;
    private final String message;

    public LadderAction(Player mob, GameObject ladder, Position position) {
        this(mob, ladder, position, null, null);
    }

    public LadderAction(Player mob, GameObject ladder, Position position, Predicate<Player> condition, String message) {
        super(mob, 1, false);
        this.ladder = ladder;
        this.position = position;
        this.condition = condition;
        this.message = message;
    }

    @Override
    public boolean canSchedule() {
        if (condition != null && !condition.test(getMob())) {
            getMob().send(new SendMessage(message, MessageColor.RED));
            return false;
        }
        return true;
    }

    @Override
    public void onSchedule() {
        getMob().locking.lock();
        getMob().face(ladder.getPosition());
        getMob().getCombat().reset();
        getMob().damageImmunity.reset(3_000);
    }

    @Override
    public void execute() {
        if (count == 0) {
            getMob().animate(new Animation(828, UpdatePriority.VERY_HIGH));
        } else if (count == 1) {
            getMob().move(position);
            cancel();
        }
        count++;
    }

    @Override
    public void onCancel(boolean logout) {
    }

    @Override
    public WalkablePolicy getWalkablePolicy() {
        return WalkablePolicy.NON_WALKABLE;
    }

    @Override
    public String getName() {
        return "Ladder action";
    }

    @Override
    public boolean prioritized() {
        return false;
    }
}
