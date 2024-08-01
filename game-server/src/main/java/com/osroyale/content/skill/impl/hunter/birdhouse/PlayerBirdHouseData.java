package com.osroyale.content.skill.impl.hunter.birdhouse;

import com.osroyale.game.world.position.Position;

public class PlayerBirdHouseData {
    public BirdhouseData birdhouseData;

    public int oldObjectId;
    public Position birdhousePosition;
    public int rotation;
    public int type;
    public int seedAmount;
    public long birdhouseTimer;

    public PlayerBirdHouseData(BirdhouseData birdhouseData, int oldObjectId, Position birdhousePosition, int rotation, int type) {
        this.birdhouseData = birdhouseData;
        this.oldObjectId = oldObjectId;
        this.birdhousePosition = birdhousePosition;
        this.rotation = rotation;
        this.type = type;
        seedAmount = 0;
        birdhouseTimer = 0;
    }

}
