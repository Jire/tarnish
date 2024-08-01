package com.osroyale.content.collectionlog;

import com.osroyale.game.world.entity.mob.player.Player;
import com.osroyale.game.world.items.Item;
import com.osroyale.game.world.items.ItemDefinition;
import com.osroyale.net.packet.out.SendConfig;
import com.osroyale.net.packet.out.SendItemOnInterface;
import com.osroyale.net.packet.out.SendMessage;
import com.osroyale.net.packet.out.SendString;

import java.util.ArrayList;

public class CollectionLog {

    public static final int INTERFACE_ID = 44500;
    public static final int NAME_ID = 44516;
    public static final int OBTAINED_ID = 44517;
    public static final int KC_ID = 44518;
    public static final int ITEM_CONTAINER = 44521;

    private ArrayList<CollectionLogItem> log;

    public CollectionLog() {
        log = new ArrayList<>();
    }

    public ArrayList<CollectionLogItem> getLog() {
        return this.log;
    }

    public static void open(Player player) {
        player.interfaceManager.open(INTERFACE_ID);

        if (player.collectionLogPageOpen != null)
            loadPage(player, player.collectionLogPageOpen);
        else
            loadPage(player, CollectionLogPage.BOSSES);

        if (player.collectionLogView != null)
            selectLog(player, player.collectionLogView, -1);
        else
            selectLog(player, CollectionLogData.BARROWS, 0);
    }

    public static void loadPage(Player player, CollectionLogPage page) {
        player.collectionLogPageOpen = page;
        player.send(new SendConfig(906, page.ordinal()));
        player.send(new SendConfig(907, 0));
        sendButtons(player, page);

        ArrayList<CollectionLogData> list = CollectionLogData.getPageList(player.collectionLogPageOpen);
        CollectionLogData selected = list.get(0);
        selectLog(player, selected, 0);
    }

    public static void sendButtons(Player player, CollectionLogPage page) {
        ArrayList<CollectionLogData> list = CollectionLogData.getPageList(page);
        int index = 44524;
        for (int i = 0; i < list.size(); ++i, index += 2) {
            int color = 0;
            CollectionLogData selected = list.get(i);
            for (CollectionLogItem cli : player.getCollectionLog().getLog()) {
                if (cli.getData() == selected) {
                    if (cli.hasClaimed()) {
                        color = 1;
                    }
                }
            }

            player.send(new SendString(color > 0 ? "@gre@" + list.get(i).getName() : list.get(i).getName(), index));
        }

        for(int i = list.size(); i < 17; i++, index += 2)
            player.send(new SendString("", index));
    }

    public static void selectLog(Player player, CollectionLogData selected, int slot) {
        if(slot != -1)
            player.send(new SendConfig(907, slot));

        player.send(new SendString(selected.getName(), NAME_ID));


        ArrayList<Item> items = new ArrayList<>();

        int[] itemIds = selected.getItems();
        int total = itemIds == null ? 0 : itemIds.length;

        if (itemIds != null) {
            for (int i : itemIds) {
                items.add(new Item(i, getLogItemAmount(player, selected, i)));
            }
        }

        int obtained = 0;
        for (Item gi : items) {
            if (gi.getAmount() > 0) {
                obtained++;
            }
        }

        String colorCode = obtained >= total ? "65280" : obtained > 0 ? "FFFF00" : "FF0000";
        player.send(new SendString("Obtained: <col="+colorCode+">" + obtained + "/" + total + "</col>", OBTAINED_ID));

        String outcome = "";
        int counter = getCounter(player.getCollectionLog(), selected);
        if (selected.getType().equals(CollectionCategory.BOSSES))
            outcome = selected.getName() + " kills: <col=FFFFFF>" + counter + "</col>";
        else if (selected.getCounterText() != null)
            outcome = selected.getCounterText() + ": <col=FFFFFF>" + counter + "</col>";

        player.send(new SendString(outcome, KC_ID));

        player.send(new SendItemOnInterface(ITEM_CONTAINER, items.toArray(new Item[0])));

        player.collectionLogView = selected;
    }

    public static void selectLogButton(Player player, int slot) {
        ArrayList<CollectionLogData> list = CollectionLogData.getPageList(player.collectionLogPageOpen);
        if (slot > list.size() - 1) {
            return;
        }
        CollectionLogData selected = list.get(slot);
        selectLog(player, selected, slot);
    }

    public static boolean clickButton(Player player, int button) {
        CollectionLogPage page = CollectionLogPage.forButton(button);
        if (page != null) {
            loadPage(player, page);
            return true;
        }
        if (button >= -21013 && button < -20915) {
            selectLogButton(player, (button - -21013) / 2);
            return true;
        }
        return false;
    }


    public static int getCounter(CollectionLog cl, CollectionLogData data) {
        for (CollectionLogItem i : cl.getLog()) {
            if (i.getData() == data) {
                return i.getCounter();
            }
        }
        return 0;
    }

    public static int getLogItemAmount(Player player, CollectionLogData data, int item) {
        for (CollectionLogItem cli : player.getCollectionLog().getLog()) {
            if (cli.getData() == data) {
                for (int i = 0; i < cli.getItems().size(); ++i) {
                    if (cli.getItems().get(i).getId() == item) {
                        return cli.getItems().get(i).getAmount();
                    }
                }
            }
        }
        return 0;
    }

    public static void checkItemDrop(Player player, int npcId, int itemId, int amount) {
        if (npcId < 1) {
            return;
        }
        for (CollectionLogData data : CollectionLogData.values()) {
            for(int npc : data.getNpcIds()) {
                if (npc == npcId) {
                    logItem(player, data, itemId, amount);
                    return;
                }
            }
        }
    }

    public static void logItem(Player player, CollectionLogData data, int item, int amount) {
        boolean valid = false;
        for (int i : data.getItems()) {
            if (i == item) {
                valid = true;
                break;
            }
        }
        if (!valid) {
            return;
        }
        for (CollectionLogItem cli : player.getCollectionLog().getLog()) {
            if (cli.getData() == data) {
                cli.addItem(item, amount);
                player.send(new SendMessage("@red@An item was added to your collection log: " + amount + "x " + ItemDefinition.get(item).getName() + "!"));
                CollectionLogSaving.save(player);
                return;
            }
        }
        player.send(new SendMessage("@red@An item was added to your collection log: " + amount + "x " + ItemDefinition.get(item).getName() + "!"));
        CollectionLogItem addLog = new CollectionLogItem(data);
        addLog.addItem(item, amount);
        player.getCollectionLog().getLog().add(addLog);
        CollectionLogSaving.save(player);
    }

    public static void onNpcKill(Player player, int npcId) {
        if (npcId < 1) {
            return;
        }
        for (CollectionLogData data : CollectionLogData.values()) {
            for(int npc : data.getNpcIds()) {
                if (npc == npcId) {
                    increaseCounter(player, data);
                    return;
                }
            }
        }
    }

    public static void increaseCounter(Player player, CollectionLogData data) {
        for (CollectionLogItem cli : player.getCollectionLog().getLog()) {
            if (cli.getData() == data) {
                cli.setCounter(cli.getCounter() + 1);
                return;
            }
        }
        CollectionLogItem addLog = new CollectionLogItem(data);
        addLog.setCounter(1);
        player.getCollectionLog().getLog().add(addLog);
    }
}