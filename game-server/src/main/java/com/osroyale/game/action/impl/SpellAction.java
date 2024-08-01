package com.osroyale.game.action.impl;

import com.osroyale.content.skill.impl.magic.spell.Spell;
import com.osroyale.game.action.Action;
import com.osroyale.game.action.policy.WalkablePolicy;
import com.osroyale.game.world.entity.mob.player.Player;
import com.osroyale.game.world.items.Item;

/**
 * Handles the spell casting action.
 * @author Daniel
 */
public final class SpellAction extends Action<Player> {
	private final Spell spell;
	private final Item item;

	/**  Creates the <code>SpellAction</code>.  */
	public SpellAction(Player player, Spell spell, Item item) {
		super(player, 3);
		this.spell = spell;
		this.item = item;
	}

	@Override
	public void onSchedule() {
		spell.execute(getMob(), item);
	}

	@Override
	public void execute() {
		cancel();
	}

	@Override
	public String getName() {
		return "spell-action";
	}

	@Override
	public boolean prioritized() {
		return true;
	}

	@Override
	public WalkablePolicy getWalkablePolicy() {
		return WalkablePolicy.WALKABLE;
	}

}
