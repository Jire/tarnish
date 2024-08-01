package com.osroyale.content.activity.randomevent.impl;


import com.osroyale.content.activity.randomevent.RandomEvent;
import com.osroyale.content.event.impl.NpcInteractionEvent;
import com.osroyale.game.Animation;
import com.osroyale.game.world.entity.mob.player.Player;
import com.osroyale.game.world.items.Item;
import com.osroyale.net.packet.out.SendMessage;

/**
 * The Genie random event.
 *
 * @author Daniel
 */
public class GenieEvent extends RandomEvent {

    /** Constructs a new <code>GenieEvent</code>. */
    private GenieEvent(Player player) {
        super(player, 20);
    }

    /** Creates a new Genie event.  */
    public static GenieEvent create(Player player) {
        GenieEvent event = new GenieEvent(player);
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
            player.dialogueFactory.sendNpcChat(eventNpcIdentification(), "You had your chance...").execute();
            return true;
        }
        if (event.getOpcode() == 0) {
            int id = eventNpcIdentification();
            player.dialogueFactory.sendNpcChat(id, "Take this lamp as a token of appreciation for playing", "Tarnish.").onAction(() -> {
                player.dialogueFactory.clear();
                player.inventory.addOrDrop(new Item(2528, 1));
                finishCooldown();
            }).execute();
        } else if (event.getOpcode() == 1) {
            angered = true;
            player.send(new SendMessage("You have dismissed the genie random event."));
            player.animate(new Animation(863));
            player.interact(eventNpc);
            finishCooldown();
        }
        return true;
    }

    @Override
    protected String[] eventNpcShout() {
        return new String[]{
                "Hello %name, I'm here to grant you a wish!",
                "Are you there %name? I have something for you!",
                "C'mon %name, I don't have all day!",
                "I guess you didn't want a free wish! Had your chance!"
        };
    }

    @Override
    protected int eventNpcIdentification() {
        return 326;
    }
}
