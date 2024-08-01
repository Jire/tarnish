package com.osroyale;


/**
 * Handles the developer terminal console.
 *
 * @author Daniel
 * @author Zion
 */
public class Console {

	/**
	 * The index of the last command that was executed.
	 */
	private int commandIndex;

	/**
	 * The console being opened flag.
	 */
	public boolean openConsole;

	/**
	 * The render time stamp flag.
	 */
	public boolean displayTime;

	/**
	 * The console input string.
	 */
	public String consoleInput;

	/**
	 * The console messages.
	 */
	public final String[] terminal;

	/**
	 * The terminal command prefix.
	 */
	private String TERMINAL_COMMAND_PREFIX = (displayTime ? "[" + Utility.getTime() + "] " : "") + "-> <col=ffffff>";

	/**
	 * Constructs a new developer terminal console.
	 */
	public Console() {
		this.openConsole = false;
		this.displayTime = false;
		this.terminal = new String[17];
		clear();
	}

	/**
	 * Handles the clearing of the developer terminal console.
	 */
	public final void clear() {
		commandIndex = 0;
		consoleInput = "";
		for (int index = 0; index < terminal.length; index++) {
			terminal[index] = null;
		}
		terminal[4] = "<img=3>Welcome to the developer terminal.";
		terminal[3] = " - Type \"clear\" to clear the developer terminal.";
		terminal[2] = " - Press \"page up\" or \"page down\" to navigate through the terminal commands.";
		terminal[1] = " - Type \"time\" to render the time stamp of each executed command.";
	}

	/**
	 * Draws the developer terminal console.
	 *
	 * @param width The width of the console.
	 */
	public void drawConsole(int width) {
		if (openConsole) {
			Rasterizer2D.fillRectangle(0, 0, width, 334, 0x406AA1, 185);
			Rasterizer2D.drawPixels(1, 334 - 21, 0, 0xffffff, width);
			String title = Utility.formatName(Client.instance.myUsername) + "@Tarnish:";
			Client.instance.newBoldFont.drawBasicString(title + " ", 5, 334 - 6, 0xACB0B5, 0);
			Client.instance.newBoldFont.drawBasicString("<col=ffffff>" + consoleInput + (Client.tick % 20 < 10 ? "|" : ""), Client.instance.newBoldFont.getTextWidth(title) + 8, 334 - 5, 0xffffff, 0);
			for (int index = 0, messageY = 308; index < 17; index++, messageY -= 18) {
				String msg = terminal[index];
				if (msg != null && msg.length() > 0) {
					Client.instance.newSmallFont.drawBasicString(msg, 5, messageY, 0xACB0B5, 0);
				}
			}
		}
	}

	/**
	 * Handles the console commands.
	 *
	 * @param consoleCommand The console command being executed.
	 */
	public void sendConsoleCommands(String consoleCommand) {
		if (consoleCommand.equalsIgnoreCase("clear")) {
			clear();
			return;
		}
		if (consoleCommand.equalsIgnoreCase("time")) {
			displayTime = !displayTime;
			return;
		}

		Client.instance.outgoing.writeOpcode(103);
		Client.instance.outgoing.writeByte(consoleCommand.length() + 1);
		Client.instance.outgoing.writeString(consoleCommand);
		commandIndex = -1;
	}


	/**
	 * Handles choosing the developer command.
	 *
	 * @param previous The previous flag.
	 */
	public void chooseCommand(boolean previous) {
		String next = null;

		if (previous) {
			do {
				if (commandIndex + 1 < terminal.length) {
					next = terminal[++commandIndex];
					if (next == null) {
						commandIndex--;
						break;
					}
				} else
					break;
			} while (!next.startsWith(TERMINAL_COMMAND_PREFIX));
		} else {
			do {
				if (commandIndex - 1 >= 0) {
					next = terminal[--commandIndex];
					if (next == null) {
						commandIndex++;
						break;
					}
				} else
					break;
			} while (!next.startsWith(TERMINAL_COMMAND_PREFIX));
		}

		if (next != null) {
			consoleInput = next.substring(TERMINAL_COMMAND_PREFIX.length());
		}
	}

	/**
	 * Sends a console message.
	 *
	 * @param message The message to send.
	 */
	public void sendConsoleMessage(String message) {
		if (Client.instance.backDialogueId == -1)
			Client.redrawDialogueBox = true;
		System.arraycopy(terminal, 0, terminal, 1, 16);
		message = TERMINAL_COMMAND_PREFIX + message;
		terminal[0] = message;
	}
}

