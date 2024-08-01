package plugin.click.button;

import com.osroyale.content.achievement.AchievementType;
import com.osroyale.content.achievement.AchievementHandler;
import com.osroyale.content.achievement.AchievementList;
import com.osroyale.game.plugin.PluginContext;
import com.osroyale.game.world.entity.mob.player.Player;
import com.osroyale.net.packet.out.*;
import com.osroyale.util.Utility;

import java.util.List;


public class AchievementButtonPlugin extends PluginContext {

    @Override
    protected boolean onClick(Player player, int button) {
        if (button == -30485) {
            open(player, 0, AchievementType.PVP);
            return true;
        }
        if (button == -30484) {
            open(player, 0, AchievementType.PVM);
            return true;
        }
        if (button == -30483) {
            open(player, 0, AchievementType.SKILLING);
            return true;
        }
        if (button == -30482) {
            open(player, 0, AchievementType.MINIGAME);
            return true;
        }
        if (button == -30481) {
            open(player, 0, AchievementType.MISCELLANEOUS);
            return true;
        }
        return click(player, button);
    }

    private void open(Player player, int index, AchievementType difficulty) {
        List<AchievementList> list = AchievementList.asList(difficulty);
        int total = AchievementHandler.getDifficultyCompletion(player, difficulty);

        if (index < list.size()) {
            int scroll = list.size() < 10 ? 10 : list.size();

            drawList(player, index, list);
            drawAchievement(player, list.get(index));
            player.send(new SendString(difficulty.getTitle() + " Achievement", 56814));
            player.send(new SendString(total + "/" + list.size(), 56815));
            player.send(new SendScrollbar(56830, scroll * 20));
            player.attributes.set("ACHIEVEMENT_DIFFICULTY_KEY", difficulty);
            player.interfaceManager.open(56800);
        }
    }

    private void drawList(Player player, int index, List<AchievementList> list) {
        for (int idx = 0, string = 56832; idx < 50; idx++, string += 2) {
            if (idx >= list.size()) {
                player.send(new SendString("", string));
                continue;
            }

            AchievementList achievement = list.get(idx);
            int completion = player.playerAchievements.computeIfAbsent(achievement.getKey(), a -> 0);
            double progress = getPercent(completion, achievement.getAmount());

            String color = index == idx ? "<col=FFFFFF>" : (progress >= 100 ? "<col=257A1A>" : progress > 0 ? "<col=FFFF00>" : "<col=FF0000>");
            player.send(new SendString(color + Utility.formatName(achievement.name().toLowerCase()).replace("_", " "), string));
        }
    }

    private void drawAchievement(Player player, AchievementList achievement) {
        int completion = player.playerAchievements.computeIfAbsent(achievement.getKey(), a -> 0);
        double progress = getPercent(completion, achievement.getAmount());

        if (completion > achievement.getAmount())
            completion = achievement.getAmount();

        player.send(new SendString(achievement.getTask(), 56807));
        player.send(new SendString(Utility.formatDigits(achievement.getReward().getAmount()) + " " + achievement.getReward().getName(), 56809));
        player.send(new SendString(completion + "/" + achievement.getAmount(), 56812));
        player.send(new SendString(String.format("%.2f", progress) + "%", 56813));
        player.send(new SendItemOnInterface(56810, achievement.getReward()));
        player.send(new SendConfig(527, achievement.getType().ordinal()));
        player.send(new SendProgressBar(56811, (int) progress));
    }

    private double getPercent(int completion, int total) {
        double progress = (completion * 100 / (double) total);
        if (progress > 100) {
            progress = 100;
        }
        return progress;
    }

    private boolean click(Player player, int button) {
        if (button >= -8705 && button <= -8627) {
            int index = (button - -8705) / 2;
            open(player, index, player.attributes.get("ACHIEVEMENT_DIFFICULTY_KEY", AchievementType.class));
            return true;
        }
        return false;
    }
}
