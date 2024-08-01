package com.osroyale.content.donators;

/**
 * Holds all the donator bond data.
 *
 * @author Daniel.
 */
public enum DonatorBond {
    BOND_10(13190, 10, 100),
    BOND_50(13191, 50, 500),
    BOND_100(13192, 100, 1000),
    BOND_200(13193, 200, 2000),
    BOND_500(13194, 500, 5000);

    /** The item identification of the donator bond. */
    public final int item;

    /** The amount of money spent that was required for this bond. */
    public final int moneySpent;

    /** The amount of credits this bond will give. */
    public final int credits;

    /** Constructs a new <code>DonatorBond</code>. */
    DonatorBond(int item, int moneySpent, int credits) {
        this.item = item;
        this.moneySpent = moneySpent;
        this.credits = credits;
    }

    /** Gets the bond data based on the item provided. */
    public static DonatorBond forId(int item) {
        for (DonatorBond bond : values()) {
            if (bond.item == item)
                return bond;
        }
        return null;
    }
}