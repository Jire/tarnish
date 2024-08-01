package com.osroyale.content.wintertodt.actions;

import com.osroyale.Config;
import com.osroyale.content.wintertodt.Brazier;
import com.osroyale.content.wintertodt.Wintertodt;
import com.osroyale.game.action.Action;
import com.osroyale.game.action.policy.WalkablePolicy;
import com.osroyale.game.world.entity.mob.player.Player;
import com.osroyale.game.world.entity.skill.Skill;

public class LightBrazier extends Action<Player> {

    private Brazier brazier;

    public LightBrazier(Player player, Brazier brazier) {
        super(player, 3);
        this.brazier = brazier;
    }

    @Override
    public WalkablePolicy getWalkablePolicy() {
        return WalkablePolicy.NON_WALKABLE;
    }

    @Override
    public String getName() {
        return "Light brazier";
    }

    @Override
    public void execute() {
        if(brazier.getBrazierState() != 2) {
            //brazier.getObject().transform(Wintertodt.BURNING_BRAZIER_ID);
            brazier.getObject().unregister();
            brazier.setObject(Wintertodt.BURNING_BRAZIER_ID);
            brazier.getObject().register();
        }

        getMob().skills.addExperience(Skill.FIREMAKING, (Skill.getLevelForExperience(getMob().skills.get(Skill.FIREMAKING).getExperience()) * 6) * Config.FIREMAKING_MODIFICATION);
        Wintertodt.addPoints(getMob(), 25);
        getMob().animate(65535);
        getMob().action.getCurrentAction().cancel();
    }
}