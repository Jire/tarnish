package com.osroyale.util;

import com.osroyale.fs.cache.FileSystem;

import java.text.NumberFormat;
import java.util.stream.IntStream;

public class StringUtils {

    /** An array containing valid username characters. */
    private static final char VALID_USERNAME_CHARACTERS[] = {
        '_',
        'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm',
        'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z',
        '0', '1', '2', '3', '4', '5', '6', '7', '8', '9'
    };

    public static String formatPrice(long amount) {
        if (amount >= 1_000 && amount < 1_000_000) {
            return (amount / 1_000) + "K";
        } else if (amount >= 1_000_000 && amount < 1_000_000_000) {
            return (amount / 1_000_000) + "M";
        } else if (amount >= 1_000_000_000) {
            return (amount / 1_000_000_000) + "B";
        }
        return "" + amount;
    }

    public static int convertAmount(String input) {
        try {
            input = input.toLowerCase().replace(" ", "");

            int pos = input.indexOf('k');

            if (pos != -1) {
                return Integer.parseInt(input.substring(0, pos)) * 1_000;
            }

            pos = input.indexOf('m');

            if (pos != -1) {
                return Integer.parseInt(input.substring(0, pos)) * 1_000_000;
            }

            pos = input.indexOf('b');

            if (pos != -1) {
                return Integer.parseInt(input.substring(0, pos)) * 1_000_000_000;
            }

        } catch (Exception ex) {

        }
        return 0;
    }

    /**
     * Determines the indefinite article of {@code thing}.
     *
     * @param thing the thing to determine for.
     * @return the indefinite article.
     */
    private static String determineIndefiniteArticle(String thing) {
        char first = thing.toLowerCase().charAt(0);
        boolean vowel = first == 'a' || first == 'e' || first == 'i' || first == 'o' || first == 'u';
        return vowel ? "an" : "a";
    }

    /**
     * Determines the plural check of {@code thing}.
     *
     * @param thing the thing to determine for.
     * @return the plural check.
     */
    private static String determinePluralCheck(String thing) {
        boolean needsPlural = !thing.endsWith("s") && !thing.endsWith(")");
        return needsPlural ? "s" : "";
    }

    /**
     * Appends the determined plural check to {@code thing}.
     *
     * @param thing the thing to append.
     * @return the {@code thing} after the plural check has been appended.
     */
    public static String appendPluralCheck(String thing) {
        return thing.concat(determinePluralCheck(thing));
    }

    /**
     * Appends the determined indefinite article to {@code thing}.
     *
     * @param thing the thing to append.
     * @return the {@code thing} after the indefinite article has been appended.
     */
    public static String appendIndefiniteArticle(String thing) {
        return determineIndefiniteArticle(thing).concat(" " + thing);
    }

    public static String capitalize(String string) {
        return Character.toUpperCase(string.charAt(0)) + string.substring(1);
    }

    public static String format(int num) {
        return NumberFormat.getInstance().format(num);
    }

    public static String formatText(String s) {
        for (int i = 0; i < s.length(); i++) {
            if (i == 0) {
                s = String.format("%s%s", Character.toUpperCase(s.charAt(0)), s.substring(1));
            }
            if (!Character.isLetterOrDigit(s.charAt(i))) {
                if (i + 1 < s.length()) {
                    s = String.format("%s%s%s", s.subSequence(0, i + 1), Character.toUpperCase(s.charAt(i + 1)), s.substring(i + 2));
                }
            }
        }
        return s.replace("_", " ");
    }

    public static String getAOrAn(String nextWord) {
        String s = "a";
        final char c = nextWord.toUpperCase().charAt(0);
        if (c == 'A' || c == 'E' || c == 'I' || c == 'O' || c == 'U') {
            s = "an";
        }
        return s;
    }

    public static String hashToString(long input) {
        int length = 12;
        char decoded[] = new char[length];

        while (input != 0) {
            long hash = input;
            input /= 37;
            int index = (int) (hash - input * 37);
            decoded[length--] = VALID_USERNAME_CHARACTERS[index];
        }

        return new String(decoded, length, decoded.length - length);
    }

    /**
     * Hashes a given string input.
     *
     * @param input The string to hash.
     * @return A hash as a {@code long}.
     */
    public static long hash(String input) {
        long hash = 0;

        for (int index = 0; index < input.length(); index++) {
            char key = input.charAt(index);

            hash *= 37;

            if (key >= 'A' && key <= 'Z') {
                hash += (1 + key) - 65;
            } else if (key >= 'a' && key <= 'z') {
                hash += (1 + key) - 97;
            } else if (key >= '0' && key <= '9') {
                hash += (27 + key) - 48;
            }
        }

        while (hash % 37 == 0 && hash != 0) hash /= 37;
        return hash;
    }

    /**
     * Hashes a {@code String} using Jagex's algorithm, this method should be
     * used to convert actual names to hashed names to lookup files within the
     * {@link FileSystem}.
     * @param string The string to hash.
     * @return The hashed string.
     */
    public static int hashArchive(String string) {
        return _hash(string.toUpperCase());
    }

    /**
     * Hashes a {@code String} using Jagex's algorithm, this method should be
     * used to convert actual names to hashed names to lookup files within the
     * {@link FileSystem}.
     * <p>
     * <p>
     * This method should <i>only</i> be used internally, it is marked
     * deprecated as it does not properly hash the specified {@code String}. The
     * functionality of this method is used to register a proper {@code String}
     * {@link #hash(String) <i>hashing method</i>}. The scope of this method has
     * been marked as {@code private} to prevent confusion.
     * </p>
     * @param string The string to hash.
     * @return The hashed string.
     * @deprecated This method should only be used internally as it does not
     * correctly hash the specified {@code String}. See the note
     * below for more information.
     */
    @Deprecated
    private static int _hash(String string) {
        return IntStream.range(0, string.length()).reduce(0, (hash, index) -> hash * 61 + string.charAt(index) - 32);
    }

}
