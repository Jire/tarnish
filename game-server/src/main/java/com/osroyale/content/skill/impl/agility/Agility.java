package com.osroyale.content.skill.impl.agility;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import com.osroyale.Config;
import com.osroyale.content.skill.impl.agility.obstacle.Obstacle;
import com.osroyale.game.world.entity.mob.player.Player;
import com.osroyale.game.world.entity.skill.Skill;
import com.osroyale.game.world.items.Item;
import com.osroyale.game.world.position.Position;

import java.io.*;
import java.util.HashMap;
import java.util.function.Consumer;

/**
 * Created by Daniel on 2017-11-02.
 */
public class Agility {
    public static final int GNOME_FLAGS = 0b0111_1111;
    public static final int BARBARIAN_FLAGS = 0b0111_1111;
    public static final int WILDERNESS_FLAGS = 0b0001_1111;
    public static final int SEERS_FLAGS = 0b0011_1111;
    public static final int ARDOUGNE_FLAGS = 0b0111_1111;
    public static final HashMap<Position, Obstacle> obstacles = new HashMap<>();

    public static void declare() {
        try {
            Obstacle[] loaded = new Gson().fromJson(new BufferedReader(new FileReader("./data/content/skills/agility.json")), Obstacle[].class);
            for (Obstacle obstacle : loaded) {
                if (obstacle.getObjectPosition() == null) {
                    obstacle.setObjectPosition(obstacle.getStart());
                }
                if (obstacle.getType() == null) {
                    System.out.println(obstacle + " No object type found. FIX MUST FIX");
                }
                obstacles.put(obstacle.getObjectPosition(), obstacle);
            }
        } catch (JsonSyntaxException | JsonIOException | FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private final static double TICKET_EXPERIENCE = 50 * Config.AGILITY_MODIFICATION;

    public static boolean clickButton(Player player, int button) {
        int amount = -1;
        Consumer<Player> onClick = null;

        switch (button) {
            case 8387:
                amount = 1;
                onClick = p -> p.skills.addExperience(Skill.AGILITY, TICKET_EXPERIENCE * 1);
                break;
            case 8389:
                amount = 10;
                onClick = p -> p.skills.addExperience(Skill.AGILITY, TICKET_EXPERIENCE * 10);
                break;
            case 8390:
                amount = 25;
                onClick = p -> p.skills.addExperience(Skill.AGILITY, TICKET_EXPERIENCE * 25);
                break;
            case 8391:
                amount = 100;
                onClick = p -> p.skills.addExperience(Skill.AGILITY, TICKET_EXPERIENCE * 100);
                break;
            case 8392:
                amount = 1_000;
                onClick = p -> p.skills.addExperience(Skill.AGILITY, TICKET_EXPERIENCE * 1000);
                break;
            case 8382:
                amount = 3;
                onClick = p -> p.inventory.addOrDrop(new Item(3049, 1));
                break;
            case 8383:
                amount = 10;
                onClick = p -> p.inventory.addOrDrop(new Item(3051, 1));
                break;
            case 8381:
                amount = 800;
                onClick = p -> p.inventory.addOrDrop(new Item(2997, 1));
                break;
        }
        if (amount > -1) {
            if (player.inventory.contains(2996, amount)) {
                player.inventory.remove(2996, amount);
                onClick.accept(player);
                player.interfaceManager.close();
                return true;
            } else {
                player.message("You do not have enough agility tickets to purchase that.");
                return true;
            }
        }
        return false;
    }

    public static void main(String[] args) {
        // Write the obstacles so we don't have to deal with ugly as fuck wall
        // of code.
//		 obstacles.put(new Location(2551, 3554), new Obstacle.ObstacleBuilder(ObstacleType.ROPE_SWING, new ObeliskData(2551, 3554), new ObeliskData(2551, 3549))
//		 .setExperience(22f)
//		 .setLevel(35)
//		 .setOrdinal(0)
//		 .setNext(new Obstacle.ObstacleBuilder(ObstacleType.ROPE_SWING, new Location(2551, 3554), new ObeliskData(2551, 3549)).build())
//		 .build());

        try (FileWriter writer = new FileWriter(new File("./data/def/skills/agility1.json"))) {
            Gson builder = new GsonBuilder().setPrettyPrinting().create();

            writer.write(builder.toJson(obstacles.values()).replaceAll("\\{\n      \"x\"", "\\{ \"x\"").replaceAll(",\n      \"y\"", ", \"y\"").replaceAll(",\n      \"z\"", ", \"z\"").replaceAll("\n    \\},", " \\},"));
        } catch (Exception e) {
        }
    }

}
