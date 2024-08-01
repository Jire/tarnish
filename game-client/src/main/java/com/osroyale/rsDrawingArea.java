package com.osroyale;

public class rsDrawingArea extends Rasterizer2D {

	public static int height;
	public static int bottomX;
	public static int[] anIntArray1399;
	public static int topX;
	public static int width;
	public static int[] pixels;
	public static int topY;
	public static int bottomY = 0;
	public static int[] anIntArray1405;

	public static void drawBox(int drawX, int drawY, int boxWidth, int boxHeight, int color) {
		drawHorizontalLine(drawX, color, boxWidth, drawY);
		drawHorizontalLine(drawX, color, boxWidth, drawY + boxHeight - 1);
		drawVerticalLine(drawX, drawY, boxHeight, color);
		drawVerticalLine(drawX + boxWidth - 1, drawY, boxHeight, color);
	}

	public static void setDefaultDrawingArea() {
		topX = 0;
		topY = 0;
		bottomX = width;
		bottomY = height;
		clear();
	}

	public static void method1417(int drawX, int drawY, int width, int height, int color) {
		width -= drawX;
		height -= drawY;
		if (height == 0) {
			if (width >= 0) {
				drawHorizontalLine(drawX, color, width + 1, drawY);
			} else {
				drawHorizontalLine(drawX + width, color, -width + 1, drawY);
			}
		} else if (width == 0) {
			if (height >= 0) {
				drawVerticalLine(drawX, drawY, height + 1, color);
			} else {
				drawVerticalLine(drawX, drawY + height, -height + 1, color);
			}
		} else {
			if (width + height < 0) {
				drawX += width;
				width = -width;
				drawY += height;
				height = -height;
			}
			if (width > height) {
				drawY <<= 16;
				drawY += 32768;
				height <<= 16;
				int i_8_ = (int) Math.floor((double) height / (double) width + 0.5);
				width += drawX;
				if (drawX < topX) {
					drawY += i_8_ * (topX - drawX);
					drawX = topX;
				}
				if (width >= bottomX) {
					width = bottomX - 1;
				}
				for (/**/; drawX <= width; drawX++) {
					int i_9_ = drawY >> 16;
					if (i_9_ >= topY && i_9_ < bottomY) {
						pixels[drawX + i_9_ * width] = color;
					}
					drawY += i_8_;
				}
			} else {
				drawX <<= 16;
				drawX += 32768;
				width <<= 16;
				int i_10_ = (int) Math.floor((double) width / (double) height + 0.5);
				height += drawY;
				if (drawY < topY) {
					drawX += i_10_ * (topY - drawY);
					drawY = topY;
				}
				if (height >= bottomY) {
					height = bottomY - 1;
				}
				for (/**/; drawY <= height; drawY++) {
					int i_11_ = drawX >> 16;
					if (i_11_ >= topX && i_11_ < bottomX) {
						pixels[i_11_ + drawY * width] = color;
					}
					drawX += i_10_;
				}
			}
		}
	}

	public static void clearDrawingArea() {
		int i = 0;
		int i_12_ = width * height - 7;
		while (i < i_12_) {
			pixels[i++] = 0;
			pixels[i++] = 0;
			pixels[i++] = 0;
			pixels[i++] = 0;
			pixels[i++] = 0;
			pixels[i++] = 0;
			pixels[i++] = 0;
			pixels[i++] = 0;
		}
		i_12_ += 7;
		while (i < i_12_) {
			pixels[i++] = 0;
		}
	}

	public static void method1419(int drawX, int drawY, int color, int[] is, int[] is_15_) {
		int curPixel = drawX + drawY * width;
		for (drawY = 0; drawY < is.length; drawY++) {
			int i_17_ = curPixel + is[drawY];
			for (drawX = -is_15_[drawY]; drawX < 0; drawX++) {
				pixels[i_17_++] = color;
			}
			curPixel += width;
		}
	}

	public static void nullLoader() {
		pixels = null;
		anIntArray1399 = null;
		anIntArray1405 = null;
	}

	public static void drawVerticalLineAlpha(int drawX, int drawY, int lineHeightI, int color, int alpha) {
		if (drawX >= topX && drawX < bottomX) {
			if (drawY < topY) {
				lineHeightI -= topY - drawY;
				drawY = topY;
			}
			if (drawY + lineHeightI > bottomY) {
				lineHeightI = bottomY - drawY;
			}
			int realAlpha = 256 - alpha;
			int tempRed = (color >> 16 & 0xff) * alpha;
			int tempGreen = (color >> 8 & 0xff) * alpha;
			int tempblue = (color & 0xff) * alpha;
			int currentPixel = drawX + drawY * width;
			for (int count = 0; count < lineHeightI; count++) {
				int finalRed = (pixels[currentPixel] >> 16 & 0xff) * realAlpha;
				int finalGreen = (pixels[currentPixel] >> 8 & 0xff) * realAlpha;
				int finalBlue = (pixels[currentPixel] & 0xff) * realAlpha;
				int pixelColor = ((tempRed + finalRed >> 8 << 16) + (tempGreen + finalGreen >> 8 << 8) + (tempblue + finalBlue >> 8));
				pixels[currentPixel] = pixelColor;
				currentPixel += width;
			}
		}
	}

	public static void setDrawingArea(int[] is) {
		topX = is[0];
		topY = is[1];
		bottomX = is[2];
		bottomY = is[3];
		clear();
	}

	public static void startSetAreaColor(int drawX, int drawY, int color) {
		if (drawX >= topX && drawY >= topY && drawX < bottomX && drawY < bottomY) {
			pixels[drawX + drawY * width] = color;
		}
	}

	public static void method1424(int[] is, int[] is_34_) {
		if (is.length != bottomY - topY || is_34_.length != bottomY - topY) {
			throw new IllegalArgumentException();
		}
		anIntArray1399 = is;
		anIntArray1405 = is_34_;
	}

	public static void method1425(int i, int i_35_, int i_36_, int i_37_) {
		if (i_36_ == 0) {
			startSetAreaColor(i, i_35_, i_37_);
		} else {
			if (i_36_ < 0) {
				i_36_ = -i_36_;
			}
			int i_38_ = i_35_ - i_36_;
			if (i_38_ < topY) {
				i_38_ = topY;
			}
			int i_39_ = i_35_ + i_36_ + 1;
			if (i_39_ > bottomY) {
				i_39_ = bottomY;
			}
			int i_40_ = i_38_;
			int i_41_ = i_36_ * i_36_;
			int i_42_ = 0;
			int i_43_ = i_35_ - i_40_;
			int i_44_ = i_43_ * i_43_;
			int i_45_ = i_44_ - i_43_;
			if (i_35_ > i_39_) {
				i_35_ = i_39_;
			}
			while (i_40_ < i_35_) {
				for (/**/; i_45_ <= i_41_ || i_44_ <= i_41_; i_45_ += i_42_++ + i_42_) {
					i_44_ += i_42_ + i_42_;
				}
				int i_46_ = i - i_42_ + 1;
				if (i_46_ < topX) {
					i_46_ = topX;
				}
				int i_47_ = i + i_42_;
				if (i_47_ > bottomX) {
					i_47_ = bottomX;
				}
				int i_48_ = i_46_ + i_40_ * width;
				for (int i_49_ = i_46_; i_49_ < i_47_; i_49_++) {
					pixels[i_48_++] = i_37_;
				}
				i_40_++;
				i_44_ -= i_43_-- + i_43_;
				i_45_ -= i_43_ + i_43_;
			}
			i_42_ = i_36_;
			i_43_ = i_40_ - i_35_;
			i_45_ = i_43_ * i_43_ + i_41_;
			i_44_ = i_45_ - i_42_;
			i_45_ -= i_43_;
			while (i_40_ < i_39_) {
				for (/**/; i_45_ > i_41_ && i_44_ > i_41_; i_44_ -= i_42_ + i_42_) {
					i_45_ -= i_42_-- + i_42_;
				}
				int i_50_ = i - i_42_;
				if (i_50_ < topX) {
					i_50_ = topX;
				}
				int i_51_ = i + i_42_;
				if (i_51_ > bottomX - 1) {
					i_51_ = bottomX - 1;
				}
				int i_52_ = i_50_ + i_40_ * width;
				for (int i_53_ = i_50_; i_53_ <= i_51_; i_53_++) {
					pixels[i_52_++] = i_37_;
				}
				i_40_++;
				i_45_ += i_43_ + i_43_;
				i_44_ += i_43_++ + i_43_;
			}
		}
	}

	public static void createDrawingArea(int startX, int startY, int areaWidthI, int areaHeight) {
		if (topX < startX) {
			topX = startX;
		}
		if (topY < startY) {
			topY = startY;
		}
		if (bottomX > areaWidthI) {
			bottomX = areaWidthI;
		}
		if (bottomY > areaHeight) {
			bottomY = areaHeight;
		}
		clear();
	}

	public static void setDrawingArea(int startX, int startYI, int areaWidth, int areaHeight) {
		if (startX < 0) {
			startX = 0;
		}
		if (startYI < 0) {
			startYI = 0;
		}
		if (areaWidth > width) {
			areaWidth = width;
		}
		if (areaHeight > height) {
			areaHeight = height;
		}
		topX = startX;
		topY = startYI;
		bottomX = areaWidth;
		bottomY = areaHeight;
		clear();
	}


	public static void drawHorizontalLineAlpha(int i, int i_89_, int i_90_, int i_91_, int i_92_) {
		if (i_89_ >= topY && i_89_ < bottomY) {
			if (i < topX) {
				i_90_ -= topX - i;
				i = topX;
			}
			if (i + i_90_ > bottomX) {
				i_90_ = bottomX - i;
			}
			int i_93_ = 256 - i_92_;
			int i_94_ = (i_91_ >> 16 & 0xff) * i_92_;
			int i_95_ = (i_91_ >> 8 & 0xff) * i_92_;
			int i_96_ = (i_91_ & 0xff) * i_92_;
			int i_97_ = i + i_89_ * width;
			for (int i_98_ = 0; i_98_ < i_90_; i_98_++) {
				int i_99_ = (pixels[i_97_] >> 16 & 0xff) * i_93_;
				int i_100_ = (pixels[i_97_] >> 8 & 0xff) * i_93_;
				int i_101_ = (pixels[i_97_] & 0xff) * i_93_;
				int i_102_ = ((i_94_ + i_99_ >> 8 << 16) + (i_95_ + i_100_ >> 8 << 8) + (i_96_ + i_101_ >> 8));
				pixels[i_97_++] = i_102_;
			}
		}
	}

	public static void drawVerticalLine(int i, int i_103_, int i_104_, int i_105_) {
		if (i >= topX && i < bottomX) {
			if (i_103_ < topY) {
				i_104_ -= topY - i_103_;
				i_103_ = topY;
			}
			if (i_103_ + i_104_ > bottomY) {
				i_104_ = bottomY - i_103_;
			}
			int i_106_ = i + i_103_ * width;
			for (int i_107_ = 0; i_107_ < i_104_; i_107_++) {
				pixels[i_106_ + i_107_ * width] = i_105_;
			}
		}
	}

	public static void initDrawingArea(int[] is, int i, int i_108_) {
		pixels = is;
		width = i;
		height = i_108_;
		setDrawingArea(0, 0, i, i_108_);
	}

	public static void drawBoxAlpha(int drawX, int drawY, int boxWidth, int boxHeight, int boxColor, int alphaI) {
		drawHorizontalLineAlpha(drawX, drawY, boxWidth, boxColor, alphaI);
		drawHorizontalLineAlpha(drawX, drawY + boxHeight - 1, boxWidth, boxColor, alphaI);
		if (boxHeight >= 3) {
			drawVerticalLineAlpha(drawX, drawY + 1, boxHeight - 2, boxColor, alphaI);
			drawVerticalLineAlpha(drawX + boxWidth - 1, drawY + 1, boxHeight - 2, boxColor, alphaI);
		}
	}

	public static void method1434(int i, int i_114_, int i_115_, int i_116_, int i_117_) {
		if (i_117_ != 0) {
			if (i_117_ == 256) {
				method1425(i, i_114_, i_115_, i_116_);
			} else {
				if (i_115_ < 0) {
					i_115_ = -i_115_;
				}
				int i_118_ = 256 - i_117_;
				int i_119_ = (i_116_ >> 16 & 0xff) * i_117_;
				int i_120_ = (i_116_ >> 8 & 0xff) * i_117_;
				int i_121_ = (i_116_ & 0xff) * i_117_;
				int i_122_ = i_114_ - i_115_;
				if (i_122_ < topY) {
					i_122_ = topY;
				}
				int i_123_ = i_114_ + i_115_ + 1;
				if (i_123_ > bottomY) {
					i_123_ = bottomY;
				}
				int i_124_ = i_122_;
				int i_125_ = i_115_ * i_115_;
				int i_126_ = 0;
				int i_127_ = i_114_ - i_124_;
				int i_128_ = i_127_ * i_127_;
				int i_129_ = i_128_ - i_127_;
				if (i_114_ > i_123_) {
					i_114_ = i_123_;
				}
				while (i_124_ < i_114_) {
					for (/**/; i_129_ <= i_125_ || i_128_ <= i_125_; i_129_ += i_126_++ + i_126_) {
						i_128_ += i_126_ + i_126_;
					}
					int i_130_ = i - i_126_ + 1;
					if (i_130_ < topX) {
						i_130_ = topX;
					}
					int i_131_ = i + i_126_;
					if (i_131_ > bottomX) {
						i_131_ = bottomX;
					}
					int i_132_ = i_130_ + i_124_ * width;
					for (int i_133_ = i_130_; i_133_ < i_131_; i_133_++) {
						int i_134_ = (pixels[i_132_] >> 16 & 0xff) * i_118_;
						int i_135_ = (pixels[i_132_] >> 8 & 0xff) * i_118_;
						int i_136_ = (pixels[i_132_] & 0xff) * i_118_;
						int i_137_ = ((i_119_ + i_134_ >> 8 << 16) + (i_120_ + i_135_ >> 8 << 8) + (i_121_ + i_136_ >> 8));
						pixels[i_132_++] = i_137_;
					}
					i_124_++;
					i_128_ -= i_127_-- + i_127_;
					i_129_ -= i_127_ + i_127_;
				}
				i_126_ = i_115_;
				i_127_ = -i_127_;
				i_129_ = i_127_ * i_127_ + i_125_;
				i_128_ = i_129_ - i_126_;
				i_129_ -= i_127_;
				while (i_124_ < i_123_) {
					for (/**/; i_129_ > i_125_ && i_128_ > i_125_; i_128_ -= i_126_ + i_126_) {
						i_129_ -= i_126_-- + i_126_;
					}
					int i_138_ = i - i_126_;
					if (i_138_ < topX) {
						i_138_ = topX;
					}
					int i_139_ = i + i_126_;
					if (i_139_ > bottomX - 1) {
						i_139_ = bottomX - 1;
					}
					int i_140_ = i_138_ + i_124_ * width;
					for (int i_141_ = i_138_; i_141_ <= i_139_; i_141_++) {
						int i_142_ = (pixels[i_140_] >> 16 & 0xff) * i_118_;
						int i_143_ = (pixels[i_140_] >> 8 & 0xff) * i_118_;
						int i_144_ = (pixels[i_140_] & 0xff) * i_118_;
						int i_145_ = ((i_119_ + i_142_ >> 8 << 16) + (i_120_ + i_143_ >> 8 << 8) + (i_121_ + i_144_ >> 8));
						pixels[i_140_++] = i_145_;
					}
					i_124_++;
					i_129_ += i_127_ + i_127_;
					i_128_ += i_127_++ + i_127_;
				}
			}
		}
	}

	public static void fillBoxAlpha(int i, int i_146_, int i_147_, int i_148_, int i_149_, int i_150_) {
		if (i < topX) {
			i_147_ -= topX - i;
			i = topX;
		}
		if (i_146_ < topY) {
			i_148_ -= topY - i_146_;
			i_146_ = topY;
		}
		if (i + i_147_ > bottomX) {
			i_147_ = bottomX - i;
		}
		if (i_146_ + i_148_ > bottomY) {
			i_148_ = bottomY - i_146_;
		}
		i_149_ = (((i_149_ & 0xff00ff) * i_150_ >> 8 & 0xff00ff) + ((i_149_ & 0xff00) * i_150_ >> 8 & 0xff00));
		int i_151_ = 256 - i_150_;
		int i_152_ = width - i_147_;
		int i_153_ = i + i_146_ * width;
		for (int i_154_ = 0; i_154_ < i_148_; i_154_++) {
			for (int i_155_ = -i_147_; i_155_ < 0; i_155_++) {
				int i_156_ = pixels[i_153_];
				i_156_ = (((i_156_ & 0xff00ff) * i_151_ >> 8 & 0xff00ff) + ((i_156_ & 0xff00) * i_151_ >> 8 & 0xff00));
				pixels[i_153_++] = i_149_ + i_156_;
			}
			i_153_ += i_152_;
		}
	}

	public static void method1436(int[] is) {
		is[0] = topX;
		is[1] = topY;
		is[2] = bottomX;
		is[3] = bottomY;
	}

	public static void method1437(int drawX, int drawY, int boxWidth, int boxHeight, int color, int alpha) {
		int i_162_ = 0;
		int i_163_ = 65536 / boxHeight;
		if (drawX < topX) {
			boxWidth -= topX - drawX;
			drawX = topX;
		}
		if (drawY < topY) {
			i_162_ += (topY - drawY) * i_163_;
			boxHeight -= topY - drawY;
			drawY = topY;
		}
		if (drawX + boxWidth > bottomX) {
			boxWidth = bottomX - drawX;
		}
		if (drawY + boxHeight > bottomY) {
			boxHeight = bottomY - drawY;
		}
		int widthOffset = width - boxWidth;
		int i_165_ = drawX + drawY * width;
		for (int i_166_ = -boxHeight; i_166_ < 0; i_166_++) {
			int i_167_ = 65536 - i_162_ >> 8;
			int i_168_ = i_162_ >> 8;
			int finalColor = (((color & 0xff00ff) * i_167_ + (alpha & 0xff00ff) * i_168_ & ~0xff00ff) + ((color & 0xff00) * i_167_ + (alpha & 0xff00) * i_168_ & 0xff0000)) >>> 8;
			for (int i_170_ = -boxWidth; i_170_ < 0; i_170_++) {
				pixels[i_165_++] = finalColor;
			}
			i_165_ += widthOffset;
			i_162_ += i_163_;
		}
	}

	public static void clear() {
		anIntArray1399 = null;
		anIntArray1405 = null;
	}

	public static void fillBox(int drawX, int drawY, int boxWidth, int boxHeight, int color) {
		if (drawX < topX) {
			boxWidth -= topX - drawX;
			drawX = topX;
		}
		if (drawY < topY) {
			boxHeight -= topY - drawY;
			drawY = topY;
		}
		if (drawX + boxWidth > bottomX) {
			boxWidth = bottomX - drawX;
		}
		if (drawY + boxHeight > bottomY) {
			boxHeight = bottomY - drawY;
		}
		int widthOffsets = width - boxWidth;
		int pixelOffset = drawX + drawY * width;
		for (int widthOffset = -boxHeight; widthOffset < 0; widthOffset++) {
			for (int heightOffset = -boxWidth; heightOffset < 0; heightOffset++) {
				pixels[pixelOffset++] = color;
			}
			pixelOffset += widthOffsets;
		}
	}

	static {
		bottomX = 0;
		topX = 0;
		topY = 0;
	}
}