package com.osroyale.util.tools.wiki.impl;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.osroyale.game.world.items.containers.equipment.Equipment;
import com.osroyale.game.world.items.containers.equipment.EquipmentType;
import com.osroyale.util.Utility;
import com.osroyale.util.parser.GsonParser;
import com.osroyale.util.tools.wiki.parser.WikiTable;
import com.osroyale.util.tools.wiki.parser.WikiTableParser;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.*;

import static com.osroyale.game.world.entity.combat.CombatConstants.BONUS_CONFIG_FIELD_NAMES;

public class ItemDefParser extends WikiTableParser {

    private static final Logger logger = LogManager.getLogger();

    private ItemDefParser() {
        super(generateTables());
    }

    public static void main(String[] args) throws InterruptedException {
        ItemDefParser parser = new ItemDefParser();
        parser.begin();
    }

    private static LinkedList<WikiTable> generateTables() {
        List<Definition> definitions = parseDefinitions();
        LinkedList<WikiTable> tables = new LinkedList<>();
        String link = "https://oldschoolrunescape.fandom.com/wiki/Category:Items";
        Iterator<Definition> iterator = definitions.iterator();

        while (iterator.hasNext()) {
            Definition definition = iterator.next();
            iterator.remove();
            tables.add(new WikiTable(link + format(definition.name)) {
                @Override
                protected void parseDocument(Document document) {
                    Elements infobox = document.select(".wikitable.infobox");
                    Elements equipment = document.select(".wikitable.smallpadding");
                    int idx = 0;

                    EquipmentType type = EquipmentType.NOT_WIELDABLE;
                    boolean twoHanded = false;
                    int[] bonuses = null;

                    for (Element info : infobox) {
                        JsonObject object = new JsonObject();
                        Elements attributes = infobox.select("a");

                        String destroyMessage = "Drop";
                        double weight = 0;
                        boolean tradeable = false;

                        for (Element attribute : attributes) {
                            String attr = attribute.attr("title");
                            try {
                                switch (attr) {
                                    case "Tradeable": {
                                        tradeable = attribute.parent().nextElementSibling().text().equalsIgnoreCase("Yes");
                                        break;
                                    }
                                    case "Destroy": {
                                        destroyMessage = attribute.parent().nextElementSibling().text();
                                        break;
                                    }
                                    case "Weight": {
                                        Element element = attribute.parent().nextElementSibling();
                                        String alchemy = element.text().replaceAll("[^-\\d.]", "");
                                        if (!alchemy.matches("-?[0-9]*\\.?[0-9]*")) {
                                            alchemy = element.children().last().nextSibling().toString().replaceAll("[^-\\d.]", "");
                                        }
                                        if (!alchemy.isEmpty())
                                            weight = Double.parseDouble(alchemy);
                                        break;
                                    }
                                }
                            } catch (Exception ignored) {
                                logger.warn("Exception while parsing item [id=" + definition.id + ", name=" + definition.name + "]", ignored);
                            }
                        }

                        try {
                            if (!equipment.isEmpty()) {
                                bonuses = new int[Equipment.SIZE];

                                Elements equipmentInfo = equipment.select("td");
                                int ind = 0;
                                if (equipmentInfo.size() > idx) {
                                    do {
                                        Element attribute = equipmentInfo.get(idx++);
                                        if (attribute.attr("style").equals("text-align: center; width: 35px;")
                                                || attribute.attr("style").equals("text-align: center; width: 30px;")) {
                                            bonuses[ind++ % Equipment.SIZE] = Integer.parseInt(attribute.text().replace("%", ""));
                                        }
                                    } while (ind < Equipment.SIZE);
                                }

                                for (Element attribute : equipment.select("img")) {
                                    switch (attribute.attr("data-image-key")) {
                                        case "Head_slot.png":
                                            type = EquipmentType.HELM; // TODO
                                            break;
                                        case "Cape_slot.png":
                                            type = EquipmentType.CAPE;
                                            break;
                                        case "Weapon_slot.png":
                                            type = EquipmentType.WEAPON;
                                            break;
                                        case "Neck_slot.png":
                                            type = EquipmentType.AMULET;
                                            break;
                                        case "Ammo_slot.png":
                                            type = EquipmentType.ARROWS;
                                            break;
                                        case "Shield_slot.png":
                                            type = EquipmentType.SHIELD;
                                            break;
                                        case "Torso_slot.png":
                                            type = EquipmentType.BODY; // TODO
                                            break;
                                        case "Legs_slot.png":
                                            type = EquipmentType.LEGS;
                                            break;
                                        case "Gloves_slot.png":
                                            type = EquipmentType.GLOVES;
                                            break;
                                        case "Boots_slot.png":
                                            type = EquipmentType.BOOTS;
                                            break;
                                        case "Ring_slot.png":
                                            type = EquipmentType.RING;
                                            break;
                                        case "2h_slot.png":
                                            type = EquipmentType.WEAPON;
                                            twoHanded = true;
                                            break;
                                    }
                                }
                            }
                        } catch (Exception ignored) {
                            bonuses = null;
                            type = EquipmentType.NOT_WIELDABLE;
                            logger.warn("Exception while parsing item equipment data [id=" + definition.id + ", name=" + definition.name + "]", ignored);
                        }

                        object.addProperty("name", info.select("caption").text());

                        if (!destroyMessage.equals("Drop"))
                            object.addProperty("destroy-message", destroyMessage);

                        if (tradeable)
                            object.addProperty("tradable", true);

                        if (twoHanded)
                            object.addProperty("two-handed", true);

                        if (weight != 0)
                            object.addProperty("weight", weight);

                        if (type != EquipmentType.NOT_WIELDABLE)
                            object.addProperty("equipment-type", type.name());

                        if (bonuses != null) {
                            for (int index = 0; index < BONUS_CONFIG_FIELD_NAMES.length; index++) {
                                String bonusName = BONUS_CONFIG_FIELD_NAMES[index];
                                if (bonuses[index] != 0) {
                                    object.addProperty(bonusName, bonuses[index]);
                                }
                            }
                        }

                        if (!table.contains(object)) {
                            table.add(object);
                        }
                    }
                }
            });
        }
        return tables;
    }

    @Override
    protected void finish() {
        JsonArray array = new JsonArray();
        for (WikiTable table : tables) {
            array.addAll(table.getTable());
        }
        writeToJson("wiki_item_dump", array);
    }

    private static String format(String name) {
        name = Utility.capitalizeSentence(name);
        name = name.replace(" ", "_");
        return name;
    }

    private static List<Definition> parseDefinitions() {
        Set<String> added = new HashSet<>();
        List<Definition> definitions = new LinkedList<>();
        new GsonParser("wiki/wiki_item_dump") {
            @Override
            protected void parse(JsonObject data) {
                Definition definition = new Definition();

                definition.id = data.get("id").getAsInt();
                definition.name = data.get("name").getAsString();

                if (added.contains(definition.name))
                    return;

                added.add(definition.name);
                definitions.add(definition);
            }
        }.run();
        added.clear();
        return definitions;
    }

    public static final class Definition {
        public int id;
        public String name;
    }

}
