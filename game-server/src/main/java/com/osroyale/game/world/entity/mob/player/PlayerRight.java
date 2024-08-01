package com.osroyale.game.world.entity.mob.player;

import java.util.Arrays;
import java.util.Optional;

/**
 * Holds all the player right data.
 *
 * @author Daniel
 */
public enum PlayerRight {
    PLAYER("Player", "000000", 0, -1, 4111),
    MODERATOR("Moderator", "245EFF", 1, -1, 4116),
    ADMINISTRATOR("Administrator", "D17417", 2, -1, 4116),
    MANAGER("Manager", "D17417", 3, -1, 4116),
    OWNER("Owner", "ED0C0C", 2, -1, 4117),
    DEVELOPER("Developer", "7D0CED", 4, -1, 4117),

    DONATOR("Donator", "9C4B2F", 5, 10, 4112),
    SUPER_DONATOR("Super Donator", "2F809C", 6, 50, 4112),
    EXTREME_DONATOR("Extreme Donator", "158A76", 7, 250, 4113),
    ELITE_DONATOR("Elite Donator", "2CA395", 8, 500, 4114),
    KING_DONATOR("King Donator", "8F7A42", 9, 1000, 4115),
    VETERAN("Veteran", "B1800A", 10, -1, 4115),
    YOUTUBER("Youtuber", "91111A", 11, -1, 4112),
    IRONMAN("Ironman", "7A6F74", 12, -1, 4112),
    ULTIMATE_IRONMAN("Ultimate Ironman", "7A6F74", 13, -1, 4113),
    HARDCORE_IRONMAN("Hardcore Ironman", "7A6F74", 14, -1, 4114),
    HELPER("Helper", "5C5858", 15, -1, 4115),
    GRAPHIC("Graphic", "CE795A", 17, -1, 4112);

    /**
     * The rank name.
     */
    private final String name;

    /**
     * The crown identification.
     */
    private final int crown;

    private final int moneyRequired;

    /**
     * The rank color.
     */
    private final String color;

    /**
     * The rank rest animation.
     */
    private final int restAnimation;

    /**
     * Constructs a new <code>PlayerRight</code>.
     */
    PlayerRight(String name, String color, int crown, int moneyRequired, int restAnimation) {
        this.name = name;
        this.color = color;
        this.crown = crown;
        this.moneyRequired = moneyRequired;
        this.restAnimation = restAnimation;
    }

    public static Optional<PlayerRight> lookup(int id) {
        return Arrays.stream(values()).filter(it -> it.crown == id).findFirst();
    }

    public static boolean isOwner(Player player) {
        return player.right.equals(OWNER);
    }

    /**
     * Checks if the player has developer status.
     */
    public static boolean isDeveloper(Player player) {
        return player.right.equals(OWNER) || player.right.equals(DEVELOPER);
    }

    /**
     * Checks if the player is a privileged member.
     */
    public static boolean isManager(Player player) {
        return isDeveloper(player) || player.right.equals(MANAGER);
    }

    /**
     * Checks if the player is a privileged member.
     */
    public static boolean isAdministrator(Player player) {
        return isManager(player) || player.right.equals(ADMINISTRATOR);
    }

    /**
     * Checks if the player is a management member.
     */
    public static boolean isModerator(Player player) {
        return isAdministrator(player) || player.right.equals(MODERATOR);
    }

    /**
     * Checks if the player is a HELPER member.
     */
    public static boolean isHelper(Player player) {
        return isModerator(player) || player.right.equals(HELPER);
    }

    /**
     * Checks if the player has donator status.
     */
    public static boolean isDonator(Player player) {
        return isModerator(player) || isHelper(player) || player.donation.getSpent() >= DONATOR.getMoneyRequired();
    }

    /**
     * Checks if the player has super donator status.
     */
    public static boolean isSuper(Player player) {
        return isModerator(player) || isHelper(player) || player.donation.getSpent() >= SUPER_DONATOR.getMoneyRequired();
    }

    /**
     * Checks if the player has extreme donator status.
     */
    public static boolean isExtreme(Player player) {
        return isAdministrator(player) || player.donation.getSpent() >= EXTREME_DONATOR.getMoneyRequired();
    }

    /**
     * Checks if the player has elite donator status.
     */
    public static boolean isElite(Player player) {
        return isAdministrator(player) || player.donation.getSpent() >= ELITE_DONATOR.getMoneyRequired();
    }

    /**
     * Checks if the player has king donator status.
     */
    public static boolean isKing(Player player) {
        return isAdministrator(player) || player.donation.getSpent() >= KING_DONATOR.getMoneyRequired();
    }

    /**
     * Checks if the player is an ironman.
     */
    public static boolean isIronman(Player player) {
        return player.right.equals(IRONMAN) || player.right.equals(ULTIMATE_IRONMAN) || player.right.equals(HARDCORE_IRONMAN);
    }

    /**
     * Gets the crown display.
     */
    public static String getCrown(Player player) {
        return getCrown(player.right);
    }

    /**
     * Gets the crown display.
     */
    public static String getCrown(PlayerRight right) {
        return right.equals(PLAYER) ? "" : "<img=" + (right.getCrown() - 1) + ">";
    }

    public static String getColor(PlayerRight right) {
        return "<col=" + right.getColor() + "><img=" + (right.getCrown() - 1) + ">";
    }

    public String getCrownText() {
        return this == PLAYER ? "" : "<img=" + (crown - 1) + "> ";
    }

    public static int getForumGroupId(PlayerRight right) {
        switch (right) {

            case DONATOR:
                return 10;

            case SUPER_DONATOR:
                return 14;

            case EXTREME_DONATOR:
                return 11;

            case ELITE_DONATOR:
                return 12;

            case KING_DONATOR:
                return 16;

            default:
                return -1;
        }
    }

    /**
     * Gets the deposit amount.
     */
    public static int getDepositAmount(Player player) {
        if (isKing(player))
            return 28;
        else if (isElite(player))
            return 20;
        else if (isExtreme(player))
            return 15;
        else if (isSuper(player))
            return 10;
        else if (isDonator(player))
            return 5;
        return 1;
    }

    public static int getPresetAmount(Player player) {
        if (isKing(player))
            return 10;
        else if (isElite(player))
            return 9;
        else if (isExtreme(player))
            return 8;
        else if (isSuper(player))
            return 7;
        else if (isDonator(player))
            return 6;
        return 5;
    }

    public static int getBloodMoney(Player player) {
        if (isKing(player))
            return 1500;
        else if (isElite(player))
            return 1400;
        else if (isExtreme(player))
            return 1300;
        else if (isSuper(player))
            return 1200;
        else if (isDonator(player))
            return 1100;
        return 1000;
    }

    public static int dropRateIncrease(Player player, int roll) {
        if (isKing(player)) return roll * 9 / 10; // 10%
        else if (isElite(player)) return roll * 37 / 40; // 7.5%
        else if (isExtreme(player)) return roll * 47 / 50; // 6%
        else if (isSuper(player)) return roll * 189 / 200; // 5.5%
        else if (isDonator(player)) return roll * 191 / 200; // 4.5%
        else return roll * 97 / 100; // 3%
    }

    public static double getDropRateBonus(Player player) {
        if (isKing(player)) return 10;
        else if (isElite(player)) return 7.5;
        else if (isExtreme(player)) return 6;
        else if (isSuper(player)) return 5.5;
        else if (isDonator(player)) return 4.5;
        else return 3;
    }

    public static PlayerRight forSpent(int spent) {
        if (spent >= PlayerRight.KING_DONATOR.getMoneyRequired())
            return PlayerRight.KING_DONATOR;
        if (spent >= PlayerRight.EXTREME_DONATOR.getMoneyRequired())
            return PlayerRight.EXTREME_DONATOR;
        if (spent >= PlayerRight.ELITE_DONATOR.getMoneyRequired())
            return PlayerRight.ELITE_DONATOR;
        if (spent >= PlayerRight.SUPER_DONATOR.getMoneyRequired())
            return PlayerRight.SUPER_DONATOR;
        if (spent >= PlayerRight.DONATOR.getMoneyRequired())
            return PlayerRight.DONATOR;
        return null;
    }

    /**
     * Gets the name of the rank.
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the crown of the rank.
     */
    public int getCrown() {
        return crown;
    }

    public int getMoneyRequired() {
        return moneyRequired;
    }

    /**
     * Gets the color of the rank.
     */
    public String getColor() {
        return color;
    }

    /**
     * Gets the rest animation of the rank.
     */
    public int getRestAnimation() {
        return restAnimation;
    }

    public final boolean greater(PlayerRight other) {
        return ordinal() > other.ordinal();
    }

    public final boolean greaterOrEqual(PlayerRight other) {
        return ordinal() >= other.ordinal();
    }

    public final boolean less(PlayerRight other) {
        return ordinal() < other.ordinal();
    }

    public final boolean lessOrEqual(PlayerRight other) {
        return ordinal() <= other.ordinal();
    }
}
