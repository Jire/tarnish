package com.osroyale.game.action.impl;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.osroyale.game.task.Task;
import com.osroyale.net.packet.out.SendInputMessage;
import com.osroyale.net.packet.out.SendMessage;
import com.osroyale.game.world.entity.mob.player.Player;
import com.osroyale.util.MessageColor;

import java.io.FileWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * This is a debug action. Feel free to modify this how you want to test
 * whatever.
 *
 * @author Daniel
 */
public class DebugAction extends Task {

    private Map<Integer, String> DATA = new HashMap<>();

    private final Player player;

    private int identification;

    private int tick;

    private boolean pause;

    private boolean inputTaken;

    public DebugAction(Player player) {
        super(true, 1);
        this.player = player;
        this.tick = 0;
        this.identification = 0;
        this.inputTaken = false;
        this.pause = false;
        this.DATA = new HashMap<>();
    }

    private final void save() {
        Gson gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
        try (FileWriter fw = new FileWriter("./data/INTERFACES.json")) {
            fw.write(gson.toJson(DATA));
        } catch (final Exception e) {
            e.printStackTrace();
        }
    }

    private void input(Player player) {
        player.send(new SendInputMessage("Enter the itemcontainer description", 100, input -> {
            DATA.put(identification, input);
            inputTaken = false;
            pause = false;
        }));
    }


    @Override
    public void execute() {
        if (inputTaken) {
            if (tick++ == 1) {
                input(player);
            }
            return;
        }

        if (pause) {
            player.dialogueFactory.sendOption(
            "Empty",
            () -> pause = false,

            "Enter", () -> {
                player.dialogueFactory.clear();
                tick = 0;
                inputTaken = true;
            },
            "Save & End",
            () -> {
                save();
                cancel();
                player.send(new SendMessage("A total of " + DATA.size() + " interfaces were saved."));
            }).execute();
            return;
        }

        identification++;
        player.interfaceManager.open(identification);
        player.send(new SendMessage("Opening itemcontainer: " + identification, MessageColor.DARK_RED));
        pause = true;
    }

}