package com.osroyale.fog;

public abstract class Fog {

    /**
     * The state of the fade on the screen
     */
    protected int state;

    /**
     * The position on the screen we're drawing, along with the dimensions
     */
    protected final int x, y;

    public Fog(int state, int x, int y) {
        this.state = state;
        this.x = x;
        this.y = y;
    }

    /**
     * Draws the animation on the screen. If the state of the screen is
     * currently 0 the animation will not be drawn.
     */
    public abstract void draw();

    public abstract void update(int o, int x, int y);

    /**
     * Modifies the current state of the fading screen to zero which
     * should be defined as preventing drawing operations in the overriden
     * draw function of each sub class.
     */
    public void stop() {
        state = 0;
    }

    public abstract void setBounds(int x, int y);
}

