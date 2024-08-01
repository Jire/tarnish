package com.osroyale.content.achievement;

import com.osroyale.content.writer.InterfaceWriter;
import com.osroyale.game.world.entity.mob.player.Player;
import com.osroyale.net.packet.out.SendColor;
import com.osroyale.net.packet.out.SendScrollbar;
import com.osroyale.net.packet.out.SendString;

import static com.osroyale.content.achievement.AchievementType.*;

/**
 * Handles the achievement tab.
 *
 * @author Daniel
 */
public class AchievementWriter extends InterfaceWriter {

    public AchievementWriter(Player player) {
        super(player);
        int total = AchievementList.values().length;
        int progress = (int) (AchievementHandler.getTotalCompleted(player) * 100 / (double) AchievementList.getTotal());

        int pvp = (int) (AchievementHandler.getDifficultyCompletion(player, PVP) * 100 / (double) AchievementHandler.getDifficultyAmount(PVP));
        int pvm = (int) (AchievementHandler.getDifficultyCompletion(player, PVM) * 100 / (double) AchievementHandler.getDifficultyAmount(PVM));
        int skilling = (int) (AchievementHandler.getDifficultyCompletion(player, SKILLING) * 100 / (double) AchievementHandler.getDifficultyAmount(SKILLING));
        int minigame = (int) (AchievementHandler.getDifficultyCompletion(player, MINIGAME) * 100 / (double) AchievementHandler.getDifficultyAmount(MINIGAME));
        int miscellaneous = (int) (AchievementHandler.getDifficultyCompletion(player, MISCELLANEOUS) * 100 / (double) AchievementHandler.getDifficultyAmount(MISCELLANEOUS));

        player.send(new SendString("Player Killing (" + pvp + "%)", 35051));
        player.send(new SendColor(35051, AchievementHandler.getColor(player, PVP)));
        player.send(new SendString("Monster Killing (" + pvm + "%)", 35052));
        player.send(new SendColor(35052, AchievementHandler.getColor(player, PVM)));
        player.send(new SendString("Skilling (" + skilling + "%)", 35053));
        player.send(new SendColor(35053, AchievementHandler.getColor(player, AchievementType.SKILLING)));
        player.send(new SendString("Minigames (" + minigame + "%)", 35054));
        player.send(new SendColor(35054, AchievementHandler.getColor(player, AchievementType.MINIGAME)));
        player.send(new SendString("Miscellaneous (" + miscellaneous + "%)", 35055));
        player.send(new SendColor(35055, AchievementHandler.getColor(player, AchievementType.MISCELLANEOUS)));

        player.send(new SendScrollbar(35050, 200));
        player.send(new SendString("Completed: " + AchievementHandler.getTotalCompleted(player) + "/" + total + " (" + progress + "%)", 35004));
    }

    @Override
    protected int startingIndex() {
        return 35051;
    }

    @Override
    protected String[] text() {
        return null;
    }

    @Override
    protected int[][] color() {
        return null;
    }

    @Override
    protected int[][] font() {
        return null;
    }
}
