package com.osroyale.content;

import com.osroyale.game.world.World;
import com.osroyale.game.world.entity.mob.player.Player;
import com.osroyale.game.world.entity.mob.player.PlayerRight;
import com.osroyale.net.packet.out.SendMessage;
import com.osroyale.util.Utility;

import java.util.Arrays;

/**
 * Handles the yelling command.
 *
 * @author Daniel
 */
public class Yell {

    /** Array of all invalid strings. */
    public static final String[] INVALID = {
            ".com", "@cr", "<img=", "</col", "<col=",
            "@whi@", "@blu@", "@gre@", "@red@",
            "@mag@", "@cya@"
    };

    /** Yells a message to the server. */
    public static void yell(Player player, String message) {
        if (!PlayerRight.isDonator(player) && !PlayerRight.isHelper(player) && !PlayerRight.isOwner(player)) {
            player.send(new SendMessage("You must be a donator to use this command!"));
            return;
        }

        if (!player.settings.yell) {
            player.send(new SendMessage("You can not send a yell message as you have the yell setting disabled!"));
            return;
        }

        if (player.punishment.isMuted()) {
            player.message("You are muted and can not yell!");
            return;
        }

        if (player.punishment.isJailed()) {
            player.message("You are jailed and can not yell!");
            return;
        }


        if (Arrays.stream(INVALID).anyMatch(message::contains)) {
            player.send(new SendMessage("Your message contains invalid characters."));
            return;
        }
        final String prefix = "[<col=" + player.right.getColor() + ">" + player.right.getName() + "</col>] <col=" + player.right.getColor() + ">" + player.getName();
        final String formatted_message = prefix + "</col>: " + Utility.capitalizeSentence(message);
        World.sendMessage(formatted_message, exception -> exception.settings.yell);
        System.out.println("Yell" +formatted_message);
    }
}
