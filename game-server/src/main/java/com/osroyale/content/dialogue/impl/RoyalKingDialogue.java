package com.osroyale.content.dialogue.impl;

import com.osroyale.content.dialogue.Dialogue;
import com.osroyale.content.dialogue.DialogueFactory;
import com.osroyale.content.dialogue.Expression;
import com.osroyale.content.store.Store;
import com.osroyale.game.world.entity.mob.player.Player;
import com.osroyale.net.packet.out.SendMessage;
import com.osroyale.net.packet.out.SendURL;

/**
 * The royal king dialogue.
 *
 * @author Daniel
 */
public class RoyalKingDialogue extends Dialogue {

    private int index;

    public RoyalKingDialogue(int index) {
        this.index = index;
    }

    @Override
    public void sendDialogues(DialogueFactory factory) {
        if (index == 1) {
            claim(factory);
            factory.execute();
            return;
        }
        if (index == 2) {
            store(factory);
            factory.execute();
            return;
        }
        Player player = factory.getPlayer();
        factory.sendNpcChat(5523, Expression.HAPPY, "Hello adventurer, how may I help you?");
        factory.sendOption("Claim Purchase", () -> claim(factory), "Donator Information", () -> player.send(new SendURL("https://tarnishps.everythingrs.com/services/store")), "My donation statistics", () -> myStats(factory), "Open Store", () -> store(factory), "Nevermind", factory::clear);
        factory.execute();
    }

    private void myStats(DialogueFactory factory) {
        factory.sendStatement("Money spent: $" + factory.getPlayer().donation.getSpent(), "Current credits: " + factory.getPlayer().donation.getCredits());
    }


    private void claim(DialogueFactory factory) {
        Player player = factory.getPlayer();
        player.send(new SendMessage("Claiming is temporarily disabled until EverythingRS is replaced."));
        /*new Thread(() -> {
            try {
                Donation[] donations = Donation.donations(
                        "V48tgd7OxwrvPMmZZbjkVt6qTYB2MrZ5sw6PD4DBJnM83zepnUlIT8DiFoND3BYEQjqxPamH", player.getName());
                if (donations.length == 0) {
                    player.send(new SendMessage("You currently don't have any items waiting. You must donate first!"));
                    return;
                }
                if (donations[0].message != null) {
                    player.send(new SendMessage(donations[0].message));
                    return;
                }
                for (Donation donate : donations) {
                    player.inventory.add(new Item(donate.product_id, donate.product_amount));
                }
                player.send(new SendMessage("Thank you for donating!"));
            } catch (Exception e) {
                player.send(new SendMessage("Api Services are currently offline. Please check back shortly"));
                e.printStackTrace();
            }
        }).start();*/
    }


    private void store(DialogueFactory factory) {
        factory.sendOption("Open Donator Store", () -> Store.STORES.get("Donator Store").open(factory.getPlayer()), "Ironman Donator Store", () -> Store.STORES.get("Ironman Donator Store").open(factory.getPlayer()),"Nevermind", factory::clear);
    }
}
