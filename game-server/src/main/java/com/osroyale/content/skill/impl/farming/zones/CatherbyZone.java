package com.osroyale.content.skill.impl.farming.zones;

import com.osroyale.content.skill.impl.farming.patches.impl.AllotmentPatch;
import com.osroyale.content.skill.impl.farming.patches.impl.FlowerPatch;
import com.osroyale.content.skill.impl.farming.patches.impl.HerbPatch;
import com.osroyale.game.world.Interactable;
import com.osroyale.game.world.entity.mob.player.Player;
import com.osroyale.game.world.position.Position;

/**
 * The Catherby farming zone.
 *
 * @author Michael | Chex
 */
public class CatherbyZone extends FarmingZone {

    /** The Catherby farming zone boundary. */
    private static final Interactable CATHERBY_BOUNDARY = Interactable.create(new Position(2804, 3458), 11, 11);

    /** The northern Catherby allotment patch boundary. */
    private static final Interactable[] NORTH_ALLOTMENT_PATCH = {
        Interactable.create(new Position(2805, 3466), 2, 3),
        Interactable.create(new Position(2807, 3467), 7, 2)
    };

    /** The southern Catherby allotment patch boundary. */
    private static final Interactable[] SOUTH_ALLOTMENT_PATCH = {
        Interactable.create(new Position(2805, 3459), 2, 3),
        Interactable.create(new Position(2807, 3459), 7, 2)
    };

    /** The Catherby flower patch boundary. */
    private static final Interactable[] FLOWER_PATCH = {
        Interactable.create(new Position(2809, 3463), 2, 2)
    };

    /** The Catherby herb patch boundary. */
    private static final Interactable[] HERB_PATCH = {
        Interactable.create(new Position(2813, 3463), 2, 2)
    };

    public CatherbyZone(Player player) {
        super(CATHERBY_BOUNDARY, 4);

        FlowerPatch flowerPatch = new FlowerPatch(player, this, FLOWER_PATCH);
        setPatch(0, new AllotmentPatch(player, this, flowerPatch, NORTH_ALLOTMENT_PATCH));
        setPatch(1, new AllotmentPatch(player, this, flowerPatch, SOUTH_ALLOTMENT_PATCH));
        setPatch(2, flowerPatch);
        setPatch(3, new HerbPatch(player, this, HERB_PATCH));
    }

}
