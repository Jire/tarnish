package com.osroyale.content.skill.impl.woodcutting;

import com.osroyale.content.event.impl.ObjectInteractionEvent;
import com.osroyale.content.skill.SkillRepository;
import com.osroyale.game.world.entity.mob.player.Player;
import com.osroyale.game.world.entity.skill.Skill;
import com.osroyale.game.world.items.Item;
import com.osroyale.net.packet.out.SendMessage;
import com.osroyale.util.StringUtils;

/**
 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
 * @since 24-12-2016.
 */
public final class Woodcutting extends Skill {

    /** Constructs a new {@link Woodcutting} skill. */
    public Woodcutting(int level, double experience) {
        super(Skill.WOODCUTTING, level, experience);
    }

    public static double getBonus(Player player) {
        double bonus = 0;
        if(player.equipment.getId(0) == 10941)
            bonus += 0.4;
        if(player.equipment.getId(4) == 10939)
            bonus += 0.8;
        if(player.equipment.getId(7) == 10940)
            bonus += 0.6;
        if(player.equipment.getId(10) == 10933)
            bonus += 0.2;

        if(player.equipment.containsAll(10941, 10939, 10940, 10933))
            bonus = 2.5;

        return bonus;
    }

    public static boolean success(Player player, TreeData tree, AxeData axe) {
        return SkillRepository.isSuccess(player, Skill.WOODCUTTING, tree.levelRequired, axe.level);
    }

    @Override
    protected boolean clickObject(Player player, ObjectInteractionEvent event) {
        TreeData tree = TreeData.forId(event.getObject().getId());

        if (tree == null)
            return false;

        AxeData axe = AxeData.getDefinition(player).orElse(null);

        if (axe == null) {
            player.send(new SendMessage("You don't have a hatchet."));
            return true;
        }

        if (!player.skills.getSkills()[Skill.WOODCUTTING].reqLevel(axe.level)) {
            player.send(new SendMessage("You need a level of " + axe.level + " to use this hatchet!"));
            return true;
        }

        if (!player.skills.getSkills()[Skill.WOODCUTTING].reqLevel(tree.levelRequired)) {
            player.send(new SendMessage("You need a level of " + tree.levelRequired + " to cut " + StringUtils.appendIndefiniteArticle(event.getObject().getDefinition().getName()) + "."));
            return true;
        }

        if (!player.inventory.hasCapacityFor(new Item(tree.item))) {
            player.message("You do not have enough inventory spaces to do this!");
            return true;
        }

        player.action.execute(new WoodcuttingAction(player, event.getObject(), tree, axe));
        player.message(true, "You swing your axe at the tree...");
        return true;
    }
}
