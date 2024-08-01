package com.osroyale.game.event.impl;

import com.osroyale.game.event.Event;

public class ButtonClickEvent implements Event {

    private final int button;

    public ButtonClickEvent(int button) {
        this.button = button;
    }

    public int getButton() {
        return button;
    }

}
