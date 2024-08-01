package com.osroyale.content.emote;

import com.osroyale.game.action.impl.EmoteAction;
import com.osroyale.game.world.entity.mob.player.Player;
import com.osroyale.game.world.items.Item;
import com.osroyale.game.world.items.containers.equipment.Equipment;
import com.osroyale.net.packet.out.SendMessage;
import com.osroyale.util.generic.BooleanInterface;

import java.util.Arrays;
import java.util.Optional;

/**
 * Holds all the emote data.
 * 
 * @author Daniel
 */
public enum Emote implements BooleanInterface<Player> {
	YES(168, 855, -1, -1) {
		@Override
		public boolean activated(Player player) {
			return true;
		}
	},
	NO(169, 856, -1, -1) {
		@Override
		public boolean activated(Player player) {
			return true;
		}
	},
	BOW(164, 858, -1, -1) {
		@Override
		public boolean activated(Player player) {
			return true;
		}
	},
	ANGRY(167, 864, -1, -1) {
		@Override
		public boolean activated(Player player) {
			return true;
		}
	},
	THINK(162, 857, -1, -1) {
		@Override
		public boolean activated(Player player) {
			return true;
		}
	},
	WAVE(163, 863, -1, -1) {
		@Override
		public boolean activated(Player player) {
			return true;
		}
	},
	SHRUG(13370, 2113, -1, -1) {
		@Override
		public boolean activated(Player player) {
			return true;
		}
	},
	CHEER(171, 862, -1, -1) {
		@Override
		public boolean activated(Player player) {
			return true;
		}
	},
	BECKON(165, 859, -1, -1) {
		@Override
		public boolean activated(Player player) {
			return true;
		}
	},
	LAUGH(170, 861, -1, -1) {
		@Override
		public boolean activated(Player player) {
			return true;
		}
	},
	JUMP_FOR_JOY(13366, 2109, -1, -1) {
		@Override
		public boolean activated(Player player) {
			return true;
		}
	},
	YAWN(13368, 2111, -1, -1) {
		@Override
		public boolean activated(Player player) {
			return true;
		}
	},
	DANCE(166, 866, -1, -1) {
		@Override
		public boolean activated(Player player) {
			return true;
		}
	},
	JIG(13363, 2106, -1, -1) {
		@Override
		public boolean activated(Player player) {
			return true;
		}
	},
	TWIRL(13364, 2107, -1, -1) {
		@Override
		public boolean activated(Player player) {
			return true;
		}
	},
	HEADBANG(13365, 2108, -1, -1) {
		@Override
		public boolean activated(Player player) {
			return true;
		}
	},
	CRY(161, 860, -1, -1) {
		@Override
		public boolean activated(Player player) {
			return true;
		}
	},
	BLOW_KISS(11100, 0x558, -1, -1) {
		@Override
		public boolean activated(Player player) {
			return true;
		}
	},
	PANIC(13362, 2105, -1, -1) {
		@Override
		public boolean activated(Player player) {
			return true;
		}
	},
	RASBERRY(13367, 2110, -1, -1) {
		@Override
		public boolean activated(Player player) {
			return true;
		}
	},
	CLAP(172, 865, -1, -1) {
		@Override
		public boolean activated(Player player) {
			return true;
		}
	},
	SALUTE(13369, 2112, -1, -1) {
		@Override
		public boolean activated(Player player) {
			return true;
		}
	},
	GOBLIN_BOW(13383, 0x84F, -1, -1) {
		@Override
		public boolean activated(Player player) {
			return true;
		}
	},
	GOBLIN_SALUTE(13384, 0x850, -1, -1) {
		@Override
		public boolean activated(Player player) {
			return true;
		}
	},

	GLASS_BOX(18717, 0x46B, -1, 1117) {
		@Override
		public boolean activated(Player player) {
			return player.emoteUnlockable.contains(EmoteUnlockable.GLASS_BOX);
		}
	},
	CLIMB_ROPE(18718, 0x46A, -1, 1118) {
		@Override
		public boolean activated(Player player) {
			return player.emoteUnlockable.contains(EmoteUnlockable.CLIMB_ROPE);
		}
	},
	LEAN(18719, 0x469, -1, 1119) {
		@Override
		public boolean activated(Player player) {
			return player.emoteUnlockable.contains(EmoteUnlockable.LEAN);
		}
	},
	GLASS_WALL(18720, 0x468, -1, 1120) {
		@Override
		public boolean activated(Player player) {
			return player.emoteUnlockable.contains(EmoteUnlockable.GLASS_WALL);
		}
	},

	IDEA(18700, 4276, 712, 1100) {
		@Override
		public boolean activated(Player player) {
			return player.emoteUnlockable.contains(EmoteUnlockable.IDEA);
		}
	},
	STOMP(18701, 4278, -1, 1101) {
		@Override
		public boolean activated(Player player) {
			return player.emoteUnlockable.contains(EmoteUnlockable.STOMP);
		}
	},
	FLAP(18702, 4280, -1, 1102) {
		@Override
		public boolean activated(Player player) {
			return player.emoteUnlockable.contains(EmoteUnlockable.FLAP);
		}
	},
	SLAP_HEAD(18703, 4275, -1, 1103) {
		@Override
		public boolean activated(Player player) {
			return player.emoteUnlockable.contains(EmoteUnlockable.SLAP);
		}
	},
	ZOMBIE_WALK(18704, 3544, -1, 1104) {
		@Override
		public boolean activated(Player player) {
			return player.emoteUnlockable.contains(EmoteUnlockable.ZOMBIE_WALK);
		}
	},
	ZOMBIE_DANCE(18705, 3543, -1, 1105) {
		@Override
		public boolean activated(Player player) {
			return player.emoteUnlockable.contains(EmoteUnlockable.ZOMBIE_DANCE);
		}
	},
	SCARED(18706, 2836, -1, 1106) {
		@Override
		public boolean activated(Player player) {
			return player.emoteUnlockable.contains(EmoteUnlockable.SCARED);
		}
	},
	RABBIT_HOP(18707, 6111, -1, 1107) {
		@Override
		public boolean activated(Player player) {
			return player.emoteUnlockable.contains(EmoteUnlockable.BUNNY_HOP);
		}
	},
	SIT_UP(18708, 2763, -1, 1108) {
		@Override
		public boolean activated(Player player) {
			return player.emoteUnlockable.contains(EmoteUnlockable.SIT_UP);
		}
	},
	PUSH_UP(18709, 2756, -1, 1109) {
		@Override
		public boolean activated(Player player) {
			return player.emoteUnlockable.contains(EmoteUnlockable.PUSH_UP);
		}
	},
	STAR_JUMP(18710, 2761, -1, 1110) {
		@Override
		public boolean activated(Player player) {
			return player.emoteUnlockable.contains(EmoteUnlockable.JUMPING_JACK);
		}
	},
	JOG(18711, 2764, -1, 1111) {
		@Override
		public boolean activated(Player player) {
			return player.emoteUnlockable.contains(EmoteUnlockable.JOG);
		}
	},
	ZOMBIE_HAND(18712, 4513, 320, 1112) {
		@Override
		public boolean activated(Player player) {
			return player.emoteUnlockable.contains(EmoteUnlockable.DEMON_HAND);
		}
	},
	HYPERMOBILE_DRINKER(18713, 7131, -1, 1113) {
		@Override
		public boolean activated(Player player) {
			return player.emoteUnlockable.contains(EmoteUnlockable.DRINK);
		}
	},
	AIR_GUITAR(18715, -1, 1239, 1115) {
		@Override
		public boolean activated(Player player) {
			return player.emoteUnlockable.contains(EmoteUnlockable.GUITAR);
		}
	},
	URI_TRANSFORM(18716, -1, -1, 1116) {
		@Override
		public boolean activated(Player player) {
			return player.emoteUnlockable.contains(EmoteUnlockable.URI);
		}
	};

	/** The button identification. */
	private final int button;

	/** The emote animation. */
	private final int animation;

	/** The emote graphic. */
	private final int graphic;

	/** The emote configuration. */
	private final int config;

	/** Constructs a new <code>Emote<code>.  */
	Emote(int button, int animation, int graphic, int config) {
		this.button = button;
		this.animation = animation;
		this.graphic = graphic;
		this.config = config;
	}

	public static void skillcape(Player player) {
		Item item = player.equipment.get(Equipment.CAPE_SLOT);

		if (item == null) {
			player.send(new SendMessage("You must be wearing a skillcape to perform this emote!"));
			return;
		}

		Skillcape skillcape = Skillcape.forId(item.getId());

		if (skillcape == null) {
			player.send(new SendMessage("You must be wearing a skillcape to perform this emote!"));
			return;
		}
		player.locking.lock(30);
		execute(player, skillcape.getAnimation(), skillcape.getGraphic());
		player.locking.unlock();
	}

	public static void execute(Player player, int animation, int graphic) {
		player.locking.lock(30);
		player.action.execute(new EmoteAction(player, animation, graphic), false);
		player.locking.unlock();
	}

	/** Gets the button identification of the emote.  */
	public int getButton() {
		return button;
	}

	/** Gets the animation of the the emote.  */
	public int getAnimation() {
		return animation;
	}

	/** Gets the graphic of the emote. */
	public int getGraphic() {
		return graphic;
	}

	/** Gets the config of the emote. */
	public int getConfig() {
		return config;
	}

	/** Gets emote data corresponding the the button identification. */
	public static Optional<Emote> forId(int button) {
		return Arrays.stream(values()).filter(a -> a.button == button).findAny();
	}
}