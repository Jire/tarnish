package com.osroyale.content.activity.randomevent.impl;

import com.osroyale.net.packet.out.SendMessage;
import com.osroyale.game.Animation;
import com.osroyale.game.world.entity.mob.player.Player;
import com.osroyale.game.world.entity.mob.player.appearance.Gender;
import com.osroyale.content.activity.randomevent.RandomEvent;
import com.osroyale.content.event.impl.NpcInteractionEvent;
import com.osroyale.game.world.items.Item;

/**
 * The mime random event.
 *
 * @author Daniel
 */
public class MimeEvent extends RandomEvent {

    /** Constructs a new <code>MimeEvent</code>.  */
    private MimeEvent(Player player) {
        super(player, 20);
    }

    /** Creates a new Mime event.  */
    public static MimeEvent create(Player player) {
        MimeEvent event = new MimeEvent(player);
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
            player.dialogueFactory.sendStatement("The mime is no longer interested in you.").execute();
            return true;
        }
        if (event.getOpcode() == 0) {
            player.dialogueFactory.sendStatement("You have been given a reward from the mime.").onAction(() -> {
                player.dialogueFactory.clear();
                player.inventory.addOrDrop(new Item(6831, 1));
                finishCooldown();
            }).execute();
        } else if (event.getOpcode() == 1) {
            angered = true;
            player.send(new SendMessage("You have dismissed the mime random event."));
            player.animate(new Animation(863));
            player.interact(eventNpc);
            finishCooldown();
        }
        return true;
    }

    @Override
    protected int eventNpcIdentification() {
        return 321;
    }

    @Override
    protected String[] eventNpcShout() {
        return new String[]{
                "Mr" + (player.appearance.getGender() == Gender.FEMALE ? "s" : "") + ". %name, I have something for you!",
                "Can you hear me %name? You need to talk to me!",
                "Listen I don't have all day! Talk to me %name",
                "That's it! No reward for you %name."
        };
    }
}
