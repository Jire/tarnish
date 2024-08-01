package com.osroyale.content;

import com.osroyale.game.world.World;
import com.osroyale.game.world.entity.mob.player.Player;
import com.osroyale.game.world.entity.mob.player.PlayerRight;
import com.osroyale.game.world.items.Item;
import com.osroyale.game.world.items.ItemDefinition;
import com.osroyale.game.world.position.Area;
import com.osroyale.net.packet.out.SendItemOnInterface;
import com.osroyale.net.packet.out.SendMessage;
import com.osroyale.net.packet.out.SendScrollbar;
import com.osroyale.net.packet.out.SendString;
import com.osroyale.util.MessageColor;
import com.osroyale.util.Utility;

import java.util.Arrays;
import java.util.Optional;



/**
 * Created by Daniel on 2017-07-13.
 */
public class RoyaltyProgram {

    /** Holds all the RoyaltyReward data. */
    public enum RoyaltyReward {
	/*	ONE(1, new Item(4587, 1)), // LOBSTER
		THIRTEEN(13, new Item(560, 17500)), // DEATH RUNE
		TWENTY_FIVE(25, new Item(7158, 1)), // DRAGON 2H
		THIRTY_EIGHT(38, new Item(4588, 5)), // DRAGON SCIMITAR
		FIFTY(50, new Item(537, 300)), // DRAGON BONES
		SIXTY_SEVEN(67, new Item(12934, 1500)), // ZULRAH_SCALES
		SEVENTY_FIVE(75, new Item(11936, 500)), // DARK CRAB
		EIGHTY_EIGHT(88, new Item(995, 999_000)), // COINS
		ONE_HUNDRED(100, new Item(6199, 1)); // MYSTERY BOX*/
        ;
        private final int level;
        private final Item reward;

        RoyaltyReward(int level, Item reward) {
            this.level = level;
            this.reward = reward;
        }

        public int getLevel() {
            return level;
        }

        public Item getReward() {
            return reward;
        }

        public static Optional<RoyaltyReward> forLevel(int level) {
            return Arrays.stream(values()).filter(a -> a.level == level).findAny();
        }

        public static Optional<RoyaltyReward> forOrdinal(int ordinal) {
            return Arrays.stream(values()).filter(a -> a.ordinal() == ordinal).findAny();
        }
    }

    /** Handles the royalty program reward. */
    public static void append(Player player) {
        append(player, 1);
    }

    /** Handles the royalty program reward. */
    public static void append(Player player, int increment) {
        player.royalty += increment;
        String suffix = increment > 1 ? "s" : "";

        int royalty = player.royalty;
        boolean levelUp = royalty >= 100;

        if (levelUp) {
            player.royaltyLevel++;
            player.royalty = 0;
            World.sendMessage("<col=9E4629> RP: " + PlayerRight.getCrown(player) + "<col=9E4629>" + player.getName()
                    + "</col> has just reached prestige <col=9E4629>" + player.royaltyLevel + "</col>.");
        }

        player.send(new SendMessage("You have just received <col=9E4629>" + increment + "</col> royalty point"
                + (suffix) + ". You now have <col=9E4629>" + player.royalty + "</col> point"
                + (player.royalty > 1 ? "s" : "") + " and are level <col=9E4629>" + player.royaltyLevel + "</col>."));

        if (!RoyaltyReward.forLevel(royalty).isPresent())
            return;

        RoyaltyReward reward = RoyaltyReward.forLevel(royalty).get();
        int item = reward.getReward().getId();
        int amount = reward.getReward().getAmount() * player.royaltyLevel;
        String name = ItemDefinition.get(item).getName();
        player.inventory.addOrDrop(new Item(item, amount));
        player.send(
                new SendMessage("You have been rewarded with " + amount + " " + name + ".", MessageColor.DARK_GREEN));
    }

    /** Opens the Royalty Reward Program itemcontainer. */
    public static void open(Player player) {

        if (Area.inWilderness(player)) {
            player.dialogueFactory.sendStatement("You can not open a Royalty Program while in the wilderness!")
                    .execute();
            return;
        }

        if (player.getCombat().inCombat()) {
            player.dialogueFactory.sendStatement("You can not open a Royalty Program while in combat!").execute();
            return;
        }

        if (player.playerAssistant.busy()) {
            player.dialogueFactory.sendStatement("You can not open a Royalty Program right now!").execute();
            return;

        }

        else {

            int size = RoyaltyReward.values().length;
           // Item[] items = new Item[size + 3];

            for (int index = 0, string = 37116; index < size; index++) {
                RoyaltyReward perk = RoyaltyReward.forOrdinal(index).get();
                Item item = perk.getReward();
                int amount = player.royaltyLevel == 0 ? perk.getReward().getAmount()
                        : perk.getReward().getAmount() * player.royaltyLevel;

           //     items[index + 3] = new Item(item.getId(), amount);
                player.send(new SendString("<col=3c50b2>Level: " + perk.getLevel(), string++));
                player.send(new SendString("<col=347043>" + Utility.formatDigits(amount) + "x " + item.getName(),
                        string++));
            }

            player.send(new SendString("The Program rewards players for their activity in game. Every 30", 37111));
            player.send(
                    new SendString("minutes everyone is awarded 1 point. Points accumulate until 100 where", 37112));
            player.send(
                    new SendString("you gain a prestige level, points will be reset and you will be eligible", 37113));
            player.send(
                    new SendString("for the rewards again. Reward amounts are multiplied by prestige level.", 37114));
            player.send(new SendString("", 37115));
            player.send(new SendString("RP: " + player.royalty + "\\nLevel:" + player.royaltyLevel, 37107));
            player.send(new SendString("Royalty Program", 37103));
            player.send(new SendScrollbar(37110, size * 50));
          //  player.send(new SendItemOnInterface(37199, items));
            player.interfaceManager.open(37100);
        }
    }
}
