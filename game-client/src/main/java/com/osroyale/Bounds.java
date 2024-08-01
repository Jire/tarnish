package com.osroyale;

import net.runelite.rs.api.RSBounds;

public class Bounds implements RSBounds {

    public int lowX;

    public int lowY;

    public int highX;

    public int highY;

    public Bounds(int var1, int var2, int var3, int var4) {
        this.setLow(var1, var2);
        this.setHigh(var3, var4);
    } 

    public Bounds(int var1, int var2) {
        this(0, 0, var1, var2); 
    } 


    public void setLow(int var1, int var2) {
        this.lowX = var1; 
        this.lowY = var2; 
    } 


    public void setHigh(int var1, int var2) {
        this.highX = var1; 
        this.highY = var2; 
    } 


    public String toString() {
        return null;
    }

} 