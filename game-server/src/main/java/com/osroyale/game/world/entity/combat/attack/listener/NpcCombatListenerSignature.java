package com.osroyale.game.world.entity.combat.attack.listener;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Listens for npcs.
 * @author Michael | Chex
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface NpcCombatListenerSignature {

	/**
	 * The NPC ids to attach this listener to.
	 * @return the npc ids
	 */
	int[] npcs();

}
