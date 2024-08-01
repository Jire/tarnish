package com.osroyale.math;

public class FloatMaths {
    // in the skeletal anim code this is used as the epsilon
    public static final float ulp1 = Math.ulp(1.0F);

    public static final float ulpSq;

    static {
        ulpSq = ulp1 * 2.0F;
    }
}
