package com.osroyale.content.skill.impl.farming.zones;

import com.osroyale.content.skill.impl.farming.patches.impl.AllotmentPatch;
import com.osroyale.content.skill.impl.farming.patches.impl.FlowerPatch;
import com.osroyale.content.skill.impl.farming.patches.impl.HerbPatch;
import com.osroyale.game.world.Interactable;
import com.osroyale.game.world.entity.mob.player.Player;
import com.osroyale.game.world.position.Position;

/**
 * The Phasmaty farming zone.
 *
 * @author Michael | Chex
 */
public class PhasmatyZone extends FarmingZone {

    /** The Phasmaty farming zone boundary. */
    private static final Interactable PHASMATY_BOUNDARY = Interactable.create(new Position(3596, 3520), 11, 11);

    /** The northern Phasmaty allotment patch boundary. */
    private static final Interactable[] NORTH_ALLOTMENT_PATCH = {
        Interactable.create(new Position(3597, 3525), 2, 6),
        Interactable.create(new Position(3599, 3529), 3, 2)
    };

    /** The southern Phasmaty allotment patch boundary. */
    private static final Interactable[] SOUTH_ALLOTMENT_PATCH = {
        Interactable.create(new Position(3602, 3521), 3, 2),
        Interactable.create(new Position(3605, 3521), 2, 6)
    };

    /** The Phasmaty flower patch boundary. */
    private static final Interactable[] FLOWER_PATCH = {
        Interactable.create(new Position(3601, 3525), 2, 2)
    };

    /** The Phasmaty herb patch boundary. */
    private static final Interactable[] HERB_PATCH = {
        Interactable.create(new Position(3605, 3529), 2, 2)
    };

    public PhasmatyZone(Player player) {
        super(PHASMATY_BOUNDARY, 4);

        FlowerPatch flowerPatch = new FlowerPatch(player, this, FLOWER_PATCH);
        setPatch(0, new AllotmentPatch(player, this, flowerPatch, NORTH_ALLOTMENT_PATCH));
        setPatch(1, new AllotmentPatch(player, this, flowerPatch, SOUTH_ALLOTMENT_PATCH));
        setPatch(2, flowerPatch);
        setPatch(3, new HerbPatch(player, this, HERB_PATCH));
    }

}
