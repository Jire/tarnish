package com.osroyale.content.collectionlog;

import java.util.HashMap;

public enum CollectionLogPage {

    BOSSES(-21030, CollectionCategory.BOSSES),
    RAIDS(-21028, CollectionCategory.RAIDS),
    CLUES(-21026, CollectionCategory.CLUES),
    MINIGAMES(-21024, CollectionCategory.MINIGAMES),
    OTHER(-21022, CollectionCategory.OTHER);

    private final int button;
    private final CollectionCategory category;

    private CollectionLogPage(int button, CollectionCategory category) {
        this.button = button;
        this.category = category;
    }

    private static HashMap<Integer, CollectionLogPage> pages;

    public static CollectionLogPage forButton(int button) {
        return pages.get(button);
    }

    static {
        pages = new HashMap<>();
        for (CollectionLogPage page : values()) {
            pages.put(page.getButton(), page);
        }
    }


    public int getButton() {
        return this.button;
    }

    public CollectionCategory getCategory() {
        return this.category;
    }

}
