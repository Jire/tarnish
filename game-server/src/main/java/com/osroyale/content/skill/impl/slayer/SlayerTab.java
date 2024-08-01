package com.osroyale.content.skill.impl.slayer;

import com.osroyale.game.world.entity.mob.player.Player;
import com.osroyale.game.world.items.Item;
import com.osroyale.net.packet.out.*;
import com.osroyale.util.Utility;

import java.util.Arrays;
import java.util.List;

/**
 * Enum for the slayer tab.
 * 
 * @author Daniel
 */
public enum SlayerTab {
	/** The main itemcontainer - all the general information. */
	MAIN(46700),

	/** The duo itemcontainer - all the duo information. */
	DUO(46800),

	/** The unlock itemcontainer - holds all the unlockable perks. */
	UNLOCK(46900),

	/** The reward itemcontainer - holds all the rewards for slayer points. */
	REWARD(46500),

	/** The tasks itemcontainer - displays all the available task for player. */
	TASK(46300),

	/** The confirm itemcontainer - keeps player confirmation on purchase. */
	CONFIRM(46400);

	/** The itemcontainer identification. */
	private final int identification;

	/** Constructs a new <code>SlayerTab<code>.  */
	SlayerTab(int identification) {
		this.identification = identification;
	}

	/** Gets the itemcontainer identification.  */
	public int getIdentification() {
		return identification;
	}

	/** Refreshes the tab itemcontainer. */
	public static void refresh(Player player, SlayerTab tab) {
		if (tab == null) {
			return;
		}

		SlayerTask task = player.slayer.getTask();
		switch (tab) {

		case MAIN:
			setFrame(player, 0);
//			player.send(new SendNpcDisplay(task == null ? 2044 : task.getNpc()[0], task == null ? 300 : task.getSize()));
			player.send(new SendString(task == null ? "" : "</col>Name: <col=ffffff>" + task.getName(), 46716));
			player.send(new SendString(task == null ? "" : "</col>Level: <col=ffffff>" + task.getCombatLevel(), 46717));
			player.send(new SendString(task == null ? "You do not have a task assigned!" : "</col>Assigned: <col=ffffff>" + player.slayer.getAssigned(), 46718));
			player.send(new SendString(task == null ? "" : "</col>Remaining: <col=ffffff>" + player.slayer.getAmount(), 46735));
			player.send(new SendString(task == null ? "" : "</col>Position: <col=ffffff>" + task.getLocation(), 46736));
			player.send(new SendString("Tasks assigned:\\n<col=ffffff>" + player.slayer.getTotalAssigned(), 46737));
			player.send(new SendString("Tasks completed:\\n<col=ffffff>" + player.slayer.getTotalCompleted(), 46738));
			player.send(new SendString("Tasks Cancelled:\\n<col=ffffff>" + player.slayer.getTotalCancelled(), 46739));
			player.send(new SendString("Accumulated points:\\n<col=ffffff>" + player.slayer.getTotalPoints(), 46740));

			for (int index = 0, string = 46753; index < 5; index++, string += 3) {
				player.send(new SendTooltip("", string - 1));
				player.send(new SendString("", string));
			}

			for (int index = 0, string = 46753; index < player.slayer.getBlocked().size(); index++, string += 3) {
				String name = player.slayer.getBlocked().get(index).getName();
				player.send(new SendTooltip("Unblock <col=FF981F>" + name + "</col>", string - 1));
				player.send(new SendString(name, string));
			}

			player.send(new SendString(Utility.formatDigits(player.slayer.getPoints()) + "\\nPoints", 46714));
			player.send(new SendString("Blocked tasks:  (" + player.slayer.getBlocked().size() + "/5)", 46728));
			break;

		case DUO:
			setFrame(player, 1);
			player.send(new SendString("Current slayer partner: None!", 46811));
			break;

		case UNLOCK:
			setFrame(player, 2);
			int string = 46911;
			for (SlayerUnlockable unlockable : SlayerUnlockable.values()) {
				player.send(new SendTooltip("Purchase <col=FF981F>" + unlockable.getName() + "</col>", string));
				string++;
				player.send(new SendString(unlockable.getName(), string));
				string++;
				player.send(new SendString(unlockable.getDescription(), string));
				string++;
				player.send(new SendString(unlockable.getCost() + " points", string));
				string++;
				player.send(new SendConfig(560 + unlockable.ordinal(), player.slayer.getUnlocked().contains(unlockable) ? 0 : 1));
				string++;
				player.send(new SendItemOnInterfaceSlot(string, new Item(unlockable.getItem(), 1), 0));
				string++;
			}
			player.send(new SendScrollbar(46910, 310));
			break;

		case REWARD:
			player.send(new SendItemOnInterface(46503, Slayer.ITEMS));
			setFrame(player, 3);
			break;

		case CONFIRM:
			SlayerUnlockable unlockable = player.attributes.get("SLAYER_CONFIRM_KEY", SlayerUnlockable.class);
			player.send(new SendString(unlockable.getName(), 46403));
			player.send(new SendString(unlockable.getDescription(), 46404));
			player.send(new SendString("Pay " + Utility.formatDigits(unlockable.getCost()) + " points?", 46405));

			Item[] items = new Item[3];
			for (int index = 0; index < 3; index++) {
				items[index] = new Item(unlockable.getItem());
				items[index].setAmount(0);
			}

			player.send(new SendItemOnInterface(46414, items));
			break;

		case TASK:
			List<SlayerTask> task_list;
			task_list = Arrays.asList(SlayerTask.values());
			int line = 46321;

			for (int index = 0; index < 35; index++) {
				player.send(new SendString("", line + index));
			}

			for (SlayerTask tasks : task_list) {
				line++;
				player.send(new SendString(tasks.getName(), line));
				line++;
				player.send(new SendString(tasks.getCombatLevel(), line));
				line++;
				player.send(new SendString(tasks.getLevel(), line));
				line++;
			}

			player.send(new SendScrollbar(46320, 400));
			break;
		}
	}

	/** Sets the tab frame.  */
	private static void setFrame(Player player, int index) {
		player.send(new SendConfig(710, index));
		player.send(new SendString("<col=" + (index == 0 ? "FF981F" : "ff9933") + ">Main", 46710));
		player.send(new SendString("<col=" + (index == 1 ? "FF981F" : "ff9933") + ">Duo", 46711));
		player.send(new SendString("<col=" + (index == 2 ? "FF981F" : "ff9933") + ">Unlocks", 46712));
		player.send(new SendString("<col=" + (index == 3 ? "FF981F" : "ff9933") + ">Store", 46713));
	}
}
