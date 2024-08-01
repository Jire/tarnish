package com.osroyale.content.activity.impl.duelarena;

import com.google.common.collect.ImmutableList;
import com.osroyale.game.world.position.Area;
import com.osroyale.game.world.position.Position;
import com.osroyale.game.world.position.impl.SquareArea;

public class DuelConstants {

    private DuelConstants() {

    }

    public static final Position DUEL_RESPAWN = new Position(3366, 3266);

    public static final ImmutableList<SquareArea> RESPAWN_LOCATIONS = ImmutableList.of(
            new SquareArea(3356, 3268, 3359, 3274),
            new SquareArea(3360, 3274, 3378, 3277),
            new SquareArea(3355, 3274, 3357, 3278),
            new SquareArea(3373, 3264, 3373, 3268));


}
