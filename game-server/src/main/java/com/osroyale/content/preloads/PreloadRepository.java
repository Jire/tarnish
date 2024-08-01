package com.osroyale.content.preloads;

import com.osroyale.content.preloads.impl.*;

import java.util.ArrayList;
import java.util.List;

/**
 * The preload repository.
 *
 * @author Daniel.
 */
public class PreloadRepository {

    /** The list containing all the preloads. */
    public static List<Preload> PRELOADS = new ArrayList<>();

    /** The indexes of all random preloads. */
    public static final int
            CAPE = 1,
            HAT = 2,
            ROBE_TOP = 3,
            ROBE_BOTTOM = 4,
            GOD_CAPE = 5,
            GOD_BOOK = 6,
            MYSTIC_TOP = 7,
            MYSTIC_BOTTOM = 8;

    /** Declares all the preloads into the list on startup. */
    public static void declare() {
        /* Mains */
        PRELOADS.add(new MainMeleePreload());
        PRELOADS.add(new MainRangePreload());
        PRELOADS.add(new MainNHPreload());

        /* Zerkers */
        PRELOADS.add(new ZerkerMeleePreload());
        PRELOADS.add(new ZerkerRangePreload());
        PRELOADS.add(new ZerkerNHPreload());

        /* Pures  */
        PRELOADS.add(new PureMeleePreload());
        PRELOADS.add(new PureRangePreload());
        PRELOADS.add(new PureNHPreload());

        /* Initiates */
        PRELOADS.add(new InitiateMeleePreload());
        PRELOADS.add(new InitiateRangePreload());
        PRELOADS.add(new InitiateNHPreload());

        /* F2Ps */
        PRELOADS.add(new F2PMeleePreload());
        PRELOADS.add(new F2PRangePreload());
    }
}
