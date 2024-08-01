/*
package com.osroyale.login.impl;

import com.osroyale.*;
import com.osroyale.login.LoginComponent;
import com.osroyale.login.ScreenType;

*/
/**
 * Handles the account screen of login.
 *
 * @author Daniel
 *//*

public class AvatarScreen extends LoginComponent {

	@Override
	public void render(Client client) {
		int centerX = getX();
		int centerY = getY();
		int offset = 29;
		int frameColor = 0x3C99A3;
		int frameHoverColor = 0x3C767D;
		int avatarHoverAlpha = 70;

		refresh(client);
		load(client, 13);

          */
/* Background *//*

		Client.spriteCache.get(678).drawTransparentSprite((Client.frameWidth / 2) - (Client.spriteCache.get(57).width / 2), (Client.frameHeight / 2) - (Client.spriteCache.get(57).height / 2), client.loginTick);

		//Darth Vader
		if (client.mouseInRegion(centerX - 215, centerY - 136, centerX - 115, centerY - 62)) {
			Client.spriteCache.get(534).drawARGBSprite(165 + offset, 115, avatarHoverAlpha);
			Raster.drawRectangle(165 + offset, 115, Client.spriteCache.get(534).width, Client.spriteCache.get(534).height, frameHoverColor);
		} else {
			Client.spriteCache.get(534).drawARGBSprite(165 + offset, 115);
			Raster.drawRectangle(165 + offset, 115, Client.spriteCache.get(534).width, Client.spriteCache.get(534).height, frameColor);
		}
		//Skeleton
		if (client.mouseInRegion(centerX - 91, centerY - 136, centerX - 14, centerY - 62)) {
			Client.spriteCache.get(535).drawARGBSprite(265 + offset, 115, avatarHoverAlpha);
			Raster.drawRectangle(265 + offset, 115, Client.spriteCache.get(535).width, Client.spriteCache.get(535).height, frameHoverColor);
		} else {
			Client.spriteCache.get(535).drawARGBSprite(265 + offset, 115);
			Raster.drawRectangle(265 + offset, 115, Client.spriteCache.get(535).width, Client.spriteCache.get(535).height, frameColor);
		}
		//Cool dude
		if (client.mouseInRegion(centerX + 9, centerY - 136, centerX + 85, centerY - 62)) {
			Client.spriteCache.get(536).drawARGBSprite(365 + offset, 115, avatarHoverAlpha);
			Raster.drawRectangle(365 + offset, 115, Client.spriteCache.get(536).width, Client.spriteCache.get(536).height, frameHoverColor);
		} else {
			Client.spriteCache.get(536).drawARGBSprite(365 + offset, 115);
			Raster.drawRectangle(365 + offset, 115, Client.spriteCache.get(536).width, Client.spriteCache.get(536).height, frameColor);
		}
		//Gas mask
		if (client.mouseInRegion(centerX + 112, centerY - 136, centerX + 185, centerY - 62)) {
			Client.spriteCache.get(537).drawARGBSprite(465 + offset, 115, avatarHoverAlpha);
			Raster.drawRectangle(465 + offset, 115, Client.spriteCache.get(537).width, Client.spriteCache.get(537).height, frameHoverColor);
		} else {
			Client.spriteCache.get(537).drawARGBSprite(465 + offset, 115);
			Raster.drawRectangle(465 + offset, 115, Client.spriteCache.get(537).width, Client.spriteCache.get(537).height, frameColor);
		}
		//KFC
		if (client.mouseInRegion(centerX - 188, centerY - 46, centerX - 114, centerY + 30)) {
			Client.spriteCache.get(538).drawARGBSprite(165 + offset, 205, avatarHoverAlpha);
			Raster.drawRectangle(165 + offset, 205, Client.spriteCache.get(538).width, Client.spriteCache.get(538).height, frameHoverColor);
		} else {
			Client.spriteCache.get(538).drawARGBSprite(165 + offset, 205);
			Raster.drawRectangle(165 + offset, 205, Client.spriteCache.get(538).width, Client.spriteCache.get(538).height, frameColor);
		}
		//Dog
		if (client.mouseInRegion(centerX - 91, centerY - 46, centerX - 14, centerY + 30)) {
			Client.spriteCache.get(539).drawARGBSprite(265 + offset, 205, avatarHoverAlpha);
			Raster.drawRectangle(265 + offset, 205, Client.spriteCache.get(539).width, Client.spriteCache.get(539).height, frameHoverColor);
		} else {
			Client.spriteCache.get(539).drawARGBSprite(265 + offset, 205);
			Raster.drawRectangle(265 + offset, 205, Client.spriteCache.get(539).width, Client.spriteCache.get(539).height, frameColor);
		}
		//Monkey
		if (client.mouseInRegion(centerX + 9, centerY - 46, centerX + 85, centerY + 30)) {
			Client.spriteCache.get(540).drawARGBSprite(365 + offset, 205, avatarHoverAlpha);
			Raster.drawRectangle(365 + offset, 205, Client.spriteCache.get(540).width, Client.spriteCache.get(540).height, frameHoverColor);
		} else {
			Client.spriteCache.get(540).drawARGBSprite(365 + offset, 205);
			Raster.drawRectangle(365 + offset, 205, Client.spriteCache.get(540).width, Client.spriteCache.get(540).height, frameColor);
		}
		//Wolf
		if (client.mouseInRegion(centerX + 112, centerY - 46, centerX + 185, centerY + 30)) {
			Client.spriteCache.get(541).drawARGBSprite(465 + offset, 205, avatarHoverAlpha);
			Raster.drawRectangle(465 + offset, 205, Client.spriteCache.get(541).width, Client.spriteCache.get(541).height, frameHoverColor);
		} else {
			Client.spriteCache.get(541).drawARGBSprite(465 + offset, 205);
			Raster.drawRectangle(465 + offset, 205, Client.spriteCache.get(541).width, Client.spriteCache.get(541).height, frameColor);
		}
		//Joker
		if (client.mouseInRegion(centerX - 189, centerY + 45, centerX - 114, centerY + 122)) {
			Client.spriteCache.get(680).drawARGBSprite(165 + offset, 295, avatarHoverAlpha);
			Raster.drawRectangle(165 + offset, 295, Client.spriteCache.get(680).width, Client.spriteCache.get(680).height, frameHoverColor);
		} else {
			Client.spriteCache.get(680).drawARGBSprite(165 + offset, 295);
			Raster.drawRectangle(165 + offset, 295, Client.spriteCache.get(680).width, Client.spriteCache.get(680).height, frameColor);
		}
		//Demon
		if (client.mouseInRegion(centerX - 91, centerY + 45, centerX - 14, centerY + 122)) {
			Client.spriteCache.get(681).drawARGBSprite(265 + offset, 295, avatarHoverAlpha);
			Raster.drawRectangle(265 + offset, 295, Client.spriteCache.get(681).width, Client.spriteCache.get(681).height, frameHoverColor);
		} else {
			Client.spriteCache.get(681).drawARGBSprite(265 + offset, 295);
			Raster.drawRectangle(265 + offset, 295, Client.spriteCache.get(681).width, Client.spriteCache.get(681).height, frameColor);
		}
		//Hot girl 1
		if (client.mouseInRegion(centerX + 9, centerY + 45, centerX + 85, centerY + 122)) {
			Client.spriteCache.get(682).drawARGBSprite(365 + offset, 295, avatarHoverAlpha);
			Raster.drawRectangle(365 + offset, 295, Client.spriteCache.get(682).width, Client.spriteCache.get(682).height, frameHoverColor);
		} else {
			Client.spriteCache.get(682).drawARGBSprite(365 + offset, 295);
			Raster.drawRectangle(365 + offset, 295, Client.spriteCache.get(682).width, Client.spriteCache.get(682).height, frameColor);
		}
		//Hot girl 2
		if (client.mouseInRegion(centerX + 112, centerY + 45, centerX + 185, centerY + 122)) {
			Client.spriteCache.get(683).drawARGBSprite(465 + offset, 295, avatarHoverAlpha);
			Raster.drawRectangle(465 + offset, 295, Client.spriteCache.get(683).width, Client.spriteCache.get(683).height, frameHoverColor);
		} else {
			Client.spriteCache.get(683).drawARGBSprite(465 + offset, 295);
			Raster.drawRectangle(465 + offset, 295, Client.spriteCache.get(683).width, Client.spriteCache.get(683).height, frameColor);
		}

		client.boldText.drawCenteredText(0xFFFFFF, centerX + 5, "[ Click on the avatar you wish to select ]", centerY + 150, true);

        */
/* Announcement *//*

		announcement(client);

        */
/* Bubble *//*

		drawSetting(client);

        */
/* Other *//*

		client.smallFont.drawCenteredText(0x27A2B0, centerX + 330, "Client Build: " + Configuration.GAME_VERSION, centerY + 245, true);

		if (Configuration.DEBUG_MODE) {
			client.smallFont.drawCenteredText(0xFFFFFF, centerX + 300, "MouseX: " + (client.mouseX - (centerX)) + " Mouse Y: " + (client.mouseY - (centerY)), centerY - 225, true);
		}

        */
/* Drawing *//*

		Client.loginScreenIP.drawGraphics(client.graphics, 0, 0);
		Raster.reset();
	}

	@Override
	public void click(Client client) {
		int centerX = getX();
		int centerY = getY();

        */
/* Bubble *//*

		settingButton(client);

		//Darth Vader
		if (client.lastMetaModifier == 1 && client.mouseInRegion(centerX - 215, centerY - 136, centerX - 115, centerY - 62)) {
			select(client, 534);
		}
		//Skeleton
		if (client.lastMetaModifier == 1 && client.mouseInRegion(centerX - 91, centerY - 136, centerX - 14, centerY - 62)) {
			select(client, 535);
		}
		//Cool dude
		if (client.lastMetaModifier == 1 && client.mouseInRegion(centerX + 9, centerY - 136, centerX + 85, centerY - 62)) {
			select(client, 536);
		}
		//Gas mask
		if (client.lastMetaModifier == 1 && client.mouseInRegion(centerX + 112, centerY - 136, centerX + 185, centerY - 62)) {
			select(client, 537);
		}
		//KFC
		if (client.lastMetaModifier == 1 && client.mouseInRegion(centerX - 188, centerY - 46, centerX - 114, centerY + 30)) {
			select(client, 538);
		}
		//Dog
		if (client.lastMetaModifier == 1 && client.mouseInRegion(centerX - 91, centerY - 46, centerX - 14, centerY + 30)) {
			select(client, 539);
		}
		//Monkey
		if (client.lastMetaModifier == 1 && client.mouseInRegion(centerX + 9, centerY - 46, centerX + 85, centerY + 30)) {
			select(client, 540);
		}
		//Wolf
		if (client.lastMetaModifier == 1 && client.mouseInRegion(centerX + 112, centerY - 46, centerX + 185, centerY + 30)) {
			select(client, 541);
		}
		//Joker
		if (client.lastMetaModifier == 1 && client.mouseInRegion(centerX - 189, centerY + 45, centerX - 114, centerY + 122)) {
			select(client, 680);
		}
		//Demon
		if (client.lastMetaModifier == 1 && client.mouseInRegion(centerX - 91, centerY + 45, centerX - 14, centerY + 122)) {
			select(client, 681);
		}
		//Hot girl 1
		if (client.lastMetaModifier == 1 && client.mouseInRegion(centerX + 9, centerY + 45, centerX + 85, centerY + 122)) {
			select(client, 682);
		}
		//Hot girl 2
		if (client.lastMetaModifier == 1 && client.mouseInRegion(centerX + 112, centerY + 45, centerX + 185, centerY + 122)) {
			select(client, 683);
		}
	}

	@Override
	public ScreenType type() {
		return ScreenType.AVATAR;
	}

	*/
/**
	 * Handles selecting an avatar.
	 *
	 * @param client The client instance.
	 * @param avatar The account avatar.
	 *//*

	private void select(Client client, int avatar) {
		AccountData account = client.lastAccount;

		if (account == null) {
			return;
		}

		account.avatar = avatar;
		AccountManager.setAvatar(account.username, avatar);
		AccountManager.saveAccount();
		client.loginMessage1 = "";
		client.loginMessage2 = "";
		client.loginRenderer.setScreen(new AccountScreen());
	}
}
*/
