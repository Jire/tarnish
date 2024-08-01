package com.osroyale.textures;

import com.osroyale.Buffer;
import com.osroyale.Client;
import com.osroyale.Deque;
import com.osroyale.StreamLoader;
import net.runelite.rs.api.RSTexture;
import net.runelite.rs.api.RSTextureProvider;

public class TextureProvider implements RSTextureProvider, TextureLoader {

    private final Texture[] textures;

    private Deque deque;

    private int capacity;

    private int remaining;

    private double brightness;

    private int textureSize;


    public TextureProvider(final Client client,
                           final StreamLoader configArchive,
                           final int capacity,
                           final int textureSize) {
        deque = new Deque();
        this.capacity = capacity;
        this.remaining = this.capacity;
        this.textureSize = textureSize;

        final Buffer buffer = new Buffer(configArchive.getFile("textures.dat"));

        final int highestFileId = buffer.readUShort();
        textures = new Texture[highestFileId + 1];

        for (int i = 0; i <= highestFileId; ++i) {
            final int id = buffer.readUnsignedShort();
            final int size = buffer.readUnsignedShort();
            final byte[] data = buffer.readBytes(size);

            final Texture texture = new Texture(id, new Buffer(data), client);
            textures[id] = texture;

            if (id >= highestFileId) break;
        }

        setMaxSize(128);
        setSize(128);

        client.flushFileClient();
    }

    @Override
    public double getBrightness() {
        return brightness;
    }

    public void setBrightness(double brightness) {
        this.brightness = brightness;
        clear();
    }

    public void setTextureSize(int textureSize) {
        this.textureSize = textureSize;
        clear();
    }

    @Override
    public void setMaxSize(int maxSize) {
        capacity = maxSize;
    }

    @Override
    public void setSize(int size) {
        remaining = size;
    }

    @Override
    public RSTexture[] getTextures() {
        return textures;
    }

    @Override
    public int[] load(int textureId) {
        return getTexturePixels(textureId);
    }

    public int[] getTexturePixels(final int textureID) {
        final Texture texture = textures[textureID];
        if (texture != null) {
            //noinspection SynchronizationOnLocalVariableOrMethodParameter
            synchronized (texture) {
                if (texture.pixels != null) {
                    deque.insertTail(texture);
                    texture.isLoaded = true;
                    return texture.pixels;
                }

                boolean hasLoaded = texture.load(brightness, textureSize, Client.instance);
                if (hasLoaded) {
                    if (remaining == 0) {
                        Texture currentTexture = (Texture) deque.popHead();
                        currentTexture.reset();
                    } else {
                        --remaining;
                    }

                    deque.insertTail(texture);
                    texture.isLoaded = true;
                    return texture.pixels;
                }
            }
        }

        return null;
    }

    public int getAverageTextureRGB(int textureID) {
        return textures[textureID] != null ? textures[textureID].averageRGB : 0;
    }

    public boolean isTransparent(int textureID) {
        return textures[textureID].isTransparent;
    }

    @Override
    public boolean isLowDetail(int textureID) {
        return textureSize == 64;
    }

    public void clear() {
        for (Texture texture : textures) {
            if (texture != null) {
                texture.reset();
            }
        }

        deque = new Deque();
        remaining = capacity;
    }

    public void animate(int textureID) {
        for (Texture texture : textures) {
            if (texture != null && texture.animationDirection != 0 && texture.isLoaded) {
                texture.animate(textureID);
                texture.isLoaded = false;
            }
        }
    }

}
