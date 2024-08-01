package com.osroyale.login;

import com.osroyale.*;
import com.osroyale.engine.impl.MouseHandler;
import com.osroyale.login.impl.MainScreen;
import com.osroyale.login.impl.SettingScreen;

/**
 * The login screen class.
 *
 * @author Daniel
 */
public abstract class LoginComponent {

    /** Renders the login screen component. */
    public abstract void render(Client client);

    /** Handles clicking buttons on the login screen components. */
    public abstract void click(Client client);

    public abstract ScreenType type();

    /** Handles refreshing the login screen components. */
    protected void refresh(Client client) {

    }

    /** Handles loading the login screen components. */
    public void load(Client client, int rate) {
        client.loginTick += rate;
        if (client.loginTick > 255) {
            client.loginTick = 255;
            client.loginLoaded = true;
        }
    }

    /** Draws the announcement bar */
    protected void announcement(Client client) {
        if (Settings.DRAW_ANNOUNCEMENT) {
            client.announcementMovement--;
            client.announcementFade++;
            if (client.announcementMovement < -Configuration.ANNOUNCEMENT[client.announcementTicks].length() - 10) {
                client.announcementMovement = Client.canvasWidth + 2;
                client.announcementTicks++;
                if (client.announcementTicks >= Configuration.ANNOUNCEMENT.length) {
                    client.announcementTicks = 0;
                }
            }
            int color1 = 0xCB9535;
            int color2 = 0xCB9535;
            Rasterizer2D.drawAlphaGradient(0, 0, Client.canvasWidth, 25, color1, color2, 205 - (int) (50 * Math.sin(client.announcementFade / 20.0)));
            client.smallFont.drawText(true, client.announcementMovement, 0xffffff, Configuration.ANNOUNCEMENT[client.announcementTicks], 17);
        }
    }

    /** Draws the bubbles on the login screen. */
    protected void drawSetting(Client client) {
        int centerX = getX();
        int centerY = getY();

        if (Settings.DRAW_BUBBLE)
            for (Bubble bubble : Client.bubbles)
                bubble.draw(Bubble.BOUNCING_BALLS);

        if (client.mouseInRegion(centerX - 363, centerY + 195, centerX - 262, centerY + 235)) {
            Rasterizer2D.fillRectangle(20, 445, 100, 40, 0xCB9535, 185);
            Rasterizer2D.drawRectangle(20, 445, 100, 40, 0xCB9535);
            addTooltip("Manage login screen settings");
        } else {
            Rasterizer2D.fillRectangle(20, 445, 100, 40, 0xC47423, 185);
            Rasterizer2D.drawRectangle(20, 445, 100, 40, 0xCB9535);
        }

        String string = client.loginRenderer.getScreen(ScreenType.SETTING) ? "Back" : "Settings";
        client.boldText.drawCenteredText(0xFFFFFF, centerX - 312, string, centerY + 221, true);
    }

    /** Draws the bubble toggle on the login screen. */
    protected void settingButton(Client client) {
        int centerX = getX();
        int centerY = getY();
        if (MouseHandler.clickMode3 == 1 && client.mouseInRegion(centerX - 363, centerY + 195, centerX - 262, centerY + 235)) {
            if (client.loginRenderer.getScreen(ScreenType.SETTING)) {
                client.loginRenderer.setScreen(new MainScreen());
            } else {
                client.loginRenderer.setScreen(new SettingScreen());
            }
        }
    }

    /** Draws a tooltip hover to the login screen. */
    protected void addTooltip(String message) {
        if (Settings.LOGINSCREEN_HOVER_BOXES) {
            int x = MouseHandler.mouseX;
            int y = MouseHandler.mouseY;
            int boxWith = Client.instance.newSmallFont.getTextWidth(message) + 30;

            if (x > 638)
                x = 638;
            if (y > 467)
                y = 467;

            Rasterizer2D.drawTransparentBox(x + 15, y + 10, boxWith, 22, 0xCB9535, 185);
            Rasterizer2D.drawRectangle(x + 15, y + 10, boxWith, 22, 0xCB9535);
            Client.instance.newSmallFont.drawBasicString(message, x + 30, y + 27, 16777215, 1);
        }
    }

    /** Gets the x position of the mouse. */
    public int getX() {
        return Client.canvasWidth / 2;
    }

    /** Gets the y position of the mouse. */
    public int getY() {
        return Client.canvasHeight / 2;
    }
}
