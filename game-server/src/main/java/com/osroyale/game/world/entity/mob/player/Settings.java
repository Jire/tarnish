package com.osroyale.game.world.entity.mob.player;

import com.osroyale.content.clanchannel.content.ClanMemberComporator;
import com.osroyale.net.packet.out.SendConfig;
import com.osroyale.net.packet.out.SendScreenMode;
import com.osroyale.net.packet.out.SendZoom;

/**
 * Handles settings.
 *
 * @author Daniel
 */
public class Settings {
    /** The lock experience flag. */
    public boolean lockExperience = false;
    /** The TriviaBot flag. */
    public boolean triviaBot = true;
    /** The drop notification flag. */
    public boolean dropNotification = true;
    /** The untradeable notification flag. */
    public boolean untradeableNotification = true;
    /** Welcome screen flag. */
    public boolean welcomeScreen = true;
    /** Prestige colors flag. */
    public boolean prestigeColors = true;
    /** The yell flag. */
    public boolean yell = true;
    /** Brightness. */
    public int brightness = 3;
    /** Zoom. */
    public int zoom = -1;
    /** Auto-retaliate. */
    public boolean autoRetaliate = true;
    /** Mouse button flag. */
    public boolean mouse = false;
    /** Chat effects flag. */
    public boolean chatEffects = true;
    /** Accept aid flag. */
    public boolean acceptAid = false;
    /** Split Private Chat flag. */
    public boolean privateChat = true;
    /** Profanity filter flag/ */
    public boolean profanityFilter = false;
    /** Camera movement flag/ */
    public boolean cameraMovement = true;
    /** ESC Close flage */
    public boolean ESC_CLOSE = false;
    public int clientWidth = 765;
    public int clientHeight = 503;
    public boolean screenshotKill = true;

    /** The player instance. */
    public Player player;

    /** The clan sort type. */
    public ClanMemberComporator clanMemberComporator = ClanMemberComporator.PRIVILAGE;

    /** Constructs a new <code>Settings</code>. */
    public Settings(Player player) {
        this.player = player;
    }

    /** Handles a player logging in. */
    public void login() {
        setZoom(zoom, false);
        player.send(new SendConfig(172, autoRetaliate ? 1 : 0));
        player.send(new SendConfig(166, brightness));
        player.send(new SendConfig(152, player.movement.isRunningToggled() ? 1 : 0));
        player.send(new SendConfig(981, acceptAid ? 1 : 0));
        player.send(new SendConfig(171, chatEffects ? 1 : 0));
        player.send(new SendConfig(287, privateChat ? 1 : 0));
        player.send(new SendConfig(203, profanityFilter ? 1 : 0));
        player.send(new SendConfig(170, mouse ? 1 : 0));
        player.send(new SendConfig(207, cameraMovement ? 1 : 0));
        player.send(new SendConfig(980, 0));
        player.send(new SendConfig(594, ESC_CLOSE ? 1 : 0));
        player.send(new SendConfig(394, clanMemberComporator.ordinal()));
    }

    /** Sets the Zoom */
    public void setZoom(int zoom, boolean save) {
        int value = 0;
        int config = zoom > 4 ? 4 : (zoom == -1 ? 1 : zoom);
        switch (zoom) {
            case -1: value = 600;  break;
            case 1:  value = 200;  break;
            case 2:  value = 400;  break;
            case 3:  value = 800;  break;
            case 4:  value = 1000; break;
        }
        if (this.zoom != zoom)
            this.zoom = zoom;
        if (!save) {
            player.send(new SendZoom(value));
            player.send(new SendConfig(176, config + 1));
        }
    }

    /** Sets the brightness. */
    public void setBrightness(int brightness) {
        if (brightness > 4) {
            brightness = 4;
        } else if (brightness < 0) {
            brightness = 0;
        }
        this.brightness = (byte) brightness;
    }
}
