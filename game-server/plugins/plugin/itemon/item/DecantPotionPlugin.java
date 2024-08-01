package plugin.itemon.item;

import com.osroyale.Config;
import com.osroyale.content.clanchannel.content.ClanTaskKey;
import com.osroyale.content.consume.PotionData;
import com.osroyale.game.event.impl.ItemOnItemEvent;
import com.osroyale.game.plugin.PluginContext;
import com.osroyale.game.world.entity.mob.player.Player;
import com.osroyale.game.world.entity.skill.Skill;
import com.osroyale.game.world.items.Item;
import com.osroyale.game.world.items.ItemDefinition;
import com.osroyale.net.packet.out.SendMessage;

public class DecantPotionPlugin extends PluginContext {

    private final static int[] COMBAT_POTION_ITEMS = { 2436, 2440, 2442, 269 };

    private void makeCombatPotion(Player player) {
        if (player.skills.getMaxLevel(Skill.HERBLORE) < 90) {
            player.message("You need a Herblore level of 90 to do this!");
            return;
        }

        if (!player.inventory.containsAll(COMBAT_POTION_ITEMS)) {
            player.message("You need a clean torstol and all 3 super combat potions.");
            return;
        }

        for (int potion : COMBAT_POTION_ITEMS) {
            player.inventory.remove(potion, 1);
        }

        player.inventory.add(12695, 1);
        player.skills.addExperience(Skill.HERBLORE, 325 * Config.HERBLORE_MODIFICATION);
        player.forClan(channel -> channel.activateTask(ClanTaskKey.SUPER_COMBAT_POTION, player.getName()));
    }

    @Override
    protected boolean itemOnItem(Player player, ItemOnItemEvent event) {
        final Item used = event.getUsed();
        final int usedSlot = event.getUsedSlot();
        final Item with = event.getWith();
        final int withSlot = event.getWithSlot();

        for (int potion : COMBAT_POTION_ITEMS) {
            if (used.getId() == potion ||with.getId() == potion) {
                makeCombatPotion(player);
                return true;
            }
        }

        final PotionData first = PotionData.forId(used.getId()).orElse(null);

        if (first == null) {
            return false;
        }

        final PotionData second = PotionData.forId(with.getId()).orElse(null);

        if (second == null) {
            return false;
        }

        if (first != second) {
            player.send(new SendMessage("You can't mix two different types of potions."));
            return true;
        }

        if (used.matchesId(first.getIds()[0]) || with.matchesId(second.getIds()[0])) {
            player.send(new SendMessage("You can't combine these potions as one of them is already full."));
            return true;
        }

        int doses = getDoses(used.getId()) + getDoses(with.getId());
        int remainder = doses > 4 ? doses % 4 : 0;
        doses -= remainder;

        player.inventory.replace(with.getId(), first.getIdForDose(doses), withSlot, true);

        if (remainder > 0) {
            player.inventory.replace(used.getId(), first.getIdForDose(remainder), usedSlot, true);
        } else {
            System.out.println(player.inventory.get(usedSlot));
            player.inventory.replace(used.getId(), 229, usedSlot, true);
        }

        player.send(new SendMessage("You carefully combine the potions.", true));
        return true;
    }

    private static int getDoses(int id) {
        ItemDefinition definition = ItemDefinition.get(id);
        int index = definition.getName().lastIndexOf(')');
        return Integer.valueOf(definition.getName().substring(index - 1, index));
    }

}
