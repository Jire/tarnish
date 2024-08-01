package com.osroyale;

import com.osroyale.engine.impl.MouseHandler;

import java.text.NumberFormat;

/**
 * Created by Daniel on 2017-10-24.
 */
public class SkillOrb {

	/**
	 * The skill this orb is intended for.
	 */
	private final int skill;

	/**
	 * The sprite icon for this skill orb.
	 */
	private final Sprite icon;

	/**
	 * The show timer. Resets when this orb receives experience.
	 */
	private SecondsTimer showTimer = new SecondsTimer();

	/**
	 * The orb's current alpha (transparency)
	 */
	private int alpha;

	/**
	 * Constructs this skill orb
	 */
	public SkillOrb(int skill, Sprite icon) {
		this.skill = skill;
		this.icon = icon;
	}

	/**
	 * Called upon the player receiving experience.
	 * <p>
	 * Resets the attributes of the orb
	 * to make sure the orb is drawn
	 * properly.
	 */
	public void receivedExperience() {
		alpha = 255;
		showTimer.start(5);
	}

	/**
	 * Draws this skill orb
	 */
	public void draw(int x, int y) {
		final int percentProgress = percentage();
		Client.spriteCache.get(743).drawARGBSprite(x, y, alpha);
		Rasterizer2D.setDrawingArea2(60 + y, x + 1, x + 30, 45 - (currentLevel() >= 99 ? 100 : percentProgress) + 1 + y);
		Client.spriteCache.get(744).drawARGBSprite(x + 1, 1 + y, alpha);
		Rasterizer2D.setDrawingArea2((currentLevel() >= 99 ? 100 : percentProgress) - 38 + y, x + 30, x + 56, 1 + y);
		Client.spriteCache.get(744).drawARGBSprite(x + 2, 1 + y, alpha);
		Rasterizer2D.defaultDrawingAreaSize();
		icon.drawARGBSprite(x + 30 - icon.width / 2, 28 - icon.height / 2 + y, alpha);
	}

	/**
	 * Draws a tooltip containing information about
	 * this skill orb.
	 */
	public void drawTooltip() {
		NumberFormat nf = NumberFormat.getInstance();
		int mouseX = MouseHandler.mouseX;
		int mouseY = MouseHandler.mouseY;
		int experience = Client.instance.currentExp[skill];
		int level = Client.instance.maxStats[skill];
		int maxExperience = SkillConstants.getExperienceForLevel(level + 1);
		int completion = experience * 100 / maxExperience;
		int percentage = completion * 119 / 100;
		if(completion > 100)
			completion = 100;

		Rasterizer2D.drawTransparentBox(mouseX + 1, mouseY + 6, 122, 82, 0x7D5C51, 150);
		Rasterizer2D.drawRectangle(mouseX, mouseY + 5, 122, 82, 0x000000);

		Client.instance.newSmallFont.drawBasicString(SkillConstants.SKILL_NAMES[skill], mouseX + Client.instance.newSmallFont.getTextWidth(SkillConstants.SKILL_NAMES[skill]) - 25, mouseY + 20, 16777215, 1);
		Client.instance.newSmallFont.drawBasicString("Level: @gre@" + Client.instance.maxStats[skill], mouseX + 5, mouseY + 35, 16777215, 1);
		Client.instance.newSmallFont.drawBasicString("Exp: @gre@" + nf.format(Client.instance.currentExp[skill]), mouseX + 5, mouseY + 50, 16777215, 1);

		Client.instance.newSmallFont.drawBasicString("Exp Left: @gre@" + nf.format(remainderExp()), mouseX + 5, mouseY + 65, 16777215, 1);

		Rasterizer2D.drawRoundedRectangle(mouseX, mouseY + 70, 121, 15, 0xED4747, 100, true, true);

		if (percentage > 121)
			percentage = 121;

		Rasterizer2D.fillRectangle(mouseX + 2, mouseY + 70, 119, 15, 0xDB2323);
		Rasterizer2D.fillRectangle(mouseX + 1, mouseY + 70, percentage, 15, 0x37A351);
		Client.instance.newSmallFont.drawCenteredString(completion + "% ", mouseX + 118 / 2 + 10, mouseY + 83, 0xFFFFFF, 1);
	}

	private int currentLevel() {
		return Client.instance.maxStats[skill];
	}

	private int startExp() {
		return Client.getXPForLevel(currentLevel());
	}

	private int requiredExp() {
		return Client.getXPForLevel(currentLevel() + 1);
	}

	private int obtainedExp() {
		return Client.instance.currentExp[skill] - startExp();
	}

	private int remainderExp() {
		return requiredExp() - (startExp() + obtainedExp());
	}

	private int percentage() {
		int percent = 0;
		try {
			percent = (int) (((double) obtainedExp() / (double) (requiredExp() - startExp())) * 100);
			if (percent > 100) {
				percent = 100;
			}
		} catch (ArithmeticException e) {
			e.printStackTrace();
		}
		return percent;
	}

	public SecondsTimer getShowTimer() {
		return showTimer;
	}

	public int getSkill() {
		return skill;
	}

	public int getAlpha() {
		return alpha;
	}

	public void decrementAlpha() {
		alpha -= 5;
	}
}
