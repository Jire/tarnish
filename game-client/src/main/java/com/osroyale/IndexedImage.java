package com.osroyale;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.runelite.rs.api.RSIndexedSprite;

public final class IndexedImage extends Rasterizer2D implements RSIndexedSprite {

	public static final int FLAG_VERTICAL = 0b01;
	public static final int FLAG_ALPHA = 0b10;

	public static final Int2ObjectMap<IndexedImage[]> indexedImages = new Int2ObjectOpenHashMap<>();

	public static void load(final int groupId, final Buffer buffer) {
		final IndexedImage[] array = loadArray(groupId, buffer);
		indexedImages.put(groupId, array);
	}

	public static IndexedImage[] loadArray(final int groupId, final Buffer buffer) {
		buffer.position = buffer.array.length - 2;

		final int spriteCount = buffer.readUnsignedShort();
		final IndexedImage[] sprites = new IndexedImage[spriteCount];

		// 2 for size
		// 5 for width, height, palette length
		// + 8 bytes per sprite for offset x/y, width, and height
		buffer.position = buffer.array.length - 7 - spriteCount * 8;

		// max width and height
		final int width = buffer.readUnsignedShort();
		final int height = buffer.readUnsignedShort();

		final int paletteLength = buffer.readUnsignedByte() + 1;

		for (int i = 0; i < spriteCount; i++) {
			final IndexedImage sprite = new IndexedImage();
			sprite.id = groupId;
			//sprite.setFrame(i)
			sprite.width = width; // maxWidth
			sprite.height = height; // maxHeight
			sprites[i] = sprite;
		}

		for (int i = 0; i < spriteCount; i++) {
			sprites[i].xOffset = buffer.readUnsignedShort();
		}

		for (int i = 0; i < spriteCount; i++) {
			sprites[i].yOffset = buffer.readUnsignedShort();
		}

		for (int i = 0; i < spriteCount; i++) {
			sprites[i].subWidth = buffer.readUnsignedShort();
		}
		for (int i = 0; i < spriteCount; i++) {
			sprites[i].subHeight = buffer.readUnsignedShort();
		}

		// same as above + 3 bytes for each palette entry, except for the first one (which is transparent)
		buffer.position = buffer.array.length - 7 - spriteCount * 8 - (paletteLength - 1) * 3;

		final int[] palette = new int[paletteLength];
		for (int i = 1; i < paletteLength; i++) {
			final int rgb = buffer.read24BitInt();
			palette[i] = rgb == 0 ? 1 : rgb;
		}

		buffer.position = 0;

		for (int i = 0; i < spriteCount; i++) {
			final IndexedImage sprite = sprites[i];

			final int spriteWidth = sprite.subWidth;
			final int spriteHeight = sprite.subHeight;

			final int dimension = spriteWidth * spriteHeight;

			final byte[] pixelPaletteIndicies = new byte[dimension];
			final byte[] pixelAlphas = new byte[dimension];

			sprite.palettePixels = pixelPaletteIndicies;
			sprite.palette = palette;

			final int flags = buffer.readUnsignedByte();

			if ((flags & FLAG_VERTICAL) == 0) {
				// read horizontally
				for (int j = 0; j < dimension; ++j) {
					pixelPaletteIndicies[j] = buffer.readSignedByte();
				}
			} else {
				// read vertically
				for (int j = 0; j < spriteWidth; ++j) {
					for (int k = 0; k < spriteHeight; ++k) {
						pixelPaletteIndicies[spriteWidth * k + j] = buffer.readSignedByte();
					}
				}
			}

			// read alphas
			if ((flags & FLAG_ALPHA) != 0) {
				if ((flags & FLAG_VERTICAL) == 0) {
					// read horizontally
					for (int j = 0; j < dimension; ++j) {
						pixelAlphas[j] = buffer.readSignedByte();
					}
				} else {
					// read vertically
					for (int j = 0; j < spriteWidth; ++j) {
						for (int k = 0; k < spriteHeight; ++k) {
							pixelAlphas[spriteWidth * k + j] = buffer.readSignedByte();
						}
					}
				}
			} else {
				// everything non-zero is opaque
				for (int j = 0; j < dimension; ++j) {
					int index = pixelPaletteIndicies[j];

					if (index != 0)
						pixelAlphas[j] = (byte) 0xFF;
				}
			}

			int[] pixels = new int[dimension];

			// build argb pixels from palette/alphas
			for (int j = 0; j < dimension; ++j) {
				int index = pixelPaletteIndicies[j] & 0xFF;

				pixels[j] = palette[index] | (pixelAlphas[j] << 24);
			}

			//sprite.pixels = pixels;

			sprite.setTransparency(255, 0, 255);
			sprite.normalize();
		}

		return sprites;
	}

	public IndexedImage() {
	}

	public IndexedImage(StreamLoader streamLoader, String s, int i) {
		Buffer stream = new Buffer(streamLoader.getFile(s + ".dat"));
		Buffer stream_1 = new Buffer(streamLoader.getFile("index.dat"));
		stream_1.position = stream.readUnsignedShort();
		width = stream_1.readUnsignedShort();
		height = stream_1.readUnsignedShort();
		int j = stream_1.readUnsignedByte();
		palette = new int[j];
		for (int k = 0; k < j - 1; k++)
			palette[k + 1] = stream_1.readUnsignedTriByte();

		for (int l = 0; l < i; l++) {
			stream_1.position += 2;
			stream.position += stream_1.readUnsignedShort() * stream_1.readUnsignedShort();
			stream_1.position++;
		}

		xOffset = stream_1.readUnsignedByte();
		yOffset = stream_1.readUnsignedByte();
		subWidth = stream_1.readUnsignedShort();
		subHeight = stream_1.readUnsignedShort();
		int i1 = stream_1.readUnsignedByte();
		int j1 = subWidth * subHeight;
		palettePixels = new byte[j1];
		if (i1 == 0) {
			for (int k1 = 0; k1 < j1; k1++)
				palettePixels[k1] = stream.readSignedByte();

			return;
		}
		if (i1 == 1) {
			for (int l1 = 0; l1 < subWidth; l1++) {
				for (int i2 = 0; i2 < subHeight; i2++)
					palettePixels[l1 + i2 * subWidth] = stream.readSignedByte();

			}

		}
	}

	public void setTransparency(int transRed, int transGreen, int transBlue) {
		for (int index = 0; index < palette.length; index++)
			if (((palette[index] >> 16) & 255) == transRed && ((palette[index] >> 8) & 255) == transGreen && (palette[index] & 255) == transBlue)
				palette[index] = 0;
	}

	public void method356() {
		width /= 2;
		height /= 2;
		byte abyte0[] = new byte[width * height];
		int i = 0;
		for (int j = 0; j < subHeight; j++) {
			for (int k = 0; k < subWidth; k++)
				abyte0[(k + xOffset >> 1) + (j + yOffset >> 1) * width] = palettePixels[i++];

		}

		palettePixels = abyte0;
		subWidth = width;
		subHeight = height;
		xOffset = 0;
		yOffset = 0;
	}

	public void method357() {
		if (subWidth == width && subHeight == height)
			return;
		byte abyte0[] = new byte[width * height];
		int i = 0;
		for (int j = 0; j < subHeight; j++) {
			for (int k = 0; k < subWidth; k++)
				abyte0[k + xOffset + (j + yOffset) * width] = palettePixels[i++];

		}

		palettePixels = abyte0;
		subWidth = width;
		subHeight = height;
		xOffset = 0;
		yOffset = 0;
	}

	public void method358() {
		byte abyte0[] = new byte[subWidth * subHeight];
		int j = 0;
		for (int k = 0; k < subHeight; k++) {
			for (int l = subWidth - 1; l >= 0; l--)
				abyte0[j++] = palettePixels[l + k * subWidth];

		}

		palettePixels = abyte0;
		xOffset = width - subWidth - xOffset;
	}

	public void method359() {
		byte abyte0[] = new byte[subWidth * subHeight];
		int i = 0;
		for (int j = subHeight - 1; j >= 0; j--) {
			for (int k = 0; k < subWidth; k++)
				abyte0[i++] = palettePixels[k + j * subWidth];

		}

		palettePixels = abyte0;
		yOffset = height - subHeight - yOffset;
	}

	public void method360(int i, int j, int k) {
		for (int i1 = 0; i1 < palette.length; i1++) {
			int j1 = palette[i1] >> 16 & 0xff;
			j1 += i;
			if (j1 < 0)
				j1 = 0;
			else if (j1 > 255)
				j1 = 255;
			int k1 = palette[i1] >> 8 & 0xff;
			k1 += j;
			if (k1 < 0)
				k1 = 0;
			else if (k1 > 255)
				k1 = 255;
			int l1 = palette[i1] & 0xff;
			l1 += k;
			if (l1 < 0)
				l1 = 0;
			else if (l1 > 255)
				l1 = 255;
			palette[i1] = (j1 << 16) + (k1 << 8) + l1;
		}
	}

	public void drawBackground(int i, int k) {
		i += xOffset;
		k += yOffset;
		int l = i + k * Rasterizer2D.width;
		int i1 = 0;
		int j1 = subHeight;
		int k1 = subWidth;
		int l1 = Rasterizer2D.width - k1;
		int i2 = 0;
		if (k < Rasterizer2D.topY) {
			int j2 = Rasterizer2D.topY - k;
			j1 -= j2;
			k = Rasterizer2D.topY;
			i1 += j2 * k1;
			l += j2 * Rasterizer2D.width;
		}
		if (k + j1 > Rasterizer2D.bottomY)
			j1 -= (k + j1) - Rasterizer2D.bottomY;
		if (i < Rasterizer2D.leftX) {
			int k2 = Rasterizer2D.leftX - i;
			k1 -= k2;
			i = Rasterizer2D.leftX;
			i1 += k2;
			l += k2;
			i2 += k2;
			l1 += k2;
		}
		if (i + k1 > Rasterizer2D.bottomX) {
			int l2 = (i + k1) - Rasterizer2D.bottomX;
			k1 -= l2;
			i2 += l2;
			l1 += l2;
		}
		if (!(k1 <= 0 || j1 <= 0)) {
			method362(j1, Rasterizer2D.pixels, palettePixels, l1, l, k1, i1, palette, i2);
		}
	}

	private void method362(int i, int ai[], byte abyte0[], int j, int k, int l, int i1, int ai1[], int j1) {
		int k1 = -(l >> 2);
		l = -(l & 3);
		for (int l1 = -i; l1 < 0; l1++) {
			for (int i2 = k1; i2 < 0; i2++) {
				byte byte1 = abyte0[i1++];
				if (byte1 != 0)
					ai[k++] = ai1[byte1 & 0xff];
				else
					k++;
				byte1 = abyte0[i1++];
				if (byte1 != 0)
					ai[k++] = ai1[byte1 & 0xff];
				else
					k++;
				byte1 = abyte0[i1++];
				if (byte1 != 0)
					ai[k++] = ai1[byte1 & 0xff];
				else
					k++;
				byte1 = abyte0[i1++];
				if (byte1 != 0)
					ai[k++] = ai1[byte1 & 0xff];
				else
					k++;
			}

			for (int j2 = l; j2 < 0; j2++) {
				byte byte2 = abyte0[i1++];
				if (byte2 != 0)
					ai[k++] = ai1[byte2 & 0xff];
				else
					k++;
			}

			k += j;
			i1 += j1;
		}

	}

	public void normalize() {
		if (subWidth != width || subHeight != height) { // L: 18
			byte[] pixels = new byte[width * height]; // L: 19
			int var2 = 0; // L: 20

			for (int var3 = 0; var3 < subHeight; ++var3) { // L: 21
				for (int var4 = 0; var4 < subWidth; ++var4) { // L: 22
					pixels[var4 + (var3 + yOffset) * width + xOffset] = palettePixels[var2++]; // L: 23
				}
			}

			palettePixels = pixels; // L: 26
			subWidth = width; // L: 27
			subHeight = height; // L: 28
			xOffset = 0; // L: 29
			yOffset = 0; // L: 30
		}
	}

	public byte palettePixels[];
	public int[] palette;
	public int subWidth;
	public int subHeight;
	public int xOffset;
	public int yOffset;
	public int width;
	private int height;

	@Override
	public byte[] getPixels() {
		return palettePixels;
	}

	@Override
	public void setPixels(byte[] pixels) {
		palettePixels = pixels;
	}

	@Override
	public int[] getPalette() {
		return pixels;
	}

	@Override
	public void setPalette(int[] palette) {
		pixels = palette;
	}

	@Override
	public int getOriginalWidth() {
		return subWidth;
	}

	@Override
	public void setOriginalWidth(int originalWidth) {
		subWidth = originalWidth;
	}

	@Override
	public int getOriginalHeight() {
		return subHeight;
	}

	@Override
	public void setOriginalHeight(int originalHeight) {
		subHeight = originalHeight;
	}

	@Override
	public int getHeight() {
		return height;
	}

	@Override
	public void setHeight(int height) {
		this.height = height;
	}

	@Override
	public int getOffsetX() {
		return xOffset;
	}

	@Override
	public void setOffsetX(int offsetX) {
		this.xOffset = offsetX;
	}

	@Override
	public int getOffsetY() {
		return yOffset;
	}

	@Override
	public void setOffsetY(int offsetY) {
		this.yOffset = offsetY;
	}

	@Override
	public int getWidth() {
		return width;
	}

	@Override
	public void setWidth(int width) {
		this.width = width;
	}

}