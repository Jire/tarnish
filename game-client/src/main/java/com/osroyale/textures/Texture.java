package com.osroyale.textures;

import com.osroyale.*;
import net.runelite.rs.api.RSTexture;

public class Texture extends Linkable implements RSTexture {

    private static int[] animatedPixels;

    public int averageRGB;

    public boolean isTransparent = false;

    private int[] fileIds;

    private int[] field2301;

    private int[] field2296;

    private int[] field2295;

    public int animationDirection;

    private int animationSpeed;
    public final int id;
    public int[] pixels;

    public boolean isLoaded;

    Texture(final int id, final Buffer buffer, final Client client) {
        this.id = id;
        this.isLoaded = false;

        averageRGB = buffer.readUShort();
        isTransparent = buffer.readSignedByte() != 0;

        final int count = buffer.readUnsignedByte();

        fileIds = new int[count];
        for (int index = 0; index < count; index++) {
            fileIds[index] = buffer.readUShort();
        }

        if (count > 1) {
            field2301 = new int[count - 1];
            for (int index = 0; index < count - 1; index++) {
                field2301[index] = buffer.readUnsignedByte();
            }

            field2296 = new int[count - 1];
            for (int index = 0; index < count - 1; index++) {
                field2296[index] = buffer.readUnsignedByte();
            }
        }

        field2295 = new int[count];
        for (int index = 0; index < count; index++) {
            field2295[index] = buffer.readInt();
        }

        animationDirection = buffer.readUnsignedByte();
        animationSpeed = buffer.readUnsignedByte();

        this.pixels = null;

        for (final int spriteGroupId : this.fileIds) {
            final IndexedImage[] indexedImages = IndexedImage.indexedImages.get(spriteGroupId);
            if (indexedImages == null) {
                client.onDemandFetcher.loadData(4, spriteGroupId, false);
            }
        }
    }

    boolean load(double brightness, int textureSize, Client client) {
        boolean ready = true;
        for (final int spriteGroupId : this.fileIds) {
            final IndexedImage[] indexedImages = IndexedImage.indexedImages.get(spriteGroupId);
            if (indexedImages == null) {
                client.onDemandFetcher.loadData(4, spriteGroupId, false);
                ready = false;
            }
        }
        if (!ready) {
            client.flushFileClient();
            return false;
        }

        int pixelCount = textureSize * textureSize;
        this.pixels = new int[pixelCount];

        for (int i = 0; i < this.fileIds.length; ++i) {
            final int spriteGroupId = fileIds[i];
            final IndexedImage[] indexedImages = IndexedImage.indexedImages.get(spriteGroupId);
            final IndexedImage indexedImage = indexedImages[0];
            final int[] indexedImagePalette = indexedImage.palette;

            final byte[] palettePixels = indexedImage.palettePixels;
            final int[] palette = new int[indexedImagePalette.length];
            System.arraycopy(indexedImagePalette, 0, palette, 0, palette.length);

            final int var10 = this.field2295[i];
           
            int var11;
            int var12;
            int var13;
            int var14;
            if ((var10 & -16777216) == 50331648) {
                var11 = var10 & 16711935;
                var12 = var10 >> 8 & 255;

                for (var13 = 0; var13 < palette.length; ++var13) {
                    var14 = palette[var13];
                    if (var14 >> 8 == (var14 & 65535)) {
                        var14 &= 255;
                        palette[var13] = var11 * var14 >> 8 & 16711935 | var12 * var14 & 65280;
                    }
                }
            }

            for (var11 = 0; var11 < palette.length; ++var11) {
                palette[var11] = Rasterizer3D.adjustBrightness(palette[var11], brightness);
            }

            if (i == 0) {
                var11 = 0;
            } else {
                var11 = this.field2301[i - 1];
            }

            if (var11 == 0) {
                if (textureSize == indexedImage.subWidth) {
                    for (var12 = 0; var12 < pixelCount; ++var12) {
                        this.pixels[var12] = palette[palettePixels[var12] & 255];
                    }
                } else if (indexedImage.subWidth == 64 && textureSize == 128) {
                    var12 = 0;

                    for (var13 = 0; var13 < textureSize; ++var13) {
                        for (var14 = 0; var14 < textureSize; ++var14) {
                            this.pixels[var12++] = palette[palettePixels[(var13 >> 1 << 6) + (var14 >> 1)] & 255];
                        }
                    }
                } else {
                    if (indexedImage.subWidth != 128 || textureSize != 64) {
                        throw new RuntimeException();
                    }

                    var12 = 0;

                    for (var13 = 0; var13 < textureSize; ++var13) {
                        for (var14 = 0; var14 < textureSize; ++var14) {
                            this.pixels[var12++] = palette[palettePixels[(var14 << 1) + (var13 << 1 << 7)] & 255];
                        }
                    }
                }
            }

        }

        return true;
    }

    void reset() {
        this.pixels = null;
    }

    void animate(int var1) {
        if (Client.instance.isGpu()) {
            Client.instance.getDrawCallbacks().animate(this, var1);
            return;
        }

        if (this.pixels != null) {
            short var2;
            int var3;
            int var4;
            int var5;
            int var6;
            int var7;
            int[] var10;
            if (this.animationDirection == 1 || this.animationDirection == 3) {
                if (animatedPixels == null || animatedPixels.length < this.pixels.length) {
                    animatedPixels = new int[this.pixels.length];
                }

                if (this.pixels.length == 4096) {
                    var2 = 64;
                } else {
                    var2 = 128;
                }

                var3 = this.pixels.length;
                var4 = var2 * this.animationSpeed * var1;
                var5 = var3 - 1;
                if (this.animationDirection == 1) {
                    var4 = -var4;
                }

                for (var6 = 0; var6 < var3; ++var6) {
                    var7 = var6 + var4 & var5;
                    animatedPixels[var6] = this.pixels[var7];
                }

                var10 = this.pixels;
                this.pixels = animatedPixels;
                animatedPixels = var10;
            }

            if (this.animationDirection == 2 || this.animationDirection == 4) {
                if (animatedPixels == null || animatedPixels.length < this.pixels.length) {
                    animatedPixels = new int[this.pixels.length];
                }

                if (this.pixels.length == 4096) {
                    var2 = 64;
                } else {
                    var2 = 128;
                }

                var3 = this.pixels.length;
                var4 = this.animationSpeed * var1;
                var5 = var2 - 1;
                if (this.animationDirection == 2) {
                    var4 = -var4;
                }

                for (var6 = 0; var6 < var3; var6 += var2) {
                    for (var7 = 0; var7 < var2; ++var7) {
                        int var8 = var6 + var7;
                        int var9 = var6 + (var7 + var4 & var5);
                        animatedPixels[var8] = this.pixels[var9];
                    }
                }

                var10 = this.pixels;
                this.pixels = animatedPixels;
                animatedPixels = var10;
            }

        }
    }



    private float textureU;
    private float textureV;

    public float getU() {
        return textureU;
    }
    public void setU(float u) {
        textureU = u;
    }

    public float getV() {
        return textureV;
    }

    public void setV(float v) {
        textureV = v;
    }

    public int[] getPixels() {
        return pixels;
    }

    public int getAnimationSpeed() {
        return animationSpeed;
    }

    @Override
    public boolean isLoaded() {
        return isLoaded;
    }

    public int getAnimationDirection() {
        return animationDirection;
    }

}
