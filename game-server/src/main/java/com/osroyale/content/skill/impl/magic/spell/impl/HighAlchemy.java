package com.osroyale.content.skill.impl.magic.spell.impl;

import com.osroyale.Config;
import com.osroyale.content.achievement.AchievementHandler;
import com.osroyale.content.achievement.AchievementKey;
import com.osroyale.content.activity.randomevent.RandomEventHandler;
import com.osroyale.content.skill.impl.magic.Magic;
import com.osroyale.content.skill.impl.magic.Spellbook;
import com.osroyale.content.skill.impl.magic.spell.Spell;
import com.osroyale.game.Animation;
import com.osroyale.game.Graphic;
import com.osroyale.game.world.entity.mob.player.Player;
import com.osroyale.game.world.entity.skill.Skill;
import com.osroyale.game.world.items.Item;
import com.osroyale.net.packet.out.SendForceTab;
import com.osroyale.net.packet.out.SendMessage;

import java.util.Arrays;

/**
 * The high alchemy spell.
 *
 * @author Daniel
 */
public class HighAlchemy implements Spell {

	@Override
	public String getName() {
		return "High alchemy";
	}

	@Override
	public int getLevel() {
		return 55;
	}

	@Override
	public Item[] getRunes() {
		return new Item[] { new Item(554, 5), new Item(561, 1) };
	}

	@Override
	public void execute(Player player, Item item) {
		if (player.spellbook != Spellbook.MODERN)
			return;

        if (!player.spellCasting.castingDelay.elapsed(500)) {
            return;
		}

		if (Arrays.stream(Magic.UNALCHABLES).anyMatch($it -> item.getId() == $it.getId())) {
			player.send(new SendMessage("You can not alch this item!"));
			return;
		}

		int value = item.getHighAlch();

		player.animate(new Animation(713));
		player.graphic(new Graphic(113, true));
		player.inventory.remove(item.getId(), 1);
		player.inventory.removeAll(getRunes());
		player.inventory.add(Config.CURRENCY, value == 0 ? 1 : value);
		player.inventory.refresh();
		player.send(new SendForceTab(6));
		player.skills.addExperience(Skill.MAGIC, 65 * (Config.MAGIC_MODIFICATION + 5));
		AchievementHandler.activate(player, AchievementKey.HIGH_ALCHEMY, 1);
		player.spellCasting.castingDelay.reset();
		player.action.clearNonWalkableActions();
		RandomEventHandler.trigger(player);
	}
}
