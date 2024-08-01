package com.osroyale.rsinterfaces;

import com.osroyale.Client;
import com.osroyale.RSInterface;
import com.osroyale.TextDrawingArea;

public class KeepsakeOverrides extends RSInterface {
    //TODO: clean code up lol, pushing now so can get client out
    public static void init(TextDrawingArea[] tda) {
        RSInterface tab = addTabInterface(60106);
        addSprite(60107, 145);
        addHoverButton(60109, 24, 15, 15, "Close", 250, 60110, 3);
        addHoveredButton(60110, 25, 15, 15, 60111);
        addHoverButton(60141, 840, 20, 20, "Manage Overrides", -1, 60142, 1);
        addHoveredButton(60142, 841, 20, 20, 60143);
        addHoverButton(60250, 77, 16, 16, "Override Info", -1, 60251, 1);
        addHoveredButton(60251, 78, 16, 16, 60252);
        addHoverText(60108, "View All Overrides", "View All Overrides", tda, 0, 0xFFB83F, 0xFFFFFF, false, true, 85);

        addText(60115, "Keepsake Overrides", tda, 2, 0xFF981F, false, true);
        addChar(60120, 650);
        addText(60116, "Select an equipment\\nslot above to view\\navailable overrides", tda, 0, 0xFF981F, true, true);
        addText(60117, "", tda, 0, 0xFF981F, true, true);
        addText(60118, "", tda, 0, 0xFF981F, true, true);
        addRectangle(60112, 100, 0, false, 154, 33);

        addHoverText(60114, "Save as Preset", "Save Override Preset", tda, 0, 0xFFB83F, 0xFFFFFF, false, true, 85);
        addHoverText(60119, "View Presets", "View Presets", tda, 0, 0xFFB83F, 0xFFFFFF, true, true, 85);

        addText(60113, "All Overrides", tda, 2, 0xFF981F, true, true);

        addConfigButton(60121, tab.parentID, 3622, 3621, 36, 36, "Select", 1, 1, 1300);
        addConfigButton(60122, tab.parentID, 3623, 3621, 36, 36, "Select", 1, 1, 1301);
        addConfigButton(60123, tab.parentID, 3624, 3621, 36, 36, "Select", 1, 1, 1302);
        addConfigButton(60125, tab.parentID, 3626, 3621, 36, 36, "Select", 1, 1, 1303);
        addConfigButton(60126, tab.parentID, 3627, 3621, 36, 36, "Select", 1, 1, 1304);
        addConfigButton(60127, tab.parentID, 3628, 3621, 36, 36, "Select", 1, 1, 1305);
        addConfigButton(60128, tab.parentID, 3629, 3621, 36, 36, "Select", 1, 1, 1306);
        addConfigButton(60129, tab.parentID, 3631, 3621, 36, 36, "Select", 1, 1, 1307);
        addConfigButton(60130, tab.parentID, 3630, 3621, 36, 36, "Select", 1, 1, 1308);

        //add as regular sprites as the models for these equip. slots aren't shown, therefore no overrides
        addSprite(60124, 3625); //ammo slot
        addSprite(60140, 3632); //ring slot

        addContainer(60131, RSInterface.interfaceCache[5064].width, RSInterface.interfaceCache[5064].height, RSInterface.interfaceCache[5064].invSpritePadX, RSInterface.interfaceCache[5064].invSpritePadY, false, false, "Remove", null, null, null, null);
        addContainer(60132, RSInterface.interfaceCache[5064].width, RSInterface.interfaceCache[5064].height, RSInterface.interfaceCache[5064].invSpritePadX, RSInterface.interfaceCache[5064].invSpritePadY, false, false, "Remove", null, null, null, null);
        addContainer(60133, RSInterface.interfaceCache[5064].width, RSInterface.interfaceCache[5064].height, RSInterface.interfaceCache[5064].invSpritePadX, RSInterface.interfaceCache[5064].invSpritePadY, false, false, "Remove", null, null, null, null);
        addContainer(60134, RSInterface.interfaceCache[5064].width, RSInterface.interfaceCache[5064].height, RSInterface.interfaceCache[5064].invSpritePadX, RSInterface.interfaceCache[5064].invSpritePadY, false, false, "Remove", null, null, null, null);
        addContainer(60135, RSInterface.interfaceCache[5064].width, RSInterface.interfaceCache[5064].height, RSInterface.interfaceCache[5064].invSpritePadX, RSInterface.interfaceCache[5064].invSpritePadY, false, false, "Remove", null, null, null, null);
        addContainer(60136, RSInterface.interfaceCache[5064].width, RSInterface.interfaceCache[5064].height, RSInterface.interfaceCache[5064].invSpritePadX, RSInterface.interfaceCache[5064].invSpritePadY, false, false, "Remove", null, null, null, null);
        addContainer(60137, RSInterface.interfaceCache[5064].width, RSInterface.interfaceCache[5064].height, RSInterface.interfaceCache[5064].invSpritePadX, RSInterface.interfaceCache[5064].invSpritePadY, false, false, "Remove", null, null, null, null);
        addContainer(60138, RSInterface.interfaceCache[5064].width, RSInterface.interfaceCache[5064].height, RSInterface.interfaceCache[5064].invSpritePadX, RSInterface.interfaceCache[5064].invSpritePadY, false, false, "Remove", null, null, null, null);
        addContainer(60139, RSInterface.interfaceCache[5064].width, RSInterface.interfaceCache[5064].height, RSInterface.interfaceCache[5064].invSpritePadX, RSInterface.interfaceCache[5064].invSpritePadY, false, false, "Remove", null, null, null, null);

        tab.totalChildren(38);
        tab.child(0, 60107, 15, 5);
        tab.child(1, 60109, 474, 14);
        tab.child(2, 60110, 474, 14);
        tab.child(33, 60141, 471, 44);
        tab.child(34, 60142, 471, 44);
        tab.child(35, 60108, 30, 16);
        tab.child(36, 60250, 457, 14);
        tab.child(37, 60251, 457, 14);
        tab.child(25, 60114, 220, 295);
        tab.child(3, 60115, 195, 13);
        tab.child(4, 60120, 188, 175);
        tab.child(26, 60119, 377, 253);
        tab.child(5, 60116, 417, 277);
        tab.child(6, 60117, 420, 300);
        tab.child(7, 60118, 420, 305);
        tab.child(23, 60113, 99, 48);
        tab.child(8, 60112, 23, 40);
        tab.child(9, 60121, 398, 50);
        tab.child(10, 60122, 358, 90);
        tab.child(11, 60123, 398, 90);
        tab.child(12, 60124, 438, 90);
        tab.child(13, 60125, 343, 130);
        tab.child(14, 60126, 398, 130);
        tab.child(15, 60127, 453, 130);
        tab.child(16, 60128, 398, 170);
        tab.child(17, 60129, 398, 210);
        tab.child(18, 60130, 343, 210);
        tab.child(19, 60140, 453, 210);
        tab.child(21, 60131, 400, 50);
        tab.child(22, 60132, 360, 90);
        tab.child(24, 60133, 400, 90);
        tab.child(27, 60134, 345, 130);
        tab.child(28, 60135, 400, 130);
        tab.child(29, 60136, 455, 130);
        tab.child(30, 60137, 400, 170);
        tab.child(31, 60138, 345, 210);
        tab.child(32, 60139, 400, 210);


        //Options layer
        int optionLayer = 60144;
        int interfaceId = 60145;
        int subIndex = 0;
        RSInterface options = addTabInterface(optionLayer);
        options.width = 140;
        options.height = 245;
        options.scrollMax = 750;

        options.totalChildren(2 * 50);

        for (int i = 0, y2 = 0; i < 50; ++i, y2 += 15) {
            addButton(interfaceId, Client.spriteCache.get(i % 2 == 0 ? 1732 : 1736), Client.spriteCache.get(i % 2 == 0 ? 1732 : 1736), "Select", 140, 15);
            //addConfigButton(interfaceId, options.parentID, i % 2 == 0 ? 1732 : 1736, 1737, 140, 15, "Select", i, 1, 0, false);
            interfaceCache[interfaceId].hoveredSprite = Client.spriteCache.get(1737);
            options.child(subIndex++, interfaceId++, 0, y2);

            addText(interfaceId, "", tda, 1, 0xff981f, false, true);
            options.child(subIndex++, interfaceId++, 1, y2 - 2);
        }
        tab.child(20, optionLayer, 23, 75);

        infoInterface(tda);
    }

    private static void infoInterface(TextDrawingArea[] tda) {
        RSInterface rsi = addTabInterface(60260);

        addSprite(60261, 478);
        addText(60262, "Keepsake Override Information", 0xff9933, true, true, -1, tda, 2);
        addText(60263, "How Overrides Work", 0xff9933, true, true, -1, tda, 1);
        addText(60264, "Using a keepsake key on an item will consume the item and add it to\\nyour available overrides. You can then equip overrides to have your" +
                "\\ncurrent gear overriden. Overrides are purely cosmetic, and do not\\naffect the stats of your worn gear.", 0xFF981F, true, true, -1, tda, 0);
        addText(60265, "Items cannot be reclaimed once keepsaked.", 0xFF0000, true, true, -1, tda, 1);
        addHoverButton(60266, 391, 118, 32, "Close", -1, 60267, 1);
        addHoveredButton(60267, 392, 118, 32, 60268);
        addHoverButton(60269, 391, 118, 32, "Go Back", -1, 60270, 1);
        addHoveredButton(60270, 392, 118, 32, 60271);
        addText(60272, "Close", 0xFFB83F, true, true, -1, tda, 1);
        addText(60273, "Go Back", 0xFFB83F, true, true, -1, tda, 1);

        rsi.totalChildren(11);
        rsi.child(0, 60261, 8, 7);
        rsi.child(1, 60262, 255, 16);
        rsi.child(2, 60263, 255, 107);
        rsi.child(3, 60264, 255, 127);
        rsi.child(4, 60265, 255, 175);
        rsi.child(5, 60266, 114, 206);
        rsi.child(6, 60267, 114, 206);
        rsi.child(7, 60269, 278, 206);
        rsi.child(8, 60270, 278, 206);
        rsi.child(9, 60272, 173, 211);
        rsi.child(10, 60273, 338, 211);
    }

}