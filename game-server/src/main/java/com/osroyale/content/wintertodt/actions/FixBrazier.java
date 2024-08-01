package com.osroyale.content.wintertodt.actions;

import com.osroyale.content.wintertodt.Brazier;
import com.osroyale.content.wintertodt.Wintertodt;
import com.osroyale.game.action.Action;
import com.osroyale.game.action.policy.WalkablePolicy;
import com.osroyale.game.world.entity.mob.player.Player;
import com.osroyale.game.world.entity.skill.Skill;

public class FixBrazier extends Action<Player> {

    private Brazier brazier;

    public FixBrazier(Player player, Brazier brazier) {
        super(player, 3);
        this.brazier = brazier;
    }

    @Override
    public WalkablePolicy getWalkablePolicy() {
        return WalkablePolicy.NON_WALKABLE;
    }

    @Override
    public String getName() {
        return "Fix brazier";
    }

    @Override
    public void execute() {
        if(brazier.getBrazierState() != 2) {
            System.out.println("???????????");
            //brazier.getObject().transform(Wintertodt.EMPTY_BRAZIER_ID);
            brazier.getObject().unregister();
            brazier.setObject(Wintertodt.EMPTY_BRAZIER_ID);
            brazier.getObject().register();
        }

        getMob().skills.addExperience(Skill.CONSTRUCTION, Skill.getLevelForExperience(getMob().skills.get(Skill.CONSTRUCTION).getExperience()) * 4);
        Wintertodt.addPoints(getMob(), 25);
        getMob().animate(65535);
        getMob().action.getCurrentAction().cancel();
    }
}
