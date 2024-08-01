package com.osroyale.game.task.impl;

import com.osroyale.game.task.Task;
import com.osroyale.game.world.entity.mob.player.Player;
import com.osroyale.game.world.items.Item;
import com.osroyale.game.world.position.Area;
import com.osroyale.game.world.position.Position;

/**
 * An randomevent which handles the warrior guild cyclops randomevent.
 *
 * @author Daniel | Obey
 */
public class WarriorGuildCyclopEvent extends Task {

    /** The player instance. */
    private final Player player;

    /** Constructs a new <code>WarriorGuildCyclopEvent</code>. */
    public WarriorGuildCyclopEvent(Player player) {
        super(50);
        this.player = player;
        this.attach(player);
    }

    @Override
    public boolean canSchedule() {
        return !player.warriorGuidTask;
    }

    @Override
    public void onSchedule() {
        player.warriorGuidTask = true;
    }

    @Override
    public void onCancel(boolean logout) {
        player.warriorGuidTask = false;
    }

    @Override
    public void execute() {
        if (player.isDead() || !player.isValid() || !player.inActivity() || !Area.inCyclops(player)) {
            cancel();
            return;
        }

        if (!player.inventory.contains(new Item(8851, 10))) {
            if (Area.inCyclops(player)) {
                player.move(new Position(2846, 3540, 2));
            }
            player.dialogueFactory.sendStatement("You have been removed from the Cyclops", "room as you have run out of tokens.").execute();
            cancel();
            return;
        }

        player.inventory.remove(new Item(8851, 10));
        player.message("10 tokens have been removed from your inventory.");

        if (!player.inventory.contains(new Item(8851, 10))) {
            if (Area.inCyclops(player)) {
                player.move(new Position(2846, 3540, 2));
            }
            player.dialogueFactory.sendStatement("You have been removed from the Cyclops", "room as you have run out of tokens.").execute();
        }
    }
}
