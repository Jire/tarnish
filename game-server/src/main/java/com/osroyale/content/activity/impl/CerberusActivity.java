package com.osroyale.content.activity.impl;

import com.osroyale.content.activity.Activity;
import com.osroyale.content.activity.ActivityType;
import com.osroyale.game.Animation;
import com.osroyale.game.Graphic;
import com.osroyale.game.world.World;
import com.osroyale.game.world.entity.mob.Mob;
import com.osroyale.game.world.entity.mob.npc.Npc;
import com.osroyale.game.world.entity.mob.npc.NpcDeath;
import com.osroyale.game.world.entity.mob.player.Player;
import com.osroyale.game.world.object.CustomGameObject;
import com.osroyale.game.world.object.GameObject;
import com.osroyale.game.world.position.Area;
import com.osroyale.game.world.position.Position;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Daniel on 2018-01-20.
 */
public class CerberusActivity extends Activity {
    private static final GameObject[] OBJECTS = {new CustomGameObject(23105, new Position(1241, 1242)), new CustomGameObject(23105, new Position(1240, 1242)), new CustomGameObject(23105, new Position(1239, 1242)), new CustomGameObject(23105, new Position(1240, 1236))};
    private final Player player;
    private Npc cerberus = new Npc(5862, new Position(1240, 1252));
    private List<GameObject> objectList = new ArrayList<>(4);

    private CerberusActivity(Player player, int instance) {
        super(1, instance);
        this.player = player;
    }

    public static CerberusActivity create(Player player) {
        CerberusActivity activity = new CerberusActivity(player, player.playerAssistant.instance());
        activity.add(player);
        activity.resetCooldown();
        return activity;
    }

    @Override
    protected void start() {
        for (GameObject object : OBJECTS) {
            object.register();
            objectList.add(object);
        }

        add(cerberus);
        pause();
    }

    @Override
    public void finish() {
        cleanup();
        remove(player);
    }

    @Override
    public void cleanup() {
        for (GameObject object : objectList) {
            System.out.println("unregistering " + object.getId());
            object.unregister();
        }

        objectList.clear();
        remove(cerberus);
    }

    @Override
    public void onDeath(Mob mob) {
        if (mob.isPlayer()) {
            cleanup();

            player.move(new Position(3087, 3497));

            player.animate(Animation.RESET, true);
            player.graphic(Graphic.RESET, true);
            return;
        }

        World.schedule(new NpcDeath(mob.getNpc(), () -> {
            finish();
            restart(10, () -> {
                if (Area.inCerberus(player)) {
                    create(player);
                } else {
                    remove(player);
                }
            });
        }));
    }

    @Override
    public void onRegionChange(Player player) {
        if (!Area.inCerberus(player)) {
            cleanup();
            remove(player);
        }
    }

    @Override
    public void onLogout(Player player) {
        cleanup();
        remove(player);
    }

    @Override
    public boolean canTeleport(Player player) {
        return true;
    }

    @Override
    public ActivityType getType() {
        return ActivityType.CERBERUS;
    }
}
