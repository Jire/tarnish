package com.osroyale.game.world.entity.mob.player;

import com.osroyale.content.EmblemData;
import com.osroyale.content.clanchannel.content.ClanAchievement;
import com.osroyale.content.pet.PetData;
import com.osroyale.content.pet.Pets;
import com.osroyale.content.writer.InterfaceWriter;
import com.osroyale.content.writer.impl.InformationWriter;
import com.osroyale.game.world.items.Item;
import com.osroyale.util.Utility;
import static com.osroyale.content.clanchannel.content.ClanTaskKey.PLAYER_KILLING;

public class PlayerKilling {

    public static void handle(Player killer, Player victim) {
        if (killer.isBot) {
            return;
        }

        if (killer.lastHost.equalsIgnoreCase(victim.lastHost)) {
            killer.message("<col=FF0019>You were not rewarded since you killed someone with your same IP.");
            return;
        }

        if (killer.clanChannel != null && victim.clanChannel != null && killer.clanChannel.getName().equalsIgnoreCase(victim.clanChannel.getName())) {
            if (!killer.clanChannel.getName().equals("tarnish")) {
                killer.message("<col=FF0019>You were not rewarded since you killed someone from your clan.");
                return;
            }
        }

        int reward = PlayerRight.getBloodMoney(killer);

        killer.inventory.addOrDrop(new Item(13307, reward));
        killer.message("<col=295EFF>You were rewarded with " + reward + " blood money for that kill.");
        killer.killstreak.add();

        for (Item item : killer.inventory) {
            if (item == null)
                continue;
            EmblemData emblem = EmblemData.forId(item.getId());
            if (emblem == null)
                continue;
            if (emblem.getNext() == -1)
                continue;
            killer.inventory.replace(item.getId(), emblem.getNext(), true);
            break;
        }

        killer.forClan(channel -> {
            channel.activateTask(PLAYER_KILLING, killer.getName());
            channel.activateAchievement(ClanAchievement.PLAYER_KILLER_I);
            channel.activateAchievement(ClanAchievement.PLAYER_KILLER_II);
            channel.activateAchievement(ClanAchievement.PLAYER_KILLER_III);
        });

        Pets.onReward(killer, PetData.BABY_DARTH);
        InterfaceWriter.write(new InformationWriter(killer));
        add(killer, victim.lastHost);
    }

    public static void add(Player player, String host) {
        if (host == null || host.isEmpty()) {
            return;
        }
        if (player.lastKilled.contains(host)) {
            return;
        }
        if (player.lastKilled.size() >= 2) {
            player.lastKilled.removeFirst();
        }
        player.lastKilled.add(host);
    }

    public static boolean remove(Player player, String host) {
        return player.lastKilled.remove(host);
    }

    public static boolean contains(Player player, String host) {
        return player.lastKilled != null && player.lastKilled.contains(host);
    }
}
