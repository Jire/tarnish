package com.osroyale.fs.cache.decoder;

import io.netty.buffer.ByteBuf;

public final class AnimationDefinition {

    public final int id;

    public int length;
    public int[] primary;
    public int secondary[];
    public int[] duration;
    public int padding;
    public int interleaveOrder[];
    public boolean allowsRotation;
    public int priority;
    public int shield;
    public int weapon;
    public int resetCycle;
    public int runFlag;
    public int walkFlag;
    public int type;

    public AnimationDefinition(final int id) {
        this.id = id;

        padding = -1;
        allowsRotation = false;
        priority = 5;
        shield = -1;
        weapon = -1;
        resetCycle = 99;
        runFlag = -1;
        walkFlag = -1;
        type = 1;
    }

    public void decode(ByteBuf buffer) {
        while(true) {
            final int opcode = buffer.readUnsignedByte();

            if (opcode == 0) {
                break;
            } else if (opcode == 1) {
                length = buffer.readUnsignedShort();
                primary = new int[length];
                secondary = new int[length];
                duration = new int[length];

                for (int i = 0; i < length; i++) {
                    duration[i] = buffer.readUnsignedShort();
                }

                for (int i = 0; i < length; i++) {
                    primary[i] = buffer.readUnsignedShort();
                    secondary[i] = -1;
                }

                for (int i = 0; i < length; i++) {
                    primary[i] += buffer.readUnsignedShort() << 16;
                }
            } else if (opcode == 2) {
                padding = buffer.readUnsignedShort();
            } else if (opcode == 3) {
                int len = buffer.readUnsignedByte();
                interleaveOrder = new int[len + 1];
                for (int i = 0; i < len; i++) {
                    interleaveOrder[i] = buffer.readUnsignedByte();
                }
                interleaveOrder[len] = 9999999;
            } else if (opcode == 4) {
                allowsRotation = true;
            } else if (opcode == 5) {
                priority = buffer.readUnsignedByte();
            } else if (opcode == 6) {
                shield = buffer.readUnsignedShort();
            } else if (opcode == 7) {
                weapon = buffer.readUnsignedShort();
            } else if (opcode == 8) {
                resetCycle = buffer.readUnsignedByte();
            } else if (opcode == 9) {
                runFlag = buffer.readUnsignedByte();
            } else if (opcode == 10) {
                walkFlag = buffer.readUnsignedByte();
            } else if (opcode == 11) {
                type = buffer.readUnsignedByte();
            } else if (opcode == 12) {
                int len = buffer.readUnsignedByte();

                for (int i = 0; i < len; i++) {
                    buffer.readUnsignedShort();
                }

                for (int i = 0; i < len; i++) {
                    buffer.readUnsignedShort();
                }
            } else if (opcode == 13) {
                int len = buffer.readUnsignedByte();
                for (int i = 0; i < len; i++) {
                    buffer.readUnsignedMedium();
                }
            } else if (opcode == 14) {
                buffer.readUnsignedInt();
            } else if (opcode == 15) {
                int len = buffer.readUnsignedShort();

                for (int index = 0; index < len; ++index) {
                    buffer.readUnsignedShort();
                    buffer.readUnsignedMedium();
                }
            } else if (opcode == 16) {
                buffer.readUnsignedShort();
                buffer.readUnsignedShort();
            } else if (opcode == 17) {

                int len = buffer.readUnsignedByte();
                for (int i = 0; i < len; ++i) {
                    buffer.readUnsignedByte();
                }
            }
        }
        if (length == 0) {
            length = 1;
            primary = new int[1];
            primary[0] = -1;
            secondary = new int[1];
            secondary[0] = -1;
            duration = new int[1];
            duration[0] = -1;
        }

        if (runFlag == -1) {
            runFlag = (interleaveOrder == null) ? 0 : 2;
        }

        if (priority == -1) {
            priority = (interleaveOrder == null) ? 0 : 2;
        }

        durationTime = calculateDuration();
    }

    public int durationTime = 0;

    private int calculateDuration() {
        int duration = 0;
        if (this.duration == null) {
            return 0;
        }
        for (final int i : this.duration) {
            /*if (i < 0 || i > 30) {
                continue;
            }*/
            duration += Math.abs(i) * 20;
        }
        return duration;
    }

}
