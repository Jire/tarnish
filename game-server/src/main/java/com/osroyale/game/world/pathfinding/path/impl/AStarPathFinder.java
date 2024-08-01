package com.osroyale.game.world.pathfinding.path.impl;

import com.osroyale.game.world.Interactable;
import com.osroyale.game.world.entity.mob.Direction;
import com.osroyale.game.world.entity.mob.Mob;
import com.osroyale.game.world.pathfinding.distance.Distance;
import com.osroyale.game.world.pathfinding.path.Path;
import com.osroyale.game.world.pathfinding.path.PathFinder;
import com.osroyale.game.world.position.Position;
import com.osroyale.game.world.region.Region;

import java.util.*;

/**
 * Represents a {@code PathFinder} which uses the A* search algorithm(by passing
 * obstacles).
 *
 * @author Graham
 * @author Major | Suggestions, discussion
 * @author Artem Batutin <artembatutin@gmail.com>
 * @author Ryley Kimmel <ryley.kimmel@live.com>
 */
public final class AStarPathFinder extends PathFinder {

    /** The Heuristic used by this {@code PathFinder}. */
    private final Distance heuristic;
    private final Mob character;
    private final Map<Position, Node> nodes = new HashMap<>(Region.SIZE * Region.SIZE);
    private final Set<Node> open = new HashSet<>(Region.SIZE * Region.SIZE);
    private final Queue<Node> sorted = new PriorityQueue<>(Region.SIZE * Region.SIZE);
    private final Deque<Position> shortest = new ArrayDeque<>(100);

    /**
     * Constructs a new {@link AStarPathFinder} with the specified traversal
     * tool.mapviewer.
     */
    public AStarPathFinder(Mob character, Distance heuristic) {
        this.character = character;
        this.heuristic = heuristic;
    }

    /**
     * A default method to find a path for the specified {@link Mob}.
     *
     * @param destination The destination of the path.
     * @return A {@link Deque} of {@link Position steps} to the specified
     * destination.
     */
    public Path find(Position destination) {
        Position origin = character.getPosition();
        if (origin.getHeight() != destination.getHeight())
            return new Path(null);
        return find(character, destination);
    }

    /**
     * A default method to find a path for the specified {@link Mob}.
     *
     * @param destination The destination of the path.
     * @return A {@link Deque} of {@link Position steps} to the specified
     * destination.
     */
    public Path find(Interactable destination) {
        Position origin = character.getPosition();
        if (origin.getHeight() != destination.getHeight())
            return new Path(null);
        return find(character, destination);
    }

    /**
     * Performs the path finding calculations to find the path using the A*
     * algorithm.
     *
     *
     * @param source
     * @param target The target Position.
     * @return The path to pursue to reach the destination.
     */
    @Override
    public Path find(Mob source, Position target, int targetWidth, int targetLength) {
        if (source.getHeight() != target.getHeight())
            return new Path(null);

        Node start = new Node(source.getPosition()), end = new Node(target);

        open.clear();
        nodes.clear();
        sorted.clear();
        shortest.clear();

        nodes.put(source.getPosition(), start);
        nodes.put(target, end);

        open.add(start);
        sorted.add(start);

        Node active;
        Position next;
        boolean found = false;
        int distance = (int) (source.getPosition().getDistance(target) * Region.SIZE);

        do {
            active = getCheapest(sorted);
            Position position = active.getPosition();

            if (position.equals(target)) {
                found = true;
                break;
            }

            open.remove(active);
            active.close();

            for (Direction direction : Direction.valid()) {
                next = position.transform(direction.getFaceLocation());
                if (traversable(position, source.width(), direction) && traversable(next, source.width(), Direction.getOppositeDirection(direction))) {
                    Node node = nodes.computeIfAbsent(next, Node::new);
                    compare(active, node);
                }
            }
        } while (!open.isEmpty() && sorted.size() < distance);

        if (found) {

            if (end.hasParent()) {
                active = end;
            }

            if (active.hasParent()) {
                Position position = active.getPosition();

                while (!source.getPosition().equals(position)) {
                    shortest.addFirst(position);
                    active = active.getParent();
                    position = active.getPosition();

                    if (shortest.size() >= 100) {
                        break;
                    }
                }
            }

            return new Path(shortest);
        }
        return new Path(null);
    }

    private void compare(Node active, Node other) {
        int cost = active.getCost() + heuristic.calculate(active.getPosition(), other.getPosition());
        if (other.getCost() > cost) {
            open.remove(other);
            other.close();
        } else if (other.isOpen() && !open.contains(other)) {
            other.setCost(cost);
            other.setParent(active);
            open.add(other);
            sorted.add(other);
        }
    }

    /**
     * Gets the cheapest open {@link Node} from the {@link Queue}.
     *
     * @param nodes The queue of nodes.
     * @return The cheapest node.
     */
    private Node getCheapest(Queue<Node> nodes) {
        Node node = nodes.peek();
        while (!node.isOpen()) {
            nodes.poll();
            node = nodes.peek();
        }
        return node;
    }

}

/**
 * A {@code Entity} representing a weighted {@link Position}.
 *
 * @author Graham
 * @author Major
 * @author Artem Batutin <artembatutin@gmail.com>
 * @author Ryley Kimmel <ryley.kimmel@live.com>
 */
final class Node implements Comparable<Node> {

    /** The cost of this {@code Entity}. */
    private int cost;

    /** Whether or not this {@code Entity} is openShop. */
    private boolean open = true;

    /** The parent {@code Entity} of this Entity. */
    private Node parent;

    /** The Position of this {@code Entity}. */
    private final Position position;

    /**
     * Creates the {@code Entity} with the specified {@link Position} and cost.
     *
     * @param position The Position.
     */
    Node(Position position) {
        this(position, 0);
    }

    /**
     * Creates the {@code Entity} with the specified {@link Position} and cost.
     *
     * @param position The Position.
     * @param cost     The cost of the Entity.
     */
    private Node(Position position, int cost) {
        this.position = position;
        this.cost = cost;
    }

    /**
     * Sets the cost of this Entity.
     *
     * @param cost The cost.
     */
    void setCost(int cost) {
        this.cost = cost;
    }

    /**
     * Gets the cost of this Entity.
     *
     * @return The cost.
     */
    int getCost() {
        return cost;
    }

    /**
     * Closes this Entity.
     */
    public void close() {
        open = false;
    }

    /**
     * Returns whether or not this {@link Node} is openShop.
     *
     * @return {@code true} if this Entity is openShop, otherwise {@code false}.
     */
    public boolean isOpen() {
        return open;
    }

    /**
     * Sets the parent Entity of this Entity.
     *
     * @param parent The parent Entity. May be {@code null}.
     */
    void setParent(Node parent) {
        this.parent = parent;
    }

    /**
     * Gets the parent Entity of this Entity.
     *
     * @return The parent Entity.
     * @throws NoSuchElementException If this Entity does not have a parent.
     */
    Node getParent() {
        return parent;
    }

    /**
     * Returns whether or not this Entity has a parent Entity.
     *
     * @return {@code true} if this Entity has a parent Entity, otherwise {@code
     * false}.
     */
    boolean hasParent() {
        return parent != null;
    }

    /**
     * Gets the {@link Position} this Entity represents.
     *
     * @return The position.
     */
    public Position getPosition() {
        return position;
    }

    /**
     * Compares the {@code Entity}'s cost with another.
     *
     * @param other The other Entity to check.
     * @return The differential Integer.
     */
    @Override
    public int compareTo(Node other) {
        return Integer.compare(cost, other.cost);
    }

    /**
     * Gets the condition if the Entity equals another object.
     *
     * @param obj The object to be checked.
     * @return {@code true} if it's the same as the object, {@code false}
     * otherwise.
     */
    @Override
    public final boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Node)) return false;
        Node other = (Node) obj;
        return getPosition().getX() == other.getPosition().getX()
            && getPosition().getY() == other.getPosition().getY()
            && getPosition().getHeight() == other.getPosition().getHeight()
            && getCost() == other.getCost()
            && getParent().getCost() == other.getParent().getCost()
            && getParent().getPosition().getX() == other.getParent().getPosition().getX()
            && getParent().getPosition().getY() == other.getParent().getPosition().getY()
            && getParent().getPosition().getHeight() == other.getPosition().getHeight();
    }

    /**
     * Gets the node's hash code.
     *
     * @return hash code.
     */
    @Override
    public final int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + position.getX();
        result = prime * result + position.getY();
        result = prime * result + position.getHeight();
        if (hasParent()) {
            Position p = parent.getPosition();
            result = prime * result + p.getX();
            result = prime * result + p.getY();
            result = prime * result + p.getHeight();
        }
        result = prime * result + cost;
        return result;
    }

}

