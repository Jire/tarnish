package com.osroyale;


import net.runelite.client.RuneLite;
import java.io.*;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

/**
 * Handles all utility type methods for the client.
 *
 * @author Daniel
 */
public class Utility {

    public static final Random RANDOM = new Random();

    public static final Path CACHE_DIRECTORY = Path.of(System.getProperty("user.home"), Configuration.CACHE_NAME);

    public static String findcachedir() {


        return RuneLite.CACHE_DIR.getAbsolutePath() + "/";
    }


/*
    static void captureScreen() {
        try {
            Window window = KeyboardFocusManager.getCurrentKeyboardFocusManager().getFocusedWindow();
            Point point = window.getLocationOnScreen();
            int x = (int) point.getX();
            int y = (int) point.getY();
            int width = window.getWidth();
            int height = window.getHeight();

            Rectangle screenRectangle = new Rectangle(x, y, width, height);
            BufferedImage image = new Robot().createScreenCapture(screenRectangle);
            String path = System.getProperty("user.home") + "/Desktop/Tarnish Screenshots/Screenshot ";

//            if (Client.instance.captureDelay > Client.tick) {
//                Client.instance.pushMessage("Please wait a moment before using this function again.");
//                return;
//            }

            for (int index = 0; ; index++) {
                File indexedImage = new File(path + index + ".png");
                if (!indexedImage.exists()) {
//                    Client.instance.captureDelay = Client.tick + 150;
                    Client.instance.captureId = index;
                    break;
                }
            }

            File directory = new File(path + Client.instance.captureId + ".png");
            directory.getParentFile().mkdirs();
            ImageIO.write(image, "PNG", directory);
            Client.instance.pushMessage("Screenshot was taken successfully - " + getDate(), false);
        } catch (Exception e) {
            e.printStackTrace();
            Client.instance.pushMessage("There was an issue taking your screenshot!", false);
        }
    }
*/

    /** Formats digits for integers. */
    static String formatDigits(final int amount) {
        return NumberFormat.getInstance().format(amount);
    }

    /** Formats digits for longs. */
    public static String formatDigits(final long amount) {
        return NumberFormat.getInstance().format(amount);
    }

    /** Formats digits for doubles. */
    public static String formatDigits(final double amount) {
        return NumberFormat.getInstance().format(amount);
    }

    /** Formats booleans. */
    public static String formatBoolean(final boolean flag) {
        return flag ? "<col=64E838>On" : "<col=ED3E3E>Off";
    }

    /** Gets the prefix of a boolean. */
    public static String booleanPrefix(boolean flag) {
        return "" + (flag ? "<col=4DE024>" : "<col=D61E30>");
    }

    /** Formats booleans. */
    public static int getPrefix(final boolean flag) {
        return flag ? 0x64E838 : 0xED3E3E;
    }

    /** Gets the date of server. */
    static String getDate() {
        return new SimpleDateFormat("EE MMM dd yyyy").format(new Date());
    }

    /** Gets the current server time and formats it */
    static String getTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm aa");
        return dateFormat.format(new Date());
    }

    /** Gets the time based off a long. */
    public static String getTime(long period) {
        return new SimpleDateFormat("m:ss").format(System.currentTimeMillis() - period);
    }

    /** Checks if the player is a staff member. */
    static boolean staff(int right) {
        return right > 0 && right < 5;
    }

    static void reporterror(String s) {
        System.err.println("Error: " + s);
    }

    /** Formats the player name. */
    public static String formatName(final String name) {
        if (name.length() > 0) {
            char first = name.charAt(0);
            StringBuilder fixed = new StringBuilder("" + Character.toUpperCase(first));
            for (int index = 1; index < name.length(); index++) {
                char character = name.charAt(index);
                if (character == '_' || character == ' ') {
                    character = ' ';
                    fixed.append(character);
                    if (index + 1 < name.length() && name.charAt(index + 1) >= 'a' && name.charAt(index + 1) <= 'z') {
                        fixed.append(Character.toUpperCase(name.charAt(index + 1)));
                        index++;
                    }
                } else {
                    fixed.append(character);
                }
            }
            return fixed.toString();
        } else {
            return name;
        }
    }

    public static String getRank(int rank) {
        switch (rank) {
            case 0:
                return "Moderator";
            case 1:
                return "Administrator";
            case 2:
                return "Owner";
            case 3:
                return "Developer";
        }
        return "Player";
    }

    static String getDropColor(int type) {
        if (!Settings.ITEM_RARITY_COLOR)
            return "<col=FF9040>";
        if (type == 0)
            return "<col=FF9040>";
        if (type == 1)
            return "<col=ed322f>";
        if (type == 2)
            return "<col=1f81dd>";
        return "<col=FF9040>";
    }

    static int dropColor(int type) {
        if (!Settings.ITEM_RARITY_COLOR)
            return 0xffffff;
        if (type == 0)
            return 0xffffff;
        if (type == 1)
            return 0xed322f;
        if (type == 2)
            return 0x1f81dd;
        return 0xffffff;
    }

    public static int getProgressColor(int percent) {
        if (percent <= 15) {
            return 0x808080;
        }
        if (percent <= 45) {
            return 0x7f7f00;
        }
        if (percent <= 65) {
            return 0x999900;
        }
        if (percent <= 75) {
            return 0xb2b200;
        }
        if (percent <= 90) {
            return 0x007f00;
        }
        return 31744;
    }

    /** Capitalize each letter after . */
    public static String capitalizeSentence(final String string) {
        StringBuilder sb = new StringBuilder(string);
        try {
            int pos = 0;
            boolean capitalize = true;
            while (pos < sb.length()) {
                if (sb.charAt(pos) == '.') {
                    capitalize = true;
                } else if (capitalize && !Character.isWhitespace(sb.charAt(pos))) {
                    sb.setCharAt(pos, Character.toUpperCase(sb.charAt(pos)));
                    capitalize = false;
                }
                pos++;
            }
            return sb.toString();
        } finally {
            sb.setLength(0);
            sb = null;
        }
    }

    static String intToKOrMilLongName(int i) {
        String s = "" + i;
        for (int k = s.length() - 3; k > 0; k -= 3)
            s = s.substring(0, k) + "," + s.substring(k);
        if (s.length() > 8)
            s = "<col=475154>" + s.substring(0, s.length() - 8) + " million <col=ffffff>(" + s + ")";
        else if (s.length() > 4)
            s = "<col=65535>" + s.substring(0, s.length() - 4) + "K <col=ffffff>(" + s + ")";
        return " " + s;
    }

    static String methodR(int j) {
        if (j >= 0 && j < 10000)
            return "" + j;
        if (j >= 10000 && j < 10000000)
            return j / 1000 + "K";
        if (j >= 10000000 && j < 999999999)
            return j / 1000000 + "M";
        if (j >= 999999999)
            return "*";
        else
            return "?";
    }

    static String getFileNameWithoutExtension(String fileName) {
        File tmpFile = new File(fileName);
        tmpFile.getName();
        int whereDot = tmpFile.getName().lastIndexOf('.');
        if (0 < whereDot && whereDot <= tmpFile.getName().length() - 2) {
            return tmpFile.getName().substring(0, whereDot);
        }
        return "";
    }

    public static void launchURL(String url) {
        String osName = System.getProperty("os.name");
        try {
            if (osName.startsWith("Mac OS")) {
                Class<?> fileMgr = Class.forName("com.apple.eio.FileManager");
                Method openURL = fileMgr.getDeclaredMethod("openURL", String.class);
                openURL.invoke(null, url);
            } else if (osName.startsWith("Windows"))
                Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler " + url);
            else {
                String[] browsers = {"firefox", "opera", "konqueror", "epiphany", "mozilla", "netscape", "safari"};
                String browser = null;
                for (int count = 0; count < browsers.length && browser == null; count++)
                    if (Runtime.getRuntime().exec(new String[]{"which", browsers[count]}).waitFor() == 0)
                        browser = browsers[count];
                if (browser == null) {
                    throw new Exception("Could not find web browser");
                } else
                    Runtime.getRuntime().exec(new String[]{browser, url});
            }
        } catch (Exception e) {
            System.out.println("Failed to open URL.");
        }
    }

    static String getFormattedTime(int time) {
        int seconds = time / 50;

        if (seconds < 60)
            return "0:" + seconds + "";
        else {
            int mins = seconds / 60;
            int remainderSecs = seconds - (mins * 60);
            if (mins < 60) {
                return mins + ":" + (remainderSecs < 10 ? "0" : "") + remainderSecs + "";
            } else {
                int hours = mins / 60;
                int remainderMins = mins - (hours * 60);
                return (hours < 10 ? "0" : "") + hours + "h " + (remainderMins < 10 ? "0" : "") + remainderMins + "m " + (remainderSecs < 10 ? "0" : "") + remainderSecs + "s";
            }
        }
    }

    public static int random(int bound) {
        return random(0, bound, false);
    }

    public static int random(int lowerBound, int upperBound) {
        return random(lowerBound, upperBound, false);
    }

    public static double round(double value, int places) {
        if (places < 0) {
            throw new IllegalArgumentException();
        }

        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    public static int random(int lowerBound, int upperBound, boolean inclusive) {
        if (lowerBound >= upperBound) {
            throw new IllegalArgumentException("The lower bound cannot be larger than or equal to the upper bound!");
        }

        return lowerBound + RANDOM.nextInt(upperBound - lowerBound) + (inclusive ? 1 : 0);
    }


    static <T> T randomElement(T[] array) {
        return array[(int) (RANDOM.nextDouble() * array.length)];
    }

    static int randomElement(int[] array) {
        return array[(int) (RANDOM.nextDouble() * array.length)];
    }
}