package com.osroyale.game.service;

import com.osroyale.Config;
import com.osroyale.game.world.WorldType;
import com.osroyale.game.world.entity.mob.player.Player;
import com.osroyale.game.world.entity.mob.player.PlayerRight;
import com.osroyale.game.world.entity.skill.Skill;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public final class HighscoreService {

    private static final Logger logger = LogManager.getLogger();

    private static final String CONNECTION_STRING = "jdbc:mysql://45.88.231.118:3306/hiscores";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "bQ9R#UnPsW5^HLiU9$4LcJvE4%ZwJWLz";

    public static void saveHighscores(Player player) {
        if (player == null || Config.WORLD_TYPE != WorldType.LIVE || PlayerRight.isAdministrator(player)) {
            return;
        }

        try (Connection connection = DriverManager.getConnection(CONNECTION_STRING, USERNAME, PASSWORD);
             PreparedStatement dsta = connection.prepareStatement("DELETE FROM hs_users WHERE id = ?");
             PreparedStatement ista = connection.prepareStatement(generateQuery())) {

            dsta.setInt(1, player.getMemberId());
            dsta.execute();

            ista.setInt(1, player.getMemberId());
            ista.setString(2, player.getName());
            ista.setInt(3, getRank(player.right));
            ista.setInt(4, getRank(player.right));
            ista.setInt(5, player.prestige.totalPrestige);

            for (int x = 0; x < Skill.SKILL_COUNT; x++) {
                ista.setInt(6 + x, player.prestige.prestige[x]);
            }

            ista.setInt(29, player.skills.getTotalLevel());
            ista.setLong(30, player.skills.getTotalXp());

            for (int i = 0; i < Skill.SKILL_COUNT; i++) {
                ista.setInt(31 + i, player.skills.get(i).getRoundedExperience());
            }

            ista.execute();
        } catch (SQLException ex) {
            logger.error(String.format("Failed to save highscores for player=%s", player.getName()), ex);
        }
    }

    private static int getRank(PlayerRight right) {
        if (right == PlayerRight.ULTIMATE_IRONMAN)
            return 3;
        if (right == PlayerRight.HARDCORE_IRONMAN)
            return 2;
        if (right == PlayerRight.IRONMAN)
            return 1;
        return 0;
    }

    private static String generateQuery() {
        StringBuilder sb = new StringBuilder();
        sb.append("INSERT INTO hs_users (");
        sb.append("id, ");
        sb.append("username, ");
        sb.append("rights, ");
        sb.append("mode, ");
        sb.append("total_prestiges, ");
        sb.append("attack_prestiges, ");
        sb.append("defence_prestiges, ");
        sb.append("strength_prestiges, ");
        sb.append("hitpoints_prestiges, ");
        sb.append("ranged_prestiges, ");
        sb.append("prayer_prestiges, ");
        sb.append("magic_prestiges, ");
        sb.append("cooking_prestiges, ");
        sb.append("woodcutting_prestiges, ");
        sb.append("fletching_prestiges, ");
        sb.append("fishing_prestiges, ");
        sb.append("firemaking_prestiges, ");
        sb.append("crafting_prestiges, ");
        sb.append("smithing_prestiges, ");
        sb.append("mining_prestiges, ");
        sb.append("herblore_prestiges, ");
        sb.append("agility_prestiges, ");
        sb.append("thieving_prestiges, ");
        sb.append("slayer_prestiges, ");
        sb.append("farming_prestiges, ");
        sb.append("runecrafting_prestiges, ");
        sb.append("hunter_prestiges, ");
        sb.append("construction_prestiges,");
        sb.append("total_level, ");
        sb.append("overall_xp, ");
        sb.append("attack_xp, ");
        sb.append("defence_xp, ");
        sb.append("strength_xp, ");
        sb.append("hitpoints_xp, ");
        sb.append("ranged_xp, ");
        sb.append("prayer_xp, ");
        sb.append("magic_xp, ");
        sb.append("cooking_xp, ");
        sb.append("woodcutting_xp, ");
        sb.append("fletching_xp, ");
        sb.append("fishing_xp, ");
        sb.append("firemaking_xp, ");
        sb.append("crafting_xp, ");
        sb.append("smithing_xp, ");
        sb.append("mining_xp, ");
        sb.append("herblore_xp, ");
        sb.append("agility_xp, ");
        sb.append("thieving_xp, ");
        sb.append("slayer_xp, ");
        sb.append("farming_xp, ");
        sb.append("runecrafting_xp, ");
        sb.append("hunter_xp, ");
        sb.append("construction_xp)");
        sb.append("VALUES (" +
                "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, " +
                "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, " +
                "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, " +
                "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, " +
                "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, " +
                "?, ?, ?)");
        return sb.toString();
    }

    private HighscoreService() {

    }

}