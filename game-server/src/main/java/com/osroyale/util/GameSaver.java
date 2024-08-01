package com.osroyale.util;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.osroyale.content.WellOfGoodwill;
import com.osroyale.content.WellOfGoodwill.Contributor;
import com.osroyale.content.bot.BotUtility;
import com.osroyale.game.world.entity.mob.player.PlayerRight;
import com.osroyale.util.parser.GsonParser;

import java.io.FileWriter;
import java.util.HashMap;
import java.util.Optional;

/**
 * Created by Daniel on 2017-11-05.
 */
public final class GameSaver {

    public static int MAX_PLAYERS;
    public static long PERSONAL_ITEM_WORTH = 0;
    public static long ITEMS_SOLD = 0;

    public static void save() {
        Thread.startVirtualThread(() -> {
            try (FileWriter fw = new FileWriter("./data/content/game/world.json")) {
                fw.write(GsonUtils.JSON_PRETTY_NO_NULLS.get().toJson(toJson()));
            } catch (final Exception e) {
                e.printStackTrace();
            }
        });
    }

    public static JsonObject toJson() {
        JsonObject object = new JsonObject();

        object.addProperty("max-players", MAX_PLAYERS);

        /* Well of Goodwill */
        object.addProperty("wog-active-time", WellOfGoodwill.activeTime);
        object.addProperty("wog-contribution", WellOfGoodwill.CONTRIBUTION);
        object.addProperty("wog-last-contributor", WellOfGoodwill.lastContributor);

        JsonArray wogArray = new JsonArray();
        for (Contributor contributor : WellOfGoodwill.contributors) {
            JsonObject contributorObj = new JsonObject();
            contributorObj.addProperty("name", contributor.name);
            contributorObj.addProperty("rank", contributor.rank.getCrown());
            contributorObj.addProperty("amount", contributor.contribution);
            wogArray.add(contributorObj);
        }

        object.add("contributors", wogArray);

        /* Personal Store */
        object.addProperty("personal-store-worth", PERSONAL_ITEM_WORTH);
        object.addProperty("personal-store-items-sold", ITEMS_SOLD);
        object.add("pk-bot-loot", GsonUtils.JSON_PRETTY_ALLOW_NULL.get().toJsonTree(BotUtility.BOOT_LOOT));
        return object;
    }

    public static void load() {
        new GsonParser("content/game/world") {
            @Override
            protected void parse(JsonObject object) {
                MAX_PLAYERS = object.get("max-players").getAsInt();

                /* Well of Goodwill */
                WellOfGoodwill.activeTime = object.get("wog-active-time").getAsInt();
                WellOfGoodwill.CONTRIBUTION = object.get("wog-contribution").getAsInt();

                if (object.has("wog-last-contributor")) {
                    WellOfGoodwill.lastContributor = object.get("wog-last-contributor").getAsString();
                }

                final JsonArray contributors = object.get("contributors").getAsJsonArray();
                for (JsonElement element : contributors) {
                    final JsonObject contributorObj = (JsonObject) element;
                    final String name = contributorObj.get("name").getAsString();
                    final Optional<PlayerRight> rank = PlayerRight.lookup(contributorObj.get("rank").getAsInt());
                    final int amount = contributorObj.get("amount").getAsInt();
                    WellOfGoodwill.add(name, rank.orElse(PlayerRight.PLAYER), amount);
                }

                 /* Personal Store */
                PERSONAL_ITEM_WORTH = object.get("personal-store-worth").getAsLong();
                ITEMS_SOLD = object.get("personal-store-items-sold").getAsLong();
                BotUtility.BOOT_LOOT = GsonUtils.JSON_PRETTY_ALLOW_NULL.get().fromJson(object.get("pk-bot-loot"), new TypeToken<HashMap<Integer, MutableNumber>>() {  }.getType());
            }
        }.run();
    }
}
