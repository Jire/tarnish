package com.osroyale;

import java.awt.*;

public class Rasterizer2D extends Cacheable {

	public static int pixels[];
	public static int width;
	public static int height;
	public static int topY;
	public static int bottomY;
	public static int leftX;
	public static int bottomX;
	public static int lastX;
	public static int viewportCenterX;
	public static int viewportCenterY;

	public static void initDrawingArea(int[] pixels, int w, int h) {
		Rasterizer2D.pixels = pixels;
		width = w;
		height = h;
		setDrawingArea(0, 0, w, h);
	}

	public static void defaultDrawingAreaSize() {
		leftX = 0;
		topY = 0;
		bottomX = width;
		bottomY = height;
		lastX = bottomX;
		viewportCenterX = bottomX / 2;
	}

	public static void setDrawingArea1(int bottomY, int _topX, int rightX, int _topY) {
		if (_topX < 0)
			_topX = 0;
		if (_topY < 0)
			_topY = 0;
		if (rightX > width)
			rightX = width;
		if (bottomY > height)
			bottomY = height;
		leftX = _topX;
		topY = _topY;
		bottomX = rightX;
		Rasterizer2D.bottomY = bottomY;
		lastX = bottomX;
		viewportCenterX = bottomX / 2;
		viewportCenterY = Rasterizer2D.bottomY / 2;
	}

	public static void setDrawingArea(int x1, int y1, int x2, int y2) {
		if (x1 < 0) {
			x1 = 0;
		}
		if (y1 < 0) {
			y1 = 0;
		}
		if (x2 > width) {
			x2 = width;
		}
		if (y2 > height) {
			y2 = height;
		}
		leftX = x1;
		topY = y1;
		bottomX = x2;
		bottomY = y2;
		lastX = bottomX;
		viewportCenterX = bottomX / 2;
		viewportCenterY = bottomY / 2;
	}

	public static void setDrawingArea2(int bottomY, int leftX, int rightX, int topY) {
		if (leftX < 0) {
			leftX = 0;
		}
		if (topY < 0) {
			topY = 0;
		}
		if (rightX > width) {
			rightX = width;
		}
		if (bottomY > height) {
			bottomY = height;
		}
		Rasterizer2D.leftX = leftX;
		Rasterizer2D.topY = topY;
		bottomX = rightX;
		Rasterizer2D.bottomY = bottomY;
		lastX = bottomX;
		viewportCenterX = bottomX / 2;
		viewportCenterY = Rasterizer2D.bottomY / 2;
	}

	public static void reset() {
		int canvas = width * height;
		for (int j = 0; j < canvas; j++) {
			pixels[j] = 0;
		}
	}

	public static void reset(Color color) {
		int canvas = width * height;
		for (int j = 0; j < canvas; j++) {
			pixels[j] = color.getRGB();
		}
	}

	public static void drawAlpha(int[] pixels, int index, int value, int alpha) {
		if (!Client.instance.isGpu() || pixels != Client.instance.getBufferProvider().getPixels()) {
			pixels[index] = value;
			return;
		}

		int outAlpha = alpha + ((pixels[index] >>> 24) * (255 - alpha) * 0x8081 >>> 23);
		pixels[index] = value & 0x00FFFFFF | outAlpha << 24;
	}

	public static void fillCircle(int x, int y, int radius, int color) {
		int y1 = y - radius;
		if (y1 < 0) {
			y1 = 0;
		}
		int y2 = y + radius;
		if (y2 >= height) {
			y2 = height - 1;
		}
		for (int iy = y1; iy <= y2; iy++) {
			int dy = iy - y;
			int dist = (int) Math.sqrt(radius * radius - dy * dy);
			int x1 = x - dist;
			if (x1 < 0) {
				x1 = 0;
			}
			int x2 = x + dist;
			if (x2 >= width) {
				x2 = width - 1;
			}
			int pos = x1 + iy * width;
			for (int ix = x1; ix <= x2; ix++) {
				pixels[pos++] = color;
				drawAlpha(pixels, pos++,color,255);
			}
		}
	}

	public static void fillCircle(int x, int y, int radius, int color, int alpha) {
		int y1 = y - radius;
		if (y1 < 0) {
			y1 = 0;
		}
		int y2 = y + radius;
		if (y2 >= height) {
			y2 = height - 1;
		}
		int a2 = 256 - alpha;
		int r1 = (color >> 16 & 0xff) * alpha;
		int g1 = (color >> 8 & 0xff) * alpha;
		int b1 = (color & 0xff) * alpha;
		for (int iy = y1; iy <= y2; iy++) {
			int dy = iy - y;
			int dist = (int) Math.sqrt(radius * radius - dy * dy);
			int x1 = x - dist;
			if (x1 < 0) {
				x1 = 0;
			}
			int x2 = x + dist;
			if (x2 >= width) {
				x2 = width - 1;
			}
			int pos = x1 + iy * width;
			for (int ix = x1; ix <= x2; ix++) {
				int r2 = (pixels[pos] >> 16 & 0xff) * a2;
				int g2 = (pixels[pos] >> 8 & 0xff) * a2;
				int b2 = (pixels[pos] & 0xff) * a2;
				drawAlpha(pixels, pos++,((r1 + r2 >> 8) << 16) + ((g1 + g2 >> 8) << 8) + (b1 + b2 >> 8),alpha);
			}
		}
	}

	public static void drawAlphaGradient(int x, int y, int gradientWidth, int gradientHeight, int startColor, int endColor, int alpha) {
		int k1 = 0;
		int l1 = 0x10000 / gradientHeight;
		if (x < leftX) {
			gradientWidth -= leftX - x;
			x = leftX;
		}
		if (y < topY) {
			k1 += (topY - y) * l1;
			gradientHeight -= topY - y;
			y = topY;
		}
		if (x + gradientWidth > bottomX)
			gradientWidth = bottomX - x;
		if (y + gradientHeight > bottomY)
			gradientHeight = bottomY - y;
		int i2 = width - gradientWidth;
		int result_alpha = 256 - alpha;
		int total_pixels = x + y * width;
		for (int k2 = -gradientHeight; k2 < 0; k2++) {
			int gradient1 = 0x10000 - k1 >> 8;
			int gradient2 = k1 >> 8;
			int gradient_color = ((startColor & 0xff00ff) * gradient1 + (endColor & 0xff00ff) * gradient2 & 0xff00ff00) + ((startColor & 0xff00) * gradient1 + (endColor & 0xff00) * gradient2 & 0xff0000) >>> 8;
			int color = ((gradient_color & 0xff00ff) * alpha >> 8 & 0xff00ff) + ((gradient_color & 0xff00) * alpha >> 8 & 0xff00);
			for (int k3 = -gradientWidth; k3 < 0; k3++) {
				int colored_pixel = pixels[total_pixels];
				colored_pixel = ((colored_pixel & 0xff00ff) * result_alpha >> 8 & 0xff00ff) + ((colored_pixel & 0xff00) * result_alpha >> 8 & 0xff00);
				drawAlpha(pixels, total_pixels++,color + colored_pixel, alpha);
			}
			total_pixels += i2;
			k1 += l1;
		}
	}

	public static void fillRectangle(int x, int y, int w, int h, int color) {
		if (x < leftX) {
			w -= leftX - x;
			x = leftX;
		}
		if (y < topY) {
			h -= topY - y;
			y = topY;
		}
		if (x + w > bottomX) {
			w = bottomX - x;
		}
		if (y + h > bottomY) {
			h = bottomY - y;
		}
		int k1 = width - w;
		int l1 = x + y * width;
		for (int i2 = -h; i2 < 0; i2++) {
			for (int j2 = -w; j2 < 0; j2++) {
				drawAlpha(pixels, l1++,color,255);
			}
			l1 += k1;
		}
	}

	public static void fillRectangle(int x, int y, int w, int h, int color, int alpha) {
		if (x < leftX) {
			w -= leftX - x;
			x = leftX;
		}
		if (y < topY) {
			h -= topY - y;
			y = topY;
		}
		if (x + w > bottomX) {
			w = bottomX - x;
		}
		if (y + h > bottomY) {
			h = bottomY - y;
		}
		int a2 = 256 - alpha;
		int r1 = (color >> 16 & 0xff) * alpha;
		int g1 = (color >> 8 & 0xff) * alpha;
		int b1 = (color & 0xff) * alpha;
		int k3 = width - w;
		int pixel = x + y * width;
		for (int i4 = 0; i4 < h; i4++) {
			for (int j4 = -w; j4 < 0; j4++) {
				int r2 = (pixels[pixel] >> 16 & 0xff) * a2;
				int g2 = (pixels[pixel] >> 8 & 0xff) * a2;
				int b2 = (pixels[pixel] & 0xff) * a2;
				int rgb = ((r1 + r2 >> 8) << 16) + ((g1 + g2 >> 8) << 8) + (b1 + b2 >> 8);
				drawAlpha(pixels, pixel++,rgb,alpha);
			}
			pixel += k3;
		}
	}

	public static void drawAlphaPixels(int x, int y, int w, int h, int color, int alpha, boolean override) {
		if (x < leftX) {
			w -= leftX - x;
			x = leftX;
		}
		if (y < topY) {
			h -= topY - y;
			y = topY;
		}
		if (x + w > bottomY && !override)
			w = bottomY - x;
		if (y + h > bottomY)
			h = bottomY - y;
		int l1 = 256 - alpha;
		int i2 = (color >> 16 & 0xff) * alpha;
		int j2 = (color >> 8 & 0xff) * alpha;
		int k2 = (color & 0xff) * alpha;
		int k3 = width - w;
		int l3 = x + y * width;
		for (int i4 = 0; i4 < h; i4++) {
			for (int j4 = -w; j4 < 0; j4++) {
				int l2 = (pixels[l3] >> 16 & 0xff) * l1;
				int i3 = (pixels[l3] >> 8 & 0xff) * l1;
				int j3 = (pixels[l3] & 0xff) * l1;
				int k4 = ((i2 + l2 >> 8) << 16) + ((j2 + i3 >> 8) << 8) + (k2 + j3 >> 8);
				drawAlpha(pixels, l3++,k4,alpha);
			}

			l3 += k3;
		}
	}

	public static void drawRectangle(int x, int y, int width, int height, int color) {
		drawHorizontalLine(x, y, width, color);
		drawHorizontalLine(x, (y + height) - 1, width, color);
		drawVerticalLine(x, y, height, color);
		drawVerticalLine((x + width) - 1, y, height, color);
	}

	public static void drawTransparentBox(int leftX, int topY, int width, int height, int rgbColour, int opacity) {
		if (leftX < Rasterizer2D.leftX) {
			width -= Rasterizer2D.leftX - leftX;
			leftX = Rasterizer2D.leftX;
		}
		if (topY < Rasterizer2D.topY) {
			height -= Rasterizer2D.topY - topY;
			topY = Rasterizer2D.topY;
		}
		if (leftX + width > bottomX)
			width = bottomX - leftX;
		if (topY + height > bottomY)
			height = bottomY - topY;
		int transparency = 256 - opacity;
		int red = (rgbColour >> 16 & 0xff) * opacity;
		int green = (rgbColour >> 8 & 0xff) * opacity;
		int blue = (rgbColour & 0xff) * opacity;
		int leftOver = Rasterizer2D.width - width;
		int pixelIndex = leftX + topY * Rasterizer2D.width;
		for (int rowIndex = 0; rowIndex < height; rowIndex++) {
			for (int columnIndex = 0; columnIndex < width; columnIndex++) {
				int otherRed = (pixels[pixelIndex] >> 16 & 0xff) * transparency;
				int otherGreen = (pixels[pixelIndex] >> 8 & 0xff) * transparency;
				int otherBlue = (pixels[pixelIndex] & 0xff) * transparency;
				int transparentColour = ((red + otherRed >> 8) << 16) + ((green + otherGreen >> 8) << 8) + (blue + otherBlue >> 8);
				drawAlpha(pixels, pixelIndex++, transparentColour, opacity);
			}
			pixelIndex += leftOver;
		}
	}

	public static void drawRectangle(int x, int y, int width, int height, int color, int alpha) {
		drawHorizontalLine(x, y, width, color, alpha);
		drawHorizontalLine(x, (y + height) - 1, width, color, alpha);
		if (height >= 3) {
			drawVerticalLine(x, y + 1, height - 2, color, alpha);
			drawVerticalLine((x + width) - 1, y + 1, height - 2, color, alpha);
		}
	}

	public static void drawHorizontalLine(int x, int y, int length, int color) {
		if (y < topY || y >= bottomY) {
			return;
		}
		if (x < leftX) {
			length -= leftX - x;
			x = leftX;
		}
		if (x + length > bottomX) {
			length = bottomX - x;
		}
		int pixel = x + y * width;
		for (int index = 0; index < length; index++) {
			drawAlpha(pixels, pixel + index, color, 255);
		}
	}

	public static void drawHorizontalLine(int x, int y, int length, int color, int alpha) {
		if (y < topY || y >= bottomY) {
			return;
		}
		if (x < leftX) {
			length -= leftX - x;
			x = leftX;
		}
		if (x + length > bottomX) {
			length = bottomX - x;
		}
		int a2 = 256 - alpha;
		int r1 = (color >> 16 & 0xff) * alpha;
		int g1 = (color >> 8 & 0xff) * alpha;
		int b1 = (color & 0xff) * alpha;
		int pixel = x + y * width;
		for (int j3 = 0; j3 < length; j3++) {
			int r2 = (pixels[pixel] >> 16 & 0xff) * a2;
			int g2 = (pixels[pixel] >> 8 & 0xff) * a2;
			int b2 = (pixels[pixel] & 0xff) * a2;
			int rgb = ((r1 + r2 >> 8) << 16) + ((g1 + g2 >> 8) << 8) + (b1 + b2 >> 8);
			drawAlpha(pixels, pixel++, rgb, alpha);
		}
	}

	public static void drawVerticalLine(int x, int y, int length, int color) {
		if (x < leftX || x >= bottomX) {
			return;
		}
		if (y < topY) {
			length -= topY - y;
			y = topY;
		}
		if (y + length > bottomY) {
			length = bottomY - y;
		}
		int pixel = x + y * width;
		for (int k1 = 0; k1 < length; k1++) {
			drawAlpha(pixels, pixel + k1 * width, color,255);
		}
	}

	public static void drawVerticalLine(int x, int y, int length, int color, int alpha) {
		if (x < leftX || x >= bottomX) {
			return;
		}
		if (y < topY) {
			length -= topY - y;
			y = topY;
		}
		if (y + length > bottomY) {
			length = bottomY - y;
		}
		int a2 = 256 - alpha;
		int r1 = (color >> 16 & 0xff) * alpha;
		int g1 = (color >> 8 & 0xff) * alpha;
		int b1 = (color & 0xff) * alpha;
		int pixel = x + y * width;
		for (int j3 = 0; j3 < length; j3++) {
			int r2 = (pixels[pixel] >> 16 & 0xff) * a2;
			int g2 = (pixels[pixel] >> 8 & 0xff) * a2;
			int b2 = (pixels[pixel] & 0xff) * a2;
			int rgb = ((r1 + r2 >> 8) << 16) + ((g1 + g2 >> 8) << 8) + (b1 + b2 >> 8);
			drawAlpha(pixels, pixel, rgb,alpha);
			pixel += width;
		}
	}

	public static void drawRoundedRectangle(int x, int y, int width, int height, int color, int alpha, boolean filled, boolean shadowed) {
		if (shadowed) {
			drawRoundedRectangle(x + 1, y + 1, width, height, 0, alpha, filled, false);
		}
		if (alpha == -1) {
			if (filled) {
				method339(y + 1, color, width - 4, x + 2);
				method339(y + height - 2, color, width - 4, x + 2);
				drawPixels(height - 4, y + 2, x + 1, color, width - 2);
			}
			method339(y, color, width - 4, x + 2);
			method339(y + height - 1, color, width - 4, x + 2);
			method341(y + 2, color, height - 4, x);
			method341(y + 2, color, height - 4, x + width - 1);
			drawPixels(1, y + 1, x + 1, color, 1);
			drawPixels(1, y + 1, x + width - 2, color, 1);
			drawPixels(1, y + height - 2, x + width - 2, color, 1);
			drawPixels(1, y + height - 2, x + 1, color, 1);
		} else if (alpha != -1) {
			if (filled) {
				method340(color, width - 4, y + 1, alpha, x + 2);
				method340(color, width - 4, y + height - 2, alpha, x + 2);
				method335(color, y + 2, width - 2, height - 4, alpha, x + 1);
			}
			method340(color, width - 4, y, alpha, x + 2);
			method340(color, width - 4, y + height - 1, alpha, x + 2);
			method342(color, x, alpha, y + 2, height - 4);
			method342(color, x + width - 1, alpha, y + 2, height - 4);
			method335(color, y + 1, 1, 1, alpha, x + 1);
			method335(color, y + 1, 1, 1, alpha, x + width - 2);
			method335(color, y + height - 2, 1, 1, alpha, x + 1);
			method335(color, y + height - 2, 1, 1, alpha, x + width - 2);
		}
	}

	public static void drawPixels(int i, int j, int k, int l, int i1) {
		if (k < leftX) {
			i1 -= leftX - k;
			k = leftX;
		}
		if (j < topY) {
			i -= topY - j;
			j = topY;
		}
		if (k + i1 > bottomX)
			i1 = bottomX - k;
		if (j + i > bottomY)
			i = bottomY - j;
		int k1 = width - i1;
		int l1 = k + j * width;
		for (int i2 = -i; i2 < 0; i2++) {
			for (int j2 = -i1; j2 < 0; j2++)
			drawAlpha(pixels, l1++, l,255);
			l1 += k1;
		}

	}

	public static void method335(int i, int j, int k, int l, int i1, int k1) {
		if (k1 < leftX) {
			k -= leftX - k1;
			k1 = leftX;
		}
		if (j < topY) {
			l -= topY - j;
			j = topY;
		}
		if (k1 + k > bottomX)
			k = bottomX - k1;
		if (j + l > bottomY)
			l = bottomY - j;
		int l1 = 256 - i1;
		int i2 = (i >> 16 & 0xff) * i1;
		int j2 = (i >> 8 & 0xff) * i1;
		int k2 = (i & 0xff) * i1;
		int k3 = width - k;
		int l3 = k1 + j * width;
		for (int i4 = 0; i4 < l; i4++) {
			for (int j4 = -k; j4 < 0; j4++) {
				int l2 = (pixels[l3] >> 16 & 0xff) * l1;
				int i3 = (pixels[l3] >> 8 & 0xff) * l1;
				int j3 = (pixels[l3] & 0xff) * l1;
				int k4 = ((i2 + l2 >> 8) << 16) + ((j2 + i3 >> 8) << 8) + (k2 + j3 >> 8);
				drawAlpha(pixels, l3++, k4,i1);
			}

			l3 += k3;
		}
	}

	public static void method339(int i, int j, int k, int l) {
		if (i < topY || i >= bottomY)
			return;
		if (l < leftX) {
			k -= leftX - l;
			l = leftX;
		}
		if (l + k > bottomX)
			k = bottomX - l;
		int i1 = l + i * width;
		for (int j1 = 0; j1 < k; j1++)
			drawAlpha(pixels, i1 + j1, j,255);

	}

	private static void method340(int i, int j, int k, int l, int i1) {
		if (k < topY || k >= bottomY)
			return;
		if (i1 < leftX) {
			j -= leftX - i1;
			i1 = leftX;
		}
		if (i1 + j > bottomX)
			j = bottomX - i1;
		int j1 = 256 - l;
		int k1 = (i >> 16 & 0xff) * l;
		int l1 = (i >> 8 & 0xff) * l;
		int i2 = (i & 0xff) * l;
		int i3 = i1 + k * width;
		for (int j3 = 0; j3 < j; j3++) {
			int j2 = (pixels[i3] >> 16 & 0xff) * j1;
			int k2 = (pixels[i3] >> 8 & 0xff) * j1;
			int l2 = (pixels[i3] & 0xff) * j1;
			int k3 = ((k1 + j2 >> 8) << 16) + ((l1 + k2 >> 8) << 8) + (i2 + l2 >> 8);
			drawAlpha(pixels, i3++, k3,l);
		}

	}

	public static void method341(int i, int j, int k, int l) {
		if (l < leftX || l >= bottomX)
			return;
		if (i < topY) {
			k -= topY - i;
			i = topY;
		}
		if (i + k > bottomY)
			k = bottomY - i;
		int j1 = l + i * width;
		for (int k1 = 0; k1 < k; k1++)
		drawAlpha(pixels, j1 + k1 * width, k,255);

	}

	private static void method342(int i, int j, int k, int l, int i1) {
		if (j < leftX || j >= bottomX)
			return;
		if (l < topY) {
			i1 -= topY - l;
			l = topY;
		}
		if (l + i1 > bottomY)
			i1 = bottomY - l;
		int j1 = 256 - k;
		int k1 = (i >> 16 & 0xff) * k;
		int l1 = (i >> 8 & 0xff) * k;
		int i2 = (i & 0xff) * k;
		int i3 = j + l * width;
		for (int j3 = 0; j3 < i1; j3++) {
			int j2 = (pixels[i3] >> 16 & 0xff) * j1;
			int k2 = (pixels[i3] >> 8 & 0xff) * j1;
			int l2 = (pixels[i3] & 0xff) * j1;
			int k3 = ((k1 + j2 >> 8) << 16) + ((l1 + k2 >> 8) << 8) + (i2 + l2 >> 8);
			drawAlpha(pixels, i3,k3,k);
			i3 += width;
		}
	}

	public static void drawTransparentGradientBox(int leftX, int topY, int width, int height, int topColour, int bottomColour, int opacity) {
		int gradientProgress = 0;
		int progressPerPixel = 0x10000 / height;
		if(leftX < Rasterizer2D.leftX) {
			width -= Rasterizer2D.leftX - leftX;
			leftX = Rasterizer2D.leftX;
		}
		if(topY < Rasterizer2D.topY) {
			gradientProgress += (Rasterizer2D.topY - topY) * progressPerPixel;
			height -= Rasterizer2D.topY - topY;
			topY = Rasterizer2D.topY;
		}
		if(leftX + width > bottomX)
			width = bottomX - leftX;
		if(topY + height > bottomY)
			height = bottomY - topY;
		int leftOver = Rasterizer2D.width - width;
		int transparency = 256 - opacity;
		int pixelIndex = leftX + topY * Rasterizer2D.width;
		for(int rowIndex = 0; rowIndex < height; rowIndex++) {
			int gradient = 0x10000 - gradientProgress >> 8;
			int inverseGradient = gradientProgress >> 8;
			int gradientColour = ((topColour & 0xff00ff) * gradient + (bottomColour & 0xff00ff) * inverseGradient & 0xff00ff00) + ((topColour & 0xff00) * gradient + (bottomColour & 0xff00) * inverseGradient & 0xff0000) >>> 8;
			int transparentPixel = ((gradientColour & 0xff00ff) * opacity >> 8 & 0xff00ff) + ((gradientColour & 0xff00) * opacity >> 8 & 0xff00);
			for(int columnIndex = 0; columnIndex < width; columnIndex++) {
				int backgroundPixel = pixels[pixelIndex];
				backgroundPixel = ((backgroundPixel & 0xff00ff) * transparency >> 8 & 0xff00ff) + ((backgroundPixel & 0xff00) * transparency >> 8 & 0xff00);
				drawAlpha(pixels, pixelIndex++, transparentPixel + backgroundPixel, opacity);
			}
			pixelIndex += leftOver;
			gradientProgress += progressPerPixel;
		}
	}

	public static void drawBubble(int x, int y, int radius, int colour, int initialAlpha) {
		fillCircleAlpha(x, y, radius, colour, initialAlpha);
		fillCircleAlpha(x, y, radius + 2, colour, 8);
		fillCircleAlpha(x, y, radius + 4, colour, 6);
		fillCircleAlpha(x, y, radius + 6, colour, 4);
		fillCircleAlpha(x, y, radius + 8, colour, 2);
	}

	public static void fillCircleAlpha(int posX, int posY, int radius, int colour, int alpha) {
		int dest_intensity = 256 - alpha;
		int src_red = (colour >> 16 & 0xff) * alpha;
		int src_green = (colour >> 8 & 0xff) * alpha;
		int src_blue = (colour & 0xff) * alpha;
		int i3 = posY - radius;
		if (i3 < 0)
			i3 = 0;
		int j3 = posY + radius;
		if (j3 >= height)
			j3 = height - 1;
		for (int y = i3; y <= j3; y++) {
			int l3 = y - posY;
			int i4 = (int) Math.sqrt(radius * radius - l3 * l3);
			int x = posX - i4;
			if (x < 0)
				x = 0;
			int k4 = posX + i4;
			if (k4 >= width)
				k4 = width - 1;
			int pixel_offset = x + y * width;
			for (int i5 = x; i5 <= k4; i5++) {
				int dest_red = (pixels[pixel_offset] >> 16 & 0xff) * dest_intensity;
				int dest_green = (pixels[pixel_offset] >> 8 & 0xff) * dest_intensity;
				int dest_blue = (pixels[pixel_offset] & 0xff) * dest_intensity;
				int result_rgb = ((src_red + dest_red >> 8) << 16) + ((src_green + dest_green >> 8) << 8) + (src_blue + dest_blue >> 8);
				pixels[pixel_offset++] = result_rgb;
				drawAlpha(pixels, pixel_offset++,result_rgb,alpha);

			}
		}
	}


}