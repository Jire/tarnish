package com.osroyale.skeletal;

import com.osroyale.Buffer;

public final class ABW {

    AB[] es;

    int mc;

    public ABW(Buffer buffer, int length) {
        this.es = new AB[length];
        this.mc = buffer.readUnsignedByte();
        for (int index = 0; index < this.es.length; index++) {
            AB bone = new AB(this.mc, buffer, false);
            this.es[index] = bone;
        }
        try {
            this.ip();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void ip() {
        AB[] es = this.es;
        for (AB ab : es) {
            final int b_pid = ab.b_pid;
            if (b_pid >= 0 && b_pid < es.length) {
                ab.p_b = es[b_pid];
            }
        }
    }

    public int getEsLength() {
        return this.es.length;
    }

    public AB getAB(int bi) {
        return this.es[bi];
    }

    public AB[] getEs() {
        return this.es;
    }

    public void ut(SkeletalFrame skeletalFrame, int tick) {
        this.ut(skeletalFrame, tick, null, false);
    }

    public void ut(SkeletalFrame sk, int ct, boolean[] bm, boolean state) {
        int fid = sk.getFid();
        int ti = 0;
        AB[] es = this.getEs();
        for (AB cB : es) {
            if (bm == null || state == bm[ti]) {
                sk.du(ct, cB, ti, fid);
            }
            ti++;
        }
    }
}