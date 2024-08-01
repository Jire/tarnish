package com.osroyale.content.wintertodt.actions;

import com.osroyale.Config;
import com.osroyale.content.wintertodt.Wintertodt;
import com.osroyale.game.action.Action;
import com.osroyale.game.action.policy.WalkablePolicy;
import com.osroyale.game.world.entity.mob.player.Player;
import com.osroyale.game.world.entity.skill.Skill;
import com.osroyale.game.world.items.Item;

public class MixHerb extends Action<Player> {

    int tick, amount;

    public MixHerb(Player player, int amount) {
        super(player, 1);
        this.amount = amount;
    }

    @Override
    public WalkablePolicy getWalkablePolicy() {
        return WalkablePolicy.NON_WALKABLE;
    }

    @Override
    public String getName() {
        return "Mix herb";
    }

    @Override
    public void execute() {
        if(!getMob().inventory.contains(Wintertodt.BRUMA_HERB) || !getMob().inventory.contains(Wintertodt.REJUV_POT_UNF)) {
            getMob().animate(65535);
            getMob().action.getCurrentAction().cancel();
            return;
        }

        if(tick % 2 == 0) {

            getMob().message("You combine the bruma herb into the unfinished potion.");
            getMob().animate(363);
            getMob().inventory.remove(new Item(Wintertodt.BRUMA_HERB));
            getMob().inventory.remove(new Item(Wintertodt.REJUV_POT_UNF));
            getMob().inventory.add(new Item(Wintertodt.REJUV_POT_4));
            getMob().inventory.refresh();
            double xp = Skill.getLevelForExperience(getMob().skills.get(Skill.HERBLORE).getExperience()) * 0.1;
            if(xp > 0) getMob().skills.addExperience(Skill.HERBLORE, xp * Config.HERBLORE_MODIFICATION);

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
