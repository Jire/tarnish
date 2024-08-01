package com.osroyale.content.dialogue;

import com.osroyale.net.packet.out.SendChatBoxInterface;
import com.osroyale.net.packet.out.SendItemModelOnInterface;
import com.osroyale.net.packet.out.SendMoveComponent;
import com.osroyale.net.packet.out.SendString;
import com.osroyale.game.world.entity.mob.player.Player;
import com.osroyale.game.world.items.Item;
import com.osroyale.game.world.items.ItemDefinition;

public abstract class ChatBoxItemDialogue {
    private final Player player;

    protected ChatBoxItemDialogue(Player player) {
        this.player = player;
    }

    public static void sendInterface(Player player, int interfaceId, Item item, int zoom) {
        sendInterface(player, interfaceId, item.getId(), 0, 10, zoom);
    }

    public static void sendInterface(Player player, int interfaceId, int item, int zoom) {
        sendInterface(player, interfaceId, item, 0, 10, zoom);
    }

    public static void sendInterface(Player player, int interfaceId, int item, int x, int y, int zoom) {
        player.send(new SendChatBoxInterface(4429));
        player.send(new SendMoveComponent(x, y, interfaceId));
        player.send(new SendItemModelOnInterface(interfaceId, zoom, item));
        player.send(new SendString("\\n \\n \\n \\n" + ItemDefinition.get(item).getName(), 2799));
    }

    public boolean clickButton(int button) {
        switch (button) {
        /* Option 1 */
            case 2799:
                firstOptionClick(player);
                return true;

		/* Option 5 */
            case 2798:
                secondOptionClick(player);
                return true;

		/* Option x */
            case 1748:
                thirdOptionClick(player);
                return true;

		/* Option all */
            case 1747:
                fourthOptionClick(player);
                return true;
        }
        return false;
    }

    public abstract void firstOption(Player player);

    public abstract void secondOption(Player player);

    public abstract void thirdOption(Player player);

    public abstract void fourthOption(Player player);

    private void firstOptionClick(Player player) {
        player.interfaceManager.close();
        firstOption(player);
    }

    private void secondOptionClick(Player player) {
        player.interfaceManager.close();
        secondOption(player);
    }

    private void thirdOptionClick(Player player) {
        player.interfaceManager.close();
        thirdOption(player);
    }

    private void fourthOptionClick(Player player) {
        player.interfaceManager.close();
        fourthOption(player);
    }

}