package com.osroyale.game.task.impl;

import com.osroyale.game.task.Task;
import com.osroyale.game.world.entity.mob.player.Player;
import com.osroyale.net.packet.out.SendMessage;
import com.osroyale.net.packet.out.SendWidget;

/**
 * This randomevent handles the teleblock counter.
 *
 * @author Daniel | Obey
 */
public class TeleblockTask extends Task {

    private final Player player;

    public TeleblockTask(Player player) {
        super(false, 0);
        this.player = player;
    }

    @Override
    public void execute() {
        if (player == null || !player.isValid()) {
            cancel();
            return;
        }

        if (player.teleblockTimer.decrementAndGet() <= 0) {
            cancel();
        }
    }

    @Override
    public void onCancel(boolean logout) {
        player.send(new SendWidget(SendWidget.WidgetType.TELEBLOCK, 0));
        player.send(new SendMessage("You feel the effects of the tele-block spell go away."));
    }

}