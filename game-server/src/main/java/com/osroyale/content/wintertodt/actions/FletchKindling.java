package com.osroyale.content.wintertodt.actions;

import com.osroyale.Config;
import com.osroyale.content.wintertodt.Wintertodt;
import com.osroyale.content.wintertodt.WintertodtAction;
import com.osroyale.game.world.entity.mob.player.Player;
import com.osroyale.game.world.entity.skill.Skill;
import com.osroyale.game.world.items.Item;

public class FletchKindling extends WintertodtAction {

    int tick, amount;

    public FletchKindling(Player player, int amount) {
        super(player);
        this.amount = amount;
    }

    @Override
    public void execute() {

        if(!getMob().inventory.contains(Wintertodt.BRUMA_ROOT)) {
            getMob().animate(65535);
            getMob().action.getCurrentAction().cancel();
            return;
        }

        if(tick % 3 == 0) {
            getMob().animate(1248);

            getMob().inventory.remove(new Item(Wintertodt.BRUMA_ROOT));
            getMob().inventory.add(new Item(Wintertodt.BRUMA_KINDLING));
            getMob().inventory.refresh();
            double xp = Skill.getLevelForExperience(getMob().skills.get(Skill.FLETCHING).getExperience()) * 0.6;
            if(xp > 0) getMob().skills.addExperience(Skill.FLETCHING, xp * Config.FLETCHING_MODIFICATION);

            amount--;
            if(amount <= 0) {
                getMob().animate(65535);
                getMob().action.getCurrentAction().cancel();
                return;
            }
        }
        tick++;
    }

}
