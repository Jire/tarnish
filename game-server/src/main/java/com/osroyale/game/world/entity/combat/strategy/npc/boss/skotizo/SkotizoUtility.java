package com.osroyale.game.world.entity.combat.strategy.npc.boss.skotizo;

import com.osroyale.game.world.World;
import com.osroyale.game.world.entity.mob.Direction;
import com.osroyale.game.world.entity.mob.Mob;
import com.osroyale.game.world.entity.mob.npc.Npc;
import com.osroyale.game.world.entity.mob.player.Player;
import com.osroyale.game.world.position.Position;
import com.osroyale.net.discord.DiscordPlugin;
import com.osroyale.util.Utility;

/**
 * Created by Daniel on 2017-12-20.
 */
public class SkotizoUtility {

    static Npc skotizo = null;
    private static SpawnData data;

    static Npc generateSpawn() {
        data = SpawnData.generate();
        skotizo = new Npc(7286, data.position, 10, Mob.DEFAULT_INSTANCE, Direction.NORTH);
        skotizo.register();
        skotizo.definition.setRespawnTime(-1);
        skotizo.definition.setAggressive(true);
        skotizo.speak("Darkness is here to penetrate your souls!");
        World.sendMessage("<icon=6><col=8714E6> Skotizo has just spawned! He is located at " + data.location + "!");
        DiscordPlugin.sendSimpleMessage("Skotizo has entered the wilderness! He is located at " + data.location + "!");
        return skotizo;
    }

    public static String getInformation() {
        return (skotizo == null || skotizo.isDead()) ? "Not Active" : data.location;
    }

    public static void defeated(Npc npc, Player player) {
        if (skotizo != null && skotizo.isRegistered()) {
            skotizo.unregister();
        }

        skotizo = null;
    }

    public enum SpawnData {
        LEVEL_18("Near boneyard hunter", new Position(3307, 3668, 0)),
        LEVEL_19("North of chaos altar", new Position(3222, 3658, 0)),
        LEVEL_28("Near vennenatis", new Position(3308, 3737, 0)),
        LEVEL_41("Near callisto", new Position(3270, 3843, 0)),
        LEVEL_52("Near rogue's castle", new Position(3304, 3929, 0)),
        LEVEL_53("Near scorpia's cave", new Position(3211, 3944, 0));

        public final String location;
        public final Position position;

        SpawnData(String location, Position position) {
            this.location = location;
            this.position = position;
        }

        public static SpawnData generate() {
            return Utility.randomElement(values());
        }
    }
}
