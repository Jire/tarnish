package com.osroyale;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static com.osroyale.Configuration.CHAR_PATH;

/**
 * Handles the customizable settings for the client.
 *
 * @author Daniel
 */
public final class Settings {
    private final static String FILE_NAME = "settings.dat";

    public static boolean REMEMBER_ME = true;
    public static boolean DRAW_BUBBLE;
    public static boolean DRAW_ANNOUNCEMENT = false;
    public static boolean LOGINSCREEN_HOVER_BOXES = false;
    public static boolean PROMPT_ON_CLOSE = true;

    static int CONTEXT_MENU;
    static int HITSPLAT;
    static int GAMEFRAME = 474;
    static int HP_BAR;
    static boolean PROFANITY_FILTER = false;


	static boolean HD_MINIMAP;
	static boolean TWEENING;
	static boolean GROUND_DECORATIONS = true;
	static boolean CUSTOM_LIGHTING;
	private static boolean MIPMAPPING;
	static boolean FOG;
	static boolean SMOOTH_SHADING;
	static boolean SNOW;
	static boolean PARTICLES = false;

    static boolean DAMAGE_MULTIPLIER;
    static boolean ENTITY_FEED = true;
    static boolean STATUS_ORB = true;
    static boolean SPECIAL_ATTACK_ORB = true;
    static boolean ATTACK_PRIORITY = true;
    static boolean WIDGET = true;
    static boolean MINIMAP_RANK;
    static boolean ROOF = true;
    static boolean MOVING_TEXTURE = true;
    static boolean NOTIFICATION_FEED = true;
    static boolean DISPLAY_NAMES;
    static boolean DISPLAY_CLAN_TAG = true;
    static boolean DISPLAY_KILL_FEED = true;
    static boolean DISPLAY_GROUND_ITEM;
    static boolean ITEM_RARITY_COLOR = true;
    static boolean EXPERIENCE_ORBS = false;
    static volatile boolean DISPLAY_PING = false;
    static boolean DISPLAY_FPS = false;
    static boolean VALUE_ICONS = true;
    static boolean SHIFT_DROP = true;

    public static volatile boolean FIRST_CLIENT_START = true;
    public static volatile boolean RESIZABLE = false;

    static int COUNTER_SIZE = 1;
    static int COUNTER_COLOR = 0xffffff;
    static float COUNTER_SPEED = 1.0f;
    static int COUNTER_POSITION;
    static boolean COUNTER_GROUP = true;
    static boolean COUNTER_PROGRESS = true;

    static int PRIVATE_MESSAGE = 65535;
    static int PLAYER_TITLE = 0x255;
    static int YELL = 65535;

    static void update(Client client) {
        int length = SettingData.values().length;
        for (int i = 0; i < length; i++) {
            SettingData setting = SettingData.forOrdinal(i);
            if (setting == null) {
                return;
            }
            Client.sendString(setting.setting, 50301 + i);
            client.toggleConfig(900 + i, setting.status() ? 1 : 0);
        }

        client.toggleConfig(880, CUSTOM_LIGHTING ? 1 : 0);
        client.toggleConfig(881, Client.transparentTabArea ? 1 : 0);
        client.toggleConfig(882, Client.changeChatArea ? 1 : 0);
        client.toggleConfig(883, Client.changeTabArea ? 1 : 0);
        client.toggleConfig(884, STATUS_ORB ? 1 : 0);
        client.toggleConfig(885, SPECIAL_ATTACK_ORB ? 1 : 0);
        client.toggleConfig(886, ROOF ? 1 : 0);
        client.toggleConfig(887, DAMAGE_MULTIPLIER ? 1 : 0);
        client.toggleConfig(888, HITSPLAT);
        client.toggleConfig(889, HP_BAR);
        client.toggleConfig(890, CONTEXT_MENU);
        client.toggleConfig(891, DISPLAY_FPS ? 1 : 0);
        client.toggleConfig(892, DISPLAY_PING ? 1 : 0);
        client.toggleConfig(893, VALUE_ICONS ? 1 : 0);

        //update strings = hitsplit, hp bar, context menu
    }

    static void config(Client client, int config) {
//		System.out.println(config);
        switch (config) {
            case 28520:
                HITSPLAT = 0;
                client.toggleConfig(888, HITSPLAT);
                return;
            case 28521:
                HITSPLAT = 1;
                client.toggleConfig(888, HITSPLAT);
                return;
            case 28522:
                HITSPLAT = 2;
                client.toggleConfig(888, HITSPLAT);
                return;
            case 28524:
                HP_BAR = 0;
                client.toggleConfig(889, HP_BAR);
                return;
            case 28525:
                HP_BAR = 1;
                client.toggleConfig(889, HP_BAR);
                return;
            case 28526:
                HP_BAR = 2;
                client.toggleConfig(889, HP_BAR);
                return;
            case 28528:
                CONTEXT_MENU = 0;
                client.toggleConfig(890, CONTEXT_MENU);
                Client.sendString("OSRS\\nMenu", 28532);
                return;
            case 28529:
                CONTEXT_MENU = 1;
                client.toggleConfig(890, CONTEXT_MENU);
                Client.sendString("614\\nMenu", 28532);
                return;
            case 28530:
                CONTEXT_MENU = 2;
                client.toggleConfig(890, CONTEXT_MENU);
                Client.sendString("634\\nMenu", 28532);
                return;
            case 28531:
                CONTEXT_MENU = 3;
                client.toggleConfig(890, CONTEXT_MENU);
                Client.sendString("Custom\\nMenu", 28532);
                return;
        }
    }

    /**
     * Handles clicking on the settings interface.
     */
    static boolean click(Client client, int button) {
//		        System.out.println(button);
        switch (button) {
            case 28505:
                CUSTOM_LIGHTING = !CUSTOM_LIGHTING;
                graphic(client, CUSTOM_LIGHTING);
                client.toggleConfig(880, CUSTOM_LIGHTING ? 1 : 0);
                break;
            case 28506:
                if (!Client.instance.isResized()) {
                    client.pushMessage("You can't do this while in fixed screen mode!", 0, "", false);
                } else {
                    Client.transparentTabArea = !Client.transparentTabArea;
                    client.toggleConfig(881, Client.transparentTabArea ? 1 : 0);
                }
                return true;
            case 28508:
                if (!Client.instance.isResized()) {
                    client.pushMessage("You can't do this while in fixed screen mode!", 0, "", false);
                } else {
                    Client.changeChatArea = !Client.changeChatArea;
                    client.toggleConfig(882, Client.changeChatArea ? 1 : 0);
                }
                return true;
            case 28510:
                if (!Client.instance.isResized()) {
                    client.pushMessage("You can't do this while in fixed screen mode!", 0, "", false);
                } else {
                    Client.changeTabArea = !Client.changeTabArea;
                    client.toggleConfig(883, Client.changeTabArea ? 1 : 0);
                }
                return true;
            case 28512:
                STATUS_ORB = !STATUS_ORB;
                client.toggleConfig(884, STATUS_ORB ? 1 : 0);
                return true;
            case 28514:
                SPECIAL_ATTACK_ORB = !SPECIAL_ATTACK_ORB;
                client.toggleConfig(885, SPECIAL_ATTACK_ORB ? 1 : 0);
                return true;
            case 28516:
                ROOF = !ROOF;
                client.toggleConfig(886, ROOF ? 1 : 0);
                return true;
            case 28518:
                DAMAGE_MULTIPLIER = !DAMAGE_MULTIPLIER;
                client.toggleConfig(887, DAMAGE_MULTIPLIER ? 1 : 0);
                return true;
            case 28533:
                DISPLAY_FPS = !DISPLAY_FPS;
                client.toggleConfig(891, DISPLAY_FPS ? 1 : 0);
                return true;
            case 28535:
                DISPLAY_PING = !DISPLAY_PING;
                client.toggleConfig(892, DISPLAY_PING ? 1 : 0);
                return true;
            case 28537:
                VALUE_ICONS = !VALUE_ICONS;
                client.toggleConfig(893, VALUE_ICONS ? 1 : 0);
                return true;
            case 28547:
            case 56750:
                client.colorOpen();
                return true;
        }

        int base_button = 50301;
        int index = button - base_button;

        SettingData settingData = SettingData.forOrdinal(index);

        if (!(index >= SettingData.values().length) && settingData != null) {
            settingData.handle(client);
            update(client);
            return true;
        }

        base_button = 50350;
        index = button - base_button;

        settingData = SettingData.forOrdinal(index);

        if (!(index >= SettingData.values().length) && settingData != null) {
            settingData.handle(client);
            update(client);
            return true;
        }
        return false;
    }

    /**
     * Handles changing the graphical mode.
     */
    private static void graphic(Client client, boolean high) {
        CUSTOM_LIGHTING = high;
        MOVING_TEXTURE = high;
        TWEENING = high;
        HD_MINIMAP = high;
        MIPMAPPING = high;
        FOG = high;
        SMOOTH_SHADING = high;
        PARTICLES = high;
        client.loadingStage = 1;
        client.minimapImage.method343();
        System.runFinalization();
        System.gc();
        update(client);
        client.pushMessage("Your client will now be performing in " + (!high ? "standard" : "high") + " definition.", false);
    }

    /**
     * Handles saving the
     */
    public static void save() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {

                    File dir = Paths.get(CHAR_PATH).toFile();

                    if (!dir.exists()) {
                        dir.mkdirs();
                    }

                    DataOutputStream out = new DataOutputStream(new FileOutputStream(Paths.get(CHAR_PATH, FILE_NAME).toFile()));

                    out.writeByte(1);
                    out.writeInt(CONTEXT_MENU);

                    out.writeByte(2);
                    out.writeInt(HITSPLAT);

                    out.writeByte(3);
                    out.writeInt(GAMEFRAME);

                    out.writeByte(4);
                    out.writeInt(HP_BAR);

                    out.writeByte(5);
                    out.writeBoolean(DAMAGE_MULTIPLIER);

                    out.writeByte(6);
                    out.writeBoolean(ENTITY_FEED);

                    out.writeByte(7);
                    out.writeBoolean(STATUS_ORB);

                    out.writeByte(8);
                    out.writeBoolean(SPECIAL_ATTACK_ORB);

                    out.writeByte(9);
                    out.writeBoolean(ATTACK_PRIORITY);

                    out.writeByte(10);
                    out.writeBoolean(WIDGET);

                    out.writeByte(11);
                    out.writeBoolean(MINIMAP_RANK);

                    out.writeByte(12);
                    out.writeBoolean(ROOF);

                    out.writeByte(13);
                    out.writeBoolean(MOVING_TEXTURE);

                    out.writeByte(14);
                    out.writeBoolean(NOTIFICATION_FEED);

                    out.writeByte(15);
                    out.writeBoolean(GROUND_DECORATIONS);

                    out.writeByte(16);
                    out.writeBoolean(CUSTOM_LIGHTING);

                    out.writeByte(17);
                    out.writeBoolean(HD_MINIMAP);

                    out.writeByte(18);
                    out.writeBoolean(MIPMAPPING);

                    out.writeByte(19);
                    out.writeBoolean(TWEENING);

                    out.writeByte(20);
                    out.writeBoolean(FOG);

                    out.writeByte(21);
                    out.writeBoolean(SMOOTH_SHADING);

                    out.writeByte(22);
                    out.writeInt(COUNTER_SIZE);

                    out.writeByte(23);
                    out.writeInt(COUNTER_COLOR);

                    out.writeByte(24);
                    out.writeInt(COUNTER_POSITION);

                    out.writeByte(25);
                    out.writeBoolean(COUNTER_GROUP);

                    out.writeByte(26);
                    out.writeInt(PRIVATE_MESSAGE);

                    out.writeByte(27);
                    out.writeInt(PLAYER_TITLE);

                    out.writeByte(28);
                    out.writeInt(YELL);

                    out.writeByte(29);
                    out.writeBoolean(DISPLAY_CLAN_TAG);

                    out.writeByte(30);
                    out.writeBoolean(DISPLAY_NAMES);

                    out.writeByte(31);
                    out.writeBoolean(DISPLAY_KILL_FEED);

                    out.writeByte(32);
                    out.writeBoolean(DISPLAY_GROUND_ITEM);

                    out.writeByte(33);
                    out.writeBoolean(ITEM_RARITY_COLOR);

                    out.writeByte(34);
                    out.writeBoolean(SNOW);

                    out.writeByte(35);
                    out.writeBoolean(PARTICLES);

                    out.writeByte(36);
                    out.writeBoolean(REMEMBER_ME);

                    out.writeByte(37);
                    out.writeBoolean(DRAW_BUBBLE);

                    out.writeByte(38);
                    out.writeBoolean(EXPERIENCE_ORBS);

                    out.writeByte(39);
                    out.writeFloat(COUNTER_SPEED);

                    out.writeByte(40);
                    out.writeBoolean(COUNTER_PROGRESS);

                    out.writeByte(41);
                    out.writeBoolean(DRAW_ANNOUNCEMENT);

                    out.writeByte(42);
                    out.writeBoolean(DISPLAY_FPS);

                    out.writeByte(43);
                    out.writeBoolean(DISPLAY_PING);

                    out.writeByte(44);
                    out.writeBoolean(VALUE_ICONS);

                    out.writeByte(45);
                    out.writeBoolean(SHIFT_DROP);

                    out.writeByte(46);
                    out.writeBoolean(LOGINSCREEN_HOVER_BOXES);

                    out.writeByte(47);
                    out.writeBoolean(PROMPT_ON_CLOSE);

                    out.writeByte(48);
                    out.writeBoolean(RESIZABLE);

                    out.writeByte(49);
                    out.writeBoolean(FIRST_CLIENT_START);

                    out.writeByte(0);
                    out.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    /**
     * Handles loading the
     */
    public static void load() {
        try {
            final Path path = Path.of(CHAR_PATH, FILE_NAME);
            if (!Files.exists(path)) {
                return;
            }

            final File file = path.toFile();
            try (final DataInputStream in = new DataInputStream(new BufferedInputStream(new FileInputStream(file)))) {
                while (!Thread.interrupted()) {
                    final int opcode = in.readByte();
                    if (handleOpcode(in, opcode)) {
                        break;
                    }
                }
            }
        } catch (final Exception e) {
            e.printStackTrace();
        }

        Settings.FIRST_CLIENT_START = false;
    }

    private static boolean handleOpcode(final DataInputStream in,
                                        final int opcode) throws IOException {
        switch (opcode) {
            case 0:
                return true;
            case 1:
                CONTEXT_MENU = in.readInt();
                break;
            case 2:
                HITSPLAT = in.readInt();
                break;
            case 3:
                GAMEFRAME = in.readInt();
                break;
            case 4:
                HP_BAR = in.readInt();
                break;
            case 5:
                DAMAGE_MULTIPLIER = in.readBoolean();
                break;
            case 6:
                ENTITY_FEED = in.readBoolean();
                break;
            case 7:
                STATUS_ORB = in.readBoolean();
                break;
            case 8:
                SPECIAL_ATTACK_ORB = in.readBoolean();
                break;
            case 9:
                ATTACK_PRIORITY = in.readBoolean();
                break;
            case 10:
                WIDGET = in.readBoolean();
                break;
            case 11:
                MINIMAP_RANK = in.readBoolean();
                break;
            case 12:
                ROOF = in.readBoolean();
                break;
            case 13:
                MOVING_TEXTURE = in.readBoolean();
                break;
            case 14:
                NOTIFICATION_FEED = in.readBoolean();
                break;
            case 15:
                GROUND_DECORATIONS = in.readBoolean();
                break;
            case 16:
                CUSTOM_LIGHTING = in.readBoolean();
                break;
            case 17:
                HD_MINIMAP = in.readBoolean();
                break;
            case 18:
                MIPMAPPING = in.readBoolean();
                break;
            case 19:
                TWEENING = in.readBoolean();
                break;
            case 20:
                FOG = in.readBoolean();
                break;
            case 21:
                SMOOTH_SHADING = in.readBoolean();
                break;
            case 22:
                COUNTER_SIZE = in.readInt();
                break;
            case 23:
                COUNTER_COLOR = in.readInt();
                break;
            case 24:
                COUNTER_POSITION = in.readInt();
                break;
            case 25:
                COUNTER_GROUP = in.readBoolean();
                break;
            case 26:
                PRIVATE_MESSAGE = in.readInt();
                break;
            case 27:
                PLAYER_TITLE = in.readInt();
                break;
            case 28:
                YELL = in.readInt();
                break;
            case 29:
                DISPLAY_CLAN_TAG = in.readBoolean();
                break;
            case 30:
                DISPLAY_NAMES = in.readBoolean();
                break;
            case 31:
                DISPLAY_KILL_FEED = in.readBoolean();
                break;
            case 32:
                DISPLAY_GROUND_ITEM = in.readBoolean();
                break;
            case 33:
                ITEM_RARITY_COLOR = in.readBoolean();
                break;
            case 34:
                SNOW = in.readBoolean();
                break;
            case 35:
                PARTICLES = in.readBoolean();
                break;
            case 36:
                REMEMBER_ME = in.readBoolean();
                break;
            case 37:
                DRAW_BUBBLE = in.readBoolean();
                break;
            case 38:
                EXPERIENCE_ORBS = in.readBoolean();
                break;
            case 39:
                COUNTER_SPEED = in.readFloat();
                break;
            case 40:
                COUNTER_PROGRESS = in.readBoolean();
                break;
            case 41:
                DRAW_ANNOUNCEMENT = in.readBoolean();
                break;
            case 42:
                DISPLAY_FPS = in.readBoolean();
                break;
            case 43:
                DISPLAY_PING = in.readBoolean();
                break;
            case 44:
                VALUE_ICONS = in.readBoolean();
                break;
            case 45:
                SHIFT_DROP = in.readBoolean();
                break;
            case 46:
                LOGINSCREEN_HOVER_BOXES = in.readBoolean();
                break;
            case 47:
                PROMPT_ON_CLOSE = in.readBoolean();
                break;
            case 48:
                RESIZABLE = in.readBoolean();
                break;
            case 49:
                FIRST_CLIENT_START = in.readBoolean();
                break;
        }
        return false;
    }

}
