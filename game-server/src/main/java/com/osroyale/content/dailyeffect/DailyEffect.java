package com.osroyale.content.dailyeffect;

import com.osroyale.game.world.entity.mob.player.Player;
import com.osroyale.util.Utility;

public abstract class DailyEffect {

    private int uses;
    private int dayUsed;

    public void use() {
        if (uses == 0) {
            dayUsed = Utility.getCurrentDay();
        }
        uses++;
    }

    public boolean canUse(Player player) {
        if (dayUsed == Utility.getCurrentDay()) {
            if (uses >= maxUses(player)) {
                player.dialogueFactory.sendStatement("You must wait until tomorrow before you can use this option again.").execute();
                return false;
            }
        } else {
            uses = 0;
        }
        return true;
    }

    public int remainingUses(Player player) {
        return (maxUses(player) - uses);
    }

    public abstract int maxUses(Player player);
}
