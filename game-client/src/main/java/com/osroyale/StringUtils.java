package com.osroyale;

import java.text.NumberFormat;
import java.util.Locale;

public final class StringUtils {

	public static long longForName(String s) {
		long l = 0L;
		for (int i = 0; i < s.length() && i < 12; i++) {
			char c = s.charAt(i);
			l *= 37L;
			if (c >= 'A' && c <= 'Z')
				l += (1 + c) - 65;
			else if (c >= 'a' && c <= 'z')
				l += (1 + c) - 97;
			else if (c >= '0' && c <= '9')
				l += (27 + c) - 48;
		}

		for (; l % 37L == 0L && l != 0L; l /= 37L)
			;
		return l;
	}

	public static String nameForLong(long l) {
		try {
			if (l <= 0L || l >= 0x5b5b57f8a98a5dd1L)
				return "invalid_name";
			if (l % 37L == 0L)
				return "invalid_name";
			int i = 0;
			char ac[] = new char[12];
			while (l != 0) {
				long l1 = l;
				l /= 37L;
				ac[11 - i++] = validChars[(int) (l1 - l * 37L)];
			}
			String value = new String(ac, 12 - i, i);
			ac = null;
			return value;
		} catch (RuntimeException runtimeexception) {
			Utility.reporterror("81570, " + l + ", " + (byte) -99 + ", " + runtimeexception.toString());
		}
		throw new RuntimeException();
	}

	public static long method585(String s) {
		s = s.toUpperCase();
		long l = 0L;
		for (int i = 0; i < s.length(); i++) {
			l = (l * 61L + (long) s.charAt(i)) - 32L;
			l = l + (l >> 56) & 0xffffffffffffffL;
		}
		return l;
	}

	public static String method586(int i) {
		return (i >> 24 & 0xff) + "." + (i >> 16 & 0xff) + "." + (i >> 8 & 0xff) + "." + (i & 0xff);
	}

	public static String fixName(String name) {
		if (name.length() > 0) {
			char first = name.charAt(0);

			StringBuilder fixed = new StringBuilder("" + Character.toUpperCase(first));

			for (int index = 1; index < name.length(); index++) {
				char character = name.charAt(index);

				if (character == '_') {
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

	public static String toAsterisks(String s) {
		StringBuilder result = new StringBuilder();
		for (int j = 0; j < s.length(); j++) {
			result.append("*");
		}
		return result.toString();
	}

	public static String format(int n) {
		return NumberFormat.getInstance(Locale.US).format(n);
	}

	private static final char[] validChars = {'_', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};

}
