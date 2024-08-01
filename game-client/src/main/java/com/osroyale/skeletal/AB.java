package com.osroyale.skeletal;

import com.osroyale.Buffer;
import com.osroyale.math.Matrix4f;

public class AB {

    public final int b_pid;
    public AB p_b;
    // this isn't used anywhere in the engine so no clue what this could be.
    public float[][] field1404;
    public final Matrix4f[] ml;
    public Matrix4f[] mg;
    public Matrix4f[] img;
    Matrix4f clm = new Matrix4f();
    boolean scsgm = true;
    Matrix4f cgmt = new Matrix4f();
    boolean scbm = true;
    Matrix4f cmb = new Matrix4f();
    float[][] ea;
    float[][] ts;
    float[][] sc;

    public AB(int length, Buffer buffer, boolean use4x3) {
        this.b_pid = buffer.readShort();
        this.ml = new Matrix4f[length];
        this.mg = new Matrix4f[this.ml.length];
        this.img = new Matrix4f[this.ml.length];
        this.field1404 = new float[this.ml.length][3];
        for (int index = 0; index < this.ml.length; index++) {
            this.ml[index] = new Matrix4f(buffer, use4x3);
            // no idea but these numbers are huge
            this.field1404[index][0] = buffer.readFloat();
            this.field1404[index][1] = buffer.readFloat();
            this.field1404[index][2] = buffer.readFloat();
        }
        this.cacheTRSComponents();
    }

    void cacheTRSComponents() {
        this.ea = new float[this.ml.length][3];
        this.ts = new float[this.ml.length][3];
        this.sc = new float[this.ml.length][3];
        Matrix4f matrix = Matrix4f.get();
        for (int index = 0; index < this.ml.length; index++) {
            Matrix4f lm = this.gml(index);
            matrix.sf(lm);
            matrix.iv();
            this.ea[index] = matrix.gEA2();
            this.ts[index][0] = lm.values[12];
            this.ts[index][1] = lm.values[13];
            this.ts[index][2] = lm.values[14];
            this.sc[index] = lm.gs();
        }
        matrix.r();
    }

    public Matrix4f gml(int bi) {
        return this.ml[bi];
    }

    public Matrix4f cgm(int fi) {
        if (this.mg[fi] == null) {
            this.mg[fi] = new Matrix4f(this.gml(fi));
            if (this.p_b != null) {
                this.mg[fi].mp(this.p_b.cgm(fi));
            } else {
                // strange because multiplying a matrix by the identity matrix has no effect
                this.mg[fi].mp(Matrix4f.IDENTITY);
            }
        }
        return this.mg[fi];
    }

    public Matrix4f gimg(int frameId) {
        if (this.img[frameId] == null) {
            this.img[frameId] = new Matrix4f(this.cgm(frameId));
            this.img[frameId].iv();
        }
        return this.img[frameId];
    }

    void setClm(Matrix4f clm) {
        this.clm.sf(clm);
        this.scsgm = true;
        this.scbm = true;
    }

    Matrix4f getClm() {
        return this.clm;
    }

    Matrix4f ccgm() {
        if (this.scsgm) {
            this.cgmt.sf(this.getClm());
            if (this.p_b != null) {
                this.cgmt.mp(this.p_b.ccgm());
            }
            this.scsgm = false;
        }
        return this.cgmt;
    }

    public Matrix4f gcbm(int frameId) {
        if (this.scbm) {
            this.cmb.sf(this.gimg(frameId));
            this.cmb.mp(this.ccgm());
            this.scbm = false;
        }
        return this.cmb;
    }

    public float[] getEa(int index) {
        return this.ea[index];
    }

    public float[] getTs(int index) {
        return this.ts[index];
    }

    public float[] getSc(int index) {
        return this.sc[index];
    }
}