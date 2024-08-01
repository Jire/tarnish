package com.osroyale.content.gambling.impl;

import com.osroyale.content.gambling.Gamble;
import com.osroyale.game.task.Task;
import com.osroyale.game.world.World;
import com.osroyale.game.world.entity.mob.player.Player;
import com.osroyale.util.Utility;

public class FiftyFive extends Gamble {

    public FiftyFive(Player host, Player opponent) {
        super(host, opponent);
    }

    @Override
    public String toString() {
        return "55x2";
    }

    @Override
    public void gamble() {
        World.schedule(new Task(1) {
            int time = 0;
            public int randomRoll = 1 + Utility.random(99);
            int hostScore, opponentScore;

            @Override
            public void execute() {
                switch (time) {
                    case 2:
                        getHost().speak("I rolled " + randomRoll + " on the dice.");
                        getOpponent().speak("The host rolled " + randomRoll + " on the dice.");
                        break;
                    case 4:
                        if (randomRoll > 55) {
                            hostScore = 0;
                            opponentScore = 1;
                        } else if (randomRoll < 55) {
                            hostScore = 1;
                            opponentScore = 0;
                        } else {
                            hostScore = 0;
                            opponentScore = 0;
                        }
                        getHost().getGambling().finish(getHost(), getOpponent(), hostScore, opponentScore);
                        cancel();
                        break;
                }
                time++;
            }
        });
    }

}
