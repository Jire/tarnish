package com.osroyale.skeletal;

import com.osroyale.Buffer;
import com.osroyale.Frame;
import com.osroyale.FrameBase;
import com.osroyale.datastructure.DualNode;
import com.osroyale.datastructure.EvictingDualNodeHashTable;
import com.osroyale.math.Matrix4f;
import com.osroyale.math.Quaternionf;
import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import it.unimi.dsi.fastutil.ints.IntSet;

public final class SkeletalFrame extends DualNode implements Frame {

    public TO[][] tt = null;
    public TO[][] vt = null;

    private final FrameBase frameBase;

    int fid = 0;

    boolean hExisting;

    public final int v;
    public final int baseGroupId;

    public static final EvictingDualNodeHashTable cache = new EvictingDualNodeHashTable(100);

    public SkeletalFrame(final byte[] frameData) {
        final Buffer frameBuffer = new Buffer(frameData);
        v = frameBuffer.readUnsignedByte();
        baseGroupId = frameBuffer.readUnsignedShort();

        this.frameBase = FrameBase.frameBases[baseGroupId];

        //System.err.println("v=" + v + ", baseGroupId=" + baseGroupId + " and length = " + frameData.length);

        decode(frameBuffer);
    }

    public static SkeletalFrame getSkeletalFrame(final int skeletalId) {
        return (SkeletalFrame) Frame.getFrame(skeletalId);
    }

    public static final IntSet skeletalFrameIds = new IntOpenHashSet();

    void decode(final Buffer buffer) {
        buffer.readUnsignedShort();
        buffer.readUnsignedShort();
        this.fid = buffer.readUnsignedByte();
        final int tc = buffer.readUnsignedShort();
        final FrameBase base = getFrameBase();
        final ABW abw = base.getAbw();
        this.vt = new TO[abw.getEsLength()][];
        this.tt = new TO[base.getLength()][];
        for (int index = 0; index < tc; index++) {
            int tId = buffer.readUnsignedByte();
            ToType to = ToType.lookUpById(tId);
            int ti = buffer.readSignedSmart();
            int tcmpId = buffer.readUnsignedByte();
            ToCmp tcmp = ToCmp.lookup(tcmpId);
            TO curTo = new TO();
            curTo.dc(buffer);
            int count = to.getDimensions();
            TO[][] TOS;
            if (to == ToType.TV) {
                TOS = this.vt;
            } else {
                TOS = this.tt;
            }
            if (TOS[ti] == null) {
                TOS[ti] = new TO[count];
            }
            TOS[ti][tcmp.component()] = curTo;
            if (to == ToType.TT) {
                this.hExisting = true;
            }
        }
    }

    public int getFid() {
        return this.fid;
    }

    public boolean hExisting() {
        return this.hExisting;
    }

    public void du(int cT, AB cB, int ti, int fid) {
        Matrix4f clm = Matrix4f.get();
        this.ur(clm, ti, cB, cT);
        this.us(clm, ti, cB, cT);
        this.ut(clm, ti, cB, cT);
        cB.setClm(clm);
        clm.r();
    }

    void ur(Matrix4f clm, int transformIndex, AB cB, int cT) {
        float[] eA = cB.getEa(this.fid);
        float e1 = eA[0];
        float e2 = eA[1];
        float e3 = eA[2];
        if (this.vt[transformIndex] != null) {
            TO to1 = this.vt[transformIndex][0];
            TO to2 = this.vt[transformIndex][1];
            TO to3 = this.vt[transformIndex][2];
            if (to1 != null) {
                e1 = to1.gv(cT);
            }
            if (to2 != null) {
                e2 = to2.gv(cT);
            }
            if (to3 != null) {
                e3 = to3.gv(cT);
            }
        }
        Quaternionf xrq = Quaternionf.take();
        xrq.faa(1.0F, 0.0F, 0.0F, e1);
        Quaternionf yrq = Quaternionf.take();
        yrq.faa(0.0F, 1.0F, 0.0F, e2);
        Quaternionf zrq = Quaternionf.take();
        zrq.faa(0.0F, 0.0F, 1.0F, e3);

        Quaternionf frq = Quaternionf.take();
        frq.mp(zrq);
        frq.mp(xrq);
        frq.mp(yrq);

        Matrix4f rm = Matrix4f.get();
        rm.sfq(frq);
        clm.mp(rm);

        xrq.release();
        yrq.release();
        zrq.release();
        frq.release();
        rm.r();
    }

    void ut(Matrix4f clm, int ti, AB cB, int cT) {
        float[] gt = cB.getTs(this.fid);
        float x = gt[0];
        float y = gt[1];
        float z = gt[2];
        if (this.vt[ti] != null) {
            TO to1 = this.vt[ti][3];
            TO to2 = this.vt[ti][4];
            TO to3 = this.vt[ti][5];
            if (to1 != null) {
                x = to1.gv(cT);
            }
            if (to2 != null) {
                y = to2.gv(cT);
            }
            if (to3 != null) {
                z = to3.gv(cT);
            }
        }
        clm.values[12] = x;
        clm.values[13] = y;
        clm.values[14] = z;
    }

    void us(Matrix4f clm, int ti, AB cB, int cT) {
        float[] scale = cB.getSc(this.fid);
        float x = scale[0];
        float y = scale[1];
        float z = scale[2];
        if (this.vt[ti] != null) {
            TO to1 = this.vt[ti][6];
            TO to2 = this.vt[ti][7];
            TO to3 = this.vt[ti][8];
            if (to1 != null) {
                x = to1.gv(cT);
            }
            if (to2 != null) {
                y = to2.gv(cT);
            }
            if (to3 != null) {
                z = to3.gv(cT);
            }
        }
        Matrix4f sm = Matrix4f.get();
        sm.sc(x, y, z);
        clm.mp(sm);
        sm.r();
    }

    @Override
    public FrameBase getFrameBase() {
        return frameBase;
    }

    @Override
    public boolean hasAlphaTransform() {
        return false;
    }

}
