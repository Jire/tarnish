package com.osroyale.content.itemaction.impl;

import com.osroyale.content.itemaction.ItemAction;
import com.osroyale.game.world.entity.mob.player.Player;
import com.osroyale.game.world.items.Item;

public class ClanResourceBox extends ItemAction {

    @Override
    public String name() {
        return "Clan Resource Box";
    }

    @Override
    public boolean inventory(Player player, Item item, int opcode) {
//        if (opcode != 1)
            return false;
//        Clan channel = ClanRepository.get(player.clan);
//        if (channel == null) {
//            player.send(new SendMessage("You need to be in a clan to do this!"));
//            return true;
//        }
//        player.inventory.remove(item);
//        Item showcaseReward = new Item(Utility.randomElement(ClanUtility.getRewardItems(channel)));
//        channel.showcaseItems.add(showcaseReward.getId());
//        player.send(new SendMessage("Inside the box you found " + showcaseReward.getName() + "! Showcase size: " + channel.showcaseItems.size() + "/28", SendMessage.MessageColor.DARK_PURPLE));
//        return true;
    }
}
