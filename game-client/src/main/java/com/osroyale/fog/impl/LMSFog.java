package com.osroyale.fog.impl;

import com.osroyale.Rasterizer2D;
import com.osroyale.fog.Fog;

public class LMSFog extends Fog {

    private int width, height;
    public int opacity;

    public LMSFog(int state, int width, int height) {
        super(state, 0, 0);
        this.width = width;
        this.height = height;
    }

    @Override
    public void draw() {
        if (state == 0)
            return;

        //TODO: For some reason this doesnt work...
        Rasterizer2D.setDrawingArea(y + height, x, x + width, y);
		Rasterizer2D.drawTransparentBox(-100, 0, Rasterizer2D.width + 100, Rasterizer2D.height, 0xE67D2D, opacity);
        //Raster.fillRectangle(x, y, width, height, 0xE67D2D, opacity);
    }

    @Override
    public void update(int opacity, int height, int width) {
        if (state == 0)
            return;
        this.opacity = opacity;
        this.height = height;
        this.width = width;
    }

    @Override
    public void setBounds(int x, int y) {
        this.height = x;
        this.width = y;
    }
}