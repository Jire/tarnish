package com.osroyale;


import net.runelite.api.IterableHashTable;
import net.runelite.rs.api.RSItemComposition;
import net.runelite.rs.api.RSIterableNodeHashTable;
import net.runelite.rs.api.RSModel;

public final class ItemDefinition implements RSItemComposition {

    static void nullLoader() {
        mruNodes2 = null;
        mruNodes1 = null;
        offsets = null;
        cache = null;
        dataBuffer = null;
    }

    public boolean method192(int j) {
        int k = maleHeadModel;
        int l = maleHeadModel2;
        if (j == 1) {
            k = femaleHeadModel;
            l = femaleHeadModel2;
        }
        if (k == -1)
            return true;
        boolean flag = Model.isCached(k);
        if (l != -1 && !Model.isCached(l))
            flag = false;
        return flag;
    }

    public static ItemDefinition lookup(int i) {
        for (int j = 0; j < 10; j++)
            if (cache[j].id == i)
                return cache[j];

        cacheIndex = (cacheIndex + 1) % 10;
        ItemDefinition itemDef = cache[cacheIndex];
        dataBuffer.position = offsets[i];
        itemDef.id = i;
        itemDef.setDefaults();
        itemDef.readValues(dataBuffer);
        /* Customs added here? */

        switch (i) {
            case 2399://keepsake key - originally silverlight key
                itemDef.name = "Keepsake key";
                itemDef.itemActions = new String[]{null, null, null, null, "Drop"};
                break;
            case 25527://Stardust
                itemDef.itemActions = new String[5];
                itemDef.itemActions[0] = "Open-shop";
                break;
            case 9786: // Slayer skill cape
            case 9787:
                itemDef.itemActions = new String[]{null, "Wear", "Task", "Check", "Drop"};
                break;
            case 13307:
                itemDef.description = "Lovely blood money!";
                break;
            case 13190:
                itemDef.name = "$10 Donator bond";
                itemDef.itemActions = new String[5];
                itemDef.itemActions[0] = "Redeem";
                itemDef.inventoryModel = 29210;
                itemDef.modelZoom = 2300;
                itemDef.modelRotationY = 512;
                itemDef.recolorFrom = new short[]{5652, 5652, 5652, 5652, 5652, 5652, 5652};
                itemDef.recolorTo = new short[]{22464, 20416, 22451, 22181, 22449, 22305, 21435};
                break;
            case 80:
                final var whip = ItemDefinition.lookup(4151);
                itemDef.name = "Abyssal whip (starter)";
                itemDef.inventoryModel = whip.inventoryModel;
                itemDef.femaleWield = whip.femaleWield;
                itemDef.maleWield = whip.maleWield;
                itemDef.modelZoom = whip.modelZoom;
                itemDef.modelRotationX = whip.modelRotationX;
                itemDef.modelRotationY = whip.modelRotationY;
                itemDef.modelOffset1 = whip.modelOffset1;
                itemDef.modelOffset2 = whip.modelOffset2;
                itemDef.stackable = whip.stackable;
                itemDef.itemActions = new String[]{null, "Wield", "Check", null, "Destroy"};
                break;
            case 81:
                final var ags = ItemDefinition.lookup(11802);
                itemDef.name = "Armadyl godsword (starter)";
                itemDef.inventoryModel = ags.inventoryModel;
                itemDef.femaleWield = ags.femaleWield;
                itemDef.maleWield = ags.maleWield;
                itemDef.modelZoom = ags.modelZoom;
                itemDef.modelRotationX = ags.modelRotationX;
                itemDef.modelRotationY = ags.modelRotationY;
                itemDef.modelOffset1 = ags.modelOffset1;
                itemDef.modelOffset2 = ags.modelOffset2;
                itemDef.stackable = ags.stackable;
                itemDef.itemActions = new String[]{null, "Wield", "Check", null, "Destroy"};
                break;
            case 4151:
                System.out.println("ZOOM:" +itemDef.modelZoom);
                break;
            case 13191:
                itemDef.name = "$50 Donator bond";
                itemDef.itemActions = new String[5];
                itemDef.itemActions[0] = "Redeem";//where is your cache do you have any cache manager tools
                itemDef.inventoryModel = 29210;
                itemDef.modelZoom = 2300;
                itemDef.modelRotationY = 512;
                itemDef.recolorFrom = new short[]{-21568, -21568, -21568, -21568, -21568, -21568, -21568};
                itemDef.recolorTo = new short[]{22464, 20416, 22451, 22181, 22449, 22305, 21435};
                break;
            case 13192:
                itemDef.name = "$100 Donator bond";
                itemDef.itemActions = new String[5];
                itemDef.itemActions[0] = "Redeem";
                itemDef.inventoryModel = 29210;
                itemDef.modelZoom = 2300;//Do you have any armor defintion? not in client in source ye how so theres no customs on here
                itemDef.modelRotationY = 512;
                itemDef.recolorFrom = new short[]{22464, 22464, 22464, 22464, 22464, 22464, 22464};
                itemDef.recolorTo = new short[]{22464, 20416, 22451, 22181, 22449, 22305, 21435};
                break;
            case 13193:
                itemDef.name = "$200 Donator bond";
                itemDef.itemActions = new String[5];
                itemDef.itemActions[0] = "Redeem";
                itemDef.inventoryModel = 29210;
                itemDef.modelZoom = 2300;
                itemDef.modelRotationY = 512;
                itemDef.recolorFrom = new short[]{-31858, -31858, -31858, -31858, -31858, -31858, -31858};
                itemDef.recolorTo = new short[]{22464, 20416, 22451, 22181, 22449, 22305, 21435};
                break;
            case 13194:
                itemDef.name = "$500 Donator bond";
                itemDef.itemActions = new String[5];
                itemDef.itemActions[0] = "Redeem";
                itemDef.inventoryModel = 29210;
                itemDef.modelZoom = 2300;
                itemDef.modelRotationY = 512;
                itemDef.recolorFrom = new short[]{8125, 8125, 8125, 8125, 8125, 8125, 8125};
                itemDef.recolorTo = new short[]{22464, 20416, 22451, 22181, 22449, 22305, 21435};
                itemDef.certID = -1;
                itemDef.certTemplateID = -1;
                break;

            case 6798:
                itemDef.name = "Arrowhead scroll";
                itemDef.itemActions = new String[5];
                itemDef.itemActions[0] = "Unlock";
                break;
            case 6806:
                itemDef.name = "Custom title scroll";
                itemDef.itemActions = new String[5];
                itemDef.itemActions[0] = "Redeem";
                break;
            case 6799:
                itemDef.name = "Masterbaiter scroll";
                itemDef.itemActions = new String[5];
                itemDef.itemActions[0] = "Unlock";
                break;
            case 6800:
                itemDef.name = "Double wood scroll";
                itemDef.itemActions = new String[5];
                itemDef.itemActions[0] = "Unlock";
                break;
            case 6801:
                itemDef.name = "Little birdy scroll";
                itemDef.itemActions = new String[5];
                itemDef.itemActions[0] = "Unlock";
                break;
            case 6802:
                itemDef.name = "The rock scroll";
                itemDef.itemActions = new String[5];
                itemDef.itemActions[0] = "Unlock";
                break;
            case 6803:
                itemDef.name = "Flame on scroll";
                itemDef.itemActions = new String[5];
                itemDef.itemActions[0] = "Unlock";
                break;
            case 11941:
                itemDef.itemActions = new String[]{"Open", null, "Check", null, "Destroy"};
                break;
            case 12019:
            case 12020:
                itemDef.itemActions = new String[]{"Fill", "Open", "Check", "Empty", "Destroy"};
                break;
            case 24480:
            case 24481:
                itemDef.itemActions = new String[]{"Fill", "Close", "Check", "Empty", "Destroy"};
                break;
            case 22586:
                itemDef.itemActions = new String[]{"Close", null, "Check", null, "Destroy"};
                break;
            case 7478:
                itemDef.itemActions = new String[5];
                itemDef.name = "Vote token";
                itemDef.description = "Exchange these tokens for a mystery box and vote points by speaking to Hopleez!";
                break;
            case 8038:
                itemDef.name = "Pet mystery box";
                itemDef.itemActions = new String[5];
                itemDef.itemActions[0] = "Open";
                break;
            case 6199:
                itemDef.name = "Bronze mystery box";
                itemDef.inventoryModel = 2426;
                itemDef.modelZoom = 1180;
                itemDef.modelRotationX = 172;
                itemDef.modelRotationY = 160;
                itemDef.itemActions = new String[5];
                itemDef.itemActions[0] = "Open";
                itemDef.recolorFrom = new short[]{22410, 2999};
                itemDef.recolorTo = new short[]{5652, 7050};
                break;
            case 12955:
                itemDef.name = "Silver mystery box";
                itemDef.inventoryModel = 2426;
                itemDef.modelZoom = 1180;
                itemDef.modelRotationX = 172;
                itemDef.modelRotationY = 160;
                itemDef.itemActions = new String[5];
                itemDef.itemActions[0] = "Open";
                itemDef.recolorFrom = new short[]{22410, 2999};
                itemDef.recolorTo = new short[]{115, 61};
                break;
            case 11739:
                itemDef.name = "Gold mystery box";
                itemDef.inventoryModel = 2426;
                itemDef.modelZoom = 1180;
                itemDef.modelRotationX = 172;
                itemDef.modelRotationY = 160;
                itemDef.itemActions = new String[5];
                itemDef.itemActions[0] = "Open";
                itemDef.recolorFrom = new short[]{22410, 2999};
                itemDef.recolorTo = new short[]{8128, 6073};
                break;
           /*     case 20080:
                itemDef.name = "Black Ankou Mask";
                itemDef.modelID = 46502;
                itemDef.maleWield = 46501;
                itemDef.femaleWield = 46501;
                itemDef.modelZoom = 750;
                itemDef.modelRotationX = 172;
                itemDef.modelRotationY = 160;
                itemDef.itemActions = new String[5];
                itemDef.itemActions[1] = "Wear";
                itemDef.itemActions[4] = "Drop";
                break;*/

            case 5509:
            case 5510:
            case 5512:
            case 5514:
                itemDef.itemActions = new String[5];
                itemDef.itemActions[0] = "Fill";
                itemDef.itemActions[2] = "Empty";
                break;

            case 5733:
                itemDef.itemActions = new String[5];
                itemDef.itemActions[0] = "Yum";
                break;

            case 4155:
                itemDef.itemActions = new String[]{"Check", null, "Teleport", null, "Drop"};
                break;
            case 15098:
                itemDef.name = "Dice (up to 100)";
                itemDef.inventoryModel = 47852;
                itemDef.itemActions = new String[5];
                itemDef.itemActions[0] = "Private-roll";
                itemDef.itemActions[2] = "Clan-roll";
                itemDef.modelRotationX = 215;
                itemDef.modelRotationY = 94;
                itemDef.modelZoom = 1104;
                itemDef.brightness = 25;
                break;
            case 671:
                itemDef.name = "Baby Darth";
                itemDef.itemActions = new String[5];
                itemDef.groundActions = new String[5];
                itemDef.groundActions[0] = "Drop";
                itemDef.description = "Aw it's a little baby darth!";
                break;
            case 6831:
                itemDef.name = "Mime Box";
                itemDef.itemActions = new String[5];
                itemDef.itemActions[0] = "Open";
                itemDef.description = "Contains a random reward of mime costume, mime emote or 75k coins.";
                break;

                /*
                * BLACK ANKOU
                 */
            case 13357:
                itemDef.name = "Lynch's Ankou Mask";
                itemDef.inventoryModel = 32031;
                itemDef.maleWield = 31747;
                itemDef.femaleWield = 31825;
                itemDef.modelZoom = 530;
                itemDef.modelRotationX = 0;
                itemDef.modelRotationY = 500;
                itemDef.modelOffset1 = 0;
                itemDef.modelOffset2 = 8;
                itemDef.itemActions = new String[5];
                itemDef.itemActions[1] = "Wear";
                itemDef.itemActions[4] = "Drop";
                itemDef.recolorTo = new short[]{10411, 14532, 12492, 0, 0, 0};
                itemDef.recolorFrom = new short[]{10411, 14532, 12492, 12500, 14554, 960};
                break;
            case 13358:
                itemDef.name = "Lynch's Ankou Top";
                itemDef.inventoryModel = 32017;
                itemDef.maleWield = 31811;
                itemDef.maleWield2 = 31798;
                itemDef.femaleWield = 31874;
                itemDef.femaleWield2 = 31870;
                itemDef.modelZoom = 1160;
                itemDef.modelRotationX = 0;
                itemDef.modelRotationY = 550;
                itemDef.modelOffset1 = 0;
                itemDef.modelOffset2 = 0;
                itemDef.itemActions = new String[5];
                itemDef.itemActions[1] = "Wear";
                itemDef.itemActions[4] = "Drop";
                itemDef.recolorTo = new short[]{10411, 14532, 12492, 0, 0, 0};
                itemDef.recolorFrom = new short[]{10411, 14532, 12492, 12500, 14554, 960};
                break;
            case 13359:
                itemDef.name = "Lynch's Ankou Legs";
                itemDef.inventoryModel = 32024;
                itemDef.maleWield = 31789;
                itemDef.femaleWield = 31856;
                itemDef.modelZoom = 1720;
                itemDef.modelRotationX = 0;
                itemDef.modelRotationY = 450;
                itemDef.modelOffset1 = 0;
                itemDef.modelOffset2 = 9;
                itemDef.itemActions = new String[5];
                itemDef.itemActions[1] = "Wear";
                itemDef.itemActions[4] = "Drop";
                itemDef.recolorTo = new short[]{10411, 14532, 12492, 0, 0, 0};
                itemDef.recolorFrom = new short[]{10411, 14532, 12492, 12500, 14554, 960};
                break;
            case 13360:
                itemDef.name = "Lynch's Ankou Gloves";
                itemDef.inventoryModel = 31964;
                itemDef.maleWield = 31779;
                itemDef.femaleWield = 31851;
                itemDef.modelZoom = 450;
                itemDef.modelRotationX = 0;
                itemDef.modelRotationY = 550;
                itemDef.modelOffset1 = 1;
                itemDef.modelOffset2 = 1;
                itemDef.itemActions = new String[5];
                itemDef.itemActions[1] = "Wear";
                itemDef.itemActions[4] = "Drop";
                itemDef.recolorTo = new short[]{10411, 14532, 12492, 0, 0, 0};
                itemDef.recolorFrom = new short[]{10411, 14532, 12492, 12500, 14554, 960};
                break;
            case 13682:
                itemDef.name = "Lynch's Ankou Socks";
                itemDef.inventoryModel = 32002;
                itemDef.maleWield = 31820;
                itemDef.femaleWield = 31880;
                itemDef.modelZoom = 653;
                itemDef.modelRotationX = 67;
                itemDef.modelRotationY = 579;
                itemDef.modelOffset1 = 4;
                itemDef.modelOffset2 = 3;
                itemDef.itemActions = new String[5];
                itemDef.itemActions[1] = "Wear";
                itemDef.itemActions[4] = "Drop";
                itemDef.recolorTo = new short[]{10411, 14532, 12492, 0, 0, 0};
                itemDef.recolorFrom = new short[]{10411, 14532, 12492, 12500, 14554, 960};
                break;

                /*
                * GREEN ANKOU
                 */

            case 13683:
                itemDef.name = "Green Ankou Mask";
                itemDef.inventoryModel = 32031;
                itemDef.maleWield = 31747;
                itemDef.femaleWield = 31825;
                itemDef.modelZoom = 530;
                itemDef.modelRotationX = 0;
                itemDef.modelRotationY = 500;
                itemDef.modelOffset1 = 0;
                itemDef.modelOffset2 = 8;
                itemDef.itemActions = new String[5];
                itemDef.itemActions[1] = "Wear";
                itemDef.itemActions[4] = "Drop";
                itemDef.recolorTo = new short[]{10411, 14532, 12492, (short) 350770, (short) 350770, (short) 350770};
                itemDef.recolorFrom = new short[]{10411, 14532, 12492, 12500, 14554, 960};
                break;
            case 13684:
                itemDef.name = "Green Ankou Top";
                itemDef.inventoryModel = 32017;
                itemDef.maleWield = 31811;
                itemDef.maleWield2 = 31798;
                itemDef.femaleWield = 31874;
                itemDef.femaleWield2 = 31870;
                itemDef.modelZoom = 1160;
                itemDef.modelRotationX = 0;
                itemDef.modelRotationY = 550;
                itemDef.modelOffset1 = 0;
                itemDef.modelOffset2 = 0;
                itemDef.itemActions = new String[5];
                itemDef.itemActions[1] = "Wear";
                itemDef.itemActions[4] = "Drop";
                itemDef.recolorTo = new short[]{10411, 14532, 12492, (short) 350770, (short) 350770, (short) 350770};
                itemDef.recolorFrom = new short[]{10411, 14532, 12492, 12500, 14554, 960};
                break;
            case 13685:
                itemDef.name = "Green Ankou Legs";
                itemDef.inventoryModel = 32024;
                itemDef.maleWield = 31789;
                itemDef.femaleWield = 31856;
                itemDef.modelZoom = 1720;
                itemDef.modelRotationX = 0;
                itemDef.modelRotationY = 450;
                itemDef.modelOffset1 = 0;
                itemDef.modelOffset2 = 9;
                itemDef.itemActions = new String[5];
                itemDef.itemActions[1] = "Wear";
                itemDef.itemActions[4] = "Drop";
                itemDef.recolorTo = new short[]{10411, 14532, 12492, (short) 350770, (short) 350770, (short) 350770};
                itemDef.recolorFrom = new short[]{10411, 14532, 12492, 12500, 14554, 960};
                break;
            case 13686:
                itemDef.name = "Green Ankou Gloves";
                itemDef.inventoryModel = 31964;
                itemDef.maleWield = 31779;
                itemDef.femaleWield = 31851;
                itemDef.modelZoom = 450;
                itemDef.modelRotationX = 0;
                itemDef.modelRotationY = 550;
                itemDef.modelOffset1 = 1;
                itemDef.modelOffset2 = 1;
                itemDef.itemActions = new String[5];
                itemDef.itemActions[1] = "Wear";
                itemDef.itemActions[4] = "Drop";
                itemDef.recolorTo = new short[]{10411, 14532, 12492, (short) 350770, (short) 350770, (short) 350770};
                itemDef.recolorFrom = new short[]{10411, 14532, 12492, 12500, 14554, 960};
                break;
            case 13687:
                itemDef.name = "Green Ankou Socks";
                itemDef.inventoryModel = 32002;
                itemDef.maleWield = 31820;
                itemDef.femaleWield = 31880;
                itemDef.modelZoom = 653;
                itemDef.modelRotationX = 67;
                itemDef.modelRotationY = 579;
                itemDef.modelOffset1 = 4;
                itemDef.modelOffset2 = 3;
                itemDef.itemActions = new String[5];
                itemDef.itemActions[1] = "Wear";
                itemDef.itemActions[4] = "Drop";
                itemDef.recolorTo = new short[]{10411, 14532, 12492, (short) 350770, (short) 350770, (short) 350770};
                itemDef.recolorFrom = new short[]{10411, 14532, 12492, 12500, 14554, 960};
                break;

                /*
                * BLUE ANKOU
                 */

            case 13688:
                itemDef.name = "Blue Ankou Mask";
                itemDef.inventoryModel = 32031;
                itemDef.maleWield = 31747;
                itemDef.femaleWield = 31825;
                itemDef.modelZoom = 530;
                itemDef.modelRotationX = 0;
                itemDef.modelRotationY = 500;
                itemDef.modelOffset1 = 0;
                itemDef.modelOffset2 = 8;
                itemDef.itemActions = new String[5];
                itemDef.itemActions[1] = "Wear";
                itemDef.itemActions[4] = "Drop";
                itemDef.recolorTo = new short[]{10411, 14532, 12492, -21568, -21568, -21568};
                itemDef.recolorFrom = new short[]{10411, 14532, 12492, 12500, 14554, 960};
                break;
            case 13689:
                itemDef.name = "Blue Ankou Top";
                itemDef.inventoryModel = 32017;
                itemDef.maleWield = 31811;
                itemDef.maleWield2 = 31798;
                itemDef.femaleWield = 31874;
                itemDef.femaleWield2 = 31870;
                itemDef.modelZoom = 1160;
                itemDef.modelRotationX = 0;
                itemDef.modelRotationY = 550;
                itemDef.modelOffset1 = 0;
                itemDef.modelOffset2 = 0;
                itemDef.itemActions = new String[5];
                itemDef.itemActions[1] = "Wear";
                itemDef.itemActions[4] = "Drop";
                itemDef.recolorTo = new short[]{10411, 14532, 12492, -21568, -21568, -21568};
                itemDef.recolorFrom = new short[]{10411, 14532, 12492, 12500, 14554, 960};
                break;
            case 13690:
                itemDef.name = "Blue Ankou Legs";
                itemDef.inventoryModel = 32024;
                itemDef.maleWield = 31789;
                itemDef.femaleWield = 31856;
                itemDef.modelZoom = 1720;
                itemDef.modelRotationX = 0;
                itemDef.modelRotationY = 450;
                itemDef.modelOffset1 = 0;
                itemDef.modelOffset2 = 9;
                itemDef.itemActions = new String[5];
                itemDef.itemActions[1] = "Wear";
                itemDef.itemActions[4] = "Drop";
                itemDef.recolorTo = new short[]{10411, 14532, 12492, -21568, -21568, -21568};
                itemDef.recolorFrom = new short[]{10411, 14532, 12492, 12500, 14554, 960};
                break;
            case 13691:
                itemDef.name = "Blue Ankou Gloves";
                itemDef.inventoryModel = 31964;
                itemDef.maleWield = 31779;
                itemDef.femaleWield = 31851;
                itemDef.modelZoom = 450;
                itemDef.modelRotationX = 0;
                itemDef.modelRotationY = 550;
                itemDef.modelOffset1 = 1;
                itemDef.modelOffset2 = 1;
                itemDef.itemActions = new String[5];
                itemDef.itemActions[1] = "Wear";
                itemDef.itemActions[4] = "Drop";
                itemDef.recolorTo = new short[]{10411, 14532, 12492, -21568, -21568, -21568};
                itemDef.recolorFrom = new short[]{10411, 14532, 12492, 12500, 14554, 960};
                break;
            case 13692:
                itemDef.name = "Blue Ankou Socks";
                itemDef.inventoryModel = 32002;
                itemDef.maleWield = 31820;
                itemDef.femaleWield = 31880;
                itemDef.modelZoom = 653;
                itemDef.modelRotationX = 67;
                itemDef.modelRotationY = 579;
                itemDef.modelOffset1 = 4;
                itemDef.modelOffset2 = 3;
                itemDef.itemActions = new String[5];
                itemDef.itemActions[1] = "Wear";
                itemDef.itemActions[4] = "Drop";
                itemDef.recolorTo = new short[]{10411, 14532, 12492, -21568, -21568, -21568};
                itemDef.recolorFrom = new short[]{10411, 14532, 12492, 12500, 14554, 960};
                break;

                /*
                * PURPLE ANKOU SET
                 */

            case 13693:
                itemDef.name = "Purple Ankou Mask";
                itemDef.inventoryModel = 32031;
                itemDef.maleWield = 31747;
                itemDef.femaleWield = 31825;
                itemDef.modelZoom = 530;
                itemDef.modelRotationX = 0;
                itemDef.modelRotationY = 500;
                itemDef.modelOffset1 = 0;
                itemDef.modelOffset2 = 8;
                itemDef.itemActions = new String[5];
                itemDef.itemActions[1] = "Wear";
                itemDef.itemActions[4] = "Drop";
                itemDef.recolorTo = new short[]{10411, 14532, 12492, (short) 50000, (short) 50000, (short) 50000};
                itemDef.recolorFrom = new short[]{10411, 14532, 12492, 12500, 14554, 960};
                break;
            case 13694:
                itemDef.name = "Purple Ankou Top";
                itemDef.inventoryModel = 32017;
                itemDef.maleWield = 31811;
                itemDef.maleWield2 = 31798;
                itemDef.femaleWield = 31874;
                itemDef.femaleWield2 = 31870;
                itemDef.modelZoom = 1160;
                itemDef.modelRotationX = 0;
                itemDef.modelRotationY = 550;
                itemDef.modelOffset1 = 0;
                itemDef.modelOffset2 = 0;
                itemDef.itemActions = new String[5];
                itemDef.itemActions[1] = "Wear";
                itemDef.itemActions[4] = "Drop";
                itemDef.recolorTo = new short[]{10411, 14532, 12492, (short) 50000, (short) 50000, (short) 50000};
                itemDef.recolorFrom = new short[]{10411, 14532, 12492, 12500, 14554, 960};
                break;
            case 13695:
                itemDef.name = "Purple Ankou Legs";
                itemDef.inventoryModel = 32024;
                itemDef.maleWield = 31789;
                itemDef.femaleWield = 31856;
                itemDef.modelZoom = 1720;
                itemDef.modelRotationX = 0;
                itemDef.modelRotationY = 450;
                itemDef.modelOffset1 = 0;
                itemDef.modelOffset2 = 9;
                itemDef.itemActions = new String[5];
                itemDef.itemActions[1] = "Wear";
                itemDef.itemActions[4] = "Drop";
                itemDef.recolorTo = new short[]{10411, 14532, 12492, (short) 50000, (short) 50000, (short) 50000};
                itemDef.recolorFrom = new short[]{10411, 14532, 12492, 12500, 14554, 960};
                break;
            case 13696:
                itemDef.name = "Purple Ankou Gloves";
                itemDef.inventoryModel = 31964;
                itemDef.maleWield = 31779;
                itemDef.femaleWield = 31851;
                itemDef.modelZoom = 450;
                itemDef.modelRotationX = 0;
                itemDef.modelRotationY = 550;
                itemDef.modelOffset1 = 1;
                itemDef.modelOffset2 = 1;
                itemDef.itemActions = new String[5];
                itemDef.itemActions[1] = "Wear";
                itemDef.itemActions[4] = "Drop";
                itemDef.recolorTo = new short[]{10411, 14532, 12492, (short) 50000, (short) 50000, (short) 50000};
                itemDef.recolorFrom = new short[]{10411, 14532, 12492, 12500, 14554, 960};
                break;
            case 13697:
                itemDef.name = "Purple Ankou Socks";
                itemDef.inventoryModel = 32002;
                itemDef.maleWield = 31820;
                itemDef.femaleWield = 31880;
                itemDef.modelZoom = 653;
                itemDef.modelRotationX = 67;
                itemDef.modelRotationY = 579;
                itemDef.modelOffset1 = 4;
                itemDef.modelOffset2 = 3;
                itemDef.itemActions = new String[5];
                itemDef.itemActions[1] = "Wear";
                itemDef.itemActions[4] = "Drop";
                itemDef.recolorTo = new short[]{10411, 14532, 12492, (short) 50000, (short)50000, (short) 50000};
                itemDef.recolorFrom = new short[]{10411, 14532, 12492, 12500, 14554, 960};
                break;


            case 6832:
                itemDef.name = "Drill Demon Box";
                itemDef.itemActions = new String[5];
                itemDef.itemActions[0] = "Open";
                itemDef.description = "Contains a random reward of drill demon costume, drill demon emote or 75k coins.";
                break;
            case 12897:
                itemDef.itemActions = new String[5];
                itemDef.itemActions[0] = "Open";
                itemDef.name = "Clan Showcase Box";
                itemDef.description = "Opening this box will award a random showcase item based on the clan's level.";
                break;
            case 6854:
                itemDef.itemActions = new String[5];
                itemDef.itemActions[0] = "Open";
                itemDef.name = "Clan Resource Box";
                itemDef.description = "Inside this box contains random resources for the clan's bank.";
                break;
            case 2568:
                itemDef.itemActions[2] = "Check charges";
                break;
            case 13188:
                itemDef.name = "Dragon claws";
                itemDef.itemActions = new String[5];
                itemDef.itemActions[1] = "Wield";
                break;
            case 8013:
                itemDef.name = "Home teleport";
                break;
            case 20527:
                itemDef.name = "Royale tokens";
                itemDef.description = "The main currency for OS Royale.";
                itemDef.stackable = true;
                break;
            case 11188:
                itemDef.name = "2x experience (1hr)";
                itemDef.description = "This lamp will give clan members double experience for a random skill for 1 hour.";
                break;
            case 11189:
                itemDef.name = "2x experience (3hr)";
                itemDef.description = "This lamp will give clan members double experience for a random skill for 3 hour.";
                break;
            case 11679:
                itemDef.name = "2x experience (5hr)";
                itemDef.description = "This lamp will give clan members double experience for a random skill for 5 hour.";
                break;
            case 11187:
                itemDef.name = "250k experience lamp";
                itemDef.description = "This lamp will give clan members 250,000 experience for a random skill.";
                break;
            case 4447:
                itemDef.name = "500k experience lamp";
                itemDef.description = "This lamp will give clan members 500,000 experience for a random skill.";
                break;
            case 6543:
                itemDef.name = "Drop rate increase (1.5x)";
                itemDef.description = "This lamp will give clan members a 1x drop rate increase for a random boss. (15min)";
                break;
            case 7498:
                itemDef.name = "Drop rate increase (2x)";
                itemDef.description = "This lamp will give clan members a 3x drop rate increase for a random boss. (15min)";
                break;
            case 11137:
                itemDef.name = "Drop rate increase (2.5x)";
                itemDef.description = "This lamp will give clan members a 5x drop rate increase for a random boss. (15min)";
                break;
            case 11139:
                itemDef.name = "2x resource (15min)";
                itemDef.description = "This lamp will give clan members double resource rewards for 15 minutes.";
                break;
          /*  case 20107:
            case 20095:
            case 20098:
            case 20101:
            case 20104:
                System.out.println(itemDef.name);
                System.out.println("-----------");
                System.out.println("offset 1 - " + itemDef.modelOffset1);
                System.out.println("offset 2 - " + itemDef.modelOffset2);
                System.out.println("x rotation - " + itemDef.modelRotationX);
                System.out.println("model zoom - " + itemDef.modelZoom);
                System.out.println("x rotation - " + itemDef.modelRotationX);
                System.out.println("y rotation - " + itemDef.modelRotationY);
                System.out.println("model ID - " + itemDef.modelID);
                System.out.println("female wield - " + itemDef.femaleWield);
                System.out.println("male wield - " + itemDef.maleWield);
                break;*/
            case 12690://Armour sets
            case 12873:
            case 12875:
            case 12877:
            case 12879:
            case 12881:
            case 12883:
            case 12962:
            case 12972:
            case 12974:
            case 12984:
            case 12986:
            case 12988:
            case 12990:
            case 13000:
            case 13002:
            case 13012:
            case 13014:
            case 13024:
            case 13026:
            case 11738:
            case 9666:
            case 9670:
            case 12865:
            case 12867:
            case 12869:
            case 12871:
            case 12966:
            case 12964:
            case 12968:
            case 12970:
            case 12976:
            case 12978:
            case 12980:
            case 12982:
            case 12992:
            case 12994:
            case 12996:
            case 12998:
            case 13004:
            case 13006:
            case 13008:
            case 13010:
            case 13016:
            case 13018:
            case 13020:
            case 13022:
            case 13028:
            case 13030:
            case 13032:
            case 13034:
            case 13036:
            case 13038:
            case 12960:
            case 13173:
            case 13175:
            case 13064:
            case 13066:
                itemDef.itemActions = new String[5];
                itemDef.itemActions[0] = "Unpack";
                break;
        }

        if (itemDef.certTemplateID != -1)
            itemDef.toNote();
        return itemDef;
    }


    static void dumpList() {
        /*JsonArray array = new JsonArray();

        for (int index = 0; index < totalItems; index++) {
            try {
                ItemDefinition definition = lookup(index);

                if (definition.name == null || definition.name.equals("null") || definition.name.isEmpty())
                    continue;

                JsonObject object = new JsonObject();
                array.add(object);

                object.addProperty("id", definition.id);
                object.addProperty("name", definition.name);

                if (definition.stackable) {
                    object.addProperty("stackable", true);
                }
                if (definition.itemActions != null) {
                    for (int idx = 0; idx < definition.itemActions.length; idx++) {
                        String action = definition.itemActions[idx];
                        if (action != null) {
                            if (action.contains("Wear") || action.contains("Wield")) {
                                object.addProperty("equipable", true);
                            }
                            if (action.contains("Destroy")) {
                                object.addProperty("destroyable", true);
                            }
                        }
                    }
                }
                if (definition.certTemplateID == -1 && definition.certID != -1) {
                    object.addProperty("noted-id", definition.certID);
                }
                if (definition.certTemplateID != -1 && definition.certID != definition.id) {
                    object.addProperty("unnoted-id", definition.certID);
                }
                if (definition.value > 1) {
                    object.addProperty("base-value", definition.value);
                }
                if (definition.itemActions != null) {
                    for (int idx = 0; idx < definition.itemActions.length; idx++) {
                        String action = definition.itemActions[idx];
                        if (action != null) {
                            if (action.contains("Wield")) {
                                object.addProperty("equipment-type", "WEAPON");
                            }
                            break;
                        }
                    }
                }

            } catch (Exception e) {
                System.out.println(index);
                e.printStackTrace();
            }
        }

        try {
            Files.write(Paths.get("./item_dump.json"), new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create().toJson(array).getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }*/
    }

    static void unpackConfig(final StreamLoader archive) {
        dataBuffer = new Buffer(archive.getFile("obj.dat"));
        final Buffer idxBuffer = new Buffer(archive.getFile("obj.idx"));

        highestFileId = idxBuffer.readUnsignedShort();
        offsets = new int[highestFileId + 1];

        int offset = 0;
        for (int i = 0; i < highestFileId; i++) {
            final int size = idxBuffer.readUnsignedShort();
            if (size == 65535) break;
            offsets[i] = offset;
            offset += size;
        }
        cache = new ItemDefinition[10];

        for (int _ctr = 0; _ctr < 10; _ctr++) {
            cache[_ctr] = new ItemDefinition();
        }
    }


    //		Buffer = new Buffer(FileUtility.readFile(SignLink.findcachedir() + "obj.dat"));
    //		Buffer Buffer = new Buffer(FileUtility.readFile(SignLink.findcachedir() + "obj.idx"));
    //		totalItems = Buffer.readUShort() + 21;
    //		BufferIndices = new int[totalItems + 50000];
    //		int i = 2;
    //		for (int j = 0; j < totalItems - 21; j++) {
    //			BufferIndices[j] = i;
    //			i += Buffer.readUShort();
    //		}
    //
    //		cache = new ItemDefinition[10];
    //		for (int k = 0; k < 10; k++)
    //			cache[k] = new ItemDefinition();

    public Model getModelForGender(int genderType) {
        int firstModelId = maleHeadModel;
        int secondModelId = maleHeadModel2;
        if (genderType == 1) {
            firstModelId = femaleHeadModel;
            secondModelId = femaleHeadModel2;
        }
        if (firstModelId == -1)
            return null;
        Model firstModel = Model.getModel(firstModelId);
        if (secondModelId != -1) {
            Model secondModel = Model.getModel(secondModelId);
            Model[] models = {firstModel, secondModel};
            firstModel = new Model(2, models);
        }
        if (retextureFrom != null) {
            for (int i1 = 0; i1 < retextureFrom.length; i1++)
                firstModel.recolor(retextureFrom[i1], recolorTo[i1]);

        }
        return firstModel;
    }

    boolean method195(int j) {
        int k = maleWield;
        int l = maleWield2;
        int i1 = maleModel2;
        if (j == 1) {
            k = femaleWield;
            l = femaleWield2;
            i1 = femaleModel2;
        }
        if (k == -1)
            return true;
        boolean flag = Model.isCached(k);
        if (l != -1 && !Model.isCached(l))
            flag = false;
        if (i1 != -1 && !Model.isCached(i1))
            flag = false;
        return flag;
    }

    Model method196(int i) {
        int j = maleWield;
        int k = maleWield2;
        int l = maleModel2;
        if (i == 1) {
            j = femaleWield;
            k = femaleWield2;
            l = femaleModel2;
        }
        if (j == -1)
            return null;
        Model model = Model.getModel(j);
        if (k != -1)
            if (l != -1) {
                Model model_1 = Model.getModel(k);
                Model model_3 = Model.getModel(l);
                Model[] aclass30_sub2_sub4_sub6_1s = {model, model_1, model_3};
                model = new Model(3, aclass30_sub2_sub4_sub6_1s);
            } else {
                Model model_2 = Model.getModel(k);
                Model[] aclass30_sub2_sub4_sub6s = {model, model_2};
                model = new Model(2, aclass30_sub2_sub4_sub6s);
            }
        if (i == 0 && maleOffset != 0)
            model.offsetBy(0, maleOffset, 0);
        if (i == 1 && femaleOffset != 0)
            model.offsetBy(0, femaleOffset, 0);
        if (recolorFrom != null) {
            for (int i1 = 0; i1 < recolorFrom.length; i1++)
                model.recolor(recolorFrom[i1], recolorTo[i1]);
        }
        return model;
    }

    private void setDefaults() {
        inventoryModel = 0;
        name = null;
        description = null;
        recolorTo = null;
        recolorFrom = null;
        modelZoom = 2000;
        modelRotationY = 0;
        modelRotationX = 0;
        zan2d = 0;
        modelOffset1 = 0;
        modelOffset2 = 0;
        stackable = false;
        value = 1;
        membersObject = false;
        groundActions = null;
        itemActions = null;
        maleWield = -1;
        maleWield2 = -1;
        maleOffset = 0;
        femaleWield = -1;
        femaleWield2 = -1;
        femaleOffset = 0;
        maleModel2 = -1;
        femaleModel2 = -1;
        maleHeadModel = -1;
        maleHeadModel2 = -1;
        femaleHeadModel = -1;
        femaleHeadModel2 = -1;
        stackIDs = null;
        stackAmounts = null;
        certID = -1;
        certTemplateID = -1;
        resizeX = 128;
        resizeY = 128;
        resizeZ = 128;
        brightness = 0;
        contrast = 0;
        team = 0;
    }

    private void toNote() {
        ItemDefinition noted = lookup(certTemplateID);
        inventoryModel = noted.inventoryModel;
        modelZoom = noted.modelZoom;
        modelRotationY = noted.modelRotationY;
        modelRotationX = noted.modelRotationX;

        zan2d = noted.zan2d;
        modelOffset1 = noted.modelOffset1;
        modelOffset2 = noted.modelOffset2;
        recolorTo = noted.recolorTo;
        recolorFrom = noted.recolorFrom;
        ItemDefinition unnoted = lookup(certID);
        name = unnoted.name;
        membersObject = unnoted.membersObject;
        value = unnoted.value;
        String s = "a";
        char c = unnoted.name.charAt(0);
        if (c == 'A' || c == 'E' || c == 'I' || c == 'O' || c == 'U')
            s = "an";
        description = "Swap this note at any bank for " + s + " " + unnoted.name + ".";
        stackable = true;
    }

    public static Sprite getSprite(int item, int amount, int color) {
        if (color == 0) {
            Sprite sprite = (Sprite) mruNodes1.get(item);
            if (sprite != null && sprite.resizeHeight != amount && sprite.resizeHeight != -1) {

                sprite.unlink();
                sprite = null;
            }
            if (sprite != null)
                return sprite;
        }
        ItemDefinition itemDef = lookup(item);
        if (itemDef.stackIDs == null)
            amount = -1;
        if (amount > 1) {
            int i1 = -1;
            for (int j1 = 0; j1 < 10; j1++)
                if (amount >= itemDef.stackAmounts[j1] && itemDef.stackAmounts[j1] != 0)
                    i1 = itemDef.stackIDs[j1];

            if (i1 != -1)
                itemDef = lookup(i1);
        }
        Model model = itemDef.method201(1);
        if (model == null)
            return null;
        Sprite sprite = null;
        if (itemDef.certTemplateID != -1) {
            sprite = getSprite(itemDef.certID, 10, -1);
            if (sprite == null)
                return null;
        }
        Sprite enabledSprite = new Sprite(32, 32);
        int k1 = Rasterizer3D.originViewX;
        int l1 = Rasterizer3D.originViewY;
        int[] ai = Rasterizer3D.scanOffsets;
        int[] ai1 = Rasterizer2D.pixels;
        int i2 = Rasterizer2D.width;
        int j2 = Rasterizer2D.height;
        int k2 = Rasterizer2D.leftX;
        int l2 = Rasterizer2D.bottomX;
        int i3 = Rasterizer2D.topY;
        int j3 = Rasterizer2D.bottomY;
        Rasterizer3D.world = false;
        Rasterizer3D.aBoolean1464 = false;
        Rasterizer2D.initDrawingArea(enabledSprite.raster, 32, 32);
        Rasterizer2D.fillRectangle(0, 0, 32, 32, 0);
        Rasterizer3D.useViewport();
        int k3 = itemDef.modelZoom;
        if (color == -1)
            k3 = (int) ((double) k3 * 1.5D);
        if (color > 0)
            k3 = (int) ((double) k3 * 1.04D);
        int l3 = Rasterizer3D.SINE[itemDef.modelRotationY] * k3 >> 16;
        int i4 = Rasterizer3D.COSINE[itemDef.modelRotationY] * k3 >> 16;
        Rasterizer3D.renderOnGpu = true;
        model.renderModel(itemDef.modelRotationX, itemDef.zan2d, itemDef.modelRotationY, itemDef.modelOffset1, l3 + model.modelBaseY / 2 + itemDef.modelOffset2, i4 + itemDef.modelOffset2);
        Rasterizer3D.renderOnGpu = false;
        enabledSprite.outline(1);
        if (color > 0) {
            enabledSprite.outline(16777215);
        }
        if (color == 0) {
            enabledSprite.shadow(3153952);
        }
        Rasterizer2D.initDrawingArea(enabledSprite.raster, 32, 32);
        if (itemDef.certTemplateID != -1) {
            int l5 = sprite.resizeWidth;
            int j6 = sprite.resizeHeight;
            sprite.resizeWidth = 32;
            sprite.resizeHeight = 32;
            sprite.drawSprite(0, 0);
            sprite.resizeWidth = l5;
            sprite.resizeHeight = j6;
        }
        if (color == 0)
            mruNodes1.put(enabledSprite, item);
        Rasterizer2D.initDrawingArea(ai1, i2, j2);
        Rasterizer2D.setDrawingArea(k2, i3, l2, j3);
        Rasterizer3D.originViewX = k1;
        Rasterizer3D.originViewY = l1;
        Rasterizer3D.scanOffsets = ai;
        Rasterizer3D.aBoolean1464 = true;
        Rasterizer3D.world = true;
        if (itemDef.stackable)
            enabledSprite.resizeWidth = 33;
        else
            enabledSprite.resizeWidth = 32;
        enabledSprite.resizeHeight = amount;
        return enabledSprite;
    }

    public static Sprite getSprite(int item, int amount, int k, int zoom) {
        if (k == 0) {
            Sprite sprite = (Sprite) mruNodes1.get(item);
            if (sprite != null && sprite.resizeHeight != amount && sprite.resizeHeight != -1) {

                sprite.unlink();
                sprite = null;
            }
            if (sprite != null)
                return sprite;
        }
        ItemDefinition itemDef = lookup(item);
        if (itemDef.stackIDs == null)
            amount = -1;
        if (amount > 1) {
            int i1 = -1;
            for (int j1 = 0; j1 < 10; j1++)
                if (amount >= itemDef.stackAmounts[j1] && itemDef.stackAmounts[j1] != 0)
                    i1 = itemDef.stackIDs[j1];

            if (i1 != -1)
                itemDef = lookup(i1);
        }
        Model model = itemDef.method201(1);
        if (model == null)
            return null;
        Sprite sprite = null;
        if (itemDef.certTemplateID != -1) {
            sprite = getSprite(itemDef.certID, 10, -1);
            if (sprite == null)
                return null;
        }
        Sprite enabledSprite = new Sprite(32, 32);
        int k1 = Rasterizer3D.originViewX;
        int l1 = Rasterizer3D.originViewY;
        int[] ai = Rasterizer3D.scanOffsets;
        int[] ai1 = Rasterizer2D.pixels;
        int i2 = Rasterizer2D.width;
        int j2 = Rasterizer2D.height;
        int k2 = Rasterizer2D.leftX;
        int l2 = Rasterizer2D.bottomX;
        int i3 = Rasterizer2D.topY;
        int j3 = Rasterizer2D.bottomY;
        Rasterizer3D.world = false;
        Rasterizer3D.aBoolean1464 = false;
        Rasterizer2D.initDrawingArea(enabledSprite.raster, 32, 32);
        Rasterizer2D.fillRectangle(0, 0, 32, 32, 0);
        Rasterizer3D.useViewport();
        int k3 = itemDef.modelZoom;
        if (k == -1)
            k3 = (int) ((double) k3 * 1.5D);
        if (k > 0)
            k3 = (int) ((double) k3 * 1.04D);
        int l3 = Rasterizer3D.SINE[itemDef.modelRotationY] * k3 >> 16;
        int i4 = Rasterizer3D.COSINE[itemDef.modelRotationY] * k3 >> 16;
        Rasterizer3D.renderOnGpu = true;
        model.renderModel(itemDef.modelRotationX, itemDef.zan2d, itemDef.modelRotationY, itemDef.modelOffset1, l3 + model.modelBaseY / 2 + itemDef.modelOffset2, i4 + itemDef.modelOffset2);
        Rasterizer3D.renderOnGpu = false;
        for (int i5 = 31; i5 >= 0; i5--) {
            for (int j4 = 31; j4 >= 0; j4--)
                if (enabledSprite.raster[i5 + j4 * 32] == 0)
                    if (i5 > 0 && enabledSprite.raster[(i5 - 1) + j4 * 32] > 1)
                        enabledSprite.raster[i5 + j4 * 32] = 1;
                    else if (j4 > 0 && enabledSprite.raster[i5 + (j4 - 1) * 32] > 1)
                        enabledSprite.raster[i5 + j4 * 32] = 1;
                    else if (i5 < 31 && enabledSprite.raster[i5 + 1 + j4 * 32] > 1)
                        enabledSprite.raster[i5 + j4 * 32] = 1;
                    else if (j4 < 31 && enabledSprite.raster[i5 + (j4 + 1) * 32] > 1)
                        enabledSprite.raster[i5 + j4 * 32] = 1;

        }

        if (k > 0) {
            for (int j5 = 31; j5 >= 0; j5--) {
                for (int k4 = 31; k4 >= 0; k4--)
                    if (enabledSprite.raster[j5 + k4 * 32] == 0)
                        if (j5 > 0 && enabledSprite.raster[(j5 - 1) + k4 * 32] == 1)
                            enabledSprite.raster[j5 + k4 * 32] = k;
                        else if (k4 > 0 && enabledSprite.raster[j5 + (k4 - 1) * 32] == 1)
                            enabledSprite.raster[j5 + k4 * 32] = k;
                        else if (j5 < 31 && enabledSprite.raster[j5 + 1 + k4 * 32] == 1)
                            enabledSprite.raster[j5 + k4 * 32] = k;
                        else if (k4 < 31 && enabledSprite.raster[j5 + (k4 + 1) * 32] == 1)
                            enabledSprite.raster[j5 + k4 * 32] = k;

            }

        } else if (k == 0) {
            for (int k5 = 31; k5 >= 0; k5--) {
                for (int l4 = 31; l4 >= 0; l4--)
                    if (enabledSprite.raster[k5 + l4 * 32] == 0 && k5 > 0 && l4 > 0 && enabledSprite.raster[(k5 - 1) + (l4 - 1) * 32] > 0)
                        enabledSprite.raster[k5 + l4 * 32] = 0x302020;

            }

        }
        if (itemDef.certTemplateID != -1) {
            int l5 = sprite.resizeWidth;
            int j6 = sprite.resizeHeight;
            sprite.resizeWidth = 32;
            sprite.resizeHeight = 32;
            sprite.drawSprite(0, 0);
            sprite.resizeWidth = l5;
            sprite.resizeHeight = j6;
        }
        if (k == 0)
            mruNodes1.put(enabledSprite, item);
        Rasterizer2D.initDrawingArea(ai1, i2, j2);
        Rasterizer2D.setDrawingArea(k2, i3, l2, j3);
        Rasterizer3D.originViewX = k1;
        Rasterizer3D.originViewY = l1;
        Rasterizer3D.scanOffsets = ai;
        Rasterizer3D.aBoolean1464 = true;
        Rasterizer3D.world = true;
        if (itemDef.stackable)
            enabledSprite.resizeWidth = 33;
        else
            enabledSprite.resizeWidth = 32;
        enabledSprite.resizeHeight = amount;
        return enabledSprite;
    }

    public Model method201(int i) {
        if (stackIDs != null && i > 1) {
            int j = -1;
            for (int k = 0; k < 10; k++)
                if (i >= stackAmounts[k] && stackAmounts[k] != 0)
                    j = stackIDs[k];

            if (j != -1)
                return lookup(j).method201(1);
        }
        Model model = (Model) mruNodes2.get(id);
        if (model != null)
            return model;
        model = Model.getModel(inventoryModel);
        if (model == null)
            return null;
        if (resizeX != 128 || resizeY != 128 || resizeZ != 128)
            model.scale(resizeX, resizeZ, resizeY);
        if (recolorFrom != null) {
            for (int l = 0; l < recolorFrom.length; l++)
                model.recolor(recolorFrom[l], recolorTo[l]);
        }
        if (retextureFrom != null) {
            for (int l = 0; l < retextureFrom.length; l++)
                model.retexture(retextureFrom[l], retextureTo[l]);
        }
        model.light(64 + brightness, 768 + contrast, -50, -10, -50, false);
        model.singleTile = true;
        mruNodes2.put(model, id);
        return model;
    }

    public Model method202(int i) {
        if (stackIDs != null && i > 1) {
            int j = -1;
            for (int k = 0; k < 10; k++)
                if (i >= stackAmounts[k] && stackAmounts[k] != 0)
                    j = stackIDs[k];

            if (j != -1)
                return lookup(j).method202(1);
        }
        Model model = Model.getModel(inventoryModel);
        if (model == null)
            return null;
        if (recolorFrom != null) {
            for (int l = 0; l < recolorFrom.length; l++)
                model.recolor(recolorFrom[l], recolorTo[l]);
        }
        if (retextureFrom != null) {
            for (int l = 0; l < retextureFrom.length; l++)
                model.retexture(retextureFrom[l], retextureTo[l]);
        }
        return model;
    }

    private void readValues(Buffer buffer) {
        int lastOpcode = -1;
        do {
            int opcode = buffer.readUnsignedByte();
            if (opcode == 0)
                return;
            if (opcode == 1)
                inventoryModel = buffer.readUnsignedShort();
            else if (opcode == 2)
                name = buffer.readStringCp1252NullTerminated();
            else if (opcode == 3)
                description = buffer.readStringCp1252NullTerminated();
            else if (opcode == 4)
                modelZoom = buffer.readUnsignedShort();
            else if (opcode == 5)
                modelRotationY = buffer.readUnsignedShort();
            else if (opcode == 6)
                modelRotationX = buffer.readUnsignedShort();
            else if (opcode == 7) {
                modelOffset1 = buffer.readUnsignedShort();
                if (modelOffset1 > 32767)
                    modelOffset1 -= 0x10000;
            } else if (opcode == 8) {
                modelOffset2 = buffer.readUnsignedShort();
                if (modelOffset2 > 32767)
                    modelOffset2 -= 0x10000;
            } else if (opcode == 9) {
                buffer.readStringCp1252NullTerminated(); // unknown1
            } else if (opcode == 10)
                buffer.readUnsignedShort();
            else if (opcode == 11)
                stackable = true;
            else if (opcode == 12)
                value = buffer.readUnsignedInt();
            else if (opcode == 13)
                buffer.readSignedByte(); // wearPos1
            else if (opcode == 14)
                buffer.readSignedByte(); // wearPos2
            else if (opcode == 16)
                membersObject = true;
            else if (opcode == 23) {
                maleWield = buffer.readUnsignedShort();
                maleOffset = buffer.readSignedByte();
            } else if (opcode == 24)
                maleWield2 = buffer.readUnsignedShort();
            else if (opcode == 25) {
                femaleWield = buffer.readUnsignedShort();
                femaleOffset = buffer.readSignedByte();
            } else if (opcode == 26)
                femaleWield2 = buffer.readUnsignedShort();
            else if (opcode == 27)
                buffer.readSignedByte(); // wearPos3
            else if (opcode >= 30 && opcode < 35) {
                if (groundActions == null)
                    groundActions = new String[5];
                groundActions[opcode - 30] = buffer.readStringCp1252NullTerminated();
                if (groundActions[opcode - 30].equalsIgnoreCase("hidden"))
                    groundActions[opcode - 30] = null;
            } else if (opcode >= 35 && opcode < 40) {
                if (itemActions == null)
                    itemActions = new String[5];
                itemActions[opcode - 35] = buffer.readStringCp1252NullTerminated();
            } else if (opcode == 40) {
                int j = buffer.readUnsignedByte();
                recolorFrom = new short[j];
                recolorTo = new short[j];
                for (int k = 0; k < j; k++) {
                    recolorFrom[k] = (short) buffer.readUnsignedShort();
                    recolorTo[k] = (short) buffer.readUnsignedShort();
                }
            } else if (opcode == 41) {
                int j = buffer.readUnsignedByte();
                retextureFrom = new short[j];
                retextureTo = new short[j];
                for (int k = 0; k < j; k++) {
                    retextureFrom[k] = (short) buffer.readUnsignedShort();
                    retextureTo[k] = (short) buffer.readUnsignedShort();
                }
            } else if (opcode == 42) {
                buffer.readSignedByte(); // shiftClickDropIndex
            } else if (opcode == 65) {
                // isTradeable = true
            } else if (opcode == 75) {
                buffer.readShort(); // weight
            } else if (opcode == 78)
                maleModel2 = buffer.readUnsignedShort();
            else if (opcode == 79)
                femaleModel2 = buffer.readUnsignedShort();
            else if (opcode == 90)
                maleHeadModel = buffer.readUnsignedShort();
            else if (opcode == 91)
                femaleHeadModel = buffer.readUnsignedShort();
            else if (opcode == 92)
                maleHeadModel2 = buffer.readUnsignedShort();
            else if (opcode == 93)
                femaleHeadModel2 = buffer.readUnsignedShort();
            else if (opcode == 94)
                buffer.readUnsignedShort(); // category
            else if (opcode == 95)
                zan2d = buffer.readUnsignedShort();
            else if (opcode == 97)
                certID = buffer.readUnsignedShort();
            else if (opcode == 98)
                certTemplateID = buffer.readUnsignedShort();
            else if (opcode >= 100 && opcode < 110) {
                if (stackIDs == null) {
                    stackIDs = new int[10];
                    stackAmounts = new int[10];
                }
                stackIDs[opcode - 100] = buffer.readUnsignedShort();
                stackAmounts[opcode - 100] = buffer.readUnsignedShort();
            } else if (opcode == 110)
                resizeX = buffer.readUnsignedShort();
            else if (opcode == 111)
                resizeY = buffer.readUnsignedShort();
            else if (opcode == 112)
                resizeZ = buffer.readUnsignedShort();
            else if (opcode == 113)
                brightness = buffer.readSignedByte();
            else if (opcode == 114)
                contrast = buffer.readSignedByte() * 5;
            else if (opcode == 115)
                team = buffer.readUnsignedByte();
            else if (opcode == 139)
                buffer.readUnsignedShort(); // boughtId
            else if (opcode == 140)
                buffer.readUnsignedShort(); // boughtTemplateId
            else if (opcode == 148)
                buffer.readUnsignedShort(); // placeholderId
            else if (opcode == 149)
                buffer.readUnsignedShort(); // placeholderTemplateId
            else if (opcode == 249) {
                int length = buffer.readUnsignedByte();
                for (int i = 0; i < length; i++) {
                    boolean isString = buffer.readUnsignedByte() == 1;
                    int key = buffer.read24Int();
                    if (isString) {
                        buffer.readStringCp1252NullTerminated();
                    } else {
                        buffer.readInt();
                    }
                }
            } else {
                throw new RuntimeException("Unknown item definition opcode " + opcode + ", last="+lastOpcode);
            }
            lastOpcode = opcode;
        } while (true);
    }

    private ItemDefinition() {
        id = -1;
    }

    private byte femaleOffset;
    private byte maleOffset;
    public int id;// anInt157
    public int team;
    public int value;// anInt155
    public int certID;
    public int inventoryModel;// dropModel
    public int maleHeadModel;
    public int femaleWield;// femWieldModel
    public int zan2d;// modelPositionUp
    public int femaleWield2;// femArmModel
    public int femaleHeadModel;
    public int maleWield2;// maleArmModel
    public int maleWield;// maleWieldModel
    public int modelZoom;
    public int modelOffset1;
    public int modelOffset2;//
    public int certTemplateID;
    public int modelRotationX;// modelRotateRight
    public int modelRotationY;// modelRotateUp
    public static int highestFileId;
    public int[] stackIDs;// modelStack
    public int[] stackAmounts;// itemAmount
    public short[] recolorTo;// newModelColor
    public short[] recolorFrom;
    public short[] retextureTo;
    public short[] retextureFrom;
    private int brightness;
    private int femaleModel2;
    private int maleHeadModel2;
    private int contrast;
    private int maleModel2;
    private int resizeZ;
    private int resizeY;
    private int resizeX;
    private int femaleHeadModel2;
    private int shiftDrop;
    private boolean stockMarket;
    private static int cacheIndex;
    private static int[] offsets;
    public boolean stackable;// itemStackable
    public boolean membersObject;// aBoolean161
    public static boolean isMembers = true;
    public String name;// itemName
    public String description;// itemExamine
    public String[] itemActions;// itemMenuOption
    public String[] groundActions;
    private static Buffer dataBuffer;
    private static ItemDefinition[] cache;
    public static Cache mruNodes1 = new Cache(100);
    public static Cache mruNodes2 = new Cache(50);

    @Override
    public int getHaPrice() {
        return 0;
    }

    @Override
    public boolean isStackable() {
        return stackable;
    }

    @Override
    public void setShiftClickActionIndex(int shiftClickActionIndex) {

    }

    @Override
    public void resetShiftClickActionIndex() {

    }

    @Override
    public int getIntValue(int paramID) {
        return 0;
    }

    @Override
    public void setValue(int paramID, int value) {

    }

    @Override
    public String getStringValue(int paramID) {
        return null;
    }

    @Override
    public void setValue(int paramID, String value) {

    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {

    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public int getNote() {
        return 0;
    }

    @Override
    public int getLinkedNoteId() {
        return 0;
    }

    @Override
    public int getPlaceholderId() {
        return 0;
    }

    @Override
    public int getPlaceholderTemplateId() {
        return 0;
    }

    @Override
    public int getPrice() {
        return value;
    }

    @Override
    public boolean isMembers() {
        return membersObject;
    }

    @Override
    public boolean isTradeable() {
        return false;
    }

    @Override
    public void setTradeable(boolean yes) {

    }

    @Override
    public int getIsStackable() {
        return stackable ? 1 : 0;
    }

    @Override
    public int getMaleModel() {
        return 0;
    }

    @Override
    public String[] getInventoryActions() {
        return itemActions;
    }

    @Override
    public String[] getGroundActions() {
        return groundActions;
    }

    @Override
    public int getShiftClickActionIndex() {
        return 0;
    }

    @Override
    public RSModel getModel(int quantity) {
        return null;
    }

    @Override
    public int getInventoryModel() {
        return 0;
    }

    @Override
    public short[] getColorToReplaceWith() {
        return new short[0];
    }

    @Override
    public short[] getTextureToReplaceWith() {
        return new short[0];
    }

    @Override
    public RSIterableNodeHashTable getParams() {
        return null;
    }

    @Override
    public void setParams(IterableHashTable params) {

    }

    @Override
    public void setParams(RSIterableNodeHashTable params) {

    }
}