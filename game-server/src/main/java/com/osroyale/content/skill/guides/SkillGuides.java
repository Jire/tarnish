package com.osroyale.content.skill.guides;

import com.osroyale.game.world.entity.mob.player.Player;
import com.osroyale.game.world.items.Item;
import com.osroyale.net.packet.out.SendInterface;
import com.osroyale.net.packet.out.SendItemOnInterface;
import com.osroyale.net.packet.out.SendScrollbar;
import com.osroyale.net.packet.out.SendString;
import com.osroyale.util.Utility;

public class SkillGuides {

    public static final int INTERFACE_ID = 42550;
    private static final int TITLE_ID = 42557;
    private static final int CATEGORY_ID = 42558;
    private static final int RIGHT_OPTIONS_START = 42559;
    private static final int SCROLL_LAYER = 42573;
    private static final int ITEMS_ID = 42574;
    private static final int TEXT_LAYER_START = 42575;


    public static void openInterface(Player player, int skillId, int optionId, boolean update) {
        if(player.menuOpened == skillId && player.optionOpened == optionId) return;

        GuideData data = GuideData.values()[skillId];

        player.send(new SendString(Utility.capitalizeSentence(data.name().toLowerCase()), TITLE_ID));
        player.send(new SendString(data.options[optionId], CATEGORY_ID));

        int menuLength = data.options.length, length = data.menus[optionId].option.length;
        for(int index = 0; index < menuLength; index++)
            player.send(new SendString(data.options[index], RIGHT_OPTIONS_START + index));


        Item[] items = new Item[length];
        for(int i = 0; i < length; i++)
            items[i] = new Item(data.menus[optionId].option[i].itemId);

        for(int i = 0, id = TEXT_LAYER_START; i < length; i++, id += 2) {
            String level = data.menus[optionId].option[i].level == -1 ? "" : data.menus[optionId].option[i].level + "";
            System.out.println("id: " + id);
            System.out.println("level: " + level);
            System.out.println("data.menus[optionId].option[i].name: " + data.menus[optionId].option[i].name);
            player.send(new SendString(data.menus[optionId].option[i].name, id));
            player.send(new SendString(level, id + 1));
        }

        player.send(new SendItemOnInterface(ITEMS_ID, items));

        cleanInterface(player, menuLength, length);

        int scrollMax = 1;
        if(length == 58) scrollMax = ((length + 3) * 33) + 1;
        else if(length >= 7) scrollMax = (length * 33) + 1;
        player.send(new SendScrollbar(SCROLL_LAYER, scrollMax));

        player.menuOpened = skillId;
        player.optionOpened = optionId;

        if(!update) player.send(new SendInterface(INTERFACE_ID));
    }

    private static void cleanInterface(Player player, int menuLength, int start) {
        /**
         * Cleans the right side
         */
        for(int index = menuLength; index < 14; index++) {
            System.out.println("RIGHT_OPTIONS_START + index: " + (RIGHT_OPTIONS_START + index));
            player.send(new SendString("", RIGHT_OPTIONS_START + index));
        }
        //for(int index = 2, child = RIGHT_OPTIONS_START + menuLength; index < 12; index++, child++)
        //    player.send(new SendString("", child));

        if(start < 7) {
            for (int index = 0, id = TEXT_LAYER_START + (2 * start); index < 7; index++, id += 2) {
                player.send(new SendString("", id));
                player.send(new SendString("", id + 1));
            }
        }
        /*for(int index = 0, id = TEXT_LAYER_START + (2 * length) + 1; index < 7; index++, id += 2) {
            player.send(new SendString("", id));
            player.send(new SendString("", id + 1));
        }*/
    }

}