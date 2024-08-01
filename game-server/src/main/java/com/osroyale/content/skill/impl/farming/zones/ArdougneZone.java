package com.osroyale.content.skill.impl.farming.zones;

import com.osroyale.content.skill.impl.farming.patches.impl.AllotmentPatch;
import com.osroyale.content.skill.impl.farming.patches.impl.FlowerPatch;
import com.osroyale.content.skill.impl.farming.patches.impl.HerbPatch;
import com.osroyale.game.world.Interactable;
import com.osroyale.game.world.entity.mob.player.Player;
import com.osroyale.game.world.position.Position;

/**
 * The Ardougne farming zone.
 *
 * @author Michael | Chex
 */
public class ArdougneZone extends FarmingZone {

    /** The Ardougne farming zone boundary. */
    private static final Interactable ARDOUGNE_BOUNDARY = Interactable.create(new Position(2661, 3369), 11, 11);

    /** The northern Ardougne allotment patch boundary. */
    private static final Interactable[] NORTH_ALLOTMENT_PATCH = {
        Interactable.create(new Position(2662, 3377), 2, 3),
        Interactable.create(new Position(2664, 3378), 7, 2)
    };

    /** The southern Ardougne allotment patch boundary. */
    private static final Interactable[] SOUTH_ALLOTMENT_PATCH = {
        Interactable.create(new Position(2662, 3370), 2, 3),
        Interactable.create(new Position(2664, 3370), 7, 2)
    };

    /** The Ardougne flower patch boundary. */
    private static final Interactable[] FLOWER_PATCH = {
        Interactable.create(new Position(2666, 3374), 2, 2)
    };

    /** The Ardougne herb patch boundary. */
    private static final Interactable[] HERB_PATCH = {
        Interactable.create(new Position(2670, 3374), 2, 2)
    };

    public ArdougneZone(Player player) {
        super(ARDOUGNE_BOUNDARY, 4);

        FlowerPatch flowerPatch = new FlowerPatch(player, this, FLOWER_PATCH);
        setPatch(0, new AllotmentPatch(player, this, flowerPatch, NORTH_ALLOTMENT_PATCH));
        setPatch(1, new AllotmentPatch(player, this, flowerPatch, SOUTH_ALLOTMENT_PATCH));
        setPatch(2, flowerPatch);
        setPatch(3, new HerbPatch(player, this, HERB_PATCH));
    }

}
