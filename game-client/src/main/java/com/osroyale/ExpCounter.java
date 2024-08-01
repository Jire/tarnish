package com.osroyale;

import com.osroyale.engine.impl.MouseHandler;

import java.text.NumberFormat;
import java.util.Queue;
import java.util.*;

public class ExpCounter {
	private static final int START_SPRITE = 82;
	private static final int START = 130;
	private static final int STOP = 35;
	private static final int MIDLINE = (START + STOP) / 2;
	private static int xpCounter;
	private static final LinkedList<ExpGain> GAINS = new LinkedList<>();
	private static ExpGain currentGain = null;
	static int lastSkill = -1;

	static void addXP(int skill, int xp, boolean increment) {
		if (skill == 99) {
			xpCounter = xp;
		} else {
			if (increment) {
				xpCounter += xp;
				lastSkill = skill;
			}
			if (xp != 0) {
				if (currentGain != null && Math.abs(currentGain.getY() - START) <= getSize2(Settings.COUNTER_SIZE).baseCharacterHeight) {
					currentGain.xp += xp;
					currentGain.addSprite(skill);
				} else {
					ExpGain gain = new ExpGain(skill, xp);
					GAINS.add(gain);
					currentGain = gain;
				}
			}
		}
	}

	static void drawExperienceCounter() {
		boolean isFixed = !Client.instance.isResized();

		int x = 0, y = 12;
		int boxWidth = Client.spriteCache.get(451).width;
		int skillWidth = Client.spriteCache.get(81).width;

		if (Settings.COUNTER_POSITION == 0) {
			x = Client.canvasWidth - boxWidth - 255;
		} else if (Settings.COUNTER_POSITION == 1) {
			x = (Client.canvasWidth - boxWidth - (isFixed ? 255 : 0)) / 2;
		} else if (Settings.COUNTER_POSITION == 2) {
			x = 2;
		}


		int backgroundSprite = Settings.COUNTER_PROGRESS ? 451 : 777;
		Client.spriteCache.get(lastSkill == -1 ? 777: backgroundSprite).drawTransparentSprite(x, 2, 230);
		Client.spriteCache.get(81).drawSprite(x + 4, 6);

		if (xpCounter >= 0) {
			TextDrawingArea text = getSize(Settings.COUNTER_SIZE);
			int xPos = x + (skillWidth + boxWidth) / 2;
			String string = NumberFormat.getInstance().format(xpCounter);
			text.drawCenteredText(Settings.COUNTER_COLOR, xPos, string, 23 + Settings.COUNTER_SIZE, true);
		}

		if (Settings.COUNTER_PROGRESS && lastSkill != -1 && lastSkill <= SkillConstants.SKILL_COUNT) {
			int level = Client.instance.maxStats[lastSkill];
			int experience = Client.instance.currentExp[lastSkill];
			int startExp = SkillConstants.getExperienceForLevel(level);
			int endExp = SkillConstants.getExperienceForLevel(level + 1);

//			if (level < 1 || level > 99)
//				return;

			if (endExp < 0)
				endExp = 1;

			int remainder = endExp - experience;
			int percentage = (int) (100D * (experience - startExp) / (endExp - startExp));
			int width = (int) (136D * (experience - startExp) / (endExp - startExp));

			if (percentage > 100)
				percentage = 100;

			if (width > 136)//login and show when it errors
				width = 136;

			if (width < 0)
				width = 0;

			Rasterizer2D.fillRectangle(x + 2, 32, width, 12, 0x37A351);
			Client.instance.smallFont.drawCenteredText(0xFFFFFF, x + 70, percentage + "%", 43, true);

			if (Client.instance.hover(x, y, Client.spriteCache.get(backgroundSprite))) {
				NumberFormat nf = NumberFormat.getInstance();
				int mouseX = MouseHandler.mouseX;
				int mouseY = MouseHandler.mouseY;

				if (isFixed && mouseX > 393)
					mouseX = 393;

				Rasterizer2D.drawTransparentBox(mouseX + 1, mouseY + 6, 113, level < 99 ? 78 : 48, 0x7A5E3D, 185);
				Rasterizer2D.drawRectangle(mouseX, mouseY + 5, 115, level < 99 ? 80 : 50, 0);

				String skillName = SkillConstants.SKILL_NAMES[lastSkill];
				Client.instance.newSmallFont.drawBasicString("Skill: @gre@" + skillName, mouseX + Client.instance.newSmallFont.getTextWidth(skillName) - 25, mouseY + 20, 16777215, 1);
				Client.instance.newSmallFont.drawBasicString("Level: @gre@" + level, mouseX + 3, mouseY + 35, 16777215, 1);
				Client.instance.newSmallFont.drawBasicString("Exp: @gre@" + nf.format(experience), mouseX + 3, mouseY + 50, 16777215, 1);
				if (level < 99) {
					Client.instance.newSmallFont.drawBasicString("Next Level: @gre@" + nf.format(endExp), mouseX + 3, mouseY + 65, 16777215, 1);
					Client.instance.newSmallFont.drawBasicString("Exp Left: @gre@" + nf.format(remainder), mouseX + 3, mouseY + 80, 16777215, 1);
				}
			}
		}

		if (!GAINS.isEmpty()) {
			Iterator<ExpGain> gained = GAINS.iterator();

			while (gained.hasNext()) {
				ExpGain gain = gained.next();

				if (gain.getY() > STOP) {

					if (gain.getY() >= MIDLINE) {
						gain.increaseAlpha();
					} else {
						gain.decreaseAlpha();
					}

					gain.changeY();

				} else if (gain.getY() <= STOP) {
					gained.remove();
				}

				if (gain.getY() > STOP) {
					Queue<ExpSprite> temp = new PriorityQueue<>(gain.sprites);
					int dx = 0;

					while (!temp.isEmpty()) {
						ExpSprite expSprite = temp.poll();
						expSprite.sprite.drawSprite1(x + dx, (int) (y + gain.getY()), gain.getAlpha());
						dx += expSprite.sprite.width + 1;
					}
					String drop = String.format("<trans=%s>%,d", gain.getAlpha(), gain.getXP());
					getSize2(Settings.COUNTER_SIZE).drawBasicString(drop, x + dx + 2, (int) (gain.getY() + y) + 14, Settings.COUNTER_COLOR, 0);
				}
			}
		}
	}


	private static TextDrawingArea getSize(int size) {
		if (size == 0)
			return Client.instance.smallFont;
		if (size == 2)
			return Client.instance.boldText;
		return Client.instance.regularText;
	}

	private static RSFont getSize2(int size) {
		if (size == 0)
			return Client.instance.newSmallFont;
		if (size == 2)
			return Client.instance.newBoldFont;
		return Client.instance.newRegularFont;
	}

	static class ExpSprite implements Comparable<ExpSprite> {
		private int skill;
		private Sprite sprite;

		ExpSprite(int skill, Sprite sprite) {
			this.skill = skill;
			this.sprite = sprite;
		}

		@Override
		public int compareTo(ExpSprite other) {
			return Integer.signum(other.skill - skill);
		}
	}

	static class ExpGain {
		private int skill;
		private int xp;
		private float y;
		private double alpha = 0;
		private Set<ExpSprite> sprites = new TreeSet<>();

		ExpGain(int skill, int xp) {
			this.skill = skill;
			this.xp = xp;
			this.y = START;
			addSprite(skill);
		}

		void addSprite(int skill) {
			for (ExpSprite sprite : sprites) {
				if (sprite.skill == skill) {
					return;
				}
			}

			int spriteId = START_SPRITE + skill;
			if (skill == 21) { // construction
				spriteId = 104;
			} else if (skill == 22) { // hunter
				spriteId = 103;
			}

			sprites.add(new ExpSprite(skill, Client.spriteCache.get(spriteId)));
		}

		void changeY() {
			y -= Settings.COUNTER_SPEED;
		}

		int getXP() {
			return xp;
		}

		public float getY() {
			return y;
		}

		public int getAlpha() {
			return (int) alpha;
		}

		void increaseAlpha() {
			alpha += alpha < 256 ? 30 : 0;
			alpha = alpha > 256 ? 256 : alpha;
		}

		void decreaseAlpha() {
			alpha -= (alpha > 0 ? 30 : 0) * 0.10;
			alpha = alpha > 256 ? 256 : alpha;
		}
	}
}