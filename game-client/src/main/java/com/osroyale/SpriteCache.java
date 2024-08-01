package com.osroyale;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.io.ByteArrayInputStream;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.Arrays;

public final class SpriteCache implements Closeable {

    private Sprite[] cache;

    private ByteBuffer dataChannel;
    private ByteBuffer metaChannel;

    public void init(StreamLoader archive) {
        byte[] data = archive.getFile(Configuration.SPRITE_FILE_NAME + ".dat");
        dataChannel = ByteBuffer.allocateDirect(data.length);
        dataChannel.put(data);
        dataChannel.flip();

        byte[] meta = archive.getFile(Configuration.SPRITE_FILE_NAME + ".idx");
        metaChannel = ByteBuffer.allocateDirect(meta.length);
        metaChannel.put(meta);
        metaChannel.flip();

        final int spriteCount = meta.length / 10;

        cache = new Sprite[spriteCount];
    }

    public Sprite get(int id) {
        try {
            final Sprite[] cache = this.cache;
            if (cache == null) {
                return null;
            }

            if (contains(id)) {
                return cache[id];
            }

            final int entries = metaChannel.capacity() / 10;

            if (id > entries) {
                //System.out.printf("id=%d > size=%d%n", id, entries);
                return null;
            }

            int p = id * 10;

            final int pos = ((metaChannel.get(p) & 0xFF) << 16) | ((metaChannel.get(p + 1) & 0xFF) << 8) | (metaChannel.get(p + 2) & 0xFF);
            final int len = ((metaChannel.get(p + 3) & 0xFF) << 16) | ((metaChannel.get(p + 4) & 0xFF) << 8) | (metaChannel.get(p + 5) & 0xFF);
            final int offsetX = metaChannel.getShort(p + 6) & 0xFF;
            final int offsetY = metaChannel.getShort(p + 8) & 0xFF;

            final byte[] dataBuf = new byte[len];
            dataChannel.position(pos);
            dataChannel.get(dataBuf, 0, len);

            try (InputStream is = new ByteArrayInputStream(dataBuf)) {

                BufferedImage bimage = ImageIO.read(is);

                if (bimage == null) {
                    System.out.printf("Could not read image at %d%n", id);
                    return null;
                }

                if (bimage.getType() != BufferedImage.TYPE_INT_ARGB) {
                    bimage = convert(bimage, BufferedImage.TYPE_INT_ARGB);
                }

                int[] pixels = ((DataBufferInt) bimage.getRaster().getDataBuffer()).getData();

                Sprite sprite = new Sprite(bimage.getWidth(), bimage.getHeight(), offsetX, offsetY, pixels);

                // cache so we don't have to perform I/O calls again
                cache[id] = sprite;

                return sprite;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.printf("No sprite found for id=%d%n", id);
        return null;
    }

    public boolean contains(final int id) {
        final Sprite[] cache = this.cache;
        if (cache == null) {
            return false;
        }
        if (id < 0 || id >= cache.length) {
            return false;
        }
        return cache[id] != null;
    }

    public void set(final int id,
                    final Sprite sprite) {
        cache[id] = sprite;
    }

    public void clear() {
        Arrays.fill(cache, null);
    }

    private static BufferedImage convert(BufferedImage bimage, int type) {
        BufferedImage converted = new BufferedImage(bimage.getWidth(), bimage.getHeight(), type);
        converted.getGraphics().drawImage(bimage, 0, 0, null);
        return converted;
    }

    public void close() throws IOException {
    }

}
