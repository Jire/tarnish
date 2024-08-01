package com.osroyale.game.world;

import com.osroyale.Config;
import com.osroyale.content.activity.impl.pestcontrol.PestControlLobby;
import com.osroyale.content.bot.PlayerBot;
import com.osroyale.content.clanchannel.ClanRepository;
import com.osroyale.content.tradingpost.TradingPost;
import com.osroyale.game.Graphic;
import com.osroyale.game.Projectile;
import com.osroyale.game.ProjectileTest;
import com.osroyale.game.event.bus.DataBus;
import com.osroyale.game.event.listener.WorldEventListener;
import com.osroyale.game.task.Task;
import com.osroyale.game.task.impl.PlayerRemovalTask;
import com.osroyale.game.task.impl.SystemUpdateEvent;
import com.osroyale.game.world.entity.MobList;
import com.osroyale.game.world.entity.mob.Mob;
import com.osroyale.game.world.entity.mob.npc.Npc;
import com.osroyale.game.world.entity.mob.player.Player;
import com.osroyale.game.world.entity.mob.player.PlayerRight;
import com.osroyale.game.world.entity.mob.player.persist.PlayerSerializer;
import com.osroyale.game.world.object.GameObject;
import com.osroyale.game.world.position.Area;
import com.osroyale.game.world.position.Position;
import com.osroyale.game.world.region.Region;
import com.osroyale.game.world.region.RegionManager;
import com.osroyale.net.packet.out.*;
import com.osroyale.util.GameSaver;
import com.osroyale.util.Utility;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jire.tarnishps.task.TaskManager;

import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Predicate;

/**
 * Represents the game world.
 *
 * @author Daniel
 * @author Michael
 */
public final class World {

    private static final Logger logger = LogManager.getLogger();

    /** The players registered in this world. */
    private final MobList<Player> players = new MobList<>(Config.MAX_PLAYERS);

    /** The npcs registered in this world. */
    private final MobList<Npc> npcs = new MobList<>(Config.MAX_NPCS);

    /** The {@link Player}s waiting to login. */
    private final Queue<Player> logins = new ConcurrentLinkedQueue<>();

    /** The {@link Player}s waiting to onLogout. */
    private final Queue<Player> logouts = new ConcurrentLinkedQueue<>();

    /** The task manager. */
    private final TaskManager taskManager;

    /** The region manager. */
    private final RegionManager regionManager = new RegionManager();

    public static final AtomicBoolean update = new AtomicBoolean(false);

    public static int addition = 0;


    private static final DataBus dataBus = DataBus.getInstance();

    /** The world instance. */
    private static final World WORLD = new World();

    private World() {
        dataBus.subscribe(new WorldEventListener());
        taskManager = new TaskManager();
    }

    /** Handles the world sequencing. */
    public void sequence() {

    }

    /** Saves all the game data. */
    public static void save() {
        System.out.println("Saving Tarnish...");
        get().players.forEach(PlayerSerializer::save);
        logger.info("All players were successfully saved.");
       /* GlobalRecords.save();
        logger.info("All global records were successfully saved.");*/
        ClanRepository.saveAllActiveClans();
        logger.info("All clans were successfully saved.");
        TradingPost.saveRecentHistory();
        TradingPost.saveAllItemHistory();
        logger.info("All trading post history was successfully saved");
        GameSaver.save();
        logger.info("All game data were successfully saved.");
    }

    /** Updates the server. */
    public static void update(int time) {
        if (!update.get()) {
            update.set(true);
            schedule(new SystemUpdateEvent(time));
            get().players.stream().forEach(it -> it.send(new SendGameMessage(0, time / 100, "System update" + " in:")));
        }
    }

    /** Shuts down the server. */
    public static void shutdown() {
        System.out.println("shutting down...");
        System.exit(0);
    }

    /** Handles queueing the player logins. */
    public static void queueLogin(Player player) {
        if (player.isBot) {
            get().logins.add(player);
        } else {
            player.getSession().ifPresent(it -> World.get().logins.add(player));
        }
    }

    /** Handles queueing the player logouts. */
    public static void queueLogout(Player player) {
        if (player == null || get().logouts.contains(player)) {
            return;
        }

        get().logouts.add(player);
    }

    /** Gets a player by name. */
    public static Optional<Player> search(String name) {
        for (Player player : get().players) {
            if (player == null) {
                continue;
            }

            if (player.getUsername().equalsIgnoreCase(name)) {
                return Optional.of(player);
            }
        }
        return Optional.empty();
    }

    public static Optional<Player> searchAll(String name) {
        for (Player player : get().players) {
            if (player == null) {
                continue;
            }

            if (player.getUsername().equalsIgnoreCase(name)) {
                return Optional.of(player);
            }
        }

        for (Player player : World.get().getLogins()) {
            if (player == null) {
                continue;
            }

            if (player.getUsername().equalsIgnoreCase(name)) {
                return Optional.of(player);
            }
        }
        return Optional.empty();
    }

    public static Optional<Player> getPlayerByHash(long usernameHash) {
        for (Player player : World.getPlayers()) {
            if (Objects.equals(player.usernameLong, usernameHash))
                return Optional.of(player);
        }
        return Optional.empty();
    }

    public static Optional<Player> getPlayerByName(String username) {
        for (Player player : World.getPlayers()) {
            if (Objects.equals(player.getUsername(), username))
                return Optional.of(player);
        }
        return Optional.empty();
    }

    /** Gets a player by index. */
    public static Optional<Player> getPlayerBySlot(int index) {
        for (Player player : World.getPlayers()) {
            if (player.getIndex() == index)
                return Optional.of(player);
        }
        return Optional.empty();
    }

    /** Gets an npc by index. */
    public static Optional<Npc> getNpcBySlot(int index) {
        for (Npc npc : World.getNpcs()) {
            if (npc.getIndex() == index)
                return Optional.of(npc);
        }
        return Optional.empty();
    }

    public static void kickPlayer(Player other) {
        other.send(new SendLogout());
        queueLogout(other);
    }

    public static void kickPlayer(Predicate<Player> condition) {
        for (Player player : get().players) {
            if (player.isBot)
                continue;
            if (!condition.test(player))
                continue;
            player.send(new SendLogout());
            queueLogout(player);
        }
    }

    public void process() {
        try {
            if (!taskManager.process()) {
                logger.warn("Task manager was cancelled mid-process");
            }
        } catch (Exception ex) {
            logger.error("Error sequencing task manager", ex);
        }
        try {
            PestControlLobby.sequence();
        } catch (Exception ex) {
            logger.error("Error sequencing pest control.", ex);
        }
    }

    /** Submits a new event. */
    public static boolean schedule(Task task) {
        return get().taskManager.schedule(task);
    }

    /** Executes a runnable on a delay. */
    public static boolean schedule(int delay, Runnable runnable) {
        return schedule(new Task(delay) {
            @Override
            public void execute() {
                runnable.run();
                cancel();
            }
        });
    }

    /** Executes a runnable on a 1 tick delay. */
    public static boolean schedule(Runnable runnable) {
        return schedule(1, runnable);
    }

    /** Sends a graphic to the world. */
    public static void sendGraphic(Graphic graphic, Position position, int instance) {
        for (Player player : getPlayers()) {
            if (player.instance != instance)
                continue;
            if (!Utility.withinDistance(player, position, Region.VIEW_DISTANCE))
                continue;
            player.send(new SendGraphic(graphic, position));
        }
    }
    public static void sendGraphic(Graphic graphic, Position position) {
        get().players.stream().filter(player -> Utility.withinDistance(player, position, Region.VIEW_DISTANCE))
                .forEach(player -> {
                    player.send(new SendGraphic(graphic, position));
                });
    }
    /** Sends a world object animation. */
    public static void sendObjectAnimation(int animation, GameObject object) {
        for (Player player : getPlayers()) {
            if (player.instance != object.getInstancedHeight())
                continue;
            if (!Utility.withinDistance(player, object.getPosition(), Region.VIEW_DISTANCE))
                continue;
            player.send(new SendObjectAnimation(animation, object));
        }
    }

    /** Sends a world projectile. */
    public static void sendProjectile(Projectile projectile, Position position, int instance, int lock, byte offsetX, byte offsetY) {
        for (Player player : getPlayers()) {
            if (player.instance != instance)
                continue;
            if (!Utility.withinDistance(player, position, Region.VIEW_DISTANCE))
                continue;
            player.send(new SendProjectile(projectile, position, lock, offsetX, offsetY));

        }
    }

    /** Sends a world projectile. */
    public static void sendProjectile(Mob source, Mob target, Projectile projectile) {
        int lockon = target.isNpc() ? target.getIndex() + 1 : -target.getIndex() - 1;
        int sourceX = source.getX() + source.width() / 2, sourceY = source.getY() + source.length() / 2;
        int targetX = target.getX() + target.width() / 2, targetY = target.getY() + target.length() / 2;
        byte offsetX = (byte) (targetX - sourceX);
        byte offsetY = (byte) (targetY - sourceY);
        Position center = new Position(sourceX, sourceY);

        for (Player player : getPlayers()) {
            if (source.instance != player.instance)
                continue;
            if (!Utility.withinDistance(target, player, Region.VIEW_DISTANCE))
                continue;
            player.send(new SendProjectile(projectile, center, lockon, offsetX, offsetY));
        }
    }

    public static void sendProjectile(Position source, Mob target, Projectile projectile) {
        int lockon = target.isNpc() ? target.getIndex() + 1 : -target.getIndex() - 1;
        int sourceX = source.getX(), sourceY = source.getY();
        int targetX = target.getX() + target.width() / 2, targetY = target.getY() + target.length() / 2;
        byte offsetX = (byte) (targetX - sourceX);
        byte offsetY = (byte) (targetY - sourceY);
        Position center = new Position(sourceX, sourceY);

        for (Player player : getPlayers()) {
            if (!Utility.withinDistance(target, player, Region.VIEW_DISTANCE))
                continue;
            player.send(new SendProjectile(projectile, center, lockon, offsetX, offsetY));
        }
    }

    /** Sends a world projectile. */
    public static void sendProjectile(Mob source, Position target, Projectile projectile) {
        int sourceX = source.getX() + source.width() / 2, sourceY = source.getY() + source.length() / 2;
        Position center = new Position(sourceX, sourceY);
        byte offsetX = (byte) (target.getX() - sourceX);
        byte offsetY = (byte) (target.getY() - sourceY);
        for (Player player : getPlayers()) {
            if (source.instance != player.instance)
                continue;
            if (!Utility.withinDistance(player, target, Region.VIEW_DISTANCE))
                continue;
            player.send(new SendProjectile(projectile, center, -1, offsetX, offsetY));
        }
    }

    public static void sendProjectile(Position source, Position target, Projectile projectile) {
        int sourceX = source.getX(), sourceY = source.getY();
        Position center = new Position(sourceX, sourceY);
        byte offsetX = (byte) (target.getX() - sourceX);
        byte offsetY = (byte) (target.getY() - sourceY);
        for (Player player : getPlayers()) {
            if (!Utility.withinDistance(player, target, Region.VIEW_DISTANCE))
                continue;
            player.send(new SendProjectile(projectile, center, -1, offsetX, offsetY));
        }
    }

    public static int executeProjectile(Mob entity, ProjectileTest projectile) {
        if (projectile == null) {
            return 0;
        }
        Position source = projectile.getStart();
        Position target = projectile.getTarget();
        if (source == null || target == null) {
            return 0;
        }
        Position delta = projectile.getTarget().getDelta(source, target);

        int distance = entity.getPosition().getChevDistance(target);

        if (distance <= 60) {
            int creatorSize = projectile.getCreatorSize() == -1 ? 1 : projectile.getCreatorSize(); //TODO size

            for (Player player : getPlayers()) {
                if (player == null) {
                    continue;
                }

                if (source.isViewableFrom(player.getPosition())) {
                    player.send(new SendProjectileTestPacket(source, delta, projectile.getSlope(), projectile.getSpeed(), projectile.getProjectileId(), projectile.getStartHeight(), projectile.getEndHeight(), projectile.getLockon(), projectile.getDelay(), creatorSize, projectile.getStartDistanceOffset()));
                }
            }
        }
        return projectile.getHitDelay(distance);
    }

    /** Sends a global message. */
    public static void sendMessage(String... messages) {
        for (Player player : getPlayers()) {
            if (player != null)
                player.message(messages);
        }
    }

    /** Sends a global message with an exception. */
    public static void sendMessage(String message, Predicate<Player> filter) {
        for (Player player : getPlayers()) {
            if (player != null && filter.test(player))
                player.message(message);
        }
    }

    public static void sendStaffMessage(String... messages) {
        for (Player player : getStaff()) {
            player.message(messages);
        }
    }

    /** Sends a game message. */
    public static void sendBroadcast(int time, String message, boolean countdown) {
        get().players.stream().forEach($it -> {
            $it.send(new SendGameMessage(countdown ? 0 : 1, time, Utility.capitalizeSentence(message)));
            $it.send(new SendMessage("<col=2C7526>Broadcast: </col>" + Utility.capitalizeSentence(message)));
        });
    }

    public void dequeLogins() {
        if (logins.isEmpty()) {
            return;
        }

        for (int index = 0; index < Config.LOGIN_THESHOLD; index++) {
            Player player = logins.poll();

            if (player == null) {
                break;
            }

            try {
                player.register();
            } catch (Exception ex) {
                logger.error(String.format("error registering %s", player), ex);
            }

        }
    }

    public void dequeLogouts() {
        if (logouts.isEmpty()) {
            return;
        }

        for (int index = 0; index < Config.LOGOUT_THESHOLD; index++) {
            Player player = logouts.poll();

            if (player == null) {
                break;
            }

            World.schedule(new PlayerRemovalTask(player, false));
        }
    }

    public int getWildernessResourcePlayers() {
        int count = 0;
        for (Player player : getPlayers()) {
            if (player != null && Area.inWildernessResource(player))
                count++;
        }
        return count;
    }

    /** Sends a kill feed notification. */
    public static void sendKillFeed(final Mob killer, final Mob victim) {
        if (victim.isNpc()) {
            if (Area.inWilderness(victim)) {
                return;
            }
            if (!victim.getNpc().npcAssistant.isBoss()) {
                return;
            }
        }

        for (final Player player : getPlayers()) {
            player.send(new SendKillFeed(killer, victim));
        }
    }

    /** Gets the amount of valid players online. */
    public static int getPlayerCount() {
        return Math.toIntExact(getPlayers().size() + addition);
    }

    public static int getBotCount() {
        return PlayerBot.BOT_COUNT.get();
    }

    public static int getStaffCount() {
        int count = 0;

        for (Player player : getPlayers()) {
            if (player != null && (PlayerRight.isModerator(player) || player.right == PlayerRight.HELPER))
                count++;
        }

        return count;
    }

    public static int getWildernessCount() {
        int count = 0;

        for (Player player : getPlayers()) {
            if (player != null && !player.isBot && Area.inWilderness(player))
                count++;
        }

        return count;
    }

    /** Gets the staff players currently online. */
    public static List<Player> getStaff() {
        List<Player> staff = new ArrayList<>();

        for (Player player : getPlayers()) {
            if (player != null && (PlayerRight.isModerator(player) || player.right == PlayerRight.HELPER))
                staff.add(player);
        }

        return staff;
    }

    public static World get() {
        return WORLD;
    }

    public static boolean cancelTask(Object attachment) {
        return cancelTask(attachment, false);
    }

    public static boolean cancelTask(Object attachment, boolean logout) {
        return get().taskManager.cancel(attachment, logout);
    }

    public static MobList<Player> getPlayers() {
        return get().players;
    }

    public static MobList<Npc> getNpcs() {
        return get().npcs;
    }

    public static RegionManager getRegions() {
        return get().regionManager;
    }

    public TaskManager getTaskManager() {
        return taskManager;
    }

    public Queue<Player> getLogins() {
        return logins;
    }

    public Queue<Player> getLogouts() {
        return logouts;
    }

    public static DataBus getDataBus() {
        return dataBus;
    }

}
