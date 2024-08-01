package com.osroyale;

public final class NormalFrame implements Frame {

    public NormalFrame(final int groupId, final byte[] bytes) {
        final Buffer in = new Buffer(bytes);

        final int framemapArchiveIndex = this.framemapArchiveIndex = in.readUnsignedShort();
        final int length = in.readUnsignedByte();
        //System.err.println(animationFile+":"+frameFile+" array="+array.length+ ", length="+length);

        this.frameBase = FrameBase.frameBases[framemapArchiveIndex];
        //System.err.println("frame " + groupId + " vs " + framemapArchiveIndex + " (base=" + frameBase + " vs real " + FrameBase.frameBases[groupId] + ")");

        final Buffer data = new Buffer(bytes);
        data.position = length + in.position;

        final int[] types = frameBase.types;

        final int[] indexFrameIds = new int[500];
        final int[] scratchTranslatorX = new int[500];
        final int[] scratchTranslatorY = new int[500];
        final int[] scratchTranslatorZ = new int[500];

        int lastI = -1;
        int index = 0;
        for (int i = 0; i < length; ++i) {
            final int attribute = in.readUnsignedByte();
            if (attribute <= 0) continue;

            final int type = types[i];
            if (type != 0) {
                for (int j = i - 1; j > lastI; --j) {
                    if (types[j] == 0) {
                        indexFrameIds[index] = j;
                        scratchTranslatorX[index] = 0;
                        scratchTranslatorY[index] = 0;
                        scratchTranslatorZ[index] = 0;
                        ++index;
                        break;
                    }
                }
            }

            indexFrameIds[index] = i;

            final int defaultValue = type == 3 ? 128 : 0;

            scratchTranslatorX[index] = (attribute & 1) == 0 ? defaultValue : data.readShortSmart();
            scratchTranslatorY[index] = (attribute & 2) == 0 ? defaultValue : data.readShortSmart();
            scratchTranslatorZ[index] = (attribute & 4) == 0 ? defaultValue : data.readShortSmart();

            if (type == 5) {
                this.hasAlphaTransform = true;
            }

            lastI = i;
            ++index;
        }

        if (data.position != bytes.length) {
            new RuntimeException("mismatch for frame, lenght=" + length + ", index=" + index).printStackTrace();
            return;
        }

        this.translatorCount = index;
        this.indexFrameIds = new int[index];
        this.translatorX = new int[index];
        this.translatorY = new int[index];
        this.translatorZ = new int[index];

        for (int i = 0; i < index; ++i) {
            this.indexFrameIds[i] = indexFrameIds[i];
            this.translatorX[i] = scratchTranslatorX[i];
            this.translatorY[i] = scratchTranslatorY[i];
            this.translatorZ[i] = scratchTranslatorZ[i];
        }
    }

    public final int framemapArchiveIndex;

    int defaultFrameDelay;
    private final FrameBase frameBase;
    int translatorCount;
    int[] indexFrameIds;
    int[] translatorX;
    int[] translatorY;
    int[] translatorZ;

    private boolean hasAlphaTransform;

    @Override
    public FrameBase getFrameBase() {
        return frameBase;
    }

    @Override
    public boolean hasAlphaTransform() {
        return hasAlphaTransform;
    }

}
