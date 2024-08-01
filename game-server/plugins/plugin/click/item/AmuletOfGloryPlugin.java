package plugin.click.item;

import com.osroyale.content.skill.impl.magic.teleport.Teleportation;
import com.osroyale.game.event.impl.ItemClickEvent;
import com.osroyale.game.event.impl.ItemContainerContextMenuEvent;
import com.osroyale.game.plugin.PluginContext;
import com.osroyale.game.world.entity.mob.player.Player;
import com.osroyale.game.world.items.Item;
import com.osroyale.game.world.items.containers.equipment.Equipment;
import com.osroyale.game.world.position.Position;
import com.osroyale.util.Utility;
import plugin.click.object.FountainOfRunePlugin;

public class AmuletOfGloryPlugin extends PluginContext {

    private static final int[] AMULETS = {
            1704, 1706, 1708, 1710, 1712, 11976, 11978
    };

    public void teleport(Player player, Item item, Position position, int index, boolean equipment, boolean eternal) {
        if (eternal) {
            Teleportation.teleport(player, position,30);
            return;
        }

        index = index -1;

        if (Teleportation.teleport(player, position,30)) {
            if (equipment) {
                player.equipment.set(Equipment.AMULET_SLOT, new Item(AMULETS[index]), true);
            } else {
                player.inventory.remove(item);
                player.inventory.add(AMULETS[index], 1);
            }
            player.message(true, "<col=7F007F>" + (index == 0 ? "You have used your last charge." : "Your amulet of glory has " + Utility.convertWord(index).toLowerCase() + "charge" + (index == 1 ? "" : "s") + " remaining."));
        }
    }

    private int getIndex(int item) {
        int index = -1;
        for (int amulet = 0; amulet < AMULETS.length; amulet++) {
            if (item == AMULETS[amulet]) {
                return amulet;
            }
        }
        return index;
    }

    @Override
    protected boolean thirdClickItem(Player player, ItemClickEvent event) {
        Item item = event.getItem();
        int index = getIndex(item.getId());

        if (item.getId() == FountainOfRunePlugin.ETERNAL_GLORY) {
            player.dialogueFactory.sendOption("Edgeville", () -> {
                teleport(player, item, new Position(3087, 3496), -1, false, true);
            }, "Karamja", () -> {
                teleport(player, item, new Position(2918, 3176), -1, false, true);
            }, "Dranyor Village", () -> {
                teleport(player, item, new Position(3105, 3251), -1, false, true);
            }, "Al-Kahrid", () -> {
                teleport(player, item, new Position(3293, 3163), -1, false, true);
            }, "Nowhere", player.interfaceManager::close).execute();
            return true;
        }

        if (index == -1) {
            return false;
        }

        if (index == 0) {
            player.message(true, "Your amulet of glory has no charges left!");
            return true;
        }

        player.dialogueFactory.sendOption("Edgeville", () -> {
            teleport(player, item, new Position(3087, 3496), index, false, false);
        }, "Karamja", () -> {
            teleport(player, item, new Position(2918, 3176), index, false, false);
        }, "Dranyor Village", () -> {
            teleport(player, item, new Position(3105, 3251), index, false, false);
        }, "Al-Kahrid", () -> {
            teleport(player, item, new Position(3293, 3163), index, false, false);
        }, "Nowhere", player.interfaceManager::close).execute();
        return true;
    }

    @Override
    protected boolean secondClickItemContainer(Player player, ItemContainerContextMenuEvent event) {
        final int interfaceId = event.getInterfaceId();

        if (interfaceId != 1688) {
            return false;
        }

        final int removeId = event.getRemoveId();
        Item item = player.equipment.getAmuletSlot();
        if (item == null) {
            return false;
        }

        if (item.getId() == FountainOfRunePlugin.ETERNAL_GLORY) {
            player.dialogueFactory.sendOption("Edgeville", () -> {
                teleport(player, item, new Position(3087, 3496), -1, true, true);
            }, "Karamja", () -> {
                teleport(player, item, new Position(2918, 3176), -1, true, true);
            }, "Dranyor Village", () -> {
                teleport(player, item, new Position(3105, 3251), -1, true, true);
            }, "Al-Kahrid", () -> {
                teleport(player, item, new Position(3293, 3163), -1, true, true);
            }, "Nowhere", player.interfaceManager::close).execute();
            return true;
        }

        int index = getIndex(removeId);
        if (index == -1) {
            return false;
        }

        if (index == 0) {
            player.message(true, "Your amulet of glory has no charges left!");
            return true;
        }

        player.dialogueFactory.sendOption("Edgeville", () -> {
            teleport(player, item, new Position(3087, 3496), index, true, false);
        }, "Karamja", () -> {
            teleport(player, item, new Position(2918, 3176), index, true, false);
        }, "Dranyor Village", () -> {
            teleport(player, item, new Position(3105, 3251), index, true, false);
        }, "Al-Kahrid", () -> {
            teleport(player, item, new Position(3293, 3163), index, true, false);
        }, "Nowhere", player.interfaceManager::close).execute();
        return true;
    }
}
