package com.osroyale.engine;

import com.osroyale.Client;
import net.runelite.rs.api.RSCanvas;

import java.awt.*;
import java.awt.event.FocusListener;

public final class Canvas extends java.awt.Canvas implements RSCanvas {

    Component component;
    private static boolean shouldNotHaveFocus;

    public Canvas(Component var1) {
        this.component = var1; // L: 11
    } // L: 12

    public void update(Graphics var1) {
        this.component.update(var1); // L: 15
    } // L: 16

    public void paint(Graphics var1) {
        this.component.paint(var1); // L: 19
    } // L: 20

    public void removeFocusListener(FocusListener l)
    {
        super.removeFocusListener(l);
        shouldNotHaveFocus = !this.hasFocus();
    }

    @Override
    public void requestFocus()
    {
        // Runescape requests focus whenever the window is resized. Because of this, PluginPanels cannot have focus
        // if they cause the sidebar to expand. This change prevents Runescape from requesting focus whenever it wants
        if (!shouldNotHaveFocus)
        {
            this.requestFocusInWindow();
        }
    }

    @Override
    public void setSize(int width, int height)
    {

        if (Client.instance.isStretchedEnabled())
        {
            super.setSize(Client.instance.getStretchedDimensions().width, Client.instance.getStretchedDimensions().height);
        }
        else
        {
            super.setSize(width, height);
        }

    }

    @Override
    public void setLocation(int x, int y)
    {
        if (Client.instance.isStretchedEnabled())
        {
            super.setLocation((getParent().getWidth() - Client.instance.getStretchedDimensions().width) / 2, 0);
        }
        else
        {
            super.setLocation(x, y);
        }
    }

}
