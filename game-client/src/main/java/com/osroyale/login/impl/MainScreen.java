package com.osroyale.login.impl;

import com.osroyale.*;
import com.osroyale.engine.impl.KeyHandler;
import com.osroyale.engine.impl.MouseHandler;
import com.osroyale.login.LoginComponent;
import com.osroyale.login.ScreenType;
import com.osroyale.profile.Profile;
import com.osroyale.profile.ProfileManager;

/**
 * Handles the main screen of login.
 *
 * @author Daniel
 */
public class MainScreen extends LoginComponent {

	private static final int EMAIL_CHARACTER_LIMIT = 28;

	public MainScreen() {
	}

	@Override
	public void render(Client client) {
		refresh(client);
		load(client, 10);

        /* Message Check */
		if (client.loginMessage1.length() > 0 || client.loginMessage2.length() > 0) {
			client.loginRenderer.setScreen(new MessageScreen());
		}

        /* Background */
		//new Sprite(1920, 1080).drawARGBSprite(0, 0, 0);
		Sprite backgroundSprite = Client.spriteCache.get(57);
		int alpha = 255;
		backgroundSprite.drawARGBSprite(0, 0, alpha);

		if (client.mouseInRegion(150, 272, 416, 304)) {
			Client.spriteCache.get(680).drawARGBSprite(150, 270, alpha);
		} else {
			Client.spriteCache.get(676).drawARGBSprite(150, 270, alpha);
		}
		if (client.mouseInRegion(150, 320, 416, 354)) {
			Client.spriteCache.get(680).drawARGBSprite(150, 320, alpha);
		} else {
			Client.spriteCache.get(676).drawARGBSprite(150, 320, alpha);
		}
		client.boldText.drawText(true, 160, 0xFFFFFF, Utility.formatName(client.myUsername) + ((client.loginScreenCursorPos == 0) & (Client.tick % 40 < 20) ? "|" : ""), 295);
		client.boldText.drawText(true, 160, 0xFFFFFF, StringUtils.toAsterisks(client.myPassword) + ((client.loginScreenCursorPos == 1) & (Client.tick % 40 < 20) ? "|" : ""), 345);

		Client.spriteCache.get(Settings.REMEMBER_ME ? 180 : 178).drawARGBSprite(150, 362, alpha);

		if (client.mouseInRegion(214, 414, 376, 461)) {
			Client.spriteCache.get(59).drawTransparentSprite(210, 410, alpha);
		} else {
			Client.spriteCache.get(58).drawTransparentSprite(210, 410, alpha);
		}

		int profileY = 245;
		for (int i = 0; i < ProfileManager.MAX_PROFILES; i++, profileY += 65) {
			Profile profile = ProfileManager.profiles.get(i);
			if (profile == null) {
				continue;
			}


			Client.spriteCache.get(681).drawSprite(484, profileY - 5);
			profile.drawProfileHead(489, profileY + 4);
			client.boldText.drawCenteredText(0x000000, 585, Utility.formatName(profile.getUsername()), profileY + 33, true);
			client.boldText.drawCenteredText(0xFFFFFF, 230, "Remember me?", 383, true);
		}
	}

	@Override
	public void click(Client client) {
        /* Username */
		if (MouseHandler.clickMode3 == 1 && client.mouseInRegion(150, 272, 416, 304)) {
			client.loginScreenCursorPos = 0;
		}

        /* Password */
		if (MouseHandler.clickMode3 == 1 && client.mouseInRegion(150, 320, 416, 354)) {
			client.loginScreenCursorPos = 1;
		}

        /* Remember Me */
		if (MouseHandler.clickMode3 == 1 && client.mouseInRegion(150, 360, 172, 383)) {
			Settings.REMEMBER_ME = !Settings.REMEMBER_ME;
		}

		int profileY = 245;
		for (int i = 0; i < ProfileManager.MAX_PROFILES; i++, profileY += 65) {
			Profile profile = ProfileManager.profiles.get(i);
			if (profile == null) {
				continue;
			}

			if (profile.emptySlot()) {
				continue;
			}

			if (MouseHandler.clickMode3 == 1 && client.mouseInRegion(483, profileY - 5, 497, profileY + 10)) {
				ProfileManager.delete(profile);
				break;
			} else if (MouseHandler.clickMode3 == 1 && client.mouseInRegion(488, profileY, 635, profileY + 50)) {
				if (!Client.loggedIn) {
					client.myUsername = profile.getUsername();
					client.myPassword = profile.getPassword();
					client.attemptLogin(client.myUsername, client.myPassword, false);
				}
				break;
			}
		}

        /* Login Buttons */
		if (MouseHandler.clickMode3 == 1 && client.mouseInRegion(214, 414, 376, 461)) {
			if (!Client.loggedIn) {
				client.attemptLogin(client.myUsername, client.myPassword, false);
			}
		}

        /* Writing */
		handleWriting(client);
	}

	/**
	 * Handles writing in the client.
	 */
	private void handleWriting(Client client) {
		do {
			int line = KeyHandler.instance.readChar();
			if (line == -1)
				break;
			boolean flag = false;
			for (int index = 0; index < Client.validUserPassChars.length(); index++) {
				if (line != Client.validUserPassChars.charAt(index))
					continue;
				flag = true;
				break;
			}

			// Main account username
			if (client.loginScreenCursorPos == 0) {
				if (line == 8 && client.myUsername.length() > 0)
					client.myUsername = client.myUsername.substring(0, client.myUsername.length() - 1);
				if (line == 9 || line == 10 || line == 13) {
					client.loginScreenCursorPos = 1;
				}
				if (flag) {
					client.myUsername += (char) line;
				}

				if (client.myUsername.length() > EMAIL_CHARACTER_LIMIT) {
					client.myUsername = client.myUsername.substring(0, EMAIL_CHARACTER_LIMIT);
				}

				// Main account password
			} else if (client.loginScreenCursorPos == 1) {
				if (line == 8 && client.myPassword.length() > 0)
					client.myPassword = client.myPassword.substring(0, client.myPassword.length() - 1);
				if (line == 9 || line == 10 || line == 13) {
					client.attemptLogin(client.myUsername, client.myPassword, false);
				}
				if (flag) {
					client.myPassword += (char) line;
				}
				if (client.myPassword.length() > 20) {
					client.myPassword = client.myPassword.substring(0, 20);
				}
			}
		} while (true);
	}

	@Override
	public ScreenType type() {
		return ScreenType.MAIN;
	}
}
