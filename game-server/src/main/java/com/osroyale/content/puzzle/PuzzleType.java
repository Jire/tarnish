package com.osroyale.content.puzzle;

import com.osroyale.content.bloodmoney.BloodMoneyChest;
import com.osroyale.game.world.entity.combat.hit.Hit;
import com.osroyale.game.world.entity.mob.player.Player;
import com.osroyale.util.Utility;

/**
 * The difference puzzle types.
 *
 * @author Daniel
 */
public enum PuzzleType implements PuzzleInterface<Player> {
    BLOOD_MONEY {
        @Override
        public void onSuccess(Player player) {
            int count = player.puzzle.successCount;

            if (count >= 5) {
                player.puzzle.successCount = 0;
                BloodMoneyChest.finish(true);
                return;
            }

            player.puzzle.open(BLOOD_MONEY);
            player.message("Good job, " + (5 - count) + " puzzles remaining!");
        }

        @Override
        public void onFailure(Player player) {
            player.speak("Ouch!");
            player.message("You failed to unlock the blood money chest!");
            player.writeDamage(new Hit(Utility.random(5, 10)));
            player.interfaceManager.close();
        }
    }
}
