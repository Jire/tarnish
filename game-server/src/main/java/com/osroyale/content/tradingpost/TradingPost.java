package com.osroyale.content.tradingpost;

import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.hankcs.algorithm.AhoCorasickDoubleArrayTrie;
import com.osroyale.game.task.Task;
import com.osroyale.game.world.World;
import com.osroyale.game.world.entity.mob.player.Player;
import com.osroyale.game.world.items.Item;
import com.osroyale.game.world.items.ItemDefinition;
import com.osroyale.net.packet.out.*;
import com.osroyale.util.GsonUtils;
import com.osroyale.util.Utility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import java.util.stream.Collectors;

//@Todo history, tax
public class TradingPost  {

    private static final Logger logger = LoggerFactory.getLogger(TradingPost.class);

    private static final int OVERVIEW_INTERFACE_ID = 80000;
    public static final int BUYING_PAGE_INTERFACE_ID = 80174;
    private static final int HISTORY_PAGE_INTERFACE_ID = 80616;
    private static final int MAX_LISTINGS_SHOWN = 50;
    private static final int MAX_LISTING_SIZE = 20;
    private static final int CURRENCY_ID = 995;
    private static final int TAX_RATE = 10;

    private final AhoCorasickDoubleArrayTrie<String> acdat = new AhoCorasickDoubleArrayTrie<>();

    /**
     * A list containing all the trading post listings
     */
    private static final List<TradingPostListing> allListings = new ArrayList<>();

    /**
     * Hashmap containing the coffers <PlayerName, Coffer>
     */
    private static final Map<String, Coffer> allCoffers = Collections.synchronizedMap(new HashMap<>());

    /**
     * List containing the players existing listings/offers
     */
    private List<TradingPostListing> myListings = new ArrayList<>();

    /**
     * The listing that the player will add to their existing listings
     */
    private TradingPostListing listingToAdd;

    /**
     * The listing that the player selects in the buying page
     */
    private TradingPostListing listingToBuy;

    /**
     * The current item or player search
     */
    private TradingPostActiveListingSearch tradingPostSearch;

    /**
     * The current sort of the buyable listings
     */
    private TradingPostActiveListingSort tradingPostSort;

    /**
     * The current searched listings
     */
    private List<TradingPostListing> searchedListings = new ArrayList<>();

    /**
     * The displayed listings on the buying page
     */
    private List<TradingPostListing> displayedPageListings = new ArrayList<>();

    /**
     * Contains a list of the last 50 sold of each item
     */
    private static final Map<String, List<ItemHistory>> itemHistories = new HashMap<>();

    /**
     * contains the last 50 sold items on the trading post
     */
    private static final List<ItemHistory> recentlySoldItems = new ArrayList<>();

    /**
     * contains last 50 players buying history
     */
    private final List<ItemHistory> playersItemHistory = new ArrayList<>();

    /**
     * The page # of the buying interface
     */
    private int page;

    private final Player player;

    public TradingPost(Player player) {
        this.player = player;
    }

    public void openOverviewInterface() {
        cleanUp();
        updateExistingListingsList();
        updateExistingListingsWidgets();
        updateCofferAmountWidget();
        toggleSellingOverlayVisibilityAndOpen(true);
        player.interfaceManager.open(OVERVIEW_INTERFACE_ID);
    }

    public void updateCofferAmountWidget() {
        getOrMakeNewCofferIfNotExists(player.getName(), coffer -> {
            player.send(new SendString(Utility.formatDigits(coffer.getAmount()), 80005));
        });
    }

    public boolean handleButtonClick(int btnId) {
        if(!player.interfaceManager.hasAnyOpen(OVERVIEW_INTERFACE_ID, BUYING_PAGE_INTERFACE_ID,HISTORY_PAGE_INTERFACE_ID)) {
            return false;
        }
        
        switch (btnId) {
            case 14650:
            case 15418:
                openOverviewInterface();
                return true;
            case 15091:
            case 14597:
                searchItemHistoryInput();
                return true;
            case 14600:
                searchPlayerInput();
                return true;
            case 14603:
                displayMostRecentListings();
                return true;
            case 14662:
                player.dialogueFactory.sendOption("Search item", this::searchExistingItemInput, "Search player", this::searchPlayerInput, "Nevermind", () -> player.dialogueFactory.clear()).execute();
                return true;
            case 14474:
                searchExistingItemInput();
                return true;
            case 14477:
                openSellingOverlay();
                return true;
            case 14484:
                sendItemHistoryData(playersItemHistory);
                return true;
            case 14487:
                sendItemHistoryData(recentlySoldItems);
                return true;
            case 14614:
                toggleSellingOverlayVisibilityAndOpen(true);
                return true;
            case 14659:
                nextPage();
                return true;
            case 14653:
                previousPage();
                return true;
            case 14656:
                refresh();
                return true;
            case 14466:
                withdrawFromCoffer();
                return true;
            case 14649:
                sortPrice();
                return true;
            case 14648:
                sortQuantity();
                return true;
            default: {
                /*
                 * Returns true or false based on button ids
                 */
                if(handleBuyingButton(btnId) || handleDismissListingButton(btnId) || handleSellingOverlayButtons(btnId)) {
                    return true;
                }
            }
        }

        return false;
    }

    private void searchItemHistoryInput() {
        player.send(new SendInputMessage("Enter item name to search history for:", 16, input -> {

            if(Objects.equals(input, "")) {
                sendItemHistoryData(recentlySoldItems);
                return;
            }

            List<ItemHistory> historyItems = new ArrayList<>();
            TreeMap<String, String> map = new TreeMap<>();
            map.put(input,input);
            acdat.build(map);

            for(Map.Entry<String, List<ItemHistory>> lf : itemHistories.entrySet()) {
                acdat.parseText(lf.getKey().toLowerCase(), (b,e,v) -> {
                    List<ItemHistory> temp = lf.getValue();

                    for(ItemHistory h : temp) {
                        if(!historyItems.contains(h)) {
                            historyItems.add(h);
                        }

                        if(historyItems.size() == 50) {
                            break;
                        }

                    }
                });
            }

            sendItemHistoryData(historyItems);
        }));
    }

    public void sendItemHistoryData(List<ItemHistory> itemHistories) {
            for(int i = 0; i < MAX_LISTINGS_SHOWN; i++) {
                if(itemHistories.size() > i) {
                    ItemHistory ih = itemHistories.get(i);
                    player.send(new SendString(ItemDefinition.get(ih.getItemId()).getName(),80753+i));
                    player.send(new SendString(Utility.formatDigits(ih.getPrice()),80803+i));
                    player.send(new SendString(ih.getSeller(),80853+i));
                    player.send(new SendString(ih.getBuyer(),80903+i));
                } else {
                    player.send(new SendString("",80753+i));
                    player.send(new SendString("",80803+i));
                    player.send(new SendString("",80853+i));
                    player.send(new SendString("",80903+i));
                }
            }

        player.send(new SendItemOnInterface(80953, getItemArrayFromItemHistory(itemHistories)));
        player.interfaceManager.open(HISTORY_PAGE_INTERFACE_ID);
    }

    private void searchExistingItemInput() {
        World.schedule(new Task(1) { // task added so input prompt shows after dialogue
            @Override
            public void execute() {
                player.send(new SendInputMessage("Enter item name to search for:", 16, input -> {
                    searchExistingListing(new TradingPostActiveListingSearch(TradingPostActiveListingSearchType.ITEM, String.valueOf(input)));
                    player.interfaceManager.open(BUYING_PAGE_INTERFACE_ID);
                }));
                this.cancel();
            }
        });
    }

    private void searchPlayerInput() {
        World.schedule(new Task(1) { // task added so input prompt shows after dialogue
            @Override
            public void execute() {
                player.send(new SendInputMessage("Enter player name to search for:", 16, input -> {
                    searchExistingListing(new TradingPostActiveListingSearch(TradingPostActiveListingSearchType.PLAYER, String.valueOf(input)));
                    player.interfaceManager.open(BUYING_PAGE_INTERFACE_ID);
                }));
                this.cancel();
            }
        });
    }

    private void sort() {
        if(displayedPageListings == null) return;
        if(tradingPostSort == null) return;

        page = 0;

        searchedListings = searchedListings
                .stream()
                .sorted(Comparator.comparingInt(
                                (tradingPostSort.postSortType == TradingPostSortType.HIGHEST_PRICE ||  tradingPostSort.postSortType == TradingPostSortType.LOWEST_PRICE)
                                ? TradingPostListing::getPrice
                                : TradingPostListing::getAmountLeft))
                .collect(Collectors.toList());


        if(tradingPostSort.postSortType == TradingPostSortType.HIGHEST_QUANTITY || tradingPostSort.postSortType == TradingPostSortType.HIGHEST_PRICE) {
            Collections.reverse(searchedListings);
        }

        updateBuyingPageWidgets(getPageListings(searchedListings));
    }

    private void sortPrice() {
        tradingPostSort = tradingPostSort != null ? tradingPostSort.setPostSortType(
                tradingPostSort.postSortType == TradingPostSortType.HIGHEST_PRICE
                        ? TradingPostSortType.LOWEST_PRICE
                        : TradingPostSortType.HIGHEST_PRICE
        ) : new TradingPostActiveListingSort(TradingPostSortType.LOWEST_PRICE);

        sort();
    }

    public void sortQuantity() {
        tradingPostSort = tradingPostSort != null ? tradingPostSort.setPostSortType(
                tradingPostSort.postSortType == TradingPostSortType.HIGHEST_QUANTITY
                        ? TradingPostSortType.LOWEST_QUANTITY
                        : TradingPostSortType.HIGHEST_QUANTITY
        ) : new TradingPostActiveListingSort(TradingPostSortType.HIGHEST_QUANTITY);

        sort();
    }


    private void withdrawFromCoffer() {
        getOrMakeNewCofferIfNotExists(player.getName(), coffer -> {
            if (coffer.getAmount() == 0) {
                return;
            }

            player.send(new SendInputAmount("How much gold would you like to withdraw?", 10, input -> {
                int amount = Integer.parseInt(input);

                if (amount > coffer.getAmount()) {
                    amount = (int) coffer.getAmount();
                }

                if (amount > coffer.getAmount()) {
                    return;
                }

                if (player.inventory.add(CURRENCY_ID, amount)) {
                    coffer.subtractAmount(amount);
                    updateCofferAmountWidget();
                    saveCoffer(coffer);
                } else {
                    player.send(new SendMessage("You do not have enough inventory spaces to withdraw this gold"));
                }

            }));
        });
    }

    private void refresh() {
        if(tradingPostSearch != null) {
            addSearchResultsAndUpdateInterface(tradingPostSearch);
        } else {
            displayMostRecentListings();
        }
    }

    private void nextPage() {
        page++;
        List<TradingPostListing> temp;
        temp = getPageListings(searchedListings);
        if(temp.isEmpty()) {
            page--;
            player.send(new SendMessage("No more results on the next page"));
        } else {
            updateBuyingPageWidgets(temp);
        }
    }

    private void previousPage() {
        if(page == 0) return;
        page--;
        updateBuyingPageWidgets(getPageListings(searchedListings));
    }

    private boolean handleSellingOverlayButtons(int btnId) {
        if(listingToAdd == null) return false;

        switch (btnId) {
            case 14618:
                listingToAdd.addQuantity(player,1);
                return true;
            case 14621:
                listingToAdd.removeQuantity(player,1);
                return true;
            case 14636:
                editQuantityInputPrompt();
                return true;
            case 14628:
                editPriceInputPrompt();
                return true;
            case 14631:
                confirmToAddListing();
                return true;
        }

        return false;
    }

    private void displayMostRecentListings() {
        if(!player.interfaceManager.isInterfaceOpen(BUYING_PAGE_INTERFACE_ID)) {
            player.interfaceManager.open(BUYING_PAGE_INTERFACE_ID);
        }
        player.send(new SendString("Search: Most recent",80176));

        tradingPostSearch = new TradingPostActiveListingSearch(TradingPostActiveListingSearchType.MOST_RECENT, "");
        addSearchResultsAndUpdateInterface(tradingPostSearch);
    }

    private void searchExistingListing(TradingPostActiveListingSearch search) {
        if(search.text.length() == 0) {
            cleanUp();
            displayMostRecentListings();
            return;
        }
        tradingPostSearch = search;
        this.page = 0;
        addSearchResultsAndUpdateInterface(search);
        player.send(new SendString("Search: " + tradingPostSearch.text,80176));
    }

    public void addSearchResultsAndUpdateInterface(TradingPostActiveListingSearch search) {
        searchedListings.clear();
        searchedListings.addAll(getSearchResults(search.searchType,search.text));
        updateBuyingPageWidgets(getPageListings(searchedListings));
    }

    private List<TradingPostListing> getSearchResults(TradingPostActiveListingSearchType type, String search) {
        if(type == TradingPostActiveListingSearchType.MOST_RECENT) {
            return Lists.reverse(allListings);
        }
        List<TradingPostListing> temp = new ArrayList<>();
        TreeMap<String, String> map = new TreeMap<>();
        map.put(search,search);
        tradingPostSearch.acdat.build(map);
        for(TradingPostListing listing : allListings) {
            tradingPostSearch.acdat.parseText(type == TradingPostActiveListingSearchType.ITEM ? ItemDefinition.get(listing.getItemId()).getName().toLowerCase() : listing.getSeller().toLowerCase(), (begin, end, value) -> {
                if(!temp.contains(listing)) {
                    temp.add(listing);
                }
            });
        }
        return temp;
    }

    private List<TradingPostListing> getPageListings(List<TradingPostListing> listings) {
        return listings.subList(
                        Math.min(listings.size(), page * MAX_LISTINGS_SHOWN),
                        Math.min(listings.size(), (page * MAX_LISTINGS_SHOWN + MAX_LISTINGS_SHOWN))).
                stream()
                .filter(tradingPostListing -> !tradingPostListing.getSeller().equalsIgnoreCase(player.getName()))
                .limit(MAX_LISTINGS_SHOWN)
                .collect(Collectors.toList());
    }

    private void updateBuyingPageWidgets(List<TradingPostListing> listings) {
         displayedPageListings = listings;

         for(int i = 0; i < MAX_LISTINGS_SHOWN; i++) {
             if(displayedPageListings.size() > i) {
                TradingPostListing listing = displayedPageListings.get(i);
                 player.send(new SendString(ItemDefinition.get(listing.getItemId()).getName(),80214+i));
                 player.send(new SendString(Utility.formatDigits(listing.getAmountLeft()*listing.getPrice()),80264+i));
                 player.send(new SendString("= " + Utility.formatDigits(listing.getPrice()) + " (ea)",80314+i));
                 player.send(new SendString(listing.getSeller(),80364+i));
                 toggleBuyingPageWidgetVisibility(i,false);
             } else {
                 toggleBuyingPageWidgetVisibility(i, true);
             }
         }

        player.send(new SendString("Page: " + (page+1),80615));
        player.send(new SendItemOnInterface(80614, getItemArrayFromActiveListings(displayedPageListings)));
        player.send(new SendScrollbar(80211, Math.max(238, displayedPageListings.size()*34)));
    }

    private void toggleBuyingPageWidgetVisibility(int i, boolean isHidden) {
        player.send(new SendInterfaceVisibility(80214+i, isHidden));
        player.send(new SendInterfaceVisibility(80264+i, isHidden));
        player.send(new SendInterfaceVisibility(80314+i, isHidden));
        player.send(new SendInterfaceVisibility(80364+i, isHidden));
        player.send(new SendInterfaceVisibility(80414+i, isHidden));
        player.send(new SendInterfaceVisibility(80464+i, isHidden));
        player.send(new SendInterfaceVisibility(80564+i, isHidden));
    }

    private void toggleSellingOverlayVisibilityAndOpen(boolean isHidden) {
        if(isHidden) {
            player.interfaceManager.close();
        }
        player.interfaceManager.open(OVERVIEW_INTERFACE_ID);
        player.send(new SendInterfaceVisibility(80146, isHidden));
    }

    private int getSlotSizeByDonatorRank() {
        switch (player.right) {
            case KING_DONATOR -> {
                return MAX_LISTING_SIZE;
            }
            case ELITE_DONATOR -> {
                return 18;

            }
            case EXTREME_DONATOR -> {
                return 15;
            }
            case SUPER_DONATOR -> {
                return 12;
            }
            case DONATOR -> {
                return 10;
            }
            default -> {
                return 5;
            }
        }
    }

    private void openSellingOverlay() {
        clearSellingOverlay();
        toggleSellingOverlayVisibilityAndOpen(false);
        listingToAdd = null;
        player.send(new SendInventoryInterface(OVERVIEW_INTERFACE_ID, 65300));
        player.send(new SendItemOnInterface(65301, player.inventory.toArray()));
    }

    public void selectItemToList(Item item) {
        if(!item.isTradeable() || item.getId() == 995) {
            player.send(new SendMessage("You cannot list this item."));
            return;
        }
        listingToAdd = new TradingPostListing(item.getId(), player.getName());
        player.send(new SendItemOnInterface(80153,item));
        player.send(new SendString(item.getDefinition().getName(),80160));
        updatePriceStrings();
        updateQuantityString();
    }

    public void updatePriceStrings() {
        if(listingToAdd == null) return;
        player.send(new SendString("Price: @gre@" + Utility.formatDigits(listingToAdd.getPrice()), 80161));
        player.send(new SendString("Total: @gre@" + Utility.formatDigits(listingToAdd.getPrice()*listingToAdd.getInitialQuantity()), 80163));
    }

    public void updateQuantityString() {
        if(listingToAdd == null) return;
        player.send(new SendString("Quantity: @gre@" + listingToAdd.getInitialQuantity(), 80162));
        player.send(new SendString(listingToAdd.getInitialQuantity(), 80173));
    }

    private void clearSellingOverlay() {
        player.send(new SendItemOnInterface(80153, new Item(-1,0)));
        player.send(new SendString("",80160));
        player.send(new SendString("",80161));
        player.send(new SendString("",80162));
        player.send(new SendString("",80163));
        player.send(new SendString("",80173));
    }

    private void editQuantityInputPrompt() {
        player.send(new SendInputAmount("How many would you like to sell?", 10, input -> listingToAdd.setQuantity(player,Integer.parseInt(input))));
    }

    private void editPriceInputPrompt() {
        player.send(new SendInputAmount("How much would you like sell this for?", 10, input -> listingToAdd.setPrice(player, Integer.parseInt(input))));
    }

    private void confirmToAddListing() {
        if(listingToAdd.getPrice() == 0) {
            player.send(new SendMessage("Unable to add listing with a price of zero."));
            return;
        }
        if(myListings.size() > getSlotSizeByDonatorRank()) {
            player.send(new SendMessage("Unable to add listing with max number listings listed."));
            return;
        }

        if(player.inventory.remove(listingToAdd.getItemId(), listingToAdd.getInitialQuantity())) {
            allListings.add(listingToAdd);
            updateExistingListingsList();
            openSellingOverlay();
            updateExistingListingsWidgets();
            saveListings(player.getName(),getListingsByName(player.getName()));
        } else {
            player.send(new SendMessage("Unable to list this item. Please try again."));
        }
    }

    private void updateExistingListingsList() {
        myListings = getListingsByName(player.getName());
    }

    private List<TradingPostListing> getListingsByName(String name) {
        return allListings
                .stream()
                .filter(tradingPostListing -> tradingPostListing.getSeller().equals(name))
                .collect(Collectors.toList());
    }

    private Item[] getItemArrayFromActiveListings(List<TradingPostListing> list) {
        return list
                .stream()
                .map(tradingPostListing -> new Item(ItemDefinition.get(tradingPostListing.getItemId()).isNoted()
                        ?  ItemDefinition.get(tradingPostListing.getItemId()).getUnnotedId()
                        : tradingPostListing.getItemId(), tradingPostListing.getAmountLeft()))
                .toArray(Item[]::new);
    }

    private Item[] getItemArrayFromItemHistory(List<ItemHistory> list) {
        return list
                .stream()
                .map(ih -> new Item(ItemDefinition.get(ih.getItemId()).isNoted()
                        ?  ItemDefinition.get(ih.getItemId()).getUnnotedId()
                        : ih.getItemId(), ih.getQuantity()))
                .toArray(Item[]::new);
    }

    private void updateExistingListingsWidgets() {
        player.send(new SendItemOnInterface(80132, getItemArrayFromActiveListings(myListings)));

        for(int i = 0; i < MAX_LISTING_SIZE; i++) {
            if(myListings.size() > i) {
                TradingPostListing tradingPostListing = myListings.get(i);
                sendOverviewWidgetVisibility(i,false);
                player.send(new SendString(tradingPostListing.getAmountSold() + "/" + tradingPostListing.getInitialQuantity(), 80112+i));
                player.send(new SendProgressBar(80032+i,  ((tradingPostListing.getAmountSold() * 100) / tradingPostListing.getInitialQuantity())));
            } else {
                sendOverviewWidgetVisibility(i,true);
            }
        }

        player.send(new SendScrollbar(80029, Math.max(38*myListings.size(), 157)));
    }

    private void sendOverviewWidgetVisibility(int i, boolean isHidden) {
        player.send(new SendInterfaceVisibility(80032+i,isHidden));
        player.send(new SendInterfaceVisibility(80052+i,isHidden));
        player.send(new SendInterfaceVisibility(80072+i,isHidden));
        player.send(new SendInterfaceVisibility(80112+i,isHidden));
    }


    private boolean handleBuyingButton(int btnId) {
        if(btnId >= 14878 && btnId <= 14927) {
            int index = btnId - 14878;

            if(displayedPageListings.size() < index) {
                return true;
            }

            TradingPostListing temp = displayedPageListings.get(index);

            if(temp == null) {
                return true;
            }

            if(!allListings.contains(temp)) {
                return true;
            }

            listingToBuy = temp;

            if(listingToBuy.getAmountLeft() > 1) {
                player.send(new SendInputAmount("How many of " + ItemDefinition.get(listingToBuy.getItemId()).getName() + "?", 10, input -> buyingDialogueOptions(Integer.parseInt(input))));
            } else {
                buyingDialogueOptions(1);
            }
        }

        return false;
    }

    public void buyingDialogueOptions(int amount) {
        if((long)amount + listingToBuy.getPrice() > Integer.MAX_VALUE) {
            player.send(new SendMessage("Cannot buy this quantity for this price."));
            return;
        }

        if(amount > listingToBuy.getAmountLeft()) {
            amount = listingToBuy.getAmountLeft();
        }

        int finalAmount = amount;
        player.dialogueFactory.sendStatement("Purchase " + amount + " of " + ItemDefinition.get(listingToBuy.getItemId()).getName(),
                        "for <col=A52929>" + Utility.formatDigits(listingToBuy.getPrice()*amount) + " " + ItemDefinition.get(CURRENCY_ID).getName() + "?")
                .sendOption("Yes", () -> purchase(finalAmount), "Nevermind", () -> player.dialogueFactory.clear()).execute();
    }

    private void alertSeller(String sellerName, String item, int amount) {
        Optional<Player> playerOptional = World.search(sellerName);

        if(playerOptional.isPresent()) {
            Player player = playerOptional.get();

            player.send(new SendMessage("@red@Trading post: " + amount + " of your " + item + "s have been sold"));

            player.tradingPost.updateExistingListingsList();

            if(player.interfaceManager.hasAnyOpen(OVERVIEW_INTERFACE_ID)) {
                player.tradingPost.openOverviewInterface();
            }
        }
    }

    private void purchase(int amount) {
        if(listingToBuy == null) {
            return;
        }

        int getPlayerCurrencyAmount = player.inventory.computeAmountForId(CURRENCY_ID);
        int totalPrice = listingToBuy.getPrice()*amount;

        if(totalPrice > getPlayerCurrencyAmount) {
            player.send(new SendMessage("Insufficient " + ItemDefinition.get(CURRENCY_ID).getName() + " to complete your transaction."));
            return;
        }

        if(amount > listingToBuy.getAmountLeft()) {
            amount = listingToBuy.getAmountLeft();
            player.send(new SendMessage("Your purchase amount has been lowered to " + amount));
        }

        if(!allListings.contains(listingToBuy)) {
            player.send(new SendMessage("This item does not exist in the trading post anymore."));
            return;
        }

        if(player.inventory.add(listingToBuy.getItemId(), amount)) {
            player.inventory.remove(CURRENCY_ID,totalPrice);
            listingToBuy.addToAmountSold(amount);

            if(listingToBuy.getAmountLeft() == 0) {
                allListings.remove(listingToBuy);
                displayedPageListings.remove(listingToBuy);
                searchedListings.remove(listingToBuy);
            }

            if(tradingPostSearch != null) {
                addSearchResultsAndUpdateInterface(tradingPostSearch);
            } else {
                displayMostRecentListings();
            }

            addToCoffer(listingToBuy.getSeller(),listingToBuy.getPrice()*amount);
            alertSeller(listingToBuy.getSeller(),ItemDefinition.get(listingToBuy.getItemId()).getName(), amount);

            String seller = listingToBuy.getSeller();
            saveListings(seller,getListingsByName(seller));

            addToItemHistory(new ItemHistory(amount, listingToBuy.getItemId(), listingToBuy.getPrice()*amount, listingToBuy.getSeller(), player.getName()));

        } else {
            player.send(new SendMessage("You do not have enough inventory spaces to complete this transaction"));
        }

    }

    public void addToItemHistory(ItemHistory itemHistory) {
        List<ItemHistory> itemHistoryList = itemHistories.computeIfAbsent(ItemDefinition.get(itemHistory.getItemId()).getName(), x -> new ArrayList<>());

        if(itemHistoryList.size() == 50) {
            itemHistoryList.remove(49);
        }

        itemHistoryList.add(itemHistory);

        if(recentlySoldItems.size() == 50) {
            recentlySoldItems.remove(49);
        }

        recentlySoldItems.add(itemHistory);

        if(playersItemHistory.size() == 50) {
            playersItemHistory.remove(49);
        }

        playersItemHistory.add(itemHistory);
    }

    public void addToCoffer(String owner, int amount) {
        getOrMakeNewCofferIfNotExists(owner, coffer -> {
            coffer.addAmount((int) (amount - (TAX_RATE/100.0*amount)));
            saveCoffer(coffer);
        });
    }

    public void getOrMakeNewCofferIfNotExists(final String owner,
                                              final Consumer<Coffer> useCoffer) {
        getCoffer(owner).thenAccept(coffer -> {
            if (coffer == null) {
                coffer = allCoffers.compute(owner, (k, v) -> new Coffer(owner));
                saveCoffer(coffer);
            }

            useCoffer.accept(coffer);
        });
    }

    private boolean handleDismissListingButton(int btnId) {
        if(btnId >= 14516 && btnId <= 14535) {
            int index = btnId - 14516;

            if(myListings.isEmpty()) {
                return true;
            }

            if(myListings.size() < index) {
                return true;
            }

            TradingPostListing listing = myListings.get(index);

            if(!delist(listing)) {
                player.send(new SendMessage("Unable to delist this item. Please try again."));
            } else {
                saveListings(player.getName(),getListingsByName(player.getName()));
                player.inventory.addOrBank(new Item(listing.getItemId(),listing.getAmountLeft()));
                openOverviewInterface();
            }

            return true;
        }

        return false;
    }

    public boolean delist(TradingPostListing listing) {
        if(myListings.isEmpty()) {
            return false;
        }

        if(!myListings.contains(listing)) {
            return false;
        }

        if(!allListings.contains(listing)) {
            return false;
        }

        return allListings.remove(listing) && myListings.remove(listing);
    }

    public void cleanUp() {
        displayedPageListings.clear();
        searchedListings.clear();
        tradingPostSearch = null;
        tradingPostSort = null;
        listingToAdd = null;
        listingToBuy = null;
        page = 0;
    }

    public void testData() {
        int addCounter = 0;
        for(int i = 0; i < 1000; i++) {
            ItemDefinition def = ItemDefinition.DEFINITIONS[new Random().nextInt(ItemDefinition.DEFINITIONS.length)];
            if(def != null) {
                TradingPostListing listing = new TradingPostListing(def.getId(), "John"+i);
                listing.setInitialQuantity(i+1);
                listing.setPrice(i+1);
                allListings.add(listing);
                addCounter++;
                System.out.println("Adding listing: " + addCounter);
            }
        }
    }

    public void saveCoffer(final Coffer coffer) {
        Thread.startVirtualThread(() -> {
            try {
                final String json = GsonUtils.JSON_PRETTY_ALLOW_NULL.get().toJson(coffer);

                final Path path = Path.of("./data/profile/save/tradingpost/coffers/", coffer.getOwner(), ".json");
                Path parent = path.getParent();
                if (parent == null) {
                    throw new UnsupportedOperationException("Path must have a parent " + path);
                }
                if (!Files.exists(parent)) {
                    parent = Files.createDirectories(parent);
                }

                final Path tempFile = Files.createTempFile(parent, path.getFileName().toString(), ".tmp");
                Files.writeString(tempFile, json, StandardCharsets.UTF_8);

                Files.move(tempFile, path, StandardCopyOption.ATOMIC_MOVE, StandardCopyOption.REPLACE_EXISTING);
            } catch (final Exception ex) {
                logger.error(String.format("Error while saving player=%s", player.getName()), ex);
            }
        });
    }

    public CompletableFuture<Coffer> getCoffer(final String owner) {
        final Coffer cachedCoffer = allCoffers.get(owner);
        if (cachedCoffer != null) {
            return CompletableFuture.completedFuture(cachedCoffer);
        }

        // retrieve coffer if coffer hasn't been retrieved since server start
        return CompletableFuture.supplyAsync(() -> {
            final Path path = Path.of("./data/profile/save/tradingpost/coffers/", owner, ".json");
            if (!Files.exists(path)) {
                return null;
            }

            final Gson gson = GsonUtils.JSON_ALLOW_NULL.get();
            try (final BufferedReader reader = Files.newBufferedReader(path)) {
                final Coffer coffer = gson.fromJson(reader, Coffer.class);
                if (coffer != null) {
                    allCoffers.put(coffer.getOwner(), coffer);
                    return coffer;
                }
            } catch (final IOException e) {
                logger.error("Failed reading coffer for \"" + owner + "\"", e);
            }
            return null;
        });
    }

    public void saveListings(final String owner, final List<TradingPostListing> listings) {
        Thread.startVirtualThread(() -> {
           try {
               final String json = GsonUtils.JSON_PRETTY_ALLOW_NULL.get().toJson(listings);

               final Path path = Path.of("./data/profile/save/tradingpost/listings/", owner, ".json");
               Path parent = path.getParent();
               if (parent == null) {
                   throw new UnsupportedOperationException("Path must have a parent " + path);
               }
               if (!Files.exists(parent)) {
                   parent = Files.createDirectories(parent);
               }

               final Path tempFile = Files.createTempFile(parent, path.getFileName().toString(), ".tmp");
               Files.writeString(tempFile, json, StandardCharsets.UTF_8);

               Files.move(tempFile, path, StandardCopyOption.ATOMIC_MOVE, StandardCopyOption.REPLACE_EXISTING);

           } catch (final Exception ex) {
               logger.error(String.format("Error while saving player=%s", player.getName()), ex);
           }
        });
    }

    public static void loadAllListings() {
        try {
            File folder = new File("./data/profile/save/tradingpost/listings/");
            File[] files = folder.listFiles();
            if (files == null) return;
            for(File f : files) {
                File toRead = null;
                if(f.isDirectory()) {
                    toRead = Objects.requireNonNull(f.getAbsoluteFile().listFiles())[0];
                }
                try {
                    assert toRead != null;
                    final Gson gson = GsonUtils.JSON_ALLOW_NULL.get();
                    try (final BufferedReader reader = Files.newBufferedReader(Path.of(toRead.getPath()))) {
                        final List<TradingPostListing> listings = List.of(gson.fromJson(reader, TradingPostListing[].class));
                        allListings.addAll(listings);
                    } catch (final IOException e) {
                        logger.error("Failed reading listings", e);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void saveAllItemHistory() {
        Path path = Paths.get("./data/profile/save/tradingpost/itemhistory.json");
        File file = path.toFile();
        file.getParentFile().setWritable(true);

        if(!file.getParentFile().exists()) {
            try {
                file.getParentFile().mkdirs();
            } catch (SecurityException e) {
                System.out.println("Error while creating item history directory");
            }
        }
        try(FileWriter writer = new FileWriter(file)) {
            Gson builder = new GsonBuilder().setPrettyPrinting().create();
            JsonObject object = new JsonObject();
            object.add("itemhistory",builder.toJsonTree(itemHistories));
            writer.write(builder.toJson(object));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void saveRecentHistory() {
        Path path = Paths.get("./data/profile/save/tradingpost/itemhistoryrecent.json");
        File file = path.toFile();
        file.getParentFile().setWritable(true);

        if(!file.getParentFile().exists()) {
            try {
                file.getParentFile().mkdirs();
            } catch (SecurityException e) {
                System.out.println("Error while creating recent item history directory");
            }
        }
        try(FileWriter writer = new FileWriter(file)) {
            Gson builder = new GsonBuilder().setPrettyPrinting().create();
            JsonObject object = new JsonObject();
            object.add("recentitemhistory",builder.toJsonTree(recentlySoldItems));
            writer.write(builder.toJson(object));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void loadItemHistory() {
        Path path = Paths.get("./data/profile/save/tradingpost/itemhistory.json");
        File file = path.toFile();

        createFileAndDirIfNotExists(file);
        if(file.length() == 0) return;

        try (FileReader fileReader = new FileReader(file)) {
            JsonParser fileParser = new JsonParser();
            Gson builder = new GsonBuilder()
                    .create();
            JsonObject reader = (JsonObject) fileParser.parse(fileReader);

            HashMap<String,  List<ItemHistory>> itemHistory = builder.fromJson(reader.get("itemhistory"),
                    new TypeToken<HashMap<String, List<ItemHistory>>>() {
                    }.getType());
            itemHistories.putAll(itemHistory);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void loadRecentItemHistory() {
        Path path = Paths.get("./data/profile/save/tradingpost/itemhistoryrecent.json");
        File file = path.toFile();

        createFileAndDirIfNotExists(file);
        if(file.length() == 0) return;

        try (FileReader fileReader = new FileReader(file)) {
            JsonParser fileParser = new JsonParser();
            Gson builder = new GsonBuilder()
                    .create();
            JsonObject reader = (JsonObject) fileParser.parse(fileReader);
            ItemHistory[] temp = builder.fromJson(reader.get("recentitemhistory").getAsJsonArray(), ItemHistory[].class);
            recentlySoldItems.addAll(Arrays.asList(temp));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void createFileAndDirIfNotExists(File file) {
        if (!file.getParentFile().exists() || !file.exists()) {
            try {
                file.getParentFile().mkdirs();
                file.createNewFile();
            } catch (SecurityException e) {
                System.out.println("Unable to create directory");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public List<ItemHistory> getPlayersItemHistory() {
        return playersItemHistory;
    }


    static class TradingPostActiveListingSearch {
        private final TradingPostActiveListingSearchType searchType;
        private final String text;
        private final AhoCorasickDoubleArrayTrie<String> acdat = new AhoCorasickDoubleArrayTrie<>();

        TradingPostActiveListingSearch(TradingPostActiveListingSearchType searchType, String text) {
            this.searchType = searchType;
            this.text = text.toLowerCase();
        }
    }

    enum TradingPostActiveListingSearchType {
        PLAYER,
        ITEM,
        MOST_RECENT;
    }

    static class TradingPostActiveListingSort {
        TradingPostSortType postSortType;

        public TradingPostActiveListingSort(TradingPostSortType postSortType) {
            this.postSortType = postSortType;
        }

        public TradingPostSortType getPostSortType() {
            return postSortType;
        }

        public TradingPostActiveListingSort setPostSortType(TradingPostSortType postSortType) {
            this.postSortType = postSortType;
            return this;
        }
    }

    enum TradingPostSortType {
        HIGHEST_QUANTITY,
        LOWEST_QUANTITY,
        HIGHEST_PRICE,
        LOWEST_PRICE;
    }
}
