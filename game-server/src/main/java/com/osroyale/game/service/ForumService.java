package com.osroyale.game.service;

import com.osroyale.Config;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.SQLException;

public final class ForumService {

    private static final Logger logger = LogManager.getLogger();

    private static HikariDataSource connectionPool;

    public static void start() throws Exception {
        HikariConfig config = new HikariConfig();
        config.setDriverClassName("com.mysql.jdbc.Driver");
        config.setJdbcUrl(Config.FORUM_DB_URL);
        config.setUsername(Config.FORUM_DB_USER);
        config.setPassword(Config.FORUM_DB_PASS);
        config.setMaximumPoolSize(10);
        config.setConnectionTimeout(5_000);
        config.setIdleTimeout(0);
        config.setMaxLifetime(0);
        config.addDataSourceProperty("cachePrepStmts", "true");
        connectionPool = new HikariDataSource(config);
        logger.info("Successfully connected to forum database.");
    }

    public static HikariDataSource getConnectionPool() {
        return connectionPool;
    }

    public static Connection getConnection() throws SQLException {
        return connectionPool.getConnection();
    }

}
