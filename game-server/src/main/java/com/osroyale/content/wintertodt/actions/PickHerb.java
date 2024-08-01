package com.osroyale.content.wintertodt.actions;

import com.osroyale.Config;
import com.osroyale.content.wintertodt.Wintertodt;
import com.osroyale.game.action.Action;
import com.osroyale.game.action.policy.WalkablePolicy;
import com.osroyale.game.world.entity.mob.player.Player;
import com.osroyale.game.world.entity.skill.Skill;

public class PickHerb extends Action<Player> {

    int tick;

    public PickHerb(Player player) {
        super(player, 3);
    }

    @Override
    public WalkablePolicy getWalkablePolicy() {
        return WalkablePolicy.NON_WALKABLE;
    }

    @Override
    public String getName() {
        return "Pick herb";
    }

    @Override
    public void execute() {
        if(tick % 2 == 0) {
            if(getMob().inventory.getFreeSlots() > 0) {
                getMob().message("You pick a bruma herb.");
                getMob().inventory.add(Wintertodt.BRUMA_HERB, 1);
                getMob().inventory.refresh();
                double xp = Skill.getLevelForExperience(getMob().skills.get(Skill.FARMING).getExperience()) * 0.1;
                if(xp > 0) getMob().skills.addExperience(Skill.FARMING, xp * Config.FARMING_MODIFICATION);
                getMob().animate(2282);
            } else {
                getMob().message("You have no space for that.");
                getMob().action.getCurrentAction().cancel();
            }
        }
        tick++;
    }
}
