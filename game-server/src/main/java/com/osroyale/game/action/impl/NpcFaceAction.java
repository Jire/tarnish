
package com.osroyale.game.action.impl;

import com.osroyale.game.action.Action;
import com.osroyale.game.action.policy.WalkablePolicy;
import com.osroyale.game.world.entity.mob.npc.Npc;
import com.osroyale.content.pet.PetData;
import com.osroyale.game.world.position.Position;

import java.util.Arrays;

/**
 * An action that faces an npc, but resets their facing to their default facing
 * direction after 15 seconds.
 *
 * @author Daniel
 * @author Michael | Chex
 */
public final class NpcFaceAction extends Action<Npc> {
    /**
     * The npc identifications to check if face action is allowed.
     */
    private int[] identification;

    /**
     * The array list of all the first option npcs that can not face.
     */
    private final int[] FIRST_OPTION = {};

    /**
     * The array list of all the second option npcs that can not face.
     */
    private final int[] SECOND_OPTION = {3080, 3010};

    /**
     * The array list of all the third option npcs that can not face.
     */
    private final int[] THIRD_OPTION = {};

    private final int[] FOURTH_OPTION = {};

    /**
     * Constructs a new <code>NpcFaceAction</code>.
     *
     * @param npc    The npc.
     * @param face   The face position.
     * @param option The option id.
     */
    public NpcFaceAction(Npc npc, Position face, int option) {
        super(npc, 25);
        if (option == 0) identification = FIRST_OPTION;
        else if (option == 1) identification = SECOND_OPTION;
        else if (option == 2) identification = THIRD_OPTION;
        else if (option == 3) identification = FOURTH_OPTION;

        if (Arrays.stream(PetData.values()).anyMatch(p -> npc.id == p.getNpc())) {
            cancel();
            return;
        }

        if (identification != null && Arrays.stream(identification).anyMatch($it -> npc.id == $it)) {
            cancel();
            return;
        }

        getMob().face(face);
    }

    @Override
    public void execute() {
        Npc npc = getMob().getNpc();

        if (npc.walk) {
            getMob().face(getMob().faceDirection);
        }

        cancel();
    }

    @Override
    public String getName() {
        return "Npc face";
    }

    @Override
    public boolean prioritized() {
        return false;
    }

    @Override
    public WalkablePolicy getWalkablePolicy() {
        return WalkablePolicy.NON_WALKABLE;
    }

}
