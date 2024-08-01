package com.osroyale.util;

/**
 * This class specially written to convert the given number into words. It will
 * support upto 1 crore.
 * 
 * @author SANTHOSH REDDY MANDADI
 * @version release: 2_0_48
 * @since April 03 2008
 */
public class Words {
	long num;

	private Words() {
		num = 0;
	}

	private Words(long num) {
		this.num = num;
	}

	public void setNumber(long num) {
		this.num = num;
	}

	public long getNumber() {
		return num;
	}

	public static Words getInstance(long num) {
		return new Words(num);
	}

	public static String leftChars(String str, int n) {
		if (str.length() <= n)
			return str;
		else
			return str.substring(0, n);
	}

	public static String rightChars(String str, int n) {
		if (str.length() <= n)
			return str;
		else
			return str.substring(str.length() - n);
	}

	public long leftChars(int n) {
		return Long.parseLong(leftChars(Long.toString(num), n));
	}

	public long rightChars(int n) {
		return Long.parseLong(rightChars(Long.toString(num), n));
	}

	public long leftChars(long num, int n) {
		return Long.parseLong(leftChars(Long.toString(num), n));
	}

	public long rightChars(long num, int n) {
		return Long.parseLong(rightChars(Long.toString(num), n));
	}

	public int length(long num) {
		return Long.toString(num).length();
	}

	private String belowTen(long x) {
		switch ((int) x) {
		case 1:
			return "One ";
		case 2:
			return "Two ";
		case 3:
			return "Three ";
		case 4:
			return "Four ";
		case 5:
			return "Five ";
		case 6:
			return "Six ";
		case 7:
			return "Seven ";
		case 8:
			return "Eight ";
		case 9:
			return "Nine ";
		}
		return "";
	}

	private String belowTwenty(long x) {
		if (x < 10)
			return belowTen(x);
		switch ((int) x) {
		case 10:
			return "Ten ";
		case 11:
			return "Eleven ";
		case 12:
			return "Twelve ";
		case 13:
			return "Thirteen ";
		case 14:
			return "Fourteen ";
		case 15:
			return "Fifteen ";
		case 16:
			return "Sixteen ";
		case 17:
			return "Seventeen ";
		case 18:
			return "Eighteen ";
		case 19:
			return "Nineteen ";
		}
		return "";
	}

	private String belowHundred(long x) {
		if (x < 10)
			return belowTen(x);
		else if (x < 20)
			return belowTwenty(x);
		switch ((int) leftChars(x, 1)) {
		case 2:
			return "Twenty " + belowTen(rightChars(x, 1));
		case 3:
			return "Thirty " + belowTen(rightChars(x, 1));
		case 4:
			return "Fourty " + belowTen(rightChars(x, 1));
		case 5:
			return "Fifty " + belowTen(rightChars(x, 1));
		case 6:
			return "Sixty " + belowTen(rightChars(x, 1));
		case 7:
			return "Seventy " + belowTen(rightChars(x, 1));
		case 8:
			return "Eighty " + belowTen(rightChars(x, 1));
		case 9:
			return "Ninety " + belowTen(rightChars(x, 1));
		}
		return "";
	}

	private String belowThousand(long x) {
		if (x < 10)
			return belowTen(x);
		else if (x < 20)
			return belowTwenty(x);
		else if (x < 100)
			return belowHundred(x);
		return belowTen(leftChars(x, 1)) + "Hundred " + belowHundred(rightChars(x, 2));
	}

	private String belowLakh(long x) {
		if (x < 10)
			return belowTen(x);
		else if (x < 20)
			return belowTwenty(x);
		else if (x < 100)
			return belowHundred(x);
		else if (x < 1000)
			return belowThousand(x);
		if (length(x) == 4)
			return belowTen(leftChars(x, 1)) + "Thousand " + belowThousand(rightChars(x, 3));
		else
			return belowHundred(leftChars(x, 2)) + "Thousand " + belowThousand(rightChars(x, 3));
	}

	public String belowCrore(long x) {
		if (x < 10)
			return belowTen(x);
		else if (x < 20)
			return belowTwenty(x);
		else if (x < 100)
			return belowHundred(x);
		else if (x < 1000)
			return belowThousand(x);
		else if (x < 100000)
			return belowLakh(x);
		if (length(x) == 6)
			return belowTen(leftChars(x, 1)) + "Lakh " + belowLakh(rightChars(x, 5));
		else
			return belowHundred(leftChars(x, 2)) + "Lakh " + belowLakh(rightChars(x, 5));
	}

	public String belowBillion(long x) {
		if (x < 10)
			return belowTen(x);
		else if (x < 20)
			return belowTwenty(x);
		else if (x < 100)
			return belowHundred(x);
		else if (x < 1000)
			return belowThousand(x);
		else if (x < 100000)
			return belowLakh(x);
		else if (x < 100000000)
			return belowCrore(x);

		if (length(x) == 8)
			return belowTen(leftChars(x, 1)) + "Bilion " + belowCrore(rightChars(x, 7));
		else
			return belowHundred(leftChars(x, 2)) + "Bilion " + belowCrore(rightChars(x, 7));
	}

	public String getNumberInWords() {
		if (num < 10)
			return belowTen(num);
		else if (num < 20)
			return belowTwenty(num);
		else if (num < 100)
			return belowHundred(num);
		else if (num < 1000)
			return belowThousand(num);
		else if (num < 100000)
			return belowLakh(num);
		else if (num < 10000000)
			return belowCrore(num);
		else if (num < 1000000000)
			return belowBillion(num);
		return "";
	}

}