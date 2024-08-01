package com.osroyale;

import com.osroyale.skeletal.SkeletalFrame;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;

import java.util.Arrays;

public final class Animation {

    static void unpackConfig(final StreamLoader streamLoader) {
        final Buffer stream = new Buffer(streamLoader.getFile("seq.dat"));

        final int highestFileId = stream.readUnsignedShort();
        System.err.println("HIGHEST ANIM IS " + highestFileId);

        Animation[] animations = Animation.animations;
        if (animations == null || animations.length < highestFileId + 1) {
            animations = Animation.animations = new Animation[highestFileId + 1];
        }

        for (int i = 0; i < highestFileId; i++) {
            try {
                final int id = stream.readUnsignedShort();
                //System.err.println("decoded " + id);
                if (id == -1 || id == 65535 || id >= 32767) {
                    System.err.println("skip " + id + " (i=" + i + ")");
                    continue;
                }

                final int animLength = stream.readUnsignedShort();
                final byte[] animData = new byte[animLength];
                System.arraycopy(stream.array, stream.position, animData, 0, animLength);
                stream.position += animLength;

                Animation animation = animations[id];
                if (animation == null) {
                    animation = animations[id] = new Animation(id);
                }

                //System.err.println("(" + i + ") decoded " + id + " with length " + animLength);

                animation.decode(new Buffer(animData));

                if (id >= highestFileId) {
                    //System.err.println("BREAK AT " + id + " (i=" + i + ")");
                    break;
                }
            } catch (final Exception e) {
                throw new RuntimeException("Failed at " + i, e);
            }
        }
    }

    public int duration(int i) {
        int j = frameDelays[i];
        if (j == 0) {
            final Frame frame = Frame.getFrame(primaryFrameIds[i]);
            if (frame instanceof NormalFrame) {
                final NormalFrame normalFrame = (NormalFrame) frame;
                j = frameDelays[i] = normalFrame.defaultFrameDelay;
            }
        }
        if (j == 0)
            j = 1;
        return j;
    }

    private void decode(final Buffer buffer) {
        int lastOpcode = -1;
        while (true) {
            final int opcode = buffer.readUnsignedByte();
            opcodes.add(opcode);
            if (opcode == 0) {
                break;
            } else if (opcode == 1) {
                frameCount = buffer.readUnsignedShort();
                primaryFrameIds = new int[frameCount];
                secondaryFrameIds = new int[frameCount];
                frameDelays = new int[frameCount];

                for (int i = 0; i < frameCount; i++) {
                    frameDelays[i] = buffer.readUnsignedShort();
                }

                for (int i = 0; i < frameCount; i++) {
                    primaryFrameIds[i] = buffer.readUnsignedShort();
                    secondaryFrameIds[i] = -1;
                }

                for (int i = 0; i < frameCount; i++) {
                    int frameBase = buffer.readUnsignedShort();
                    primaryFrameIds[i] += frameBase << 16;
                }
            } else if (opcode == 2) {
                frameStep = buffer.readUnsignedShort();
            } else if (opcode == 3) {
                int len = buffer.readUnsignedByte();
                final int[] interleaveOrder = this.interleaveOrder = new int[len + 1];
                final boolean[] booleanMasks = this.booleanMasks;
                for (int i = 0; i < len; i++) {
                    final int mask = buffer.readUnsignedByte();
                    interleaveOrder[i] = mask;
                    booleanMasks[mask] = true;
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
                loopCount = buffer.readUnsignedByte();
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
                    buffer.readUnsignedTriByte();
                }
            } else if (opcode == 14) {
                final int skeletalId = this.skeletalFrameId = buffer.readInt();
                if (SkeletalFrame.skeletalFrameIds.add(skeletalId)) {
                    /*System.err.println("(anim " + id + ") has skeletal frame "
                            + skeletalId + " (" + (skeletalId >>> 16) + ":" + (skeletalId & 0xFFFF) + ")");*/
                }
            } else if (opcode == 15) {
                // sound related
                int len = buffer.readUnsignedShort();

                for (int index = 0; index < len; ++index) {
                    buffer.readUnsignedShort();
                    buffer.readUnsignedTriByte();
                }
            } else if (opcode == 16) {
                this.rangeBegin = buffer.readUnsignedShort();
                this.rangeEnd = buffer.readUnsignedShort();
                this.skeletalLength = rangeEnd - rangeBegin;
            } else if (opcode == 17) {
                final boolean[] booleanMasks = this.booleanMasks;
                int len = buffer.readUnsignedByte();
                for (int i = 0; i < len; ++i) {
                    booleanMasks[buffer.readUnsignedByte()] = true;
                }
            } else {
                System.err.println("unknown opcode " + opcode + " for id " + id + ", last = " + lastOpcode);
                break;
            }
            lastOpcode = opcode;
        }

        if (frameCount == 0) {
            frameCount = 1;
            primaryFrameIds = new int[1];
            primaryFrameIds[0] = -1;
            secondaryFrameIds = new int[1];
            secondaryFrameIds[0] = -1;
            frameDelays = new int[1];
            frameDelays[0] = -1;
        }

        if (runFlag == -1) {
            runFlag = (interleaveOrder == null) ? 0 : 2;
        }

        if (priority == -1) {
            priority = (interleaveOrder == null) ? 0 : 2;
        }
    }


    private Animation(final int id) {
        this.id = id;
        frameStep = -1;
        allowsRotation = false;
        priority = 5;
        shield = -1;
        weapon = -1;
        loopCount = 99;
        runFlag = -1;
        walkFlag = -1;
        type = 1;
    }

    static Animation[] animations;

    public int frameCount;
    public int[] primaryFrameIds;
    int[] secondaryFrameIds;
    int[] frameDelays;
    public int frameStep;
    int[] interleaveOrder;
    boolean allowsRotation;
    int priority;
    int shield;
    int weapon;
    int loopCount;
    int runFlag;
    int walkFlag;
    public int type;
    final int id;

    private int skeletalFrameId = -1;
    private int rangeBegin;
    private int rangeEnd;
    private int skeletalLength;
    private final boolean[] booleanMasks = new boolean[256];

    IntList opcodes = new IntArrayList();

    public int getSkeletalFrameId() {
        return skeletalFrameId;
    }

    public int getRangeBegin() {
        return rangeBegin;
    }

    public int getRangeEnd() {
        return rangeEnd;
    }

    public boolean[] getBooleanMasks() {
        return booleanMasks;
    }

    public boolean isSkeletalAnimation() {
        return skeletalFrameId >= 0;
    }

    public int getSkeletalLength() {
        return skeletalLength;
    }

    public int getFrameStep() {
        return frameStep;
    }

    @Override
    public String toString() {
        return "Animation{" +
                "length=" + frameCount +
                ", primary=" + Arrays.toString(primaryFrameIds) +
                ", secondary=" + Arrays.toString(secondaryFrameIds) +
                ", duration=" + Arrays.toString(frameDelays) +
                ", padding=" + frameStep +
                ", interleaveOrder=" + Arrays.toString(interleaveOrder) +
                ", allowsRotation=" + allowsRotation +
                ", priority=" + priority +
                ", shield=" + shield +
                ", weapon=" + weapon +
                ", resetCycle=" + loopCount +
                ", runFlag=" + runFlag +
                ", walkFlag=" + walkFlag +
                ", type=" + type +
                ", id=" + id +
                '}';
    }

    public Model animateLoc(Model model, int direction, int frame) {
        Model animatedModel = model.toSharedSequenceModel(Model.objectModel, false);

        direction &= 0x3;
        if (direction == 1) {
            animatedModel.rotateBy270();
        } else if (direction == 2) {
            animatedModel.rotateBy180();
        } else if (direction == 3) {
            animatedModel.rotate90Degrees();
        }

        animatedModel.interpolate(primaryFrameIds[frame]);

        if (direction == 1) {
            animatedModel.rotate90Degrees();
        } else if (direction == 2) {
            animatedModel.rotateBy180();
        } else if (direction == 3) {
            animatedModel.rotateBy270();
        }
        return animatedModel;
    }
}
