package com.osroyale.content.simulator;

import com.osroyale.game.world.entity.mob.npc.definition.NpcDefinition;
import com.osroyale.game.world.entity.mob.npc.drop.NpcDropManager;
import com.osroyale.game.world.entity.mob.player.Player;
import com.osroyale.net.packet.out.SendScrollbar;
import com.osroyale.net.packet.out.SendString;
import com.osroyale.net.packet.out.SendTooltip;
import com.osroyale.util.Utility;

import java.util.ArrayList;
import java.util.List;

import static com.osroyale.content.simulator.Simulation.BLOOD_MONEY_CHEST;

/**
 * This class simulates drops of an npc and places it on an itemcontainer.
 *
 * @author Daniel.
 */
public class DropSimulator {

    /** The default NPCs that will have their drops simulated. */
    private static final int[] DEFAULT = {3080};

    public static final Simulation[] CUSTOM_SIMULATIONS = { };

    /** Handles opening the drop simulator itemcontainer. */
    public static void open(Player player) {
        int npc = Utility.randomElement(DEFAULT);
        String name = NpcDefinition.get(npc).getName();
        drawList(player, name);
        simulate(player, Simulation.NPC_DROP, npc, 100);
        player.send(new SendString(name, 26810, true));
        player.interfaceManager.open(26800);
    }

    /** Handles drawing the lsit of npcs based off the search context. */
    public static void drawList(Player player, String context) {
        for (int index = 0, string = 26851; index < CUSTOM_SIMULATIONS.length; index++, string++) {
            Simulation simuation = CUSTOM_SIMULATIONS[index];
            String name = Utility.formatEnum(simuation.name());

            player.send(new SendTooltip("Open simulator for " + name, string));
            player.send(new SendString(name, string));
        }

        List<String> npc = new ArrayList<>();
        List<Integer> button = new ArrayList<>();
        for (NpcDefinition definition : NpcDefinition.DEFINITIONS) {
            if (npc.size() >= 50)
                break;
            if (definition == null)
                continue;
            if (!NpcDropManager.NPC_DROPS.containsKey(definition.getId())) {
                continue;
            }
            if (!definition.getName().toLowerCase().contains(context.toLowerCase())) {
                continue;
            }
            if (npc.contains(definition.getName()))
                continue;
            npc.add(definition.getName());
            button.add(definition.getId());
        }
        int size = npc.size() < 14 ? 14 : npc.size();
        for (int index = 0, string = 26851 + CUSTOM_SIMULATIONS.length; index < size; index++, string++) {
            String name = index >= npc.size() ? "" : npc.get(index);
            player.send(new SendTooltip(name.isEmpty() ? "" : "Open drop simulator for " + name, string));
            player.send(new SendString(name, string));
        }
        player.attributes.set("DROP_SIMULATOR_BUTTON_KEY", button);
        player.send(new SendScrollbar(26850, size * 15));
    }

    /** Handles displaying the simulated drops. */
    public static void simulate(Player player, Simulation simulation, int id, int amount) {
        simulation.execute(player, id, amount);
    }
}
