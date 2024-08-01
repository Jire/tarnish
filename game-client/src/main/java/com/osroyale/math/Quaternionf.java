package com.osroyale.math;

public final class Quaternionf {

    private static final Quaternionf[] p;
    private static final int pl;
    private static int pc;

    public static final Quaternionf IDENTITY = new Quaternionf();

    float x;
    float y;
    float w;
    float z;

    static {
        pl = 100;
        p = new Quaternionf[100];
        pc = 0;
    }

    public Quaternionf() {
        this.identity();
    }

    public Quaternionf(float x, float y, float w, float z) {
        this.x = x;
        this.y = y;
        this.w = w;
        this.z = z;
    }

    public void release() {
        synchronized (p) {
            if (pc < pl - 1) {
                p[pc++] = this;
            }
        }
    }

    public static Quaternionf take() {
        synchronized (p) {
            if (pc == 0) {
                return new Quaternionf();
            } else {
                p[--pc].identity();
                return p[pc];
            }
        }
    }

    void set(float x, float y, float z, float w) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.w = w;
    }

    public void faa(float a1, float a2, float a3, float air) {
        float s = ((float) (Math.sin(air * 0.5F)));
        float c = ((float) (Math.cos(air * 0.5F)));
        this.x = s * a1;
        this.y = a2 * s;
        this.z = s * a3;
        this.w = c;
    }

    void identity() {
        this.x = 0.0F;
        this.y = 0.0F;
        this.z = 0.0F;
        this.w = 1.0F;
    }

    public void mp(Quaternionf other) {
        this.set(other.x * this.w + other.w * this.x + other.y * this.z - other.z * this.y, other.z * this.x + this.w * other.y + (this.y * other.w - this.z * other.x), other.x * this.y + other.w * this.z - this.x * other.y + other.z * this.w, other.w * this.w - other.x * this.x - this.y * other.y - other.z * this.z);
    }

    public boolean equals(Object o) {
        if (!(o instanceof Quaternionf)) {
            return false;
        } else {
            Quaternionf other = ((Quaternionf) (o));
            return this.x == other.x && this.y == other.y && other.z == this.z && other.w == this.w;
        }
    }

    public String toString() {
        return this.x + "," + this.y + "," + this.z + "," + this.w;
    }

    public int hashCode() {
        float hashCode = 1.0F;
        hashCode = hashCode * 31.0F + this.x;
        hashCode = this.y + hashCode * 31.0F;
        hashCode = 31.0F * hashCode + this.z;
        hashCode = 31.0F * hashCode + this.w;
        return ((int) (hashCode));
    }
}