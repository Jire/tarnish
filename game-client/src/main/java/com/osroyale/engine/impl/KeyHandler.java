package com.osroyale.engine.impl;


import com.osroyale.Client;
import com.osroyale.login.ScreenType;
import net.runelite.api.GameState;
import net.runelite.api.KeyCode;
import net.runelite.api.events.FocusChanged;
import net.runelite.rs.api.RSKeyHandler;

import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public final class KeyHandler implements KeyListener, FocusListener, RSKeyHandler {

    public static KeyHandler instance;

    static  {
        instance = new KeyHandler();
    }

    public static final int[] keyArray = new int[128];
    private final int[] charQueue = new int[128];
    private int readIndex;
    private int writeIndex;
    int keyPressed = 0;
    public static volatile int idleCycles = 0;

    @Override
    public void focusGained(FocusEvent e) {}

    @Override
    public void focusLost(FocusEvent e) {
        final FocusChanged focusChanged = new FocusChanged();
        focusChanged.setFocused(false);
        Client.instance.getCallbacks().post(focusChanged);

        for (int i = 0; i < 128; i++) {
            keyArray[i] = 0;
        }
    }

    @Override
    public void keyTyped(KeyEvent keyEvent) {
        Client.instance.getCallbacks().keyTyped(keyEvent);
        if (!keyEvent.isConsumed())
        {

        }
    }

    @Override
    public void keyPressed(KeyEvent keyevent) {
        idleCycles = 0;

        Client.instance.getCallbacks().keyPressed(keyevent);
        if (!keyevent.isConsumed())
        {

            int i = keyevent.getKeyCode();
            if(i == KeyEvent.VK_SHIFT) {
                keyPressed = KeyCode.KC_SHIFT;
            } else {
                keyPressed = i;
            }
            int keyChar = keyevent.getKeyChar();

            if (keyevent.isShiftDown()) {
                Client.shiftIsDown = true;
            }

            if(Client.instance.getGameState() == GameState.LOGGED_IN) {
                if (Client.instance.chatDelay.elapsed() > 599 && keyevent.getKeyCode() == KeyEvent.VK_ESCAPE || keyevent.getKeyCode() == KeyEvent.VK_TAB || keyevent.getKeyCode() == KeyEvent.VK_SPACE || keyevent.getKeyCode() == KeyEvent.VK_1 || keyevent.getKeyCode() == KeyEvent.VK_2 || keyevent.getKeyCode() == KeyEvent.VK_3 || keyevent.getKeyCode() == KeyEvent.VK_4 || keyevent.getKeyCode() == KeyEvent.VK_5 || keyevent.getKeyCode() == KeyEvent.VK_NUMPAD1 || keyevent.getKeyCode() == KeyEvent.VK_NUMPAD2 || keyevent.getKeyCode() == KeyEvent.VK_NUMPAD3 || keyevent.getKeyCode() == KeyEvent.VK_NUMPAD4 || keyevent.getKeyCode() == KeyEvent.VK_NUMPAD5) {
                    Client.instance.outgoing.writeOpcode(186);
                    Client.instance.outgoing.writeShort(keyevent.getKeyCode());
                }
            }

            //keybind
            Client.instance.keybindManager.handleBind(keyevent.getKeyCode());

            if (keyevent.getKeyChar() == 96)
                Client.instance.console.openConsole = !Client.instance.console.openConsole;
            if (Client.instance.console.openConsole && (keyevent.getKeyChar() == KeyEvent.VK_PAGE_UP || keyevent.getKeyChar() == KeyEvent.VK_PAGE_DOWN))
                Client.instance.console.chooseCommand(keyevent.getKeyChar() == KeyEvent.VK_PAGE_UP);
            if (keyevent.isControlDown())
                Client.controlIsDown = true;

            //Ctrl V
            if ((keyevent.isControlDown() && keyevent.getKeyCode() == KeyEvent.VK_V)) {
                String clipboard = Client.getClipboardContents();

                if (Client.loggedIn) {
                    Client.inputString = (Client.inputString + clipboard).substring(0, 128);
                } else if (Client.instance.loginRenderer.getScreen(ScreenType.MAIN)) {
                    if (Client.instance.loginScreenCursorPos == 0) {
                        Client.instance.myUsername = (Client.instance.myUsername + clipboard).substring(0, 28);
                    } else if (Client.instance.loginScreenCursorPos == 1) {
                        Client.instance.myPassword = (Client.instance.myUsername + clipboard).substring(0, 28);
                    }
                }
            }
            if ((keyevent.isControlDown() && keyevent.getKeyCode() == KeyEvent.VK_X)) {
                Client.setClipboardContents(Client.inputString);
                Client.inputString = "";
            }




            if (keyChar < 30)
                keyChar = 0;
            if (i == 37)
                keyChar = 1;
            if (i == 39)
                keyChar = 2;
            if (i == 38)
                keyChar = 3;
            if (i == 40)
                keyChar = 4;
            if (i == 17)
                keyChar = 5;
            if (i == 8)
                keyChar = 8;
            if (i == 127)
                keyChar = 8;
            if (i == 9)
                keyChar = 9;
            if (i == 10)
                keyChar = 10;
            if (i >= 112 && i <= 123)
                keyChar = (1008 + i) - 112;
            if (i == 36)
                keyChar = 1000;
            if (i == 35)
                keyChar = 1001;
            if (i == 33)
                keyChar = 1002;
            if (i == 34)
                keyChar = 1003;
            if (keyChar > 0 && keyChar < 128)
                keyArray[keyChar] = 1;
            if (Client.instance.getOculusOrbState() == 0 && keyChar > 4) {
                charQueue[writeIndex] = keyChar;
                writeIndex = writeIndex + 1 & 0x7f;
            }
        }
    }

    public int readChar() {
        int k = -1;
        if (writeIndex != readIndex) {
            k = charQueue[readIndex];
            readIndex = readIndex + 1 & 0x7f;
        }
        return k;
    }


    @Override
    public void keyReleased(KeyEvent event) {
        Client.instance.getCallbacks().keyReleased(event);
        if (!event.isConsumed())
        {
            idleCycles = 0;
            int i = event.getKeyCode();
            char c = event.getKeyChar();

            if (i == KeyEvent.VK_SHIFT) {
                Client.shiftIsDown = false;
            }

            if (i == KeyEvent.VK_CONTROL) {
                Client.controlIsDown = false;
            }

            if (c < '\036')
                c = '\0';
            if (i == 37)
                c = '\001';
            if (i == 39)
                c = '\002';
            if (i == 38)
                c = '\003';
            if (i == 40)
                c = '\004';
            if (i == 17)
                c = '\005';
            if (i == 8)
                c = '\b';
            if (i == 127)
                c = '\b';
            if (i == 9)
                c = '\t';
            if (i == 10)
                c = '\n';
            if (c > 0 && c < '\200')
                keyArray[c] = 0;
        }

    }

}
