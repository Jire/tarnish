package com.osroyale;

import net.runelite.rs.api.RSAbstractRasterProvider;

public abstract class AbstractRasterProvider implements RSAbstractRasterProvider {
    public int[] pixels;
    public int width;
    public int height;

    public abstract void drawFull(int var1, int var2);
    public abstract void draw(int var1, int var2, int var3, int var4);

    public final void apply() {
        Rasterizer2D.initDrawingArea(this.pixels,this.width, this.height); // L: 11
    }

}
