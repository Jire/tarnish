package com.osroyale.content.achievement;

import com.osroyale.game.world.entity.mob.player.Player;
import com.osroyale.game.world.items.containers.bank.VaultCurrency;
import com.osroyale.net.packet.out.SendAnnouncement;
import com.osroyale.net.packet.out.SendMessage;

/**
 * Handles the achievements.
 *
 * @author Daniel
 */
public class AchievementHandler {

    /**
     * Activates the achievement for the individual player. Increments the
     * completed amount for the player. If the player has completed the
     * achievement, they will receive their reward.
     */
    public static void activate(Player player, AchievementKey achievement) {
        activate(player, achievement, 1);
    }

    /**
     * Activates the achievement for the individual player. Increments the
     * completed amount for the player. If the player has completed the
     * achievement, they will receive their reward.
     */
    public static void activate(Player player, AchievementKey achievement, int increase) {
        final int current = player.playerAchievements.computeIfAbsent(achievement, a -> 0);
        for (AchievementList list : AchievementList.values()) {
            if (list.getKey() == achievement) {
                if (current >= list.getAmount())
                    continue;
                player.playerAchievements.put(achievement, current + increase);
                if (player.playerAchievements.get(achievement) >= list.getAmount()) {
                    player.bankVault.add(VaultCurrency.BLOOD_MONEY, list.getReward().getAmount());
                    player.send(new SendAnnouncement("You've completed the achievement", "'" + list.getTask() + "'", list.getType().getColor()));
                }
            }
        }
    }

    public static void set(Player player, AchievementKey achievement, int increase) {
        final int current = player.playerAchievements.computeIfAbsent(achievement, a -> 0);
        for (AchievementList list : AchievementList.values()) {
            if (list.getKey() == achievement) {
                if (current >= list.getAmount())
                    continue;
                player.playerAchievements.put(achievement, increase);
                if (player.playerAchievements.get(achievement) >= list.getAmount()) {
                    player.bankVault.add(VaultCurrency.BLOOD_MONEY, list.getReward().getAmount());
                    player.send(new SendAnnouncement("You've completed the achievement", "'" + list.getTask() + "'", list.getType().getColor()));
                }
            }
        }
    }

    /** Completes all the achievements for player (used for administrative purposes). */
    public static void completeAll(Player player) {
        if (!completedAll(player)) {
            for (AchievementList achievement : AchievementList.values()) {
                if (!player.playerAchievements.containsKey(achievement.getKey())) {
                    player.playerAchievements.put(achievement.getKey(), achievement.getAmount());
                    continue;
                }
                player.playerAchievements.replace(achievement.getKey(), achievement.getAmount());
            }
            player.send(new SendMessage("You have successfully mastered all achievements."));
        }
    }

    /** Checks if the reward is completed. */
    public static boolean completed(Player player, AchievementList achievement) {
        if (!player.playerAchievements.containsKey(achievement.getKey()))
            player.playerAchievements.put(achievement.getKey(), 0);
        return player.playerAchievements.get(achievement.getKey()) >= achievement.getAmount();
    }

    /** Gets the total amount of achievements completed. */
    public static int getTotalCompleted(Player player) {
        int count = 0;
        for (AchievementList achievement : AchievementList.values()) {
            if (player.playerAchievements.containsKey(achievement.getKey()) && completed(player, achievement)) count++;
        }
        return count;
    }

    static int getDifficultyAmount(AchievementType difficulty) {
        return AchievementList.asList(difficulty).size();
    }

    public static int getColor(Player player, AchievementType difficulty) {
        int count = getDifficultyCompletion(player, difficulty);

        if (count == 0) {
            return 0xFF0000;
        }

        return count == getDifficultyAmount(difficulty) ? 0x257A1A : 0xFFFF00;
    }

    /** Handles getting the amount of achievements completed based on it's difficulty. */
    public static int getDifficultyCompletion(Player player, AchievementType difficulty) {
        int count = 0;
        for (AchievementList achievement : AchievementList.values()) {
            if (player.playerAchievements.containsKey(achievement.getKey()) && achievement.getType() == difficulty && completed(player, achievement))
                count++;
        }
        return count;
    }

    /** Handles getting the amount of achievements based on the difficulty. */
    public static int getDifficultyAchievement(AchievementType difficulty) {
        int count = 0;
        for (AchievementList achievement : AchievementList.values()) {
            if (achievement.getType() == difficulty) count++;
        }
        return count;
    }

    /** Checks if a player has completed all the available achievements. */
    public static boolean completedAll(Player player) {
        return getTotalCompleted(player) == AchievementList.getTotal();
    }
}
