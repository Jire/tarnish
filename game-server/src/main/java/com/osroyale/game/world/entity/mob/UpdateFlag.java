package com.osroyale.game.world.entity.mob;

import com.osroyale.game.world.entity.mob.npc.Npc;
import com.osroyale.game.world.entity.mob.player.Player;
import com.osroyale.net.packet.out.SendPlayerUpdate;

/**
 * Entity update flags.
 * 
 * @author Michael | Chex
 * @author Jire
 */
public enum UpdateFlag {
	APPEARANCE(0x10, -1),
	CHAT(0x80, -1) {
		@Override
		public boolean canApply(Player player, Player other, SendPlayerUpdate.UpdateState state) {
			return state != SendPlayerUpdate.UpdateState.UPDATE_SELF && other.getChatMessage().isPresent();
		}
	},
	GRAPHICS(0x100, 0x80) {
		@Override
		public boolean canApply(Player player, Player other, SendPlayerUpdate.UpdateState state) {
			return other.getGraphic().isPresent();
		}

		@Override
		public boolean canApply(Npc npc) {
			return npc.getGraphic().isPresent();
		}
	},
	ANIMATION(0x8, 0x10) {
		@Override
		public boolean canApply(Player player, Player other, SendPlayerUpdate.UpdateState state) {
			return other.getAnimation().isPresent();
		}

		@Override
		public boolean canApply(Npc npc) {
			return npc.getAnimation().isPresent();
		}
	},
	FORCED_CHAT(0x4, 0x1),
	INTERACT(0x1, 0x20),
	FACE_COORDINATE(0x2, 0x4),
	FIRST_HIT(0x20, 0x40),
	SECOND_HIT(0x200, 0x8),
	TRANSFORM(-1, 0x2),
	FORCE_MOVEMENT(0x400, -1) {
		@Override
		public boolean canApply(Player player, Player other, SendPlayerUpdate.UpdateState state) {
			return other.getForceMovement() != null;
		}
	};

	public final int playerMask;
	public final int npcMask;

	UpdateFlag(int playerMask, int npcMask) {
		this.playerMask = playerMask;
		this.npcMask = npcMask;
	}

	public static final UpdateFlag[] playerOrder = {
			FORCE_MOVEMENT,
			GRAPHICS,
			ANIMATION,
			FORCED_CHAT,
			CHAT,
			INTERACT,
			APPEARANCE,
			FACE_COORDINATE,
			FIRST_HIT,
			SECOND_HIT
	};

	public static final UpdateFlag[] npcOrder = {
			ANIMATION,
			GRAPHICS,
			INTERACT,
			FORCED_CHAT,
			FIRST_HIT,
			SECOND_HIT,
			TRANSFORM,
			FACE_COORDINATE
	};

	public static boolean contains(int masks, int mask) {
		return (masks & mask) != 0;
	}

	public static boolean containsPlayer(int masks, UpdateFlag flag) {
		return contains(masks, flag.playerMask);
	}

	public static boolean containsNpc(int masks, UpdateFlag flag) {
		return contains(masks, flag.npcMask);
	}

	public boolean canApply(Player player, Player other, SendPlayerUpdate.UpdateState state) {
		return true;
	}

	public boolean canApply(Npc npc) {
		return true;
	}
}
