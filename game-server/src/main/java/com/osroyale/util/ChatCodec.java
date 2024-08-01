package com.osroyale.util;

import com.osroyale.game.world.entity.mob.player.relations.ChatMessage;

import java.nio.ByteBuffer;

/**
 * A utility class for encoding/decoding messages.
 *
 * @author nshusa
 */
public final class ChatCodec {

    /**
     * The maximum size of an encoded message.
     */
    private static final int MAX_COMPRESS_LEN = 80;

    /**
     * The table that holds all the valid characters that can be used.
     * These characters are in frequency order. The characters with lower
     * indexes are used more often.
     */
    private static final char[] CHARACTER_TABLE = {
            ' ', 'e', 't', 'a', 'o', 'i', 'h', 'n', 's', 'r',
            'd', 'l', 'u', 'm', 'w', 'c', 'y', 'f', 'g', 'p',
            'b', 'v', 'k', 'x', 'j', 'q', 'z', '0', '1', '2',
            '3', '4', '5', '6', '7', '8', '9', ' ', '!', '?',
            '.', ',', ':', ';', '(', ')', '-', '&', '*', '\\',
            '\'', '@', '#', '+', '=', '\243', '$', '%', '"',
            '[', ']', '_', '/', '<', '>', '^', '|'
    };

    /**
     * Prevent instantiation
     */
    private ChatCodec() {

    }

    /**
     * Encodes an {@code input} string.
     *
     * @param input
     *      The input text that will be encoded.
     *
     * @return The encoded string.
     */
    public static byte[] encode(String input) {
        if (input.length() > ChatMessage.CHARACTER_LIMIT) {
            input = input.substring(0, ChatMessage.CHARACTER_LIMIT).toLowerCase();
        } else {
            input = input.toLowerCase();
        }

        ByteBuffer buffer = ByteBuffer.allocate(ChatMessage.CHARACTER_LIMIT);

        int count = 0;
        for (int i = 0; i < input.length(); i++) {
            char key = input.charAt(i);
            int index = 0;

            for (int n = 0; n < CHARACTER_TABLE.length; n++) {
                if (key != CHARACTER_TABLE[n]) {
                    continue;
                }

                index = n;
                count++;
                break;
            }

            buffer.put((byte) index);
        }

        final byte[] temp = new byte[count];
        System.arraycopy(buffer.array(), 0, temp, 0, temp.length);

        return temp;
    }

    /**
     * Decodes an {@code input} string.
     *
     * @param input
     *      The string that will be decoded.
     *
     * @return The decoded string.
     */
    public static String decode(byte[] input) {
        if (input.length > MAX_COMPRESS_LEN) {
            byte[] temp = new byte[MAX_COMPRESS_LEN];
            System.arraycopy(input, 0, temp, 0, temp.length);
            input = temp;
        }

        final ByteBuffer buffer = ByteBuffer.wrap(input);
        final StringBuilder builder = new StringBuilder();
        for (int i = 0; i < buffer.capacity(); i++) {
            final int index = buffer.get() & 0xFF;
            if (index < CHARACTER_TABLE.length) {
                builder.append(CHARACTER_TABLE[index]);
            }
        }
        return filter(builder.toString());
    }

    /**
     * Filters out invalid characters from the {@code input} string and formats the text.
     *
     * @code input
     *      The string to filter.
     *
     * @return The filtered text.
     */
    public static String filter(String input) {
        final StringBuilder builder = new StringBuilder();
        for (char c : input.toLowerCase().toCharArray()) {
            for (char validChar : CHARACTER_TABLE) {
                if (c == validChar) {
                    builder.append(c);
                    break;
                }
            }
        }

        boolean capitalize = true;

        int length = builder.length();

        for (int index = 0; index < length; index++) {
            char character = builder.charAt(index);

            if (character == '.' || character == '!' || character == '?') {
                capitalize = true;
            } else if (capitalize && !Character.isWhitespace(character)) {
                builder.setCharAt(index, Character.toUpperCase(character));
                capitalize = false;
            } else {
                builder.setCharAt(index, Character.toLowerCase(character));
            }
        }

        return builder.toString();
    }

}
