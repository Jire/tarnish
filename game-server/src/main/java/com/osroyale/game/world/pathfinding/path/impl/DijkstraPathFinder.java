package com.osroyale.game.world.pathfinding.path.impl;

import com.osroyale.game.world.Interactable;
import com.osroyale.game.world.entity.mob.Mob;
import com.osroyale.game.world.pathfinding.path.Path;
import com.osroyale.game.world.pathfinding.path.PathFinder;
import com.osroyale.game.world.position.Position;
import com.osroyale.game.world.region.Region;
import com.osroyale.util.Utility;

import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;

import static com.osroyale.game.world.entity.mob.Direction.*;

public final class DijkstraPathFinder extends PathFinder {

    @Override
    public Path find(Mob source, Position target, int targetWidth, int targetLength) {
        final Deque<Position> path = new LinkedList<>();

        final int distance = source.getPosition().getChebyshevDistance(target);
        if (distance > Region.SIZE) {
            return new Path(path); // don't allow pathfinding too far distances away to prevent easily abusing smart pathfinder.
        }

        Position targ = target;
        target = Utility.findBestInside(source, Interactable.create(target, targetWidth, targetLength));

        if (source.getPosition().equals(target)) {
            path.add(source.getPosition());
            return new Path(path);
        }

        int tail = 0;
        int[][] via = new int[104][104];
        int[][] cost = new int[104][104];
        int regionX = source.getPosition().getChunkX() << 3;
        int regionY = source.getPosition().getChunkY() << 3;
        int curX = source.getPosition().getLocalX();
        int curY = source.getPosition().getLocalY();
        int destX = target.getX() - regionX;
        int destY = target.getY() - regionY;
        List<Integer> tileQueueX = new ArrayList<>(9000);
        List<Integer> tileQueueY = new ArrayList<>(9000);

        via[curX][curY] = 99;
        cost[curX][curY] = 1;
        tileQueueX.add(curX);
        tileQueueY.add(curY);

        boolean foundPath = false;
        int pathLength = 4096;

        while (tail != tileQueueX.size() && tileQueueX.size() < pathLength) {

            curX = tileQueueX.get(tail);
            curY = tileQueueY.get(tail);

            int curAbsX = regionX + curX;
            int curAbsY = regionY + curY;

            if (curX == destX && curY == destY) {
                foundPath = true;
                break;
            }

            Position position = Position.create(curAbsX, curAbsY, source.getHeight());
            if (targetWidth > 0 && targetLength > 0 && Region.reachable(targ, targetWidth, targetLength, position, source.width(), source.length())) {
                foundPath = true;
                break;
            }

            int thisCost = cost[curX][curY] + 1 + 1;
            tail = (tail + 1) % pathLength;

            if (curY > 0 && via[curX][curY - 1] == 0
                    && traversable(position, source.width(), SOUTH)) {
                tileQueueX.add(curX);
                tileQueueY.add(curY - 1);
                via[curX][curY - 1] = 1;
                cost[curX][curY - 1] = thisCost;
            }

            if (curX > 0 && via[curX - 1][curY] == 0
                    && traversable(position, source.width(), WEST)) {
                tileQueueX.add(curX - 1);
                tileQueueY.add(curY);
                via[curX - 1][curY] = 2;
                cost[curX - 1][curY] = thisCost;
            }

            if (curY < 104 - 1 && via[curX][curY + 1] == 0
                    && traversable(position, source.width(), NORTH)) {
                tileQueueX.add(curX);
                tileQueueY.add(curY + 1);
                via[curX][curY + 1] = 4;
                cost[curX][curY + 1] = thisCost;
            }

            if (curX < 104 - 1 && via[curX + 1][curY] == 0
                    && traversable(position, source.width(), EAST)) {
                tileQueueX.add(curX + 1);
                tileQueueY.add(curY);
                via[curX + 1][curY] = 8;
                cost[curX + 1][curY] = thisCost;
            }

            if (curX > 0 && curY > 0 && via[curX - 1][curY - 1] == 0
                    && traversable(position, source.width(), SOUTH_WEST)
                    && traversable(position, source.width(), SOUTH)
                    && traversable(position, source.width(), WEST)) {
                tileQueueX.add(curX - 1);
                tileQueueY.add(curY - 1);
                via[curX - 1][curY - 1] = 3;
                cost[curX - 1][curY - 1] = thisCost;
            }

            if (curX > 0 && curY < 104 - 1 && via[curX - 1][curY + 1] == 0
                    && traversable(position, source.width(), NORTH_WEST)
                    && traversable(position, source.width(), NORTH)
                    && traversable(position, source.width(), WEST)) {
                tileQueueX.add(curX - 1);
                tileQueueY.add(curY + 1);
                via[curX - 1][curY + 1] = 6;
                cost[curX - 1][curY + 1] = thisCost;
            }

            if (curX < 104 - 1 && curY > 0 && via[curX + 1][curY - 1] == 0
                    && traversable(position, source.width(), SOUTH_EAST)
                    && traversable(position, source.width(), SOUTH)
                    && traversable(position, source.width(), EAST)) {
                tileQueueX.add(curX + 1);
                tileQueueY.add(curY - 1);
                via[curX + 1][curY - 1] = 9;
                cost[curX + 1][curY - 1] = thisCost;
            }

            if (curX < 104 - 1 && curY < 104 - 1 && via[curX + 1][curY + 1] == 0
                    && traversable(position, source.width(), NORTH_EAST)
                    && traversable(position, source.width(), NORTH)
                    && traversable(position, source.width(), EAST)) {
                tileQueueX.add(curX + 1);
                tileQueueY.add(curY + 1);
                via[curX + 1][curY + 1] = 12;
                cost[curX + 1][curY + 1] = thisCost;
            }
        }

        if (!foundPath) {
            int hippo_max = 1_000;
            int thisCost = 100 + 1;
            int init_x = 10;

            for (int x = destX - init_x; x <= destX + init_x; x++) {
                for (int y = destY - init_x; y <= destY + init_x; y++) {
                    if (x >= 0 && y >= 0 && x < 104 && y < 104 && cost[x][y] < 100 && cost[x][y] != 0) {
                        int dx = 0;
                        if (x < destX) {
                            dx = destX - x;
                        } else if (x > destX + source.width() - 1) {
                            dx = x - (destX + source.width() - 1);
                        }
                        int dy = 0;
                        if (y < destY) {
                            dy = destY - y;
                        } else if (y > destY + source.length() - 1) {
                            dy = y - (destY + source.length() - 1);
                        }
                        int hippo = dx * dx + dy * dy;
                        if (hippo < hippo_max || hippo == hippo_max && cost[x][y] < thisCost && cost[x][y] != 0) {
                            hippo_max = hippo;
                            thisCost = cost[x][y];
                            curX = x;
                            curY = y;
                        }
                    }
                }
            }

            if (hippo_max == 1000) {
                return new Path(null);
            }
        }

        tail = 0;
        tileQueueX.set(tail, curX);
        tileQueueY.set(tail++, curY);
        int vurVia;

        for (int nextVia = vurVia = via[curX][curY]; curX != source.getPosition().getLocalX() || curY != source.getPosition().getLocalY(); nextVia = via[curX][curY]) {
            if (nextVia != vurVia) {
                vurVia = nextVia;
                tileQueueX.set(tail, curX);
                tileQueueY.set(tail++, curY);
            }
            if ((nextVia & 2) != 0) {
                curX++;
            } else if ((nextVia & 8) != 0) {
                curX--;
            }
            if ((nextVia & 1) != 0) {
                curY++;
            } else if ((nextVia & 4) != 0) {
                curY--;
            }
        }

        int s = tail--;
        int pathX = (regionX) + tileQueueX.get(tail);
        int pathY = (regionY) + tileQueueY.get(tail);

        path.add(Position.create(pathX, pathY));

        for (int i = 1; i < s; i++) {
            tail--;
            pathX = (regionX) + tileQueueX.get(tail);
            pathY = (regionY) + tileQueueY.get(tail);
            path.add(Position.create(pathX, pathY));
        }

        return new Path(path);
    }

}
