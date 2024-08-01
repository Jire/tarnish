package com.osroyale;

final class TextInput {

	static String decode(int len, Buffer buffer) {
		int length = 0;

		for (int i = 0; i < len; i++) {
			int index = buffer.readUnsignedByte();// recieved from server
			CHAR_BUFFER[length++] = CHAR_TABLE[index];
		}

		boolean doUpperCaseNext = true;

		for (int i = 0; i < length; i++) {
			char key = CHAR_BUFFER[i];

			if (doUpperCaseNext && (key >= 'a') && (key <= 'z')) {
				CHAR_BUFFER[i] += '\uFFE0';
				doUpperCaseNext = false;
			}

			if ((key == '.') || (key == '!') || (key == '?')) {
				doUpperCaseNext = true;
			}
		}

		return new String(CHAR_BUFFER, 0, length);
	}

	static void encode(String string, Buffer buffer) {
		if (string.length() > 100) {
			string = string.substring(0, 100).toLowerCase();
		} else {
			string = string.toLowerCase();
		}

		for (int i = 0; i < string.length(); i++) {
			char key = string.charAt(i);
			int index = 0;

			for (int n = 0; n < CHAR_TABLE.length; n++) {
				if (key != CHAR_TABLE[n]) {
					continue;
				}

				index = n;
				break;
			}

			buffer.writeByte(index);
		}
	}

	static String processText(String string) {
		STREAM.position = 0;
		encode(string, STREAM);
		int length = STREAM.position;
		STREAM.position = 0;
		return decode(length, STREAM);
	}

	private static final char[] CHAR_BUFFER = new char[100];
	private static final Buffer STREAM = new Buffer(new byte[100]);

	private static final char[] CHAR_TABLE = {' ', 'e', 't', 'a', 'o', 'i', 'h', 'n', 's', 'r', 'd', 'l', 'u', 'm', 'w', 'c', 'y', 'f', 'g', 'p', 'b', 'v', 'k', 'x', 'j', 'q', 'z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', ' ', '!', '?', '.', ',', ':', ';', '(', ')', '-', '&', '*', '\\', '\'', '@', '#', '+', '=', '\243', '$', '%', '"', '[', ']', '_', '/', '<', '>', '^', '|'};

}
