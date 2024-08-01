package com.osroyale.util.tools.wiki.impl;

import com.google.common.collect.LinkedListMultimap;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.osroyale.game.world.entity.mob.npc.definition.NpcDefinition;
import com.osroyale.game.world.entity.mob.npc.drop.NpcDropManager;
import com.osroyale.game.world.items.ItemDefinition;
import com.osroyale.util.parser.impl.NpcDropParser;
import com.osroyale.util.tools.wiki.parser.WikiTable;
import com.osroyale.util.tools.wiki.parser.WikiTableParser;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.*;

public class NpcDropsParser extends WikiTableParser {

    private static final Map<String, ItemDefinition> itemByName = new HashMap<>();

    private NpcDropsParser() {
        super(generateTables());
    }

    private static LinkedList<WikiTable> generateTables() {
        ItemDefinition.createParser().run();

        for (ItemDefinition definition : ItemDefinition.DEFINITIONS) {
            if (definition == null || definition.getName() == null || definition.getName().equals("null") || itemByName.containsKey(definition.getName())) {
                continue;
            }
            itemByName.put(definition.getName(), definition);
        }

        LinkedListMultimap<String, NpcDefinition> byName = LinkedListMultimap.create();
        NpcDefinition.createParser().run();
        new NpcDropParser().run();

        for (NpcDefinition definition : NpcDefinition.DEFINITIONS) {
            if (definition == null || definition.getName() == null || definition.getName().equals("null")) {
                continue;
            }

            if (!definition.isAttackable()) {
                continue;
            }

            if (NpcDropManager.NPC_DROPS.containsKey(definition.getId())) {
                continue;
            }

            byName.put(definition.getName(), definition);
        }

        LinkedList<WikiTable> tables = new LinkedList<>();
//        tables.add(new NpcDropTable(WIKI_LINK.concat("King Black Dragon".replaceAll(" ", "_")), ImmutableList.of(NpcDefinition.DEFINITIONS[239])));

        for (Map.Entry<String, Collection<NpcDefinition>> entry : byName.asMap().entrySet()) {
            String name = entry.getKey();
            name = name.replaceAll(" ", "_");
            int idx = name.indexOf("(");
            if (idx > -1)
                name = name.substring(0, idx);
            tables.add(new NpcDropTable(WIKI_LINK.concat(name), entry.getValue()));
        }
        return tables;
    }

    @Override
    protected void finish() {
        JsonArray array = new JsonArray();
        for (WikiTable table : tables) {
            array.addAll(table.getTable());
        }
        writeToJson("wiki_drop_dump", array);
    }

    public static void main(String[] args) throws InterruptedException {
        NpcDropsParser parser = new NpcDropsParser();
        parser.begin();
    }

    private static class NpcDropTable extends WikiTable {
        private final Collection<NpcDefinition> definitions;

        NpcDropTable(String link, Collection<NpcDefinition> definitions) {
            super(link);
            this.definitions = definitions;
        }

        @Override
        protected void parseDocument(Document document) {
            Elements dropTable = document.select(".wikitable.sortable.dropstable");
            Elements td = dropTable.select("td");
            Iterator<Element> iterator = td.iterator();

            List<DropItem> drops = new LinkedList<>();
            boolean rareDropTable = false;

            while (iterator.hasNext()) {
                iterator.next();
                String name = iterator.next().text();
                String amount = iterator.next().text().replaceAll(",", "");
                String rarity = iterator.next().text().toLowerCase();
                iterator.next();

                if (!itemByName.containsKey(name)) {
                    log("missing item: " + name);
                    if (name.contains("Rare drop table")) {
                        rareDropTable = true;
                    }
                    continue;
                }

                if (amount.contains("Unknown")) {
                    log("unknown drop amount: " + name);
                    continue;
                }

                try {
                    int id, min, max;

                    int notedIndex = amount.indexOf("(noted)");
                    if (notedIndex > -1) {
                        amount = amount.substring(0, notedIndex - 1).trim();
                        id = itemByName.get(name).getNotedId();
                    } else {
                        id = itemByName.get(name).getId();
                    }

                    DropRarity rate;
                    if (rarity.startsWith("very rare")) {
                        rate = DropRarity.VERY_RARE;
                    } else if (rarity.startsWith("rare")) {
                        rate = DropRarity.RARE;
                    } else if (rarity.startsWith("uncommon")) {
                        rate = DropRarity.UNCOMMON;
                    } else if (rarity.startsWith("common")) {
                        rate = DropRarity.COMMON;
                    } else if (rarity.startsWith("always")) {
                        rate = DropRarity.ALWAYS;
                    } else {
                        log("unknown drop rarity: " + rarity);
                        continue;
                    }

                    if (amount.contains(";")) {
                        String[] amounts = amount.split("; ");
                        for (String s : amounts) {
                            int hyphen = s.indexOf("–");
                            if (hyphen > -1) {
                                min = Integer.valueOf(s.substring(0, hyphen));
                                max = Integer.valueOf(s.substring(hyphen + 1));
                            } else {
                                min = max = Integer.valueOf(s);
                            }
                            drops.add(new DropItem(id, min, max, rate));
                        }
                    } else {
                        int hyphen = amount.indexOf("–");
                        if (hyphen > -1) {
                            min = Integer.valueOf(amount.substring(0, hyphen));
                            max = Integer.valueOf(amount.substring(hyphen + 1));
                        } else {
                            min = max = Integer.valueOf(amount);
                        }
                        drops.add(new DropItem(id, min, max, rate));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            if (drops.isEmpty()) {
                return;
            }

            JsonObject object = new JsonObject();
            JsonArray array = new JsonArray();

            for (NpcDefinition definition : definitions) {
                array.add(definition.getId());
            }

            drops.sort(Comparator.comparing(drop -> drop.rarity));

            object.add("ids", array);
            object.addProperty("rare_table", rareDropTable);
            object.add("drops", buildDropItemTables(drops));
            table.add(object);
        }

        private static JsonArray buildDropItemTables(Collection<DropItem> drops) {
            JsonArray items = new JsonArray();

            for (DropItem item : drops) {
                JsonObject dropItem = new JsonObject();

                dropItem.addProperty("id", item.id);
                dropItem.addProperty("minimum", item.min);
                dropItem.addProperty("maximum", item.max);
                dropItem.addProperty("type", item.rarity.name());

                items.add(dropItem);
            }
            return items;
        }

        private void log(String message) {
            String name = ((NpcDefinition) definitions.toArray()[0]).getName();
            System.out.println("[name=" + name + "]  --  " + message);
        }

    }

    private static class DropItem {
        private final int id;
        private final int min;
        private final int max;
        private final DropRarity rarity;

        private DropItem(int id, int min, int max, DropRarity rarity) {
            this.id = id;
            this.min = min;
            this.max = max;
            this.rarity = rarity;
        }

        @Override
        public int hashCode() {
            return Objects.hash(id, min, max, rarity);
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == this) return true;
            if (obj instanceof DropItem) {
                DropItem other = (DropItem) obj;
                return other.id == id && other.min == min && other.max == max && other.rarity.equals(rarity);
            }
            return false;
        }
    }

    private enum DropRarity {
        ALWAYS,
        COMMON,
        UNCOMMON,
        RARE,
        VERY_RARE
    }

}
