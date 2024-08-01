package com.osroyale.content.skill.impl.woodcutting;

import com.osroyale.content.prestige.PrestigePerk;
import com.osroyale.game.world.entity.mob.player.Player;
import com.osroyale.game.world.items.Item;
import com.osroyale.game.world.items.ground.GroundItem;
import com.osroyale.net.packet.out.SendMessage;
import com.osroyale.util.MessageColor;
import com.osroyale.util.RandomUtils;
import com.osroyale.util.Utility;

/**
 * Handles dropping & searching birds nest which are obtained from woodcutting.
 *
 * @author Daniel
 */
public class BirdsNest {

    /** Identification of all bird nest items. */
    private static final int[] BIRD_NEST = {5070, 5071, 5072, 5073, 5074};

    /** Identification of all seeds obtainable from birds nest. */
    private static final int[] SEED_REWARD = {5312, 5313, 5314, 5315, 5316, 5283, 5284, 5285, 5286, 5287, 5288, 5289, 5290, 5317};

    /** Identification of all rings obtainable from birds nest. */
    private static final int[] RING_REWARD = {1635, 1637, 1639, 1641, 1643};

    /** Handles dropping the birds nest. */
    public static void drop(Player player) {
        int chance = player.prestige.hasPerk(PrestigePerk.LITTLE_BIRDY) ? 10 : 5;
        if (Utility.random(1, 200) < chance) {
            Item item = new Item(Utility.randomElement(BIRD_NEST), 1);
            GroundItem.create(player, item);
            player.send(new SendMessage("A bird's nest falls out of the tree.", MessageColor.RED));
        }
    }

    /** Handles searching a birds nest. */
    public static void search(Player player, int item) {
        if (player.inventory.remaining() == 0) {
            player.send(new SendMessage("You do not have enough inventory space to do this."));
            return;
        }
        int reward = 0;
        if (item == 5074)
            reward = Utility.randomElement(RING_REWARD);
        else if (item == 5073)
            reward = Utility.randomElement(SEED_REWARD);
        else if (item == 5070)
            reward = 5076;
        else if (item == 5071)
            reward = 5077;
        else if (item == 5072)
            reward = 5078;
        Item rewardItem = new Item(reward, 1);
        String name = rewardItem.getName();
        player.send(new SendMessage("You search the nest... and find " + Utility.getAOrAn(name) + " " + name + "."));
        player.inventory.replace(item, 5075, true);
        player.inventory.add(rewardItem);
    }
}
