package com.osroyale.engine.task.impl;

import com.osroyale.engine.task.Clock;
import com.osroyale.engine.task.TaskUtils;
import net.runelite.rs.api.RSMilliClock;

public class MilliClock extends Clock implements RSMilliClock {

    long[] field1788;
    int field1787;

    int field1789;

    long field1794;

    int field1791;

    int field1792;

    public MilliClock() {
        this.field1788 = new long[10];
        this.field1787 = 256;
        this.field1789 = 1;
        this.field1791 = 0;
        this.field1794 = method2692();

        for (int var1 = 0; var1 < 10; ++var1) {
            this.field1788[var1] = this.field1794;
        }

    }


    public void mark() {
        for (int var1 = 0; var1 < 10; ++var1) {
            this.field1788[var1] = 0L;
        }
    }

    static long field3170;
    static long field4425;

    public static synchronized long method2692() {
        long var0 = System.currentTimeMillis();
        if (var0 < field3170) {
            field4425 += field3170 - var0;
        }

        field3170 = var0;
        return field4425 + var0;
    }

    public int wait(int var1, int var2) {
        int var3 = this.field1787;
        int var4 = this.field1789;
        this.field1787 = 300;
        this.field1789 = 1;
        this.field1794 = method2692();
        if (this.field1788[this.field1792] == 0L) {
            this.field1787 = var3;
            this.field1789 = var4;
        } else if (this.field1794 > this.field1788[this.field1792]) {
            this.field1787 = (int) ((long) (var1 * 2560) / (this.field1794 - this.field1788[this.field1792]));
        }

        if (this.field1787 < 25) {
            this.field1787 = 25;
        }

        if (this.field1787 > 256) {
            this.field1787 = 256;
            this.field1789 = (int) ((long) var1 - (this.field1794 - this.field1788[this.field1792]) / 10L);
        }

        if (this.field1789 > var1) {
            this.field1789 = var1;
        }

        this.field1788[this.field1792] = this.field1794;
        this.field1792 = (this.field1792 + 1) % 10;
        int var5;
        if (this.field1789 > 1) {
            for (var5 = 0; var5 < 10; ++var5) {
                if (this.field1788[var5] != 0L) {
                    this.field1788[var5] += (long) this.field1789;
                }
            }
        }

        if (this.field1789 < var2) {
            this.field1789 = var2;
        }

        TaskUtils.sleep((long) this.field1789);

        for (var5 = 0; this.field1791 < 256; this.field1791 += this.field1787) {
            ++var5;
        }

        this.field1791 &= 255;
        return var5;
    }


}
