package com.osroyale.content.bot;

import com.osroyale.content.tittle.PlayerTitle;
import com.osroyale.game.world.World;
import com.osroyale.game.world.entity.mob.player.Player;
import com.osroyale.game.world.entity.mob.player.appearance.Appearance;
import com.osroyale.game.world.entity.mob.player.appearance.Gender;
import com.osroyale.game.world.items.Item;
import com.osroyale.util.MutableNumber;
import com.osroyale.util.Utility;

import java.util.*;

/**
 * Holds all the constants used by bot.
 *
 * @author Daniel
 */
public class BotUtility {

    public static Map<Integer, MutableNumber> BOOT_LOOT = new HashMap<>();

    public static void logLoot(Item item) {
        MutableNumber amount = BOOT_LOOT.getOrDefault(item.getId(), new MutableNumber());
        amount.incrementAndGet(item.getAmount());
        BOOT_LOOT.put(item.getId(), amount);
    }

    /** List of all available bot names. */
    public static final String[] BOT_NAMES = {
            "Bang Bot",
            "DJ Headshot",
            "Kill Me",
            "Not A Bot",
            "Botting Betty",
            "Wall E",
            "S O P H I E",
            "C H R O M P S",
            "The Shredder",
            "Humans DIE",
            "Beep Boop",
            "System Fail",
            "Win32",
            "Microbot",
            "I Am Program",
            "Bed Bath Bot",
            "Corrupted Bot",
            "Rouge Bot"
    };

    /** The default bot title. */
    static final PlayerTitle TITLE = PlayerTitle.create("[BOT]", 0xC74C1C);

    /** The default bot appearance. */
    public static final Appearance APPEARANCE = new Appearance(Gender.MALE, Utility.random(0, 8), Utility.random(10, 17), Utility.random(18, 25), Utility.random(26, 31), Utility.random(33, 34), Utility.random(36, 40), Utility.random(42, 43), 7, 8, 9, 5, 0);

    /** Array of all the possible fight start message. */
    public static final String[] GEAR_UP_MESSAGES = {
            "Time to kick ass and take names!",
            "I am the eco cleaner and cleanse I shall!",
            "Time to make my daddy, Daniel proud!"
    };

    /** Array of all the possible fight start message. */
   public static final String[] FIGHT_START_MESSAGES = {
            "Good luck",
            "Gl",
            "Let's see what you got"
    };

    /** Array of all the possible fight end message. */
    public static final String[] FIGHT_END_MESSAGES = {
            "Out gf",
            "I'm out",
            ""
    };

    /** Array of all the possible fight start message. */
    public static final String[] KILLED_MESSAGES = {
            "Too ez",
            "Ty 4 loot bruh",
            "Good fight",
            "Sit",
            "Cya",
            "Cya in edge",
            "Later dink"
    };

    /** Array of all the possible death message. */
    static final String[] DEATH_MESSAGES = {
            "Good game!",
            "Gf",
            "Gg",
            "Gx",
            "Ugh.. Developers needs to make me stronger!"
    };

    /** Generates a random bot named based on the available names. */
    static String nameGenerator() {
        return Utility.randomElement(getAvailableBotNames());
    }

    /** Generates a list of all available names. */
    private static List<String> getAvailableBotNames() {
        List<String> names = new ArrayList<>(BOT_NAMES.length);
        names.addAll(Arrays.asList(BOT_NAMES));
        for (Player bot : World.getPlayers()) {
            if (!bot.isBot) {
                continue;
            }
            for (String nameList : BOT_NAMES) {
                if (bot.getName().equalsIgnoreCase(nameList)) {
                    names.remove(nameList);
                }
            }
        }
        return names;
    }

    /** Generates a random bot type. */
//    static BotClass classGenerator() {
//        return Utility.randomElement(BotClass.values());
//    }
}