package com.osroyale.game.task.impl;

import com.osroyale.content.clanchannel.ClanRepository;
import com.osroyale.game.task.Task;
import com.osroyale.game.world.World;
import com.osroyale.game.world.entity.mob.player.Player;
import com.osroyale.game.world.entity.mob.player.persist.PlayerSerializer;
import com.osroyale.net.discord.DiscordPlugin;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class PlayerSaveEvent extends Task {
    private static final Logger logger = LogManager.getLogger();

    public PlayerSaveEvent() {
        super(1000);
    }

    @Override
    public void execute() {
        if (World.update.get()) {
            return;
        }

        int count = 0;
        for (Player player : World.getPlayers()) {
            if (player != null && !player.isBot) {
                PlayerSerializer.save(player);
                count++;
            }
        }

        if (count != 0) {

            if (count > 10) {
                DiscordPlugin.sendSimpleMessage("There are currently " + count + " players online!");
            }

            logger.info(count + " players were saved.");
            ClanRepository.saveAllActiveClans();
        }
    }
}
