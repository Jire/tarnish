package com.osroyale.util.parser.old;

import com.google.gson.JsonObject;
import com.osroyale.game.world.items.ItemDefinition;
import com.osroyale.game.world.items.containers.equipment.EquipmentType;
import com.osroyale.util.parser.GsonParser;

/**
 * Parses through the equipment definitions file and creates equipment
 * definition object.
 *
 * @author Daniel | Obey
 */
public class EquipmentDefinitionParser extends GsonParser {

    public EquipmentDefinitionParser() {
        super("def/equipment/equipment_definitions");
    }

    public static void main(String[] args) {
       new EquipmentDefinitionParser().run();
      //  ItemDefinition.merge("wiki/client_item_dump", "wiki/wiki_item_dump", "wiki/new_item_dump");
        ItemDefinition.dump(ItemDefinition.create("wiki/new_item_dump"), "wiki/item_definitions");
    }

    @Override
    public void initialize(int size) {
        ItemDefinition.createParser().run();
    }

    @Override
    protected void parse(JsonObject data) {
        int id = data.get("id").getAsInt();
        ItemDefinition definition = ItemDefinition.get(id);

        if (definition.getEquipmentType() == EquipmentType.NOT_WIELDABLE) {
            String name = definition.getName().toLowerCase();
            if (name.startsWith("hood") || name.endsWith("hood")) {
                definition.setEquipmentType(EquipmentType.FACE);
            } else if (name.contains("full helm")) {
                definition.setEquipmentType(EquipmentType.HELM);
            } else if (name.contains("med helm")) {
                definition.setEquipmentType(EquipmentType.FACE);
            } else if (name.contains("helm")) {
                if (name.startsWith("dharok")) {
                    definition.setEquipmentType(EquipmentType.FACE);
                } else {
                    definition.setEquipmentType(EquipmentType.HELM);
                }
            }
        }

    }

    @Override
    protected void onEnd() {
        ItemDefinition.dump("def/item/newer_item_definitions");
    }

    private static int getReq(String first, String second, EquipmentType type) {
        switch (first) {
            case "bronze":
                if (type == EquipmentType.WEAPON || type == EquipmentType.ARROWS) {
                    return -1;
                }
                return 1;

            case "iron":
                if (type == EquipmentType.WEAPON || type == EquipmentType.ARROWS) {
                    return -1;
                }
                return 1;

            case "steel":
                if (type == EquipmentType.WEAPON || type == EquipmentType.ARROWS) {
                    return -1;
                }
                return 10;

            case "mithril":
                if (type == EquipmentType.WEAPON || type == EquipmentType.ARROWS) {
                    return -1;
                }
                return 20;

            case "adamant":
                if (type == EquipmentType.WEAPON || type == EquipmentType.ARROWS) {
                    return -1;
                }
                return 30;

            case "rune":
                if (type == EquipmentType.WEAPON || type == EquipmentType.ARROWS) {
                    return -1;
                }
                return 40;

            case "dragon":
            case "toktz-ket-xil":
                if (type == EquipmentType.WEAPON || type == EquipmentType.ARROWS) {
                    return -1;
                }
                return 60;

            case "bandos":
                if (second.equals("mitre") || second.equals("robe")) {
                    return -1;
                }

                if (second.startsWith("plate") || second.startsWith("full") || second.startsWith("kite")) {
                    return 40;
                }
                if (type == EquipmentType.WEAPON) {
                    return -1;
                }
                return 65;

            case "armadyl":
                if (second.equals("mitre") || second.equals("robe")) {
                    return -1;
                }

                if (second.startsWith("plate") || second.startsWith("full") || second.startsWith("kite")) {
                    return 40;
                }
                if (type == EquipmentType.WEAPON) {
                    return -1;
                }
                return 70;

            case "dharok's":
            case "guthan's":
            case "torag's":
            case "verac's":
            case "karil's":
            case "ahrim's":
            case "barrows":
                if (type == EquipmentType.WEAPON) {
                    return -1;
                }
                return 70;

            case "dragonfire":
                return 75;

            case "neitiznot":
                return 55;

            case "saradomin":
            case "guthix":
            case "zamorak":
                if (second.equals("mitre") || second.equals("robe")) {
                    return -1;
                }
                if (type == EquipmentType.WEAPON) {
                    return -1;
                }
                return 40;

            case "berserker":
            case "archer":
            case "farseer":
            case "warrior":
                if (type == EquipmentType.RING) {
                    return -1;
                }
                return 45;

            case "green":
            case "red":
            case "blue":
            case "black":
                if (type != EquipmentType.BODY) {
                    return -1;
                }

                return 40;

            case "hardleather":
                if (type != EquipmentType.BODY) {
                    return -1;
                }

                return 10;

            case "void":
                return 42;

            case "snakeskin":
                return 30;

            case "3rd":
                return 45;

            case "penance":
            case "runner":
            case "fighter":
                if (type == EquipmentType.GLOVES) {
                    return -1;
                }
                return 40;
        }
        return -1;
    }

}
