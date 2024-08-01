package com.osroyale.math;

public class Vector3f {
    public float x;
    public float y;
    public float z;

    public static final Vector3f ZERO = new Vector3f(0f, 0f, 0f);
    public static final Vector3f ONE = new Vector3f(1f, 1f, 1f);
    public static final Vector3f ROTATE_X = new Vector3f(1f, 0f, 0f);
    public static final Vector3f ROTATE_Y = new Vector3f(0f, 1f, 0f);
    public static final Vector3f ROTATE_Z = new Vector3f(0f, 0f, 1f);

    public Vector3f(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    final float length() {
        return (float) Math.sqrt(this.z * this.z + this.x * this.x + this.y * this.y);
    }

    public String toString() {
        return this.x + ", " + this.y + ", " + this.z;
    }
}