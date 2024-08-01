package com.osroyale;

import java.awt.*;

public class RSFont extends Rasterizer2D {

	private boolean strikeThrough;
	public int baseCharacterHeight = 0;
	public int anInt4142;
	public int anInt4144;
	public int[] characterDrawYOffsets;
	public int[] characterHeights;
	public int[] characterDrawXOffsets;
	public int[] characterWidths;
	public int[] iconWidths;
	public byte[] aByteArray4151;
	public byte[][] fontPixels;
	public int[] characterScreenWidths;
	public static Sprite[] chatImages;
	public static Sprite[] clanImages;
	public static Sprite[] iconImages;
	public static String startImage;
	public static String startClanImage;
	public static String startIconImage;
	public static String aRSString_4135;
	public static String startTransparency;
	public static String startDefaultShadow;
	public static String endShadow = "/shad";
	public static String endEffect;
	public static String aRSString_4143;
	public static String endStrikethrough = "/str";
	public static String aRSString_4147;
	public static String startColor;
	public static String lineBreak;
	public static String startStrikethrough;
	public static String endColor;
	public static String endUnderline;
	public static String defaultStrikethrough;
	public static String startShadow;
	public static String startEffect;
	public static String aRSString_4162;
	public static String aRSString_4163;
	public static String endTransparency;
	public static String aRSString_4165;
	public static String startUnderline;
	public static String startDefaultUnderline;
	public static String aRSString_4169;
	public static String[] splitTextStrings;
	public static int defaultColor;
	public static int textShadowColor;
	public static int strikethroughColor;
	public static int defaultTransparency;
	public static int anInt4175;
	public static int underlineColor;
	public static int defaultShadow;
	public static int anInt4178;
	public static int transparency;
	public static int textColor;

	private void drawCharacter(byte charPixels[], int x, int y, int width, int height, int color) {
		int offset = x + y * Rasterizer2D.width;
		int k1 = Rasterizer2D.width - width;
		int l1 = 0;
		int pixel = 0;
		if (y < Rasterizer2D.topY) {
			int offsetY = Rasterizer2D.topY - y;
			height -= offsetY;
			y = Rasterizer2D.topY;
			pixel += offsetY * width;
			offset += offsetY * Rasterizer2D.width;
		}
		if (y + height >= Rasterizer2D.bottomY) {
			height -= ((y + height) - Rasterizer2D.bottomY);
		}
		if (x < Rasterizer2D.leftX) {
			int offsetX = Rasterizer2D.leftX - x;
			width -= offsetX;
			x = Rasterizer2D.leftX;
			pixel += offsetX;
			offset += offsetX;
			l1 += offsetX;
			k1 += offsetX;
		}
		if (x + width >= Rasterizer2D.bottomX) {
			int l2 = ((x + width) - Rasterizer2D.bottomX);
			width -= l2;
			l1 += l2;
			k1 += l2;
		}
		if (!(width <= 0 || height <= 0)) {
			createPixels(Rasterizer2D.pixels, charPixels, color, pixel, offset, width, height, k1, l1);
		}
	}

	private void createPixels(int drawingAreaPixels[], byte charPixels[], int color, int pixel, int offset, int width, int height, int unknown1, int unknown2) {
		int l1 = -(width >> 2);
		width = -(width & 3);
		for (int i2 = -height; i2 < 0; i2++) {
			for (int j2 = l1; j2 < 0; j2++) {
				if (charPixels[pixel++] != 0) {
					drawingAreaPixels[offset++] = color;
				} else {
					offset++;
				}
				if (charPixels[pixel++] != 0) {
					drawingAreaPixels[offset++] = color;
				} else {
					offset++;
				}
				if (charPixels[pixel++] != 0) {
					drawingAreaPixels[offset++] = color;
				} else {
					offset++;
				}
				if (charPixels[pixel++] != 0) {
					drawingAreaPixels[offset++] = color;
				} else {
					offset++;
				}
			}
			for (int k2 = width; k2 < 0; k2++) {
				if (charPixels[pixel++] != 0) {
					drawingAreaPixels[offset++] = color;
				} else {
					offset++;
				}
			}
			offset += unknown1;
			pixel += unknown2;
		}
	}

	public RSFont(boolean TypeFont, String s, StreamLoader archive) {
		fontPixels = new byte[256][];
		characterWidths = new int[256];
		characterHeights = new int[256];
		characterDrawXOffsets = new int[256];
		characterDrawYOffsets = new int[256];
		characterScreenWidths = new int[256];
		strikeThrough = false;
		Buffer stream = new Buffer(archive.getFile(s + ".dat"));
		Buffer stream_1 = new Buffer(archive.getFile("index.dat"));
		stream_1.position = stream.readUnsignedShort() + 4;
		int k = stream_1.readUnsignedByte();
		if (k > 0) {
			stream_1.position += 3 * (k - 1);
		}
		for (int l = 0; l < 256; l++) {
			characterDrawXOffsets[l] = stream_1.readUnsignedByte();
			characterDrawYOffsets[l] = stream_1.readUnsignedByte();
			int i1 = characterWidths[l] = stream_1.readUnsignedShort();
			int j1 = characterHeights[l] = stream_1.readUnsignedShort();
			int k1 = stream_1.readUnsignedByte();
			int l1 = i1 * j1;
			fontPixels[l] = new byte[l1];
			if (k1 == 0) {
				for (int i2 = 0; i2 < l1; i2++) {
					fontPixels[l][i2] = stream.readSignedByte();
				}

			} else if (k1 == 1) {
				for (int j2 = 0; j2 < i1; j2++) {
					for (int l2 = 0; l2 < j1; l2++) {
						fontPixels[l][j2 + l2 * i1] = stream.readSignedByte();
					}

				}

			}
			if (j1 > baseCharacterHeight && l < 128) {
				baseCharacterHeight = j1;
			}
			characterDrawXOffsets[l] = 1;
			characterScreenWidths[l] = i1 + 2;
			int k2 = 0;
			for (int i3 = j1 / 7; i3 < j1; i3++) {
				k2 += fontPixels[l][i3 * i1];
			}

			if (k2 <= j1 / 7) {
				characterScreenWidths[l]--;
				characterDrawXOffsets[l] = 0;
			}
			k2 = 0;
			for (int j3 = j1 / 7; j3 < j1; j3++) {
				k2 += fontPixels[l][(i1 - 1) + j3 * i1];
			}

			if (k2 <= j1 / 7) {
				characterScreenWidths[l]--;
			}
		}

		if (TypeFont) {
			characterScreenWidths[32] = characterScreenWidths[73];
		} else {
			characterScreenWidths[32] = characterScreenWidths[105];
		}
	}

	public void drawStringMoveY(String string, int drawX, int drawY, int color, int shadow, int randomMod, int randomMod2) {
		if (string != null) {
			setColorAndShadow(color, shadow);
			double d = 7.0 - (double) randomMod2 / 8.0;
			if (d < 0.0) {
				d = 0.0;
			}
			int[] yOffset = new int[string.length()];
			for (int index = 0; index < string.length(); index++) {
				yOffset[index] = (int) (Math.sin((double) index / 1.5 + (double) randomMod) * d);
			}
			drawBaseStringMoveXY(string, drawX - getTextWidth(string) / 2, drawY, null, yOffset);
		}
	}

	public int getCharacterWidth(int i) {
		return characterScreenWidths[i & 0xff];
	}

	public void setTrans(int shadow, int color, int trans) {
		textShadowColor = defaultShadow = shadow;
		textColor = defaultColor = color;
		transparency = defaultTransparency = trans;
	}

	public void drawCenteredString(String s, int i, int j) {
		if (s != null)
			drawBasicString(s, i - getTextWidth(s) / 2, j);
	}

	private int getColorByName(String color) {
		if (color.equals("pt1")) {
			return 0x005eff;
		}
		if (color.equals("pt2")) {
			return 0x336600;
		}
		if (color.equals("pt3")) {
			return 0xA300CC;
		}
		if (color.equals("pt4")) {
			return 0xE6E600;
		}
		if (color.equals("pt5")) {
			return 0xB80000;
		}
		if (color.equals("pt6")) {
			return 0xCC5200;
		}
		if (color.equals("pt7")) {
			return 0x3D991F;
		}
		if (color.endsWith("inf")) {
			return 0xff9933;
		}
		if (color.equals("ceo")) {
			return 0x1F9100;
		}
		if (color.equals("dev")) {
			return 0xBB2AF5;
		}
		if (color.equals("war")) {
			return 0xF752CB;
		}
		if (color.equals("369")) {
			return 0x336699;
		}
		if (color.equals("def")) {
			return 0xB8934A;
		}
		if (color.equals("red")) {
			return 0xff0000;
		}
		if (color.equals("gre")) {
			return 65280;
		}
		if (color.equals("blu")) {
			return 255;
		}
		if (color.equals("yel")) {
			return 0xffff00;
		}
		if (color.equals("cya")) {
			return 65535;
		}
		if (color.equals("mag")) {
			return 0xff00ff;
		}
		if (color.equals("whi")) {
			return 0xffffff;
		}
		if (color.equals("gry")) {
			return 0x475154;
		}
		if (color.equals("bla")) {
			return 0;
		}
		if (color.equals("lre")) {
			return 0xff9040;
		}
		if (color.equals("dre")) {
			return 0x800000;
		}
		if (color.equals("dbl")) {
			return 128;
		}
		if (color.equals("or1")) {
			return 0xffb000;
		}
		if (color.equals("or2")) {
			return 0xff7000;
		}
		if (color.equals("or3")) {
			return 0xff3000;
		}
		if (color.equals("gr1")) {
			return 0xc0ff00;
		}
		if (color.equals("gr2")) {
			return 0x80ff00;
		}
		if (color.equals("gr3")) {
			return 0x40ff00;
		}
		if (color.equals("str")) {
			strikeThrough = true;
		}
		if (color.equals("end")) {
			strikeThrough = false;
		}
		if (color.equals("mbl")) {
			return 0x359BBD;
		}
		return -1;
	}

	public void setDefaultTextEffectValues(int color, int shadow, int trans) {
		strikethroughColor = -1;
		underlineColor = -1;
		textShadowColor = defaultShadow = shadow;
		textColor = defaultColor = color;
		transparency = defaultTransparency = trans;
		anInt4178 = 0;
		anInt4175 = 0;
	}

	public static int method1014(byte[][] is, byte[][] is_27_, int[] is_28_, int[] is_29_, int[] is_30_, int i, int i_31_) {
		int i_32_ = is_28_[i];
		int i_33_ = i_32_ + is_30_[i];
		int i_34_ = is_28_[i_31_];
		int i_35_ = i_34_ + is_30_[i_31_];
		int i_36_ = i_32_;
		if (i_34_ > i_32_) {
			i_36_ = i_34_;
		}
		int i_37_ = i_33_;
		if (i_35_ < i_33_) {
			i_37_ = i_35_;
		}
		int i_38_ = is_29_[i];
		if (is_29_[i_31_] < i_38_) {
			i_38_ = is_29_[i_31_];
		}
		byte[] is_39_ = is_27_[i];
		byte[] is_40_ = is[i_31_];
		int i_41_ = i_36_ - i_32_;
		int i_42_ = i_36_ - i_34_;
		for (int i_43_ = i_36_; i_43_ < i_37_; i_43_++) {
			int i_44_ = is_39_[i_41_++] + is_40_[i_42_++];
			if (i_44_ < i_38_) {
				i_38_ = i_44_;
			}
		}
		return -i_38_;
	}

	public void drawCenteredStringMoveXY(String string, int drawX, int drawY, int color, int shadow, int randomMod) {
		if (string != null) {
			setColorAndShadow(color, shadow);
			int[] xMods = new int[string.length()];
			int[] yMods = new int[string.length()];
			for (int index = 0; index < string.length(); index++) {
				xMods[index] = (int) (Math.sin((double) index / 5.0 + (double) randomMod / 5.0) * 5.0);
				yMods[index] = (int) (Math.sin((double) index / 3.0 + (double) randomMod / 5.0) * 5.0);
			}
			drawBaseStringMoveXY(string, drawX - getTextWidth(string) / 2, drawY, xMods, yMods);
		}
	}

	public void drawCenteredStringMoveY(String class100, int drawX, int drawY, int color, int shadow, int i_54_) {
		if (class100 != null) {
			setColorAndShadow(color, shadow);
			int[] yOffset = new int[class100.length()];
			for (int index = 0; index < class100.length(); index++) {
				yOffset[index] = (int) (Math.sin((double) index / 2.0 + (double) i_54_ / 5.0) * 5.0);
			}
			drawBaseStringMoveXY(class100, drawX - getTextWidth(class100) / 2, drawY, null, yOffset);
		}
	}

	public static void unpackImages(Sprite[] chatImages, Sprite[] clanImages, Sprite[] iconImages) {
		RSFont.chatImages = chatImages;
		RSFont.clanImages = clanImages;
		RSFont.iconImages = iconImages;
	}

	public void drawBasicString(String string, int drawX, int drawY) {
		drawY -= baseCharacterHeight;
		for (int currentCharacter = 0; currentCharacter < string.length(); currentCharacter++) {
			int character = string.charAt(currentCharacter);
			if (character > 255) {
				character = 32;
			}

			if (character == '<') {
				int end = string.indexOf(">", currentCharacter);
				if (end != currentCharacter + 1) {
					if (end > -1 && end < string.length()) {
						String effect = string.substring(currentCharacter + 1, end);

						if (effect.startsWith(startImage)) {
							int idx = 0;
							for (int index = effect.length() - 1; index >= 4; index--) {
								int num = effect.charAt(index) - 48;
								int pow = 10 * (effect.length() - 1 - index);
								if (pow > 0)
									idx += num * pow;
								else
									idx += num;
							}
							if (idx >= 0 && idx < chatImages.length) {
								Sprite icon = chatImages[idx];
								icon.drawSprite(drawX, drawY - 2);
								drawX += icon.width + icon.offsetX;
							}
						} else if (effect.startsWith(startClanImage)) {
							int idx = 0;
							for (int index = effect.length() - 1; index >= 5; index--) {
								int num = effect.charAt(index) - 48;
								int pow = 10 * (effect.length() - 1 - index);
								if (pow > 0)
									idx += num * pow;
								else
									idx += num;
							}
							if (idx >= 0 && idx < clanImages.length) {
								Sprite icon = clanImages[idx];
								icon.drawSprite(drawX, drawY );
								drawX += icon.width + icon.offsetX;
							}
						} else if (effect.startsWith(startIconImage)) {
							int idx = 0;
							for (int index = effect.length() - 1; index >= 5; index--) {
								int num = effect.charAt(index) - 48;
								int pow = 10 * (effect.length() - 1 - index);
								if (pow > 0)
									idx += num * pow;
								else
									idx += num;
							}
							if (idx >= 0 && idx < iconImages.length) {
								Sprite icon = iconImages[idx];
								icon.drawSprite(drawX, drawY + 1);
								drawX += icon.width + icon.offsetX;
							}
						} else if (!setTextEffects(effect)) {
							end = -1;
						}
					}
					if (end > -1) {
						currentCharacter = end;
						continue;
					}
				}
			}

			if (character == '@' && currentCharacter + 4 < string.length() && string.charAt(currentCharacter + 4) == '@') {
				textColor = getColorByName(string.substring(currentCharacter + 1, currentCharacter + 4));
				currentCharacter += 4;
				continue;
			}

			int width = characterWidths[character];
			int height = characterHeights[character];
			if (character != 32) {
				if (transparency == 256) {
					if (textShadowColor != -1) {
						drawCharacter(character, drawX + characterDrawXOffsets[character] + 1, drawY + characterDrawYOffsets[character] + 1, width, height, textShadowColor);
					}
					drawCharacter(character, drawX + characterDrawXOffsets[character], drawY + characterDrawYOffsets[character], width, height, textColor);
				} else {
					if (textShadowColor != -1) {
						drawTransparentCharacter(character, drawX + characterDrawXOffsets[character] + 1, drawY + characterDrawYOffsets[character] + 1, width, height, textShadowColor, transparency, true);
					}
					drawTransparentCharacter(character, drawX + characterDrawXOffsets[character], drawY + characterDrawYOffsets[character], width, height, textColor, transparency, false);
				}
			} else if (anInt4178 > 0) {
				anInt4175 += anInt4178;
				drawX += anInt4175 >> 8;
				anInt4175 &= 0xff;
			}
			int lineWidth = characterScreenWidths[character];
			if (strikethroughColor != -1) {
				rsDrawingArea.drawHorizontalLine(drawX, strikethroughColor, lineWidth, drawY + (int) ((double) baseCharacterHeight * 0.69999999999999996D));
			}
			if (underlineColor != -1) {
				rsDrawingArea.drawHorizontalLine(drawX, underlineColor, lineWidth, drawY + baseCharacterHeight + 3);
			}
			drawX += lineWidth;
		}
	}

	public void drawRAString(String string, int drawX, int drawY, int color, int shadow) {
		if (string != null) {
			setColorAndShadow(color, shadow);
			drawBasicString(string, drawX - getTextWidth(string), drawY);
		}
	}

	public void drawBaseStringMoveXY(String string, int drawX, int drawY, int[] xModifier, int[] yModifier) {
		drawY -= baseCharacterHeight;
		int modifierOffset = 0;
		for (int currentCharacter = 0; currentCharacter < string.length(); currentCharacter++) {
			int character = string.charAt(currentCharacter);
			int width = characterWidths[character];
			int height = characterHeights[character];
			int xOff;
			if (xModifier != null) {
				xOff = xModifier[modifierOffset];
			} else {
				xOff = 0;
			}
			int yOff;
			if (yModifier != null) {
				yOff = yModifier[modifierOffset];
			} else {
				yOff = 0;
			}
			modifierOffset++;

			if (character != 32) {
				if (transparency == 256) {
					if (textShadowColor != -1) {
						drawCharacter(character, (drawX + characterDrawXOffsets[character] + 1 + xOff), (drawY + characterDrawYOffsets[character] + 1 + yOff), width, height, textShadowColor);
					}
					drawCharacter(character, drawX + characterDrawXOffsets[character] + xOff, drawY + characterDrawYOffsets[character] + yOff, width, height, textColor);
				} else {
					if (textShadowColor != -1) {
						drawTransparentCharacter(character, (drawX + characterDrawXOffsets[character] + 1 + xOff), (drawY + characterDrawYOffsets[character] + 1 + yOff), width, height, textShadowColor, transparency, true);
					}
					drawTransparentCharacter(character, drawX + characterDrawXOffsets[character] + xOff, drawY + characterDrawYOffsets[character] + yOff, width, height, textColor, transparency, false);
				}
			} else if (anInt4178 > 0) {
				anInt4175 += anInt4178;
				drawX += anInt4175 >> 8;
				anInt4175 &= 0xff;
			}
			int i_109_ = characterScreenWidths[character];
			if (strikethroughColor != -1) {
				drawHorizontalLine(drawX, drawY + (int) ((double) baseCharacterHeight * 0.69999999999999996D), i_109_, strikethroughColor);
			}
			if (underlineColor != -1) {
				drawHorizontalLine(drawX, (drawY+1) +  baseCharacterHeight, i_109_, underlineColor);
			}
			drawX += i_109_;
		}
	}

	public boolean setTextEffects(String string) {
		if (string.startsWith(startColor)) {
			String color = string.substring(4);
			textColor = color.length() < 6 ? Color.decode(color).getRGB() : Integer.parseInt(color, 16);
			return true;
		} else if (string.equals(endColor)) {
			textColor = defaultColor;
			return true;
		} else if (string.startsWith(startTransparency)) {
			transparency = Integer.parseInt(string.substring(6));
			return true;
		} else if (string.equals(endTransparency)) {
			transparency = defaultTransparency;
			return true;
		} else if (string.startsWith(startStrikethrough)) {
			strikethroughColor = Integer.parseInt(string.substring(4));
			return true;
		} else if (string.equals(defaultStrikethrough)) {
			strikethroughColor = 8388608;
			return true;
		} else if (string.equals(endStrikethrough)) {
			strikethroughColor = -1;
			return true;
		} else if (string.startsWith(startUnderline)) {
			underlineColor = Integer.parseInt(string.substring(2));
			return true;
		} else if (string.equals(startDefaultUnderline)) {
			underlineColor = 0;
			return true;
		} else if (string.equals(endUnderline)) {
			underlineColor = -1;
			return true;
		} else if (string.startsWith(startShadow)) {
			textShadowColor = Integer.parseInt(string.substring(5));
			return true;
		} else if (string.equals(startDefaultShadow)) {
			textShadowColor = 0;
			return true;
		} else if (string.equals(endShadow)) {
			textShadowColor = defaultShadow;
			return true;
		} else if (string.equals(lineBreak)) {
			setDefaultTextEffectValues(defaultColor, defaultShadow, defaultTransparency);
			return true;
		}
		return false;
	}

	public void setColorAndShadow(int color, int shadow) {
		strikethroughColor = -1;
		underlineColor = -1;
		textShadowColor = defaultShadow = shadow;
		textColor = defaultColor = color;
		transparency = defaultTransparency = 256;
		anInt4178 = 0;
		anInt4175 = 0;
	}

	public int getRealTextWidth(String string) {
		if (string == null) {
			return 0;
		}
		return string.length();
	}

	public int getTextWidth(String string) {
		if (string == null) {
			return 0;
		}
		int finalWidth = 0;
		for (int currentCharacter = 0; currentCharacter < string.length(); currentCharacter++) {
			int character = string.charAt(currentCharacter);
			if (character > 255) {
				character = 32;
			}

			if (character == '<') {
				int end = string.indexOf(">", currentCharacter);
				if (end != currentCharacter + 1) {
					if (end > -1 && end != currentCharacter + 1 && end < string.length()) {
						String effect = string.substring(currentCharacter + 1, end);

						if (effect.startsWith(startImage) || effect.startsWith(startClanImage) || effect.startsWith(startIconImage)) {
							finalWidth += 11;
						}

						currentCharacter = end;
						continue;
					}
				}
			}

			if (character == '@' && currentCharacter + 4 < string.length() && string.charAt(currentCharacter + 4) == '@') {
				textColor = getColorByName(string.substring(currentCharacter + 1, currentCharacter + 4));
				currentCharacter += 4;
				continue;
			}

			finalWidth += characterScreenWidths[character];
		}
		return finalWidth;
	}

	public void drawBasicString(String string, int drawX, int drawY, int color, int shadow) {
		if (string != null) {
			setColorAndShadow(color, shadow);
			drawBasicString(string, drawX, drawY);
		}
	}

	public void drawString(String string, int drawX, int drawY, int color, int shadow, boolean centered) {
		if (centered) {
			drawCenteredString(string, drawX, drawY, color, shadow);
		} else {
			drawBasicString(string, drawX, drawY, color, shadow);
		}
	}

	public void drawCenteredString(String string, int drawX, int drawY, int color, int shadow) {
		if (string != null) {
			setColorAndShadow(color, shadow);
			drawBasicString(string, drawX - getTextWidth(string) / 2, drawY);
		}
	}

	public void drawCenteredString(String string, int drawX, int drawY, int color, int shadow, int trans) {
		if (string != null) {
			setTrans(shadow, color, trans);
			drawBasicString(string, drawX - getTextWidth(string) / 2, drawY);
		}
	}

	public static void createTransparentCharacterPixels(int[] is, byte[] is_0_, int i, int i_1_, int i_2_, int i_3_, int i_4_, int i_5_, int i_6_, int i_7_) {
		i = ((i & 0xff00ff) * i_7_ & ~0xff00ff) + ((i & 0xff00) * i_7_ & 0xff0000) >> 8;
		i_7_ = 256 - i_7_;
		for (int i_8_ = -i_4_; i_8_ < 0; i_8_++) {
			for (int i_9_ = -i_3_; i_9_ < 0; i_9_++) {
				if (is_0_[i_1_++] != 0) {
					int i_10_ = is[i_2_];
					drawAlpha(is, i_2_++, ((((i_10_ & 0xff00ff) * i_7_ & ~0xff00ff)
							+ ((i_10_ & 0xff00) * i_7_ & 0xff0000)) >> 8) + i, 255);
				} else {
					i_2_++;
				}
			}
			i_2_ += i_5_;
			i_1_ += i_6_;
		}
	}

	public void drawTransparentCharacter(int i, int i_11_, int i_12_, int i_13_, int i_14_, int i_15_, int i_16_, boolean bool) {
		int i_17_ = i_11_ + i_12_ * Rasterizer2D.width;
		int i_18_ = Rasterizer2D.width - i_13_;
		int i_19_ = 0;
		int i_20_ = 0;
		if (i_12_ < Rasterizer2D.topY) {
			int i_21_ = Rasterizer2D.topY - i_12_;
			i_14_ -= i_21_;
			i_12_ = Rasterizer2D.topY;
			i_20_ += i_21_ * i_13_;
			i_17_ += i_21_ * Rasterizer2D.width;
		}
		if (i_12_ + i_14_ > Rasterizer2D.bottomY) {
			i_14_ -= i_12_ + i_14_ - Rasterizer2D.bottomY;
		}
		if (i_11_ < Rasterizer2D.leftX) {
			int i_22_ = Rasterizer2D.leftX - i_11_;
			i_13_ -= i_22_;
			i_11_ = Rasterizer2D.leftX;
			i_20_ += i_22_;
			i_17_ += i_22_;
			i_19_ += i_22_;
			i_18_ += i_22_;
		}
		if (i_11_ + i_13_ > Rasterizer2D.bottomX) {
			int i_23_ = i_11_ + i_13_ - Rasterizer2D.bottomX;
			i_13_ -= i_23_;
			i_19_ += i_23_;
			i_18_ += i_23_;
		}
		if (i_13_ > 0 && i_14_ > 0) {
			createTransparentCharacterPixels(Rasterizer2D.pixels, fontPixels[i], i_15_, i_20_, i_17_, i_13_, i_14_, i_18_, i_19_, i_16_);
		}
	}

	public static void createCharacterPixels(int[] is, byte[] is_24_, int i, int i_25_,
											 int i_26_, int i_27_, int i_28_, int i_29_,
											 int i_30_) {
		int i_31_ = -(i_27_ >> 2);
		i_27_ = -(i_27_ & 0x3);
		for (int i_32_ = -i_28_; i_32_ < 0; i_32_++) {
			for (int i_33_ = i_31_; i_33_ < 0; i_33_++) {
				if (is_24_[i_25_++] != 0) {
					drawAlpha(is, i_26_++, i, 255);
				} else {
					i_26_++;
				}
				if (is_24_[i_25_++] != 0) {
					drawAlpha(is, i_26_++, i, 255);
				} else {
					i_26_++;
				}
				if (is_24_[i_25_++] != 0) {
					drawAlpha(is, i_26_++, i, 255);
				} else {
					i_26_++;
				}
				if (is_24_[i_25_++] != 0) {
					drawAlpha(is, i_26_++, i, 255);
				} else {
					i_26_++;
				}
			}
			for (int i_34_ = i_27_; i_34_ < 0; i_34_++) {
				if (is_24_[i_25_++] != 0) {
					drawAlpha(is, i_26_++, i, 255);
				} else {
					i_26_++;
				}
			}
			i_26_ += i_29_;
			i_25_ += i_30_;
		}
	}

	public void drawCharacter(int character, int x, int y, int width, int height, int shadowColor) {
		int pixel = x + y * Rasterizer2D.width;
		int i_41_ = Rasterizer2D.width - width;
		int i_42_ = 0;
		int i_43_ = 0;
		if (y < Rasterizer2D.topY) {
			int i_44_ = Rasterizer2D.topY - y;
			height -= i_44_;
			y = Rasterizer2D.topY;
			i_43_ += i_44_ * width;
			pixel += i_44_ * Rasterizer2D.width;
		}
		if (y + height > Rasterizer2D.bottomY) {
			height -= y + height - Rasterizer2D.bottomY;
		}
		if (x < Rasterizer2D.leftX) {
			int i_45_ = Rasterizer2D.leftX - x;
			width -= i_45_;
			x = Rasterizer2D.leftX;
			i_43_ += i_45_;
			pixel += i_45_;
			i_42_ += i_45_;
			i_41_ += i_45_;
		}
		if (x + width > Rasterizer2D.bottomX) {
			int i_46_ = x + width - Rasterizer2D.bottomX;
			width -= i_46_;
			i_42_ += i_46_;
			i_41_ += i_46_;
		}
		if (width > 0 && height > 0) {
			createCharacterPixels(Rasterizer2D.pixels, fontPixels[character], shadowColor, i_43_, pixel, width, height, i_41_, i_42_);
		}
	}

	static {
		startTransparency = "trans=";
		startStrikethrough = "str=";
		startDefaultShadow = "shad";
		startColor = "col=";
		lineBreak = "br";
		defaultStrikethrough = "str";
		endUnderline = "/u";
		startImage = "img=";
		startClanImage = "clan=";
		startIconImage = "icon=";
		startShadow = "shad=";
		startUnderline = "u=";
		endColor = "/col";
		startDefaultUnderline = "u";
		endTransparency = "/trans";
		aRSString_4143 = Integer.toString(100);
		aRSString_4135 = "nbsp";
		aRSString_4169 = "reg";
		aRSString_4165 = "times";
		aRSString_4162 = "shy";
		aRSString_4163 = "copy";
		endEffect = "gt";
		aRSString_4147 = "euro";
		startEffect = "lt";
		defaultTransparency = 256;
		defaultShadow = -1;
		anInt4175 = 0;
		textShadowColor = -1;
		textColor = 0;
		defaultColor = 0;
		strikethroughColor = -1;
		splitTextStrings = new String[100];
		underlineColor = -1;
		anInt4178 = 0;
		transparency = 256;
	}
}