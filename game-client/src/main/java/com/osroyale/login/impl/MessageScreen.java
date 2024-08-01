package com.osroyale.login.impl;

import com.osroyale.Client;
import com.osroyale.Configuration;
import com.osroyale.engine.impl.MouseHandler;
import com.osroyale.login.LoginComponent;
import com.osroyale.login.ScreenType;

/**
 * Handles the message screen of login.
 *
 * @author Daniel
 */
public class MessageScreen extends LoginComponent {

    @Override
    public void render(Client client) {
        int centerX = getX();
        int centerY = getY();
        refresh(client);
        load(client, 13);

        /* Background */
        Client.spriteCache.get(678).drawTransparentSprite((Client.canvasWidth / 2) - (Client.spriteCache.get(57).width / 2), (Client.canvasHeight / 2) - (Client.spriteCache.get(57).height / 2), client.loginTick);

        /* Messages */
        client.boldText.drawCenteredText(0xFFFFFF, centerX + 5, Configuration.NAME, centerY + 10, true);
        client.regularText.drawCenteredText(0xFFFFFF, centerX + 5, "Error Message", centerY + 30, true);
        client.boldText.drawCenteredText(0xFFFFFF, centerX + 5, client.loginMessage1, centerY + 70, true);
        client.boldText.drawCenteredText(0xFFFFFF, centerX + 5, client.loginMessage2, centerY + 85, true);

        client.boldText.drawCenteredText(0xFFFFFF, centerX + 5, "[ Click anywhere to return to the main screen ]", centerY + 150, true);


    }

    @Override
    public void click(Client client) {
        int centerX = getX();
        int centerY = getY();

        if (MouseHandler.clickMode3 == 1 && client.mouseInRegion(centerX - 381, centerY - 249, centerX + 381, centerY + 245)) {
            client.loginMessage1 = "";
            client.loginMessage2 = "";
            client.loginRenderer.setScreen(new MainScreen());
        }
    }

    @Override
    public ScreenType type() {
        return ScreenType.MESSAGE;
    }
}
