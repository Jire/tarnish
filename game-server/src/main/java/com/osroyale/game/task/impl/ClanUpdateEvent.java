package com.osroyale.game.task.impl;

import com.osroyale.content.clanchannel.ClanRepository;
import com.osroyale.content.clanchannel.channel.ClanChannel;
import com.osroyale.game.task.TickableTask;
import com.osroyale.util.GsonUtils;

import java.io.File;
import java.io.FileWriter;
import java.nio.file.Paths;

/**
 * An randomevent which handles updating clan chats.
 *
 * @author Daniel | Obey
 */
public class ClanUpdateEvent extends TickableTask {

    public ClanUpdateEvent() {
        super(false, 60);
    }

    @Override
    protected void tick() {
        if (ClanRepository.ACTIVE_CHANNELS.isEmpty()) {
            return;
        }

        for (ClanChannel channel : ClanRepository.ACTIVE_CHANNELS) {
            if (channel.activeSize() <= 0) {
                continue;
            }

            channel.forEach(clanMember -> channel.getHandler().updateMemberList(clanMember));

            Thread.startVirtualThread(() -> {
                final File dir = Paths.get("data", "content", "clan").toFile();

                if (!dir.exists()) {
                    dir.mkdirs();
                }

                try (FileWriter fw = new FileWriter(dir.toPath().resolve(channel.getOwner() + ".json").toFile())) {
                    fw.write(GsonUtils.JSON_PRETTY_NO_NULLS.get().toJson(channel.toJson()));
                } catch (final Exception e) {
                    e.printStackTrace();
                }
            });

        }
    }

}
