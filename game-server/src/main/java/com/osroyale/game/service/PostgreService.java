package com.osroyale.game.service;

import com.osroyale.Config;
import com.osroyale.game.world.WorldType;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public final class PostgreService {

    private static final Logger logger = LogManager.getLogger();

    private static HikariDataSource connectionPool;

    public static void start() throws Exception {
        HikariConfig config = new HikariConfig();
        config.setDriverClassName("org.postgresql.Driver");
        config.setJdbcUrl(Config.POSTGRE_URL);
        config.setUsername(Config.POSTGRE_USER);
        config.setPassword(Config.POSTGRE_PASS);
        config.setMaximumPoolSize(50);
        config.setConnectionTimeout(10_000);
        config.setIdleTimeout(0);
        config.setMaxLifetime(0);
        config.addDataSourceProperty("cachePrepStmts", "true");
        connectionPool = new HikariDataSource(config);
        logger.info("Successfully connected to game database.");
    }

    public static HikariDataSource getConnectionPool() {
        return connectionPool;
    }

    public static Connection getConnection() throws SQLException {
        if (Config.WORLD_TYPE != WorldType.LIVE) {
            return DriverManager.getConnection(Config.POSTGRE_URL, Config.POSTGRE_USER, Config.POSTGRE_PASS);
        }
        return connectionPool.getConnection();
    }

}
