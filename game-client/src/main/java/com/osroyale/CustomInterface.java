package com.osroyale;

import com.osroyale.rsinterfaces.KeepsakeOverrides;
import com.osroyale.rsinterfaces.TradingPost;

import java.util.ArrayList;

/**
 * Hard-coding for all custom interfaces.
 *
 * @author Daniel
 */
public class CustomInterface extends RSInterface {

    private static void misc() {
        RSInterface energy = interfaceCache[149];
        energy.textColor = 0xff9933;
    }

    /** Loads all the custom interfaces. */
    static void decode(StreamLoader archive, TextDrawingArea textDrawingAreas[]) {
        misc();
        barrowsChest(textDrawingAreas);
        bounty(textDrawingAreas);
        KeepsakeOverrides.init(textDrawingAreas);
        simpleBounty(textDrawingAreas);
        simpleSafe(textDrawingAreas);
        dropViewer(textDrawingAreas);
        screenOptions(textDrawingAreas);
        quickPrayers(textDrawingAreas);
        itemsKeptOnDeath(textDrawingAreas);
        secondTrade(textDrawingAreas);
        firstTrade(textDrawingAreas);
        settings(textDrawingAreas);
        priceChecker(textDrawingAreas);
        starter(textDrawingAreas);
        boltEnchanting(textDrawingAreas);
        playerTitles(textDrawingAreas);
        shop(textDrawingAreas);
        giveInterface(textDrawingAreas);
        botLootInterface(textDrawingAreas);
        preloads(textDrawingAreas);
        bank(textDrawingAreas);
        teleport(textDrawingAreas);
        friendsTab(textDrawingAreas);
        ignoreTab(textDrawingAreas);
        equipmentScreen(textDrawingAreas);
        equipmentTab(textDrawingAreas);
        achievementTab(textDrawingAreas);
        informationTab(textDrawingAreas);
        questInterface(textDrawingAreas);
        serverSetting(textDrawingAreas);
        serverInterface(textDrawingAreas);
        firstDuel(textDrawingAreas);
        secondDuel(textDrawingAreas);
        thirdDuel(textDrawingAreas);
        duelEquipmentSlots(textDrawingAreas);
        clanManageScroll(textDrawingAreas);
        clanMemberManage(textDrawingAreas);
        clanManage(textDrawingAreas);
        clanChat(textDrawingAreas);
        clanInformation(textDrawingAreas);
        clanInformation2(textDrawingAreas);
        experienceSetting(textDrawingAreas);
//        personalStores(textDrawingAreas);
        colorChanger(textDrawingAreas);
        slayerMain(textDrawingAreas);
        slayerDuo(textDrawingAreas);
        slayerUnlock(textDrawingAreas);
        slayerReward(textDrawingAreas);
        slayerConfirm(textDrawingAreas);
        slayerTasks(textDrawingAreas);
        staffPanel(textDrawingAreas);
        toolPanel(textDrawingAreas);
        emote();
        hallOfFame(textDrawingAreas);
        playerProfiler(textDrawingAreas);
        donatorDeposit(textDrawingAreas);
        lostItems(textDrawingAreas);
        activityPanel(textDrawingAreas);
        prestige(textDrawingAreas);
        lootingBag(textDrawingAreas);
        tabCreation(textDrawingAreas);
        dropSimulator(textDrawingAreas);
        storeMenu(textDrawingAreas);
        storeSettingMenu(textDrawingAreas);
        keyBinding(textDrawingAreas);
        goodwillInterface(textDrawingAreas);
        clanShowcase(textDrawingAreas);
        settingsTab();
        displayTab(textDrawingAreas);
        chatTab();
        controlTab(textDrawingAreas);
        customTab(textDrawingAreas);
        donatorTab(textDrawingAreas);
        //        clanBank(textDrawingAreas);
        //        constructionBuild(textDrawingAreas);
        welcome(textDrawingAreas);
        gameRecords(textDrawingAreas);
        quickCurses(textDrawingAreas);
        mysteryBox(textDrawingAreas);
        runePouch(textDrawingAreas);
        achievementInterface(textDrawingAreas);
        bankVault(textDrawingAreas);
        gamePanel(textDrawingAreas);
        presetManager(textDrawingAreas);
        collectionLog(textDrawingAreas);
        TradingPost.init(textDrawingAreas);
        lmsLobby(textDrawingAreas);
        lmsOverlay(textDrawingAreas);
        gamblingInterface(textDrawingAreas);
        gambleInventory(textDrawingAreas);
        regularAutocast(archive, textDrawingAreas);
        ancientAutocast(archive, textDrawingAreas);
        skillGuides(archive, textDrawingAreas);
        wintertodt(archive, textDrawingAreas);
    }

    private static void wintertodt(StreamLoader archive, TextDrawingArea[] font) {
        int interfaceId = 41550;
        int childIndex = 0;
        RSInterface widget = addInterface(interfaceId++);

        widget.totalChildren(14); //14

        addSprite(interfaceId, 3617);
        widget.child(childIndex++, interfaceId++, 2, 25);

        addRectangle(interfaceId, 0, 0x008000, true, 198, 13);
        widget.child(childIndex++, interfaceId++, 3, 26);

        addText(interfaceId, "Wintertodt's Energy: 100%", font, 0, 0x000000, true, false);
        widget.child(childIndex++, interfaceId++, 108, 26);

        addText(interfaceId, "The Wintertodt returns in: 1:07", font, 0, 0xff991f, false, true);
        widget.child(childIndex++, interfaceId++, 2, 44);

        addSprite(interfaceId, 3618);
        widget.child(childIndex++, interfaceId++, 26, 84);

        addText(interfaceId, "Points\\n0", font, 0, 0xFF0000, true, true);
        widget.child(childIndex++, interfaceId++, 52, 111);

        addConfigButton(interfaceId, widget.interfaceId, 3619, 3616, 24, 23, "", 1, 0, 2222, false);
        widget.child(childIndex++, interfaceId++, 2, 60);

        addConfigButton(interfaceId, widget.interfaceId, 3619, 3616, 24, 23, "", 1, 0, 2223, false);
        widget.child(childIndex++, interfaceId++, 78, 60);

        addConfigButton(interfaceId, widget.interfaceId, 3619, 3616, 24, 23, "", 1, 0, 2224, false);
        widget.child(childIndex++, interfaceId++, 2, 136);

        addConfigButton(interfaceId, widget.interfaceId, 3619, 3616, 24, 23, "", 1, 0, 2225, false);
        widget.child(childIndex++, interfaceId++, 78, 136);

        addConfigButton(interfaceId, widget.interfaceId, 3613, 0, 2226);
        widget.child(childIndex++, interfaceId++, 12, 70);

        addConfigButton(interfaceId, widget.interfaceId, 3613, 0, 2227);
        widget.child(childIndex++, interfaceId++, 69, 70);

        addConfigButton(interfaceId, widget.interfaceId, 3613, 0, 2228);
        widget.child(childIndex++, interfaceId++, 12, 127);

        addConfigButton(interfaceId, widget.interfaceId, 3613, 0, 2229);
        widget.child(childIndex++, interfaceId++, 69, 127);
    }

    private static void skillGuides(StreamLoader archive, TextDrawingArea[] font) {
        int interfaceId = 42550; //44950
        int childIndex = 0;
        RSInterface widget = addInterface(interfaceId++);

        widget.totalChildren(22);

        addSprite(interfaceId, 3604);
        widget.child(childIndex++, interfaceId++, 0, 0);

        addSprite(interfaceId, 3605);
        widget.child(childIndex++, interfaceId++, 353, 56);

        addSprite(interfaceId, 3606);
        widget.child(childIndex++, interfaceId++, 353, 305);

        addHoverButton(interfaceId, 3607, 24, 23, "Close", -1, interfaceId + 1, 1);
        widget.child(childIndex++, interfaceId++, 483, 6);

        addHoveredButton(interfaceId, 3608, 24, 23, interfaceId + 1);
        widget.child(childIndex++, interfaceId++, 483, 6);
        interfaceId++;

        addText(interfaceId, "Title", font, 3, 0x46320A, true, false);
        widget.child(childIndex++, interfaceId++, 171, 6);

        addText(interfaceId, "Category", font, 0, 0x46320A, true, false);
        widget.child(childIndex++, interfaceId++, 171, 40);

        for(int index = 0, startY = 69; index < 14; index++, startY += 17) {
            addHoverText(interfaceId, "", "Open subsection", font, 3, 0x46320A, true, false, 119, 15, 1, true);
            widget.child(childIndex++, interfaceId++, 370, startY);
        }

        int infoTab = interfaceId++;
        RSInterface tab = addInterface(infoTab);
        tab.width = 286;
        tab.height = 228;
        tab.scrollMax = 1000;
        tab.totalChildren(1 + (100 * 2));
        int subChildId = 0;

        addToItemGroup(interfaceId, 1, 75, 20, 1, false);
        interfaceCache[interfaceId].displayAmount = false;
        for(int index = 0; index < (1 * 75); index++) {
            interfaceCache[interfaceId].inv[index] = 6200;
            interfaceCache[interfaceId].invStackSizes[index] = 1;
        }
        tab.child(subChildId++, interfaceId++, 20, 0);

        int y = 0;
        for(int index = 0; index < 100; index++, y += 33) {
            addText(interfaceId, "Info", font, 1, 0x46320A, false, false);
            tab.child(subChildId++, interfaceId++, 60, y);

            addText(interfaceId, "1", font, 3, 0x46320A, true, false);
            tab.child(subChildId++, interfaceId++, 10, y);
        }

        widget.child(childIndex++, infoTab, 30, 83);
    }

    private static void ancientAutocast(StreamLoader archive, TextDrawingArea[] font) {
        int interfaceId = 43950; //44950
        int childIndex = 0;
        RSInterface widget = addInterface(interfaceId++); //3516

        widget.totalChildren(2 + (ancients.length * 2));

        addSprite(interfaceId, 3516);
        widget.child(childIndex++, interfaceId++, 5, 174);

        addHoverText(interfaceId, "Cancel", "Cancel", font, 1, 0x8F8F8F, true, true, 36, 11, 1, true);
        widget.child(childIndex++, interfaceId++, 148, 159);

        int startX = 23, startY = 13;
        int index = 0;
        ArrayList<Integer> spellSprites = new ArrayList<>();
        for(AncientSpellData data : ancients) {
            if (data.spell.runes_data.length == 2)
                add2Runes(archive, interfaceId, data.spell.runes_data, data.spell.level, data.spell.spellName, data.spell.spellName, data.spell.description, font, data.spriteOff, data.spriteOn, data.spell.spellUseable, 5);
            else if (data.spell.runes_data.length == 3)
                add3Runes(archive, interfaceId, data.spell.runes_data, data.spell.level, data.spell.spellName, data.spell.description,font, data.spriteOff, data.spriteOn, data.spell.spellUseable, 5);
            else if (data.spell.runes_data.length == 4)
                add4Runes(archive, interfaceId, data.spell.runes_data, data.spell.level, data.spell.spellName, data.spell.description,font, data.spriteOff, data.spriteOn, data.spell.spellUseable, 5);

            spellSprites.add(interfaceId);
            widget.child(childIndex++, interfaceId++, startX, startY);

            startX += 40;
            index++;
            if(index % 4 == 0) {
                startX = 23;
                startY += regularStaff.length > 20 ? 25 : 31; //25 - staff of the dead
            }
        }

        index = 0;
        for(AncientSpellData data : ancients) {
            int extraIds = 0;
            if(data.spell.runes_data.length == 2)
                extraIds = build2RunesBoxSmall2(archive, interfaceId, data.spell.runes_data, data.spell.level, data.spell.spellName, data.spell.description, font);
            if(data.spell.runes_data.length == 3)
                extraIds = build3RunesBoxSmall2(archive, interfaceId, data.spell.runes_data, data.spell.level, data.spell.spellName, data.spell.description, font);
            else if(data.spell.runes_data.length == 4)
                extraIds = build4RunesBoxSmall2(archive, interfaceId, data.spell.runes_data, data.spell.level, data.spell.spellName, data.spell.description, font);

            interfaceCache[spellSprites.get(index++).intValue()].hoverType = interfaceId;

            widget.child(childIndex++, interfaceId++, 5, 174);
            interfaceId += extraIds;
        }
    }

    private static void regularAutocast(StreamLoader archive, TextDrawingArea[] font) { //
        int interfaceId = 44780;
        int childIndex = 0;
        RSInterface widget = addInterface(interfaceId++); //3516

        widget.totalChildren(2 + (regularStaff.length * 2));

        addSprite(interfaceId, 3516); //866
        widget.child(childIndex++, interfaceId++, 5, 174);

        addHoverText(interfaceId, "Cancel", "Cancel", font, 1, 0x8F8F8F, true, true, 36, 11, 1, true);
        widget.child(childIndex++, interfaceId++, 148, 159);

        int startX = 23, startY = 13;
        int index = 0;

        ArrayList<Integer> spellSprites = new ArrayList<>();
        for(AutocastData data : regularStaff) {
            if (data.spell.runes_data.length == 2)
                add2Runes(archive, interfaceId, data.spell.runes_data, data.spell.level, data.spell.spellName, data.spell.spellName, data.spell.description, font, data.spriteOff, data.spriteOn, data.spell.spellUseable, 5);
            else if (data.spell.runes_data.length == 3)
                add3Runes(archive, interfaceId, data.spell.runes_data, data.spell.level, data.spell.spellName, data.spell.description,font, data.spriteOff, data.spriteOn, data.spell.spellUseable, 5);
            else if (data.spell.runes_data.length == 4)
                add4Runes(archive, interfaceId, data.spell.runes_data, data.spell.level, data.spell.spellName, data.spell.description,font, data.spriteOff, data.spriteOn, data.spell.spellUseable, 5);

            spellSprites.add(interfaceId);
            widget.child(childIndex++, interfaceId++, startX, startY);

            startX += 40;
            index++;
            if(index % 4 == 0) {
                startX = 23;
                startY += regularStaff.length > 20 ? 25 : 31; //25 - staff of the dead
            }
        }

        index = 0;
        for(AutocastData data : regularStaff) {
            int extraIds = 0;
            if(data.spell.runes_data.length == 2)
                extraIds = build2RunesBoxSmall2(archive, interfaceId, data.spell.runes_data, data.spell.level, data.spell.spellName, data.spell.description, font);
            if(data.spell.runes_data.length == 3)
                extraIds = build3RunesBoxSmall2(archive, interfaceId, data.spell.runes_data, data.spell.level, data.spell.spellName, data.spell.description, font);
            else if(data.spell.runes_data.length == 4)
                extraIds = build4RunesBoxSmall2(archive, interfaceId, data.spell.runes_data, data.spell.level, data.spell.spellName, data.spell.description, font);

            interfaceCache[spellSprites.get(index++).intValue()].hoverType = interfaceId;

            widget.child(childIndex++, interfaceId++, 5, 174);
            interfaceId += extraIds;
        }
    }

    static AutocastData[] regularStaff = new AutocastData[] {
            new AutocastData(SpellData.WIND_STRIKE, 3518, 3547), new AutocastData(SpellData.WATER_STRIKE, 3519, 3546), new AutocastData(SpellData.EARTH_STRIKE, 3571, 3547), new AutocastData(SpellData.FIRE_STRIKE, 3570, 3546),
            new AutocastData(SpellData.WIND_BOLT, 3569, 3543), new AutocastData(SpellData.WATER_BOLT, 3568, 3542), new AutocastData(SpellData.EARTH_BOLT, 3567, 3541), new AutocastData(SpellData.FIRE_BOLT, 3566, 3540),
            new AutocastData(SpellData.WIND_BLAST, 3565, 3539), new AutocastData(SpellData.WATER_BLAST, 3564, 3538), new AutocastData(SpellData.EARTH_BLAST, 3563, 3537), new AutocastData(SpellData.FIRE_BLAST, 3562, 3536),
            new AutocastData(SpellData.AIR_WAVE, 3561, 3535), new AutocastData(SpellData.WATER_WAVE, 3560, 3534), new AutocastData(SpellData.EARTH_WAVE, 3559, 3533), new AutocastData(SpellData.FIRE_WAVE, 3558, 3532),
            new AutocastData(SpellData.WIND_SURGE, 3557, 3531), new AutocastData(SpellData.WATER_SURGE, 3556, 3530), new AutocastData(SpellData.EARTH_SURGE, 3555, 3529), new AutocastData(SpellData.FIRE_SURGE, 3554, 3528),
    };

    static AncientSpellData[] ancients = new AncientSpellData[] {
            new AncientSpellData(AncientsSpellData.SMOKE_RUSH, 3572, 3588), new AncientSpellData(AncientsSpellData.SHADOW_RUSH, 3573, 3589), new AncientSpellData(AncientsSpellData.BLOOD_RUSH, 3574, 3590), new AncientSpellData(AncientsSpellData.ICE_RUSH, 3575, 3591),
            new AncientSpellData(AncientsSpellData.SMOKE_BURST, 3576, 3592), new AncientSpellData(AncientsSpellData.SHADOW_BURST, 3577, 3593), new AncientSpellData(AncientsSpellData.BLOOD_BURST, 3578, 3594), new AncientSpellData(AncientsSpellData.ICE_BURST, 3579, 3595),
            new AncientSpellData(AncientsSpellData.SMOKE_BLITZ, 3580, 3596), new AncientSpellData(AncientsSpellData.SHADOW_BLITZ, 3581, 3597), new AncientSpellData(AncientsSpellData.BLOOD_BLITZ, 3582, 3598), new AncientSpellData(AncientsSpellData.ICE_BLITZ, 3583, 3599),
            new AncientSpellData(AncientsSpellData.SMOKE_BARRAGE, 3584, 3600), new AncientSpellData(AncientsSpellData.SHADOW_BARRAGE, 3585, 3601), new AncientSpellData(AncientsSpellData.BLOOD_BARRAGE, 3586, 3602), new AncientSpellData(AncientsSpellData.ICE_BARRAGE, 3587, 3603)
    };

    public static class AutocastData {

        public SpellData spell;
        public int spriteOn, spriteOff;

        public AutocastData(SpellData spell) {
            this(spell, spell.ordinal(), spell.ordinal());
        }

        public AutocastData(SpellData spell, int spriteOn, int spriteOff) {
            this.spell = spell;
            this.spriteOn = spriteOn;
            this.spriteOff = spriteOff;
        }
    }

    public static class AncientSpellData {

        public AncientsSpellData spell;
        public int spriteOn, spriteOff;

        public AncientSpellData(AncientsSpellData spell) {
            this(spell, spell.ordinal(), spell.ordinal());
        }

        public AncientSpellData(AncientsSpellData spell, int spriteOn, int spriteOff) {
            this.spell = spell;
            this.spriteOn = spriteOn;
            this.spriteOff = spriteOff;
        }
    }

    private static void gamblingInterface(TextDrawingArea[] font) {
        int interfaceId = 44750;
        int childIndex = 0;
        RSInterface widget = addInterface(interfaceId++);
        widget.width = 512;
        widget.height = 340;

        widget.totalChildren(17);

        int x = 10, y = 5;

        addSprite(interfaceId, 3512); //866
        widget.child(childIndex++, interfaceId++, x, y);

        addHoverButton(interfaceId, 3515, 148, 38, "Select Flower poker", -1, interfaceId + 1, 1);
        widget.child(childIndex++, interfaceId++, x + 177, y + 70);

        addHoveredButton(interfaceId, 3515, 148, 38, interfaceId + 1);
        widget.child(childIndex++, interfaceId++, x + 177, y + 70);
        interfaceId++;

        addHoverButton(interfaceId, 3513, 148, 38, "Select 55x2", -1, interfaceId + 1, 1);
        widget.child(childIndex++, interfaceId++, x + 177, y + 114);

        addHoveredButton(interfaceId, 3513, 148, 38, interfaceId + 1);
        widget.child(childIndex++, interfaceId++, x + 177, y + 114);
        interfaceId++;

        addConfigButton(interfaceId, widget.interfaceId, 234, 235, 15, 15, "", 1, 0, 444, false);
        widget.child(childIndex++, interfaceId++, x + 155, y + 82);

        addConfigButton(interfaceId, widget.interfaceId, 234, 235, 15, 15, "", 2, 0, 444, false);
        widget.child(childIndex++, interfaceId++, x + 155, y + 126);

        addHoverButton(interfaceId, 3514, 72, 32, "Accept", -1, interfaceId + 1, 1);
        widget.child(childIndex++, interfaceId++, x + 162, y + 286);

        addHoveredButton(interfaceId, 3514, 72, 32, interfaceId + 1);
        widget.child(childIndex++, interfaceId++, x + 162, y + 286);
        interfaceId++;

        addHoverButton(interfaceId, 3514, 72, 32, "Decline", -1, interfaceId + 1, 1);
        widget.child(childIndex++, interfaceId++, x + 269, y + 286);

        addHoveredButton(interfaceId, 3514, 72, 32, interfaceId + 1);
        widget.child(childIndex++, interfaceId++, x + 269, y + 286);
        interfaceId++;

        addHoverText(interfaceId, "Accept", "Accept", font, 2, 0xccaa44, true, true, 72, 32, 0, true);
        widget.child(childIndex++, interfaceId++, x + 162, y + 286);

        addHoverText(interfaceId, "Decline", "Decline", font, 2, 0xccaa44, true, true, 72, 32, 0, true);
        widget.child(childIndex++, interfaceId++, x + 269, y + 286);

        addText(interfaceId, "Player1", font, 1, 0xccaa44, true, true);
        widget.child(childIndex++, interfaceId++, x + 68, y + 290);

        addText(interfaceId, "Player2", font, 1, 0xccaa44, true, true);
        widget.child(childIndex++, interfaceId++, x + 432, y + 290);

        addToItemGroup(interfaceId, 3, 6, 12, 2, true, "Withdraw 1", "Withdraw 5", "Withdraw 10", "Withdraw X", "Withdraw All");
        interfaceCache[interfaceId].displayAmount = true;
        for(int index = 0; index < (3 * 6); index++) {
            interfaceCache[interfaceId].inv[index] = 6200;
            interfaceCache[interfaceId].invStackSizes[index] = 1;
        }
        widget.child(childIndex++, interfaceId++, x + 8, y + 66);

        addToItemGroup(interfaceId, 3, 6, 12, 2, false);
        interfaceCache[interfaceId].displayAmount = true;
        for(int index = 0; index < (3 * 6); index++) {
            interfaceCache[interfaceId].inv[index] = 6200;
            interfaceCache[interfaceId].invStackSizes[index] = 1;
        }
        widget.child(childIndex++, interfaceId++, x + 371, y + 66);
    }

    private static void gambleInventory(TextDrawingArea[] font) {
        int interfaceId = 44775; //44775
        int childIndex = 0;
        RSInterface widget = addInterface(interfaceId++);

        widget.totalChildren(1);

        addToItemGroup(interfaceId, 4, 7, 10, 4, true, "Gamble 1", "Gamble 5", "Gamble 10", "Gamble X", "Gamble All");
        interfaceCache[interfaceId].displayAmount = true;
        widget.child(childIndex++, interfaceId++, 16, 8);
    }

    private static void lmsOverlay(TextDrawingArea[] font) {
        int interfaceId = 44660;
        int childIndex = 0;
        RSInterface widget = addInterface(interfaceId++);

        widget.totalChildren(6);

        int baseX = 381, baseY = 6;

        addRectangle(interfaceId, 0, 0x383023, false, 113, 58);
        widget.child(childIndex++, interfaceId++, baseX, baseY);

        addRectangle(interfaceId, 0, 0x5A5245, false, 111, 56);
        widget.child(childIndex++, interfaceId++, baseX + 1, baseY + 1);

        addRectangle(interfaceId, 85, 0x5A5756, true, 109, 54);
        widget.child(childIndex++, interfaceId++, baseX + 2, baseY + 2);

        addText(interfaceId, "Survivors: 2/12", font, 0, 0xffffff, false, true);
        widget.child(childIndex++, interfaceId++, baseX + 8, baseY + 9);

        addText(interfaceId, "Kills: 5", font, 0, 0xffffff, false, true);
        widget.child(childIndex++, interfaceId++, baseX + 8, baseY + 23);

        addText(interfaceId, "Fog: Safe", font, 0, 0xffffff, false, true);
        widget.child(childIndex++, interfaceId++, baseX + 8, baseY + 37);
    }

    private static void lmsLobby(TextDrawingArea[] font) {
        int interfaceId = 44650;
        int childIndex = 0;
        RSInterface widget = addInterface(interfaceId++);

        widget.totalChildren(9);

        addRectangle(interfaceId, 0, 0x383023, false, 126, 44);
        widget.child(childIndex++, interfaceId++, 381, 6);

        addRectangle(interfaceId, 0, 0x5A5245, false, 124, 42);
        widget.child(childIndex++, interfaceId++, 382, 7);

        addRectangle(interfaceId, 85, 0x5A5756, true, 122, 40);
        widget.child(childIndex++, interfaceId++, 383, 8);

        addText(interfaceId, "Timer: ", font, 0, 0xffffff, false, true);
        widget.child(childIndex++, interfaceId++, 385, 10);

        addText(interfaceId, "0", font, 0, 0xffffff, false, true);
        interfaceCache[interfaceId].rightAlign = true;
        widget.child(childIndex++, interfaceId++, 501, 10);

        addText(interfaceId, "Game Type: ", font, 0, 0xffffff, false, true);
        widget.child(childIndex++, interfaceId++, 385, 22);

        addText(interfaceId, "N/A", font, 0, 0xffffff, false, true);
        interfaceCache[interfaceId].rightAlign = true;
        widget.child(childIndex++, interfaceId++, 501, 22);

        addText(interfaceId, "Players: ", font, 0, 0xffffff, false, true);
        widget.child(childIndex++, interfaceId++, 385, 34);

        addText(interfaceId, "0/24", font, 0, 0xffffff, false, true);
        interfaceCache[interfaceId].rightAlign = true;
        widget.child(childIndex++, interfaceId++, 501, 34);
    }

    public static int totalCollectionLogOptions = 50;

    private static void collectionLog(TextDrawingArea[] tda) {
        int interfaceId = 44500;
        int childIndex = 0;
        RSInterface rsi = addTabInterface(interfaceId++);

        String[] tabNames = {"Bosses", "Raids", "Clues", "Minigames", "Other"};

        rsi.totalChildren(10 + (tabNames.length * 2));

        int x = 6, y = 10;

        addSprite(interfaceId, 1733); //866
        rsi.child(childIndex++, interfaceId++, x, y);

        addHoverButton(interfaceId, 24, 16, 16, "Close", -1, interfaceId + 1, 3);
        rsi.child(childIndex++, interfaceId++, x + 472, y + 10);

        addHoveredButton(interfaceId, 25, 16, 16, interfaceId + 1);
        rsi.child(childIndex++, interfaceId++, x + 472, y + 10);
        interfaceId++;

        addText(interfaceId, "Collection Log", tda, 2, 0xff981f, true, true);
        rsi.child(childIndex++, interfaceId++, x + 250, y + 9);

        for (int i = 0; i < tabNames.length; ++i) {
            addConfigButton(interfaceId, rsi.parentID, 1734, 1735, 96, 20, tabNames[i], i, 1, 906, false);
            interfaceCache[interfaceId].hoveredSprite = Client.spriteCache.get(1735);
            rsi.child(childIndex++, interfaceId++, x + 10 + (i * 96), y + 35);

            addText(interfaceId, tabNames[i], tda, 1, 0xff981f, false, true);
            rsi.child(childIndex++, interfaceId++, x + 15 + (i * 96), y + 35);
        }

        addText(interfaceId, "Barrows Chest", tda, 2, 0xff981f, false, true);
        rsi.child(childIndex++, interfaceId++, x + 221, y + 57);

        addText(interfaceId, "Obtained: <col=FF0000>0/25</col>", tda, 0, 0xff981f, false, true);
        rsi.child(childIndex++, interfaceId++, x + 221, y + 80);

        addText(interfaceId, "Barrows Chests opened: <col=FFFFFF>0</col>", tda, 0, 0xff981f, false, true);
        interfaceCache[interfaceId].rightAlign = true;
        rsi.child(childIndex++, interfaceId++, x + 483, y + 80);

        addText(interfaceId, "", tda, 0, 0xFF0000, false, true);
        interfaceCache[interfaceId].rightAlign = true;
        rsi.child(childIndex++, interfaceId++, x + 484, y + 59);

        //Items layer
        int itemLayer = interfaceId++;
        int subIndex = 0;
        RSInterface items = addTabInterface(itemLayer);
        items.width = 258;
        items.height = 206;
        items.scrollMax = 800;

        items.totalChildren(1);

        addToItemGroup(interfaceId, 6, 20, 12, 6, false, "", "", "");
        interfaceCache[interfaceId].transparentItems = true;
        interfaceCache[interfaceId].displayAmount = true;
        for(int index = 0; index < (6 * 20); index++) {
            interfaceCache[interfaceId].inv[index] = 6200;
            interfaceCache[interfaceId].invStackSizes[index] = 0;
        }
        items.child(subIndex++, interfaceId++, 2, 5);

        //Options layer
        int optionLayer = interfaceId++;
        subIndex = 0;
        RSInterface options = addTabInterface(optionLayer);
        options.width = 186;
        options.height = 245;
        options.scrollMax = 750;

        options.totalChildren(2 * totalCollectionLogOptions);

        for (int i = 0, y2 = 0; i < 50; ++i, y2 += 15) {
            addConfigButton(interfaceId, options.parentID, i % 2 == 0 ? 1732 : 1736, 1737, 186, 15, "View", i, 1, 907, false);
            interfaceCache[interfaceId].hoveredSprite = Client.spriteCache.get(1737);
            options.child(subIndex++, interfaceId++, 0, y2);

            addText(interfaceId, "", tda, 1, 0xff981f, false, true);
            options.child(subIndex++, interfaceId++, 1, y2 - 2);
        }

        //Items layer
        rsi.child(childIndex++, itemLayer, x + 215, y + 97);
        //Options layer
        rsi.child(childIndex++, optionLayer, x + 11, y + 57);
    }

    /** Interface for creating custom loadouts. */
    private static void presetManager(TextDrawingArea[] tda) {
        RSInterface rsi = addTabInterface(57000);
        addSprite(57001, 408);
        addText(57002, "Preloading Gear Manager", tda, 2, 0xFF981F, true, true);
        addText(57003, "Equipment", tda, 1, 0xFF9933, true, true);
        addText(57004, "Inventory", tda, 1, 0xFF9933, true, true);
        addText(57005, "Skills", tda, 1, 0xFF9933, true, true);
        addHoverText(57006, "Open preset settings", "Handle extra settings", tda, 0, 0xBD7E42, false, true, 168);
        addText(57007, "Upload", tda, 0, 0x3ACCFC, true, true);
        addText(57008, "Delete", tda, 0, 0xCC2525, true, true);
        addText(57009, "Activate", tda, 0, 0x37BF43, true, true);
        addText(57010, "Spellbook", tda, 1, 0xFF981F, true, true);
        addText(57011, "Modern", tda, 0, 0xD9DBDE, true, true);
        addText(57012, "Quick Prayer", tda, 1, 0xFF981F, true, true);
        addText(57013, "Not set", tda, 0, 0xCC2525, true, true);
        addText(57014, "99", tda, 0, 0xFFFF32, true, true);
        addText(57015, "99", tda, 0, 0xFFFF32, true, true);
        addText(57016, "99", tda, 0, 0xFFFF32, true, true);
        addText(57017, "99", tda, 0, 0xFFFF32, true, true);
        addText(57018, "99", tda, 0, 0xFFFF32, true, true);
        addText(57019, "99", tda, 0, 0xFFFF32, true, true);
        addText(57020, "99", tda, 0, 0xFFFF32, true, true);
        addInputField(57021, 12, 0xFF981F, "Enter Preset name", 118, 22, false, false, "[A-Za-z0-9 ]");
        addContainer(57022, 3, 7, 8, 9, false, true, false, null, null, null, null, null);
        addContainer(57023, 4, 7, 5, 5, false, true, false, null, null, null, null, null);
        addText(57024, "2/10", tda, 0, 0xFF9933, true, true);
        addHoverButton(57026, 24, 16, 16, "Close", -1, 57027, 3);
        addHoveredButton(57027, 25, 16, 16, 57028);
        addHoverButton(57029, 413, 60, 28, "Upload preset", -1, 57030, 1);
        addHoveredButton(57030, 414, 60, 28, 57031);
        addHoverButton(57032, 411, 59, 28, "Delete preset", -1, 57033, 1);
        addHoveredButton(57033, 412, 59, 28, 57034);
        addHoverButton(57035, 416, 118, 28, "Activate preset", -1, 57036, 1);
        addHoveredButton(57036, 417, 118, 28, 57037);
        addHoverButton(57038, 77, 16, 16, "Help", -1, 57039, 1);
        addHoveredButton(57039, 78, 16, 16, 57040);
        addText(57041, "Cmb lvl: 126", tda, 0, 0xFFFF32, true, true);

        rsi.totalChildren(43);
        rsi.child(0, 57001, 10, 3);
        rsi.child(1, 57002, 255, 9);
        rsi.child(2, 57003, 203, 33);
        rsi.child(3, 57004, 349, 33);
        rsi.child(4, 57005, 460, 33);
        rsi.child(5, 57006, 25, 248);
        rsi.child(6, 57010, 205, 263 + 5);
        rsi.child(7, 57011, 205, 278 + 5);
        rsi.child(8, 57012, 205, 291 + 5);
        rsi.child(9, 57013, 205, 305 + 5);
        rsi.child(10, 57014, 465, 70 - 2 + 5);
        rsi.child(11, 57014, 481, 81 - 2 + 5);
        rsi.child(12, 57015, 465, 104 - 2 + 5);
        rsi.child(13, 57015, 481, 115 - 2 + 5);
        rsi.child(14, 57016, 465, 138 - 2 + 5);
        rsi.child(15, 57016, 481, 149 - 2 + 5);
        rsi.child(16, 57017, 465, 172 - 2 + 5);
        rsi.child(17, 57017, 481, 183 - 2 + 5);
        rsi.child(18, 57018, 465, 206 - 2 + 5);
        rsi.child(19, 57018, 481, 217 - 2 + 5);
        rsi.child(20, 57019, 465, 240 - 2 + 5);
        rsi.child(21, 57019, 481, 251 - 2 + 5);
        rsi.child(22, 57020, 465, 274 - 2 + 5);
        rsi.child(23, 57020, 481, 285 - 2 + 5);
        rsi.child(24, 57021, 19, 219);
        rsi.child(25, 57022, 150, 55 + 5);
        rsi.child(26, 57023, 275, 57 + 5);
        rsi.child(27, 57045, 13, 34);
        rsi.child(28, 57024, 76, 199);
        rsi.child(29, 57026, 478, 10);
        rsi.child(30, 57027, 478, 10);
        rsi.child(31, 57029, 19, 265);
        rsi.child(32, 57030, 19, 265);
        rsi.child(33, 57007, 57, 274);
        rsi.child(34, 57032, 78, 265);
        rsi.child(35, 57033, 78, 265);
        rsi.child(36, 57008, 115, 274);
        rsi.child(37, 57035, 19, 294);
        rsi.child(38, 57036, 19, 294);
        rsi.child(39, 57009, 103, 303);
        rsi.child(40, 57038, 458, 10);
        rsi.child(41, 57039, 458, 10);
        rsi.child(42, 57041, 463, 308);

        RSInterface scrollInterface = addTabInterface(57045);
        scrollInterface.scrollPosition = 0;
        scrollInterface.contentType = 0;
        scrollInterface.width = 107;
        scrollInterface.height = 160;
        scrollInterface.scrollMax = 280;
        scrollInterface.totalChildren(14 * 3);

        int x = 5;
        int y = -10;
        for (int i = 0, child = 0; i < 14 * 4; i += 4) {
            addHoverButton(57046 + i, 409, 113, 34, "Select", -1, 57046 + (i + 1), 1);
            addHoveredButton(57046 + (i + 1), 415, 113, 34, 57046 + (i + 2));
            addText(57046 + i + 3, "Preset #" + (57046 + i + 3), tda, 0, 0xFF981F, true, true);
            scrollInterface.child(child++, 57046 + i, x, y + 11);
            scrollInterface.child(child++, 57046 + (i + 1), x, y + 11);
            scrollInterface.child(child++, 57046 + (i + 3), x + 100 / 2, y + 24);
            y += 35;
        }
    }


    private static void gamePanel(TextDrawingArea[] tda) {
        RSInterface panel = addInterface(56600);
        addSprite(56601, 864);
        addText(56602, "Battlegrounds", tda, 3, 0xFFA500, true, true);
        addText(56603, "Alive:", tda, 0, 0xD9A13B, true, true);
        addText(56604, "", tda, 0, 0xD9A13B, true, true);
        addText(56605, "Team Kills:", tda, 0, 0xD9A13B, true, true);
        addText(56606, "36", tda, 0, 0xD1C0A1, true, true);
        addText(56607, "", tda, 0, 0xD1C0A1, true, true);
        addText(56608, "14", tda, 0, 0xD1C0A1, true, true);
        addText(56609, "2:17", tda, 3, 0xFFA500, true, true);

        addText(56610, "Apollo", tda, 0, 0xFFA500, true, true);
        addText(56611, "Kills: 5", tda, 0, 0xFFA500, true, true);
        addProgressBar(56612, 97, 72, 15, false, false);
        addText(56613, "97/99", tda, 0, 0xFFFFFF, true, true);
        //
        addText(56614, "Nan Smasher", tda, 0, 0xFFA500, true, true);
        addText(56615, "Kills: 2", tda, 0, 0xFFA500, true, true);
        addProgressBar(56616, 48, 72, 15, false, false);
        addText(56617, "48/99", tda, 0, 0xFFFFFF, true, true);
        //
        addText(56618, "Guthix Dad", tda, 0, 0xFFA500, true, true);
        addText(56619, "Kills: 0", tda, 0, 0xFFA500, true, true);
        addProgressBar(56620, 100, 72, 15, false, false);
        addText(56621, "100/99", tda, 0, 0xFFFFFF, true, true);
        //
        addText(56622, "Skimdoodle", tda, 0, 0xFFA500, true, true);
        addText(56623, "Kills: 1", tda, 0, 0xFFA500, true, true);
        addProgressBar(56624, 3, 72, 15, false, false);
        addText(56625, "3/99", tda, 0, 0xFFFFFF, true, true);

        panel.totalChildren(25);
        panel.child(0, 56601, -1, 259);
        panel.child(1, 56602, 400 + 45, 264);

        panel.child(2, 56603, 385 + 45, 290);
        panel.child(3, 56604, 385 + 45, 298);
        panel.child(4, 56605, 385 + 45, 306);

        panel.child(5, 56606, 425 + 45, 290);
        panel.child(6, 56607, 425 + 45, 298);
        panel.child(7, 56608, 425 + 45, 306);
        panel.child(8, 56609, 36, 285);

        panel.child(9, 56610, 114, 277);
        panel.child(10, 56611, 114, 295);
        panel.child(11, 56612, 79, 312);
        panel.child(12, 56613, 114, 313);

        panel.child(13, 56614, 114 + 74, 277);
        panel.child(14, 56615, 114 + 74, 295);
        panel.child(15, 56616, 79 + 74, 312);
        panel.child(16, 56617, 114 + 74, 313);

        panel.child(17, 56618, 114 + 74 + 74, 277);
        panel.child(18, 56619, 114 + 74 + 74, 295);
        panel.child(19, 56620, 79 + 74 + 74, 312);
        panel.child(20, 56621, 114 + 74 + 74, 313);

        panel.child(21, 56622, 114 + 74 + 74 + 74, 277);
        panel.child(22, 56623, 114 + 74 + 74 + 74, 295);
        panel.child(23, 56624, 79 + 74 + 74 + 74, 312);
        panel.child(24, 56625, 114 + 74 + 74 + 74, 313);
    }

    private static void bankVault(TextDrawingArea[] tda) {
        RSInterface rsint = addInterface(56300);
        addSprite(56301, 862);
        addText(56302, "Tarnish Bank Vault", tda, 2, 0xFFA500, true, true);
        addHoverButton(56303, 252, 21, 21, "Close", 250, 56304, 3);
        addHoveredButton(56304, 253, 21, 21, 56305);
        addText(56306, "This is your bank vault, here you can deposit", tda, 0, 0xFFA500, true, true);
        addText(56307, "and withdraw both coins and blood money", tda, 0, 0xFFA500, true, true);
        addText(56308, "which can exceed the 2.147M limit. ", tda, 0, 0xFFA500, true, true);
        addText(56309, "Gold Coins:", tda, 3, 0xFFA500, false, true);
        addText(56310, "Blood Money:", tda, 3, 0xFFA500, false, true);
        addText(56311, "100,453,500", tda, 3, 0x26C998, false, true);
        addText(56312, "48,540", tda, 3, 0x26C998, false, true);
        addText(56313, "Back to Bank", tda, 1, 0xFFA500, true, true);
        addContainer(56314, 0, 1, 5, 22, 32, 100, false, true, true);
        addButton(56315, 863, "Deposit Gold Coins");
        addButton(56316, 474, "Withdraw Gold Coins");
        addButton(56317, 863, "Deposit Blood Money");
        addButton(56318, 474, "Withdraw Blood Money");
        addHoverButton(56319, 391, 118, 32, "Go back to Bank", 0, 56320, 1);
        addHoveredButton(56320, 392, 118, 32, 56322);
        rsint.totalChildren(19);
        rsint.child(0, 56301, 55, 17);
        rsint.child(1, 56303, 430, 25);
        rsint.child(2, 56304, 430, 25);
        rsint.child(3, 56302, 253, 28);
        rsint.child(4, 56306, 253, 71);
        rsint.child(5, 56307, 253, 86);
        rsint.child(6, 56308, 253, 101);
        rsint.child(7, 56309, 145, 150);
        rsint.child(8, 56310, 145, 215);
        rsint.child(9, 56311, 225, 150);
        rsint.child(10, 56312, 240, 215);
        rsint.child(11, 56314, 88, 145);
        rsint.child(12, 56315, 400, 152);
        rsint.child(13, 56316, 420, 152);
        rsint.child(14, 56317, 400, 217);
        rsint.child(15, 56318, 420, 217);
        rsint.child(16, 56319, 195, 267);
        rsint.child(17, 56320, 195, 267);
        rsint.child(18, 56313, 253, 274);
    }

    private static void achievementInterface(TextDrawingArea[] tda) {
        RSInterface rsint = addInterface(56800);
        addSprite(56801, 861);
        addText(56802, "Tarnish Achievements", tda, 2, 0xFFA500, true, true);
        addHoverButton(56803, 252, 21, 21, "Close", -1, 56804, 1);
        addHoveredButton(56804, 253, 21, 21, 56805);
        addText(56806, "Desciption:", tda, 3, 0xFFA500, true, true);
        addText(56807, "Kill 100 players with Granite maul special", tda, 0, 0xBF8E22, true, true);
        addText(56808, "Rewards:", tda, 3, 0xFFA500, true, true);
        addText(56809, "5,000 Blood money", tda, 0, 0xBF8E22, true, true);
        addContainer(56810, 0, 6, 5, 22, 21, 100, false, true, true);
        addProgressBar(56811, 30, 228, 35, null);
        addText(56812, "30/100", tda, 0, 0xFFFFFF, true, true);
        addText(56813, "30%", tda, 0, 0xFFFFFF, true, true);
        addText(56814, "PvP Achievements", tda, 0, 0xF0A31F, true, true);
        addText(56815, "5/23 Completed", tda, 0, 0xBF9E63, true, true);
        addConfigSprite(56816, 171, 298, 0, 527);
        addConfigSprite(56817, 172, 298, 1, 527);
        addConfigSprite(56818, 170, 298, 2, 527);
        addConfigSprite(56819, 173, 298, 3, 527);
        addConfigSprite(56820, 174, 298, 4, 527);
        rsint.totalChildren(20);
        rsint.child(0, 56801, 65, 32);
        rsint.child(1, 56802, 253, 40);
        rsint.child(2, 56803, 418, 39);
        rsint.child(3, 56804, 418, 39);
        rsint.child(4, 56806, 325, 90);
        rsint.child(5, 56807, 325, 115);
        rsint.child(6, 56808, 325, 145);
        rsint.child(7, 56809, 325, 170);
        rsint.child(8, 56810, 305, 190);
        rsint.child(9, 56811, 213, 268);
        rsint.child(10, 56812, 325, 270);
        rsint.child(11, 56813, 325, 286);
        rsint.child(12, 56814, 138, 271);
        rsint.child(13, 56815, 138, 285);
        rsint.child(14, 56830, -50, 66);
        rsint.child(15, 56816, 70, 37);
        rsint.child(16, 56817, 70, 37);
        rsint.child(17, 56818, 70, 37);
        rsint.child(18, 56819, 70, 37);
        rsint.child(19, 56820, 70, 37);

        int size = 80;
        int y = 0;

        RSInterface scroll = addTabInterface(56830);
        scroll.scrollPosition = 0;
        scroll.contentType = 0;
        scroll.width = 243;
        scroll.height = 197;
        scroll.scrollMax = 300;
        scroll.totalChildren(size);

        for (int i = 0, child = 0, count = 0; i < size; i += 2, count++) {
            int sprite = count % 2 == 0 ? 856 : 857;
            addButton(56831 + i, sprite, "View achievement");
            scroll.child(child++, 56831 + i, 120, y);
            addText(56831 + (i + 1), "" + (56831 + (i + 1)), tda, 0, 0xFFA500, false, true);
            scroll.child(child++, 56831 + (i + 1), 124, y + 5);
            y += 20;
        }
    }

    private static void runePouch(TextDrawingArea[] tda) {
        RSInterface tab = addInterface(41700);
        addSprite(41701, 457);
        addText(41702, "Rune pouch", tda, 2, 0xFFA500, true, true);
        addText(41703, "Pouch", tda, 1, 0xFFA500, true, true);
        addText(41704, "Inventory", tda, 2, 0xFFA500, true, true);
        addHoverButton(41707, 252, 21, 21, "Close", -1, 41708, 1);
        addHoveredButton(41708, 253, 21, 21, 41709);
        addContainer(41710, 3, 1, 22, 1, false, true, true, "Withdraw-1", "Withdraw-10", "Withdraw-100", "Withdraw-All", "Withdraw-X");
        addContainer(41711, 7, 4, 14, 2, false, true, true, "Deposit-1", "Deposit-10", "Deposit-100", "Deposit-All", "Deposit-X");

        tab.totalChildren(8);
        tab.child(0, 41701, 0, 0);
        tab.child(1, 41702, 253, 27);
        tab.child(2, 41703, 253, 62);
        tab.child(3, 41704, 253, 137);
        tab.child(4, 41707, 406, 26);
        tab.child(5, 41708, 406, 26);
        tab.child(6, 41710, 188, 84);
        tab.child(7, 41711, 105, 154);
    }

    private static void mysteryBox(TextDrawingArea[] TDA) {
        RSInterface rsinterface = addInterface(59500);
        addSprite(59501, 437);
        addText(59502, "Mystery Box", 0xFF9900, true, true, 52, TDA, 2);
        addText(59503, "Level 1", 0x99401C, true, true, 52, TDA, 2);
        addText(59504, "You must have a mystery box in your", 0xFF9900, true, true, 52, TDA, 0);
        addText(59505, "inventory for you to spin.", 0xFF9900, true, true, 52, TDA, 0);
        addText(59506, "May the odds be forever in your favor.", 0xFF9900, true, true, 52, TDA, 0);
        addText(59507, "You have 1 mystery box available!", 0xFFFFFF, true, true, 52, TDA, 0);
        addText(59508, "Let's Spin!", 0x37991C, true, true, 52, TDA, 3);
        addHoverButton(59509, 391, 118, 32, "Let's Spin!", 0, 59510, 1);
        addHoveredButton(59510, 392, 118, 32, 59511);
        addContainer(59512, 11, 10, 9, 15, false, true, false);
        addSprite(59514, 436);
        addConfigButton(59515, 59500, 298, 846, 15, 15, "", 0, 1, 430, false);
        addConfigButton(59516, 59500, 298, 847, 15, 15, "", 1, 1, 430, false);
        addConfigButton(59517, 59500, 298, 848, 15, 15, "", 2, 1, 430, false);
        addConfigButton(59518, 59500, 298, 846, 15, 15, "", 0, 1, 431, false);
        addConfigButton(59519, 59500, 298, 847, 15, 15, "", 1, 1, 431, false);
        addConfigButton(59520, 59500, 298, 848, 15, 15, "", 2, 1, 431, false);
        addConfigButton(59521, 59500, 298, 846, 15, 15, "", 0, 1, 432, false);
        addConfigButton(59522, 59500, 298, 847, 15, 15, "", 1, 1, 432, false);
        addConfigButton(59523, 59500, 298, 848, 15, 15, "", 2, 1, 432, false);
        addConfigButton(59524, 59500, 298, 846, 15, 15, "", 0, 1, 433, false);
        addConfigButton(59525, 59500, 298, 847, 15, 15, "", 1, 1, 433, false);
        addConfigButton(59526, 59500, 298, 848, 15, 15, "", 2, 1, 433, false);
        addConfigButton(59527, 59500, 298, 846, 15, 15, "", 0, 1, 434, false);
        addConfigButton(59528, 59500, 298, 847, 15, 15, "", 1, 1, 434, false);
        addConfigButton(59529, 59500, 298, 848, 15, 15, "", 2, 1, 434, false);
        addConfigButton(59530, 59500, 298, 846, 15, 15, "", 0, 1, 435, false);
        addConfigButton(59531, 59500, 298, 847, 15, 15, "", 1, 1, 435, false);
        addConfigButton(59532, 59500, 298, 848, 15, 15, "", 2, 1, 435, false);
        addConfigButton(59533, 59500, 298, 846, 15, 15, "", 0, 1, 436, false);
        addConfigButton(59534, 59500, 298, 847, 15, 15, "", 1, 1, 436, false);
        addConfigButton(59535, 59500, 298, 848, 15, 15, "", 2, 1, 436, false);
        addConfigButton(59536, 59500, 298, 846, 15, 15, "", 0, 1, 437, false);
        addConfigButton(59537, 59500, 298, 847, 15, 15, "", 1, 1, 437, false);
        addConfigButton(59538, 59500, 298, 848, 15, 15, "", 2, 1, 437, false);
        addConfigButton(59539, 59500, 298, 846, 15, 15, "", 0, 1, 438, false);
        addConfigButton(59540, 59500, 298, 847, 15, 15, "", 1, 1, 438, false);
        addConfigButton(59541, 59500, 298, 848, 15, 15, "", 2, 1, 438, false);
        addConfigButton(59542, 59500, 298, 846, 15, 15, "", 0, 1, 439, false);
        addConfigButton(59543, 59500, 298, 847, 15, 15, "", 1, 1, 439, false);
        addConfigButton(59544, 59500, 298, 848, 15, 15, "", 2, 1, 439, false);
        addConfigButton(59545, 59500, 298, 846, 15, 15, "", 0, 1, 440, false);
        addConfigButton(59546, 59500, 298, 847, 15, 15, "", 1, 1, 440, false);
        addConfigButton(59547, 59500, 298, 848, 15, 15, "", 2, 1, 440, false);

        rsinterface.totalChildren(47);
        rsinterface.child(0, 59501, 25, 13);
        rsinterface.child(1, 59502, 258, 21);
        rsinterface.child(2, 32303, 460, 17);
        rsinterface.child(3, 59503, 258, 145);
        rsinterface.child(4, 59504, 258, 160);
        rsinterface.child(5, 59505, 258, 175);
        rsinterface.child(6, 59506, 258, 190);
        rsinterface.child(7, 59507, 258, 209);
        rsinterface.child(8, 59509, 196, 231);
        rsinterface.child(9, 59510, 196, 231);
        rsinterface.child(10, 59508, 258, 237);
        rsinterface.child(11, 59580, 5, 46);
        rsinterface.child(12, 59515, 31, 275);
        rsinterface.child(13, 59516, 31, 275);
        rsinterface.child(14, 59517, 31, 275);
        rsinterface.child(15, 59518, 72, 275);
        rsinterface.child(16, 59519, 72, 275);
        rsinterface.child(17, 59520, 72, 275);
        rsinterface.child(18, 59521, 113, 275);
        rsinterface.child(19, 59522, 113, 275);
        rsinterface.child(20, 59523, 113, 275);
        rsinterface.child(21, 59524, 154, 275);
        rsinterface.child(22, 59525, 154, 275);
        rsinterface.child(23, 59526, 154, 275);
        rsinterface.child(24, 59527, 195, 275);
        rsinterface.child(25, 59528, 195, 275);
        rsinterface.child(26, 59529, 195, 275);
        rsinterface.child(27, 59530, 236, 275);
        rsinterface.child(28, 59531, 236, 275);
        rsinterface.child(29, 59532, 236, 275);
        rsinterface.child(30, 59533, 277, 275);
        rsinterface.child(31, 59534, 277, 275);
        rsinterface.child(32, 59535, 277, 275);
        rsinterface.child(33, 59536, 318, 275);
        rsinterface.child(34, 59537, 318, 275);
        rsinterface.child(35, 59538, 318, 275);
        rsinterface.child(36, 59539, 359, 275);
        rsinterface.child(37, 59540, 359, 275);
        rsinterface.child(38, 59541, 359, 275);
        rsinterface.child(39, 59542, 400, 275);
        rsinterface.child(40, 59543, 400, 275);
        rsinterface.child(41, 59544, 400, 275);
        rsinterface.child(42, 59545, 441, 275);
        rsinterface.child(43, 59546, 441, 275);
        rsinterface.child(44, 59547, 441, 275);
        rsinterface.child(45, 59512, 35, 278);
        rsinterface.child(46, 59514, 248, 266);

        RSInterface typeScroll = addTabInterface(59580);
        typeScroll.scrollPosition = 0;
        typeScroll.contentType = 0;
        typeScroll.width = 461;
        typeScroll.height = 82;
        typeScroll.scrollMax = 105;
        typeScroll.totalChildren(1);
        addContainer(59581, 10, 10, 12, 10, false, true, false);
        typeScroll.child(0, 59581, 30, 3);
    }

    private static void gameRecords(TextDrawingArea[] TDA) {
        RSInterface rsinterface = addInterface(32300);
        addSprite(32301, 826);
        addText(32302, "Game Records", 0xFF9900, true, true, 52, TDA, 2);
        addHoverButton(32303, 252, 21, 21, "Close", -1, 32304, 1);
        addHoveredButton(32304, 253, 21, 21, 32305);
        addMarqueeText(32306, 0xFF9900, false, 2, 0, TDA);
        addConfigButton(32307, 32300, 235, 234, 15, 15, "Toggle global records", 0, 1, 325, false);
        addText(32308, "Global", 0xFF9900, true, true, 52, TDA, 0);
        addConfigButton(32309, 32300, 235, 234, 15, 15, "Toggle global records", 1, 1, 325, false);
        addText(32310, "Personal", 0xFF9900, true, true, 52, TDA, 0);
        rsinterface.totalChildren(11);
        rsinterface.child(0, 32301, 12, 13);
        rsinterface.child(1, 32302, 270, 22);
        rsinterface.child(2, 32303, 470, 19);
        rsinterface.child(3, 32304, 470, 19);
        rsinterface.child(4, 32400, 66, 47);
        rsinterface.child(5, 32350, 15, 47);
        rsinterface.child(6, 32306, 270, 307);
        rsinterface.child(7, 32307, 25, 22);
        rsinterface.child(8, 32308, 58, 24);
        rsinterface.child(9, 32309, 85, 22);
        rsinterface.child(10, 32310, 125, 24);
        RSInterface typeScroll = addTabInterface(32350);
        typeScroll.scrollPosition = 0;
        typeScroll.contentType = 0;
        typeScroll.width = 124;
        typeScroll.height = 235;
        typeScroll.scrollMax = 300;
        typeScroll.totalChildren(30);
        int y = 0;
        int sprite = 0;
        for (int index = 0, child = 0; index < 30; index += 2) {
            int id = (sprite % 2 == 0 ? 831 : 832);
            addSprite((32351 + index), id);
            typeScroll.child(child++, (32351 + index), 3, y - 1);
            addHoverText((32351 + (index + 1)), "", "", TDA, 0, 0xFF9900, 0xFFFFFF, false, true, 168);
            typeScroll.child(child++, (32351 + (index + 1)), 10, y + 3);
            y += 20;
            sprite++;
        }

        RSInterface scrollInterface = addTabInterface(32400);
        scrollInterface.scrollPosition = 0;
        scrollInterface.contentType = 0;
        scrollInterface.width = 409;
        scrollInterface.height = 235;
        scrollInterface.scrollMax = 625;
        scrollInterface.totalChildren(125);
        y = 0;
        for (int index = 0, child = 0; index < 125; index += 5) {
            int id = 830;
            int color = 0xC9C2BF;
            if (index == 0) {
                id = 827;
                color = 0x000000;
            } else if (index == 5) {
                id = 828;
                color = 0x000000;
            } else if (index == 10) {
                id = 829;
                color = 0x000000;
            }
            addSprite(32401 + index, id);
            addText(32401 + (index + 1), (index / 5) + ")", color, true, false, 52, TDA, 1);
            addText(32401 + (index + 2), "Name", color, false, false, 52, TDA, 1);
            addText(32401 + (index + 3), "10:34", color, false, false, 52, TDA, 1);
            addText(32401 + (index + 4), "2017-11-21", color, false, false, 52, TDA, 1);
            scrollInterface.child(child++, 32401 + index, 95, y);
            scrollInterface.child(child++, 32401 + (index + 1), 107, y + 6);
            scrollInterface.child(child++, 32401 + (index + 2), 125, y + 6);
            scrollInterface.child(child++, 32401 + (index + 3), 225, y + 6);
            scrollInterface.child(child++, 32401 + (index + 4), 305, y + 6);
            y += 28;
        }
    }

    private static void welcome(TextDrawingArea[] TDA) {
        RSInterface rsinterface = addInterface(450);
        addSprite(21300, 778);
        addHoverButton(21301, 298, 367, 45, "Enter Tarnish", -1, 21302, 1);
        addHoveredButton(21302, 779, 367, 45, 21303);
        addHoverButton(21304, 298, 172, 28, "View Latest Announcements", -1, 21305, 1);
        addHoveredButton(21305, 780, 172, 28, 21306);
        addHoverButton(21307, 298, 172, 28, "View Latest Updates", -1, 21308, 1);
        addHoveredButton(21308, 780, 172, 28, 21309);
        //addHead(21310, 35, 35, 2500);
        addHead(21342, 2, 306, 588, 35, 35, 900);
        addText(21311, "WELCOME TO TARNISH!", 0xE9EBC5, true, true, 52, TDA, 3);
        addText(21312, "CLICK HERE TO PLAY", 0xFF4545, true, true, 52, TDA, 3);
        addText(21313, "View Latest Announcement", 0xC9C2BF, true, true, 52, TDA, 1);
        addText(21314, "View Latest Update", 0xC9C2BF, true, true, 52, TDA, 1);
        addText(21315, "Tarnish welcomes you!", 0x000000, true, false, 52, TDA, 0);
        addText(21316, "Tarnish welcomes you!", 0x000000, true, false, 52, TDA, 0);
        addText(21317, "Tarnish welcomes you!", 0x000000, true, false, 52, TDA, 0);
        addText(21318, "You last logged in from 127.0.0.1", 0xEBCD96, true, true, 52, TDA, 0);
        addMarqueeText(21319, 0xEBCD96, true, 52, 3, TDA);
        addText(21320, "Bank pin line 1", 0xC9C2BF, false, true, 52, TDA, 0);
        addText(21321, "Bank pin line 2", 0xC9C2BF, false, true, 52, TDA, 0);
        addText(21322, "Bank pin line 3", 0xC9C2BF, false, true, 52, TDA, 0);
        addText(21323, "Bank pin line 4", 0xC9C2BF, false, true, 52, TDA, 0);
        addText(21324, "Account security line 1", 0xC9C2BF, false, true, 52, TDA, 0);
        addText(21325, "Account security line 2", 0xC9C2BF, false, true, 52, TDA, 0);
        addText(21326, "Account security line 3", 0xC9C2BF, false, true, 52, TDA, 0);
        addText(21327, "Account security line 4", 0xC9C2BF, false, true, 52, TDA, 0);
        addHoverText(21328, "Email line 1", "Click link", TDA, 0, 0xFF9900, 0xFFFFFF, false, true, 168);
        addHoverText(21329, "Email line 2", "Click link", TDA, 0, 0xFF9900, 0xFFFFFF, false, true, 168);
        addHoverText(21330, "Email line 3", "Click link", TDA, 0, 0xFF9900, 0xFFFFFF, false, true, 168);
        addHoverText(21331, "Email line 4", "Click link", TDA, 0, 0xFF9900, 0xFFFFFF, false, true, 168);
        addText(21332, "Beta Mode", 0xFF9900, true, true, 52, TDA, 1);
        addText(21333, "2017-10-31", 0xFF9900, true, true, 52, TDA, 0);
        addText(21334, "Beta mode is now available for", 0xE1E8DF, false, true, 52, TDA, 0);
        addText(21335, "select members. Stay tuned for", 0xE1E8DF, false, true, 52, TDA, 0);
        addText(21336, "our public release.", 0xE1E8DF, false, true, 52, TDA, 0);
        addText(21337, "Game Updates", 0xFF9900, true, true, 52, TDA, 1);
        addText(21338, "", 0xFF9900, true, true, 52, TDA, 0);
        addText(21339, "Pk bots, new player saving,", 0xE1E8DF, false, true, 52, TDA, 0);
        addText(21340, "teleport interface improvements", 0xE1E8DF, false, true, 52, TDA, 0);
        addText(21341, "& much more!", 0xE1E8DF, false, true, 52, TDA, 0);
        rsinterface.totalChildren(39);
        rsinterface.child(0, 21300, 0, -4);
        rsinterface.child(1, 21301, 203, 401);
        rsinterface.child(2, 21302, 203, 401);
        rsinterface.child(3, 21304, 91, 261);
        rsinterface.child(4, 21305, 91, 261);
        rsinterface.child(5, 21307, 297, 261);
        rsinterface.child(6, 21308, 297, 261);
        rsinterface.child(7, 21342, 125, 355);//21310
        rsinterface.child(8, 21311, 380, 55);
        rsinterface.child(9, 21312, 385, 412);
        rsinterface.child(10, 21313, 175, 266);
        rsinterface.child(11, 21314, 383, 266);
        rsinterface.child(12, 21315, 345, 335);
        rsinterface.child(13, 21316, 345, 351);
        rsinterface.child(14, 21317, 345, 366);
        rsinterface.child(15, 21318, 380, 90);
        rsinterface.child(16, 21319, 380, 37);
        rsinterface.child(17, 21320, 558, 130);
        rsinterface.child(18, 21321, 558, 145);
        rsinterface.child(19, 21322, 558, 160);
        rsinterface.child(20, 21323, 558, 175);
        rsinterface.child(21, 21324, 558, 225);
        rsinterface.child(22, 21325, 558, 240);
        rsinterface.child(23, 21326, 558, 255);
        rsinterface.child(24, 21327, 558, 270);
        rsinterface.child(25, 21328, 558, 320);
        rsinterface.child(26, 21329, 558, 335);
        rsinterface.child(27, 21330, 558, 350);
        rsinterface.child(28, 21331, 558, 365);
        rsinterface.child(29, 21332, 175, 137);
        rsinterface.child(30, 21333, 175, 157);
        rsinterface.child(31, 21334, 95, 200);
        rsinterface.child(32, 21335, 95, 215);
        rsinterface.child(33, 21336, 95, 230);
        rsinterface.child(34, 21337, 383, 137);
        rsinterface.child(35, 21338, 383, 157);
        rsinterface.child(36, 21339, 303, 200);
        rsinterface.child(37, 21340, 303, 215);
        rsinterface.child(38, 21341, 303, 230);
        //        rsinterface.child(31, 21333, 385, 137);
        //        rsinterface.child(32, 21334, 385, 157);
    }

    private static void settingsTab() {
        RSInterface tab = addInterface(50000);
        addSprite(50001, 728);
        addConfigButton(50002, 50000, 722, 723, 40, 40, "View display settings", 0, 5, 980, false);
        addConfigButton(50003, 50000, 724, 725, 40, 40, "View chat settings", 1, 5, 980, false);
        addConfigButton(50004, 50000, 726, 727, 40, 40, "View control settings", 2, 5, 980, false);
        addConfigButton(50005, 50000, 140, 141, 40, 40, "View extra settings", 3, 5, 980, false);
        addConfigButton(50006, 50000, 127, 126, 40, 40, "Toggle accept aid", 0, 5, 427, false);
        addConfigButton(50007, 50000, 129, 128, 40, 40, "Toggle run", 0, 5, 152, true);
        addConfigButton(50008, 50000, 731, 732, 40, 40, "View donator tab", 4, 5, 980, false);
        tab.totalChildren(9);
        tab.child(0, 50001, 3, 48);
        tab.child(1, 50002, 5, 4);
        tab.child(2, 50003, 52, 4);
        tab.child(3, 50004, 101, 4);
        tab.child(4, 50005, 147, 4);
        tab.child(5, 50006, 25, 220);
        tab.child(6, 50007, 75, 220);
        tab.child(7, 50008, 125, 220);
        tab.child(8, 149, 80, 240);
    }

    private static void displayTab(TextDrawingArea[] TDA) {
        RSInterface mainTab = addInterface(50020);
        addSprite(50021, 418);
        addConfigButton(50022, 50020, 118, 119, 32, 16, "Zoom in", 1, 5, 176, false);
        addConfigButton(50023, 50020, 120, 121, 32, 16, "Normal zoom", 2, 5, 176, false);
        addConfigButton(50024, 50020, 122, 123, 32, 16, "Zoom out", 3, 5, 176, false);
        addConfigButton(50025, 50020, 124, 125, 32, 16, "Zoom out max", 4, 5, 176, false);
        addSprite(50026, 117);
        addConfigButton(50027, 50020, 118, 119, 32, 16, "Dark", 1, 5, 166);
        addConfigButton(50028, 50020, 120, 121, 32, 16, "Normal", 2, 5, 166);
        addConfigButton(50029, 50020, 122, 123, 32, 16, "Bright", 3, 5, 166);
        addConfigButton(50030, 50020, 124, 125, 32, 16, "Very Bright", 4, 5, 166);
        addHoverButton(50031, 111, 54, 34, "Fixed Mode", -1, 50032, 1);
        addHoveredButton(50032, 112, 54, 34, 50033);
        addHoverButton(50034, 113, 54, 34, "Resizable Mode", -1, 50035, 1);
        addHoveredButton(50035, 114, 54, 34, 50036);
        addHoverButton(50037, 115, 54, 34, "Fullscreen Mode", -1, 50038, 1);
        addHoveredButton(50038, 116, 54, 34, 50039);
        addHoverButton(50040, 729, 150, 26, "Advanced options", -1, 50041, 1);
        addHoveredButton(50041, 730, 150, 26, 50042);
        addText(50043, "Advanced options", 0xFF9900, true, true, 52, TDA, 1);
        mainTab.totalChildren(18);
        mainTab.child(0, 50000, 0, 0);
        mainTab.child(1, 50021, 15, 55);
        mainTab.child(2, 50022, 50, 65);
        mainTab.child(3, 50023, 82, 65);
        mainTab.child(4, 50024, 114, 65);
        mainTab.child(5, 50025, 146, 65);
        mainTab.child(6, 50026, 15, 85);
        mainTab.child(7, 50027, 50, 95);
        mainTab.child(8, 50028, 82, 95);
        mainTab.child(9, 50029, 114, 95);
        mainTab.child(10, 50030, 146, 95);
        mainTab.child(11, 50031, 35, 125); // fixed mode button
        mainTab.child(12, 50032, 35, 125); // fixed mode sprite
        mainTab.child(13, 50034, 105, 125); // resizable mode button
        mainTab.child(14, 50035, 105, 125); // resizable mode sprite
        mainTab.child(15, 50040, 20, 175);
        mainTab.child(16, 50041, 20, 175);
        mainTab.child(17, 50043, 95, 178);
    }

    private static void chatTab() {
        RSInterface mainTab = addInterface(50100);
        addConfigButton(50101, 51000, 132, 133, 40, 40, "Toggle Chat Effects", 0, 5, 171);
        addConfigButton(50102, 51000, 134, 135, 40, 40, "Toggle Split Chat", 1, 5, 287);
        addConfigButton(50103, 51000, 130, 131, 40, 40, "Toggle Profanity", 1, 5, 203);
        mainTab.totalChildren(4);
        mainTab.child(0, 50000, 0, 0);
        mainTab.child(1, 50101, 20, 105);
        mainTab.child(2, 50102, 75, 105);
        mainTab.child(3, 50103, 130, 105);
    }

    private static void controlTab(TextDrawingArea[] TDA) {
        RSInterface mainTab = addInterface(50200);
        addConfigButton(50201, 50200, 142, 143, 40, 40, "Toggle Right-click", 1, 5, 170);
        addConfigButton(50202, 50200, 136, 137, 40, 40, "Toggle Scroll Camera Movement", 1, 5, 207);
        addConfigButton(50203, 50200, 138, 139, 40, 40, "Open Keybinding Interface", 1, 5, 208, false);
        addText(50204, "Entity 'Attack' option:", 0xFF9900, false, true, 52, TDA, 1);
        addDropdownMenu(50205, 155, 0, false, true, Dropdown.DEFAULT, "Depends on combat levels", "Always right-click", "Left click where available", "Hidden");
        mainTab.totalChildren(6);
        mainTab.child(0, 50000, 0, 0);
        mainTab.child(1, 50201, 20, 75);
        mainTab.child(2, 50202, 75, 75);
        mainTab.child(3, 50203, 130, 75);
        mainTab.child(4, 50204, 15, 130);
        mainTab.child(5, 50205, 20, 150);
    }

    private static void customTab(TextDrawingArea[] TDA) {
        RSInterface mainTab = addInterface(50300);
        mainTab.totalChildren(2);
        mainTab.child(0, 50000, 0, 0);
        mainTab.child(1, 50010, 2, 54);
        RSInterface scrollInterface = addTabInterface(50010);
        scrollInterface.scrollPosition = 0;
        scrollInterface.contentType = 0;
        scrollInterface.width = 165;
        scrollInterface.height = 155;
        scrollInterface.scrollMax = 305;
        int y = 5, length = SettingData.values().length;
        scrollInterface.totalChildren(length * 2);
        int child = 0;
        for (int i = 0; i < length; i++) {
            SettingData settingData = SettingData.forOrdinal(i);
            String name = settingData == null ? "None" : settingData.setting;
            addHoverText(50301 + i, name, "Toggle " + name, TDA, 0, 0xD46E08, 0xFFFFFF, false, true, 168);
            scrollInterface.child(child, 50301 + i, 30, y);
            child++;
            addConfigButton(50350 + i, 50300, 424, 423, 15, 15, "Toggle " + name, 0, 1, 900 + i);
            scrollInterface.child(child, 50350 + i, 12, y - 1);
            child++;
            y += 20;
        }
    }

    private static void donatorTab(TextDrawingArea[] TDA) {
        RSInterface mainTab = addInterface(50400);
        mainTab.totalChildren(2);
        mainTab.child(0, 50000, 0, 0);
        mainTab.child(1, 50420, 2, 54);

        RSInterface scrollInterface = addTabInterface(50420);
        scrollInterface.scrollPosition = 0;
        scrollInterface.contentType = 0;
        scrollInterface.width = 165;
        scrollInterface.height = 155;
        scrollInterface.scrollMax = 300;
        int y = 5;
        scrollInterface.totalChildren(6);

        addHoverButton(50421, 391, 118, 32, "Teleport to Donator Zone", 0, 50422, 1);
        addHoveredButton(50422, 392, 118, 32, 50423);
        addText(50424, "Donator Zone", TDA, 1, 0xFF9900, true, true);

        addHoverButton(50425, 391, 118, 32, "Manage donator titles", 0, 50426, 1);
        addHoveredButton(50426, 392, 118, 32, 50427);
        addText(50428, "Donator Titles", TDA, 1, 0xFF9900, true, true);


        scrollInterface.child(0, 50421, 30, y);
        scrollInterface.child(1, 50422, 30, y);
        scrollInterface.child(2, 50424, 88, y + 7);

        y = 45;
        scrollInterface.child(3, 50425, 30, y);
        scrollInterface.child(4, 50426, 30, y);
        scrollInterface.child(5, 50428, 88, y + 7);


    }

    public static void settings(TextDrawingArea[] tda) {
        RSInterface rsi = addInterface(28500);
        addSprite(28501, 144);
        addHoverButton(28502, 252, 21, 21, "Close", -1, 28503, 1);
        addHoveredButton(28503, 253, 21, 21, 28504);
        //addConfigButton(28505, 28500, 734, 733, 40, 40, "Toggle HD", 0, 1, 880);
        addConfigButton(28506, 28500, 736, 735, 40, 40, "Toggle transparent tab", 0, 1, 881);
        addSprite(28507, 738);
        addConfigButton(28508, 28500, 736, 735, 40, 40, "Toggle transparent chatbox", 0, 1, 882);
        addSprite(28509, 740);
        addConfigButton(28510, 28500, 736, 735, 40, 40, "Toggle sidestones", 0, 1, 883);
        addSprite(28511, 741);
        addConfigButton(28512, 28500, 736, 735, 40, 40, "Toggle status orbs", 0, 1, 884);
        addSprite(28513, 737);
        addConfigButton(28514, 28500, 736, 735, 40, 40, "Toggle special attack orb", 0, 1, 885);
        addSprite(28515, 742);
        addConfigButton(28516, 28500, 736, 735, 40, 40, "Toggle roofs", 0, 1, 886);
        addSprite(28517, 739);
        addConfigButton(28518, 28500, 736, 735, 40, 40, "Toggle x10 damage", 0, 1, 887);
        addText(28519, "x10\\nDamage", tda, 0, 0xFF9900, true, true);
        addConfigButton(28520, 28500, 298, 420, 40, 40, "OSRS hitsplats", 0, 5, 888);
        addConfigButton(28521, 28500, 298, 421, 40, 40, "614 hitsplats", 1, 5, 888);
        addConfigButton(28522, 28500, 298, 422, 40, 40, "634 hitsplats", 2, 5, 888);
        addText(28523, "Some settings have a 'right-click' option available", tda, 0, 0xF6FF000, true, true);
        addConfigButton(28524, 28500, 298, 431, 40, 40, "OSRS hp bar", 0, 5, 889);
        addConfigButton(28525, 28500, 298, 432, 40, 40, "614 hp bar", 1, 5, 889);
        addConfigButton(28526, 28500, 298, 438, 40, 40, "Custom hp bar", 2, 5, 889);
        addConfigButton(28528, 28500, 298, 736, 40, 40, "OSRS context menu", 0, 5, 890);
        addConfigButton(28529, 28500, 298, 736, 40, 40, "614 context menu", 1, 5, 890);
        addConfigButton(28530, 28500, 298, 736, 40, 40, "634 context menu", 2, 5, 890);
        addConfigButton(28531, 28500, 298, 736, 40, 40, "Custom context menu", 3, 5, 890);
        addText(28532, "Custom\\nMenu", tda, 0, 0xFF9900, true, true);
        addConfigButton(28533, 28500, 736, 735, 40, 40, "Toggle FPS", 0, 1, 891);
        addText(28534, "FPS", tda, 1, 0xFF9900, true, true);
        addConfigButton(28535, 28500, 736, 735, 40, 40, "Toggle PING", 0, 1, 892);
        addText(28536, "PING", tda, 1, 0xFF9900, true, true);
        addConfigButton(28537, 28500, 736, 735, 40, 40, "Toggle value icons", 0, 1, 893);
        addSprite(28538, 405);

        rsi.totalChildren(35);
        rsi.child(0, 28501, 100, 65);
        rsi.child(1, 28502, 381, 72);
        rsi.child(2, 28503, 381, 72);
        //rsi.child(3, 28505, 110, 109);
        rsi.child(3, 28537, 110, 109);
        rsi.child(4, 28506, 160, 109);
        rsi.child(5, 28507, 164, 113);
        rsi.child(6, 28508, 210, 109);
        rsi.child(7, 28509, 214, 113);
        rsi.child(8, 28510, 260, 109);
        rsi.child(9, 28511, 264, 113);
        rsi.child(10, 28512, 310, 109);
        rsi.child(11, 28513, 314, 113);
        rsi.child(12, 28514, 360, 109);
        rsi.child(13, 28515, 366, 115);
        rsi.child(14, 28516, 110, 159);
        rsi.child(15, 28517, 114, 171);
        rsi.child(16, 28518, 160, 159);
        rsi.child(17, 28519, 180, 165);
        rsi.child(18, 28522, 210, 159);
        rsi.child(19, 28521, 210, 159);
        rsi.child(20, 28520, 210, 159);
        rsi.child(21, 28523, 252, 246);
        rsi.child(22, 28526, 260, 159);
        rsi.child(23, 28525, 260, 159);
        rsi.child(24, 28524, 260, 159);
        rsi.child(25, 28531, 310, 159);
        rsi.child(26, 28530, 310, 159);
        rsi.child(27, 28529, 310, 159);
        rsi.child(28, 28528, 310, 159);
        rsi.child(29, 28532, 330, 165);
        rsi.child(30, 28533, 360, 159);
        rsi.child(31, 28534, 380, 170);
        rsi.child(32, 28535, 110, 207);
        rsi.child(33, 28536, 130, 218);
        rsi.child(34, 28538, 118, 121); //166, 220
    }

    public static void clanBank(TextDrawingArea[] tda) {
        RSInterface rsinterface = addInterface(43800);
        addSprite(43801, 712);
        addText(43802, "Clan Bank", tda, 2, 0xFF9900, true, true);
        addText(43803, "Networth: <col=5ec94e>152m</col>", tda, 0, 0xFF9900, false, true);
        addText(43804, "Total Contributors: <col=5ec94e>37</col>", tda, 0, 0xFF9900, false, true);
        addText(43805, "Size: <col=5ec94e>85/100</col>", tda, 0, 0xFF9900, false, true);
        addHoverButton(43806, 24, 16, 16, "Close", 250, 43807, 3);
        addHoveredButton(43807, 25, 16, 16, 43808);
        addText(43821, "Top Contributors:", tda, 1, 0xdbdac7, true, true);
        String[] name = {"Settings (<col=5ec94e>100m</col>)", "Chex (<col=5ec94e>90m</col>)", "Owain (<col=5ec94e>26m</col>)", "Goat (<col=5ec94e>15m</col>)", "Pure (5m</col>)", "Jeff (<col=5ec94e>2m</col>)", "Ice Pker13 (<col=5ec94e>1.4m</col>)", "Morning Pillxx (<col=ffffff>900k</col>)", "Obamacare 1956 (<col=ffffff>400k</col>)", "Monster (<col=ffffff>4k</col>)"};
        for (int index = 0; index < 10; index++) {
            int color = index % 2 == 0 ? 0xf2bc6d : 0xFF9900;
            addText(43809 + index, (index + 1) + ": " + name[index], tda, 0, color, false, true);
        }
        rsinterface.totalChildren(18);
        rsinterface.child(0, 43801, 13, 12);
        rsinterface.child(1, 43802, 260, 20);
        rsinterface.child(2, 43803, 35, 65);
        rsinterface.child(3, 43804, 35, 80);
        rsinterface.child(4, 43805, 35, 95);
        rsinterface.child(5, 43806, 472, 21);
        rsinterface.child(6, 43807, 472, 21);
        for (int index = 0, y = 160; index < 10; index++) {
            rsinterface.child(6 + index, 43809 + index, 25, y);
            y += 15;
        }
        rsinterface.child(16, 43821, 85, 139);
        rsinterface.child(17, 43850, 150, 60);
        RSInterface scrollInterface = addTabInterface(43850);
        scrollInterface.scrollPosition = 0;
        scrollInterface.contentType = 0;
        scrollInterface.width = 311;
        scrollInterface.height = 235;
        scrollInterface.scrollMax = 470;
        scrollInterface.totalChildren(1);
        addContainer(43851, 8, 10, 14, 15, false, "Withdraw 1", "Withdraw 5", "Withdraw 10", "Withdraw 100", "Withdraw X");
        scrollInterface.child(0, 43851, 40, 9);
    }

    public static void goodwillInterface(TextDrawingArea[] tda) {
        RSInterface tab = addInterface(43700);
        addSprite(43701, 698);
        addText(43702, "Well of Goodwill", tda, 2, 0xFF9900, true, true);
        addHoverButton(43703, 24, 16, 16, "Close", 250, 43704, 3);
        addHoveredButton(43704, 25, 16, 16, 43705);
        addProgressBar(43706, 30, 250, 30, "");
        addText(43707, "Top 3 Contributors:", tda, 1, 0xFF9900, true, true);
        addText(43708, "---", tda, 0, 0xFFFFFF, true, true);
        addText(43709, "---", tda, 0, 0xFFFFFF, true, true);
        addText(43710, "---", tda, 0, 0xFFFFFF, true, true);
        addText(43711, "Latest contributor", tda, 1, 0xFF9900, true, true);
        addText(43712, "---", tda, 0, 0xFFFFFF, true, true);
        addHoverButton(43713, 247, 117, 35, "Contribute to goodwill", -1, 43714, 1);
        addHoveredButton(43714, 246, 117, 35, 43715);
        addText(43716, "Contribute", tda, 2, 0x16a02b, true, true);
        addText(43717, "", tda, 1, 0xFFFFFF, true, true);

        tab.totalChildren(15);
        tab.child(0, 43701, 115, 45);
        tab.child(1, 43702, 255, 58);
        tab.child(2, 43703, 368, 58);
        tab.child(3, 43704, 368, 58);
        tab.child(4, 43706, 128, 95);
        tab.child(5, 43707, 255, 135);
        tab.child(6, 43708, 255, 155);
        tab.child(7, 43709, 255, 170);
        tab.child(8, 43710, 255, 185);
        tab.child(9, 43711, 255, 200);
        tab.child(10, 43712, 255, 220);
        tab.child(11, 43713, 195, 242);
        tab.child(12, 43714, 195, 242);
        tab.child(13, 43716, 255, 250);
        tab.child(14, 43717, 255, 100);
    }

    private static void keyBinding(TextDrawingArea[] tda) {
        RSInterface tab = addInterface(39300);
        addSprite(39301, 697);
        addHoverButton(39302, 252, 21, 21, "Close", 250, 39303, 3);
        addHoveredButton(39303, 253, 21, 21, 39304);
        int startingLine = 39305;
        for (int index = 0; index < 14; index++) {
//            addDropdownMenu(26811, 136, 0, false, true, Dropdown.DEFAULT, "10", "100", "1,000", "10,000", "100,000");

            addDropdownMenu(startingLine, 85, -1, false, false, Dropdown.KEYBINDING, "None", "F1", "F2", "F3", "F4", "F5", "F6", "F7", "F8", "F9", "F10", "F11", "F12", "ESC");
  //          addDropdownMenu(26811, 150, -1, false, false, Dropdown.KEYBINDING, "None", "F1", "F2", "F3", "F4", "F5", "F6", "F7", "F8", "F9", "F10", "F11", "F12", "ESC");
            startingLine++;
        }
        addHoverButton(39319, 247, 117, 35, "Restore default value", -1, 39320, 1);
        addHoveredButton(39320, 246, 117, 35, 39321);
        addText(39322, "Default values", tda, 2, 0xFF9900, true, true);
        addConfigButton(39323, 39300, 235, 234, 15, 15, "Toggle 'esc' button closing interface", 0, 1, 594);
        tab.totalChildren(21);
        tab.child(0, 39301, 5, 15);
        tab.child(1, 39302, 477, 23);
        tab.child(2, 39303, 477, 23);
        tab.child(3, 39309, 70, 237);
        tab.child(4, 39308, 70, 194);
        tab.child(5, 39307, 70, 151);
        tab.child(6, 39306, 70, 108);
        tab.child(7, 39319, 360, 267);
        tab.child(8, 39320, 360, 267);
        tab.child(9, 39322, 417, 275);
        tab.child(10, 39323, 35, 283);
        tab.child(11, 39305, 70, 65);
        tab.child(12, 39314, 230, 237);
        tab.child(13, 39313, 230, 194);
        tab.child(14, 39312, 230, 151);
        tab.child(15, 39311, 230, 108);
        tab.child(16, 39310, 230, 65);
        tab.child(17, 39315, 390, 194);
        tab.child(18, 39316, 390, 151);
        tab.child(19, 39317, 390, 108);
        tab.child(20, 39318, 390, 65);
    }

    private static void storeSettingMenu(TextDrawingArea[] tda) {
        RSInterface tab = addInterface(38300);
        addSprite(38301, 689);
        addHoverButton(38302, 24, 16, 16, "Close", 250, 38303, 3);
        addHoveredButton(38303, 25, 16, 16, 38304);
        addText(38305, "Store Settings", tda, 2, 0xFF9900, true, true);
        addText(38314, "Press enter on your input field to confirm your choice!", tda, 0, 0xFFFFFF, true, true);

        addText(38306, "Store Name:", tda, 2, 0xFF9900, false, true);
        addInputField(38307, 15, 0xFF981F, "Enter store name...", 265, 25, false, false, "[A-Za-z0-9 ]");
        addText(38308, "Store Slogan:", tda, 2, 0xFF9900, false, true);
        addInputField(38309, 15, 0xFF981F, "Enter store slogan...", 265, 25, false, false, "[A-Za-z0-9 ]");
        addHoverButton(38310, 393, 77, 32, "Back", 0, 38311, 1);
        addHoveredButton(38311, 394, 77, 32, 38312);
        addText(38313, "Back", tda, 1, 0xFF9900, false, true);

        tab.totalChildren(12);
        tab.child(0, 38301, 110, 40);
        tab.child(1, 38302, 375, 55);
        tab.child(2, 38303, 375, 55);
        tab.child(3, 38305, 257, 52);
        tab.child(4, 38314, 257, 88);

        tab.child(5, 38306, 125, 100 + 8);
        tab.child(6, 38307, 125, 125 + 8);
        tab.child(7, 38308, 125, 165 + 8);
        tab.child(8, 38309, 125, 195 + 8);
        tab.child(9, 38310, 220, 235 + 10);
        tab.child(10, 38311, 220, 235 + 8);
        tab.child(11, 38313, 240, 241 + 8);
    }

    private static void storeMenu(TextDrawingArea[] tda) {
        RSInterface tab = addInterface(38200);
        addSprite(38201, 684);
        addHoverButton(38202, 24, 16, 16, "Close", 250, 38203, 3);
        addHoveredButton(38203, 25, 16, 16, 38204);
        addHoverButton(38215, 298, 119, 135, "View all stores", 0, 38216, 1);
        addHoveredButton(38216, 685, 119, 135, 38217);
        addHoverButton(38218, 298, 163, 61, "Collect Coins", 0, 38219, 1);
        addHoveredButton(38219, 686, 163, 61, 38220);
        addHoverButton(38221, 298, 163, 74, "Manage Store", 0, 38222, 1);
        addHoveredButton(38222, 687, 163, 74, 38223);
        addHoverButton(38224, 298, 282, 61, "Open your store", 0, 38225, 1);
        addHoveredButton(38225, 688, 282, 61, 38226);
        addText(38205, "Store Manager", tda, 2, 0xFF9900, true, true);
        addText(38206, "Collect Coins:", tda, 3, 0xFF9900, true, true);
        addText(38207, "765,000", tda, 1, 0xFFFFFF, true, true);
        addText(38208, "Manage Store", tda, 3, 0xFF9900, true, true);
        addText(38209, "View All Stores", tda, 3, 0xFF9900, true, true);
        addText(38210, "Active Stores: 32", tda, 0, 0xFFFFFF, true, true);
        addText(38211, "Last Updated: 2mins", tda, 0, 0xFFFFFF, true, true);
        addText(38212, "Open Your Store", tda, 2, 0xFF9900, false, true);
        addText(38213, "Total Sold Worth: 20m", tda, 0, 0xFFFFFF, true, true);
        tab.totalChildren(20);
        tab.child(0, 38201, 110, 45);
        tab.child(1, 38202, 375, 60);
        tab.child(2, 38203, 375, 60);
        tab.child(3, 38215, 117, 89);
        tab.child(4, 38216, 117, 89);
        tab.child(5, 38218, 236, 89);
        tab.child(6, 38219, 236, 89);
        tab.child(7, 38221, 236, 149);
        tab.child(8, 38222, 236, 149);
        tab.child(9, 38224, 117, 223);
        tab.child(10, 38225, 117, 223);
        tab.child(11, 38205, 255, 57);
        tab.child(12, 38206, 345, 100);
        tab.child(13, 38207, 345, 115);
        tab.child(14, 38208, 305, 175);
        tab.child(15, 38209, 175, 95);
        tab.child(16, 38210, 175, 125);
        tab.child(17, 38211, 175, 140);
        tab.child(18, 38212, 205, 244);
        tab.child(19, 38213, 175, 155);
    }

    private static void dropSimulator(TextDrawingArea[] tda) {
        RSInterface tab = addInterface(26800);
        addSprite(26801, 679);
        addHoverButton(26802, 24, 16, 16, "Close", 250, 26803, 3);
        addHoveredButton(26803, 25, 16, 16, 26804);
        addText(26805, "NPC Drop Simulator", tda, 2, 0xFF9900, true, true);
        addText(26806, "King Black Dragon:", tda, 0, 0xFF9900, true, true);
        addText(26807, "Drops: 35", tda, 0, 0xFF9900, true, true);
        addText(26808, "Load Time: 2 seconds", tda, 0, 0xFF9900, true, true);
        addInputField(26810, 15, 0xFF981F, "Search for npc", 121, 23, false, false, "[A-Za-z0-9 ]");
        addInputField(26811, 7, 0xFF981F, "Simulation amount", 100, 20, false, false, "[0-9]");
        addDropdownMenu(26811, 136, 0, false, true, Dropdown.DEFAULT, "10", "100", "1,000", "10,000", "100,000");
        tab.totalChildren(11);
        tab.child(0, 26801, 45, 23);
        tab.child(1, 26802, 435, 33);
        tab.child(2, 26803, 435, 33);
        tab.child(3, 26805, 260, 32);
        tab.child(4, 26806, 320, 65);
        tab.child(5, 26807, 320, 80);
        tab.child(6, 26808, 320, 95);
        tab.child(7, 26810, 58, 273);
        tab.child(8, 26815, 198, 117);
        tab.child(9, 26850, -65, 60);
        tab.child(10, 26811, 50, 28);
        RSInterface scrollInterface = addTabInterface(26815);
        scrollInterface.scrollPosition = 0;
        scrollInterface.contentType = 0;
        scrollInterface.width = 238;
        scrollInterface.height = 180;
        scrollInterface.scrollMax = 500;
        scrollInterface.totalChildren(1);
        addContainer(26816, 0, 6, 10, 8, 12, 100, false, true, true, null, null, null, null, null);
        scrollInterface.child(0, 26816, 3, 3);
        RSInterface scrollInterface2 = addTabInterface(26850);
        scrollInterface2.scrollPosition = 0;
        scrollInterface2.contentType = 0;
        scrollInterface2.width = 231;
        scrollInterface2.height = 205;
        scrollInterface2.scrollMax = 500;
        scrollInterface2.totalChildren(50);
        int y = 0;
        for (int i = 0, child = 0; i < 50; i++) {
            addHoverText(26851 + i, "123", "", tda, 0, 0xFFB83F, 0xFFFFFF, false, true, 160);
            scrollInterface2.child(child++, 26851 + i, 125, y + 5);
            y += 15;
        }
    }

    private static void tabCreation(TextDrawingArea[] TDA) {
        RSInterface tab = addTabInterface(26750);
        addSprite(26751, 853);
        addHoverButton(26752, 24, 21, 21, "Close", 250, 26753, 3);
        addHoveredButton(26753, 25, 21, 21, 26754);
        addText(26755, "Tablet Creation", TDA, 2, 0xff9933, true, true);
        addContainer(26756, 0, 5, 12, 9, 8, 100, false, true, true, "Make", "Info");
        addText(26757, "Please click on a tab to create it", TDA, 0, 0xff9933, true, true);
        addText(26758, "Click on 'info' to get requirements.", TDA, 0, 0xff9933, true, true);
        tab.totalChildren(7);
        tab.child(0, 26751, 100, 70);
        tab.child(1, 26752, 475, 10);
        tab.child(2, 26753, 475, 10);
        tab.child(3, 26755, 257, 80);
        tab.child(4, 26756, 158, 144);
        tab.child(5, 26757, 257, 112);
        tab.child(6, 26758, 257, 122);
    }

    private static void lootingBag(TextDrawingArea[] tda) {
        RSInterface tab = addInterface(26700);
        addSprite(26701, 249);
        addHoverButton(26702, 24, 16, 16, "Close", 0, 26703, 1);
        addHoveredButton(26703, 25, 16, 16, 26704);
        addText(26705, "Looting bag", tda, 2, 0xFF9900, true, true);
        addContainer(26706, 0, 4, 7, 13, 0, 100, false, true, true, "Remove 1", "Remove 5", "Remove 10", "Remove All", "Remove X");
        addText(26707, "Value: 0 coins", tda, 0, 0xFF9900, true, true);
        tab.totalChildren(6);
        tab.child(0, 26701, 9, 21);
        tab.child(1, 26702, 168, 4);
        tab.child(2, 26703, 168, 4);
        tab.child(3, 26705, 95, 4);
        tab.child(4, 26706, 12, 23);
        tab.child(5, 26707, 95, 250);
    }

    private static void lostItems(TextDrawingArea[] TDA) {
        RSInterface tab = addInterface(57300);
        addSprite(57301, 455);

        addText(57302, "Lost Untradeables", TDA, 2, 0xff981f, true, true);
        addHoverButton(57303, 24, 21, 21, "Close", 250, 57304, 3);
        addHoveredButton(57304, 25, 21, 21, 57305);
        addText(57306, "0/28", TDA, 0, 0xff981f, true, true);
        addContainer(57307, 0, 6, 5, 22, 21, 100, false, true, true, "Claim", "Remove");

        tab.totalChildren(6);
        tab.child(0, 57301, 85, 12);
        tab.child(1, 57302, 250, 21);
        tab.child(2, 57303, 397, 21);
        tab.child(3, 57304, 397, 21);
        tab.child(4, 57306, 110, 24);
        tab.child(5, 57307, 100, 55);
    }

    private static void donatorDeposit(TextDrawingArea[] TDA) {
        RSInterface tab = addInterface(57200);
        addSprite(57201, 550);
        addText(57202, "Donator Deposit Box", TDA, 2, 0xff981f, true, true);
        addHoverButton(57203, 24, 21, 21, "Close", 250, 57204, 3);
        addHoveredButton(57204, 25, 21, 21, 57205);
        addText(57206, "0/28", TDA, 0, 0xff981f, true, true);
        addContainer(57207, 0, 6, 5, 22, 21, 100, false, false, true, "Remove 1", "Remove 5", "Remove 10", "Remove All", "Remove X");
        addHoverButton(57208, 167, 72, 32, "Confirm", -1, 57209, 1);
        addHoveredButton(57209, 168, 72, 32, 57210);
        addText(57211, "Confirm", TDA, 0, 0xff981f, true, true);
        tab.totalChildren(9);
        tab.child(0, 57201, 85, 12);
        tab.child(1, 57202, 250, 21);
        tab.child(2, 57203, 397, 21);
        tab.child(3, 57204, 397, 21);
        tab.child(4, 57206, 110, 24);
        tab.child(5, 57207, 100, 55);
        tab.child(6, 57208, 326, 270);
        tab.child(7, 57209, 326, 270);
        tab.child(8, 57211, 360, 280);
    }

    private static void clanShowcase(TextDrawingArea[] TDA) {
        RSInterface tab = addInterface(57700);
        addSprite(57701, 702);
        addText(57702, "Daniel's Clan Showcase", TDA, 2, 0xff981f, true, true);
        addHoverButton(57703, 24, 21, 21, "Close", 250, 57704, 3);
        addHoveredButton(57704, 25, 21, 21, 57705);
        addHoverButton(57706, 298, 63, 25, "Confirm", -1, 57707, 1);
        addHoveredButton(57707, 700, 63, 25, 57708);
        addHoverButton(57709, 298, 63, 25, "Delete", -1, 57710, 1);
        addHoveredButton(57710, 701, 63, 25, 57711);
        addText(57712, "Confirm", TDA, 0, 0xFFFFFF, true, true);
        addText(57713, "Delete", TDA, 0, 0xFFFFFF, true, true);
        addText(57714, "You can earn items to display in your showcase by progressing", TDA, 0, 0xd8c491, true, true);
        addText(57715, "your clan or by purchasing the mystery showcase box.", TDA, 0, 0xd8c491, true, true);
        addContainer(57716, 0, 6, 5, 22, 21, 100, false, false, false, "Select");
        addContainer(57717, 0, 6, 5, 22, 21, 50, false, false, false);
        addText(57718, "3/28", TDA, 0, 0xff981f, true, true);
        tab.totalChildren(15);
        tab.child(0, 57701, 85, 2);
        tab.child(1, 57702, 250, 11);
        tab.child(2, 57703, 397, 11);
        tab.child(3, 57704, 397, 11);
        tab.child(4, 57706, 353, 272);
        tab.child(5, 57707, 353, 272);
        tab.child(6, 57709, 353, 300);
        tab.child(7, 57710, 353, 300);
        tab.child(8, 57712, 391, 277);
        tab.child(9, 57713, 381, 306);
        tab.child(10, 57714, 250, 37);
        tab.child(11, 57715, 250, 47);
        tab.child(12, 57716, 100, 69);
        tab.child(13, 57717, 315, 283);
        tab.child(14, 57718, 370, 13);
    }

    private static void playerProfiler(TextDrawingArea[] daniel) {
        RSInterface tab = addInterface(51800);
        addSprite(51801, 543);
        addText(51802, "Profile Viewer", daniel, 2, 0xff981f, true, true);
        addHoverButton(51803, 24, 21, 21, "Close", 250, 51804, 3);
        addHoveredButton(51804, 25, 21, 21, 51805);
        addOtherChar(51806, 700);// this
        addText(51807, "</col>Name: <col=FFB83F>Daniel", daniel, 1, 0xff981f, true, true);
        addText(51808, "</col>Rank: <col=FFB83F><img=2> Owner", daniel, 1, 0xff981f, true, true);
        addText(51809, "</col>Level: <col=FFB83F>126", daniel, 1, 0xff981f, true, true);
        addText(51810, "Profile views: 132", daniel, 0, 0xff981f, true, true);
        addHoverText(51811, "Manage Player", "Manage Player", daniel, 0, 0xFFB83F, 0xFFFFFF, false, true, 85);

        tab.totalChildren(12);
        tab.child(0, 51801, 10, 5);
        tab.child(1, 51802, 255, 13);
        tab.child(2, 51803, 478, 13);
        tab.child(3, 51804, 478, 13);
        tab.child(4, 51806, 35, 210);
        tab.child(5, 51807, 105, 55);
        tab.child(6, 51808, 105, 70);
        tab.child(7, 51809, 105, 85);
        tab.child(8, 51900, 150, 46);
        tab.child(9, 51830, 75, 46);
        tab.child(10, 51810, 65, 16);
        tab.child(11, 51811, 345, 16);

        RSInterface skills = addTabInterface(51830);
        skills.width = 181;
        skills.height = 270;
        skills.scrollMax = 455;
        int amount = 46, y = 1, child = 0, sprite = 0;
        setChildren(amount, skills);
        for (int index = 0; index < amount; index += 2) {
            addSprite(51831 + index, 82 + sprite);
            addText(51831 + (index + 1), "99<col=ff981f>/</col>99", daniel, 0, 0xFFB83F, true, true);
            skills.child(child++, 51831 + index, 128, y);
            skills.child(child++, 51831 + (index + 1), 162, y + 2);
            y += 20;
            sprite++;
        }

        RSInterface main = addTabInterface(51900);
        main.width = 326;
        main.height = 270;
        main.scrollMax = 650;

        int children = 80, yPos = 0, count = 0, bgc = 0;

        setChildren(children, main);

        for (int index = 0; index < children; index += 2) {
            int background = bgc % 2 == 0 ? 856 : 857;

            addSprite(51901 + index, background);
            addText(51901 + (index + 1), "", daniel, 0, bgc % 2 == 0 ? 0xff981f : 0xFFB83F, true, true);

            main.child(count++, 51901 + index, 129, yPos);
            main.child(count++, 51901 + (index + 1), 225, yPos + 4);
            yPos += 20;
            bgc++;
        }
    }

    private static void hallOfFame(TextDrawingArea[] tda) {
        RSInterface fame = addTabInterface(58500);
        addSprite(58501, 525);
        addHoverButton(58502, 24, 16, 16, "Close", 250, 58503, 3);
        addHoveredButton(58503, 25, 16, 16, 58504);
        addText(58505, "Hall of Fame", 0xff9933, true, true, -1, tda, 2);
        addInputField(58506, 15, 0xFF981F, "Search for player", 121, 23, false, false, "[A-Za-z0-9 ]");
        addConfigButton(58507, 58500, 298, 526, 103, 28, "Player killing", 0, 5, 1150, false);
        addConfigButton(58508, 58500, 298, 526, 103, 28, "Monster killing", 1, 5, 1150, false);
        addConfigButton(58509, 58500, 298, 526, 103, 28, "Skilling", 2, 5, 1150, false);
        addConfigButton(58510, 58500, 298, 526, 103, 28, "Miscellaneous", 3, 5, 1150, false);
        addSprite(58511, 530);
        addSprite(58512, 531);
        addSprite(58513, 532);
        addSprite(58514, 533);
        addText(58515, "Player Killing", 0xff9933, true, true, -1, tda, 0);
        addText(58516, "Monster Killing", 0xff9933, true, true, -1, tda, 0);
        addText(58517, "Skilling", 0xff9933, true, true, -1, tda, 0);
        addText(58518, "Miscellaneous", 0xff9933, true, true, -1, tda, 0);
        addSprite(58519, 529);
        addText(58520, "Claim to fame:", 0xff9933, false, true, -1, tda, 1);
        addText(58521, "Name:", 0xff9933, false, true, -1, tda, 1);
        addText(58522, "Date:", 0xff9933, false, true, -1, tda, 1);
        addText(58523, "0/35 Remaining", 0xff9933, true, true, -1, tda, 0);
        fame.totalChildren(23);
        fame.child(0, 58501, 40, 23);
        fame.child(1, 58502, 435, 33);
        fame.child(2, 58503, 435, 33);
        fame.child(3, 58505, 250, 33);
        fame.child(4, 58506, 47, 29);
        fame.child(5, 58507, 47, 58);
        fame.child(6, 58508, 149, 58);
        fame.child(7, 58509, 251, 58);
        fame.child(8, 58510, 353, 58);
        fame.child(9, 58511, 47, 59);
        fame.child(10, 58512, 149, 60);
        fame.child(11, 58513, 253, 60);
        fame.child(12, 58514, 355, 62);
        fame.child(13, 58515, 106, 67);
        fame.child(14, 58516, 208, 67);
        fame.child(15, 58517, 306, 67);
        fame.child(16, 58518, 410, 67);
        fame.child(17, 58519, 46, 91);
        fame.child(18, 58520, 70, 94);
        fame.child(19, 58521, 250, 94);
        fame.child(20, 58522, 345, 94);
        fame.child(21, 58530, -78, 112);
        fame.child(22, 58523, 385, 35);
        RSInterface scrollbar = addTabInterface(58530);
        scrollbar.width = 518;
        scrollbar.height = 192;
        scrollbar.scrollMax = 500;
        int amount = 80;
        setChildren(amount + 1, scrollbar);
        int y = 1, child = 0, sprite = 0;
        for (int index = 0; index < amount; index += 4) {
            int id = sprite % 2 == 0 ? 527 : 528;
            addSprite(58532 + index, id);
            addText(58532 + (index + 1), "itemId: " + (58532 + (index + 1)), tda, 0, 0xFFB83F, false, true);
            addText(58532 + (index + 2), "itemId: " + (58532 + (index + 2)), tda, 0, 0xff9933, false, true);
            addText(58532 + (index + 3), "itemId: " + (58532 + (index + 3)), tda, 0, 0xFFB83F, false, true);
            scrollbar.child(child++, 58532 + index, 125, y);
            scrollbar.child(child++, 58532 + (index + 1), 160, y + 12);
            scrollbar.child(child++, 58532 + (index + 2), 330, y + 12);
            scrollbar.child(child++, 58532 + (index + 3), 425, y + 12);
            y += 34;
            sprite++;
        }
        addContainer(58531, 0, 1, 80, 30, 2, 100, false, false, false, null, null, null, null, null);
        scrollbar.child(child++, 58531, 125, 0);
    }

    public static void emote() {
        RSInterface tab = addTabInterface(41000);
        tab.totalChildren(1);
        tab.child(0, 41001, 0, 1);
        addConfigButton(18700, 41000, 503, 491, 32, 48, "Idea", 0, 5, 1100, false);
        addConfigButton(18701, 41000, 504, 492, 28, 48, "Stomp", 0, 5, 1101, false);
        addConfigButton(18702, 41000, 505, 493, 41, 45, "Flap", 0, 5, 1102, false);
        addConfigButton(18703, 41000, 506, 494, 29, 47, "Slap head", 0, 5, 1103, false);
        addConfigButton(18704, 41000, 496, 495, 24, 47, "Zombie walk", 0, 5, 1104, false);
        addConfigButton(18705, 41000, 498, 497, 28, 46, "Zombie dance", 0, 5, 1105, false);
        addConfigButton(18706, 41000, 500, 499, 29, 42, "Scared", 0, 5, 1106, false);
        addConfigButton(18707, 41000, 502, 501, 18, 45, "Rabbit hop", 0, 5, 1107, false);
        addConfigButton(18708, 41000, 508, 507, 48, 48, "Sit up", 0, 5, 1108, false);
        addConfigButton(18709, 41000, 510, 509, 48, 48, "Push up", 0, 5, 1109, false);
        addConfigButton(18710, 41000, 512, 511, 48, 48, "Star jump", 0, 5, 1110, false);
        addConfigButton(18711, 41000, 514, 513, 25, 45, "Jog", 0, 5, 1111, false);
        addConfigButton(18712, 41000, 516, 515, 44, 46, "Zombie hand", 0, 5, 1112, false);
        addConfigButton(18713, 41000, 518, 517, 27, 47, "Hypermobile Drinker", 0, 5, 1113, false);
        addConfigButton(18714, 41000, 524, 523, 41, 48, "Skillcape", 0, 5, 1114, false);
        addConfigButton(18715, 41000, 520, 519, 36, 47, "Air guitar", 0, 5, 1115, false);
        addConfigButton(18716, 41000, 522, 521, 24, 47, "Uri", 0, 5, 1116, false);

        addConfigButton(18717, 41000, 715, 714, 32, 48, "Glass box", 0, 5, 1117, false);
        addConfigButton(18718, 41000, 717, 716, 32, 48, "Climb rope", 0, 5, 1118, false);
        addConfigButton(18719, 41000, 719, 718, 32, 48, "Lean", 0, 5, 1119, false);
        addConfigButton(18720, 41000, 721, 720, 32, 48, "Glass Wall", 0, 5, 1120, false);

        RSInterface scroll = addTabInterface(41001);
        scroll.width = 173;
        scroll.height = 260;
        scroll.scrollMax = 598;
        scroll.totalChildren(45);
        scroll.child(0, 168, 10, 7);
        scroll.child(1, 169, 54, 7);
        scroll.child(2, 164, 98, 14);
        scroll.child(3, 167, 137, 7);
        scroll.child(4, 162, 9, 56);
        scroll.child(5, 163, 48, 56);
        scroll.child(6, 13370, 95, 56);
        scroll.child(7, 171, 137, 56);
        scroll.child(8, 165, 7, 105);
        scroll.child(9, 170, 51, 105);
        scroll.child(10, 13366, 95, 104);
        scroll.child(11, 13368, 139, 105);
        scroll.child(12, 166, 6, 154);
        scroll.child(13, 13363, 50, 154);
        scroll.child(14, 13364, 90, 154);
        scroll.child(15, 13365, 135, 154);
        scroll.child(16, 161, 8, 204);
        scroll.child(17, 11100, 51, 203);
        scroll.child(18, 13362, 99, 204);
        scroll.child(19, 13367, 137, 203);
        scroll.child(20, 172, 10, 253);
        scroll.child(21, 13369, 53, 253);
        scroll.child(22, 13383, 88, 258);
        scroll.child(23, 13384, 138, 252);

        scroll.child(24, 18717, 6, 302);
        scroll.child(25, 18718, 49, 302);
        scroll.child(26, 18719, 93, 302);
        scroll.child(27, 18720, 137, 302);

        scroll.child(28, 18700, 6, 352);
        scroll.child(29, 18701, 49, 352);
        scroll.child(30, 18702, 87, 352);
        scroll.child(31, 18703, 137, 352);
        scroll.child(32, 18704, 10, 402);
        scroll.child(33, 18705, 49, 402);
        scroll.child(34, 18706, 92, 402);
        scroll.child(35, 18707, 137, 402);
        scroll.child(36, 18708, 1, 455);
        scroll.child(37, 18709, 49, 455);
        scroll.child(38, 18710, 92, 448);
        scroll.child(39, 18711, 139, 448);
        scroll.child(40, 18712, 6, 498);
        scroll.child(41, 18713, 55, 498);
        scroll.child(42, 18714, 87, 498);
        scroll.child(43, 18715, 129, 498);
        scroll.child(44, 18716, 9, 550);
    }

    public static void constructionBuild(TextDrawingArea[] tda) {
        RSInterface rsi = addTabInterface(47500);
        addSprite(47501, 488);
        addHoverButton(47502, 24, 16, 16, "Close", 250, 47503, 3);
        addHoveredButton(47503, 25, 16, 16, 47504);
        addText(47505, "Construction Builder", 0xff9933, true, true, -1, tda, 2);
        addConfigButton(47506, 47500, 397, 398, 83, 20, "View main objects", 0, 5, 687);
        addConfigButton(47507, 47500, 397, 398, 83, 20, "View skill objects", 1, 5, 687);
        addConfigButton(47508, 47500, 397, 398, 83, 20, "View misc objects", 2, 5, 687);
        addConfigButton(47509, 47500, 397, 398, 83, 20, "View npcs", 3, 5, 687);
        addText(47510, "Main Objects", 0xff9933, true, true, -1, tda, 0);
        addText(47511, "Skill Objects", 0xff9933, true, true, -1, tda, 0);
        addText(47512, "Misc Objects", 0xff9933, true, true, -1, tda, 0);
        addText(47513, "Npcs", 0xff9933, true, true, -1, tda, 0);
        addText(47514, "Total Objects:", 0xff9933, true, true, -1, tda, 0);
        addText(47515, "Information", 0xff9933, true, true, -1, tda, 1);
        addText(47516, "Materials required:", 0xff9933, true, false, -1, tda, 1);
        addContainer(47517, 8, 7, 9, 8, false, null, null, null, null, null);
        addText(47518, "Size: <col=ff9933>1</col>", 0xFFB83F, true, true, -1, tda, 0);
        addText(47519, "Level: <col=ff9933>53</col>", 0xFFB83F, true, true, -1, tda, 0);
        addText(47520, "Experience: <col=ff9933>1,500</col>", 0xFFB83F, true, true, -1, tda, 0);
        addHoverButton(47521, 298, 20, 16, "Change rotation", -1, 47522, 1);
        addHoveredButton(47522, 486, 20, 16, 47523);
        addHoverButton(47524, 298, 20, 16, "Change rotation", -1, 47525, 1);
        addHoveredButton(47525, 489, 20, 16, 47526);
        addText(47527, "North", 0xff9933, true, true, -1, tda, 0);
        addHoverButton(47528, 247, 117, 35, "Construct", -1, 47529, 1);
        addHoveredButton(47529, 246, 117, 35, 47530);
        addText(47531, "Construct", 0xff9933, true, true, -1, tda, 0);
        addSprite(47532, 490);

        rsi.totalChildren(29);
        rsi.child(0, 47501, 25, 8);
        rsi.child(1, 47502, 457, 20);
        rsi.child(2, 47503, 457, 20);
        rsi.child(3, 47505, 270, 20);
        rsi.child(4, 47506, 152, 57);
        rsi.child(5, 47507, 234, 57);
        rsi.child(6, 47508, 316, 57);
        rsi.child(7, 47509, 398, 57);
        rsi.child(8, 47510, 194, 62);
        rsi.child(9, 47511, 275, 62);
        rsi.child(10, 47512, 358, 62);
        rsi.child(11, 47513, 440, 62);
        rsi.child(12, 47514, 85, 297);
        rsi.child(13, 47515, 315, 110);
        rsi.child(14, 47516, 315, 205);
        rsi.child(15, 47517, 175, 228);
        rsi.child(16, 47518, 315, 145);
        rsi.child(17, 47519, 315, 165);
        rsi.child(18, 47520, 315, 185);
        rsi.child(19, 47540, -89, 48);
        rsi.child(20, 47521, 165, 281);
        rsi.child(21, 47522, 165, 281);
        rsi.child(22, 47524, 219, 281);
        rsi.child(23, 47525, 219, 281);
        rsi.child(24, 47527, 202, 284);
        rsi.child(25, 47528, 257, 272);
        rsi.child(26, 47529, 257, 272);
        rsi.child(27, 47531, 320, 284);
        rsi.child(28, 47532, 265, 274);

        RSInterface scrollInterface = addTabInterface(47540);
        scrollInterface.scrollPosition = 0;
        scrollInterface.contentType = 0;
        scrollInterface.width = 218;
        scrollInterface.height = 237;
        scrollInterface.scrollMax = 500;
        scrollInterface.totalChildren(50);
        int y = 0;
        for (int i = 0, child = 0; i < 50; i++) {
            addHoverText(47541 + i, "123", "", tda, 0, 0xFFB83F, 0xFFFFFF, false, true, 85);
            scrollInterface.child(child++, 47541 + i, 125, y + 5);
            y += 15;
        }
    }

    private static void staffPanel(TextDrawingArea[] tda) {
        RSInterface rsi = addTabInterface(36700);
        addSprite(36701, 487);
        addHoverButton(36702, 24, 16, 16, "Close", 250, 36703, 3);
        addHoveredButton(36703, 25, 16, 16, 36704);
        addText(36705, "Player Management", 0xff9933, true, true, -1, tda, 3);
        addText(36706, "Daniel", 0x63DB6D, true, true, -1, tda, 2);
        addText(36707, "Rank:", 0xff9933, true, true, -1, tda, 3);
        addText(36708, "Owner", 0xEDA540, true, true, -1, tda, 0);
        addText(36709, "Created:", 0xff9933, true, true, -1, tda, 3);
        addText(36710, "127.0.0.1", 0xEDA540, true, true, -1, tda, 0);
        addText(36711, "Play Time:", 0xff9933, true, true, -1, tda, 3);
        addText(36712, "Placeholder", 0xEDA540, true, true, -1, tda, 0);
        addText(36713, "IP Address", 0xff9933, true, true, -1, tda, 3);
        addText(36714, "Placeholder", 0xEDA540, true, true, -1, tda, 0);
        addText(36715, "All action colors are based on your rank", 0xEDA540, true, true, -1, tda, 0);
        addText(36716, "Moderator", 0x245EFF, true, true, -1, tda, 0);
        addText(36717, "Administrator", 0xD17417, true, true, -1, tda, 0);
        addText(36718, "Owner", 0xED0C0C, true, true, -1, tda, 0);

        rsi.totalChildren(18);
        rsi.child(0, 36701, 37, 23);
        rsi.child(1, 36702, 452, 29);
        rsi.child(2, 36703, 452, 29);
        rsi.child(3, 36705, 325, 40);
        rsi.child(4, 36706, 325, 80);
        rsi.child(5, 36707, 325, 100);
        rsi.child(6, 36708, 325, 120);
        rsi.child(7, 36709, 325, 135);
        rsi.child(8, 36710, 325, 155);
        rsi.child(9, 36711, 325, 170);
        rsi.child(10, 36712, 325, 190);
        rsi.child(11, 36713, 325, 205);
        rsi.child(12, 36714, 325, 225);
        rsi.child(13, 36715, 325, 255);
        rsi.child(14, 36716, 325, 268);
        rsi.child(15, 36717, 325, 280);
        rsi.child(16, 36718, 325, 292);
        rsi.child(17, 36730, -52, 29);

        RSInterface scrollInterface = addTabInterface(36730);
        scrollInterface.scrollPosition = 0;
        scrollInterface.contentType = 0;
        scrollInterface.width = 211;
        scrollInterface.height = 278;
        scrollInterface.scrollMax = 465;
        scrollInterface.totalChildren(40);

        int y = 0;
        int count = 0;
        for (int i = 0, child = 0; i < 40; i += 2) {
            addButton(36731 + i, count % 2 == 0 ? 165 : 166, "Execute");
            addText(36731 + (i + 1), "" + (36731 + (i + 1)), 0xFF981F, false, true, 52, tda, 0);
            scrollInterface.child(child++, 36731 + i, 95, y);
            scrollInterface.child(child++, 36731 + (i + 1), 100, y + 6);
            y += 23;
            count++;
        }
    }

    private static void slayerMain(TextDrawingArea[] tda) {
        RSInterface rsi = addTabInterface(46700);
        addSprite(46701, 468);
        addHoverButton(46702, 24, 16, 16, "Close", 250, 46703, 3);
        addHoveredButton(46703, 25, 16, 16, 46704);
        addText(46705, "Slayer", 0xff9933, true, true, -1, tda, 2);
        addConfigButton(46706, 46700, 298, 469, 103, 28, "View main tab", 0, 5, 710, false);
        addConfigButton(46707, 46700, 298, 469, 103, 28, "View duo tab", 1, 5, 710, false);
        addConfigButton(46708, 46700, 298, 469, 103, 28, "View task tab", 2, 5, 710, false);
        addConfigButton(46709, 46700, 298, 469, 103, 28, "View reward tab", 3, 5, 710, false);
        addText(46710, "Main", 0xff9933, true, true, -1, tda, 2);
        addText(46711, "Duo", 0xff9933, true, true, -1, tda, 2);
        addText(46712, "Unlocks", 0xff9933, true, true, -1, tda, 2);
        addText(46713, "Store", 0xff9933, true, true, -1, tda, 2);
        addText(46714, "58 \\nPoints", 0xff9933, true, true, -1, tda, 0);
        modelViewer(46715, 2, 315);
        addText(46716, "</col>Name: <col=FFB200>Fire giant", 0xFF981F, true, true, -1, tda, 0);
        addText(46717, "</col>Level: <col=FFB200>86", 0xFF981F, true, true, -1, tda, 0);
        addText(46718, "</col>Assigned: <col=FFB200>142", 0xFF981F, true, true, -1, tda, 0);
        addHoverButton(46719, 391, 118, 32, "Obtain Task", -1, 46720, 1);
        addHoveredButton(46720, 392, 118, 32, 46721);
        addHoverButton(46722, 391, 118, 32, "Cancel Task", -1, 46723, 1);
        addHoveredButton(46723, 392, 118, 32, 46724);
        addHoverButton(46725, 391, 118, 32, "Block Task", -1, 46726, 1);
        addHoveredButton(46726, 392, 118, 32, 46727);
        addText(46728, "Blocked tasks: (0/5)", 0xff9933, false, true, -1, tda, 0);
        addText(46729, "Obtain Task", 0xff9933, true, true, -1, tda, 1);
        addText(46730, "Cancel Task", 0xff9933, true, true, -1, tda, 1);
        addText(46731, "Block Task", 0xff9933, true, true, -1, tda, 1);
        addSprite(46732, 471);
        addSprite(46733, 472);
        addSprite(46734, 473);
        addText(46735, "</col>Remaining: <col=FFB200>86", 0xFF981F, true, true, -1, tda, 0);
        addText(46736, "</col>Location: <col=FFB200>Slayer cave", 0xFF981F, true, true, -1, tda, 0);
        addText(46737, "</col>Tasks Assigned: <col=FFB200>\\n 50", 0xFF981F, true, true, -1, tda, 0);
        addText(46738, "</col>Tasks Completed: <col=FFB200>\\n 40", 0xFF981F, true, true, -1, tda, 0);
        addText(46739, "</col>Tasks Cancelled: <col=FFB200>\\n 10", 0xFF981F, true, true, -1, tda, 0);
        addText(46740, "</col>Points accumulated: <col=FFB200>\\n 100", 0xFF981F, true, true, -1, tda, 0);
        addHoverButton(46741, 459, 16, 16, "View available slayer tasks", -1, 46742, 1);
        addHoveredButton(46742, 460, 16, 16, 46743);

        rsi.totalChildren(39);
        rsi.child(0, 46701, 8, 7);
        rsi.child(1, 46702, 477, 14);
        rsi.child(2, 46703, 477, 14);
        rsi.child(3, 46705, 255, 13);
        rsi.child(4, 46706, 15, 30);
        rsi.child(5, 46707, 117, 30);
        rsi.child(6, 46708, 117, 30);
        rsi.child(7, 46709, 219, 30);
        rsi.child(8, 46710, 64, 36);
        rsi.child(9, 46711, 166, 36);
        rsi.child(10, 46712, 166, 36);
        rsi.child(11, 46713, 268, 36);
        rsi.child(12, 46714, 460, 34);
        rsi.child(13, 46715, 50, 50);
        rsi.child(14, 46716, 190, 80);
        rsi.child(15, 46717, 190, 100);
        rsi.child(16, 46718, 190, 120);
        rsi.child(17, 46719, 49, 191);
        rsi.child(18, 46720, 49, 191);
        rsi.child(19, 46722, 193, 191);
        rsi.child(20, 46723, 193, 191);
        rsi.child(21, 46725, 336, 191);
        rsi.child(22, 46726, 336, 191);
        rsi.child(23, 46750, 242, 93);
        rsi.child(24, 46728, 343, 79);
        rsi.child(25, 46729, 115, 199);
        rsi.child(26, 46730, 260, 199);
        rsi.child(27, 46731, 405, 199);
        rsi.child(28, 46732, 55, 196);
        rsi.child(29, 46733, 200, 195);
        rsi.child(30, 46734, 345, 193);
        rsi.child(31, 46735, 190, 140);
        rsi.child(32, 46736, 190, 160);
        rsi.child(33, 46737, 80, 270);
        rsi.child(34, 46738, 190, 270);
        rsi.child(35, 46739, 300, 270);
        rsi.child(36, 46740, 420, 270);
        rsi.child(37, 46741, 460, 14);
        rsi.child(38, 46742, 460, 14);

        RSInterface scrollInterface = addTabInterface(46750);
        scrollInterface.scrollPosition = 0;
        scrollInterface.contentType = 0;
        scrollInterface.width = 226;
        scrollInterface.height = 86;
        scrollInterface.scrollMax = 115;

        scrollInterface.totalChildren(30);

        int y = 0;
        int count = 0;
        for (int i = 0, child = 0; i < 30; i += 3) {
            addSprite(46751 + i, count % 2 == 0 ? 470 : 298);
            addButton(46751 + (i + 1), 474, "Remove");
            addText(46751 + (i + 2), "" + (46751 + (i + 2)), 0xFF981F, false, true, 52, tda, 0);
            scrollInterface.child(child++, 46751 + i, 95, y);
            scrollInterface.child(child++, 46751 + (i + 1), 100, y + 6);
            scrollInterface.child(child++, 46751 + (i + 2), 120, y + 6);
            y += 23;
            count++;
        }
        interfaceCache[46707].isHidden = true;
        interfaceCache[46711].isHidden = true;
        interfaceCache[46741].isHidden = true;
        interfaceCache[46742].isHidden = true;
    }
    private static void slayerDuo(TextDrawingArea[] tda) {
        RSInterface rsi = addTabInterface(46800);

        addSprite(46801, 476);
        addText(46802, "Slayer Duo", 0xff9933, true, true, -1, tda, 2);
        addText(46803, "What is duo slayer?", 0xFF981F, true, true, -1, tda, 1);
        addText(46804, "Duo-slayer is when you team up with a partner of your choice to complete \\na slayer assignment.", 0xff9933, true, true, -1, tda, 0);
        addText(46805, "How to partner up with someone?", 0xFF981F, true, true, -1, tda, 1);
        addText(46806, "Finding a partner is easy! Simply ensure that both you and your partner has no\\nactive slayer assignment. Then, use an enchanted gem on them. They will\\nreceive an invitation of your partner request.", 0xff9933, true, true, -1, tda, 0);
        addText(46807, "How to cancel/assign duo tasks?", 0xFF981F, true, true, -1, tda, 1);
        addText(46808, "Once you have a partner, simply go to the main slayer tab and request an assignment.\\nYou can cancel your received task by clicking on the cancel button.", 0xff9933, true, true, -1, tda, 0);
        addText(46809, "Other information:", 0xFF981F, true, true, -1, tda, 1);
        addText(46810, "Tasks will be assigned only if both you and your partner have them unlocked.\\nExperience and points will be split between you and your partner.\\nTo remove a partner simply click on the cancel button in the main tab.", 0xff9933, true, true, -1, tda, 0);
        addText(46811, "Current partner: None", 0xFF0000, true, true, -1, tda, 0);

        rsi.totalChildren(24);
        rsi.child(0, 46801, 8, 7);
        rsi.child(1, RSInterface.interfaceCache[46700].children[1], 477, 14);
        rsi.child(2, RSInterface.interfaceCache[46700].children[2], 477, 14);
        rsi.child(3, 46802, 255, 13);
        rsi.child(4, RSInterface.interfaceCache[46700].children[4], 15, 30);
        rsi.child(5, RSInterface.interfaceCache[46700].children[5], 117, 30);
        rsi.child(6, RSInterface.interfaceCache[46700].children[6], 219, 30);
        rsi.child(7, RSInterface.interfaceCache[46700].children[7], 321, 30);
        rsi.child(8, RSInterface.interfaceCache[46700].children[8], 64, 36);
        rsi.child(9, RSInterface.interfaceCache[46700].children[9], 166, 36);
        rsi.child(10, RSInterface.interfaceCache[46700].children[10], 268, 36);
        rsi.child(11, RSInterface.interfaceCache[46700].children[11], 370, 36);
        rsi.child(12, RSInterface.interfaceCache[46700].children[12], 460, 34);
        rsi.child(13, RSInterface.interfaceCache[46700].children[37], 460, 14);
        rsi.child(14, RSInterface.interfaceCache[46700].children[38], 460, 14);
        rsi.child(15, 46803, 255, 85);
        rsi.child(16, 46804, 255, 100);
        rsi.child(17, 46805, 255, 125);
        rsi.child(18, 46806, 255, 140);
        rsi.child(19, 46807, 255, 175);
        rsi.child(20, 46808, 255, 190);
        rsi.child(21, 46809, 255, 215);
        rsi.child(22, 46810, 255, 230);
        rsi.child(23, 46811, 255, 275);
    }

    private static void slayerUnlock(TextDrawingArea[] tda) {
        RSInterface rsi = addTabInterface(46900);
        addSprite(46901, 475);
        addText(46902, "Slayer Unlock", 0xff9933, true, true, -1, tda, 2);
        rsi.totalChildren(16);
        rsi.child(0, 46901, 8, 7);
        rsi.child(1, RSInterface.interfaceCache[46700].children[1], 477, 14);
        rsi.child(2, RSInterface.interfaceCache[46700].children[2], 477, 14);
        rsi.child(3, 46902, 255, 13);
        rsi.child(4, RSInterface.interfaceCache[46700].children[4], 15, 30);
        rsi.child(5, RSInterface.interfaceCache[46700].children[5], 117, 30);
        rsi.child(6, RSInterface.interfaceCache[46700].children[6], 117, 30);
        rsi.child(7, RSInterface.interfaceCache[46700].children[7], 219, 30);
        rsi.child(8, RSInterface.interfaceCache[46700].children[8], 64, 36);
        rsi.child(9, RSInterface.interfaceCache[46700].children[9], 166, 36);
        rsi.child(10, RSInterface.interfaceCache[46700].children[10], 166, 36);
        rsi.child(11, RSInterface.interfaceCache[46700].children[11], 268, 36);
        rsi.child(12, RSInterface.interfaceCache[46700].children[12], 460, 34);
        rsi.child(13, 46910, -70, 72);
        rsi.child(14, RSInterface.interfaceCache[46700].children[37], 460, 11);
        rsi.child(15, RSInterface.interfaceCache[46700].children[38], 460, 11);

        RSInterface scrollInterface = addTabInterface(46910);
        scrollInterface.scrollPosition = 0;
        scrollInterface.contentType = 0;
        scrollInterface.width = 545;
        scrollInterface.height = 242;
        scrollInterface.scrollMax = 500;
        scrollInterface.totalChildren(60);
        int count = 0;
        for (int i = 0, child = 0; i < 60; i += 6) {
            int x = (i / 6) % 2;
            int y = (i / 6) / 2;
            x *= 300;
            y *= 78;
            addButton(46911 + i, 477, "Purchase");
            addText(46911 + (i + 1), "Task", 0xff9933, false, true, 52, tda, 3);
            addText(46911 + (i + 2), "Hello, this is a description of the fucking \\ntask lmao.", 0xFF981F, false, true, 52, tda, 0);
            addText(46911 + (i + 3), "5 points", 0xFF0000, false, true, 52, tda, 0);
            addConfigButton(46911 + (i + 4), 46900, 234, 235, 15, 15, "", 0, 5, 560 + count, false);
            addContainer(46911 + (i + 5), 3, 30, 10, 7, false, null, null, null, null, null);
            scrollInterface.child(child++, 46911 + i, count % 2 == 0 ? x + 90 : x + 17, y);
            scrollInterface.child(child++, 46911 + (i + 1), count % 2 == 0 ? x + 150 : x + 74, y + 13);
            scrollInterface.child(child++, 46911 + (i + 2), count % 2 == 0 ? x + 95 : x + 20, y + 36);
            scrollInterface.child(child++, 46911 + (i + 3), count % 2 == 0 ? x + 250 : x + 174, y + 5);
            scrollInterface.child(child++, 46911 + (i + 4), count % 2 == 0 ? x + 130 : x + 55, y + 16);
            scrollInterface.child(child++, 46911 + (i + 5), count % 2 == 0 ? x + 95 : x + 20, y);
            count++;
        }
    }
    private static void slayerReward(TextDrawingArea[] tda) {
        RSInterface rsi = addTabInterface(46500);

        addSprite(46501, 475);
        addText(46502, "Slayer Store", 0xff9933, true, true, -1, tda, 2);
        addContainer(46503, 8, 10, 22, 25, false, "Value", "Buy 1", "Buy 10");

        rsi.totalChildren(16);
        rsi.child(0, 46501, 8, 7);
        rsi.child(1, RSInterface.interfaceCache[46700].children[1], 477, 14);
        rsi.child(2, RSInterface.interfaceCache[46700].children[2], 477, 14);
        rsi.child(3, 46502, 255, 13);
        rsi.child(4, RSInterface.interfaceCache[46700].children[4], 15, 30);
        rsi.child(5, RSInterface.interfaceCache[46700].children[5], 117, 30);
        rsi.child(6, RSInterface.interfaceCache[46700].children[6], 117, 30);
        rsi.child(7, RSInterface.interfaceCache[46700].children[7], 219, 30);
        rsi.child(8, RSInterface.interfaceCache[46700].children[8], 64, 36);
        rsi.child(9, RSInterface.interfaceCache[46700].children[9], 166, 36);
        rsi.child(10, RSInterface.interfaceCache[46700].children[10], 166, 36);
        rsi.child(11, RSInterface.interfaceCache[46700].children[11], 268, 36);
        rsi.child(12, RSInterface.interfaceCache[46700].children[12], 460, 34);
        rsi.child(13, 46503, 45, 85);
        rsi.child(14, RSInterface.interfaceCache[46700].children[37], 460, 14);
        rsi.child(15, RSInterface.interfaceCache[46700].children[38], 460, 14);
    }


    public static void slayerConfirm(TextDrawingArea[] tda) {
        RSInterface rsi = addTabInterface(46400);

        addSprite(46401, 478);
        addText(46402, "Slayer Confirmation", 0xff9933, true, true, -1, tda, 2);
        addText(46403, "Reward name", 0xff9933, true, true, -1, tda, 1);
        addText(46404, "Description of the poop", 0xFF981F, true, true, -1, tda, 0);
        addText(46405, "Pay 1000 points?", 0xFF0000, true, true, -1, tda, 1);
        addHoverButton(46406, 391, 118, 32, "Go back", -1, 46407, 1);
        addHoveredButton(46407, 392, 118, 32, 46408);
        addHoverButton(46409, 391, 118, 32, "Confirm", -1, 46410, 1);
        addHoveredButton(46410, 392, 118, 32, 46411);
        addText(46412, "Go back", 0xF04648, true, true, -1, tda, 1);
        addText(46413, "Confirm", 0x49C951, true, true, -1, tda, 1);
        addContainer(46414, 8, 10, 22, 25, false);

        rsi.totalChildren(14);
        rsi.child(0, 46401, 8, 7);
        rsi.child(1, 46402, 255, 11);
        rsi.child(2, RSInterface.interfaceCache[46700].children[1], 477, 11);
        rsi.child(3, RSInterface.interfaceCache[46700].children[2], 477, 11);
        rsi.child(4, 46403, 255, 115);
        rsi.child(5, 46404, 255, 140);
        rsi.child(6, 46405, 255, 175);
        rsi.child(7, 46406, 114, 206);
        rsi.child(8, 46407, 114, 206);
        rsi.child(9, 46409, 278, 206);
        rsi.child(10, 46410, 278, 206);
        rsi.child(11, 46412, 170, 214);
        rsi.child(12, 46413, 335, 214);
        rsi.child(13, 46414, 185, 70);
    }

    private static void slayerTasks(TextDrawingArea[] tda) {
        RSInterface rsi = addTabInterface(46300);
        addSprite(46301, 479);
        addText(46302, "Available Slayer Tasks", 0xff9933, true, true, -1, tda, 2);
        addHoverButton(46303, 71, 16, 16, "Return to main menu", -1, 46304, 1);
        addHoveredButton(46304, 72, 16, 16, 46305);
        addText(46306, "Name:", 0xff9933, true, true, -1, tda, 0);
        addText(46307, "Combat\\nLevel:", 0xff9933, true, true, -1, tda, 0);
        addText(46308, "Slayer Level\\nRequired:", 0xff9933, true, true, -1, tda, 0);
        addText(46309, "", 0xff9933, true, true, -1, tda, 0);
        rsi.totalChildren(11);
        rsi.child(0, 46301, 116, 35);
        rsi.child(1, 46302, 245, 45);
        rsi.child(2, RSInterface.interfaceCache[46700].children[1], 365, 44);
        rsi.child(3, RSInterface.interfaceCache[46700].children[2], 365, 44);
        rsi.child(4, 46303, 348, 44);
        rsi.child(5, 46304, 348, 44);
        rsi.child(6, 46306, 160, 85);
        rsi.child(7, 46307, 268, 80);
        rsi.child(8, 46308, 328, 80);
        rsi.child(9, 46320, 138, 105);
        rsi.child(10, 46309, 245, 60);

        RSInterface scrollInterface = addTabInterface(46320);
        scrollInterface.scrollPosition = 0;
        scrollInterface.contentType = 0;
        scrollInterface.width = 218;
        scrollInterface.height = 177;
        scrollInterface.scrollMax = 255;
        scrollInterface.totalChildren(60);
        int y = 1;
        int child2 = 0;
        for (int i = 0, child = 0; i < 60; i += 4) {
            addSprite(46321 + i, 480);
            addText(46321 + (1 + i), "", tda, 0, 0xDE8B0D, false, true);
            addText(46321 + (2 + i), "", tda, 0, 0xDE8B0D, true, true);
            addText(46321 + (3 + i), "", tda, 0, 0xDE8B0D, true, true);
            scrollInterface.child(child++, 46321 + i, 0, y);
            scrollInterface.child(child++, 46321 + (1 + i), 3, y + 8);
            scrollInterface.child(child++, 46321 + (2 + i), 129, y + 8);
            scrollInterface.child(child++, 46321 + (3 + i), 185, y + 8);
            child2 += 4;
            y += 25;
        }
    }

    private static void colorChanger(TextDrawingArea[] tda) {
        RSInterface rsi = addTabInterface(56700);
        addSprite(56701, 464);
        addText(56702, "Color Changer", 0xE1981F, true, true, -1, tda, 2);
        addHoverButton(56703, 24, 16, 16, "Close", 250, 56704, 3);
        addHoveredButton(56704, 25, 16, 16, 56705);
        addText(56706, "Private message", 0xff9933, false, true, -1, tda, 0);
        addText(56707, "Player title", 0xff9933, false, true, -1, tda, 0);
        addText(56708, "Yell", 0xff9933, false, true, -1, tda, 0);
        addText(56709, "Fog", 0xff9933, false, true, -1, tda, 0);
        addColorBox(56710, 0xff0000, 178, 40, true);
        addColorBox(56711, 0xff0000, 12, 12, false);
        addColorBox(56712, 0xff0000, 12, 12, false);
        addColorBox(56713, 0xff0000, 12, 12, false);
        addColorBox(56714, 0xff0000, 12, 12, false);
        addHDButton(56715, 462, "Select");
        interfaceCache[56715].atActionType = 10;
        addButton(56716, 461, "Select");
        interfaceCache[56716].atActionType = 10;
        addSprite(56717, 463);
        addColorBox(56718, 0xff0000, 133, 133, false);
        addText(56719, "Hello, this is a preview.", 0xFFFFFF, true, true, -1, tda, 0);
        addHoverButton(56750, 298, 192, 23, "Select private message color", -1, 56751, 1);
        addHoveredButton(56751, 465, 192, 23, 56752);
        addHoverButton(56753, 298, 192, 23, "Select player title color", -1, 56754, 1);
        addHoveredButton(56754, 465, 192, 23, 56755);
        addHoverButton(56756, 298, 192, 23, "Select yell color", -1, 56757, 1);
        addHoveredButton(56757, 465, 192, 23, 56758);
        addHoverButton(56759, 298, 192, 23, "Select fog color", -1, 56760, 1);
        addHoveredButton(56760, 465, 192, 23, 56761);
        addConfigButton(56762, 56700, 298, 466, 0, 0, "", 0, 5, 836);
        addHoverButton(56763, 298, 74, 26, "Reset to default", -1, 56764, 1);
        addHoveredButton(56764, 467, 74, 26, 56765);
        addHoverButton(56766, 298, 74, 26, "Confirm", -1, 56767, 1);
        addHoveredButton(56767, 467, 74, 26, 56768);
        addText(56769, "Default", 0xff9933, true, true, -1, tda, 0);
        addText(56770, "Confirm", 0xff9933, true, true, -1, tda, 0);

        rsi.totalChildren(33);
        rsi.child(0, 56701, 34, 50);
        rsi.child(1, 56702, 255, 53);
        rsi.child(2, 56703, 450, 54);
        rsi.child(3, 56704, 450, 54);
        rsi.child(4, 56750, 253, 98);
        rsi.child(5, 56751, 253, 98);
        rsi.child(6, 56753, 253, 130);
        rsi.child(7, 56754, 253, 130);
        rsi.child(8, 56756, 253, 162);
        rsi.child(9, 56757, 253, 162);
        rsi.child(10, 56759, 253, 193);
        rsi.child(11, 56760, 253, 193);
        rsi.child(12, 56706, 260, 104);
        rsi.child(13, 56707, 260, 135);
        rsi.child(14, 56708, 260, 168);
        rsi.child(15, 56709, 260, 200);
        rsi.child(16, 56710, 168, 248);
        rsi.child(17, 56711, 423, 103);
        rsi.child(18, 56718, 58, 90);
        rsi.child(19, 56712, 423, 135);
        rsi.child(20, 56713, 423, 167);
        rsi.child(21, 56714, 423, 198);
        rsi.child(22, 56715, 58, 90);
        rsi.child(23, 56716, 198, 90);
        rsi.child(24, 56717, 196, 89);
        rsi.child(25, 56762, 172, 254);
        rsi.child(26, 56719, 257, 262);
        rsi.child(27, 56763, 66, 254);
        rsi.child(28, 56764, 66, 254);
        rsi.child(29, 56766, 374, 254);
        rsi.child(30, 56767, 374, 254);
        rsi.child(31, 56769, 102, 262);
        rsi.child(32, 56770, 409, 262);

        RSInterface privateMessage = addTabInterface(56720);
        privateMessage.totalChildren(1);
        privateMessage.child(0, 56700, 0, 0);

        RSInterface title = addTabInterface(56721);
        title.totalChildren(1);
        title.child(0, 56700, 0, 0);

        RSInterface yell = addTabInterface(56722);
        yell.totalChildren(1);
        yell.child(0, 56700, 0, 0);

        RSInterface fog = addTabInterface(56723);
        fog.totalChildren(1);
        fog.child(0, 56700, 0, 0);
    }

    public static void experienceSetting(TextDrawingArea[] tda) {
        RSInterface rsi = addTabInterface(56500);

        addSprite(56501, 456);
        addText(56502, "Experience Counter Management", 0xff981f, true, true, -1, tda, 2);
        addHoverButton(56503, 24, 16, 16, "Close", -1, 56504, 3);
        addHoveredButton(56504, 25, 16, 16, 56505);

        addText(56506, "Progress Bar:", 0xff981f, true, true, -1, tda, 1);
        addText(56507, "Text Size:", 0xff981f, true, true, -1, tda, 1);
        addText(56508, "Text Color:", 0xff981f, true, true, -1, tda, 1);
        addText(56509, "Position:", 0xff981f, true, true, -1, tda, 1);
        addText(56510, "Duration:", 0xff981f, true, true, -1, tda, 1);
        addText(56511, "Grouped:", 0xff981f, true, true, -1, tda, 1);
        addText(56512, "Skill Orbs:", 0xff981f, true, true, -1, tda, 1);
        addText(56513, "Experience Rate:", 0xff981f, true, true, -1, tda, 1);

        addDropdownMenu(56514, 125, 0, false, true, Dropdown.COUNTER_PROGRESS, "Enabled", "Disabled");
        addDropdownMenu(56515, 125, 0, false, true, Dropdown.COUNTER_SIZE, "Small", "Regular", "Large");
        addDropdownMenu(56516, 125, 0, false, true, Dropdown.COUNTER_COLOR, "White", "Cyan", "Lime", "Pink", "Red", "Orange");
        addDropdownMenu(56517, 125, 0, false, true, Dropdown.COUNTER_POSITION, "Right", "Middle", "Left");
        addDropdownMenu(56518, 125, 0, false, true, Dropdown.COUNTER_SPEED, "Normal", "Slow", "Fast");
        addDropdownMenu(56519, 125, 0, false, true, Dropdown.DEFAULT, "Disabled", "Enabled");
        addDropdownMenu(56520, 125, 0, false, true, Dropdown.SKILL_ORB, "Enabled", "Disabled");
        addDropdownMenu(56521, 125, 0, false, true, Dropdown.DEFAULT, "1000x (Default)", "4x (Runescape)");
        rsi.totalChildren(20);
        rsi.child(0, 56501, 45, 70);
        rsi.child(1, 56502, 260, 73);
        rsi.child(2, 56503, 430, 73);
        rsi.child(3, 56504, 430, 73);
        rsi.child(4, 56506, 130, 205);
        rsi.child(5, 56507, 260, 205);
        rsi.child(6, 56508, 385, 205);
        rsi.child(7, 56509, 130, 155);
        rsi.child(8, 56510, 260, 155);
        rsi.child(9, 56511, 385, 155);
        rsi.child(10, 56512, 190, 105);
        rsi.child(11, 56513, 320, 105);
        rsi.child(12, 56514, 64, 225);
        rsi.child(13, 56515, 194, 225);
        rsi.child(14, 56516, 325, 225);
        rsi.child(15, 56517, 64, 174);
        rsi.child(16, 56518, 194, 174);
        rsi.child(17, 56519, 325, 174);
        rsi.child(18, 56520, 129, 123);
        rsi.child(19, 56521, 261, 123);
    }

    public static void prestige(TextDrawingArea[] daniel) {
        RSInterface tab = addInterface(52000);
        addSprite(52001, 554);
        addChar(52002, 675);
        addHoverButton(52003, 24, 16, 16, "Close", 250, 52004, 3);
        addHoveredButton(52004, 25, 16, 16, 52005);
        addText(52006, "Prestige Panel", daniel, 2, 0xff981f, false, true);
        addText(52007, "Daniel", daniel, 1, 0xff981f, true, true);
        addText(52008, "Total Prestige(s): 0", daniel, 1, 0xff981f, true, true);
        addText(52009, "Prestige Pnt(s): 0", daniel, 1, 0xff981f, true, true);
        addText(52010, "Attack (0)", daniel, 0, 0xff981f, true, true);
        addText(52011, "Strength (0)", daniel, 0, 0xff981f, true, true);
        addText(52012, "Defence (0)", daniel, 0, 0xff981f, true, true);
        addText(52013, "Ranged (0)", daniel, 0, 0xff981f, true, true);
        addText(52014, "Prayer (0)", daniel, 0, 0xff981f, true, true);
        addText(52015, "Magic (0)", daniel, 0, 0xff981f, true, true);
        addText(52016, "RC (0)", daniel, 0, 0xff981f, true, true);
        addText(52017, "Hitpoints (0)", daniel, 0, 0xff981f, true, true);
        addText(52018, "Agility (0)", daniel, 0, 0xff981f, true, true);
        addText(52019, "Herblore (0)", daniel, 0, 0xff981f, true, true);
        addText(52020, "Thieving (0)", daniel, 0, 0xff981f, true, true);
        addText(52021, "Crafting (0)", daniel, 0, 0xff981f, true, true);
        addText(52022, "Fletching (0)", daniel, 0, 0xff981f, true, true);
        addText(52023, "Slayer (0)", daniel, 0, 0xff981f, true, true);
        addText(52024, "Mining (0)", daniel, 0, 0xff981f, true, true);
        addText(52025, "Smithing (0)", daniel, 0, 0xff981f, true, true);
        addText(52026, "Fishing (0)", daniel, 0, 0xff981f, true, true);
        addText(52027, "Cooking (0)", daniel, 0, 0xff981f, true, true);
        addText(52028, "Firemaking (0)", daniel, 0, 0xff981f, true, true);
        addText(52029, "Woodcutting (0)", daniel, 0, 0xff981f, true, true);
        addText(52030, "Farming (0)", daniel, 0, 0xff981f, true, true);
        addText(52031, "Hunter (0)", daniel, 0, 0xff981f, true, true);
        addHoverButton(52032, "", 0, 104, 30, "Prestige Attack", 0, 52033, 1);
        addHoveredButton(52033, 555, 104, 30, 52034);
        addHoverButton(52035, "", 0, 104, 30, "Prestige Strength", 0, 52036, 1);
        addHoveredButton(52036, 556, 104, 30, 52037);
        addHoverButton(52038, "", 0, 104, 30, "Prestige Defence", 0, 52039, 1);
        addHoveredButton(52039, 557, 104, 30, 52040);
        addHoverButton(52041, "", 0, 104, 30, "Prestige Ranged", 0, 52042, 1);
        addHoveredButton(52042, 558, 104, 30, 52043);
        addHoverButton(52044, "", 0, 104, 30, "Prestige Prayer", 0, 52045, 1);
        addHoveredButton(52045, 559, 104, 30, 52046);
        addHoverButton(52047, "", 0, 104, 30, "Prestige Magic", 0, 52048, 1);
        addHoveredButton(52048, 560, 104, 30, 52049);
        addHoverButton(52050, "", 0, 104, 30, "Prestige Runecrafting", 0, 52051, 1);
        addHoveredButton(52051, 561, 104, 30, 52052);
        addHoverButton(52053, "", 0, 104, 30, "Prestige Hitpoints", 0, 52054, 1);
        addHoveredButton(52054, 562, 104, 30, 52055);
        addHoverButton(52056, "", 0, 104, 30, "Prestige Agility", 0, 52057, 1);
        addHoveredButton(52057, 563, 104, 30, 52058);
        addHoverButton(52059, "", 0, 104, 30, "Prestige Herblore", 0, 52060, 1);
        addHoveredButton(52060, 564, 104, 30, 52061);
        addHoverButton(52062, "", 0, 104, 30, "Prestige Thieving", 0, 52063, 1);
        addHoveredButton(52063, 565, 104, 30, 52064);
        addHoverButton(52065, "", 0, 104, 30, "Prestige Crafting", 0, 52066, 1);
        addHoveredButton(52066, 566, 104, 30, 52067);
        addHoverButton(52068, "", 0, 104, 30, "Prestige Fletching", 0, 52069, 1);
        addHoveredButton(52069, 567, 104, 30, 52070);
        addHoverButton(52071, "", 0, 104, 30, "Prestige Slayer", 0, 52072, 1);
        addHoveredButton(52072, 568, 104, 30, 52073);
        addHoverButton(52074, "", 0, 104, 30, "Prestige Mining", 0, 52075, 1);
        addHoveredButton(52075, 569, 104, 30, 52076);
        addHoverButton(52077, "", 0, 104, 30, "Prestige Smithing", 0, 52078, 1);
        addHoveredButton(52078, 570, 104, 30, 52079);
        addHoverButton(52080, "", 0, 104, 30, "Prestige Fishing", 0, 52081, 1);
        addHoveredButton(52081, 571, 104, 30, 52082);
        addHoverButton(52083, "", 0, 104, 30, "Prestige Cooking", 0, 52084, 1);
        addHoveredButton(52084, 572, 104, 30, 52085);
        addHoverButton(52086, "", 0, 104, 30, "Prestige Firemaking", 0, 52087, 1);
        addHoveredButton(52087, 573, 104, 30, 52088);
        addHoverButton(52089, "", 0, 104, 30, "Prestige Woodcutting", 0, 52090, 1);
        addHoveredButton(52090, 574, 104, 30, 52091);
        addHoverButton(52092, "", 0, 104, 30, "Prestige Farming", 0, 52093, 1);
        addHoveredButton(52093, 575, 104, 30, 52094);
        addHoverButton(52095, "", 0, 104, 30, "Prestige Slayer", 0, 52096, 1);
        addHoveredButton(52096, 576, 104, 30, 52097);
        tab.totalChildren(74);
        tab.child(0, 52001, 10, 2);
        tab.child(1, 52002, 28, 150);
        tab.child(2, 52003, 475, 10);
        tab.child(3, 52004, 475, 10);
        tab.child(4, 52032, 181, 37);
        tab.child(5, 52033, 181, 37);
        tab.child(6, 52035, 181, 74);
        tab.child(7, 52036, 181, 74);
        tab.child(8, 52038, 181, 111);
        tab.child(9, 52039, 181, 111);
        tab.child(10, 52041, 181, 148);
        tab.child(11, 52042, 181, 148);
        tab.child(12, 52044, 181, 185);
        tab.child(13, 52045, 181, 185);
        tab.child(14, 52047, 181, 222);
        tab.child(15, 52048, 181, 222);
        tab.child(16, 52050, 181, 259);
        tab.child(17, 52051, 181, 259);
        tab.child(18, 52053, 286, 37);
        tab.child(19, 52054, 286, 37);
        tab.child(20, 52056, 286, 74);
        tab.child(21, 52057, 286, 74);
        tab.child(22, 52059, 286, 111);
        tab.child(23, 52060, 286, 111);
        tab.child(24, 52062, 286, 148);
        tab.child(25, 52063, 286, 148);
        tab.child(26, 52065, 286, 185);
        tab.child(27, 52066, 286, 185);
        tab.child(28, 52068, 286, 222);
        tab.child(29, 52069, 286, 222);
        tab.child(30, 52071, 286, 259);
        tab.child(31, 52072, 286, 259);
        tab.child(32, 52074, 390, 37);
        tab.child(33, 52075, 390, 37);
        tab.child(34, 52077, 390, 74);
        tab.child(35, 52078, 390, 74);
        tab.child(36, 52080, 390, 111);
        tab.child(37, 52081, 390, 111);
        tab.child(38, 52083, 390, 148);
        tab.child(39, 52084, 390, 148);
        tab.child(40, 52086, 390, 185);
        tab.child(41, 52087, 390, 185);
        tab.child(42, 52089, 390, 222);
        tab.child(43, 52090, 390, 222);
        tab.child(44, 52092, 390, 259);
        tab.child(45, 52093, 390, 259);
        tab.child(46, 52095, 286, 293);
        tab.child(47, 52096, 286, 293);
        tab.child(48, 52006, 220, 12 - 2);
        tab.child(49, 52007, 95, 270 - 2);
        tab.child(50, 52008, 95, 285 - 2);
        tab.child(51, 52009, 95, 300 - 2);
        tab.child(52, 52010, 235, 46);
        tab.child(53, 52011, 235, 85);
        tab.child(54, 52012, 235, 121);
        tab.child(55, 52013, 235, 158);
        tab.child(56, 52014, 235, 195);
        tab.child(57, 52015, 235, 232);
        tab.child(58, 52016, 235, 269);
        tab.child(59, 52017, 345, 46);
        tab.child(60, 52018, 345, 85);
        tab.child(61, 52019, 345, 121);
        tab.child(62, 52020, 345, 158);
        tab.child(63, 52021, 345, 195);
        tab.child(64, 52022, 345, 232);
        tab.child(65, 52023, 345, 269);
        tab.child(66, 52024, 450, 46);
        tab.child(67, 52025, 450, 85);
        tab.child(68, 52026, 450, 121);
        tab.child(69, 52027, 450, 158);
        tab.child(70, 52028, 450, 195);
        tab.child(71, 52029, 450, 232);
        tab.child(72, 52030, 450, 269);
        tab.child(73, 52031, 345, 303);
    }


    public static void barrowsChest(TextDrawingArea[] TDA) {
        RSInterface tab = addTabInterface(33300);
        addSprite(33301, 181);
        addHoverButton(33302, 252, 21, 21, "Close", 250, 33303, 3);
        addHoveredButton(33303, 253, 21, 21, 33304);
        addContainer(33305, 3, 30, 10, 7, false, null, null, null, null, null);
        addText(33306, "Total worth: 500k", TDA, 0, 0xff9933, true, true);

        tab.totalChildren(5);
        tab.child(0, 33301, 140, 60);
        tab.child(1, 33302, 342, 67);
        tab.child(2, 33303, 342, 67);
        tab.child(3, 33305, 240, 105);
        tab.child(4, 33306, 305, 230);
    }

    public static void simpleBounty(TextDrawingArea[] TDA) {
        RSInterface tab = addTabInterface(23400);
        tab.totalChildren(3);
        tab.child(0, RSInterface.interfaceCache[23300].children[20], 460, 275);
        tab.child(1, RSInterface.interfaceCache[23300].children[21], 473, 302);
        tab.child(2, RSInterface.interfaceCache[23300].children[22], 473, 299);
    }

    public static void simpleSafe(TextDrawingArea[] TDA) {
        RSInterface tab = addTabInterface(23410);
        addSprite(23411, 237);

        tab.totalChildren(3);
        tab.child(0, 23411, 460, 275);
        tab.child(1, RSInterface.interfaceCache[23300].children[21], 473, 302);
        tab.child(2, RSInterface.interfaceCache[23300].children[22], 473, 299);
    }

    public static void bounty(TextDrawingArea[] TDA) {
        RSInterface tab = addTabInterface(23300);
        addTransparentSprite(23301, 403);
        addTransparentSprite(23302, 404);
        addConfigSprite(23303, 405, -1, 0, 876);
        addSprite(23304, 406);
        addText(23305, "---", TDA, 0, 0xffff00, true, true);
        addText(23306, "Target:", TDA, 0, 0xffff00, true, true);
        addText(23307, "None", TDA, 1, 0xffffff, true, true);
        addText(23308, "Level: ------", TDA, 0, 0xffff00, true, true);
        addText(23309, "Current  Record", TDA, 0, 0xffff00, true, true);
        addText(23310, "0", TDA, 0, 0xffff00, true, true);
        addText(23311, "0", TDA, 0, 0xffff00, true, true);
        addText(23312, "0", TDA, 0, 0xffff00, true, true);
        addText(23313, "0", TDA, 0, 0xffff00, true, true);
        addText(23314, "Rogue:", TDA, 0, 0xffff00, true, true);
        addText(23315, "Hunter:", TDA, 0, 0xffff00, true, true);
        addConfigSprite(23316, 238, -1, 0, 877);
        addConfigSprite(23317, 239, -1, 1, 877);
        addConfigSprite(23318, 240, -1, 2, 877);
        addConfigSprite(23319, 241, -1, 3, 877);
        addConfigSprite(23320, 242, -1, 4, 877);
        addConfigSprite(23325, 236, 236, 1, 598);
        addText(23326, "", TDA, 0, 0xff0000, true, true);
        addText(23327, "Wild lvl", TDA, 1, 0xF6FF00, true, true);
        tab.totalChildren(23);
        tab.child(0, 23301, 319, 8);
        tab.child(1, 23302, 339, 56);
        tab.child(2, 23303, 345, 18);
        tab.child(3, 23304, 348, 73);
        tab.child(4, 23305, 358, 41);
        tab.child(5, 23306, 455, 12);
        tab.child(6, 23307, 456, 25);
        tab.child(7, 23308, 457, 41);
        tab.child(8, 23309, 460, 59);
        tab.child(9, 23310, 438, 72);
        tab.child(10, 23311, 481, 72);
        tab.child(11, 23312, 438, 85);
        tab.child(12, 23313, 481, 85);
        tab.child(13, 23314, 393, 72);
        tab.child(14, 23315, 394, 85);
        tab.child(15, 23316, 345, 18);
        tab.child(16, 23317, 345, 18);
        tab.child(17, 23318, 345, 18);
        tab.child(18, 23319, 345, 18);
        tab.child(19, 23320, 345, 18);
        tab.child(20, 23325, 460, 275);
        tab.child(21, 23326, 473, 302);
        tab.child(22, 23327, 473, 303);
    }

    private static void personalStores(TextDrawingArea[] tda) {
        RSInterface tab = addInterface(53000);
        addSprite(53001, 375);
        addText(53002, "Personal Stores", 0xff9933, true, true, -1, tda, 2);
        addInputField(53003, 15, 0xFF981F, "Search for player", 122, 25, false, false, "[A-Za-z0-9 ]");
        addInputField(53004, 15, 0xFF981F, "Search for item", 122, 25, false, false, "[A-Za-z0-9 ]");
        addText(53005, "Filter: <col=FFB83F>highest price", 0xFF981F, false, true, -1, tda, 0);
        addText(53006, "Filter: <col=FFB83F>lowest price", 0xFF981F, false, true, -1, tda, 0);
        addText(53007, "Featured:", 0xff9933, false, true, -1, tda, 2);
        addText(53023, "Available stores: 0", 0xff9933, false, true, -1, tda, 0);
        for (int index = 0; index < 10; index++) {
            addHoverText(53008 + index, "<clan=6> Daniel", "", tda, 0, 0xCF4F0A, false, true, 100);
        }
        addText(53018, "Owner:", 0xff9933, false, true, -1, tda, 3);
        addText(53019, "Caption:", 0xff9933, false, true, -1, tda, 3);
        addHoverButton(53020, 24, 16, 16, "Close", 250, 53021, 3);
        addHoveredButton(53021, 25, 16, 16, 53022);
        addSprite(53024, 248);
        addConfigButton(53025, 53000, 235, 234, 15, 15, "", 0, 5, 838);
        addConfigButton(53026, 53000, 235, 234, 15, 15, "", 1, 5, 838);
        tab.totalChildren(35);
        tab.child(0, 53001, 9, 6 - 5);
        tab.child(1, 53002, 255, 10 + 1);
        tab.child(2, 53003, 16 - 1, 31 + 4);
        tab.child(3, 53004, 16 - 1, 58 + 4);
        tab.child(4, 53005, 37, 90 + 5);
        tab.child(5, 53006, 37, 108 + 5);
        tab.child(6, 53007, 46, 127 + 5);
        for (int index = 0, y = 150; index < 10; index++, y += 17) {
            tab.child(7 + index, 53008 + index, 20, y + 5);
        }
        tab.child(17, 53018, 168, 31 + 5);
        tab.child(18, 53019, 335, 31 + 5);
        tab.child(19, 53030, -118, 54 + 6);
        tab.child(20, 53020, 478, 10 + 1);
        tab.child(21, 53021, 478, 10 + 1);
        tab.child(22, 53023, 365, 13);
        tab.child(23, 53024, 17, 164 + 5);
        tab.child(24, 53024, 17, 181 + 5);
        tab.child(25, 53024, 17, 198 + 5);
        tab.child(26, 53024, 17, 215 + 5);
        tab.child(27, 53024, 17, 232 + 5);
        tab.child(28, 53024, 17, 249 + 5);
        tab.child(29, 53024, 17, 266 + 5);
        tab.child(30, 53024, 17, 283 + 5);
        tab.child(31, 53024, 17, 300 + 5);
        tab.child(32, 53024, 17, 317 + 5);
        tab.child(33, 53025, 20, 88 + 5);
        tab.child(34, 53026, 20, 106 + 5);
        RSInterface scrollInterface = addTabInterface(53030);
        scrollInterface.width = 597;
        scrollInterface.height = 265;
        scrollInterface.scrollMax = 563;
        setChildren(60, scrollInterface);
        int child = 0;
        int y = 0;
        int x = 7;
        int sprite = 0;
        for (int i = 0; i < 60; i += 3) {
            int id = (449 + (sprite % 2 == 0 ? 1 : 0));
            sprite++;
            addButton(53031 + i, id, "View store");
            addText((53031 + (i + 1)), "Daniel", tda, 0, 0xFFB83F, false, true);
            addText((53031 + (i + 2)), "Best store in town!", tda, 0, 0xFFB83F, false, true);
            scrollInterface.child(child++, 53031 + i, x + 253, y + -1);
            scrollInterface.child(child++, (53031 + (i + 1)), x + 260, y + 9);
            scrollInterface.child(child++, (53031 + (i + 2)), x + 360, y + 9);
            y += 28;
        }
    }

    private static void dropViewer(TextDrawingArea[] tda) {
        RSInterface tab = addInterface(54500);
        addSprite(54501, 483);
        addHoverButton(54502, 24, 16, 16, "Close", 250, 54503, 3);
        addHoveredButton(54503, 25, 16, 16, 54504);
        addText(54505, "Monster Drop Guide", 0xff9933, true, true, -1, tda, 2);
        addInputField(54506, 15, 0xFF981F, "Search for item", 111, 25, false, false, "[A-Za-z0-9 ]");
        addInputField(54507, 15, 0xFF981F, "Search for Npc", 111, 25, false, false, "[A-Za-z0-9 ]");
        addText(54508, "Item:", 0xff9933, true, true, -1, tda, 1);
        addText(54509, "Min:", 0xff9933, true, true, -1, tda, 1);
        addText(54510, "Max:", 0xff9933, true, true, -1, tda, 1);
        addText(54511, "Chance:", 0xff9933, true, true, -1, tda, 1);
        addText(54512, "Information:", 0xff9933, true, true, -1, tda, 0);
        addText(54513, "Name: Man", 0xFFB83F, true, true, -1, tda, 0);
        addText(54514, "Combat level: 2", 0xFFB83F, true, true, -1, tda, 0);

        tab.totalChildren(15);
        tab.child(0, 54501, 9, 5);
        tab.child(1, 54502, 477, 12);
        tab.child(2, 54503, 477, 12);
        tab.child(3, 54505, 255, 12);
        tab.child(4, 54506, 21, 90);
        tab.child(5, 54507, 21, 116);
        tab.child(6, 54508, 202, 35);
        tab.child(7, 54509, 331, 35);
        tab.child(8, 54510, 379, 35);
        tab.child(9, 54511, 441, 35);
        tab.child(10, 54550, -118, 55);
        tab.child(11, 54515, -235, 147);
        tab.child(12, 54512, 75, 40);
        tab.child(13, 54513, 75, 58);
        tab.child(14, 54514, 75, 73);

        RSInterface mainScroll = addTabInterface(54550);
        mainScroll.width = 598;
        mainScroll.height = 268;
        mainScroll.scrollMax = 1911;

        setChildren(450 + 1, mainScroll);
        int child = 0, y = 1, x = 7, sprite = 0;
        for (int i = 0; i < 450; i += 5) {
            int id = sprite % 2 == 0 ? 481 : 482;
            sprite++;
            addSprite(54552 + i, id);
            addText((54552 + (i + 1)), "" + (54552 + (i + 1)), tda, 0, sprite % 2 == 0 ? 0xFFB83F : 0xFF981F, false, false);
            addText((54552 + (i + 2)), "" + (54552 + (i + 2)), tda, 0, sprite % 2 == 0 ? 0xFFB83F : 0xFF981F, true, false);
            addText((54552 + (i + 3)), "" + (54552 + (i + 3)), tda, 0, sprite % 2 == 0 ? 0xFFB83F : 0xFF981F, true, false);
            addText((54552 + (i + 4)), "" + (54552 + (i + 4)), tda, 0, 0xFF981F, true, false);
            mainScroll.child(child++, 54552 + i, x + 254, y);
            mainScroll.child(child++, (54552 + (i + 1)), x + 300, y + 10);
            mainScroll.child(child++, (54552 + (i + 2)), x + 440, y + 10);
            mainScroll.child(child++, (54552 + (i + 3)), x + 490, y + 10);
            mainScroll.child(child++, (54552 + (i + 4)), x + 550, y + 6);
            y += 32;
        }
        addContainer(54551, 0, 1, 80, 30, 0, 150, false, false, false, null, null, null, null, null);
        mainScroll.child(child++, 54551, 265, 0);

        RSInterface npcListScroll = addTabInterface(54515);
        npcListScroll.width = 352;
        npcListScroll.height = 172;
        npcListScroll.scrollMax = 181;
        setChildren(30, npcListScroll);
        int y2 = 0, child2 = 0, count = 0;
        for (int i = 0; i < 30; i += 2) {
            addButton((54516 + i), count % 2 == 0 ? 485 : 484, "");
            addHoverText((54516 + (i + 1)), "", "", tda, 0, 0xFFB83F, false, false, 100);
            npcListScroll.child(child2++, (54516 + i), 255, y2);
            npcListScroll.child(child2++, (54516 + (i + 1)), 256, y2 + 3);
            y2 += 18;
            count++;
        }
    }

    public static void screenOptions(TextDrawingArea tda[]) {

        RSInterface tab = addInterface(28200);
        addSprite(28201, 371);
        addText(28202, "Screen Options", tda, 2, 0xff9933, true, true);
        addHoverButton(28203, 24, 21, 21, "Close", 250, 28204, 3);
        addHoveredButton(28204, 25, 21, 21, 28205);
        addButton(28206, 374, "Transparent side panel");
        addButton(28207, 373, "Transparent chatbox");
        addButton(28208, 372, "Side-stones arrangement");
        addText(28209, "Transparent side panel", tda, 1, 0xff9933, false, true);
        addText(28210, "Make each side panel transparent", tda, 0, 0xff9933, false, true);
        addText(28211, "Transparent chatbox", tda, 1, 0xff9933, false, true);
        addText(28212, "Make chatbox transparent", tda, 0, 0xff9933, false, true);
        addText(28213, "Side-stones arrangement", tda, 1, 0xff9933, false, true);
        addText(28214, "Change the side-stones arrangement", tda, 0, 0xff9933, false, true);
        addHoverButton(28216, 374, 40, 40, "Transparent side panel", -1, 28217, 1);
        addHoveredButton(28217, 374, 40, 40, 28218);
        addHoverButton(28219, 373, 40, 40, "Transparent chatbox", -1, 28220, 1);
        addHoveredButton(28220, 373, 40, 40, 28221);
        addHoverButton(28222, 372, 40, 40, "Side-stones arrangement", -1, 28223, 1);
        addHoveredButton(28223, 372, 40, 40, 28224);
        tab.totalChildren(16);
        tab.child(0, 28201, 110, 40);
        tab.child(1, 28202, 260, 51);
        tab.child(2, 28203, 379, 47);
        tab.child(3, 28204, 379, 47);
        tab.child(4, 28209, 170, 87);
        tab.child(5, 28210, 170, 102);
        tab.child(6, 28211, 170, 149);
        tab.child(7, 28212, 170, 164);
        tab.child(8, 28213, 170, 206);
        tab.child(9, 28214, 170, 221);
        tab.child(10, 28216, 120, 80);
        tab.child(11, 28217, 120, 80);
        tab.child(12, 28219, 120, 140);
        tab.child(13, 28220, 120, 140);
        tab.child(14, 28222, 120, 200);
        tab.child(15, 28223, 120, 200);
    }

    public static void quickCurses(TextDrawingArea[] TDA) {
        RSInterface tab = addTabInterface(17234);
        addSprite(17201, 545);
        for (int i = 17202, j = 630; i <= 17230 && j <= 659; i++, j++) {
            addConfigButton(i, 17200, 546, 547, 14, 15, "Select", 0, 1, j);
        }

        addTransparentSprite(17232, 544, 50);
        addText(17233, "Select your quick prayers:", TDA, 0, 0xFF981F, false, true);
        addHoverButton(17235, 548, 190, 24, "Confirm Selection", -1, 17236, 1);
        addHoveredButton(17236, 549, 190, 24, 17237);
        int frame = 0;
        setChildren(46, tab);
        setBounds(21358, 11, 8 + 20, frame++, tab);
        setBounds(21360, 50, 11 + 20, frame++, tab);
        setBounds(21362, 87, 11 + 20, frame++, tab);
        setBounds(21364, 122, 10 + 20, frame++, tab);
        setBounds(21366, 159, 11 + 20, frame++, tab);
        setBounds(21368, 12, 45 + 20, frame++, tab);
        setBounds(21370, 46, 45 + 20, frame++, tab);
        setBounds(21372, 83, 46 + 20, frame++, tab);
        setBounds(21374, 119, 45 + 20, frame++, tab);
        setBounds(21376, 157, 45 + 20, frame++, tab);
        setBounds(21378, 11, 83 + 20, frame++, tab);
        setBounds(21380, 49, 84 + 20, frame++, tab);
        setBounds(21382, 84, 83 + 20, frame++, tab);
        setBounds(21384, 123, 84 + 20, frame++, tab);
        setBounds(21386, 159, 83 + 20, frame++, tab);
        setBounds(21388, 12, 119 + 20, frame++, tab);
        setBounds(21390, 49, 119 + 20, frame++, tab);
        setBounds(21392, 88, 119 + 20, frame++, tab);
        setBounds(21394, 122, 121 + 20, frame++, tab);
        setBounds(21396, 155, 122 + 20, frame++, tab);
        setBounds(17229, 0, 25, frame++, tab);
        setBounds(17201, 0, 22, frame++, tab);
        setBounds(17201, 0, 237, frame++, tab);
        setBounds(17202, 13 - 3, 8 + 17, frame++, tab);
        setBounds(17203, 52 - 3, 8 + 17, frame++, tab);
        setBounds(17204, 90 - 3, 8 + 17, frame++, tab);
        setBounds(17205, 126 - 3, 8 + 17, frame++, tab);
        setBounds(17206, 162 - 3, 8 + 17, frame++, tab);
        setBounds(17207, 13 - 3, 45 + 17, frame++, tab);
        setBounds(17208, 52 - 3, 45 + 17, frame++, tab);
        setBounds(17209, 90 - 3, 45 + 17, frame++, tab);
        setBounds(17210, 126 - 3, 45 + 17, frame++, tab);
        setBounds(17211, 162 - 3, 45 + 17, frame++, tab);
        setBounds(17212, 13 - 3, 80 + 17, frame++, tab);
        setBounds(17213, 52 - 3, 80 + 17, frame++, tab);
        setBounds(17214, 90 - 3, 80 + 17, frame++, tab);
        setBounds(17215, 126 - 3, 80 + 17, frame++, tab);
        setBounds(17216, 162 - 3, 80 + 17, frame++, tab);
        setBounds(17217, 13 - 3, 119 + 17, frame++, tab);
        setBounds(17218, 52 - 3, 119 + 17, frame++, tab);
        setBounds(17219, 90 - 3, 119 + 17, frame++, tab);
        setBounds(17220, 126 - 3, 119 + 17, frame++, tab);
        setBounds(17221, 162 - 3, 119 + 17, frame++, tab);
        setBounds(17232, 0, 237, frame++, tab);
        setBounds(17233, 0, 237, frame++, tab);
    }

    public static void quickPrayers(TextDrawingArea[] vencillio) {
        int frame = 0;
        RSInterface tab = addTabInterface(17200);
        setChildren(63, tab);
        setBounds(5632, 5, 8 + 20, frame++, tab);
        setBounds(5633, 44, 8 + 20, frame++, tab);
        setBounds(5634, 79, 11 + 20, frame++, tab);
        setBounds(19813, 116, 10 + 20, frame++, tab);
        setBounds(19815, 153, 9 + 20, frame++, tab);
        setBounds(5635, 5, 48 + 20 - 4, frame++, tab);
        setBounds(5636, 44, 47 + 20 - 4, frame++, tab);
        setBounds(5637, 79, 49 + 20 - 4, frame++, tab);
        setBounds(5638, 116, 50 + 20 - 4, frame++, tab);
        setBounds(5639, 154, 50 + 20 - 4, frame++, tab);
        setBounds(5640, 4, 84 + 20 - 4, frame++, tab);
        setBounds(19817, 44, 87 + 20 - 4, frame++, tab);
        setBounds(19820, 81, 85 + 20 - 4, frame++, tab);
        setBounds(5641, 117, 85 + 20 - 4, frame++, tab);
        setBounds(5642, 156, 87 + 20 - 4, frame++, tab);
        setBounds(5643, 5, 125 + 20 - 4, frame++, tab);
        setBounds(5644, 43, 124 + 20 - 4, frame++, tab);
        setBounds(13984, 83, 124 + 20 - 4, frame++, tab);
        setBounds(5645, 115, 121 + 20 - 4, frame++, tab);
        setBounds(19822, 154, 124 + 20 - 4, frame++, tab);
        setBounds(19824, 5, 160 + 20 - 4, frame++, tab);
        setBounds(5649, 41, 158 + 20 - 4, frame++, tab);
        setBounds(5647, 79, 163 + 20 - 4, frame++, tab);
        setBounds(5648, 116, 158 + 20 - 4, frame++, tab);

        setBounds(28002, 154, 157 + 20 - 4, frame++, tab);
        setBounds(28005, 80, 190 + 20, frame++, tab);
        setBounds(28008, 118, 190 + 20, frame++, tab);

        //        setBounds(19826, 161, 160 + 20, frame++, tab);
        setBounds(19826, 10, 186 + 20, frame++, tab);

        //        setBounds(19828, 4, 207 + 12, frame++, tab);
        setBounds(19828, 41, 207 + 12, frame++, tab);

        setBounds(17201, 0, 22, frame++, tab);
        setBounds(17201, 0, 237, frame++, tab);
        setBounds(17202, 5 - 3, 8 + 17, frame++, tab);
        setBounds(17203, 44 - 3, 8 + 17, frame++, tab);
        setBounds(17204, 79 - 3, 8 + 17, frame++, tab);
        setBounds(17205, 116 - 3, 8 + 17, frame++, tab);
        setBounds(17206, 153 - 3, 8 + 17, frame++, tab);
        setBounds(17207, 5 - 3, 48 + 17 - 4, frame++, tab);
        setBounds(17208, 44 - 3, 48 + 17 - 4, frame++, tab);
        setBounds(17209, 79 - 3, 48 + 17 - 4, frame++, tab);
        setBounds(17210, 116 - 3, 48 + 17 - 4, frame++, tab);
        setBounds(17211, 153 - 3, 48 + 17 - 4, frame++, tab);
        setBounds(17212, 5 - 3, 85 + 17 - 4, frame++, tab);
        setBounds(17213, 44 - 3, 85 + 17 - 4, frame++, tab);
        setBounds(17214, 79 - 3, 85 + 17 - 4, frame++, tab);
        setBounds(17215, 116 - 3, 85 + 17 - 4, frame++, tab);
        setBounds(17216, 153 - 3, 85 + 17 - 4, frame++, tab);
        setBounds(17217, 5 - 3, 124 + 17 - 4, frame++, tab);
        setBounds(17218, 44 - 3, 124 + 17 - 4, frame++, tab);
        setBounds(17219, 79 - 3, 124 + 17 - 4, frame++, tab);
        setBounds(17220, 116 - 3, 124 + 17 - 4, frame++, tab);
        setBounds(17221, 153 - 3, 124 + 17 - 4, frame++, tab);
        setBounds(17222, 5 - 3, 160 + 17 - 4, frame++, tab);
        setBounds(17223, 44 - 3, 160 + 17 - 4, frame++, tab);
        setBounds(17224, 79 - 3, 160 + 17 - 4, frame++, tab);
        setBounds(17225, 116 - 3, 160 + 17 - 4, frame++, tab);
        setBounds(17226, 153 - 3, 160 + 17 - 4, frame++, tab);

        setBounds(17227, 5 - 3, 207 + 4, frame++, tab);
        setBounds(17228, 44 - 3, 207 + 4, frame++, tab);
        setBounds(17229, 79 - 3, 207 + 4, frame++, tab);
        setBounds(17230, 116 - 3, 207 + 4, frame++, tab);

        setBounds(17233, 5, 5, frame++, tab);
        setBounds(17232, 0, 25, frame++, tab);
        setBounds(17235, 0, 237, frame++, tab);
    }

    private static void itemsKeptOnDeath(TextDrawingArea[] tda) {
        RSInterface rsinterface = addInterface(17100);
        addSprite(17101, 367);
        addText(17102, "Items Kept On Death", tda, 2, 0xff9933, true, true);
        addHoverButton(17103, 24, 21, 21, "Close", 250, 17104, 3);
        addHoveredButton(17104, 25, 21, 21, 17105);
        addText(17106, "Items you will keep on death if not skulled:", tda, 1, 0xff9933, false, true);
        addText(17107, "Items you will @red@NOT</col> keep on death if not skulled:", tda, 1, 0xff9933, false, true);
        addText(17108, "Information:", tda, 1, 0xff9933, false, true);
        addText(17109, "The normal amount of items \\nkept is three.", tda, 0, 0xff9933, false, true);
        addText(17110, "You have no factors affecting \\nthe items you keep.", tda, 0, 0xff9933, false, true);
        addText(17111, "Max items kept on death:", tda, 1, 0xFFCC33, false, true);
        addText(17112, "~ 3 ~", tda, 1, 0xFFCC33, false, true);
        addContainer(17113, 10, 7, 8, 0, false, null, null, null, null, null);
        addContainer(17114, 8, 8, 8, 2, false, null, null, null, null, null);
        addText(17115, "Crarried: @red@0", tda, 0, 0xff9933, false, true);
        addText(17116, "Risked: @red@0", tda, 0, 0xff9933, false, true);
        rsinterface.totalChildren(15);
        rsinterface.child(0, 17101, 7, 8);
        rsinterface.child(1, 17102, 250, 19 - 2);
        rsinterface.child(2, 17103, 479, 17);
        rsinterface.child(3, 17104, 479, 17);
        rsinterface.child(4, 17106, 20, 50 - 2);
        rsinterface.child(5, 17107, 20, 110 - 2);
        rsinterface.child(6, 17108, 348, 50 - 2);
        rsinterface.child(7, 17109, 348, 70);
        rsinterface.child(8, 17110, 348, 100);
        rsinterface.child(9, 17111, 349, 270);
        rsinterface.child(10, 17112, 403, 300);
        rsinterface.child(11, 17113, 21, 73);
        rsinterface.child(12, 17114, 21, 140);
        rsinterface.child(13, 17115, 348, 190);
        rsinterface.child(14, 17116, 348, 230);
    }

    private static void secondTrade(TextDrawingArea[] tda) {
        RSInterface rsinterface = addInterface(33200);
        addSprite(33201, 326);
        addText(33202, "Are you sure you want to make this trade?", tda, 0, 0x28EBD7, true, true);
        addText(33203, "There is <col=FF0000>NO WAY </col>to reverse a trade if you change your mind.", tda, 0, 0xFFFFFF, true, true);
        addHoverButton(33204, 24, 15, 15, "Close", 250, 33205, 3);
        addHoveredButton(33205, 25, 15, 15, 33206);
        addText(33207, "Trading With:", tda, 2, 0x28EBD7, true, true);
        addText(33208, "Mod Daniel", tda, 2, 0x28EBD7, true, true);
        addHoverButton(33209, 325, 73, 30, "Accept trade", -1, 33210, 1);
        addHoveredButton(33210, 324, 73, 30, 33211);
        addHoverButton(33212, 325, 73, 30, "Decline trade", -1, 33213, 1);
        addHoveredButton(33213, 324, 73, 30, 33214);
        addText(33215, "Accept", tda, 1, 0x63F731, true, true);
        addText(33216, "Decline", tda, 1, 0xFF0000, true, true);
        rsinterface.totalChildren(15);
        rsinterface.child(0, 33201, 13, 12);
        rsinterface.child(1, 33202, 255, 24);
        rsinterface.child(2, 33203, 255, 40);
        rsinterface.child(3, 33204, 470, 22);
        rsinterface.child(4, 33205, 470, 22);
        rsinterface.child(5, 33207, 100, 280);
        rsinterface.child(6, 33208, 100, 295);
        rsinterface.child(7, 33209, 180, 280);
        rsinterface.child(8, 33210, 180, 280);
        rsinterface.child(9, 33212, 257, 280);
        rsinterface.child(10, 33213, 257, 280);
        rsinterface.child(11, 33215, 215, 287);
        rsinterface.child(12, 33216, 293, 287);
        rsinterface.child(13, 33220, 34, 81);
        rsinterface.child(14, 33250, 273, 81);
        RSInterface scrollInterface1 = addTabInterface(33220);
        scrollInterface1.scrollPosition = 0;
        scrollInterface1.contentType = 0;
        scrollInterface1.width = 202;
        scrollInterface1.height = 196;
        scrollInterface1.scrollMax = 510;
        int y = 8;
        scrollInterface1.totalChildren(28);
        for (int i = 0; i < 28; i++) {
            addHoverText(33221 + i, "", "", tda, 0, 0xff9933, true, true, 168);
            scrollInterface1.child(i, 33221 + i, 15, y);
            y += 18;
        }
        RSInterface scrollInterface2 = addTabInterface(33250);
        scrollInterface2.scrollPosition = 0;
        scrollInterface2.contentType = 0;
        scrollInterface2.width = 202;
        scrollInterface2.height = 196;
        scrollInterface2.scrollMax = 510;
        int y2 = 8;
        scrollInterface2.totalChildren(28);
        for (int i = 0; i < 28; i++) {
            addHoverText(33251 + i, "", "", tda, 0, 0xff9933, true, true, 168);
            scrollInterface2.child(i, 33251 + i, 15, y2);
            y2 += 18;
        }
    }

    public static void firstTrade(TextDrawingArea[] tda) {
        RSInterface rsinterface = addInterface(33000);
        addSprite(33001, 323);
        addText(33002, "Trading with: Mod Daniel", tda, 2, 0xff9933, true, true);
        addText(33003, "Mod Daniel", tda, 0, 0xff9933, true, true);
        addText(33004, "has 12 free", tda, 0, 0xff9933, true, true);
        addText(33005, "inventory spaces", tda, 0, 0xff9933, true, true);
        addHoverButton(33006, 24, 15, 15, "Close", 250, 33007, 3);
        addHoveredButton(33007, 25, 15, 15, 33008);
        addHoverButton(33009, 325, 73, 30, "Accept trade", -1, 33010, 1);
        addHoveredButton(33010, 324, 73, 30, 33011);
        addHoverButton(33012, 325, 73, 30, "Decline trade", -1, 33013, 1);
        addHoveredButton(33013, 324, 73, 30, 33014);
        addText(33015, "Accept", tda, 1, 0x2DFA45, true, true);
        addText(33016, "Decline", tda, 1, 0xFF0000, true, true);
        addText(33017, "Wealth transfer:", tda, 1, 0xff9933, true, true);
        addText(33018, "", tda, 0, 0xff9933, true, true);
        addText(33019, "Nothing", tda, 0, 0xff9933, true, true);
        addText(33020, "Nothing", tda, 0, 0xff9933, true, true);
        addContainer(33021, 4, 7, 8, 0, false, "Remove 1", "Remove 5", "Remove 10", "Remove All", "Remove X");
        addContainer(33022, 4, 7, 8, 0, false, null, null, null, null, null);
        addHoverButton(33023, 221, 35, 25, "Deposit All", -1, 33024, 1);
        addHoveredButton(33024, 222, 35, 25, 33025);
        addHoverButton(33026, 327, 35, 25, "Withdraw All", -1, 33027, 1);
        addHoveredButton(33027, 328, 35, 25, 33028);
        addText(33029, "Other player has accepted.", tda, 0, 0xFFFFFF, true, true);
        addText(33030, "Trade has been modified!", tda, 0, 0xFF0000, true, true);
        addText(33031, "Your offer", tda, 0, 0xff9933, true, true);
        addText(33032, "Opponent's offer", tda, 0, 0xff9933, true, true);
        rsinterface.totalChildren(27);
        rsinterface.child(0, 33001, 13, 12);
        rsinterface.child(1, 33002, 255, 24);
        rsinterface.child(2, 33003, 255, 76);
        rsinterface.child(3, 33004, 255, 88);
        rsinterface.child(4, 33005, 255, 100);
        rsinterface.child(5, 33006, 470, 22);
        rsinterface.child(6, 33007, 470, 22);
        rsinterface.child(7, 33009, 220, 132);
        rsinterface.child(8, 33010, 220, 132);
        rsinterface.child(9, 33012, 220, 174);
        rsinterface.child(10, 33013, 220, 174);
        rsinterface.child(11, 33015, 255, 140);
        rsinterface.child(12, 33016, 255, 182);
        rsinterface.child(13, 33017, 255, 287);
        rsinterface.child(14, 33018, 255, 302);
        rsinterface.child(15, 33019, 75, 300);
        rsinterface.child(16, 33020, 430, 300);
        rsinterface.child(17, 33021, 38, 60);
        rsinterface.child(18, 33022, 317, 60);
        rsinterface.child(19, 33023, 237, 216);
        rsinterface.child(20, 33024, 237, 216);
        rsinterface.child(21, 33026, 237, 251);
        rsinterface.child(22, 33027, 237, 251);
        rsinterface.child(23, 33029, 255, 50);
        rsinterface.child(24, 33030, 85, 24);
        rsinterface.child(25, 33031, 120, 48);
        rsinterface.child(26, 33032, 400, 48);
    }

    /**
     * Interface for checking prices of items.
     */
    public static void priceChecker(TextDrawingArea[] wid) {

        RSInterface rsi = addInterface(48500);
        addSprite(48501, 299);
        addContainer(48542, 7, 4, 40, 28, false, "Take 1", "Take 5", "Take 10", "Take All", "Take X");
        addHoverButton(48502, 252, 16, 21, "Close", 250, 48503, 3);
        addHoveredButton(48503, 253, 21, 21, 48504);
        addHoverButton(48505, 300, 36, 36, "Add all", -1, 48506, 1);
        addHoveredButton(48506, 301, 36, 36, 48507);
        addHoverButton(48578, 302, 36, 36, "Withdraw all", -1, 48579, 1);
        addHoveredButton(48579, 303, 36, 36, 48580);
        addHoverButton(48508, 304, 36, 36, "Search for item", -1, 48509, 1);
        addHoveredButton(48509, 305, 36, 36, 48510);
        addText(48511, "Price Checker", wid, 2, 0xFF981F, true, true);
        addText(48512, "Total Price:", wid, 1, 0xFF981F, true, true);
        addText(48513, "115,424,152", wid, 0, 0xffffff, true, true);
        addText(48550, "", wid, 0, 0xffffff, true, true);
        addText(48551, "", wid, 0, 0xffffff, true, true);
        addText(48552, "", wid, 0, 0xffffff, true, true);
        addText(48553, "", wid, 0, 0xffffff, true, true);
        addText(48554, "", wid, 0, 0xffffff, true, true);
        addText(48555, "", wid, 0, 0xffffff, true, true);
        addText(48556, "", wid, 0, 0xffffff, true, true);
        addText(48557, "", wid, 0, 0xffffff, true, true);
        addText(48558, "", wid, 0, 0xffffff, true, true);
        addText(48559, "", wid, 0, 0xffffff, true, true);
        addText(48560, "", wid, 0, 0xffffff, true, true);
        addText(48561, "", wid, 0, 0xffffff, true, true);
        addText(48562, "", wid, 0, 0xffffff, true, true);
        addText(48563, "", wid, 0, 0xffffff, true, true);
        addText(48564, "", wid, 0, 0xffffff, true, true);
        addText(48565, "", wid, 0, 0xffffff, true, true);
        addText(48566, "", wid, 0, 0xffffff, true, true);
        addText(48567, "", wid, 0, 0xffffff, true, true);
        addText(48568, "", wid, 0, 0xffffff, true, true);
        addText(48569, "", wid, 0, 0xffffff, true, true);
        addText(48570, "", wid, 0, 0xffffff, true, true);
        addText(48571, "", wid, 0, 0xffffff, true, true);
        addText(48572, "", wid, 0, 0xffffff, true, true);
        addText(48573, "", wid, 0, 0xffffff, true, true);
        addText(48574, "", wid, 0, 0xffffff, true, true);
        addText(48575, "", wid, 0, 0xffffff, true, true);
        addText(48576, "", wid, 0, 0xffffff, true, true);
        addText(48577, "", wid, 0, 0xffffff, true, true);
        addContainer(48581, 7, 4, 40, 28, false, null, null, null, null, null);
        addText(48582, "", wid, 0, 0xffffff, false, true);
        addText(48583, "", wid, 0, 0xffffff, false, true);
        addConfigButton(48584, 48500, 235, 234, 15, 15, "Price by item value", 0, 5, 237);
        addConfigButton(48585, 48500, 235, 234, 15, 15, "Price by item high alch value", 1, 5, 237);
        addText(48586, "Price by Value", wid, 0, 0xFF981F, false, true);
        addText(48587, "Price by High Alch", wid, 0, 0xFF981F, false, true);
        rsi.totalChildren(48);
        rsi.child(0, 48501, 0, 0);
        rsi.child(1, 48502, 485, 7);
        rsi.child(2, 48503, 485, 7);
        rsi.child(3, 48505, 434, 290);
        rsi.child(4, 48506, 434, 290);
        rsi.child(5, 48508, 8, 290);
        rsi.child(6, 48509, 8, 290);
        rsi.child(7, 48511, 255, 10 - 1);
        rsi.child(8, 48512, 255, 290);
        rsi.child(9, 48513, 255, 310);
        rsi.child(10, 48542, 24, 37);
        rsi.child(11, 48550, 39, 70);
        rsi.child(12, 48551, 105, 70);
        rsi.child(13, 48552, 182, 70);
        rsi.child(14, 48553, 254, 70);
        rsi.child(15, 48554, 326, 70);
        rsi.child(16, 48555, 400, 70);
        rsi.child(17, 48556, 468, 70);
        rsi.child(18, 48557, 39, 133);
        rsi.child(19, 48558, 110, 133);
        rsi.child(20, 48559, 182, 133);
        rsi.child(21, 48560, 254, 133);
        rsi.child(22, 48561, 326, 133);
        rsi.child(23, 48562, 400, 133);
        rsi.child(24, 48563, 468, 133);
        rsi.child(25, 48564, 39, 194);
        rsi.child(26, 48565, 110, 194);
        rsi.child(27, 48566, 182, 194);
        rsi.child(28, 48567, 254, 194);
        rsi.child(29, 48568, 326, 194);
        rsi.child(30, 48569, 400, 194);
        rsi.child(31, 48570, 468, 194);
        rsi.child(32, 48571, 39, 256);
        rsi.child(33, 48572, 110, 256);
        rsi.child(34, 48573, 182, 256);
        rsi.child(35, 48574, 254, 256);
        rsi.child(36, 48575, 326, 256);
        rsi.child(37, 48576, 400, 256);
        rsi.child(38, 48577, 468, 256);
        rsi.child(39, 48578, 470, 290);
        rsi.child(40, 48579, 470, 290);
        rsi.child(41, 48581, 12, 291);
        rsi.child(42, 48582, 51, 296);
        rsi.child(43, 48583, 51, 312);
        rsi.child(44, 48584, 320, 291);
        rsi.child(45, 48585, 320, 311);
        rsi.child(46, 48586, 338, 293 - 1);
        rsi.child(47, 48587, 338, 313 - 1);
    }

    public static void boltEnchanting(TextDrawingArea[] daniel) {
        RSInterface tab = addInterface(42750);
        addSprite(42751, 713);
        addContainer(42752, 5, 5, 66, 100, false, "Make", null, null, null, null);
        addHoverButton(42753, 252, 21, 21, "Close", 250, 42754, 3);
        addHoveredButton(42754, 253, 21, 21, 42755);
        addText(42756, "Magic 4", daniel, 0, 0xff9933, true, true);
        addText(42757, "Magic 7", daniel, 0, 0xff9933, true, true);
        addText(42758, "Magic 14", daniel, 0, 0xff9933, true, true);
        addText(42759, "Magic 24", daniel, 0, 0xff9933, true, true);
        addText(42760, "Magic 27", daniel, 0, 0xff9933, true, true);
        addText(42761, "Magic 29", daniel, 0, 0xff9933, true, true);
        addText(42762, "Magic 49", daniel, 0, 0xff9933, true, true);
        addText(42763, "Magic 57", daniel, 0, 0xff9933, true, true);
        addText(42764, "Magic 68", daniel, 0, 0xff9933, true, true);
        addText(42765, "Magic 87", daniel, 0, 0xff9933, true, true);
        addText(42766, "0/0", daniel, 0, 0xFA0A0A, true, true);
        addText(42767, "0/0", daniel, 0, 0xFA0A0A, true, true);
        addText(42768, "0/0", daniel, 0, 0xFA0A0A, true, true);
        addText(42769, "0/0", daniel, 0, 0xFA0A0A, true, true);
        addText(42770, "0/0", daniel, 0, 0xFA0A0A, true, true);
        addText(42771, "0/0", daniel, 0, 0xFA0A0A, true, true);
        addText(42772, "0/0", daniel, 0, 0xFA0A0A, true, true);
        addText(42773, "0/0", daniel, 0, 0xFA0A0A, true, true);
        addText(42774, "0/0", daniel, 0, 0xFA0A0A, true, true);
        addText(42775, "0/0", daniel, 0, 0xFA0A0A, true, true);
        addText(42776, "0/0", daniel, 0, 0xFA0A0A, true, true);
        addText(42777, "0/0", daniel, 0, 0xFA0A0A, true, true);
        addText(42778, "0/0", daniel, 0, 0xFA0A0A, true, true);
        addText(42779, "0/0", daniel, 0, 0xFA0A0A, true, true);
        addText(42780, "0/0", daniel, 0, 0xFA0A0A, true, true);
        addText(42781, "0/0", daniel, 0, 0xFA0A0A, true, true);
        addText(42782, "0/0", daniel, 0, 0xFA0A0A, true, true);
        addText(42783, "0/0", daniel, 0, 0xFA0A0A, true, true);
        addText(42784, "0/0", daniel, 0, 0xFA0A0A, true, true);
        addText(42785, "0/0", daniel, 0, 0xFA0A0A, true, true);
        addText(42786, "0/0", daniel, 0, 0xFA0A0A, true, true);
        addText(42787, "0/0", daniel, 0, 0xFA0A0A, true, true);
        addText(42788, "0/0", daniel, 0, 0xFA0A0A, true, true);
        addText(42789, "0/0", daniel, 0, 0xFA0A0A, true, true);
        addText(42790, "0/0", daniel, 0, 0xFA0A0A, true, true);
        addText(42791, "0/0", daniel, 0, 0xFA0A0A, true, true);

        tab.totalChildren(40);
        tab.child(0, 42751, 11, 15);
        tab.child(1, 42752, 40, 100);
        tab.child(2, 42753, 471, 22);
        tab.child(3, 42754, 471, 22);
        tab.child(4, 42756, 56, 70);
        tab.child(5, 42757, 156, 70);
        tab.child(6, 42758, 256, 70);
        tab.child(7, 42759, 352, 70);
        tab.child(8, 42760, 443, 70);
        tab.child(9, 42761, 65, 200);
        tab.child(10, 42762, 152, 200);
        tab.child(11, 42763, 245, 200);
        tab.child(12, 42764, 342, 200);
        tab.child(13, 42765, 438, 200);
        tab.child(14, 42766, 41, 162);
        tab.child(15, 42767, 74, 162);
        tab.child(16, 42768, 124, 162);
        tab.child(17, 42769, 155, 162);
        tab.child(18, 42770, 185, 162);
        tab.child(19, 42771, 233, 162);
        tab.child(20, 42772, 265, 162);
        tab.child(21, 42773, 331, 162);
        tab.child(22, 42774, 364, 162);
        tab.child(23, 42775, 415, 162);
        tab.child(24, 42776, 447, 162);
        tab.child(25, 42777, 476, 162);
        tab.child(26, 42778, 40, 290);
        tab.child(27, 42779, 72, 290);
        tab.child(28, 42780, 126, 290);
        tab.child(29, 42781, 156, 290);
        tab.child(30, 42782, 185, 290);
        tab.child(31, 42783, 221, 290);
        tab.child(32, 42784, 250, 290);
        tab.child(33, 42785, 280, 290);
        tab.child(34, 42786, 314, 290);
        tab.child(35, 42787, 344, 290);
        tab.child(36, 42788, 372, 290);
        tab.child(37, 42789, 413, 290);
        tab.child(38, 42790, 445, 290);
        tab.child(39, 42791, 475, 290);
    }

    /**
     * Starter interface for new players.
     */
    private static void starter(TextDrawingArea[] daniel) {
        RSInterface Interface = addInterface(45000);
        addSprite(45001, 275);
        addText(45002, "Tarnish Account Setup", 0xff9933, true, true, 52, daniel, 2);
        addText(45003, "Mode Description:", 0xff9933, true, true, 52, daniel, 2);
        addText(45004, "Description line here (45004)", 0xff9933, true, true, 52, daniel, 0);
        addText(45005, "Description line here (45005)", 0xff9933, true, true, 52, daniel, 0);
        addText(45006, "Description line here (45006)", 0xff9933, true, true, 52, daniel, 0);
        addText(45007, "Description line here (45007)", 0xff9933, true, true, 52, daniel, 0);
        addConfigButton(45008, 45000, 234, 235, 15, 15, "Select normal account settings", 0, 5, 1085, false);
        addConfigButton(45009, 45000, 234, 235, 15, 15, "Select ironman account settings", 1, 5, 1085, false);
        addConfigButton(45010, 45000, 234, 235, 15, 15, "Select ultimate ironman account settings", 2, 5, 1085, false);
        addConfigButton(45011, 45000, 234, 235, 15, 15, "Select hardcore ironman account settings", 3, 5, 1085, false);
        addHoverText(45012, "Normal", "Select normal account settings", daniel, 0, 0xF7AA25, false, true, 65);
        addHoverText(45013, "<img=11> Ironman", "Select ironman account settings", daniel, 0, 0xF7AA25, false, true, 65);
        addHoverText(45014, "<img=12> Ultimate Ironman", "Select ultimate ironman account settings", daniel, 0, 0xF7AA25, false, true, 65);
        addHoverText(45015, "<img=13> Hardcore Ironman", "Select hardcore ironman account settings", daniel, 0, 0xF7AA25, false, true, 65);
        addHoverButton(45016, -1, 127, 26, "Confirm selection", -1, 45017, 1);
        addHoveredButton(45017, 276, 127, 26, 45018);
        addText(45019, "Confirm", 0xff9933, true, true, 52, daniel, 3);
        addChar(45020, 825);
        addContainer(45021, 6, 18, 5, 6, false, null, null, null, null, null);

        Interface.totalChildren(20);
        Interface.child(0, 45001, 11, 13);
        Interface.child(1, 45002, 255, 22);
        Interface.child(2, 45003, 255, 213);
        Interface.child(3, 45004, 255, 240);
        Interface.child(4, 45005, 255, 255);
        Interface.child(5, 45006, 255, 270);
        Interface.child(6, 45007, 255, 285);
        Interface.child(7, 45008, 31, 64);
        Interface.child(8, 45009, 31, 93);
        Interface.child(9, 45010, 31, 122);
        Interface.child(10, 45011, 31, 151);
        Interface.child(11, 45012, 51, 66);
        Interface.child(12, 45013, 51, 95);
        Interface.child(13, 45014, 51, 124);
        Interface.child(14, 45015, 51, 153);
        Interface.child(15, 45016, 27, 174);
        Interface.child(16, 45017, 27, 174);
        Interface.child(17, 45019, 93, 178);
        Interface.child(18, 45020, 145, 105);
        Interface.child(19, 45021, 265, 55);
    }

    private static void serverInterface(TextDrawingArea[] TDA) {
        RSInterface rsinterface = addInterface(51300);
        addText(51301, "Clan Banned Members", 0x284777, true, false, 52, TDA, 3);
        addText(51302, "Click on the name of the player to remove from the banned list.", 0x3f6baf, true, false, 52, TDA, 0);
        rsinterface.totalChildren(6);
        rsinterface.child(0, 51001, 18, 35);
        rsinterface.child(1, 51301, 260, 37);
        rsinterface.child(2, 51302, 260, 281);
        rsinterface.child(3, 51004, 452, 35);
        rsinterface.child(4, 51005, 452, 35);
        rsinterface.child(5, 51310, 0, 60);
        RSInterface scrollInterface = addTabInterface(51310);
        scrollInterface.scrollPosition = 0;
        scrollInterface.contentType = 0;
        scrollInterface.width = 456;
        scrollInterface.height = 215;
        scrollInterface.scrollMax = 960;
        int y = 10;
        int amountOfLines = 50;
        scrollInterface.totalChildren(amountOfLines);
        for (int i = 0; i < amountOfLines; i++) {
            addHoverText(51311 + i, "", "Click", TDA, 0, 0xce4040, true, false, 150);
            scrollInterface.child(i, 51311 + i, 185, y);
            y += 19;
        }
    }

    private static void serverSetting(TextDrawingArea[] TDA) {
        RSInterface rsinterface = addInterface(51000);
        addSprite(51001, 271);
        addText(51002, "Server Settings", 0x483407, true, false, 52, TDA, 3);
        addText(51003, "To toggle the client settings click on the settings tab.", 0x483407, true, false, 52, TDA, 0);
        addHoverButton(51004, 272, 26, 23, "Close", 250, 51005, 3);
        addHoveredButton(51005, 273, 26, 23, 51006);

        rsinterface.totalChildren(6);
        rsinterface.child(0, 51001, 18, 35);
        rsinterface.child(1, 51002, 260, 37);
        rsinterface.child(2, 51003, 260, 281);
        rsinterface.child(3, 51004, 452, 35);
        rsinterface.child(4, 51005, 452, 35);
        rsinterface.child(5, 51010, 0, 60);
        RSInterface scrollInterface = addTabInterface(51010);
        scrollInterface.scrollPosition = 0;
        scrollInterface.contentType = 0;
        scrollInterface.width = 456;
        scrollInterface.height = 215;
        scrollInterface.scrollMax = 960;
        int y = 10;
        int amountOfLines = 50;
        scrollInterface.totalChildren(amountOfLines);
        for (int i = 0; i < amountOfLines; i++) {
            addHoverText(51011 + i, "", "Toggle setting", TDA, 0, 0x624D23, true, false, 150);
            scrollInterface.child(i, 51011 + i, 185, y);
            y += 19;
        }
    }

    /**
     * Displays a list of text
     */
    public static void questInterface(TextDrawingArea[] TDA) {

        RSInterface rsinterface = addInterface(37100);
        addSprite(37101, 271);
        addSprite(37102, 270);
        addText(37103, "Quest Name", 0x000080, true, false, 52, TDA, 3);
        addHoverButton(37104, 272, 26, 23, "Close", 250, 37105, 3);
        addHoveredButton(37105, 273, 26, 23, 37106);
        addText(37107, "Written:\\n2016/04/58", 0xA11A1A, true, false, 52, TDA, 0);
        rsinterface.totalChildren(7);
        rsinterface.child(0, 37102, 18, 4);
        rsinterface.child(1, 37101, 18, 62);
        rsinterface.child(2, 37103, 260, 14);
        rsinterface.child(3, 37110, 40, 86);
        rsinterface.child(4, 37104, 452, 63);
        rsinterface.child(5, 37105, 452, 63);
        rsinterface.child(6, 37107, 410, 13);
        RSInterface scrollInterface = addTabInterface(37110);
        scrollInterface.scrollPosition = 0;
        scrollInterface.contentType = 0;
        scrollInterface.width = 417;
        scrollInterface.height = 217;
        scrollInterface.scrollMax = 960;
        int y = 10;
        int amountOfLines = 50;
        scrollInterface.totalChildren(amountOfLines + 1);

        addContainer(37199, 1, 95, 22, 7, false, "Value");
        scrollInterface.child(0, 37199, 83, -15);

        for (int i = 0; i < amountOfLines; i++) {
            addText(37111 + i, "", 0xA11A1A, true, false, 52, TDA, 1);
            scrollInterface.child(1 + i, 37111 + i, 205, y);
            y += 19;
        }
    }

    /** Player title interface players to choose a custom title. */
    private static void playerTitles(TextDrawingArea[] tda) {
        RSInterface rsi = addInterface(39000);
        addSprite(39001, 266);
        addText(39002, "Player Title Manager", tda, 2, 0xFF981F, true, true);
        addText(39003, "<u=255>Drop-out", tda, 2, 0xFF981F, true, true);
        addText(39004, "How to unlock:", tda, 1, 0xFF981F, true, true);
        addText(39005, "This title can only be unlocked by", tda, 0, 0xBF7D0A, false, true);
        addText(39006, "completing the 'Time for lessons'", tda, 0, 0xBF7D0A, false, true);
        addText(39007, "achievement.", tda, 0, 0xBF7D0A, false, true);
        addHoverButton(39008, 250, 37, 25, "Reset", -1, 39009, 1);
        addHoveredButton(39009, 251, 37, 25, 39010);
        addHoverButton(39011, 267, 90, 25, "Purchase", -1, 39012, 1);
        addHoveredButton(39012, 268, 90, 25, 39013);
        addText(39014, "Reset", tda, 0, 0xFF981F, false, true);
        addText(39015, "Redeem", tda, 0, 0xFF981F, false, true);
        addHoverButton(39016, 24, 15, 15, "Close", 250, 39017, 3);
        addHoveredButton(39017, 25, 15, 15, 39018);
        addText(39019, "Not completed", tda, 0, 0xBF7D0A, true, true);
        rsi.totalChildren(17);
        rsi.child(0, 39001, 55, 42);
        rsi.child(1, 39002, 325, 60);
        rsi.child(2, 39003, 325, 100);
        rsi.child(3, 39050, 58, 48);
        rsi.child(4, 39004, 270, 123);
        rsi.child(5, 39005, 233, 148);
        rsi.child(6, 39006, 233, 165);
        rsi.child(7, 39007, 233, 181);
        rsi.child(8, 39008, 240, 235);
        rsi.child(9, 39009, 240, 235);
        rsi.child(10, 39011, 280, 235);
        rsi.child(11, 39012, 280, 235);
        rsi.child(12, 39014, 242, 241);
        rsi.child(13, 39015, 305, 241);
        rsi.child(14, 39016, 435, 48);
        rsi.child(15, 39017, 435, 48);
        rsi.child(16, 39019, 385, 205);

        RSInterface scrollInterface = addTabInterface(39050);
        scrollInterface.scrollPosition = 0;
        scrollInterface.contentType = 0;
        scrollInterface.width = 118;
        scrollInterface.height = 230;
        scrollInterface.scrollMax = 253;
        scrollInterface.totalChildren(50);

        int y = 1, child = 0;
        for (int i = 0; i < 50; i += 2) {
            addConfigButton(39051 + i, 39000, 277, 278, 114, 26, "", 0, 1, 750 + i);
            addText(39051 + (i + 1), "String: " + (39051 + (i + 1)), 0xFF981F, false, true, 52, tda, 0);

            scrollInterface.child(child++, 39051 + i, 4, y);
            scrollInterface.child(child++, 39051 + (i + 1), 10, y + 9);
            y += 26;
        }
    }

    private static void botLootInterface(TextDrawingArea[] tda) {
        RSInterface rsi = addInterface(37550);
        addSprite(37551, 254);
        addText(37552, "Bot Loot", tda, 2, 0xFF981F, true, true);
        addText(37553, "Total Value:", tda, 0, 0xFF981F, false, true);
        addText(37554, "Total Items", tda, 0, 0xFF981F, true, true);

        rsi.totalChildren(7);
        rsi.child(0, 37551, 6, 8);
        rsi.child(1, 37552, 255, 18);
        rsi.child(2, 37503, 475, 15);
        rsi.child(3, 37504, 475, 15);
        rsi.child(4, 37559, 35, 48);
        rsi.child(5, 37553, 50, 20);
        rsi.child(6, 37554, 400, 20);

        RSInterface scrollInterface = addTabInterface(37559);
        scrollInterface.scrollPosition = 0;
        scrollInterface.contentType = 0;
        scrollInterface.width = 444;
        scrollInterface.height = 250;
        scrollInterface.scrollMax = 1000;
        scrollInterface.totalChildren(1);
        addContainer(37560, 8, 50, 22, 20, false, true, false);
        scrollInterface.child(0, 37560, 16, 9);
    }

    private static void giveInterface(TextDrawingArea[] tda) {
        RSInterface rsi = addInterface(37500);
        addSprite(37501, 254);
        addText(37502, "Spawn Administration", tda, 2, 0xFF981F, true, true);
        addHoverButton(37503, 252, 21, 21, "Close", 250, 37504, 3);
        addHoveredButton(37504, 253, 21, 21, 37505);
        addText(37506, "Search: armadyl", tda, 0, 0xFF981F, false, true);
        addText(37507, "Results found: 95", tda, 0, 0xFF981F, true, true);

        rsi.totalChildren(7);
        rsi.child(0, 37501, 6, 8);
        rsi.child(1, 37502, 255, 18);
        rsi.child(2, 37503, 475, 15);
        rsi.child(3, 37504, 475, 15);
        rsi.child(4, 37520, 35, 48);
        rsi.child(5, 37506, 50, 20);
        rsi.child(6, 37507, 400, 20);

        RSInterface scrollInterface = addTabInterface(37520);
        scrollInterface.scrollPosition = 0;
        scrollInterface.contentType = 0;
        scrollInterface.width = 444;
        scrollInterface.height = 250;
        scrollInterface.scrollMax = 1000;
        scrollInterface.totalChildren(1);
        addContainer(37521, 8, 50, 22, 20, false, false, false, "Take 1", "Take 10", "Take 100", "Take 500", "Take max");
        scrollInterface.child(0, 37521, 16, 9);
    }

    private static void shop(TextDrawingArea[] tda) {
        RSInterface rsi = addInterface(47500);
        addSprite(47501, 254);
        addText(47502, "General Store", tda, 2, 0xFF981F, true, true);
        addHoverButton(47503, 252, 21, 21, "Close", 250, 47504, 3);
        addHoveredButton(47504, 253, 21, 21, 47505);
        addText(47506, "Rick-click on an item in the shop or your inventory to see the available options.", tda, 0, 0xFF981F, true, true);
        addText(47507, "Shop size: ", tda, 0, 0xFF981F, true, true);
        addText(47508, "Points: 324", tda, 0, 0xFF981F, true, true);
        rsi.totalChildren(8);
        rsi.child(0, 47501, 6, 8);
        rsi.child(1, 47502, 255, 18);
        rsi.child(2, 47503, 476, 16);
        rsi.child(3, 47504, 476, 16);
        rsi.child(4, 47550, 35, 48);
        rsi.child(5, 47506, 255, 302);
        rsi.child(6, 47507, 80, 20);
        rsi.child(7, 47508, 400, 20);
        RSInterface scrollInterface = addTabInterface(47550);
        scrollInterface.scrollPosition = 0;
        scrollInterface.contentType = 0;
        scrollInterface.width = 444;
        scrollInterface.height = 250;
        scrollInterface.scrollMax = 400;
        scrollInterface.totalChildren(1 + 80);
        addContainer(47551, 8, 10, 22, 25, false, "Value", "Buy 1", "Buy 10", "Buy 100", "Buy 500");
        for (int i = 0; i < 80; i++) {
            addText(47552 + i, "-1,0", 0xffff00, false, false, 52, tda, 0);
        }
        int shift = 9;
        scrollInterface.child(0, 47551, 7 + shift, 9);
        for (int i = 0; i < 80; i++) {
            int x = i % 8;
            int y = i / 8;
            x = (32 + 22) * x + 8 + shift;
            y = (32 + 25) * y + 45;
            scrollInterface.child(1 + i, 47552 + i, x, y);
        }
    }

    private static void preloads(TextDrawingArea[] tda) {
        RSInterface rsi = addTabInterface(57400);
        addSprite(57401, 858);
        addText(57402, "Equipment Pre-loads", tda, 2, 0xFF981F, true, true);
        addHoverButton(57403, 24, 16, 16, "Close", -1, 57404, 3);
        addHoveredButton(57404, 25, 16, 16, 57405);
        addChar(57406, 690);
        addContainer(57407, 4, 7, 8, 5, false, true, false, null, null, null, null, null);
        addText(57408, "Basic Melee", tda, 3, 0xFCA751, true, true);
        addText(57409, "Level: 126", tda, 3, 0xDB7600, true, true);
        addText(57410, "Total: 4", tda, 3, 0xDB7600, true, true);

        rsi.totalChildren(10);
        rsi.child(0, 57401, 11, 13);
        rsi.child(1, 57402, 255, 20);
        rsi.child(2, 57403, 475, 21);
        rsi.child(3, 57404, 475, 21);
        rsi.child(4, 57406, 175, 200);
        rsi.child(5, 57407, 338, 48);
        rsi.child(6, 57420, 8, 44);
        rsi.child(7, 57408, 240, 57);
        rsi.child(8, 57409, 240, 77);
        rsi.child(9, 57410, 79, 287);

        RSInterface scrollInterface = addTabInterface(57420);
        scrollInterface.scrollPosition = 0;
        scrollInterface.contentType = 0;
        scrollInterface.width = 126;
        scrollInterface.height = 240;
        scrollInterface.scrollMax = 325;
        scrollInterface.totalChildren(80);

        int y = 0;
        int sprite = 0;
        for (int i = 0, child = 0; i < 80; i += 2) {
            int background = sprite % 2 == 0 ? 859 : 860;
            addButton(57421 + i, background, "Activate Preload");
            addText(57421 + (i + 1), "Preload #" + (57421 + (i + 1)), tda, 0, 0xFF981F, false, true);

            scrollInterface.child(child++, 57421 + i, 10, y);
            scrollInterface.child(child++, 57421 + (i + 1), 15, y + 7);

            sprite++;
            y += 25;
        }
    }

    /** Display's bank interface with tabs drawn from client */
    private static void bank(TextDrawingArea[] tda) {
        int interfaceId = 60_000;
        RSInterface bank = addInterface(interfaceId);
        bank.totalChildren(61);
        int child = 23;
        for (int tab = 0; tab < 40; tab += 4) {
            addButton(interfaceId + 31 + tab, Client.spriteCache.get(195), Client.spriteCache.get(195), "Collapse " + (tab == 0 ? "@or2@all tabs" : "tab @or2@" + (tab / 4)), 39, 40);
            int[] array = {21, (tab / 4), 0};
            if (tab / 4 == 0) {
                array = new int[]{5, 211, 0};
            }
            addHoverConfigButton(interfaceId + 32 + tab, interfaceId + 33 + tab, 193, 195, 39, 40, tab == 0 ? "View all" : "New tab", new int[]{1, tab / 4 == 0 ? 1 : 3}, new int[]{(tab / 4), 0}, new int[][]{{5, 211, 0}, array});
            addHoveredConfigButton(interfaceCache[interfaceId + 32 + tab], interfaceId + 33 + tab, interfaceId + 34 + tab, 194, 195);
            interfaceCache[interfaceId + 32 + tab].parentID = interfaceId;
            interfaceCache[interfaceId + 33 + tab].parentID = interfaceId;
            interfaceCache[interfaceId + 34 + tab].parentID = interfaceId;
            bank.child(child++, interfaceId + 31 + tab, 55 + 40 * (tab / 4), 37);
            bank.child(child++, interfaceId + 32 + tab, 55 + 40 * (tab / 4), 37);
            bank.child(child++, interfaceId + 33 + tab, 55 + 40 * (tab / 4), 37);
        }
        addSprite(interfaceId + 1, Client.spriteCache.get(192));
        addHoverButton(interfaceId + 2, 24, 15, 15, "Close", 250, interfaceId + 3, 3);
        addHoveredButton(interfaceId + 3, 25, 15, 15, interfaceId + 4);
        addText(interfaceId + 5, "The Bank of Tarnish", tda, 2, 0xFF981F, true, true);
        addConfigButton(interfaceId + 6, 60_000, 217, 218, 32, 20, "Toggle Insert/Swap", 1, 5, 304);
        addConfigButton(interfaceId + 7, 60_000, 219, 220, 32, 20, "Toggle Note/Un-note", 1, 5, 115);


        addHoverButton(interfaceId + 8, 221, 35, 25, "Deposit Inventory", -1, interfaceId + 9, 1);
        addHoveredButton(interfaceId + 9, 222, 35, 25, interfaceId + 10);
        addHoverButton(interfaceId + 11, 223, 35, 25, "Deposit Equipment", -1, interfaceId + 12, 1);
        addHoveredButton(interfaceId + 12, 224, 35, 25, interfaceId + 13);
        addHoverButton(interfaceId + 14, 225, 35, 25, "Manage Bank Vault", -1, interfaceId + 15, 1);
        addHoveredButton(interfaceId + 15, 226, 35, 25, interfaceId + 16);
        addText(interfaceId + 17, "%1", tda, 0, 0xFE9624, true);
        interfaceCache[interfaceId + 17].scripts = new int[][]{{22, 5382, 0}};
        addText(interfaceId + 18, "360", tda, 0, 0xFF981F, true, true);
        addInputField(interfaceId + 19, 25, 0xFF981F, "Search...", 125, 22, false, true, "[A-Za-z0-9 ]");
        addHoverButton(interfaceId + 20, 298, 0, 0, "", -1, interfaceId + 21, 1);
        addHoveredButton(interfaceId + 21, 298, 0, 0, interfaceId + 22);
        addConfigButton(interfaceId + 23, 60_000, 234, 235, 15, 15, "Display tabs as items", 0, 5, 210);
        addHoverText(interfaceId + 24, "Item", "Display tabs as items", tda, 0, 0xFF981F, false, true, 30);
        addConfigButton(interfaceId + 25, 60_000, 234, 235, 15, 15, "Display tabs as numbers", 1, 5, 210);
        addHoverText(interfaceId + 26, "Number", "Display tabs as numbers", tda, 0, 0xFF981F, false, true, 40);
        addConfigButton(interfaceId + 27, 60_000, 234, 235, 15, 15, "Display tabs as roman numerals", 2, 5, 210);
        addHoverText(interfaceId + 28, "Roman", "Display tabs as roman numerals", tda, 0, 0xFF981F, false, true, 35);
        addSprite(interfaceId + 29, Client.spriteCache.get(197));
        addContainer(5382, 109, 9, 89, "Withdraw-1", "Withdraw-5", "Withdraw-10", "Withdraw-All", "Withdraw-X", null, "Withdraw-All but one");
        addConfigButton(interfaceId + 72, 60_000, 233, 232, 35, 25, "Release Place Holders", 0, 5, 116);
        addHoverButton(interfaceId + 73, "b", 1, 35, 25, "Toggle Place Holders", -1, interfaceId + 72, 1);
        addHoverButton(interfaceId + 74, 227, 115, 25, "Access bank pins settings", -1, interfaceId + 75, 1);
        addHoveredButton(interfaceId + 75, 228, 115, 25, interfaceId + 76);
        addHoverButton(interfaceId + 77, "b", 1, 35, 25, "Deposit coins", -1, interfaceId + 14, 1);
        addHoverButton(interfaceId + 78, "b:", 1, 35, 25, "Check vault", -1, interfaceId + 14, 1);
        //addText(interfaceId + 79, "Bank value: 0", tda, 0, 0xFF981F, false, true);
        addText(interfaceId + 80, "", tda, 0, 0xFF981F, false, true);
        interfaceCache[5385].width += 22;
        interfaceCache[5385].height -= 18;
        interfaceCache[5385].scrollMax = 1444;
        interfaceCache[5382].contentType = 206;
        bank.child(0, interfaceId + 1, 12, 2);
        bank.child(1, interfaceId + 2, 470, 12);
        bank.child(2, interfaceId + 3, 470, 12);
        bank.child(3, interfaceId + 5, 255, 12);
        bank.child(4, interfaceId + 6, 20, 104);
        bank.child(5, interfaceId + 7, 20, 138);
        bank.child(6, interfaceId + 8, 20, 172);
        bank.child(7, interfaceId + 9, 20, 172);
        bank.child(8, interfaceId + 11, 20, 203);
        bank.child(9, interfaceId + 12, 20, 203);
        bank.child(10, interfaceId + 14, 20, 240);
        bank.child(11, interfaceId + 15, 20, 240);
        bank.child(12, interfaceId + 17, 472, 299);
        bank.child(13, interfaceId + 18, 472, 313);
        bank.child(14, interfaceId + 19, 318, 301);
        bank.child(15, interfaceId + 20, 195, 300);
        bank.child(16, interfaceId + 21, 195, 300);
        bank.child(17, interfaceId + 23, 23, 304);
        bank.child(18, interfaceId + 24, 42, 307 - 1);
        bank.child(19, interfaceId + 25, 70, 304);
        bank.child(20, interfaceId + 26, 88, 307 - 1);
        bank.child(21, interfaceId + 27, 130, 304);
        bank.child(22, interfaceId + 28, 148, 307 - 1);
        bank.child(53, interfaceId + 29, 58, 42);
        bank.child(54, 5385, 30, 80);
        bank.child(55, interfaceId + 72, 20, 70);
        bank.child(56, interfaceId + 73, 20, 70);
        bank.child(57, interfaceId + 74, 195, 300);
        bank.child(58, interfaceId + 75, 195, 300);
//        bank.child(59, interfaceId + 77, 19, 240);
//        bank.child(60, interfaceId + 78, 19, 240);
        bank.child(59, interfaceId + 79, 40, 14);
        bank.child(60, interfaceId + 80, 365, 14);
    }

    /**
     * Teleporting interface.
     */
    public static void teleport(TextDrawingArea[] tda) {
        RSInterface rsinterface = addTabInterface(58000);
        addSprite(58001, 164);
        addHoverButton(58002, 252, 21, 21, "Close", -1, 58003, 1);
        addHoveredButton(58003, 253, 21, 21, 58004);
        addText(58005, "Teleportation Menu", tda, 2, 0xff9933, true, true);
        addHoverButton(58006, 165, 159, 23, "Favorites", -1, 58007, 1);
        addHoveredButton(58007, 166, 159, 23, 58008);
        addText(58009, "Cities", tda, 3, 0xff9933, true, true);
        addHoverButton(58010, 165, 159, 23, "Minigames", -1, 58011, 1);
        addHoveredButton(58011, 166, 159, 23, 58012);
        addText(58013, "Pk'ing", tda, 3, 0xff9933, true, true);
        addHoverButton(58014, 165, 159, 23, "Skilling", -1, 58015, 1);
        addHoveredButton(58015, 166, 159, 23, 58016);
        addText(58017, "Bossing", tda, 3, 0xff9933, true, true);
        addHoverButton(58018, 165, 159, 23, "Monster Killing", -1, 58019, 1);
        addHoveredButton(58019, 166, 159, 23, 58020);
        addText(58021, "Minigames", tda, 3, 0xff9933, true, true);
        addHoverButton(58022, 165, 159, 23, "Player Killing", -1, 58023, 1);
        addHoveredButton(58023, 166, 159, 23, 58024);
        addText(58025, "Skilling", tda, 3, 0xff9933, true, true);
        addHoverButton(58026, 165, 159, 23, "Boss Killing", -1, 58027, 1);
        addHoveredButton(58027, 166, 159, 23, 58028);
        addText(58029, "Training", tda, 3, 0xff9933, true, true);
        addText(58031, "2", tda, 3, 0xff9933, true, true);
        addText(58032, "3", tda, 0, 0xff9933, true, true);
        addText(58033, "4", tda, 0, 0xff9933, true, true);
        addHoverButton(58035, 246, 117, 35, "Teleport to", -1, 58036, 1);
        addHoveredButton(58036, 247, 117, 35, 58037);
        addText(58038, "Teleport", tda, 1, 0xff9933, true, true);
        addConfigButton(58039, 58000, 234, 235, 15, 15, "Favorite teleport", 0, 5, 348, false);
        addHoverText(58040, "Favorite teleport", "Favorite teleport", tda, 0, 0xEB981F, 0xFFFFFF, false, true, 60);
        addContainer(58041, 3, 7, 8, 8, false, false, false, null, null, null, null, null);

        addConfigSprite(58042, 171, 171, 0, 877);
        addConfigSprite(58043, 173, 173, 1, 877);
        addConfigSprite(58044, 169, 169, 2, 877);
        addConfigSprite(58046, 170, 170, 3, 877);
        addConfigSprite(58045, 174, 174, 4, 877);
        addConfigSprite(58047, 172, 172, 5, 877);
        rsinterface.totalChildren(38);
        rsinterface.child(0, 58001, 11, 11);
        rsinterface.child(1, 58002, 472, 19);
        rsinterface.child(2, 58003, 472, 19);
        rsinterface.child(3, 58005, 255, 21);
        rsinterface.child(4, 58006, 18, 49);
        rsinterface.child(5, 58007, 18, 49);
        rsinterface.child(6, 58009, 95 + 12, 49);
        rsinterface.child(7, 58010, 176, 49);
        rsinterface.child(8, 58011, 176, 49);
        rsinterface.child(9, 58013, 255 + 12, 49);
        rsinterface.child(10, 58014, 334, 49);
        rsinterface.child(11, 58015, 334, 49);
        rsinterface.child(12, 58017, 413 + 12, 49);
        rsinterface.child(13, 58018, 18, 71);
        rsinterface.child(14, 58019, 18, 71);
        rsinterface.child(15, 58021, 95 + 12, 72);
        rsinterface.child(16, 58022, 176, 71);
        rsinterface.child(17, 58023, 176, 71);
        rsinterface.child(18, 58025, 255 + 12, 72);
        rsinterface.child(19, 58026, 334, 71);
        rsinterface.child(20, 58027, 334, 71);
        rsinterface.child(21, 58029, 413 + 12, 72);
        rsinterface.child(22, 58031, 335, 130);
        rsinterface.child(23, 58032, 335, 165);
        rsinterface.child(24, 58033, 335, 185);
        rsinterface.child(25, 58035, 280, 250);
        rsinterface.child(26, 58036, 280, 250);
        rsinterface.child(27, 58038, 340, 259);
        rsinterface.child(28, 58050, -31, 101);
        rsinterface.child(29, 58039, 205, 290);
        rsinterface.child(30, 58040, 225, 292);
        rsinterface.child(31, 58042, 176, 70);
        rsinterface.child(32, 58043, 176, 47);
        rsinterface.child(33, 58044, 18, 70);
        rsinterface.child(34, 58045, 18, 47);
        rsinterface.child(35, 58047, 334, 70);
        rsinterface.child(36, 58046, 334, 47);
        rsinterface.child(37, 58041, 280, 210);
        RSInterface scrollInterface = addTabInterface(58050);
        scrollInterface.scrollPosition = 0;
        scrollInterface.contentType = 0;
        scrollInterface.width = 202;
        scrollInterface.height = 216;
        scrollInterface.scrollMax = 225;
        scrollInterface.totalChildren(10 * 4);
        int y = 0;
        int count = 0;
        for (int i = 0, child = 0; i < 10 * 4; i += 2) {
            addSprite(58051 + i, count % 2 == 0 ? 452 : -1);
            addHoverText(58051 + (i + 1), "", "View teleport", tda, 0, 0xff9933, false, true, 150);
            scrollInterface.child(child++, 58051 + i, 50, y);
            scrollInterface.child(child++, 58051 + (i + 1), 60, y + 8);
            y += 25;
            count++;
        }
    }

    private static void clanManageScroll(TextDrawingArea[] tda) {
        RSInterface scrollInterface = addTabInterface(42100);
        addText(42101, "Name:", tda, 0, 0xDE8B0D, true, true);
        addInputField(42102, 10, 0xFF981F, "Enter name...", 115, 25, false, false, "[A-Za-z0-9 ]");
        addText(42103, "Tag:", tda, 0, 0xDE8B0D, true, true);
        addInputField(42104, 4, 0xFF981F, "Enter tag...", 115, 25, false, false, "[A-Za-z0-9 ]");
        addText(42105, "Slogan:", tda, 0, 0xDE8B0D, true, true);
        addInputField(42106, 24, 0xFF981F, "Enter slogan...", 115, 25, false, false, "[A-Za-z0-9 ]");
        addText(42107, "Pass:", tda, 0, 0xDE8B0D, true, true);
        addInputField(42108, 10, 0xFF981F, "Enter password...", 115, 25, false, false, "[A-Za-z0-9 ]");
        addText(42137, "Thread:", tda, 0, 0xDE8B0D, true, true);
        addInputField(42138, 25, 0xFF981F, "Enter thread url...", 115, 25, false, false, "[A-Za-z0-9 ]");
        addText(42109, "Type:", tda, 0, 0xDE8B0D, true, true);
        addDropdownMenu(42110, 115, 0, false, false, Dropdown.DEFAULT, "<icon=8> PvP", "<icon=3> PvM", "<icon=5> Skilling", "<icon=20> Social", "<icon=18> Ironman");
        addText(42111, "Enter:", tda, 0, 0xDE8B0D, true, true);
        addDropdownMenu(42112, 115, 0, false, false, Dropdown.DEFAULT, "Anyone", "<clan=0> Friends", "<clan=1> Recruit", "<clan=2> Corporeal", "<clan=3> Sergeant", "<clan=4> Lieutenant", "<clan=5> Captain", "<clan=6> General", "<clan=7> Leader");
        addText(42113, "Talk:", tda, 0, 0xDE8B0D, true, true);
        addDropdownMenu(42114, 115, 0, false, false, Dropdown.DEFAULT, "Anyone", "<clan=0> Friends", "<clan=1> Recruit", "<clan=2> Corporeal", "<clan=3> Sergeant", "<clan=4> Lieutenant", "<clan=5> Captain", "<clan=6> General", "<clan=7> Leader");
        addText(42115, "Manage:", tda, 0, 0xDE8B0D, true, true);
        addDropdownMenu(42116, 115, 0, false, false, Dropdown.DEFAULT, "Anyone", "<clan=0> Friends", "<clan=1> Recruit", "<clan=2> Corporeal", "<clan=3> Sergeant", "<clan=4> Lieutenant", "<clan=5> Captain", "<clan=6> General", "<clan=7> Leader");
        addHoverButton(42117, 699, 36, 36, "Change item showcase", -1, 42118, 1);
        addHoveredButton(42118, 287, 36, 36, 42119);
        addHoverButton(42120, 699, 36, 36, "Change item showcase", -1, 42121, 1);
        addHoveredButton(42121, 287, 36, 36, 42122);
        addHoverButton(42123, 699, 36, 36, "Change item showcase", -1, 42124, 1);
        addHoveredButton(42124, 287, 36, 36, 42125);
        addContainer(42126, 3, 7, 9, 8, false, false, false, null, null, null, null, null);
        addHoverText(42127, "Lock clan (no one can join)", "Toggle clan lock", tda, 0, 0xEB981F, 0xFFFFFF, false, true, 60);
        addConfigButton(42128, 42100, 235, 234, 15, 15, "Toggle clan lock", 1, 5, 326, false);
        addText(42129, "Banned Members", tda, 0, 0xDE8B0D, false, true);
        addHoverButton(42130, 391, 118, 32, "Manage banned members", 0, 42131, 1);
        addHoveredButton(42131, 392, 118, 32, 42132);
        addText(42133, "Color:", tda, 0, 0xDE8B0D, true, true);
        addDropdownMenu(42134, 115, 0, false, false, Dropdown.DEFAULT,
                "<col=ffffff>White",
                "<col=F03737>Red",
                "<col=2ADE36>Green",
                "<col=2974FF>Blue",
                "<col=EBA226>Orange",
                "<col=A82D81>Purple",
                "<col=FF57CA>Pink");
        addText(42135, "To confirm an input field change", tda, 0, 0xA1D490, true, true);
        addText(42136, "press enter after filling it out", tda, 0, 0xA1D490, true, true);


        scrollInterface.totalChildren(34);
        scrollInterface.scrollPosition = 0;
        scrollInterface.contentType = 0;
        scrollInterface.width = 178;
        scrollInterface.height = 190;
        scrollInterface.scrollMax = 460;

        scrollInterface.child(0, 42117, 75, 8 + 30);
        scrollInterface.child(1, 42118, 75, 8 + 30);
        scrollInterface.child(2, 42120, 35, 8 + 30);
        scrollInterface.child(3, 42121, 35, 8 + 30);
        scrollInterface.child(4, 42123, 115, 8 + 30);
        scrollInterface.child(5, 42124, 115, 8 + 30);
        scrollInterface.child(6, 42126, 38, 8 + 30);
        scrollInterface.child(7, 42101, 28, 55 + 30);
        scrollInterface.child(8, 42102, 60, 50 + 30);
        scrollInterface.child(9, 42103, 28, 85 + 30);
        scrollInterface.child(10, 42104, 60, 80 + 30);
        scrollInterface.child(11, 42105, 28, 115 + 30);
        scrollInterface.child(12, 42106, 60, 110 + 30);
        scrollInterface.child(13, 42107, 28, 145 + 30);
        scrollInterface.child(14, 42108, 60, 140 + 30);
        scrollInterface.child(15, 42115, 28, 265 + 60);
        scrollInterface.child(16, 42127, 35, 295 + 90 + 5);
        scrollInterface.child(17, 42128, 15, 293 + 90 + 5);
        scrollInterface.child(18, 42130, 35, 320 + 90 + 5);
        scrollInterface.child(19, 42131, 35, 320 + 90 + 5);
        scrollInterface.child(20, 42129, 52, 328 + 90 + 5);
        scrollInterface.child(21, 42133, 30, 290 + 60);
        scrollInterface.child(22, 42134, 60, 290 + 60);
        scrollInterface.child(23, 42116, 60, 260 + 60);
        scrollInterface.child(24, 42113, 28, 235 + 60);
        scrollInterface.child(25, 42114, 60, 230 + 60);
        scrollInterface.child(26, 42111, 28, 205 + 60);
        scrollInterface.child(27, 42112, 60, 200 + 60);
        scrollInterface.child(28, 42109, 28, 175 + 60);
        scrollInterface.child(29, 42110, 60, 170 + 60);
        scrollInterface.child(30, 42135, 95, 6);
        scrollInterface.child(31, 42136, 95, 6 + 15);
        scrollInterface.child(32, 42137, 28, 145 + 60);
        scrollInterface.child(33, 42138, 60, 140 + 60);
    }

    private static void clanManage(TextDrawingArea[] tda) {
        RSInterface rsinterface = addTabInterface(42000);
        addText(42001, "Clan Management", tda, 2, 0xDE8B0D, true, true);
        addSprite(42004, 265);
        addSprite(42005, 64);

        addHoverButton(42006, 391, 118, 32, "Return to main tab", 0, 42007, 1);
        addHoveredButton(42007, 392, 118, 32, 42008);
        addText(42009, "Close Tab", tda, 3, 0xF7DC6F, true, true);
        rsinterface.totalChildren(9);
        rsinterface.child(0, 42001, 97, 5);
        rsinterface.child(1, 42004, -5, 25);
        rsinterface.child(2, 42004, -5, 75);
        rsinterface.child(3, 42005, 0, 25);
        rsinterface.child(4, 42005, 0, 217);
        rsinterface.child(5, 42100, -3, 27);
        rsinterface.child(6, 42006, 40, 224);
        rsinterface.child(7, 42007, 40, 224);
        rsinterface.child(8, 42009, 98, 230);
    }

    private static void clanChat(TextDrawingArea[] tda) {
        RSInterface rsinterface = addInterface(33500);
        addSprite(33501, 67);
        addText(33502, Configuration.NAME, tda, 3, 0xDE8B0D, true, true);
        addSprite(33503, 68);
        addHoverButton(33507, 834, 20, 20, "Join clan", -1, 33508, 1);
        addHoveredButton(33508, 835, 20, 20, 33509);
        addHoverButton(33510, 836, 20, 20, "Leave clan", -1, 33511, 1);
        addHoveredButton(33511, 837, 20, 20, 33512);
        addHoverButton(33513, 838, 20, 20, "View clan information", -1, 33514, 1);
        addHoveredButton(33514, 839, 20, 20, 33515);
        addHoverButton(33520, 840, 20, 20, "Manage clan", -1, 33521, 1);
        addHoveredButton(33521, 841, 20, 20, 33522);
        addConfigButton(33518, 33500, 706, 705, 24, 22, "Manage Lootshare", 0, 5, 393, false);
        addConfigButton(33525, 33500, 444, 443, 24, 12, "Sort by privilege", 0, 5, 394, false);
        addConfigButton(33526, 33500, 446, 445, 117, 12, "Sort by name", 1, 5, 394, false);
        addConfigButton(33527, 33500, 448, 447, 56, 12, "Sort by rank", 2, 5, 394, false);
        rsinterface.totalChildren(16);
        rsinterface.child(0, 33502, 97, 2);
        rsinterface.child(1, 33507, 5, 237);
        rsinterface.child(2, 33508, 5, 237);
        rsinterface.child(3, 33510, 25, 235);
        rsinterface.child(4, 33511, 25, 235);
        rsinterface.child(5, 33513, 45, 235);
        rsinterface.child(6, 33514, 45, 235);
        rsinterface.child(7, 33520, 65, 235);
        rsinterface.child(8, 33521, 65, 235);
        rsinterface.child(9, 33518, 155, 235);
        rsinterface.child(10, 33530, -3, 38);
        rsinterface.child(11, 33503, 0, 25);
        rsinterface.child(12, 33503, 0, 223);
        rsinterface.child(13, 33525, 0, 26);
        rsinterface.child(14, 33526, 20, 26);
        rsinterface.child(15, 33527, 135, 26);
        RSInterface scrollInterface = addTabInterface(33530);
        scrollInterface.scrollPosition = 0;
        scrollInterface.contentType = 0;
        scrollInterface.width = 178;
        scrollInterface.height = 188;
        scrollInterface.scrollMax = 400;
        int x = 3, y = 0, child = 0, sprite = 0, size = 600;
        scrollInterface.totalChildren(size);
        for (int i = 0; i < size; i += 4) {
            sprite++;
            int id = sprite % 2 == 0 ? 65 : 66;
            addSprite(33531 + i, id);
            addText((33531 + (i + 1)), "<clan=" + Utility.random(1, 8) + ">", tda, 3, 0xA4997D, false, true);
            addHoverText((33531 + (i + 2)), "Daniel", "", tda, 3, 0xA4997D, false, true, 60);
            addText((33531 + (i + 3)), "#" + Utility.random(1, 150), tda, 3, 0xF7DC6F, false, true);
            scrollInterface.child(child++, 33531 + i, x, y);
            scrollInterface.child(child++, (33531 + (i + 1)), x + 5, y + 4);
            scrollInterface.child(child++, (33531 + (i + 2)), x + 20, y + 1);
            scrollInterface.child(child++, (33531 + (i + 3)), x + 140, y + 1);
            y += 21;
        }
    }

    private static void clanMemberManage(TextDrawingArea[] tda) {
        RSInterface menu = addTabInterface(43600);
        addSprite(43601, 709);
        addText(43602, "Member Management", tda, 2, 0xDE8B0D, true, true);
        addHoverButton(43603, 24, 16, 16, "Close", 250, 43604, 3);
        addHoveredButton(43604, 25, 16, 16, 43605);
        addText(43606, "Daniel", tda, 0, 0xDE8B0D, true, true);
        addHoverButton(43607, 288, 150, 35, "Promote member", -1, 43608, 1);
        addHoveredButton(43608, 289, 150, 35, 43609);
        addHoverButton(43610, 288, 150, 35, "Demote member", -1, 43611, 1);
        addHoveredButton(43611, 289, 150, 35, 43612);
        addHoverButton(43613, 288, 150, 35, "Kick member", -1, 43614, 1);
        addHoveredButton(43614, 289, 150, 35, 43615);
        addHoverButton(43616, 288, 150, 35, "Ban member", -1, 43617, 1);
        addHoveredButton(43617, 289, 150, 35, 43618);
        addText(43619, "Promote Member", tda, 3, 0xDE8B0D, true, true);
        addText(43620, "Demote Member", tda, 3, 0xDE8B0D, true, true);
        addText(43621, "Kick Member", tda, 3, 0xDE8B0D, true, true);
        addText(43622, "Ban Member", tda, 3, 0xDE8B0D, true, true);
        menu.totalChildren(17);
        menu.child(0, 43601, 125, 35);
        menu.child(1, 43602, 259, 43);
        menu.child(2, 43603, 365, 43);
        menu.child(3, 43604, 365, 44);
        menu.child(4, 43606, 259, 81);
        menu.child(5, 43607, 182, 108);
        menu.child(6, 43608, 182, 108);
        menu.child(7, 43610, 182, 152);
        menu.child(8, 43611, 182, 152);
        menu.child(9, 43613, 182, 196);
        menu.child(10, 43614, 182, 196);
        menu.child(11, 43616, 182, 241);
        menu.child(12, 43617, 182, 241);
        menu.child(13, 43619, 259, 114);
        menu.child(14, 43620, 259, 158);
        menu.child(15, 43621, 259, 203);
        menu.child(16, 43622, 259, 248);
    }

    private static void clanInformation2(TextDrawingArea[] tda) {
        RSInterface rsinterface = addTabInterface(53500);
        addSprite(55001, 435);
        rsinterface.totalChildren(14);

        rsinterface.child(0, 55001, 13, 13);
        rsinterface.child(1, 53002, 473, 22);
        rsinterface.child(2, 53003, 473, 22);
        rsinterface.child(3, 53005, 258, 21);
        rsinterface.child(4, 53006, 95, 46);
        rsinterface.child(5, 53009, 275, 88);
        rsinterface.child(6, 53010, 275, 105);
        rsinterface.child(7, 53011, 351, 85);
        rsinterface.child(8, 53013, 180, 48);
        rsinterface.child(9, 53014, 340, 48);
        rsinterface.child(10, 53007, 258, 46);
        rsinterface.child(11, 53008, 417, 46);
        rsinterface.child(12, 55010, 189, 140);
        rsinterface.child(13, 55050, 18, 74);

        RSInterface clanMembersList = addTabInterface(55050);
        clanMembersList.scrollPosition = 0;
        clanMembersList.contentType = 0;
        clanMembersList.width = 138;
        clanMembersList.height = 240;
        clanMembersList.scrollMax = 385;
        clanMembersList.totalChildren(200);
        int y = 1, count = 0;
        for (int i = 0, child = 0; i < 200; i += 2) {
            addSprite(55051 + i, count % 2 == 0 ? 453 : 454);
            addHoverText((55051 + (i + 1)), "", "", tda, 0, 0xFF9900, 0xFFFFFF, false, true, 168);
            clanMembersList.child(child++, 55051 + i, 2, y + 1);
            clanMembersList.child(child++, 55051 + (1 + i), 7, y + 4);
            y += 22;
            count++;
        }

        RSInterface clanMemberInfo = addTabInterface(55010);
        clanMemberInfo.scrollPosition = 0;
        clanMemberInfo.contentType = 0;
        clanMemberInfo.width = 276;
        clanMemberInfo.height = 160;
        clanMemberInfo.scrollMax = 385;
        clanMemberInfo.totalChildren(20);
        y = 1;
        count = 0;
        for (int i = 0, child = 0; i < 20; i += 2) {
            addSprite(55011 + i, count % 2 == 0 ? 453 : 454);
            addText(55011 + (1 + i), "2", tda, 0, 0xDE8B0D, false, true);
            clanMemberInfo.child(child++, 55011 + i, 2, y + 1);
            clanMemberInfo.child(child++, 55011 + (1 + i), 7, y + 4);
            y += 22;
            count++;
        }
    }


    private static void clanInformation(TextDrawingArea[] tda) {
        RSInterface rsinterface = addTabInterface(53000);
        addSprite(53001, 833);

        addHoverButton(53002, 252, 20, 19, "Close", -1, 53003, 3);
        addHoveredButton(53003, 253, 20, 19, 53004);


        addText(53005, "Clan Viewer", tda, 2, 0xFF981F, true, true);
        addText(53006, "Top Clans:", tda, 3, 0xEB981F, true, true);
        addText(53007, "Clan Overview", tda, 3, 0xEB981F, true, true);
        addText(53008, "Clan Members", tda, 3, 0xEB981F, true, true);
        addText(53009, "Teh Beasts", tda, 1, 0xF7DC6F, true, true);
        addText(53010, "Unleash your inner beast", tda, 0, 0xC47423, true, true);
        addContainer(53011, 3, 7, 9, 8, false, false, false, null, null, null, null, null);
        addInputField(53012, 15, 0xFF981F, "Search for clan...", 156, 24, false, false, "[A-Za-z0-9 ]");
        addConfigButton(53013, 53000, 433, 434, 159, 19, "View clan overview", 0, 5, 531, false);
        addConfigButton(53014, 53000, 433, 434, 159, 19, "View clan members", 1, 5, 531, false);
        addHoverButton(53015, 391, 118, 32, "Join clan channel", -1, 53016, 1);
        addHoveredButton(53016, 392, 118, 32, 53017);
        addText(53018, "Join Clan", tda, 3, 0xFF981F, true, true);

        addDropdownMenu(53019, 159, 0, false, true, Dropdown.DEFAULT, "All Time", "Top PvP", "Top PvM", "Top Skilling", "Top Ironman");
        //When adding dropdown back, remember to increase total children

        rsinterface.totalChildren(18);
        rsinterface.child(0, 53001, 13, 13);
        rsinterface.child(1, 53002, 473, 19);
        rsinterface.child(2, 53003, 473, 19);
        rsinterface.child(3, 53005, 258, 21);
        rsinterface.child(4, 53006, 95, 46);
        rsinterface.child(5, 53009, 275, 88);
        rsinterface.child(6, 53010, 275, 105);
        rsinterface.child(7, 53011, 351, 85);
        rsinterface.child(8, 53100, 189, 140);
        rsinterface.child(9, 53200, 18, 74);
        rsinterface.child(10, 53012, 18, 291);
        rsinterface.child(11, 53013, 180, 48);
        rsinterface.child(12, 53014, 340, 48);
        rsinterface.child(13, 53007, 258, 46);
        rsinterface.child(14, 53008, 417, 46);
        rsinterface.child(15, 53015, 280, 270);
        rsinterface.child(16, 53016, 280, 270);
        rsinterface.child(17, 53018, 335, 275);
        //rsinterface.child(18, 53019, 18, 18);

        RSInterface clanDetailScroll = addTabInterface(53100);
        RSInterface clanListScroll = addTabInterface(53200);

        clanDetailScroll.scrollPosition = 0;
        clanDetailScroll.contentType = 0;
        clanDetailScroll.width = 276;
        clanDetailScroll.height = 128;
        clanDetailScroll.scrollMax = 385;
        clanDetailScroll.totalChildren(40);
        int y = 1, count = 0;
        for (int i = 0, child = 0; i < 40; i += 2) {
            addSprite(53101 + i, count % 2 == 0 ? 453 : 454);
            addText(53101 + (1 + i), "", tda, 0, 0xDE8B0D, false, true);
            clanDetailScroll.child(child++, 53101 + i, 2, y + 1);
            clanDetailScroll.child(child++, 53101 + (1 + i), 7, y + 4);
            y += 22;
            count++;
        }
        clanListScroll.scrollPosition = 0;
        clanListScroll.contentType = 0;
        clanListScroll.width = 140;
        clanListScroll.height = 216;
        clanListScroll.scrollMax = 385;
        clanListScroll.totalChildren(40);
        y = 1;
        count = 0;
        for (int i = 0, child = 0; i < 40; i += 2) {
            int id;
            int color;
            int x = 0;
            if (i == 0) {
                id = 843;
                color = 0xFF9900;
                x += 2;
            } else if (i == 2) {
                id = 844;
                color = 0xFF9900;
                x += 2;
            } else if (i == 4) {
                id = 845;
                color = 0xFF9900;
                x += 2;
            } else {
                id = count % 2 == 0 ? 453 : 454;
                color = 0xFF9900;
            }

            addSprite(53201 + i, id);
            addHoverText((53201 + (i + 1)), "", "", tda, 0, color, 0xFFFFFF, false, true, 168);
            clanListScroll.child(child++, 53201 + i, x, y + 1);
            clanListScroll.child(child++, 53201 + (1 + i), 7, y + 4);
            y += 22;
            count++;
        }
    }

        /** Displays all the players friends players. */
        private static void friendsTab(TextDrawingArea[] tda) {
            RSInterface tab = addTabInterface(5065);
        RSInterface list = interfaceCache[5066];
        addSprite(16126, 154);
        addSprite(16127, 155);
        addText(5067, "Friends List", tda, 1, 0xff9933, true, true);
        addText(5070, "Economy World", tda, 0, 0xff9933, true, true);
        addText(5071, "", tda, 0, 0xff9933, false, true);
        addHoverButton(5068, 156, 20, 29, "Add Friend", -1, 5072, 1);
        addHoveredButton(5072, 157, 20, 29, 5073);
        addHoverButton(5069, 158, 20, 29, "Delete Friend", 202, 5074, 1);
        addHoveredButton(5074, 159, 20, 29, 5075);
        addText(5079, "0 / 200", tda, 0, 0xFFFFFF, false, true, 901, 0);
        tab.totalChildren(12);
        tab.child(0, 16127, -2, 40);
        tab.child(1, 5067, 95, 5);
        tab.child(2, 16126, 0, 40);
        tab.child(3, 5066, 0, 42);
        tab.child(4, 16126, 0, 233);
        tab.child(5, 5068, 5, 240);
        tab.child(6, 5072, 5, 240);
        tab.child(7, 5069, 30, 240);
        tab.child(8, 5074, 30, 240);
        tab.child(9, 5070, 95, 25);
        tab.child(10, 5071, 106, 237);
        tab.child(11, 5079, 145, 242);
        list.height = 191;
        list.width = 175;
        list.scrollMax = 200;
        for (int id = 5092, i = 0; id <= 5191 && i <= 99; id++, i++) {
            list.children[i] = id;
            list.childX[i] = 3;
            list.childY[i] = list.childY[i] - 7;
        }
        for (int id = 5192, i = 100; id <= 5291 && i <= 199; id++, i++) {
            list.children[i] = id;
            list.childX[i] = 126;
            list.childY[i] = list.childY[i] - 7;
        }
    }

    /** Displays all the players ignored players. */
    private static void ignoreTab(TextDrawingArea[] tda) {
        RSInterface tab = addTabInterface(5715);
        RSInterface list = interfaceCache[5716];
        addText(5717, "Ignore List", tda, 1, 0xff9933, true, true);
        addText(5720, "", tda, 0, 0xff9933, true, true);
        addText(5721, "", tda, 0, 0xff9933, false, true);
        addHoverButton(5718, 160, 20, 29, "Add Name", 501, 5722, 1);
        addHoveredButton(5722, 161, 20, 29, 5723);
        addHoverButton(5719, 162, 20, 29, "Delete Name", 502, 5724, 1);
        addHoveredButton(5724, 163, 20, 29, 5725);
        addText(5824, "0 / 100", tda, 0, 0xFFFFFF, false, true, 902, 0);
        tab.totalChildren(12);
        tab.child(0, 5717, 95, 15);
        tab.child(1, 16127, -2, 40);
        tab.child(2, 16126, 0, 40);
        tab.child(3, 5716, 0, 42);
        tab.child(4, 16126, 0, 233);
        tab.child(5, 5718, 5, 240);
        tab.child(6, 5722, 5, 240);
        tab.child(7, 5719, 30, 240);
        tab.child(8, 5724, 30, 240);
        tab.child(9, 5720, 95, 25);
        tab.child(10, 5721, 108, 237);
        tab.child(11, 5824, 145, 242);
        list.height = 191;
        list.width = 175;
        list.scrollMax = 200;
        for (int id = 5742, i = 0; id <= 5841 && i <= 99; id++, i++) {
            list.children[i] = id;
            list.childX[i] = 3;
            list.childY[i] = list.childY[i] - 7;
        }
    }

    /** Displays the player's character on screen with bonuses text. */
    private static void equipmentScreen(TextDrawingArea[] tda) {
        RSInterface tab = addTabInterface(15106);
        addSprite(15107, 145);
        addHoverButton(15210, 24, 15, 15, "Close", 250, 15211, 3);
        addHoveredButton(15211, 25, 15, 15, 15212);
        addText(15115, "Equipment Bonuses", tda, 2, 0xFF981F, false, true);
        addChar(15125, 650);
        addText(15116, "1", tda, 0, 0xFF981F, true, true);
        addText(15117, "2", tda, 0, 0xFF981F, true, true);
        addText(15118, "", tda, 0, 0xFF981F, true, true);
        addText(15112, "Attack bonus:", tda, 2, 0xFF981F, false, true);
        addText(15113, "Defence bonus:", tda, 2, 0xFF981F, false, true);
        addText(15114, "Other bonuses:", tda, 2, 0xFF981F, false, true);
        addText(15130, "15130", tda, 1, 0xFF9200, false, true);
        addText(15131, "15131", tda, 1, 0xFF9200, false, true);
        addText(15132, "15132", tda, 1, 0xFF9200, false, true);
        addText(15133, "15133", tda, 1, 0xFF9200, false, true);
        addText(15134, "15134", tda, 1, 0xFF9200, false, true);
        addText(15135, "15135", tda, 1, 0xFF9200, false, true);
        addText(15136, "15136", tda, 1, 0xFF9200, false, true);
        addText(15137, "15137", tda, 1, 0xFF9200, false, true);
        addText(15138, "15138", tda, 1, 0xFF9200, false, true);
        addText(15139, "15139", tda, 1, 0xFF9200, false, true);
        addText(15140, "15140", tda, 1, 0xFF9200, false, true);
        addText(15141, "15141", tda, 1, 0xFF9200, false, true);
        addText(15142, "15142", tda, 1, 0xFF9200, false, true);
        addText(15143, "15143", tda, 1, 0xFF9200, false, true);
        addSprite(15144, 183);
        addText(15145, "1.2 Kg", tda, 0, 0xFF9200, false, true);
        tab.totalChildren(39);
        tab.child(0, 15107, 15, 5);
        tab.child(1, 15210, 474, 14);
        tab.child(2, 15211, 474, 14);
        tab.child(3, 15115, 195, 13);
        tab.child(4, 15125, 188, 175);
        tab.child(5, 15116, 420, 280);
        tab.child(6, 15117, 420, 300);
        tab.child(7, 15118, 420, 305);
        tab.child(8, RSInterface.interfaceCache[1644].children[12], 400, 50);
        tab.child(9, RSInterface.interfaceCache[1644].children[13], 360, 90);
        tab.child(10, RSInterface.interfaceCache[1644].children[14], 400, 90);
        tab.child(11, RSInterface.interfaceCache[1644].children[15], 440, 90);
        tab.child(12, RSInterface.interfaceCache[1644].children[16], 345, 130);
        tab.child(13, RSInterface.interfaceCache[1644].children[17], 400, 130);
        tab.child(14, RSInterface.interfaceCache[1644].children[18], 455, 130);
        tab.child(15, RSInterface.interfaceCache[1644].children[19], 400, 170);
        tab.child(16, RSInterface.interfaceCache[1644].children[20], 400, 210);
        tab.child(17, RSInterface.interfaceCache[1644].children[21], 345, 210);
        tab.child(18, RSInterface.interfaceCache[1644].children[22], 455, 210);
        tab.child(19, RSInterface.interfaceCache[1644].children[23], 346, 89);
        tab.child(20, 15112, 30, 45);
        tab.child(21, 15113, 30, 140);
        tab.child(22, 15114, 30, 235);
        tab.child(23, 15130, 35, 60);
        tab.child(24, 15131, 35, 75);
        tab.child(25, 15132, 35, 90);
        tab.child(26, 15133, 35, 105);
        tab.child(27, 15134, 35, 120);
        tab.child(28, 15135, 35, 155);
        tab.child(29, 15136, 35, 170);
        tab.child(30, 15137, 35, 185);
        tab.child(31, 15138, 35, 200);
        tab.child(32, 15139, 35, 215);
        tab.child(33, 15140, 35, 250);
        tab.child(34, 15141, 35, 265);
        tab.child(35, 15142, 35, 280);
        tab.child(36, 15143, 35, 295);
        tab.child(37, 15144, 235, 295);
        tab.child(38, 15145, 255, 299);
    }

    /**
     * Equipment tab gives option to view price checker, items kept on death,
     * and equipment screen.
     */
    public static void equipmentTab(TextDrawingArea[] wid) {
        RSInterface Interface = interfaceCache[1644];
        addSprite(15101, 0, "Interfaces/Equipment/bl");
        addSprite(15102, 1, "Interfaces/Equipment/bl");
        addSprite(15109, 2, "Interfaces/Equipment/bl");
        removeSomething(21338);
        removeSomething(21344); // remove "Stats" text
        //        removeConfig(21342);
        removeSomething(21341);
        removeSomething(21340);
        //        removeConfig(15103);
        //        removeConfig(15104);
        Interface.childX[23] = 24;
        Interface.childY[23] = 42;
        Interface.children[24] = 15102;
        Interface.childX[24] = 110;
        Interface.childY[24] = 205;
        Interface.children[25] = 15109;
        Interface.childX[25] = 39;
        Interface.childY[25] = 240;
        Interface.children[26] = 27650;
        Interface.childX[26] = 0;
        Interface.childY[26] = 0;
        Interface = addInterface(27650);
        addHoverButton(27651, 146, 40, 40, "Price-checker", -1, 27652, 1);
        addHoveredButton(27652, 147, 40, 40, 27658);
        addHoverButton(27653, 148, 40, 40, "Show Equipment Stats", -1, 27655, 1);
        addHoveredButton(27655, 149, 40, 40, 27665);
        addHoverButton(27654, 150, 40, 40, "Show items kept on death", -1, 27657, 1);
        addHoveredButton(27657, 151, 40, 40, 27666);
        addButton(27659, 3636, "Open Overrides");

        setChildren(7, Interface);
        setBounds(27653, 22, 210, 0, Interface);
        setBounds(27655, 22, 210, 1, Interface);
        setBounds(27654, 78, 210, 2, Interface);
        setBounds(27657, 78, 210, 3, Interface);
        setBounds(27651, 133, 210, 4, Interface);
        setBounds(27652, 133, 210, 5, Interface);
        setBounds(27659, 37, 4, 6, Interface);
    }


    private static void informationTab(TextDrawingArea tda[]) {
        RSInterface rsinterface = addInterface(29400);
        addSprite(29401, 63);
        addSprite(29402, 64);
        addText(29403, Configuration.NAME, tda, 2, 0xC47423, false, true);
        addHoverText(29404, "www.tarnishps.com", "Visit website", tda, 3, 0xC47423, true, true, 168);
        addHoverButton(29408, 292, 18, 18, "View Achievements Tab", -1, 29409, 1);
        addHoveredButton(29409, 293, 18, 18, 29410);
        addHoverButton(29411, 71, 16, 16, "Refresh Quest Tab", -1, 29412, 1);
        addHoveredButton(29412, 72, 16, 16, 29413);
        addHoverButton(29414, 296, 18, 18, "View Tool Tab", -1, 29415, 1);
        addHoveredButton(29415, 297, 18, 18, 29416);
        addHoverButton(29417, 552, 24, 24, "View Server Settings", -1, 29418, 1);
        addHoveredButton(29418, 553, 24, 24, 29419);
        rsinterface.totalChildren(14);
        rsinterface.child(0, 29401, -5, 35);
        rsinterface.child(1, 29402, 0, 35);
        rsinterface.child(2, 29402, 0, 234);
        rsinterface.child(3, 29403, 10, 12);
        rsinterface.child(4, 29404, 9, 238);
        rsinterface.child(5, 29450, 3, 37);
        rsinterface.child(6, 29408, 125, 11);
        rsinterface.child(7, 29409, 125, 11);
        rsinterface.child(8, 29411, 160, 37);
        rsinterface.child(9, 29412, 160, 37);
        rsinterface.child(10, 29414, 145, 11);
        rsinterface.child(11, 29415, 145, 11);
        rsinterface.child(12, 29417, 164, 8);
        rsinterface.child(13, 29418, 164, 8);
        RSInterface scrollInterface = addTabInterface(29450);
        scrollInterface.scrollPosition = 0;
        scrollInterface.contentType = 0;
        scrollInterface.width = 172;
        scrollInterface.height = 197;
        scrollInterface.scrollMax = 550;
        int y = 9;
        int amountOfLines = 35;
        scrollInterface.totalChildren(amountOfLines);
        for (int i = 0; i < amountOfLines; i++) {
            addHoverText(29451 + i, "", "", tda, 0, 0xD46E08, 0xD46E08, false, true, 168);
            scrollInterface.child(i, 29451 + i, -1, y);
            y += 18;
        }
    }

    private static void achievementTab(TextDrawingArea tda[]) {
        RSInterface rsinterface = addInterface(35000);
        addSprite(35001, 63);
        addSprite(35002, 64);
        addText(35003, "Achievements", tda, 2, 0xC47423, true, true);
        addText(35004, "Completed:", tda, 2, 0xC47423, true, true);
        addHoverButton(35008, 294, 18, 18, "View Quest Tab", -1, 35009, 1);
        addHoveredButton(35009, 295, 18, 18, 35010);
        addHoverButton(35011, 296, 18, 18, "View Tool Tab", -1, 35012, 1);
        addHoveredButton(35012, 297, 18, 18, 35013);

        rsinterface.totalChildren(10);
        rsinterface.child(0, 35001, -5, 35);
        rsinterface.child(1, 35002, 0, 35);
        rsinterface.child(2, 35002, 0, 234);
        rsinterface.child(3, 35003, 55, 12);
        rsinterface.child(4, 35004, 95, 242);
        rsinterface.child(5, 35050, 0, 37);
        rsinterface.child(6, 35008, 145, 12);
        rsinterface.child(7, 35009, 145, 12);
        rsinterface.child(8, 35011, 165, 12);
        rsinterface.child(9, 35012, 165, 12);
        RSInterface scrollInterface = addTabInterface(35050);
        scrollInterface.scrollPosition = 0;
        scrollInterface.contentType = 0;
        scrollInterface.width = 175;
        scrollInterface.height = 197;
        scrollInterface.scrollMax = 250;
        int x = 7, y = 9;
        int amountOfLines = 150;
        scrollInterface.totalChildren(amountOfLines);
        for (int i = 0; i < amountOfLines; i++) {
            addHoverText(35051 + i, "", "View achievement", tda, 0, 0xFF7000, 0xFFFFFF, false, true, 168);
            scrollInterface.child(i, 35051 + i, x, y);
            y += 18;
        }
    }

    private static void activityPanel(TextDrawingArea tda[]) {
        RSInterface rsinterface = addInterface(38000);
        addSprite(38001, 249);
        addText(38002, "Activity Panel", tda, 2, 0xff9933, true, true);
        addText(38003, "", tda, 0, 0xC47423, true, true);
        addText(38004, "", tda, 0, 0xff9933, true, true);
        addText(38005, "", tda, 0, 0xff9933, true, true);
        addText(38006, "", tda, 0, 0xff9933, true, true);
        addText(38007, "", tda, 0, 0xff9933, true, true);
        addText(38008, "", tda, 0, 0xff9933, true, true);
        addText(38009, "", tda, 0, 0xff9933, true, true);
        addText(38010, "", tda, 0, 0xff9933, true, true);
        addText(38011, "", tda, 0, 0xff9933, true, true);
        addProgressBar(38015, -1, 160, 20, false);
        addContainer(38016, 0, 6, 5, 22, 21, 80, false, false, false, null, null, null, null, null);
        rsinterface.totalChildren(13);
        rsinterface.child(0, 38001, 9, 25);
        rsinterface.child(1, 38002, 93, 6);
        rsinterface.child(2, 38003, 93, 40);
        rsinterface.child(3, 38004, 93, 205);
        rsinterface.child(4, 38015, 15, 225);
        rsinterface.child(5, 38016, 75, 170);
        rsinterface.child(6, 38005, 93, 60);
        rsinterface.child(7, 38006, 93, 75);
        rsinterface.child(8, 38007, 93, 90);
        rsinterface.child(9, 38008, 93, 105);
        rsinterface.child(10, 38009, 93, 120);
        rsinterface.child(11, 38010, 93, 135);
        rsinterface.child(12, 38011, 93, 150);
    }

    private static void toolPanel(TextDrawingArea tda[]) {
        RSInterface rsinterface = addInterface(35400);
        addSprite(35401, 63);
        addSprite(35402, 64);
        addText(35403, "Tools", tda, 2, 0xC47423, true, true);
        addText(35404, "", tda, 0, 0xC47423, true, true);

        addHoverButton(35408, 294, 18, 18, "View Quest Tab", -1, 35409, 1);
        addHoveredButton(35409, 295, 18, 18, 35410);
        addHoverButton(35411, 292, 18, 18, "View Achievements Tab", -1, 35412, 1);
        addHoveredButton(35412, 293, 18, 18, 35413);

        addHoverText(35414, "www.tarnishps.com", "Visit website", tda, 3, 0xC47423, true, true, 168);

        rsinterface.totalChildren(11);
        rsinterface.child(0, 35401, -5, 35);
        rsinterface.child(1, 35402, 0, 35);
        rsinterface.child(2, 35402, 0, 234);
        rsinterface.child(3, 35403, 30, 12);
        rsinterface.child(4, 35404, 95, 238);
        rsinterface.child(5, 35450, 0, 37);
        rsinterface.child(6, 35408, 145, 12);
        rsinterface.child(7, 35409, 145, 12);
        rsinterface.child(8, 35411, 165, 12);
        rsinterface.child(9, 35412, 165, 12);
        rsinterface.child(10, 35414, 10, 237);
        RSInterface scrollInterface = addTabInterface(35450);
        scrollInterface.scrollPosition = 0;
        scrollInterface.contentType = 0;
        scrollInterface.width = 175;
        scrollInterface.height = 197;
        scrollInterface.scrollMax = 0;
        int x = 7, y = 9;
        int amountOfLines = 49;
        scrollInterface.totalChildren(amountOfLines);
        for (int i = 0; i < amountOfLines; i++) {
            addHoverText(35451 + i, "", "Click", tda, 0, 0xF0C44A, 0xFFFFFF, false, true, 168);
            scrollInterface.child(i, 35451 + i, x, y);
            y += 20;
        }
    }

    private static void firstDuel(TextDrawingArea[] tda) {
        RSInterface rsinterface = addInterface(31000);
        addSprite(31001, 330);
        addHoverButton(31002, 24, 15, 15, "Close", -1, 31003, 1);
        addHoveredButton(31003, 25, 15, 15, 31004);
        addText(31005, "Dueling with: Mod Daniel", tda, 1, 0xE1981F, false, true);
        addText(31006, "Opponent's combat level: @or2@126", tda, 1, 0xE1981F, false, true);
        addText(31007, "Your stake:", tda, 1, 0xE1981F, false, true);
        addText(31008, "Opponent's stake:", tda, 1, 0xE1981F, false, true);
        addText(31009, "@whi@Other player has accepted.", tda, 0, 0xff9933, true, true);
        addContainer(31010, 3, 7, 9, 8, false, null, null, null, null, null);
        addHoverButton(31015, 325, 73, 30, "Accept Duel", -1, 31016, 1);
        addHoveredButton(31016, 324, 73, 30, 31017);
        addHoverButton(31018, 325, 73, 30, "Decline Duel", -1, 31019, 1);
        addHoveredButton(31019, 324, 73, 30, 31020);
        addText(31021, "@gre@Accept", tda, 1, 0xff9933, false, true);
        addText(31022, "@red@Decline", tda, 1, 0xff9933, false, true);
        addConfigButton(31034, 31000, 235, 234, 15, 15, "Toggle no ranged rule", 0, 5, 631);
        addConfigButton(31035, 31000, 235, 234, 15, 15, "Toggle no melee rule", 0, 5, 632);
        addConfigButton(31036, 31000, 235, 234, 15, 15, "Toggle no magic rule", 0, 5, 633);
        addConfigButton(31037, 31000, 235, 234, 15, 15, "Toggle no special attack rule", 0, 5, 634);
        addConfigButton(31038, 31000, 235, 234, 15, 15, "Toggle fun weapons rule", 0, 5, 635);
        addConfigButton(31039, 31000, 235, 234, 15, 15, "Toggle no forfeit rule", 0, 5, 636);
        addConfigButton(31040, 31000, 235, 234, 15, 15, "Toggle no prayer rule", 0, 5, 637);
        addConfigButton(31041, 31000, 235, 234, 15, 15, "Toggle no drinks rule", 0, 5, 638);
        addConfigButton(31042, 31000, 235, 234, 15, 15, "Toggle no food rule", 0, 5, 639);
        addConfigButton(31043, 31000, 235, 234, 15, 15, "Toggle no movement rule", 0, 5, 640);
        addConfigButton(31044, 31000, 235, 234, 15, 15, "Toggle obstacles rule", 0, 5, 641);
        addText(31045, "No Ranged", tda, 1, 0xBF751D, false, true);
        addText(31046, "No Melee", tda, 1, 0xBF751D, false, true);
        addText(31047, "No Magic", tda, 1, 0xBF751D, false, true);
        addText(31048, "No Sp. Atk", tda, 1, 0xBF751D, false, true);
        addText(31049, "Fun Weapons", tda, 1, 0xBF751D, false, true);
        addText(31050, "No Forfeit", tda, 1, 0xBF751D, false, true);
        addText(31051, "No Prayer", tda, 1, 0xBF751D, false, true);
        addText(31052, "No Drinks", tda, 1, 0xBF751D, false, true);
        addText(31053, "No Food", tda, 1, 0xBF751D, false, true);
        addText(31054, "No Movement", tda, 1, 0xBF751D, false, true);
        addText(31055, "Obstacles", tda, 1, 0xBF751D, false, true);
        addConfigButton(31056, 31000, 235, 234, 15, 15, "Toggle whip & dds only rule", 0, 5, 642);
        addText(31057, "Whip/DDS Only", tda, 1, 0xBF751D, false, true);
        addConfigButton(31058, 31000, 338, 298, 155, 124, "", 0, 5, 655);
        addConfigButton(31059, 31000, 235, 234, 15, 15, "Toggle lock items", 0, 5, 643);
        addText(31060, "Lock Items", tda, 1, 0xBF751D, false, true);
        addHoverButton(31061, 339, 16, 16, "Save dueling preset", -1, 31062, 1);
        addHoveredButton(31062, 341, 16, 16, 31063);
        addText(31064, "Save", tda, 1, 0xBF751D, false, true);
        addHoverButton(31065, 340, 16, 16, "load dueling preset", -1, 31066, 1);
        addHoveredButton(31066, 342, 16, 16, 31067);
        addText(31068, "Load", tda, 1, 0xBF751D, false, true);
        rsinterface.totalChildren(51);
        rsinterface.child(0, 31001, 13, 12);
        rsinterface.child(1, 31058, 341, 47);
        rsinterface.child(2, 31002, 470, 22);
        rsinterface.child(3, 31003, 470, 22);
        rsinterface.child(4, 31005, 25, 24);
        rsinterface.child(5, 31006, 200, 24);
        rsinterface.child(6, 31007, 25, 50);
        rsinterface.child(7, 31008, 345, 50);
        rsinterface.child(8, 31009, 259, 288);
        rsinterface.child(9, 31010, 201, 46);
        rsinterface.child(10, 31011, 20, 63);
        rsinterface.child(11, 31013, 338, 63);
        rsinterface.child(12, 31015, 182, 246);
        rsinterface.child(13, 31016, 182, 246);
        rsinterface.child(14, 31018, 262, 246);
        rsinterface.child(15, 31019, 262, 246);
        rsinterface.child(16, 31021, 199, 254);
        rsinterface.child(17, 31022, 280, 254);
        rsinterface.child(18, 31034, 40, 178);
        rsinterface.child(19, 31035, 40, 196);
        rsinterface.child(20, 31036, 40, 214);
        rsinterface.child(21, 31037, 40, 232);
        rsinterface.child(22, 31038, 40, 250);
        rsinterface.child(23, 31039, 40, 268);
        rsinterface.child(24, 31040, 40, 286);
        rsinterface.child(25, 31041, 360, 178);
        rsinterface.child(26, 31042, 360, 196);
        rsinterface.child(27, 31043, 360, 214);
        rsinterface.child(28, 31044, 360, 232);
        rsinterface.child(29, 31045, 60, 178);
        rsinterface.child(30, 31046, 60, 196);
        rsinterface.child(31, 31047, 60, 214);
        rsinterface.child(32, 31048, 60, 232);
        rsinterface.child(33, 31049, 60, 250);
        rsinterface.child(34, 31050, 60, 268);
        rsinterface.child(35, 31051, 60, 286);
        rsinterface.child(36, 31052, 380, 178);
        rsinterface.child(37, 31053, 380, 196);
        rsinterface.child(38, 31054, 380, 214);
        rsinterface.child(39, 31055, 380, 232);
        rsinterface.child(40, 31056, 360, 250);
        rsinterface.child(41, 31057, 380, 250);
        rsinterface.child(42, 35730, -4, -6);
        rsinterface.child(43, 31059, 360, 268);
        rsinterface.child(44, 31060, 380, 268);
        rsinterface.child(45, 31061, 360, 288);
        rsinterface.child(46, 31062, 360, 288);
        rsinterface.child(47, 31064, 380, 289);
        rsinterface.child(48, 31065, 420, 288);
        rsinterface.child(49, 31066, 420, 288);
        rsinterface.child(50, 31068, 440, 289);
        RSInterface scrollInterface = addTabInterface(31011);
        scrollInterface.scrollPosition = 0;
        scrollInterface.contentType = 0;
        scrollInterface.width = 139;
        scrollInterface.height = 108;
        scrollInterface.scrollMax = 470;
        scrollInterface.totalChildren(1);
        addContainer(31012, 3, 10, 14, 15, false, "Withdraw 1", "Withdraw 5", "Withdraw 10", "Withdraw All", "Withdraw X");
        scrollInterface.child(0, 31012, 7, 9);

        RSInterface scrollInterface2 = addTabInterface(31013);
        scrollInterface2.scrollPosition = 0;
        scrollInterface2.contentType = 0;
        scrollInterface2.width = 139;
        scrollInterface2.height = 108;
        scrollInterface2.scrollMax = 470;
        scrollInterface2.totalChildren(1);
        addContainer(31014, 3, 10, 14, 15, false, null, null, null, null, null);
        scrollInterface2.child(0, 31014, 7, 9);

    }

    public static void thirdDuel(TextDrawingArea[] tda) {
        RSInterface rsinterface = addInterface(31700);
        addSprite(31701, 329);
        addHoverButton(31702, 24, 15, 15, "Close", -1, 31703, 1);
        addHoveredButton(31703, 25, 15, 15, 31704);
        addText(31705, "You are victorious!", tda, 2, 0xE1981F, false, true);
        addText(31706, "Goat", tda, 1, 0xA8A8A8, false, true);
        addText(31707, "5", tda, 1, 0xA8A8A8, false, true);
        addContainer(31708, 5, 6, 9, 6, false, null, null, null, null, null);
        addText(31709, "<col=E1981F>Total Value: 726,432,531", tda, 0, 0xE1981F, false, true);

        addHoverButton(31710, 336, 102, 40, "Claim", -1, 31711, 1);
        addHoveredButton(31711, 337, 102, 40, 31712);
        addText(31713, "@gre@Claim!", tda, 1, 0xE1981F, false, true);

        rsinterface.totalChildren(11);
        rsinterface.child(0, 31701, 12, 14);
        rsinterface.child(1, 31702, 471, 22);
        rsinterface.child(2, 31703, 471, 22);
        rsinterface.child(3, 31705, 190, 24);
        rsinterface.child(4, 31706, 360, 90);
        rsinterface.child(5, 31707, 400, 115);
        rsinterface.child(6, 31708, 105, 85);
        rsinterface.child(7, 31709, 25, 26);
        rsinterface.child(8, 31710, 345, 259);
        rsinterface.child(9, 31711, 345, 259);
        rsinterface.child(10, 31713, 378, 270);
    }

    public static void secondDuel(TextDrawingArea[] tda) {
        RSInterface rsinterface = addInterface(31500);
        addSprite(31501, 331);
        addHoverButton(31502, 24, 15, 15, "Close", -1, 31503, 1);
        addHoveredButton(31503, 25, 15, 15, 31504);
        addText(31505, "", tda, 0, 0xA8A8A8, false, true);
        addText(31506, "", tda, 0, 0xA8A8A8, false, true);
        addText(31507, "", tda, 0, 0xA8A8A8, false, true);
        addText(31508, "", tda, 0, 0xA8A8A8, false, true);
        addText(31509, "", tda, 0, 0xA8A8A8, false, true);
        addText(31510, "", tda, 0, 0xA8A8A8, false, true);
        addText(31511, "", tda, 0, 0xA8A8A8, false, true);
        addText(31512, "", tda, 0, 0xA8A8A8, false, true);
        addText(31513, "", tda, 0, 0xA8A8A8, false, true);
        addText(31514, "", tda, 0, 0xA8A8A8, false, true);
        addText(31515, "", tda, 0, 0xA8A8A8, false, true);
        addText(31516, "", tda, 0, 0xA8A8A8, false, true);
        addText(31517, "", tda, 0, 0xA8A8A8, false, true);
        addText(31518, "", tda, 0, 0xA8A8A8, false, true);
        addText(31519, "", tda, 0, 0xA8A8A8, false, true);
        addHoverButton(31520, 332, 60, 16, "Accept Duel", -1, 31521, 1);
        addHoveredButton(31521, 334, 60, 16, 31522);
        addHoverButton(31523, 333, 60, 16, "Decline Duel", -1, 31524, 1);
        addHoveredButton(31524, 335, 60, 16, 31525);
        addText(31526, "Other player has accepted the duel.", tda, 0, 0xA8A8A8, true, true);

        rsinterface.totalChildren(25);
        rsinterface.child(0, 31501, 12, 14);
        rsinterface.child(1, 31502, 471, 22);
        rsinterface.child(2, 31503, 471, 22);
        rsinterface.child(3, 31505, 260, 65);
        rsinterface.child(4, 31506, 260, 77);
        rsinterface.child(5, 31507, 260, 89);
        rsinterface.child(6, 31508, 260, 101);
        rsinterface.child(7, 31509, 260, 140);
        rsinterface.child(8, 31510, 260, 152);
        rsinterface.child(9, 31511, 260, 164);
        rsinterface.child(10, 31512, 260, 176);
        rsinterface.child(11, 31513, 260, 188);
        rsinterface.child(12, 31514, 260, 200);
        rsinterface.child(13, 31515, 260, 212);
        rsinterface.child(14, 31516, 260, 224);
        rsinterface.child(15, 31517, 260, 236);
        rsinterface.child(16, 31518, 260, 248);
        rsinterface.child(17, 31519, 260, 260);
        rsinterface.child(18, 31520, 305, 281);
        rsinterface.child(19, 31521, 305, 281);
        rsinterface.child(20, 31523, 374, 281);
        rsinterface.child(21, 31524, 374, 281);
        rsinterface.child(22, 31526, 370, 298);
        rsinterface.child(23, 31530, 30, 60);
        rsinterface.child(24, 31560, 30, 198);

        RSInterface scrollInterface = addTabInterface(31530);
        scrollInterface.scrollPosition = 0;
        scrollInterface.contentType = 0;
        scrollInterface.width = 197;
        scrollInterface.height = 115;
        scrollInterface.scrollMax = 515;
        int x = 7, y = 9;
        int amountOfLines = 28;
        scrollInterface.totalChildren(amountOfLines);
        for (int i = 0; i < amountOfLines; i++) {
            addHoverText(31531 + i, "</col>Coins @whi@itemX 10M (10,000,000)", "", tda, 0, 0xFE8E42, false, true, 168);
            scrollInterface.child(i, 31531 + i, x, y);
            y += 18;
        }

        RSInterface scrollInterface2 = addTabInterface(31560);
        scrollInterface2.scrollPosition = 0;
        scrollInterface2.contentType = 0;
        scrollInterface2.width = 197;
        scrollInterface2.height = 115;
        scrollInterface2.scrollMax = 515;
        int x2 = 7, y2 = 9;
        int amountOfLines2 = 28;
        scrollInterface2.totalChildren(amountOfLines2);
        for (int i = 0; i < amountOfLines2; i++) {
            addHoverText(31561 + i, "</col>Coins @whi@itemX 10M (10,000,000)", "", tda, 0, 0xFE8E42, false, true, 168);
            scrollInterface2.child(i, 31561 + i, x2, y2);
            y2 += 18;
        }

    }

    public static void duelEquipmentSlots(TextDrawingArea[] wid) {
        RSInterface rsi = interfaceCache[35730];
        rsi = addInterface(35730);
        rsi.totalChildren(23);
        rsi.child(0, RSInterface.interfaceCache[6575].children[167], RSInterface.interfaceCache[6575].childX[167], RSInterface.interfaceCache[6575].childY[167]); // helm
        rsi.child(1, RSInterface.interfaceCache[6575].children[168], RSInterface.interfaceCache[6575].childX[168], RSInterface.interfaceCache[6575].childY[168]); // cape
        rsi.child(2, RSInterface.interfaceCache[6575].children[169], RSInterface.interfaceCache[6575].childX[169], RSInterface.interfaceCache[6575].childY[169]); // amulet
        rsi.child(3, RSInterface.interfaceCache[6575].children[170], RSInterface.interfaceCache[6575].childX[170], RSInterface.interfaceCache[6575].childY[170]); // arrows
        rsi.child(4, RSInterface.interfaceCache[6575].children[171], RSInterface.interfaceCache[6575].childX[171], RSInterface.interfaceCache[6575].childY[171]); // weapon
        rsi.child(5, RSInterface.interfaceCache[6575].children[172], RSInterface.interfaceCache[6575].childX[172], RSInterface.interfaceCache[6575].childY[172]); // body
        rsi.child(6, RSInterface.interfaceCache[6575].children[173], RSInterface.interfaceCache[6575].childX[173], RSInterface.interfaceCache[6575].childY[173]); // shield
        rsi.child(7, RSInterface.interfaceCache[6575].children[174], RSInterface.interfaceCache[6575].childX[174], RSInterface.interfaceCache[6575].childY[174]); // legs
        rsi.child(8, RSInterface.interfaceCache[6575].children[175], RSInterface.interfaceCache[6575].childX[175], RSInterface.interfaceCache[6575].childY[175]); // ring
        rsi.child(9, RSInterface.interfaceCache[6575].children[176], RSInterface.interfaceCache[6575].childX[176], RSInterface.interfaceCache[6575].childY[176]); // boot
        rsi.child(10, RSInterface.interfaceCache[6575].children[177], 189, 212);
        rsi.child(11, RSInterface.interfaceCache[6575].children[178], 190, 94);
        rsi.child(12, RSInterface.interfaceCache[6575].children[179], 246, 56);
        rsi.child(13, RSInterface.interfaceCache[6575].children[180], 205, 96);
        rsi.child(14, RSInterface.interfaceCache[6575].children[181], 247, 95);
        rsi.child(15, RSInterface.interfaceCache[6575].children[182], 288, 96);
        rsi.child(16, RSInterface.interfaceCache[6575].children[183], 191, 135);
        rsi.child(17, RSInterface.interfaceCache[6575].children[184], 246, 135);
        rsi.child(18, RSInterface.interfaceCache[6575].children[185], 301, 134);
        rsi.child(19, RSInterface.interfaceCache[6575].children[186], 246, 174);
        rsi.child(20, RSInterface.interfaceCache[6575].children[187], 190, 215);
        rsi.child(21, RSInterface.interfaceCache[6575].children[188], 245, 214);
        rsi.child(22, RSInterface.interfaceCache[6575].children[189], 302, 215);
    }
}
