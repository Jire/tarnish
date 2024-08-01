package com.osroyale.content.skill.impl.magic.spell.impl;

import com.osroyale.Config;
import com.osroyale.content.skill.impl.magic.Spellbook;
import com.osroyale.content.skill.impl.magic.spell.Spell;
import com.osroyale.content.skill.impl.smithing.Smelting;
import com.osroyale.content.skill.impl.smithing.SmeltingData;
import com.osroyale.game.Graphic;
import com.osroyale.game.world.entity.mob.player.Player;
import com.osroyale.game.world.entity.skill.Skill;
import com.osroyale.game.world.items.Item;
import com.osroyale.net.packet.out.SendForceTab;

import java.util.Optional;

/**
 * The superheat spell
 * 
 * @author Daniel
 */
public class SuperHeat implements Spell {

	@Override
	public String getName() {
		return "Super heat";
	}

	@Override
	public int getLevel() {
		return 43;
	}

	@Override
	public Item[] getRunes() {
		return new Item[] { new Item(554, 1), new Item(561, 1) };
	}

	@Override
	public void execute(Player player, Item item) {
		if (player.spellbook != Spellbook.MODERN)
			return;
        if (!player.spellCasting.castingDelay.elapsed(599))
            return;

		Optional<SmeltingData> data = SmeltingData.getDefinitionByItem(item.getId());

		if (!data.isPresent()) {
			player.message("You can not super heat this item!");
			return;
		}

		if (!player.inventory.containsAll(data.get().required)) {
			player.message("You do not contain the required items to super heat!");
			return;
		}

		if (player.skills.getMaxLevel(Skill.SMITHING) < data.get().requirement) {
			player.message("You need a smithing level of " + data.get().requirement + " to do super heat this item!");
			return;
		}

		player.animate(722);
		player.graphic(new Graphic(148, false));
		player.send(new SendForceTab(6));
		player.inventory.removeAll(data.get().required);
		player.inventory.addAll(data.get().produced);
		player.skills.addExperience(Skill.MAGIC, 53* Config.MAGIC_MODIFICATION);
		player.skills.addExperience(Skill.SMITHING, data.get().experience * Config.SMITHING_MODIFICATION);
		player.spellCasting.castingDelay.reset();
	}
}
