package com.osroyale.content.skill.impl.magic.spell.impl;

import com.osroyale.content.skill.impl.magic.Spellbook;
import com.osroyale.content.skill.impl.magic.spell.Spell;
import com.osroyale.game.Animation;
import com.osroyale.game.Graphic;
import com.osroyale.game.UpdatePriority;
import com.osroyale.game.world.entity.combat.attack.listener.other.VengeanceListener;
import com.osroyale.game.world.entity.mob.player.Player;
import com.osroyale.game.world.entity.skill.Skill;
import com.osroyale.game.world.items.Item;
import com.osroyale.net.packet.out.SendMessage;
import com.osroyale.net.packet.out.SendWidget;

import java.util.concurrent.TimeUnit;

/**
 * Handles the vengeance spell.
 */
public class Vengeance implements Spell {
    @Override
    public String getName() {
        return "Vengeance";
    }

    @Override
    public Item[] getRunes() {
        return new Item[]{new Item(9075, 4), new Item(557, 10), new Item(560, 2)};
    }

    @Override
    public int getLevel() {
        return 94;
    }

    @Override
    public void execute(Player player, Item item) {
        if (player.spellbook != Spellbook.LUNAR)
            return;
        if (player.skills.getMaxLevel(Skill.DEFENCE) < 45) {
            player.send(new SendMessage("You need a defence level of 45 to cast this spell!"));
            return;
        }
        if (player.venged) {
            player.send(new SendMessage("You already have vengeance casted!"));
            return;
        }
        if (player.spellCasting.vengeanceDelay.elapsedTime(TimeUnit.SECONDS) < 30) {
            player.send(new SendMessage("You can only cast vengeance once every 30 seconds."));
            return;
        }
        player.animate(new Animation(8317, UpdatePriority.HIGH), true);
        player.graphic(new Graphic(726, true));
        player.spellCasting.vengeanceDelay.reset();
        player.send(new SendWidget(SendWidget.WidgetType.VENGEANCE, 30));
        player.venged = true;
        player.getCombat().addListener(VengeanceListener.get());
    }
}
