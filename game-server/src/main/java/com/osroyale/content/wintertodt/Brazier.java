package com.osroyale.content.wintertodt;

import com.osroyale.game.world.entity.mob.Direction;
import com.osroyale.game.world.entity.mob.npc.Npc;
import com.osroyale.game.world.object.CustomGameObject;
import com.osroyale.game.world.object.GameObject;

public class Brazier {

    private GameObject object;
    private Npc pyromancer;
    private boolean snowStorm;
    private int flameOffsetX, flameOffsetY;
    public Direction direction;

    public Brazier(GameObject object, Npc pyromancer, int flameOffsetX, int flameOffsetY, Direction direction) {
        this.object = object;
        this.pyromancer = pyromancer;
        this.flameOffsetX = flameOffsetX;
        this.flameOffsetY = flameOffsetY;
        this.direction = direction;
    }

    public GameObject getObject() {
        return object;
    }

    public Npc getPyromancer() {
        return pyromancer;
    }

    public boolean isPyromancerAlive() {
        return pyromancer.id == Wintertodt.PYROMANCER;
    }

    public int getFlameOffsetX() {
        return flameOffsetX;
    }

    public int getFlameOffsetY() {
        return flameOffsetY;
    }

    public boolean hasSnowStorm() {
        return snowStorm;
    }

    public void setSnowStorm(boolean snowStorm) {
        this.snowStorm = snowStorm;
    }

    public int getBrazierState() {
        return object.getId() == Wintertodt.EMPTY_BRAZIER_ID ? 0 :
                (object.getId() == Wintertodt.BURNING_BRAZIER_ID ? 1 : 3);
    }

    public void setObject(int objectId) {
        this.object = new CustomGameObject(objectId, this.object.getPosition(), this.object.getDirection(), this.object.getObjectType());
    }
}