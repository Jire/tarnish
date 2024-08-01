package com.osroyale.content.activity.impl.zulrah;


import com.osroyale.content.activity.Activity;
import com.osroyale.content.activity.ActivityDeathType;
import com.osroyale.content.activity.ActivityListener;
import com.osroyale.content.activity.ActivityType;
import com.osroyale.game.Animation;
import com.osroyale.game.Graphic;
import com.osroyale.game.world.World;
import com.osroyale.game.world.entity.combat.hit.Hit;
import com.osroyale.game.world.entity.combat.hit.Hitsplat;
import com.osroyale.game.world.entity.combat.strategy.npc.boss.Zulrah;
import com.osroyale.game.world.entity.mob.Mob;
import com.osroyale.game.world.entity.mob.npc.Npc;
import com.osroyale.game.world.entity.mob.npc.NpcDeath;
import com.osroyale.game.world.entity.mob.player.Player;
import com.osroyale.game.world.object.CustomGameObject;
import com.osroyale.game.world.object.GameObject;
import com.osroyale.game.world.position.Area;
import com.osroyale.game.world.position.Position;
import com.osroyale.net.packet.out.SendMessage;
import com.osroyale.util.RandomUtils;
import com.osroyale.util.Stopwatch;
import com.osroyale.util.Utility;

import java.util.*;

public class ZulrahActivity extends Activity {
    /** The player instance for this activity. */
    protected final Player player;

    /** The Zulrah npc for this activity. */
    protected Npc zulrah;

    /** The activity stopwatch. */
    protected Stopwatch stopwatch = Stopwatch.start();

    /** The activity count. */
    protected int count;

    /** The current zulrah target position. */
    public Position target;

    /** The current zulrah phase. */
    ZulrahPhase phase = ZulrahPhase.INITIALIZATION;

    /** The currently spawned snakes. */
    List<Npc> snakes = new ArrayList<>();

    /** The currently spawned clouds. */
    List<CustomGameObject> clouds = new ArrayList<>();

    /** The combat listener for this activity. */
    private final ActivityListener<? extends Activity> LISTENER = new ZulrahListener(this);

    /** Constructs a new <code>ZulrahActivity</code>. */
    private ZulrahActivity(Player player, int instance) {
        super(1, instance);
        this.player = player;
    }

    /** Creates a new Zulrah activity for the player. */
    public static ZulrahActivity create(Player player) {
        ZulrahActivity activity = new ZulrahActivity(player, player.playerAssistant.instance());
        activity.add(player);
        activity.resetCooldown();
        player.gameRecord.start();
        player.dialogueFactory.sendStatement("Welcome to Zulrah's shrine.").execute();
        return activity;
    }

    /** Increments the activity count. */
    void increment() {
        count++;
    }

    /** Resets the Zulrah phase. */
    void reset() {
        player.getCombat().reset();
        zulrah.getCombat().reset();
        zulrah.resetFace();
        stopwatch.reset();
        count = -1;
    }

    /** Gets the next Zulrah form. */
    int nextForm() {
        int next = RandomUtils.randomExclude(ZulrahConstants.ZULRAH_IDS, zulrah.id);
        if (next == 2042) {
            ((Zulrah) zulrah.getStrategy()).setRanged();
        } else if (next == 2043) {
            ((Zulrah) zulrah.getStrategy()).setMelee();
        } else if (next == 2044) {
            ((Zulrah) zulrah.getStrategy()).setMagic();
        }
        return next;
    }

    /** Gets the next Zulrah phase. */
    ZulrahPhase nextPhase() {
        if (zulrah.id == 2043) {
            return ZulrahPhase.ATTACKING;
        }
        Set<ZulrahPhase> states = new HashSet<>();
        states.add(ZulrahPhase.ATTACKING);
        if (clouds.size() < ZulrahConstants.MAXIMUM_CLOUDS) {
            states.add(ZulrahPhase.POISONOUS_CLOUD);
        }
        if (snakes.size() < ZulrahConstants.MAXIMUM_SNAKELINGS) {
            states.add(ZulrahPhase.SNAKELINGS);
        }
        return Utility.randomElement(states);
    }

    /** Gets the snakeling position. */
    Position getSnakelingPosition() {
        Set<Position> positions = new HashSet<>(Arrays.asList(ZulrahConstants.SNAKELINGS_POSITIONS));
        for (Npc snake : snakes) {
            if (positions.contains(snake.getPosition())) {
                positions.remove(snake.getPosition());
            }
        }
        return Utility.randomElement(positions);
    }

    /** Gets the cloud position. */
    Position getCloudPosition() {
        Set<Position> positions = new HashSet<>(Arrays.asList(ZulrahConstants.CLOUD_POSITIONS));
        for (GameObject cloud : clouds) {
            if (positions.contains(cloud.getPosition())) {
                positions.remove(cloud.getPosition());
            }
        }
        return Utility.randomElement(positions);
    }

    /** Handles the cloud effect. */
    private void cloudEffect() {
        for (CustomGameObject cloud : clouds) {
            if (Utility.inside(cloud.getPosition(), 2, 2, player.getPosition(), 1, 1))
                player.damage(new Hit(RandomUtils.inclusive(1, 5), Hitsplat.VENOM));
        }
    }

    @Override
    protected void start() {
        cloudEffect();

        boolean valid = zulrah != null;

        if (valid && zulrah.isDead()) {
            return;
        }

        phase.execute(this);

        if (valid) {
            zulrah.getCombat().cooldown(2);
        }
    }

    @Override
    public void finish() {
        cleanup();
        remove(player);
        player.animate(Animation.RESET, true);
        player.graphic(Graphic.RESET, true);
        if (zulrah.isDead()) {
            player.send(new SendMessage("Fight duration: @red@" + Utility.getTime(player.gameRecord.end(ActivityType.ZULRAH)) + "</col>."));
        }
    }

    @Override
    public void cleanup() {
        if (zulrah != null) {
            remove(zulrah);
        }
        for (Npc snakeling : snakes) {
            remove(snakeling);
        }
        for (GameObject cloud : clouds) {
            cloud.unregister();
        }
        snakes.clear();
        clouds.clear();
    }

    @Override
    public void onRegionChange(Player player) {
        if (!Area.inZulrah(player)) {
            cleanup();
            remove(player);
            player.animate(Animation.RESET, true);
            player.graphic(Graphic.RESET, true);

        }
    }

    @Override
    public void onDeath(Mob mob) {
        if (mob.isNpc()) {
            Npc npc = mob.getNpc();

            if (mob.equals(zulrah)) {
                World.schedule(new NpcDeath(npc, this::finish));
            }

            if (snakes.contains(npc)) {
                World.schedule(new NpcDeath(npc, () -> {
                    remove(npc);
                    snakes.remove(npc);
                }));
            }

            return;
        }

        super.onDeath(mob);
    }

    @Override
    public ActivityType getType() {
        return ActivityType.ZULRAH;
    }

    @Override
    public boolean canTeleport(Player player) {
        return true;
    }

    @Override
    public ActivityDeathType deathType() {
        return ActivityDeathType.PURCHASE;
    }

    @Override
    protected Optional<? extends ActivityListener<? extends Activity>> getListener() {
        return Optional.of(LISTENER);
    }

}
