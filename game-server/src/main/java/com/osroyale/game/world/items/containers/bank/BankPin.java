package com.osroyale.game.world.items.containers.bank;

import com.osroyale.content.dialogue.DialogueFactory;
import com.osroyale.game.world.entity.mob.player.Player;
import com.osroyale.net.packet.out.SendInputMessage;
import com.osroyale.net.packet.out.SendMessage;
import com.osroyale.util.MessageColor;

/**
 * Handles the bank pin system.
 *
 * @author Daniel
 * @since 13-1-2017.
 */
public class BankPin {

    /** The player instance. */
    private final Player player;

    /** The player's bank pin. */
    public String pin;

    /** If the player has unlocked the bank for this session. */
    public boolean entered = false;

    /** Constructs a new <code>BankPin</code>. */
    public BankPin(Player player) {
        this.player = player;
    }

    /** Handles opening the bank pin dialogue. */
    public void open() {
        DialogueFactory factory = player.dialogueFactory;
        factory.sendOption("Set Bank Pin", () -> set(factory), "Remove Bank Pin", () -> remove(factory), "Nevermind", factory::clear);
        factory.execute();
    }

    private void set(DialogueFactory factory) {
        factory.sendStatement("Enter a 4-5 digit pin for your bank.", "Make sure to write it down!").onAction(() -> {
            player.send(new SendInputMessage("Enter the pin you would like to assign your bank:", 5, input -> {
                if (input.length() < 4 || input.length() > 5) {
                    player.send(new SendMessage("Your pin must have 4-5 digits.", MessageColor.RED));
                    return;
                }

                pin = input;
                entered = true;
                player.send(new SendMessage("Your new bank pin is now: " + pin + ". Write it down!", MessageColor.BLUE));
                factory.clear();
            }));
        }).execute();
    }

    private void remove(DialogueFactory factory) {
        factory.onAction(() -> {
            if (!hasPin()) {
                player.send(new SendMessage("You don't have a pin set!"));
                factory.clear();
                return;
            }
            player.send(new SendInputMessage("Enter your bank pin:", 5, input -> {
                if (!pin.equalsIgnoreCase(input)) {
                    player.send(new SendMessage("You have entered an invalid bank pin!", MessageColor.RED));
                    factory.clear();
                    return;
                }

                pin = null;
                player.send(new SendMessage("You have successfully removed your bank pin.", MessageColor.BLUE));
                factory.clear();
            }));
        });

    }

    /** Handles entering the bank pin. */
    public void enter() {
        player.send(new SendInputMessage("Enter your bank pin:", 5, input -> {

            if (!pin.equalsIgnoreCase(input)) {
                player.dialogueFactory.sendStatement("You have entered an invalid bank pin!").execute();
                return;
            }

            entered = true;
            player.send(new SendMessage("You have successfully entered your bank pin. You can access your bank for this session.", MessageColor.BLUE));
            player.bank.open();
        }));
    }

    /** Checks if player has a bank pin. */
    public boolean hasPin() {
        return pin != null;
    }
}
