package com.osroyale.content.gambling;

public enum Flowers {
    /**
     * The black flower
     */
    BLACK(2988),
    /**
     * The blue flower
     */
    BLUE(2982),
    /**
     * The orange flower
     */
    ORANGE(2985),
    /**
     * The pastel flower. Purple, blue, cyan
     */
    PASTEL(2980),
    /**
     * The purple flower
     */
    PURPLE(2984),
    /**
     * The rainbow flower. Red, yellow, blue
     */
    RAINBOW(2986),
    /**
     * The red flower
     */
    RED(2981),
    /**
     * The white flower
     */
    WHITE(2987),
    /**
     * The yellow
     */
    YELLOW(2983);

    /**
     * The id
     */
    private int id;

    /**
     * The id
     *
     * @param id
     *            the id
     */
    Flowers(int id) {
        this.setId(id);
    }

    /**
     * Gets the id
     *
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * Sets the id
     *
     * @param id
     *            the id
     */
    public void setId(int id) {
        this.id = id;
    }
}
