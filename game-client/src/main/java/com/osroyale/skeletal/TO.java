package com.osroyale.skeletal;

import com.osroyale.Buffer;
import com.osroyale.math.FloatMaths;
import com.osroyale.misc.SerialEnum;
import lombok.ToString;

@ToString
public class TO {

    private boolean iti;

    private boolean it;

    private IpMode sip;

    private IpMode eip;

    private Tkf[] tkfs;

    private boolean isc;

    private float aS;

    private float eS;

    private final float[] ib1 = new float[4];

    private final float[] ib2 = new float[4];

    private boolean kD = true;

    private int ckI = 0;

    public float[] v;

    private int st;

    private int et;

    private float miv;

    private float mav;

    TO() {
    }

    int dc(Buffer b) {
        int mc = b.readUnsignedShort();
        int cid = b.readUnsignedByte();
        Toca ctg = SerialEnum.findEnumerated(Toca.VALUES, cid);
        if (ctg == null) {
            ctg = Toca.NULL;
        }
        int sipId = b.readUnsignedByte();
        IpMode sip = SerialEnum.findEnumerated(IpMode.VALUES, sipId);
        if (sip == null) {
            sip = IpMode.NULL;
        }
        this.sip = sip;
        int eipId = b.readUnsignedByte();
        IpMode eip = SerialEnum.findEnumerated(IpMode.VALUES, eipId);

        if (eip == null) {
            eip = IpMode.NULL;
        }
        this.eip = eip;

        this.iti = b.readUnsignedByte() != 0;
        this.tkfs = new Tkf[mc];
        Tkf ltkf = null;
        for (int index = 0; index < mc; index++) {
            Tkf tkf = new Tkf();
            tkf.decode(b);
            this.tkfs[index] = tkf;
            if (ltkf != null) {
                ltkf.nTkf = tkf;
            }
            ltkf = tkf;
        }
        this.st = this.tkfs[0].kti;
        this.et = this.tkfs[this.getKC() - 1].kti;
        this.v = new float[this.getEtSt() + 1];
        for (int cT = this.getSt(); cT <= this.getEt(); cT++) {
            this.v[cT - this.getSt()] = cv(this, (float) cT);
        }
        this.tkfs = null;
        this.miv = cv(this, ((float) (this.getSt() - 1)));
        this.mav = cv(this, ((float) (this.getEt() + 1)));
        return mc;
    }

    static float cv(TO to, float cT) {
        if (to != null && to.getKC() != 0) {
            if (cT < ((float) (to.tkfs[0].kti))) {
                return to.sip == IpMode.NULL ? to.tkfs[0].vAtTkf : method3390(to, cT, true);
            } else if (cT > ((float) (to.tkfs[to.getKC() - 1].kti))) {
                return to.eip == IpMode.NULL ? to.tkfs[to.getKC() - 1].vAtTkf : method3390(to, cT, false);
            } else if (to.it) {
                return to.tkfs[0].vAtTkf;
            } else {
                Tkf tkf = to.getTkf(cT);
                boolean isS = false;
                boolean cIP = false;
                if (tkf == null) {
                    return 0.0F;
                } else {
                    if (0.0 == ((double) (tkf.btDelta)) && ((double) (tkf.bvDelta)) == 0.0) {
                        isS = true;
                    } else if (Float.MAX_VALUE == tkf.btDelta && Float.MAX_VALUE == tkf.bvDelta) {
                        cIP = true;
                    } else if (tkf.nTkf != null) {
                        if (to.kD) {
                            float[] buf1 = new float[4];
                            float[] buf2 = new float[4];
                            buf1[0] = ((float) (tkf.kti));
                            buf2[0] = tkf.vAtTkf;
                            buf1[1] = buf1[0] + tkf.btDelta * 0.33333334F;
                            buf2[1] = 0.33333334F * tkf.bvDelta + buf2[0];
                            buf1[3] = ((float) (tkf.nTkf.kti));
                            buf2[3] = tkf.nTkf.vAtTkf;
                            buf1[2] = buf1[3] - tkf.nTkf.etDelta * 0.33333334F;
                            buf2[2] = buf2[3] - 0.33333334F * tkf.nTkf.evDelta;
                            if (to.iti) {
                                method1774(to, buf1, buf2);
                            } else {
                                to.aS = buf1[0];
                                float var7 = buf1[3] - buf1[0];
                                float var8 = buf2[3] - buf2[0];
                                float var9 = buf1[1] - buf1[0];
                                float var10 = 0.0F;
                                float var11 = 0.0F;
                                if (0.0 != ((double) (var9))) {
                                    var10 = (buf2[1] - buf2[0]) / var9;
                                }
                                var9 = buf1[3] - buf1[2];
                                if (0.0 != ((double) (var9))) {
                                    var11 = (buf2[3] - buf2[2]) / var9;
                                }
                                float var12 = 1.0F / (var7 * var7);
                                float var13 = var10 * var7;
                                float var14 = var7 * var11;
                                to.ib1[0] = var12 * (var13 + var14 - var8 - var8) / var7;
                                to.ib1[1] = (var8 + var8 + var8 - var13 - var13 - var14) * var12;
                                to.ib1[2] = var10;
                                to.ib1[3] = buf2[0];
                            }
                            to.kD = false;
                        }
                    } else {
                        isS = true;
                    }
                    if (isS) {
                        return tkf.vAtTkf;
                    } else if (cIP) {
                        return ((float) (tkf.kti)) != cT && tkf.nTkf != null ? tkf.nTkf.vAtTkf : tkf.vAtTkf;
                    } else {
                        return to.iti ? cTVAT(to, cT) : method354(to, cT);
                    }
                }
            }
        } else {
            return 0.0F;
        }
    }

    // interpolation related
    static float method354(TO to, float cT) {
        if (to == null) {
            return 0.0F;
        } else {
            float p = cT - to.aS;
            return p * (to.ib1[2] + (p * to.ib1[0] + to.ib1[1]) * p) + to.ib1[3];
        }
    }

    public static void method1774(TO operation, float[] buf1, float[] buf2) {
        if (operation != null) {
            float var3 = buf1[3] - buf1[0];
            if (0.0 != ((double) (var3))) {
                float var4 = buf1[1] - buf1[0];
                float var5 = buf1[2] - buf1[0];
                float var6 = var4 / var3;
                float var7 = var5 / var3;
                operation.isc = var6 == 0.33333334F && var7 == 0.6666667F;
                float var8 = var6;
                float var9 = var7;
                if (((double) (var6)) < 0.0) {
                    var6 = 0.0F;
                }
                if (((double) (var7)) > 1.0) {
                    var7 = 1.0F;
                }
                if (((double) (var6)) > 1.0 || var7 < -1.0F) {
                    method5509(var6, var7);
                }
                if (var6 != var8) {
                    buf1[1] = buf1[0] + var6 * var3;
                    if (0.0 != ((double) (var8))) {
                        buf2[1] = buf2[0] + (buf2[1] - buf2[0]) * var6 / var8;
                    }
                }
                if (var7 != var9) {
                    buf1[2] = buf1[0] + var7 * var3;
                    if (1.0 != ((double) (var9))) {
                        buf2[2] = ((float) (((double) (buf2[3])) - ((double) (buf2[3] - buf2[2])) * (1.0 - ((double) (var7))) / (1.0 - ((double) (var9)))));
                    }
                }
                operation.aS = buf1[0];
                operation.eS = buf1[3];
                float var10 = var6;
                float var11 = var7;
                float[] ib1 = operation.ib1;
                float var13 = var10 - 0.0F;
                float var14 = var11 - var10;
                float var15 = 1.0F - var11;
                float var16 = var14 - var13;
                ib1[3] = var15 - var14 - var16;
                ib1[2] = var16 + var16 + var16;
                ib1[1] = var13 + var13 + var13;
                ib1[0] = 0.0F;
                var13 = buf2[0];
                var14 = buf2[1];
                var15 = buf2[2];
                var16 = buf2[3];
                float[] ib2 = operation.ib2;
                float var18 = var14 - var13;
                float var19 = var15 - var14;
                float var20 = var16 - var15;
                float var21 = var19 - var18;
                ib2[3] = var20 - var19 - var21;
                ib2[2] = var21 + var21 + var21;
                ib2[1] = var18 + var18 + var18;
                ib2[0] = var13;
            }
        }
    }

    static void method5509(float var0, float var1) {
        var1 = 1.0F - var1;
        if (var0 < 0.0F) {
            var0 = 0.0F;
        }
        if (var1 < 0.0F) {
            var1 = 0.0F;
        }
        if (var0 > 1.0F || var1 > 1.0F) {
            float var2 = ((float) (((double) (var0 * (var0 - 2.0F + var1))) + ((double) (var1)) * (((double) (var1)) - 2.0) + 1.0));
            if (var2 + FloatMaths.ulp1 > 0.0F) {
                method270(var0, var1);
            }
        }
        var1 = 1.0F - var1;
    }

    static void method270(float var0, float var1) {
        if (var0 + FloatMaths.ulp1 < 1.3333334F) {
            float var2 = var0 - 2.0F;
            float var3 = var0 - 1.0F;
            float var4 = ((float) (Math.sqrt(var2 * var2 - var3 * var3 * 4.0F)));
            float var5 = (var4 + -var2) * 0.5F;
            if (var1 + FloatMaths.ulp1 > var5) {
                var1 = var5 - FloatMaths.ulp1;
            } else {
                var5 = (-var2 - var4) * 0.5F;
                if (var1 < FloatMaths.ulp1 + var5) {
                    var1 = FloatMaths.ulp1 + var5;
                }
            }
        } else {
            var0 = 1.3333334F - FloatMaths.ulp1;
            var1 = 0.33333334F - FloatMaths.ulp1;
        }
    }

    public static float method3390(TO TO, float var1, boolean var2) {
        float var3 = 0.0F;
        if (TO != null && TO.getKC() != 0) {
            float firstKeyframeTick = ((float) (TO.tkfs[0].kti));
            float lastKeyframeTick = ((float) (TO.tkfs[TO.getKC() - 1].kti));
            float keyFrameTickDifference = lastKeyframeTick - firstKeyframeTick;
            if (0.0 == ((double) (keyFrameTickDifference))) {
                return TO.tkfs[0].vAtTkf;
            } else {
                float var7;
                if (var1 > lastKeyframeTick) {
                    var7 = (var1 - lastKeyframeTick) / keyFrameTickDifference;
                } else {
                    var7 = (var1 - firstKeyframeTick) / keyFrameTickDifference;
                }
                double var8 = (int) var7;
                float var10 = Math.abs(((float) (((double) (var7)) - var8)));
                float var11 = var10 * keyFrameTickDifference;
                var8 = Math.abs(var8 + 1.0);
                double var12 = var8 / 2.0;
                double var14 = (int) var12;
                var10 = ((float) (var12 - var14));
                float var16;
                float var17;
                if (var2) {
                    if (TO.sip == IpMode.field1422) {
                        if (((double) (var10)) != 0.0) {
                            var11 += firstKeyframeTick;
                        } else {
                            var11 = lastKeyframeTick - var11;
                        }
                    } else if (TO.sip != IpMode.field1424 && TO.sip != IpMode.field1425) {
                        if (TO.sip == IpMode.field1423) {
                            var11 = firstKeyframeTick - var1;
                            var16 = TO.tkfs[0].etDelta;
                            var17 = TO.tkfs[0].evDelta;
                            var3 = TO.tkfs[0].vAtTkf;
                            if (((double) (var16)) != 0.0) {
                                var3 -= var17 * var11 / var16;
                            }
                            return var3;
                        }
                    } else {
                        var11 = lastKeyframeTick - var11;
                    }
                } else if (TO.eip == IpMode.field1422) {
                    if (0.0 != ((double) (var10))) {
                        var11 = lastKeyframeTick - var11;
                    } else {
                        var11 += firstKeyframeTick;
                    }
                } else if (TO.eip != IpMode.field1424 && TO.eip != IpMode.field1425) {
                    if (TO.eip == IpMode.field1423) {
                        var11 = var1 - lastKeyframeTick;
                        var16 = TO.tkfs[TO.getKC() - 1].btDelta;
                        var17 = TO.tkfs[TO.getKC() - 1].bvDelta;
                        var3 = TO.tkfs[TO.getKC() - 1].vAtTkf;
                        if (((double) (var16)) != 0.0) {
                            var3 += var11 * var17 / var16;
                        }
                        return var3;
                    }
                } else {
                    var11 += firstKeyframeTick;
                }
                var3 = TO.cv(TO, var11);
                float var18;
                if (var2 && TO.sip == IpMode.field1425) {
                    var18 = TO.tkfs[TO.getKC() - 1].vAtTkf - TO.tkfs[0].vAtTkf;
                    var3 = ((float) (((double) (var3)) - ((double) (var18)) * var8));
                } else if (!var2 && TO.eip == IpMode.field1425) {
                    var18 = TO.tkfs[TO.getKC() - 1].vAtTkf - TO.tkfs[0].vAtTkf;
                    var3 = ((float) (((double) (var3)) + ((double) (var18)) * var8));
                }
                return var3;
            }
        } else {
            return var3;
        }
    }

    public static float cTVAT(TO TO, float time) {
        if (TO == null) {
            return 0.0F;
        } else {
            float p;
            if (time == TO.aS) {
                p = 0.0F;
            } else if (TO.eS == time) {
                p = 1.0F;
            } else {
                p = (time - TO.aS) / (TO.eS - TO.aS);
            }
            float r;
            if (TO.isc) {
                r = p;
            } else {
                float[] cv = new float[]{TO.ib1[0] - p, TO.ib1[1], TO.ib1[2], TO.ib1[3]};
                float[] ftk = new float[5];
                int ntkfs = ip(cv, 3, 0.0F, true, 1.0F, true, ftk);
                if (ntkfs == 1) {
                    r = ftk[0];
                } else {
                    r = 0.0F;
                }
            }
            return r * (TO.ib2[1] + r * (TO.ib2[2] + TO.ib2[3] * r)) + TO.ib2[0];
        }
    }

    public static int ip(float[] jw, int cT, float rS, boolean iS, float rE, boolean iE, float[] ftk) {
        float var7 = 0.0F;
        for (int var8 = 0; var8 < cT + 1; ++var8) {
            var7 += Math.abs(jw[var8]);
        }
        float var24 = (Math.abs(rS) + Math.abs(rE)) * ((float) (cT + 1)) * FloatMaths.ulp1;
        if (var7 <= var24) {
            return -1;
        } else {
            float[] ctw = new float[cT + 1];
            int var10;
            for (var10 = 0; var10 < cT + 1; ++var10) {
                ctw[var10] = jw[var10] * (1.0F / var7);
            }
            while (Math.abs(ctw[cT]) < var24) {
                --cT;
            }
            var10 = 0;
            if (cT == 0) {
                return var10;
            } else if (cT == 1) {
                ftk[0] = -ctw[0] / ctw[1];
                boolean var22 = (iS) ? rS < var24 + ftk[0] : rS < ftk[0] - var24;
                boolean var23 = (iE) ? rE > ftk[0] - var24 : rE > ftk[0] + var24;
                var10 = (var22 && var23) ? 1 : 0;
                if (var10 > 0) {
                    if (iS && ftk[0] < rS) {
                        ftk[0] = rS;
                    } else if (iE && ftk[0] > rE) {
                        ftk[0] = rE;
                    }
                }
                return var10;
            } else {
                IWT IWT = new IWT(ctw, cT);
                float[] var12 = new float[cT + 1];
                for (int var13 = 1; var13 <= cT; ++var13) {
                    var12[var13 - 1] = ctw[var13] * ((float) (var13));
                }
                float[] var21 = new float[cT + 1];
                int var14 = ip(var12, cT - 1, rS, false, rE, false, var21);
                if (var14 == -1) {
                    return 0;
                } else {
                    boolean var15 = false;
                    float var17 = 0.0F;
                    float var18 = 0.0F;
                    float var19 = 0.0F;
                    for (int var20 = 0; var20 <= var14; ++var20) {
                        if (var10 > cT) {
                            return var10;
                        }
                        float var16;
                        if (var20 == 0) {
                            var16 = rS;
                            var18 = acm(ctw, cT, rS);
                            if (Math.abs(var18) <= var24 && iS) {
                                ftk[var10++] = rS;
                            }
                        } else {
                            var16 = var19;
                            var18 = var17;
                        }
                        if (var20 == var14) {
                            var19 = rE;
                            var15 = false;
                        } else {
                            var19 = var21[var20];
                        }
                        var17 = acm(ctw, cT, var19);
                        if (var15) {
                            var15 = false;
                        } else if (Math.abs(var17) < var24) {
                            if (var20 != var14 || iE) {
                                ftk[var10++] = var19;
                                var15 = true;
                            }
                        } else if (var18 < 0.0F && var17 > 0.0F || var18 > 0.0F && var17 < 0.0F) {
                            ftk[var10++] = fov(IWT, var16, var19, 0.0F);
                            if (var10 > 1 && ftk[var10 - 2] >= ftk[var10 - 1] - var24) {
                                ftk[var10 - 2] = (ftk[var10 - 2] + ftk[var10 - 1]) * 0.5F;
                                --var10;
                            }
                        }
                    }
                    return var10;
                }
            }
        }
    }

    static float fov(IWT iwt, float s, float e, float tol) {
        float var4 = acm(iwt.iwt, iwt.cT, s);
        if (Math.abs(var4) < FloatMaths.ulp1) {
            return s;
        } else {
            float var5 = acm(iwt.iwt, iwt.cT, e);
            if (Math.abs(var5) < FloatMaths.ulp1) {
                return e;
            } else {
                float var6 = 0.0F;
                float var7 = 0.0F;
                float var8 = 0.0F;
                float var13 = 0.0F;
                boolean var14 = true;
                boolean var15 = false;
                do {
                    var15 = false;
                    if (var14) {
                        var6 = s;
                        var13 = var4;
                        var7 = e - s;
                        var8 = var7;
                        var14 = false;
                    }
                    if (Math.abs(var13) < Math.abs(var5)) {
                        s = e;
                        e = var6;
                        var6 = s;
                        var4 = var5;
                        var5 = var13;
                        var13 = var4;
                    }
                    float var16 = FloatMaths.ulpSq * Math.abs(e) + tol * 0.5F;
                    float var17 = (var6 - e) * 0.5F;
                    boolean var18 = Math.abs(var17) > var16 && 0.0F != var5;
                    if (var18) {
                        if (!(Math.abs(var8) < var16) && !(Math.abs(var4) <= Math.abs(var5))) {
                            float var12 = var5 / var4;
                            float var9;
                            float var10;
                            if (s == var6) {
                                var9 = 2.0F * var17 * var12;
                                var10 = 1.0F - var12;
                            } else {
                                var10 = var4 / var13;
                                float var11 = var5 / var13;
                                var9 = (2.0F * var17 * var10 * (var10 - var11) - (var11 - 1.0F) * (e - s)) * var12;
                                var10 = (var10 - 1.0F) * (var11 - 1.0F) * (var12 - 1.0F);
                            }
                            if (((double) (var9)) > 0.0) {
                                var10 = -var10;
                            } else {
                                var9 = -var9;
                            }
                            var12 = var8;
                            var8 = var7;
                            if (2.0F * var9 < var17 * 3.0F * var10 - Math.abs(var10 * var16) && var9 < Math.abs(var10 * 0.5F * var12)) {
                                var7 = var9 / var10;
                            } else {
                                var7 = var17;
                                var8 = var17;
                            }
                        } else {
                            var7 = var17;
                            var8 = var17;
                        }
                        s = e;
                        var4 = var5;
                        if (Math.abs(var7) > var16) {
                            e += var7;
                        } else if (((double) (var17)) > 0.0) {
                            e += var16;
                        } else {
                            e -= var16;
                        }
                        var5 = acm(iwt.iwt, iwt.cT, e);
                        if (((double) (var5 * (var13 / Math.abs(var13)))) > 0.0) {
                            var14 = true;
                            var15 = true;
                        } else {
                            var15 = true;
                        }
                    }
                } while (var15);
                return e;
            }
        }
    }

    public static float acm(float[] av, int cT, float mbE) {
        float accumulated = av[cT];
        for (int index = cT - 1; index >= 0; --index) {
            accumulated = mbE * accumulated + av[index];
        }
        return accumulated;
    }

    public float gv(int cT) {
        if (cT < this.getSt()) {
            return this.miv;
        } else {
            return cT > this.getEt() ? this.mav : this.v[cT - this.getSt()];
        }
    }

    int getSt() {
        return this.st;
    }

    int getEt() {
        return this.et;
    }

    int getEtSt() {
        return this.getEt() - this.getSt();
    }

    int getKBT(float cT) {
        if (this.ckI < 0 || !(((float) (this.tkfs[this.ckI].kti)) <= cT) || this.tkfs[this.ckI].nTkf != null && !(((float) (this.tkfs[this.ckI].nTkf.kti)) > cT)) {
            if (!(cT < ((float) (this.getSt()))) && !(cT > ((float) (this.getEt())))) {
                int count = this.getKC();
                int s = this.ckI;
                if (count > 0) {
                    int start = 0;
                    int end = count - 1;
                    // binary search
                    do {
                        int middle = start + end >> 1;
                        if (cT < ((float) (this.tkfs[middle].kti))) {
                            if (cT > ((float) (this.tkfs[middle - 1].kti))) {
                                s = middle - 1;
                                break;
                            }
                            end = middle - 1;
                        } else {
                            if (!(cT > ((float) (this.tkfs[middle].kti)))) {
                                s = middle;
                                break;
                            }
                            if (cT < ((float) (this.tkfs[middle + 1].kti))) {
                                s = middle;
                                break;
                            }
                            start = middle + 1;
                        }
                    } while (start <= end);
                }
                if (s != this.ckI) {
                    this.ckI = s;
                    this.kD = true;
                }
                return this.ckI;
            } else {
                return -1;
            }
        } else {
            return this.ckI;
        }
    }

    Tkf getTkf(float tick) {
        int kbt = this.getKBT(tick);
        return kbt >= 0 && kbt < this.tkfs.length ? this.tkfs[kbt] : null;
    }

    int getKC() {
        return this.tkfs == null ? 0 : this.tkfs.length;
    }
}
