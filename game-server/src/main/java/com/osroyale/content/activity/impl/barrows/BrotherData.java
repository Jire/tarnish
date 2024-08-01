package com.osroyale.content.activity.impl.barrows;

import com.osroyale.game.world.entity.mob.npc.Npc;
import com.osroyale.game.world.position.Position;

public enum BrotherData {
    AHRIM(1672, new Position(3562, 3285), new Position(3568, 3291), new Position(3565, 3288), new Position(3557, 9703, 3)),
    GUTHAN(1674, new Position(3575, 3279), new Position(3580, 3286), new Position(3577, 3282), new Position(3534, 9704, 3)),
    VERAC(1677, new Position(3554, 3293), new Position(3560, 3301), new Position(3557, 3297), new Position(3578, 9706, 3)),
    KARIL(1675, new Position(3563, 3273), new Position(3568, 3278), new Position(3566, 3275), new Position(3546, 9684, 3)),
    DHAROK(1673, new Position(3573, 3295), new Position(3578, 3300), new Position(3575, 3298), new Position(3556, 9718, 3)),
    TORAG(1676, new Position(3551, 3279), new Position(3556, 3285), new Position(3554, 3282), new Position(3568, 9683, 3));

    /** The npc id of the barrows brother. */
    private int npcId;

    /** The Position that has to be dug to get into the crypt. */
    private Position southWest;

    /** The Position that has to be dug to get into the crypt. */
    private Position northEast;

    /** The position of the hill. */
    private Position hillPosition;

    /** The position of the crypt. */
    private Position cryptPosition;

    BrotherData(int npcId, Position southWest, Position northEast, Position hillPosition, Position cryptPosition) {
        this.npcId = npcId;
        this.southWest = southWest;
        this.northEast = northEast;
        this.hillPosition = hillPosition;
        this.cryptPosition = cryptPosition;
    }

    /**
     * Gets the npc id of the barrows brother.
     *
     * @return the npc id of the barrows brother.
     */
    public int getNpcId() {
        return npcId;
    }

    /**
     * Gets the Position that has to be dug to get into the crypt.
     *
     * @return the Position that has to be dug to get into the crypt.
     */
    public Position getSouthWest() {
        return southWest;
    }

    /**
     * Gets the Position that has to be dug to get into the crypt.
     *
     * @return the Position that has to be dug to get into the crypt.
     */
    public Position getNorthEast() {
        return northEast;
    }

    /**
     * Gets the position of the hill.
     *
     * @return the position of the hill.
     */
    public Position getHillPosition() {
        return hillPosition;
    }

    /**
     * Gets the position of the crypt.
     *
     * @return the position of the crypt.
     */
    public Position getCryptPosition() {
        return cryptPosition;
    }

    public static boolean isBarrowsBrother(Npc mob) {
        for (BrotherData brother : values()) {
            if (brother.getNpcId() == mob.id) {
                return true;
            }
        }
        return false;
    }

    public static BrotherData getBarrowsBrother(Npc mob) {
        for (BrotherData brother : values()) {
            if (brother.getNpcId() == mob.id) {
                return brother;
            }
        }
        return null;
    }
}
