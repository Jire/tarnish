package com.osroyale.game.world.entity.mob.player.persist;

import com.osroyale.game.world.entity.mob.player.Player;
import com.osroyale.net.codec.login.LoginResponse;

public interface PlayerPersistable {

    void save(Player player);

    LoginResponse load(Player player, String expectedPassword);

}
