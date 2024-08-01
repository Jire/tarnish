package com.osroyale.content.activity.impl.magearena;

import com.osroyale.content.ActivityLog;
import com.osroyale.content.activity.Activity;
import com.osroyale.content.activity.ActivityDeathType;
import com.osroyale.content.activity.ActivityType;
import com.osroyale.content.activity.panel.ActivityPanel;
import com.osroyale.content.skill.impl.magic.teleport.Teleportation;
import com.osroyale.content.skill.impl.magic.teleport.TeleportationData;
import com.osroyale.game.Animation;
import com.osroyale.game.Graphic;
import com.osroyale.game.world.World;
import com.osroyale.game.world.entity.combat.hit.Hit;
import com.osroyale.game.world.entity.mob.Direction;
import com.osroyale.game.world.entity.mob.Mob;
import com.osroyale.game.world.entity.mob.npc.Npc;
import com.osroyale.game.world.entity.mob.npc.NpcDeath;
import com.osroyale.game.world.entity.mob.player.Player;
import com.osroyale.game.world.items.Item;
import com.osroyale.game.world.pathfinding.TraversalMap;
import com.osroyale.game.world.position.Area;
import com.osroyale.game.world.position.Position;
import com.osroyale.net.packet.out.SendMessage;
import com.osroyale.util.Utility;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * The Kolodion's mage arena activity.
 *
 * @author Daniel
 */
public class MageArena extends Activity {
    /** The spawn boundaries. */
    private static final Position[] BOUNDARIES = Utility.getInnerBoundaries(new Position(3105, 3934, 0), 8, 8);

    /** The stages of the activity. */
    private enum Stage {
        INITIALIZE, FIGHTING, FINISH
    }

    /** The player instance. */
    private final Player player;

    /** The amount of npcs the player has killed. */
    private int killed;

    /** The amount of points the player has been awarded. */
    private int points;

    /** Activity completion flag. */
    private Stage stage = Stage.INITIALIZE;

    /** Holds all the monsters of the activity. */
    private Map<Integer, Npc> monsters = new HashMap<>();

    /** Constructs a new <code>MageArena</code>. */
    private MageArena(Player player, int instance) {
        super(5, instance);
        this.player = player;
    }

    /** Creates a new <code>MageArena</code> activity. */
    public static MageArena create(Player player) {
        MageArena activity = new MageArena(player, player.playerAssistant.instance());
        player.move(new Position(3105, 3934, 0));
        player.gameRecord.start();
        activity.add(player);
        activity.resetCooldown();
        return activity;
    }

    /** Gets the next best available position. */
    private Position getAvailablePosition() {
        Position available = Utility.randomElement(BOUNDARIES);
        while (!TraversalMap.isTraversable(available, Direction.NORTH, false)) {
            available = Utility.randomElement(BOUNDARIES);
        }
        return available;
    }

    /** Handles the meteors. */
    private void meteors() {
        if (Utility.random(1, 3) == 2) {
            Position center = new Position(player.getX() - 2, player.getY() - 2, player.getHeight());
            Position[] bounds = Utility.getInnerBoundaries(center, 5, 5);
            Set<Position> meteors = new HashSet<>();
            for (Position bound : bounds) {
                if (Utility.random(100) > 55) {
                    meteors.add(bound);
                }
            }
            for (Position meteor : meteors) {
                World.sendGraphic(new Graphic(659), meteor, getInstance());
                World.schedule(1, () -> {
                    if (player.getPosition().equals(meteor))
                        player.damage(new Hit(Utility.random(5, 12)));
                });
            }
        }
    }

    @Override
    protected void start() {
        switch (stage) {
            case INITIALIZE:
                for (int index = 0; index < 3; index++) {
                    int id = index == 0 ? 1157 : (index == 1 ? 1160 : 1158);
                    Npc monster = new Npc(id, getAvailablePosition());
                    monster.owner = player;
                    monsters.put(id, monster);
                    add(monster);
                }
                stage = Stage.FIGHTING;
                cooldown(1);
                break;
            case FIGHTING:
                for (Npc monster : monsters.values()) {
                    if (!monster.getCombat().isAttacking(player)) {
                        monster.getCombat().attack(player);
                        continue;
                    }
                    if (monster.getCombat().isUnderAttackBy(player) && Utility.random(5, 15) == 5) {
                        monster.speak("ARGHHH!");
                        World.sendGraphic(new Graphic(1213), player.getPosition(), getInstance());
                        player.damage(new Hit(Utility.random(1, 5)));
                        Teleportation.activateOverride(monster, getAvailablePosition(), TeleportationData.MODERN, () -> monster.getCombat().attack(player));
                    }
                }
                meteors();
                resetCooldown();
                break;
            case FINISH:
                finish();
                break;
        }
    }

    @Override
    public void finish() {
        long time = player.gameRecord.end(ActivityType.KOLODION_ARENA);
        cleanup();
        remove(player);
        ActivityPanel.clear(player);
        player.move(new Position(2540, 4715, 0));

        player.animate(Animation.RESET, true);
        player.graphic(Graphic.RESET, true);

        if (stage == Stage.FINISH) {
            points += 30;
            player.send(new SendMessage("You have completed Kolodion's arena. Final time: @red@" + Utility.getTime(time) + "</col>. Earned Points: @red@" + points + "</col>."));
            player.activityLogger.add(ActivityLog.KOLODIONS_MINIGAME);
            player.completedMageArena = true;
        }

        player.mageArenaPoints += points;
    }

    @Override
    public void cleanup() {
        for (Npc monster : monsters.values()) {
            monsters.remove(monster);
            remove(monster);
        }
    }

    @Override
    public void onLogout(Player player) {
        finish();
    }

    @Override
    public void onRegionChange(Player player) {
        if (!Area.inkolodionArena(player))
            finish();
    }

    @Override
    public boolean canTeleport(Player player) {
        player.send(new SendMessage("You can not teleport out of this activity."));
        return true;
    }

    @Override
    public void update() {
        int progress = (int) Utility.getPercentageAmount(killed, 3);
        String clock = "Time: <col=FF5500>" + Utility.getTime(System.currentTimeMillis() - player.gameRecord.time) + "</col>";
        String remain = "Monsters Left: <col=FF5500>" + (3 - killed) + "</col>";
        String gained = "Points Gained: <col=FF5500>" + points + "</col>";
        Item[] items = {new Item(2412), new Item(2413), new Item(2414)};
        ActivityPanel.update(player, progress, "Mage's Arena", "Progress:", Utility.randomElement(items), clock, remain, gained);
    }

    @Override
    public void onDeath(Mob mob) {
        if (mob.isPlayer()) {
            mob.animate(Animation.RESET, true);
            mob.graphic(Graphic.RESET, true);
            finish();
            return;
        }
        if (monsters.containsKey(mob.id)) {
            killed++;
            points += Utility.random(15, 20);
            monsters.remove(mob.id);
            World.schedule(new NpcDeath(mob.getNpc()));
        }
        if (monsters.isEmpty()) {
            stage = Stage.FINISH;
            cooldown(10);
            return;
        }
        resetCooldown();
    }

    @Override
    public ActivityDeathType deathType() {
        return ActivityDeathType.SAFE;
    }

    @Override
    public ActivityType getType() {
        return ActivityType.KOLODION_ARENA;
    }
}
