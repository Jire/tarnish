package com.osroyale.game.world.position;

import com.osroyale.game.world.World;
import com.osroyale.game.world.entity.mob.Mob;
import com.osroyale.game.world.entity.mob.player.Player;

import java.util.*;

/**
 * Created by Daniel on 2017-11-24.
 */
public class Boundary {

    int minX, minY, highX, highY;
    int height;

    public Boundary(int minX, int minY, int highX, int highY) {
        this.minX = minX;
        this.minY = minY;
        this.highX = highX;
        this.highY = highY;
        height = -1;
    }

    public Boundary(int minX, int minY, int highX, int highY, int height) {
        this.minX = minX;
        this.minY = minY;
        this.highX = highX;
        this.highY = highY;
        this.height = height;
    }

    public int getMinimumX() {
        return minX;
    }

    public int getMinimumY() {
        return minY;
    }

    public int getMaximumX() {
        return highX;
    }

    public int getMaximumY() {
        return highY;
    }

    public static boolean isIn(Mob mob, Boundary... boundaries) {
        for (Boundary b : boundaries) {
            if (b.height >= 0) {
                if (mob.getHeight() != b.height)
                    continue;
            }
            if (mob.getX() >= b.minX && mob.getX() <= b.highX && mob.getY() >= b.minY && mob.getY() <= b.highY)
                return true;
        }
        return false;
    }

    public static boolean isIn(int x, int y, int z, Boundary boundaries) {
        if (boundaries.height >= 0) {
            if (z != boundaries.height) {
                return false;
            }
        }
        return x >= boundaries.minX && x <= boundaries.highX && y >= boundaries.minY && y <= boundaries.highY;
    }

    public static List<Player> getPlayers(Boundary boundary) {
        List<Player> list = new ArrayList<>();
        for (Player player : World.getPlayers()) {
            if (player != null && isIn(player, boundary))
                list.add(player);
        }
        return list;
    }

    public static boolean isInSameBoundary(Player player1, Player player2, Boundary[] boundaries) {
        Optional<Boundary> boundary1 = Arrays.stream(boundaries).filter(b -> isIn(player1, b)).findFirst();
        Optional<Boundary> boundary2 = Arrays.stream(boundaries).filter(b -> isIn(player2, b)).findFirst();
        if (!boundary1.isPresent() || !boundary2.isPresent()) {
            return false;
        }
        return Objects.equals(boundary1.get(), boundary2.get());
    }

}
