package com.osroyale.content;

import com.osroyale.net.packet.out.SendItemOnInterface;
import com.osroyale.net.packet.out.SendString;
import com.osroyale.game.world.entity.mob.player.Player;
import com.osroyale.game.world.entity.mob.prayer.Prayer;
import com.osroyale.game.world.items.Item;
import com.osroyale.game.world.items.ItemComparator;
import com.osroyale.game.world.items.containers.pricechecker.PriceType;
import com.osroyale.util.Utility;

import java.math.BigInteger;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.PriorityQueue;
import java.util.Queue;

public class ItemsKeptOnDeath {

	public static void open(Player player) {
		int kept = 3;
		if (player.skulling.isSkulled()) {
			kept = player.prayer.isActive(Prayer.PROTECT_ITEM) ? 1 : 0;
		} else {
			kept = player.prayer.isActive(Prayer.PROTECT_ITEM) ? 4 : 3;
		}

		final Item[] keep = new Item[kept];

		final Queue<Item> items = new PriorityQueue<Item>(ItemComparator.SHOP_VALUE_COMPARATOR);

		for (final Item item : player.inventory.toArray()) {
			if (item != null) {
				items.add(item.copy());
			}
		}

		for (final Item item : player.equipment.toArray()) {
			if (item != null) {
				items.add(item.copy());
			}
		}

		final Queue<Item> temp = new PriorityQueue<>(items);

		for (int index = 0, taken = 0; index < keep.length; index++) {
			keep[index] = temp.poll();
			items.remove(keep[index]);

			if (keep[index] != null) {
				if (keep[index].getAmount() == keep.length - taken) {
					break;
				}

				if (keep[index].getAmount() > keep.length - taken) {
					items.add(new Item(keep[index].getId(), keep[index].getAmount() - (keep.length - taken)));
					keep[index].setAmount(keep.length - taken);
					break;
				}

				taken += keep[index].getAmount();
			}
		}

		player.send(new SendString("~ " + kept + " ~", 17112));

		switch (kept) {
		case 0:
		default:
			player.send(new SendString("You're marked with a \\n<col=ff0000>skull. </col>This reduces the \\nitems you keep from \\nthree to zero!", 17110));
			break;
		case 1:
			player.send(new SendString("You're marked with a \\n<col=ff0000>skull. </col>This reduces the \\nitems you keep from \\nthree to zero! \\nHowever, you also have the \\n<col=ff0000>Protect </col>Items prayer \\nactive, which saves you \\none extra item!", 17110));
			break;
		case 3:
			player.send(new SendString("You have no factors affecting \\nthe items you keep.", 17110));
			break;
		case 4:
			player.send(new SendString("You have the <col=ff0000>Protect Item</col> \\nprayer active, which saves \\nyou none extra item!", 17110));
			break;
		}

		long carried = (player.inventory.containerValue(PriceType.VALUE) + player.equipment.containerValue(PriceType.VALUE));
		
		if (carried <= 0) {
			player.send(new SendString("Carried wealth: \\n<col=ff0000>Nothing!", 17115));
		} else if (carried >= Long.MAX_VALUE) {
			player.send(new SendString("Carried wealth: \\n<col=ff0000>Too much!", 17115));
		} else {
			player.send(new SendString("Carried wealth: \\n<col=ff0000>" + Utility.formatDigits(carried) + "</col> coins.", 17115));
		}

		Item[] dropped = items.toArray(new Item[0]);

		BigInteger risked = BigInteger.ZERO;
		while (items.peek() != null) {
			final Item dropping = items.poll();

			if (dropping == null) {
				continue;
			}

			risked = risked.add(new BigInteger(String.valueOf(dropping.getValue(PriceType.VALUE))).multiply(new BigInteger(String.valueOf(dropping.getAmount()))));
		}

		player.send(new SendString("Risked wealth:", 17124));

		if (risked.equals(BigInteger.ZERO)) {
			player.send(new SendString("Risked wealth: \\n<col=ff0000>Nothing!", 17116));
		} else {
			player.send(new SendString("Risked wealth: \\n<col=ff0000>" + NumberFormat.getNumberInstance(Locale.US).format(risked) + "</col> coins.", 17116));
		}

		player.send(new SendItemOnInterface(17113, keep));
		player.send(new SendItemOnInterface(17114, dropped));
		player.interfaceManager.open(17100);
	}

}
