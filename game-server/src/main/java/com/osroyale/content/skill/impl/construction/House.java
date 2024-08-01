package com.osroyale.content.skill.impl.construction;

import com.osroyale.game.Animation;
import com.osroyale.game.world.World;
import com.osroyale.game.world.entity.mob.player.Player;
import com.osroyale.game.world.entity.mob.player.profile.ProfileRepository;
import com.osroyale.game.world.entity.skill.Skill;
import com.osroyale.game.world.items.Item;
import com.osroyale.game.world.object.CustomGameObject;
import com.osroyale.game.world.object.GameObject;
import com.osroyale.game.world.object.ObjectDirection;
import com.osroyale.game.world.object.ObjectType;
import com.osroyale.game.world.position.Position;
import com.osroyale.net.packet.out.SendMessage;
import com.osroyale.net.packet.out.SendMinimapState;
import com.osroyale.net.packet.out.SendMinimapState.MinimapState;
import com.osroyale.net.packet.out.SendString;
import com.osroyale.util.MessageColor;
import com.osroyale.util.Utility;

import java.util.ArrayList;
import java.util.List;

public class House {

    private Player player;

    private BuildableMap map;

    private List<ConstructionObject> OBJECT = new ArrayList<ConstructionObject>();

    private boolean inside;

    private int height;

    public House(Player player) {
        this.player = player;
        this.height = /* player.getPlayerAssistant().instance() */0;
    }

    public void purchase() {
        if (map != null) {
            player.dialogueFactory.sendStatement("You already have a house purchased!").execute();
            return;
        }

        if (!player.inventory.contains(new Item(995, 1000000))) {
            player.dialogueFactory.sendStatement("You need 1,000,000 royale tokens to purchase a house.").execute();
            return;
        }

        map = BuildableMap.SMALL_CAVE;
        player.inventory.remove(new Item(995, 1000000));
        player.dialogueFactory.sendStatement("You have successfully purchased a house for 1,000,000 royale tokens.", "Your house has been set to the default location.").execute();
    }

    public void location(BuildableMap newMap) {
        if (map == null) {
            player.dialogueFactory.sendStatement("Please purchase a house first.").execute();
            return;
        }

        if (map == newMap) {
            player.dialogueFactory.sendStatement("Your house is already set to this location.").execute();
            return;
        }

        if (player.skills.getLevel(Skill.CONSTRUCTION) < newMap.getLevel()) {
            player.dialogueFactory.sendStatement("You need a construction level of " + newMap.getLevel() + " to build this.").execute();
            return;
        }

        if (!player.inventory.contains(new Item(995, newMap.getCost()))) {
            player.dialogueFactory.sendStatement("You need " + newMap.getCost() + " royale tokens to purchase this map.").execute();
            return;
        }

        map = newMap;
        OBJECT.clear();
        player.inventory.remove(new Item(995, newMap.getCost()));
        player.dialogueFactory.sendStatement("You have successfully purchased the " + newMap.getName() + " map.").execute();
    }

    public void enter() {
        if (map == null) {
            player.dialogueFactory.sendStatement("You need to purchase a house first.", "Speak to the agent to purchase one.").execute();
            return;
        }

        player.send(new SendString("Please wait as your house data is loaded...", 12285));
        player.send(new SendMinimapState(MinimapState.HIDDEN));
        player.interfaceManager.open(12283);
        player.move(new Position(map.getPosition().getX(), map.getPosition().getY(), height));
        map.execute(player);

        for (ConstructionObject aOBJECT : OBJECT) {
            new CustomGameObject(aOBJECT.getObject(), aOBJECT.getPosition()).register();
        }

        World.schedule(2500, () -> {
            player.send(new SendMinimapState(MinimapState.NORMAL));
            player.interfaceManager.close();
            player.send(new SendMessage("Welcome to your house! To start building please click on the world map to enter the", MessageColor.DARK_GREEN));
            player.send(new SendMessage("Construction Builder itemcontainer. To leave your house please click on the portal.", MessageColor.DARK_GREEN));
            player.send(new SendString("[CONSTRUCTION_MAP]", 0));
        });

        inside = true;
    }

    public void enter(String name) {
        if (!ProfileRepository.exist(name)) {
            player.dialogueFactory.sendStatement("According to our data base, this player does not exist.").execute();
            return;
        }

        if (!World.search(name).isPresent()) {
            player.dialogueFactory.sendStatement(Utility.formatName(name) + " is not currently online.").execute();
            return;
        }

        Player other = World.search(name).get();
        House otherHouse = other.house;

        if (otherHouse.getMap() == null) {
            player.dialogueFactory.sendStatement(Utility.formatName(name) + " does not have a house for you to visit.").execute();
            return;
        }

        if (!otherHouse.isInside()) {
            player.dialogueFactory.sendStatement(Utility.formatName(name) + " must be in their house for you to visit.").execute();
        }

        player.send(new SendString("Please wait as your friend's house data is loaded...", 12285));
        player.send(new SendMinimapState(MinimapState.HIDDEN));
        player.interfaceManager.open(12283);
        player.move(new Position(other.house.getMap().getPosition().getX(), otherHouse.getMap().getPosition().getY(), otherHouse.getHeight()));
        otherHouse.getMap().execute(player);

        for (int index = 0; index < otherHouse.getObjects().size(); index++) {
            new CustomGameObject(otherHouse.getObjects().get(index).getObject(), otherHouse.getObjects().get(index).getPosition()).register();
        }

        World.schedule(2500, () -> {
            player.send(new SendMinimapState(MinimapState.NORMAL));
            player.interfaceManager.close();
            player.send(new SendMessage("Welcome to your friend's house.", MessageColor.DARK_GREEN));
        });

        inside = true;
    }

    public void leave() {
        if (inside) {
            inside = false;
            player.move(new Position(3079, 3484, 0));
            player.send(new SendString("[NORMAL_MAP]", 0));
        }
    }

    public boolean toolkit(GameObject object) {
        if (!inside) {
            return false;
        }

        ConstructionObject constructionObject = null;

        for (int index = 0; index < OBJECT.size(); index++) {
            if (OBJECT.get(index).getObject() == object.getId() && OBJECT.get(index).getPosition().equals(object.getPosition())) {
                constructionObject = new ConstructionObject(object.getId(), OBJECT.get(index).getRotation(), object.getPosition());
                break;
            }
        }

        if (constructionObject == null) {
            return false;
        }

        player.dialogueFactory.sendOption("Remove object", () -> {
            // OBJECT.withdraw(constructionObject);
            object.unregister();
        }, "Change rotation", () -> {
            player.dialogueFactory.onAction(() -> {
                player.dialogueFactory.sendStatement("Please select the rotation direction").sendOption("North", () -> {
                    object.unregister();
                    new CustomGameObject(object.getId(), object.getPosition(), ObjectDirection.NORTH, ObjectType.GENERAL_PROP).register();
                }, "East", () -> {
                    object.unregister();
                    new CustomGameObject(object.getId(), object.getPosition(), ObjectDirection.EAST, ObjectType.GENERAL_PROP).register();
                }, "West", () -> {
                    object.unregister();
                    new CustomGameObject(object.getId(), object.getPosition(), ObjectDirection.WEST, ObjectType.GENERAL_PROP).register();
                }, "South", () -> {
                    object.unregister();
                    new CustomGameObject(object.getId(), object.getPosition(), ObjectDirection.SOUTH, ObjectType.GENERAL_PROP).register();
                }, "Nevermind", () -> {
                    player.interfaceManager.close();
                }).execute();

            });
        }, "Nevermind", () -> {
            player.interfaceManager.close();
        }).execute();
        return true;
    }

    public void construct() {
        BuildableObject object = player.attributes.get("CONSTRUCTION_BUILDOBJECT_KEY", BuildableObject.class);

        if (player.skills.getLevel(Skill.CONSTRUCTION) < object.getLevel()) {
            player.send(new SendMessage("You need a construction level of " + object.getLevel() + " to build this."));
            return;
        }

        // if (!player.inventory.contains(object.getItems())) {
        // player.send(new SendMessage("You do not have the required item to
        // build this."));
        // return;
        // }

        player.interfaceManager.close();
        player.animate(new Animation(898));
        player.inventory.removeAll(object.getItems());
        CustomGameObject gameObject = new CustomGameObject(object.getObject(), player.getPosition().copy());

        World.schedule(5, () -> {
            gameObject.register();
            player.skills.addExperience(Skill.CONSTRUCTION, object.getExperience());
            OBJECT.add(new ConstructionObject(gameObject.getId(), 0, gameObject.getPosition()));
            player.send(new SendMessage("You have successfully constructed " + Utility.getAOrAn(gameObject.getName()) + " " + gameObject.getName() + "."));
        });
    }

    public BuildableMap getMap() {
        return map;
    }

    public void setMap(BuildableMap map) {
        this.map = map;
    }

    public List<ConstructionObject> getObjects() {
        return OBJECT;
    }

    public void setObject(List<ConstructionObject> object) {
        OBJECT = object;
    }

    public boolean isInside() {
        return inside;
    }

    public int getHeight() {
        return height;
    }
}
