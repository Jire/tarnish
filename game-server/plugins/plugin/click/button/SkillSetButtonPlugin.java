package plugin.click.button;

import com.osroyale.content.SkillSet;
import com.osroyale.game.plugin.PluginContext;
import com.osroyale.game.world.entity.mob.UpdateFlag;
import com.osroyale.game.world.entity.mob.player.Player;
import com.osroyale.game.world.entity.mob.player.PlayerRight;
import com.osroyale.game.world.entity.skill.Skill;
import com.osroyale.game.world.position.Area;
import com.osroyale.net.packet.out.SendInputAmount;
import com.osroyale.net.packet.out.SendMessage;

public class SkillSetButtonPlugin extends PluginContext {

    @Override
    protected boolean onClick(Player player, int button) {
        if (!SkillSet.forButton(button).isPresent()) {
            return false;
        }
        if (PlayerRight.isIronman(player)) {
            player.message("You can not set your skill as an iron account.");
            return true;
        }

        if (Area.inWilderness(player)) {
            player.send(new SendMessage("You can not do this whilst in the wilderness."));
            return true;
        }
        if (player.getCombat().inCombat()) {
            player.send(new SendMessage("You can not do this whilst in combat."));
            return true;
        }
        if (!player.equipment.isEmpty()) {
            player.message("Please remove all your equipment before doing this!");
            return true;
        }

        SkillSet data = SkillSet.forButton(button).get();

        player.send(new SendInputAmount("Enter your desired <col=255>" + Skill.getName(data.skill) + "</col> level:", 2, input -> {
            int level = Integer.parseInt(input);

            if (level > 99)
                level = 99;
            if (level < 1)
                level = 1;
            if (data.skill == Skill.HITPOINTS && level < 10)
                level = 10;

            player.skills.setLevel(data.skill, level);
            player.skills.setMaxLevel(data.skill, level);
            player.skills.setCombatLevel();
            player.updateFlags.add(UpdateFlag.APPEARANCE);
            player.dialogueFactory.sendStatement("You have successfully set your <col=255>" + Skill.getName(data.skill) + "</col> level to <col=255>" + level + "</col>.", "Your combat level is now: <col=255>" + player.skills.getCombatLevel() + "</col>.").execute();
        }));
        return true;
    }
}
