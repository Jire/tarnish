package com.osroyale.content.simulator;

import com.osroyale.content.activity.impl.barrows.BarrowsUtility;
import com.osroyale.game.world.entity.mob.npc.definition.NpcDefinition;
import com.osroyale.game.world.entity.mob.npc.drop.NpcDrop;
import com.osroyale.game.world.entity.mob.npc.drop.NpcDropManager;
import com.osroyale.game.world.entity.mob.npc.drop.NpcDropTable;
import com.osroyale.game.world.entity.mob.player.Player;
import com.osroyale.game.world.items.Item;
import com.osroyale.game.world.items.containers.pricechecker.PriceType;
import com.osroyale.net.packet.out.SendItemOnInterface;
import com.osroyale.net.packet.out.SendScrollbar;
import com.osroyale.net.packet.out.SendString;
import com.osroyale.util.RandomGen;
import com.osroyale.util.Utility;

import java.util.*;

/**
 * Created by Daniel on 2018-02-03.
 */
public enum Simulation implements SimulationFunction {
    BLOOD_MONEY_CHEST() {
        @Override
        public void execute(Player player, int id, int amount) {
            Map<Integer, Integer> items = new HashMap<>();
            int count = 0;
            long value = 0;

            for (int index = 0; index < amount; index++) {
                Item money = new Item(13307, Utility.random(5000, 8000));
                value += money.getValue() * money.getAmount();

                if (items.containsKey(money.getId())) {
                    int toAdd = items.get(money.getId()) + money.getAmount();

                    items.replace(money.getId(), toAdd);
                } else {
                    items.put(money.getId(), money.getAmount());
                }
                count++;

                Item piece = new Item(Utility.randomElement(BarrowsUtility.BARROWS));
                value += piece.getValue() * piece.getAmount();
                if (items.containsKey(piece.getId())) {
                    int toAdd = items.get(piece.getId()) + piece.getAmount();
                    items.replace(piece.getId(), toAdd);
                } else {
                    items.put(piece.getId(), piece.getAmount());
                }
                count++;
            }

            Iterator it = items.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry pair = (Map.Entry) it.next();
                int mapValue = (int) pair.getValue();



                Item item = new Item((int) pair.getKey(), (int) pair.getValue());



                System.out.println(pair.getKey() + " = " + pair.getValue());
//                player.send(new SendItemOnInterfaceSlot(26816, item, count));
                it.remove();
                count++;
            }

            int scroll = (items.size() / 6 + (items.size() % 6 > 0 ? 1 : 0)) * 44;
            player.send(new SendString(amount, 26811, true));
            player.send(new SendString("<col=C1A875>Blood Money Chest", 26806));
            player.send(new SendString("Simulated <col=C1A875>" + Utility.formatDigits(amount) + "</col> drops", 26807));
            player.send(new SendString("Total value: <col=01FF80>" + Utility.formatDigits(value) + "</col>", 26808));
            player.send(new SendScrollbar(26815, scroll));
            player.attributes.set("DROP_SIMULATOR_CUSTOM_KEY", 0);
        }
    },
    NPC_DROP() {
        @Override
        public void execute(Player player, int id, int amount) {
            if (amount > 100_000) {
                amount = 100_000;
            }
            NpcDefinition npc = NpcDefinition.get(id);
            if (npc == null)
                return;
            NpcDropTable drop = NpcDropManager.NPC_DROPS.get(id);
            if (drop == null)
                return;
            Map<Integer, Item> items = new HashMap<>();
            long value = 0;
            for (int index = 0; index < amount; index++) {
                List<NpcDrop> npc_drops = drop.generate(player, true);
                RandomGen gen = new RandomGen();
                for (NpcDrop drops : npc_drops) {
                    Item item = drops.toItem(gen);
                    value += item.getValue(PriceType.VALUE) * item.getAmount();
                    items.compute(item.getId(), (key, val) -> val == null ? item : val.getId() == item.getId() ? val.createAndIncrement(item.getAmount()) : val);
                }
            }

            List<Item> sorted = new ArrayList<>(items.values());
            sorted.sort((first, second) -> second.getValue() - first.getValue());

            player.attributes.set("DROP_SIMULATOR_SORTED_LIST", sorted.toArray(new Item[0]));
            int scroll = (items.size() / 6 + (items.size() % 6 > 0 ? 1 : 0)) * 44;
            player.send(new SendScrollbar(26815, scroll));
            player.send(new SendItemOnInterface(26816, sorted.toArray(new Item[0])));
            player.send(new SendString(amount, 26811, true));
            player.send(new SendString("<col=C1A875>" + npc.getName(), 26806));
            player.send(new SendString("Simulated <col=C1A875>" + Utility.formatDigits(amount) + "</col> drops", 26807));
            player.send(new SendString("Total value: <col=01FF80>" + Utility.formatDigits(value) + "</col>", 26808));
            player.attributes.set("DROP_SIMULATOR_KEY", id);
        }
    }
}

interface SimulationFunction {

    void execute(Player player, int id, int amount);

}