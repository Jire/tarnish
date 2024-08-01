package com.osroyale.content;

import com.osroyale.game.world.entity.mob.player.Player;
import com.osroyale.game.world.position.Area;
import com.osroyale.net.packet.out.SendMessage;

public class Waypoints {

    public static void setWaypoint(Player player, String waypointName) {
        if(Area.inRestrictedArea(player)) {
            player.message("You cannot set a waypoint here");
            return;
        }
        if (player.waypoints.size() >= getAllowedPoints(player)) {
            player.message("You have reached the maximum amount of waypoints allowed for your rank.");
            return;
        }
        if (player.waypoints.containsKey(waypointName)) {
            player.message("You already have a waypoint with this name.");
            return;
        }
        player.waypoints.put(waypointName, player.getPosition().copy());
        player.dialogueFactory.sendStatement("You have set a waypoint named " + waypointName + " at your current location.").execute();
    }

    public static void removeWaypoint (Player player, String waypointName) {
        player.waypoints.remove(waypointName);
        player.dialogueFactory.sendStatement("You have removed " + waypointName + " from your waypoints.").execute();
    }
    public static void getWaypoints(Player player) {
        if (player.waypoints.isEmpty()) {
            player.message("You have no waypoints set.");
            return;
        }
        player.message("Current waypoints:");
        player.waypoints.forEach((k, v) -> player.message("@red@" +k));
    }
    private static int getAllowedPoints(Player player) {
        return switch (player.right.getName()) {
            case "King Donator" -> 10;
            case "Elite Donator" -> 8;
            case "Extreme Donator" -> 6;
            case "Super Donator" -> 4;
            case "Donator" -> 2;
            default -> 5;
        };
    }

}