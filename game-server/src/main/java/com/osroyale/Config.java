package com.osroyale;

import com.moandjiezana.toml.Toml;
import com.osroyale.game.world.WorldType;
import com.osroyale.game.world.entity.mob.player.appearance.Appearance;
import com.osroyale.game.world.entity.mob.player.appearance.Gender;
import com.osroyale.game.world.items.Item;
import com.osroyale.game.world.position.Position;
import com.osroyale.net.session.Session;
import io.netty.util.AttributeKey;
import io.netty.util.ResourceLeakDetector;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import it.unimi.dsi.fastutil.objects.ObjectSet;

import java.io.File;
import java.math.BigInteger;
import java.util.Arrays;

/**
 * The class that contains setting-related constants for the server.
 *
 * @author Daniel
 */
public final class Config {

    /** The latest announcement thread link. */
    public static final String LATEST_ANNOUNCEMENT_THREAD = "https://tarnishps.com/";

    /** The latest update thread link. */
    public static final String LATEST_UPDATE_THREAD = "https://tarnishps.com/";

    /** The welcome marquee. */
    public static final String[] WELCOME_MARQUEE = {
            "There are currently #players players online!",
            "Make sure to vote daily for great rewards!",
            "Donating really helps us with keeping the server alive"
    };

    /** The welcome dialogue. */
    public static final String[] WELCOME_DIALOGUE = {
            "Update threads are released every tuesday",
            "The owner is Apollo",
            "The community manager is Prodigy"
    };

    /** The welcome announcement. */
    public static final String[] WELCOME_ANNOUNCEMENT = {
            "Welcome!",
            "Tarnish is currently in it's",
            "early stages. We are only",
            "100 players until official",
            "launch!"
    };

    public static final String[] WELCOME_UPDATE = {
            "Game Updates",
            "[ February 24, 2018 ]",
            "Instant Pking, PvP instance",
            "Presets, New achievements",
            "& much more!"
    };

    /** The amount of client (incoming) packets that can be handled by the server each tick. */
    public static final int CLIENT_PACKET_THRESHOLD;
    /**
     * The amount of server (outgoing) packets that can be written by the server each tick.
     */
    public static final int SERVER_PACKET_THRESHOLD;

    /** Displays packet information in the output stream. */
    public static boolean DISPLAY_PACKETS = Config.SERVER_DEBUG;

    /**
     * The maximum amount of connections that can be active at a time, or in
     * other words how many clients can be logged in at once per connection.
     */
    public static final int CONNECTION_LIMIT;

    /** The number of seconds before a connection becomes idle. */
    public static final int IDLE_TIMEOUT;

    /**
     * The resource leak detection level, should be {@code PARANOID} in a
     * development environment and {@code DISABLED} in a production environment.
     */
    public static final ResourceLeakDetector.Level RESOURCE_LEAK_DETECTION;

    /** The list of exceptions that are ignored and discarded by the */
    public static final ObjectSet<String> IGNORED_EXCEPTIONS = new ObjectOpenHashSet<>(
            Arrays.asList("An existing connection was forcibly closed by the remote host",
                    "An established connection was aborted by the software in your host machine"));

    /** The session key. */
    public static final AttributeKey<Session> SESSION_KEY = AttributeKey.valueOf("session.key");

    public static final AttributeKey<String> SOURCE_ADDRESS = AttributeKey.valueOf("source.address");

    /** The RSA modulus. */
    public static final BigInteger RSA_MODULUS;

    /** The RSA exponent. */
    public static final BigInteger RSA_EXPONENT;

    public static final boolean SUPPORT_HAPROXY;
    public static final int IP_TOS;

    public static WorldType WORLD_TYPE = WorldType.LOCAL;

    public static final boolean LOG_PLAYER;

    /** This will use the parallel game game. */
    public static final boolean PARALLEL_GAME_ENGINE;

    /** The name of the server. */
    public static final String SERVER_NAME;

    /**
     * If forum integration is true, users can only login if they enter the same username and password that's on the forums
     * also mongo must be running when this is true. Since all the players will be stored in the mongo database.
     **/
    public static final boolean FORUM_INTEGRATION;

    /** This is used for mySQL to authenticate username and passwords from the forum. */
    public static final String FORUM_DB_URL;

    /** This is used for mySQL to authenticate username and passwords from the forum. */
    public static final String FORUM_DB_USER;

    /** This is used for mySQL to authenticate username and passwords from the forum. */
    public static final String FORUM_DB_PASS;

    public static final String POSTGRE_URL;
    public static final String POSTGRE_USER;
    public static final String POSTGRE_PASS;

    public static final int FAILED_LOGIN_ATTEMPTS;
    public static final int FAILED_LOGIN_TIMEOUT;

    public static final int CLIENT_VERSION;

    public static boolean highscoresEnabled = false;

    public static boolean DOUBLE_EXPERIENCE = false;

    /** The OS Royale community discord token. */
    public static final String DISCORD_TOKEN;

    /** The development state flag. (Always make sure you run the official server with this disabled!) */
    public static boolean SERVER_DEBUG;

    /** Displays the time in milliseconds each tick the game takes to run through a game cycle. */
    public static final boolean SERVER_CYCLE_DEBUG;

    /** The server port. */
    public static final int SERVER_PORT;

    /** The walking radius for Npc. */
    public static final int NPC_WALKING_RADIUS;

    /** The amount of players that can be logged in, in a single cycle **/
    public static final int LOGIN_THESHOLD;

    /** The amount of players that can logout in a single cycle **/
    public static final int LOGOUT_THESHOLD;

    /** The maximum amount of players that can be held within the game world. */
    public static final int MAX_PLAYERS;

    /** The maximum amount of npcs that can be held within the game world. */
    public static final int MAX_NPCS;

    /** The amount of bots that will spawn into the game world. */
    public static final int MAX_BOTS;

    /** The currency identification of the server. */
    public static final int CURRENCY = 995;

    /** The limit of the npc identification. */
    public static final int NPC_DEFINITION_LIMIT;

    /** The limit of the item identification. */
    public static final int ITEM_DEFINITION_LIMIT;

    /** The npc bits for the server which can execute 6755 npcs. */
    public static final int NPC_BITS;

    /**
     * The time in ticks a player remains skulled for.
     *
     * 720 ticks ~20 minutes
     */
    public static final int SKULL_TIME;

    /** All the tab identifications */
    public static final int ATTACK_TAB = 0, SKILL_TAB = 1, QUEST_TAB = 2, INVENTORY_TAB = 3, EQUIPMENT_TAB = 4, PRAYER_TAB = 5, MAGIC_TAB = 6, ACTIVITY_TAB = 7, FRIENDS_TAB = 8, IGNORE_TAB = 9, CLAN_TAB = 10, WRENCH_TAB = 11, EMOTE_TAB = 12, MUSIC_TAB = 13, LOGOUT_TAB = 14;

    /** The default, i.e. onSpawn, location. */
    public static final Position DEFAULT_POSITION = new Position(3087, 3497, 0);

    /** The donator zone. */
    public static final Position SUPER_DONATOR_ZONE = new Position(2426, 10419, 0);
    public static final Position REGULAR_DONATOR_ZONE = new Position(2478, 10435, 0);

    /** The staff zone. */
    public static final Position STAFF_ZONE = new Position(2602, 3874, 0);

    /** The Jail zone. */
    public static final Position JAIL_ZONE = new Position(2713, 2564, 0);

    public static final Position GAMBLING_ZONE = new Position(1886, 5025, 0);

    /** The default appearance of a player. */
    public static final Appearance DEFAULT_APPEARANCE = new Appearance(Gender.MALE,
            0, 10, 18, 26, 34, 36, 42, 0, 0, 0, 3, 1);

    /** Strings that are classified as bad. */
    public static final String[] BAD_STRINGS = {
            "fag", "f4g", "faggot", "nigger", "fuck", "bitch", "whore", "slut",
            "gay", "lesbian", "scape", ".net", ".org", "vagina", "dick",
            "cock", "penis", "hoe", "soulsplit", "ikov", "retard", "cunt",
            "g ay", "ga y", "g a y", "h o e",
    };

    /** Messages that are sent periodically to all players. */
    public static final String[] MESSAGES = {
            "Our website is located at: http://www.tarnishps.com/",
            "Feel free to join the (help) clan channel",
            "Add more security to your account by setting a bank pin.",
            "Have any ideas on how we could improve our gameplay? Post on forums!",
            "Voting daily can be very beneficial & it supports the server!",
            "Found a bug? Let a moderator know or post it on the forums!",
            "You can do ::commands for a list of commands!",
    };

    /**
     * Holds the array of all the side-bar identification and their
     * corresponding itemcontainer identification.
     */
    public static final int[][] SIDEBAR_INTERFACE = {
            {Config.ATTACK_TAB, 5855}, {Config.SKILL_TAB, 3917}, {Config.INVENTORY_TAB, 3213}, {Config.QUEST_TAB, 29400},
            {Config.EQUIPMENT_TAB, 1644}, {Config.PRAYER_TAB, 5608}, {Config.CLAN_TAB, 33500}, {Config.FRIENDS_TAB, 5065},
            {Config.IGNORE_TAB, 5715}, {Config.WRENCH_TAB, 50020}, {Config.EMOTE_TAB, 41000}, {Config.MUSIC_TAB, -1},
            {Config.ACTIVITY_TAB, -1}, {Config.LOGOUT_TAB, 2449}
    };

    public static final String WEBSITE_URL;

    /** The experience modification for combat. */
    public static final double COMBAT_MODIFICATION;

    /** The experience modification for agility. */
    public static final double AGILITY_MODIFICATION;

    /** The experience modification for cooking. */
    public static final double COOKING_MODIFICATION;

    /** The experience modification for crafting. */
    public static final double CRAFTING_MODIFICATION;

    /** The experience modification for firemaking. */
    public static final double FIREMAKING_MODIFICATION;

    /** The experience modification for fletching. */
    public static final double FLETCHING_MODIFICATION;

    /** The experience modification for herblore. */
    public static final double HERBLORE_MODIFICATION;

    /** The experience modification for hunter. */
    public static final double HUNTER_MODIFICATION;

    /** The experience modification for magic. */
    public static final double MAGIC_MODIFICATION;

    /** The experience modification for mining. */
    public static final double MINING_MODIFICATION;

    /** The experience modification for prayer. */
    public static final double PRAYER_MODIFICATION;

    /** The experience modification for runecrafting. */
    public static final double RUNECRAFTING_MODIFICATION;

    /** The experience modification for Slayer. */
    public static final double SLAYER_MODIFICATION;

    /** The experience modification for woodcutting. */
    public static final double WOODCUTTING_MODIFICATION;

    /** The experience modification for thieving. */
    public static final double THIEVING_MODIFICATION;

    /** The experience modification for smithing. */
    public static final double SMITHING_MODIFICATION;

    /** The experience modification for fishing. */
    public static final double FISHING_MODIFICATION;

    /** The experience modification for farming. */
    public static final double FARMING_MODIFICATION;

    public static final int EMAIL_MAX_CHARACTERS = 28;
    public static final int EMAIL_MIN_CHARACTERS = 7;
    public static final int USERNAME_MAX_CHARACTERS = 12;
    public static final int USERNAME_MIN_CHARACTERS = 3;
    public static final int PASSWORD_MAX_CHARACTERS = 20;

    static {
        try {
            final Toml parser = new Toml().read(new File("./settings.toml"));
            PARALLEL_GAME_ENGINE = parser.getBoolean("server.parallel_game_engine");
            SERVER_NAME = parser.getString("server.server_name");
            SERVER_PORT = Math.toIntExact(parser.getLong("server.server_port"));
            SERVER_DEBUG = parser.getBoolean("server.server_debug");
            SERVER_CYCLE_DEBUG = parser.getBoolean("server.server_cycle_debug");
            NPC_BITS = Math.toIntExact(parser.getLong("game.npc_bits"));
            NPC_WALKING_RADIUS = Math.toIntExact(parser.getLong("game.npc_walking_radius"));
            LOGIN_THESHOLD = Math.toIntExact(parser.getLong("network.login_threshold"));
            LOGOUT_THESHOLD = Math.toIntExact(parser.getLong("network.logout_threshold"));
            MAX_PLAYERS = Math.toIntExact(parser.getLong("game.max_players"));
            MAX_NPCS = Math.toIntExact(parser.getLong("game.max_npcs"));
            MAX_BOTS = Math.toIntExact(parser.getLong("game.max_bots"));
            NPC_DEFINITION_LIMIT = Math.toIntExact(parser.getLong("game.npc_definition_limit"));
            ITEM_DEFINITION_LIMIT = Math.toIntExact(parser.getLong("game.item_definition_limit"));
            WEBSITE_URL = parser.getString("website.website_url");
            FORUM_INTEGRATION = parser.getBoolean("server.website_integration");
            FORUM_DB_URL = parser.getString("website.forum_db_url");
            FORUM_DB_USER = parser.getString("website.forum_db_user");
            FORUM_DB_PASS = parser.getString("website.forum_db_pass");
            DISCORD_TOKEN = parser.getString("discord.token");
            COMBAT_MODIFICATION = parser.getDouble("game.combat_modifier");
            AGILITY_MODIFICATION = parser.getDouble("game.agility_modifier");
            COOKING_MODIFICATION = parser.getDouble("game.cooking_modifier");
            CRAFTING_MODIFICATION = parser.getDouble("game.crafting_modifier");
            FIREMAKING_MODIFICATION = parser.getDouble("game.firemaking_modifier");
            FLETCHING_MODIFICATION = parser.getDouble("game.fletching_modifier");
            HERBLORE_MODIFICATION = parser.getDouble("game.herblore_modifier");
            HUNTER_MODIFICATION = parser.getDouble("game.hunter_modifier");
            MAGIC_MODIFICATION = parser.getDouble("game.magic_modifier");
            MINING_MODIFICATION = parser.getDouble("game.mining_modifier");
            PRAYER_MODIFICATION = parser.getDouble("game.prayer_modifier");
            RUNECRAFTING_MODIFICATION = parser.getDouble("game.runecrafting_modifier");
            SLAYER_MODIFICATION = parser.getDouble("game.slayer_modifier");
            WOODCUTTING_MODIFICATION = parser.getDouble("game.woodcutting_modifier");
            THIEVING_MODIFICATION = parser.getDouble("game.thieving_modifier");
            SMITHING_MODIFICATION = parser.getDouble("game.smithing_modifier");
            FARMING_MODIFICATION = parser.getDouble("game.farming_modifier");
            FISHING_MODIFICATION = parser.getDouble("game.fishing_modifier");
            LOG_PLAYER = parser.getBoolean("game.log_player");
            SKULL_TIME = Math.toIntExact(parser.getLong("game.skull_time"));

            // network
            CONNECTION_LIMIT = Math.toIntExact(parser.getLong("network.connection_limit"));
            FAILED_LOGIN_ATTEMPTS = Math.toIntExact(parser.getLong("network.failed_login_attempts"));
            FAILED_LOGIN_TIMEOUT = Math.toIntExact(parser.getLong("network.failed_login_timeout"));
            IDLE_TIMEOUT = Math.toIntExact(parser.getLong("network.idle_timeout"));
            CLIENT_PACKET_THRESHOLD = Math.toIntExact(parser.getLong("network.client_packet_threshold"));
            SERVER_PACKET_THRESHOLD = Math.toIntExact(parser.getLong("network.server_packet_threshold"));
            DISPLAY_PACKETS = parser.getBoolean("network.display_packets");
            RESOURCE_LEAK_DETECTION = ResourceLeakDetector.Level.valueOf(parser.getString("network.resource_leak_detection").toUpperCase());
            ResourceLeakDetector.setLevel(RESOURCE_LEAK_DETECTION);
            RSA_MODULUS = new BigInteger(parser.getString("network.rsa_modulus"));
            RSA_EXPONENT = new BigInteger(parser.getString("network.rsa_exponent"));
            SUPPORT_HAPROXY = parser.getBoolean("network.support_haproxy");
            IP_TOS = Integer.parseInt(parser.getString("network.ip_tos"), 2);

            POSTGRE_URL = parser.getString("postgre.postgre_url");
            POSTGRE_USER = parser.getString("postgre.postgre_user");
            POSTGRE_PASS = parser.getString("postgre.postgre_pass");



            // o it's not actually using the setting lmao


            String world = parser.getString("world.type");

            switch (world) {
                case "LIVE":
                    WORLD_TYPE = WorldType.LIVE;
                    break;
                case "TEST":
                    WORLD_TYPE = WorldType.TEST;
                    break;
                case "LOCAL":
                    WORLD_TYPE = WorldType.LOCAL;
                    break;
            }

            CLIENT_VERSION = Math.toIntExact(parser.getLong("client.client_version"));

            highscoresEnabled = parser.getBoolean("services.highscores_enabled");
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new ExceptionInInitializerError("Failed to parse config file.");
        }
    }

    /** Starter bank */
    public final static int[] STARTER_BANK_AMOUNT = { 2, 8, 21, 11, 48, 9, 0, 0, 0, 0, };
    public final static Item[] STARTER_BANK = {
           new Item(995, 500000), new Item(8013, 2500), new Item(1704, 1000), new Item(1725, 1000), new Item(1731, 1000),
            new Item(2550, 1000), new Item(7458, 1000), new Item(7460, 1000), new Item(7461, 1000), new Item(3105, 1000),
            new Item(4587, 1000), new Item(1305, 1000), new Item(1377, 1000), new Item(1434, 1000), new Item(5698, 1000),
            new Item(8850, 1000), new Item(10828, 1000), new Item(3751, 1000), new Item(1163, 1000), new Item(1127, 1000),
            new Item(1079, 1000), new Item(1093, 1000), new Item(1201, 1000), new Item(4131, 1000), new Item(1153, 1000),
            new Item(1115, 1000), new Item(1067, 1000), new Item(1081, 1000), new Item(1191, 1000), new Item(544, 1000),
            new Item(542, 1000), new Item(861, 1000), new Item(892, 100000), new Item(9185, 1000), new Item(9242, 100000),
            new Item(9243, 100000), new Item(9244, 100000), new Item(10499, 1000), new Item(10498, 1000), new Item(2497, 1000),
            new Item(2491, 1000), new Item(2503, 1000), new Item(4675, 1000), new Item(1387, 1000), new Item(1383, 1000),
            new Item(1381, 1000), new Item(556, 100000), new Item(557, 100000), new Item(554, 100000), new Item(555, 100000),
            new Item(558, 100000), new Item(559, 100000), new Item(560, 100000), new Item(561, 100000), new Item(562, 100000),
            new Item(563, 100000), new Item(564, 100000), new Item(565, 100000), new Item(566, 100000), new Item(9075, 100000),
            new Item(21880, 100000), new Item(3841, 1000), new Item(2414, 1000), new Item(3843, 1000), new Item(2413, 1000),
            new Item(3839, 1000), new Item(2412, 1000), new Item(3749, 1000), new Item(3755, 1000), new Item(4089, 1000),
            new Item(4091, 1000), new Item(4093, 1000), new Item(4095, 1000), new Item(4097, 1000), new Item(4099, 1000),
            new Item(4101, 1000), new Item(4103, 1000), new Item(4105, 1000), new Item(4107, 1000), new Item(4109, 1000),
            new Item(4111, 1000), new Item(4113, 1000), new Item(4115, 1000), new Item(4117, 1000), new Item(6109, 1000),
            new Item(6107, 1000), new Item(6108, 1000), new Item(6111, 1000), new Item(6110, 1000), new Item(6106, 1000),
            new Item(391, 1000), new Item(385, 1000), new Item(3144, 1000), new Item(2436, 1000), new Item(2440, 1000),
            new Item(2442, 1000), new Item(2444, 1000), new Item(6685, 1000), new Item(3024, 1000),
    };

    /** TEMP VARS - WILL BE DELETED AFTER BETA IS FINISHED */
    public final static int[] LEET_BANK_AMOUNTS = {7, 46, 19, 42, 41, 15, 21, 4, 0, 0,};
    public final static Item[] LEET_BANK_ITEMS = {
            new Item(995, 10000), new Item(11665, 10000), new Item(11663, 10000), new Item(11664, 10000), new Item(8840, 10000),
            new Item(8839, 10000), new Item(8842, 10000), new Item(4151, 10000), new Item(11802, 10000), new Item(11804, 10000),
            new Item(11806, 10000), new Item(11808, 10000), new Item(13652, 10000), new Item(13576, 10000), new Item(4587, 10000),
            new Item(1305, 10000), new Item(7158, 10000), new Item(1434, 10000), new Item(5698, 10000), new Item(1215, 10000),
            new Item(11840, 10000), new Item(6585, 10000), new Item(12954, 10000), new Item(11283, 10000), new Item(10551, 10000),
            new Item(4153, 10000), new Item(10828, 10000), new Item(3751, 10000), new Item(2550, 10000), new Item(3105, 10000),
            new Item(1725, 10000), new Item(1704, 10000), new Item(1731, 10000), new Item(1163, 10000), new Item(1127, 10000),
            new Item(1079, 10000), new Item(1201, 10000), new Item(7460, 10000), new Item(4131, 10000), new Item(11832, 10000),
            new Item(11834, 10000), new Item(11836, 10000), new Item(21015, 10000), new Item(6570, 10000), new Item(13239, 10000),
            new Item(11773, 10000), new Item(12931, 10000), new Item(13197, 10000), new Item(13199, 10000), new Item(19639, 10000),
            new Item(19643, 10000), new Item(19647, 10000), new Item(11772, 10000), new Item(861, 10000), new Item(892, 10000),
            new Item(9185, 10000), new Item(9244, 10000), new Item(868, 10000), new Item(811, 10000), new Item(10498, 10000),
            new Item(10499, 10000), new Item(3749, 10000), new Item(2503, 10000), new Item(2497, 10000), new Item(2491, 10000),
            new Item(19481, 10000), new Item(11785, 10000), new Item(11826, 10000), new Item(11828, 10000), new Item(11830, 10000),
            new Item(13237, 10000), new Item(11771, 10000), new Item(4675, 10000), new Item(12904, 10000), new Item(11791, 10000),
            new Item(11770, 10000), new Item(13235, 10000), new Item(3755, 10000), new Item(1540, 10000), new Item(6889, 10000),
            new Item(2412, 10000), new Item(3840, 10000), new Item(2413, 10000), new Item(3844, 10000), new Item(2414, 10000),
            new Item(3842, 10000), new Item(4089, 10000), new Item(4091, 10000), new Item(4093, 10000), new Item(4095, 10000),
            new Item(4097, 10000), new Item(4099, 10000), new Item(4101, 10000), new Item(4103, 10000), new Item(4105, 10000),
            new Item(4107, 10000), new Item(4109, 10000), new Item(4111, 10000), new Item(4113, 10000), new Item(4115, 10000),
            new Item(4117, 10000), new Item(554, 10000), new Item(555, 10000), new Item(556, 10000), new Item(557, 10000),
            new Item(558, 10000), new Item(559, 10000), new Item(560, 10000), new Item(561, 10000), new Item(562, 10000),
            new Item(563, 10000), new Item(564, 10000), new Item(565, 10000), new Item(9075, 10000), new Item(6685, 10000),
            new Item(3024, 10000), new Item(2436, 10000), new Item(2440, 10000), new Item(2442, 10000), new Item(2444, 10000),
            new Item(3040, 10000), new Item(2434, 10000), new Item(2448, 10000), new Item(6687, 10000), new Item(3026, 10000),
            new Item(145, 10000), new Item(157, 10000), new Item(163, 10000), new Item(169, 10000), new Item(3042, 10000),
            new Item(139, 10000), new Item(181, 10000), new Item(6689, 10000), new Item(3028, 10000), new Item(147, 10000),
            new Item(159, 10000), new Item(165, 10000), new Item(171, 10000), new Item(3044, 10000), new Item(141, 10000),
            new Item(183, 10000), new Item(6691, 10000), new Item(3030, 10000), new Item(149, 10000), new Item(161, 10000),
            new Item(167, 10000), new Item(173, 10000), new Item(3046, 10000), new Item(143, 10000), new Item(185, 10000),
            new Item(12695, 10000), new Item(3144, 10000), new Item(391, 10000), new Item(7060, 10000), new Item(385, 10000),
            new Item(4315, 10000), new Item(4317, 10000), new Item(4319, 10000), new Item(4335, 10000), new Item(4337, 10000),
            new Item(4339, 10000), new Item(4355, 10000), new Item(4357, 10000), new Item(4359, 10000), new Item(4375, 10000),
            new Item(4377, 10000), new Item(4379, 10000), new Item(4395, 10000), new Item(4397, 10000), new Item(4399, 10000),
            new Item(4716, 10000), new Item(4718, 10000), new Item(4720, 10000), new Item(4722, 10000), new Item(4745, 10000),
            new Item(4747, 10000), new Item(4749, 10000), new Item(4751, 10000), new Item(4732, 10000), new Item(4734, 10000),
            new Item(4736, 10000), new Item(4738, 10000), new Item(4740, 10000), new Item(4708, 10000), new Item(4710, 10000),
            new Item(4712, 10000), new Item(4714, 10000), new Item(4753, 10000), new Item(4755, 10000), new Item(4757, 10000),
            new Item(4759, 10000), new Item(12829, 10000), new Item(12821, 10000), new Item(12817, 10000), new Item(12825, 10000),
    };
}
