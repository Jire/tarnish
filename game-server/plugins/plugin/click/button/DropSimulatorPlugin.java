package plugin.click.button;

import com.osroyale.content.simulator.DropSimulator;
import com.osroyale.content.simulator.Simulation;
import com.osroyale.game.plugin.PluginContext;
import com.osroyale.game.world.entity.mob.player.Player;

import java.util.List;

public class DropSimulatorPlugin extends PluginContext {

    @Override
    protected boolean onClick(Player player, int button) {
//        if (button == 26851) {
//            DropSimulator.simulate(player, Simulation.BLOOD_MONEY_CHEST, 1, 100);
//            return true;
//        }

        if (button >= 26851 + DropSimulator.CUSTOM_SIMULATIONS.length && button <= 26900) {
            int base_button = 26851;
            int modified_button = (base_button - button);
            int index = Math.abs(modified_button);
            List<Integer> npc = player.attributes.get("DROP_SIMULATOR_BUTTON_KEY", List.class);
            if (npc == null)
                return false;
            if (index >= npc.size())
                return false;
            DropSimulator.simulate(player, Simulation.NPC_DROP, npc.get(index), 100);
            return true;
        }
        return false;
    }
}
