package com.osroyale.content.activity.randomevent.impl;

import com.osroyale.net.packet.out.SendMessage;
import com.osroyale.game.Animation;
import com.osroyale.game.world.entity.mob.player.Player;
import com.osroyale.content.activity.randomevent.RandomEvent;
import com.osroyale.content.event.impl.NpcInteractionEvent;
import com.osroyale.game.world.items.Item;

/**
 * The drill demon random event.
 *
 * @author Daniel
 */
public class DrillDemonEvent extends RandomEvent {

    /** Constructs a new <code>DrillDemonEvent</code>.  */
    private DrillDemonEvent(Player player) {
        super(player, 20);
    }

    /** Creates a new Mime event. */
    public static DrillDemonEvent create(Player player) {
        DrillDemonEvent event = new DrillDemonEvent(player);
        event.add(player);
        return event;
    }

    @Override
    protected boolean clickNpc(Player player, NpcInteractionEvent event) {
        if (event.getNpc().id != eventNpcIdentification())
            return false;
        if (!event.getNpc().owner.equals(player))
            return false;
        if (angered) {
            player.dialogueFactory.sendNpcChat(eventNpcIdentification(), "You will suffer for ignoring me!").execute();
            return true;
        }
        if (event.getOpcode() == 0) {
            player.dialogueFactory.sendStatement("You have been given a reward from the Drill Demon.").onAction(() -> {
                player.dialogueFactory.clear();
                player.inventory.addOrDrop(new Item(6832, 1));
                finishCooldown();
            }).execute();
        } else if (event.getOpcode() == 1) {
            angered = true;
            player.send(new SendMessage("You have dismissed the drill demon random event."));
            player.animate(new Animation(863));
            player.interact(eventNpc);
            finishCooldown();
        }
        return true;
    }

    @Override
    protected int eventNpcIdentification() {
        return 337;
    }

    @Override
    protected String[] eventNpcShout() {
        return new String[]{
                "Attention, %name! I have something for you.",
                "%name! I said I have something for you!",
                "You are starting to really get my angry %name!",
                "I hope you burn in hell, %name!"
        };
    }
}
