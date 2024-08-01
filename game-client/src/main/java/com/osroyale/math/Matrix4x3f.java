package com.osroyale.math;

// TODO verify that these are correct and make sure whether it's row or column major
public class Matrix4x3f {

    float m00;
    float m10;
    float m20;
    float m01;
    float m11;
    float m21;
    float m02;
    float m12;
    float m22;
    float m03;
    float m13;
    float m23;

    public static final Matrix4x3f IDENTITY = new Matrix4x3f();

    public Matrix4x3f() {
        this.identity();
    }

    public void identity() {
        this.m23 = 0.0F;
        this.m13 = 0.0F;
        this.m03 = 0.0F;
        this.m12 = 0.0F;
        this.m02 = 0.0F;
        this.m21 = 0.0F;
        this.m01 = 0.0F;
        this.m20 = 0.0F;
        this.m10 = 0.0F;
        this.m22 = 1.0F;
        this.m11 = 1.0F;
        this.m00 = 1.0F;
    }

    public void a1(float angle) {
        float cos = ((float) (Math.cos(angle)));
        float sin = ((float) (Math.sin(angle)));
        float m10 = this.m10;
        float m11 = this.m11;
        float m12 = this.m12;
        float m13 = this.m13;
        this.m10 = cos * m10 - sin * this.m20;
        this.m20 = m10 * sin + cos * this.m20;
        this.m11 = cos * m11 - this.m21 * sin;
        this.m21 = cos * this.m21 + m11 * sin;
        this.m12 = cos * m12 - sin * this.m22;
        this.m22 = m12 * sin + cos * this.m22;
        this.m13 = cos * m13 - sin * this.m23;
        this.m23 = cos * this.m23 + m13 * sin;
    }

    public void a2(float angle) {
        float cos = ((float) (Math.cos(angle)));
        float sin = ((float) (Math.sin(angle)));
        float m00 = this.m00;
        float m01 = this.m01;
        float m02 = this.m02;
        float m03 = this.m03;
        this.m00 = this.m20 * sin + cos * m00;
        this.m20 = this.m20 * cos - m00 * sin;
        this.m01 = cos * m01 + sin * this.m21;
        this.m21 = cos * this.m21 - m01 * sin;
        this.m02 = this.m22 * sin + m02 * cos;
        this.m22 = cos * this.m22 - m02 * sin;
        this.m03 = m03 * cos + sin * this.m23;
        this.m23 = cos * this.m23 - m03 * sin;
    }

    public void a3(float theta) {
        float cos = ((float) (Math.cos(theta)));
        float sin = ((float) (Math.sin(theta)));
        float m00 = this.m00;
        float m01 = this.m01;
        float m02 = this.m02;
        float m03 = this.m03;
        this.m00 = m00 * cos - sin * this.m10;
        this.m10 = m00 * sin + this.m10 * cos;
        this.m01 = cos * m01 - this.m11 * sin;
        this.m11 = cos * this.m11 + sin * m01;
        this.m02 = m02 * cos - this.m12 * sin;
        this.m12 = m02 * sin + this.m12 * cos;
        this.m03 = m03 * cos - this.m13 * sin;
        this.m13 = m03 * sin + cos * this.m13;
    }

    public void a4(float x, float y, float z) {
        this.m03 += x;
        this.m13 += y;
        this.m23 += z;
    }

    public String toString() {
        return this.m00 + "," + this.m01 + "," + this.m02 + "," + this.m03 + "\n" + this.m10 + "," + this.m11 + "," + this.m12 + "," + this.m13 + "\n" + this.m20 + "," + this.m21 + "," + this.m22 + "," + this.m23;
    }

}