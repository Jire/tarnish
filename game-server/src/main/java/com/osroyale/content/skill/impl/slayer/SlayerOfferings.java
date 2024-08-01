package com.osroyale.content.skill.impl.slayer;

import com.osroyale.Config;
import com.osroyale.game.world.entity.mob.player.Player;
import com.osroyale.game.world.entity.skill.Skill;
import com.osroyale.game.world.items.Item;
import com.osroyale.util.Utility;

/**
 * Handles selling offering slayer rewards.
 *
 * @author Daniel
 */
public class SlayerOfferings {

    public static void offer(Player player) {
        int points = 0;
        double experience = 0;

        for (Item item : player.inventory) {
            if (item == null)
                continue;
            OfferingData offering = OfferingData.forItem(item.getId());
            if (offering == null)
                continue;
            points += offering.getPoints();
            experience += offering.getExperience();
        }

        if (points == 0 && experience == 0) {
            player.dialogueFactory.sendNpcChat(6797, "You have no offerings for me!").execute();
            return;
        }

        player.dialogueFactory.sendNpcChat(6797, "I will give you " + points + " slayer points &", Utility.formatDigits(experience) + " experience for your offerings.", "Do you accept?");
        player.dialogueFactory.sendOption("Yes", () -> confirm(player),"No", () -> player.dialogueFactory.clear());
        player.dialogueFactory.execute();
    }

    private static void confirm(Player player) {
        int points = 0;
        double experience = 0;

        for (Item item : player.inventory) {
            if (item == null)
                continue;
            OfferingData offering = OfferingData.forItem(item.getId());
            if (offering == null)
                continue;
            points += offering.getPoints();
            experience += offering.getExperience();
            player.inventory.remove(item);
        }

        player.message("Your offerings were accepted!");
        player.slayer.setPoints(player.slayer.getPoints() + points);
        player.skills.addExperience(Skill.SLAYER, experience);
        player.dialogueFactory.clear();
    }

    public enum OfferingData {
        ANCIENT_SHARD(19677, 1, 1350),
        TOTEM_BASE(19679, 1, 1350),
        TOTEM_MIDDLE(19681, 1, 1350),
        TOTEM_TOP(19683, 1, 1350),
        TOTEM(19685, 1, 1350),
        ENSOULED_GOBLIN_HEAD(13447, 1, 1000),
        ENSOULED_GOBLIN_HEAD_II(13448, 1, 1000),
        ENSOULED_MONKEY_HEAD(13450, 1, 1000),
        ENSOULED_MONKEY_HEAD_II(13451, 1, 1000),
        ENSOULED_IMP_HEAD(13453, 1, 1000),
        ENSOULED_IMP_HEAD_II(13454, 1, 1000),
        ENSOULED_MINOTAUR_HEAD(13456, 1, 1000),
        ENSOULED_MINOTAUR_HEAD_II(13457, 1, 1000),
        ENSOULED_SCORPION_HEAD(13459, 1, 1000),
        ENSOULED_SCORPION_HEAD_II(13460, 1, 1000),
        ENSOULED_BEAR_HEAD(13462, 1, 1000),
        ENSOULED_BEAR_HEAD_II(13463, 1, 1000),
        ENSOULED_UNICORN_HEAD(13465, 1, 1000),
        ENSOULED_UNICORN_HEAD_II(13466, 1, 1000),
        ENSOULED_DOG_HEAD(13468, 1, 1000),
        ENSOULED_DOG_HEAD_II(13469, 1, 1000),
        ENSOULED_CHAOS_DRUID_HEAD(13471, 1, 1000),
        ENSOULED_CHAOS_DRUID_HEAD_II(13472, 1, 1000),
        ENSOULED_GIANT_HEAD(13474, 1, 1000),
        ENSOULED_GIANT_HEAD_II(13475, 1, 1000),
        ENSOULED_OGRE_HEAD(13477, 1, 1000),
        ENSOULED_OGRE_HEAD_II(13478, 1, 1000),
        ENSOULED_ELF_HEAD(13480, 1, 1000),
        ENSOULED_ELF_HEAD_II(13481, 1, 1000),
        ENSOULED_TROLL_HEAD(13483, 1, 1000),
        ENSOULED_TROLL_HEAD_II(13484, 1, 1000),
        ENSOULED_HORROR_HEAD(13486, 1, 1000),
        ENSOULED_HORROR_HEAD_II(13487, 1, 1000),
        ENSOULED_KALPHITE_HEAD(13489, 1, 1000),
        ENSOULED_KALPHITE_HEAD_II(13490, 1, 1000),
        ENSOULED_DAGANNOTH_HEAD(13492, 1, 1000),
        ENSOULED_DAGANNOTH_HEAD_II(13493, 1, 1000),
        ENSOULED_BLOODVELD_HEAD(13495, 1, 1000),
        ENSOULED_BLOODVELD_HEAD_II(13496, 1, 1000),
        ENSOULED_TZHAAR_HEAD(13498, 1, 1000),
        ENSOULED_TZHAAR_HEAD_II(13499, 1, 1000),
        ENSOULED_DEMON_HEAD(13501, 1, 1000),
        ENSOULED_DEMON_HEAD_II(13502, 1, 1000),
        ENSOULED_AVIANSIE_HEAD(13504, 1, 1000),
        ENSOULED_AVIANSIE_HEAD_II(13505, 1, 1000),
        ENSOULED_ABYSSAL_HEAD(13507, 1, 1000),
        ENSOULED_ABYSSAL_HEAD_II(13508, 1, 1000),
        ENSOULED_DRAGON_HEAD(13511, 1, 1000),
        ENSOULED_DRAGON_HEAD_II(13510, 1, 1000);

        private final int item;
        private final int points;
        private final int experience;

        /** Constructs a new <code>OfferingData<code>. */
        OfferingData(int item, int points, int experience) {
            this.item = item;
            this.points = points;
            this.experience = experience;
        }

        public int getItem() {
            return item;
        }

        public int getPoints() {
            return points;
        }

        public double getExperience() {
            return experience;
        }

        public static OfferingData forItem(int item) {
            for (OfferingData offering : values()) {
                if (offering.getItem() == item)
                    return offering;
            }
            return null;
        }
    }
}
