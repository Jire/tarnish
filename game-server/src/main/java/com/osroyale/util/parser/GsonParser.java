package com.osroyale.util.parser;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileReader;

/**
 * This class provides an easy to use google gson parser specifically designed for parsing JSON files.
 *
 * @author Seven
 * @author Daniel
 */
public abstract class GsonParser extends GenericParser {

    private static final Logger logger = LogManager.getLogger();

    /** The {@code Gson} object. */
    protected transient Gson builder;

    /**
     * Creates a new {@code GsonParser}.
     *
     * @param path The specified path of the json file to parse.
     */
    public GsonParser(String path) {
        this(path, true);
    }

    /**
     * Creates a new {@code GsonParser}.
     *
     * @param path The specified path of the json file to parse.
     * @param log  The flag that denotes to log messages.
     */
    public GsonParser(String path, boolean log) {
        super(path, ".json", log);
        this.builder = new GsonBuilder().create();
    }

    public void initialize(int size) {
    }

    /**
     * The method allows a user to modify the data as its being parsed.
     *
     * @param data The {@code JsonObject} that contains all serialized information.
     */
    protected abstract void parse(JsonObject data);

    /**
     * This method handles what happens after the parser has ended.
     */
    protected void onEnd() {
    }

    @Override
    public final void deserialize() {
        try (final JsonReader reader = new JsonReader(new FileReader(path.toFile()))) {
            final JsonElement element = JsonParser.parseReader(reader);

            if (element.isJsonNull()) {
                logger.warn(String.format("json document=%s is null", path));
                return;
            }

            if (element.isJsonArray()) {
                final JsonArray array = element.getAsJsonArray();
                initialize(array.size());
                for (index = 0; index < array.size(); index++) {
                    final JsonElement element2 = array.get(index);
                    if (element2 == null) continue;
                    if (element2 instanceof JsonNull) continue;

                    final JsonObject data = (JsonObject) element2;
                    parse(data);
                }
            } else if (element.isJsonObject()) {
                parse(element.getAsJsonObject());
            }

            onEnd();
        } catch (final Exception e) {
            logger.error("Error parsing json file: " + path, e);
        }
    }

}
