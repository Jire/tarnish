package plugin;

import com.osroyale.Config;
import com.osroyale.content.preloads.Preload;
import com.osroyale.content.preloads.PreloadRepository;
import com.osroyale.content.preloads.RandomItem;
import com.osroyale.game.plugin.PluginContext;
import com.osroyale.game.world.entity.mob.UpdateFlag;
import com.osroyale.game.world.entity.mob.player.Player;
import com.osroyale.game.world.items.Item;
import com.osroyale.game.world.position.Area;
import com.osroyale.net.packet.out.SendItemOnInterface;
import com.osroyale.net.packet.out.SendScrollbar;
import com.osroyale.net.packet.out.SendString;
import com.osroyale.net.packet.out.SendTooltip;

public class PreloadPlugin extends PluginContext {

    private boolean click(Player player, int button) {
        if (button >= -8115 && button <= -8080) {
            int index = (button - -8115) / 2;

            if (index > PreloadRepository.PRELOADS.size()) {
                return false;
            }

            if (index < PreloadRepository.PRELOADS.size()) {
                open(player, index);
                return true;
            }
        }
        return false;
    }

    public static void open(Player player, int slot) {
        if (player.getCombat().inCombat()) {
            player.message("You can not do this while in combat!");
            return;
        }

        if (Area.inWilderness(player)) {
            player.message("You can not do this while in the wilderness!");
            return;
        }

        if (player.pvpInstance && Area.inPvP(player)) {
            player.message("You must be in a safe zone to do this!");
            return;
        }

        if (slot > PreloadRepository.PRELOADS.size()) {
            return;
        }

        for (int index = 0, string = 57422; index < 30; index++, string += 2) {
            if (index >= PreloadRepository.PRELOADS.size()) {
                player.send(new SendString("", string));
                player.send(new SendTooltip("", string - 1));
                continue;
            }

            String name = PreloadRepository.PRELOADS.get(index).title();
            String color = index == slot ? "<col=ffffff>" : "</col>";
            player.send(new SendString(color + name, string));
            player.send(new SendTooltip("Activate Preset: <col=CC8914>" + name + "</col>", string - 1));
        }

        player.send(new SendScrollbar(57420, 350));

        if (slot == -1) {
            player.send(new SendString("", 57410));
            player.send(new SendString("", 57409));
            player.send(new SendString("", 57408));
            player.send(new SendItemOnInterface(57407));
            player.interfaceManager.open(57400);
            return;
        }

        Preload preload = PreloadRepository.PRELOADS.get(slot);

        player.prayer.reset();
        player.bank.depositeEquipment(false);
        player.bank.depositeInventory(false);

        if (!player.inventory.isEmpty() || !player.equipment.isEmpty()) {
            player.message("You have no space in your bank!");
            return;
        }

        for (int index = 0; index < preload.skills().length; index++) {
            player.skills.setMaxLevel(index, preload.skills()[index]);
            player.skills.setLevel(index, preload.skills()[index]);
        }

        player.skills.setCombatLevel();
        player.updateFlags.add(UpdateFlag.APPEARANCE);

        for (Item item : preload.equipment()) {
            if (item != null) {
                if (item.getId() == PreloadRepository.CAPE) {
                    item.setId(RandomItem.CAPE.getItem());
                } else if (item.getId() == PreloadRepository.HAT) {
                    item.setId(RandomItem.HAT.getItem());
                } else if (item.getId() == PreloadRepository.ROBE_TOP) {
                    item.setId(RandomItem.ROBE_TOP.getItem());
                } else if (item.getId() == PreloadRepository.ROBE_BOTTOM) {
                    item.setId(RandomItem.ROBE_BOTTOM.getItem());
                } else if (item.getId() == PreloadRepository.GOD_CAPE) {
                    item.setId(RandomItem.GOD_CAPE.getItem());
                } else if (item.getId() == PreloadRepository.GOD_BOOK) {
                    item.setId(RandomItem.BOOKS.getItem());
                } else if (item.getId() == PreloadRepository.MYSTIC_TOP) {
                    item.setId(RandomItem.MYSTIC_TOP.getItem());
                } else if (item.getId() == PreloadRepository.MYSTIC_BOTTOM) {
                    item.setId(RandomItem.MYSTIC_BOTTOM.getItem());
                }
                player.equipment.manualWear(item);
            }
        }

        player.spellbook = preload.spellbook();
        player.inventory.addAll(preload.inventory());
        player.equipment.refresh();
        player.interfaceManager.setSidebar(Config.MAGIC_TAB, player.spellbook.getInterfaceId());

        player.send(new SendString("Total: " + PreloadRepository.PRELOADS.size(), 57410));
        player.send(new SendString("Combat Level: " + player.skills.getCombatLevel(), 57409));
        player.send(new SendString(preload.title(), 57408));
        player.send(new SendItemOnInterface(57407, preload.inventory()));

        player.interfaceManager.open(57400);

        if (player.pvpInstance) {
            player.playerAssistant.setValueIcon();
        }
    }

    @Override
    protected boolean onClick(Player player, int button) {
        return click(player, button);
    }
}
