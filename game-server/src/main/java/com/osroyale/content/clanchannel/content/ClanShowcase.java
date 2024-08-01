package com.osroyale.content.clanchannel.content;

import com.osroyale.content.clanchannel.channel.ClanChannel;
import com.osroyale.content.clanchannel.channel.ClanChannelHandler;
import com.osroyale.game.world.entity.mob.player.Player;
import com.osroyale.game.world.items.Item;
import com.osroyale.game.world.items.ItemDefinition;
import com.osroyale.net.packet.out.SendItemOnInterface;
import com.osroyale.net.packet.out.SendMessage;
import com.osroyale.net.packet.out.SendString;

import java.util.ArrayList;
import java.util.List;

public class ClanShowcase {
    private final ClanChannel channel;
    public int[] showcase = new int[3];
    public List<Integer> showcaseItems = new ArrayList<>(28);
    private int currentItem = -1, currentSlot = -1;
    private int showcaseSlot;

    public ClanShowcase(ClanChannel channel) {
        this.channel = channel;
    }

    public void openShowcase(Player player, int slot) {
        Item[] showcase = new Item[28];
        for (int index = 0; index < showcaseItems.size(); index++) {
            showcase[index] = new Item(showcaseItems.get(index));
        }
        player.send(new SendString(channel.getName() + "'s Showcase", 57702));
        player.send(new SendString(showcaseItems.size() + "/28", 57718));
        player.send(new SendItemOnInterface(57716, showcase));
        player.send(new SendItemOnInterface(57717));
        showcaseSlot = slot;
        currentSlot = -1;
        currentItem = -1;
        player.interfaceManager.open(57700);
    }

    public void select(Player player, int item, int slot) {
        if (!player.interfaceManager.isInterfaceOpen(57700) || slot < 0 || slot >= showcaseItems.size())
            return;

        int id = showcaseItems.get(slot);
        if (item == id) {
            currentSlot = slot;
            currentItem = id;
            player.send(new SendItemOnInterface(57717, new Item(id)));
        }
    }

    public void set(Player player) {
        if (!player.interfaceManager.isInterfaceOpen(57700) || showcaseSlot < 0 || showcaseSlot >= 3)
            return;

        if (currentSlot == -1 || currentItem == -1) {
            player.send(new SendMessage("You should select an item first."));
            return;
        }

        showcase[showcaseSlot] = currentItem;
        ClanChannelHandler.manage(player);
        Item[] showcase = new Item[28];
        for (int index = 0; index < showcaseItems.size(); index++) {
            showcase[index] = new Item(showcaseItems.get(index));
        }
        player.send(new SendItemOnInterface(57716));
        player.send(new SendItemOnInterface(57716, showcase));
        player.send(new SendMessage("You have successfully changed your showcase."));
    }

    public void remove(Player player) {
        if (!player.interfaceManager.isInterfaceOpen(57700))
            return;

        if (currentSlot == -1 || currentItem == -1) {
            player.send(new SendMessage("You should select an item first."));
            return;
        }

        if (showcaseItems.size() <= 3) {
            player.send(new SendMessage("You need a minimum of 3 showcase items. This action can not be performed."));
            return;
        }

        player.dialogueFactory.sendStatement(
            "Are you sure you want to delete <col=225>" + ItemDefinition.get(currentItem).getName() + "</col>?",
            "Once this action is performed it can not be undone!")
            .sendOption("Yes", () -> {
                showcaseItems.remove(currentSlot);
                openShowcase(player, currentItem);
                player.send(new SendMessage("You have successfully removed that item from your showcase."));
            }, "Nevermind", () -> player.dialogueFactory.clear()).execute();
    }
}
