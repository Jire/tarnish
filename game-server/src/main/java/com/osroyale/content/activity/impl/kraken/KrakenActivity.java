package com.osroyale.content.activity.impl.kraken;


import com.osroyale.Config;
import com.osroyale.content.achievement.AchievementHandler;
import com.osroyale.content.achievement.AchievementKey;
import com.osroyale.content.activity.Activity;
import com.osroyale.content.activity.ActivityType;
import com.osroyale.content.event.impl.ObjectInteractionEvent;
import com.osroyale.content.skill.impl.magic.teleport.Teleportation;
import com.osroyale.content.skill.impl.magic.teleport.TeleportationData;
import com.osroyale.game.Animation;
import com.osroyale.game.UpdatePriority;
import com.osroyale.game.world.World;
import com.osroyale.game.world.entity.mob.Mob;
import com.osroyale.game.world.entity.mob.npc.Npc;
import com.osroyale.game.world.entity.mob.npc.NpcDeath;
import com.osroyale.game.world.entity.mob.player.Player;
import com.osroyale.game.world.position.Area;
import com.osroyale.game.world.position.Position;
import com.osroyale.net.packet.out.SendMessage;
import com.osroyale.util.Utility;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Optional;
import java.util.Set;

/**
 * Handles the Kraken boss.
 *
 * @author Daniel
 */
public class KrakenActivity extends Activity {

    /** The player fighting the Kraken. */
    private final Player player;

    /** The Kraken npc. */
    public Npc kraken = null;

    /** The whirlpool activated count. */
    public int count = 0;

    /** Flag if kraken has been defeated. */
    private boolean completed;

    /** Set of npc tentacles. */
    private Set<Npc> tentacles = new HashSet<>();

    /** The Kraken activity listener. */
    private final KrakenActivityListener listener = new KrakenActivityListener(this);

    /** Holds all the monster spawn information. */
    private static final int[][] SPAWN = {
            {493, 2276, 10038},
            {493, 2276, 10034},
            {496, 2278, 10035},
            {493, 2283, 10038},
            {493, 2283, 10034},
    };

    /** Constructs a new <code>KrakenActivity</code>. */
    private KrakenActivity(Player player, int instance) {
        super(1, instance);
        this.player = player;
    }

    /** Creates a new Kraken activity. */
    public static KrakenActivity create(Player player) {
        KrakenActivity minigame = new KrakenActivity(player, player.playerAssistant.instance());
        minigame.add(player);
        player.gameRecord.start();
        minigame.count = 0;
        return minigame;
    }

    /** Handles transforming a Npc. */
    public void transform(Npc npc, int transform) {
        npc.transform(transform);
        npc.npcAssistant.login();
        if (transform == 5535)
            count++;
        if (transform == 494)
            npc.animate(new Animation(7135, UpdatePriority.HIGH));
        npc.getCombat().attack(player);
    }

    @Override
    public void onDeath(Mob mob) {
        if (mob.isPlayer() && mob.equals(player)) {
            player.send(new SendMessage("Better luck next time old chap!"));
            cleanup();
            remove(player);
            return;
        }
        if (mob.isNpc() && mob.getNpc().id == 494) {
            World.schedule(new NpcDeath(mob.getNpc(), () -> {
                completed = true;
                finish();
            }));
            return;
        }
        super.onDeath(mob);
    }

    @Override
    public void add(Mob mob) {
        super.add(mob);
        if (mob.isNpc()) {
            if (mob.getNpc().id == 496) {
                kraken = mob.getNpc();
            } else {
                tentacles.add(mob.getNpc());
            }
            mob.locking.lock();
        }
    }

    @Override
    public void remove(Mob mob) {
        if (!mob.isNpc()) {
            super.remove(mob);
            return;
        }
        int id = mob.getNpc().id;
        if (id == 496) {
            kraken = null;
        } else {
            tentacles.remove(mob.getNpc());
        }
        super.remove(mob);
    }

    @Override
    protected void start() {
        for (int[] aSPAWN : SPAWN) {
            Npc npc = new Npc(aSPAWN[0], new Position(aSPAWN[1], aSPAWN[2]));
            npc.owner = player;
            add(npc);
        }
        player.face(kraken.getPosition());
        pause();
    }

    @Override
    public void onLogout(Player player) {
        player.move(Config.DEFAULT_POSITION);
        cleanup();
        finish();
    }

    @Override
    public void onRegionChange(Player player) {
        if (!Area.inKraken(player)) {
            cleanup();
            finish();
        }
    }

    @Override
    public boolean canTeleport(Player player) {
        return true;
    }

    @Override
    public void finish() {
        cleanup();

        if (completed) {
            AchievementHandler.activate(player, AchievementKey.KRAKEN);
            player.send(new SendMessage("Congratulations, you have killed the Kraken. Fight duration: @red@" + Utility.getTime(player.gameRecord.end(ActivityType.KRAKEN)) + "</col>."));
        } else {
            player.gameRecord.end(ActivityType.KRAKEN, false);
        }

        remove(player);
        World.schedule(10, () -> {
            if(Area.inKraken(player)) {
                KrakenActivity.create(player);
            }
        });
    }

    @Override
    public void cleanup() {
        count = 0;
        if (kraken != null && kraken.isRegistered())
            kraken.unregister();
        Iterator<Npc> it = tentacles.iterator();
        while (it.hasNext()) {
            Npc npc = it.next();
            npc.animate(npc.definition.getDeathAnimation());
            World.schedule(3, npc::unregister);
            it.remove();
        }
    }

    @Override
    protected boolean clickObject(Player player, ObjectInteractionEvent event) {
        if (event.getObject().getId() != 538)
            return false;
        player.dialogueFactory.sendOption("Restart Kraken instance", () -> {
            remove(player);
            Teleportation.teleport(player, new Position(2280, 10031, 0), TeleportationData.CREVICE, () -> KrakenActivity.create(player));
        }, "Leave Kraken Instance", () -> {
            remove(player);
            Teleportation.activateOverride(player, Config.DEFAULT_POSITION, TeleportationData.CREVICE);
        }).execute();
        return true;
    }

    @Override
    public ActivityType getType() {
        return ActivityType.KRAKEN;
    }

    @Override
    protected Optional<KrakenActivityListener> getListener() {
        return Optional.of(listener);
    }
}