package com.osroyale.content.wintertodt.actions;

import com.osroyale.Config;
import com.osroyale.content.skill.impl.woodcutting.AxeData;
import com.osroyale.content.wintertodt.Wintertodt;
import com.osroyale.game.action.Action;
import com.osroyale.game.action.policy.WalkablePolicy;
import com.osroyale.game.world.entity.mob.player.Player;
import com.osroyale.game.world.entity.skill.Skill;
import com.osroyale.game.world.items.Item;

public class ChopRoots extends Action<Player> {

    int tick;
    public ChopRoots(Player player) {
        super(player, 1);
    }

    @Override
    public WalkablePolicy getWalkablePolicy() {
        return WalkablePolicy.NON_WALKABLE;
    }

    @Override
    public String getName() {
        return "Chop roots";
    }

    @Override
    public void execute() {
        AxeData axeData = AxeData.getDefinition(getMob()).orElse(null);

        if (axeData == null) {
            getMob().message("You need an axe to chop this tree.");
            getMob().animate(65535);
            getMob().action.getCurrentAction().cancel();
            return;
        }

        if(getMob().inventory.getFreeSlots() <= 0) {
            getMob().message("You have no space for that.");
            getMob().animate(65535);
            getMob().action.getCurrentAction().cancel();
            return;
        }

        getMob().animate(axeData.animation);

        if(tick % 3 == 0) {
            getMob().inventory.add(new Item(Wintertodt.BRUMA_ROOT));
            getMob().inventory.refresh();
            getMob().message("You get a bruma root.");
            double xp = Skill.getLevelForExperience(getMob().skills.get(Skill.WOODCUTTING).getExperience()) * 0.3;
            if(xp > 0) getMob().skills.addExperience(Skill.WOODCUTTING, xp * Config.WOODCUTTING_MODIFICATION);
        }
        tick++;
    }
}
