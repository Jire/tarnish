package com.osroyale.content.activity.impl.warriorguild;

import com.osroyale.game.world.position.Position;

import java.util.Arrays;
import java.util.Optional;

/**
 * The animator data.
 *
 * @author Daniel
 */
public enum AnimatorData {
    SOUTH_WEST(new Position(2851, 3536), new Position(2851, 3537), new Position(2851, 3541)),
    SOUTH_EAST(new Position(2857, 3536), new Position(2857, 3537), new Position(2857, 3541)),
    NORTH_WEST(new Position(2853, 3552), new Position(2853, 3551), new Position(2853, 3547)),
    NORTH_EAST(new Position(2857, 3552), new Position(2857, 3551), new Position(2857, 3547));

    /** The animator object position. */
    public final Position objectPosition;

    /** The animatior stand position. */
    public final Position standPosition;

    /** The animator walk to position. */
    public final Position walkToPosition;

    /** Constructs a new <code>AnimatorData</code>. */
    AnimatorData(Position position, Position standPosition, Position walkToPosition) {
        this.objectPosition = position;
        this.standPosition = standPosition;
        this.walkToPosition = walkToPosition;
    }

    /** Gets the animator data based on the object position. */
    public static Optional<AnimatorData> getAnimator(Position position) {
        return Arrays.stream(values()).filter(animator -> animator.objectPosition.equals(position)).findFirst();
    }
}
