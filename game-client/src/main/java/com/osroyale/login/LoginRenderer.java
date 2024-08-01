package com.osroyale.login;


import com.osroyale.Client;
import com.osroyale.login.impl.MainScreen;

/**
 * Handles the rendering of the login screen.
 */
public class LoginRenderer {

	/** The client instance. */
	private final Client client;

	/** The login screen to render. */
	public LoginComponent screen;

	/** Constructs a new <code>LoginRenderer</code>. */
	public LoginRenderer(Client client) {
		this.client = client;
		this.setScreen(new MainScreen());
	}

	/** Handles rendering the login screen. */
	public void display() {
		screen.render(client);
	}

	/** Handles clicking on the login screen. */
	public void click() {
		if (!client.loginLoaded)
			return;
		screen.click(client);
	}

	/** Sets the login screen. */
	public void setScreen(LoginComponent screen) {
		this.screen = screen;
		this.client.loginLoaded = false;
		this.client.loginTick = 50;
	}

	public boolean getScreen(ScreenType type) {
		return screen.type() == type;
	}
}
