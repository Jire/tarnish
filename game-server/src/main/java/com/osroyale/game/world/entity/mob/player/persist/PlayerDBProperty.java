package com.osroyale.game.world.entity.mob.player.persist;

import com.jcabi.jdbc.JdbcSession;
import com.osroyale.game.world.entity.mob.player.Player;

import java.sql.SQLException;

public abstract class PlayerDBProperty {

    private final String name;

    public PlayerDBProperty(String name) {
        this.name = name;
    }

    abstract void read(Player player, JdbcSession session) throws SQLException;

    abstract void write(Player player, JdbcSession session) throws SQLException;

    public String getName() {
        return name;
    }

}
