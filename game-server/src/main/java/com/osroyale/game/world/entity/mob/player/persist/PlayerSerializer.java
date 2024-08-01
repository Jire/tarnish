package com.osroyale.game.world.entity.mob.player.persist;

import com.osroyale.Config;
import com.osroyale.game.world.entity.mob.player.Player;
import com.osroyale.net.codec.login.LoginResponse;

public final class PlayerSerializer {

    private static final PlayerPersistable perstable = Config.FORUM_INTEGRATION
            ? new PlayerPersistDB()
            : new PlayerPersistFile();

    public static void save(Player player) {
        if (player.isBot) {
            return;
        }

        // player save thread
        Thread.startVirtualThread(() -> {
            try {
                perstable.save(player);
            } catch (final Exception e) {
                e.printStackTrace();
            }
        });
    }

    public static LoginResponse load(Player player, String expectedPassword) {
        if (player.isBot) {
            return LoginResponse.COULD_NOT_COMPLETE_LOGIN;
        }

        return perstable.load(player, expectedPassword);
    }
}
