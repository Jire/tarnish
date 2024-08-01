package com.osroyale.net.packet.in;

import com.google.common.collect.ImmutableSet;
import com.osroyale.game.world.entity.mob.player.Player;
import com.osroyale.game.world.entity.mob.player.PlayerRight;
import com.osroyale.game.world.entity.mob.player.appearance.Appearance;
import com.osroyale.net.packet.GamePacket;
import com.osroyale.net.packet.PacketListener;
import com.osroyale.net.packet.PacketListenerMeta;
import com.osroyale.net.packet.out.SendMessage;
import org.jire.tarnishps.event.widget.AppearanceChangeEvent;

/**
 * The packet responsible for changing a characters appearance.
 *
 * @author nshusa
 */
@PacketListenerMeta(101)
public final class AppearanceChangePacketListener implements PacketListener {

	private static final ImmutableSet<Integer> VALID_MALE_HEAD_MODELS = ImmutableSet.of(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 129, 130, 131, 132, 133, 134, 142, 144, 145, 146, 147, 148, 149, 150);
	private static final ImmutableSet<Integer> VALID_MALE_JAW_MODELS = ImmutableSet.of(10, 11, 12, 13, 14, 15, 16, 17, 111, 112, 113, 114, 115, 116, 117);
	private static final ImmutableSet<Integer> VALID_MALE_TORSO_MODELS = ImmutableSet.of(18, 19, 20, 21, 22, 23, 24, 25, 105, 106, 107, 108, 109, 110);
	private static final ImmutableSet<Integer> VALID_MALE_ARM_MODELS = ImmutableSet.of(26, 27, 28, 29, 30, 31, 32, 84, 85, 86, 87, 88);
	private static final ImmutableSet<Integer> VALID_MALE_LEG_MODLES = ImmutableSet.of(36, 37, 38, 39, 40, 41, 100, 101, 102, 103, 104);
	private static final ImmutableSet<Integer> VALID_FEMALE_HEAD_MODELS = ImmutableSet.of(45, 46, 47, 48, 49, 50, 51, 52, 53, 54, 55, 118, 119, 120, 121, 122, 123, 124, 125, 126, 127, 128, 141, 143);
	private static final ImmutableSet<Integer> VALID_FEMALE_TORSO_MODELS = ImmutableSet.of(56, 57, 58, 59, 60, 89, 90, 91, 92, 93, 94);
	private static final ImmutableSet<Integer> VALID_FEMALE_ARM_MODELS = ImmutableSet.of(61, 62, 63, 64, 65, 66, 95, 96, 97, 98, 99);
	private static final ImmutableSet<Integer> VALID_FEMALE_LEG_MODELS = ImmutableSet.of(70, 71, 72, 73, 74, 75, 76, 77, 78, 135, 136, 137, 138, 139, 140);

	@Override
	public void handlePacket(Player player, GamePacket packet) {
		final int gender = packet.readByte(false);
		final int head = packet.readByte(false);
		final int jaw = packet.readByte(false);
		final int torso = packet.readByte(false);
		final int arms = packet.readByte(false);
		final int hands = packet.readByte(false);
		final int legs = packet.readByte(false);
		final int feet = packet.readByte(false);
		final int hairColor = packet.readByte(false);
		final int torsoColor = packet.readByte(false);
		final int legsColor = packet.readByte(false);
		final int feetColor = packet.readByte(false);
		final int skinColor = packet.readByte(false);

		player.getEvents().widget(player, new AppearanceChangeEvent(
				gender, head, jaw, torso, arms, hands, legs, feet,
				hairColor, torsoColor, legsColor, feetColor, skinColor
		));
	}

	public static boolean isValid(Player player, Appearance appearance) {
		switch (appearance.getGender()) {

			case MALE:

				if (!VALID_MALE_HEAD_MODELS.contains(appearance.getHead())) {
					player.send(new SendMessage(PlayerRight.isDeveloper(player) ? String.format("Cannot change appearance - Invalid head id %d", appearance.getHead()) : "You cannot change your appearance, because you have an invalid head id."));
					return false;
				}

				if (!VALID_MALE_JAW_MODELS.contains(appearance.getBeard())) {
					player.send(new SendMessage(PlayerRight.isDeveloper(player) ? String.format("Cannot change appearance - Invalid jaw id %d", appearance.getBeard()) : "You cannot change your appearance, because you have an invalid jaw id."));
					return false;
				}

				if (!VALID_MALE_TORSO_MODELS.contains(appearance.getTorso())) {
					player.send(new SendMessage(PlayerRight.isDeveloper(player) ? String.format("Cannot change appearance - Invalid torso id %d", appearance.getTorso()) : "You cannot change your appearance, because you have an invalid torso id."));
					return false;
				}

				if (!VALID_MALE_ARM_MODELS.contains(appearance.getArms())) {
					player.send(new SendMessage(PlayerRight.isDeveloper(player) ? String.format("Cannot change appearance - Invalid arm id %d", appearance.getHead()) : "You cannot change your appearance, because you have an invalid arm id."));
					return false;
				}

				if (!(appearance.getHands() >= 33 && appearance.getHands() <= 34)) {
					player.send(new SendMessage(PlayerRight.isDeveloper(player) ? String.format("Cannot change appearance - Invalid hand id %d", appearance.getHands()) : "You cannot change your appearance, because you have an invalid hand id."));
					return false;
				}

				if (!VALID_MALE_LEG_MODLES.contains(appearance.getLegs())) {
					player.send(new SendMessage(PlayerRight.isDeveloper(player) ? String.format("Cannot change appearance - Invalid leg id %d", appearance.getLegs()) : "You cannot change your appearance, because you have an invalid leg id."));
					return false;
				}

				if (!(appearance.getFeet() >= 42 && appearance.getFeet() <= 43)) {
					player.send(new SendMessage(PlayerRight.isDeveloper(player) ? String.format("Cannot change appearance - Invalid feet id %d", appearance.getFeet()) : "You cannot change your appearance, because you have an invalid feet id."));
					return false;
				}
				break;

			case FEMALE:
				if (!VALID_FEMALE_HEAD_MODELS.contains(appearance.getHead())) {
					player.send(new SendMessage(PlayerRight.isDeveloper(player) ? String.format("Cannot change appearance - Invalid head id %d", appearance.getHead()) : "You cannot change your appearance, because you have an invalid head id."));
					return false;
				}

				if (appearance.getBeard() != -1) {
					player.send(new SendMessage(PlayerRight.isDeveloper(player) ? String.format("Cannot change appearance - Invalid jaw id %d", appearance.getBeard()) : "You cannot change your appearance, because you have an invalid jaw id."));
					return false;
				}

				if (!VALID_FEMALE_TORSO_MODELS.contains(appearance.getTorso())) {
					player.send(new SendMessage(PlayerRight.isDeveloper(player) ? String.format("Cannot change appearance - Invalid torso id %d", appearance.getTorso()) : "You cannot change your appearance, because you have an invalid torso id."));
					return false;
				}

				if (!VALID_FEMALE_ARM_MODELS.contains(appearance.getArms())) {
					player.send(new SendMessage(PlayerRight.isDeveloper(player) ? String.format("Cannot change appearance - Invalid arm id %d", appearance.getHead()) : "You cannot change your appearance, because you have an invalid arm id."));
					return false;
				}

				if (!(appearance.getHands() >= 67 && appearance.getHands() <= 68)) {
					player.send(new SendMessage(PlayerRight.isDeveloper(player) ? String.format("Cannot change appearance - Invalid hand id %d", appearance.getHands()) : "You cannot change your appearance, because you have an invalid hand id."));
					return false;
				}

				if (!VALID_FEMALE_LEG_MODELS.contains(appearance.getLegs())) {
					player.send(new SendMessage(PlayerRight.isDeveloper(player) ? String.format("Cannot change appearance - Invalid leg id %d", appearance.getLegs()) : "You cannot change your appearance, because you have an invalid leg id."));
					return false;
				}

				if (!(appearance.getFeet() >= 79 && appearance.getFeet() <= 80)) {
					player.send(new SendMessage(PlayerRight.isDeveloper(player) ? String.format("Cannot change appearance - Invalid feet id %d", appearance.getFeet()) : "You cannot change your appearance, because you have an invalid feet id."));
					return false;
				}
				break;
		}

		if (!(appearance.getHairColor() >= 0 && appearance.getHairColor() <= 11)) {
			player.send(new SendMessage(PlayerRight.isDeveloper(player) ? String.format("Cannot change appearance - Invalid hair color %d", appearance.getHairColor()) : "Cannot change appearance, invalid hair color."));
			return false;
		}

		if (!(appearance.getTorsoColor() >= 0 && appearance.getTorsoColor() <= 15)) {
			player.send(new SendMessage(PlayerRight.isDeveloper(player) ? String.format("Cannot change appearance, invalid torso color %d", appearance.getTorsoColor()) : "Cannot change appearance, invalid torso color."));
			return false;
		}

		if (!(appearance.getLegsColor() >= 0 && appearance.getLegsColor() <= 15)) {
			player.send(new SendMessage(PlayerRight.isDeveloper(player) ? String.format("Cannot change appearance, invalid leg color %d", appearance.getLegsColor()) : "Cannot change appearance, invalid leg color."));
			return false;
		}

		if (!(appearance.getFeetColor() >= 0 && appearance.getFeetColor() <= 5)) {
			player.send(new SendMessage(PlayerRight.isDeveloper(player) ? String.format("Cannot change appearance, invalid feet color %d", appearance.getFeetColor()) : "Cannot change appearance, invalid feet color."));
			return false;
		}

		if (!(appearance.getSkinColor() >= 0 && appearance.getSkinColor() <= 9)) {
			player.send(new SendMessage(PlayerRight.isDeveloper(player) ? String.format("Cannot change appearance, invalid skin color %d", appearance.getSkinColor()) : "Cannot change appearance, invalid skin color."));
			return false;
		}

		int[] DONATOR_SKINS = { 8, 9 };

		for (int skin : DONATOR_SKINS) {
			if (appearance.getSkinColor() == skin && !PlayerRight.isDonator(player)) {
				player.message("You need to be a donator to use this skin!");
			}
		}
		return true;
	}

}
