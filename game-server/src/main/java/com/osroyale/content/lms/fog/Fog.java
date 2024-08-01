package com.osroyale.content.lms.fog;

public class Fog {

    private int lowX;

    public int getLowX() { return lowX; }

    private int lowY;

    public int getLowY() { return lowY; }

    private int highX;

    public int getHighX() { return highX; }

    private int highY;

    public int getHighY() { return highY; }

    public Fog(int lowX, int lowY, int highX, int highY) {
        this.lowX = lowX;
        this.lowY = lowY;
        this.highX = highX;
        this.highY = highY;
    }

    public void decrease() {
        lowX += 1;
        lowY += 1;
        highX -= 1;
        highY -= 1;
        System.out.println("fog decrease....");
    }

    public void reset() {
        lowX = -1;
        lowY = -1;
        highX = -1;
        highY = -1;
    }

    public boolean isSafe() {
        return lowX == -1 && lowY == -1 && highX == -1 && highY == -1;
    }
}
