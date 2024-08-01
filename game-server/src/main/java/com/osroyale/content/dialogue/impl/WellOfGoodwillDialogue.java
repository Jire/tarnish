package com.osroyale.content.dialogue.impl;

import com.osroyale.content.WellOfGoodwill;
import com.osroyale.content.dialogue.Dialogue;
import com.osroyale.content.dialogue.DialogueFactory;
import com.osroyale.util.Utility;

/**
 * Created by Daniel on 2017-06-02.
 */
public class WellOfGoodwillDialogue extends Dialogue {

    @Override
    public void sendDialogues(DialogueFactory factory) {
        factory.sendStatement("Greetings "+factory.getPlayer().getName()+".", "How may the well be of assistance today?").sendOption("How does this work?", () -> {
            factory.onAction(() -> {
                factory.sendStatement(
                    "Players may all contribute as much gold as they desire",
                    "towards the well. In return, once the well reaches",
                    Utility.formatDigits(WellOfGoodwill.MAXIMUM) + " the entire server will be granted").sendStatement(
                    "double experience for 30minutes.",
                    "After those 30 minutes the well will need to be replenished.",
                    "Well data is saved and stores during server restarts and shutdowns.",
                    "Ensuring safety for contributors."
                ).execute();
            });
        }, "Check time left", () -> {
            if (!WellOfGoodwill.isActive()) {
                factory.sendStatement("").sendStatement("The well is not active!").execute();
                return;
            }
            factory.sendStatement("").sendStatement("Time left: " + (30 - WellOfGoodwill.activeTime) + " minutes").execute();

        }, "Check current status", () -> {
            WellOfGoodwill.open(factory.getPlayer());
        }, "Nevermind", factory::clear).execute();
    }
}
