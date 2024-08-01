package com.osroyale.content.skill.impl.crafting.impl;

import com.osroyale.Config;
import com.osroyale.net.packet.out.SendInputAmount;
import com.osroyale.net.packet.out.SendItemOnInterfaceZoom;
import com.osroyale.net.packet.out.SendMessage;
import com.osroyale.net.packet.out.SendString;
import com.osroyale.game.world.entity.mob.player.Player;
import com.osroyale.game.world.items.Item;
import com.osroyale.game.world.items.ItemDefinition;
import com.osroyale.util.Utility;

/**
 * Handles tanning leathers.
 *
 * @author Daniel
 */
public class Tanning {

    /**
     * The tan data.
     */
    public enum TanData {
        LEATHER(1739, 1741, 1),
        HARD_LEATHER(1739, 1743, 3),
        SNAKE_HIDE(6287, 6287, 15),
        SNAKESKIN(6287, 6289, 20),
        GREEN_LEATHER(1753, 1745, 20),
        BLUE_LEATHER(1751, 2505, 20),
        RED_LEATHER(1749, 2507, 20),
        BLACK_LEATHER(1747, 2509, 20);

        /**
         * The ingredient item.
         */
        public final int ingredient;

        /**
         * The product item
         */
        public final int product;

        /**
         * The tan cost.
         */
        public final int cost;

        /**
         * Constructs a new <code>TanData</code>.
         *
         * @param ingredient The ingredient item.
         * @param product    The product item.
         * @param cost       The tan cost.
         */
        TanData(int ingredient, int product, int cost) {
            this.ingredient = ingredient;
            this.product = product;
            this.cost = cost;
        }
    }

    /**
     * Handles opening the tanning itemcontainer.
     *
     * @param player The player instance.
     */
    public static void open(Player player) {
        int count = 0;
        for (TanData data : TanData.values()) {
            player.send(new SendItemOnInterfaceZoom(14769 + count, 250, data.ingredient));
            player.send(new SendString((player.inventory.contains(data.ingredient) ? "<col=23db44>" : "<col=e0061c>") + Utility.formatEnum(data.name()), 14777 + count));
            player.send(new SendString((player.inventory.contains(new Item(Config.CURRENCY, data.cost)) ? "<col=23db44>" : "<col=e0061c>") + data.cost + " coins", 14785 + count));
            count++;
        }

        player.interfaceManager.open(14670);
    }

    /**
     * Tans the leather.
     *
     * @param player The player instance.
     * @param amount The amount being tanned.
     * @param data   The tan data.
     */
    public static void tan(Player player, int amount, TanData data) {
        if (!player.inventory.contains(data.ingredient)) {
            player.send(new SendMessage("You do not have any " + ItemDefinition.get(data.ingredient).getName() + " to do this."));
            return;
        }

        int contain = player.inventory.computeAmountForId(data.ingredient);

        if (amount > contain)
            amount = contain;

        int cost = data.cost * amount;

        if (!player.inventory.contains(new Item(Config.CURRENCY, cost))) {
            player.send(new SendMessage("You need " + cost + " coins to tan " + amount + " " + ItemDefinition.get(data.ingredient).getName() + "!"));
            return;
        }

        player.inventory.remove(Config.CURRENCY, cost);
        player.inventory.remove(data.ingredient, amount);
        player.inventory.add(data.product, amount);
        player.send(new SendMessage("You successfully tan all the " + ItemDefinition.get(data.ingredient).getName() + " for " + amount + " coins.", true));
    }

    /**
     * Handles clicking the tan buttons on the itemcontainer.
     *
     * @param player The player instance.
     * @param button The button identification.
     * @return If a button was clicked.
     */
    public static boolean click(Player player, int button) {
        switch (button) {
            /** Leather */
            case 14817:
                tan(player, 1, TanData.LEATHER);
                return true;
            case 14809:
                tan(player, 5, TanData.LEATHER);
                return true;
            case 14801:
                player.send(new SendInputAmount("How many leathers would you like to tan?", 2, input -> {
                    tan(player, Integer.parseInt(input), TanData.LEATHER);
                }));
                return true;
            case 14793:
                tan(player, 28, TanData.LEATHER);
                return true;

            /** Hard leather */
            case 14818:
                tan(player, 1, TanData.HARD_LEATHER);
                return true;
            case 14810:
                tan(player, 5, TanData.HARD_LEATHER);
                return true;
            case 14802:
                player.send(new SendInputAmount("How many leathers would you like to tan?", 2, input -> {
                    tan(player, Integer.parseInt(input), TanData.HARD_LEATHER);
                }));
                return true;
            case 14794:
                tan(player, 28, TanData.HARD_LEATHER);
                return true;

            /** Snake hide */
            case 14819:
                tan(player, 1, TanData.SNAKE_HIDE);
                return true;
            case 14811:
                tan(player, 5, TanData.SNAKE_HIDE);
                return true;
            case 14803:
                player.send(new SendInputAmount("How many leathers would you like to tan?", 2, input -> {
                    tan(player, Integer.parseInt(input), TanData.SNAKE_HIDE);
                }));
                return true;
            case 14795:
                tan(player, 28, TanData.SNAKE_HIDE);
                return true;

            /** Snakeskin */
            case 14820:
                tan(player, 1, TanData.SNAKESKIN);
                return true;
            case 14812:
                tan(player, 5, TanData.SNAKESKIN);
                return true;
            case 14804:
                player.send(new SendInputAmount("How many leathers would you like to tan?", 2, input -> {
                    tan(player, Integer.parseInt(input), TanData.SNAKESKIN);
                }));
                return true;
            case 14796:
                tan(player, 28, TanData.SNAKESKIN);
                return true;

            /** Green leather */
            case 14821:
                tan(player, 1, TanData.GREEN_LEATHER);
                return true;
            case 14813:
                tan(player, 5, TanData.GREEN_LEATHER);
                return true;
            case 14805:
                player.send(new SendInputAmount("How many leathers would you like to tan?", 2, input -> {
                    tan(player, Integer.parseInt(input), TanData.GREEN_LEATHER);
                }));
                return true;
            case 14797:
                tan(player, 28, TanData.GREEN_LEATHER);
                return true;

            /** Blue leather */
            case 14822:
                tan(player, 1, TanData.BLUE_LEATHER);
                return true;
            case 14814:
                tan(player, 5, TanData.BLUE_LEATHER);
                return true;
            case 14806:
                player.send(new SendInputAmount("How many leathers would you like to tan?", 2, input -> {
                    tan(player, Integer.parseInt(input), TanData.BLUE_LEATHER);
                }));
                return true;
            case 14798:
                tan(player, 28, TanData.BLUE_LEATHER);
                return true;

            /** Red leather */
            case 14823:
                tan(player, 1, TanData.RED_LEATHER);
                return true;
            case 14815:
                tan(player, 5, TanData.RED_LEATHER);
                return true;
            case 14807:
                player.send(new SendInputAmount("How many leathers would you like to tan?", 2, input -> {
                    tan(player, Integer.parseInt(input), TanData.RED_LEATHER);
                }));
                return true;
            case 14799:
                tan(player, 28, TanData.RED_LEATHER);
                return true;

            /** Black leather */
            case 14824:
                tan(player, 1, TanData.BLACK_LEATHER);
                return true;
            case 14816:
                tan(player, 5, TanData.BLACK_LEATHER);
                return true;
            case 14808:
                player.send(new SendInputAmount("How many leathers would you like to tan?", 2, input -> {
                    tan(player, Integer.parseInt(input), TanData.BLACK_LEATHER);
                }));
                return true;
            case 14800:
                tan(player, 28, TanData.BLACK_LEATHER);
                return true;
        }
        return false;
    }
}
