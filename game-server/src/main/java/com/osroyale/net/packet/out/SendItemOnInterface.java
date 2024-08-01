package com.osroyale.net.packet.out;

import com.osroyale.game.world.InterfaceConstants;
import com.osroyale.game.world.entity.mob.player.Player;
import com.osroyale.game.world.items.Item;
import com.osroyale.net.packet.OutgoingPacket;
import com.osroyale.net.packet.PacketType;

public class SendItemOnInterface extends OutgoingPacket {

	private final int id;
	private final Item[] items;
	private final int[] tabAmounts;

	public SendItemOnInterface(int id) {
		this(id, null, new Item[]{});
	}

	public SendItemOnInterface(int id, Item... items) {
		this(id, null, items);
	}

	public SendItemOnInterface(int id, int[] tabAmounts, Item... items) {
		super(53, PacketType.VAR_SHORT);
		this.id = id;
		this.items = items;
		this.tabAmounts = tabAmounts;
	}

	@Override
	public boolean encode(Player player) {
		final boolean sendTabs = id == InterfaceConstants.WITHDRAW_BANK && tabAmounts != null;
		builder.writeInt(id)
				.writeShort(items.length)
				.writeShort(sendTabs ? tabAmounts.length : 0);
		for (final Item item : items) {
			if (item != null) {
				final int amount = item.getAmount();
				if (amount > 254) {
					builder.writeByte(255)
							.writeInt(amount);
				} else {
					builder.writeByte(amount);
				}
				builder.writeShort(item.getId() + 1);
			} else {
				builder.writeByte(0)
						.writeShort(0);
			}
		}

		if (sendTabs) {
			for (final int amount : tabAmounts) {
				builder.writeInt(amount);
			}
		}
		return true;
	}
}
