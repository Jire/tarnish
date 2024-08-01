package com.osroyale;

import com.osroyale.skeletal.ABW;

public final class FrameBase {

    public final int id;
    public final int length;
    final int[] types;
    final int[][] frameMaps;

    public final ABW abw;

    FrameBase(final int id, final Buffer in) {
        this.id = id;

        final int length = this.length = in.readUnsignedByte();
        final int[] types = this.types = new int[length];
        final int[][] frameMaps = this.frameMaps = new int[length][];

        for (int i = 0; i < length; ++i) {
            types[i] = in.readUnsignedByte();
        }

        for (int i = 0; i < length; ++i) {
            frameMaps[i] = new int[in.readUnsignedByte()];
        }

        for (int i = 0; i < length; ++i) {
            final int[] frameMap = frameMaps[i];
            for (int j = 0; j < frameMap.length; ++j) {
                frameMap[j] = in.readUnsignedByte();
            }
        }

        this.abw = loadABW(in);
    }

    public int getId() {
        return id;
    }

    public int getLength() {
        return length;
    }

    public int[] getTypes() {
        return types;
    }

    public int[][] getFrameMaps() {
        return frameMaps;
    }

    public ABW getAbw() {
        return abw;
    }

    private static ABW loadABW(final Buffer in) {
        if (in.position < in.array.length) {
            final int abwLength = in.readUnsignedShort();
            return abwLength > 0
                    ? new ABW(in, abwLength)
                    : null;
        }

        return null;
    }

    public static final FrameBase[] frameBases = new FrameBase[4000];

    public static void loadFrameBases(final StreamLoader streamLoader) {
        final Buffer stream = new Buffer(streamLoader.getFile("framebases.dat"));

        final int count = stream.readUnsignedShort();
        for (int i = 0; i < count; i++) {
            final int fileId = stream.readUnsignedShort();
            final int fileSize = stream.readUnsignedShort();

            final byte[] fileData = stream.readBytes(fileSize);
            final Buffer fileBuffer = new Buffer(fileData);

            frameBases[fileId] = new FrameBase(fileId, fileBuffer);
        }
        System.err.println("framebases count " + count + " loaded!");
    }

}