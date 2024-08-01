package com.osroyale.content;


import com.osroyale.Config;
import com.osroyale.game.task.impl.DoubleExperienceEvent;
import com.osroyale.game.world.World;
import com.osroyale.game.world.entity.mob.player.Player;
import com.osroyale.game.world.entity.mob.player.PlayerRight;
import com.osroyale.game.world.items.Item;
import com.osroyale.net.packet.out.SendMessage;
import com.osroyale.net.packet.out.SendProgressBar;
import com.osroyale.net.packet.out.SendString;
import com.osroyale.util.Utility;

import java.util.Iterator;
import java.util.Objects;
import java.util.TreeSet;

/**
 * Handles contribution towards the well of goodwill.
 *
 * @author Daniel.
 */
public class WellOfGoodwill {

    /** The current contribution amount for the well. */
    public static int CONTRIBUTION;

    public static int activeTime = -1;
    public static boolean active;


    /** The array list of contributors. */
    public static TreeSet<Contributor> contributors = new TreeSet<>();

    /** The last contributor to the well. */
    public static String lastContributor;


    /** The maximum contribution limit for the well. */
    public static final int MAXIMUM = 15_000_000;

    /** Opens the well itemcontainer. */
    public static void open(Player player) {
        int progress = (int) Utility.getPercentageAmount(CONTRIBUTION, MAXIMUM);
        int id = 43708;
        int count = 0;

        Iterator<Contributor> iterator = contributors.iterator();
        while (count++ < 3 && iterator.hasNext()) {
            Contributor contributor = iterator.next();
            String rank = contributor.rank.getCrownText();
            player.send(new SendString("Latest contributor:", 43711));
            player.send(new SendString(rank + "<col=ffb83f>" + contributor.name + "</col> (" + Utility.formatDigits(contributor.contribution) + ")", id++));
        }

        for (int index = count; index < 3; index++) {
            player.send(new SendString("", id + index));
        }

        if (isActive())
            player.send(new SendMessage("The well will be active for " + (30 - activeTime) + " minutes."));

        player.send(new SendProgressBar(43706, progress));
        player.send(new SendString(lastContributor == null ? "" : lastContributor, 43712));
        player.send(new SendString(progress + "%", 43717));
        player.interfaceManager.open(43700);
    }

    /** Handles contributing to the well. */
    public static void contribute(Player player, int amount) {
     /*   if (PlayerRight.isAdministrator(player)) {
            player.message("Fuck off pleb");
            return;
        }
*/
        if (Config.DOUBLE_EXPERIENCE) {
            player.message("Double experience is currently active which has temporarily disabled the wog.");
            return;
        }

        if (amount < 100_000) {
            player.message("You must contribute at least 100k into the well.");
            return;
        }

        if (isActive()) {
            player.message("The well is currently active and does not need", "any contribution.");
            return;
        }

        if (amount > MAXIMUM - CONTRIBUTION) {
            amount = MAXIMUM - CONTRIBUTION;
        }

        if (!player.inventory.contains(new Item(995, amount))) {
            player.message("You do not have the required funds to contribute!");
            return;
        }

        player.inventory.remove(new Item(995, amount));
        CONTRIBUTION += amount;

        add(player.getName(), player.right, amount);
        lastContributor = PlayerRight.getCrown(player) + " <col=ffb83f>" + player.getName() + "</col> (" + Utility.formatDigits(amount) + ")";

        player.message("Thank you! Your contribution of " + Utility.formatDigits(amount) + "gp to the well is appreciated!");

        if (amount >= 250000) {
            World.sendMessage("<col=2b58a0>WOG</col>: " + PlayerRight.getCrown(player) + "<col=2b58a0>" + player.getName() + "</col> has just contributed <col=2b58a0>" + Utility.formatDigits(amount) + "</col> coins.");
        }

        if (CONTRIBUTION >= MAXIMUM) activate();
        open(player);
    }

    /** Handles activating the well. */
    public static void activate() {
        activeTime = 0;
        World.schedule(new DoubleExperienceEvent());
        World.sendMessage("<col=2b58a0>WOG</col>: The well has been fully replenished. 30 minutes of double XP is active.");
        active = true;
    }

    public static void add(String name, PlayerRight rank, int amount) {
        Contributor contributor = new Contributor(name, rank, amount);
        for (Iterator<Contributor> iterator = contributors.iterator(); iterator.hasNext(); ) {
            Contributor other = iterator.next();
            if (contributor.name.equals(other.name)) {
                contributor.add(other.contribution);
                iterator.remove();
                break;
            }
        }
        contributors.add(contributor);
    }

    public static boolean isActive() {
        return activeTime != -1;
    }
    public static String isActivated() {
        return "Active";
    }

    public static class Contributor implements Comparable<Contributor> {
        public final String name;
        public final PlayerRight rank;
        public int contribution;

        Contributor(String name, PlayerRight rank, int contribution) {
            this.name = name;
            this.rank = rank;
            this.contribution = contribution;
        }

        public void add(int amount) {
            contribution += amount;
        }

        @Override
        public int compareTo(Contributor other) {
            return other.contribution - contribution;
        }

        @Override
        public int hashCode() {
            return Objects.hash(name, contribution);
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == this) return true;
            if (obj instanceof Contributor) {
                Contributor other = (Contributor) obj;
                return name.equals(other.name) && contribution == other.contribution;
            }
            return false;
        }

        @Override
        public String toString() {
            return name + " -- " + contribution;
        }
    }
    /**
     * The information displayed on information tab.
     */
    public static String getInformation() {
        return !active ? "Not Active" : isActivated();
    }

}
