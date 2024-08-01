package com.osroyale.login.impl;

import com.osroyale.*;
import com.osroyale.engine.impl.MouseHandler;
import com.osroyale.login.LoginComponent;
import com.osroyale.login.ScreenType;

/**
 * Handles the setting screen of login.
 *
 * @author Daniel
 */
public class SettingScreen extends LoginComponent {

	@Override
	public void render(Client client) {
		int centerX = getX();
		int centerY = getY();
		refresh(client);
		load(client, 13);

          /* Background */
		Client.spriteCache.get(678).drawTransparentSprite((Client.canvasWidth / 2) - (Client.spriteCache.get(57).width / 2), (Client.canvasHeight / 2) - (Client.spriteCache.get(57).height / 2), client.loginTick);

		client.boldText.drawCenteredText(0xCB9535, centerX + 5, Configuration.NAME, centerY - 105, true);
		client.regularText.drawCenteredText(0xCB9535, centerX + 5, "Account Manager", centerY - 85, true);

		//announcement
		client.regularText.drawCenteredText(0xFFFFFF, centerX + 5, "Announcement:", centerY - 50, true);
		if (client.mouseInRegion(centerX - 47, centerY - 37, centerX + 51, centerY - 9)) {
			Rasterizer2D.fillRectangle(336, 215, 100, 30, 0xCB9535, 105);
			Rasterizer2D.drawRectangle(336, 215, 100, 30, 0xCB9535);
		} else {
			Rasterizer2D.fillRectangle(336, 215, 100, 30, 0xC47423, 105);
			Rasterizer2D.drawRectangle(336, 215, 100, 30, 0xCB9535);
		}
		client.smallFont.drawCenteredText(Utility.getPrefix(Settings.DRAW_ANNOUNCEMENT), centerX + 5, (Settings.DRAW_ANNOUNCEMENT ? "Enabled" : "Disabled"), centerY - 16, true);

		//bubbles
		client.regularText.drawCenteredText(0xFFFFFF, centerX + 5, "Bubbles:", centerY + 20, true);
		if (client.mouseInRegion(centerX - 47, centerY + 24, centerX + 58, centerY + 64)) {
			Rasterizer2D.fillRectangle(336, 280, 100, 30, 0xCB9535, 105);
			Rasterizer2D.drawRectangle(336, 280, 100, 30, 0xCB9535);
		} else {
			Rasterizer2D.fillRectangle(336, 280, 100, 30, 0xC47423, 105);
			Rasterizer2D.drawRectangle(336, 280, 100, 30, 0xCB9535);
		}
		client.smallFont.drawCenteredText(Utility.getPrefix(Settings.DRAW_BUBBLE), centerX + 5, (Settings.DRAW_BUBBLE ? "Enabled" : "Disabled"), centerY + 49, true);

		//hover boxes
		client.regularText.drawCenteredText(0xFFFFFF, centerX + 5, "Tooltip Boxes:", centerY + 85, true);
		if (client.mouseInRegion(centerX - 47, centerY + 95, centerX + 58, centerY + 120)) {
			Rasterizer2D.fillRectangle(336, 345, 100, 30, 0xCB9535, 105);
			Rasterizer2D.drawRectangle(336, 345, 100, 30, 0xCB9535);
		} else {
			Rasterizer2D.fillRectangle(336, 345, 100, 30, 0xC47423, 105);
			Rasterizer2D.drawRectangle(336, 345, 100, 30, 0xCB9535);
		}
		client.smallFont.drawCenteredText(Utility.getPrefix(Settings.LOGINSCREEN_HOVER_BOXES), centerX + 5, (Settings.LOGINSCREEN_HOVER_BOXES ? "Enabled" : "Disabled"), centerY + 114, true);

        /* Announcement */
		announcement(client);

        /* Bubble */
		drawSetting(client);

        /* Other */
		client.smallFont.drawCenteredText(0xCB9535, centerX + 330, "Client Build: " + Configuration.CLIENT_VERSION, centerY + 245, true);
		if (Configuration.DEBUG_MODE) {
			client.smallFont.drawCenteredText(0xFFFFFF, centerX + 300, "MouseX: " + (MouseHandler.mouseX - (centerX)) + " Mouse Y: " + (MouseHandler.mouseY - (centerY)), centerY - 225, true);
		}


	}

	@Override
	public void click(Client client) {
		int centerX = getX();
		int centerY = getY();

		if (MouseHandler.clickMode3 == 1 && client.mouseInRegion(centerX - 47, centerY - 37, centerX + 51, centerY - 9)) {
			Settings.DRAW_ANNOUNCEMENT = !Settings.DRAW_ANNOUNCEMENT;
		}

		if (MouseHandler.clickMode3 == 1 && client.mouseInRegion(centerX - 47, centerY + 24, centerX + 58, centerY + 64)) {
			Settings.DRAW_BUBBLE = !Settings.DRAW_BUBBLE;
		}

		if (MouseHandler.clickMode3 == 1 && client.mouseInRegion(centerX - 47, centerY + 95, centerX + 58, centerY + 120)) {
			Settings.LOGINSCREEN_HOVER_BOXES = !Settings.LOGINSCREEN_HOVER_BOXES;
		}

        /* Bubble */
		settingButton(client);
	}

	@Override
	public ScreenType type() {
		return ScreenType.SETTING;
	}
}
