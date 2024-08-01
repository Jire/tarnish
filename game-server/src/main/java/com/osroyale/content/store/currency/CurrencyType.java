package com.osroyale.content.store.currency;

import com.google.common.collect.ImmutableSet;
import com.osroyale.content.store.currency.impl.*;
import com.osroyale.game.world.entity.mob.player.Player;
import com.osroyale.util.Utility;

import java.util.Arrays;
import java.util.Optional;

/**
 * The enumerated type whom holds all the currencies usable for a server.
 *
 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
 * @since 4-1-2017.
 */
public enum CurrencyType {
    COINS(0, new ItemCurrency(995)),
    TOKKUL(1, new ItemCurrency(6529)),
    BLOOD_MONEY(2, new ItemCurrency(13307)),
    SLAYER_POINTS(4, new SlayerPointCurrency()),
    PEST_POINTS(5, new PestPointCurrency()),
    STARDUST(6, new ItemCurrency(25527)),
    CLAN_POINTS(6, new ClanPointCurrency()),
    LMS_POINTS(6, new LMSPointCurrency()),
    VOTE_POINTS(7, new VotePointCurrency()),
    PRESTIGE_POINTS(8, new PrestigePointCurrency()),
    KOLODION_POINTS(9, new MageArenaCurrency()),
    GRACEFUL_TOKEN(10, new ItemCurrency(11849)),
    DONATOR_POINTS(11, new DonatorPointCurrency()),
    SKILLING_POINTS(12, new SkillingPointCurrency()),
    PLATINUM_TOKEN(13, new ItemCurrency(13204)),
    ROYALE_TOKEN(14, new ItemCurrency(20527)),
    PARAMAYA_TICKET(15, new ItemCurrency(620));

    private static final ImmutableSet<CurrencyType> VALUES = ImmutableSet.copyOf(values());

    public final Currency currency;

    public final int id;

    CurrencyType(int id, Currency currency) {
        this.id = id;
        this.currency = currency;

    }

    public static Optional<CurrencyType> lookup(int id) {
        return Arrays.stream(values()).filter(it -> it.getId() == id).findFirst();
    }

    public int getId() {
        return id;
    }

    public static boolean isCurrency(int id) {
        return VALUES.stream().filter(i -> i.currency.tangible()).anyMatch(i -> ((ItemCurrency) i.currency).itemId == id);
    }

    public static String getValue(Player player, CurrencyType currency) {
        String value = "";
        switch (currency) {
            case COINS:
                value = "Coins: " + Utility.formatDigits(player.inventory.contains(995) ? player.inventory.computeAmountForId(995) : 0);
                break;
            case TOKKUL:
                value = "Tokkuls: " + Utility.formatDigits(player.inventory.contains(6529) ? player.inventory.computeAmountForId(6529) : 0);
                break;
            case GRACEFUL_TOKEN:
                value = "Tokens: " + Utility.formatDigits(player.inventory.contains(11849) ? player.inventory.computeAmountForId(11849) : 0);
                break;
            case KOLODION_POINTS:
                value = "Points: " + Utility.formatDigits(player.mageArenaPoints);
                break;
            case BLOOD_MONEY:
                value = "BM: " + Utility.formatDigits(player.inventory.contains(13307) ? player.inventory.computeAmountForId(13307) : 0);
                break;
            case PLATINUM_TOKEN:
                value = "Platinum Token: " + Utility.formatDigits(player.inventory.contains(13204) ? player.inventory.computeAmountForId(13204) : 0);
                break;
            case ROYALE_TOKEN:
                value = "Royale Token: " + Utility.formatDigits(player.inventory.contains(20527) ? player.inventory.computeAmountForId(20527) : 0);
                break;
            case SLAYER_POINTS:
                value = "Points: " + Utility.formatDigits(player.slayer.getPoints());
                break;
            case VOTE_POINTS:
                value = "Points: " + Utility.formatDigits(player.votePoints);
                break;
            case PEST_POINTS:
                value = "Points: " + Utility.formatDigits(player.pestPoints);
                break;
            case LMS_POINTS:
                value = "Points: " + Utility.formatDigits(player.lmsPoints);
                break;
            case PRESTIGE_POINTS:
                value = "Points: " + Utility.formatDigits(player.prestige.getPrestigePoint());
                break;
            case DONATOR_POINTS:
                value = "Credits: " + Utility.formatDigits(player.donation.getCredits());
                break;
            case SKILLING_POINTS:
                value = "Points: " + Utility.formatDigits(player.skillingPoints);
                break;
            case STARDUST:
                value = "Stardust: " + Utility.formatDigits(player.inventory.contains(25527) ? player.inventory.computeAmountForId(25527) : 0);
                break;
            case CLAN_POINTS:
                if (player.clanChannel == null) {
                    value = "0";
                } else {
                    value = "CP: " + Utility.formatDigits(player.clanChannel.getDetails().points);
                }
                break;
        }
        return value;
    }

    @Override
    public String toString() {
        return name().toLowerCase().replace("_", " ");
    }
}
