package com.osroyale.content.skill.impl.farming.zones;

import com.osroyale.content.skill.impl.farming.patches.impl.AllotmentPatch;
import com.osroyale.content.skill.impl.farming.patches.impl.FlowerPatch;
import com.osroyale.content.skill.impl.farming.patches.impl.HerbPatch;
import com.osroyale.game.world.Interactable;
import com.osroyale.game.world.entity.mob.player.Player;
import com.osroyale.game.world.position.Position;

/**
 * The Falador farming zone.
 *
 * @author Michael | Chex
 */
public class FaladorZone extends FarmingZone {

    /** The Falador farming zone boundary. */
    private static final Interactable FALADOR_BOUNDARY = Interactable.create(new Position(3049, 3302), 11, 11);

    /** The northern Falador allotment patch boundary. */
    private static final Interactable[] NORTH_ALLOTMENT_PATCH = {
        Interactable.create(new Position(3050, 3307), 2, 6),
        Interactable.create(new Position(3052, 3311), 3, 2)
    };

    /** The southern Falador allotment patch boundary. */
    private static final Interactable[] SOUTH_ALLOTMENT_PATCH = {
        Interactable.create(new Position(3055, 3303), 3, 2),
        Interactable.create(new Position(3058, 3303), 2, 6)
    };

    /** The Falador flower patch boundary. */
    private static final Interactable[] FLOWER_PATCH = {
        Interactable.create(new Position(3054, 3307), 2, 2)
    };

    /** The Falador herb patch boundary. */
    private static final Interactable[] HERB_PATCH = {
        Interactable.create(new Position(3058, 3311), 2, 2)
    };

    public FaladorZone(Player player) {
        super(FALADOR_BOUNDARY, 4);

        FlowerPatch flowerPatch = new FlowerPatch(player, this, FLOWER_PATCH);
        setPatch(0, new AllotmentPatch(player, this, flowerPatch, NORTH_ALLOTMENT_PATCH));
        setPatch(1, new AllotmentPatch(player, this, flowerPatch, SOUTH_ALLOTMENT_PATCH));
        setPatch(2, flowerPatch);
        setPatch(3, new HerbPatch(player, this, HERB_PATCH));
    }

}
