package com.osroyale.content.combat.cannon;

import com.osroyale.game.Animation;
import com.osroyale.game.task.TickableTask;
import com.osroyale.game.world.entity.mob.Direction;
import com.osroyale.game.world.entity.mob.player.Player;
import com.osroyale.game.world.items.Item;
import com.osroyale.game.world.object.CustomGameObject;

/**
 * Created by Daniel on 2017-12-12.
 */
public class CannonBuild extends TickableTask {
    
    private final Player player;
    private final Cannon cannon;


    public CannonBuild(Player player, Cannon cannon) {
        super(false, 1);
        this.player = player;
        this.cannon = cannon;
    }

    @Override
    public void onSchedule() {
        player.locking.lock();
    }

    @Override
    public void onCancel(boolean logout) {
        player.locking.unlock();
    }

    @Override
    protected void tick() {
        switch (tick) {
            case 1:
                cannon.setStage(CannonManager.Setup.BASE);
                player.face(Direction.NORTH_EAST);
                player.animate(new Animation(827));
                player.inventory.remove(new Item(6));
                break;
            case 2:
                cannon.register();
                break;
            case 3:
                cannon.setStage(CannonManager.Setup.STAND);
                player.animate(new Animation(827));
                player.inventory.remove(new Item(8));
                break;
            case 5:
                cannon.setStage(CannonManager.Setup.BARRELS);
                player.animate(new Animation(827));
                player.inventory.remove(new Item(10));
                break;
            case 6:
                cannon.unregister();
                cannon.setObject(new CustomGameObject(9, cannon.getPosition()));
                cannon.register();
                break;
            case 7:
                cannon.setStage(CannonManager.Setup.FURNACE);
                player.animate(new Animation(827));
                player.inventory.remove(new Item(12));
                break;
            case 8:
                cannon.unregister();
                cannon.setObject(new CustomGameObject(6, cannon.getPosition()));
                cannon.register();
                break;
            case 9:
                cannon.setRotation(CannonManager.Rotation.NORTH);
                cannon.setStage(CannonManager.Setup.COMPLETE_CANNON);
                player.locking.unlock();
                CannonManager.ACTIVE_CANNONS.put(player.getName(), cannon);
                cancel();
                break;
        }
    }
}
