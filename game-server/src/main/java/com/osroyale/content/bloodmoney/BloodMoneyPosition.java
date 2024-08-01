package com.osroyale.content.bloodmoney;

import com.osroyale.game.world.object.ObjectDirection;
import com.osroyale.game.world.position.Position;

public enum BloodMoneyPosition {
//    CASTLE("Castle", new Position(3028, 3622), ObjectDirection.EAST),
    BANDIT_CAMPE("Bandit Camp", new Position(3036, 3700), ObjectDirection.SOUTH),
    LAVA_DRAGONS("Lava Dragons", new Position(3200,3829), ObjectDirection.NORTH),
    DEMONIC_RUINS("Demonic Ruins", new Position(3287, 3881), ObjectDirection.NORTH),
    FOUNTAIN_OF_RUNE("Fountain of Rune", new Position(3372,3896), ObjectDirection.SOUTH),
    LAVA_BRIDGE("Lava Bridge", new Position(3367,3936), ObjectDirection.SOUTH),
    ROUGES_CASTLE("Rouge's Castle", new Position(3285,3946), ObjectDirection.SOUTH),
    SCORPION_PIT("Scorpion Pit", new Position(3237,3948), ObjectDirection.SOUTH);

    public final String name;
    public final Position position;
    public final ObjectDirection direction;

    BloodMoneyPosition(String name, Position position, ObjectDirection direction) {
        this.name = name;
        this.position = position;
        this.direction = direction;
    }
}
