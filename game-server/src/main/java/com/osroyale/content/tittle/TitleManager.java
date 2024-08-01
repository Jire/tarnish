package com.osroyale.content.tittle;

import com.osroyale.game.world.entity.mob.UpdateFlag;
import com.osroyale.game.world.entity.mob.player.Player;
import com.osroyale.net.packet.out.*;
import com.osroyale.util.Utility;

/**
 * Handles unlocking player titles.
 *
 * @author Daniel
 */
public class TitleManager {

    /** The base button identification. */
    private final static int BUTTON_IDENTIFICATION = -26485;

    /** Opens the title itemcontainer. */
    public static void open(Player player) {
        refresh(player);
        player.interfaceManager.open(39_000);
    }

    /** Handles clicking buttons on the title itemcontainer. */
    public static boolean click(Player player, int button) {
        int ordinal = (button - BUTTON_IDENTIFICATION) / 2;
        if (Title.forOrdinal(ordinal).isPresent()) {
            player.attributes.set("PLAYER_TITLE_KEY", ordinal);
            refresh(player);
            return true;
        }
        return false;
    }

    /** Handles refreshing (send all strings and data) the itemcontainer. */
    public static void refresh(Player player) {
        int ordinal = player.attributes.get("PLAYER_TITLE_KEY", Integer.class);
        int string = 39_052;
        int config = 750;
        Title view = null;

        for (Title title : Title.values()) {
            if (title.ordinal() == ordinal) {
                view = title;
            }
            player.send(new SendString((title.ordinal() == ordinal ? "<col=DEB07A>" : "<col=A8865E>") + title.getTitle().getTitle(), string));
            player.send(new SendTooltip("View title: <col=db9423>" + title.getTitle().getTitle() + "</col>", string - 1));
            player.send(new SendConfig(config, title.activated(player) ? 1 : 0));
            string += 2;
            config += 2;
        }

        if (view == null) {
            return;
        }

        String color = Integer.toHexString(view.getTitle().getColor());
        int scroll = Title.values().length * 26;

        for (int index = 0; index < view.getRequirement().length; index++) {
            player.send(new SendString(view.getRequirement()[index], 39006 + index));
        }
        player.send(new SendString("<col=" + (view.activated(player) ? "A1D490>- UNLOCKED -" : "E34F52>- LOCKED -"), 39019));
        player.send(new SendString("<col=" + color + ">" + view.getTitle().getTitle() + "<col=DEB07A> " + Utility.formatName(player.getName()), 39_003));
        player.send(new SendScrollbar(39_050, scroll));
    }

    /** Handles a player redeeming a title. */
    public static void redeem(Player player) {
        int ordinal = player.attributes.get("PLAYER_TITLE_KEY", Integer.class);
        if (Title.forOrdinal(ordinal).isPresent()) {
            Title title = Title.forOrdinal(ordinal).get();
            if (!title.activated(player)) {
                player.send(new SendMessage("You have not activated this title yet!"));
                return;
            }

            player.playerTitle = title.getTitle();
            player.updateFlags.add(UpdateFlag.APPEARANCE);
            player.send(new SendMessage("You have successfully redeemed your title."));
        }
    }

    /** Handles reseting the player title. */
    public static void reset(Player player) {
        player.playerTitle = PlayerTitle.empty();
        player.updateFlags.add(UpdateFlag.APPEARANCE);
        player.send(new SendMessage("You have successfully reset your title."));
    }
}
