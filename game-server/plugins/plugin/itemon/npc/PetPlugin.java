package plugin.itemon.npc;

import com.osroyale.content.dialogue.DialogueFactory;
import com.osroyale.content.dialogue.Expression;
import com.osroyale.content.pet.PetData;
import com.osroyale.content.pet.Pets;
import com.osroyale.game.Animation;
import com.osroyale.game.event.impl.DropItemEvent;
import com.osroyale.game.event.impl.ItemOnNpcEvent;
import com.osroyale.game.event.impl.NpcClickEvent;
import com.osroyale.game.plugin.PluginContext;
import com.osroyale.game.world.World;
import com.osroyale.game.world.entity.mob.player.Player;
import com.osroyale.net.packet.out.SendMessage;
import com.osroyale.util.Utility;

/**
 * The Pet insurance plugin.
 *
 * @author Daniel
 */
public class PetPlugin extends PluginContext {

    @Override
    protected boolean itemOnNpc(Player player, ItemOnNpcEvent event) {
        if (event.getNpc().id != 7601) {
            return false;
        }
        Pets.buyInsurance(player, event.getUsed());
        return true;
    }

    @Override
    protected boolean onDropItem(Player player, DropItemEvent event) {
        return Pets.onSpawn(player, event.getItem().getId(), true);
    }

    @Override
    protected boolean firstClickNpc(Player player, NpcClickEvent event) {
        if (Pets.dialogue(player, event.getNpc())) {
            return true;
        }
        if (event.getNpc().id != 7601) {
            return false;
        }
        DialogueFactory factory = player.dialogueFactory;
        factory.sendNpcChat(7601, Expression.EVIL, "Welcome to the", "Pet Insurance Center", "How can I help you today?");
        factory.sendOption("How it works ", () -> message(factory), "View Insurance Information", () -> Pets.openInsurance(player),"View Lost Pets", () -> Pets.openLostPets(player), "Claim Lost Pets", () -> Pets.claimLostPets(player), "Nevermind", factory::clear);
        factory.execute();
        return true;
    }

    @Override
    protected boolean thirdClickNpc(Player player, NpcClickEvent event) {
        if (event.getNpc().id == 7601) {
            Pets.openInsurance(player);
            return true;
        }
        if (!PetData.forNpc(event.getNpc().id).isPresent()) {
            return false;
        }
        if (event.getNpc().owner != null && event.getNpc().owner != player) {
            player.send(new SendMessage("This is not your pet!"));
            return true;
        }
        if (player.pet == null || !player.pet.equals(event.getNpc())) {
            return true;
        }
        if (player.inventory.remaining() == 0) {
            player.send(new SendMessage("You need at least 1 free inventory space to do this."));
            return true;
        }
        PetData pets = PetData.forNpc(event.getNpc().id).get();
        player.interact(player.pet);
        player.animate(new Animation(827));
        World.schedule(1, () -> {
            player.pet.unregister();
            player.pet = null;
            player.inventory.add(pets.getItem(), 1);
        });
        return true;
    }
    @Override
    protected boolean secondClickNpc(Player player, NpcClickEvent event) {
        if (event.getNpc().id == 7601) {
            Pets.openInsurance(player);
            return true;
        }
        if (!PetData.forNpc(event.getNpc().id).isPresent()) {
            return false;
        }
        if (event.getNpc().owner != null && event.getNpc().owner != player) {
            player.send(new SendMessage("This is not your pet!"));
            return true;
        }
        if (player.pet == null || !player.pet.equals(event.getNpc())) {
            return true;
        }
        if (player.inventory.remaining() == 0) {
            player.send(new SendMessage("You need at least 1 free inventory space to do this."));
            return true;
        }
        PetData pets = PetData.forNpc(event.getNpc().id).get();
        player.interact(player.pet);
        player.animate(new Animation(827));
        World.schedule(1, () -> {
            player.pet.unregister();
            player.pet = null;
            player.inventory.add(pets.getItem(), 1);
        });
        return true;
    }


    private void message(DialogueFactory factory) {
        factory.sendNpcChat(7601, Expression.EVIL, "Just use your pet on me and I will insure it", "for " + Utility.formatDigits(Pets.INSRUANCE_COST) + " gold.");
    }

}

