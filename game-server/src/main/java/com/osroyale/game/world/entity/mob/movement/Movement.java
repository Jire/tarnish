package com.osroyale.game.world.entity.mob.movement;

import com.osroyale.game.world.Interactable;
import com.osroyale.game.world.entity.mob.Direction;
import com.osroyale.game.world.entity.mob.Mob;
import com.osroyale.game.world.entity.mob.data.PacketType;
import com.osroyale.game.world.pathfinding.distance.Manhattan;
import com.osroyale.game.world.pathfinding.path.Path;
import com.osroyale.game.world.pathfinding.path.impl.AStarPathFinder;
import com.osroyale.game.world.pathfinding.path.impl.DijkstraPathFinder;
import com.osroyale.game.world.pathfinding.path.impl.SimplePathFinder;
import com.osroyale.game.world.position.Position;
import com.osroyale.net.packet.out.SendConfig;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * Handles the movement for the player.
 *
 * @author Graham Edgecombe
 */
public class Movement {
    /** The maximum size of the queue. If there are more points than this size, they are discarded. */
    private static final int MAXIMUM_SIZE = 50;

    /** The smart path finder. */
    private final AStarPathFinder smartPathFinder;

    /** The smart path finder. */
    private static final SimplePathFinder SIMPLE_PATH_FINDER = new SimplePathFinder();

    /** The smart path finder. */
    private static final DijkstraPathFinder DIJKSTRA_PATH_FINDER = new DijkstraPathFinder();

    /** The mob. */
    private Mob mob;

    /** The queue of waypoints. */
    private Deque<Point> waypoints = new ArrayDeque<>();

    /** The last direction the mob walked in. Default to south */
    public Direction lastDirection = Direction.SOUTH;

    /** Mob is moving. */
    private boolean isMoving;

    /** Run toggle (button in client). */
    private boolean runToggled = false;

    /** Run for this queue (CTRL-CLICK) toggle. */
    private boolean runQueue = false;

    private int walkingDirection = -1;
    private int runningDirection = -1;

    /** Creates the <code>WalkingQueue</code> for the specified */
    public Movement(Mob mob) {
        this.mob = mob;
        this.smartPathFinder = new AStarPathFinder(mob, new Manhattan());
    }

    /** Walks to a certain position. */
    public void walk(Position position) {
        reset();
        addStep(position.getX(), position.getY());
        finish();
    }

    /** Handles mob walking to certain coordinates. */
    public void walkTo(int x, int y) {
        final int newX = mob.getX() + x;
        final int newY = mob.getY() + y;
        reset();
        addStepInternal(newX, newY);
        finish();
    }

    /** Handles mob walking to a certain position. */
    public void walkTo(Position position) {
        reset();
        addStepInternal(position.getX(), position.getY());
        finish();
    }

    /** Sets the run toggled flag. */
    public void setRunningToggled(boolean runToggled) {
        this.runToggled = runToggled;
        if (mob.isPlayer()) {
            mob.getPlayer().send(new SendConfig(152, runToggled ? 1 : 0));
        }
    }

    /** Resets the walking queue so it contains no more steps. */
    public void reset() {
        runQueue = false;
        waypoints.clear();
        waypoints.add(new Point(mob.getX(), mob.getY(), -1));
    }

    /** Removes the first waypoint which is only used for calculating directions. This means walking begins at the correct time. */
    public void finish() {
        waypoints.removeFirst();
    }

    /** Adds a single step to the walking queue, filling in the points to the previous point in the queue if necessary. */
    public void addStep(int x, int y) {
//        if (mob.locked()) return;
        if (waypoints.size() == 0)
            reset();
        Point last = waypoints.peekLast();
        int diffX = x - last.x;
        int diffY = y - last.y;
        int max = Math.max(Math.abs(diffX), Math.abs(diffY));
        for (int i = 0; i < max; i++) {
            if (diffX < 0) {
                diffX++;
            } else if (diffX > 0) {
                diffX--;
            }
            if (diffY < 0) {
                diffY++;
            } else if (diffY > 0) {
                diffY--;
            }
            addStepInternal(x - diffX, y - diffY);
        }
    }

    /**
     * Adds a single step to the queue internally without counting gaps. This
     * method is unsafe if used incorrectly so it is private to protect the
     * queue.
     */
    private void addStepInternal(int x, int y) {
        if (waypoints.size() >= MAXIMUM_SIZE)
            return;
        Point last = waypoints.peekLast();
        int diffX = x - last.x;
        int diffY = y - last.y;
        int dir = Direction.direction(diffX, diffY);
        if (dir > -1)
            waypoints.add(new Point(x, y, dir));
    }

    /** Processes the next player's movement. */
    public void processNextMovement() {
        boolean teleporting = mob.teleportTarget != null;
        if (teleporting) {
            reset();
            mob.positionChange = true;
            mob.setPosition(mob.teleportTarget);
            mob.clearTeleportTarget();
        } else {
            Point walkPoint, runPoint = null;
            walkPoint = getNextPoint();

            if (runToggled || runQueue) {
                runPoint = getNextPoint();
            }

            int walkDir = walkPoint == null ? -1 : walkPoint.dir;
            int runDir = runPoint == null ? -1 : runPoint.dir;
            if (runDir != -1) lastDirection = Direction.DIRECTIONS.get(runDir);
            else if (walkDir != -1) lastDirection = Direction.DIRECTIONS.get(walkDir);
            this.walkingDirection = walkDir;
            this.runningDirection = runDir;
        }
        int diffX = mob.getPosition().getLocalX(mob.lastPosition);
        int diffY = mob.getPosition().getLocalY(mob.lastPosition);
        boolean changed = false;
        if (diffX < 16) {
            changed = true;
        } else if (diffX >= 88) {
            changed = true;
        }
        if (diffY < 16) {
            changed = true;
        } else if (diffY >= 88) {
            changed = true;
        }
        if (changed)
            mob.regionChange = true;

//        if (mob.attributes.has("mob-following")) {
//            Waypoint waypoint = mob.attributes.get("mob-following");
//            waypoint.onChange();
//        }
    }

    /** Gets the next point of movement. */
    private Point getNextPoint() {
        Point p = waypoints.poll();
        if (p == null || p.dir == -1) {
            if (isMoving)
                isMoving = false;
            return null;
        } else {
            int diffX = Direction.DELTA_X[p.dir];
            int diffY = Direction.DELTA_Y[p.dir];
            mob.setPosition(mob.getPosition().transform(diffX, diffY));
            if (!isMoving)
                isMoving = true;
            mob.onStep();
            return p;
        }
    }

    /** Finds a smart path to the target. */
    public boolean simplePath(Position destination) {
        return !mob.locking.locked(PacketType.MOVEMENT) && addPath(SIMPLE_PATH_FINDER.find(mob, destination));
    }

    /** Finds a medium path to the target. */
    public boolean dijkstraPath(Position destination) {
        return !mob.locking.locked(PacketType.MOVEMENT) && addPath(DIJKSTRA_PATH_FINDER.find(mob, destination));
    }

    /** Finds a smart path to the target. */
    public boolean aStarPath(Position destination) {
        return !mob.locking.locked(PacketType.MOVEMENT) && addPath(smartPathFinder.find(mob, destination));
    }

    /** Finds a smart path to the target. */
    public boolean simplePath(Interactable interactable) {
        return !mob.locking.locked(PacketType.MOVEMENT) && addPath(SIMPLE_PATH_FINDER.find(mob, interactable));
    }

    /** Finds a medium path to the target. */
    public boolean dijkstraPath(Interactable interactable) {
        return !mob.locking.locked(PacketType.MOVEMENT) && addPath(DIJKSTRA_PATH_FINDER.find(mob, interactable));
    }

    /** Finds a smart path to the target. */
    public boolean aStarPath(Interactable interactable) {
        return !mob.locking.locked(PacketType.MOVEMENT) && addPath(smartPathFinder.find(mob, interactable));
    }

    /** Finds a smart path to the target. */
    public boolean addPath(Path path) {
        if (!path.isPossible() || mob.locking.locked(PacketType.MOVEMENT))
            return false;
        path.addSteps(this);
        return true;
    }

    /** Sets the run queue flag. */
    public void setRunningQueue(boolean runQueue) {
        this.runQueue = runQueue;
    }

    /** Gets the run toggled flag. */
    public boolean isRunningToggled() {
        return runToggled;
    }

    /** Gets the running queue flag. */
    public boolean isRunningQueue() {
        return runQueue;
    }

    /** Checks if any running flag is set. */
    public boolean isRunning() {
        return runToggled || runQueue;
    }

    public boolean isMoving() {
        return isMoving;
    }

    public boolean isMovementDone() {
        return waypoints.size() == 0;
    }

    public boolean hasSteps() {
        return !waypoints.isEmpty();
    }

    public boolean needsPlacement() {
        return isMoving || hasSteps();
    }

    public AStarPathFinder getSmartPathFinder() {
        return smartPathFinder;
    }

    public int getWalkingDirection() {
        return walkingDirection;
    }

    public int getRunningDirection() {
        return runningDirection;
    }

}
