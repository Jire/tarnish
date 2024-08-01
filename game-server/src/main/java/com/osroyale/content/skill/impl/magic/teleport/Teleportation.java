package com.osroyale.content.skill.impl.magic.teleport;

import com.osroyale.content.activity.Activity;
import com.osroyale.content.lms.LMSGame;
import com.osroyale.content.lms.lobby.LMSLobby;
import com.osroyale.game.action.impl.TeleportAction;
import com.osroyale.game.world.entity.mob.Mob;
import com.osroyale.game.world.entity.mob.data.PacketType;
import com.osroyale.game.world.entity.mob.player.Player;
import com.osroyale.game.world.entity.mob.player.PlayerRight;
import com.osroyale.game.world.position.Position;
import com.osroyale.net.packet.out.SendMessage;

/**
 * Handles a player teleporting.
 *
 * @author Daniel
 */
public class Teleportation {


    public static void activateOverride(Mob mob, Position position, TeleportationData teleport) {
        mob.action.execute(new TeleportAction(mob, position, teleport, () -> {}), true);
    }

    public static void activateOverride(Mob mob, Position position, TeleportationData teleport, Runnable runnable) {
        mob.action.execute(new TeleportAction(mob, position, teleport, runnable), true);
    }

    public static boolean teleport(Player player, Position position) {
        return teleport(player, position, 20, TeleportationData.MODERN, () -> {});
    }

    public static boolean teleport(Player player, Position position, TeleportationData teleport, Runnable runnable) {
        return teleport(player, position, 20, teleport, runnable);
    }

    public static boolean teleport(Player player, Position position, TeleportationData teleport) {
        return teleport(player, position, 20, teleport, () -> {});
    }

    public static boolean teleport(Player player, Position position, int wilderness) {
        return teleport(player, position, wilderness, TeleportationData.MODERN, () -> {});
    }

    public static boolean teleport(Player player, Position position, int wilderness, Runnable runnable) {
        return teleport(player, position, wilderness, TeleportationData.MODERN, runnable);
    }

    public static boolean teleport(Player player, Position position, int wilderness, TeleportationData teleport) {
        return teleport(player, position, wilderness, teleport, () -> {});
    }

    public static boolean teleport(Player player, Position position, int wilderness, TeleportationData teleport, Runnable runnable) {
        if(player.isGambleLocked()) return false;

        player.interfaceManager.close(false);

        if (Activity.evaluate(player, it -> !it.canTeleport(player))) {
            return false;
        }

        if(LMSLobby.lobbyMembers.contains(player) || LMSGame.isActivePlayer(player)) {
            return false;
        }

        if (player.locking.locked(PacketType.TELEPORT)) {
            return false;
        }

        if (player.isTeleblocked()) {
            player.message("You are currently under the affects of a teleblock spell and can not teleport!");
            return false;
        }

        if (player.wilderness > wilderness && !PlayerRight.isDeveloper(player)) {
            player.send(new SendMessage("You can't teleport past " + wilderness + " wilderness!"));
            return false;
        }

        if (teleport != TeleportationData.HOME) {
            player.getCombat().reset();
            player.damageImmunity.reset(3_000);
        }

        if (teleport != TeleportationData.TABLET && player.pvpInstance) {
            player.pvpInstance = false;
            player.instance = Mob.DEFAULT_INSTANCE;
            if (player.pet != null) {
                player.pet.instance = player.instance;
            }
        }

        player.action.execute(new TeleportAction(player, position, teleport, runnable), true);
        return true;
    }
}
