package com.osroyale.content.lms.safezone;

import com.osroyale.game.world.entity.mob.player.Player;
import com.osroyale.game.world.position.Boundary;

public enum LMSSafezone {

    MOUNTAIN(3430, 3440, 5839, 5849),
    TRINITY_OUTPOST(3481, 3491, 5870, 5880),
    DEBTOR_HIDEOUT(3400, 3410, 5794, 5804),
    MOSER_SETTLEMENT(3470, 3480, 5782, 5792)
    ;

    public int boundsXSW, boundsXNE, boundsYSW, boundsYNE;

    LMSSafezone(int boundsXSW, int boundsXNE, int boundsYSW, int boundsYNE) {
        this.boundsXSW = boundsXSW;
        this.boundsXNE = boundsXNE;
        this.boundsYSW = boundsYSW;
        this.boundsYNE = boundsYNE;
    }

    public boolean inSafeZone(Player player) {
        return Boundary.isIn(player, new Boundary(this.boundsXSW, this.boundsYSW, this.boundsXNE, this.boundsYNE));
    }
}