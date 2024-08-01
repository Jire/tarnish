package com.osroyale.util;

/**
 * Holds an enum of colors for ease.
 *
 * @author Daniel
 */
public enum MessageColor {
    /* Developer Debug */
    DEVELOPER("C93443"),

    /* Neutral */
    BLACK("000000"),
    WHITE("FFFFFF"),
    GREY("736D6D"),

    /* Red */
    RED("FF0000"),
    LIGHT_RED("E33653"),
    DARK_RED("A11A1A"),

    /* Purple */
    PURPLE("7F007F"),
    LIGHT_PURPLE("CD45FF"),
    DARK_PURPLE("9415C2"),

    /* Green */
    GREEN("1ADB34"),
    LIGHT_GREEN("34ED4D"),
    DARK_GREEN("118C21"),

    /* Orange */
    ORANGE("FF8400"),
    LIGHT_ORANGE("FFA340"),
    DARK_ORANGE("D47A19"),

    /* Blue */
    BLUE("1944BD"),
    LIGHT_BLUE("0DA6D9"),
    DARK_BLUE("11558C"),

    // other
    BRONZE("7E3200");

    /* The color */
    private final String color;

    MessageColor(String color) {
        this.color = color;
    }

    /**
     * Gets the color.
     * @return The color.
     */
    public String getColor() { return color; }
}
