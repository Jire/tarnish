package com.osroyale.game.task.impl;

import com.osroyale.content.skill.impl.woodcutting.AxeData;
import com.osroyale.game.Animation;
import com.osroyale.game.UpdatePriority;
import com.osroyale.game.task.Task;
import com.osroyale.game.world.entity.mob.Direction;
import com.osroyale.game.world.entity.mob.player.Player;
import com.osroyale.game.world.entity.skill.Skill;
import com.osroyale.game.world.object.GameObject;
import com.osroyale.net.packet.out.SendMessage;

import java.util.Optional;

public class ChopVineTask extends Task {

    private int tick = 0;
    private final Player player;
    private final GameObject object;
    private final int respawn;

    public ChopVineTask(Player player, GameObject object, int respawn) {
        super(true, 0);
        this.player = player;
        this.object = object;
        this.respawn = respawn;
    }

    @Override
    public void onSchedule() {
        if (!player.getPosition().isWithinDistance(object.getPosition(), 1)) {
            cancel();
            return;
        }

        if (player.skills.getLevel(Skill.WOODCUTTING) < 34) {
            player.send(new SendMessage("You need a woodcutting level of 34 or more to cut this."));
            cancel();
            return;
        }

        Optional<AxeData> result = AxeData.getDefinition(player);

        if (!result.isPresent()) {
            player.send(new SendMessage("You need an axe to cut this."));
            cancel();
            return;
        }

        AxeData data = result.get();

        player.animate(new Animation(data.animation, UpdatePriority.HIGH));
    }

    @Override
    public void execute() {
        if (tick == 1) {
            object.unregister();
        } else if (tick == respawn / 2) {
            Direction direction = Direction.getDirection(player.getPosition(), object.getPosition());
            player.walk(player.getPosition().transform(direction.getDirectionX() * 2, direction.getDirectionY() * 2), true);
        } else if (tick >= respawn) {
            object.register();
            cancel();
        }

        tick++;
    }

    @Override
    public void onCancel(boolean logout) {

    }

}
