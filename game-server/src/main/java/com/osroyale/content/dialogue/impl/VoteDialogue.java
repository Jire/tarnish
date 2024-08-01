package com.osroyale.content.dialogue.impl;

import com.osroyale.content.dialogue.Dialogue;
import com.osroyale.content.dialogue.DialogueFactory;
import com.osroyale.content.dialogue.Expression;
import com.osroyale.content.store.Store;
import com.osroyale.game.world.World;
import com.osroyale.game.world.entity.mob.player.Player;
import com.osroyale.net.packet.out.SendInputAmount;
import com.osroyale.net.packet.out.SendURL;
import com.osroyale.util.Utility;

/**
 * Handles the vote dialogue.
 *
 * @author Daniel
 */
public class VoteDialogue extends Dialogue {

    @Override
    public void sendDialogues(DialogueFactory factory) {
        Player player = factory.getPlayer();
        factory.sendNpcChat(7481, Expression.HAPPY, "Hello " + player.getName() + "!", "What can I do for you today?");

        factory.sendOption("Would you want to know what are some of the benefits of voting are?", () -> {
            factory.sendPlayerChat(Expression.HAPPY, "Yes I would!");
            factory.sendNpcChat(7481, "Voting results in attracting new players!", "In return, you'll receive", "vote points that you can use to buy", "items from the vote store.");
        }, "Exchange vote token", () -> {
            World.schedule(1, () -> player.send(new SendInputAmount("How many vote tokens would you like to exchange?", 10, input -> exchange(factory, Integer.parseInt(input)))));
        }, "Show me your voting store!", () -> {
            Store.STORES.get("Tarnish Vote Store").open(player);
        }, "I would like to vote to support this great server!", () -> {
            player.send(new SendURL("https://tarnishps.everythingrs.com/services/vote"));
            factory.sendNpcChat(7481, "Thank you for voting!");
        }, "Nevermind, I don't want to do anything for this server.", () -> {
            factory.clear();
        });
        factory.execute();
    }

    /** Handles redeeming vote tokens. */
    private void exchange(DialogueFactory factory, int amount) {
        Player player = factory.getPlayer();
        if (amount > 10000) {
            factory.sendNpcChat(7481, Expression.SAD, "Sorry but you can only exchange", " 10,000 tokens at a time!").execute();
            return;
        }

        int tokenAmount = player.inventory.computeAmountForId(7478);

        if (amount > tokenAmount)
            amount = tokenAmount;

        if (amount == 0) {
            factory.sendNpcChat(7481, Expression.SAD, "Sorry but you do not have enough", "vote tokens to do this!").execute();
            return;
        }

        player.votePoints += amount;
        player.inventory.remove(7478, amount);
        factory.sendNpcChat(7481, Expression.SAD, "I've exchanged " + amount + " vote tokens for you!", "You now have " + Utility.formatDigits(player.votePoints) + " vote points!").execute();
    }
}
