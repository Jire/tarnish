package com.osroyale.util.tools.wiki.impl;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.osroyale.game.world.items.ItemDefinition;
import com.osroyale.game.world.items.containers.equipment.Equipment;
import com.osroyale.util.tools.wiki.parser.WikiTable;
import com.osroyale.util.tools.wiki.parser.WikiTableParser;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.*;

public class BonusParser extends WikiTableParser {

    /** The table links. */
    private static final String[] TABLE_LINKS = {
            "http://oldschoolrunescape.wikia.com/wiki/Ammunition_slot_table",
            "http://oldschoolrunescape.wikia.com/wiki/Body_slot_table",
            "http://oldschoolrunescape.wikia.com/wiki/Cape_slot_table",
            "http://oldschoolrunescape.wikia.com/wiki/Feet_slot_table",
            "http://oldschoolrunescape.wikia.com/wiki/Hand_slot_table",
            "http://oldschoolrunescape.wikia.com/wiki/Head_slot_table",
            "http://oldschoolrunescape.wikia.com/wiki/Legwear_slot_table",
            "http://oldschoolrunescape.wikia.com/wiki/Neck_slot_table",
            "http://oldschoolrunescape.wikia.com/wiki/Ring_slot_table",
            "http://oldschoolrunescape.wikia.com/wiki/Shield_slot_table",
            "http://oldschoolrunescape.wikia.com/wiki/Two-handed_slot_table",
            "http://oldschoolrunescape.wikia.com/wiki/Weapons_table"
    };

    private BonusParser() {
        super(generateTables());
    }

    private static LinkedList<WikiTable> generateTables() {
        LinkedList<WikiTable> tables = new LinkedList<>();

        for (String link : TABLE_LINKS) {
            tables.add(new WikiTable(link) {
                @Override
                public void parseDocument(Document document) {
                    for (Element wikiTable : document.select(".wikitable")) {
                        for (Element children : wikiTable.children()) {
                            Iterator<Element> iterator = children.children().iterator();
                            iterator.next();
                            while (iterator.hasNext()) {
                                Element child = iterator.next();
                                Elements bonuses = child.children();
                                String name = bonuses.remove(0).text();
                                try {
                                    JsonObject object = new JsonObject();
                                    object.addProperty("name", name);

                                    int idx = 0;
                                    int[] bonusArray = new int[Equipment.SIZE];
                                    for (Element data : bonuses) {

                                        if (data.text().isEmpty()) {
                                            idx++;
                                            continue;
                                        }

                                        if (data.text().contains("trimmed"))
                                            continue;

                                        if (idx >= bonusArray.length)
                                            break;

                                        if ((link.contains("Neck") || link.contains("Body") || link.contains("Leg") || link.contains("Feet") || link.contains("Head") || link.contains("Cape") || link.contains("Ammu") || link.contains("Hand")) && idx == Equipment.RANGED_STRENGTH) {
                                            bonusArray[Equipment.PRAYER_BONUS] = Integer.parseInt(data.text());
                                        } else {
                                            bonusArray[idx++] = Integer.parseInt(data.text());
                                        }
                                    }
                                    object.add("bonuses", GSON.toJsonTree(bonusArray));
                                    table.add(object);
                                } catch (Exception e) {
                                    System.err.println("Failed to parse " + name + "   ---   " + e.toString());
                                }
                            }
                        }
                    }
                }
            });
        }

        return tables;
    }

    @Override
    protected void finish() {
        Map<String, Integer[]> bonusMap = new HashMap<>(tables.size());
        JsonArray array = new JsonArray();

        for (WikiTable table : tables) {
            JsonArray tableData = table.getTable();
            array.addAll(tableData);

            for (JsonElement data : tableData) {
                JsonObject next = (JsonObject) data;
                String name = next.get("name").getAsString();
                Integer[] bonuses = GSON.fromJson(next.get("bonuses"), Integer[].class);
                if (bonusMap.containsKey(name)) {
                    System.err.println("Conflicting item: " + name);
                    System.err.println(Arrays.toString(bonuses));
                    System.err.println(Arrays.toString(bonusMap.get(name)));
                    System.err.println();
                } else {
                    bonusMap.put(name, bonuses);
                }
            }
        }

        writeToJson("wiki_dump", array);

        ItemDefinition.createParser().run();
        for (ItemDefinition definition : ItemDefinition.DEFINITIONS) {
            if (definition == null) continue;
            Integer[] bonuses = bonusMap.get(definition.getName());
            if (bonuses != null) {
                Arrays.setAll(definition.getBonuses(), index -> bonuses[index]);
            } else {
                Arrays.setAll(definition.getBonuses(), index -> 0);
            }
        }
        ItemDefinition.dump("wiki/bonus_dump");
    }

    public static void main(String[] args) throws InterruptedException {
        BonusParser parser = new BonusParser();
        parser.begin();
    }

}
