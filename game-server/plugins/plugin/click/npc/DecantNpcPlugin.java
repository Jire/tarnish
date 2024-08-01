package plugin.click.npc;

import com.osroyale.content.consume.PotionData;
import com.osroyale.content.dialogue.DialogueFactory;
import com.osroyale.game.event.impl.NpcClickEvent;
import com.osroyale.game.plugin.PluginContext;
import com.osroyale.game.world.entity.mob.player.Player;
import com.osroyale.game.world.items.Item;
import com.osroyale.game.world.items.ItemDefinition;
import com.osroyale.util.Utility;

import java.util.HashMap;
import java.util.Map;

public class DecantNpcPlugin extends PluginContext {

    private final static int COST = 150_000;

    private void decant(Player player) {
        if (!player.inventory.contains(995, COST)) {
            player.dialogueFactory.sendStatement("You do not have enough coins to do this!");
            return;
        }

        player.inventory.remove(995, COST);
        decantAll(player);
    }

    @Override
    protected boolean firstClickNpc(Player player, NpcClickEvent event) {
        if (event.getNpc().id != 1146) {
            return false;
        }

        DialogueFactory factory = player.dialogueFactory;
        if (!canDecant(player)) {
            factory.sendNpcChat(1146, "Hello, I am Joe the chemist.", "You don't have any potions for me to decant!", "Bye.").execute();
            return true;
        }
        factory.sendNpcChat(1146, "Hello, I am Joe the chemist.", "How may I help you?");
        factory.sendOption(
                "Decant all potions (" + Utility.formatDigits(COST) + " coins)",
                () -> factory.onAction(() -> decant(player)).sendStatement("Joe the Chemist has successfully decanted all your", "positions for " + Utility.formatDigits(COST) + " coins.").onNext(),

                "Nevermind",
                () -> factory.onAction(player.interfaceManager::close));
        factory.execute();
        return true;
    }

    private boolean canDecant(Player player) {
        Map<PotionData, Integer> doses = new HashMap<>();

        int emptyVials = 0;
        for (int index = 0; index < player.inventory.capacity(); index++) {
            Item item = player.inventory.get(index);

            if (item == null) {
                continue;
            }

            PotionData potion = PotionData.forId(item.getId()).orElse(null);

            if (potion == null) {
                continue;
            }

            emptyVials++;
            doses.computeIfPresent(potion, (data, dosage) -> dosage += getDoses(item.getId()));
            doses.computeIfAbsent(potion, data -> getDoses(item.getId()));
        }

        for (Map.Entry<PotionData, Integer> potion : doses.entrySet()) {
            int fullPotions = potion.getValue() / 4;
            int remainder = potion.getValue() - fullPotions * 4;
            emptyVials -= fullPotions;

            if (remainder > 0) {
                emptyVials--;
            }
        }
        return emptyVials > 0;
    }

    private void decantAll(Player player) {
        Map<PotionData, Integer> doses = new HashMap<>();

        int emptyVials = 0;
        for (int index = 0; index < player.inventory.capacity(); index++) {
            Item item = player.inventory.get(index);

            if (item == null) {
                continue;
            }

            PotionData potion = PotionData.forId(item.getId()).orElse(null);

            if (potion == null) {
                continue;
            }

            doses.computeIfPresent(potion, (data, dosage) -> dosage += getDoses(item.getId()));
            doses.computeIfAbsent(potion, data -> getDoses(item.getId()));
            player.inventory.remove(item, index, false);
            emptyVials++;
        }

        for (Map.Entry<PotionData, Integer> potion : doses.entrySet()) {
            int fullPotions = potion.getValue() / 4;
            int remainder = potion.getValue() - fullPotions * 4;
            emptyVials -= fullPotions;

            player.inventory.add(new Item(potion.getKey().getIds()[0], fullPotions), -1, false);

            if (remainder > 0) {
                emptyVials--;
                player.inventory.add(new Item(potion.getKey().getIdForDose(remainder)), -1, false);
            }
        }

        player.inventory.add(new Item(229, emptyVials), -1, false);
        player.inventory.refresh();
    }

    private static int getDoses(int id) {
        ItemDefinition definition = ItemDefinition.get(id);
        int index = definition.getName().lastIndexOf(')');
        return Integer.valueOf(definition.getName().substring(index - 1, index));
    }
}
