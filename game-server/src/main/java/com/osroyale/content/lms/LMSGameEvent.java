package com.osroyale.content.lms;

import com.osroyale.content.lms.crate.LMSCrate;
import com.osroyale.game.task.Task;

public class LMSGameEvent extends Task {

    public LMSGameEvent() {
        super(1);
    }

    @Override
    public void execute() {
        if(!LMSGame.gameInProgress) return;

        LMSGame.updateInterface();
        LMSGame.gamePlayers.stream().forEach(player -> LMSGame.processPlayer(player));

        LMSGame.gameTicks++;
        LMSGame.crateTicks++;

        if (LMSGame.crateTicks == 300) {
            LMSGame.crateTicks = 0;
            if(LMSGame.lmsCrate == null)
                LMSGame.lmsCrate = new LMSCrate();
        }

        if(LMSGame.gameTicks == 100)
            LMSGame.setupSafezone();

        if (LMSGame.gameTicks >= 100)
            LMSGame.handleFog();

        if (--LMSGame.startingTicks >= 0) {
            if (LMSGame.startingTicks > 0)
                LMSGame.gamePlayers.stream().forEach(player -> player.speak(LMSGame.startingTicks + ".."));
            else if (LMSGame.startingTicks == 0) {
                LMSGame.gamePlayers.stream().forEach(player -> player.speak("FIGHT!"));
                LMSGame.canAttack = true;
            }
        }
    }

}
