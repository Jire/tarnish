package com.osroyale.net.packet.in;

import com.osroyale.content.event.EventDispatcher;
import com.osroyale.content.event.impl.ItemOnItemInteractionEvent;
import com.osroyale.content.itemaction.ItemActionRepository;
import com.osroyale.game.event.impl.ItemOnItemEvent;
import com.osroyale.game.plugin.PluginManager;
import com.osroyale.game.world.entity.mob.data.PacketType;
import com.osroyale.game.world.entity.mob.player.Player;
import com.osroyale.game.world.entity.mob.player.PlayerRight;
import com.osroyale.game.world.items.Item;
import com.osroyale.game.world.items.containers.inventory.Inventory;
import com.osroyale.net.codec.ByteModification;
import com.osroyale.net.codec.ByteOrder;
import com.osroyale.net.packet.ClientPackets;
import com.osroyale.net.packet.GamePacket;
import com.osroyale.net.packet.PacketListener;
import com.osroyale.net.packet.PacketListenerMeta;
import com.osroyale.net.packet.out.SendMessage;
import com.osroyale.util.MessageColor;
import org.jire.tarnishps.event.npc.ItemOnNpcEvent;
import org.jire.tarnishps.event.object.ItemOnObjectEvent;
import org.jire.tarnishps.event.player.ItemOnPlayerEvent;

/**
 * The {@link GamePacket}'s responsible for an items "use" option.
 *
 * @author Daniel
 */
@PacketListenerMeta({ClientPackets.ITEM_ON_NPC, ClientPackets.ITEM_ON_ITEM, ClientPackets.ITEM_ON_OBJECT, ClientPackets.ITEM_ON_GROUND_ITEM, ClientPackets.ITEM_ON_PLAYER})
public class UseItemPacketListener implements PacketListener {

    @Override
    public void handlePacket(Player player, GamePacket packet) {
        if (player.isDead())
            return;
        if (player.locking.locked(PacketType.USE_ITEM))
            return;

        switch (packet.getOpcode()) {
            case ClientPackets.ITEM_ON_GROUND_ITEM:
                handleItemOnGround(player, packet);
                break;
            case ClientPackets.ITEM_ON_ITEM:
                handleItemOnItem(player, packet);
                break;
            case ClientPackets.ITEM_ON_NPC:
                handleItemOnNpc(player, packet);
                break;
            case ClientPackets.ITEM_ON_OBJECT:
                handleItemOnObject(player, packet);
                break;
            case ClientPackets.ITEM_ON_PLAYER:
                handleItemOnPlayer(player, packet);
                break;
        }
    }

    /**
     * Handles an randomevent when a player uses the "Use" option of an item
     * with an item on the ground.
     */
    private void handleItemOnGround(Player player, GamePacket packet) {
        final int a1 = packet.readShort(ByteOrder.LE);
        final int itemUsed = packet.readShort(false, ByteModification.ADD);
        final int groundItem = packet.readShort();
        final int gItemY = packet.readShort(false, ByteModification.ADD);
        final int itemUsedSlot = packet.readShort(ByteOrder.LE, ByteModification.ADD);
        final int gItemX = packet.readShort();

        if (PlayerRight.isDeveloper(player)) {
            player.send(new SendMessage("[ItemUsed] - " + itemUsed + " groundItem: " + groundItem + " itemUsedSlot: " + itemUsedSlot + " gItemX: " + gItemX + " gItemY: " + gItemY + " a1: " + a1, MessageColor.DEVELOPER));
        }

        player.send(new SendMessage("Nothing interesting happens."));
    }

    /**
     * Handles an randomevent when a player uses the "Use" option of an item
     * with another item in a players inventory.
     */
    private void handleItemOnItem(Player player, GamePacket packet) {
        final int usedWithSlot = packet.readShort();
        final int itemUsedSlot = packet.readShort(ByteModification.ADD);
        final Item used = player.inventory.get(itemUsedSlot);
        final Item with = player.inventory.get(usedWithSlot);

        if (used != null && with != null) {
            if (EventDispatcher.execute(player, new ItemOnItemInteractionEvent(used, with, usedWithSlot, itemUsedSlot))) {
                return;
            }

            if (ItemActionRepository.itemOnItem(player, used, with)) {
                return;
            }

            if (PluginManager.getDataBus().publish(player, new ItemOnItemEvent(used, itemUsedSlot, with, usedWithSlot))) {
                return;
            }

            player.send(new SendMessage("Nothing interesting happens."));
        }
    }

    /**
     * Handles an randomevent when a player uses the "Use" option of an item
     * with an in-game npc.
     */
    private void handleItemOnNpc(Player player, GamePacket packet) {
        final int itemId = packet.readShort(false, ByteModification.ADD);
        final int index = packet.readShort(false, ByteModification.ADD);
        final int slot = packet.readShort(ByteOrder.LE);

        player.getEvents().interact(player, new ItemOnNpcEvent(itemId, index, slot));
    }

    /**
     * Handles an randomevent when a player uses the "Use" option of an item
     * with an in-game object.
     */
    private void handleItemOnObject(Player player, GamePacket packet) {
        int interfaceType = packet.readShort();
        final int objectId = packet.readShort(ByteOrder.LE);
        final int y = packet.readShort(ByteOrder.LE, ByteModification.ADD);
        final int slot = packet.readShort(ByteOrder.LE);
        final int x = packet.readShort(ByteOrder.LE, ByteModification.ADD);
        final int itemId = packet.readShort();

        player.getEvents().interact(player, new ItemOnObjectEvent(
                interfaceType, itemId, slot, objectId, x, y
        ));
    }

    /**
     * Handles an randomevent when a player uses the "Use" option of an item
     * with an in-game player.
     */
    private void handleItemOnPlayer(Player player, GamePacket packet) {
        final int interfaceId = packet.readShort(ByteModification.ADD);
        final int slot = packet.readShort();
        final int item = packet.readShort();
        final int itemSlot = packet.readShort(ByteOrder.LE);

        if (interfaceId != Inventory.INVENTORY_DISPLAY_ID) {
            return;
        }

        player.getEvents().interact(player, new ItemOnPlayerEvent(
                interfaceId, item, itemSlot, slot
        ));
    }

}
