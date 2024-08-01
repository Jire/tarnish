package com.osroyale;

/**
 * Created by Daniel on 2017-10-24.
 */
public class SkillOrbs {

	/**
	 * The array containing all skill orbs. Each skill orb per available skill.
	 */
	public static final SkillOrb[] orbs = new SkillOrb[SkillConstants.SKILL_COUNT];

	/**
	 * Initializes orbs and their sprites.
	 */
	public static void init() {
		for (int i = 0; i < SkillConstants.SKILL_COUNT; i++) {
			orbs[i] = new SkillOrb(i, Client.spriteCache.get(745 + i));
		}
	}

	/**
	 * Processes all orbs.
	 */
	static void process() {
		int totalOrbs = 0;

		for (SkillOrb orb : orbs) {
			if (draw(orb)) {
				totalOrbs++;
			}
		}

		//Is the bounty hunter interface open? Then the orbs may need to be re-positioned.
		final boolean blockingInterfaceOpen = /*Client.instance.openWalkableInterface == 23300 */false;
		boolean hpOverlay = /*Client.instance.shouldDrawCombatBox()*/ false;

		int y = 5;
		int x = (int) (Client.canvasWidth / (Settings.RESIZABLE ? 2.0 : 3.1)) - (totalOrbs * 30);

		if (blockingInterfaceOpen) {
			x -= (totalOrbs * 10);
		} else {
			if (hpOverlay) {
				if (x < 130) {
					x = 130;
				}
				y = 12;
			}
		}

		if (x < 5) {
			x = 5;
		}

		SkillOrb hover = null;

		for (SkillOrb orb : orbs) {
			if (draw(orb)) {
				if (orb.getShowTimer().finished()) {
					orb.decrementAlpha();
				}
				orb.draw(x, y);
				if (Client.instance.hover(x, y, Client.spriteCache.get(743))) {
					hover = orb;
				}
				x += 62;
				if (x > (blockingInterfaceOpen ? 300 : 460) && !Settings.RESIZABLE) {
					break;
				}
			}
		}

		if (hover != null) {
			hover.drawTooltip();
		}
	}

	/**
	 * Should a skillorb be drawn?
	 */
	private static boolean draw(SkillOrb orb) {
		return !orb.getShowTimer().finished() || orb.getAlpha() > 0;
	}
}
