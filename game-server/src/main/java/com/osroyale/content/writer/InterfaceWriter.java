package com.osroyale.content.writer;

import com.osroyale.net.packet.out.SendColor;
import com.osroyale.net.packet.out.SendFont;
import com.osroyale.net.packet.out.SendString;
import com.osroyale.game.world.entity.mob.player.Player;

/**
 * Handles writing on an itemcontainer.
 * 
 * @author Daniel
 *
 */
public abstract class InterfaceWriter {

	public InterfaceWriter(Player player) {
		this.player = player;
	}
	
	protected Player player;
	
	protected abstract int startingIndex();
	
	protected abstract String[] text();
	
	protected abstract int[][] color();
	
	protected abstract int[][] font();

	public void scroll() {

	}
	
	public static void write(InterfaceWriter writer) {
		writer.scroll();

		/* Sends the text */
		int line = writer.startingIndex();
		if (writer.text() != null) {
			for (int index = 0; index < writer.text().length; index++) {
				writer.player.send(new SendString(writer.text()[index], line++));
			}
		}
		
		/* Sends the color */
		if (writer.color() != null) {
			for (int index = 0; index < writer.color().length; index++) {
				writer.player.send(new SendColor(writer.color()[index][0], writer.color()[index][1]));
			}
		}
		
		/* Sends the font */
		if (writer.font() != null) {
			for (int index = 0; index < writer.font().length; index++) {
				writer.player.send(new SendFont(writer.font()[index][0], writer.font()[index][1]));
			}
		}
	}

}
