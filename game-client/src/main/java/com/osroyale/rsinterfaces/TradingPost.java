package com.osroyale.rsinterfaces;

import com.osroyale.Client;
import com.osroyale.RSInterface;
import com.osroyale.TextDrawingArea;

import java.util.Objects;

public class TradingPost extends RSInterface {
    private static final int EXISTING_OFFERS_SIZE = 20;
    private static final int MAX_LISTINGS_SHOWN_SIZE = 50;

    public static void init(TextDrawingArea[] tda) {
        overviewPage(tda);
        sellingOverlay(tda);
        buyingPage(tda);
    }

    public static void overviewPage(TextDrawingArea[] tda) {
        RSInterface rsi = addInterface(80000);
        rsi.totalChildren(34);

        addSprite(80001,3468);
        rsi.child(0,80001,100,26);

        addHoverButton(80002, 3477, "Claim", -1, 80003, 1);
        rsi.child(1, 80002, 109, 35);

        addHoveredButton(80003, 3478, 80004);
        rsi.child(2, 80003, 109, 35);

        addText(80005,"503,389,234",tda,0,0x36d53e,false,true);
        rsi.child(3, 80005, 141, 36);

        addText(80006,"Marketplace",tda,2,0xff8624,false,true);
        rsi.child(4, 80006, 227, 34);

        addHoverButton(80007, 24, "Close", -1, 80008, 3);
        rsi.child(5, 80007, 400, 35);

        addHoveredButton(80008, 25, 80009);
        rsi.child(6, 80008, 400, 35);

        addHoverButton(80010, 3471, "Buy Item", -1, 80011, 1);
        rsi.child(7, 80010, 110, 64);

        addHoveredButton(80011, 3472, 80012);
        rsi.child(8, 80011, 110, 64);

        addHoverButton(80013, 3471, "Sell Item", -1, 80014, 1);
        rsi.child(9, 80013, 110, 100);

        addHoveredButton(80014, 3472, 80015);
        rsi.child(10, 80014, 110, 100);

        addSprite(80016,3470);
        rsi.child(11, 80016, 116, 67);

        addSprite(80017,3469);
        rsi.child(12, 80017, 116, 102);

        addText(80018,"Buy Item",tda,2,0xff981f,false,true);
        rsi.child(13, 80018, 140, 71);

        addText(80019,"Sell Item",tda,2,0xff981f,false,true);
        rsi.child(14, 80019, 140, 106);

        addHoverButton(80020, 3471, "Your History", -1, 80021, 1);
        rsi.child(15, 80020, 316, 64);

        addHoveredButton(80021, 3472, 80022);
        rsi.child(16, 80021, 316, 64);

        addHoverButton(80023, 3471, "Global History", -1, 80024, 1);
        rsi.child(17, 80023, 316, 100);

        addHoveredButton(80024, 3472, 80025);
        rsi.child(18, 80024, 316, 100);

        addSprite(80026,3474);
        rsi.child(19, 80026, 319, 69);
        rsi.child(20, 80026, 319, 105);

        addWrappingText(80027,"Your History",tda,0,0xff981f,true,true,4);
        rsi.child(21, 80027, 372, 54);

        addWrappingText(80028,"Global History",tda,0,0xff981f,true,true,6);
        rsi.child(22, 80028, 372, 91);

        RSInterface scroll = addTabInterface(80029);
        int spriteHeight = Objects.requireNonNull(Client.spriteCache.get(3475)).height; // 38
        scroll.width = 284;
        scroll.height = 156;
        scroll.scrollMax = spriteHeight * 20;
        scroll.totalChildren(101);

        rsi.child(23,80029,113,138);

        int y = 0;

        addSprite(80030,3475);
        addSprite(80031,3476);

        for(int i = 0; i < EXISTING_OFFERS_SIZE; i++) {
            if(i % 2 == 0) {
                scroll.child(i,80030,0,y);
            } else {
                scroll.child(i,80031,0,y);
            }

            addProgressBar(80032+i, 25, 120, 17, true, false);
            scroll.child(i+20,80032+i,35,y+12);

            addHoverButton(80052+i, 3508, "Dismiss", -1, 80072+i, 1);
            scroll.child(i+40,80052+i,265,y+13);

            addHoveredButton(80072+i, 3509, 80092+i);
            scroll.child(i+60,80072+i,265,y+13);

            addText(80112+i,"25/100",tda,1,0xff8624,false,true);
            scroll.child(i+80,80112+i,165,y+11);

            y += spriteHeight;
        }

        addToItemGroup(80132,1,20,1,6,false,"","","");
        scroll.child(100,80132,0,1);

        for(int i = 0; i < EXISTING_OFFERS_SIZE; i++) {
            RSInterface.interfaceCache[80132].inv[i] = 1154;
            RSInterface.interfaceCache[80132].invStackSizes[i] = 1;
        }

        addHoverButton(80133, 3506, "Search Item", -1, 80134, 1);
        rsi.child(24, 80133, 213, 64);

        addHoveredButton(80134, 3507, 80135);
        rsi.child(25, 80134, 213, 64);

        addHoverButton(80136, 3506, "Search Player", -1, 80137, 1);
        rsi.child(26, 80136, 213, 88);

        addHoveredButton(80137, 3507, 80138);
        rsi.child(27, 80137, 213, 88);

        addHoverButton(80139, 3506, "Most Recent", -1, 80140, 1);
        rsi.child(28, 80139, 213, 112);

        addHoveredButton(80140, 3507, 80141);
        rsi.child(29, 80140, 213, 112);

        addText(80142,"Search Item",tda,0,0xff981f,false,true);
        rsi.child(30, 80142, 233, 67);

        addText(80143,"Search Player",tda,0,0xff981f,false,true);
        rsi.child(31, 80143, 233, 91);

        addText(80144,"Most Recent",tda,0,0xff981f,false,true);
        rsi.child(32, 80144, 233, 115);

        addTabInterface(80145); //overlay placeholder to get hovers working
        rsi.child(33,80145,0,0);
    }

    public static void sellingOverlay(TextDrawingArea[] tda) {
        RSInterface sellingOverlay = addTabInterface(80146);
        sellingOverlay.totalChildren(22);

        addRectangle(80147,100 ,0,true , 325, 280);
        sellingOverlay.child(0,80147,100,26);

        addSprite(80148,3497);
        sellingOverlay.child(1,80148,182,59);

        addText(80149,"Sell",tda,2,0xff8624,false,true);
        sellingOverlay.child(2,80149,250,69);

        addHoverButton(80150, 24, "Close", -1, 80151, 1);
        sellingOverlay.child(3, 80150, 321, 68);

        addHoveredButton(80151, 25, 80152);
        sellingOverlay.child(4, 80151, 321, 68);

        addToItemGroup(80153,1,1,0,0,false,"","","");
        sellingOverlay.child(5, 80153, 198, 101);

        RSInterface.interfaceCache[80153].inv[0] = 1154;
        RSInterface.interfaceCache[80153].invStackSizes[0] = 1;

        addHoverButton(80154, 3500, "Increment Quantity", -1, 80155, 1);
        sellingOverlay.child(6, 80154, 314, 112);

        addHoveredButton(80155, 3501, 80156);
        sellingOverlay.child(7, 80155, 314, 112);

        addHoverButton(80157, 3499, "Decrement Quantity", -1, 80158, 1);
        sellingOverlay.child(8, 80157, 246, 112);

        addHoveredButton(80158, 3502, 80159);
        sellingOverlay.child(9, 80158, 246, 112);

        addText(80160,"Iron Full Helm",tda,0,0xff8624,false,true);
        sellingOverlay.child(10, 80160, 200, 151);

        addText(80161,"Price: @gre@500m",tda,0,0xe39b45,false,true);
        sellingOverlay.child(11, 80161, 200, 165);

        addText(80162,"Quantity: @gre@50",tda,0,0xe39b45,false,true);
        sellingOverlay.child(12, 80162, 200, 178);

        addText(80163,"Total: @gre@5.05m",tda,0,0xe39b45,false,true);
        sellingOverlay.child(13, 80163, 200, 192);

        addHoverButton(80164, 3503, "Set Item Price", -1, 80165, 1);
        sellingOverlay.child(14, 80164, 192, 214);

        addHoveredButton(80165, 3504, 80166);
        sellingOverlay.child(15, 80165, 192, 214);

        addHoverButton(80167, 3503, "Confirm sale", -1, 80168, 1);
        sellingOverlay.child(16, 80167, 192, 243);

        addHoveredButton(80168, 3504, 80169);//was at 60169
        sellingOverlay.child(17, 80168, 192, 243);

        addText(80170,"Set Item Price",tda,0,0x8a8a8a,false,true);
        sellingOverlay.child(18, 80170, 231, 220);

        addText(80171,"Confirm Listing",tda,0,0x8a8a8a,false,true);
        sellingOverlay.child(19, 80171, 228, 248);

        addButton(80172,3505,"Edit Quantity");
        sellingOverlay.child(20,80172,264,112);

        addText(80173,"1",tda,0,0x8a8a8a,true,true);
        sellingOverlay.child(21,80173,289,114);

        RSInterface.interfaceCache[RSInterface.interfaceCache[80000].children[33]] = sellingOverlay;
        sellingOverlay.isHidden = true;

        inventoryOverlay();
    }

    public static void inventoryOverlay() {
        RSInterface rsi = addInterface(65300);
        rsi.totalChildren(1);
        addContainer(65301, RSInterface.interfaceCache[5064].width, RSInterface.interfaceCache[5064].height, RSInterface.interfaceCache[5064].invSpritePadX, RSInterface.interfaceCache[5064].invSpritePadY, false, "Select", null, null, null, null);
        rsi.child(0,65301,16,8);
    }

    public static void buyingPage(TextDrawingArea[] tda) {
        RSInterface rsi = addInterface(80174);
        rsi.totalChildren(32);

        addSprite(80175,3481);
        rsi.child(0,80175,1,1);

        addText(80176,"Recent Items",tda,2,0xff8624,true,true);
        rsi.child(1,80176,255,11);

        addHoverButton(80177, 24, "Close", -1, 80178, 3);
        rsi.child(2, 80177, 486, 10);

        addHoveredButton(80178, 25, 80179);
        rsi.child(3, 80178, 486, 10);

        addText(80180,"Quantity",tda,0,0xff8624,false,true);
        rsi.child(4, 80180, 15, 41);

        addText(80181,"Name",tda,0,0xff8624,false,true);
        rsi.child(5, 80181, 127, 41);

        addText(80182,"Price",tda,0,0xff8624,false,true);
        rsi.child(6, 80182, 251, 41);

        addText(80183,"Seller",tda,0,0xff8624,false,true);
        rsi.child(7, 80183, 365, 41);

        addButton(80184,3485,"Sort");
        rsi.child(8, 80184, 60, 45);

        addButton(80185,3485,"Sort");
        rsi.child(9, 80185, 280, 45);

        addHoverButton(80186, 3494, "Return", -1, 80187, 1);
        rsi.child(10, 80186, 11, 298);

        addHoveredButton(80187, 3495, 80188);
        rsi.child(11, 80187, 11, 298);

        addHoverButton(80189, 3486, "Previous Page", -1, 80190, 1);
        rsi.child(12, 80189, 89, 298);

        addHoveredButton(80190, 3487, 80191);
        rsi.child(13, 80190, 89, 298);

        addHoverButton(80192, 3486, "Refresh Page", -1, 80193, 1);
        rsi.child(14, 80192, 193, 298);

        addHoveredButton(80193, 3487, 80194);
        rsi.child(15, 80193, 193, 298);

        addHoverButton(80195, 3486, "Next Page", -1, 80196, 1);
        rsi.child(16, 80195, 297, 298);

        addHoveredButton(80196, 3487, 80197);
        rsi.child(17, 80196, 297, 298);

        addHoverButton(80198, 3486, "Modify", -1, 80199, 1);
        rsi.child(18, 80198, 401, 298);

        addHoveredButton(80199, 3487, 80200);
        rsi.child(19, 80199, 401, 298);

        addText(80201,"Return",tda,0,0xff8624,false,true);
        rsi.child(20, 80201, 35, 304);

        addSprite(80202,3488);
        rsi.child(21, 80202, 15, 302);

        addText(80203,"Previous Page",tda,0,0xff8624,false,true);
        rsi.child(22, 80203, 110, 304);

        addSprite(80204,3490);
        rsi.child(23, 80204, 93, 303);

        addText(80205,"Next Page",tda,0,0xff8624,false,true);
        rsi.child(24, 80205, 325, 304);

        addSprite(80206,3489);
        rsi.child(25, 80206, 302, 303);

        addText(80207,"Refresh Page",tda,0,0xff8624,false,true);
        rsi.child(26, 80207, 219, 304);

        addSprite(80208,3480);
        rsi.child(27, 80208, 200, 303);

        addText(80209,"Modify Search",tda,0,0xff8624,false,true);
        rsi.child(28, 80209, 424, 304);

        addSprite(80210,3491);
        rsi.child(29, 80210, 404, 302);

        RSInterface scroll = addTabInterface(80211);
        scroll.scrollMax = 1700;
        scroll.width = 470;
        scroll.height = 237;
        scroll.totalChildren(401);
        rsi.child(30, 80211, 13, 55);

        addSprite(80212,3482);
        addSprite(80213,3483); // height 34

        int y = 0;
        for(int i = 0; i < MAX_LISTINGS_SHOWN_SIZE; i++) {
            if(i%2==0) {
                scroll.child(i,80212,0,y);
            } else {
                scroll.child(i,80213,0,y);
            }

            addWrappingText(80214+i,"Abyssal whip",tda,0,0xff8624,true,true,100);
            scroll.child(i+50,80214+i,79,y+4);

            addText(80264+i,"3,068,704",tda,0,0xff8624,true,true);
            scroll.child(i+100,80264+i,253,y+4);

            addText(80314+i,"= 1,534,352 (ea)",tda,0,0xaeaaa5,true,true);
            scroll.child(i+150,80314+i,253,y+17);

            addText(80364+i,"John",tda,0,0xff8624,true,true);
            scroll.child(i+200,80364+i,367,y+10);

            addHoverButton(80414+i, 3492, "Buy", -1, 80464+i, 1);
            scroll.child(i+250, 80414+i, 415, y+4);

            addHoveredButton(80464+i, 3493, 80514+i);
            scroll.child(i+300, 80464+i, 415, y+4);

            addText(80564+i,"Buy",tda,0,0xff8624,false,true);
            scroll.child(i+350,80564+i,427,y+10);

            y += 34;
        }

        addToItemGroup(80614,1,50,0,2,false,"","","");
        scroll.child(400,80614,10,0);

        for(int i = 0; i < MAX_LISTINGS_SHOWN_SIZE; i++) {
            RSInterface.interfaceCache[80614].inv[i] = 4152;
            RSInterface.interfaceCache[80614].invStackSizes[i] = i+1;
        }

        addText(80615,"Page: 0",tda,0,0xff8624,false,true);
        rsi.child(31, 80615, 13, 13);

        history(tda);

    }

    public static void history(TextDrawingArea[] tda) {
        RSInterface rsi = addInterface(80616);
        rsi.totalChildren(18);

        addSprite(80617,3481);
        rsi.child(0,80617,1,1);

        addText(80618,"Item history",tda,2,0xff8624,true,true);
        rsi.child(1,80618,255,11);

        addHoverButton(80619, 24, "Close", -1, 80620, 3);
        rsi.child(2, 80619, 486, 10);

        addHoveredButton(80620, 25, 80621);
        rsi.child(3, 80620, 486, 10);

        addText(80622,"Quantity",tda,0,0xff8624,false,true);
        rsi.child(4, 80622, 15, 41);

        addText(80623,"Name",tda,0,0xff8624,false,true);
        rsi.child(5, 80623, 115, 41);

        addText(80624,"Price",tda,0,0xff8624,false,true);
        rsi.child(6, 80624, 225, 41);

        addText(80625,"Seller",tda,0,0xff8624,false,true);
        rsi.child(7, 80625, 335, 41);

        addText(80626,"Buyer",tda,0,0xff8624,false,true);
        rsi.child(8, 80626, 429, 41);

        addHoverButton(80627, 3486, "Modify", -1, 80628, 1);
        rsi.child(9, 80627, 401, 298);

        addHoveredButton(80628, 3487, 80629);
        rsi.child(10, 80628, 401, 298);

        addText(80630,"Modify Search",tda,0,0xff8624,false,true);
        rsi.child(11, 80630, 424, 304);

        addSprite(80631,3491);
        rsi.child(12, 80631, 404, 302);

        RSInterface scroll = addTabInterface(80632);
        scroll.scrollMax = 1700;
        scroll.width = 470;
        scroll.height = 237;
        rsi.child(13, 80632, 13, 55);

        scroll.totalChildren(251);

        int y = 0;

        for(int i = 0; i < MAX_LISTINGS_SHOWN_SIZE; i++) {
            if(i%2==0) {
                scroll.child(i,80212,0,y);
            } else {
                scroll.child(i,80213,0,y);
            }

            addWrappingText(80753+i,"Abyssal whip",tda,0,0xff8624,true,true,100);
            scroll.child(i+50,80753+i,63,y+4);

            addText(80803+i,"3,068,704",tda,0,0xff8624,true,true);
            scroll.child(i+100,80803+i,228,y+10);

            addText(80853+i,"JohnJohnJohn",tda,0,0xff8624,true,true);
            scroll.child(i+150,80853+i,337,y+10);

            addText(80903+i,"JohnJohnJohn",tda,0,0xff8624,true,true);
            scroll.child(i+200,80903+i,430,y+10);

            y+=34;
        }

        addToItemGroup(80953,1,50,0,2,false,"","","");
        scroll.child(250,80953,10,0);

        addHoverButton(80954, 3494, "Return", -1, 80955, 1);
        rsi.child(14, 80954, 11, 298);

        addHoveredButton(80955, 3495, 80956);
        rsi.child(15, 80955, 11, 298);

        addSprite(80957,3488);
        rsi.child(16, 80957, 15, 302);

        addText(80958,"Return",tda,0,0xff8624,false,true);
        rsi.child(17, 80958, 35, 304);
    }
}
