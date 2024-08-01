package com.osroyale.content.combat.cannon;

import com.osroyale.content.combat.cannon.CannonManager.Rotation;
import com.osroyale.game.task.Task;
import com.osroyale.game.world.World;
import com.osroyale.game.world.entity.combat.hit.Hit;
import com.osroyale.game.world.entity.combat.hit.HitIcon;
import com.osroyale.game.world.entity.mob.npc.Npc;
import com.osroyale.game.world.entity.mob.player.Player;
import com.osroyale.net.packet.out.SendMessage;

public final class CannonFireAction extends Task {
    private final Player player;
    private final Cannon cannon;

    public CannonFireAction(Player player, Cannon cannon) {
        super(true, 2);
        this.player = player;
        this.cannon = cannon;
    }

    @Override
    public void execute() {
        if (cannon.getAmmunition() <= 0) {
            player.send(new SendMessage("Your cannon has run out of ammunition!"));
            cannon.setFiring(false);
            cancel();
            return;
        }

        switch (cannon.getRotation()) {
            case NORTH:
                cannon.setRotation(Rotation.NORTH_EAST);
                break;
            case NORTH_EAST:
                cannon.setRotation(Rotation.EAST);
                break;
            case EAST:
                cannon.setRotation(Rotation.SOUTH_EAST);
                break;
            case SOUTH_EAST:
                cannon.setRotation(Rotation.SOUTH);
                break;
            case SOUTH:
                cannon.setRotation(Rotation.SOUTH_WEST);
                break;
            case SOUTH_WEST:
                cannon.setRotation(Rotation.WEST);
                break;
            case WEST:
                cannon.setRotation(Rotation.NORTH_WEST);
                break;
            case NORTH_WEST:
                cannon.setRotation(Rotation.NORTH);
                break;
        }

        CannonManager.rotate(cannon);

        Npc[] mobs = CannonManager.getNpc(cannon);

        if (mobs != null) {
            for (Npc i : mobs) {
                if (i != null) {
                    int lockon = i.getIndex() + 1;
                    byte offsetX = (byte) ((i.getPosition().getY() - i.getPosition().getY()) * -1);
                    byte offsetY = (byte) ((i.getPosition().getX() - i.getPosition().getX()) * -1);
                    World.sendProjectile(CannonManager.getCannonFire(), cannon.getPosition(), cannon.instance, lockon, offsetX, offsetY);

                    Hit hit = new Hit(3, HitIcon.CANON);

                    i.damage(hit);
                    cannon.setAmmunition(cannon.getAmmunition() - 1);
                }
            }
        }
    }
}
