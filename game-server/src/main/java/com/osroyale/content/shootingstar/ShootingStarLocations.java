package com.osroyale.content.shootingstar;

import com.osroyale.game.world.position.Position;

public enum ShootingStarLocations {
    FALADOR(new Position(3053, 3301, 0), new String[] { "Falador farming patches", "at the" }),
    EDGEVILLE(new Position(3100, 3484, 0), new String[] { "Edgeville bank", "near" }),
    GNOME_AGILITY_COURSE(new Position(2480, 3433, 0), new String[] { "Gnome agility course", "by the" }),
    WILDERNESS_AGILITY_COURSE(new Position(2995, 3911, 0), new String[] { "Wildy agility course", "outside the" }),
    WEST_DRAGONS(new Position(2986, 3599, 0), new String[] { "West dragons", "at the" }),
    VOID_KNIGHT_ISLAND(new Position(2666, 2648, 0), new String[] { "Void Knight's island", "at the" }),
    BARROWS_HILL(new Position(3566, 3297, 0), new String[] { "Barrows hills", "on top of the" }),
    DUEL_ARENA(new Position(3368, 3269, 0), new String[] { "Duel Arena", "at the" })
    ;

    public Position starPosition;
    public String[] location;

    ShootingStarLocations(Position starPosition, String[] location) {
        this.starPosition = starPosition;
        this.location = location;
    }
}
