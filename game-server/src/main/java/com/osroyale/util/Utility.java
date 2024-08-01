package com.osroyale.util;

import com.osroyale.game.world.Interactable;
import com.osroyale.game.world.entity.mob.Direction;
import com.osroyale.game.world.entity.mob.Mob;
import com.osroyale.game.world.entity.mob.player.Player;
import com.osroyale.game.world.entity.skill.Skill;
import com.osroyale.game.world.pathfinding.TraversalMap;
import com.osroyale.game.world.pathfinding.path.Path;
import com.osroyale.game.world.position.Position;
import com.osroyale.net.packet.out.SendMessage;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Handles miscellaneous methods.
 *
 * @author Daniel
 */
public class Utility {

    /** Random instance, used to generate pseudo-random primitive types. */
    private static final Random RANDOM = new Random(System.currentTimeMillis());

    /** Array of all valid characters. */
    private static final char[] VALID_CHARS = {'_', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '/'};

    /** Gets a percentage amount. */
    public static double getPercentageAmount(int progress, int total) {
        return 100D * progress / total;
    }

    /** Formats digits for integers. */
    public static String formatDigits(final int amount) {
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

    /** Formats a price for longs. */
    public static String formatPrice(final long amount) {
        if (amount >= 0 && amount < 1_000)
            return "" + amount;
        if (amount >= 1_000 && amount < 1_000_000) {
            return (amount / 1_000) + "K";
        }
        if (amount >= 1_000_000 && amount < 1_000_000_000) {
            return (amount / 1_000_000) + "M";
        }
        if (amount >= 1_000_000_000 && amount < Integer.MAX_VALUE) {
            return (amount / 1_000_000_000) + "B";
        }
        return "<col=fc2a2a>Lots!";
    }

    public static long stringToLong(String string) {
        long l = 0L;
        for (int i = 0; i < string.length() && i < 12; i++) {
            char c = string.charAt(i);
            l *= 37L;
            if (c >= 'A' && c <= 'Z')
                l += (1 + c) - 65;
            else if (c >= 'a' && c <= 'z')
                l += (1 + c) - 97;
            else if (c >= '0' && c <= '9')
                l += (27 + c) - 48;
        }
        while (l % 37L == 0L && l != 0L)
            l /= 37L;
        return l;
    }

    /** Formats name of enum. */
    public static String formatEnum(final String string) {
        return capitalizeSentence(string.toLowerCase().replace("_", " "));
    }

    /** Formats the player name. */
    public static String rank(final String string) {
        return Stream.of(string.trim().split("\\s")).filter(word -> word.length() > 0).map(word -> word.substring(0, 1).toUpperCase() + word.substring(1)).collect(Collectors.joining(" "));
    }

    /** Capitalize each letter after . */
    public static String capitalizeSentence(final String string) {
        int pos = 0;
        boolean capitalize = true;
        StringBuilder sb = new StringBuilder(string);
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
    }

    /** A or an */
    public static String getAOrAn(String nextWord) {
        String s = "a";
        char c = nextWord.toUpperCase().charAt(0);
        if (c == 'A' || c == 'E' || c == 'I' || c == 'O' || c == 'U') {
            s = "an";
        }
        return s;
    }

    /** Gets the date of server. */
    public static String getDate() {
        return new SimpleDateFormat("EE MMM dd yyyy").format(new Date());
    }

    /** Gets the date of server. */
    public static String getSimpleDate() {
        return new SimpleDateFormat("yyyy/MM/dd").format(new Date());
    }

    /** Converts an integer into words. */
    public static String convertWord(int amount) {
        return Words.getInstance(amount).getNumberInWords();
    }

    /** Gets the current server time and formats it */
    public static String getTime() {
        return new SimpleDateFormat("hh:mm aa").format(new Date());
    }

    /** Gets the time based off a long. */
    public static String getTime(long period) {
        return new SimpleDateFormat("m:ss").format(period);
    }

    public static String bigDaddyTime(long period) {
        return new SimpleDateFormat("HH:mm:ss").format(period);
    }

    /** Gets the current uptime of server and formats it */
    public static String getUptime(Stopwatch stopwatch) {
        return getTime((int) (stopwatch.elapsedTime() / 600));
    }

    /** Gets a basic time based off seconds. */
    public static String getTime(int ticks) {
        long secs = ticks * 3 / 5;

        if (secs < 60) {
            return "0:" + (secs < 10 ? "0" : "") + secs;
        }

        long mins = secs / 60;
        long remainderSecs = secs - (mins * 60);
        if (mins < 60) {
            return mins + ":" + (remainderSecs < 10 ? "0" : "") + remainderSecs + "";
        }

        long hours = mins / 60;
        long remainderMins = mins - (hours * 60);
        if (hours < 24) {
            return hours + "h " + (remainderMins < 10 ? "0" : "") + remainderMins + "m " + (remainderSecs < 10 ? "0" : "") + remainderSecs + "s";
        }

        long days = hours / 24;
        long remainderHrs = hours - (days * 24);
        return days + "d " + (remainderHrs < 10 ? "0" : "") + remainderHrs + "h " + (remainderMins < 10 ? "0" : "") + remainderMins + "m";
    }

    /** Converts the first 12 characters in a string of text to a hash. */
    public static long nameToLong(String text) {
        long hash = 0L;
        for (int index = 0; index < text.length() && index < 12; index++) {
            char key = text.charAt(index);
            hash *= 37L;
            if (key >= 'A' && key <= 'Z')
                hash += (1 + key) - 65;
            else if (key >= 'a' && key <= 'z')
                hash += (1 + key) - 97;
            else if (key >= '0' && key <= '9')
                hash += (27 + key) - 48;
        }
        while (hash % 37L == 0L && hash != 0L)
            hash /= 37L;
        return hash;
    }

    public static String longToString(long l) {
        if (l <= 0L || l >= 0x5b5b57f8a98a5dd1L)
            return null;
        if (l % 37L == 0L)
            return null;
        int i = 0;
        char ac[] = new char[12];
        while (l != 0L) {
            long l1 = l;
            l /= 37L;
            ac[11 - i++] = VALID_CHARS[(int) (l1 - l * 37L)];
        }
        return new String(ac, 12 - i, i);
    }

    public static long hash(String input) {
        long hash = 0L;
        if (input == null) {
            input = "null";
        }
        for (int index = 0; index < input.length() && index < 12; index++) {
            char key = input.charAt(index);
            hash *= 37L;
            if (key >= 'A' && key <= 'Z') {
                hash += (1 + key) - 65;
            } else if (key >= 'a' && key <= 'z') {
                hash += (1 + key) - 97;
            } else if (key >= '0' && key <= '9') {
                hash += (27 + key) - 48;
            }
        }
        while (hash % 37L == 0L && hash != 0L) {
            hash /= 37L;
        }
        return hash;
    }

    public static int random(int bound) {
        return random(0, bound, false);
    }

    public static int random(int lowerBound, int upperBound) {
        return random(lowerBound, upperBound, false);
    }

    /** Picks a random element out of any array type. */
    public static <T> T randomElement(Collection<T> collection) {
        return new ArrayList<T>(collection).get((int) (RANDOM.nextDouble() * collection.size()));
    }

    /** Picks a random element out of any list type. */
    public static <T> T randomElement(List<T> list) {
        return list.get((int) (RANDOM.nextDouble() * list.size()));
    }

    public static boolean hasOneOutOf(double chance) {
        if (chance < 1) {
            chance = 0;
        }
        chance *= 100.0;
        return random(1, (int) chance) <= 100;
    }

    /** Picks a random element out of any array type. */
    public static <T> T randomElement(T[] array) {
        return array[(int) (RANDOM.nextDouble() * array.length)];
    }

    /** Picks a random element out of any array type. */
    public static int randomElement(int[] array) {
        return array[(int) (RANDOM.nextDouble() * array.length)];
    }

    public static int random(int lowerBound, int upperBound, boolean inclusive) {
        if (lowerBound >= upperBound) {
            throw new IllegalArgumentException("The lower bound cannot be larger than or equal to the upper bound!");
        }

        return lowerBound + RANDOM.nextInt(upperBound - lowerBound) + (inclusive ? 1 : 0);
    }

    /** Gets all of the classes in a directory */
    public static List<Object> getClassesInDirectory(String directory) {
        List<Object> classes = new LinkedList<>();
        File dir = new File(directory);
        if (!dir.exists() || !dir.isDirectory()) {
            return classes;
        }
        try {
            File[] files = dir.listFiles();
            if (files == null) {
                return classes;
            }
            for (File f : files) {
                if (f.isDirectory() || f.getName().contains("$")) {
                    continue;
                }
                String domainPath = Utility.class.getProtectionDomain().getCodeSource().getLocation().getPath().replace("/", "\\");
                String filePath = "\\" + f.getPath();
                String clazzName = filePath.replace(domainPath, "");
                clazzName = clazzName.replace("\\", ".");
                clazzName = clazzName.replace(".class", "");
                Class<?> clazz = Class.forName(clazzName);
                Object o = clazz.getDeclaredConstructor().newInstance();
                classes.add(o);
            }
        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException
                 | InvocationTargetException | NoSuchMethodException e) {
            // e.printStackTrace();
        }
        return classes;
    }

    /** Gets all of the sub directories of a folder */
    public static List<String> getSubDirectories(Class<?> clazz) {
        String filePath = clazz.getResource("/" + clazz.getName().replace(".", "/") + ".class").getFile();
        File file = new File(filePath);
        File directory = file.getParentFile();
        List<String> list = new ArrayList<>();

        File[] files = directory.listFiles();

        if (files == null) {
            return Collections.emptyList();
        }

        for (File f : files) {
            if (f.isDirectory()) {
                list.add(f.getPath());
            }
        }

        String[] directories = list.toArray(new String[0]);
        return Arrays.asList(directories);
    }


    /*
     * Check for a map region change, and if the map region has changed, set the
     * appropriate flag so the new map region packet is sent.
     */
    public static boolean isRegionChange(Position position, Position region) {
        int diffX = position.getX() - region.getChunkX() * 8;
        int diffY = position.getY() - region.getChunkY() * 8;
        boolean changed = false;

        if (diffX < 16) {
            changed = true;
        } else if (diffX >= 88) {
            changed = true;
        }

        if (diffY < 16) {
            changed = true;
        } else if (diffY >= 88) {
            changed = true;
        }

        return changed;
    }

    public static int getDistance(Interactable source, Position target) {
        Position sourceTopRight = source.getPosition().transform(source.width() - 1, source.length() - 1);

        int dx, dy;

        if (sourceTopRight.getX() < target.getX()) {
            dx = target.getX() - sourceTopRight.getX();
        } else if (source.getX() > target.getX()) {
            dx = source.getX() - target.getX();
        } else {
            dx = 0;
        }

        if (sourceTopRight.getY() < target.getY()) {
            dy = target.getY() - sourceTopRight.getY();
        } else if (source.getY() > target.getY()) {
            dy = source.getY() - target.getY();
        } else {
            dy = 0;
        }

        return dx + dy;
    }

    public static int getDistance(Position source, int sourceWidth, int sourceLength, Position target, int targetWidth, int targetLength) {
        if (source.getHeight() != target.getHeight()) {
            return Integer.MAX_VALUE;
        }

        if (sourceWidth <= 0) sourceWidth = 1;
        if (sourceLength <= 0) sourceLength = 1;
        if (targetWidth <= 0) targetWidth = 1;
        if (targetLength <= 0) targetLength = 1;

        Position sourceTopRight = source.transform(sourceWidth - 1, sourceLength - 1, 0);
        Position targetTopRight = target.transform(targetWidth - 1, targetLength - 1, 0);

        int dx, dy;

        if (sourceTopRight.getX() < target.getX()) {
            dx = target.getX() - sourceTopRight.getX();
        } else if (source.getX() > targetTopRight.getX()) {
            dx = source.getX() - targetTopRight.getX();
        } else {
            dx = 0;
        }

        if (sourceTopRight.getY() < target.getY()) {
            dy = target.getY() - sourceTopRight.getY();
        } else if (source.getY() > targetTopRight.getY()) {
            dy = source.getY() - targetTopRight.getY();
        } else {
            dy = 0;
        }

        return dx + dy;
    }

    public static Position getDelta(Position source, int sourceWidth, int sourceLength, Position target, int targetWidth, int targetLength) {
        if (source.getHeight() != target.getHeight()) {
            return Position.create(Integer.MAX_VALUE, Integer.MAX_VALUE);
        }

        if (sourceWidth <= 0) sourceWidth = 1;
        if (sourceLength <= 0) sourceLength = 1;
        if (targetWidth <= 0) targetWidth = 1;
        if (targetLength <= 0) targetLength = 1;

        Position sourceTopRight = source.transform(sourceWidth - 1, sourceLength - 1, 0);
        Position targetTopRight = target.transform(targetWidth - 1, targetLength - 1, 0);

        int dx, dy;

        if (sourceTopRight.getX() < target.getX()) {
            dx = target.getX() - sourceTopRight.getX();
        } else if (source.getX() > targetTopRight.getX()) {
            dx = source.getX() - targetTopRight.getX();
        } else {
            dx = 0;
        }

        if (sourceTopRight.getY() < target.getY()) {
            dy = target.getY() - sourceTopRight.getY();
        } else if (source.getY() > targetTopRight.getY()) {
            dy = source.getY() - targetTopRight.getY();
        } else {
            dy = 0;
        }

        return Position.create(dx, dy);
    }

    public static int getDistance(Interactable source, Interactable target) {
        return getDistance(source.getPosition(), source.width(), source.length(), target.getPosition(), target.width(), target.length());
    }

    public static Position getDelta(Interactable source, Interactable target) {
        return getDelta(source.getPosition(), source.width(), source.length(), target.getPosition(), target.width(), target.length());
    }

    public static Position getDelta(Position source, Position target) {
        int dx = target.getX() - source.getX();
        int dy = target.getY() - source.getY();
        return Position.create(dx, dy);
    }


    public static boolean withinDistance(Interactable source, Interactable target, int radius) {
        return within(source, target, radius);
    }

    public static boolean withinDistance(Interactable source, Position target, int radius) {
        return within(source.getPosition(), source.width(), source.length(), target, 1, 1, radius);
    }

    public static Position findAccessableTile(Interactable source) {
        Position found = null;
        Position[] positions = getBoundaries(source);

        for (Position next : positions) {
            Direction direction = Direction.getDirection(source.getPosition(), next);

            if (inside(next, 0, 0, source.getPosition(), source.width(), source.length())) {
                continue;
            }

            if (TraversalMap.isTraversable(next, direction, false)) {
                found = next;
                break;
            }
        }

        return found;
    }

    public static Position findBestInside(Interactable source, Interactable target) {
        if (target.width() <= 1 || target.length() <= 1) {
            return target.getPosition();
        }

        int dx, dy, dist = Integer.MAX_VALUE;
        Position best = source.getPosition();

        for (int x = 0; x < target.width(); x++) {
            Position boundary = target.getPosition().transform(x, 0);
            int distance = getDistance(source, boundary);

            if (dist > distance) {
                dist = distance;
                best = boundary;
            }

            boundary = target.getPosition().transform(x, target.length() - 1);
            distance = getDistance(source, boundary);

            if (dist > distance) {
                dist = distance;
                best = boundary;
            }
        }

        for (int y = 0; y < target.length(); y++) {
            Position boundary = target.getPosition().transform(0, y);
            int distance = getDistance(source, boundary);

            if (dist > distance) {
                dist = distance;
                best = boundary;
            }

            boundary = target.getPosition().transform(target.width() - 1, y);
            distance = getDistance(source, boundary);

            if (dist > distance) {
                dist = distance;
                best = boundary;
            }
        }

        if (best.equals(source.getPosition())) {
            return source.getPosition();
        }

        Direction direction = Direction.getDirection(source.getPosition(), best);
        Position sourceTopRight = source.getPosition().transform(source.width() - 1, source.length() - 1, 0);

        if (source.getX() > best.getX()) {
            dx = best.getX() - source.getX();
        } else if (sourceTopRight.getX() < best.getX()) {
            dx = best.getX() - sourceTopRight.getX();
        } else {
            dx = direction.getDirectionX();
        }
        if (source.getY() > best.getY()) {
            dy = best.getY() - source.getY();
        } else if (sourceTopRight.getY() < best.getY()) {
            dy = best.getY() - sourceTopRight.getY();
        } else {
            dy = direction.getDirectionY();
        }

        return source.getPosition().transform(dx, dy);
    }

    public static void fixInsidePosition(Mob source, Interactable target) {
        if (source.movement.needsPlacement()) return;

        List<Position> boundaries = new LinkedList<>();
        Map<Position, LinkedList<Position>> paths = new HashMap<>();
        Position cur = source.getPosition();

        for (Position position : getBoundaries(target)) {
            Position delta = getDelta(target.getPosition(), position);
            int dx = Integer.signum(delta.getX()), dy = Integer.signum(delta.getY());
            Direction direction = Direction.getDirection(dx, dy);

            if (direction == Direction.NONE)
                continue;

            while (inside(cur, source.width(), source.length(), target.getPosition(), target.width(), target.length())) {
                if (!TraversalMap.isTraversable(cur, direction, source.width())) {
                    break;
                }

                cur = cur.transform(direction.getFaceLocation());
                LinkedList<Position> list = paths.computeIfAbsent(position, pos -> {
                    boundaries.add(position);
                    return new LinkedList<>();
                });

                list.add(cur);
                paths.put(position, list);
            }
        }

        if (boundaries.isEmpty()) return;

        source.setFixingInside(true);
        Position random = boundaries.get(0);//RandomUtils.random(boundaries);
        source.movement.addPath(new Path(paths.get(random)));
    }

    public static Position[] getBoundaries(Interactable interactable) {
        int nextSlot = 0;
        int width = interactable.width();
        int length = interactable.length();
        Position[] boundaries = new Position[2 * (width + length)];
        for (int y = 0; y < length + 2; y++) {
            for (int x = 0; x < width + 2; x++) {
                int xx = x % (width + 1);
                int yy = y % (length + 1);
                if (xx == 0 && yy == 0 || xx != 0 && yy != 0) continue;
                boundaries[nextSlot++] = interactable.getPosition().transform(x - 1, y - 1, 0);
            }
        }
        return boundaries;
    }

    public static Position[] getInnerBoundaries(Position position, int width, int length) {
        int nextSlot = 0;
        Position[] boundaries = new Position[width * length];
        for (int y = 0; y < length; y++) {
            for (int x = 0; x < width; x++) {
                boundaries[nextSlot++] = position.transform(x, y, 0);
            }
        }
        return boundaries;
    }

    public static Position[] getInnerBoundaries(Interactable interactable) {
        int nextSlot = 0;
        int width = interactable.width();
        int length = interactable.length();
        Position[] boundaries = new Position[width * length];
        for (int y = 0; y < length; y++) {
            for (int x = 0; x < width; x++) {
                boundaries[nextSlot++] = interactable.getPosition().transform(x, y, 0);
            }
        }
        return boundaries;
    }

    public static String formatName(final String input) {
        if(input == null) {
            return null;
        }
        //Open chagpt
        StringBuilder formattedName = new StringBuilder();

        // Split the input string by spaces
        String[] words = input.trim().split(" ");

        // Capitalize the first letter of each word
        for (String word : words) {
            if (!word.isEmpty()) {
                // Capitalize the first letter
                char firstLetter = Character.toUpperCase(word.charAt(0));
                formattedName.append(firstLetter);

                // Append the remaining letters
                if (word.length() > 1) {
                    formattedName.append(word.substring(1));
                }

                // Append a space
                formattedName.append(" ");
            }
        }

        // Remove the trailing space and return the formatted name
        return formattedName.toString().trim();
    }

    public static boolean within(Position source, Position target, int distance) {
        Interactable interactableSource = Interactable.create(source);
        Interactable interactableTarget = Interactable.create(target);
        return within(interactableSource, interactableTarget, distance);
    }

    public static String formatText(String s) {
        for (int i = 0; i < s.length(); i++) {
            if (i == 0) {
                s = String.format("%s%s", Character.toUpperCase(s.charAt(0)),
                        s.substring(1));
            }
            if (!Character.isLetterOrDigit(s.charAt(i))) {
                if (i + 1 < s.length()) {
                    s = String.format("%s%s%s", s.subSequence(0, i + 1),
                            Character.toUpperCase(s.charAt(i + 1)),
                            s.substring(i + 2));
                }
            }
        }
        return s.replace("_", " ");
    }

    public static boolean within(Interactable source, Interactable target, int distance) {
        return within(source.getPosition(), source.width(), source.length(), target.getPosition(), target.width(), target.length(), distance);
    }

    public static boolean withinOctal(Interactable source, Interactable target, int distance) {
        return withinOctal(source.getPosition(), source.width(), source.length(), target.getPosition(), target.width(), target.length(), distance);
    }

    public static boolean withinOctal(Position source, int sourceWidth, int sourceLength, Position target, int targetWidth, int targetLength, int distance) {
        if (target.getHeight() != source.getHeight()) {
            return false;
        }
        Position sourceTopRight = source.transform(sourceWidth - 1, sourceLength - 1);
        Position targetTopRight = target.transform(targetWidth - 1, targetLength - 1);
        int dx, dy;
        if (sourceTopRight.getX() < target.getX()) {
            dx = Math.abs(target.getX() - sourceTopRight.getX());
        } else if (source.getX() > targetTopRight.getX()) {
            dx = Math.abs(targetTopRight.getX() - source.getX());
        } else {
            dx = 0;
        }
        if (sourceTopRight.getY() < target.getY()) {
            dy = Math.abs(target.getY() - sourceTopRight.getY());
        } else if (source.getY() > targetTopRight.getY()) {
            dy = Math.abs(targetTopRight.getY() - source.getY());
        } else {
            dy = 0;
        }
        return dx <= distance && dy <= distance;
    }

    public static boolean within(Position source, int sourceWidth, int sourceLength, Position target, int targetWidth, int targetLength, int distance) {
        if (target.getHeight() != source.getHeight()) {
            return false;
        }
        Position sourceTopRight = source.transform(sourceWidth - 1, sourceLength - 1);
        Position targetTopRight = target.transform(targetWidth - 1, targetLength - 1);
        int dx, dy;
        if (sourceTopRight.getX() < target.getX()) {
            dx = Math.abs(target.getX() - sourceTopRight.getX());
        } else if (source.getX() > targetTopRight.getX()) {
            dx = Math.abs(targetTopRight.getX() - source.getX());
        } else {
            dx = 0;
        }
        if (sourceTopRight.getY() < target.getY()) {
            dy = Math.abs(target.getY() - sourceTopRight.getY());
        } else if (source.getY() > targetTopRight.getY()) {
            dy = Math.abs(targetTopRight.getY() - source.getY());
        } else {
            dy = 0;
        }
        return dx + dy <= distance;
    }

    public static boolean withinViewingDistance(Interactable source, Interactable target, int radius) {
        if (source == null || target == null) {
            return false;
        }

        if (target.getHeight() != source.getHeight()) {
            return false;
        }
        Position sourceTopRight = source.getPosition().transform(source.width() - 1, source.length() - 1);
        Position targetTopRight = target.getPosition().transform(target.width() - 1, target.length() - 1);
        int dx, dy;
        if (sourceTopRight.getX() < target.getX()) {
            dx = Math.abs(target.getX() - sourceTopRight.getX());
        } else if (source.getX() > targetTopRight.getX()) {
            dx = Math.abs(targetTopRight.getX() - source.getX());
        } else {
            dx = 0;
        }
        if (sourceTopRight.getY() < target.getY()) {
            dy = Math.abs(target.getY() - sourceTopRight.getY());
        } else if (source.getY() > targetTopRight.getY()) {
            dy = Math.abs(targetTopRight.getY() - source.getY());
        } else {
            dy = 0;
        }
        return dx <= radius && dy <= radius;
    }

    public static boolean inside(Interactable source, Interactable target) {
        return inside(source.getPosition(), source.width(), source.length(), target.getPosition(), target.width(), target.length());
    }

    public static boolean inside(Interactable source, Position target) {
        return inside(source.getPosition(), source.width(), source.length(), target, 1, 1);
    }

    public static boolean inside(Position source, int sourceWidth, int sourceLength, Position target, int targetWidth, int targetLength) {
        if (sourceWidth <= 0) sourceWidth = 1;
        if (sourceLength <= 0) sourceLength = 1;
        if (targetWidth <= 0) targetWidth = 1;
        if (targetLength <= 0) targetLength = 1;
        Position sourceTopRight = source.transform(sourceWidth - 1, sourceLength - 1, 0);
        Position targetTopRight = target.transform(targetWidth - 1, targetLength - 1, 0);
        if (source.equals(target) || sourceTopRight.equals(targetTopRight)) {
            return true;
        }
        if (source.getX() > targetTopRight.getX() || sourceTopRight.getX() < target.getX()) {
            return false;
        }
        return source.getY() <= targetTopRight.getY() && sourceTopRight.getY() >= target.getY();
    }

    public static boolean checkRequirements(Player player, int[] requirements, String action) {
        boolean can = true;
        for (int index = 0; index < requirements.length; index++) {
            int level = player.skills.getMaxLevel(index);
            int required = requirements[index];

            if (level < required) {
                player.send(new SendMessage("You need a level of " + required + " " + Skill.getName(index) + " to " + action));
                can = false;
            }
        }
        return can;
    }

    public static boolean isLarger(Interactable source, Interactable other) {
        return source.width() * source.length() > other.width() * other.length();
    }

    public static int getStarters(String host) {
        int amount = 0;
        try {
            File file = new File("./data/starters/" + host + ".txt");
            if (!file.exists()) {
                return 0;
            }
            BufferedReader in = new BufferedReader(new FileReader(file));

            String whatever = in.readLine();

            long max = Long.parseLong(whatever);

            if (max > Integer.MAX_VALUE) {
                amount = 2;
            } else {
                amount = (int) max;
            }

            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return amount;
    }

    public static boolean setStarter(Player player) {
        String host = player.lastHost;

        int amount = getStarters(host);

        if (amount >= 2) {
            return false;
        }

        if (amount == 0) {
            amount = 1;
        } else if (amount == 1) {
            amount = 2;
        }

        try {
            File file = new File("./data/starters/" + host + ".txt");
            BufferedWriter out = new BufferedWriter(new FileWriter(file, false));
            out.write(String.valueOf(amount));
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public static int[] shuffleArray(int[] array) {
        int[] shuffledArray = new int[array.length];
        System.arraycopy(array, 0, shuffledArray, 0, array.length);

        for (int i = shuffledArray.length - 1; i > 0; i--) {
            int index = RANDOM.nextInt(i + 1);
            int a = shuffledArray[index];
            shuffledArray[index] = shuffledArray[i];
            shuffledArray[i] = a;
        }
        return shuffledArray;
    }

    public static int getCurrentDay() {
        return Calendar.getInstance().get(Calendar.DAY_OF_YEAR);
    }

    public static int min(int... values) {
        int i = Integer.MAX_VALUE;
        for(int i2 : values)
            if(i2 < i)
                i = i2;
        return i;
    }

    public static final boolean goodDistance(int objectX, int objectY, int playerX, int playerY, int distance) {
        int deltaX = objectX - playerX;
        int deltaY = objectY - playerY;
        int trueDistance = ((int) Math.sqrt(Math.pow(deltaX, 2) + Math.pow(deltaY, 2)));
        return trueDistance <= distance;
    }

    public static boolean inRange(int absX, int absY, int size, int targetX, int targetY, int targetSize, int distance) {
        if (absX < targetX) {
            /*
             * West of target
             */
            int closestX = absX + (size - 1);
            int diffX = targetX - closestX;
            if (diffX > distance)
                return false;
        } else if (absX > targetX) {
            /*
             * East of target
             */
            int closestTargetX = targetX + (targetSize - 1);
            int diffX = absX - closestTargetX;
            if (diffX > distance)
                return false;
        }
        if (absY < targetY) {
            /*
             * South of target
             */
            int closestY = absY + (size - 1);
            int diffY = targetY - closestY;
            return diffY <= distance;
        } else if (absY > targetY) {
            /*
             * North of target
             */
            int closestTargetY = targetY + (targetSize - 1);
            int diffY = absY - closestTargetY;
            return diffY <= distance;
        }
        return true;
    }

    public static boolean inRange(Interactable source, Interactable target, int distance) {
        return inRange(source.getX(), source.getY(), source.width(), target.getX(), target.getY(), target.width(), distance);
    }

}
