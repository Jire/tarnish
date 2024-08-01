package com.osroyale.content.wintertodt.actions;

import com.osroyale.Config;
import com.osroyale.content.wintertodt.Brazier;
import com.osroyale.content.wintertodt.Wintertodt;
import com.osroyale.content.wintertodt.WintertodtAction;
import com.osroyale.game.world.entity.mob.player.Player;
import com.osroyale.game.world.entity.skill.Skill;
import com.osroyale.game.world.items.Item;

public class FeedBrazier extends WintertodtAction {

    Brazier brazier;
    int tick;

    public FeedBrazier(Player player, Brazier brazier) {
        super(player);
        this.brazier = brazier;
    }

    @Override
    public void execute() {
        if (brazier.getObject().getId() != Wintertodt.BURNING_BRAZIER_ID) {
            getMob().message("The brazier has gone out.");
            getMob().animate(65535);
            getMob().action.getCurrentAction().cancel();
            return;
        }

        if(!getMob().inventory.contains(Wintertodt.BRUMA_ROOT) && !getMob().inventory.contains(Wintertodt.BRUMA_KINDLING)) {
            getMob().animate(65535);
            getMob().action.getCurrentAction().cancel();
            return;
        }

        if(tick % 2 == 0) {
            getMob().animate(832);
            if (getMob().inventory.remove(new Item(Wintertodt.BRUMA_KINDLING))) {
                double xp = Skill.getLevelForExperience(getMob().skills.get(Skill.FIREMAKING).getExperience()) * 3.8;
                if(xp > 0) getMob().skills.addExperience(Skill.FIREMAKING, xp * Config.FIREMAKING_MODIFICATION);
                Wintertodt.addPoints(getMob(), 25);
            } else {
                getMob().inventory.remove(new Item(Wintertodt.BRUMA_ROOT));
                double xp = Skill.getLevelForExperience(getMob().skills.get(Skill.FIREMAKING).getExperience()) * 3;
                if(xp > 0) getMob().skills.addExperience(Skill.FIREMAKING, xp * Config.FIREMAKING_MODIFICATION);
                Wintertodt.addPoints(getMob(), 10);
            }
            getMob().inventory.refresh();
        }

        tick++;
    }
}
