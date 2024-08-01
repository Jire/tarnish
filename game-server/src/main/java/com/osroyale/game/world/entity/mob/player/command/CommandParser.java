package com.osroyale.game.world.entity.mob.player.command;

import java.util.Arrays;

/**
 * @author Chex | Michael
 * Modified by nshusa
 */
public final class CommandParser {

    private final String command;
    private final String[] arguments;

    private int pointer;

    private CommandParser(String command, String input, String splitter) {
        this.command = command;
        this.arguments = Arrays.stream(input.split(splitter)).toArray(String[]::new);
    }

    public static CommandParser split(CommandParser parser, String splitter) {
        return new CommandParser(parser.getCommand(), parser.nextLine(), splitter);
    }

    public static CommandParser split(String input, String splitter) {
        final int index = input.indexOf(splitter);

        final String command = index == -1 ? input : input.substring(0, index);

        return split(command, input, splitter);
    }

    public static CommandParser split(String command, String input, String splitter) {

        int position = input.indexOf(command);

        if (position == -1) {
            position = 0;
        }

        final String string = input.substring(position + command.length()).trim();

        return new CommandParser(command, string, splitter);
    }

    public String getCommand() {
        return command;
    }

    public boolean hasNext() {
        if (arguments.length == 1) {
            if (arguments[0].isEmpty()) {
                return false;
            }
        }
        return hasNext(1);
    }

    public boolean hasNext(int length) {
        return pointer + length <= arguments.length;
    }

    public boolean nextBoolean() {
        return Boolean.parseBoolean(nextString());
    }

    public byte nextByte() throws NumberFormatException {
        return Byte.parseByte(nextString());
    }

    public double nextDouble() throws NumberFormatException {
        return Double.parseDouble(nextString());
    }

    public int nextInt() throws NumberFormatException {
        return Integer.parseInt(nextString());
    }

    public long nextLong() throws NumberFormatException {
        return Long.parseLong(nextString());
    }

    public short nextShort() throws NumberFormatException {
        return Short.parseShort(nextString());
    }

    public String nextString() throws ArrayIndexOutOfBoundsException {
        if (pointer >= arguments.length) {
            throw new ArrayIndexOutOfBoundsException("The next argument does not exist. [Size: " + arguments.length + ", Attempted: " + pointer + "]");
        }

        return arguments[pointer++];
    }

    public String nextLine() {
        final StringBuilder builder = new StringBuilder();

        while (hasNext()) {
            builder.append(" ").append(nextString());
        }
        return builder.toString().trim();
    }

    public int argumentCount() {
        return arguments.length;
    }

    public String[] getArguments() {
        return arguments;
    }

    @Override
    public String toString() {
        return String.format("command=[%s] arguments=%s", command, arguments == null ? "none" : Arrays.toString(arguments));
    }

}