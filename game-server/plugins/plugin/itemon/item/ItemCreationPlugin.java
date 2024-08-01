package plugin.itemon.item;

import com.osroyale.content.ItemCreation;
import com.osroyale.content.dialogue.DialogueFactory;
import com.osroyale.content.skill.impl.slayer.SlayerUnlockable;
import com.osroyale.game.event.impl.ItemOnItemEvent;
import com.osroyale.game.plugin.PluginContext;
import com.osroyale.game.world.entity.mob.player.Player;
import com.osroyale.game.world.entity.skill.Skill;
import com.osroyale.game.world.items.Item;
import com.osroyale.net.packet.out.SendMessage;
import com.osroyale.util.Utility;

import java.util.Arrays;

public class ItemCreationPlugin extends PluginContext {

    @Override
    protected boolean itemOnItem(Player player, ItemOnItemEvent event) {
        Item used = event.getUsed();
        Item with = event.getWith();

        if ((used.getId() == 233 || with.getId() == 233) && (with.getId() == 5075 || with.getId() == 5075)) {
            player.inventory.remove(5075, 1);
            player.inventory.add(6693, 1);
            player.skills.addExperience(Skill.CRAFTING, 150);
            player.message(true ,"You have crushed up the birds nest.");
            return true;
        }

        if ((used.getId() == 233 || with.getId() == 233) && (with.getId() == 237 || with.getId() == 237)) {
            player.inventory.remove(237, 1);
            player.inventory.add(235, 1);
            player.skills.addExperience(Skill.CRAFTING, 150);
            player.message(true,"You have crushed up the unicorn horn.");
            return true;
        }

        if ((used.getId() == 233 || with.getId() == 233) && (with.getId() == 1973 || with.getId() == 1973)) {
            player.inventory.remove(1973, 1);
            player.inventory.add(1975, 1);
            player.skills.addExperience(Skill.CRAFTING, 150);
            player.message(true,"You have crushed up the chocolate bar.");
            return true;
        }

        if (!ItemCreation.CreationData.forItems(used, with).isPresent()) {
            return false;
        }

        final ItemCreation.CreationData creation = ItemCreation.CreationData.forItems(event.getUsed(), event.getWith()).get();
        final String name = creation.product[0].getName();

        if (!player.inventory.containsAll(creation.required)) {
            player.send(new SendMessage("You do not have the required items to make " + Utility.getAOrAn(name) + " " + name + "."));
            return true;
        }


        if (creation.level != null) {
            for (Skill skill : creation.level) {
                if (player.skills.getMaxLevel(skill.getSkill()) < skill.getLevel()) {
                    String skillName = Skill.getName(skill.getSkill());
                    player.message("You need " + Utility.getAOrAn(skillName) + " " + skillName + " level of " + skill.getLevel() + " to do this!");
                    return true;
                }
            }
        }

        if (creation == ItemCreation.CreationData.SLAYER_HELM && !player.slayer.getUnlocked().contains(SlayerUnlockable.SLAYER_HELM)) {
            player.message("You need to unlock this ability first!");
            return true;
        }

        final DialogueFactory factory = player.dialogueFactory;

        factory.sendItem(name, "Are you sure you want to do this?", creation.product[0]).sendOption("Yes", () -> {
            Arrays.stream(creation.required).forEach(item -> {
                if (item.getId() == 1755 || item.getId() == 2347)
                    return;
                player.inventory.remove(item);
            });

            factory.clear();
            player.inventory.addAll(creation.product);
            player.send(new SendMessage("You successfully create " + Utility.getAOrAn(name) + " " + name + ".", true));
            if (creation.level != null) {
                for (Skill skill : creation.level) {
                    player.skills.addExperience(skill.getSkill(), skill.getExperience());
                }
            }
        }, "No", factory::clear).execute();
        return true;
    }

}
