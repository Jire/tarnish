package com.osroyale.content.activity.impl.pestcontrol;

import com.osroyale.content.ActivityLog;
import com.osroyale.content.achievement.AchievementHandler;
import com.osroyale.content.achievement.AchievementKey;
import com.osroyale.content.activity.Activity;
import com.osroyale.content.activity.ActivityDeathType;
import com.osroyale.content.activity.ActivityType;
import com.osroyale.content.activity.lobby.LobbyManager;
import com.osroyale.content.activity.lobby.LobbyNode;
import com.osroyale.content.activity.panel.Activity_Panel;
import com.osroyale.content.event.impl.ObjectInteractionEvent;
import com.osroyale.game.Animation;
import com.osroyale.game.Graphic;
import com.osroyale.game.world.World;
import com.osroyale.game.world.entity.Entity;
import com.osroyale.game.world.entity.mob.Mob;
import com.osroyale.game.world.entity.mob.npc.Npc;
import com.osroyale.game.world.entity.mob.npc.NpcDeath;
import com.osroyale.game.world.entity.mob.player.Player;
import com.osroyale.game.world.entity.mob.player.PlayerRight;
import com.osroyale.game.world.items.Item;
import com.osroyale.game.world.pathfinding.TraversalMap;
import com.osroyale.game.world.position.Area;
import com.osroyale.game.world.position.Position;
import com.osroyale.net.packet.out.SendMessage;
import com.osroyale.util.RandomUtils;
import com.osroyale.util.Utility;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static com.osroyale.game.world.entity.combat.attack.listener.SimplifiedListener.CANT_ATTACK;

public class PestControlGame extends LobbyNode {

    /** The position of the pest control boat. */
    private static final Position BOAT = new Position(2656, 2609);

    /** The position outside the pest control boat. */
    private static final Position OUTSIDE_BOAT_POSITION = new Position(2657, 2639);

    /** The Pest control activity listener. */
    private final PestControlListener listener = new PestControlListener(this);

    /** The void knight. */
    private final Npc voidKnight = new Npc(1756, Position.create(2656, 2592));

    /** The portal names. */
    private static final String[] PORTAL_NAMES = {
        "Purple Portal",
        "Blue Portal",
        "Yellow Portal",
        "Red Portal"
    };

    /** The portals. */
    private final Portal[] portals = new Portal[] {
    /* Purple */  new Portal(1739, Position.create(2628, 2591)),
    /* Blue */    new Portal(1740, Position.create(2680, 2588)),
    /* Yellow */  new Portal(1741, Position.create(2669, 2570)),
    /* Red */     new Portal(1742, Position.create(2645, 2569))
    };

    private static final Position BLUE_BOUNDS = new Position(2679, 2587);
    private static final Position RED_BOUNDS = new Position(2644, 2568);
    private static final Position YELLOW_BOUNDS = new Position(2668, 2569);
    private static final Position PURPLE_BOUNDS = new Position(2627, 2590);
    private static final Position KNIGHT_BOUNDS = new Position(2654, 2590);

    private static final int[] RAVAGERS = {1704, 1705, 1706, 1707, 1708};
    private static final int[] SPLATTERS = {1691, 1692};
    private static final int[] SHIFTERS = {1698, 1699, 1700, 1701};
    private static final int[] DEFILERS = {1728, 1729};
    private static final int[] TORCHERS = {1728, 1729};

    /** The void knight messages that he will chant. */
    private static final String[] VOID_KNIGHT_MESSAGES = {
        "We must not fail!",
        "Take down the portals",
        "The Void Knights will not fall!",
        "Hail the Void Knights!",
        "We are beating these scum!"
    };

    /** The active monsters. */
    private Set<Npc> monsters = new HashSet<>();

    /** The active portals. */
    private Set<Npc> portalSet = new HashSet<>();

    PestControlGame(LobbyManager manager) {
        super(manager);
        setInstance(Entity.DEFAULT_INSTANCE);
    }

    @Override
    public void sequence() {
        super.sequence();

        if (!inLobby()) {
            if (getTicks() == 0) {
                finishCooldown();
                return;
            }

            if (getTicks() % 20 == 0) {
                voidKnight.speak(Utility.randomElement(VOID_KNIGHT_MESSAGES));

                if (getTicks() > 10) {
                    agressiveMonsters();
                }
            }
        }

        forEachActivity((mob, activity) -> activity.getPanel().ifPresent(panel -> ((PestControlPanel) panel).update()));
    }

    @Override
    protected void onStart() {
        forEachActivity((mob, activity) -> {
            if (mob.isPlayer()) {
                mob.getPlayer().playerAssistant.restore();
            }
        });

        add(voidKnight);
        voidKnight.blockFace = true;

        for (Npc portal : portals) {
            add(portal);
            portalSet.add(portal);
        }

        groupMessage("Protect the void knight at all costs, good luck!");
        spawnMonsters();
    }

    @Override
    public void onDeath(Mob mob) {
        if (mob.isNpc()) {
            Npc npc = mob.getNpc();

            if (npc.id >= 1739 && npc.id <= 1742) {
                portalSet.remove(npc);
                voidKnight.heal(25);
                groupMessage("The " + PORTAL_NAMES[npc.id - 1739] + " is dead! The Void Knight has gained 25 HP.");
            } else {
                monsters.remove(mob.getNpc());
            }

            World.schedule(new NpcDeath(mob.getNpc(), () -> remove(mob)));
        } else {
            mob.move(BOAT.transform(Utility.random(4), Utility.random(6)));
            mob.getPlayer().animate(Animation.RESET, true);
            mob.getPlayer().graphic(Graphic.RESET, true);
        }
    }

    @Override
    public void onLogout(Player player) {
        removeActivity(player);
    }

    @Override
    public boolean canLogout(Player player) {
        player.message("You cannot log out during a Pest Control game.");
        return false;
    }

    @Override
    public void onRegionChange(Player player) {
        if (!Area.inPestControl(player)) {
            removeActivity(player);
        }
    }

    @Override
    protected boolean clickObject(Player player, ObjectInteractionEvent event) {
        if (event.getObject().getId() == 14314) {
            manager.leave(player);
            return true;
        }
        return false;
    }

    @Override
    public void finish() {
        super.finish();
        remove(voidKnight);
        monsters.forEach(this::remove);
        portalSet.forEach(this::remove);
        monsters.clear();
        portalSet.clear();
    }

    @Override
    protected Optional<PestControlListener> getListener() {
        return Optional.of(listener);
    }

    @Override
    public ActivityType getType() {
        return ActivityType.PEST_CONTROL;
    }

    @Override
    public ActivityDeathType deathType() {
        return ActivityDeathType.SAFE;
    }
    @Override
    protected Activity createActivity(Player player) {
        PestControlNode node = new PestControlNode(player);

        PestControlPanel panel = new PestControlPanel(player, node);
        panel.open();

        return node;
    }

    @Override
    protected boolean finished() {
        if (getTicks() == FINISH || getActiveSize() == 0) {
            return true;
        }

        if (!inLobby()) {
            if (portalSet.isEmpty()) {
                return true;
            }

            if (voidKnight.isRegistered() && voidKnight.getCurrentHealth() <= 0) {
                return true;
            }
        }

        return false;
    }

    private void spawnMonsters() {
        for (int index = 0; index < 5; index++) {
            spawn(Utility.randomElement(RAVAGERS), BLUE_BOUNDS, 5);
        }
        for (int index = 0; index < 5; index++) {
            spawn(Utility.randomElement(RAVAGERS), RED_BOUNDS, 5);
        }
        for (int index = 0; index < 5; index++) {
            spawn(Utility.randomElement(RAVAGERS), YELLOW_BOUNDS, 5);
        }
        for (int index = 0; index < 5; index++) {
            spawn(Utility.randomElement(RAVAGERS), PURPLE_BOUNDS, 5);
        }
        for (int index = 0; index < 2; index++) {
            spawn(Utility.randomElement(RAVAGERS), KNIGHT_BOUNDS, 6);
        }
    }

    private void spawn(int id, Position southWest, int size) {
        Position target = RandomUtils.random(TraversalMap.getTraversableTiles(southWest, size, size));
        Npc monster = new Npc(id, target);
        monsters.add(monster);
        add(monster);
    }

    private void agressiveMonsters() {
        if (monsters.isEmpty())
            return;
        if (voidKnight == null)
            return;
        for (Npc monster : monsters) {
            if (monster.getCombat().inCombat())
                continue;
            if (monster.getPosition().isWithinDistance(voidKnight.getPosition(), 10)) {
                monster.getCombat().attack(voidKnight);
                continue;
            }
            activities.keySet().forEach(mob -> {
                if (!mob.isPlayer())
                    return;
                if (monster.getPosition().isWithinDistance(mob.getPosition(), 8))
                    monster.getCombat().attack(mob);
            });
        }
    }

    private class Portal extends Npc {
        Portal(int id, Position position) {
            super(id, position);
            walk = false;
            getCombat().addListener(CANT_ATTACK);
        }
    }

    public class PestControlNode extends Activity {
        
        /** The amount of damage the player has dealt. */
        public int damage;
        private final Player player;

        private PestControlNode(Player player) {
            super(1, Entity.DEFAULT_INSTANCE);
            this.player = player;
        }

        @Override
        protected void start() {
            player.move(BOAT.transform(Utility.random(4), Utility.random(6)));
            player.dialogueFactory.sendNpcChat(1756, "Go with strength!", "Defend the void knight and destroy the portals!", "You are our only hope!").execute();
        }

        @Override
        public void finish() {
            if (!inLobby() && !Area.inPestControl(player)) {
                return;
            }

            getPanel().ifPresent(Activity_Panel::close);
            player.move(OUTSIDE_BOAT_POSITION);
            player.playerAssistant.restore();
            player.animate(Animation.RESET, true);
            player.graphic(Graphic.RESET, true);

            if (inLobby()) {
                return;
            }

            if (voidKnight.getCurrentHealth() <= 0) {
                player.dialogueFactory.sendNpcChat(1756, "You let the Void Knight die!", "Keep him alive you noobs...").execute();
                return;
            }

            if (portalSet.isEmpty()) {
                if (damage == 0) {
                    player.dialogueFactory.sendNpcChat(1756, "You have disgraced your squad by not dealing any damage.", "As punishment, you get no points! Teamwork next time!").execute();
                    return;
                }

                if (damage < 50) {
                    player.dialogueFactory.sendNpcChat(1756, "You only dealt " + damage + " damage this round.", "You need to deal at least 50 damage to receive a reward.").execute();
                    return;
                }

                int points = PlayerRight.getBloodMoney(player) / 100;
                AchievementHandler.activate(player, AchievementKey.PEST_CONTROL);
                player.activityLogger.add(ActivityLog.PEST_CONTROL);
                player.pestPoints += points;
                player.dialogueFactory.sendNpcChat(1756, "You have beaten the minigame!", "You were rewarded with " + points + " pest control points.", "You now have: " + player.pestPoints + ".").execute();
            } else {
                player.dialogueFactory.sendNpcChat(1756, "You have run out of time!", "You failed the mission and don't get any points.").execute();
            }
        }

        @Override
        public void cleanup() {}

        @Override
        public ActivityType getType() {
            return ActivityType.PEST_CONTROL;
        }

    }
    @Override
    public boolean canTeleport(Player player) {
        player.send(new SendMessage("Talk to the void-knight next to the boat to leave."));
        return false;
    }

    private final class PestControlPanel extends Activity_Panel {
        private final PestControlNode node;

        private PestControlPanel(Player player, PestControlNode node) {
            super(player, "Pest Control");
            this.node = node;
            node.setPanel(this);
        }

        public void update() {
            if (inLobby()) {
                set(0, "Next Departure: <col=FF5500>" + Utility.getTime(getTicks()) + "</col>");
                set(1, "Players Ready: <col=FF5500>" + getActiveSize() + "</col>");
                set(2, "(Need <col=FF5500>" + manager.getMinimumRequired() + "</col> to " + manager.getPlayerCapacity() + " players)");
                set(3, "Points: <col=FF5500>" + node.player.pestPoints + "</col>");
                setFooter("Players Ready:");
                setProgress((int) Utility.getPercentageAmount(getActiveSize(), manager.getPlayerCapacity()));
            } else {
                set(0, "Time remaining: <col=FF5500>" + Utility.getTime(getTicks()) + "</col>");
                set(1, "Knight's health: <col=FF5500>" + voidKnight.getCurrentHealth() + "</col>");
                set(2, "Damage: <col=FF5500>" + node.damage + "</col>");
                int dead = 0;
                for (int index = 0; index <= 3; index++) {
                    String value = "dead";
                    Portal portal = portals[index];
                    if (portal.getCurrentHealth() > 0) {
                        value = String.valueOf(portal.getCurrentHealth());
                    } else {
                        dead++;
                    }
                    set(3 + index, PORTAL_NAMES[index] + ": <col=FF5500>" + value + "</col>");
                }
                setFooter("Minigame Completion:");
                setProgress((int) Utility.getPercentageAmount(dead, 4));
            }
            setItem(new Item(11666));
        }
    }

}
