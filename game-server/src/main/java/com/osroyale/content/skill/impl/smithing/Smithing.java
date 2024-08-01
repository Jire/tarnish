package com.osroyale.content.skill.impl.smithing;

import com.osroyale.game.world.entity.mob.player.Player;
import com.osroyale.content.event.impl.ClickButtonInteractionEvent;
import com.osroyale.content.event.impl.ItemContainerInteractionEvent;
import com.osroyale.content.event.impl.ItemOnObjectInteractionEvent;
import com.osroyale.content.event.impl.ObjectInteractionEvent;
import com.osroyale.game.world.entity.skill.Skill;
import com.osroyale.game.world.items.Item;
import com.osroyale.net.packet.out.SendInputAmount;
import com.osroyale.net.packet.out.SendMessage;

/**
 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
 * @since 11-2-2017.
 */
public final class Smithing extends Skill {

    public Smithing(int level, double experience) {
        super(Skill.SMITHING, level, experience);
    }

    @Override
    public boolean itemContainerAction(Player player, ItemContainerInteractionEvent event) {
        switch(event.id) {
            case 1:
                return SmithingArmour.forge(player, event.interfaceId, event.removeSlot, 1);
            case 2:
                return SmithingArmour.forge(player, event.interfaceId, event.removeSlot, 5);
            case 3:
                return SmithingArmour.forge(player, event.interfaceId, event.removeSlot, 10);
            case 4:
                return SmithingArmour.forge(player, event.interfaceId, event.removeSlot, Integer.MAX_VALUE);
            case 5:
                switch(event.interfaceId) {
                    case 1119:
                    case 1120:
                    case 1121:
                    case 1122:
                    case 1123:
                        player.send(new SendInputAmount("Enter amount", 2, input -> SmithingArmour.forge(player, event.interfaceId, event.removeSlot, Integer.parseInt(input))));
                        return true;
                }
                return false;
            default:
            return false;
        }
    }
    @Override
    public boolean clickButton(Player player, ClickButtonInteractionEvent event) {
        switch(event.getType()) {
            case CLICK_BUTTON:
                return Smelting.smelt(player, event.getButton());
            default:
                return false;
        }
    }

    @Override
    public boolean useItem(Player player, ItemOnObjectInteractionEvent event) {
        switch(event.getType()) {
            case ITEM_ON_OBJECT:
                return SmithingArmour.openInterface(player, event.getItem(), event.getObject());
            default:
                return false;
        }
    }

    @Override
    protected boolean clickObject(Player player, ObjectInteractionEvent event) {
        if (event.getObject().getId() == 2097) {
            Item foundBar = null;
            for (int bar : Smelting.SMELT_BARS) {
                if (player.inventory.contains(bar)) {
                    foundBar = new Item(bar);
                    break;
                }
            }
            if (foundBar != null) {
                SmithingArmour.openInterface(player, foundBar, event.getObject());
            } else {
                player.send(new SendMessage("You have no bar which you have the required Smithing level to use."));
            }
            return true;
        }
        switch(event.getType()) {
            case FIRST_CLICK_OBJECT:
            case SECOND_CLICK_OBJECT:
                return Smelting.openInterface(player, event.getObject());
            default:
                return false;
        }
    }
}
