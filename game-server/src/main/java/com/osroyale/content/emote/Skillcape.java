package com.osroyale.content.emote;

import com.osroyale.content.store.StoreItem;
import com.osroyale.content.store.currency.CurrencyType;
import com.osroyale.game.world.entity.mob.player.Player;
import com.osroyale.game.world.entity.skill.Skill;
import com.osroyale.game.world.items.Item;
import com.osroyale.net.packet.out.SendMessage;

import java.util.Arrays;
import java.util.Optional;
import java.util.OptionalInt;

/**
 * Holds the data for skillcape emotes.
 *
 * @author Daniel
 */
public enum Skillcape {
    ATTACK_CAPE(new int[]{9747, 9748, 9479}, 823, 4959, Skill.ATTACK),
    STRENGTH_CAPE(new int[]{9750, 9751, 9752}, 828, 4981, Skill.STRENGTH),
    DEFENCE_CAPE(new int[]{9753, 9754, 9755}, 824, 4961, Skill.DEFENCE),
    RANGING_CAPE(new int[]{9756, 9757, 9758}, 832, 4973, Skill.RANGED),
    PRAYER_CAPE(new int[]{9759, 9760, 9761}, 829, 4979, Skill.PRAYER),
    MAGIC_CAPE(new int[]{9762, 9763, 9764}, 813, 4939, Skill.MAGIC),
    RUNECRAFT_CAPE(new int[]{9765, 9766, 9767}, 817, 4947, Skill.RUNECRAFTING),
    HITPOINTS_CAPE(new int[]{9768, 9769, 9770}, 833, 4971, Skill.HITPOINTS),
    AGILITY_CAPE(new int[]{9771, 9772, 9773}, 830, 4977, Skill.AGILITY),
    HERBLORE_CAPE(new int[]{9774, 9775, 9776}, 835, 4969, Skill.HERBLORE),
    THEIVING_CAPE(new int[]{9777, 9778, 9779}, 826, 4965, Skill.THIEVING),
    CRAFTING_CAPE(new int[]{9780, 9781, 9782}, 818, 4949, Skill.CRAFTING),
    FLETCHING_CAPE(new int[]{9783, 9784, 9785}, 812, 4937, Skill.FLETCHING),
    SLAYER_CAPE(new int[]{9786, 9787, 9788}, 827, 4967, Skill.SLAYER),
    CONSTRUCTION_CAPE(new int[]{9789, 9790, 9791}, 820, 4953, Skill.CONSTRUCTION),
    MINING_CAPE(new int[]{9792, 9793, 9794}, 814, 4941, Skill.MINING),
    SMITHING_CAPE(new int[]{9795, 9796, 9797}, 815, 4943, Skill.SMITHING),
    FISHING_CAPE(new int[]{9798, 9799, 9800}, 819, 4951, Skill.FISHING),
    COOKING_CAPE(new int[]{9801, 9802, 9803}, 821, 4955, Skill.COOKING),
    FIREMAKING_CAPE(new int[]{9804, 9805, 9806}, 831, 4975, Skill.FIREMAKING),
    WOODCUTTING_CAPE(new int[]{9807, 9808, 9809}, 822, 4957, Skill.WOODCUTTING),
    FARMING_CAPE(new int[]{9810, 9811, 9812}, 825, 4963, Skill.FARMING),
    HUNTER_CAPE(new int[]{9948, 9949, 9950}, 907, 5158, Skill.HUNTER),
    QUEST_POINT_CAPE(new int[]{9813, 9814, 9815}, 816, 4945, -1),
    ACHIEVEMENT_CAPE(new int[]{13069}, 323, 2709, -1),
    MAX_CAPE(new int[]{13280, 13329, 13331, 13333, 13335, 13337}, 1286, 7121, -1);

    private final int[] item;
    private final int graphic;
    private final int animation;
    private final int skill;

    Skillcape(int[] item, int graphic, int animation, int skill) {
        this.item = item;
        this.graphic = graphic;
        this.animation = animation;
        this.skill = skill;
    }

    public int getAnimation() {
        return animation;
    }

    public int getGraphic() {
        return graphic;
    }

    public int[] getSkillcape() {
        return item;
    }

    public int getSkill() {
        return skill;
    }

    public static Skillcape forId(int id) {
        for (Skillcape data : Skillcape.values())
            for (int index = 0; index < data.getSkillcape().length; index++)
                if (data.getSkillcape()[index] == id)
                    return data;
        return null;
    }

    public static Optional<Skillcape> forSkill(int skill) {
        return Arrays.stream(values()).filter(s -> s.getSkill() == skill).findFirst();
    }

    public static StoreItem[] getItems() {
        final StoreItem[] items = new StoreItem[Skill.SKILL_COUNT];

        int index = 0;
        for (Skillcape data : values()) {
            if (data.skill == -1)
                continue;
            items[index] = new StoreItem(data.getSkillcape()[1], 1, OptionalInt.of(100000), Optional.of(CurrencyType.COINS));
            index++;
        }

        return items;
    }

    public static boolean equip(Player player, Item item) {
        Skillcape data = forId(item.getId());

        if (data == null) {
            return true;
        }

        if (data.getSkill() == -1) {
            return true;
        }

        if (player.skills.getMaxLevel(data.getSkill()) != 99) {
            player.send(new SendMessage("You need to have a " + Skill.getName(data.getSkill()) + " level of 99 to equip this item."));
            return false;
        }

        return true;
    }
}