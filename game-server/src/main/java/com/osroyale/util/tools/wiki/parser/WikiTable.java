package com.osroyale.util.tools.wiki.parser;

import com.google.gson.JsonArray;
import org.jsoup.nodes.Document;

public abstract class WikiTable {
    private final String link;
    protected final JsonArray table = new JsonArray();

    protected WikiTable(String link) {
        this.link = link;
    }

    public String getLink() {
        return link;
    }

    protected abstract void parseDocument(Document document);

    public JsonArray getTable() {
        return table;
    }

}
