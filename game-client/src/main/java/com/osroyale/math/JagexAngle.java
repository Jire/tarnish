package com.osroyale.math;

public class JagexAngle {
    public static float mapAngleToTWO_PI(int angle) {
        angle &= 16383;
        double TWO_PI = 6.283185307179586;
        return ((float) (((double) (((float) (angle)) / 16384.0F)) * TWO_PI));
    }
}
