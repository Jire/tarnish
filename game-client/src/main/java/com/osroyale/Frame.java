package com.osroyale;

import com.osroyale.skeletal.SkeletalFrame;

/**
 * @author Jire
 */
public interface Frame {

    FrameBase getFrameBase();

    boolean hasAlphaTransform();

    Frame[][] allFrames = new Frame[4000][];

    static Frame getFrame(final int frameId) {
        final int groupId = frameId >>> 16;

        final Frame[] frames = allFrames[groupId];
        if (frames == null) {
            Client.instance.onDemandFetcher.loadData(1, groupId);
            return null;
        }

        final int fileId = frameId & 0xFFFF;
        return frames[fileId];
    }

    static Frame[] loadFrames(final int groupId, final byte[] data) {
        final Buffer buffer = new Buffer(data);

        final int highestFileId = buffer.readUnsignedShort();
        final Frame[] frames = new Frame[highestFileId + 1];

        for (int i = 0; i <= highestFileId; i++) {
            final int fileId = buffer.readUnsignedShort();
            final int fileSize = buffer.readMedium();

            final byte[] fileData = new byte[fileSize];
            System.arraycopy(buffer.array, buffer.position, fileData, 0, fileSize);
            buffer.position += fileSize;

            final int frameId = (groupId << 16) | fileId;

            final Frame frame =
                    SkeletalFrame.skeletalFrameIds.contains(frameId)
                            ? new SkeletalFrame(fileData)
                            : new NormalFrame(groupId, fileData);
            frames[fileId] = frame;

            if (fileId >= highestFileId) break;
        }

        return allFrames[groupId] = frames;
    }

    static boolean hasAlphaTransform(final int frameID) {
        return frameID == -1;
    }

}
