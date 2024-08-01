package com.osroyale.content.event.impl;

import com.osroyale.game.world.entity.mob.npc.Npc;
import com.osroyale.content.event.InteractionEvent;

public class NpcInteractionEvent extends InteractionEvent {

	private final Npc npc;
	private final int opcode;

	public NpcInteractionEvent(InteractionType type, Npc npc, int opcode) {
		super(type);
		this.npc = npc;
		this.opcode = opcode;
	}

	public Npc getNpc() {
		return npc;
	}

	public int getOpcode() {
		return opcode;
	}

}
