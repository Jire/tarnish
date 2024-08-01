package plugin.click.button;

import com.osroyale.content.GenieLamp;
import com.osroyale.game.plugin.PluginContext;
import com.osroyale.game.world.entity.mob.player.Player;
import com.osroyale.game.world.entity.skill.Skill;
import com.osroyale.game.world.items.Item;
import com.osroyale.net.packet.out.SendMessage;
import com.osroyale.util.Utility;

import java.util.concurrent.TimeUnit;

public class GenieLampButtonPlugin extends PluginContext {

    @Override
    protected boolean onClick(Player player, int button) {
        if (!GenieLamp.forButton(button).isPresent()) {
            return false;
        }

        if (!player.buttonDelay.elapsed(599, TimeUnit.MILLISECONDS)) {
            return true;
        }

        player.buttonDelay.reset();

        if (!player.inventory.contains(2528)) {
            return true;
        }

        if (!player.interfaceManager.isInterfaceOpen(2808)) {
            player.interfaceManager.close();
            return true;
        }

        GenieLamp genie = GenieLamp.forButton(button).get();
        int base = 2500;
        double modified = (base * player.skills.getMaxLevel(genie.getSkill())) / 3.5;
        double experience = modified + Utility.random(500);

        player.dialogueFactory.sendOption("Confirm <col=255>"+Skill.getName(genie.getSkill())+"</col>?",()-> {
            player.interfaceManager.close();
            player.inventory.remove(new Item(2528));
            player.skills.addExperience(genie.getSkill(), experience);
            player.send(new SendMessage("You rub on the lamp... and were given experience in " + Skill.getName(genie.getSkill()) + "."));
        },"Nevermind", () -> player.dialogueFactory.clear());
        player.dialogueFactory.execute();
        return true;
    }
}
