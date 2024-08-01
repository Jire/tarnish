package com.osroyale.content.skill.impl.fishing;

import com.osroyale.content.event.impl.NpcInteractionEvent;
import com.osroyale.content.skill.SkillRepository;
import com.osroyale.game.world.entity.mob.npc.Npc;
import com.osroyale.game.world.entity.mob.player.Player;
import com.osroyale.game.world.entity.skill.Skill;
import com.osroyale.game.world.items.Item;
import com.osroyale.game.world.items.ItemDefinition;
import com.osroyale.util.Utility;

/**
 * Handles the fishing skill.
 *
 * @author Daniel
 */
public class Fishing extends Skill {

    public Fishing(int level, double experience) {
        super(Skill.FISHING, level, experience);
    }
    public static double getBonus(Player player) {
        double bonus = 0;
        if(player.equipment.getId(0) == 13258)
            bonus += 0.4;
        if(player.equipment.getId(4) == 13259)
            bonus += 0.8;
        if(player.equipment.getId(7) == 13260)
            bonus += 0.6;
        if(player.equipment.getId(10) == 13261)
            bonus += 0.2;

        if(player.equipment.containsAll(13258, 13259, 13260, 13261))
            bonus = 2.5;

        return bonus;
    }



    static boolean canFish(Player player, Fishable fish, boolean message) {
        if (player.skills.get(Skill.FISHING).getLevel() < fish.getRequiredLevel()) {
            if (message) player.message("You need a fishing level of " + fish.getRequiredLevel() + " to fish here.");
            return false;
        }
        return hasFishingItems(player, fish, message);
    }

    private static boolean hasFishingItems(Player player, Fishable fish, boolean message) {
        int tool = fish.getToolId();
        int bait = fish.getBaitRequired();
        boolean hasDragonHarpoon;
        if (tool == 311) {
            hasDragonHarpoon = player.inventory.contains(new Item(21028, 1));
            if (!player.inventory.contains(new Item(tool, 1)) && !hasDragonHarpoon) {
                String name = ItemDefinition.get(tool).getName();
                player.message("You need " + Utility.getAOrAn(name) + " " + name + " to fish here.");
                return false;
            }
        } else {
            if (!player.inventory.contains(new Item(tool, 1)) && message) {
                String name = ItemDefinition.get(tool).getName();
                player.message("You need " + Utility.getAOrAn(name) + " " + name + " to fish here.");
                return false;
            }
        }
        if (bait > -1 && !player.inventory.contains(new Item(bait, 1))) {
            String name = ItemDefinition.get(bait).getName();
            if (message) {
                player.message("You need " + Utility.getAOrAn(name) + " " + name + " to fish here.");
            }
            return false;
        }
        return true;
    }

    public static boolean success(Player player, Fishable fish) {
        var usingDragonHarpoon = (player.inventory.contains(new Item(21028, 1))
                || player.equipment.contains(new Item(21028, 1))) && player.skills.get(Skill.FISHING).getLevel() > 60;
        if (usingDragonHarpoon && (fish.getToolId() == 311 || fish.getToolId() == 21028)) {
            return SkillRepository.isSuccess(player, Skill.FISHING, fish.getRequiredLevel(), true);
        }
        return SkillRepository.isSuccess(player, Skill.FISHING, fish.getRequiredLevel(), false);
    }

    @Override
    protected boolean clickNpc(Player player, NpcInteractionEvent event) {
        Npc npc = event.getNpc();
        int opcode = event.getOpcode();
        FishingSpot spot = FishingSpot.forId(npc.id);

        if (spot == null) {
            return false;
        }
        if (player.skills.get(Skill.FISHING).isDoingSkill()) {
            return true;
        }

        int amount = 0;
        Fishable[] fish;
        Fishable[] fishable = new Fishable[3];

        switch (opcode) {
            case 0:
                fish = spot.getFirstOption();
                for (int i = 0; i < fish.length; i++) {
                    if (canFish(player, fish[i], i == 0)) {
                        fishable[i] = fish[i];
                        amount++;
                    }
                }
                break;
            case 1:
                fish = spot.getSecondOption();
                for (int i = 0; i < fish.length; i++) {
                    if (canFish(player, fish[i], i == 0)) {
                        fishable[i] = fish[i];
                        amount++;
                    }
                }
        }

        if (amount == 0)
            return true;

        Fishable[] fishing = new Fishable[amount];
        System.arraycopy(fishable, 0, fishing, 0, amount);
        start(player, fishing, 0);
        return true;
    }

    public void start(Player player, Fishable[] fishing, int option) {
        if (fishing == null || fishing[option] == null || fishing[option].getToolId() == -1) {
            return;
        }

        FishingTool tool = FishingTool.forId(fishing[option].getToolId());

        if (!hasFishingItems(player, fishing[option], true)) {
            return;
        }

        player.action.execute(new FishingAction(player, tool, fishing));
    }
}
