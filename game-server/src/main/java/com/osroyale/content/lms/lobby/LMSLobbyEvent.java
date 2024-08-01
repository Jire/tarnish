package com.osroyale.content.lms.lobby;

import com.osroyale.content.lms.LMSGame;
import com.osroyale.game.task.Task;

import java.util.Objects;

public class LMSLobbyEvent extends Task {

    public static final int defaultLobbyTime = LMSLobby.DEVELOPMENT_MODE ? 15 : 300;
    public static int lobbyTicks = defaultLobbyTime;

    public LMSLobbyEvent() {
        super(1);
    }

    @Override
    public void execute() {
        if(LMSGame.gameInProgress) return;

        if (lobbyTicks >= 0) {
            if (lobbyTicks == 10) {
                if(LMSLobby.lobbyMembers.size() < LMSLobby.requiredPlayers) {
                    int playersStillRequired = LMSLobby.requiredPlayers - LMSLobby.lobbyMembers.size();
                    LMSLobby.lobbyMembers.stream().filter(Objects::nonNull).forEach(p -> p.message("@red@"+playersStillRequired+" more player"+(playersStillRequired > 1 ? "s are" : " is")+" required to start a game of LMS."));
                } else LMSLobby.lobbyMembers.stream().filter(Objects::nonNull).forEach(p -> p.message("@red@The game will begin in 10 seconds."));
            }
            if (lobbyTicks == 0) {
                if (LMSLobby.lobbyMembers.size() < LMSLobby.requiredPlayers) {
                    lobbyTicks = defaultLobbyTime + 1;
                } else {
                    LMSLobby.lobbyMembers.stream().filter(Objects::nonNull).forEach(p -> p.message("@red@The game will begin shortly."));
                    LMSGame.moveToGame(LMSLobby.lobbyMembers);
                    LMSLobby.lobbyMembers.clear();
                }
            }
            lobbyTicks--;
        }
    }

}
