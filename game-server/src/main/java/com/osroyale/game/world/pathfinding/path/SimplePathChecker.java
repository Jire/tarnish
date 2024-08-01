package com.osroyale.game.world.pathfinding.path;

import com.osroyale.game.world.Interactable;
import com.osroyale.game.world.entity.mob.Mob;
import com.osroyale.game.world.position.Position;
import com.osroyale.util.Utility;

/**
 * Represents a {@code PathFinder} which is meant to be used to check
 * projectiles passage in a straight line.
 *
 * @author Artem Batutin <artembatutin@gmail.com>
 * @author Michael | Chex (improvements to "check" method)
 */
public class SimplePathChecker extends PathFinder {
    private static final SimplePathChecker SIMPLE_PATH_CHECKER = new SimplePathChecker();

    @Override
    public Path find(Mob source, Position end, int targetWidth, int targetLength) {
        /* empty path */
        return new Path(null);
    }

    public static boolean checkLine(Interactable source, Interactable target) {
        Position targetPosition = Utility.findBestInside(source, target);
        return SIMPLE_PATH_CHECKER.check(source.getPosition(), targetPosition, source.width(), false);
    }

    public static boolean checkLine(Interactable source, Position target) {
        return SIMPLE_PATH_CHECKER.check(source.getPosition(), target, source.width(), false);
    }

    public static boolean checkProjectile(Interactable source, Interactable target) {
        Position targetPosition = Utility.findBestInside(source, target);
        return SIMPLE_PATH_CHECKER.check(source.getPosition(), targetPosition, Math.max(1, Math.max(source.width(), source.length())), true);
    }

    /**
     * Determines if the projectile can reach it's destination.
     *
     * @param start      The projectile's starting Position.
     * @param end        The projectile's ending Position.
     * @param projectile The condition if the check is meant for projectiles.
     * @return {@code true} if the projectile can reach it's destination, {@code
     * false} otherwise.
     */
    private boolean check(Position start, Position end, int size, boolean projectile) {
        int dx = end.getX() - start.getX();
        int dy = end.getY() - start.getY();
        int xSignum = Integer.signum(dx);
        int ySignum = Integer.signum(dy);
        int height = start.getHeight();
        int nextX = start.getX();
        int nextY = start.getY();
        int currentX = nextX;
        int currentY = nextY;

        while (true) {
            if (dx == 0) {
                nextY += ySignum;
            } else if (dy == 0) {
                nextX += xSignum;
            } else if (nextX != end.getX()) {
                int pointSlope = (nextX + xSignum - start.getX()) * dy / dx + start.getY();

                if (Math.abs(pointSlope - currentY) > 1) {
                    nextY += ySignum;
                } else {
                    nextX += xSignum;
                    nextY = pointSlope;
                }
            }

            Position current = new Position(currentX, currentY, height);
            Position next = new Position(nextX, nextY, height);

            if (projectile && !projectileCheck(current, next)) {
                return false;
            }

            if (!projectile && !traversable(current, next, size)) {
                return false;
            }

            if (nextX == end.getX() && nextY == end.getY()) {
                break;
            }

            currentX = nextX;
            currentY = nextY;
        }
        return true;
    }

}