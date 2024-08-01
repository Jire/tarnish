package plugin;

import com.osroyale.Config;
import com.osroyale.content.dialogue.DialogueFactory;
import com.osroyale.content.donators.DonatorBond;
import com.osroyale.content.skill.impl.magic.teleport.Teleportation;
import com.osroyale.content.tittle.PlayerTitle;
import com.osroyale.game.event.impl.ItemClickEvent;
import com.osroyale.game.plugin.PluginContext;
import com.osroyale.game.world.entity.mob.UpdateFlag;
import com.osroyale.game.world.entity.mob.player.Player;
import com.osroyale.game.world.entity.mob.player.PlayerRight;
import com.osroyale.game.world.items.Item;
import com.osroyale.game.world.position.Area;

/**
 * Handles the donator plugin.
 *
 * @author Daniel.
 */
public class DonatorPlugin extends PluginContext {

    public void title(Player player, PlayerTitle title, boolean permitted) {
        if (permitted) {
            player.playerTitle = title;
            player.updateFlags.add(UpdateFlag.APPEARANCE);
            player.dialogueFactory.sendStatement("You have successfully changed your title.");
            return;

        }

        player.dialogueFactory.sendStatement("You are not permitted to change your title to that!");
    }

    @Override
    protected boolean onClick(Player player, int button) {
        if (button == -8328) {
            player.donatorDeposit.confirm();
            return true;
        }
        if (button == -15115) {
            DialogueFactory factory = player.dialogueFactory;
            factory.sendOption("Regular Donator Zone", () -> {
                if (PlayerRight.isDonator(player) || PlayerRight.isSuper(player) || PlayerRight.isExtreme(player)) {
                    Teleportation.teleport(player, Config.REGULAR_DONATOR_ZONE);
                } else {
                    player.dialogueFactory.sendStatement("You must be a donator to access this zone!");
                }
            }, "Super Donator Zone", () -> {
                if (PlayerRight.isElite(player) || PlayerRight.isKing(player)) {
                    Teleportation.teleport(player, Config.SUPER_DONATOR_ZONE);
                } else {
                    player.dialogueFactory.sendStatement("You must be an elite or king donator to access this zone!");
                }
            });
            factory.execute();
            return true;
        }
        if (button == -15111) {
            DialogueFactory factory = player.dialogueFactory;
            factory.sendOption("Select <col=9C4B2F>Donator", () -> {
                title(player, PlayerTitle.create("Donator", 0x9C4B2F), PlayerRight.isDonator(player));
            }, "Select <col=2F809C>Super", () -> {
                title(player, PlayerTitle.create("Super", 0x2F809C), PlayerRight.isSuper(player));
            }, "Select <col=158A76>Extreme", () -> {
                title(player, PlayerTitle.create("Extreme", 0x158A76), PlayerRight.isExtreme(player));
            }, "Select <col=2CA395>Elite", () -> {
                title(player, PlayerTitle.create("Elite", 0x2CA395), PlayerRight.isElite(player));
            }, "Select <col=8F7A42>King", () -> {
                title(player, PlayerTitle.create("King", 0x8F7A42), PlayerRight.isKing(player));
            });
            factory.execute();
            return true;
        }
        return false;
    }

    @Override
    protected boolean firstClickItem(Player player, ItemClickEvent event) {
        DonatorBond bond = DonatorBond.forId(event.getItem().getId());

        if (bond == null)
            return false;

        DialogueFactory factory = player.dialogueFactory;

        if (Area.inWilderness(player)) {
            factory.sendStatement("You can not be in the wilderness to redeem a bond!").execute();
            return true;
        }

        if (player.getCombat().inCombat()) {
            factory.sendStatement("You can not be in combat to redeem a bond!").execute();
            return true;
        }

        factory.sendStatement("Are you sure you want to redeem this <col=255>" + event.getItem().getName() + "</col>?", "There is no going back!");
        factory.sendOption("Yes, redeem <col=255>" + event.getItem().getName() + "</col>!", () -> redeem(player, event.getItem(), bond), "Nevrmind", factory::clear);
        factory.execute();
        return true;
    }

    private void redeem(Player player, Item item, DonatorBond bond) {
        if (player.inventory.contains(item.getId(), 1)) {
            player.inventory.remove(item.getId(), 1);
            player.donation.redeem(bond);
        }
    }
}
