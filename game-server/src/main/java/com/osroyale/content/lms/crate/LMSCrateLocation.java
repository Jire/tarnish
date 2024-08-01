package com.osroyale.content.lms.crate;

import com.osroyale.game.world.position.Position;

public enum LMSCrateLocation {

    DEBTOR_HIDEOUT_NORTH(new Position(3409, 5819, 0), "near the Debtor hideout"),
    DEBTOR_HIDEOUT_EAST(new Position(3421, 5801, 0), "near the Debtor hideout"),
    MOUNTAIN(new Position(3450, 5852, 0), "near the mountain"),
    MOSER_SETTLEMENT_NORTH(new Position(3479, 5809, 0), "near the Moser settlement"),
    MOSER_SETTLEMENT_EAST(new Position(3494, 5791, 0), "near the Moser settlement"),
    MOSER_SETTLEMENT_WEST(new Position(3459, 5789, 0), "near the Moser settlement"),
    ROCKY_OUTPOST(new Position(3459, 5772, 0), "near the Rocky outcrop"),
    TRINITY_OUTPOST(new Position(3480, 5876, 0), "near the Trinity outpost");

    public Position location;
    public String tip;

    LMSCrateLocation(Position location, String tip) {
        this.location = location;
        this.tip = tip;

    }

}