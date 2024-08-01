package com.osroyale.content.dialogue;

/**
 * Handles the information box dialogue.
 *
 * @author Daniel
 */
public class InformationDialogue implements Chainable {

    /** The title of the item dialogue. */
    private final String title;

    /** The context of the item dialogue. */
    private final String[] lines;

    /**
     * Constructs a new <code>ItemDialogue</code>.
     *
     * @param title   The title of the dialogue.
     * @param lines The context of the dialogue.
     */
    public InformationDialogue(String title, String...lines) {
        this.title = title;
        this.lines = lines;
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
    public String[] getLines() {
        return lines;
    }

    @Override
    public void accept(DialogueFactory factory) {
        factory.sendInformationBox(this);
    }
}
