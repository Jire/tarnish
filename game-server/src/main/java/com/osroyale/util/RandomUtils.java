package com.osroyale.util;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * A static-util class that provides additional functionality for generating pseudo-random numbers. All functions in this
 * class are backed by {@link ThreadLocalRandom} rather than the more commonly used {@link Random}. It is generally preferred
 * to use this over {@code Random} because although {@code Random} is thread safe; the same seed is shared concurrently,
 * which leads to contention between multiple threads and overhead as a result. Surprisingly because of the way that {@code
 * ThreadLocalRandom} works, even in completely single-threaded situations it runs up to three times faster than {@code
 * Random}.
 *
 * @author lare96 <http://github.com/lare96>
 * @see <a href= "http://java-performance.info/java-util-random-java-util-concurrent-threadlocalrandom-multithreaded-environments/"
 * >java.util.Random and java.util.concurrent.ThreadLocalRandom in multithreaded environments</a>
 */
public final class RandomUtils {

    /**
     * A private constructor to discourage external instantiation.
     */
    private RandomUtils() {
    }

    /**
     * Returns a pseudo-random {@code boolean}.
     *
     * @return The pseudo-random {@code boolean}.
     */
    public static boolean nextBoolean() {
        return ThreadLocalRandom.current().nextBoolean();
    }

    /**
     * Returns a pseudo-random {@code double}.
     *
     * @return The pseudo-random {@code double}.
     */
    public static double nextDouble() {
        return ThreadLocalRandom.current().nextDouble();
    }

    /**
     * Returns a pseudo-random {@code float} between inclusive {@code 0} and
     * exclusive {@code range}.
     *
     * @param range The exclusive range.
     * @return The pseudo-random {@code float}.
     * @exception IllegalArgumentException If the specified range is less than {@code 0}.
     */
    public static float floatRandom(float range) {
        if (range < 0F)
            throw new IllegalArgumentException("range <= 0");
        return ThreadLocalRandom.current().nextFloat() * range;
    }

    /**
     * Returns a pseudo-random {@code int} value between inclusive {@code min} and inclusive {@code max}.
     *
     * @param min The minimum inclusive number.
     * @param max The maximum inclusive number.
     * @return The pseudo-random {@code int}.
     * @exception IllegalArgumentException If {@code max - min + 1} is less than {@code 0}.
     */
    public static int inclusive(int min, int max) {
        if (min == max)
            return min;
        checkArgument(max >= min, "max < min");
        return ThreadLocalRandom.current().nextInt((max - min) + 1) + min;
    }

    /**
     * Returns a pseudo-random {@code int} value between inclusive {@code min}
     * and inclusive {@code max}.
     *
     * @param min The minimum inclusive number.
     * @param max The maximum inclusive number.
     * @return The pseudo-random {@code int}.
     * @exception IllegalArgumentException If {@code max - min + 1} is less than
     *                                     {@code 0}.
     */
    public static double inclusive(double min, double max) {
        if (min == max)
            return min;
        checkArgument(max >= min, "max < min");
        return ThreadLocalRandom.current().nextDouble(max - min) + min;
    }

    /**
     * Returns a pseudo-random {@code int} value between inclusive {@code 0} and inclusive {@code range}.
     *
     * @param range The maximum inclusive number.
     * @return The pseudo-random {@code int}.
     * @exception IllegalArgumentException If {@code max - min + 1} is less than {@code 0}.
     */
    public static int inclusive(int range) {
        return inclusive(0, range);
    }

    /**
     * Pseudo-randomly retrieves a element from {@code array}.
     *
     * @param array The array to retrieve an element from.
     * @return The element retrieved from the array.
     */
    public static <T> T random(T[] array) {
        return array[(int) (ThreadLocalRandom.current().nextDouble() * array.length)];
    }

    /**
     * Pseudo-randomly retrieves an {@code int} from this {@code array}.
     *
     * @param array The array to retrieve an {@code int} from.
     * @return The {@code int} retrieved from the array.
     */
    public static int random(int... array) {
        return array[(int) (ThreadLocalRandom.current().nextDouble() * array.length)];
    }

    /**
     * Pseudo-randomly retrieves an {@code long} from this {@code array}.
     *
     * @param array The array to retrieve an {@code long} from.
     * @return The {@code long} retrieved from the array.
     */
    public static long random(long... array) {
        return array[(int) (ThreadLocalRandom.current().nextDouble() * array.length)];
    }

    /**
     * Pseudo-randomly retrieves an {@code double} from this {@code array}.
     *
     * @param array The array to retrieve an {@code double} from.
     * @return The {@code double} retrieved from the array.
     */
    public static double random(double... array) {
        return array[(int) (ThreadLocalRandom.current().nextDouble() * array.length)];
    }

    /**
     * Pseudo-randomly retrieves an {@code short} from this {@code array}.
     *
     * @param array The array to retrieve an {@code short} from.
     * @return The {@code short} retrieved from the array.
     */
    public static short random(short... array) {
        return array[(int) (ThreadLocalRandom.current().nextDouble() * array.length)];
    }

    /**
     * Pseudo-randomly retrieves an {@code byte} from this {@code array}.
     *
     * @param array The array to retrieve an {@code byte} from.
     * @return The {@code byte} retrieved from the array.
     */
    public static byte random(byte... array) {
        return array[(int) (ThreadLocalRandom.current().nextDouble() * array.length)];
    }

    /**
     * Pseudo-randomly retrieves an {@code float} from this {@code array}.
     *
     * @param array The array to retrieve an {@code float} from.
     * @return The {@code float} retrieved from the array.
     */
    public static float random(float... array) {
        return array[(int) (ThreadLocalRandom.current().nextDouble() * array.length)];
    }

    /**
     * Pseudo-randomly retrieves an {@code boolean} from this {@code array}.
     *
     * @param array The array to retrieve an {@code boolean} from.
     * @return The {@code boolean} retrieved from the array.
     */
    public static boolean random(boolean... array) {
        return array[(int) (ThreadLocalRandom.current().nextDouble() * array.length)];
    }

    /**
     * Pseudo-randomly retrieves an {@code char} from this {@code array}.
     *
     * @param array The array to retrieve an {@code char} from.
     * @return The {@code char} retrieved from the array.
     */
    public static char random(char... array) {
        return array[(int) (ThreadLocalRandom.current().nextDouble() * array.length)];
    }

    /**
     * Pseudo-randomly retrieves a element from {@code list}.
     *
     * @param list The list to retrieve an element from.
     * @return The element retrieved from the list.
     */
    public static <T> T random(List<T> list) {
        if (list.isEmpty()) return null;
        return list.get((int) (ThreadLocalRandom.current().nextDouble() * list.size()));
    }

    /**
     * An implementation of the Fisher-Yates shuffle algorithm that will shuffle the elements of an {@code T} array.
     *
     * @param array The array that will be shuffled.
     * @return The shuffled array.
     */
    public static <T> T[] shuffle(T[] array) {
        for (int i = array.length - 1; i > 0; i--) {
            int index = ThreadLocalRandom.current().nextInt(i + 1);
            T a = array[index];
            array[index] = array[i];
            array[i] = a;
        }
        return array;
    }

    /**
     * An implementation of the Fisher-Yates shuffle algorithm that will shuffle the elements of an {@code int} array.
     *
     * @param array The array that will be shuffled.
     * @return The shuffled array.
     */
    public static int[] shuffle(int[] array) {
        for (int i = array.length - 1; i > 0; i--) {
            int index = ThreadLocalRandom.current().nextInt(i + 1);
            int a = array[index];
            array[index] = array[i];
            array[i] = a;
        }
        return array;
    }

    /**
     * An implementation of the Fisher-Yates shuffle algorithm that will shuffle the elements of an {@code long} array.
     *
     * @param array The array that will be shuffled.
     * @return The shuffled array.
     */
    public static long[] shuffle(long[] array) {
        for (int i = array.length - 1; i > 0; i--) {
            int index = ThreadLocalRandom.current().nextInt(i + 1);
            long a = array[index];
            array[index] = array[i];
            array[i] = a;
        }
        return array;
    }

    /**
     * An implementation of the Fisher-Yates shuffle algorithm that will shuffle the elements of an {@code double} array.
     *
     * @param array The array that will be shuffled.
     * @return The shuffled array.
     */
    public static double[] shuffle(double[] array) {
        for (int i = array.length - 1; i > 0; i--) {
            int index = ThreadLocalRandom.current().nextInt(i + 1);
            double a = array[index];
            array[index] = array[i];
            array[i] = a;
        }
        return array;
    }

    /**
     * An implementation of the Fisher-Yates shuffle algorithm that will shuffle the elements of an {@code short} array.
     *
     * @param array The array that will be shuffled.
     * @return The shuffled array.
     */
    public static short[] shuffle(short[] array) {
        for (int i = array.length - 1; i > 0; i--) {
            int index = ThreadLocalRandom.current().nextInt(i + 1);
            short a = array[index];
            array[index] = array[i];
            array[i] = a;
        }
        return array;
    }

    /**
     * An implementation of the Fisher-Yates shuffle algorithm that will shuffle the elements of an {@code byte} array.
     *
     * @param array The array that will be shuffled.
     * @return The shuffled array.
     */
    public static byte[] shuffle(byte[] array) {
        for (int i = array.length - 1; i > 0; i--) {
            int index = ThreadLocalRandom.current().nextInt(i + 1);
            byte a = array[index];
            array[index] = array[i];
            array[i] = a;
        }
        return array;
    }

    /**
     * An implementation of the Fisher-Yates shuffle algorithm that will shuffle the elements of an {@code float} array.
     *
     * @param array The array that will be shuffled.
     * @return The shuffled array.
     */
    public static float[] shuffle(float[] array) {
        for (int i = array.length - 1; i > 0; i--) {
            int index = ThreadLocalRandom.current().nextInt(i + 1);
            float a = array[index];
            array[index] = array[i];
            array[i] = a;
        }
        return array;
    }

    /**
     * An implementation of the Fisher-Yates shuffle algorithm that will shuffle the elements of an {@code boolean} array.
     *
     * @param array The array that will be shuffled.
     * @return The shuffled array.
     */
    public static boolean[] shuffle(boolean[] array) {
        for (int i = array.length - 1; i > 0; i--) {
            int index = ThreadLocalRandom.current().nextInt(i + 1);
            boolean a = array[index];
            array[index] = array[i];
            array[i] = a;
        }
        return array;
    }

    /**
     * An implementation of the Fisher-Yates shuffle algorithm that will shuffle the elements of an {@code char} array.
     *
     * @param array The array that will be shuffled.
     */
    public static char[] shuffle(char[] array) {
        for (int i = array.length - 1; i > 0; i--) {
            int index = ThreadLocalRandom.current().nextInt(i + 1);
            char a = array[index];
            array[index] = array[i];
            array[i] = a;
        }
        return array;
    }

    /**
     * Returns a pseudo-random {@code int} value between inclusive {@code min}
     * and inclusive {@code max} excluding the specified numbers within the
     * {@code excludes} array.
     *
     * @param min the minimum inclusive number.
     * @param max the maximum inclusive number.
     * @return the pseudo-random {@code int}.
     * @exception IllegalArgumentException if {@code max - min + 1} is less than {@code 0}.
     */
    public static int inclusiveExcludes(int min, int max, int... exclude) {
        Arrays.sort(exclude);

        int result = inclusive(min, max);
        while (Arrays.binarySearch(exclude, result) >= 0) {
            result = inclusive(min, max);
        }

        return result;
    }

    /**
     * Returns a pseudo-random {@code int} value between inclusive {@code min}
     * and inclusive {@code max} excluding the specified numbers within the
     * {@code excludes} array.
     *
     * @return the pseudo-random {@code int}.
     * @exception IllegalArgumentException if {@code max - min + 1} is less than {@code 0}.
     */
    public static <T> T randomExclude(T[] array, T exclude) {
        T result = random(array);
        while (exclude.equals(result))
            result = random(array);
        return result;
    }

    /**
     * Determines if a pseudorandomly generated double rounded to two decimal
     * places is below or equal to {@code value}.
     *
     * @param value the value to determine this for.
     * @return {@code true} if successful, {@code false} otherwise.
     */
    public static boolean success(double value) {
        return value >= 1.00 || ThreadLocalRandom.current().nextDouble() <= value;
    }

}