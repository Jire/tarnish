package com.osroyale.util.parser.old;

import com.google.gson.JsonObject;
import com.osroyale.util.parser.GsonParser;
import com.osroyale.util.parser.old.defs.ItemDefinition;

/**
 * Parses through the item definitions file and creates {@link ItemDefinition}s
 * on startup.
 *
 * @author Daniel | Obey
 */
public class ItemDefinitionParser extends GsonParser {

    public ItemDefinitionParser() {
        super("def/item/item_definitions");
    }

    @Override
    protected void parse(JsonObject data) {
        final int id = data.get("id").getAsInt();
        final String name = data.get("name").getAsString();
        final boolean members = data.get("gameMembers").getAsBoolean();
        final boolean tradeable = data.get("tradeable").getAsBoolean();
        final boolean stackable = data.get("stackable").getAsBoolean();
        final boolean droppable = data.get("droppable").getAsBoolean();
        final boolean noteable = data.get("noteable").getAsBoolean();
        final boolean noted = data.get("noted").getAsBoolean();
        final int notedId = data.get("notedId").getAsInt();
        final int street_value = data.get("street_value").getAsInt();
        final int base_value = data.get("base_value").getAsInt();
        final int high_alch = data.get("high_alch").getAsInt();
        final int low_alch = data.get("low_alch").getAsInt();
        final String examine = data.get("examine").getAsString();

        String destroyMessage = "This item is valuable, you will not\\nget it back once lost.";
        if (data.get("destroy_message") != null) {
            destroyMessage = data.get("destroy_message").getAsString();
        }

        final double weight = data.get("weight").getAsDouble();

        ItemDefinition.DEFINITIONS[id] = new ItemDefinition(id, name, members, tradeable, stackable, droppable, noteable, noted, notedId, street_value, base_value, high_alch, low_alch, examine, destroyMessage, weight);
    }
}
