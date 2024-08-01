package com.osroyale.net.session;

import com.jcabi.jdbc.JdbcSession;
import com.osroyale.Config;
import com.osroyale.Starter;
import com.osroyale.content.bot.BotUtility;
import com.osroyale.game.service.ForumService;
import com.osroyale.game.world.World;
import com.osroyale.game.world.entity.mob.player.BannedPlayers;
import com.osroyale.game.world.entity.mob.player.Player;
import com.osroyale.game.world.entity.mob.player.persist.PlayerSerializer;
import com.osroyale.game.world.entity.mob.player.profile.Profile;
import com.osroyale.game.world.entity.mob.player.profile.ProfileRepository;
import com.osroyale.net.codec.game.GamePacketDecoder;
import com.osroyale.net.codec.game.GamePacketEncoder;
import com.osroyale.net.codec.login.LoginDetailsPacket;
import com.osroyale.net.codec.login.LoginResponse;
import com.osroyale.net.codec.login.LoginResponsePacket;
import com.osroyale.util.Stopwatch;
import com.osroyale.util.Utility;
import de.mkammerer.argon2.Argon2Factory.Argon2Types;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelPipeline;
import org.jire.tarnishps.Argon2;
import org.mindrot.jbcrypt.BCrypt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;
import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Represents a {@link Session} for authenticating users logging in.
 *
 * @author nshusa
 */
public final class LoginSession extends Session {

    private static final Logger logger = LoggerFactory.getLogger(LoginSession.class);

    private static final ConcurrentMap<String, FailedLoginAttempt> failedLogins = new ConcurrentHashMap<>();

    private final Starter starter;

    public LoginSession(Starter starter, Channel channel) {
        super(channel);
        this.starter = starter;
    }

    @Override
    public void handleClientPacket(Object o) {
        if (o instanceof LoginDetailsPacket) {
            LoginDetailsPacket packet = (LoginDetailsPacket) o;
            starter.getLoginExecutorService().execute(this, packet);
            //handleUserLoginDetails(packet);
        }
    }

    public void handleUserLoginDetails(final LoginDetailsPacket packet) {
        final ConcurrentMap<String, FailedLoginAttempt> failedLogins = LoginSession.failedLogins;

        final String username = packet.getUsername();

        final FailedLoginAttempt attempt = failedLogins.get(username);
        if (attempt != null) {
            final Stopwatch stopwatch = attempt.getStopwatch();
            final AtomicInteger atomicTime = attempt.getAttempt();
            final int time = atomicTime.get();
            if (time >= Config.FAILED_LOGIN_ATTEMPTS
                    && !stopwatch.elapsed(Config.FAILED_LOGIN_TIMEOUT, TimeUnit.MINUTES)) {
                sendFailedResponse(channel, LoginResponse.LOGIN_ATTEMPTS_EXCEEDED);
                return;
            } else if (time >= Config.FAILED_LOGIN_ATTEMPTS
                    && stopwatch.elapsed(Config.FAILED_LOGIN_TIMEOUT, TimeUnit.MINUTES)) {
                failedLogins.remove(username);
            } else {
                atomicTime.incrementAndGet();
            }
        }

        final Player player = new Player(username);
        final String password = packet.getPassword();
        player.setPassword(password);

        final LoginResponse response = evaluate(player);
        if (response == LoginResponse.INVALID_CREDENTIALS) {
            if (!failedLogins.containsKey(username)) {
                failedLogins.put(username, new FailedLoginAttempt());
            }
        } else if (response == LoginResponse.NORMAL) {
            failedLogins.remove(username);
        }

        final Channel channel = this.channel;

        if (response != LoginResponse.NORMAL) {
            sendFailedResponse(channel, response);
            return;
        }

        final Argon2Types argon2Type = Argon2.argon2Type(player.getPassword());
        if (argon2Type != Argon2.DEFAULT_TYPE) {
            // needs rehashing (this should be moved onto another thread, as hashing is slow)
            final String passwordHash = Argon2.getDefault().hash(
                    Argon2.DEFAULT_ITERATIONS,
                    Argon2.DEFAULT_MEMORY,
                    Argon2.DEFAULT_PARALLELISM,
                    password);
            player.setPassword(passwordHash); // update password to hashed version
        }

        ProfileRepository.put(new Profile(username, player.lastHost, player.hostList, player.right));

        channel.writeAndFlush(new LoginResponsePacket(response, player.right, false))
                .addListener((ChannelFutureListener) sourceFuture -> {
                    try {
                        final ChannelPipeline pipeline = channel.pipeline();
                        pipeline.replace("login-decoder",
                                "game-decoder", new GamePacketDecoder(packet.getDecryptor()));
                        pipeline.replace("login-encoder",
                                "game-encoder", new GamePacketEncoder(packet.getEncryptor()));

                        final GameSession session = new GameSession(channel, player);
                        channel.attr(Config.SESSION_KEY).set(session);
                        player.setSession(session);

                        World.queueLogin(player);
                    } catch (final Exception e) {
                        logger.error("Failed to queue login for \"" + username + "\"", e);
                    }
                });
    }

    private static void sendFailedResponse(final Channel channel, final LoginResponse response) {
        channel.writeAndFlush(new LoginResponsePacket(response))
                .addListener(ChannelFutureListener.CLOSE);
    }

    private LoginResponse evaluate(Player player) {
        final String username = player.getUsername();
        final String password = player.getPassword();
        final boolean isEmail = username.indexOf('@') != -1;

        // prevents users from logging in before the server is ready to accept connections
        if (!starter.isServerStarted()) {
            return LoginResponse.SERVER_BEING_UPDATED;
        }

        // prevents users from using accounts with bot names
        for (String botName : BotUtility.BOT_NAMES) {
            if (username.equalsIgnoreCase(botName)) {
                return LoginResponse.INSUFFICIENT_PERMSSION;
            }
        }

        // the world is currently full
        if (World.getPlayerCount() == Config.MAX_PLAYERS) {
            return LoginResponse.WORLD_FULL;
        }

        // prevents users from logging in if the world is being updated
        if (World.update.get()) {
            return LoginResponse.SERVER_BEING_UPDATED;
        }

        if (BannedPlayers.bans.contains(username.toLowerCase())) {
            return LoginResponse.ACCOUNT_DISABLED;
        }

        if (isEmail) {
            if (!Config.FORUM_INTEGRATION) {
                return LoginResponse.BAD_USERNAME;
            }

            if (username.length() > Config.EMAIL_MAX_CHARACTERS || username.length() < Config.EMAIL_MIN_CHARACTERS) {
                return LoginResponse.INVALID_EMAIL;
            }

            // does email have illegal characters
            if (!(username.matches("^[a-zA-Z0-9.@]{1," + Config.EMAIL_MAX_CHARACTERS + "}$"))) {
                return LoginResponse.INVALID_CREDENTIALS;
            }
        } else if (username.length() < Config.USERNAME_MIN_CHARACTERS) {
            return LoginResponse.SHORT_USERNAME;
        } else if (username.length() > Config.USERNAME_MAX_CHARACTERS) {
            return LoginResponse.BAD_USERNAME;
        } else if (World.getPlayerByHash(Utility.nameToLong(username)).isPresent()) { // this user is already online
            return LoginResponse.ACCOUNT_ONLINE;
        } else if (!(username.matches("^[a-zA-Z0-9 ]{1," + Config.USERNAME_MAX_CHARACTERS + "}$"))) { // does username have illegal characters
            return LoginResponse.INVALID_CREDENTIALS;
        } else if (password.isEmpty()/* || password.length() > Config.PASSWORD_MAX_CHARACTERS*/) {
            return LoginResponse.INVALID_CREDENTIALS;
        }

        if (World.search(username).isPresent()) {
            return LoginResponse.ACCOUNT_ONLINE;
        }

        if (Config.FORUM_INTEGRATION) {
            // check username and password from client with username and password from forum
            final LoginResponse response = authenticatedForumUser(player, isEmail);
            if (response != LoginResponse.NORMAL) {
                return response;
            }
        }

        LoginResponse response = PlayerSerializer.load(player, password);

        if (World.searchAll(username).isPresent()) {
            return LoginResponse.ACCOUNT_ONLINE;
        }

        return response;
    }

    private LoginResponse authenticatedForumUser(Player player, boolean isEmail) {
        final String username = player.getUsername();
        try {
            final LoginResponse response = new JdbcSession(ForumService.getConnectionPool())
                    .sql(isEmail ? "SELECT member_id, members_pass_hash, name, temp_ban FROM core_members WHERE UPPER(email) = ?" : "SELECT member_id, members_pass_hash, temp_ban FROM core_members WHERE UPPER(name) = ?")
                    .set(username.toUpperCase())
                    .select((rset, stmt) -> {
                        if (rset.next()) {
                            final int memberId = rset.getInt(1);
                            final String passwordHash = rset.getString(2);
                            final String forumUsername = isEmail ? rset.getString(3) : username;
                            final long unixTime = rset.getLong(isEmail ? 4 : 3);

                            if (isBanned(unixTime)) {
                                return LoginResponse.ACCOUNT_DISABLED;
                            }

                            if (passwordHash.isEmpty()) {
                                return LoginResponse.INVALID_CREDENTIALS;
                            } else if (BCrypt.checkpw(player.getPassword(), passwordHash)) {
                                player.setMemberId(memberId);
                                player.setUsername(forumUsername);
                                player.setPassword(passwordHash);
                                return LoginResponse.NORMAL;
                            } else {
                                return LoginResponse.INVALID_CREDENTIALS;
                            }
                        }
                        return LoginResponse.FORUM_REGISTRATION;
                    });
            return response;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return LoginResponse.LOGIN_SERVER_OFFLINE;
    }

    private boolean isBanned(long unixTime) {
        // not banned
        if (unixTime == 0) {
            return false;
        } else if (unixTime == -1) { // perm ban
            return true;
        }

        final Date date = Date.from(Instant.ofEpochSecond(unixTime));

        final Date currentDate = Date.from(Instant.now());

        return date.after(currentDate);
    }

    /**
     * A data class that represents a failed login attempt.
     *
     * @author nshusa
     */
    private static final class FailedLoginAttempt {

        private final AtomicInteger attempt = new AtomicInteger(0);
        private final Stopwatch stopwatch = Stopwatch.start();

        public AtomicInteger getAttempt() {
            return attempt;
        }

        public Stopwatch getStopwatch() {
            return stopwatch;
        }

    }

}
