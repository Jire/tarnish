package com.osroyale.content.skill.impl.hunter.birdhouse.action;

import com.osroyale.content.skill.impl.hunter.birdhouse.BirdhouseData;
import com.osroyale.content.skill.impl.hunter.birdhouse.PlayerBirdHouseData;
import com.osroyale.game.action.Action;
import com.osroyale.game.action.policy.WalkablePolicy;
import com.osroyale.game.world.entity.mob.player.Player;
import com.osroyale.game.world.entity.mob.player.persist.PlayerSerializer;
import com.osroyale.game.world.items.Item;
import com.osroyale.game.world.object.CustomGameObject;
import com.osroyale.game.world.object.GameObject;
import com.osroyale.net.packet.out.SendAddObject;

public class PlaceBirdhouse extends Action<Player> {

    private BirdhouseData birdhouseData;
    private GameObject gameObject;
    public PlaceBirdhouse(Player player, BirdhouseData birdhouseData, GameObject gameObject) {
        super(player, 1);
        this.birdhouseData = birdhouseData;
        this.gameObject = gameObject;
    }

    @Override
    public WalkablePolicy getWalkablePolicy() {
        return WalkablePolicy.WALKABLE;
    }

    @Override
    public String getName() {
        return "Place birdhouse";
    }

    @Override
    public void execute() {
        getMob().inventory.remove(new Item(birdhouseData.birdHouseId));
        getMob().inventory.refresh();
        getMob().birdHouseData.add(new PlayerBirdHouseData(birdhouseData, gameObject.getId(), gameObject.getPosition(), gameObject.getDirection().getId(), gameObject.getObjectType().getId()));
        getMob().send(new SendAddObject(new CustomGameObject(birdhouseData.objectData[0], gameObject.getPosition(), gameObject.getDirection(), gameObject.getObjectType())));
        PlayerSerializer.save(getMob());
        getMob().action.getCurrentAction().cancel();
    }

}
