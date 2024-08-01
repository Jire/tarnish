package plugin;

import com.osroyale.Config;
import com.osroyale.game.event.impl.ItemContainerContextMenuEvent;
import com.osroyale.game.event.impl.ObjectClickEvent;
import com.osroyale.game.plugin.PluginContext;
import com.osroyale.game.world.entity.mob.player.Player;
import com.osroyale.game.world.entity.skill.Skill;
import com.osroyale.game.world.items.Item;
import com.osroyale.net.packet.out.SendItemOnInterface;
import com.osroyale.net.packet.out.SendString;

import java.util.ArrayList;

public class TabletCreationPlugin extends PluginContext {

    private final static Item[] TABS = {new Item(8007), new Item(8008), new Item(8009), new Item(8010), new Item(8011), new Item(8012), new Item(8013), new Item(8014), new Item(8015)};

    public enum ItemData {
        VARROCK(8007, 25, new Item(557, 1), new Item(556, 3), new Item(563, 1)),
        LUMBRIDGE(8008, 31, new Item(557, 1), new Item(556, 3), new Item(563, 1)),
        FALADOR(8009, 43, new Item(555, 1), new Item(556, 3), new Item(563, 1)),
        CAMELOT(8010, 55, new Item(556, 5), new Item(563, 1)),
        ARDOUGNE(8011, 63, new Item(555, 2), new Item(556, 3), new Item(563, 2)),
        WATCHTOWER(8012, 58, new Item(563, 2), new Item(555, 2)),
        HOME(8013, 90, new Item(557, 3), new Item(556, 5), new Item(563, 2)),
        BONES_TO_BANANAS(8014, 15, new Item(561, 1), new Item(557, 2), new Item(555, 2)),
        BONES_TO_PEACHES(8015, 60, new Item(561, 2), new Item(557, 4), new Item(555, 4));

        private final int tabID;
        private final int levelRequired;
        private final Item[] itemsRequired;

        ItemData(int tabID, int levelRequired, Item... itemsRequired) {
            this.tabID = tabID;
            this.levelRequired = levelRequired;
            this.itemsRequired = itemsRequired;
        }

        public static ItemData forTablet(int id) {
            for (ItemData tablet : values()) {
                if (tablet.tabID == id)
                    return tablet;
            }
            return null;
        }
    }


    @Override
    protected boolean firstClickObject(Player player, ObjectClickEvent event) {
        if (event.getObject().getId() != 22472) {
            return false;
        }

        player.send(new SendItemOnInterface(26756, TABS));
        player.interfaceManager.open(26750);
        return true;
    }

    @Override
    protected boolean firstClickItemContainer(Player player, ItemContainerContextMenuEvent event) {
        if (event.getInterfaceId() != 26756) {
            return false;
        }

        if (!player.interfaceManager.isInterfaceOpen(26750)) {
            return true;
        }

        ItemData tablet = ItemData.forTablet(event.getRemoveId());

        if (tablet == null) {
            return false;
        }

        if (player.skills.getMaxLevel(Skill.MAGIC) < tablet.levelRequired) {
            player.message("You need a magic level of " + tablet.levelRequired + " to craft this tablet!");
            return true;
        }

        if (!player.inventory.containsAll(tablet.itemsRequired)) {
            player.message("You do not have all the needed materials to craft this tablet!");
            return true;
        }

        player.inventory.removeAll(tablet.itemsRequired);
        player.inventory.add(tablet.tabID, 1);
        player.skills.addExperience(Skill.MAGIC, 500 * Config.MAGIC_MODIFICATION);
        return true;
    }

    @Override
    protected boolean secondClickItemContainer(Player player, ItemContainerContextMenuEvent event) {
        if (event.getInterfaceId() != 26756) {
            return false;
        }

        if (!player.interfaceManager.isInterfaceOpen(26750)) {
            return true;
        }

        ItemData tablet = ItemData.forTablet(event.getRemoveId());

        if (tablet == null) {
            return false;
        }

        ArrayList<String> required = new ArrayList<>();

        for (Item item : tablet.itemsRequired) {
            required.add(item.getAmount() + " " + item.getName());
        }

        required.add("1 Soft clay.");
        player.message("Items required: " + required);
        return true;
    }
}
