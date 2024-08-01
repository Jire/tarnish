package com.osroyale.content.dialogue;

/**
 * Handles the item on dialogue.
 *
 * @author Daniel
 */
public class ItemDialogue implements Chainable {

    /** The title of the item dialogue. */
    private final String title;

    /** The context of the item dialogue. */
    private final String context;

    /** The item being displayed on the dialogue. */
    private final int item;

    /**
     * Constructs a new <code>ItemDialogue</code>.
     *
     * @param title   The title of the dialogue.
     * @param context The context of the dialogue.
     * @param item    The item being displayed on the dialogue.
     */
    public ItemDialogue(String title, String context, int item) {
        this.title = title;
        this.context = context;
        this.item = item;
    }

    /**
     * Gets the title of the dialogue.
     *
     * @return The dialogue title.
     */
    public String getTitle() {
        return title;
    }

    /**
     * Gets the context of the dialogue.
     *
     * @return The dialogue context.
     */
    public String getContext() {
        return context;
    }

    /**
     * Gets the item display of the dialogue.
     *
     * @return The item being displayed on the dialogue.
     */
    public int getItem() {
        return item;
    }

    @Override
    public void accept(DialogueFactory factory) {
        factory.sendItem(this);
    }
}
